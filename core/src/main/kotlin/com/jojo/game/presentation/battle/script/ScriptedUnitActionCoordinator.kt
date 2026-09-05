package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.scenario.ScriptedUnitAction

/** Owns setAction/setAction2's finite clip callback and synchronous fast path. */
internal class ScriptedUnitActionCoordinator(
    private val lifecycle: ScriptedUnitPresentationLifecycle,
    private val port: Port,
) {
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
