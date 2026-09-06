// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallPropertyView: 거점 속성 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallPropertyView(
    val selectedTab: Int,
    val rows: List<HallPropertyRowView>,
)

/**
 * `HallPropertyRowView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallPropertyRowView(
    val itemId: Int,
    val name: String,
    val icon: Int,
    val typeName: String,
    val level: String,
    val experience: String,
    val owner: String,
)
