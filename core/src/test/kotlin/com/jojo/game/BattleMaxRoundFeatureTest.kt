package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleMaxRoundFeatureTest {
    @Test
    fun `BattleScreen setMaxRound adds original ZJHH four-turn feature only when enabled`() {
        fun battle(features: Int) = Battle(
            units = listOf(BattleUnit("u", "u", Faction.PLAYER, 0, 0)),
            events = emptyList(), enabledFeatures = features,
        )

        battle(0).also { it.setMaxRounds(12); assertEquals(12, it.maxRounds) }
        battle(8).also { it.setMaxRounds(12); assertEquals(16, it.maxRounds) }
        battle(16).also { it.setMaxRounds(12); assertEquals(12, it.maxRounds) }
    }

    @Test
    fun `resumed scene1 non annihilation result is mirrored into tactical outcome`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("mine", "mine", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "enemy", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(),
        )

        // S_12's escape branch ends while enemies still exist, so the
        // ordinary annihilation predicate deliberately remains unresolved.
        assertEquals(null, battle.outcome())
        battle.syncScriptedOutcome(BattleOutcome.PLAYER_VICTORY)
        assertEquals(BattleOutcome.PLAYER_VICTORY, battle.outcome())

        // A later ordinary scene1 pass carries no new result and must not
        // erase the authored terminal state.
        battle.syncScriptedOutcome(null)
        assertEquals(BattleOutcome.PLAYER_VICTORY, battle.outcome())
    }
}
