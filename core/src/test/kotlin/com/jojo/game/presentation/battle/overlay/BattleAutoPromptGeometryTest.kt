package com.jojo.game.presentation.battle.overlay

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BattleAutoPromptGeometryTest {
    @Test
    fun `plain prompt uses source 180px buttons and has no delegation toggle`() {
        assertEquals(1, BattleAutoPromptGeometry.buttonAt(644.186f, 296.285f, offersDelegation = false))
        assertEquals(0, BattleAutoPromptGeometry.buttonAt(844.186f, 296.285f, offersDelegation = false))
        assertNull(BattleAutoPromptGeometry.buttonAt(964.536f, 296.285f, offersDelegation = false))
        assertFalse(BattleAutoPromptGeometry.toggleAt(530f, 290f, offersDelegation = false))
        assertEquals(844.186f to 296.285f, BattleAutoPromptGeometry.confirmCenter(offersDelegation = false))
    }

    @Test
    fun `plain prompt body blocks fullscreen cancel while outside background cancels`() {
        assertFalse(BattleAutoPromptGeometry.panelCancelAt(700f, 400f, offersDelegation = false))
        assertTrue(BattleAutoPromptGeometry.panelCancelAt(100f, 400f, offersDelegation = false))
        assertTrue(BattleAutoPromptGeometry.panelCancelAt(700f, 700f, offersDelegation = false))
    }

    @Test
    fun `manual MsgBox4 keeps its authored button and toggle hit areas`() {
        assertEquals(1, BattleAutoPromptGeometry.buttonAt(749.536f, 295.197f, offersDelegation = true))
        assertEquals(0, BattleAutoPromptGeometry.buttonAt(919.536f, 295.197f, offersDelegation = true))
        assertTrue(BattleAutoPromptGeometry.toggleAt(530f, 290f, offersDelegation = true))
        assertEquals(919.536f to 295.197f, BattleAutoPromptGeometry.confirmCenter(offersDelegation = true))
        assertTrue(BattleAutoPromptGeometry.panelCancelAt(700f, 400f, offersDelegation = true))
    }
}
