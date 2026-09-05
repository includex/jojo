package com.jojo.game

import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.hall.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HallForcesRenderPlanTest {
    @Test
    fun `forces table keeps source geometry and row cell ordering`() {
        val commands = HallForcesRenderPlan.commands(
            HallForcesView(
                rows = listOf(
                    HallForcesRowView(listOf("조조", "보병", "3", "120/120", "45/45", "31", "18", "22", "9", "80")),
                ),
            ),
        )

        assertEquals(
            listOf(
                HallForcesDrawKind.TILED,
                HallForcesDrawKind.PATCH,
                HallForcesDrawKind.PATCH,
                HallForcesDrawKind.TITLE,
                HallForcesDrawKind.PATCH,
                HallForcesDrawKind.TEXT,
            ),
            commands.take(6).map { it.kind },
        )
        assertEquals("부대 정보 일람", commands[3].text)
        assertEquals(147.49f, commands[4].x)
        assertEquals(518.63f, commands[4].y)
        assertEquals("무장명", commands[5].text)
        assertEquals(Align.center, commands[5].align)

        val rowStart = 24
        assertEquals(HallForcesDrawKind.PATCH, commands[rowStart].kind)
        assertEquals(147.49f, commands[rowStart].x)
        assertEquals(469.63f, commands[rowStart].y)
        assertEquals("조조", commands[rowStart + 1].text)
        assertEquals(Align.left, commands[rowStart + 1].align)
        assertEquals("3", commands[rowStart + 5].text)
        assertEquals(Align.center, commands[rowStart + 5].align)
        assertEquals("폐쇄", commands.last().text)
        assertEquals(973.51f, commands.last().x)
        assertEquals(108.37f, commands.last().y)
    }
}
