// Battle
package com.jojo.game.presentation.battle.fight

import com.jojo.game.domain.scenario.ScenarioFightCommand
import kotlin.math.max
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
    val duration: Float get() = max(attackerEndsAt, defenderEndsAt)
}
internal object FightAttackPlanner {
    fun attack1(command: ScenarioFightCommand.Attack1, duration: (Int) -> Float, hitTime: (Int) -> Float): FightAttackPlan {
        require(command.style in 0..4) { "FightLayer.attack1 style must be 0..4" }
        val attacker = if (command.mine) FightSide.MINE else FightSide.ENEMY
        val attackerAction = if (command.critical) 24 else 19
        val defenderAction = intArrayOf(21, 20, 22, 27, 23)[command.style]
        val hitAt = hitTime(attackerAction)
        return FightAttackPlan(attacker, attacker.other(), attackerAction, defenderAction, hitAt,
            duration(attackerAction), hitAt + duration(defenderAction), false, false)
    }

    fun attack2(command: ScenarioFightCommand.Attack2, duration: (Int) -> Float, hitTime: (Int) -> Float): FightAttackPlan {
        require(command.style in 0..2) { "FightLayer.attack2 style must be 0..2" }
        val attacker = if (command.mine) FightSide.MINE else FightSide.ENEMY
        val attackerAction = intArrayOf(16, 17, 18)[command.style]
        val defenderAction = if (command.style == 2) 18 else if (command.defended) 20 else 21
        val hitAt = hitTime(attackerAction)
        return FightAttackPlan(attacker, attacker.other(), attackerAction, defenderAction, hitAt,
            duration(attackerAction), hitAt + duration(defenderAction), command.style == 2, true)
    }

    private fun FightSide.other() = if (this == FightSide.MINE) FightSide.ENEMY else FightSide.MINE
}
