package com.jojo.game.presentation.battle

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleCapturePolicyTest {
    @Test
    fun `route policy keeps capture-only clocks and action samples deterministic`() {
        val policy = BattleCapturePolicy("attack25-f2")

        assertEquals(CaptureActionSample(25, 12f / 24f), policy.actionCapture)
        assertTrue(policy.actionCaptureMode)
        assertEquals(3f, policy.animationClock(elapsed = 3f, battleElapsed = 99f))
        assertFalse(policy.mapOnlyCapture)
    }

    @Test
    fun `ordinary battle has no fixture route and follows live clock`() {
        val policy = BattleCapturePolicy(null)

        assertEquals(null, policy.actionCapture)
        assertFalse(policy.actionCaptureMode)
        assertEquals(4f, policy.animationClock(elapsed = 1f, battleElapsed = 4f))
        assertEquals(4f, policy.mapObjectAnimationClock(4f))
    }
}
