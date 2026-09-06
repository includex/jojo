// Battle
package com.jojo.game.presentation.battle.fight

import com.jojo.game.domain.scenario.ScenarioFightCommand
import kotlin.math.max
/**
 * `FightAttackPlan`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class FightAttackPlan(
    val attacker: FightSide,
    val defender: FightSide,
    val attackerAction: Int,
    val defenderAction: Int,
    val hitAt: Float,
    val attackerEndsAt: Float,
    val defenderEndsAt: Float,
    val defenderStartsImmediately: Boolean,
    val completionClearsAllCallbacks: Boolean,
) {
    /**
     * `duration` (Float get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val duration: Float get() = max(attackerEndsAt, defenderEndsAt)
}
/**
 * `FightAttackPlanner`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal object FightAttackPlanner {
    /**
     * `attack1`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun attack1(command: ScenarioFightCommand.Attack1, duration: (Int) -> Float, hitTime: (Int) -> Float): FightAttackPlan {
        require(command.style in 0..4) { "FightLayer.attack1 style must be 0..4" }
        val attacker = if (command.mine) FightSide.MINE else FightSide.ENEMY
        val attackerAction = if (command.critical) 24 else 19
        val defenderAction = intArrayOf(21, 20, 22, 27, 23)[command.style]
        val hitAt = hitTime(attackerAction)
        return FightAttackPlan(attacker, attacker.other(), attackerAction, defenderAction, hitAt,
            duration(attackerAction), hitAt + duration(defenderAction), false, false)
    }

    /**
     * `attack2`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun attack2(command: ScenarioFightCommand.Attack2, duration: (Int) -> Float, hitTime: (Int) -> Float): FightAttackPlan {
        require(command.style in 0..2) { "FightLayer.attack2 style must be 0..2" }
        val attacker = if (command.mine) FightSide.MINE else FightSide.ENEMY
        val attackerAction = intArrayOf(16, 17, 18)[command.style]
        val defenderAction = if (command.style == 2) 18 else if (command.defended) 20 else 21
        val hitAt = hitTime(attackerAction)
        return FightAttackPlan(attacker, attacker.other(), attackerAction, defenderAction, hitAt,
            duration(attackerAction), hitAt + duration(defenderAction), command.style == 2, true)
    }

    /**
     * `FightSide`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun FightSide.other() = if (this == FightSide.MINE) FightSide.ENEMY else FightSide.MINE
}
