package com.jojo.game

import com.jojo.game.domain.scenario.*

/**
 * SRPG Battle Campaign E2E State Adapter.
 *
 * Decouples the E2E verification/observation layer from the LibGDX presentation layer (BattleScreen).
 * Projects live tactical state into CampaignE2eBattleState using provided projection delegates.
 */
internal object BattleCampaignE2eAdapter {

    /**
     * data class  `ProjectionContext`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class ProjectionContext(
        val scenario: String,
        val battle: Battle,
        val selectedUnitId: String?,
        val authoredMechanicRoute: AuthoredMechanicRouteTracker,
        val scriptState: PlaybackState,
        val selectedChoice: Int,
        val bootstrapPhase: BattleBootstrapPhase,
        val initialPlayerCampScriptStarted: Boolean,
        val resultScene1Observed: Boolean,
        val naturalOutcomeScriptStarted: Boolean,
        val postBattleSceneStarted: Boolean,
        val rewardOpen: Boolean,
        val winConditionsOpen: Boolean,
        val savePromptOpen: Boolean,
        val losePromptOpen: Boolean,
        val loseTitleScreenPoint: Pair<Int, Int>,
        val playerMoveCommitted: Boolean,
        val campaignStage: Int,
        val turnPhase: BattleTurnController.Phase,
        val battleMenuOpen: Boolean,
        val battleCommandOpen: Boolean,
        val battleTargetSelectionOpen: Boolean,
        val magickListOpen: Boolean,
        val magicMode: Boolean,
        val waitCommandScreenPoint: Pair<Int, Int>,
        val endRoundCommandScreenPoint: Pair<Int, Int>,
        val battleMenuButtonScreenPoint: Pair<Int, Int>,
        val autoBattleToggleScreenPoint: Pair<Int, Int>,
        val autoBattleConfirmScreenPoint: Pair<Int, Int>,
        val autoBattleOverlay: AutoBattleFlow.Overlay,
        val autoBattleChecked: Boolean,
        val collocation: Boolean,
        val committedPlayerMove: String?,
        val screenPoint: (x: Int, y: Int) -> Pair<Int, Int>,
        val projectWorldPoint: (worldX: Float, worldY: Float) -> Pair<Int, Int>,
    )

    private val projection = CampaignE2eBattleVerificationProjection()

    /** Public observation contract; the implementation remains verification-only. */
    fun computeState(ctx: ProjectionContext): CampaignE2eBattleState = projection.computeState(ctx)
}
