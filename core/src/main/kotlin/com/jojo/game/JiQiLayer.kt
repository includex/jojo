package com.jojo.game

/** Behavioural implementation of Battle id27 / Global JiQiLayer. */
class JiQiLayer(val rates: List<Int>) {
    init { require(rates.size == 8) }
    var attached = true
        private set

    fun onCancel(event: Int): Boolean {
        if (!attached || event != TOUCH_END) return false
        attached = false
        return true
    }

    companion object { const val TOUCH_END = 2 }
}

/** Consumes the real UnitInfo button9 route request before creating id27. */
object BattleUnitInfoJiqiRoute {
    fun open(unitInfo: UnitInfoLayer, rates: List<Int>, event: Int): JiQiLayer? {
        if (!unitInfo.onButton(9, event)) return null
        val route = unitInfo.takeRoutes().lastOrNull { it.route == UnitInfoLayer.Route.JIQI } ?: return null
        return route.takeIf { it.index == 9 }?.let { JiQiLayer(rates) }
    }
}
