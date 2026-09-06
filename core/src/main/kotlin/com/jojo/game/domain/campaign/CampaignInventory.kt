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

    fun consumeItem(itemId: Int): Boolean = itemStore.consume(itemId)

    /** removeItemStack: 전투 편성에서 사용한 특정 아이템의 모든 보유 인스턴스를 제거한다. */
    internal fun removeItemStack(itemId: Int) = itemStore.removeStack(itemId)

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
