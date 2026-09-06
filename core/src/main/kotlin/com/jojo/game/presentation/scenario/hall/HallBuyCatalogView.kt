// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallBuyCatalogView: 거점 Buy 목록 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallBuyCatalogView(
    val propertyTab: Boolean,
    val rows: List<HallBuyCatalogRowView>,
)

internal data class HallBuyCatalogRowView(
    val name: String,
    val icon: Int,
    val typeName: String,
    val inventory: Int,
    val total: Int,
    val price: String,
)
