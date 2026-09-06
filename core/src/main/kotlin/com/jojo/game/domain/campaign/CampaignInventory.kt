// Campaign
package com.jojo.game.domain.campaign

import com.jojo.game.infrastructure.data.GameDataCatalog

/** CampaignInventory: 캠페인이 보유한 소비품·발견 보물·장비 인스턴스를 한 상태로 보존한다. */
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

    /** discoverTreasure: 판매 불가 보물인지 확인한 뒤 중복 없이 발견 목록에 등록한다. */
    fun discoverTreasure(itemId: Int, data: GameDataCatalog): Boolean {
        val item = data.equipmentProfile(itemId) ?: return false
        if (!item.treasure || item.price != 255) return false
        return itemStore.discover(itemId)
    }

    fun consumeItem(itemId: Int): Boolean = itemStore.consume(itemId)

    /** removeItemStack: 전투 편성에서 사용한 특정 아이템의 모든 보유 인스턴스를 제거한다. */
    internal fun removeItemStack(itemId: Int) = itemStore.removeStack(itemId)

    /** restoreDiscoveredTreasures: 저장 데이터의 보물 식별자 순서로 발견 목록을 되살린다. */
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
