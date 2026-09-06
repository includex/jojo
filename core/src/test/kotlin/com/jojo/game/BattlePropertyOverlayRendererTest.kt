// Test
package com.jojo.game

import com.jojo.game.presentation.battle.*
import com.jojo.game.presentation.battle.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattlePropertyOverlayRendererTest {
    @Test
    fun `property snapshot carries tab scroll labels and selection`() {
        val row = BattlePropertyRowView(icon = null, label = "검     관우     무기     4     MAX", selected = true)
        val view = BattlePropertyOverlayView(selectedTab = 0, firstRow = 3, rows = listOf(row))

        assertEquals(0, view.selectedTab)
        assertEquals(3, view.firstRow)
        assertEquals("검     관우     무기     4     MAX", view.rows.single().label)
        assertEquals(true, view.rows.single().selected)
    }
}
