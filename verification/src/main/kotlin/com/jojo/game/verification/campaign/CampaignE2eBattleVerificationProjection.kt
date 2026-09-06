// Verification
package com.jojo.game.verification.campaign

/** CampaignE2eBattleVerificationProjection: 검증 전용 외관이며 협력 객체는 실행 중 전투판만 관찰한다. */
internal class CampaignE2eBattleVerificationProjection {
    /** movePlanner: 검증 실행 계획을 담는다. */
    private val movePlanner = CampaignE2eBattleMovePlanner()
    /** inputProjection: 검증 입력 정보를 담는다. */
    private val inputProjection = CampaignE2eBattleInputProjection()
    /** stateProjection: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private val stateProjection = CampaignE2eBattleStateProjection()

    /** computeState: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun computeState(ctx: CampaignE2eProjectionContext): CampaignE2eBattleState {
        val board = CampaignE2eBattlePlanningBoard(ctx)
        val movePlan = movePlanner.plan(board)
        val actionInputs = inputProjection.project(board, movePlan)
        return stateProjection.project(board, movePlan, actionInputs)
    }
}
