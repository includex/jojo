// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.scenario.ScriptedUnitAction

/** 스크립트 유닛 행동의 애니메이션 시작과 완료 콜백을 조정합니다. */
internal class ScriptedUnitActionCoordinator(
    /** `lifecycle` (ScriptedUnitPresentationLifecycle): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val lifecycle: ScriptedUnitPresentationLifecycle,
    /** `port` (Port): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val port: Port,
) {
    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        /**
         * `now`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun now(): Float
        /**
         * `consumeActions`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun consumeActions(): List<ScriptedUnitAction>
        /**
         * `unit`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun unit(action: ScriptedUnitAction): BattleUnit?
        /**
         * `applyDirection`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun applyDirection(unit: BattleUnit, direction: Int)
        /**
         * `clearVisual`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun clearVisual(unitId: String)
        /**
         * `setVisual`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun setVisual(unitId: String, action: Int, startedAt: Float)
        /**
         * `startSourceAction`: 흐름을 실행하거나 다음 단계로 전달한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun startSourceAction(unit: BattleUnit, action: Int)
        /**
         * `actionDuration`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun actionDuration(action: Int, direction: Int): Float
        /**
         * `focus`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun focus(unit: BattleUnit)
        /**
         * `clearSourceAction`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun clearSourceAction(unitId: String)
        /**
         * `defaultAction`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun defaultAction(unitId: String)
        /**
         * `resumeScript`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun resumeScript()
    }

    /**
     * `busy` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val busy: Boolean get() = lifecycle.actionBusy

    /** 대기 중인 유닛 행동을 시작합니다. */
    fun consumeStarts() {
        port.consumeActions().forEach { action ->
            val unit = port.unit(action)
            if (unit == null) {
                if (action.awaitsFinishedCallback) port.resumeScript()
                return@forEach
            }
            action.direction.takeIf { it in 0..3 }?.let { port.applyDirection(unit, it) }
            when {
                action.action == 0 -> port.clearVisual(unit.id)
                action.action in setOf(6, 25, 48) -> port.startSourceAction(unit, action.action)
                else -> port.setVisual(unit.id, action.action, port.now())
            }
            if (!action.awaitsFinishedCallback) return@forEach
            val duration = port.actionDuration(action.action, unit.direction)
            if (duration <= 0f) {
                port.resumeScript()
            } else {
                port.focus(unit)
                lifecycle.startAction(action, unit.id, port.now() + duration)
            }
        }
    }

    /** 완료 시각에 도달한 유닛 행동을 정리합니다. */
    fun driveCallback() {
        val active = lifecycle.activeAction ?: return
        if (port.now() < active.endsAt) return
        port.clearSourceAction(active.battleUnitId)
        lifecycle.clearVisual(active.battleUnitId)
        port.defaultAction(active.battleUnitId)
        lifecycle.finishAction()
        port.resumeScript()
    }
}
