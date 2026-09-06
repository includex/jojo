// Battle
package com.jojo.game.domain.battle

import com.jojo.game.*

/** BattleAttackSequence: 공격 순서를 결정하는 규칙 집합으로, 유닛 상태에 맞는 일반·추가·반격 공격을 선택한다. */
object BattleAttackSequence {
    /**
     * `HIT_ATTACK` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val HIT_ATTACK = 21
    /**
     * `GONG_JI2` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val GONG_JI2 = 25
    /**
     * `FANG_YU` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val FANG_YU = 26
    /**
     * `SHOU_GONG_JI3` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val SHOU_GONG_JI3 = 32
    /**
     * `GONG_JI_DELAY` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val GONG_JI_DELAY = 48
    /**
     * `HIT_ATTACK_DELAY` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val HIT_ATTACK_DELAY = 49
    /**
     * `BLOCK_QING` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val BLOCK_QING = 30
    /**
     * `BLOCK_ZHONG` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val BLOCK_ZHONG = 31
    /**
     * `HARM_QING` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val HARM_QING = 35
    /**
     * `HARM_ZHONG` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val HARM_ZHONG = 36

    /** selectAttackAction: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    fun selectAttackAction(critical: Boolean, attackDelay: Boolean): Int = when {
        critical && attackDelay -> HIT_ATTACK_DELAY
        critical -> HIT_ATTACK
        attackDelay -> GONG_JI_DELAY
        else -> GONG_JI2
    }
}
