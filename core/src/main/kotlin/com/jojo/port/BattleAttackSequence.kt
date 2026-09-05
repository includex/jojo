package com.jojo.port

/** Shared source action IDs/selection used by the production attack timeline. */
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

        /** BattleLayer._attack2's `d ? ..._DELAY : ...` action choice. */
        fun selectAttackAction(critical: Boolean, attackDelay: Boolean): Int = when {
            critical && attackDelay -> HIT_ATTACK_DELAY
            critical -> HIT_ATTACK
            attackDelay -> GONG_JI_DELAY
            else -> GONG_JI2
        }
}
