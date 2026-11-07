package com.jojo.game.presentation.battle.assets

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BattleSpriteResolutionTest {
    @Test fun `native movement and attack frame coordinates map without gutters or size changes`() {
        for ((native, size, count) in listOf(Triple(48,62,11), Triple(64,83,12))) {
            val mapping=BattleSpriteResolution(native,native+2,1,size,size+2,1,count)
            for (index in 0 until count) {
                val rect=mapping.sourceRect(index*(native+2)+1)
                assertEquals(BattleSpriteSourceRect(0,index*(size+2)+1,size,size),rect)
                assertEquals(1,rect.y % (size+2))
                assertEquals(size+1,(rect.y+rect.height) % (size+2))
            }
            assertEquals(mapping.sourceRect(1),mapping.sourceRect(count*(native+2)+1))
        }
    }
    @Test fun `variants are optional and isolated from portraits and native source paths`() {
        assertEquals("maps/units/runtime-1_3/atk2/622.png",BattleSpriteResolution.variantPath("maps/units/atk2/622.png"))
        assertNull(BattleSpriteResolution.variantPath("maps/heads/608.png"))
        assertNull(BattleSpriteResolution.variantPath("maps/units/runtime-1_3/atk2/622.png"))
    }
}
