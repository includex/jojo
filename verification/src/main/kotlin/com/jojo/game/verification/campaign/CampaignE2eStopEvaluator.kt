// Verification
package com.jojo.game.verification.campaign

/** CampaignE2eStopEvaluator: 캠페인 추적이 요청한 중단 지점에 도달했는지 판단하는 순수 정책이다. */
internal class CampaignE2eStopEvaluator(stopAt: CampaignE2eStopPoint) {
    /** Decision: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    enum class Decision { CONTINUE, REACHED, FORWARD_OVERSHOOT }

    /** stopModule: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private val stopModule = stopAt.module
    /** stopSceneIndex: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private val stopSceneIndex = stopAt.sceneIndex
    /** requestedStage: 시나리오 진행 상태를 담는다. */
    private val requestedStage = stopModule.removePrefix("R_").toIntOrNull()?.times(2)

    /** evaluate: 검증 조건을 실행하고 결과를 판정한다. */
    fun evaluate(module: String, sceneIndex: Int, campaignStage: Int): Decision = when {
        module == stopModule && sceneIndex >= stopSceneIndex -> Decision.REACHED
        requestedStage != null && campaignStage > requestedStage -> Decision.FORWARD_OVERSHOOT
        else -> Decision.CONTINUE
    }
}
