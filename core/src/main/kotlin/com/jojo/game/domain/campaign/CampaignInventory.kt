// Campaign
package com.jojo.game.domain.campaign

import com.jojo.game.infrastructure.data.GameDataCatalog

/** CampaignInventory: 캠페인이 보유한 소비품·발견 보물·장비 인스턴스를 한 상태로 보존한다. */
class CampaignInventory internal constructor(
    /**
     * `joinedUnitIds` (() -> Iterable<Int>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val joinedUnitIds: () -> Iterable<Int>,
    /**
     * `unitAttribute` ((unitId: Int, attribute: Int, default: Int) -> Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val unitAttribute: (unitId: Int, attribute: Int, default: Int) -> Int,
) : CampaignEquipmentRepository {
    internal constructor() : this({ emptyList() }, { _, _, default -> default })

    /**
     * `itemStore` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val itemStore = CampaignInventoryItemStore()
    /** Consumables reserved for a particular character. Counts are also included in [items]. */
    private val carriedItems = linkedMapOf<Int, MutableMap<Int, Int>>()

    val unitItems: Map<Int, Map<Int, Int>> get() = carriedItems.mapValues { it.value.toMap() }

    fun unitItemCount(unitId: Int, itemId: Int): Int = carriedItems[unitId]?.get(itemId) ?: 0

    /** Unassigned stock remains available to legacy shared-inventory actions. */
    fun sharedItemCount(itemId: Int): Int = (items[itemId] ?: 0) - carriedItems.values.sumOf { it[itemId] ?: 0 }

    fun addUnitItem(unitId: Int, itemId: Int, count: Int = 1) {
        require(unitId >= 0 && itemId in 150..254 && count > 0)
        itemStore.add(itemId, count)
        assignUnitItem(unitId, itemId, count)
    }

    internal fun assignUnitItem(unitId: Int, itemId: Int, count: Int) {
        require(unitId >= 0 && itemId in 150..254 && count > 0 && sharedItemCount(itemId) >= count)
        val stock = carriedItems.getOrPut(unitId) { linkedMapOf() }
        stock[itemId] = (stock[itemId] ?: 0) + count
    }

    fun transferUnitItem(fromUnitId: Int, toUnitId: Int, itemId: Int, count: Int = 1): Boolean {
        if (fromUnitId == toUnitId || count <= 0 || unitItemCount(fromUnitId, itemId) < count) return false
        removeCarried(fromUnitId, itemId, count)
        val stock = carriedItems.getOrPut(toUnitId) { linkedMapOf() }
        stock[itemId] = (stock[itemId] ?: 0) + count
        return true
    }

    private fun removeCarried(unitId: Int, itemId: Int, count: Int) {
        val stock = carriedItems.getValue(unitId)
        val remaining = stock.getValue(itemId) - count
        if (remaining == 0) stock.remove(itemId) else stock[itemId] = remaining
        if (stock.isEmpty()) carriedItems.remove(unitId)
    }
    /**
     * `equipmentManager` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val equipmentManager = CampaignInventoryEquipmentManager(itemStore, joinedUnitIds, unitAttribute)

    /**
     * `items` (Map<Int, Int> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val items: Map<Int, Int> get() = itemStore.items
    /**
     * `discoveredTreasures` (Set<Int> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val discoveredTreasures: Set<Int> get() = itemStore.discoveredTreasures
    /**
     * `equipment` (Map<Int, CampaignEquipment> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val equipment: Map<Int, CampaignEquipment> get() = equipmentManager.equipment

    /**
     * `reset`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    internal fun reset() {
        itemStore.reset()
        carriedItems.clear()
        equipmentManager.reset()
    }

    /**
     * `addItem`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addItem(itemId: Int, count: Int = 1, level: Int = 1, experience: Int = 0) =
        itemStore.add(itemId, count, level, experience)

    /** discoverTreasure: 판매 불가 보물인지 확인한 뒤 중복 없이 발견 목록에 등록한다. */
    fun discoverTreasure(itemId: Int, data: GameDataCatalog): Boolean {
        val item = data.equipmentProfile(itemId) ?: return false
        if (!item.treasure || item.price != 255) return false
        return itemStore.discover(itemId)
    }

    /**
     * `consumeItem`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeItem(itemId: Int): Boolean = sharedItemCount(itemId) > 0 && itemStore.consume(itemId)

    /** Battle use consumes the actor's carried stock first, then shared stock. */
    fun consumeBattleItem(unitId: Int?, itemId: Int): Boolean {
        if (unitId != null && unitItemCount(unitId, itemId) > 0) {
            if (!itemStore.consume(itemId)) return false
            removeCarried(unitId, itemId, 1)
            return true
        }
        return consumeItem(itemId)
    }

    /** removeItemStack: 전투 편성에서 사용한 특정 아이템의 모든 보유 인스턴스를 제거한다. */
    internal fun removeItemStack(itemId: Int) {
        itemStore.removeStack(itemId)
        carriedItems.values.forEach { it.remove(itemId) }
        carriedItems.entries.removeIf { it.value.isEmpty() }
    }

    /** restoreDiscoveredTreasures: 저장 데이터의 보물 식별자 순서로 발견 목록을 되살린다. */
    internal fun restoreDiscoveredTreasures(itemIds: Iterable<Int>) = itemStore.restoreDiscoveries(itemIds)

    /**
     * `itemLevels`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun itemLevels(itemId: Int): List<Int> = itemStore.levels(itemId)
    /**
     * `itemExperiences`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun itemExperiences(itemId: Int): List<Int> = itemStore.experiences(itemId)

    /**
     * `setEquipment`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setEquipment(unitId: Int, weapon: Int, weaponLevel: Int, armor: Int, armorLevel: Int, auxiliary: Int) =
        equipmentManager.setEquipment(unitId, weapon, weaponLevel, armor, armorLevel, auxiliary)

    /**
     * `setEquipment`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setEquipment(unitId: Int, value: CampaignEquipment) = equipmentManager.setEquipment(unitId, value)

    /**
     * `ensureDefaultEquipment`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun ensureDefaultEquipment(unitId: Int, data: GameDataCatalog) =
        equipmentManager.ensureDefaultEquipment(unitId, data)

    /**
     * `equippedItems`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equippedItems(): List<CampaignEquippedItem> = equipmentManager.equippedItems()

    /**
     * `previewEquipInventoryItem`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun previewEquipInventoryItem(unitId: Int, itemId: Int, data: GameDataCatalog): CampaignEquipPreview? =
        equipmentManager.previewEquipInventoryItem(unitId, itemId, data)

    /**
     * `previewUnequipInventorySlot`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun previewUnequipInventorySlot(unitId: Int, slot: CampaignEquipmentSlot, data: GameDataCatalog): CampaignEquipPreview? =
        equipmentManager.previewUnequipInventorySlot(unitId, slot, data)

    /**
     * `equipInventoryItem`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipInventoryItem(unitId: Int, itemId: Int, data: GameDataCatalog): String? =
        equipmentManager.equipInventoryItem(unitId, itemId, data)

    /**
     * `unequipInventorySlot`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unequipInventorySlot(unitId: Int, slot: CampaignEquipmentSlot): Boolean =
        equipmentManager.unequipInventorySlot(unitId, slot)

    /**
     * `unequipAllEquipment`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unequipAllEquipment(): Int = equipmentManager.unequipAllEquipment()

    /**
     * `equipmentFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun equipmentFor(unitId: Int): CampaignEquipment? = equipmentManager.equipmentFor(unitId)

    /**
     * `storeEquipment`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun storeEquipment(unitId: Int, equipment: CampaignEquipment) =
        equipmentManager.storeEquipment(unitId, equipment)
}
