package com.jojo.game

import com.jojo.game.presentation.battle.overlay.BattlePickerGeometry
import kotlin.test.*

class BattlePickerGeometryTest {
    @Test fun `all ten magic cards fit their panel and use matching pointer targets`() {
        for(index in 0..9) {
            val card=BattlePickerGeometry.magicCard(index)
            assertTrue(card.y>=150.5f && card.y+card.height<=649.5f)
            assertTrue(card.x>=478.186f && card.x+card.width<=1010.186f)
            assertEquals(index,BattlePickerGeometry.magicAt(card.x+card.width/2,card.y+card.height/2,10))
        }
        assertNull(BattlePickerGeometry.magicAt(600f,100f,10))
        val last=BattlePickerGeometry.magicCard(9)
        assertNull(BattlePickerGeometry.magicAt(last.x+10,last.y+10,9))
    }
}
