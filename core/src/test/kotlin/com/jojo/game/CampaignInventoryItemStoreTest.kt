package com.jojo.game
import com.jojo.game.domain.campaign.CampaignInventory
import com.jojo.game.domain.campaign.CampaignInventoryItemStore
import com.jojo.game.domain.campaign.CampaignInventoryEquipment

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CampaignInventoryItemStoreTest {
    @Test fun `store preserves item order, clamps equipment instances, and consumes newest first`() {
        val store = CampaignInventoryItemStore()

        store.add(3, level = 0, experience = -1)
        store.add(6, level = 5, experience = 9)
        store.add(3, level = 2, experience = 7)
        store.add(150, count = 2, level = 8, experience = 10)
        store.add(-1)
        store.add(9, count = 0)

        assertEquals(listOf(3, 6, 150), store.items.keys.toList())
        assertEquals(listOf(1, 2), store.levels(3))
        assertEquals(listOf(0, 7), store.experiences(3))
        assertTrue(store.isEquipment(3))
        assertFalse(store.isEquipment(150))
        assertEquals(emptyList(), store.levels(150))
        assertEquals(CampaignInventoryEquipment(2, 7), store.takeNewestEquipment(3))
        assertEquals(listOf(1), store.levels(3))
        assertEquals(2, store.count(150))
    }

    @Test fun `store keeps discovery insertion order and resets all mutable collections`() {
        val store = CampaignInventoryItemStore()

        assertTrue(store.discover(9))
        assertFalse(store.discover(9))
        store.restoreDiscoveries(listOf(4, 9))
        store.add(3)
        store.removeStack(3)

        assertEquals(listOf(4, 9), store.discoveredTreasures.toList())
        assertNull(store.newestEquipment(3))
        assertFalse(store.consume(3))
        store.reset()
        assertTrue(store.items.isEmpty())
        assertTrue(store.discoveredTreasures.isEmpty())
    }
}
