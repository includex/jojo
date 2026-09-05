package com.jojo.game.domain.battle.command

/**
 * Injectable, side-effect-free implementation of Control.js lines 325-666 and the
 * choice portion of `_AIProcess` (717-917).  Numeric enum values deliberately
 * mirror Config.js so the adapter does not reinterpret original data.
 */
/**
 * object  `ControlScoring`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object ControlScoring {

    object Arm {
        const val QUAN_NENG = 0
        const val WEN_GUAN = 1
        const val WU_JIANG = 2
    }

    /** BattleConfg.UNIT_STATUS_LIFT: DOWN=0, NORMAL=1, UP=2. */
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

    /** Values from Config.js AI_VALUE (lines 1568-1607). */
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

        /**
         * 공개 메서드 `skill`
         *
         * ### 파라미터
        - `id` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Int`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun skill(id: Int): Int

        /**
         * 공개 메서드 `status`
         *
         * ### 파라미터
        - `index` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Int`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun status(index: Int): Int

        /** BattleUnit.isCanXue(): HP is below its source weak threshold. */
        fun isCanXue(): Boolean

        /** BattleUnit.isCanLan(): MP is usable by this unit. */
        fun isCanLan(): Boolean

        /**
         * 공개 메서드 `attackHarms`
         *
         * ### 파라미터
        - `target` (`Unit`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `List<AttackHarm>`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun attackHarms(target: Unit): List<AttackHarm>

        /**
         * 공개 메서드 `magicHarm`
         *
         * ### 파라미터
        - `magic` (`Magic`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `target` (`Unit`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Int`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

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
    /**
     * The query adapter prepares each legal PS point and its already-scored
     * attack/magic candidates.  This is the final comparison loop from
     * Control._AIProcess lines 764-916: equal values retain earlier order.
     */

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

    /**
     * Control._AIProcess's nearby-unit cover term.  `distance` is the
     * BattleUnit.distance(..., 1) result.  The source deliberately has
     * stronger discontinuities at one and two tiles.
     */

    fun coverPressure(distance: Int, sameCamp: Boolean, values: Values = Values()): Int {
        if (distance !in 1..4) return 0
        var pressure = (if (sameCamp) values.coverFriendly else values.coverEnemy) * (5 - distance)
        if (distance == 1) pressure *= 3 else if (distance == 2) pressure *= 2
        return if (sameCamp) pressure else -pressure
    }

    /**
     * Exact `BattleUnit.distance(target, 1)` contract used by
     * `Control._AIProcess` when it scores nearby-unit cover. Flag bit 1 is
     * not a Manhattan-distance option: the recovered client keeps the value
     * only for a diagonally adjacent unit and returns zero otherwise.
     */

    fun coverDistance(fromX: Int, fromY: Int, toX: Int, toY: Int): Int {
        val dx = kotlin.math.abs(fromX - toX)
        val dy = kotlin.math.abs(fromY - toY)
        val manhattan = dx + dy
        return if (manhattan == 2 && dx != 0 && dy != 0) manhattan else 0
    }

    /** Source countAttackValue. `counter` is its fourth argument bit 0. */

    data class Skills(
        /** Config.SKILL_TYPE.WFJGJ (226): disables the counter-attack branch. */
        val noCounter: Int = 226,
        /**
         * Config.SKILL_TYPE FJHFJ(44), FTSH(40), QHFJ(181), FJBDSJ(43),
         * WXFJ(232).  Control._countAttackValue calls Unit.skill2 with this
         * exact ordered list before adding AI_VALUE.ATK_XWBFJ.
         */
        val counterSkills: IntArray = intArrayOf(44, 40, 181, 43, 232),
        /** Config.SKILL_TYPE.ZSZD (273). */
        val zszd: Int = 273,
    )

}
