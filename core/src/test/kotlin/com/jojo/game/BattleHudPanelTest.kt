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
    @Test fun `chapter artwork aligns title and objective geometry without corner overlap`() {
        for(width in listOf(1422f,1488f)) {
            val image=BattleHudPanelLayout.chapter(width,800f)
            val title=BattleHudPanelLayout.panel(width,800f,true)
            val mission=BattleHudPanelLayout.mission(width,800f)
            assertEquals(image.x,title.x)
            assertEquals(image.width,title.width)
            assertEquals(image.y+image.height,title.y+title.height)
            assertEquals(image.y,mission.y)
            assertTrue(mission.x+mission.width<=image.x+image.width*.61f)
            assertTrue(image.x+image.width<BattleHudPanelLayout.weather(width,800f).x)
        }
    }
    @Test fun `unit upper right corner exactly anchors card and cursor motion does not move it`() {
        val unit=BattleHudUnitView("임충","창병",1,61,96,22,22)
        val tile=BattleHudPanelLayout.Rect(500f,300f,96f,96f)
        val context=BattleHudContextView(unit=unit,unitAnchor=tile,pointerX=501f,pointerY=301f)
        val card=assertNotNull(BattleHudPanelLayout.hoverCard(1422f,800f,context))
        assertEquals(tile.x+tile.width,card.x)
        assertEquals(tile.y+tile.height,card.y)
        assertEquals(card,BattleHudPanelLayout.hoverCard(1422f,800f,context.copy(pointerX=590f,pointerY=390f)))
    }
    @Test fun `cards flip and slide at all viewport edges without covering the unit or HUD`() {
        val unit=BattleHudUnitView("임충","창병",1,61,96,22,22)
        fun overlaps(a:BattleHudPanelLayout.Rect,b:BattleHudPanelLayout.Rect)=
            a.x<b.x+b.width && a.x+a.width>b.x && a.y<b.y+b.height && a.y+a.height>b.y
        for(width in listOf(1422f,1488f)) {
            val hud=listOf(BattleHudPanelLayout.panel(width,800f,true),BattleHudPanelLayout.mission(width,800f),
                BattleHudPanelLayout.weather(width,800f),BattleHudPanelLayout.terrain(width,800f))+
                BattleHudPanelLayout.buttons(width,800f,true).take(4).map{it.rect}
            for(x in listOf(0f,12f,350f,width/2,width-108f,width-96f))
                for(y in listOf(0f,12f,64f,350f,608f,704f)) {
                    val tile=BattleHudPanelLayout.Rect(x,y,96f,96f)
                    val context=BattleHudContextView(unit=unit,unitAnchor=tile)
                    val card=assertNotNull(BattleHudPanelLayout.hoverCard(width,800f,context),"tile=$tile")
                    assertTrue(card.x>=0 && card.y>=0)
                    assertTrue(card.x+card.width<=width && card.y+card.height<=800f)
                    assertFalse(overlaps(card,tile),"card=$card tile=$tile")
                    assertTrue(hud.none{overlaps(card,it)},"card overlaps HUD: $card tile=$tile")
                    assertTrue(BattleHudPanelController().contains(card.x+card.width/2,card.y+card.height/2,width,800f,context))
                }
        }
        assertNull(BattleHudPanelLayout.hoverCard(1422f,800f,BattleHudContextView()))
    }
}
