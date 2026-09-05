package com.jojo.game

import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.turn.BattleCampTransitionRequest
import com.jojo.game.domain.battle.turn.BattleTurnEntryRequest
import com.jojo.game.domain.battle.turn.BattleTurnPhase
import com.jojo.game.domain.battle.turn.BattleTurnPolicy
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleTurnPolicyTest {
    @Test
    fun `only a live player input state accepts end turn`() {
        assertTrue(BattleTurnPolicy.acceptsPlayerEnd(
            BattleTurnEntryRequest(BattleTurnPhase.PLAYER_INPUT, Faction.PLAYER, null),
        ))
        assertFalse(BattleTurnPolicy.acceptsPlayerEnd(
            BattleTurnEntryRequest(BattleTurnPhase.AI, Faction.PLAYER, null),
        ))
        assertFalse(BattleTurnPolicy.acceptsPlayerEnd(
            BattleTurnEntryRequest(BattleTurnPhase.PLAYER_INPUT, Faction.ENEMY, null),
        ))
    }

    @Test
    fun `camp card policy only crosses player and enemy sides`() {
        assertFalse(BattleTurnPolicy.campCardFor(
            BattleCampTransitionRequest(Faction.PLAYER, Faction.FRIEND),
        ))
        assertTrue(BattleTurnPolicy.campCardFor(
            BattleCampTransitionRequest(Faction.FRIEND, Faction.ENEMY),
        ))
        assertTrue(BattleTurnPolicy.campCardFor(
            BattleCampTransitionRequest(Faction.REINFORCEMENTS, Faction.PLAYER),
        ))
    }
}
