package com.jojo.game

/** Behavioural implementation of Hall/scene/UnitListLayer (Hall layer id 9). */
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
