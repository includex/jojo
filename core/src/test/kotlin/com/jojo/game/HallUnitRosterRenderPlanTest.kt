package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class HallUnitRosterRenderPlanTest {
    @Test fun `unit roster draw plan golden preserves chrome and source row order`() {
        val commands = HallUnitRosterRenderPlan.commands(
            HallUnitRosterView(
                listOf(HallUnitRosterRowView("조조", "군웅"), HallUnitRosterRowView("하후돈", "기병")),
            ),
        )

        assertEquals(
            listOf(
                "TILED|maps/ui/start-battle/logo9.png|",
                "SPRITE|maps/ui/start-battle/vline.png|",
                "PATCH|maps/ui/start-battle/box1.png|",
                "PATCH|maps/ui/start-battle/box2.png|",
                "TEXT||조조",
                "TEXT||군웅",
                "PATCH|maps/ui/start-battle/box2.png|",
                "TEXT||하후돈",
                "TEXT||기병",
            ),
            commands.map { "${it.kind}|${it.asset}|${it.text}" },
        )
        assertEquals(924.186f, commands[0].x)
        assertEquals(607f, commands[3].y)
        assertEquals(555f, commands[6].y)
    }

    @Test fun `unit roster plan caps presentation at six rows`() {
        val view = HallUnitRosterView((0 until 7).map { HallUnitRosterRowView("U$it", "P$it") })
        assertEquals(6, HallUnitRosterRenderPlan.commands(view).count { it.kind == HallUnitRosterDrawKind.PATCH && it.asset.endsWith("box2.png") })
    }
}
