// Battle
package com.jojo.game.domain.battle

import com.jojo.game.*

/** BattleAttackSequence: 공격 순서를 결정하는 규칙 집합으로, 유닛 상태에 맞는 일반·추가·반격 공격을 선택한다. */
object BattleAttackSequence {
    const val HIT_ATTACK = 21
    const val GONG_JI2 = 25
    const val FANG_YU = 26
    const val SHOU_GONG_JI3 = 32
    const val GONG_JI_DELAY = 48
    const val HIT_ATTACK_DELAY = 49
    const val BLOCK_QING = 30
    const val BLOCK_ZHONG = 31
    const val HARM_QING = 35
    const val HARM_ZHONG = 36

    /** selectAttackAction: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    fun selectAttackAction(critical: Boolean, attackDelay: Boolean): Int = when {
        critical && attackDelay -> HIT_ATTACK_DELAY
        critical -> HIT_ATTACK
        attackDelay -> GONG_JI_DELAY
        else -> GONG_JI2
    }
}
