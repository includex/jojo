package com.jojo.game

import com.jojo.game.presentation.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleForcesOverlayRendererTest {
    @Test
    fun `forces snapshot preserves table values and selected tab`() {
        val row = BattleForcesRowView(
            listOf("관우", "장군", "12", "100/120", "30/40", "80", "70", "60", "5", "90")
        )
        val view = BattleForcesOverlayView(selectedTab = 1, rows = listOf(row), tabsVisible = true)

        assertEquals(1, view.selectedTab)
        assertEquals(true, view.tabsVisible)
        assertEquals(row.values, view.rows.single().values)
    }
}
