// Game
package com.jojo.game.presentation.scenario.hall
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.domain.campaign.*

import com.jojo.game.domain.campaign.CampaignEquipmentSlot

/** EquipConfirmationFlow: 장비 화면과 장비 확인 화면 사이의 전환 상태를 관리한다. */
class EquipConfirmationFlow(
    /** `campaign` (CampaignState): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val campaign: CampaignState,
    /** `data` (GameDataCatalog): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val data: GameDataCatalog,
) {

    /**
     * `Request`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Request(
        /**
         * `values` (List<Int>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val values: List<Int>,
        /**
         * `actionLabel` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val actionLabel: String,
        /**
         * `itemId` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val itemId: Int? = null,
        /**
         * `unequipSlot` (CampaignEquipmentSlot?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unequipSlot: CampaignEquipmentSlot? = null,
    )

    /**
     * `pending` (Request?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var pending: Request? = null
        private set


    /**
     * `requestEquip`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun requestEquip(unitId: Int, itemId: Int): Request? =
        campaign.inventory.previewEquipInventoryItem(unitId, itemId, data)?.let { preview ->
            Request(preview.values, "장비", itemId = itemId).also { pending = it }
        }


    /**
     * `requestUnequip`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun requestUnequip(unitId: Int, slot: CampaignEquipmentSlot): Request? =
        campaign.inventory.previewUnequipInventorySlot(unitId, slot, data)?.let { preview ->
            Request(preview.values, "해제", unequipSlot = slot).also { pending = it }
        }


    /**
     * `answer`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun answer(unitId: Int, accept: Boolean): Boolean {
        val request = pending ?: return false
        pending = null
        if (!accept) return false
        return when {
            request.itemId != null -> campaign.inventory.equipInventoryItem(unitId, request.itemId, data) != null
            request.unequipSlot != null -> campaign.inventory.unequipInventorySlot(unitId, request.unequipSlot)
            else -> false
        }
    }


    /**
     * `cancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun cancel() {
        pending = null
    }
}
