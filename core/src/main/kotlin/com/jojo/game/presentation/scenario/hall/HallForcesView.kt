// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallForcesView: 거점 부대 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallForcesView(
    val rows: List<HallForcesRowView>,
)

/**
 * `HallForcesRowView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallForcesRowView(
    val values: List<String>,
)
