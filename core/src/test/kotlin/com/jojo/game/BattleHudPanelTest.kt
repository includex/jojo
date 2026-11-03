package com.jojo.game

import com.jojo.game.presentation.battle.overlay.BattleHudPanelController
import com.jojo.game.presentation.battle.overlay.BattleHudPanelLayout
import kotlin.test.*

class BattleHudPanelTest {
    @Test fun `HUD starts expanded and collapse never opens a modal menu`() {
        val controller=BattleHudPanelController()
        assertTrue(controller.expanded)
        assertEquals(5,BattleHudPanelLayout.buttons(1488f,800f,true).size)
        controller.toggle()
        assertFalse(controller.expanded)
        assertEquals(1,BattleHudPanelLayout.buttons(1488f,800f,false).size)
        controller.toggle()
        assertTrue(controller.expanded)
    }
    @Test fun `top panel buttons share drawing and pointer geometry`() {
        val controller=BattleHudPanelController()
        for(button in BattleHudPanelLayout.buttons(1488f,800f,true)) {
            val r=button.rect
            assertEquals(button.action,controller.buttonAt(r.x+r.width/2,r.y+r.height/2,1488f,800f))
        }
        assertNull(controller.buttonAt(700f,300f,1488f,800f))
        assertFalse(controller.contains(700f,300f,1488f,800f))
    }
}
