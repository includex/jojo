package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BattleRawDamageInputTest {
    private fun attackDamage(attacker: BattleUnit, target: BattleUnit): Int {
        val battle = Battle(listOf(attacker, target), events = emptyList())
        return assertIs<TacticalActionResult.Attack>(battle.attack(attacker.id, target.id)).damage
    }

    @Test
    fun `QXJD uses raw martial value instead of final attack ability`() {
        fun attacker(qxjd: Boolean) = BattleUnit(
            "a", "a", Faction.PLAYER, 0, 0, attack = 300, martial = 50,
            critical = 100, morale = 100, skills = buildMap { put(92, 0); if (qxjd) put(183, 3) },
        )
        fun target() = BattleUnit("t", "t", Faction.ENEMY, 1, 0, defense = 1, critical = 1, morale = 1)

        val without = attackDamage(attacker(false), target())
        val with = attackDamage(attacker(true), target())
        // floor(Unit.wuwei(WL) * 3 * 10 / 100) = 15, then source applies
        // this seeded attack's source JS-truthy 180% multiplier to the whole harm.
        assertEquals(27, with - without)
    }

    @Test
    fun `KZQB reduces only attacks whose source arm move sound is zero`() {
        fun attacker(moveSound: Int) = BattleUnit(
            "a", "a", Faction.PLAYER, 0, 0, attack = 300, critical = 100, morale = 100,
            armMoveSound = moveSound, skills = mapOf(92 to 0),
        )
        fun target() = BattleUnit("t", "t", Faction.ENEMY, 1, 0, defense = 1, critical = 1, morale = 1, skills = mapOf(139 to 11))

        val horseLike = attackDamage(attacker(0), target())
        val other = attackDamage(attacker(1), target())
        // KZQB is an 11 percentage-point rate reduction before the source
        // critical multiplier and final integer truncation.
        assertEquals(36, other - horseLike)
    }
}
