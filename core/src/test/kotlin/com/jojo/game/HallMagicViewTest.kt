// Test
package com.jojo.game

import com.jojo.game.presentation.shared.overlay.MagicUiList

import com.jojo.game.presentation.scenario.hall.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HallMagicViewTest {
    @Test fun `view snapshots magic values and one based sprite frames`() {
        val view = HallMagicView.from(
            MagicUiList.Magic(39, "소량의 보급품", 6, null, 7, 13, 0, "회복합니다."),
        )

        assertEquals("소량의 보급품", view.name)
        assertEquals(0, view.power)
        assertEquals(6, view.cost)
        assertEquals("회복합니다.", view.intro)
        assertEquals(8, view.iconFrame)
        assertEquals(14, view.hitAreaFrame)
        assertEquals(1, view.effectAreaFrame)
    }
}
