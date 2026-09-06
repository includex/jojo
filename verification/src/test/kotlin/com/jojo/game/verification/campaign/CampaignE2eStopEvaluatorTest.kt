// Test
package com.jojo.game.verification.campaign

import kotlin.test.Test
import kotlin.test.assertEquals

class CampaignE2eStopEvaluatorTest {
    private val evaluator = CampaignE2eStopEvaluator(CampaignE2eStopPoint("R_10", 3))

    @Test
    fun `requested scene and later scenes in the same module are reached`() {
        assertEquals(CampaignE2eStopEvaluator.Decision.CONTINUE, evaluator.evaluate("R_10", 2, 20))
        assertEquals(CampaignE2eStopEvaluator.Decision.REACHED, evaluator.evaluate("R_10", 3, 20))
        assertEquals(CampaignE2eStopEvaluator.Decision.REACHED, evaluator.evaluate("R_10", 4, 20))
    }

    @Test
    fun `jump beyond requested stage is classified as forward overshoot`() {
        assertEquals(
            CampaignE2eStopEvaluator.Decision.FORWARD_OVERSHOOT,
            evaluator.evaluate("R_12", 0, 24),
        )
    }
}
