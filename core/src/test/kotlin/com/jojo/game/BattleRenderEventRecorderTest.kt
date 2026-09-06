// Test
package com.jojo.game

import com.jojo.game.presentation.battle.evidence.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BattleRenderEventRecorderTest {
    @Test fun `initial route keeps battlefield before battle init chrome`() {
        val rows = BattleRenderEventRecorder.jsonl(
            BattleRenderEventView(
                phase = "battle-init",
                route = BattleRenderEventRoute.INIT,
                mapBottom = -560f,
                units = emptyList(),
            ),
        ).lineSequence().filter(String::isNotBlank).toList()

        assertEquals(9, rows.size)
        assertTrue(rows[0].contains("content/map"))
        assertTrue(rows[1].contains("menu_button/Background"))
        assertTrue(rows[2].contains("BattleInitLayer"))
        assertTrue(rows.last().contains("bg/label1"))
    }

    @Test fun `reward item route keeps authored labels and item rows in painter order`() {
        val json = BattleRenderEventRecorder.jsonl(
            BattleRenderEventView(
                phase = "battle-reward-items",
                route = BattleRenderEventRoute.REWARD,
                mapBottom = -560f,
                units = listOf(
                    BattleRenderEventUnitView(1f, 2f, 96f, "unit-asset", BattleRenderEventHealthBarView(5f, 6f, 44f, "Mark_5-1")),
                ),
                reward = BattleRenderEventRewardView(
                    BattleRenderEventRewardPhase.ITEMS,
                    items = listOf(BattleRenderEventRewardItemView("7-1", "검"), BattleRenderEventRewardItemView("8-1", "창")),
                ),
            ),
        )

        assertOrdered(json, "unit-asset", "Mark_5-1", "Canvas/Layer/Panel_cancel", "Canvas/Layer/bg1/label", "item0", "item1", "Canvas/Layer/bg/label1")
        assertEquals(22, json.lineSequence().count(String::isNotBlank))
    }

    @Test fun `full win condition route records supplied final-round text`() {
        val json = BattleRenderEventRecorder.jsonl(
            BattleRenderEventView(
                phase = "battle-win-condition-full",
                route = BattleRenderEventRoute.WIN_FULL,
                mapBottom = -96f,
                units = emptyList(),
                winConditions = BattleRenderEventWinConditionsView("left", "right", listOf("승리 조건", "장보와 장량을", "격퇴하십시오.", "제한 턴 수 18")),
            ),
        )

        assertOrdered(json, "Canvas/Layer/Panel_cancel", "left", "제한 턴 수 18", "right")
    }

    private fun assertOrdered(json: String, vararg fragments: String) {
        fragments.fold(-1) { previous, fragment ->
            val next = json.indexOf(fragment, previous + 1)
            assertTrue(next > previous, "expected $fragment after byte $previous")
            next
        }
    }
}
