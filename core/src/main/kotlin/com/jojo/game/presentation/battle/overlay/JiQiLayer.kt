package com.jojo.game.presentation.battle.overlay

import com.jojo.game.domain.battle.*
import com.jojo.game.UnitInfoLayer


/** Behavioural implementation of Battle id27 / Global JiQiLayer. */
class JiQiLayer(val rates: List<Int>) {
    init {
        require(rates.size == 8)
    }

    var attached = true
        private set

    /**
     * 공개 메서드 `onCancel`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onCancel(event: Int): Boolean {
        if (!attached || event != TOUCH_END) return false
        attached = false
        return true
    }

    companion object {
        const val TOUCH_END = 2
    }
}

/** Consumes the real UnitInfo button9 route request before creating id27. */
object BattleUnitInfoJiqiRoute {
    /**
     * 공개 메서드 `open`
     *
     * ### 파라미터
    - `unitInfo` (`UnitInfoLayer`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `rates` (`List<Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `JiQiLayer?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun open(unitInfo: UnitInfoLayer, rates: List<Int>, event: Int): JiQiLayer? {
        if (!unitInfo.onButton(9, event)) return null
        val route = unitInfo.takeRoutes().lastOrNull { it.route == UnitInfoLayer.Route.JIQI } ?: return null
        return route.takeIf { it.index == 9 }?.let { JiQiLayer(rates) }
    }
}
