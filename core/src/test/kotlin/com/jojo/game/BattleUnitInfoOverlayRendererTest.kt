package com.jojo.game

import com.jojo.game.presentation.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleUnitInfoOverlayRendererTest {
    @Test
    fun `unit info snapshot preserves selected tab and unit presentation values`() {
        val view = BattleUnitInfoOverlayView(
            tab = 3,
            unit = BattleUnitInfoUnitView(
                name = "관우",
                post = "무장",
                level = 12,
                hp = 80,
                maxHp = 100,
                mp = 20,
                maxMp = 30,
                attack = 55,
                defense = 44,
                spirit = 33,
                critical = 22,
                morale = 11,
            ),
            buttons = listOf(false, true, true, false, true, true, true, true, false, true),
            magicRows = listOf("화계", "수계"),
        )

        assertEquals(3, view.tab)
        assertEquals("관우", view.unit.name)
        assertEquals(80, view.unit.hp)
        assertEquals(listOf("화계", "수계"), view.magicRows)
        assertEquals(true, view.buttons[9])
    }
}
