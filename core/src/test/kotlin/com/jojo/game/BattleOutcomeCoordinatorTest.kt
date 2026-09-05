package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BattleOutcomeCoordinatorTest {

    @Test
    fun testOutcomeCalculations() {
        var round = 1
        var features = 0
        val units = mutableListOf<BattleUnit>()

        val coordinator = BattleOutcomeCoordinator(
            units = { units },
            getRound = { round },
            enabledFeatures = { features },
            initialMaxRounds = 10,
        )

        assertNull(coordinator.outcome())

        val playerUnit = BattleUnit(
            id = "player",
            name = "Player",
            faction = Faction.PLAYER,
            tileX = 0,
            tileY = 0,
            hitPoints = 100,
        )
        val enemyUnit = BattleUnit(
            id = "enemy",
            name = "Enemy",
            faction = Faction.ENEMY,
            tileX = 1,
            tileY = 1,
            hitPoints = 100,
        )
        units.add(playerUnit)
        units.add(enemyUnit)

        assertNull(coordinator.outcome())

        // Player victory when enemy defeated
        units.remove(enemyUnit)
        assertEquals(BattleOutcome.PLAYER_VICTORY, coordinator.outcome())

        // Enemy victory when round limit reached
        round = 10
        assertEquals(BattleOutcome.ENEMY_VICTORY, coordinator.outcome())

        // Scripted outcome overrides everything
        coordinator.setScriptedOutcome(BattleOutcome.PLAYER_VICTORY)
        assertEquals(BattleOutcome.PLAYER_VICTORY, coordinator.outcome())
    }

    @Test
    fun testMaxRoundsFeatureFlag() {
        var features = 0
        val coordinator = BattleOutcomeCoordinator(
            units = { emptyList() },
            getRound = { 1 },
            enabledFeatures = { features },
            initialMaxRounds = 10,
        )

        coordinator.setMaxRounds(12)
        assertEquals(12, coordinator.maxRounds)

        features = BattleOutcomeCoordinator.ENABLED_FEATURE_ZJHH
        coordinator.setMaxRounds(12)
        assertEquals(16, coordinator.maxRounds)

        coordinator.setResolvedMaxRounds(20)
        assertEquals(20, coordinator.maxRounds)
    }
}
