package com.jojo.game

import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.presentation.battle.BattleScreen
import com.jojo.game.presentation.battle.preparation.BattlePreparationScreen
import com.jojo.game.presentation.battle.preparation.CampaignE2eBattlePreparationState
import com.jojo.game.domain.battle.*
import com.jojo.game.presentation.title.TitleScreen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen

/**
 * data class  `CampaignE2eTraceConfig`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class CampaignE2eTraceConfig(
    val outputPath: String,
    val maxSeconds: Float = 900f,
    val inputIntervalSeconds: Float = .12f,
    /** Default preserves the established Title -> R_00 -> S_00 -> R_01 contract. */
    val stopAt: CampaignE2eStopPoint = CampaignE2eStopPoint(),
    /** Extended data-driven routes opt out after supplying their own verifier contract. */
    val requireYingchuanBootstrapContract: Boolean = true,
)

/**
 * data class  `CampaignE2eStopPoint`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class CampaignE2eStopPoint(val module: String = "R_01", val sceneIndex: Int = 1)

internal data class CampaignE2eScenarioState(
    val module: String,
    val playback: PlaybackState,
    val options: List<String>,
    val selectedChoice: Int,
    val sceneIndex: Int,
    val startedScenes: List<Int>,
    val campaignStage: Int,
    val menuVisible: Boolean,
    val dialogueText: String?,
    val hallBattleScenePending: Boolean,
    val battleButtonScreenX: Int,
    val battleButtonScreenY: Int,
)

internal data class CampaignE2eMoveInput(
    val sourceScreenX: Int,
    val sourceScreenY: Int,
    val destinationScreenX: Int,
    val destinationScreenY: Int,
)

internal data class CampaignE2eAttackInput(
    val commandScreenX: Int,
    val commandScreenY: Int,
    val targetScreenX: Int,
    val targetScreenY: Int,
    /** Stable BattleUnit identity; screen coordinates are reprojection-only. */
    val targetUnitId: String = "",
)

/** A still-open Attack selector must use the latest hit-area preflight/projection. */
internal fun productionLiveAttackInput(
    _openedInput: CampaignE2eAttackInput?,
    liveInput: CampaignE2eAttackInput?,
): CampaignE2eAttackInput? = liveInput?.takeIf { it.targetUnitId.isNotEmpty() }

/** The three real touches which select, choose, and target a MagickList row. */
internal data class CampaignE2eMagicInput(
    val commandScreenX: Int,
    val commandScreenY: Int,
    val rowScreenX: Int,
    val rowScreenY: Int,
    val targetScreenX: Int,
    val targetScreenY: Int,
)

/** Read-only, UI-independent subset of a strategy used by the S57 driver. */
internal data class CampaignE2eMagicOption(
    val id: Int,
    val target: Int,
    val cost: Int,
    val power: Int,
    val category: Int,
    val allScreen: Boolean,
    val offsets: Set<Pair<Int, Int>>,
)

internal data class CampaignE2eMagicTarget(val id: String, val x: Int, val y: Int)

internal data class CampaignE2eGuidedMagicPlan(val magicId: Int, val targetId: String)

/**
 * S57's production route prefers a legal offensive MagickList choice to a
 * physical command, but only while the authored room/attrition route permits
 * combat. This is deliberately a pure projection: it cannot mutate Battle,
 * Campaign, or the scenario AST.
 */
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
    // The S57 roster has exactly one offensive player strategy:
    // Cao Cao, Whirlwind (10), cost 6/power 50, enemy-targeted.
    // Do not turn this S57-specific production aid into a general AI policy.
    // The first-room leaders own this encounter's combat route.  Guard hits
    // by Whirlwind strand Cao Cao at the retreat point without advancing the
    // scenario event, so this is a hard route gate rather than a target score.
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

/**
 * One attempt dispatched to the installed production InputProcessor.
 *
 * The legacy `inputs` array remains available to old consumers.  This record
 * is the auditable evidence: it retains rejected attempts and captures the
 * observable state immediately before and after the dispatch.
 */
internal data class CampaignE2eInputRecord(
    val event: String,
    val accepted: Boolean,
    val before: String,
    val after: String,
)

internal data class CampaignE2eBattleState(
    val scenario: String,
    val playback: PlaybackState,
    val outcome: BattleOutcome?,
    val initialScene1Started: Boolean,
    val resultScene1Started: Boolean,
    val scene2Started: Boolean,
    val rewardOpen: Boolean,
    val winConditionsOpen: Boolean,
    val savePromptOpen: Boolean,
    /** Lose.scene owns a real pointer answer after its source-faithful delay. */
    val losePromptOpen: Boolean,
    /** Physical-screen centre of Lose.scene's "예" answer (return to title). */
    val loseTitleScreenX: Int,
    val loseTitleScreenY: Int,
    val playerMoveCommitted: Boolean,
    val campaignStage: Int,
    val round: Int,
    val activeFaction: Faction,
    val turnPhase: BattleTurnController.Phase,
    val battleMenuOpen: Boolean,
    val battleCommandOpen: Boolean,
    val battleTargetSelectionOpen: Boolean,
    val selectedUnit: Boolean,
    val manualMoveInput: CampaignE2eMoveInput?,
    val manualAttackInput: CampaignE2eAttackInput?,
    /** MagickListLayer is visible and owns the next pointer input. */
    val magickListOpen: Boolean,
    /** A selected MagickList row has opened its map-target selection mode. */
    val magicTargetSelection: Boolean,
    /** Present only for a range-legal offensive S57 strategy target. */
    val manualMagicInput: CampaignE2eMagicInput?,
    val commandWaitScreenX: Int,
    val commandWaitScreenY: Int,
    val menuEndRoundScreenX: Int,
    val menuEndRoundScreenY: Int,
    val battleMenuButtonScreenX: Int,
    val battleMenuButtonScreenY: Int,
    val autoBattleToggleScreenX: Int,
    val autoBattleToggleScreenY: Int,
    val autoBattleConfirmScreenX: Int,
    val autoBattleConfirmScreenY: Int,
    val manualMoveDebug: String,
    val autoBattleOverlay: AutoBattleFlow.Overlay,
    val autoBattleChecked: Boolean,
    val collocation: Boolean,
    val committedPlayerMove: String?,
    val selectedChoice: Int,
    val guidedAuthoredRoute: Boolean,
    /** The authored S57 room/trap route requires a real CommandLayer WAIT. */
    val authoredRouteHoldFire: Boolean = false,
    /** S01 must not open the end-round UI until every action-capable Mine acted. */
    val s01EligibleMineActionRemaining: Boolean = false,
)

/** No tactical key reaches Lose.scene; its MsgBox accepts a pointer only after the delay. */
internal fun productionLossRecoveryPointer(state: CampaignE2eBattleState): Pair<Int, Int>? =
    (state.loseTitleScreenX to state.loseTitleScreenY).takeIf { state.losePromptOpen }

/** S01's authored ChoiceLayer puts the withdrawal branch in row zero. */
internal enum class S01WithdrawalChoiceAction { PREVIOUS, CONFIRM }

internal fun s01WithdrawalChoiceAction(selectedChoice: Int): S01WithdrawalChoiceAction =
    if (selectedChoice > 0) S01WithdrawalChoiceAction.PREVIOUS else S01WithdrawalChoiceAction.CONFIRM

/**
 * Read-only policy extracted from S_57 scene1. The input driver still sends
 * only normal select/move/CommandLayer pointer events; this identifies when
 * those events must walk Cao Cao into the second-room rectangle, then WAIT
 * while enemy turns create the source-required attrition.
 */
internal data class S57AuthoredRouteSignal(
    val combatTargetIds: Set<Int> = emptySet(),
    /** Non-null activates the gate route; movement uses the source rectangle, not this marker point. */
    val gateTarget: Pair<Int, Int>? = null,
    val waitForAttrition: Boolean = false,
) {
    val holdFire: Boolean get() = gateTarget != null || waitForAttrition
}

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
        // The source room-clear callback has not run yet. Even when source 0
        // is the sole surviving Mine, he must remain on the first-room route;
        // the second-room gate may not be entered before all three leaders
        // have vanished.
        return S57AuthoredRouteSignal(combatTargetIds = firstRoom)
    }
    val sunFamily = visible intersect setOf(166, 167, 168)
    // The reveal callback publishes the complete trio before returning.
    // Do not infer the gate from a partial/late combat snapshot.
    if (sunFamily.size < 3) return S57AuthoredRouteSignal()
    // scene1 lines 775–867: one Mine must be inside x=2..16/y=11..23,
    // then totalUnit(MINE) must fall below two. Preserve unit 0 there and
    // let ordinary enemy turns supply the loss; never write status/HP/vars.
    return if (!mineMasterInSecondRoom) {
        S57AuthoredRouteSignal(combatTargetIds = sunFamily, gateTarget = 16 to 19)
    } else {
        S57AuthoredRouteSignal(
            combatTargetIds = sunFamily,
            waitForAttrition = visiblePlayerCount >= 2,
        )
    }
}

/** Only the authored initial scene1/startOper hand-off may expose tactical driver input. */
internal fun productionTacticalInputReady(
    initialScene1Started: Boolean,
    playback: PlaybackState,
    phase: BattleTurnController.Phase,
): Boolean = initialScene1Started && playback == PlaybackState.COMPLETE &&
        phase == BattleTurnController.Phase.PLAYER_INPUT

internal fun productionManualUnitEligible(statuses: Map<BattleStatus, Int>): Boolean =
    BattleStatus.PARALYSIS !in statuses && BattleStatus.CONFUSION !in statuses

/** S01 may open the real end-round menu only after all actionable Mine slots acted. */
internal fun productionEndRoundAllowed(scenario: String, s01EligibleMineActionRemaining: Boolean): Boolean =
    scenario != "S_01" || !s01EligibleMineActionRemaining

/**
 * Standalone traces normally set the manual quota to zero before entrusting
 * battle. S01 is intentionally manual: every Mine must take its normal UI
 * turn, so its per-turn planner bypasses that generic quota.
 */
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

/**
 * Production MsgBox4 policy after MenuLayer.HHJS.
 *
 * S_52/S_57 must retain manual control so the authored gate tiles can be
 * visited. Their real UI route therefore confirms the prompt with the
 * persisted toggle left off. Generic standalone battles first check the
 * visible toggle and then confirm, matching the source entrusted-control
 * harness. A guided prompt must never fall through to the battle-menu
 * button: MsgBox4 interprets that out-of-panel touch as Cancel.
 */
internal enum class ProductionAutoBattlePromptAction { TOGGLE, CONFIRM }

internal fun productionAutoBattlePromptAction(
    guidedAuthoredRoute: Boolean,
    checked: Boolean,
): ProductionAutoBattlePromptAction =
    if (checked == guidedAuthoredRoute) ProductionAutoBattlePromptAction.TOGGLE
    else ProductionAutoBattlePromptAction.CONFIRM

/**
 * S01 needs a real end-round answer but must never enter entrusted control:
 * source 0 remains available for the next ordinary PLAYER_INPUT turn while
 * the FRIEND camp retains its own engine AI turn. Other scenarios preserve
 * the existing source-harness toggle policy.
 */
internal fun productionAutoBattlePromptActionForScenario(
    scenario: String,
    guidedAuthoredRoute: Boolean,
    checked: Boolean,
): ProductionAutoBattlePromptAction =
    if (scenario == "S_01") {
        if (checked) ProductionAutoBattlePromptAction.TOGGLE else ProductionAutoBattlePromptAction.CONFIRM
    } else productionAutoBattlePromptAction(guidedAuthoredRoute, checked)

/**
 * The R_01 StartBattleScreen permits four through seven units, but its source
 * scene6 has just joined the complete first campaign party and scene7 exposes
 * seven authored Mine slots.  The production route deliberately fills those
 * slots before pressing the normal StartBattleScreen confirmation.  Other
 * battles retain the UI's ordinary minimum-size confirmation behavior.
 */
internal enum class CampaignBattlePreparationAction { START, NEXT_UNIT, TOGGLE_UNIT }

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

/**
 * Drives the production screens through their installed InputProcessor. It
 * only observes screen state; it never installs AST variables, battle
 * context, capture screens, or shortened delays.
 */
internal class CampaignE2eDriver(private val config: CampaignE2eTraceConfig) {
    private val route = mutableListOf<String>()
    private val stopEvaluator = CampaignE2eStopEvaluator(config.stopAt)

    // Kept for backwards compatibility.  It now includes only accepted
    // attempts, while inputRecords retains every dispatch for audit.
    private val inputs = mutableListOf<String>()
    private val inputRecords = mutableListOf<CampaignE2eInputRecord>()
    private var elapsed = 0f
    private var nextInputAt = .25f
    private var lastScreen: Screen? = null
    private var titleClicked = false
    private var sawInitialScene1 = false
    private var sawResultScene1 = false
    private var sawScene2 = false
    private var sawSavePrompt = false
    private val observedScenarioScenes = mutableSetOf<String>()
    private var transitionEnterCount = 0
    private var playerMoveBeforeScene1 = false
    private var committedPlayerMove: String? = null
    private var finished = false
    private var lastScenarioState: String? = null
    private var lastBattleState: String? = null
    private val pendingScenarioStarts = mutableListOf<Pair<String, Int>>()
    private val battleInputDriver = ProductionBattleInputDriver(
        inputIntervalSeconds = config.inputIntervalSeconds,
        onInput = { inputs += it },
        onInputRecord = { inputRecords += it },
    )
    private val observedInitialBattleScenes = mutableSetOf<String>()
    private val observedResultBattleScenes = mutableSetOf<String>()
    private val observedBattleScene2 = mutableSetOf<String>()
    private val observedSavePrompts = mutableSetOf<String>()
    private val battlePreparations = mutableListOf<String>()
    private val campaignStages = mutableListOf<Int>()
    private val hallBattleCommands = mutableSetOf<String>()
    private var sawR01DepartureDialogue = false

    private fun observeStage(stage: Int) {
        if (campaignStages.lastOrNull() != stage) campaignStages += stage
    }

    /**
     * 공개 메서드 `scenarioStarted`
     *
     * ### 파라미터
    - `module` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun scenarioStarted(module: String, index: Int) {
        pendingScenarioStarts += module to index
    }

    /**
     * 공개 메서드 `update`
     *
     * ### 파라미터
    - `delta` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `current` (`Screen?`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun update(delta: Float, current: Screen?) {
        if (finished) return
        elapsed += delta
        check(elapsed <= config.maxSeconds) { "campaign E2E timed out: ${route.joinToString(" -> ")}" }
        if (current !== lastScreen && lastScreen is ScenarioScreen) drainScenarioStarts()
        if (current !== lastScreen) {
            lastScreen = current
            when (current) {
                is TitleScreen -> route += "TitleScreen"
                is ScenarioScreen -> route += "ScenarioScreen:${current.campaignE2eState().module}"
                is BattlePreparationScreen -> current.campaignE2eState().let { state ->
                    route += "BattlePreparationScreen:${state.returnScenario}->${state.sourceScenario}"
                }

                is BattleScreen -> route += "BattleScreen:${current.campaignE2eState().scenario}"
                null -> route += "null"
                else -> route += current.javaClass.simpleName
            }
            nextInputAt = elapsed + .2f
            Gdx.app.log("JojoGame", "CAMPAIGN_E2E_SCREEN: ${route.last()}")
        }
        if (current is ScenarioScreen) drainScenarioStarts()

        when (current) {
            is TitleScreen -> if (!titleClicked && elapsed >= nextInputAt) {
                // Centre of TitleInteraction.NEW_GAME in logical window coordinates.
                pointer(1097, 688 - 500, "TitleScreen:new-game-click")
                titleClicked = true
            }

            is ScenarioScreen -> driveScenario(current.campaignE2eState())
            is BattlePreparationScreen -> driveBattlePreparation(current.campaignE2eState())
            is BattleScreen -> driveBattle(delta, current)
        }
    }

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

    private fun driveScenario(state: CampaignE2eScenarioState) {
        val scene = "${state.module}:scene${state.sceneIndex}"
        // A source scene can start, complete synchronously, and route to the
        // next screen inside one render. Consume the production screen's
        // append-only start history so those real invocations are observed
        // without adding an artificial one-frame yield.
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
            PlaybackState.DIALOGUE -> key(Input.Keys.ENTER, "${state.module}:dialogue")
            PlaybackState.MODAL -> key(Input.Keys.ENTER, "${state.module}:modal")
            PlaybackState.CHOICE -> {
                val desired = state.options.indexOfFirst { "게임 시작" in it }.takeIf { it >= 0 } ?: 0
                if (state.selectedChoice != desired) key(Input.Keys.DOWN, "${state.module}:choice-next")
                else key(Input.Keys.ENTER, "${state.module}:choice-confirm")
            }

            PlaybackState.COMPLETE -> if (state.menuVisible && hallBattleCommands.add(state.module)) {
                check(
                    state.battleButtonScreenX in 0 until Gdx.graphics.width &&
                            state.battleButtonScreenY in 0 until Gdx.graphics.height
                ) { "${state.module} projected Hall battle command is outside the viewport" }
                route += "ScenarioScreen:${state.module}:hall-battle-button"
                pointer(
                    state.battleButtonScreenX,
                    state.battleButtonScreenY,
                    "${state.module}:hall-battle-button",
                )
            }

            PlaybackState.DELAY -> Unit
        }
        nextInputAt = elapsed + config.inputIntervalSeconds
    }

    private fun driveBattlePreparation(state: CampaignE2eBattlePreparationState) {
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
            CampaignBattlePreparationAction.START -> key(Input.Keys.ENTER, "${state.sourceScenario}:preparation-start")
            CampaignBattlePreparationAction.NEXT_UNIT -> key(
                Input.Keys.RIGHT,
                "${state.sourceScenario}:preparation-next-unit"
            )

            CampaignBattlePreparationAction.TOGGLE_UNIT -> key(
                Input.Keys.SPACE,
                "${state.sourceScenario}:preparation-select-unit"
            )
        }
        nextInputAt = elapsed + config.inputIntervalSeconds
    }

    private fun driveBattle(delta: Float, screen: BattleScreen) {
        val state = screen.campaignE2eState()
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
            // The authored initial scene1 owns startOper and therefore must
            // precede the first accepted tactical move.
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
        battleInputDriver.update(delta, state, screen::campaignE2eState)
    }

    private fun key(code: Int, context: String) {
        if (context.endsWith(":transition")) transitionEnterCount++
        val before = screenObservation()
        val accepted =
            checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }.keyDown(code)
        recordInput(context, accepted, before, screenObservation())
    }

    private fun pointer(x: Int, y: Int, context: String) {
        val input = checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }
        val before = screenObservation()
        val accepted = input.touchDown(x, y, 0, Input.Buttons.LEFT)
        input.touchUp(x, y, 0, Input.Buttons.LEFT)
        recordInput(context, accepted, before, screenObservation())
    }

    private fun recordInput(event: String, accepted: Boolean, before: String, after: String) {
        inputRecords += CampaignE2eInputRecord(event, accepted, before, after)
        if (accepted) inputs += event
    }

    private fun screenObservation(): String = CampaignE2eScreenObservation.of(lastScreen)

    private fun finish(actualModule: String, actualSceneIndex: Int, forwardOvershoot: Boolean) {
        CampaignE2eTraceWriter.write(
            config = config,
            snapshot = CampaignE2eTraceWriter.Snapshot(
                route = route, inputs = inputs, inputRecords = inputRecords,
                transitionEnterCount = transitionEnterCount, playerMoveBeforeScene1 = playerMoveBeforeScene1,
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

/** Shared real-input battle driver used by both campaign and standalone traces. */
internal class ProductionBattleInputDriver(
    private val inputIntervalSeconds: Float,
    private val onInput: (String) -> Unit = {},
    private val onInputRecord: (CampaignE2eInputRecord) -> Unit = {},
    /**
     * Campaign E2E must prove one authored manual move and therefore leaves
     * this unlimited. Standalone battles set it to zero so their first Mine
     * camp enters the real auto-battle UI exactly like the original harness;
     * S52/S57 authored-route input deliberately bypasses this generic limit.
     */
    private val manualMoveAttemptLimit: Int? = null,
) {
    private var elapsed = 0f
    private var nextInputAt = .2f
    private var manualMoveAttempts = 0
    private var observeBattleState: (() -> CampaignE2eBattleState)? = null

    /** CommandLayer Attack intent retained while CHILD_ACTION is reprojected. */
    private var pendingPhysicalAttackInput: CampaignE2eAttackInput? = null

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
            // Use ChoiceLayer's real UP/ENTER input for S01 withdrawal; never
            // select a script result or alter source state directly.
            state.playback == PlaybackState.CHOICE && state.scenario == "S_01" ->
                when (s01WithdrawalChoiceAction(state.selectedChoice)) {
                    S01WithdrawalChoiceAction.PREVIOUS -> key(Input.Keys.UP, "$scenario:choice-withdraw")
                    S01WithdrawalChoiceAction.CONFIRM -> key(Input.Keys.ENTER, "$scenario:choice-withdraw-confirm")
                }
            // S_52's three timed choices put the early-withdraw branch first.
            // Move the real ChoiceLayer selection to "continue attacking";
            // confirming row zero would bypass every authored gate episode.
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
                // Re-observe before map input. BattleScreen only emits this
                // projection when the selected actor can physically hit the
                // visible targetUnitId right now.
                val opened = pendingPhysicalAttackInput ?: state.manualAttackInput ?: return
                val attack = productionLiveAttackInput(opened, observeState().manualAttackInput)
                if (attack == null) {
                    // Do not turn an invalid/stale map point into WAIT or a
                    // committed action. Keep CHILD_ACTION and pan so the next
                    // observation can reproject the live target.
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

            tacticalInputReady && !state.collocation && state.autoBattleOverlay == AutoBattleFlow.Overlay.PROMPT ->
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

    private fun key(code: Int, context: String) {
        val before = battleObservation()
        val accepted =
            checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }.keyDown(code)
        recordInput(context, accepted, before, battleObservation())
    }

    private fun pointer(x: Int, y: Int, context: String) {
        val input = checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }
        val before = battleObservation()
        val accepted = input.touchDown(x, y, 0, Input.Buttons.LEFT)
        input.touchUp(x, y, 0, Input.Buttons.LEFT)
        recordInput(context, accepted, before, battleObservation())
    }

    private fun drag(fromX: Int, fromY: Int, toX: Int, toY: Int, context: String) {
        val input = checkNotNull(Gdx.input.inputProcessor) { "no production input processor at $context" }
        val before = battleObservation()
        val accepted = input.touchDown(fromX, fromY, 0, Input.Buttons.LEFT)
        input.touchDragged(toX, toY, 0)
        input.touchUp(toX, toY, 0, Input.Buttons.LEFT)
        recordInput(context, accepted, before, battleObservation())
    }

    private fun recordInput(event: String, accepted: Boolean, before: String, after: String) {
        onInputRecord(CampaignE2eInputRecord(event, accepted, before, after))
        // Standalone full-battle consumers still use label-only input arrays.
        // Do not let a rejected callback masquerade as a recorded input there.
        if (accepted) onInput(event)
    }

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
