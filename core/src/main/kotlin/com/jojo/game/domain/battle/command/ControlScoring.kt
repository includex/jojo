// Battle
package com.jojo.game.domain.battle.command

import com.jojo.game.domain.battle.*

object ControlScoring {

    object Arm {
        const val QUAN_NENG = 0
        const val WEN_GUAN = 1
        const val WU_JIANG = 2
    }
    object Lift {
        const val DOWN = 0
        const val NORMAL = 1
        const val UP = 2
    }


    object Status {
        const val ATT = 0
        const val DEF = 1
        const val SPR = 2
        const val CRI = 3
        const val MOR = 4
        const val MOV = 5
        const val MB = 7
        const val JZ = 8
        const val HL = 9
        const val ZD = 10
        const val XD = 14
        const val MAX = 15
    }


    object Category {
        const val JDMJ = 4
        const val JDSQ = 5
        const val JDNL = 6
        const val JDFY = 7
        const val HL = 8
        const val ZD = 9
        const val MB = 10
        const val FZ = 11
        const val HFZT = 15
        const val ZJYDL = 16
        const val ZJMJ = 17
        const val ZJSQ = 18
        const val ZJNL = 19
        const val ZJFY = 20
        const val ZCXD = 21
        const val MX = 24
        const val SQ = 30
    }


    object Type {
        const val XISHOU_HP = 5
        const val XISHOU_MP = 6
        const val HUIFU_HP = 19
        const val HUIFU_MP = 20
    }
    data class Values(
        val hpMpRate: Int = 100, val accuracyBase: Int = 100, val counterNoSkill: Int = 40,
        val kill: Int = 100, val famous: Int = 1, val healFamous: Int = 100, val healNormal: Int = 70,
        val zszd: Int = 100, val attackWj: Int = 20, val attackQn: Int = 15,
        val defWj: Int = 20, val defQn: Int = 15, val defWg: Int = 10,
        val sprWg: Int = 20, val sprQn: Int = 15, val criWj: Int = 20, val criQn: Int = 15,
        val morWj: Int = 20, val morQn: Int = 15, val mov: Int = 10, val mabi: Int = 20,
        val jzWg: Int = 17, val jzQn: Int = 4, val jzWj: Int = 2,
        val hlWg: Int = 22, val hlWj: Int = 28, val hlQn: Int = 30,
        val zdWg: Int = 10, val zdWj: Int = 20, val zdQn: Int = 15,
        val coverFriendly: Int = 1, val coverEnemy: Int = 2, val distance: Int = 1
    )


    interface Unit {
        val index: Int
        val hp: Int
        val hpCur: Int
        val mp: Int
        val mpCur: Int
        val armType: Int
        val isRemote: Boolean
        val famous: Boolean
        val mine: Boolean
        val ai: Int
        val aiValue: Int


        fun skill(id: Int): Int


        fun status(index: Int): Int
        fun isCanXue(): Boolean
        fun isCanLan(): Boolean


        fun attackHarms(target: Unit): List<AttackHarm>


        fun magicHarm(magic: Magic, target: Unit): Int
    }


    data class AttackHarm(val harm: Int, val target: Unit, val flag: Int = 0, val rate: Int = 100)


    interface Magic {
        val id: Int
        val category: Int
        val type: Int
        val harmType: Int
        val expendMp: Int
    }

    data class Action(val value: Int, val targetIndex: Int, val magic: Magic? = null) {
        val kind get() = if (magic == null) "attack" else "magic"
    }


    data class Move(
        val x: Int,
        val y: Int,
        val value: Int,
        val attacks: List<Action> = emptyList(),
        val magics: List<Action> = emptyList()
    )


    data class Choice(val x: Int, val y: Int, val value: Int, val action: Action? = null)


    fun choose(moves: Iterable<Move>): Choice? {
        var selected: Choice? = null
        for (move in moves) {
            var info: Action? = null
            for (a in move.attacks) if (a.value >= 1 && (info == null || a.value > info.value)) info = a
            for (m in move.magics) if (m.value >= 1 && (info == null || m.value > info.value)) info = m
            val candidate = Choice(move.x, move.y, move.value, info)
            val total = move.value + (info?.value ?: 0) + if (info != null) 30 else 0
            val old = selected
            val oldTotal = old?.let { it.value + (it.action?.value ?: 0) + if (it.action != null) 30 else 0 }
            if (old == null || total > oldTotal!!) selected = candidate
        }
        return selected
    }

    fun coverPressure(distance: Int, sameCamp: Boolean, values: Values = Values()): Int {
        if (distance !in 1..4) return 0
        var pressure = (if (sameCamp) values.coverFriendly else values.coverEnemy) * (5 - distance)
        if (distance == 1) pressure *= 3 else if (distance == 2) pressure *= 2
        return if (sameCamp) pressure else -pressure
    }

    fun coverDistance(fromX: Int, fromY: Int, toX: Int, toY: Int): Int {
        val dx = kotlin.math.abs(fromX - toX)
        val dy = kotlin.math.abs(fromY - toY)
        val manhattan = dx + dy
        return if (manhattan == 2 && dx != 0 && dy != 0) manhattan else 0
    }

    data class Skills(
        val noCounter: Int = 226,
        val counterSkills: IntArray = intArrayOf(44, 40, 181, 43, 232),
        val zszd: Int = 273,
    )

}
