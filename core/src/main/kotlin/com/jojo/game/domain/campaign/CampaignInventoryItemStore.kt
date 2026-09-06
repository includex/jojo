// Campaign
package com.jojo.game.domain.campaign
import com.jojo.game.*

import java.util.*

/**
 * `CampaignInventoryEquipment` 클래스: campaign 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class CampaignInventoryEquipment(val level: Int = 1, val experience: Int = 0)

/**
 * `CampaignInventoryItemStore` 클래스: campaign 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal class CampaignInventoryItemStore {
    /**
     * `itemStacks` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val itemStacks = linkedMapOf<Int, Int>()
    /**
     * `items` (Map<Int, Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val items: Map<Int, Int> = Collections.unmodifiableMap(itemStacks)

    /**
     * `discoveredTreasureIds` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val discoveredTreasureIds = linkedSetOf<Int>()
    /**
     * `discoveredTreasures` (Set<Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val discoveredTreasures: Set<Int> = Collections.unmodifiableSet(discoveredTreasureIds)

    /**
     * `equipmentInstances` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val equipmentInstances = linkedMapOf<Int, MutableList<CampaignInventoryEquipment>>()

    /**
     * `reset`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun reset() {
        itemStacks.clear()
        discoveredTreasureIds.clear()
        equipmentInstances.clear()
    }

    /**
     * `add`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `consume`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consume(itemId: Int): Boolean {
        val count = itemStacks[itemId] ?: return false
        if (count < 1) return false
        if (count == 1) itemStacks.remove(itemId) else itemStacks[itemId] = count - 1
        return true
    }

    /**
     * `removeStack`: 사용한 상태와 자원을 정리한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun removeStack(itemId: Int) {
        itemStacks.remove(itemId)
        equipmentInstances.remove(itemId)
    }

    /**
     * `restoreDiscoveries`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun restoreDiscoveries(itemIds: Iterable<Int>) {
        discoveredTreasureIds.clear()
        discoveredTreasureIds.addAll(itemIds)
    }

    /**
     * `discover`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun discover(itemId: Int): Boolean = discoveredTreasureIds.add(itemId)

    /**
     * `count`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun count(itemId: Int): Int = itemStacks[itemId] ?: 0

    /**
     * `levels`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun levels(itemId: Int): List<Int> = equipmentInstances[itemId].orEmpty().map { it.level }

    /**
     * `experiences`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun experiences(itemId: Int): List<Int> = equipmentInstances[itemId].orEmpty().map { it.experience }

    /**
     * `newestEquipment`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun newestEquipment(itemId: Int): CampaignInventoryEquipment? = equipmentInstances[itemId]?.lastOrNull()

    /**
     * `takeNewestEquipment`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun takeNewestEquipment(itemId: Int): CampaignInventoryEquipment? {
        val instances = equipmentInstances[itemId] ?: return null
        val instance = instances.removeLastOrNull()
        if (instances.isEmpty()) equipmentInstances.remove(itemId)
        return instance
    }

    /**
     * `isEquipment`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun isEquipment(itemId: Int): Boolean = itemId !in ITEM_PROPERTY_FIRST..ITEM_PROPERTY_LAST

    private companion object {
        /**
         * `ITEM_PROPERTY_FIRST` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ITEM_PROPERTY_FIRST = 150
        /**
         * `ITEM_PROPERTY_LAST` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ITEM_PROPERTY_LAST = 254
    }
}
