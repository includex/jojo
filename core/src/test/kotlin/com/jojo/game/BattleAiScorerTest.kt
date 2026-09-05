package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleAiScorerTest {

    private fun unit(
        id: String,
        faction: Faction = Faction.ENEMY,
        tileX: Int = 5,
        tileY: Int = 5,
        hitPoints: Int = 100,
        maxHitPoints: Int = 100,
        armType: Int = 0,
        movement: Int = 4,
        attackOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, 0 to -1, -1 to 0),
        attackAllScreen: Boolean = false,
        statuses: Map<BattleStatus, Int> = emptyMap(),
    ) = BattleUnit(
        id = id,
        name = id,
        faction = faction,
        tileX = tileX,
        tileY = tileY,
        hitPoints = hitPoints,
        maxHitPoints = maxHitPoints,
        armType = armType,
        movement = movement,
        attackOffsets = attackOffsets,
        attackAllScreen = attackAllScreen,
        statuses = statuses.toMutableMap(),
    )

    @Test
    fun `canAttack and canAttackFrom match target offsets`() {
        val attacker = unit("attacker", tileX = 2, tileY = 2)
        val targetAdjacent = unit("target1", tileX = 2, tileY = 3)
        val targetFar = unit("target2", tileX = 5, tileY = 5)

        assertTrue(BattleAiScorer.canAttack(attacker, targetAdjacent))
        assertFalse(BattleAiScorer.canAttack(attacker, targetFar))

        assertTrue(BattleAiScorer.canAttackFrom(attacker, 5, 4, targetFar))
        assertFalse(BattleAiScorer.canAttackFrom(attacker, 0, 0, targetFar))

        val allScreenAttacker = unit("allScreen", attackAllScreen = true)
        assertTrue(BattleAiScorer.canAttack(allScreenAttacker, targetFar))
    }

    @Test
    fun `aiSortValue computes wounded, status penalties, and movement deductions`() {
        val healthy = unit("healthy", hitPoints = 100, maxHitPoints = 100, movement = 4)
        val wounded = unit("wounded", hitPoints = 10, maxHitPoints = 100, movement = 4)
        val confused = unit("confused", hitPoints = 100, maxHitPoints = 100, movement = 4, statuses = mapOf(BattleStatus.CONFUSION to 2))

        val healthyScore = BattleAiScorer.aiSortValue(healthy, null, emptyMap())
        val woundedScore = BattleAiScorer.aiSortValue(wounded, null, emptyMap())
        val confusedScore = BattleAiScorer.aiSortValue(confused, null, emptyMap())

        assertTrue(healthyScore > woundedScore)
        assertTrue(healthyScore > confusedScore)
        assertEquals(20.0, healthyScore - confusedScore, 0.001)
    }

    @Test
    fun `cocosAiBaseValueAt calculates base terrain impact and cover pressure`() {
        val terrain = BattleTerrainGrid(10, 10, List(10) { IntArray(10) { 0 } })
        val ally1 = unit("ally1", faction = Faction.PLAYER, tileX = 2, tileY = 2, armType = 1) // Civil arm checks cover
        val ally2 = unit("ally2", faction = Faction.PLAYER, tileX = 2, tileY = 3)

        val baseValue = BattleAiScorer.cocosAiBaseValueAt(
            ally1, 2, 2, listOf(ally1, ally2), terrain, emptyMap(),
        ) { a, b -> a.faction == b.faction }

        assertTrue(baseValue > 0)
    }
}
