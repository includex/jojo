// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallEquipView: 거점 Equip 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallEquipView(
    val selectedTab: Int,
    val unit: HallEquipUnitView,
    val inventoryRows: List<HallEquipInventoryRowView>,
    val notice: String?,
)

internal data class HallEquipUnitView(
    val portraitId: Int,
    val name: String,
    val armName: String,
    val level: String,
    val stats: List<HallEquipStatView>,
    val slots: List<HallEquipSlotView>,
)

internal data class HallEquipStatView(val name: String, val value: String)

internal data class HallEquipSlotView(
    val index: Int,
    val label: String,
    val name: String,
    val icon: Int?,
    val level: String?,
    val experience: String?,
)

internal data class HallEquipInventoryRowView(
    val name: String,
    val icon: Int,
    val typeName: String,
    val level: String,
    val experience: String,
)
