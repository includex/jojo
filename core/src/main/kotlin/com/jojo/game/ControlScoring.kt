package com.jojo.game

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
    /**
     * object  `Arm`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * object  `Status`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * object  `Category`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * object  `Type`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * interface  `Unit`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * data class  `AttackHarm`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class AttackHarm(val harm: Int, val target: Unit, val flag: Int = 0, val rate: Int = 100)

    /**
     * interface  `Magic`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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
    /**
     * data class  `Action`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Action(val value: Int, val targetIndex: Int, val magic: Magic? = null) {
        val kind get() = if (magic == null) "attack" else "magic"
    }

    /**
     * data class  `Move`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Move(
        val x: Int,
        val y: Int,
        val value: Int,
        val attacks: List<Action> = emptyList(),
        val magics: List<Action> = emptyList()
    )

    /**
     * data class  `Choice`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Choice(val x: Int, val y: Int, val value: Int, val action: Action? = null)

    /**
     * 공개 메서드 `choose`
     *
     * ### 파라미터
    - `moves` (`Iterable<Move>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Choice?`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * Control._AIProcess's nearby-unit cover term.  `distance` is the
     * BattleUnit.distance(..., 1) result.  The source deliberately has
     * stronger discontinuities at one and two tiles.
     */
    /**
     * 공개 메서드 `coverPressure`
     *
     * ### 파라미터
    - `distance` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `sameCamp` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `values` (`Values = Values(`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
    /**
     * 공개 메서드 `coverDistance`
     *
     * ### 파라미터
    - `fromX` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `fromY` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `toX` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `toY` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun coverDistance(fromX: Int, fromY: Int, toX: Int, toY: Int): Int {
        val dx = kotlin.math.abs(fromX - toX)
        val dy = kotlin.math.abs(fromY - toY)
        val manhattan = dx + dy
        return if (manhattan == 2 && dx != 0 && dy != 0) manhattan else 0
    }

    /** Source countAttackValue. `counter` is its fourth argument bit 0. */
    fun attackValue(
        attacker: Unit,
        target: Unit,
        counter: Boolean,
        values: Values = Values(),
        skills: Skills = Skills()
    ): Int {
        var score = 0
        val wenGuan = attacker.armType == Arm.WEN_GUAN
        for (harm in attacker.attackHarms(target)) {
            var item = floorRate(harm.harm, values.hpMpRate, harm.target.hp)
            if (harm.rate != 100) item = floorRate(item, harm.rate, values.accuracyBase)
            score += item
            if (counter && attacker.skill(skills.noCounter) == 255 && attacker.status(Status.HL) == Lift.NORMAL) {
                if (attacker.skill(skills.counterSkills) != 255) score += values.counterNoSkill
                var retaliation = attackValue(harm.target, attacker, false, values, skills)
                retaliation -= when (attacker.armType) {
                    Arm.WU_JIANG -> if (attacker.isRemote) 0 else retaliation / 2
                    Arm.QUAN_NENG -> retaliation / 3
                    else -> 0
                }
                score -= retaliation
                if (!wenGuan) score = maxOf(1, score)
            }
            if (!wenGuan) {
                if (harm.flag and 4 != 0 && !attacker.isCanXue()) score += values.kill
                if (harm.flag and 2 != 0) score += values.famous
            }
        }
        if (!wenGuan && target.skill(skills.zszd) != 255) score += values.zszd
        return score
    }

    /**
     * data class  `Skills`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    private fun Unit.skill(ids: IntArray) = ids.firstOrNull { skill(it) != 255 }?.let(::skill) ?: 255

    /** Source `_countMagicValue`; cache is exactly its supplied object. */
    fun magicValue(
        magic: Magic, caster: Unit, target: Unit, cache: MutableMap<String, Int>,
        hitRate: (Unit, Unit, Magic) -> Int = { _, _, _ -> 100 }, values: Values = Values(), skills: Skills = Skills()
    ): Int {
        val key = "magic_${caster.index}_${target.index}_${magic.id}"
        cache[key]?.let { return it }
        var score = 0
        var hpSteal = false
        var lift = 0
        var abnormal = 0
        var famousMask = if (target.famous) 3 else 1
        when (magic.type) {
            Type.XISHOU_MP -> if (caster.isCanLan() && target.mpCur >= 1) {
                val harm = caster.magicHarm(magic, target).coerceIn(1, target.mpCur)
                if (harm >= 1) score += floorRate(harm, values.hpMpRate, target.mp)
            }

            Type.XISHOU_HP -> if (caster.isCanXue() && target.hpCur >= 1) hpSteal = true
            Type.HUIFU_MP -> if (target.isCanLan() && target.mine == caster.mine) {
                if (magic.category != Category.MX || caster.hpCur > 40) {
                    val gain = if (magic.category == Category.MX) {
                        -floorRate(40, values.hpMpRate, target.hp) + floorRate(target.mpCur, values.hpMpRate, target.mp)
                    } else {
                        val harm = caster.magicHarm(magic, target)
                        -floorRate(harm, values.hpMpRate, caster.mp) + floorRate(harm, values.hpMpRate, target.mp)
                    }
                    score += maxOf(1, gain)
                }
            }

            Type.HUIFU_HP -> if (target.isCanXue() && target.mine == caster.mine) {
                val harm = caster.magicHarm(magic, target).coerceIn(0, target.hp - target.hpCur)
                if (harm >= 1) score += floorRate(
                    harm,
                    values.hpMpRate,
                    target.hp
                ) + if (famousMask and 2 != 0) values.healFamous else values.healNormal
            }

            else -> {
                // These source branches use `break` inside the category switch.
                // That skips only the status contribution; it must not discard a
                // spell which also has a physical/magical harm component below.
                run category@{
                    when (magic.category) {
                        Category.HFZT -> for (s in Status.MB..Status.ZD) if (target.status(s) != Lift.NORMAL) abnormal =
                            abnormal or (1 shl s)

                        Category.JDNL -> {
                            val k = armMask(target.armType); if (k and 1 != 0) {
                                if (target.status(Status.ATT) == Lift.DOWN) return@category; lift =
                                    lift or (1 shl Status.ATT)
                            }; if (k and 2 != 0) {
                                if (target.status(Status.SPR) == Lift.DOWN) return@category; lift =
                                    lift or (1 shl Status.SPR)
                            }
                        }

                        Category.JDFY -> {
                            if (target.status(Status.DEF) == Lift.DOWN) return@category; lift =
                                lift or (1 shl Status.DEF)
                        }

                        Category.JDMJ -> {
                            if (target.status(Status.CRI) == Lift.DOWN) return@category; lift =
                                lift or (1 shl Status.CRI)
                        }

                        Category.JDSQ -> {
                            if (target.status(Status.MOR) == Lift.DOWN) return@category; lift =
                                lift or (1 shl Status.MOR)
                        }

                        Category.SQ -> for (s in Status.ATT..Status.MOV) if (target.status(s) != Lift.DOWN) lift =
                            lift or (1 shl s)

                        Category.MB -> {
                            if (target.status(Status.MB) != Lift.NORMAL) return@category; lift =
                                lift or (1 shl Status.MB)
                        }

                        Category.FZ -> {
                            if (target.status(Status.JZ) != Lift.NORMAL) return@category; lift =
                                lift or (1 shl Status.JZ)
                        }

                        Category.HL -> {
                            if (target.status(Status.HL) != Lift.NORMAL) return@category; lift =
                                lift or (1 shl Status.HL)
                        }

                        Category.ZD -> {
                            if (target.status(Status.ZD) != Lift.NORMAL) return@category; lift =
                                lift or (1 shl Status.ZD)
                        }
                        // Source checks AI.GONG_JI_WU_JIANG (3), not the later
                        // retreat controller value (6).  AI 3..9 can therefore
                        // be released from the action-complete state.
                        Category.ZCXD -> {
                            if (target.ai < 3 || target.status(Status.XD) == Lift.NORMAL) return@category; abnormal =
                                abnormal or (1 shl Status.MOV)
                        }

                        Category.ZJYDL -> {
                            if (target.status(Status.MOV) != Lift.DOWN) return@category; abnormal =
                                abnormal or (1 shl Status.MOV)
                        }

                        Category.ZJMJ -> {
                            if (target.status(Status.CRI) != Lift.DOWN) return@category; abnormal =
                                abnormal or (1 shl Status.CRI)
                        }

                        Category.ZJSQ -> {
                            if (target.status(Status.MOR) != Lift.DOWN) return@category; abnormal =
                                abnormal or (1 shl Status.MOR)
                        }

                        Category.ZJNL -> {
                            val k = armMask(target.armType); if (k and 1 != 0) {
                                if (target.status(Status.ATT) != Lift.DOWN) return@category; abnormal =
                                    abnormal or (1 shl Status.ATT)
                            }; if (k and 2 != 0) {
                                if (target.status(Status.SPR) != Lift.DOWN) return@category; abnormal =
                                    abnormal or (1 shl Status.SPR)
                            }
                        }

                        Category.ZJFY -> {
                            if (target.status(Status.DEF) != Lift.DOWN) return@category; abnormal =
                                abnormal or (1 shl Status.DEF)
                        }
                    }
                }
                score += statusValue(abnormal, target, values) + statusValue(lift, target, values)
            }
        }
        // MAGIC_HARM_TYPE.NO is 4; NORMAL is 0 and still deals damage.
        if (magic.harmType != 4) {
            var harm = caster.magicHarm(magic, target).coerceIn(1, target.hpCur)
            if (harm >= target.hpCur) famousMask = famousMask or 4
            score += if (caster.mine == target.mine) -floorRate(harm, values.hpMpRate, target.hp) else floorRate(
                harm,
                values.hpMpRate,
                target.hp
            )
            if (hpSteal) {
                harm = minOf(caster.hp - caster.hpCur, harm); score += floorRate(harm, values.hpMpRate, caster.hp)
            }
        }
        if (score < 1) return 0
        val rate = hitRate(caster, target, magic)
        if (rate != 100) score = floorRate(score, rate, 100)
        score += maxOf(1, score)
        if (famousMask and 2 != 0) score += values.famous
        if (famousMask and 4 != 0) score += values.kill
        if (target.skill(skills.zszd) != 255) score += values.zszd
        return score.also { cache[key] = it }
    }

    private fun armMask(arm: Int) = when (arm) {
        Arm.QUAN_NENG -> 3; Arm.WEN_GUAN -> 2; else -> 1
    }

    private fun floorRate(value: Int, rate: Int, divisor: Int) = if (divisor == 0) 0 else value * rate / divisor
    private fun statusValue(mask: Int, unit: Unit, v: Values): Int {
        var n = 0

        /**
         * 공개 메서드 `has`
         *
         * ### 파라미터
        - `s` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun has(s: Int) = mask and (1 shl s) != 0
        if (has(Status.ATT)) n += if (unit.armType == Arm.QUAN_NENG) v.attackQn else if (unit.armType == Arm.WU_JIANG) v.attackWj else 0
        // Control.js intentionally uses ATT_QN (not DEF_QN) for a
        // all-rounder's defence entry.  They currently share the same
        // Config value, but preserving the source identifier matters for
        // injected parity tables.
        if (has(Status.DEF)) n += when (unit.armType) {
            Arm.QUAN_NENG -> v.attackQn; Arm.WEN_GUAN -> v.defWg; else -> v.defWj
        }
        if (has(Status.SPR)) n += if (unit.armType == Arm.QUAN_NENG) v.sprQn else if (unit.armType == Arm.WEN_GUAN) v.sprWg else 0
        if (has(Status.CRI)) n += if (unit.armType == Arm.QUAN_NENG) v.criQn else if (unit.armType == Arm.WU_JIANG) v.criWj else 0
        if (has(Status.MOR)) n += if (unit.armType == Arm.QUAN_NENG) v.morQn else if (unit.armType == Arm.WU_JIANG) v.morWj else 0
        if (has(Status.MOV)) n += v.mov
        if (has(Status.MB)) n += v.mabi
        if (has(Status.JZ)) n += when (unit.armType) {
            Arm.QUAN_NENG -> v.jzQn; Arm.WEN_GUAN -> v.jzWg; else -> v.jzWj
        }
        if (has(Status.HL)) n += when (unit.armType) {
            Arm.QUAN_NENG -> v.hlQn; Arm.WEN_GUAN -> v.hlWg; else -> v.hlWj
        }
        if (has(Status.ZD)) n += when (unit.armType) {
            Arm.QUAN_NENG -> v.zdQn; Arm.WEN_GUAN -> v.zdWg; else -> v.zdWj
        }
        if (has(Status.XD)) n += unit.aiValue
        return n
    }
}
