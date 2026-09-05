package com.jojo.game
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

/**
 * Stateful interpreter for the recovered Python source AST. The supported
 * subset matches the source corpus' common scenario constructs and stops on
 * stage.say/stage.choice so the LibGDX UI can resume it after player input.
 */
/**
 * class  `ScenarioInterpreter`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class ScenarioInterpreter internal constructor(
    private val moduleName: String,
    private val functions: Map<String, RuntimeFunction>,
    private val campaign: CampaignState,
) {
    private var externalBattlePresentation = false
    private var stagePresentationSkipped = false

    /**
     * 공개 메서드 `enableExternalBattlePresentation`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun enableExternalBattlePresentation() {
        externalBattlePresentation = true
        stage.enableBattleMovementTimeline()
    }

    /** Opt-in only once the owning screen consumes ScenarioFightCommand. */
    fun enableExternalFightPresentation() {
        delayCoordinator.externalFightPresentation = true
    }

    /**
     * 공개 메서드 `setStagePresentationSkipped`
     *
     * ### 파라미터
    - `skipped` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setStagePresentationSkipped(skipped: Boolean) {
        stagePresentationSkipped = skipped
    }

    val hasPendingBattleBackgroundLoad: Boolean get() = delayCoordinator.hasPendingBattleBackgroundLoad
    val requestedBattleBackgroundMapIndex: Int get() = delayCoordinator.requestedBattleBackgroundMapIndex

    /**
     * enum class  `ModalKind`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class ModalKind { EVENT, INFO, SECTION, MAP_INFO, AMBITION }

    /**
     * data class  `ChoiceTrace`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class ChoiceTrace(
        val module: String,
        val function: String,
        val line: Int,
        val option: Int,
        val optionCount: Int
    )

    /**
     * data class  `RandomTrace`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * data class  `UnitReference`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class UnitReference(val id: Int)

    /**
     * data class  `FightReference`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * 공개 메서드 `setBattleContext`
     *
     * ### 파라미터
    - `context` (`BattleScriptContext`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setBattleContext(context: BattleScriptContext) {
        battleContext = context
        context.stagePositions.forEach { (id, position) ->
            stage.seedBattleUnitPosition(id, position.first, position.second)
        }
    }

    /**
     * 공개 메서드 `selectHallBattleCommand`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectHallBattleCommand() {
        battleContext = battleContext.copy(clickedCharacterId = HALL_BATTLE_COMMAND_ID)
    }

    /**
     * 공개 메서드 `setRandomSequence`
     *
     * ### 파라미터
    - `values` (`Iterable<Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setRandomSequence(values: Iterable<Int>) = randomGenerator.setRandomSequence(values)

    /**
     * 공개 메서드 `stopAfterNextRandomTrace`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun stopAfterNextRandomTrace() = randomGenerator.stopAfterRandomTrace(1)

    /**
     * 공개 메서드 `stopAfterRandomTrace`
     *
     * ### 파라미터
    - `count` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun stopAfterRandomTrace(count: Int) = randomGenerator.stopAfterRandomTrace(count)

    /**
     * 공개 메서드 `setScriptVariables`
     *
     * ### 파라미터
    - `values` (`Map<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setScriptVariables(values: Map<Int, Int>) {
        values.forEach { (id, value) -> vars[id] = value }
    }

    internal fun resolveStageUnitReference(value: Int, flags: Int = 0): UnitReference? {
        val indexed = flags and 1 != 0 || gvars[4031].asInt() == 1
        return if (indexed) stage.battleUnitForSlot(value)?.let { UnitReference(it.characterId) }
        else UnitReference(value)
    }

    /**
     * 공개 메서드 `start`
     *
     * ### 파라미터
    - `functionName` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `label` (`String? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `advanceDialogue`
     *
     * ### 파라미터
    - `deferCloseCallbackFrame` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun advanceDialogue(deferCloseCallbackFrame: Boolean = false) {
        dialogueCoordinator.advanceDialogue(deferCloseCallbackFrame, state)
    }

    /**
     * 공개 메서드 `presentExternalBattleDialogue`
     *
     * ### 파라미터
    - `dialogue` (`Dialogue`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun presentExternalBattleDialogue(dialogue: Dialogue) {
        dialogueCoordinator.presentExternalBattleDialogue(dialogue, state)
    }

    /**
     * 공개 메서드 `presentExternalBattleInfo`
     *
     * ### 파라미터
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `postTypingDelaySeconds` (`Float = 1f`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun presentExternalBattleInfo(text: String, postTypingDelaySeconds: Float = 1f) {
        check(state == PlaybackState.DELAY) { "외부 전투 안내는 애니메이션 대기에서만 열 수 있습니다." }
        modalController.suspendForInfo(text, ModalKind.INFO, postTypingDelaySeconds)
    }

    /**
     * 공개 메서드 `installHallFixture`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun installHallFixture() = ScenarioFixtureInstaller.installHallFixture(this)

    /**
     * 공개 메서드 `installPalaceFixture`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun installPalaceFixture() = ScenarioFixtureInstaller.installPalaceFixture(this)

    /**
     * 공개 메서드 `installSectionFixture`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun installSectionFixture() = ScenarioFixtureInstaller.installSectionFixture(this)

    /**
     * 공개 메서드 `installOverlayFixture`
     *
     * ### 파라미터
    - `kind` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun installOverlayFixture(kind: String) = ScenarioFixtureInstaller.installOverlayFixture(this, kind)

    /**
     * 공개 메서드 `selectPrevious`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectPrevious() = choiceCoordinator.selectPrevious()

    /**
     * 공개 메서드 `selectNext`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectNext() = choiceCoordinator.selectNext()

    /**
     * 공개 메서드 `selectChoice`
     *
     * ### 파라미터
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun selectChoice(index: Int) = choiceCoordinator.selectChoice(index)

    /**
     * 공개 메서드 `confirmChoice`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `update`
     *
     * ### 파라미터
    - `delta` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `autoCloseUi` (`Boolean = true`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun update(delta: Float, autoCloseUi: Boolean = true) = delayCoordinator.update(delta, autoCloseUi)

    /**
     * 공개 메서드 `skipDelay`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun skipDelay() = delayCoordinator.skipDelay()

    /**
     * 공개 메서드 `resumeExternalDelay`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun resumeExternalDelay() = delayCoordinator.resumeExternalDelay()

    /**
     * 공개 메서드 `completeBattleBackgroundLoad`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeBattleBackgroundLoad() = delayCoordinator.completeBattleBackgroundLoad()

    /**
     * 공개 메서드 `resumeModal`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun resumeModal() {
        check(state == PlaybackState.MODAL) { "재개할 모달 대기가 없습니다." }
        modalController.resumeModal()
    }

    /**
     * 공개 메서드 `suspendForWinCondition`
     *
     * ### 파라미터
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun suspendForWinCondition(text: String) = modalController.suspendForWinCondition(text)

    /**
     * 공개 메서드 `completeModalTyping`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

        internal fun modalMayAutoClose(kind: ModalKind?, text: String?, settingEnabled: Boolean): Boolean =
            when (kind) {
                ModalKind.AMBITION -> true
                ModalKind.INFO, ModalKind.EVENT -> settingEnabled || text.orEmpty().length < 10
                ModalKind.SECTION, ModalKind.MAP_INFO -> settingEnabled
                null -> false
            }

        /**
         * 공개 메서드 `load`
         *
         * ### 파라미터
        - `moduleName` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `campaign` (`CampaignState = CampaignState(`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun load(moduleName: String, campaign: CampaignState = CampaignState()): ScenarioInterpreter =
            ScenarioLoader.load(moduleName, campaign)

        /**
         * 공개 메서드 `parseDialogueBlocks`
         *
         * ### 파라미터
        - `raw` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `List<Dialogue>`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun parseDialogueBlocks(raw: String): List<Dialogue> =
            ScenarioDialogueCoordinator.parseDialogueBlocks(raw)
    }
}
