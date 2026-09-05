package com.jojo.game.presentation.battle

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattlePresentationRoutePolicyTest {
    @Test
    fun `route policy keeps capture-only clocks and action samples deterministic`() {
        val policy = BattlePresentationRoutePolicy("attack25-f2")

        assertEquals(CaptureActionSample(25, 12f / 24f), policy.actionSample)
        assertTrue(policy.actionSampleMode)
        assertEquals(3f, policy.animationClock(elapsed = 3f, battleElapsed = 99f))
        assertFalse(policy.mapOnlyCapture)
    }

    @Test
    fun `ordinary battle has no fixture route and follows live clock`() {
        val policy = BattlePresentationRoutePolicy(null)

        assertEquals(null, policy.actionSample)
        assertFalse(policy.actionSampleMode)
        assertEquals(4f, policy.animationClock(elapsed = 1f, battleElapsed = 4f))
        assertEquals(4f, policy.mapObjectAnimationClock(4f))
    }
}
