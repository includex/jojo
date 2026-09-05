package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BattleKnockbackTest {
    @Test
    fun `TPGJ pushes a defender one passable tile directly away`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(92 to 0, 221 to 5)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, defense = 20, hitPoints = 500, maxHitPoints = 500, attackOffsets = emptySet()),
            ), events = emptyList(),
        )
        assertIs<TacticalActionResult.Attack>(battle.attack("a", "t"))
        assertEquals(2 to 0, battle.units.getValue("t").let { it.tileX to it.tileY })
    }

    @Test
    fun `TPGJ and YI_BU use source canBack result for blocked retreat`() {
        fun battle(blocked: Boolean) = Battle(
            units = listOf(
                BattleUnit("a", "공격", Faction.PLAYER, 0, 0, attack = 80, critical = 1, morale = 1, skills = mapOf(92 to 0, 221 to 5)),
                BattleUnit("t", "대상", Faction.ENEMY, 1, 0, defense = 20, hitPoints = 500, maxHitPoints = 500, attackOffsets = emptySet(), skills = mapOf(250 to 10)),
            ), events = emptyList(), blockedTiles = if (blocked) setOf(2 to 0) else emptySet(),
        )
        val open = battle(false)
        val blocked = battle(true)
        val openResult = assertIs<TacticalActionResult.Attack>(open.attack("a", "t"))
        val blockedResult = assertIs<TacticalActionResult.Attack>(blocked.attack("a", "t"))
        // These are _countAttackHarmRate percentage points, not fixed harm:
        // blocked = +5(TPGJ)-5(YI_BU), open = -10(YI_BU).
        assertEquals(6, blockedResult.damage - openResult.damage)
        assertEquals(1 to 0, blocked.units.getValue("t").let { it.tileX to it.tileY })
    }
}
