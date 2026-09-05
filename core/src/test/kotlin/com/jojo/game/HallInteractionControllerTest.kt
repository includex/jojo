package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HallInteractionControllerTest {
    @Test
    fun menuOutsideTapClosesWhileAValidIconEmitsItsIntent() {
        val controller = HallInteractionController()

        assertIs<HallInteractionIntent.OpenMenu>(controller.mainTap(50f, 340f))
        assertEquals(true, controller.view.menuOpen)
        assertIs<HallInteractionIntent.MenuClosed>(controller.mainTap(50f, 126f))
        assertEquals(false, controller.view.menuOpen)

        controller.openMenu()
        val selection = assertIs<HallInteractionIntent.MenuSelection>(controller.mainTap(123.29f, 80f))
        assertEquals(1, selection.index)
        assertEquals(false, controller.view.menuOpen)
    }

    @Test
    fun tabHitTestsUpdateOnlyTheirOwnViewState() {
        val controller = HallInteractionController()

        assertEquals(true, controller.selectEquipTabAt(390f, 580f))
        assertEquals(true, controller.selectBuyTabAt(400f, 540f))
        assertEquals(true, controller.selectSellTabAt(800f, 100f))

        assertEquals(2, controller.view.equipTabIndex)
        assertEquals(1, controller.view.buyTabIndex)
        assertEquals(1, controller.view.sellTabIndex)
    }
}
