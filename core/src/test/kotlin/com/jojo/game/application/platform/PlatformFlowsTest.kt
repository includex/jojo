// Test
package com.jojo.game.application.platform

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** PlatformFlowsTest: 데스크톱 호출자가 없는 플랫폼 흐름의 계약과 회귀 동작을 검증한다. */
class PlatformFlowsTest {
    @Test
    fun `statement countdown preserves JS floor division for negative timers`() {
        val events = mutableListOf<String>()
        val commands = mutableListOf<String>()
        val layer = LegalStatementFlow({ events += it }, { commands += "END_GAME" })

        layer.onCreate(-1)

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
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
        val layer = LegalStatementFlow({ events += it }, { commands += "END_GAME" })
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
        val layer = LegalStatementFlow({ events += it }, { commands += "END_GAME" })

        layer.touch(1, 2)

        assertFalse(layer.attached)
        assertEquals(listOf("END_GAME"), commands)
        assertTrue(events.isEmpty())
        assertEquals(0, layer.statement)
    }
}
