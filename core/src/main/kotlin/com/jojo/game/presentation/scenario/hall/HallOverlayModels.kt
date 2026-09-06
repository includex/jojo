// Scenario
package com.jojo.game.presentation.scenario.hall

import com.jojo.game.domain.campaign.CampaignEquipmentSlot

/** HallManagement: 거점 관리 오버레이로, 부대·아이템·마법·편성 메뉴의 현재 선택 영역을 나타낸다. */
enum class HallManagement { EQUIP, BUY, SELL }

/** HallInfo: 거점 정보 오버레이로, 선택 유닛이나 지형의 상세 정보를 표시하는 상태를 나타낸다. */
enum class HallInfo { FORCES, PROPERTY, TERRAIN, TREASURE, HELPER }

/**
 * `HallPropertyTab`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

enum class HallPropertyTab { WEAPON, ARMOR, AUXILIARY, PROPERTY }

/** HallEquipConfirmation: 장비 변경 전 확인 대화상자의 대상 유닛과 장비 정보를 나타낸다. */
data class HallEquipConfirmation(
    val values: List<Int>,
    val actionLabel: String,
    val itemId: Int? = null,
    val unequipSlot: CampaignEquipmentSlot? = null,
)

/** HallItemDetail: 거점 아이템 상세 오버레이에서 표시할 아이템과 사용 대상을 나타낸다. */
data class HallItemDetail(
    val itemId: Int,
    val level: String,
    val experience: Int,
    val experienceLimit: Int,
)
