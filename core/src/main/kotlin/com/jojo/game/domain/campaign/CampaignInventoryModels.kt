// Campaign
package com.jojo.game.domain.campaign

/** 장착 장비의 부위를 구분한다. */
enum class CampaignEquipmentSlot { WEAPON, ARMOR, AUXILIARY }

/** 유닛이 장착한 아이템의 조회용 정보이다. */
data class CampaignEquippedItem(
    val unitId: Int,
    val itemId: Int,
    val level: Int,
    val experience: Int,
)

/** 장비 교체 전후 능력치 미리보기이다. */
data class CampaignEquipPreview(val itemName: String, val values: List<Int>)

/** 장비 성장 기능이 사용하는 최소 장비 저장소 계약이다. */
internal interface CampaignEquipmentRepository {
    /**
     * `equipmentFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun equipmentFor(unitId: Int): CampaignEquipment?
    /**
     * `storeEquipment`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun storeEquipment(unitId: Int, equipment: CampaignEquipment)
}
