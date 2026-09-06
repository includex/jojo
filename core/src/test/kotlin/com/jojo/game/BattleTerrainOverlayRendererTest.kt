// Test
package com.jojo.game

import com.jojo.game.presentation.battle.*
import com.jojo.game.presentation.battle.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleTerrainOverlayRendererTest {
    @Test
    fun `terrain snapshot preserves arm headers and visible row order`() {
        val view = BattleTerrainOverlayView(
            armNames = listOf("관", "장"),
            rows = listOf(
                BattleTerrainRowView(
                    terrainName = "평지",
                    icon = null,
                    enabledSkills = listOf(true, false, true, false),
                    values = listOf(BattleTerrainValueView("★", 0), BattleTerrainValueView("◎", 1)),
                ),
                BattleTerrainRowView(
                    terrainName = "산지",
                    icon = null,
                    enabledSkills = listOf(false, true, false, true),
                    values = listOf(BattleTerrainValueView("○", 2), BattleTerrainValueView("--", null)),
                ),
            ),
        )

        assertEquals(listOf("관", "장"), view.armNames)
        assertEquals(listOf("평지", "산지"), view.rows.map(BattleTerrainRowView::terrainName))
        assertEquals(listOf(true, false, true, false), view.rows.first().enabledSkills)
        assertEquals("--", view.rows.last().values.last().text)
    }
}
