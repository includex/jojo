package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class ControlAttackEffectAreaTest {
    @Test
    fun `Control countAtkHarm2 scores physical effect targets at original 75 percent`() {
        fun battle(effectOffsets: Set<Pair<Int, Int>>) = Battle(
            units = listOf(
                // WFJGJ is checked on the attacking unit by
                // Control._countAttackValue; it removes the counter branch
                // so this fixture isolates countAtkHarm2's splash reduction.
                BattleUnit("attacker", "공격", Faction.ENEMY, 0, 0, attack = 100, critical = 1, attackEffectOffsets = effectOffsets, skills = mapOf(226 to 1)),
                BattleUnit("primary", "주대상", Faction.PLAYER, 1, 0, defense = 1, critical = 1),
                BattleUnit("splash", "범위대상", Faction.PLAYER, 1, 1, defense = 1, critical = 1),
            ),
            events = emptyList(),
        )

        val single = battle(emptySet()).previewAiAttackValue("attacker", "primary")
        val area = battle(setOf(0 to 1)).previewAiAttackValue("attacker", "primary")

        // countAtkHarm2 deducts floor(25% * raw harm), rather than flooring
        // a 75%-multiplied score.  With matching HP/hit-rate this is
        // single - floor(single * 25 / 100), including its rounding edge.
        assertEquals(single + single - single * 25 / 100, area)
    }
}
