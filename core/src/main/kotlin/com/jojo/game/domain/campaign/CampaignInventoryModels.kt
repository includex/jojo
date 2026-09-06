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
    fun equipmentFor(unitId: Int): CampaignEquipment?
    fun storeEquipment(unitId: Int, equipment: CampaignEquipment)
}
