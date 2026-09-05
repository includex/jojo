package com.jojo.game

import com.jojo.game.domain.battle.*


/** Result of FightUnit.__cb1 after resolving its optional `yidong` token. */
data class FightSoundDispatch(
    val resolvedId: Int,
    val effectId: Int? = null,
    val backgroundId: Int? = null,
)

/**
 * object  `FightSoundResolver`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object FightSoundResolver {
    /**
     * Recovered BattleUnit.moveSound/FightUnit.__cb1 contract.
     * ARM MOVESOUND 0/1/2 selects hoof/wheel/walk; 3 and negative values are
     * silent, while values above 3 fall back to hoof. Callback IDs above 300
     * address the background track after subtracting 300.
     */
    /**
     * 공개 메서드 `resolve`
     *
     * ### 파라미터
    - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `armMoveSound` (`Int = -1`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `FightSoundDispatch`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
