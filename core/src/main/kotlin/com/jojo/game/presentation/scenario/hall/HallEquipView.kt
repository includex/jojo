// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallEquipView: 거점 Equip 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallEquipView(
    val selectedTab: Int,
    val unit: HallEquipUnitView,
    val inventoryRows: List<HallEquipInventoryRowView>,
    val notice: String?,
)

/**
 * `HallEquipUnitView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallEquipUnitView(
    val portraitId: Int,
    val name: String,
    val armName: String,
    val level: String,
    val stats: List<HallEquipStatView>,
    val slots: List<HallEquipSlotView>,
)

/**
 * `HallEquipStatView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallEquipStatView(val name: String, val value: String)

/**
 * `HallEquipSlotView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallEquipSlotView(
    val index: Int,
    val label: String,
    val name: String,
    val icon: Int?,
    val level: String?,
    val experience: String?,
)

/**
 * `HallEquipInventoryRowView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallEquipInventoryRowView(
    val name: String,
    val icon: Int,
    val typeName: String,
    val level: String,
    val experience: String,
)
