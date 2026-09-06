// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallSellView: 거점 Sell 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallSellView(
    val rows: List<HallSellRowView>,
    val money: Int,
    val notice: String?,
)

/**
 * `HallSellRowView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallSellRowView(
    val name: String,
    val icon: Int,
    val primaryDetail: String,
    val secondaryDetail: String?,
    val salePrice: String,
)
