package com.jojo.game
import com.jojo.game.presentation.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleSettingsOverlayRendererTest {
    @Test
    fun `settings snapshot keeps independent option selections`() {
        val view = BattleSettingsOverlayView(
            flags = 0b10101,
            msgSpeed = 2,
            notifyLevel = 1,
            background = 3,
        )

        assertEquals(0b10101, view.flags)
        assertEquals(2, view.msgSpeed)
        assertEquals(1, view.notifyLevel)
        assertEquals(3, view.background)
    }
}
