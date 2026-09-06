// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallBuyUnitSummaryView: 거점 Buy 유닛 Summary 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class HallBuyUnitSummaryView(
    val portraitId: Int,
    val name: String,
    val postName: String,
    val level: Int,
    val hitPoints: Int,
    val magicPoints: Int,
    val stats: List<HallBuyUnitSummaryStat>,
)

/**
 * `HallBuyUnitSummaryStat`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class HallBuyUnitSummaryStat(
    val name: String,
    val value: Int,
)
