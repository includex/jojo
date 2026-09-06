// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallEquipmentRenderPlan: 거점 장비 렌더링 Plan이며, 해당 화면 영역의 그리기 순서와 항목 배치를 전달한다. */
internal object HallEquipmentRenderPlan {
    /**
     * `inventoryRowY`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun inventoryRowY(index: Int): Float = 515f - index * 68f

    /**
     * `slotY`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun slotY(index: Int): Float = listOf(20.97f, -114.91f, -250.79f)[index]

    /**
     * `paintOrder`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun paintOrder(inventoryCount: Int, slotCount: Int): List<String> = buildList {
        addAll(listOf("background", "frame", "title", "footer", "tabs", "inventory-frame", "unit-header"))
        repeat(inventoryCount) { add("inventory-$it") }
        add("unit-summary")
        repeat(slotCount) { add("slot-$it") }
        add("notice")
    }
}
