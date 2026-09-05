package com.jojo.game.domain.campaign

import com.jojo.game.GameDataCatalog

/** Aggregate for item stacks, discoveries, and equipped loadouts. */
class CampaignInventory internal constructor(
    private val joinedUnitIds: () -> Iterable<Int>,
    private val unitAttribute: (unitId: Int, attribute: Int, default: Int) -> Int,
) : CampaignEquipmentRepository {
    internal constructor() : this({ emptyList() }, { _, _, default -> default })

    private val itemStore = CampaignInventoryItemStore()
    private val equipmentManager = CampaignInventoryEquipmentManager(itemStore, joinedUnitIds, unitAttribute)

    val items: Map<Int, Int> get() = itemStore.items
    val discoveredTreasures: Set<Int> get() = itemStore.discoveredTreasures
    val equipment: Map<Int, CampaignEquipment> get() = equipmentManager.equipment

    internal fun reset() {
        itemStore.reset()
        equipmentManager.reset()
    }

    fun addItem(itemId: Int, count: Int = 1, level: Int = 1, experience: Int = 0) =
        itemStore.add(itemId, count, level, experience)

    /** Records an acquired, unpriced treasure independently from inventory. */
    fun discoverTreasure(itemId: Int, data: GameDataCatalog): Boolean {
        val item = data.equipmentProfile(itemId) ?: return false
        if (!item.treasure || item.price != 255) return false
        return itemStore.discover(itemId)
    }

    fun consumeItem(itemId: Int): Boolean = itemStore.consume(itemId)

    /** Removes a complete stack when deterministic battle fixtures replace their property seed. */
    internal fun removeItemStack(itemId: Int) = itemStore.removeStack(itemId)

    /** Rehydrates the persisted discovery order after aggregate reset. */
    internal fun restoreDiscoveredTreasures(itemIds: Iterable<Int>) = itemStore.restoreDiscoveries(itemIds)

    fun itemLevels(itemId: Int): List<Int> = itemStore.levels(itemId)
    fun itemExperiences(itemId: Int): List<Int> = itemStore.experiences(itemId)

    fun setEquipment(unitId: Int, weapon: Int, weaponLevel: Int, armor: Int, armorLevel: Int, auxiliary: Int) =
        equipmentManager.setEquipment(unitId, weapon, weaponLevel, armor, armorLevel, auxiliary)

    fun setEquipment(unitId: Int, value: CampaignEquipment) = equipmentManager.setEquipment(unitId, value)

    fun ensureDefaultEquipment(unitId: Int, data: GameDataCatalog) =
        equipmentManager.ensureDefaultEquipment(unitId, data)

    fun equippedItems(): List<CampaignEquippedItem> = equipmentManager.equippedItems()

    fun previewEquipInventoryItem(unitId: Int, itemId: Int, data: GameDataCatalog): CampaignEquipPreview? =
        equipmentManager.previewEquipInventoryItem(unitId, itemId, data)

    fun previewUnequipInventorySlot(unitId: Int, slot: CampaignEquipmentSlot, data: GameDataCatalog): CampaignEquipPreview? =
        equipmentManager.previewUnequipInventorySlot(unitId, slot, data)

    fun equipInventoryItem(unitId: Int, itemId: Int, data: GameDataCatalog): String? =
        equipmentManager.equipInventoryItem(unitId, itemId, data)

    fun unequipInventorySlot(unitId: Int, slot: CampaignEquipmentSlot): Boolean =
        equipmentManager.unequipInventorySlot(unitId, slot)

    fun unequipAllEquipment(): Int = equipmentManager.unequipAllEquipment()

    override fun equipmentFor(unitId: Int): CampaignEquipment? = equipmentManager.equipmentFor(unitId)

    override fun storeEquipment(unitId: Int, equipment: CampaignEquipment) =
        equipmentManager.storeEquipment(unitId, equipment)
}
