// Test
package com.jojo.game

import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.hall.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HallBuyUnitSummaryRenderPlanTest {
    @Test
    fun `buy unit summary preserves source order and authored geometry`() {
        val commands = HallBuyUnitSummaryRenderPlan.commands(
            HallBuyUnitSummaryView(
                portraitId = 12,
                name = "조조",
                postName = "군웅",
                level = 3,
                hitPoints = 120,
                magicPoints = 45,
                stats = listOf(
                    HallBuyUnitSummaryStat("공격력", 31), HallBuyUnitSummaryStat("정신력", 22),
                    HallBuyUnitSummaryStat("방어력", 18), HallBuyUnitSummaryStat("폭발력", 9),
                    HallBuyUnitSummaryStat("사기", 80), HallBuyUnitSummaryStat("이동력", 6),
                ),
            ),
        )

        assertEquals(
            listOf(
                HallBuyUnitSummaryDrawKind.PORTRAIT,
                HallBuyUnitSummaryDrawKind.TEXT,
                HallBuyUnitSummaryDrawKind.TEXT,
                HallBuyUnitSummaryDrawKind.TEXT,
                HallBuyUnitSummaryDrawKind.TEXT,
                HallBuyUnitSummaryDrawKind.PATCH,
                HallBuyUnitSummaryDrawKind.SPRITE,
                HallBuyUnitSummaryDrawKind.TEXT,
                HallBuyUnitSummaryDrawKind.TEXT,
                HallBuyUnitSummaryDrawKind.PATCH,
                HallBuyUnitSummaryDrawKind.TEXT,
            ),
            commands.take(11).map { it.kind },
        )
        assertEquals(12, commands.first().portraitId)
        assertEquals(706.77f, commands.first().x)
        assertEquals(357.81f, commands.first().y)
        assertEquals("Lv  3", commands[3].text)
        assertEquals(HallBuyUnitSummaryTint.MUTED, commands[6].tint)
        assertEquals("0/100", commands[7].text)
        assertEquals(Align.center, commands[7].align)
        assertEquals(
            listOf("HP", "120", "MP", "45", "공격력", "31", "정신력", "22", "방어력", "18", "폭발력", "9", "사기", "80", "이동력", "6"),
            commands.drop(8).filter { it.kind == HallBuyUnitSummaryDrawKind.TEXT }.map { it.text },
        )
    }
}
