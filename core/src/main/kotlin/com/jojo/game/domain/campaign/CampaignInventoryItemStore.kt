// Campaign
package com.jojo.game.domain.campaign
import com.jojo.game.*

import java.util.*

internal data class CampaignInventoryEquipment(val level: Int = 1, val experience: Int = 0)

internal class CampaignInventoryItemStore {
    private val itemStacks = linkedMapOf<Int, Int>()
    val items: Map<Int, Int> = Collections.unmodifiableMap(itemStacks)

    private val discoveredTreasureIds = linkedSetOf<Int>()
    val discoveredTreasures: Set<Int> = Collections.unmodifiableSet(discoveredTreasureIds)

    private val equipmentInstances = linkedMapOf<Int, MutableList<CampaignInventoryEquipment>>()

    fun reset() {
        itemStacks.clear()
        discoveredTreasureIds.clear()
        equipmentInstances.clear()
    }

    fun add(itemId: Int, count: Int = 1, level: Int = 1, experience: Int = 0) {
        if (itemId < 0 || count <= 0) return
        itemStacks[itemId] = (itemStacks[itemId] ?: 0) + count
        if (itemId !in ITEM_PROPERTY_FIRST..ITEM_PROPERTY_LAST) {
            val instances = equipmentInstances.getOrPut(itemId) { mutableListOf() }
            repeat(count) {
                instances += CampaignInventoryEquipment(level.coerceAtLeast(1), experience.coerceAtLeast(0))
            }
        }
    }

    fun consume(itemId: Int): Boolean {
        val count = itemStacks[itemId] ?: return false
        if (count < 1) return false
        if (count == 1) itemStacks.remove(itemId) else itemStacks[itemId] = count - 1
        return true
    }

    fun removeStack(itemId: Int) {
        itemStacks.remove(itemId)
        equipmentInstances.remove(itemId)
    }

    fun restoreDiscoveries(itemIds: Iterable<Int>) {
        discoveredTreasureIds.clear()
        discoveredTreasureIds.addAll(itemIds)
    }

    fun discover(itemId: Int): Boolean = discoveredTreasureIds.add(itemId)

    fun count(itemId: Int): Int = itemStacks[itemId] ?: 0

    fun levels(itemId: Int): List<Int> = equipmentInstances[itemId].orEmpty().map { it.level }

    fun experiences(itemId: Int): List<Int> = equipmentInstances[itemId].orEmpty().map { it.experience }

    fun newestEquipment(itemId: Int): CampaignInventoryEquipment? = equipmentInstances[itemId]?.lastOrNull()

    fun takeNewestEquipment(itemId: Int): CampaignInventoryEquipment? {
        val instances = equipmentInstances[itemId] ?: return null
        val instance = instances.removeLastOrNull()
        if (instances.isEmpty()) equipmentInstances.remove(itemId)
        return instance
    }

    fun isEquipment(itemId: Int): Boolean = itemId !in ITEM_PROPERTY_FIRST..ITEM_PROPERTY_LAST

    private companion object {
        const val ITEM_PROPERTY_FIRST = 150
        const val ITEM_PROPERTY_LAST = 254
    }
}
