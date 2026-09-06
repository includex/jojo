// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.presentation.scenario.hall.HallManagementViewFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HallManagementRenderProjectionTest {
    private val catalog = GameDataCatalog.load()

    @Test
    fun `equip projection provides ready labels stats slots and inventory rows`() {
        val item = catalog.allEquipmentProfiles().first { it.itemType in 0..19 && it.price != 255 }
        val campaign = CampaignState().apply {
            joinedUnits += 0
            unitNames[0] = "시험 조조"
            inventory.addItem(item.id, count = 2)
        }

        val view = HallManagementViewFactory(campaign, catalog, "R_00", null).equip(0, 1, "장비 알림")

        assertEquals("시험 조조", view.unit.name)
        assertEquals(8, view.unit.stats.size)
        assertEquals("장비 알림", view.notice)
        assertEquals("${item.name}  ×2", view.inventoryRows.single().name)
        assertEquals(catalog.equipmentTypeName(item.itemType), view.inventoryRows.single().typeName)
    }

    @Test
    fun `property projection retains selected tab and ready item display rows`() {
        val item = catalog.allEquipmentProfiles().first { it.id >= 150 || it.itemType in 26..45 }
        val campaign = CampaignState().apply { inventory.addItem(item.id, count = 2) }

        val factory = HallManagementViewFactory(campaign, catalog, "R_00", null)
        val property = factory.property(selectedTab = 3)

        assertEquals(3, property.selectedTab)
        assertTrue(property.rows.any { it.itemId == item.id && it.name == "${item.name} ×2" && it.owner == "창고" })
        assertTrue(factory.propertyItemIds(3).contains(item.id))
    }
}
