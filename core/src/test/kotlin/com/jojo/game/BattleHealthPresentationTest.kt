package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleHealthPresentationTest {
    @Test
    fun `HP display remains previous value until hit then uses only scheduled stage value`() {
        val presentation = BattleHealthPresentation()
        presentation.schedule("target", fromHp = 100, toHp = 70, revealAt = 1.25f)

        assertEquals(100, presentation.shownHp("target", 1.24f, fallbackHp = 40))
        assertEquals(70, presentation.shownHp("target", 1.25f, fallbackHp = 40))
        // A later continuous attack replaces 70→40 without exposing the
        // final tactical value before its own authored hit event.
        presentation.schedule("target", fromHp = 70, toHp = 40, revealAt = 2f)
        assertEquals(70, presentation.shownHp("target", 1.9f, fallbackHp = 40))
        assertEquals(40, presentation.shownHp("target", 2f, fallbackHp = 40))
    }
}
