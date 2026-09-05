package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleCounterLifeStealTest {
    @Test
    fun `counterattack life steal is retained separately from first attack result`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, hitPoints = 500, maxHitPoints = 500, skills = mapOf(92 to 0)),
                BattleUnit("d", "반격", Faction.ENEMY, 1, 0, hitPoints = 40, maxHitPoints = 100, attack = 100, skills = mapOf(238 to 50)),
            ),
            events = emptyList(),
        )

        val result = battle.attack("a", "d", damage = 10) as TacticalActionResult.Attack

        assertEquals(40 - result.damage + result.counterLifeStealHealing, battle.units.getValue("d").hitPoints)
        assertEquals(true, result.counterLifeStealHealing >= 0)
    }
}
