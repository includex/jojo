// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallBuyCatalogView: 상점 목록의 행, 가격, 소지 수와 선택 위치를 담는 불변 renderer 입력이다. */
internal data class HallBuyCatalogView(
    val propertyTab: Boolean,
    val rows: List<HallBuyCatalogRowView>,
)

/**
 * `HallBuyCatalogRowView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallBuyCatalogRowView(
    val name: String,
    val icon: Int,
    val typeName: String,
    val inventory: Int,
    val total: Int,
    val price: String,
)
