package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.settlement.sourceIndex
import com.jojo.game.presentation.battle.BattleScreenLoseCondition

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleLostFactionTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleLostFactionTest {
    @Test
    fun `loseTest follows the authored mine master while friendly actors survive`() {
        val master = BattleUnit("master", "조조", Faction.PLAYER, 0, 0, hitPoints = 0)
        val otherMine = BattleUnit("mine", "아군", Faction.PLAYER, 1, 0)
        val friend = BattleUnit("friend", "우군", Faction.FRIEND, 2, 0)

        assertTrue(BattleScreenLoseCondition.defeated(listOf(master, otherMine, friend), master.id))
        assertFalse(BattleScreenLoseCondition.defeated(listOf(otherMine, friend), otherMine.id))
        assertTrue(BattleScreenLoseCondition.defeated(listOf(friend), null), "FRIEND does not replace an absent MINE master")
    }

    @Test
    fun `lost swaps effective camp and preserves authored base faction`() {
        val mine = BattleUnit(
            "mine", "아군", Faction.PLAYER, 0, 0,
            statuses = linkedMapOf(BattleStatus.LOST to 2),
        )
        val enemy = BattleUnit(
            "enemy", "적군", Faction.REINFORCEMENTS, 1, 0,
            statuses = linkedMapOf(BattleStatus.LOST to 2),
        )

        assertEquals(Faction.PLAYER, mine.baseFaction)
        assertEquals(Faction.REINFORCEMENTS, mine.effectiveFaction())
        assertEquals(Faction.REINFORCEMENTS, mine.type())
        assertEquals(Faction.PLAYER, mine.type(baseCamp = true))
        assertEquals(Faction.PLAYER, mine.effectiveFaction(ignoreLost = true))
        assertEquals(Faction.REINFORCEMENTS, enemy.baseFaction)
        assertEquals(Faction.FRIEND, enemy.effectiveFaction())
        assertEquals(Faction.REINFORCEMENTS, enemy.effectiveFaction(ignoreLost = true))
    }

    @Test
    fun `lost round is decremented by the effective side state pass`() {
        val lost = BattleUnit(
            "lost", "길 잃은 아군", Faction.PLAYER, 0, 0,
            statuses = linkedMapOf(BattleStatus.LOST to 1),
        )
        val battle = Battle(
            units = listOf(lost, BattleUnit("mine", "아군", Faction.PLAYER, 1, 0)),
            events = emptyList(),
        )

        battle.roundLifecycle.settleActiveCampStart()
        assertEquals(1, lost.statuses[BattleStatus.LOST], "MINE pass must not consume an effectively hostile MS unit")
        battle.roundLifecycle.advanceToNextCamp() // FRIEND
        battle.roundLifecycle.advanceToNextCamp() // ENEMY
        val settlement = battle.roundLifecycle.settleActiveCampStart()

        assertFalse(BattleStatus.LOST in lost.statuses)
        assertEquals(Faction.PLAYER, lost.effectiveFaction())
        assertEquals(13, BattleStatus.LOST.sourceIndex)
        assertEquals(BattleStatus.LOST, BattleStatus.fromSourceIndex(13))
        assertEquals(setOf("lost"), settlement.changes.map { it.unitId }.toSet())
    }

    @Test
    fun `lost allied unit is selected only during reinforcement AI camp`() {
        val lost = BattleUnit(
            "lost", "길 잃은 아군", Faction.PLAYER, 0, 0,
            statuses = linkedMapOf(BattleStatus.LOST to 2),
        )
        val battle = Battle(
            units = listOf(
                lost,
                BattleUnit("mine", "아군", Faction.PLAYER, 4, 0),
            ),
            events = emptyList(),
        )
        repeat(3) { battle.roundLifecycle.advanceToNextCamp() }
        assertEquals(Faction.REINFORCEMENTS, battle.activeFaction)
        battle.roundLifecycle.prepareActiveCampOperation()

        assertTrue(battle.presentation.hasPendingAiUnits())
        battle.ai.resolveTurn(maxUnits = 1)
        assertTrue(lost.hasActed)
    }
}
