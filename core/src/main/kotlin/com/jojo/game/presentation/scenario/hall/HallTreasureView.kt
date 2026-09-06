// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallTreasureView: 보물 패널이 표시할 발견 상태·행·아이콘을 담는 불변 입력이다. */
internal data class HallTreasureView(
    val entries: List<HallTreasureEntryView>,
    val discoveredCount: Int,
    val totalCount: Int,
)

/**
 * `HallTreasureEntryView`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallTreasureEntryView(
    val name: String,
    val icon: Int,
    val discovered: Boolean,
)
