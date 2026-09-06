// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.battle.*
import com.jojo.game.presentation.shared.overlay.UnitInfoLayer


/** 선택한 유닛의 기기 목록과 사용 가능 여부를 구성하고 선택 결과를 반환한다. */
class JiQiLayer(val rates: List<Int>) {
    init {
        require(rates.size == 8)
    }

    var attached = true
        private set


    fun onCancel(event: Int): Boolean {
        if (!attached || event != TOUCH_END) return false
        attached = false
        return true
    }

    companion object {
        const val TOUCH_END = 2
    }
}
object BattleUnitInfoJiqiRoute {

    fun open(unitInfo: UnitInfoLayer, rates: List<Int>, event: Int): JiQiLayer? {
        if (!unitInfo.onButton(9, event)) return null
        val route = unitInfo.takeRoutes().lastOrNull { it.route == UnitInfoLayer.Route.JIQI } ?: return null
        return route.takeIf { it.index == 9 }?.let { JiQiLayer(rates) }
    }
}
