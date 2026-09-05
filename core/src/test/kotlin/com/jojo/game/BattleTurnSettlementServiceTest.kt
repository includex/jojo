package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleTurnSettlementServiceTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleTurnSettlementServiceTest {

    @Test
/**
 * 공개 메서드 `advanceToNextCampCyclesThroughAllFourFactions`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

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
/**
 * 공개 메서드 `advanceRoundIncrementsRoundOnlyForReinforcements`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

    fun advanceRoundIncrementsRoundOnlyForReinforcements() {
        val (newRound, advance) = BattleRoundCoordinator.advanceRound(Faction.REINFORCEMENTS, 3)
        assertEquals(4, newRound)
        assertEquals(3, advance.completedRound)
        assertEquals(4, advance.round)
    }

    @Test
/**
 * 공개 메서드 `turnSnapshotAndChangesDetectsHpAndStatusModifications`
 *
 * ### 파라미터
- 입력 파라미터: 없음
 *
 * ### 응답 스펙
 * - 반환 타입: `Unit`
 * - 반환값: 동작 결과의 도메인 값입니다.
 */

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
