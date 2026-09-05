package com.jojo.port

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import java.util.ArrayDeque
import kotlin.random.Random

/** Recovered BattleLayer._testUnitType's exact 0..6 selector table. */
internal fun sourceUnitTypeMatches(camp: Int, selector: Int): Boolean = when (selector) {
    0, 1, 2, 3 -> camp == selector
    4 -> camp <= 1
    5 -> camp >= 2
    6 -> true
    else -> false
}

/**
 * Stateful interpreter for the recovered Python source AST. The supported
 * subset matches the source corpus' common scenario constructs and stops on
 * stage.say/stage.choice so the LibGDX UI can resume it after player input.
 */
class PythonAstRuntime private constructor(
    private val moduleName: String,
    private val functions: Map<String, RuntimeFunction>,
    private val campaign: CampaignState,
) {
    private var externalBattlePresentation = false
    private var externalFightPresentation = false
    private var stagePresentationSkipped = false
    /** Source BattleLayer.loadBg pauses until its resource callback tail. */
    private var pendingBattleBackgroundLoadIndex: Int? = null

    fun enableExternalBattlePresentation() {
        externalBattlePresentation = true
        stage.enableBattleMovementTimeline()
    }

    /** Opt-in only once the owning screen consumes ScenarioFightCommand. */
    fun enableExternalFightPresentation() {
        externalFightPresentation = true
    }
    fun setStagePresentationSkipped(skipped: Boolean) { stagePresentationSkipped = skipped }
    /** True from `loadBg`'s pause until BattleLayer reports its map/avatars ready. */
    val hasPendingBattleBackgroundLoad: Boolean get() = pendingBattleBackgroundLoadIndex != null
    /** Requested HM index, including BattleLayer.loadBg's JUMP_OFFSET adjustment. */
    val requestedBattleBackgroundMapIndex: Int get() = pendingBattleBackgroundLoadIndex ?: stage.battleMapIndex
    enum class ModalKind { EVENT, INFO, SECTION, MAP_INFO, AMBITION }
    data class ChoiceTrace(val module: String, val function: String, val line: Int, val option: Int, val optionCount: Int)
    data class RandomTrace(val module: String, val function: String, val line: Int, val value: Int)
    /** Live tactical data exposed to the original S_XX script API while a battle is running. */
    data class BattleScriptContext(
        val round: Int,
        val camp: Int,
        val maxRound: Int = 99,
        val playerDefeated: Boolean = false,
        val enemyDefeated: Boolean = false,
        val clickedCharacterId: Int? = null,
        val positions: Map<Int, Pair<Int, Int>> = emptyMap(),
        /** Includes hidden BattleUnits so a later show/move starts at its real tile. */
        val stagePositions: Map<Int, Pair<Int, Int>> = positions,
        val positionsByCamp: Map<Int, List<Pair<Int, Int>>> = emptyMap(),
        /** Live BattleUnit.type() for each visible source character. */
        val campByCharacterId: Map<Int, Int> = emptyMap(),
        /** BattleUnit.hitareaIdx() offsets used by stage.isNear(..., false). */
        val attackOffsets: Map<Int, Set<Pair<Int, Int>>> = emptyMap(),
        /** Config.HITAREA.BU_BING (1), selected by stage.isNear(..., true). */
        val infantryNearOffsets: Set<Pair<Int, Int>> = DEFAULT_INFANTRY_NEAR_OFFSETS,
        val activeCharacterIds: Set<Int> = emptySet(),
        val attributes: Map<Int, Map<Int, Int>> = emptyMap(),
        /** Model.rFlag/eFlag bits needed by authored battle APIs. */
        val enabledFeatures: Int = 0,
    )
    private data class RuntimeFunction(
        val name: String,
        val statements: List<JsonValue>,
        val labels: Map<String, Int>,
        /** Nested label continuations used by recovered menu state machines. */
        val labelEntrypoints: Map<String, List<JsonValue>> = emptyMap(),
    )
    private data class Frame(
        val function: RuntimeFunction,
        var index: Int = 0,
        val locals: MutableMap<String, Any?> = mutableMapOf(),
        /** Original source function survives synthetic if/for execution frames. */
        val sourceFunction: String = function.name,
    )
    data class UnitReference(val id: Int)
    private data class HeadReference(val id: Int)
    data class FightReference(val id: Long)

    val stage = ScenarioStage(campaign)
    var state: PlaybackState = PlaybackState.COMPLETE
        private set
    var currentDialogue: Dialogue? = null
        private set
    /** Increments for every source speaker token, even for identical text. */
    var dialogueRevision: Long = 0
        private set
    /** Increments once per authored stage.say/SayLayer lifecycle. */
    var dialogueLifecycleRevision: Long = 0
        private set
    /** Complete stage.say payload shared by all speaker pages in one SayLayer. */
    var currentDialogueSourceText: String? = null
        private set
    var currentDialogueSide: Int = 0
        private set
    /** Original DialogueLayer moves the complete panel to the top when the
     * speaking HallUnit is above its authored local-y threshold (-50). */
    var currentDialogueAtTop: Boolean = false
        private set
    private var lastDialogueSpeakerId: Int = -1
    private var dialogueSpeakerIndex: Int = 0
    var currentChoice: Choice? = null
        private set
    var currentModalText: String? = null
        private set
    var currentModalKind: ModalKind? = null
        private set
    /** Text already visible when MapInfoLayer receives another setData call. */
    var currentModalFixedText: String = ""
        private set
    var ambitionFrom: Int = 0
        private set
    var ambitionTo: Int = 0
        private set
    var ambitionElapsedSeconds: Float = 0f
        private set
    var ambitionIndicatorEnabled: Boolean = true
        private set
    private var modalNextText: String? = null
    private val modalQueuedTexts = ArrayDeque<String>()
    /** MapInfoLayer is pooled by UILayer, so its content survives each close. */
    private var mapInfoContent = ""
    var selectedChoice: Int = 0
        private set
    var isAskChoice: Boolean = false
        private set
    var chosenOption: String? = null
        private set
    private var currentChoiceFunction: String? = null
    private var currentChoiceLine: Int? = null
    val choiceTrace = mutableListOf<ChoiceTrace>()
    val randomTrace = mutableListOf<RandomTrace>()
    val globalVariables = mutableMapOf<String, Any?>()
    private val vars = mutableMapOf<Int, Any?>()
    /** Python gvars map directly to original Model global variables across scenes. */
    private val gvars = campaign.globalVariables
    private val pvars = mutableMapOf<Int, Any?>()
    private val frames = ArrayDeque<Frame>()
    private val pendingDialogues = ArrayDeque<Dialogue>()
    /**
     * SayLayer removes its node and invokes Stage.resume from the close
     * callback.  The resumed Python generator is observed on the following
     * Cocos scheduler frame; running it inside TOUCH_END collapses the
     * authored close edge with the next dialogue/camp transition.
     *
     * Most unit tests intentionally use the synchronous interpreter helper,
     * so this barrier is opted into by the production BattleLayer call site.
     */
    private var dialogueCallbackFramePending = false
    private var dialogueCallbackReturnState: PlaybackState? = null
    /** Native BattleLayer say4 temporarily borrows the ordinary SayLayer. */
    private var externalDialogueReturnState: PlaybackState? = null
    // Tool.random's initial seed comes from JavaScript Math.random. Production
    // starts from an equivalent variable seed; branch tests inject exact draws.
    private var toolRandomSeed = Random.Default.nextDouble() * 1_000.0
    private val injectedRandomValues = ArrayDeque<Int>()
    var randomDrawCount: Int = 0
        private set
    val remainingInjectedRandomCount: Int get() = injectedRandomValues.size
    private var pendingChoiceTarget: JsonValue? = null
    private var pendingAskStatement: JsonValue? = null
    private var pendingAskFrame: Frame? = null
    private var pendingAskResult: Int? = null
    /** Remaining source seconds for StageLayer.delay()/attackAction(). */
    private var delayRemainingSeconds = 0f
    private var modalRemainingSeconds = 0f
    private var modalPostTypingDelaySeconds = 1f
    private var ended = false
    private var stopAfterRandomTraceCount: Int? = null
    private var executedStatements = 0

    val functionNames: Set<String> get() = functions.keys
    val unhandledCalls = linkedMapOf<String, Int>()
    private var battleContext = BattleScriptContext(round = 1, camp = 1)

    fun setBattleContext(context: BattleScriptContext) {
        battleContext = context
        // StageLayer.unit(id) addresses the already-materialized BattleUnit.
        // A proxy created lazily by the AST must therefore begin at the live
        // tactical position, not ScenarioStage.unit's harmless hall-screen
        // fallback coordinates. Otherwise the first scripted move visibly
        // jumps from that fallback before reaching its authored destination.
        context.stagePositions.forEach { (id, position) ->
            stage.seedBattleUnitPosition(id, position.first, position.second)
        }
    }

    /**
     * HallLayer's battle command re-enters the next R-script scene with
     * `battleTest()` true.  Keep the rest of the live context intact: the
     * button is a source input, not a replacement synthetic battle state.
     */
    fun selectHallBattleCommand() {
        battleContext = battleContext.copy(clickedCharacterId = HALL_BATTLE_COMMAND_ID)
    }

    /** Supplies the next recovered Model.random() values, each inclusive 0..100. */
    fun setRandomSequence(values: Iterable<Int>) {
        injectedRandomValues.clear()
        values.forEach { value ->
            require(value in 0..100) { "Model.random() value must be in 0..100: $value" }
            injectedRandomValues.addLast(value)
        }
        randomDrawCount = 0
    }

    /** Test-only safe point for source loops: stop after this many real random calls are traced. */
    fun stopAfterNextRandomTrace() {
        stopAfterRandomTrace(1)
    }

    fun stopAfterRandomTrace(count: Int) {
        require(count > 0) { "random trace count must be positive" }
        stopAfterRandomTraceCount = count
    }

    /** Supplies recovered script-local vars[] before an entry function starts. */
    fun setScriptVariables(values: Map<Int, Int>) {
        values.forEach { (id, value) -> vars[id] = value }
    }

    /** Exact BattleLayer.unit(t, flags) routing, exposed internally for contract tests. */
    internal fun resolveStageUnitReference(value: Int, flags: Int = 0): UnitReference? {
        val indexed = flags and 1 != 0 || gvars[4031].asInt() == 1
        return if (indexed) stage.battleUnitForSourceSlot(value)?.let { UnitReference(it.characterId) }
        else UnitReference(value)
    }

    fun start(functionName: String, label: String? = null) {
        frames.clear()
        pendingDialogues.clear()
        dialogueCallbackFramePending = false
        dialogueCallbackReturnState = null
        externalDialogueReturnState = null
        currentDialogue = null
        currentDialogueSourceText = null
        ended = false
        executedStatements = 0
        delayRemainingSeconds = 0f
        modalRemainingSeconds = 0f
        currentModalText = null
        currentModalKind = null
        currentModalFixedText = ""
        ambitionFrom = 0
        ambitionTo = 0
        ambitionElapsedSeconds = 0f
        ambitionIndicatorEnabled = true
        modalNextText = null
        modalQueuedTexts.clear()
        choiceTrace.clear()
        randomTrace.clear()
        currentChoiceFunction = null
        currentChoiceLine = null
        isAskChoice = false
        pendingAskStatement = null
        pendingAskFrame = null
        pendingAskResult = null
        pendingBattleBackgroundLoadIndex = null
        pushFunction(functionName, label)
        runUntilInput()
    }

    fun advanceDialogue(deferCloseCallbackFrame: Boolean = false) {
        check(state == PlaybackState.DIALOGUE) { "대기 중인 대사가 없습니다." }
        if (pendingDialogues.isNotEmpty()) {
            presentDialogue(pendingDialogues.removeFirst())
            return
        }
        currentDialogue = null
        currentDialogueSourceText = null
        if (deferCloseCallbackFrame) {
            dialogueCallbackReturnState = externalDialogueReturnState
            // An ordinary stage.say has no external return state.  The
            // synthetic one-frame DELAY is only the SayLayer removal/callback
            // observation; after it, execution must resume immediately.
            // Keeping a stale Float.MAX_VALUE from an earlier external
            // action stranded later victory dialogue callbacks forever
            // (first exposed by a complete S_01 production battle).
            if (externalDialogueReturnState == null) delayRemainingSeconds = 0f
            externalDialogueReturnState = null
            dialogueCallbackFramePending = true
            state = PlaybackState.DELAY
            return
        }
        externalDialogueReturnState?.let {
            externalDialogueReturnState = null
            state = it
            return
        }
        runUntilInput()
    }

    /** Presents BattleLayer.say4 through the same dialogue renderer/input path as stage.say. */
    fun presentExternalBattleDialogue(dialogue: Dialogue) {
        check(currentDialogue == null && state != PlaybackState.DIALOGUE) { "이미 대사가 표시 중입니다." }
        check(externalDialogueReturnState == null) { "외부 전투 대사가 이미 대기 중입니다." }
        externalDialogueReturnState = state
        beginDialogueLifecycle(dialogue.speakerId?.let { "&$it\n${dialogue.text}" } ?: dialogue.text)
        presentDialogue(dialogue)
        state = PlaybackState.DIALOGUE
    }

    /** BattleLayer.getItem opens InfoLayer before its final Script.resume callback. */
    fun presentExternalBattleInfo(text: String, postTypingDelaySeconds: Float = 1f) {
        check(state == PlaybackState.DELAY) { "외부 전투 안내는 애니메이션 대기에서만 열 수 있습니다." }
        suspendForInfo(text, ModalKind.INFO, postTypingDelaySeconds)
    }

    /** Deterministic visual oracle matching `--capture-python-hall-fixture`. */
    fun installHallFixture() {
        frames.clear()
        pendingDialogues.clear()
        stage.heads.clear()
        stage.clearUnits()
        stage.apply(ScenarioCommand.LoadBackground(2, 30))
        stage.apply(ScenarioCommand.ShowUnit(0, 45, 48, 0))
        stage.apply(ScenarioCommand.ShowUnit(157, 55, 52, 2))
        stage.apply(ScenarioCommand.ShowUnit(181, 51, 45, 3))
        stage.showHead(0, 180, 210)
        stage.showHead(157, 460, 220)
        stage.finishAnimations()
        currentChoice = null
        currentModalText = null
        currentModalKind = null
        dialogueSpeakerIndex = -1
        lastDialogueSpeakerId = -1
        presentDialogue(Dialogue("0", "원본 궁정 대화 UI 비교"))
        state = PlaybackState.DIALOGUE
    }

    /** Deterministic palace/interior variant used by the source Hall fixture. */
    fun installPalaceFixture() {
        frames.clear()
        pendingDialogues.clear()
        stage.heads.clear()
        stage.clearUnits()
        stage.apply(ScenarioCommand.LoadBackground(2, 9))
        listOf(
            ScenarioCommand.ShowUnit(181, 52, 41, 2),
            ScenarioCommand.ShowUnit(157, 64, 41, 2),
            ScenarioCommand.ShowUnit(0, 58, 70, 0),
        ).forEach(stage::apply)
        stage.finishAnimations()
        currentChoice = null
        currentModalText = null
        currentModalKind = null
        dialogueSpeakerIndex = -1
        lastDialogueSpeakerId = -1
        presentDialogue(Dialogue("0", "원본 궁정 장면 UI 비교"))
        state = PlaybackState.DIALOGUE
    }

    /** Deterministic SectionLayer frame created through Hall.sectionName. */
    fun installSectionFixture() {
        frames.clear()
        pendingDialogues.clear()
        stage.heads.clear()
        stage.clearUnits()
        // SectionLayer is created over the stable street Hall frame.
        stage.apply(ScenarioCommand.LoadBackground(2, 71))
        currentDialogue = null
        currentChoice = null
        currentModalText = "제일장막"
        currentModalKind = ModalKind.SECTION
        currentModalFixedText = ""
        modalNextText = "황건"
        modalRemainingSeconds = 3f
        state = PlaybackState.MODAL
    }

    /** Deterministic Hall overlay fixtures paired with the live Cocos layers. */
    fun installOverlayFixture(kind: String) {
        frames.clear()
        pendingDialogues.clear()
        stage.heads.clear()
        stage.clearUnits()
        stage.apply(ScenarioCommand.LoadBackground(2, 30))
        listOf(
            ScenarioCommand.ShowUnit(0, 45, 48, 0),
            ScenarioCommand.ShowUnit(157, 55, 52, 2),
            ScenarioCommand.ShowUnit(181, 51, 45, 3),
        ).forEach(stage::apply)
        stage.finishAnimations()
        currentDialogue = null
        currentChoice = null
        currentModalText = null
        currentModalKind = null
        currentModalFixedText = ""
        modalNextText = null
        modalQueuedTexts.clear()
        when (kind) {
            "info" -> {
                currentModalText = "재능의 첫 징후"
                currentModalKind = ModalKind.INFO
                modalRemainingSeconds = 5f
                state = PlaybackState.MODAL
            }
            "get-item-equipment" -> {
                // HallLayer.getItem(3, 2) stores the item, then displays the
                // value returned by ItemStore.pushItem (the new equipment's
                // source level is zero), rather than echoing argument two.
                stage.getItem(3, 2)
                currentModalText = "얻었다 단창 Lv0"
                currentModalKind = ModalKind.INFO
                modalRemainingSeconds = 5f
                state = PlaybackState.MODAL
            }
            "get-item-property" -> {
                currentModalText = stage.getItem(150, 2)
                currentModalKind = ModalKind.INFO
                modalRemainingSeconds = 5f
                state = PlaybackState.MODAL
            }
            "choice" -> {
                currentChoice = Choice(listOf("바로 이게 제가 바라는 거예요", "이건 너무 이른 것 같아"), 0)
                selectedChoice = 0
                state = PlaybackState.CHOICE
            }
            "ask" -> {
                currentChoice = Choice(listOf("예", "비"), null)
                selectedChoice = 0
                isAskChoice = true
                state = PlaybackState.CHOICE
            }
            "command", "menu", "save", "save-confirm", "item-equipment", "item-property", "item-discard-confirm", "equip", "unit-list", "unit-list-select", "unit-list-close", "equip-confirm", "equip-confirm-unload", "exclusive", "exclusive-tab1", "magic", "feats", "feats-help", "buy", "sell", "forces", "property", "terrain", "treasure", "helper", "skip-open" -> {
                stage.apply(ScenarioCommand.SetEventName(""))
                stage.setStageName("")
                stage.setMenuVisible(true)
                state = PlaybackState.COMPLETE
            }
            "map-info" -> {
                currentModalText = "조조가 수저우 도겸과 전투를 벌였을 때,"
                currentModalKind = ModalKind.MAP_INFO
                modalRemainingSeconds = 5f
                state = PlaybackState.MODAL
            }
            "ambition" -> {
                stage.apply(ScenarioCommand.SetEventName("조조가 군대를 일으키다"))
                stage.setStageName("사수관 조조군 주진영")
                suspendForAmbition(5)
                // The source fixture freezes the completed target width.
                ambitionElapsedSeconds = 2.2f
                ambitionIndicatorEnabled = false
                modalRemainingSeconds = 60f
            }
            else -> error("Unknown Hall overlay fixture: $kind")
        }
    }

    private fun presentDialogue(dialogue: Dialogue) {
        currentDialogueAtTop = false
        dialogue.speakerId?.toIntOrNull()?.let { speakerId ->
            // DialogueLayer updates its static alternating background only
            // when the speaking HallUnit exists; unit 0 always resets left.
            if (speakerId in stage.units) {
                if (speakerId != lastDialogueSpeakerId) dialogueSpeakerIndex++
                if (speakerId == 0) dialogueSpeakerIndex = 0
                lastDialogueSpeakerId = speakerId
                val unit = stage.units.getValue(speakerId)
                // HallLayer.setUnitPos: node.y = 424 - 4 * (x + y).
                currentDialogueAtTop = 424f - 4f * (unit.visualX + unit.visualY) < -50f
            }
        }
        currentDialogueSide = Math.floorMod(dialogueSpeakerIndex, 2)
        currentDialogue = dialogue
        dialogueRevision++
    }

    private fun beginDialogueLifecycle(sourceText: String) {
        currentDialogueSourceText = sourceText
        dialogueLifecycleRevision++
    }

    fun selectPrevious() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice - 1, it.size) }
    }

    fun selectNext() {
        currentChoice?.options?.let { selectedChoice = Math.floorMod(selectedChoice + 1, it.size) }
    }

    /** ChooseLayer item click: select the zero-based visible row before confirming it. */
    fun selectChoice(index: Int) {
        val choice = currentChoice ?: return
        selectedChoice = index.coerceIn(0, choice.options.lastIndex)
    }

    fun confirmChoice() {
        check(state == PlaybackState.CHOICE) { "대기 중인 선택지가 없습니다." }
        val choice = requireNotNull(currentChoice)
        choiceTrace += ChoiceTrace(
            module = moduleName,
            function = requireNotNull(currentChoiceFunction) { "choice source function is missing" },
            line = requireNotNull(currentChoiceLine) { "choice source line is missing" },
            option = selectedChoice,
            optionCount = choice.options.size,
        )
        chosenOption = choice.options[selectedChoice]
        pendingAskStatement?.let { statement ->
            val sourceFrame = requireNotNull(pendingAskFrame)
            // MsgBox2 button tags are 0=예 and 1=비. StageLayer.ask maps
            // those callbacks to Python values 1 and 0 respectively.
            pendingAskResult = if (selectedChoice == 0) 1 else 0
            val selected = if (evalBoolean(statement.field("test"), sourceFrame)) {
                statement.field("body")
            } else {
                statement.field("orelse")
            }
            pendingAskResult = null
            pendingAskStatement = null
            pendingAskFrame = null
            isAskChoice = false
            currentChoice = null
            val statements = selected.children().toList()
            if (statements.isNotEmpty()) frames.addLast(
                Frame(RuntimeFunction("<if>", statements, emptyMap()), 0, sourceFrame.locals, sourceFrame.sourceFunction),
            )
            runUntilInput()
            return
        }
        pendingChoiceTarget?.let { assign(it, selectedChoice + 1) } // Original scripts use one-based choice indices.
        pendingChoiceTarget = null
        currentChoice = null
        runUntilInput()
    }

    /** Advances a source StageLayer suspension from the render loop. */
    fun update(delta: Float, autoCloseUi: Boolean = true) {
        stage.updateAnimations(delta)
        if (state == PlaybackState.MODAL && currentModalKind == ModalKind.AMBITION) {
            ambitionElapsedSeconds += delta.coerceAtLeast(0f)
        }
        when (state) {
            PlaybackState.DELAY -> {
                if (hasPendingBattleBackgroundLoad) return
                if (dialogueCallbackFramePending) {
                    // Publish one complete render with the SayLayer absent
                    // before its callback resumes the script/controller.
                    dialogueCallbackFramePending = false
                    return
                }
                dialogueCallbackReturnState?.let {
                    dialogueCallbackReturnState = null
                    state = it
                    return
                }
                delayRemainingSeconds -= delta.coerceAtLeast(0f)
                if (delayRemainingSeconds <= 0f) {
                    delayRemainingSeconds = 0f
                    runUntilInput()
                }
            }
            PlaybackState.MODAL -> if (modalRemainingSeconds > 0f && modalMayAutoClose(currentModalKind, currentModalText, autoCloseUi)) {
                modalRemainingSeconds -= delta.coerceAtLeast(0f)
                if (modalRemainingSeconds <= 0f) resumeModal()
            }
            else -> Unit
        }
    }

    /** InfoLayer auto-closes short messages even when GAME_SETTING lacks AUTO_CLOSE. */
    /** Deterministic verifier helper: complete a source delay without wall-clock waiting. */
    fun skipDelay() {
        if (state != PlaybackState.DELAY) return
        // loadBg has no timer fallback in the source: an image-load error
        // leaves Script paused instead of executing resource-dependent code.
        if (hasPendingBattleBackgroundLoad) return
        stage.finishAnimations()
        delayRemainingSeconds = 0f
        runUntilInput()
    }

    /** Resumes an animation callback owned by the production presentation layer. */
    fun resumeExternalDelay() {
        check(state == PlaybackState.DELAY) { "재개할 외부 애니메이션 대기가 없습니다." }
        check(!hasPendingBattleBackgroundLoad) {
            "loadBg는 BattleLayer의 맵/아바타 완료 콜백으로만 재개해야 합니다."
        }
        delayRemainingSeconds = 0f
        runUntilInput()
    }

    /**
     * `_loadBg` writes BG_INDEX before calling its supplied callback.  Keep
     * that write and the AST resume together so later status/draw mutations
     * cannot overtake the map/image completion barrier.
     */
    fun completeBattleBackgroundLoad() {
        val mapIndex = requireNotNull(pendingBattleBackgroundLoadIndex) {
            "완료할 loadBg 콜백이 없습니다."
        }
        check(state == PlaybackState.DELAY) { "loadBg 완료 콜백은 Script pause 중에만 가능합니다." }
        stage.selectBattleMap(mapIndex)
        pendingBattleBackgroundLoadIndex = null
        delayRemainingSeconds = 0f
        runUntilInput()
    }

    /** BattleLayer.showWinCondition: `pause(); addLayer(... { fn: resume })`. */
    fun resumeModal() {
        check(state == PlaybackState.MODAL) { "재개할 모달 대기가 없습니다." }
        modalNextText?.let { next ->
            currentModalText = next
            modalNextText = null
            modalRemainingSeconds = 3f
            return
        }
        if (modalQueuedTexts.isNotEmpty()) {
            currentModalText = modalQueuedTexts.removeFirst()
            modalRemainingSeconds = (currentModalText.orEmpty().length * 0.04f + modalPostTypingDelaySeconds + .35f)
                .coerceAtLeast(modalPostTypingDelaySeconds + .65f)
            return
        }
        currentModalText = null
        currentModalKind = null
        currentModalFixedText = ""
        modalRemainingSeconds = 0f
        state = PlaybackState.COMPLETE
        runUntilInput()
    }

    /** Production entry for BattleLayer.showWinCondition's pause + layer request pair. */
    fun suspendForWinCondition(text: String) {
        stage.showWinCondition(text)
        state = PlaybackState.MODAL
    }

    /** A first panel click finishes typing; it must not also close the layer. */
    fun completeModalTyping() {
        if (state == PlaybackState.MODAL) modalRemainingSeconds = modalPostTypingDelaySeconds
    }

    private fun suspendForInfo(
        text: String,
        kind: ModalKind = ModalKind.EVENT,
        postTypingDelaySeconds: Float = 1f,
    ) {
        val pages = if (kind == ModalKind.INFO) splitInfoPages(text) else listOf(text)
        currentModalText = pages.firstOrNull().orEmpty()
        pages.drop(1).forEach(modalQueuedTexts::addLast)
        currentModalKind = kind
        currentModalFixedText = ""
        modalPostTypingDelaySeconds = postTypingDelaySeconds
        modalRemainingSeconds = (currentModalText.orEmpty().length * 0.04f + postTypingDelaySeconds + .35f)
            .coerceAtLeast(postTypingDelaySeconds + .65f)
        state = PlaybackState.MODAL
    }

    /** Exact MapInfoLayer.setData accumulation and auto-close contract. */
    private fun suspendForMapInfo(text: String, changePage: Boolean, wepon: Boolean, wait: Boolean) {
        if (changePage) mapInfoContent = ""
        val separator = if (!changePage && wepon && mapInfoContent.isNotEmpty()) "\n" else ""
        currentModalFixedText = mapInfoContent
        val appended = separator + text
        currentModalText = appended
        currentModalKind = ModalKind.MAP_INFO
        // Source types one rich-text token every .04 s, then waits 1 s (5 s
        // when `wait` is set) before AUTO_CLOSE advances the script.
        modalPostTypingDelaySeconds = if (wait) 5f else 1f
        modalRemainingSeconds = appended.length * 0.04f + modalPostTypingDelaySeconds
        mapInfoContent += appended
        stage.setBottomText(mapInfoContent)
        state = PlaybackState.MODAL
    }

    private fun splitInfoPages(text: String): List<String> {
        val pages = mutableListOf<String>()
        var page = ""
        text.split('\n').forEach { line ->
            page = if (page.isEmpty()) line else "$page\n$line"
            if (page.length > 100) {
                pages += page
                page = ""
            }
        }
        if (page.isNotEmpty()) pages += page
        return pages.ifEmpty { listOf("") }
    }

    private fun suspendForSection(index: Int, name: String) {
        val digits = listOf("십", "일", "2", "삼", "넷", "다섯", "육", "칠", "팔", "구")
        var value = index
        var chapter = if (value > 0) "장막" else "서막"
        while (value > 0) {
            chapter = digits[value % 10] + chapter
            value /= 10
        }
        if (index > 0) chapter = "제$chapter"
        currentModalText = chapter
        currentModalKind = ModalKind.SECTION
        modalNextText = name
        modalRemainingSeconds = 3f
        state = PlaybackState.MODAL
    }

    /** HallLayer.addAmbition opens its complete HallMenuLayer for 2.5 s. */
    private fun suspendForAmbition(delta: Int) {
        ambitionFrom = stage.ambition
        stage.addAmbition(delta)
        ambitionTo = stage.ambition
        ambitionElapsedSeconds = 0f
        ambitionIndicatorEnabled = true
        currentModalText = "ambition"
        currentModalKind = ModalKind.AMBITION
        currentModalFixedText = ""
        modalNextText = null
        modalRemainingSeconds = 2.5f
        state = PlaybackState.MODAL
    }

    private fun suspendFor(seconds: Float) {
        delayRemainingSeconds = seconds.coerceAtLeast(0f)
        state = PlaybackState.DELAY
    }

    private fun suspendForBattleBackgroundLoad(mapIndex: Int) {
        check(!hasPendingBattleBackgroundLoad) { "동시에 두 개의 loadBg 콜백이 대기 중입니다." }
        pendingBattleBackgroundLoadIndex = mapIndex
        // This is a resource event callback rather than elapsed time. Keep
        // the existing DELAY routing without allowing update() to resume it.
        delayRemainingSeconds = Float.MAX_VALUE
        state = PlaybackState.DELAY
    }

    /**
     * Every FightLayer command calls BattleLayer.pause() and resumes from its
     * native animation/typing callback.  The headless interpreter still
     * records commands synchronously, while the production renderer must
     * acknowledge each consumed command through [resumeExternalDelay].
     */
    private fun suspendForExternalFightCommand() {
        if (externalFightPresentation) suspendFor(Float.MAX_VALUE)
    }

    private fun activeFightId(): Long = requireNotNull(stage.activeFightId) {
        "$moduleName invoked a fight command without an active stage.startFight()"
    }

    private fun runUntilInput() {
        state = PlaybackState.COMPLETE
        while (frames.isNotEmpty() && !ended) {
            check(++executedStatements <= MAX_STATEMENTS_PER_START) {
                "$moduleName 실행이 $MAX_STATEMENTS_PER_START 문장을 초과했습니다. 무한 goto/호출을 확인하세요."
            }
            val frame = frames.last()
            if (frame.index >= frame.function.statements.size) {
                frames.removeLast()
                continue
            }
            val statement = frame.function.statements[frame.index++]
            executeStatement(statement, frame)
            if (state != PlaybackState.COMPLETE) return
        }
    }

    private fun executeStatement(statement: JsonValue, frame: Frame) {
        when (statement.typeName()) {
            "Expr" -> eval(statement.field("value"), frame)
            "Assign" -> {
                val valueNode = statement.field("value")
                if (isStageChoice(valueNode)) {
                    val args = evalArguments(valueNode.field("args"), frame)
                    currentChoice = Choice(
                        args.firstOrNull().asText().lineSequence().map(String::trim).filter(String::isNotEmpty).toList(),
                        args.getOrNull(1)?.asInt()?.takeIf { it >= 0 },
                    )
                    selectedChoice = 0
                    setChoiceSource(valueNode, frame)
                    pendingChoiceTarget = statement.field("targets").children().firstOrNull()
                    state = PlaybackState.CHOICE
                } else {
                    val value = eval(valueNode, frame)
                    statement.field("targets").children().forEach { assign(it, value, frame) }
                }
            }
            "AugAssign" -> {
                val target = statement.field("target")
                val current = eval(target, frame).asInt()
                val value = eval(statement.field("value"), frame).asInt()
                val result = when (statement.field("op").typeName()) {
                    "Add" -> current + value
                    "Sub" -> current - value
                    "Mult" -> current * value
                    "Mod" -> current % value
                    else -> current
                }
                assign(target, result, frame)
            }
            "If" -> {
                val ask = findStageAsk(statement.field("test"))
                if (ask != null && pendingAskResult == null) {
                    currentChoice = Choice(listOf("예", "비"), null)
                    selectedChoice = 0
                    isAskChoice = true
                    setChoiceSource(ask, frame)
                    pendingAskStatement = statement
                    pendingAskFrame = frame
                    state = PlaybackState.CHOICE
                    return
                }
                val selected = if (evalBoolean(statement.field("test"), frame)) statement.field("body") else statement.field("orelse")
                val statements = selected.children().toList()
                if (statements.isNotEmpty()) frames.addLast(Frame(RuntimeFunction("<if>", statements, emptyMap()), 0, frame.locals, frame.sourceFunction))
            }
            "For" -> executeFor(statement, frame)
            "Return" -> frames.removeLast()
        }
    }

    private fun executeFor(statement: JsonValue, frame: Frame) {
        val iterable = eval(statement.field("iter"), frame) as? List<*> ?: emptyList<Any?>()
        val body = statement.field("body").children().toList()
        iterable.asReversed().forEach { item ->
            val scoped = frame.locals.toMutableMap()
            assign(statement.field("target"), item, Frame(frame.function, frame.index, scoped))
            frames.addLast(Frame(RuntimeFunction("<for>", body, emptyMap()), 0, scoped, frame.sourceFunction))
        }
    }

    private fun eval(node: JsonValue, frame: Frame): Any? = when (node.typeName()) {
        "Constant" -> node.field("value").value()
        "Name" -> lookupName(node.field("id").asString(), frame)
        "List", "Tuple" -> node.field("elts").children().mapTo(mutableListOf()) { eval(it, frame) }
        "Dict" -> node.field("keys").children().zip(node.field("values").children()).associate { eval(it.first, frame).toString() to eval(it.second, frame) }.toMutableMap()
        "Subscript" -> readSubscript(node, frame)
        "UnaryOp" -> when (node.field("op").typeName()) {
            "Not" -> !evalBoolean(node.field("operand"), frame)
            "USub" -> -eval(node.field("operand"), frame).asInt()
            else -> 0
        }
        "BoolOp" -> {
            val values = node.field("values").children().toList()
            if (node.field("op").typeName() == "And") values.all { evalBoolean(it, frame) } else values.any { evalBoolean(it, frame) }
        }
        "Compare" -> evalCompare(node, frame)
        "BinOp" -> evalBinary(node, frame)
        "Call" -> invokeCall(node, frame)
        "JoinedStr" -> node.field("values").children().joinToString("") { eval(it, frame).asText() }
        "FormattedValue" -> eval(node.field("value"), frame).asText()
        "Attribute" -> 0
        else -> 0
    }

    private fun invokeCall(node: JsonValue, frame: Frame): Any? {
        val path = node.field("func").expressionPath()
        val args = evalArguments(node.field("args"), frame)
        when (path) {
            "stage.say" -> {
                val sourceText = args.firstOrNull().asText()
                // BattleLayer.say routes text without a leading speaker token
                // to info2/InfoLayer, not SayLayer. This occurs in S_00's
                // closing Cao Cao monologue and must not create a dialogue
                // close/open edge.
                if (!sourceText.startsWith("&")) {
                    currentDialogueSourceText = null
                    suspendForInfo(sourceText, ModalKind.INFO)
                    return null
                }
                val dialogues = parseDialogueBlocks(sourceText)
                beginDialogueLifecycle(sourceText)
                presentDialogue(dialogues.firstOrNull() ?: Dialogue(null, ""))
                dialogues.drop(1).forEach(pendingDialogues::addLast)
                state = PlaybackState.DIALOGUE
                return null
            }
            "stage.talk" -> {
                // BattleLayer.talk(primary, fallback, text) chooses the first
                // visible unit and otherwise the fallback, then opens one
                // centered, closable SayLayer with an injected speaker tag.
                val primary = args.intAt(0)
                val fallback = args.intAt(1)
                val speaker = if (primary in battleContext.activeCharacterIds) primary else fallback
                val sourceText = "&$speaker\n${args.getOrNull(2).asText()}"
                beginDialogueLifecycle(sourceText)
                presentDialogue(Dialogue(speaker.toString(), args.getOrNull(2).asText()))
                state = PlaybackState.DIALOGUE
                return null
            }
            "stage.choice" -> {
                currentChoice = Choice(
                    args.firstOrNull().asText().lineSequence().map(String::trim).filter(String::isNotEmpty).toList(),
                    args.getOrNull(1)?.asInt()?.takeIf { it >= 0 },
                )
                selectedChoice = 0
                setChoiceSource(node, frame)
                state = PlaybackState.CHOICE
                return null
            }
            "stage.loadBg" -> {
                // The same Python API name routes to different native
                // layers. HallLayer interprets (type, variant) for Mmap;
                // BattleLayer._loadBg(t) interprets its sole argument as an
                // HM index. Treating S_00.loadBg(0) as a Hall call selected
                // the default variant (20 → HM_21) instead of source HM_1.
                if (moduleName.startsWith("S_")) {
                    // BattleLayer.loadBg consumes JUMP_OFFSET before its
                    // unconditional pause. BG_INDEX itself is not published
                    // until _loadBg's successful map/avatar callback tail.
                    var mapIndex = args.intAt(0)
                    val jumpOffset = gvars[LOAD_BG_JUMP_OFFSET_GLOBAL].asInt()
                    if (mapIndex < 0 || jumpOffset != 0) {
                        gvars[LOAD_BG_JUMP_OFFSET_GLOBAL] = 0
                        mapIndex += 100 * jumpOffset
                    }
                    if (externalBattlePresentation) suspendForBattleBackgroundLoad(mapIndex)
                    else stage.selectBattleMap(mapIndex)
                } else stage.apply(ScenarioCommand.LoadBackground(args.intAt(0), args.intAt(1)))
            }
            "stage.setEventName" -> {
                val text = args.firstOrNull().asText()
                stage.apply(ScenarioCommand.SetEventName(text))
                // StageLayer mutates Model first, but creates InfoLayer only
                // after draw and only while presentation skipping is off.
                if (!stagePresentationSkipped && stage.battleDrawRequested) suspendForInfo(text)
            }
            "stage.setStageName" -> {
                val text = args.firstOrNull().asText()
                stage.setStageName(text)
                if (!stagePresentationSkipped && stage.battleDrawRequested) suspendForInfo(text)
            }
            "stage.clsUnit" -> stage.clearUnits()
            "stage.setMenuVisible" -> stage.setMenuVisible(args.firstOrNull().asBooleanValue())
            "stage.menuVisible" -> return stage.menuVisible
            "stage.sceneIndex" -> return stage.sceneIndex
            "stage.incSceneIdx" -> stage.incrementSceneIndex()
            "stage.addAmbition" -> if (moduleName.startsWith("R_")) suspendForAmbition(args.firstOrNull().asInt())
            else stage.addAmbition(args.firstOrNull().asInt())
            "model.setAmbition" -> stage.addAmbition(args.firstOrNull().asInt() - stage.ambition)
            "model.ambition" -> return stage.ambition
            "model.addMoney" -> campaign.addMoney(args.firstOrNull().asInt())
            "model.money" -> return campaign.money
            "model.setFace" -> stage.setFace(args.firstOrNull().asInt())
            "model.random" -> return nextModelRandom().also { value ->
                randomTrace += RandomTrace(
                    module = moduleName,
                    function = frame.sourceFunction,
                    line = node.get("location")?.getInt("line", -1)?.takeIf { it > 0 }
                        ?: error("$moduleName ${frame.function.name} random source line is missing"),
                    value = value,
                )
                if (stopAfterRandomTraceCount?.let { randomTrace.size >= it } == true) ended = true
            }
            "model.initLocalVar" -> stage.resetLocalVariables()
            "model.unitJoin" -> stage.joinUnit(args.firstOrNull().asInt())
            "stage.setWinCondition" -> stage.setWinCondition(args.firstOrNull().asText())
            "stage.showWinCondition" -> {
                // Original BattleLayer.showWinCondition pauses the Script
                // before it creates WinConditionsLayer.  Do not let the AST
                // execute the following battle commands until its close
                // callback invokes resume().
                suspendForWinCondition(args.firstOrNull().asText())
            }
            "stage.bottomTxt" -> {
                val text = args.firstOrNull().asText()
                if (moduleName.startsWith("R_")) {
                    suspendForMapInfo(
                        text,
                        changePage = args.getOrNull(1).asBooleanValue(),
                        wepon = args.getOrNull(2).asBooleanValue(),
                        wait = args.getOrNull(3).asBooleanValue(),
                    )
                } else stage.setBottomText(text)
            }
            "stage.setGlobalData" -> stage.setBattleGlobalData(
                args.firstOrNull().asInt(),
                args.getOrNull(1).asInt(),
                args.getOrNull(2).asInt(),
                args.getOrNull(3).asInt(),
                args.getOrNull(4).asInt(),
                args.getOrNull(5).asInt(),
            )
            "stage.initFight" -> stage.initFight()
            "stage.startOper" -> stage.startOperation()
            "stage.setMaxRound" -> stage.setMaxRound(args.intAt(0), battleContext.enabledFeatures)
            "stage.startFight" -> {
                val fightId = stage.startFight(args.intAt(0), args.intAt(1), args.intAt(2))
                suspendForExternalFightCommand()
                return FightReference(fightId)
            }
            "stage.bgSound" -> stage.setBackgroundSound(args.firstOrNull().asInt())
            "stage.sectionName" -> {
                stage.setSection(args.intAt(0), args.getOrNull(1).asText())
                if (moduleName.startsWith("R_")) suspendForSection(args.intAt(0), args.getOrNull(1).asText())
            }
            "stage.showHead" -> stage.showHead(args.intAt(0), args.intAt(1), args.intAt(2)).let { duration ->
                if (duration > 0f) suspendFor(duration)
            }
            "stage.effectSound" -> stage.effectSound(args.intAt(0), args.getOrNull(1)?.asInt() ?: 1)
            // StageLayer.delay pauses its Script component for 0.1 * t seconds.
            // Do not let the AST run into the next say/action in the same frame.
            "stage.delay" -> suspendFor(args.firstOrNull().asInt() * 0.1f)
            "stage.draw" -> stage.drawBattle()
            "stage.info" -> {
                val text = args.firstOrNull().asText()
                val delay = (args.getOrNull(1)?.asInt() ?: 1).coerceAtLeast(0).toFloat()
                // StageLayer.info consumes this one-shot global. A non-zero
                // value routes to Model.info and does not pause/open InfoLayer.
                val infoControl = gvars.remove(INFO_CTRL_GLOBAL).asInt()
                if (stagePresentationSkipped) Unit
                else if (infoControl != 0) stage.controlledInfo(infoControl, text)
                else suspendForInfo(text, ModalKind.INFO, delay)
            }
            "stage.infoTransfer" -> stage.infoTransfer(args.intAt(0), args.getOrNull(1).asText(), gvars[4054].asInt())
            "stage.setJoinBattle" -> stage.setJoinBattle(args.intAt(0), args.intAt(1), args.getOrNull(2).asList(), args.getOrNull(3).asList())
            "stage.setBattlePos" -> stage.setBattlePositions(args.firstOrNull().asList())
            "stage.setJoinEquip" -> stage.setJoinEquip(args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3), args.intAt(4), args.intAt(5))
            "stage.ending" -> stage.ending(args.intAt(0))
            "stage.reward" -> {
                stage.reward(
                    bonusMoney = args.getOrNull(0).asInt(),
                    items = args.getOrNull(1).asList(),
                    end = args.getOrNull(2).asBooleanValue(),
                )
                // BattleLayer.reward pauses the Script until RewardLayer's
                // completion callback.  Retaining that suspension is what
                // prevents scene2/end routing from bypassing the reward UI.
                state = PlaybackState.MODAL
            }
            "stage.lose" -> stage.lose()
            "stage.end" -> stage.endBattle()
            "stage.jumpScene" -> {
                stage.jumpScene(args.intAt(0))
                ended = true
            }
            "stage.itemVars" -> stage.addItemVariables(args.getOrNull(0).asList(), args.getOrNull(1).asList())
            "stage.getItem" -> {
                val itemId = args.intAt(0)
                val supplied = args.getOrNull(1).asInt()
                val addToInventory = args.getOrNull(2)?.asBooleanValue() ?: true
                val unitSelector = args.getOrNull(3)?.asInt() ?: 0
                val action = args.getOrNull(4)?.asInt() ?: 5 // BATTLE_ACTION.JU_QI_WU_QI
                stage.getItem(itemId, supplied, addToInventory).let { message ->
                    if (moduleName.startsWith("R_")) suspendForInfo(message, ModalKind.INFO)
                }
                if (moduleName.startsWith("S_") && externalBattlePresentation && action > 0 && unitSelector >= 0) {
                    stage.requestScriptPresentation(
                        ScenarioScriptPresentationRequest.GetItem(
                            itemId = itemId,
                            suppliedCountOrLevel = supplied,
                            addToInventory = addToInventory,
                            unitSelector = unitSelector,
                            action = action,
                            completionMessage = stage.battleItemCompletionMessage(itemId),
                        ),
                    )
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.nearEvent" -> stage.addNearEvent(args.firstOrNull().asList(), args.getOrNull(1).asInt())
            // BattleLayer.center mutates ScrollView.content.position and
            // dispatches MAP_SCROLLING synchronously. It never pauses the
            // source Script, so preserve every invocation in an ordered FIFO
            // and let the renderer consume all of them in this same frame.
            "stage.center" -> stage.requestCameraCenter(args.intAt(0), args.intAt(1))
            "stage.setEnemyEquip" -> stage.setEnemyEquipment(args.firstOrNull().asInt(), args.drop(1))
            "stage.unitAttr" -> return stage.unitAttribute(args.intAt(0), args.intAt(1))
            "stage.setUnitAttr" -> stage.setUnitAttribute(args.intAt(0), args.intAt(1), args.intAt(2))
            "stage.setUnitAbility" -> stage.changeUnitAttribute(args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3))
            "stage.setFAvatar" -> stage.setUnitAttribute(args.intAt(0), 27, args.intAt(1))
            "stage.varOper" -> applyStageVarOperation(args)
            "stage.varTest" -> return testStageVariables(args)
            // The desktop source leaves StageLayer.video() empty. Preserve
            // that behavior instead of reporting every authored intertitle
            // call as an unsupported runtime operation.
            "stage.video" -> Unit
            "stage.ask" -> return pendingAskResult ?: 0
            "stage.setUnitStatus" -> {
                val values = stage.setUnitStatuses(args.firstOrNull().asList())
                val presents = values.any { change ->
                    val hp = (change["hp"] as? Number)?.toInt() ?: 0
                    val mp = (change["mp"] as? Number)?.toInt() ?: 0
                    val status = (change["status"] as? Number)?.toInt() ?: -1
                    val hiddenStatuses = change["hStatus"].asList()
                    (hp != 0 && kotlin.math.abs(hp) != 255) ||
                        (mp != 0 && kotlin.math.abs(mp) != 255) ||
                        status != -1 || hiddenStatuses.isNotEmpty()
                }
                if (externalBattlePresentation && presents) {
                    stage.requestScriptPresentation(ScenarioScriptPresentationRequest.UnitStatusSettlement(values))
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.setFire" -> {
                val enabled = args.firstOrNull().asBooleanValue()
                val x = args.intAt(1); val y = args.intAt(2)
                stage.setFire(enabled, x, y)
                // BattleLayer.setObject2 centers every newly drawn fire and
                // resumes its Python helper after the authored one-second hold.
                if (externalBattlePresentation && enabled && stage.battleDrawRequested) {
                    stage.requestMapPresentation(ScenarioMapPresentationRequest(x, y, 1f))
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.setFires" -> {
                val enabled = args.firstOrNull().asBooleanValue()
                val positions = args.getOrNull(1).asList()
                stage.setFires(enabled, positions)
                // setFires starts all object loads together. Their callbacks
                // leave the camera on the final authored tile and share the
                // same one-second resume barrier (they are not serialized).
                val last = positions.lastOrNull().asList()
                if (externalBattlePresentation && enabled && stage.battleDrawRequested && last.size >= 2) {
                    stage.requestMapPresentation(ScenarioMapPresentationRequest(last[0].asInt(), last[1].asInt(), 1f))
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.playMagicMeff" -> {
                val x = args.intAt(0); val y = args.intAt(1); val raw = args.intAt(2)
                if (externalBattlePresentation) {
                    val magicCallId = raw.takeIf { it >= 100 && it != 255 }?.minus(100)
                    // Mcall5 is the authored S_00 fire invocation. Its source
                    // JsonAsset contains 30 frames at 24fps (1.25 seconds);
                    // BattleLayer owns that FINISHED callback and resumes.
                    stage.requestMapPresentation(ScenarioMapPresentationRequest(x, y, if (magicCallId != null) 1.25f else 1f, magicCallId))
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.attackAction" -> {
                val flags = args.intAt(2)
                stage.attackAction(args.intAt(0), args.intAt(1), flags)
                // playAtkAnime is resumed by the attack clip's authored `hit`
                // event, not FINISHED. It then awaits the complete target
                // reaction and restores the attacker to its default action.
                // For S_00 this is tick 22 + 14 for anime21, and tick 11 + 14
                // for anime25. Waiting for 40/29 plus an invented tail made
                // both first impacts and every following script cue late.
                suspendFor(BattlePhysicalPresentationTimeline.scriptedAttackDuration(flags))
            }
            "stage.setObjects" -> {
                val enabled = args.firstOrNull().asBooleanValue()
                val terrain = args.getOrNull(1).asInt()
                val positions = args.getOrNull(2).asList()
                stage.setMapObjects(enabled, terrain, positions)
                if (enqueueMapObjectsPresentation(enabled, terrain, positions, soundOnFirstOnly = true)) {
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.setObject" -> {
                val enabled = args.firstOrNull().asBooleanValue()
                val terrain = args.getOrNull(1).asInt()
                val positions = listOf(listOf(args.getOrNull(2).asInt(), args.getOrNull(3).asInt(), args.getOrNull(4).asInt()))
                stage.setMapObjects(enabled = enabled, terrainId = terrain, positions = positions)
                if (enqueueMapObjectsPresentation(enabled, terrain, positions, soundOnFirstOnly = true)) {
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.heightLight" -> {
                if (externalBattlePresentation) {
                    stage.requestScriptPresentation(
                        ScenarioScriptPresentationRequest.RectangleHighlight(
                            args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3),
                        ),
                    )
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.countDir" -> return stage.countDirection(args.intAt(0), args.intAt(1))
            "stage.round" -> return battleContext.round
            "stage.curCamp" -> return battleContext.camp
            "stage.maxRound" -> return battleContext.maxRound
            "stage.unitClickTest" -> return battleContext.clickedCharacterId == args.firstOrNull().asInt()
            "stage.battleTest" -> return battleContext.clickedCharacterId != null
            "stage.winTest" -> return battleContext.enemyDefeated
            "stage.loseTest", "stage.isLose" -> return battleContext.playerDefeated
            "stage.isNear" -> return isNear(args)
            "stage.isInPos" -> return isInPosition(args)
            "stage.isInRect" -> return isInRectangle(args)
            "stage.totalRectUnit" -> return totalRectangleUnits(args)
            "stage.totalUnit" -> return totalUnits(args.firstOrNull().asInt())
            "stage.unitStateTest" -> return unitStateTest(args)
            "stage.setAI" -> stage.setBattleAi(
                camp = args.intAt(4),
                x1 = args.intAt(0),
                y1 = args.intAt(1),
                x2 = args.intAt(2),
                y2 = args.intAt(3),
                ai = args.intAt(5),
                targetId = args.getOrNull(6)?.asInt() ?: -1,
                targetX = args.getOrNull(7)?.asInt() ?: 0,
                targetY = args.getOrNull(8)?.asInt() ?: 0,
            )
            "stage.resumeCtrl" -> Unit
            "stage.setRectUnitHide" -> {
                if (externalBattlePresentation) {
                    val count = stage.requestRectUnitHide(
                        args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3), args.intAt(4),
                        args.getOrNull(5)?.asInt() ?: 0,
                    )
                    // Empty selection is the source's synchronous no-op.
                    if (count > 0) suspendFor(Float.MAX_VALUE)
                } else {
                    stage.hideBattleRect(args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3), args.intAt(4))
                }
            }
            "stage.showUnit" -> stage.apply(ScenarioCommand.ShowUnit(args.intAt(0), args.intAt(1), args.intAt(2), args.intAt(3)))
            "stage.createFriend" -> stage.createBattleUnits(ScenarioUnitFaction.FRIEND, args.firstOrNull().asList())
            "stage.createEnemy", "stage.createEnemy2" -> stage.createBattleUnits(ScenarioUnitFaction.ENEMY, args.firstOrNull().asList())
            "stage.createMine" -> stage.createBattleUnits(ScenarioUnitFaction.MINE, args.firstOrNull().asList())
            "stage.showUnits" -> args.firstOrNull().asList().forEach { values ->
                val entry = values.asList()
                if (entry.size >= 3) stage.apply(ScenarioCommand.ShowUnit(entry[0].asInt(), entry[1].asInt(), entry[2].asInt(), entry.getOrNull(3).asInt()))
            }
            "stage.unitsMove" -> {
                val requests = args.firstOrNull().asList().mapNotNull { values ->
                    val entry = values.asList()
                    val unit = entry.firstOrNull() as? UnitReference ?: return@mapNotNull null
                    if (entry.size >= 3) {
                        ScenarioCommand.MoveUnit(unit.id, entry[1].asInt(), entry[2].asInt(), entry.getOrNull(3).asInt())
                    } else null
                }
                val duration = stage.moveUnits(requests)
                if (duration > 0f) suspendFor(duration)
            }
            "stage.unit" -> {
                val value = args.firstOrNull().asInt()
                // BattleLayer.unit(t, flags) switches from character-ID
                // lookup (`_unitIds`) to the physical `_unitSet` whenever
                // flags bit 1 is present. S_31..S_57 contain 260 such calls,
                // all addressing selected mine slots 0..14; interpreting the
                // slot as a character ID moved/hidden the wrong actor.
                return resolveStageUnitReference(value, args.getOrNull(1)?.asInt() ?: 0)
            }
            "stage.head" -> return HeadReference(args.firstOrNull().asInt())
            "stage.unit().move" -> unitReference(node, frame)?.let { unit ->
                val duration = stage.moveDuration(unit.id, args.intAt(0), args.intAt(1))
                stage.apply(ScenarioCommand.MoveUnit(unit.id, args.intAt(0), args.intAt(1), args.intAt(2)))
                if (duration > 0f) suspendFor(duration)
            }
            "stage.unit().setAction" -> unitReference(node, frame)?.let { unit ->
                stage.setScriptedUnitAction(
                    unitId = unit.id,
                    action = args.intAt(0),
                    direction = args.getOrNull(1)?.asInt() ?: -1,
                    loop = args.getOrNull(2)?.asBooleanValue() ?: false,
                )
                if (externalBattlePresentation && args.intAt(0) > 0 && !(args.getOrNull(2)?.asBooleanValue() ?: false)) {
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.unit().show" -> unitReference(node, frame)?.let { reference ->
                val unit = stage.unit(reference.id)
                if (!unit.visible) {
                    if (externalBattlePresentation) {
                        val request = ScenarioUnitShowRequest(
                            unitId = reference.id,
                            x = args.getOrNull(0)?.asInt() ?: -1,
                            y = args.getOrNull(1)?.asInt() ?: -1,
                            direction = args.getOrNull(2)?.asInt() ?: -1,
                            flags = args.getOrNull(3)?.asInt() ?: 0,
                        )
                        stage.requestUnitShow(request)
                        suspendFor(Float.MAX_VALUE)
                    } else unit.visible = true
                }
            }
            "stage.unit().hide" -> unitReference(node, frame)?.let {
                // BattleUnit.hide returns immediately when its node is already hidden.
                if (!stage.unit(it.id).visible) Unit
                else if (externalBattlePresentation) {
                    stage.requestUnitHide(it.id, args.firstOrNull()?.asInt() ?: 0)
                    suspendFor(Float.MAX_VALUE)
                } else stage.unit(it.id).visible = false
            }
            "stage.unit().setDir" -> unitReference(node, frame)?.let { stage.setUnitDirection(it.id, args.firstOrNull().asInt()) }
            "stage.unit().setPosts" -> unitReference(node, frame)?.let { unit ->
                val flags = args.getOrNull(1)?.asInt() ?: 19
                stage.setBattleUnitPosts(unit.id, args.firstOrNull().asInt(), flags, enabledFeatures = battleContext.enabledFeatures)
                // Recovered BattleUnit pauses only after `testAvatar()` found
                // a different group.  A same-avatar call is wholly synchronous.
                if (externalBattlePresentation && stage.lastBattleUnitPostsRequiresPause) suspendFor(Float.MAX_VALUE)
            }
            "model.unit().setPosts" -> unitReference(node, frame)?.let { unit ->
                stage.setModelUnitPosts(
                    unit.id, args.firstOrNull().asInt(), args.getOrNull(1)?.asInt() ?: 3,
                    enabledFeatures = battleContext.enabledFeatures,
                )
            }
            "stage.unit().addLv" -> unitReference(node, frame)?.let {
                stage.addUnitLevels(it.id, args.firstOrNull().asInt(), battleContext.enabledFeatures)
            }
            "stage.unit().setAI" -> unitReference(node, frame)?.let {
                stage.setUnitAi(
                    unitId = it.id,
                    ai = args.intAt(0),
                    targetId = args.getOrNull(1)?.asInt() ?: -1,
                    targetX = args.getOrNull(2)?.asInt() ?: 0,
                    targetY = args.getOrNull(3)?.asInt() ?: 0,
                )
            }
            "stage.unit().retreatTxt" -> unitReference(node, frame)?.let {
                stage.setUnitRetreatTextEnabled(it.id, args.firstOrNull().asBooleanValue())
            }
            "stage.unit().heightLight" -> unitReference(node, frame)?.let {
                if (externalBattlePresentation) {
                    stage.requestScriptPresentation(ScenarioScriptPresentationRequest.UnitHighlight(it.id))
                    suspendFor(Float.MAX_VALUE)
                }
            }
            "stage.head().move" -> headReference(node, frame)?.let {
                stage.moveHead(it.id, args.intAt(0), args.intAt(1)).let { duration ->
                    if (duration > 0f) suspendFor(duration)
                }
            }
            "stage.head().hide" -> headReference(node, frame)?.let {
                stage.hideHead(it.id).let { duration ->
                    if (duration > 0f) suspendFor(duration)
                }
            }
            "fight.showUnit" -> activeFightId().let { fightId ->
                stage.enqueueFightCommand(
                    ScenarioFightCommand.ShowUnit(
                        fightId = fightId,
                        mine = args.firstOrNull().asBooleanValue(),
                        text = args.getOrNull(1).asText(),
                        entryAction = args.intAt(2),
                    ),
                )
                suspendForExternalFightCommand()
            }
            "fight.showStart" -> activeFightId().let { fightId ->
                stage.enqueueFightCommand(ScenarioFightCommand.ShowStart(fightId))
                suspendForExternalFightCommand()
            }
            "fight.setAction" -> activeFightId().let { fightId ->
                stage.enqueueFightCommand(
                    ScenarioFightCommand.SetAction(fightId, args.firstOrNull().asBooleanValue(), args.intAt(1)),
                )
                suspendForExternalFightCommand()
            }
            "fight.say" -> activeFightId().let { fightId ->
                stage.enqueueFightCommand(
                    ScenarioFightCommand.Say(
                        fightId = fightId,
                        mine = args.firstOrNull().asBooleanValue(),
                        text = args.getOrNull(1).asText(),
                        flag = args.getOrNull(2).asBooleanValue(),
                    ),
                )
                suspendForExternalFightCommand()
            }
            "fight.attack2" -> activeFightId().let { fightId ->
                stage.enqueueFightCommand(
                    ScenarioFightCommand.Attack2(
                        fightId = fightId,
                        mine = args.firstOrNull().asBooleanValue(),
                        style = args.intAt(1),
                        defended = args.getOrNull(2).asBooleanValue(),
                    ),
                )
                suspendForExternalFightCommand()
            }
            "fight.attack1" -> activeFightId().let { fightId ->
                stage.enqueueFightCommand(
                    ScenarioFightCommand.Attack1(
                        fightId = fightId,
                        mine = args.firstOrNull().asBooleanValue(),
                        style = args.intAt(1),
                        critical = args.getOrNull(2).asBooleanValue(),
                    ),
                )
                suspendForExternalFightCommand()
            }
            "fight.death" -> activeFightId().let { fightId ->
                stage.enqueueFightCommand(
                    ScenarioFightCommand.Death(fightId, enemy = args.firstOrNull().asBooleanValue()),
                )
                suspendForExternalFightCommand()
            }
            // FightLayer.end itself is synchronous: it restores the saved
            // battle music and removes the duel layer without another pause.
            "fight.end" -> activeFightId().let { fightId ->
                stage.enqueueFightCommand(ScenarioFightCommand.End(fightId))
            }
            "label" -> Unit
            "goto" -> jumpToLabel(args.firstOrNull().asText())
            "call" -> pushFunction(args.firstOrNull().asText())
            "hasFunc" -> functions.containsKey(args.firstOrNull().asText())
            "range" -> (args.firstOrNull().asInt() until args.getOrNull(1).asInt()).toList()
            "len" -> args.firstOrNull().asList().size
            "int" -> args.firstOrNull().asInt()
            "str" -> args.firstOrNull().asText()
            else -> {
                val receiver = node.field("func").takeIf { it.typeName() == "Attribute" }?.field("value")?.let { eval(it, frame) }
                if (path?.endsWith(".push") == true || path?.endsWith(".append") == true) {
                    @Suppress("UNCHECKED_CAST")
                    (receiver as? MutableList<Any?>)?.add(args.firstOrNull())
                } else if (path in functions) {
                    pushFunction(path!!)
                } else if (path != null) {
                    unhandledCalls[path] = (unhandledCalls[path] ?: 0) + 1
                }
            }
        }
        return 0
    }

    private fun stageVariableValue(kind: Int, value: Int): Int = when (kind) {
        0 -> value
        1 -> readStageAddress(pvars[value].asInt())
        2 -> pvars[value].asInt()
        4 -> gvars[value].asInt()
        5 -> ADDRESS_INTVAR_START + 4 * value
        else -> 0
    }

    private fun readStageAddress(address: Int): Int =
        if (address in ADDRESS_INTVAR_START until ADDRESS_INTVAR_END) {
            gvars[(address - ADDRESS_INTVAR_START) / 4].asInt()
        } else 0

    private fun writeStageAddress(address: Int, value: Int) {
        if (address in ADDRESS_INTVAR_START until ADDRESS_INTVAR_END) {
            gvars[(address - ADDRESS_INTVAR_START) / 4] = value
        }
    }

    private fun applyStageVarOperation(args: List<Any?>) {
        val targetKind = args.intAt(0)
        val targetIndex = args.intAt(1)
        val operation = args.intAt(2)
        val sourceKind = args.intAt(3)
        val sourceIndex = args.intAt(4)
        val current = when (targetKind) {
            0 -> readStageAddress(pvars[targetIndex].asInt())
            1 -> pvars[targetIndex].asInt()
            2 -> gvars[targetIndex].asInt()
            else -> 0
        }
        val operand = stageVariableValue(sourceKind, sourceIndex)
        val result = when (operation) {
            0 -> current + operand
            1 -> current - operand
            2 -> operand
            3 -> current * operand
            4 -> if (operand == 0) 0 else Math.floorDiv(current, operand)
            5, 6 -> if (operand == 0) 0 else current % operand
            else -> current
        }
        when (targetKind) {
            0 -> writeStageAddress(pvars[targetIndex].asInt(), result)
            1 -> pvars[targetIndex] = result
            2 -> gvars[targetIndex] = result
        }
    }

    private fun testStageVariables(args: List<Any?>): Boolean {
        val left = stageVariableValue(args.intAt(0), args.intAt(1))
        val right = stageVariableValue(args.intAt(3), args.intAt(4))
        return when (args.intAt(2)) {
            0 -> left == right
            1 -> left >= right
            2 -> left <= right
            3 -> left != right
            4 -> left < right
            5 -> left > right
            else -> false
        }
    }

    private fun unitReference(node: JsonValue, frame: Frame): UnitReference? {
        val function = node.field("func")
        if (function.typeName() != "Attribute") return null
        return eval(function.field("value"), frame) as? UnitReference
    }

    private fun headReference(node: JsonValue, frame: Frame): HeadReference? {
        val function = node.field("func")
        if (function.typeName() != "Attribute") return null
        return eval(function.field("value"), frame) as? HeadReference
    }

    private fun isNear(args: List<Any?>): Boolean {
        var firstId = args.intAt(0)
        var target = args.intAt(1)
        if (firstId >= 1024) {
            val swapped = firstId
            firstId = target
            target = swapped
        }
        val first = battleContext.positions[firstId] ?: return false
        val offsets = if (args.getOrNull(2).asBooleanValue()) {
            battleContext.infantryNearOffsets
        } else {
            battleContext.attackOffsets[firstId] ?: DEFAULT_CARDINAL_NEAR_OFFSETS
        }
        val covered = offsets.mapTo(hashSetOf()) { (x, y) -> first.first + x to first.second + y }
        if (target < 1024) return battleContext.positions[target] in covered
        val campSelector = target - 1024
        val candidates = when (campSelector) {
            // BattleUnit.isMine() is camp < ENEMY: Mine and Friend share side.
            1 -> listOf(0, 1).flatMap { battleContext.positionsByCamp[it].orEmpty() }
            2 -> listOf(2, 3).flatMap { battleContext.positionsByCamp[it].orEmpty() }
            else -> emptyList()
        }
        return candidates.any { it in covered }
    }

    private fun isInPosition(args: List<Any?>): Boolean {
        val target = args.intAt(0)
        val x = args.intAt(1)
        val y = args.intAt(2)
        return if (target >= 1024) positionsForFilterSelector(target).any { it == (x to y) }
        else battleContext.positions[target] == (x to y)
    }

    private fun isInRectangle(args: List<Any?>): Boolean {
        val target = args.intAt(0)
        val xRange = args.intAt(1)..args.intAt(3)
        val yRange = args.intAt(2)..args.intAt(4)
        val positions = if (target >= 1024) positionsForFilterSelector(target) else listOfNotNull(battleContext.positions[target])
        return positions.any { (x, y) -> x in xRange && y in yRange }
    }

    private fun totalRectangleUnits(args: List<Any?>): Int {
        val xRange = args.intAt(1)..args.intAt(3)
        val yRange = args.intAt(2)..args.intAt(4)
        val type = args.intAt(0)
        // BattleLayer.totalRectUnit uses isExist(): hidden and HP-zero actors
        // are excluded before the 0..6 camp selector is applied.
        return battleContext.positions.count { (id, position) ->
            val camp = battleContext.campByCharacterId[id] ?: return@count false
            val hp = battleContext.attributes[id]?.get(7) ?: 1
            hp > 0 && sourceUnitTypeMatches(camp, type) && position.first in xRange && position.second in yRange
        }
    }

    private fun totalUnits(type: Int): Int = battleContext.positionsByCamp.entries.sumOf { (camp, positions) ->
        if (sourceUnitTypeMatches(camp, type)) positions.size else 0
    }

    private fun positionsForFilterSelector(selector: Int): List<Pair<Int, Int>> = when (selector) {
        // BattleLayer._filterUnit selectors: all, mine side, enemy side, and
        // the current operated unit. The first three are used extensively by
        // S_31..S_57; treating 1026 as a literal character id prevented enemy
        // trigger zones from ever firing.
        1024 -> battleContext.positions.values.toList()
        1025 -> listOf(0, 1).flatMap { battleContext.positionsByCamp[it].orEmpty() }
        1026 -> listOf(2, 3).flatMap { battleContext.positionsByCamp[it].orEmpty() }
        1027 -> battleContext.clickedCharacterId?.let { battleContext.positions[it] }?.let(::listOf).orEmpty()
        else -> emptyList()
    }

    /** Mirrors StageLayer.unitStateTest: mode 0/1/2/3 means >=, <, ==, !=. */
    private fun unitStateTest(args: List<Any?>): Boolean {
        val unitId = args.intAt(0)
        val attribute = args.intAt(1)
        val compared = args.intAt(2)
        val mode = args.intAt(3)
        val value = battleContext.attributes[unitId]?.get(attribute)
            ?: stage.unitAttribute(unitId, attribute)
        return when (mode) {
            0 -> value >= compared
            1 -> value < compared
            2 -> value == compared
            3 -> value != compared
            else -> false
        }
    }

    private fun jumpToLabel(label: String) {
        val current = frames.lastOrNull()
        val currentIndex = current?.function?.labels?.get(label)
        if (currentIndex != null) {
            current.index = currentIndex + 1
            return
        }
        val target = functions.values.firstOrNull { label in it.labels || label in it.labelEntrypoints } ?: return
        // A Python goto leaves every synthetic <if>/<for> continuation of
        // the current source function. Keeping their parent frames below the
        // label entry replayed stale loop bodies after the goto completed
        // (R_00 scene2 eventually exceeded one million statements).
        val sourceFunction = current?.sourceFunction ?: target.name
        while (frames.lastOrNull()?.sourceFunction == sourceFunction) frames.removeLast()
        target.labelEntrypoints[label]?.let { entry ->
            frames.addLast(Frame(RuntimeFunction(target.name, entry, emptyMap(), target.labelEntrypoints), sourceFunction = sourceFunction))
        } ?: frames.addLast(Frame(target, target.labels.getValue(label) + 1))
    }

    private fun evalCompare(node: JsonValue, frame: Frame): Boolean {
        val left = eval(node.field("left"), frame)
        val right = node.field("comparators").children().firstOrNull()?.let { eval(it, frame) }
        return when (node.field("ops").children().firstOrNull()?.typeName()) {
            "Eq" -> left == right
            "NotEq" -> left != right
            "Lt" -> left.asInt() < right.asInt()
            "LtE" -> left.asInt() <= right.asInt()
            "Gt" -> left.asInt() > right.asInt()
            "GtE" -> left.asInt() >= right.asInt()
            "In" -> left in right.asList()
            else -> false
        }
    }

    private fun evalBinary(node: JsonValue, frame: Frame): Any {
        val left = eval(node.field("left"), frame)
        val right = eval(node.field("right"), frame)
        return when (node.field("op").typeName()) {
            "Add" -> if (left is String || right is String) left.asText() + right.asText() else left.asInt() + right.asInt()
            "Sub" -> left.asInt() - right.asInt()
            "Mult" -> left.asInt() * right.asInt()
            "Mod" -> left.asInt() % right.asInt()
            "LShift" -> left.asInt() shl right.asInt()
            "BitOr" -> left.asInt() or right.asInt()
            else -> 0
        }
    }

    private fun evalBoolean(node: JsonValue, frame: Frame): Boolean = when (val value = eval(node, frame)) {
        is Boolean -> value
        is Number -> value.toInt() != 0
        is String -> value.isNotEmpty()
        null -> false
        else -> true
    }

    private fun lookupName(name: String, frame: Frame): Any? = when (name) {
        "vars" -> vars
        "gvars" -> gvars
        "pvars" -> pvars
        else -> frame.locals[name] ?: globalVariables[name] ?: 0
    }

    private fun readSubscript(node: JsonValue, frame: Frame): Any? {
        val container = eval(node.field("value"), frame)
        val index = eval(node.field("slice"), frame).asInt()
        return when (container) {
            is Map<*, *> -> container[index] ?: 0
            is List<*> -> container.getOrElse(index) { 0 }
            else -> 0
        }
    }

    private fun assign(target: JsonValue, value: Any?, frame: Frame = frames.lastOrNull() ?: error("실행 프레임이 없습니다.")) {
        when (target.typeName()) {
            "Name" -> frame.locals[target.field("id").asString()] = value
            "Subscript" -> {
                val container = eval(target.field("value"), frame)
                val index = eval(target.field("slice"), frame).asInt()
                @Suppress("UNCHECKED_CAST")
                (container as? MutableMap<Int, Any?>)?.set(index, value)
            }
        }
    }

    private fun pushFunction(name: String, label: String? = null) {
        val function = functions[name] ?: return
        if (label != null) {
            function.labelEntrypoints[label]?.let { entry ->
                frames.addLast(Frame(RuntimeFunction(function.name, entry, emptyMap(), function.labelEntrypoints)))
                return
            }
            error("$moduleName $name has no label $label")
        }
        frames.addLast(Frame(function))
    }

    /** Mirrors Tool.random(0, 100): inclusive range and recovered LCG. */
    private fun nextModelRandom(): Int {
        randomDrawCount++
        if (injectedRandomValues.isNotEmpty()) return injectedRandomValues.removeFirst()
        val (nextSeed, value) = toolRandomFromSeed(toolRandomSeed)
        toolRandomSeed = nextSeed
        return value
    }

    private fun evalArguments(args: JsonValue, frame: Frame): List<Any?> = args.children().map { eval(it, frame) }.toList()

    private fun enqueueMapObjectsPresentation(
        enabled: Boolean,
        terrainId: Int,
        positions: List<Any?>,
        soundOnFirstOnly: Boolean,
    ): Boolean {
        // setObject2 only enters its visible camera/tween branch after draw().
        // Pre-draw map construction still mutates gate data but has no episode.
        if (!externalBattlePresentation || !stage.battleDrawRequested) return false
        val objects = positions.mapNotNull { raw ->
            val values = raw.asList()
            if (values.size < 3) null else ScenarioScriptPresentationRequest.MapObjects.Object(
                objectId = values[0].asInt(),
                x = values[1].asInt(),
                y = values[2].asInt(),
            )
        }.filter { enabled || it.objectId >= 4 }
        if (objects.isEmpty()) return false
        stage.requestScriptPresentation(
            ScenarioScriptPresentationRequest.MapObjects(
                enabled = enabled,
                terrainId = terrainId,
                objects = objects,
                soundOnFirstObjectOnly = soundOnFirstOnly,
            ),
        )
        return true
    }

    private fun setChoiceSource(node: JsonValue, frame: Frame) {
        currentChoiceFunction = frame.sourceFunction
        currentChoiceLine = node.get("location")?.getInt("line", -1)?.takeIf { it > 0 }
            ?: error("$moduleName ${frame.function.name} choice has no source line")
    }
    private fun isStageChoice(node: JsonValue): Boolean = node.typeName() == "Call" && node.field("func").expressionPath() == "stage.choice"

    private fun findStageAsk(node: JsonValue): JsonValue? {
        if (node.get("type") != null && node.typeName() == "Call" && node.field("func").expressionPath() == "stage.ask") return node
        var child = node.child
        while (child != null) {
            findStageAsk(child)?.let { return it }
            child = child.next
        }
        return null
    }
    private fun Any?.asText(): String = when (this) { null -> ""; else -> toString() }
    private fun Any?.asInt(): Int = when (this) { is Number -> toInt(); is Boolean -> if (this) 1 else 0; is String -> toIntOrNull() ?: 0; else -> 0 }
    private fun Any?.asBooleanValue(): Boolean = when (this) { is Boolean -> this; is Number -> toInt() != 0; is String -> this.equals("true", ignoreCase = true) || this.toIntOrNull()?.let { it != 0 } == true; else -> false }
    private fun Any?.asList(): List<Any?> = this as? List<Any?> ?: emptyList()
    private fun List<Any?>.intAt(index: Int): Int = getOrNull(index).asInt()

    companion object {
        private val DEFAULT_CARDINAL_NEAR_OFFSETS = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
        private val DEFAULT_INFANTRY_NEAR_OFFSETS = DEFAULT_CARDINAL_NEAR_OFFSETS +
            setOf(1 to 1, -1 to 1, 1 to -1, -1 to -1)
        private const val HALL_BATTLE_COMMAND_ID = Int.MIN_VALUE
        private const val MAX_STATEMENTS_PER_START = 100_000
        private const val ADDRESS_INTVAR_START = 5_251_072
        private const val ADDRESS_INTVAR_END = 5_273_600
        private const val INFO_CTRL_GLOBAL = 4071
        /** Config.GLOBAL_VAR.JUMP_OFFSET, consumed by BattleLayer.loadBg. */
        private const val LOAD_BG_JUMP_OFFSET_GLOBAL = 4051

        /** Exposed for source-contract tests of recovered Tool.random. */
        internal fun toolRandomFromSeed(seed: Double): Pair<Double, Int> {
            val nextSeed = (9301.0 * seed + 49297.0) % 233280.0
            return nextSeed to ((nextSeed / 233280.0 * 201.0).toInt() % 101)
        }

        /** Source Info/MapInfo/Section auto-close gates used by live screens. */
        internal fun modalMayAutoClose(kind: ModalKind?, text: String?, settingEnabled: Boolean): Boolean = when (kind) {
            ModalKind.AMBITION -> true
            ModalKind.INFO, ModalKind.EVENT -> settingEnabled || text.orEmpty().length < 10
            ModalKind.SECTION, ModalKind.MAP_INFO -> settingEnabled
            null -> false
        }

        fun load(moduleName: String, campaign: CampaignState = CampaignState()): PythonAstRuntime {
            val resourceName = "scenario-ast/$moduleName.json"
            val payloadText = PythonAstRuntime::class.java.classLoader.getResourceAsStream(resourceName)
                ?.use { it.reader(Charsets.UTF_8).readText() }
                ?: Gdx.files.internal(resourceName).readString("UTF-8")
            val payload = JsonReader().parse(payloadText)
            val module = payload.get("ast")
            val functions = module.field("body").children()
                .filter { it.typeName() == "FunctionDef" }
                .associate { node ->
                    val statements = node.field("body").children().toList()
                    val labels = statements.mapIndexedNotNull { index, statement ->
                        val call = statement.takeIf { it.typeName() == "Expr" }?.field("value")
                        if (call?.typeName() == "Call" && call.field("func").expressionPath() == "label") {
                            call.field("args").children().firstOrNull()?.field("value")?.asString()?.let { it to index }
                        } else null
                    }.toMap()
                    node.field("name").asString() to RuntimeFunction(
                        node.field("name").asString(),
                        statements,
                        labels,
                        labelEntrypoints(statements),
                    )
                }
            return PythonAstRuntime(moduleName, functions, campaign)
        }

        private fun JsonValue.typeName(): String = getString("type")
        private fun JsonValue.field(name: String): JsonValue = get("fields").get(name)
        private fun JsonValue.children(): Sequence<JsonValue> = sequence {
            var item = child
            while (item != null) {
                yield(item)
                item = item.next
            }
        }
        private fun labelEntrypoints(statements: List<JsonValue>): Map<String, List<JsonValue>> {
            val result = linkedMapOf<String, List<JsonValue>>()
            fun scan(block: List<JsonValue>) {
                block.forEachIndexed { index, statement ->
                    val call = statement.takeIf { it.typeName() == "Expr" }?.field("value")
                    if (call?.typeName() == "Call" && call.field("func").expressionPath() == "label") {
                        call.field("args").children().firstOrNull()?.field("value")?.asString()?.let { label ->
                            result.putIfAbsent(label, block.drop(index + 1))
                        }
                    }
                    when (statement.typeName()) {
                        "If" -> {
                            scan(statement.field("body").children().toList())
                            scan(statement.field("orelse").children().toList())
                        }
                        "For" -> scan(statement.field("body").children().toList())
                    }
                }
            }
            scan(statements)
            return result
        }
        private fun JsonValue.expressionPath(): String? = when (typeName()) {
            "Name" -> field("id").asString()
            "Attribute" -> field("value").expressionPath()?.plus(".")?.plus(field("attr").asString())
            "Call" -> field("func").expressionPath()?.plus("()")
            else -> null
        }
        private fun JsonValue.value(): Any? = when (type()) {
            JsonValue.ValueType.nullValue -> null
            JsonValue.ValueType.booleanValue -> asBoolean()
            JsonValue.ValueType.longValue -> asLong().toInt()
            JsonValue.ValueType.doubleValue -> asDouble()
            else -> asString()
        }

        /** Splits original stage.say payloads whenever their speaker tag changes. */
        fun parseDialogueBlocks(raw: String): List<Dialogue> {
            val tags = Regex("""(?m)^&(\d+)\n""").findAll(raw).toList()
            if (tags.isEmpty()) return listOf(Dialogue(null, raw))
            val result = mutableListOf<Dialogue>()
            val preamble = raw.substring(0, tags.first().range.first).trim()
            if (preamble.isNotEmpty()) result += Dialogue(null, preamble)
            tags.forEachIndexed { index, tag ->
                val end = tags.getOrNull(index + 1)?.range?.first ?: raw.length
                result += Dialogue(tag.groupValues[1], raw.substring(tag.range.last + 1, end).trim())
            }
            return result.filter { it.text.isNotEmpty() }
        }
    }
}
