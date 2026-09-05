package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleRewardFlowTest {
    @Test fun `resolver keeps reward args and source minimum`() {
        val result = BattleRewardResolver.resolve(
            ScenarioRewardRequest(0, listOf(88, -1, 89, 2), false),
            averageLevel = 3, round = 8, maxRound = 20,
            mineDeaths = 0, enemiesRemaining = 0, objectivesComplete = true,
        )
        assertEquals(1130, result.money)
        assertEquals(7, result.flag)
        assertEquals(listOf(88, 89), result.itemIds)
    }

    @Test fun `money and every item phase must finish before campaign route`() {
        val flow = BattleRewardFlow(ResolvedBattleReward(900, 7, listOf(88, 89), false))
        assertEquals(BattleRewardFlow.Phase.MONEY, flow.phase)
        flow.advance()
        assertEquals(BattleRewardFlow.Phase.ITEMS, flow.phase)
        assertEquals(1, flow.visibleItemCount)
        flow.advance()
        assertFalse(flow.complete)
        assertEquals(2, flow.visibleItemCount)
        flow.advance()
        assertTrue(flow.complete)
    }

    @Test fun `end reward uses the dedicated final panel`() {
        val flow = BattleRewardFlow(ResolvedBattleReward(900, 7, emptyList(), true))
        assertEquals(BattleRewardFlow.Phase.END, flow.phase)
        flow.advance()
        assertTrue(flow.complete)
    }
}
