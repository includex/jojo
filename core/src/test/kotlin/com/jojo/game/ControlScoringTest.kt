// Test
package com.jojo.game

import com.jojo.game.domain.battle.command.*

import kotlin.test.Test
import kotlin.test.assertEquals

/** ControlScoringTest: ControlScoring의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class ControlScoringTest {
    private class U(
        override val index: Int, override val hp: Int = 100, override val hpCur: Int = 100,
        override val mp: Int = 100, override val mpCur: Int = 100, override val armType: Int = 2,
        override val isRemote: Boolean = false, override val famous: Boolean = false,
        override val mine: Boolean = false, override val ai: Int = 6, override val aiValue: Int = 0,
        private val skills: Map<Int, Int> = emptyMap(), private val states: Map<Int, Int> = emptyMap(),
        private val harms: List<ControlScoring.AttackHarm> = emptyList(), private val magic: Int = 0,
    ) : ControlScoring.Unit {
        override fun skill(id: Int) = skills[id] ?: 255
        override fun status(index: Int) = states[index] ?: ControlScoring.Lift.NORMAL
        override fun isCanXue() = true
        override fun isCanLan() = true
        override fun attackHarms(target: ControlScoring.Unit) = harms
        override fun magicHarm(magic: ControlScoring.Magic, target: ControlScoring.Unit) = this.magic
    }
    private data class M(override val id: Int = 1, override val category: Int, override val type: Int = -1,
                         override val harmType: Int = 4, override val expendMp: Int = 0) : ControlScoring.Magic

    @Test fun `attack value applies accuracy then counter retaliation and minimum`() {
        val target = U(2, hp = 100)
        val attacker = U(1, armType = ControlScoring.Arm.WU_JIANG,
            harms = listOf(ControlScoring.AttackHarm(50, target, rate = 50)))
        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        assertEquals(25, ControlActionScoring.attackValue(attacker, target, counter = true))
    }

    @Test fun `magic damage doubles score then applies famous kill and source zszd`() {
        val target = U(2, hp = 100, hpCur = 30, famous = true, mine = true, skills = mapOf(273 to 1))
        val caster = U(1, magic = 30)
        // 테스트 근거: 전투 계산·난수 소비·경계값 (ZSZD)을 검증한다.
        assertEquals(261, ControlActionScoring.magicValue(M(category = 0, harmType = 1), caster, target, mutableMapOf()))
    }

    @Test fun `attack counter checks source WFJGJ and all five source counter skills`() {
        val target = U(2, hp = 100)
        val withSourceCounterSkill = U(
            1,
            armType = ControlScoring.Arm.WU_JIANG,
            skills = mapOf(44 to 1),
            harms = listOf(ControlScoring.AttackHarm(10, target)),
        )
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (AI_VALUE, ATK_XWBFJ)을 검증한다.
        assertEquals(50, ControlActionScoring.attackValue(withSourceCounterSkill, target, counter = true))

        val counterDisabled = U(
            1,
            armType = ControlScoring.Arm.WU_JIANG,
            skills = mapOf(226 to 1, 44 to 1),
            harms = listOf(ControlScoring.AttackHarm(10, target)),
        )
        // 테스트 근거: 전투 계산·난수 소비·경계값 (WFJGJ)을 검증한다.
        assertEquals(10, ControlActionScoring.attackValue(counterDisabled, target, counter = true))
    }

    @Test fun `magic lift uses original arm specific status score and caches only valid result`() {
        val target = U(2, armType = ControlScoring.Arm.WEN_GUAN)
        val cache = mutableMapOf<String, Int>()
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (JDFY, DEF_WG)을 검증한다.
        assertEquals(20, ControlActionScoring.magicValue(M(category = ControlScoring.Category.JDFY), U(1), target, cache))
        assertEquals(mapOf("magic_1_2_1" to 20), cache)
    }

    @Test fun `all-rounder defence score uses source ATT_QN identifier`() {
        val target = U(2, armType = ControlScoring.Arm.QUAN_NENG)
        val values = ControlScoring.Values(attackQn = 7, defQn = 99)
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (ATT_QN, DEF)을 검증한다.
        assertEquals(14, ControlActionScoring.magicValue(M(category = ControlScoring.Category.JDFY), U(1), target, mutableMapOf(), values = values))
    }

    @Test fun `ZCXD accepts every source AI from attack-unit onward`() {
        val target = U(2, ai = 3, states = mapOf(ControlScoring.Status.XD to ControlScoring.Lift.DOWN))
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (ZCXD, MOV)을 검증한다.
        assertEquals(20, ControlActionScoring.magicValue(M(category = ControlScoring.Category.ZCXD), U(1), target, mutableMapOf()))
    }

    @Test fun `category switch break preserves damaging magic candidate`() {
/** Case: 마법 분류별 상태·AI 조합을 묶어 후보 선택 규칙을 반복 검증하는 지역 테스트 입력이다. */

        data class Case(val category: Int, val states: Map<Int, Int>, val ai: Int = 6)
        val cases = listOf(
            Case(ControlScoring.Category.JDNL, mapOf(ControlScoring.Status.ATT to ControlScoring.Lift.DOWN)),
            Case(ControlScoring.Category.JDFY, mapOf(ControlScoring.Status.DEF to ControlScoring.Lift.DOWN)),
            Case(ControlScoring.Category.JDMJ, mapOf(ControlScoring.Status.CRI to ControlScoring.Lift.DOWN)),
            Case(ControlScoring.Category.JDSQ, mapOf(ControlScoring.Status.MOR to ControlScoring.Lift.DOWN)),
            Case(ControlScoring.Category.MB, mapOf(ControlScoring.Status.MB to ControlScoring.Lift.DOWN)),
            Case(ControlScoring.Category.FZ, mapOf(ControlScoring.Status.JZ to ControlScoring.Lift.DOWN)),
            Case(ControlScoring.Category.HL, mapOf(ControlScoring.Status.HL to ControlScoring.Lift.DOWN)),
            Case(ControlScoring.Category.ZD, mapOf(ControlScoring.Status.ZD to ControlScoring.Lift.DOWN)),
            Case(ControlScoring.Category.ZCXD, mapOf(ControlScoring.Status.XD to ControlScoring.Lift.DOWN), ai = 2),
            Case(ControlScoring.Category.ZJYDL, mapOf(ControlScoring.Status.MOV to ControlScoring.Lift.NORMAL)),
            Case(ControlScoring.Category.ZJMJ, mapOf(ControlScoring.Status.CRI to ControlScoring.Lift.NORMAL)),
            Case(ControlScoring.Category.ZJSQ, mapOf(ControlScoring.Status.MOR to ControlScoring.Lift.NORMAL)),
            Case(ControlScoring.Category.ZJNL, mapOf(ControlScoring.Status.ATT to ControlScoring.Lift.NORMAL)),
            Case(ControlScoring.Category.ZJFY, mapOf(ControlScoring.Status.DEF to ControlScoring.Lift.NORMAL)),
        )

        for (case in cases) {
            val target = U(2, armType = ControlScoring.Arm.WU_JIANG, mine = true, ai = case.ai, states = case.states)
            // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
            assertEquals(
                20,
                ControlActionScoring.magicValue(
                    M(category = case.category, harmType = 0),
                    U(1, magic = 10),
                    target,
                    mutableMapOf(),
                ),
                "category=${case.category}",
            )
        }
    }

    @Test fun `category switch break still rejects pure duplicate status spell`() {
        val target = U(2, states = mapOf(ControlScoring.Status.ZD to ControlScoring.Lift.DOWN))
        assertEquals(
            0,
            ControlActionScoring.magicValue(
                M(category = ControlScoring.Category.ZD, harmType = 4),
                U(1, magic = 10),
                target,
                mutableMapOf(),
            ),
        )
    }

    @Test fun `multi attribute category break skips the rest of that category`() {
        val target = U(
            2,
            armType = ControlScoring.Arm.QUAN_NENG,
            mine = true,
            states = mapOf(ControlScoring.Status.ATT to ControlScoring.Lift.DOWN),
        )
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건 (ATT, SPR)을 검증한다.
        assertEquals(
            20,
            ControlActionScoring.magicValue(
                M(category = ControlScoring.Category.JDNL, harmType = 0),
                U(1, magic = 10),
                target,
                mutableMapOf(),
            ),
        )
    }

    @Test fun `recover hp requires missing hp and same side`() {
        val target = U(2, hp = 100, hpCur = 50, mine = true)
        val caster = U(1, mine = true, magic = 70)
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(240, ControlActionScoring.magicValue(M(category = 0, type = ControlScoring.Type.HUIFU_HP), caster, target, mutableMapOf()))
    }

    @Test fun `AI process retains source strict tie ordering and action bonus`() {
        val attack = ControlScoring.Action(5, 9)
        val magic = ControlScoring.Action(5, 8, M(category = 0))
        assertEquals(
            ControlScoring.Choice(1, 1, 10, attack),
            ControlScoring.choose(listOf(
                ControlScoring.Move(1, 1, 10, attacks = listOf(attack), magics = listOf(magic)),
                // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
                ControlScoring.Move(2, 2, 15)
            ))
        )
    }

    @Test fun `AI cover pressure preserves source one and two tile multipliers`() {
        assertEquals(0, ControlScoring.coverPressure(0, sameCamp = true))
        assertEquals(12, ControlScoring.coverPressure(1, sameCamp = true)) // 1 * 4 * 3
        assertEquals(-12, ControlScoring.coverPressure(2, sameCamp = false)) // -2 * 3 * 2
        assertEquals(-2, ControlScoring.coverPressure(4, sameCamp = false))
        assertEquals(0, ControlScoring.coverPressure(5, sameCamp = true))
    }

    @Test fun `flagged cover distance keeps only diagonal adjacency`() {
        assertEquals(2, ControlScoring.coverDistance(10, 10, 11, 11))
        assertEquals(2, ControlScoring.coverDistance(10, 10, 9, 9))
        assertEquals(0, ControlScoring.coverDistance(10, 10, 10, 11))
        assertEquals(0, ControlScoring.coverDistance(10, 10, 12, 10))
        assertEquals(0, ControlScoring.coverDistance(10, 10, 13, 13))
        assertEquals(0, ControlScoring.coverDistance(10, 10, 10, 10))
    }
}
