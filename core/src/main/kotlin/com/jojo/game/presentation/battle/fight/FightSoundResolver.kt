// Battle
package com.jojo.game.presentation.battle.fight

import com.jojo.game.domain.battle.*
/**
 * `FightSoundDispatch`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

data class FightSoundDispatch(
    val resolvedId: Int,
    val effectId: Int? = null,
    val backgroundId: Int? = null,
)
/**
 * `FightSoundResolver`: 관련 상태와 동작을 묶는 object다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

object FightSoundResolver {
    /** resolve: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */

    fun resolve(value: String, armMoveSound: Int = -1): FightSoundDispatch {
        val resolved = if (value == "yidong") {
            when (if (armMoveSound > 3) 0 else armMoveSound) {
                0 -> 24
                1 -> 25
                2 -> 23
                else -> -1
            }
        } else value.toIntOrNull() ?: -1
        return when {
            resolved < 0 -> FightSoundDispatch(resolved)
            resolved > 300 -> FightSoundDispatch(resolved, backgroundId = resolved - 300)
            else -> FightSoundDispatch(resolved, effectId = resolved)
        }
    }
}
