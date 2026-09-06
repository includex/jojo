// Battle
package com.jojo.game.domain.battle

/** BattleMrspDamage: 정신력 기반 공격의 피해 규칙을 계산하며, 대상의 방어와 상태 보정을 반영한다. */
object BattleMrspDamage {
    fun percent(roll: Int): Int {
        val value = roll.coerceIn(0, 99)
        var steps = 5
        var threshold = 5
        for (index in 0 until 4) {
            if (value < threshold) break
            steps--
            threshold += 5 + 2 * index
        }
        return 20 * steps
    }
}
