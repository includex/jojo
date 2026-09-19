package com.jojo.game

import com.jojo.game.presentation.battle.overlay.SettlementInfoRenderContract as Contract
import kotlin.test.Test
import kotlin.test.assertEquals

class SettlementInfoPlacementTest {
    @Test fun `successive targets move the complete other panel by their node displacement`() {
        val first = Contract.placementOffset(Contract.Panel.OTHER, 784f, 336f, 1488.3721f, 800f)
        val second = Contract.placementOffset(Contract.Panel.OTHER, 688f, 240f, 1488.3721f, 800f)
        assertEquals(96f to 94.5f, first)
        assertEquals(0f to -1.5f, second)
    }

    @Test fun `right and bottom edges flip around the actor instead of clamping`() {
        for (panel in Contract.Panel.entries) {
            assertEquals(95f to -44f, Contract.placementOffset(panel, 1350f, 100f, 1488.3721f, 800f))
        }
    }

    @Test fun `top edge respects the different panel heights`() {
        assertEquals(-188f to 510.5f, Contract.placementOffset(Contract.Panel.OTHER, 500f, 790f, 1488.3721f, 800f))
        assertEquals(-188f to 446f, Contract.placementOffset(Contract.Panel.MINE, 500f, 790f, 1488.3721f, 800f))
    }
}
