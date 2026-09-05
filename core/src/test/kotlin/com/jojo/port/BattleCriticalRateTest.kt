package com.jojo.port

import java.util.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BattleCriticalRateTest {
    private class ValuesRandom(private vararg val values: Int) : Random() {
        private var index = 0
        override fun nextInt(bound: Int): Int = values[index++].mod(bound)
    }

    @Test
    fun `counterattack uses source 75 percent default and QHFJ removes only that penalty`() {
        fun battle(counterSkills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 10, defense = 20, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500),
                BattleUnit("t", "반격", Faction.ENEMY, 1, 0, attack = 80, defense = 20, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500, skills = counterSkills),
            ), events = emptyList(), random = ValuesRandom(0, 99, 99, 0, 99, 99),
        )
        val normal = assertIs<TacticalActionResult.Attack>(battle(emptyMap()).attack("a", "t"))
        val qhfj = assertIs<TacticalActionResult.Attack>(battle(mapOf(181 to 0)).attack("a", "t"))
        // Counter base is 56; original default is floor(56 * 75 / 100).
        assertEquals(42, normal.counterDamage)
        assertEquals(56, qhfj.counterDamage)
    }

    @Test
    fun `missing ZMYJZS keeps the source JS truthy 180 percent multiplier while explicit zero uses 150`() {
        fun battle(skills: Map<Int, Int>) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = skills),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, defense = 20, critical = 1, morale = 1, hitPoints = 500, maxHitPoints = 500, attackOffsets = emptySet()),
            ), events = emptyList(),
        )
        val ordinary = assertIs<TacticalActionResult.Attack>(battle(mapOf(270 to 0)).attack("a", "t"))
        val amplified = assertIs<TacticalActionResult.Attack>(battle(mapOf(270 to 0, 271 to 0)).attack("a", "t"))
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

        // 75 + trunc((80 / 50 * .18 - .16) * 100) = 87, while the
        // opponent's source gauge becomes 88: no critical.  A rounded 13
        // would reverse this countRate boundary.
        val result = assertIs<TacticalActionResult.Attack>(battle.attack("a", "t"))
        assertEquals(false, result.critical)
    }
}
