// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*
import com.jojo.game.domain.scenario.*
import com.jojo.game.application.runtime.RuntimeScenarioScene
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*
import com.badlogic.gdx.utils.JsonValue
/** ScenarioInterpreter: AST로 변환된 시나리오 문장을 순서대로 실행하고 대화·선택·전투 대기 상태를 조정한다. */
class ScenarioInterpreter internal constructor(
    /**
     * `moduleName` (String,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val moduleName: String,
    /**
     * `functions` (Map<String, RuntimeFunction>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val functions: Map<String, RuntimeFunction>,
    /**
     * `campaign` (CampaignState,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val campaign: CampaignState,
) {
    /** externalBattlePresentation: 전투 화면이 대화와 이동 연출을 자체적으로 표시하는 실행 모드다. */
    private var externalBattlePresentation = false
    /** stagePresentationSkipped: 무대 연출을 생략하고 논리 상태만 진행해야 하는지 나타낸다. */
    private var stagePresentationSkipped = false
    /** enableExternalBattlePresentation: 전투 장면의 이동·대화 연출 제어권을 외부 화면으로 넘긴다. */
    fun enableExternalBattlePresentation() {
        externalBattlePresentation = true
        stage.enableBattleMovementTimeline()
    }
    /** enableExternalFightPresentation: 전투 연출 완료 신호를 외부 렌더러가 전달하도록 지연 처리를 전환한다. */
    fun enableExternalFightPresentation() {
        delayCoordinator.externalFightPresentation = true
    }
    /** setStagePresentationSkipped: 자동 검증 등에서 화면 연출 없이 무대 명령을 적용할지 설정한다. */
    fun setStagePresentationSkipped(skipped: Boolean) {
        stagePresentationSkipped = skipped
    }
    /**
     * `hasPendingBattleBackgroundLoad` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val hasPendingBattleBackgroundLoad: Boolean get() = delayCoordinator.hasPendingBattleBackgroundLoad
    /**
     * `requestedBattleBackgroundMapIndex` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val requestedBattleBackgroundMapIndex: Int get() = delayCoordinator.requestedBattleBackgroundMapIndex
    /** stage: 시나리오 명령이 갱신하는 배경·유닛·전술 상태의 실제 무대다. */
    val stage = ScenarioStage(campaign)
    /** state: 현재 실행이 다음 사용자 입력, 시간 경과, 모달 해제 중 무엇을 기다리는지 나타낸다. */
    var state: PlaybackState = PlaybackState.COMPLETE
        internal set
    /**
     * `modalController` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal val modalController = ScenarioModalController(
        stage = stage,
        onStateChange = { state = it },
        onResumeExecution = ::runUntilInput,
    )
    /**
     * `dialogueCoordinator` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal val dialogueCoordinator = ScenarioDialogueCoordinator(
        stage = stage,
        onStateChange = { state = it },
        onResumeExecution = ::runUntilInput,
        onSetDelayRemainingSeconds = { delayCoordinator.setDelayRemainingSeconds(it) },
    )
    /**
     * `choiceCoordinator` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal val choiceCoordinator = ScenarioChoiceCoordinator(
        onStateChange = { state = it },
    )
    @Suppress("unused")
    /** delayRemainingSeconds: 스크립트 delay 호출이 아직 대기해야 하는 남은 시간이다. */
    private var delayRemainingSeconds: Float = 0f
    /**
     * `delayCoordinator` (ScenarioDelayCoordinator): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

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
    /**
     * `currentDialogue` (Dialogue? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val currentDialogue: Dialogue? get() = dialogueCoordinator.currentDialogue
    /**
     * `dialogueRevision` (Long get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val dialogueRevision: Long get() = dialogueCoordinator.dialogueRevision
    /**
     * `dialogueLifecycleRevision` (Long get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val dialogueLifecycleRevision: Long get() = dialogueCoordinator.dialogueLifecycleRevision
    /**
     * `currentDialogueSourceText` (String? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val currentDialogueSourceText: String? get() = dialogueCoordinator.currentDialogueSourceText
    /**
     * `currentDialogueSide` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val currentDialogueSide: Int get() = dialogueCoordinator.currentDialogueSide
    /**
     * `currentDialogueAtTop` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val currentDialogueAtTop: Boolean get() = dialogueCoordinator.currentDialogueAtTop
    /**
     * `currentChoice` (Choice? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val currentChoice: Choice? get() = choiceCoordinator.currentChoice
    /**
     * `currentModalText` (String? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val currentModalText: String? get() = modalController.currentModalText
    /**
     * `currentModalKind` (ScenarioModalKind? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val currentModalKind: ScenarioModalKind? get() = modalController.currentModalKind
    /**
     * `currentModalFixedText` (String get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val currentModalFixedText: String get() = modalController.currentModalFixedText
    /**
     * `ambitionFrom` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val ambitionFrom: Int get() = modalController.ambitionFrom
    /**
     * `ambitionTo` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val ambitionTo: Int get() = modalController.ambitionTo
    /**
     * `ambitionElapsedSeconds` (Float get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val ambitionElapsedSeconds: Float get() = modalController.ambitionElapsedSeconds
    /**
     * `ambitionIndicatorEnabled` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val ambitionIndicatorEnabled: Boolean get() = modalController.ambitionIndicatorEnabled
    /**
     * `selectedChoice` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val selectedChoice: Int get() = choiceCoordinator.selectedChoice
    /**
     * `isAskChoice` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val isAskChoice: Boolean get() = choiceCoordinator.isAskChoice
    /**
     * `chosenOption` (String? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val chosenOption: String? get() = choiceCoordinator.chosenOption
    /**
     * `choiceTrace` (MutableList<ScenarioChoiceTrace> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val choiceTrace: MutableList<ScenarioChoiceTrace> get() = choiceCoordinator.choiceTrace
    /** randomTrace: 실행 중 소비한 난수 결과를 재현·검증을 위해 순서대로 기록한다. */
    val randomTrace = mutableListOf<ScenarioRandomTrace>()
    /** globalVariables: 문자열 키를 쓰는 스크립트 전역 변수의 현재 값을 보관한다. */
    val globalVariables = mutableMapOf<String, Any?>()
    /**
     * `vars` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val vars = mutableMapOf<Int, Any?>()
    /**
     * `gvars` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val gvars = campaign.globalVariables
    /**
     * `pvars` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val pvars = mutableMapOf<Int, Any?>()
    /**
     * `callStack` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal val callStack = ScenarioCallStack()
    /**
     * `frames` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal val frames get() = callStack.frames
    /**
     * `statementExecutor` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val statementExecutor = ScenarioStatementExecutor(moduleName)
    /**
     * `randomGenerator` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val randomGenerator = ScenarioRandomGenerator()
    /**
     * `randomDrawCount` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val randomDrawCount: Int get() = randomGenerator.randomDrawCount
    /**
     * `remainingInjectedRandomCount` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val remainingInjectedRandomCount: Int get() = randomGenerator.remainingInjectedRandomCount
    /** ended: 현재 함수 실행이 return 또는 프로그램 끝에 도달했는지 표시한다. */
    private var ended = false
    /**
     * `functionNames` (Set<String> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val functionNames: Set<String> get() = functions.keys
    /** unhandledCalls: 아직 구현하지 않은 원본 스크립트 호출과 발생 횟수를 누적한다. */
    val unhandledCalls = linkedMapOf<String, Int>()
    /**
     * `battleContext` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var battleContext = ScenarioBattleScriptContext(round = 1, camp = 1)
    /**
     * `callCoordinator` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

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
    /** setBattleContext: 전투 시나리오 조건식이 읽을 라운드·진영·배치 정보를 교체한다. */
    fun setBattleContext(context: ScenarioBattleScriptContext) {
        battleContext = context
        context.stagePositions.forEach { (id, position) ->
            stage.seedBattleUnitPosition(id, position.first, position.second)
        }
    }
    /** selectHallBattleCommand: 홀 화면에서 전투 시작 명령을 선택한 것처럼 시나리오 상태를 진행한다. */
    fun selectHallBattleCommand() {
        battleContext = battleContext.copy(clickedCharacterId = HALL_BATTLE_COMMAND_ID)
    }
    /**
     * `setRandomSequence`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setRandomSequence(values: Iterable<Int>) = randomGenerator.setRandomSequence(values)
    /**
     * `stopAfterNextRandomTrace`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun stopAfterNextRandomTrace() = randomGenerator.stopAfterRandomTrace(1)
    /**
     * `stopAfterRandomTrace`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun stopAfterRandomTrace(count: Int) = randomGenerator.stopAfterRandomTrace(count)
    /**
     * `setScriptVariables`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setScriptVariables(values: Map<Int, Int>) {
        values.forEach { (id, value) -> vars[id] = value }
    }
    /**
     * `resolveStageUnitReference`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun resolveStageUnitReference(value: Int, flags: Int = 0): ScenarioUnitReference? {
        val indexed = flags and 1 != 0 || gvars[4031].asInt() == 1
        return if (indexed) stage.battleUnitForSlot(value)?.let { ScenarioUnitReference(it.characterId) }
        else ScenarioUnitReference(value)
    }
    /** start: 지정 함수와 선택 레이블을 호출 스택 첫 프레임으로 올리고 다음 입력 지점까지 실행한다. */
    fun start(functionName: String, label: String? = null) {
        frames.clear()
        dialogueCoordinator.reset()
        ended = false
        delayCoordinator.reset()
        modalController.reset()
        choiceCoordinator.reset()
        randomGenerator.reset(retainTraceStop = true)
        randomTrace.clear()
        callCoordinator.pushFunction(functionName, label)
        runUntilInput()
    }
    /** advanceDialogue: 현재 대사를 닫고 필요하면 닫기 콜백을 한 프레임 뒤에 실행하며 스크립트를 재개한다. */
    fun advanceDialogue(deferCloseCallbackFrame: Boolean = false) {
        dialogueCoordinator.advanceDialogue(deferCloseCallbackFrame, state)
    }
    /**
     * `presentExternalBattleDialogue`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun presentExternalBattleDialogue(dialogue: Dialogue) {
        dialogueCoordinator.presentExternalBattleDialogue(dialogue, state)
    }
    /**
     * `presentExternalBattleInfo`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun presentExternalBattleInfo(text: String, postTypingDelaySeconds: Float = 1f) {
        check(state == PlaybackState.DELAY) { "외부 전투 안내는 애니메이션 대기에서만 열 수 있습니다." }
        modalController.suspendForInfo(text, ScenarioModalKind.INFO, postTypingDelaySeconds)
    }
    /** presentRuntimeScene: 자동 구동기가 만든 배경·유닛·대화·모달 장면을 무대와 재생 상태에 반영한다. */
    fun presentRuntimeScene(scene: RuntimeScenarioScene) {
        callStack.clear()
        dialogueCoordinator.reset()
        choiceCoordinator.reset()
        modalController.reset()
        stage.heads.clear()
        stage.clearUnits()
        scene.backgroundId?.let { stage.apply(ScenarioCommand.LoadBackground(2, it)) }
        scene.units.forEach { unit ->
            stage.apply(ScenarioCommand.ShowUnit(unit.id, unit.x, unit.y, unit.direction))
        }
        stage.finishAnimations()
        scene.dialogueText?.let {
            dialogueCoordinator.presentDialogue(Dialogue("0", it))
            state = PlaybackState.DIALOGUE
        }
        scene.modal?.let { modal ->
            modalController.setModalPresentation(modal.text, scenarioModalKind(modal.kind), modal.seconds)
            state = PlaybackState.MODAL
        }
    }

    /**
     * `scenarioModalKind`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun scenarioModalKind(kind: String): ScenarioModalKind = when (kind) {
        "info" -> ScenarioModalKind.INFO
        "event" -> ScenarioModalKind.EVENT
        "section" -> ScenarioModalKind.SECTION
        "map-info" -> ScenarioModalKind.MAP_INFO
        "ambition" -> ScenarioModalKind.AMBITION
        else -> error("Unknown runtime modal kind: $kind")
    }
    /**
     * `selectPrevious`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun selectPrevious() = choiceCoordinator.selectPrevious()
    /**
     * `selectNext`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun selectNext() = choiceCoordinator.selectNext()
    /**
     * `selectChoice`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun selectChoice(index: Int) = choiceCoordinator.selectChoice(index)
    /** confirmChoice: 현재 선택 항목을 확정해 추적에 기록하고 중단된 시나리오 실행을 이어간다. */
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
     * `update`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun update(delta: Float, autoCloseUi: Boolean = true) = delayCoordinator.update(delta, autoCloseUi)
    /**
     * `skipDelay`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun skipDelay() = delayCoordinator.skipDelay()
    /**
     * `resumeExternalDelay`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun resumeExternalDelay() = delayCoordinator.resumeExternalDelay()
    /**
     * `completeBattleBackgroundLoad`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeBattleBackgroundLoad() = delayCoordinator.completeBattleBackgroundLoad()
    /** resumeModal: 모달 표시가 끝났음을 알리고 대기 중인 스크립트 문장 실행을 다시 시작한다. */
    fun resumeModal() {
        check(state == PlaybackState.MODAL) { "재개할 모달 대기가 없습니다." }
        modalController.resumeModal()
    }
    /**
     * `suspendForWinCondition`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun suspendForWinCondition(text: String) = modalController.suspendForWinCondition(text)
    /**
     * `completeModalTyping`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun completeModalTyping() {
        if (state == PlaybackState.MODAL) modalController.completeModalTyping()
    }
    /** runUntilInput: 대화·선택·시간 지연·모달 같은 다음 중단 지점에 닿을 때까지 문장을 연속 실행한다. */
    private fun runUntilInput() {
        statementExecutor.runUntilInput(
            callStack = callStack,
            isEnded = { ended },
            getState = { state },
            setState = { state = it },
            executeStatement = ::executeStatement,
        )
    }
    /**
     * `executeStatement`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
        /**
         * `HALL_BATTLE_COMMAND_ID` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        private const val HALL_BATTLE_COMMAND_ID = Int.MIN_VALUE
        /**
         * `modalMayAutoClose`: 사용한 상태와 자원을 정리한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        internal fun modalMayAutoClose(kind: ScenarioModalKind?, text: String?, settingEnabled: Boolean): Boolean =
            when (kind) {
                ScenarioModalKind.AMBITION -> true
                ScenarioModalKind.INFO, ScenarioModalKind.EVENT -> settingEnabled || text.orEmpty().length < 10
                ScenarioModalKind.SECTION, ScenarioModalKind.MAP_INFO -> settingEnabled
                null -> false
            }
        /**
         * `load`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun load(moduleName: String, campaign: CampaignState = CampaignState()): ScenarioInterpreter =
            ScenarioLoader.load(moduleName, campaign)
        /**
         * `parseDialogueBlocks`: 입력을 규칙에 따라 계산·변환한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun parseDialogueBlocks(raw: String): List<Dialogue> =
            ScenarioDialogueCoordinator.parseDialogueBlocks(raw)
    }
}
