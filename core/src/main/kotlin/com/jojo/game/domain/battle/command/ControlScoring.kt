// Battle
package com.jojo.game.domain.battle.command

import com.jojo.game.domain.battle.*

/**
 * `ControlScoring` 싱글턴 객체: command 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

object ControlScoring {

    /**
     * `Arm` 싱글턴 객체: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    object Arm {
        /**
         * `QUAN_NENG` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val QUAN_NENG = 0
        /**
         * `WEN_GUAN` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val WEN_GUAN = 1
        /**
         * `WU_JIANG` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val WU_JIANG = 2
    }
    /**
     * `Lift` 싱글턴 객체: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    object Lift {
        /**
         * `DOWN` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val DOWN = 0
        /**
         * `NORMAL` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val NORMAL = 1
        /**
         * `UP` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val UP = 2
    }


    /**
     * `Status` 싱글턴 객체: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    object Status {
        /**
         * `ATT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ATT = 0
        /**
         * `DEF` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val DEF = 1
        /**
         * `SPR` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val SPR = 2
        /**
         * `CRI` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val CRI = 3
        /**
         * `MOR` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val MOR = 4
        /**
         * `MOV` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val MOV = 5
        /**
         * `MB` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val MB = 7
        /**
         * `JZ` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val JZ = 8
        /**
         * `HL` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val HL = 9
        /**
         * `ZD` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ZD = 10
        /**
         * `XD` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val XD = 14
        /**
         * `MAX` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val MAX = 15
    }


    /**
     * `Category` 싱글턴 객체: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    object Category {
        /**
         * `JDMJ` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val JDMJ = 4
        /**
         * `JDSQ` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val JDSQ = 5
        /**
         * `JDNL` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val JDNL = 6
        /**
         * `JDFY` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val JDFY = 7
        /**
         * `HL` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val HL = 8
        /**
         * `ZD` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ZD = 9
        /**
         * `MB` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val MB = 10
        /**
         * `FZ` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val FZ = 11
        /**
         * `HFZT` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val HFZT = 15
        /**
         * `ZJYDL` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ZJYDL = 16
        /**
         * `ZJMJ` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ZJMJ = 17
        /**
         * `ZJSQ` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ZJSQ = 18
        /**
         * `ZJNL` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ZJNL = 19
        /**
         * `ZJFY` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ZJFY = 20
        /**
         * `ZCXD` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val ZCXD = 21
        /**
         * `MX` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val MX = 24
        /**
         * `SQ` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val SQ = 30
    }


    /**
     * `Type` 싱글턴 객체: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    object Type {
        /**
         * `XISHOU_HP` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val XISHOU_HP = 5
        /**
         * `XISHOU_MP` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val XISHOU_MP = 6
        /**
         * `HUIFU_HP` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val HUIFU_HP = 19
        /**
         * `HUIFU_MP` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val HUIFU_MP = 20
    }
    /**
     * `Values` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Values(
        /**
         * `hpMpRate` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hpMpRate: Int = 100, val accuracyBase: Int = 100, val counterNoSkill: Int = 40,
        /**
         * `kill` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val kill: Int = 100, val famous: Int = 1, val healFamous: Int = 100, val healNormal: Int = 70,
        /**
         * `zszd` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zszd: Int = 100, val attackWj: Int = 20, val attackQn: Int = 15,
        /**
         * `defWj` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val defWj: Int = 20, val defQn: Int = 15, val defWg: Int = 10,
        /**
         * `sprWg` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sprWg: Int = 20, val sprQn: Int = 15, val criWj: Int = 20, val criQn: Int = 15,
        /**
         * `morWj` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val morWj: Int = 20, val morQn: Int = 15, val mov: Int = 10, val mabi: Int = 20,
        /**
         * `jzWg` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val jzWg: Int = 17, val jzQn: Int = 4, val jzWj: Int = 2,
        /**
         * `hlWg` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hlWg: Int = 22, val hlWj: Int = 28, val hlQn: Int = 30,
        /**
         * `zdWg` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zdWg: Int = 10, val zdWj: Int = 20, val zdQn: Int = 15,
        /**
         * `coverFriendly` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val coverFriendly: Int = 1, val coverEnemy: Int = 2, val distance: Int = 1
    )


    /**
     * `Unit` 계약 인터페이스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    interface Unit {
        /**
         * `index` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val index: Int
        /**
         * `hp` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hp: Int
        /**
         * `hpCur` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hpCur: Int
        /**
         * `mp` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mp: Int
        /**
         * `mpCur` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mpCur: Int
        /**
         * `armType` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val armType: Int
        /**
         * `isRemote` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val isRemote: Boolean
        /**
         * `famous` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val famous: Boolean
        /**
         * `mine` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mine: Boolean
        /**
         * `ai` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val ai: Int
        /**
         * `aiValue` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val aiValue: Int


        /**
         * `skill`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun skill(id: Int): Int


        /**
         * `status`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun status(index: Int): Int
        /**
         * `isCanXue`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun isCanXue(): Boolean
        /**
         * `isCanLan`: 조건과 입력 상태를 검증한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun isCanLan(): Boolean


        /**
         * `attackHarms`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun attackHarms(target: Unit): List<AttackHarm>


        /**
         * `magicHarm`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun magicHarm(magic: Magic, target: Unit): Int
    }


    /**
     * `AttackHarm` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class AttackHarm(val harm: Int, val target: Unit, val flag: Int = 0, val rate: Int = 100)


    /**
     * `Magic` 계약 인터페이스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    interface Magic {
        /**
         * `id` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val id: Int
        /**
         * `category` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val category: Int
        /**
         * `type` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val type: Int
        /**
         * `harmType` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val harmType: Int
        /**
         * `expendMp` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val expendMp: Int
    }

    /**
     * `Action` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Action(val value: Int, val targetIndex: Int, val magic: Magic? = null) {
        /**
         * `kind` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val kind get() = if (magic == null) "attack" else "magic"
    }


    /**
     * `Move` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Move(
        /**
         * `x` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Int,
        /**
         * `y` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Int,
        /**
         * `value` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Int,
        /**
         * `attacks` (List<Action>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attacks: List<Action> = emptyList(),
        /**
         * `magics` (List<Action>): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magics: List<Action> = emptyList()
    )


    /**
     * `Choice` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Choice(val x: Int, val y: Int, val value: Int, val action: Action? = null)


    /**
     * `choose`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
     * `coverPressure`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun coverPressure(distance: Int, sameCamp: Boolean, values: Values = Values()): Int {
        if (distance !in 1..4) return 0
        var pressure = (if (sameCamp) values.coverFriendly else values.coverEnemy) * (5 - distance)
        if (distance == 1) pressure *= 3 else if (distance == 2) pressure *= 2
        return if (sameCamp) pressure else -pressure
    }

    /**
     * `coverDistance`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun coverDistance(fromX: Int, fromY: Int, toX: Int, toY: Int): Int {
        val dx = kotlin.math.abs(fromX - toX)
        val dy = kotlin.math.abs(fromY - toY)
        val manhattan = dx + dy
        return if (manhattan == 2 && dx != 0 && dy != 0) manhattan else 0
    }

    /**
     * `Skills` 클래스: command 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Skills(
        /**
         * `noCounter` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val noCounter: Int = 226,
        /**
         * `counterSkills` (IntArray): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val counterSkills: IntArray = intArrayOf(44, 40, 181, 43, 232),
        /**
         * `zszd` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zszd: Int = 273,
    )

}
