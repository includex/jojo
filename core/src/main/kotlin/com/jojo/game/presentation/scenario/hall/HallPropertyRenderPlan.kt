// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallPropertyRenderPlan: 거점 속성 렌더링 Plan이며, 해당 화면 영역의 그리기 순서와 항목 배치를 전달한다. */
internal object HallPropertyRenderPlan {
    /**
     * `rowY`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun rowY(index: Int): Float = 481.58f - index * 67.08f

    /**
     * `paintOrder`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun paintOrder(rowCount: Int): List<String> = buildList {
        addAll(listOf("background", "frame", "title", "headers"))
        repeat(rowCount) { add("row-$it") }
        addAll(listOf("tabs", "confirm"))
    }
}
