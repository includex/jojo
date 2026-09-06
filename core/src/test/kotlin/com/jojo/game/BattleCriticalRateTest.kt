// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import java.util.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** BattleCriticalRateTest: BattleCriticalRate의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleCriticalRateTest {
    private class ValuesRandom(private vararg val values: Int) : Random() {
        private var index = 0
        override fun nextInt(bound: Int): Int = values[index++].mod(bound)
    }

    @Test
    fun `counterattack uses source 75 percent default and QHFJ removes only that penalty`() {
/** battle: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun battle(counterSkills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 10, defense = 20, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500),
                BattleUnit("t", "반격", Faction.ENEMY, 1, 0, attack = 80, defense = 20, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500, skills = counterSkills),
            ), events = emptyList(), random = ValuesRandom(0, 99, 99, 0, 99, 99),
        )
        val normal = assertIs<TacticalActionResult.Attack>(battle(emptyMap()).combat.attack("a", "t"))
        val qhfj = assertIs<TacticalActionResult.Attack>(battle(mapOf(181 to 0)).combat.attack("a", "t"))
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertEquals(42, normal.counterDamage)
        assertEquals(56, qhfj.counterDamage)
    }

    @Test
    fun `missing ZMYJZS keeps the source JS truthy 180 percent multiplier while explicit zero uses 150`() {
/** battle: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

        fun battle(skills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = skills),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, defense = 20, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500, attackOffsets = emptySet()),
            ), events = emptyList(),
        )
        val ordinary = assertIs<TacticalActionResult.Attack>(battle(mapOf(270 to 0)).combat.attack("a", "t"))
        val amplified = assertIs<TacticalActionResult.Attack>(battle(mapOf(270 to 0, 271 to 0)).combat.attack("a", "t"))
        assertEquals(-16, amplified.damage - ordinary.damage)
    }

    @Test
    fun `source count crit rate truncates 80 over 50 morale to 12 percent`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, morale = 80,
                    skills = mapOf(92 to 0, 226 to 0), rateAccumulators = linkedMapOf(6 to 75)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, morale = 50,
                    hitPoints = 500, maxHitPoints = 500, rateAccumulators = linkedMapOf(7 to 0)),
            ),
            events = emptyList(),
        )

        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        val result = assertIs<TacticalActionResult.Attack>(battle.combat.attack("a", "t"))
        assertEquals(false, result.critical)
    }
}
