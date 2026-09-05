package com.jojo.game.presentation.scenario.hall

/** Behavioural implementation of Hall/scene/UnitListLayer (Hall layer id 9). */
class HallUnitListLayer(unitIds: Collection<Int>) {
    val rows: List<Int> = unitIds.distinct().sorted()
    var selectedUnitId: Int? = null
        private set
    var attached: Boolean = true
        private set

    /**
     * 공개 메서드 `onCancel`
     *
     * ### 파라미터
    - `eventType` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCancel(eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END) return false
        attached = false
        return true
    }

    /**
     * 공개 메서드 `onRow`
     *
     * ### 파라미터
    - `row` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `eventType` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onRow(row: Int, eventType: Int): Int? {
        if (!attached || eventType != TOUCH_END) return null
        val unitId = rows.getOrNull(row) ?: return null
        selectedUnitId = unitId
        attached = false
        return unitId
    }

    companion object {
        const val TOUCH_END = 2
    }
}
