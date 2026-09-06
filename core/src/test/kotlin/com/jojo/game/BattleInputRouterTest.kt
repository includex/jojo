// Test
package com.jojo.game

import com.jojo.game.presentation.battle.input.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BattleInputRouterTest {
    @Test
    fun `modal precedence wins over map and hit testing is immutable`() {
        val router = BattleInputRouter()
        val surface = BattleInputSurface(
            dialogue = true,
            battleMenu = true,
            hitRegions = listOf(BattleInputHitRegion(BattleInputTarget.MENU_HUD, 0f, 0f, 10f, 10f)),
        )
        val down = assertIs<BattleInputIntent.PointerDown>(router.pointerDown(5f, 5f, surface))
        assertEquals(BattleInputCapture.DIALOGUE, down.capture)
        assertEquals(BattleInputTarget.MENU_HUD, down.target)
        assertEquals(BattleInputCapture.DIALOGUE, surface.pointerCapture())
    }

    @Test
    fun `press release only matches same target and drag suppresses map click`() {
        val router = BattleInputRouter()
        val map = BattleInputSurface()
        router.pointerDown(20f, 20f, map)
        router.pointerDragged(25f, 20f, map)
        val up = assertIs<BattleInputIntent.PointerUp>(router.pointerUp(25f, 20f, map))
        assertEquals(BattleInputCapture.MAP, up.pressedCapture)
        assertTrue(up.moved)
        assertFalse(up.moved.not())

        router.pointerDown(1f, 1f, map)
        val click = assertIs<BattleInputIntent.PointerUp>(router.pointerUp(1f, 1f, map))
        assertEquals(BattleInputCapture.MAP, click.pressedCapture)
        assertFalse(click.moved)
    }

    @Test
    fun `keyboard follows dialogue then modal then player precedence`() {
        val router = BattleInputRouter()
        assertEquals(
            BattleInputCapture.DIALOGUE,
            router.keyDown(13, BattleInputSurface(dialogue = true, resultPrompt = true)).capture,
        )
        assertEquals(
            BattleInputCapture.RESULT,
            router.keyDown(13, BattleInputSurface(resultPrompt = true)).capture,
        )
        assertEquals(
            BattleInputCapture.PLAYER,
            router.keyDown(13, BattleInputSurface()).capture,
        )
    }
}
