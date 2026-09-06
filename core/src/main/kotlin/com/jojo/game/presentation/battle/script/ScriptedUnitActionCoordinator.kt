// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.scenario.ScriptedUnitAction

/** 스크립트 유닛 행동의 애니메이션 시작과 완료 콜백을 조정합니다. */
internal class ScriptedUnitActionCoordinator(
    private val lifecycle: ScriptedUnitPresentationLifecycle,
    private val port: Port,
) {
    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        fun now(): Float
        fun consumeActions(): List<ScriptedUnitAction>
        fun unit(action: ScriptedUnitAction): BattleUnit?
        fun applyDirection(unit: BattleUnit, direction: Int)
        fun clearVisual(unitId: String)
        fun setVisual(unitId: String, action: Int, startedAt: Float)
        fun startSourceAction(unit: BattleUnit, action: Int)
        fun actionDuration(action: Int, direction: Int): Float
        fun focus(unit: BattleUnit)
        fun clearSourceAction(unitId: String)
        fun defaultAction(unitId: String)
        fun resumeScript()
    }

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
