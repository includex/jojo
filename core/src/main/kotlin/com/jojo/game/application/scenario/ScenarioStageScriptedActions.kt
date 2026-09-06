// Game
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.TacticalUnit

/** ScenarioStageScriptedActions: 스크립트가 요청한 유닛 동작 기록과 표시 대기열을 관리한다. */
internal class ScenarioStageScriptedActions {
    val attacks = mutableListOf<ScriptedAttackAction>()
    val unitActions = mutableListOf<ScriptedUnitAction>()
    val unitDirections = mutableListOf<Pair<Int, Int>>()

    fun attack(attackerId: Int, targetId: Int, flag: Int) {
        attacks += ScriptedAttackAction(attackerId, targetId, flag)
    }

    fun setUnitAction(
        unitId: Int,
        action: Int,
        direction: Int,
        loop: Boolean,
        unitProvider: (Int) -> TacticalUnit,
        setUnitDirection: (Int, Int) -> Unit,
    ) {
        unitProvider(unitId).action = action
        if (direction >= 0) setUnitDirection(unitId, direction)
        unitActions += ScriptedUnitAction(unitId, action, direction, loop)
    }

    fun consumeAttacks(): List<ScriptedAttackAction> = attacks.toList().also { attacks.clear() }
    fun consumeUnitActions(): List<ScriptedUnitAction> = unitActions.toList().also { unitActions.clear() }
    fun consumeUnitDirections(): List<Pair<Int, Int>> = unitDirections.toList().also { unitDirections.clear() }
}
