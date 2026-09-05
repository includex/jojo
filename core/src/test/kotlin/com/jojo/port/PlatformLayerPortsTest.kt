package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Regression tests for the platform-layer contracts that do not have a native desktop caller. */
class PlatformLayerPortsTest {
    @Test
    fun `statement countdown preserves JS floor division for negative timers`() {
        val events = mutableListOf<String>()
        val commands = mutableListOf<String>()
        val layer = StatementLayerPort({ events += it }, { commands += "END_GAME" })

        layer.onCreate(-1)

        // Math.floor(-1 / 60000) == -1 and Math.floor(-1 / 30) == -1.
        assertEquals(9, layer.time)
        assertEquals(9, layer.countdownRepeat)
        assertEquals(10, layer.unlockDelay)
        assertEquals(1, layer.countdownInterval)
        assertEquals(0, layer.countdownDelay)
    }

    @Test
    fun `statement acceptance emits enter and persists only on touch end`() {
        val events = mutableListOf<String>()
        val commands = mutableListOf<String>()
        val layer = StatementLayerPort({ events += it }, { commands += "END_GAME" })
        layer.onCreate(0)

        layer.touch(0, 1)
        assertTrue(layer.attached)
        assertEquals(0, layer.statement)
        assertTrue(events.isEmpty())

        layer.touch(0, 2)
        assertFalse(layer.attached)
        assertEquals(1, layer.statement)
        assertEquals(listOf("ENTER_GAME"), events)
        assertTrue(commands.isEmpty())
    }

    @Test
    fun `statement decline sends end game and removes layer`() {
        val events = mutableListOf<String>()
        val commands = mutableListOf<String>()
        val layer = StatementLayerPort({ events += it }, { commands += "END_GAME" })

        layer.touch(1, 2)

        assertFalse(layer.attached)
        assertEquals(listOf("END_GAME"), commands)
        assertTrue(events.isEmpty())
        assertEquals(0, layer.statement)
    }
}
