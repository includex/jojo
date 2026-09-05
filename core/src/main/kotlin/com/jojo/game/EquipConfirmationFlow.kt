package com.jojo.game

import com.jojo.game.domain.campaign.*

import com.jojo.game.domain.campaign.CampaignEquipmentSlot

/** Testable route boundary between EquipLayer and EquipConfirmLayer. */
class EquipConfirmationFlow(
    private val campaign: CampaignState,
    private val data: GameDataCatalog,
) {
    /**
     * data class  `Request`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Request(
        val values: List<Int>,
        val actionLabel: String,
        val itemId: Int? = null,
        val unequipSlot: CampaignEquipmentSlot? = null,
    )

    var pending: Request? = null
        private set

    /**
     * 공개 메서드 `requestEquip`
     *
     * ### 파라미터
    - `unitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `itemId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Request?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestEquip(unitId: Int, itemId: Int): Request? =
        campaign.inventory.previewEquipInventoryItem(unitId, itemId, data)?.let { preview ->
            Request(preview.values, "장비", itemId = itemId).also { pending = it }
        }

    /**
     * 공개 메서드 `requestUnequip`
     *
     * ### 파라미터
    - `unitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `slot` (`CampaignEquipmentSlot`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Request?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun requestUnequip(unitId: Int, slot: CampaignEquipmentSlot): Request? =
        campaign.inventory.previewUnequipInventorySlot(unitId, slot, data)?.let { preview ->
            Request(preview.values, "해제", unequipSlot = slot).also { pending = it }
        }

    /**
     * 공개 메서드 `answer`
     *
     * ### 파라미터
    - `unitId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `accept` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `cancel`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun cancel() {
        pending = null
    }
}
