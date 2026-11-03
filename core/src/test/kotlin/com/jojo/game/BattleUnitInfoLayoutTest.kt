package com.jojo.game

import com.jojo.game.presentation.battle.overlay.BattleUnitInfoLayout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BattleUnitInfoLayoutTest {
    @Test fun `drawn buttons map to the same input targets without overlap`() {
        val targets = BattleUnitInfoLayout.tabs.mapIndexed { i,r -> i to r } + listOf(
            5 to BattleUnitInfoLayout.previous, 6 to BattleUnitInfoLayout.next,
            7 to BattleUnitInfoLayout.close, 9 to BattleUnitInfoLayout.rates,
        )
        targets.forEach { (id,r) ->
            assertEquals(id,BattleUnitInfoLayout.buttonAt(r.x+r.width/2,r.y+r.height/2))
            targets.filter { it.first != id }.forEach { (_,other) ->
                assertTrue(r.x+r.width <= other.x || other.x+other.width <= r.x ||
                    r.y+r.height <= other.y || other.y+other.height <= r.y)
            }
        }
        assertNull(BattleUnitInfoLayout.buttonAt(12f,12f))
    }
}
