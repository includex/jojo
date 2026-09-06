// Test
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BattleHelperOverlayControllerTest {
    @Test
    fun `confirmation requires a press and release inside the helper button`() {
        val controller = BattleHelperOverlayController()

        controller.open(model())
        assertEquals("<color=#000000>guide</color><br/>", controller.view()?.richText)

        controller.dispatch(BattleHelperOverlayController.Intent.PointerDown(100f, 100f))
        controller.dispatch(BattleHelperOverlayController.Intent.PointerUp(1200f, 60f))
        assertEquals("확인", controller.view()?.buttonText)

        controller.dispatch(BattleHelperOverlayController.Intent.PointerDown(1200f, 60f))
        controller.dispatch(BattleHelperOverlayController.Intent.PointerUp(1200f, 60f))
        assertNull(controller.view())
    }

    private fun model() = object : HelperLayer.Model {
        override fun getInfo() = listOf(HelperLayer.Info(1, text = "guide"))

        override fun replaceSpeInfo(text: String, flags: Int) = text
    }
}
