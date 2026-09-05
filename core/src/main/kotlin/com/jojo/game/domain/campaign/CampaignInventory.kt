package com.jojo.game.domain.campaign

import com.jojo.game.*
import com.jojo.game.domain.campaign.CampaignInventoryEquipmentView
import com.jojo.game.domain.campaign.CampaignInventoryItemStore

import java.util.*

/**
 * enum class  `CampaignEquipmentSlot`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

enum class CampaignEquipmentSlot { WEAPON, ARMOR, AUXILIARY }

/**
 * data class  `CampaignEquippedItem`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class CampaignEquippedItem(
    val unitId: Int,
    val itemId: Int,
    val level: Int,
    val experience: Int,
)

/**
 * data class  `CampaignEquipPreview`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class CampaignEquipPreview(val itemName: String, val values: List<Int>)

internal data class CampaignInventoryEquipment(val level: Int = 1, val experience: Int = 0)

internal interface CampaignEquipmentRepository {
    /**
     * 공개 메서드 `equipmentFor`
     *
     * ### 파라미터
    - `unitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `CampaignEquipment?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun equipmentFor(unitId: Int): CampaignEquipment?

    /**
     * 공개 메서드 `storeEquipment`
     *
     * ### 파라미터
    - `unitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `equipment` (`CampaignEquipment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun storeEquipment(unitId: Int, equipment: CampaignEquipment)
}

/** Owns item stacks, equipment instances, discoveries and equipped loadouts. */
class CampaignInventory internal constructor(
    private val joinedUnitIds: () -> Iterable<Int>,
    private val unitAttribute: (unitId: Int, attribute: Int, default: Int) -> Int,
) : CampaignEquipmentRepository {
    internal constructor() : this({ emptyList() }, { _, _, default -> default })

    private val itemStore = CampaignInventoryItemStore()
    val items: Map<Int, Int> get() = itemStore.items
    val discoveredTreasures: Set<Int> get() = itemStore.discoveredTreasures
    private val equippedLoadouts = linkedMapOf<Int, CampaignEquipment>()
    val equipment: Map<Int, CampaignEquipment> = Collections.unmodifiableMap(equippedLoadouts)
    internal fun reset() {
        itemStore.reset()
        equippedLoadouts.clear()
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
    internal fun removeItemStack(itemId: Int) {
        itemStore.removeStack(itemId)
    }

    /** Rehydrates the persisted discovery order after aggregate reset. */
    internal fun restoreDiscoveredTreasures(itemIds: Iterable<Int>) {
        itemStore.restoreDiscoveries(itemIds)
    }

    fun itemLevels(itemId: Int): List<Int> = itemStore.levels(itemId)
    fun itemExperiences(itemId: Int): List<Int> = itemStore.experiences(itemId)

    fun setEquipment(
        unitId: Int,
        weapon: Int,
        weaponLevel: Int,
        armor: Int,
        armorLevel: Int,
        auxiliary: Int,
    ) {
        equippedLoadouts[unitId] = CampaignEquipment(weapon, weaponLevel, armor, armorLevel, auxiliary)
    }

    /**
     * 공개 메서드 `setEquipment`
     *
     * ### 파라미터
    - `unitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `value` (`CampaignEquipment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setEquipment(unitId: Int, value: CampaignEquipment) {
        equippedLoadouts[unitId] = value
    }

    /**
     * 공개 메서드 `ensureDefaultEquipment`
     *
     * ### 파라미터
    - `unitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `data` (`GameDataCatalog`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun ensureDefaultEquipment(unitId: Int, data: GameDataCatalog) {
        if (unitId in equippedLoadouts) return
        val unit = data.unitProfile(unitId) ?: return
        val level = unitAttribute(unitId, UNIT_ATTR_LEVEL, unit.level).coerceAtLeast(1)
        val posts = unitAttribute(unitId, UNIT_ATTR_POSTS, unit.posts)
        equippedLoadouts[unitId] = data.defaultEquipment(posts, level)
    }

    fun equippedItems(): List<CampaignEquippedItem> = CampaignInventoryEquipmentView.equippedItems(equippedLoadouts)

    /** Computes the confirmation values without mutating inventory or equipment. */
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

    fun previewUnequipInventorySlot(
        unitId: Int,
        slot: CampaignEquipmentSlot,
        data: GameDataCatalog,
    ): CampaignEquipPreview? {
        val current = equippedLoadouts[unitId] ?: return null
        val itemId = equippedItemId(current, slot) ?: return null
        val candidate = cleared(current, slot)
        return preview(unitId, data.equipmentProfile(itemId)?.name.orEmpty(), current, candidate, data)
    }

    /** Moves the newest owned instance onto the unit and returns the replaced instance. */
    fun equipInventoryItem(unitId: Int, itemId: Int, data: GameDataCatalog): String? {
        if (itemStore.count(itemId) < 1 || !itemStore.isEquipment(itemId)) return null
        val item = data.equipmentProfile(itemId) ?: return null
        val slot = slotFor(item.itemType)
        val current = equippedLoadouts[unitId] ?: CampaignEquipment(1, 1, 1, 1, 1)
        val previous = equippedItemId(current, slot)
        val instance = itemStore.takeNewestEquipment(itemId) ?: CampaignInventoryEquipment()
        consumeItem(itemId)
        previous?.let { previousId ->
            addItem(previousId, level = levelFor(current, slot), experience = experienceFor(current, slot))
        }
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
     * 공개 메서드 `unequipInventorySlot`
     *
     * ### 파라미터
    - `unitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `slot` (`CampaignEquipmentSlot`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun unequipInventorySlot(unitId: Int, slot: CampaignEquipmentSlot): Boolean {
        val current = equippedLoadouts[unitId] ?: return false
        val itemId = equippedItemId(current, slot) ?: return false
        addItem(itemId, level = levelFor(current, slot), experience = experienceFor(current, slot))
        equippedLoadouts[unitId] = cleared(current, slot)
        return true
    }

    /**
     * 공개 메서드 `unequipAllEquipment`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
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

    override fun equipmentFor(unitId: Int): CampaignEquipment? = equippedLoadouts[unitId]

    override fun storeEquipment(unitId: Int, equipment: CampaignEquipment) {
        equippedLoadouts[unitId] = equipment
    }

    private fun preview(
        unitId: Int,
        itemName: String,
        current: CampaignEquipment,
        candidate: CampaignEquipment,
        data: GameDataCatalog,
    ): CampaignEquipPreview {
        val unit = data.unitProfile(unitId)
        val unitLevel = unitAttribute(unitId, UNIT_ATTR_LEVEL, unit?.level ?: 1).coerceAtLeast(1)
        val before = data.equipmentBonus(current.asScriptValues(), unitLevel)
        val after = data.equipmentBonus(candidate.asScriptValues(), unitLevel)
        return CampaignEquipPreview(
            itemName,
            listOf(
                0, 0, after.attack - before.attack, after.spirit - before.spirit,
                after.defense - before.defense, 0, 0, 0
            ),
        )
    }

    private fun equippedItemId(value: CampaignEquipment, slot: CampaignEquipmentSlot): Int? = when (slot) {
        CampaignEquipmentSlot.WEAPON -> compactToItemId(value.weapon, WEAPON_OFFSET)
        CampaignEquipmentSlot.ARMOR -> compactToItemId(value.armor, ARMOR_OFFSET)
        CampaignEquipmentSlot.AUXILIARY -> compactToItemId(value.auxiliary, AUXILIARY_OFFSET)
    }

    private fun cleared(value: CampaignEquipment, slot: CampaignEquipmentSlot) = when (slot) {
        CampaignEquipmentSlot.WEAPON -> value.copy(weapon = 1, weaponLevel = 1, weaponExperience = 0)
        CampaignEquipmentSlot.ARMOR -> value.copy(armor = 1, armorLevel = 1, armorExperience = 0)
        CampaignEquipmentSlot.AUXILIARY -> value.copy(auxiliary = 1)
    }

    private fun levelFor(value: CampaignEquipment, slot: CampaignEquipmentSlot) = when (slot) {
        CampaignEquipmentSlot.WEAPON -> value.weaponLevel
        CampaignEquipmentSlot.ARMOR -> value.armorLevel
        CampaignEquipmentSlot.AUXILIARY -> 1
    }

    private fun experienceFor(value: CampaignEquipment, slot: CampaignEquipmentSlot) = when (slot) {
        CampaignEquipmentSlot.WEAPON -> value.weaponExperience
        CampaignEquipmentSlot.ARMOR -> value.armorExperience
        CampaignEquipmentSlot.AUXILIARY -> 0
    }

    private fun slotFor(itemType: Int) = when {
        itemType <= 19 -> CampaignEquipmentSlot.WEAPON
        itemType <= 25 -> CampaignEquipmentSlot.ARMOR
        else -> CampaignEquipmentSlot.AUXILIARY
    }

    private fun offsetFor(itemType: Int) = offsetFor(slotFor(itemType))

    private fun offsetFor(slot: CampaignEquipmentSlot) = when (slot) {
        CampaignEquipmentSlot.WEAPON -> WEAPON_OFFSET
        CampaignEquipmentSlot.ARMOR -> ARMOR_OFFSET
        CampaignEquipmentSlot.AUXILIARY -> AUXILIARY_OFFSET
    }

    private companion object {
        const val UNIT_ATTR_LEVEL = 18
        const val UNIT_ATTR_POSTS = 17
        const val ITEM_PROPERTY_FIRST = 150
        const val ITEM_PROPERTY_LAST = 254
        const val WEAPON_OFFSET = 0
        const val ARMOR_OFFSET = 70
        const val AUXILIARY_OFFSET = 109

        /**
         * 공개 메서드 `itemIdToCompact`
         *
         * ### 파라미터
        - `itemId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `offset` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Int`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun itemIdToCompact(itemId: Int, offset: Int): Int {
            val compactId = if (itemId >= ITEM_PROPERTY_LAST + 1) itemId - 105 else itemId
            return compactId - offset + 2
        }

        /**
         * 공개 메서드 `compactToItemId`
         *
         * ### 파라미터
        - `compact` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `offset` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Int?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun compactToItemId(compact: Int, offset: Int): Int? {
            if (compact <= 1) return null
            val id = compact - 2 + offset
            return if (id >= ITEM_PROPERTY_FIRST) id + 105 else id
        }
    }
}
