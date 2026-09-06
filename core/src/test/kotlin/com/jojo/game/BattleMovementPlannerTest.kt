// Test
package com.jojo.game

import com.jojo.game.domain.battle.BattleMovementPlanner

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** BattleMovementPlannerTest: BattleMovementPlanner의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleMovementPlannerTest {
    private data class Actor(val id: String, val side: Int)

    private val actor = Actor("actor", 0)
    private val offsets = listOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)

    @Test
    fun `reachable path retains authored down-first tie order`() {
        val planner = planner(width = 5, height = 5)

        val result = planner.movePoints(
            actor = actor,
            movement = 4,
            rules = BattleMovementPlanner.MovementRules(),
            startOverride = 0 to 0,
        )

        assertEquals(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 2 to 2), result.pathTo(2 to 2))
    }

    @Test
    fun `empty position uses accumulated terrain cost before geometric order`() {
        val occupiedSeed = Actor("occupied", 0)
        val planner = planner(
            width = 3,
            height = 3,
            occupants = mapOf((1 to 1) to occupiedSeed),
            costs = mapOf((1 to 2) to 5, (2 to 1) to 1),
        )

        val result = planner.findEmptyPosition(
            actor = actor,
            seed = 1 to 1,
            reachable = setOf(1 to 1, 1 to 2, 2 to 1),
        )

        assertEquals(2 to 1, result)
    }

    @Test
    fun `enemy avoidance routes around an occupied tile without changing stable ties`() {
        val enemy = Actor("enemy", 1)
        val planner = planner(width = 3, height = 2, occupants = mapOf((1 to 0) to enemy))

        val result = planner.findPath(
            actor = actor,
            start = 0 to 0,
            target = 2 to 0,
            rules = BattleMovementPlanner.PathRules(avoidEnemies = true),
        )

        assertEquals(listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1, 2 to 0), result)
    }

    @Test
    fun `enemy-near tile is reachable but stops expansion unless the rule permits leaving`() {
        val enemy = Actor("enemy", 1)
        val planner = planner(width = 1, height = 3, occupants = mapOf((1 to 1) to enemy))

        val stopped = planner.movePoints(
            actor = actor,
            movement = 3,
            rules = BattleMovementPlanner.MovementRules(),
            startOverride = 0 to 0,
        )
        val permitted = planner.movePoints(
            actor = actor,
            movement = 3,
            rules = BattleMovementPlanner.MovementRules(ignoresEnemyNear = true),
            startOverride = 0 to 0,
        )

        assertTrue(0 to 1 in stopped.points)
        assertFalse(0 to 2 in stopped.points)
        assertTrue(0 to 2 in permitted.points)
    }

    @Test
    fun `scripted destination probes occupied target in authored FIFO order`() {
        val occupiedSeed = Actor("occupied", 0)
        val planner = planner(width = 3, height = 3, occupants = mapOf((1 to 1) to occupiedSeed))

        assertEquals(1 to 2, planner.findScriptedDestination(actor, 1 to 1))
    }

    private fun planner(
        width: Int,
        height: Int,
        occupants: Map<Pair<Int, Int>, Actor> = emptyMap(),
        costs: Map<Pair<Int, Int>, Int> = emptyMap(),
        blocked: Set<Pair<Int, Int>> = emptySet(),
    ) = BattleMovementPlanner(
        isInside = { (x, y) -> x in 0 until width && y in 0 until height },
        terrainCost = { _, point -> costs[point] ?: 1 },
        isBlocked = { it in blocked },
        occupantAt = occupants::get,
        actorId = Actor::id,
        isSameActor = { left, right -> left === right },
        areAllied = { left, right -> left.side == right.side },
        orderedMovementOffsets = offsets,
        enemyNearOffsets = offsets,
    )
}
