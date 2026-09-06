// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.hall.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HallRendererTest {
    @Test
    fun hallViewAllowsMissingAssetsAsTheExistingFallback() {
        val view = HallViewState(null, null, null, null, null)

        assertEquals(null, view.menuTexture)
        assertEquals(null, view.battleTexture)
        assertEquals(null, view.equipTexture)
        assertEquals(null, view.buyTexture)
        assertEquals(null, view.sellTexture)
    }
}
