// Campaign
package com.jojo.game.domain.campaign

import com.jojo.game.infrastructure.data.GameDataCatalog
import java.util.Collections

/** CampaignInventoryEquipmentManager: 유닛 장비칸과 인벤토리 인스턴스를 교환하고 능력치 미리보기를 계산한다. */
internal class CampaignInventoryEquipmentManager(
    /**
     * `itemStore` (CampaignInventoryItemStore,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val itemStore: CampaignInventoryItemStore,
    /**
     * `joinedUnitIds` (() -> Iterable<Int>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val joinedUnitIds: () -> Iterable<Int>,
    /**
     * `unitAttribute` ((unitId: Int, attribute: Int, default: Int) -> Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val unitAttribute: (unitId: Int, attribute: Int, default: Int) -> Int,
) : CampaignEquipmentRepository {
    /**
     * `equippedLoadouts` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val equippedLoadouts = linkedMapOf<Int, CampaignEquipment>()
    /**
     * `equipment` (Map<Int, CampaignEquipment>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val equipment: Map<Int, CampaignEquipment> = Collections.unmodifiableMap(equippedLoadouts)

    /**
     * `reset`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun reset() = equippedLoadouts.clear()

    /**
     * `setEquipment`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setEquipment(unitId: Int, weapon: Int, weaponLevel: Int, armor: Int, armorLevel: Int, auxiliary: Int) =
        setEquipment(unitId, CampaignEquipment(weapon, weaponLevel, armor, armorLevel, auxiliary))

    /**
     * `setEquipment`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setEquipment(unitId: Int, value: CampaignEquipment) {
        equippedLoadouts[unitId] = value
    }

    /**
     * `ensureDefaultEquipment`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun ensureDefaultEquipment(unitId: Int, data: GameDataCatalog) {
        if (unitId in equippedLoadouts) return
        val unit = data.unitProfile(unitId) ?: return
        val level = unitAttribute(unitId, UNIT_ATTR_LEVEL, unit.level).coerceAtLeast(1)
        val posts = unitAttribute(unitId, UNIT_ATTR_POSTS, unit.posts)
        equippedLoadouts[unitId] = data.defaultEquipment(posts, level)
    }

    /**
     * `equippedItems`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equippedItems(): List<CampaignEquippedItem> = CampaignInventoryEquipmentView.equippedItems(equippedLoadouts)

    /**
     * `previewEquipInventoryItem`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun previewEquipInventoryItem(unitId: Int, itemId: Int, data: GameDataCatalog): CampaignEquipPreview? {
        if (itemStore.count(itemId) < 1 || !itemStore.isEquipment(itemId)) return null
        val item = data.equipmentProfile(itemId) ?: return null
        val current = equippedLoadouts[unitId] ?: CampaignEquipment(1, 1, 1, 1, 1)
        val instance = itemStore.newestEquipment(itemId) ?: CampaignInventoryEquipment()
        val compact = itemIdToCompact(itemId, offsetFor(item.itemType))
        val candidate = when {
            item.itemType <= 19 -> current.copy(
                weapon = compact,
                weaponLevel = instance.level,
                weaponExperience = instance.experience,
            )
            item.itemType <= 25 -> current.copy(
                armor = compact,
                armorLevel = instance.level,
                armorExperience = instance.experience,
            )
            else -> current.copy(auxiliary = compact)
        }
        return preview(unitId, item.name, current, candidate, data)
    }

    /**
     * `previewUnequipInventorySlot`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun previewUnequipInventorySlot(
        unitId: Int,
        slot: CampaignEquipmentSlot,
        data: GameDataCatalog,
    ): CampaignEquipPreview? {
        /**
         * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val current = equippedLoadouts[unitId] ?: return null
        /**
         * `itemId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val itemId = equippedItemId(current, slot) ?: return null
        return preview(unitId, data.equipmentProfile(itemId)?.name.orEmpty(), current, cleared(current, slot), data)
    }

    /**
     * `equipInventoryItem`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipInventoryItem(unitId: Int, itemId: Int, data: GameDataCatalog): String? {
        if (itemStore.count(itemId) < 1 || !itemStore.isEquipment(itemId)) return null
        val item = data.equipmentProfile(itemId) ?: return null
        val slot = slotFor(item.itemType)
        val current = equippedLoadouts[unitId] ?: CampaignEquipment(1, 1, 1, 1, 1)
        val previous = equippedItemId(current, slot)
        val instance = itemStore.takeNewestEquipment(itemId) ?: CampaignInventoryEquipment()
        itemStore.consume(itemId)
        previous?.let { itemStore.add(it, level = levelFor(current, slot), experience = experienceFor(current, slot)) }
        val compact = itemIdToCompact(itemId, offsetFor(slot))
        equippedLoadouts[unitId] = when (slot) {
            CampaignEquipmentSlot.WEAPON -> current.copy(
                weapon = compact,
                weaponLevel = instance.level,
                weaponExperience = instance.experience,
            )
            CampaignEquipmentSlot.ARMOR -> current.copy(
                armor = compact,
                armorLevel = instance.level,
                armorExperience = instance.experience,
            )
            CampaignEquipmentSlot.AUXILIARY -> current.copy(auxiliary = compact)
        }
        return item.name
    }

    /**
     * `unequipInventorySlot`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unequipInventorySlot(unitId: Int, slot: CampaignEquipmentSlot): Boolean {
        val current = equippedLoadouts[unitId] ?: return false
        val itemId = equippedItemId(current, slot) ?: return false
        itemStore.add(itemId, level = levelFor(current, slot), experience = experienceFor(current, slot))
        equippedLoadouts[unitId] = cleared(current, slot)
        return true
    }

    /**
     * `unequipAllEquipment`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unequipAllEquipment(): Int {
        var unloaded = 0
        joinedUnitIds().forEach { unitId ->
            CampaignEquipmentSlot.entries.forEach { slot ->
                if (unequipInventorySlot(unitId, slot)) unloaded++
            }
        }
        return unloaded
    }

    /**
     * `equipmentFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun equipmentFor(unitId: Int): CampaignEquipment? = equippedLoadouts[unitId]

    /**
     * `storeEquipment`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun storeEquipment(unitId: Int, equipment: CampaignEquipment) {
        equippedLoadouts[unitId] = equipment
    }

    /**
     * `preview`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun preview(
        unitId: Int,
        itemName: String,
        current: CampaignEquipment,
        candidate: CampaignEquipment,
        data: GameDataCatalog,
    ): CampaignEquipPreview {
        /**
         * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unit = data.unitProfile(unitId)
        /**
         * `unitLevel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unitLevel = unitAttribute(unitId, UNIT_ATTR_LEVEL, unit?.level ?: 1).coerceAtLeast(1)
        /**
         * `before` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val before = data.equipmentBonus(current.asScriptValues(), unitLevel)
        /**
         * `after` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val after = data.equipmentBonus(candidate.asScriptValues(), unitLevel)
        return CampaignEquipPreview(
            itemName,
            listOf(0, 0, after.attack - before.attack, after.spirit - before.spirit, after.defense - before.defense, 0, 0, 0),
        )
    }

    /**
     * `equippedItemId`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun equippedItemId(value: CampaignEquipment, slot: CampaignEquipmentSlot): Int? = when (slot) {
        CampaignEquipmentSlot.WEAPON -> compactToItemId(value.weapon, WEAPON_OFFSET)
        CampaignEquipmentSlot.ARMOR -> compactToItemId(value.armor, ARMOR_OFFSET)
        CampaignEquipmentSlot.AUXILIARY -> compactToItemId(value.auxiliary, AUXILIARY_OFFSET)
    }

    /**
     * `cleared`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun cleared(value: CampaignEquipment, slot: CampaignEquipmentSlot) = when (slot) {
        CampaignEquipmentSlot.WEAPON -> value.copy(weapon = 1, weaponLevel = 1, weaponExperience = 0)
        CampaignEquipmentSlot.ARMOR -> value.copy(armor = 1, armorLevel = 1, armorExperience = 0)
        CampaignEquipmentSlot.AUXILIARY -> value.copy(auxiliary = 1)
    }

    /**
     * `levelFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun levelFor(value: CampaignEquipment, slot: CampaignEquipmentSlot) = when (slot) {
        CampaignEquipmentSlot.WEAPON -> value.weaponLevel
        CampaignEquipmentSlot.ARMOR -> value.armorLevel
        CampaignEquipmentSlot.AUXILIARY -> 1
    }

    /**
     * `experienceFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun experienceFor(value: CampaignEquipment, slot: CampaignEquipmentSlot) = when (slot) {
        CampaignEquipmentSlot.WEAPON -> value.weaponExperience
        CampaignEquipmentSlot.ARMOR -> value.armorExperience
        CampaignEquipmentSlot.AUXILIARY -> 0
    }

    /**
     * `slotFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun slotFor(itemType: Int) = when {
        itemType <= 19 -> CampaignEquipmentSlot.WEAPON
        itemType <= 25 -> CampaignEquipmentSlot.ARMOR
        else -> CampaignEquipmentSlot.AUXILIARY
    }

    /**
     * `offsetFor`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun offsetFor(itemType: Int) = offsetFor(slotFor(itemType))

    /**
     * `offsetFor`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun offsetFor(slot: CampaignEquipmentSlot) = when (slot) {
        CampaignEquipmentSlot.WEAPON -> WEAPON_OFFSET
        CampaignEquipmentSlot.ARMOR -> ARMOR_OFFSET
        CampaignEquipmentSlot.AUXILIARY -> AUXILIARY_OFFSET
    }

    private companion object {
        /**
         * `UNIT_ATTR_LEVEL` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val UNIT_ATTR_LEVEL = 18
        /**
         * `UNIT_ATTR_POSTS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val UNIT_ATTR_POSTS = 17
        /**
         * `ITEM_PROPERTY_FIRST` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ITEM_PROPERTY_FIRST = 150
        /**
         * `ITEM_PROPERTY_LAST` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ITEM_PROPERTY_LAST = 254
        /**
         * `WEAPON_OFFSET` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val WEAPON_OFFSET = 0
        /**
         * `ARMOR_OFFSET` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ARMOR_OFFSET = 70
        /**
         * `AUXILIARY_OFFSET` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val AUXILIARY_OFFSET = 109

        /**
         * `itemIdToCompact`: 입력을 규칙에 따라 계산·변환한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun itemIdToCompact(itemId: Int, offset: Int): Int {
            val compactId = if (itemId >= ITEM_PROPERTY_LAST + 1) itemId - 105 else itemId
            return compactId - offset + 2
        }

        /**
         * `compactToItemId`: 입력을 규칙에 따라 계산·변환한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun compactToItemId(compact: Int, offset: Int): Int? {
            if (compact <= 1) return null
            val id = compact - 2 + offset
            return if (id >= ITEM_PROPERTY_FIRST) id + 105 else id
        }
    }
}
