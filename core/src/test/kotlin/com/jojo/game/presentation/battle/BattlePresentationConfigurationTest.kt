package com.jojo.game.presentation.battle

import com.jojo.game.application.runtime.RuntimeBattleActionSample
import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattlePresentationConfigurationTest {
    @Test
    fun `runtime presentation keeps deterministic action clocks`() {
        val policy = BattlePresentationConfiguration(
            RuntimeBattlePresentation(
                route = RuntimeBattleRoute.ACTION_25_F2,
                actionSample = RuntimeBattleActionSample(25, 12f / 24f),
            )
        )

        assertEquals(RuntimeBattleActionSample(25, 12f / 24f), policy.actionSample)
        assertTrue(policy.actionSampleMode)
        assertEquals(3f, policy.animationClock(elapsed = 3f, battleElapsed = 99f))
        assertFalse(policy.mapOnlyCapture)
    }

    @Test
    fun `ordinary battle follows live clock`() {
        val policy = BattlePresentationConfiguration(RuntimeBattlePresentation())

        assertEquals(null, policy.actionSample)
        assertFalse(policy.actionSampleMode)
        assertEquals(4f, policy.animationClock(elapsed = 1f, battleElapsed = 4f))
        assertEquals(4f, policy.mapObjectAnimationClock(4f))
    }
}
