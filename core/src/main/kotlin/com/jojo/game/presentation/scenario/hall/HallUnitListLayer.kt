// Scenario
package com.jojo.game.presentation.scenario.hall

/** HallUnitListLayer: 거점 유닛 List 레이어이며, 시나리오 화면에 표시할 요소를 그린다. */
class HallUnitListLayer(unitIds: Collection<Int>) {
    val rows: List<Int> = unitIds.distinct().sorted()
    var selectedUnitId: Int? = null
        private set
    var attached: Boolean = true
        private set


    fun onCancel(eventType: Int): Boolean {
        if (!attached || eventType != TOUCH_END) return false
        attached = false
        return true
    }


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
