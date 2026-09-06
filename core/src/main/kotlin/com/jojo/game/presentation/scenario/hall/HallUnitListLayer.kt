// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallUnitListLayer: 거점 유닛 List 레이어이며, 시나리오 화면에 표시할 요소를 그린다. */
class HallUnitListLayer(unitIds: Collection<Int>) {
    /**
     * `rows` (List<Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val rows: List<Int> = unitIds.distinct().sorted()
    /**
     * `selectedUnitId` (Int?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var selectedUnitId: Int? = null
        private set
    /**
     * `attached` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached: Boolean = true
        private set


    /**
     * `onCancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCancel(eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END) return false
        attached = false
        return true
    }


    /**
     * `onRow`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onRow(row: Int, eventType: Int): Int? {
        if (!attached || eventType != TOUCH_END) return null
        val unitId = rows.getOrNull(row) ?: return null
        selectedUnitId = unitId
        attached = false
        return unitId
    }

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
    }
}
