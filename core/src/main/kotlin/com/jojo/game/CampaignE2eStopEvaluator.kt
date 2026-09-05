package com.jojo.game

/** Pure policy for deciding when a campaign trace has reached its requested checkpoint. */
internal class CampaignE2eStopEvaluator(stopAt: CampaignE2eStopPoint) {
    enum class Decision { CONTINUE, REACHED, FORWARD_OVERSHOOT }

    private val stopModule = stopAt.module
    private val stopSceneIndex = stopAt.sceneIndex
    private val requestedStage = stopModule.removePrefix("R_").toIntOrNull()?.times(2)

    fun evaluate(module: String, sceneIndex: Int, campaignStage: Int): Decision = when {
        module == stopModule && sceneIndex >= stopSceneIndex -> Decision.REACHED
        requestedStage != null && campaignStage > requestedStage -> Decision.FORWARD_OVERSHOOT
        else -> Decision.CONTINUE
    }
}
