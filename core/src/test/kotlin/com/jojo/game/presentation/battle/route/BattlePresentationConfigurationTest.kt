// Test
package com.jojo.game.presentation.battle.route

import com.jojo.game.application.runtime.RuntimeBattlePresentation
import com.jojo.game.application.runtime.RuntimeBattleRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** runtime route와 capture clock 정책을 화면 route로 해석하는지 검증한다. */
class BattlePresentationConfigurationTest {
    @Test
    fun `fixed UI routes stop deterministic unit clocks`() {
        val policy = BattlePresentationConfiguration(
            RuntimeBattlePresentation(route = RuntimeBattleRoute.REWARD_BASIC),
        )

        assertEquals(RuntimeBattleRoute.REWARD_BASIC, policy.rewardRouteState)
        assertEquals(0f, policy.animationClock(elapsed = 3f, battleElapsed = 99f, actionSampleMode = false))
    }

    @Test
    fun `ordinary battle follows live clock`() {
        val policy = BattlePresentationConfiguration(RuntimeBattlePresentation())

        assertEquals(4f, policy.animationClock(elapsed = 1f, battleElapsed = 4f, actionSampleMode = false))
        assertEquals(4f, policy.mapObjectAnimationClock(4f))
    }

    @Test
    fun `fixed battle views are represented by runtime routes`() {
        val helper = BattlePresentationConfiguration(RuntimeBattlePresentation(RuntimeBattleRoute.HELPER))
        val winModal = BattlePresentationConfiguration(RuntimeBattlePresentation(RuntimeBattleRoute.WIN_MODAL))
        val unitInfo = BattlePresentationConfiguration(RuntimeBattlePresentation(RuntimeBattleRoute.UNIT_INFO))
        val loseResult = BattlePresentationConfiguration(RuntimeBattlePresentation(RuntimeBattleRoute.RESULT_LOSE))
        val winResult = BattlePresentationConfiguration(RuntimeBattlePresentation(RuntimeBattleRoute.RESULT_WIN))

        assertTrue(helper.helperRoute)
        assertTrue(winModal.winModalRoute)
        assertTrue(unitInfo.unitInfoRoute)
        assertTrue(loseResult.loseResultRoute)
        assertTrue(winResult.winResultRoute)
    }
}
