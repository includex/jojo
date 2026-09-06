// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.hall.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HallEquipConfirmationRenderPlanTest {
    @Test fun `confirmation view snapshots eight source order values`() {
        val source = mutableListOf(10, -5, 0)
        val view = HallEquipConfirmationView.from(source, "장비")
        source[0] = 99

        assertEquals(listOf(10, -5, 0, 0, 0, 0, 0, 0), view.values)
        assertEquals("장비", view.actionLabel)
    }

    @Test fun `confirmation draw plan golden preserves reverse value-box source order`() {
        val commands = HallEquipConfirmationRenderPlan.commands(
            HallEquipConfirmationView.from(listOf(10, -5, 0, 2, 0, 0, 1, 0), "장비"),
        )

        val expected = buildList {
            addAll(listOf(
                "OVERLAY||",
                "PATCH|maps/ui/unit-info/bg1.png|",
                "PATCH|maps/ui/unit-info/box3.png|",
            ))
            listOf("0", "+1", "0", "0", "+2", "0", "-5", "+10").forEach { value ->
                add("PATCH|maps/ui/start-battle/box2.png|")
                add("TEXT||$value")
            }
            listOf("이동력", "사기", "폭발력", "방어력", "정신력", "공격력", "MP", "HP").forEach { add("TEXT||$it") }
            addAll(listOf(
                "PATCH|maps/ui/unit-info/box3.png|",
                "TEXT||장비",
                "PATCH|maps/ui/unit-info/box3.png|",
                "TEXT||취소",
            ))
        }
        assertEquals(expected, commands.map { "${it.kind}|${it.asset}|${it.text}" })
        assertEquals(HallEquipConfirmationTextColor.GREEN, commands.first { it.text == "-5" }.color)
        assertEquals(HallEquipConfirmationTextColor.RED, commands.first { it.text == "+10" }.color)
        assertEquals(879.977f, commands.first { it.text == "0" }.x)
    }
}
