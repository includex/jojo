// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.hall.HallEquipmentRenderPlan
import com.jojo.game.presentation.scenario.hall.HallPropertyRenderPlan
import kotlin.test.Test
import kotlin.test.assertEquals

class HallEquipmentRenderPlanTest {
    @Test
    fun `equip plan preserves inventory and clipped slot geometry in source order`() {
        assertEquals(515f, HallEquipmentRenderPlan.inventoryRowY(0))
        assertEquals(379f, HallEquipmentRenderPlan.inventoryRowY(2))
        assertEquals(-114.91f, HallEquipmentRenderPlan.slotY(1))
        assertEquals(
            listOf("background", "frame", "title", "footer", "tabs", "inventory-frame", "unit-header", "inventory-0", "inventory-1", "unit-summary", "slot-0", "slot-1", "notice"),
            HallEquipmentRenderPlan.paintOrder(inventoryCount = 2, slotCount = 2),
        )
    }

    @Test
    fun `property plan preserves header rows tabs then confirm ordering`() {
        assertEquals(481.58f, HallPropertyRenderPlan.rowY(0))
        assertEquals(481.58f - 2 * 67.08f, HallPropertyRenderPlan.rowY(2))
        assertEquals(
            listOf("background", "frame", "title", "headers", "row-0", "row-1", "tabs", "confirm"),
            HallPropertyRenderPlan.paintOrder(rowCount = 2),
        )
    }
}
