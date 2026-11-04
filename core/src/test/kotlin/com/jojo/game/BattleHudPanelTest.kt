package com.jojo.game

import com.jojo.game.presentation.battle.overlay.*
import kotlin.test.*

class BattleHudPanelTest {
    @Test fun `corner HUD starts expanded and collapse hides only objective rows`() {
        val controller=BattleHudPanelController()
        assertTrue(controller.expanded)
        assertEquals(7,BattleHudPanelLayout.buttons(1488f,800f,true).size)
        controller.toggle();assertFalse(controller.expanded)
        assertEquals(7,BattleHudPanelLayout.buttons(1488f,800f,false).size)
    }
    @Test fun `corners leave the central battlefield open`() {
        for(width in listOf(1422f,1488f)) {
            val header=BattleHudPanelLayout.panel(width,800f,true)
            val weather=BattleHudPanelLayout.weather(width,800f)
            assertTrue(header.x+header.width<weather.x)
            assertFalse(BattleHudPanelController().contains(width/2f,400f,width,800f))
            val terrain=BattleHudPanelLayout.terrain(width,800f)
            val actions=BattleHudPanelLayout.buttons(width,800f,true).take(4)
            assertTrue(terrain.x+terrain.width<actions.first().rect.x)
        }
    }
    @Test fun `button labels and pointer geometry use the same reference layout`() {
        val controller=BattleHudPanelController()
        for(button in BattleHudPanelLayout.buttons(1488f,800f,true)) {
            val r=button.rect
            assertEquals(button.action,controller.buttonAt(r.x+r.width/2,r.y+r.height/2,1488f,800f))
        }
        assertNull(controller.buttonAt(700f,300f,1488f,800f))
    }
    @Test fun `floating unit card flips at viewport edge and disappears without a unit`() {
        val unit=BattleHudUnitView("임충","창병",1,61,96,22,22)
        for(x in listOf(0f,1422f))for(y in listOf(0f,800f)) {
            val context=BattleHudContextView(unit=unit,pointerX=x,pointerY=y)
            val card=assertNotNull(BattleHudPanelLayout.hoverCard(1422f,800f,context))
            assertTrue(card.x>=0 && card.y>=0)
            assertTrue(card.x+card.width<=1422f && card.y+card.height<=800f)
            assertTrue(BattleHudPanelController().contains(card.x+card.width/2,card.y+card.height/2,1422f,800f,context))
        }
        assertNull(BattleHudPanelLayout.hoverCard(1422f,800f,BattleHudContextView()))
    }
}
