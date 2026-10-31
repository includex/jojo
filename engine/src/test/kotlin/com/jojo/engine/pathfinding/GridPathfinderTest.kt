package com.jojo.engine.pathfinding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GridPathfinderTest {
    private val movementCosts = PathCosts(step = 1, blocked = 767, occupied = 511, directionChange = 15)

    @Test
    fun `direction order and turn penalty keep a straight route`() {
        val path = assertNotNull(GridPathfinder.find(10, 10, 13, 10, PathGrid.open(100, 100), emptySet(), movementCosts))
        assertEquals(listOf(10 to 10, 11 to 10, 12 to 10, 13 to 10), path)
        assertEquals(1, GridPathfinder.direction(10, 10, 11, 10))
        assertEquals(2, GridPathfinder.direction(10, 10, 10, 11))
        assertEquals(0, GridPathfinder.direction(10, 10, 10, 9))
    }

    @Test
    fun `terrain penalty preserves the upper detour on an equal-cost tie`() {
        val rows = List(100) { IntArray(100) }
        rows[10][11] = 1
        val path = assertNotNull(GridPathfinder.find(10, 10, 12, 10, PathGrid(rows), emptySet(), movementCosts))
        assertEquals(listOf(10 to 10, 10 to 9, 11 to 9, 12 to 9, 12 to 10), path)
    }

    @Test
    fun `occupied penalty avoids another entity`() {
        val path = assertNotNull(GridPathfinder.find(10, 10, 12, 10, PathGrid.open(100, 100), setOf(11 to 10), movementCosts))
        assertEquals(listOf(10 to 10, 10 to 9, 11 to 9, 12 to 9, 12 to 10), path)
    }

    @Test
    fun `terrain snapshot does not change when caller arrays mutate`() {
        val rows = List(3) { IntArray(4) }
        rows[1][2] = 1
        val grid = PathGrid(rows)
        rows[1][2] = 0
        assertEquals(true, grid.blocked(2, 1))
        assertEquals(4, grid.width)
        assertEquals(3, grid.height)
    }

    @Test
    fun `grid dimensions rather than game map constants define valid targets`() {
        val grid = PathGrid.open(2, 258)
        val path = assertNotNull(GridPathfinder.find(0, 256, 1, 1, grid, emptySet(), PathCosts(1, 0, 0, 0)))
        assertEquals(0 to 256, path.first())
        assertEquals(1 to 1, path.last())
        assertEquals(257, path.size)
        assertEquals(true, path.zipWithNext().all { (a, b) -> kotlin.math.abs(a.first - b.first) + kotlin.math.abs(a.second - b.second) == 1 })
        assertNull(GridPathfinder.find(0, 0, 2, 0, grid, emptySet(), movementCosts))
    }

    @Test
    fun `terrain and occupancy costs belong to the caller policy`() {
        val rows = List(3) { IntArray(3) }
        rows[1][1] = 1
        val path = assertNotNull(GridPathfinder.find(0, 1, 2, 1, PathGrid(rows), emptySet(), PathCosts(1, 0, 0, 0)))
        assertEquals(listOf(0 to 1, 1 to 1, 2 to 1), path)
    }
}
