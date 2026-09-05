package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleTurnSettlementServiceTest {

    @Test
    fun advanceToNextCampCyclesThroughAllFourFactions() {
        var camp = Faction.PLAYER
        val round = 1

        val (next1, res1) = BattleRoundCoordinator.advanceToNextCamp(camp, round)
        assertEquals(Faction.FRIEND, next1)
        assertEquals(Faction.FRIEND, res1.activeFaction)

        val (next2, res2) = BattleRoundCoordinator.advanceToNextCamp(next1, round)
        assertEquals(Faction.ENEMY, next2)
        assertEquals(Faction.ENEMY, res2.activeFaction)

        val (next3, res3) = BattleRoundCoordinator.advanceToNextCamp(next2, round)
        assertEquals(Faction.REINFORCEMENTS, next3)
        assertEquals(Faction.REINFORCEMENTS, res3.activeFaction)

        val (next4, res4) = BattleRoundCoordinator.advanceToNextCamp(next3, round)
        assertEquals(Faction.PLAYER, next4)
        assertEquals(Faction.PLAYER, res4.activeFaction)
    }

    @Test
    fun advanceRoundIncrementsRoundOnlyForReinforcements() {
        val (newRound, advance) = BattleRoundCoordinator.advanceRound(Faction.REINFORCEMENTS, 3)
        assertEquals(4, newRound)
        assertEquals(3, advance.completedRound)
        assertEquals(4, advance.round)
    }

    @Test
    fun turnSnapshotAndChangesDetectsHpAndStatusModifications() {
        val unit = BattleUnit(
            id = "u1",
            name = "Hero",
            faction = Faction.PLAYER,
            tileX = 0,
            tileY = 0,
            hitPoints = 100,
            maxHitPoints = 100,
            magicPoints = 50,
            maxMagicPoints = 50,
            statuses = mutableMapOf(BattleStatus.POISON to 2),
        )

        val before = BattleTurnSettlementService.turnSnapshot(listOf(unit))
        unit.hitPoints = 80
        unit.statuses[BattleStatus.POISON] = 1

        val changes = BattleTurnSettlementService.turnChanges(before) { id -> if (id == "u1") unit else null }
        assertEquals(1, changes.size)
        val change = changes.first()
        assertEquals("u1", change.unitId)
        assertEquals(100, change.hitPointsBefore)
        assertEquals(80, change.hitPointsAfter)
        assertEquals(mapOf(BattleStatus.POISON to 2), change.statusesBefore)
        assertEquals(mapOf(BattleStatus.POISON to 1), change.statusesAfter)
    }
}
