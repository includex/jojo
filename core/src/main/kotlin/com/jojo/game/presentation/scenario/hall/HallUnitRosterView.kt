// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallUnitRosterView: 거점 유닛 명단 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallUnitRosterView(val rows: List<HallUnitRosterRowView>)

internal data class HallUnitRosterRowView(
    val name: String,
    val postName: String,
)
