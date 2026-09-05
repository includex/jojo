package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

        battle.settleActiveCampStart()
        assertEquals(1, lost.statuses[BattleStatus.LOST], "MINE pass must not consume an effectively hostile MS unit")
        battle.advanceToNextCamp() // FRIEND
        battle.advanceToNextCamp() // ENEMY
        val settlement = battle.settleActiveCampStart()

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
        repeat(3) { battle.advanceToNextCamp() }
        assertEquals(Faction.REINFORCEMENTS, battle.activeFaction)
        battle.prepareActiveCampOperation()

        assertTrue(battle.hasPendingAiUnits())
        battle.resolveAiTurn(maxUnits = 1)
        assertTrue(lost.hasActed)
    }
}
