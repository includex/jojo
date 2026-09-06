// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallPropertyRenderPlan: 거점 속성 렌더링 Plan이며, 해당 화면 영역의 그리기 순서와 항목 배치를 전달한다. */
internal object HallPropertyRenderPlan {
    fun rowY(index: Int): Float = 481.58f - index * 67.08f

    fun paintOrder(rowCount: Int): List<String> = buildList {
        addAll(listOf("background", "frame", "title", "headers"))
        repeat(rowCount) { add("row-$it") }
        addAll(listOf("tabs", "confirm"))
    }
}
