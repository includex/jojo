package com.jojo.game
import com.jojo.game.presentation.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleAutoOverlayRendererTest {
    @Test
    fun `prompt snapshot carries toggle state without mutable flow dependency`() {
        val view = BattleAutoOverlayView(
            overlay = BattleAutoOverlayKind.PROMPT,
            checked = true,
        )

        assertEquals(BattleAutoOverlayKind.PROMPT, view.overlay)
        assertEquals(true, view.checked)
    }

    @Test
    fun `tuoguan and none are distinct render states`() {
        assertEquals(
            BattleAutoOverlayKind.TUOGUAN,
            BattleAutoOverlayView(BattleAutoOverlayKind.TUOGUAN).overlay,
        )
        assertEquals(
            BattleAutoOverlayKind.NONE,
            BattleAutoOverlayView(BattleAutoOverlayKind.NONE).overlay,
        )
    }
}
