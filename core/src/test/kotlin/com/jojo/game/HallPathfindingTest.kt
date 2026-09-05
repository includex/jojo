package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class HallPathfindingTest {
    @Test
    fun `source direction order and turn penalty keep a straight route`() {
        val path = assertNotNull(HallPathfinder.find(10, 10, 13, 10, null, emptySet()))
        assertEquals(listOf(10 to 10, 11 to 10, 12 to 10, 13 to 10), path)
        assertEquals(1, HallPathfinder.direction(10, 10, 11, 10))
        assertEquals(2, HallPathfinder.direction(10, 10, 10, 11))
        assertEquals(0, HallPathfinder.direction(10, 10, 10, 9))
    }

    @Test
    fun `source obstacle weight routes around a blocked hall tile`() {
        val rows = List(100) { IntArray(100) }
        rows[10][11] = 1
        val path = assertNotNull(HallPathfinder.find(10, 10, 12, 10, HallPathGrid(rows), emptySet()))
        assertEquals(0, path.drop(1).count { it == 11 to 10 })
        assertEquals(12 to 10, path.last())
    }

    @Test
    fun `source occupied penalty avoids another hall unit`() {
        val path = assertNotNull(HallPathfinder.find(10, 10, 12, 10, null, setOf(11 to 10)))
        assertEquals(0, path.drop(1).count { it == 11 to 10 })
        assertEquals(12 to 10, path.last())
    }
}
