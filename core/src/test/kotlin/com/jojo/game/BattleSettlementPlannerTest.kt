package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.settlement.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleSettlementPlannerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleSettlementPlannerTest {
    @Test
    fun `magic local settlement retains source h order and empty STATES rows`() {
        val first = BattleUnit("first", "첫", Faction.PLAYER, 0, 0)
        val second = BattleUnit("second", "둘", Faction.ENEMY, 1, 0)
        val local = MagicLocalSettlement(listOf(
            MagicLocalSettlementEntry(
                "first", emptyMap(), mapOf(BattleStatus.POISON to 3), emptyMap(), emptyMap(), true,
            ),
            // A successful damage-only spell reaches setCharInfoBykey(h,
            // STATES, {}) and therefore must not disappear from h.index.
            MagicLocalSettlementEntry(
                "second", emptyMap(), emptyMap(), emptyMap(), emptyMap(), true,
            ),
        ))

        val plan = BattleSettlementPlanner.planMagicLocal(
            local, Faction.PLAYER, snapshots(first, second),
        ) { state -> if (state.sourceStatusIndex == BattleStatus.POISON.sourceIndex) 77 else null }

        assertEquals(listOf("first", "second"), plan.units.map { it.unitId })
        assertTrue(plan.units.all { it.hasStatesPayload })
        assertEquals(emptyList(), plan.units.flatMap { it.infoDeltas })
        assertEquals(listOf("first"), plan.meffBuckets.single().targets.map { it.unitId })
    }

    @Test
    fun `plan retains values info timing effective camps and source state order`() {
        val first = BattleUnit("first", "아군", Faction.PLAYER, 0, 0)
        val second = BattleUnit("second", "적군", Faction.ENEMY, 1, 0)
        val settlement = CampSettlement(
            CampSettlementStage.START_STATE,
            Faction.ENEMY,
            listOf(
                BattleUnitTurnChange(
                    unitId = "first",
                    hitPointsBefore = 100,
                    hitPointsAfter = 94,
                    magicPointsBefore = 10,
                    magicPointsAfter = 9,
                    statusesBefore = linkedMapOf(BattleStatus.LOST to 1, BattleStatus.PARALYSIS to 2),
                    statusesAfter = linkedMapOf(BattleStatus.PARALYSIS to 1),
                    attributeLiftsBefore = emptyMap(),
                    attributeLiftsAfter = mapOf(BattleAttribute.ATTACK to 1),
                ),
                BattleUnitTurnChange(
                    unitId = "second",
                    hitPointsBefore = 50,
                    hitPointsAfter = 50,
                    magicPointsBefore = 0,
                    magicPointsAfter = 0,
                    statusesBefore = linkedMapOf(BattleStatus.POISON to 1),
                    statusesAfter = emptyMap(),
                    attributeLiftsBefore = emptyMap(),
                    attributeLiftsAfter = emptyMap(),
                ),
            ),
        )

        val plan = BattleSettlementPlanner.plan(settlement, snapshots(first, second)) { 77 }
        val firstPlan = plan.units.first()
        assertEquals(Faction.REINFORCEMENTS, firstPlan.effectiveFactionBefore)
        assertEquals(Faction.PLAYER, firstPlan.effectiveFactionAfter)
        assertEquals(SettlementInfoPanel.MINE, firstPlan.infoPanel)
        assertEquals(listOf(5, 1), firstPlan.infoDeltas.map { it.tickCount })
        assertEquals(.1f, firstPlan.preInfoDelaySeconds)
        assertEquals(.3f, firstPlan.infoCloseSeconds)
        assertEquals(1.6f, firstPlan.infoBarrierSeconds)
        assertEquals(
            listOf(13, 7, 0),
            firstPlan.stateChanges.map { it.sourceStatusIndex },
            "status insertion order precedes ATT..MOV lift order",
        )
        assertEquals(SettlementStateChangeKind.REMOVE, firstPlan.stateChanges[0].kind)
        assertEquals(SettlementStateChangeKind.ROUND_UPDATE, firstPlan.stateChanges[1].kind)

        assertEquals(1, plan.meffBuckets.size, "equal resolved meff IDs share one source bucket")
        assertEquals(77, plan.meffBuckets.single().key.actualMeffId)
        assertEquals(listOf("first", "second"), plan.meffBuckets.single().targets.map { it.unitId })
        assertEquals("second", plan.meffBuckets.single().callbackTargetUnitId)
        assertTrue(plan.fullyRepresented)
    }

    @Test
    fun `authored aura and restore experience branches cannot be reported complete`() {
        val aura = BattleUnit(
            "aura", "오라", Faction.PLAYER, 0, 0,
            skills = mapOf(103 to 1),
        )
        val trainee = BattleUnit(
            "trainee", "성장", Faction.PLAYER, 1, 0,
            skills = mapOf(149 to 1),
        )
        val unchanged = { stage: CampSettlementStage ->
            CampSettlement(stage, Faction.PLAYER, emptyList())
        }

        val start = BattleSettlementPlanner.plan(
            unchanged(CampSettlementStage.START_STATE),
            snapshots(aura, trainee),
        ) { null }
        assertFalse(start.fullyRepresented)
        assertEquals(SettlementPendingKind.LOCAL_AURA, start.pendingIntegrations.single().kind)
        assertEquals(listOf(aura.id), start.pendingIntegrations.single().unitIds)

        val restore = BattleSettlementPlanner.plan(
            unchanged(CampSettlementStage.END_RESTORE),
            snapshots(aura, trainee),
        ) { null }
        assertFalse(restore.fullyRepresented)
        assertEquals(SettlementPendingKind.EXPERIENCE_AND_LEVEL_UP, restore.pendingIntegrations.single().kind)
        assertEquals(listOf(trainee.id), restore.pendingIntegrations.single().unitIds)
    }

    @Test
    fun `pending aura lookup uses immutable effective faction snapshot`() {
        val lostPlayer = BattleUnit(
            "lost", "이탈", Faction.PLAYER, 0, 0,
            skills = mapOf(103 to 1),
            statuses = mutableMapOf(BattleStatus.LOST to 2),
        )

        val plan = BattleSettlementPlanner.plan(
            CampSettlement(CampSettlementStage.START_STATE, Faction.ENEMY, emptyList()),
            snapshots(lostPlayer),
        ) { null }

        assertEquals(listOf(lostPlayer.id), plan.pendingIntegrations.single().unitIds)
    }

    private fun snapshots(vararg units: BattleUnit): Map<String, SettlementUnitSnapshot> = units.associate { unit ->
        unit.id to SettlementUnitSnapshot(
            unit.id,
            unit.baseFaction,
            unit.skills.keys.toSet(),
            BattleStatus.LOST in unit.statuses,
        )
    }
}
