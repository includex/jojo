// Verification
package com.jojo.game.verification.campaign
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.*
import com.jojo.game.application.runtime.*
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input


/** CampaignE2eTraceConfig: 검증 추적 데이터와 증거를 표현하는 타입이다. */
data class CampaignE2eTraceConfig(
    /** outputPath: 검증 산출물 경로를 담는다. */
    val outputPath: String,
    /** maxSeconds: 검증 대상 목록을 담는다. */
    val maxSeconds: Float = 900f,
    /** inputIntervalSeconds: 검증 대상 목록을 담는다. */
    val inputIntervalSeconds: Float = .12f,
    /** stopAt: 기존 Title -> R_00 -> S_00 -> R_01 계약을 기본 경로로 유지한다. */
    val stopAt: CampaignE2eStopPoint = CampaignE2eStopPoint(),
    /** requireYingchuanBootstrapContract: 확장 데이터 기반 경로는 자체 검증 계약을 제공한 뒤 기본 경로에서 제외된다. */
    val requireYingchuanBootstrapContract: Boolean = true,
)


/** CampaignE2eStopPoint: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
data class CampaignE2eStopPoint(val module: String = "R_01", val sceneIndex: Int = 1)

/** CampaignE2eMoveInput: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class CampaignE2eMoveInput(
    /** sourceScreenX: 검증 화면 좌표를 담는다. */
    val sourceScreenX: Int,
    /** sourceScreenY: 검증 화면 좌표를 담는다. */
    val sourceScreenY: Int,
    /** destinationScreenX: 검증 화면 좌표를 담는다. */
    val destinationScreenX: Int,
    /** destinationScreenY: 검증 화면 좌표를 담는다. */
    val destinationScreenY: Int,
)

/** CampaignE2eAttackInput: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class CampaignE2eAttackInput(
    /** commandScreenX: 검증 화면 좌표를 담는다. */
    val commandScreenX: Int,
    /** commandScreenY: 검증 화면 좌표를 담는다. */
    val commandScreenY: Int,
    /** targetScreenX: 검증 화면 좌표를 담는다. */
    val targetScreenX: Int,
    /** targetScreenY: 검증 화면 좌표를 담는다. */
    val targetScreenY: Int,
    /** targetUnitId: 안정적인 BattleUnit 식별자이며 화면 좌표는 다시 계산한 표시용 값이다. */
    val targetUnitId: String = "",
)

/** productionLiveAttackInput: 열려 있는 Attack 선택기는 최신 적중 영역 사전 검사와 투영값을 사용해야 한다. */
internal fun productionLiveAttackInput(
    _openedInput: CampaignE2eAttackInput?,
    liveInput: CampaignE2eAttackInput?,
): CampaignE2eAttackInput? = liveInput?.takeIf { it.targetUnitId.isNotEmpty() }

/** CampaignE2eMagicInput: MagickList 행을 선택·결정·대상 지정하는 세 번의 실제 터치 입력이다. */
internal data class CampaignE2eMagicInput(
    /** commandScreenX: 검증 화면 좌표를 담는다. */
    val commandScreenX: Int,
    /** commandScreenY: 검증 화면 좌표를 담는다. */
    val commandScreenY: Int,
    /** rowScreenX: 검증 화면 좌표를 담는다. */
    val rowScreenX: Int,
    /** rowScreenY: 검증 화면 좌표를 담는다. */
    val rowScreenY: Int,
    /** targetScreenX: 검증 화면 좌표를 담는다. */
    val targetScreenX: Int,
    /** targetScreenY: 검증 화면 좌표를 담는다. */
    val targetScreenY: Int,
)

/** CampaignE2eMagicOption: S57 입력기가 사용하는 읽기 전용·UI 비의존 전략 정보이다. */
internal data class CampaignE2eMagicOption(
    /** id: 검증 대상 식별자를 담는다. */
    val id: Int,
    /** target: 검증 대상 정보를 담는다. */
    val target: Int,
    /** cost: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val cost: Int,
    /** power: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val power: Int,
    /** category: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val category: Int,
    /** allScreen: 화면 관찰 상태를 담는다. */
    val allScreen: Boolean,
    /** offsets: 검증 대상 목록을 담는다. */
    val offsets: Set<Pair<Int, Int>>,
)

/** CampaignE2eMagicTarget: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class CampaignE2eMagicTarget(val id: String, val x: Int, val y: Int)

/** CampaignE2eGuidedMagicPlan: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class CampaignE2eGuidedMagicPlan(val magicId: Int, val targetId: String)

/** s57GuidedOffensiveMagicPlan: S57 운영 경로는 원본 방·소모 경로가 전투를 허용할 때만 물리 명령보다 합법적인 공격 MagickList 선택을 우선한다. 순수 투영이므로 Battle·Campaign·시나리오 AST를 변경하지 않는다. */
internal fun s57GuidedOffensiveMagicPlan(
    scenario: String,
    guidedAuthoredRoute: Boolean,
    holdFire: Boolean,
    firstRoomLeaderVisible: Boolean,
    casterCharacterId: Int?,
    casterX: Int,
    casterY: Int,
    magicPoints: Int,
    options: List<CampaignE2eMagicOption>,
    visibleEnemies: List<CampaignE2eMagicTarget>,
): CampaignE2eGuidedMagicPlan? {
    // S57 명단에는 조조의 적 대상 회오리(10, 비용 6·위력 50)라는 공격 전략만 있다.
    // 이는 일반 AI 정책이 아닌 S57 전용의 실제 경로 보조이다.
    // 첫 방의 장수들이 전투 경로를 결정하므로, 회오리로 경비병을 공격해 조조가 후퇴 지점에 묶이면
    // 시나리오가 진행되지 않는다. 따라서 이는 대상 점수가 아닌 엄격한 경로 관문이다.
    if (scenario != "S_57" || !guidedAuthoredRoute || holdFire || firstRoomLeaderVisible || casterCharacterId != 0) return null
    return options.asSequence()
        .filter { option -> option.id == 10 && option.target == 0 && option.cost == 6 && option.power == 50 && magicPoints >= option.cost }
        .flatMap { option ->
            visibleEnemies.asSequence()
                .filter { target ->
                    option.category in setOf(1, 29) || option.allScreen ||
                            (target.x - casterX to target.y - casterY) in option.offsets
                }
                .map { target -> CampaignE2eGuidedMagicPlan(option.id, target.id) }
        }
        .firstOrNull()
}

/** CampaignE2eInputRecord: 설치된 운영 InputProcessor로 전달한 한 번의 시도이다. 기존 소비자를 위해 inputs 배열을 유지하며, 이 기록은 거부된 시도와 전달 직전·직후 관찰 상태를 보존하는 감사 증거이다. */
internal data class CampaignE2eInputRecord(
    /** event: 검증 이벤트 목록을 담는다. */
    val event: String,
    /** accepted: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val accepted: Boolean,
    /** before: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val before: String,
    /** after: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val after: String,
)

/** CampaignE2eBattleState: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
internal data class CampaignE2eBattleState(
    /** scenario: 시나리오 식별자를 담는다. */
    val scenario: String,
    /** playback: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val playback: PlaybackState,
    /** outcome: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val outcome: BattleOutcome?,
    /** initialScene1Started: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val initialScene1Started: Boolean,
    /** resultScene1Started: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val resultScene1Started: Boolean,
    /** scene2Started: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val scene2Started: Boolean,
    /** rewardOpen: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val rewardOpen: Boolean,
    /** winConditionsOpen: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val winConditionsOpen: Boolean,
    /** savePromptOpen: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val savePromptOpen: Boolean,
    /** losePromptOpen: Lose.scene은 원본과 같은 지연 후 실제 포인터 응답을 받는다. */
    val losePromptOpen: Boolean,
    /** loseTitleScreenX: Physical-screen centre of Lose.scene's "예" answer (return to title). */
    val loseTitleScreenX: Int,
    /** loseTitleScreenY: 검증 화면 좌표를 담는다. */
    val loseTitleScreenY: Int,
    /** playerMoveCommitted: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val playerMoveCommitted: Boolean,
    /** campaignStage: 시나리오 진행 상태를 담는다. */
    val campaignStage: Int,
    /** round: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val round: Int,
    /** activeFaction: 전투 무장 상태를 담는다. */
    val activeFaction: Faction,
    /** turnPhase: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val turnPhase: String,
    /** battleMenuOpen: 전투 검증 상태를 담는다. */
    val battleMenuOpen: Boolean,
    /** battleCommandOpen: 전투 검증 상태를 담는다. */
    val battleCommandOpen: Boolean,
    /** battleTargetSelectionOpen: 전투 대상 정보를 담는다. */
    val battleTargetSelectionOpen: Boolean,
    /** selectedUnit: 전투 무장 상태를 담는다. */
    val selectedUnit: Boolean,
    /** manualMoveInput: 검증 입력 정보를 담는다. */
    val manualMoveInput: CampaignE2eMoveInput?,
    /** manualAttackInput: 검증 입력 정보를 담는다. */
    val manualAttackInput: CampaignE2eAttackInput?,
    /** magickListOpen: MagickListLayer가 표시 중이며 다음 포인터 입력을 담당한다. */
    val magickListOpen: Boolean,
    /** magicTargetSelection: 선택된 MagickList 행이 지도 대상 선택 모드를 열었다. */
    val magicTargetSelection: Boolean,
    /** manualMagicInput: 사거리가 유효한 S57 공격 전략 대상이 있을 때만 제공한다. */
    val manualMagicInput: CampaignE2eMagicInput?,
    /** commandWaitScreenX: 검증 화면 좌표를 담는다. */
    val commandWaitScreenX: Int,
    /** commandWaitScreenY: 검증 화면 좌표를 담는다. */
    val commandWaitScreenY: Int,
    /** menuEndRoundScreenX: 검증 화면 좌표를 담는다. */
    val menuEndRoundScreenX: Int,
    /** menuEndRoundScreenY: 검증 화면 좌표를 담는다. */
    val menuEndRoundScreenY: Int,
    /** battleMenuButtonScreenX: 검증 화면 좌표를 담는다. */
    val battleMenuButtonScreenX: Int,
    /** battleMenuButtonScreenY: 검증 화면 좌표를 담는다. */
    val battleMenuButtonScreenY: Int,
    /** autoBattleToggleScreenX: 검증 화면 좌표를 담는다. */
    val autoBattleToggleScreenX: Int,
    /** autoBattleToggleScreenY: 검증 화면 좌표를 담는다. */
    val autoBattleToggleScreenY: Int,
    /** autoBattleConfirmScreenX: 검증 화면 좌표를 담는다. */
    val autoBattleConfirmScreenX: Int,
    /** autoBattleConfirmScreenY: 검증 화면 좌표를 담는다. */
    val autoBattleConfirmScreenY: Int,
    /** manualMoveDebug: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val manualMoveDebug: String,
    /** autoBattleOverlay: 전투 검증 상태를 담는다. */
    val autoBattleOverlay: String,
    /** autoBattleChecked: 전투 검증 상태를 담는다. */
    val autoBattleChecked: Boolean,
    /** collocation: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val collocation: Boolean,
    /** committedPlayerMove: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val committedPlayerMove: String?,
    /** selectedChoice: 해당 검증 조건의 현재 여부를 나타낸다. */
    val selectedChoice: Int,
    /** guidedAuthoredRoute: 검증 실행 계획을 담는다. */
    val guidedAuthoredRoute: Boolean,
    /** authoredRouteHoldFire: 원본 S57 방·함정 경로는 실제 CommandLayer WAIT를 요구한다. */
    val authoredRouteHoldFire: Boolean = false,
    /** s01EligibleMineActionRemaining: S01은 행동 가능한 모든 Mine이 행동하기 전까지 라운드 종료 UI를 열면 안 된다. */
    val s01EligibleMineActionRemaining: Boolean = false,
)

/** productionLossRecoveryPointer: 전술 키는 Lose.scene에 도달하지 않으며 해당 MsgBox는 지연 후 포인터만 받는다. */
internal fun productionLossRecoveryPointer(state: CampaignE2eBattleState): Pair<Int, Int>? =
    (state.loseTitleScreenX to state.loseTitleScreenY).takeIf { state.losePromptOpen }

/** S01WithdrawalChoiceAction: S01 원본 ChoiceLayer에서 철수 분기는 0번 행에 배치된다. */
internal enum class S01WithdrawalChoiceAction { PREVIOUS, CONFIRM }

/** s01WithdrawalChoiceAction: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
internal fun s01WithdrawalChoiceAction(selectedChoice: Int): S01WithdrawalChoiceAction =
    if (selectedChoice > 0) S01WithdrawalChoiceAction.PREVIOUS else S01WithdrawalChoiceAction.CONFIRM

/** S57AuthoredRouteSignal: S_57 scene1에서 추출한 읽기 전용 정책이다. 입력기는 일반 선택·이동·CommandLayer 포인터 이벤트만 보내며, 이 정책은 조조를 두 번째 방 영역으로 이동시킨 뒤 적 턴에서 원본에 필요한 소모가 생길 때까지 WAIT해야 하는 시점을 결정한다. */
internal data class S57AuthoredRouteSignal(
    /** combatTargetIds: 검증 대상 목록을 담는다. */
    val combatTargetIds: Set<Int> = emptySet(),
    /** gateTarget: 값이 있으면 관문 경로를 활성화하며, 이동은 이 점이 아닌 원본 영역을 사용한다. */
    val gateTarget: Pair<Int, Int>? = null,
    /** waitForAttrition: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val waitForAttrition: Boolean = false,
) {
    val holdFire: Boolean get() = gateTarget != null || waitForAttrition
}

/** s57AuthoredRouteSignal: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
internal fun s57AuthoredRouteSignal(
    scenario: String,
    visibleEnemySourceIds: Collection<Int>,
    mineMasterInSecondRoom: Boolean,
    visiblePlayerCount: Int,
): S57AuthoredRouteSignal {
    if (scenario != "S_57") return S57AuthoredRouteSignal()
    val visible = visibleEnemySourceIds.toSet()
    val firstRoom = visible intersect setOf(165, 162, 169)
    if (firstRoom.isNotEmpty()) {
        // 원본의 방 정리 콜백이 아직 실행되지 않았으므로, 원본 0번이 유일한 Mine 생존자여도
        // 첫 방 경로를 유지해야 한다. 세 장수가 모두 사라지기 전에는 두 번째 방 관문에 들어갈 수 없다.
        return S57AuthoredRouteSignal(combatTargetIds = firstRoom)
    }
    val sunFamily = visible intersect setOf(166, 167, 168)
    // 공개 콜백은 반환 전에 세 명 전체를 등록하므로, 불완전하거나 늦은 전투 스냅샷으로 관문을 추론하지 않는다.
    if (sunFamily.size < 3) return S57AuthoredRouteSignal()
    // scene1 775–867행은 Mine 하나가 x=2..16/y=11..23 안에 있고 totalUnit(MINE)이 둘 미만이 되어야 한다.
    // 0번 유닛은 그곳에 유지하고, 일반 적 턴이 패배를 만들게 하며 상태·HP·변수는 직접 쓰지 않는다.
    return if (!mineMasterInSecondRoom) {
        S57AuthoredRouteSignal(combatTargetIds = sunFamily, gateTarget = 16 to 19)
    } else {
        S57AuthoredRouteSignal(
            combatTargetIds = sunFamily,
            waitForAttrition = visiblePlayerCount >= 2,
        )
    }
}

/** productionTacticalInputReady: 원본 initial scene1/startOper 인계가 끝난 경우에만 전술 입력을 노출한다. */
internal fun productionTacticalInputReady(
    initialScene1Started: Boolean,
    playback: PlaybackState,
    phase: String,
): Boolean = initialScene1Started && playback == PlaybackState.COMPLETE &&
        phase == "PLAYER_INPUT"

/** productionTacticalInputReady: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
internal fun productionTacticalInputReady(
    initialScene1Started: Boolean,
    playback: PlaybackState,
    phase: Any,
): Boolean = productionTacticalInputReady(initialScene1Started, playback, phase.toString())

/** productionManualUnitEligible: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
internal fun productionManualUnitEligible(statuses: Collection<BattleStatus>): Boolean =
    BattleStatus.PARALYSIS !in statuses && BattleStatus.CONFUSION !in statuses

/** productionManualUnitEligible: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
internal fun productionManualUnitEligible(statuses: Map<BattleStatus, Int>): Boolean =
    productionManualUnitEligible(statuses.keys)

/** productionEndRoundAllowed: S01은 행동 가능한 모든 Mine 슬롯이 행동한 뒤에만 실제 라운드 종료 메뉴를 연다. */
internal fun productionEndRoundAllowed(scenario: String, s01EligibleMineActionRemaining: Boolean): Boolean =
    scenario != "S_01" || !s01EligibleMineActionRemaining

/** productionManualMoveAllowed: 단독 추적은 보통 전투 위임 전에 수동 행동 횟수를 0으로 설정한다. S01은 의도적으로 수동 경로를 사용하므로 모든 Mine이 일반 UI 턴을 수행하고, 턴별 계획기는 이 공통 제한을 우회한다. */
internal fun productionManualMoveAllowed(
    scenario: String,
    guidedAuthoredRoute: Boolean,
    playerMoveCommitted: Boolean,
    manualMoveAttempts: Int,
    manualMoveAttemptLimit: Int?,
): Boolean {
    val manualRoute = scenario == "S_01" || guidedAuthoredRoute || !playerMoveCommitted
    val quotaAvailable = scenario == "S_01" || guidedAuthoredRoute ||
            manualMoveAttemptLimit == null || manualMoveAttempts < manualMoveAttemptLimit
    return manualRoute && quotaAvailable
}

/** ProductionAutoBattlePromptAction: MenuLayer.HHJS 이후 운영 MsgBox4 정책이다. S_52·S_57은 원본 관문 칸을 방문해야 하므로 수동 조작을 유지한다. 실제 UI 경로는 저장된 토글을 끈 상태로 확인하고, 일반 단독 전투는 표시된 토글을 먼저 확인한 뒤 확인하여 원본 위임 하니스와 맞춘다. 안내된 확인은 전투 메뉴 버튼으로 빠지면 안 되며, MsgBox4는 패널 밖 터치를 취소로 처리한다. */
internal enum class ProductionAutoBattlePromptAction { TOGGLE, CONFIRM }

/** productionAutoBattlePromptAction: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
internal fun productionAutoBattlePromptAction(
    guidedAuthoredRoute: Boolean,
    checked: Boolean,
): ProductionAutoBattlePromptAction =
    if (checked == guidedAuthoredRoute) ProductionAutoBattlePromptAction.TOGGLE
    else ProductionAutoBattlePromptAction.CONFIRM

/** productionAutoBattlePromptActionForScenario: S01은 실제 라운드 종료 응답이 필요하지만 위임 조작에는 들어가면 안 된다. source 0은 다음 일반 PLAYER_INPUT 턴에 남아 있고 FRIEND 진영은 자체 엔진 AI 턴을 유지한다. 다른 시나리오는 기존 원본 하니스 토글 정책을 따른다. */
internal fun productionAutoBattlePromptActionForScenario(
    scenario: String,
    guidedAuthoredRoute: Boolean,
    checked: Boolean,
): ProductionAutoBattlePromptAction =
    if (scenario == "S_01") {
        if (checked) ProductionAutoBattlePromptAction.TOGGLE else ProductionAutoBattlePromptAction.CONFIRM
    } else productionAutoBattlePromptAction(guidedAuthoredRoute, checked)

/** CampaignBattlePreparationAction: R_01 StartBattleScreen은 4~7명을 허용한다. 원본 scene6에서 첫 캠페인 파티가 완성되고 scene7에 7개의 Mine 슬롯이 노출되므로 운영 경로는 일반 확인을 누르기 전에 슬롯을 의도적으로 채운다. 다른 전투는 UI의 일반 최소 인원 확인을 유지한다. */
internal enum class CampaignBattlePreparationAction { START, NEXT_UNIT, TOGGLE_UNIT }

/** campaignBattlePreparationAction: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
internal fun campaignBattlePreparationAction(
    returnScenario: String,
    sourceScenario: String,
    selectedCount: Int,
    maximum: Int,
    cursorSelected: Boolean,
    canStart: Boolean,
): CampaignBattlePreparationAction = when {
    returnScenario == "R_01" && sourceScenario == "S_01" && selectedCount < maximum ->
        if (cursorSelected) CampaignBattlePreparationAction.NEXT_UNIT else CampaignBattlePreparationAction.TOGGLE_UNIT

    canStart -> CampaignBattlePreparationAction.START
    cursorSelected -> CampaignBattlePreparationAction.NEXT_UNIT
    else -> CampaignBattlePreparationAction.TOGGLE_UNIT
}

/** CampaignE2eDriver: 설치된 InputProcessor를 통해 운영 화면을 구동한다. 화면 상태만 관찰하며 AST 변수·전투 문맥·캡처 화면·단축 지연을 설치하지 않는다. */
internal class CampaignE2eDriver(private val config: CampaignE2eTraceConfig) {
    /** route: 검증 실행 계획을 담는다. */
    private val route = mutableListOf<String>()
    /** stopEvaluator: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private val stopEvaluator = CampaignE2eStopEvaluator(config.stopAt)

    /** elapsed: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var elapsed = 0f
    /** nextInputAt: 검증 입력 정보를 담는다. */
    private var nextInputAt = .25f
    /** lastScreenName: 화면 관찰 상태를 담는다. */
    private var lastScreenName: String? = null
    /** lastScreen: 화면 관찰 상태를 담는다. */
    private var lastScreen: RuntimeScreenProbe = OtherRuntimeProbe("null")
    /** inputReporter: 검증 입력 정보를 담는다. */
    private val inputReporter = CampaignE2eInputReporter {
        CampaignE2eScreenObservation.of(lastScreen)
    }
    /** titleClicked: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var titleClicked = false
    /** sawInitialScene1: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var sawInitialScene1 = false
    /** sawResultScene1: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var sawResultScene1 = false
    /** sawScene2: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var sawScene2 = false
    /** sawSavePrompt: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var sawSavePrompt = false
    /** observedScenarioScenes: 검증 대상 목록을 담는다. */
    private val observedScenarioScenes = mutableSetOf<String>()
    /** playerMoveBeforeScene1: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var playerMoveBeforeScene1 = false
    /** committedPlayerMove: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var committedPlayerMove: String? = null
    /** finished: 해당 검증 조건의 현재 여부를 나타낸다. */
    private var finished = false
    /** lastScenarioState: 시나리오 진행 상태를 담는다. */
    private var lastScenarioState: String? = null
    /** lastBattleState: 전투 검증 상태를 담는다. */
    private var lastBattleState: String? = null
    /** pendingScenarioStarts: 검증 대상 목록을 담는다. */
    private val pendingScenarioStarts = mutableListOf<Pair<String, Int>>()
    /** battleInputDriver: 전투 검증 상태를 담는다. */
    private val battleInputDriver = ProductionBattleInputDriver(
        inputIntervalSeconds = config.inputIntervalSeconds,
        onInput = inputReporter::recordAcceptedInput,
        onInputRecord = inputReporter::recordInputAttempt,
    )
    /** observedInitialBattleScenes: 검증 대상 목록을 담는다. */
    private val observedInitialBattleScenes = mutableSetOf<String>()
    /** observedResultBattleScenes: 검증 대상 목록을 담는다. */
    private val observedResultBattleScenes = mutableSetOf<String>()
    /** observedBattleScene2: 전투 검증 상태를 담는다. */
    private val observedBattleScene2 = mutableSetOf<String>()
    /** observedSavePrompts: 검증 대상 목록을 담는다. */
    private val observedSavePrompts = mutableSetOf<String>()
    /** battlePreparations: 검증 대상 목록을 담는다. */
    private val battlePreparations = mutableListOf<String>()
    /** campaignStages: 검증 대상 목록을 담는다. */
    private val campaignStages = mutableListOf<Int>()
    /** hallBattleCommands: 검증 대상 목록을 담는다. */
    private val hallBattleCommands = mutableSetOf<String>()
    /** authoredMechanicRoutes: 검증 대상 목록을 담는다. */
    private val authoredMechanicRoutes = mutableMapOf<String, AuthoredMechanicRouteTracker>()
    /** sawR01DepartureDialogue: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var sawR01DepartureDialogue = false

    /** observeStage: 시나리오 단계 진입을 관찰한다. */
    private fun observeStage(stage: Int) {
        if (campaignStages.lastOrNull() != stage) campaignStages += stage
    }


    /** scenarioStarted: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun scenarioStarted(module: String, index: Int) {
        pendingScenarioStarts += module to index
    }


    /** update: 검증 상태를 입력에 맞게 갱신한다. */
    fun update(delta: Float, current: RuntimeScreenProbe) {
        if (finished) return
        elapsed += delta
        check(elapsed <= config.maxSeconds) { "campaign E2E timed out: ${route.joinToString(" -> ")}" }
        if (current.screenName != lastScreenName && lastScreen is ScenarioRuntimeProbe) drainScenarioStarts()
        if (current.screenName != lastScreenName) {
            lastScreen = current
            lastScreenName = current.screenName
            when (current) {
                is TitleRuntimeProbe -> route += "TitleScreen"
                is ScenarioRuntimeProbe -> route += "ScenarioScreen:${current.module}"
                is BattlePreparationRuntimeProbe -> current.let { state ->
                    route += "BattlePreparationScreen:${state.returnScenario}->${state.sourceScenario}"
                }
                is BattleRuntimeScreenProbe -> route += "BattleScreen:${current.scenario}"
                is OtherRuntimeProbe -> route += current.screenName
            }
            nextInputAt = elapsed + .2f
            Gdx.app.log("JojoGame", "CAMPAIGN_E2E_SCREEN: ${route.last()}")
        }
        if (current is ScenarioRuntimeProbe) drainScenarioStarts()

        when (current) {
            is TitleRuntimeProbe -> if (!titleClicked && elapsed >= nextInputAt) {
                // 논리 창 좌표계에서 TitleInteraction.NEW_GAME의 중심점이다.
                inputReporter.pointer(1097, 688 - 500, "TitleScreen:new-game-click")
                titleClicked = true
            }

            is ScenarioRuntimeProbe -> driveScenario(current)
            is BattlePreparationRuntimeProbe -> driveBattlePreparation(current)
            is BattleRuntimeScreenProbe -> driveBattle(delta, current)
            is OtherRuntimeProbe -> Unit
        }
    }

    /** drainScenarioStarts: 대기 중인 시나리오 시작을 모두 처리한다. */
    private fun drainScenarioStarts() {
        pendingScenarioStarts.forEach { (module, index) ->
            val started = "$module:scene$index"
            if (observedScenarioScenes.add(started)) {
                route += "ScenarioScreen:$started"
                Gdx.app.log("JojoGame", "CAMPAIGN_E2E_SCENE: $started")
            }
        }
        pendingScenarioStarts.clear()
    }

    /** driveScenario: 시나리오 입력과 재생을 진행한다. */
    private fun driveScenario(state: ScenarioRuntimeProbe) {
        val scene = "${state.module}:scene${state.sceneIndex}"
        // 원본 장면은 한 번의 렌더 안에서 시작·동기 완료·다음 화면 전환까지 할 수 있다.
        // 인위적인 한 프레임 대기 없이 실제 호출을 관찰하도록 실행 화면의 추가 전용 시작 이력을 소비한다.
        state.startedScenes.forEach { index ->
            val started = "${state.module}:scene$index"
            if (observedScenarioScenes.add(started)) {
                route += "ScenarioScreen:$started"
                Gdx.app.log("JojoGame", "CAMPAIGN_E2E_SCENE: $started")
            }
        }
        val expectedStage = state.module.removePrefix("R_").toIntOrNull()?.times(2)
        check(expectedStage == null || state.campaignStage == expectedStage) {
            "${state.module} raw campaign stage=${state.campaignStage}"
        }
        observeStage(state.campaignStage)
        val stateLabel = "$scene:${state.playback}"
        if (stateLabel != lastScenarioState) {
            lastScenarioState = stateLabel
            Gdx.app.log("JojoGame", "CAMPAIGN_E2E_STATE: $stateLabel")
        }
        if (state.module == "R_01" && state.sceneIndex == 8 && state.playback == PlaybackState.DIALOGUE) {
            check(!state.menuVisible && state.hallBattleScenePending) {
                "R_01 scene8 must hide HallLayer while its departure SayLayer is open"
            }
            check(state.dialogueText == "출발.") {
                "R_01 scene8 departure dialogue mismatch: ${state.dialogueText}"
            }
            sawR01DepartureDialogue = true
        }
        when (stopEvaluator.evaluate(state.module, state.sceneIndex, state.campaignStage)) {
            CampaignE2eStopEvaluator.Decision.REACHED ->
                return finish(state.module, state.sceneIndex, forwardOvershoot = false)
            CampaignE2eStopEvaluator.Decision.FORWARD_OVERSHOOT ->
                return finish(state.module, state.sceneIndex, forwardOvershoot = true)
            CampaignE2eStopEvaluator.Decision.CONTINUE -> Unit
        }
        if (elapsed < nextInputAt) return
        when (state.playback) {
            PlaybackState.DIALOGUE -> inputReporter.key(Input.Keys.ENTER, "${state.module}:dialogue")
            PlaybackState.MODAL -> inputReporter.key(Input.Keys.ENTER, "${state.module}:modal")
            PlaybackState.CHOICE -> {
                val desired = state.options.indexOfFirst { "게임 시작" in it }.takeIf { it >= 0 } ?: 0
                if (state.selectedChoice != desired) inputReporter.key(Input.Keys.DOWN, "${state.module}:choice-next")
                else inputReporter.key(Input.Keys.ENTER, "${state.module}:choice-confirm")
            }

            PlaybackState.COMPLETE -> if (state.menuVisible && hallBattleCommands.add(state.module)) {
                check(
                    state.battleButtonScreenX in 0 until Gdx.graphics.width &&
                            state.battleButtonScreenY in 0 until Gdx.graphics.height
                ) { "${state.module} projected Hall battle command is outside the viewport" }
                route += "ScenarioScreen:${state.module}:hall-battle-button"
                inputReporter.pointer(
                    state.battleButtonScreenX,
                    state.battleButtonScreenY,
                    "${state.module}:hall-battle-button",
                )
            }

            PlaybackState.DELAY -> Unit
        }
        nextInputAt = elapsed + config.inputIntervalSeconds
    }

    /** driveBattlePreparation: 전투 준비 입력을 진행한다. */
    private fun driveBattlePreparation(state: BattlePreparationRuntimeProbe) {
        if (state.returnScenario == "R_01" && state.sourceScenario == "S_01") {
            check(sawR01DepartureDialogue) {
                "S_01 preparation was reached without the authored R_01 scene8 departure dialogue"
            }
        }
        val expectedStage = state.sourceScenario.removePrefix("S_").toIntOrNull()?.let { it * 2 + 1 }
        check(expectedStage == null || state.campaignStage == expectedStage) {
            "${state.sourceScenario} preparation raw campaign stage=${state.campaignStage}"
        }
        observeStage(state.campaignStage)
        val evidence =
            "${state.returnScenario}->${state.sourceScenario}:${state.selectedCount}/${state.minimum}-${state.maximum}"
        if (state.canStart && battlePreparations.lastOrNull() != evidence) battlePreparations += evidence
        if (elapsed < nextInputAt) return
        when (campaignBattlePreparationAction(
            state.returnScenario,
            state.sourceScenario,
            state.selectedCount,
            state.maximum,
            state.cursorSelected,
            state.canStart,
        )) {
            CampaignBattlePreparationAction.START -> inputReporter.key(Input.Keys.ENTER, "${state.sourceScenario}:preparation-start")
            CampaignBattlePreparationAction.NEXT_UNIT -> inputReporter.key(
                Input.Keys.RIGHT,
                "${state.sourceScenario}:preparation-next-unit"
            )

            CampaignBattlePreparationAction.TOGGLE_UNIT -> inputReporter.key(
                Input.Keys.SPACE,
                "${state.sourceScenario}:preparation-select-unit"
            )
        }
        nextInputAt = elapsed + config.inputIntervalSeconds
    }

    /** driveBattle: 전투 입력과 턴 진행을 수행한다. */
    private fun driveBattle(delta: Float, screen: BattleRuntimeScreenProbe) {
        val tracker = authoredMechanicRoutes.getOrPut(screen.scenario) { AuthoredMechanicRouteTracker(screen.scenario) }
        val state = CampaignE2eBattleVerificationProjection().computeState(CampaignE2eProjectionContext(screen, tracker))
        val expectedStage = state.scenario.removePrefix("S_").toIntOrNull()?.let { it * 2 + 1 }
        check(expectedStage == null || state.campaignStage == expectedStage) {
            "${state.scenario} raw campaign stage=${state.campaignStage}"
        }
        observeStage(state.campaignStage)
        val stateLabel = "round=${state.round}:camp=${state.activeFaction}:phase=${state.turnPhase}:" +
                "playback=${state.playback}:collocation=${state.collocation}:outcome=${state.outcome}"
        if (stateLabel != lastBattleState) {
            lastBattleState = stateLabel
            Gdx.app.log("JojoGame", "CAMPAIGN_E2E_BATTLE: $stateLabel")
        }
        if (state.initialScene1Started && observedInitialBattleScenes.add(state.scenario)) {
            // 작성된 최초 scene1이 startOper를 소유하므로, 첫 번째로 허용되는 전술 이동보다 먼저 실행되어야 한다.
            check(!state.playerMoveCommitted) { "${state.scenario} accepted a player move before initial startOper" }
            playerMoveBeforeScene1 = false
            sawInitialScene1 = true
            route += "BattleScreen:${state.scenario}:scene1"
        }
        if (state.playerMoveCommitted && committedPlayerMove == null) {
            committedPlayerMove = checkNotNull(state.committedPlayerMove)
        }
        if (state.resultScene1Started && observedResultBattleScenes.add(state.scenario)) {
            sawResultScene1 = true
            route += "BattleScreen:${state.scenario}:result-scene1"
        }
        if (state.scene2Started && observedBattleScene2.add(state.scenario)) {
            sawScene2 = true
            route += "BattleScreen:${state.scenario}:scene2"
        }
        if (state.savePromptOpen && observedSavePrompts.add(state.scenario)) {
            sawSavePrompt = true
            route += "BattleScreen:${state.scenario}:save-prompt"
        }
        battleInputDriver.update(delta, state) {
            CampaignE2eBattleVerificationProjection().computeState(CampaignE2eProjectionContext(screen, tracker))
        }
    }

    /** finish: 검증 흐름을 종료하고 후속 상태를 정리한다. */
    private fun finish(actualModule: String, actualSceneIndex: Int, forwardOvershoot: Boolean) {
        CampaignE2eTraceWriter.write(
            config = config,
            snapshot = CampaignE2eTraceWriter.Snapshot(
                route = route, inputs = inputReporter.inputs, inputRecords = inputReporter.records,
                transitionEnterCount = inputReporter.transitionEnterCount, playerMoveBeforeScene1 = playerMoveBeforeScene1,
                committedPlayerMove = committedPlayerMove, initialBattleScenes = observedInitialBattleScenes,
                campaignStages = campaignStages, battlePreparations = battlePreparations,
                sawR01DepartureDialogue = sawR01DepartureDialogue,
            ),
            actualModule = actualModule, actualSceneIndex = actualSceneIndex, forwardOvershoot = forwardOvershoot,
        )
        finished = true
        Gdx.app.exit()
    }
}

/** ProductionBattleInputDriver: 캠페인과 단독 추적이 함께 사용하는 실제 입력 전투 구동기이다. */
internal class ProductionBattleInputDriver(
    /** inputIntervalSeconds: 검증 대상 목록을 담는다. */
    private val inputIntervalSeconds: Float,
    /** onInput: 검증 입력 정보를 담는다. */
    private val onInput: (String) -> Unit = {},
    /** onInputRecord: 검증 입력 정보를 담는다. */
    private val onInputRecord: (CampaignE2eInputRecord) -> Unit = {},
    /** manualMoveAttemptLimit: 캠페인 E2E는 원본 수동 이동 하나를 증명해야 하므로 제한을 두지 않는다. 단독 전투는 0으로 설정해 첫 Mine 진영이 원본 하니스와 동일하게 실제 자동 전투 UI에 진입하게 하며, S52·S57 원본 경로 입력은 이 공통 제한을 의도적으로 우회한다. */
    private val manualMoveAttemptLimit: Int? = null,
) {
    /** elapsed: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var elapsed = 0f
    /** nextInputAt: 검증 입력 정보를 담는다. */
    private var nextInputAt = .2f
    /** manualMoveAttempts: 검증 대상 목록을 담는다. */
    private var manualMoveAttempts = 0
    /** observeBattleState: 전투 검증 상태를 담는다. */
    private var observeBattleState: (() -> CampaignE2eBattleState)? = null

    /** pendingPhysicalAttackInput: CHILD_ACTION을 다시 투영하는 동안 유지하는 CommandLayer Attack 의도이다. */
    private var pendingPhysicalAttackInput: CampaignE2eAttackInput? = null

    /** update: 검증 상태를 입력에 맞게 갱신한다. */
    fun update(
        delta: Float,
        state: CampaignE2eBattleState,
        observeState: () -> CampaignE2eBattleState = { state },
    ) {
        observeBattleState = observeState
        elapsed += delta
        if (elapsed < nextInputAt) return
        val scenario = state.scenario
        val tacticalInputReady = productionTacticalInputReady(
            state.initialScene1Started,
            state.playback,
            state.turnPhase,
        )
        when {
            productionLossRecoveryPointer(state)?.let { point ->
                pointer(point.first, point.second, "$scenario:lose-title-yes")
                true
            } == true -> Unit

            state.savePromptOpen -> pointer(662, 408, "$scenario:save-prompt-no")
            state.rewardOpen -> key(Input.Keys.ENTER, "$scenario:reward")
            state.winConditionsOpen -> pointer(640, 344, "$scenario:win-conditions-close")
            state.playback == PlaybackState.DIALOGUE -> key(Input.Keys.ENTER, "$scenario:dialogue")
            // S01 철수는 ChoiceLayer의 실제 UP/ENTER 입력으로 선택하며, 스크립트 결과나 원본 상태를 직접 바꾸지 않는다.
            state.playback == PlaybackState.CHOICE && state.scenario == "S_01" ->
                when (s01WithdrawalChoiceAction(state.selectedChoice)) {
                    S01WithdrawalChoiceAction.PREVIOUS -> key(Input.Keys.UP, "$scenario:choice-withdraw")
                    S01WithdrawalChoiceAction.CONFIRM -> key(Input.Keys.ENTER, "$scenario:choice-withdraw-confirm")
                }
            // S_52의 세 시간제한 선택지는 조기 철수 분기가 첫 줄이다. 실제 ChoiceLayer 선택을 “계속 공격”으로 옮긴다.
            // 0번 행을 확정하면 작성된 모든 관문 구간을 건너뛴다.
            state.playback == PlaybackState.CHOICE && state.guidedAuthoredRoute && state.selectedChoice == 0 ->
                key(Input.Keys.DOWN, "$scenario:choice-continue")

            state.playback == PlaybackState.CHOICE -> key(Input.Keys.ENTER, "$scenario:choice-confirm")
            state.playback == PlaybackState.MODAL -> pointer(640, 344, "$scenario:modal-close")
            tacticalInputReady && state.magicTargetSelection && state.manualMagicInput != null -> {
                val magic = state.manualMagicInput
                val targetVisible = magic.targetScreenX in 1 until Gdx.graphics.width &&
                        magic.targetScreenY in 1 until Gdx.graphics.height
                if (targetVisible) {
                    pointer(magic.targetScreenX, magic.targetScreenY, "$scenario:player-magick-target")
                } else {
                    drag(
                        640,
                        344,
                        640 + (640 - magic.targetScreenX).coerceIn(-300, 300),
                        344 + (344 - magic.targetScreenY).coerceIn(-220, 220),
                        "$scenario:pan-to-magick-target",
                    )
                }
            }

            tacticalInputReady && state.magickListOpen && state.manualMagicInput != null -> {
                val magic = state.manualMagicInput
                pointer(magic.rowScreenX, magic.rowScreenY, "$scenario:player-magick-row")
            }

            tacticalInputReady && !state.magicTargetSelection &&
                    state.battleCommandOpen && state.manualMagicInput != null -> {
                val magic = state.manualMagicInput
                pointer(magic.commandScreenX, magic.commandScreenY, "$scenario:player-command-magick")
            }

            tacticalInputReady && !state.authoredRouteHoldFire &&
                    !state.magicTargetSelection && state.battleTargetSelectionOpen -> {
                // 맵 입력 전에 다시 관찰한다. BattleScreen은 선택한 병사가 현재 보이는 targetUnitId를 실제로 공격할 수 있을 때만 이 투영을 낸다.
                val opened = pendingPhysicalAttackInput ?: state.manualAttackInput ?: return
                val attack = productionLiveAttackInput(opened, observeState().manualAttackInput)
                if (attack == null) {
                    // 유효하지 않거나 오래된 맵 지점을 WAIT나 확정 행동으로 바꾸지 않는다.
                    // 다음 관찰이 실제 대상을 다시 투영하도록 CHILD_ACTION과 화면 이동 상태를 유지한다.
                    drag(
                        640,
                        344,
                        640 + (640 - opened.targetScreenX).coerceIn(-300, 300),
                        344 + (344 - opened.targetScreenY).coerceIn(-220, 220),
                        "$scenario:pan-reproject-attack-target",
                    )
                    return
                }
                val targetVisible = attack.targetScreenX in 1 until Gdx.graphics.width &&
                        attack.targetScreenY in 1 until Gdx.graphics.height
                if (targetVisible) {
                    pointer(attack.targetScreenX, attack.targetScreenY, "$scenario:player-attack-target")
                } else {
                    drag(
                        640,
                        344,
                        640 + (640 - attack.targetScreenX).coerceIn(-300, 300),
                        344 + (344 - attack.targetScreenY).coerceIn(-220, 220),
                        "$scenario:pan-to-attack-target",
                    )
                }
            }

            tacticalInputReady && !state.authoredRouteHoldFire &&
                    state.battleCommandOpen && state.manualAttackInput != null -> {
                val attack = state.manualAttackInput
                pendingPhysicalAttackInput = attack
                pointer(attack.commandScreenX, attack.commandScreenY, "$scenario:player-command-attack")
            }

            tacticalInputReady && state.battleCommandOpen ->
                pointer(state.commandWaitScreenX, state.commandWaitScreenY, "$scenario:player-command-wait")

            tacticalInputReady && !state.collocation && state.manualMoveInput != null &&
                    productionManualMoveAllowed(
                        state.scenario,
                        state.guidedAuthoredRoute,
                        state.playerMoveCommitted,
                        manualMoveAttempts,
                        manualMoveAttemptLimit,
                    ) -> {
                val move = state.manualMoveInput
                manualMoveAttempts++
                val sourceVisible =
                    move.sourceScreenX in 1 until Gdx.graphics.width && move.sourceScreenY in 1 until Gdx.graphics.height
                val destinationVisible =
                    move.destinationScreenX in 1 until Gdx.graphics.width && move.destinationScreenY in 1 until Gdx.graphics.height
                if (!sourceVisible || (state.selectedUnit && !destinationVisible)) {
                    val pointX = if (state.selectedUnit) move.destinationScreenX else move.sourceScreenX
                    val pointY = if (state.selectedUnit) move.destinationScreenY else move.sourceScreenY
                    drag(
                        640,
                        344,
                        640 + (640 - pointX).coerceIn(-300, 300),
                        344 + (344 - pointY).coerceIn(-220, 220),
                        "$scenario:pan-to-player"
                    )
                } else if (state.selectedUnit) {
                    pointer(move.destinationScreenX, move.destinationScreenY, "$scenario:player-move-destination")
                } else {
                    pointer(move.sourceScreenX, move.sourceScreenY, "$scenario:player-unit-select")
                }
            }

            tacticalInputReady && !state.collocation && state.autoBattleOverlay == "PROMPT" ->
                when (productionAutoBattlePromptActionForScenario(
                    state.scenario, state.guidedAuthoredRoute, state.autoBattleChecked,
                )) {
                    ProductionAutoBattlePromptAction.TOGGLE ->
                        pointer(
                            state.autoBattleToggleScreenX,
                            state.autoBattleToggleScreenY,
                            "$scenario:auto-battle-toggle"
                        )

                    ProductionAutoBattlePromptAction.CONFIRM ->
                        pointer(
                            state.autoBattleConfirmScreenX,
                            state.autoBattleConfirmScreenY,
                            "$scenario:auto-battle-confirm"
                        )
                }

            tacticalInputReady && !state.collocation && state.battleMenuOpen &&
                    productionEndRoundAllowed(state.scenario, state.s01EligibleMineActionRemaining) ->
                pointer(state.menuEndRoundScreenX, state.menuEndRoundScreenY, "$scenario:end-round-menu-command")

            tacticalInputReady && !state.collocation ->
                pointer(state.battleMenuButtonScreenX, state.battleMenuButtonScreenY, "$scenario:open-battle-menu")

            else -> Unit // Never synthesize Enter at COMPLETE/outcome boundaries.
        }
        nextInputAt = elapsed + inputIntervalSeconds
    }

    /** key: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    private fun key(code: Int, context: String) {
        val before = battleObservation()
        val accepted =
            checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }.keyDown(code)
        recordInput(context, accepted, before, battleObservation())
    }

    /** pointer: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    private fun pointer(x: Int, y: Int, context: String) {
        val input = checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }
        val before = battleObservation()
        val accepted = input.touchDown(x, y, 0, Input.Buttons.LEFT)
        input.touchUp(x, y, 0, Input.Buttons.LEFT)
        recordInput(context, accepted, before, battleObservation())
    }

    /** drag: 드래그 입력을 검증 상태에 반영한다. */
    private fun drag(fromX: Int, fromY: Int, toX: Int, toY: Int, context: String) {
        val input = checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }
        val before = battleObservation()
        val accepted = input.touchDown(fromX, fromY, 0, Input.Buttons.LEFT)
        input.touchDragged(toX, toY, 0)
        input.touchUp(toX, toY, 0, Input.Buttons.LEFT)
        recordInput(context, accepted, before, battleObservation())
    }

    /** recordInput: 검증 이벤트와 산출물을 기록한다. */
    private fun recordInput(event: String, accepted: Boolean, before: String, after: String) {
        onInputRecord(CampaignE2eInputRecord(event, accepted, before, after))
        // 독립 전체 전투 소비자는 여전히 레이블 전용 입력 배열을 사용하므로, 거부된 콜백을 기록된 입력처럼 취급하지 않는다.
        if (accepted) onInput(event)
    }

    /** battleObservation: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    private fun battleObservation(): String {
        val state = checkNotNull(observeBattleState) { "battle input observation is unavailable" }.invoke()
        return "screen=BattleScreen;scenario=${state.scenario};playback=${state.playback};" +
                "phase=${state.turnPhase};round=${state.round};battleMenuOpen=${state.battleMenuOpen};" +
                "battleCommandOpen=${state.battleCommandOpen};targetSelectionOpen=${state.battleTargetSelectionOpen};" +
                "magickListOpen=${state.magickListOpen};magicTargetSelection=${state.magicTargetSelection};" +
                "manualMagic=${state.manualMagicInput != null};" +
                "autoBattleOverlay=${state.autoBattleOverlay};autoBattleChecked=${state.autoBattleChecked};" +
                "collocation=${state.collocation};rewardOpen=${state.rewardOpen};savePromptOpen=${state.savePromptOpen};" +
                "losePromptOpen=${state.losePromptOpen};" +
                "outcome=${state.outcome}"
    }
}
