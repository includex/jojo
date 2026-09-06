// Game
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.TacticalUnit

/** ScenarioStageScriptedActions: 스크립트가 요청한 유닛 동작 기록과 표시 대기열을 관리한다. */
internal class ScenarioStageScriptedActions {
    /**
     * `attacks` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val attacks = mutableListOf<ScriptedAttackAction>()
    /**
     * `unitActions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val unitActions = mutableListOf<ScriptedUnitAction>()
    /**
     * `unitDirections` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val unitDirections = mutableListOf<Pair<Int, Int>>()

    /**
     * `attack`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun attack(attackerId: Int, targetId: Int, flag: Int) {
        attacks += ScriptedAttackAction(attackerId, targetId, flag)
    }

    /**
     * `setUnitAction`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `consumeAttacks`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeAttacks(): List<ScriptedAttackAction> = attacks.toList().also { attacks.clear() }
    /**
     * `consumeUnitActions`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitActions(): List<ScriptedUnitAction> = unitActions.toList().also { unitActions.clear() }
    /**
     * `consumeUnitDirections`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitDirections(): List<Pair<Int, Int>> = unitDirections.toList().also { unitDirections.clear() }
}
