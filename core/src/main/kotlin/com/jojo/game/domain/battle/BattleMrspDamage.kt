package com.jojo.game.domain.battle

/** Pure implementation of BattleUnit.count_attackHarm's MRSP random ladder. */
object BattleMrspDamage {
    /** `random(0, 99)` maps to 100%, 80%, 60%, 40%, or 20% max HP. */
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
