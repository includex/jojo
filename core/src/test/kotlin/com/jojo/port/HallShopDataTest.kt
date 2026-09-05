package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HallShopDataTest {
    private val data = OriginalGameData.load()

    @Test
    fun `hall shop selects one ordinary equipment grade per raw type`() {
        assertEquals(
            listOf(0, 3, 6, 9, 12, 15, 18, 21, 24, 27, 70, 73, 76),
            data.hallBuyProfiles(stageIndex = 0, averageLevel = 3).map { it.id },
        )
        assertEquals(
            listOf(2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 72, 75, 78),
            data.hallBuyProfiles(stageIndex = 0, averageLevel = 30).map { it.id },
        )
    }

    @Test
    fun `item prices use the source hundred-unit purchase and 75 percent sale rules`() {
        val dagger = requireNotNull(data.equipmentProfile(0))
        assertEquals(500, data.purchasePrice(dagger))
        assertEquals(375, data.sellingPrice(dagger))
    }

    @Test
    fun `equipment confirmation preview is read only until accepted`() {
        val state = CampaignState()
        state.joinedUnits += 0
        state.setUnitAttribute(0, 18, 3)
        state.setEquipment(0, 1, 1, 1, 1, 1)
        state.addItem(3, level = 2, experience = 7)

        val beforeEquipment = state.equipment[0]
        val preview = assertNotNull(state.previewEquipInventoryItem(0, 3, data))
        assertEquals(8, preview.values.size)
        assertTrue(preview.values.any { it != 0 })
        assertEquals(beforeEquipment, state.equipment[0])
        assertEquals(1, state.items[3])

        assertNotNull(state.equipInventoryItem(0, 3, data))
        assertTrue(state.equipment[0] != beforeEquipment)
        assertEquals(null, state.items[3])
    }

    @Test
    fun `unequip confirmation preview is read only until accepted`() {
        val state = CampaignState()
        state.joinedUnits += 0
        state.setEquipment(0, 1, 1, 1, 1, 1)
        state.addItem(3, level = 2, experience = 7)
        assertNotNull(state.equipInventoryItem(0, 3, data))
        val equipped = state.equipment[0]

        val preview = assertNotNull(state.previewUnequipInventorySlot(0, CampaignState.EquipmentSlot.WEAPON, data))
        assertTrue(preview.values.any { it < 0 })
        assertEquals(equipped, state.equipment[0])
        assertEquals(null, state.items[3])

        assertTrue(state.unequipInventorySlot(0, CampaignState.EquipmentSlot.WEAPON))
        assertEquals(1, state.items[3])
    }

    @Test
    fun `equip route mutates only after the confirmation answer`() {
        val state = CampaignState()
        state.joinedUnits += 0
        state.setEquipment(0, 1, 1, 1, 1, 1)
        state.addItem(3)
        val flow = EquipConfirmationFlow(state, data)

        assertNotNull(flow.requestEquip(0, 3))
        assertEquals(1, state.items[3])
        assertEquals(false, flow.answer(0, accept = false))
        assertEquals(1, state.items[3])

        assertNotNull(flow.requestEquip(0, 3))
        assertTrue(flow.answer(0, accept = true))
        assertEquals(null, state.items[3])
    }
}
