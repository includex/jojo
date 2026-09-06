// Campaign
package com.jojo.game.domain.campaign
import com.jojo.game.domain.campaign.CampaignInventory

import com.jojo.game.*

/**
 * `CampaignInventoryEquipmentView` 싱글턴 객체: campaign 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object CampaignInventoryEquipmentView {
    /**
     * `equippedItems`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equippedItems(loadouts: Map<Int, CampaignEquipment>): List<CampaignEquippedItem> =
        loadouts.flatMap { (unitId, value) ->
            buildList {
                compactToItemId(value.weapon, WEAPON_OFFSET)?.let {
                    add(CampaignEquippedItem(unitId, it, value.weaponLevel, value.weaponExperience))
                }
                compactToItemId(value.armor, ARMOR_OFFSET)?.let {
                    add(CampaignEquippedItem(unitId, it, value.armorLevel, value.armorExperience))
                }
                compactToItemId(value.auxiliary, AUXILIARY_OFFSET)?.let {
                    add(CampaignEquippedItem(unitId, it, 1, 0))
                }
            }
        }

    /**
     * `compactToItemId`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun compactToItemId(compact: Int, offset: Int): Int? {
        if (compact <= 1) return null
        val id = compact - 2 + offset
        return if (id >= ITEM_PROPERTY_FIRST) id + 105 else id
    }

    /**
     * `ITEM_PROPERTY_FIRST` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private const val ITEM_PROPERTY_FIRST = 150
    /**
     * `WEAPON_OFFSET` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private const val WEAPON_OFFSET = 0
    /**
     * `ARMOR_OFFSET` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private const val ARMOR_OFFSET = 70
    /**
     * `AUXILIARY_OFFSET` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private const val AUXILIARY_OFFSET = 109
}
