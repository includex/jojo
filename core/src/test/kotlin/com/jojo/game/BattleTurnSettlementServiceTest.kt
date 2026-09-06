// Test
package com.jojo.game

import com.jojo.game.domain.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*

import com.jojo.game.application.battle.BattleTurnSettlementService

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** BattleTurnSettlementServiceTest: BattleTurnSettlementService의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleTurnSettlementServiceTest {

    @Test
/** advanceToNextCampCyclesThroughAllFourFactions: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

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
/** advanceRoundIncrementsRoundOnlyForReinforcements: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

    fun advanceRoundIncrementsRoundOnlyForReinforcements() {
        val (newRound, advance) = BattleRoundCoordinator.advanceRound(Faction.REINFORCEMENTS, 3)
        assertEquals(4, newRound)
        assertEquals(3, advance.completedRound)
        assertEquals(4, advance.round)
    }

    @Test
/** turnSnapshotAndChangesDetectsHpAndStatusModifications: 지정한 조건의 테스트 장면을 구성하거나 결과를 검증하기 위한 보조 함수다. */

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
