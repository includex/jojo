package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class HallTreasureRenderPlanTest {
    @Test fun `treasure draw plan golden preserves discovery visibility and card ordering`() {
        val commands = HallTreasureRenderPlan.commands(
            HallTreasureView(
                entries = listOf(
                    HallTreasureEntryView("첫보물", 4, true),
                    HallTreasureEntryView("둘째보물", 9, false),
                ),
                discoveredCount = 2,
                totalCount = 13,
            ),
        )

        assertEquals(
            listOf(
                "TILED|maps/ui/start-battle/logo9.png|",
                "PATCH|maps/ui/start-battle/box1.png|",
                "PATCH|maps/ui/start-battle/title.png|",
                "TEXT||보물 도감",
                "PATCH|maps/ui/start-battle/box2.png|",
                "PATCH|maps/ui/start-battle/box2.png|",
                "SPRITE|maps/item-icons/4.png|",
                "TEXT||첫보물",
                "PATCH|maps/ui/start-battle/box2.png|",
                "PATCH|maps/ui/start-battle/box2.png|",
                "TEXT||발견되지 않음",
                "TEXT||지금까지 발견한 보물 02 / 13",
                "PATCH|maps/ui/start-battle/button.png|",
                "TEXT||종료",
            ),
            commands.map { "${it.kind}|${it.asset}|${it.text}" },
        )
        assertEquals(232.1f, commands[4].x)
        assertEquals(642.32f, commands[8].x)
        assertEquals(413.23f, commands[4].y)
        assertEquals(9, commands[12].inset)
    }
}
