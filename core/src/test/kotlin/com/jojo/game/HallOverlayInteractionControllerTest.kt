// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.hall.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HallOverlayInteractionControllerTest {
    private val controller = HallOverlayInteractionController()
    @Test fun `info routes retain close before content hit testing`() {
        assertEquals(HallInfoInputIntent.Close, controller.infoTap(HallInfoInputKind.PROPERTY, 950f, 60f))
        assertEquals(HallInfoInputIntent.SelectPropertyTab(1), controller.infoTap(HallInfoInputKind.PROPERTY, 380f, 80f))
    }
    @Test fun `forces property terrain and treasure retain source row geometry`() {
        assertEquals(HallInfoInputIntent.OpenForcesRow(2), controller.infoTap(HallInfoInputKind.FORCES, 200f, 365f))
        assertEquals(HallInfoInputIntent.OpenPropertyRow(1), controller.infoTap(HallInfoInputKind.PROPERTY, 300f, 420f))
        assertEquals(HallInfoInputIntent.SelectTerrainTab(1), controller.infoTap(HallInfoInputKind.TERRAIN, 500f, 120f))
        assertEquals(HallInfoInputIntent.OpenTreasureRow(3), controller.infoTap(HallInfoInputKind.TREASURE, 650f, 250f))
    }
    @Test fun `nested overlay hit testing emits effects without owning layers`() {
        assertEquals(HallLayerTapIntent.PRIMARY, controller.exclusiveTap(160f, 60f))
        assertEquals(HallLayerTapIntent.CLOSE, controller.magicTap(900f, 160f))
        assertEquals(HallLayerTapIntent.PRIMARY, controller.unitInfoTap(550f, 60f))
        assertEquals(HallLayerTapIntent.CANCEL, controller.featsTap(10f, 10f, helpOpen = false))
    }
}
