package com.jojo.game.presentation.battle.fight

import com.jojo.game.domain.scenario.ScenarioFightCommand
import com.jojo.game.presentation.battle.FightUnitPresentation

/** Owns FightLayer's slot assignment and initial anchor transforms. */
internal data class FightStartLayout(
    val backgroundIndex: Int,
    val mineIndex: Int,
    val enemyIndex: Int,
    val mineCharacterId: Int,
    val enemyCharacterId: Int,
)

internal object FightStartSequence {
    fun layout(command: ScenarioFightCommand.Start, isMineUnit: (Int) -> Boolean?): FightStartLayout {
        val mineIndex = if (isMineUnit(command.firstUnitId) ?: true) 0 else 1
        val enemyIndex = if (mineIndex == 0) 1 else 0
        return FightStartLayout(
            command.backgroundIndex,
            mineIndex,
            enemyIndex,
            if (mineIndex == 0) command.firstUnitId else command.secondUnitId,
            if (enemyIndex == 0) command.firstUnitId else command.secondUnitId,
        )
    }

    fun resetSlot(fighter: FightUnitPresentation, slot: Int) {
        fighter.parentX = if (slot == 0) -200f else 200f
        fighter.parentScaleX = if (slot == 0) -4f else 4f
        fighter.childX = 0f
        fighter.childScaleX = 1f
        fighter.action = null
        fighter.actionElapsedSeconds = 0f
        fighter.zIndex = 0
    }
}
