package com.jojo.game

import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.presentation.scenario.hall.HallManagementViewFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HallManagementViewFactoryTest {
    private val catalog = GameDataCatalog.load()

    @Test
    fun `factory projects buy catalog and selected unit without preparing inventory`() {
        val campaign = CampaignState().apply {
            joinedUnits += 0
            unitNames[0] = "시험 조조"
            setUnitAttribute(0, 18, 3)
        }
        val factory = HallManagementViewFactory(campaign, catalog, "R_00", overlayFixture = null)
        val candidates = factory.buyCandidates()

        campaign.inventory.addItem(candidates.first().id)
        val catalogView = factory.buyCatalog(buyTabIndex = 0)
        val summary = factory.buyUnitSummary(unitId = 0)

        assertEquals(candidates.take(3).map { it.name }, catalogView.rows.map { it.name })
        assertEquals(1, catalogView.rows.first().inventory)
        assertEquals("시험 조조", summary.name)
        assertEquals(6, summary.stats.size)
        assertNull(campaign.inventory.equipment[0])
    }

    @Test
    fun `factory keeps fixture catalog and sell equip filters in immutable projections`() {
        val campaign = CampaignState()
        val equipment = catalog.allEquipmentProfiles().first { catalog.equipmentCategory(it) <= 2 && it.price != 255 }
        val property = catalog.allEquipmentProfiles().first { catalog.equipmentCategory(it) == 3 && it.price != 255 }
        campaign.inventory.addItem(equipment.id, count = 2)
        campaign.inventory.addItem(property.id)
        val factory = HallManagementViewFactory(campaign, catalog, "R_00", overlayFixture = "buy")

        assertEquals(listOf(254, 253, 252), factory.buyCandidates().take(3).map { it.id })
        assertTrue(factory.buyProperties().zipWithNext().all { (first, second) -> first.id < second.id })
        assertEquals(listOf(equipment.id), factory.sellCandidates(sellTabIndex = 0).map { it.itemId })
        assertEquals(listOf(property.id), factory.sellCandidates(sellTabIndex = 1).map { it.itemId })
        assertEquals("판매 알림", factory.sell(sellTabIndex = 0, notice = "판매 알림").notice)
        assertTrue(factory.equipInventory(equipTabIndex = 0).all { item ->
            (catalog.equipmentProfile(item.itemId)?.itemType ?: 255) < 150
        })
    }

    @Test
    fun `factory projects forces from campaign read model`() {
        val campaign = CampaignState().apply {
            joinedUnits += 0
            unitNames[0] = "부대 조조"
            setUnitAttribute(0, 18, 4)
        }
        val factory = HallManagementViewFactory(campaign, catalog, "R_00", overlayFixture = null)

        val forces = factory.forces()

        assertEquals(1, forces.rows.size)
        assertEquals("부대 조조", forces.rows.single().values.first())
        assertEquals(10, forces.rows.single().values.size)
    }
}
