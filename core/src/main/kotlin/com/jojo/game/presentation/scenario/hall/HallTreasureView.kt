// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallTreasureView: 거점 Treasure 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallTreasureView(
    val entries: List<HallTreasureEntryView>,
    val discoveredCount: Int,
    val totalCount: Int,
)

internal data class HallTreasureEntryView(
    val name: String,
    val icon: Int,
    val discovered: Boolean,
)
