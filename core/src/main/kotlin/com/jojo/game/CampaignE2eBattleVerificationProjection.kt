package com.jojo.game

/** Verification-only façade; collaborators only observe the live battle board. */
internal class CampaignE2eBattleVerificationProjection {
    private val movePlanner = CampaignE2eBattleMovePlanner()
    private val inputProjection = CampaignE2eBattleInputProjection()
    private val stateProjection = CampaignE2eBattleStateProjection()

    fun computeState(ctx: BattleCampaignE2eAdapter.ProjectionContext): CampaignE2eBattleState {
        val board = CampaignE2eBattlePlanningBoard(ctx)
        val movePlan = movePlanner.plan(board)
        val actionInputs = inputProjection.project(board, movePlan)
        return stateProjection.project(board, movePlan, actionInputs)
    }
}
