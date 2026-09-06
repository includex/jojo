// Battle
package com.jojo.game.presentation.battle.fight

import com.jojo.game.domain.scenario.ScenarioFightCommand
/**
 * `FightStartLayout`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal data class FightStartLayout(
    val backgroundIndex: Int,
    val mineIndex: Int,
    val enemyIndex: Int,
    val mineCharacterId: Int,
    val enemyCharacterId: Int,
)

/** FightStartSequence: 시간 경과에 따른 전투 표현 순서와 상태 변화를 관리한다. */
internal object FightStartSequence {
    /**
     * `layout`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `resetSlot`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
