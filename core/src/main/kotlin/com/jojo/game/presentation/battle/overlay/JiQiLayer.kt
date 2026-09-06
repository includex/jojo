// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.battle.*
import com.jojo.game.presentation.shared.overlay.UnitInfoLayer


/** 선택한 유닛의 기기 목록과 사용 가능 여부를 구성하고 선택 결과를 반환한다. */
class JiQiLayer(val rates: List<Int>) {
    init {
        require(rates.size == 8)
    }

    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
        private set


    /**
     * `onCancel`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCancel(event: Int): Boolean {
        if (!attached || event != TOUCH_END) return false
        attached = false
        return true
    }

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
    }
}
/**
 * `BattleUnitInfoJiqiRoute`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object BattleUnitInfoJiqiRoute {

    /**
     * `open`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun open(unitInfo: UnitInfoLayer, rates: List<Int>, event: Int): JiQiLayer? {
        if (!unitInfo.onButton(9, event)) return null
        val route = unitInfo.takeRoutes().lastOrNull { it.route == UnitInfoLayer.Route.JIQI } ?: return null
        return route.takeIf { it.index == 9 }?.let { JiQiLayer(rates) }
    }
}
