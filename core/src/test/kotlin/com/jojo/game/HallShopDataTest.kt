package com.jojo.game
import com.jojo.game.domain.campaign.*

import com.jojo.game.domain.campaign.CampaignEquipmentSlot

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * class  `HallShopDataTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class HallShopDataTest {
    private val data = GameDataCatalog.load()

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
        state.inventory.setEquipment(0, 1, 1, 1, 1, 1)
        state.inventory.addItem(3, level = 2, experience = 7)

        val beforeEquipment = state.inventory.equipment[0]
        val preview = assertNotNull(state.inventory.previewEquipInventoryItem(0, 3, data))
        assertEquals(8, preview.values.size)
        assertTrue(preview.values.any { it != 0 })
        assertEquals(beforeEquipment, state.inventory.equipment[0])
        assertEquals(1, state.inventory.items[3])

        assertNotNull(state.inventory.equipInventoryItem(0, 3, data))
        assertTrue(state.inventory.equipment[0] != beforeEquipment)
        assertEquals(null, state.inventory.items[3])
    }

    @Test
    fun `unequip confirmation preview is read only until accepted`() {
        val state = CampaignState()
        state.joinedUnits += 0
        state.inventory.setEquipment(0, 1, 1, 1, 1, 1)
        state.inventory.addItem(3, level = 2, experience = 7)
        assertNotNull(state.inventory.equipInventoryItem(0, 3, data))
        val equipped = state.inventory.equipment[0]

        val preview = assertNotNull(state.inventory.previewUnequipInventorySlot(0, CampaignEquipmentSlot.WEAPON, data))
        assertTrue(preview.values.any { it < 0 })
        assertEquals(equipped, state.inventory.equipment[0])
        assertEquals(null, state.inventory.items[3])

        assertTrue(state.inventory.unequipInventorySlot(0, CampaignEquipmentSlot.WEAPON))
        assertEquals(1, state.inventory.items[3])
    }

    @Test
    fun `equip route mutates only after the confirmation answer`() {
        val state = CampaignState()
        state.joinedUnits += 0
        state.inventory.setEquipment(0, 1, 1, 1, 1, 1)
        state.inventory.addItem(3)
        val flow = EquipConfirmationFlow(state, data)

        assertNotNull(flow.requestEquip(0, 3))
        assertEquals(1, state.inventory.items[3])
        assertEquals(false, flow.answer(0, accept = false))
        assertEquals(1, state.inventory.items[3])

        assertNotNull(flow.requestEquip(0, 3))
        assertTrue(flow.answer(0, accept = true))
        assertEquals(null, state.inventory.items[3])
    }
}
