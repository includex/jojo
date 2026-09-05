package com.jojo.game

import com.badlogic.gdx.utils.JsonValue

/**
 * Stateful interpreter for the recovered Python source AST. The supported
 * subset matches the source corpus' common scenario constructs and stops on
 * stage.say/stage.choice so the LibGDX UI can resume it after player input.
 */
class ScenarioInterpreter internal constructor(
    private val moduleName: String,
    private val functions: Map<String, RuntimeFunction>,
    private val campaign: CampaignState,
) {
    private var externalBattlePresentation = false
    private var stagePresentationSkipped = false

    fun enableExternalBattlePresentation() {
        externalBattlePresentation = true
        stage.enableBattleMovementTimeline()
    }

    /** Opt-in only once the owning screen consumes ScenarioFightCommand. */
    fun enableExternalFightPresentation() {
        delayCoordinator.externalFightPresentation = true
    }
    fun setStagePresentationSkipped(skipped: Boolean) { stagePresentationSkipped = skipped }
    val hasPendingBattleBackgroundLoad: Boolean get() = delayCoordinator.hasPendingBattleBackgroundLoad
    val requestedBattleBackgroundMapIndex: Int get() = delayCoordinator.requestedBattleBackgroundMapIndex

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
        val infantryNearOffsets: Set<Pair<Int, Int>> = ScenarioConditionEvaluator.DEFAULT_INFANTRY_NEAR_OFFSETS,
        val activeCharacterIds: Set<Int> = emptySet(),
        val attributes: Map<Int, Map<Int, Int>> = emptyMap(),
        /** Model.rFlag/eFlag bits needed by authored battle APIs. */
        val enabledFeatures: Int = 0,
    )
    data class UnitReference(val id: Int)
    data class FightReference(val id: Long)

    val stage = ScenarioStage(campaign)
    var state: PlaybackState = PlaybackState.COMPLETE
        internal set
    internal val modalController = ScenarioModalController(
        stage = stage,
        onStateChange = { state = it },
        onResumeExecution = ::runUntilInput,
    )
    internal val dialogueCoordinator = ScenarioDialogueCoordinator(
        stage = stage,
        onStateChange = { state = it },
        onResumeExecution = ::runUntilInput,
        onSetDelayRemainingSeconds = { delayCoordinator.setDelayRemainingSeconds(it) },
    )
    internal val choiceCoordinator = ScenarioChoiceCoordinator(
        onStateChange = { state = it },
    )
    @Suppress("unused")
    private var delayRemainingSeconds: Float = 0f
    internal val delayCoordinator: ScenarioDelayCoordinator = ScenarioDelayCoordinator(
        stage = stage,
        dialogueCoordinator = dialogueCoordinator,
        modalController = modalController,
        getState = { state },
        onSetState = { state = it },
        onResumeExecution = ::runUntilInput,
        getDelayRemainingSeconds = { delayRemainingSeconds },
        onSetDelayRemainingSeconds = { delayRemainingSeconds = it },
    )
    val currentDialogue: Dialogue? get() = dialogueCoordinator.currentDialogue
    val dialogueRevision: Long get() = dialogueCoordinator.dialogueRevision
    val dialogueLifecycleRevision: Long get() = dialogueCoordinator.dialogueLifecycleRevision
    val currentDialogueSourceText: String? get() = dialogueCoordinator.currentDialogueSourceText
    val currentDialogueSide: Int get() = dialogueCoordinator.currentDialogueSide
    val currentDialogueAtTop: Boolean get() = dialogueCoordinator.currentDialogueAtTop
    val currentChoice: Choice? get() = choiceCoordinator.currentChoice
    val currentModalText: String? get() = modalController.currentModalText
    val currentModalKind: ModalKind? get() = modalController.currentModalKind
    val currentModalFixedText: String get() = modalController.currentModalFixedText
    val ambitionFrom: Int get() = modalController.ambitionFrom
    val ambitionTo: Int get() = modalController.ambitionTo
    val ambitionElapsedSeconds: Float get() = modalController.ambitionElapsedSeconds
    val ambitionIndicatorEnabled: Boolean get() = modalController.ambitionIndicatorEnabled
    val selectedChoice: Int get() = choiceCoordinator.selectedChoice
    val isAskChoice: Boolean get() = choiceCoordinator.isAskChoice
    val chosenOption: String? get() = choiceCoordinator.chosenOption
    val choiceTrace: MutableList<ChoiceTrace> get() = choiceCoordinator.choiceTrace
    val randomTrace = mutableListOf<RandomTrace>()
    val globalVariables = mutableMapOf<String, Any?>()
    private val vars = mutableMapOf<Int, Any?>()
    private val gvars = campaign.globalVariables
    private val pvars = mutableMapOf<Int, Any?>()
    internal val callStack = ScenarioCallStack()
    internal val frames get() = callStack.frames
    private val statementExecutor = ScenarioStatementExecutor(moduleName)
    private val randomGenerator = ScenarioRandomGenerator()
    val randomDrawCount: Int get() = randomGenerator.randomDrawCount
    val remainingInjectedRandomCount: Int get() = randomGenerator.remainingInjectedRandomCount
    private var ended = false

    val functionNames: Set<String> get() = functions.keys
    val unhandledCalls = linkedMapOf<String, Int>()
    private var battleContext = BattleScriptContext(round = 1, camp = 1)

    private val callCoordinator = ScenarioCallCoordinator(
        moduleName = moduleName,
        functions = functions,
        campaign = campaign,
        stage = stage,
        modalController = modalController,
        dialogueCoordinator = dialogueCoordinator,
        choiceCoordinator = choiceCoordinator,
        delayCoordinator = delayCoordinator,
        callStack = callStack,
        randomGenerator = randomGenerator,
        vars = vars,
        gvars = gvars,
        pvars = pvars,
        globalVariables = globalVariables,
        randomTrace = randomTrace,
        unhandledCalls = unhandledCalls,
        getBattleContext = { battleContext },
        isExternalBattlePresentation = { externalBattlePresentation },
        isStagePresentationSkipped = { stagePresentationSkipped },
        onEnd = { ended = true },
        onSetState = { state = it },
        resolveStageUnitReference = ::resolveStageUnitReference,
    )

    fun setBattleContext(context: BattleScriptContext) {
        battleContext = context
        context.stagePositions.forEach { (id, position) ->
            stage.seedBattleUnitPosition(id, position.first, position.second)
        }
    }

    fun selectHallBattleCommand() {
        battleContext = battleContext.copy(clickedCharacterId = HALL_BATTLE_COMMAND_ID)
    }

    fun setRandomSequence(values: Iterable<Int>) = randomGenerator.setRandomSequence(values)
    fun stopAfterNextRandomTrace() = randomGenerator.stopAfterRandomTrace(1)
    fun stopAfterRandomTrace(count: Int) = randomGenerator.stopAfterRandomTrace(count)
    fun setScriptVariables(values: Map<Int, Int>) {
        values.forEach { (id, value) -> vars[id] = value }
    }

    internal fun resolveStageUnitReference(value: Int, flags: Int = 0): UnitReference? {
        val indexed = flags and 1 != 0 || gvars[4031].asInt() == 1
        return if (indexed) stage.battleUnitForSlot(value)?.let { UnitReference(it.characterId) }
        else UnitReference(value)
    }

    fun start(functionName: String, label: String? = null) {
        frames.clear()
        dialogueCoordinator.reset()
        ended = false
        delayCoordinator.reset()
        modalController.reset()
        choiceCoordinator.reset()
        randomGenerator.reset()
        randomTrace.clear()
        callCoordinator.pushFunction(functionName, label)
        runUntilInput()
    }

    fun advanceDialogue(deferCloseCallbackFrame: Boolean = false) {
        dialogueCoordinator.advanceDialogue(deferCloseCallbackFrame, state)
    }

    fun presentExternalBattleDialogue(dialogue: Dialogue) {
        dialogueCoordinator.presentExternalBattleDialogue(dialogue, state)
    }

    fun presentExternalBattleInfo(text: String, postTypingDelaySeconds: Float = 1f) {
        check(state == PlaybackState.DELAY) { "외부 전투 안내는 애니메이션 대기에서만 열 수 있습니다." }
        modalController.suspendForInfo(text, ModalKind.INFO, postTypingDelaySeconds)
    }

    fun installHallFixture() = ScenarioFixtureInstaller.installHallFixture(this)
    fun installPalaceFixture() = ScenarioFixtureInstaller.installPalaceFixture(this)
    fun installSectionFixture() = ScenarioFixtureInstaller.installSectionFixture(this)
    fun installOverlayFixture(kind: String) = ScenarioFixtureInstaller.installOverlayFixture(this, kind)

    fun selectPrevious() = choiceCoordinator.selectPrevious()
    fun selectNext() = choiceCoordinator.selectNext()
    fun selectChoice(index: Int) = choiceCoordinator.selectChoice(index)

    fun confirmChoice() {
        choiceCoordinator.confirmChoice(
            currentState = state,
            moduleName = moduleName,
            frames = frames,
            evalBoolean = callCoordinator::evalBoolean,
            assign = callCoordinator::assign,
            onResumeExecution = ::runUntilInput,
        )
    }

    fun update(delta: Float, autoCloseUi: Boolean = true) = delayCoordinator.update(delta, autoCloseUi)
    fun skipDelay() = delayCoordinator.skipDelay()
    fun resumeExternalDelay() = delayCoordinator.resumeExternalDelay()
    fun completeBattleBackgroundLoad() = delayCoordinator.completeBattleBackgroundLoad()

    fun resumeModal() {
        check(state == PlaybackState.MODAL) { "재개할 모달 대기가 없습니다." }
        modalController.resumeModal()
    }

    fun suspendForWinCondition(text: String) = modalController.suspendForWinCondition(text)
    fun completeModalTyping() {
        if (state == PlaybackState.MODAL) modalController.completeModalTyping()
    }

    private fun runUntilInput() {
        statementExecutor.runUntilInput(
            callStack = callStack,
            isEnded = { ended },
            getState = { state },
            setState = { state = it },
            executeStatement = ::executeStatement,
        )
    }

    private fun executeStatement(statement: JsonValue, frame: Frame) {
        statementExecutor.executeStatement(
            statement = statement,
            frame = frame,
            choiceCoordinator = choiceCoordinator,
            eval = callCoordinator::eval,
            evalBoolean = callCoordinator::evalBoolean,
            evalArguments = callCoordinator::evalArguments,
            assign = callCoordinator::assign,
            callStack = callStack,
        )
    }

    companion object {
        private const val HALL_BATTLE_COMMAND_ID = Int.MIN_VALUE

        internal fun toolRandomFromSeed(seed: Double): Pair<Double, Int> =
            ScenarioRandomGenerator.toolRandomFromSeed(seed)

        internal fun modalMayAutoClose(kind: ModalKind?, text: String?, settingEnabled: Boolean): Boolean = when (kind) {
            ModalKind.AMBITION -> true
            ModalKind.INFO, ModalKind.EVENT -> settingEnabled || text.orEmpty().length < 10
            ModalKind.SECTION, ModalKind.MAP_INFO -> settingEnabled
            null -> false
        }

        fun load(moduleName: String, campaign: CampaignState = CampaignState()): ScenarioInterpreter =
            ScenarioLoader.load(moduleName, campaign)

        fun parseDialogueBlocks(raw: String): List<Dialogue> =
            ScenarioDialogueCoordinator.parseDialogueBlocks(raw)
    }
}
