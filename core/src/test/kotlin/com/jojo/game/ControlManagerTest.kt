// Test
package com.jojo.game

import com.jojo.game.domain.battle.command.Control
import com.jojo.game.domain.battle.command.ControlManager
import kotlin.test.Test
import kotlin.test.assertEquals

/** ControlManagerTest: ControlManager의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class ControlManagerTest {
    private class State(var controlled: Boolean = false, var aiValue: Int = 4, var target: Int = 7, var exists: Boolean = true) : ControlManager.UnitState {
        override fun isControlled() = controlled
        override fun ai() = aiValue
        override fun targetIndex() = target
        override fun targetX() = 12
        override fun targetY() = 14
        override fun targetExists(index: Int) = exists && index == target
    }

    @Test
    fun `controlled unit uses active controller and stale target becomes minus one`() {
        val seen = mutableListOf<List<Int>>()
        val state = State(controlled = true, exists = false)
        val manager = ControlManager(state, object : ControlManager.Factory {
            override fun create(ai: Int) = object : ControlManager.Driver {
                override fun setManager(manager: ControlManager) = Unit
                override fun setWithData(targetIndex: Int, x: Int, y: Int) { seen += listOf(ai, targetIndex, x, y) }
                override fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>) = 0
            }
        })
        assertEquals(0, manager.selectMovePoint(listOf(Control.Point(1, 2)), setOf(Control.Point(1, 2))))
        assertEquals(listOf(listOf(1, -1, 12, 14)), seen)
    }

    @Test
    fun `only source result one retries and never exceeds five selections`() {
        var calls = 0
        val manager = ControlManager(State(), object : ControlManager.Factory {
            override fun create(ai: Int) = object : ControlManager.Driver {
                override fun setManager(manager: ControlManager) = Unit
                override fun setWithData(targetIndex: Int, x: Int, y: Int) = Unit
                override fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int = if (++calls < 3) 1 else 2
            }
        })
        assertEquals(2, manager.selectMovePoint(emptyList(), emptySet()))
        assertEquals(3, calls)
    }

    @Test
    fun `setControl replaces the live driver used by the next source retry`() {
        val seen = mutableListOf<Int>()
        lateinit var manager: ControlManager
        manager = ControlManager(State(aiValue = 0), object : ControlManager.Factory {
            override fun create(ai: Int) = object : ControlManager.Driver {
                override fun setManager(manager: ControlManager) = Unit
                override fun setWithData(targetIndex: Int, x: Int, y: Int) = Unit
                override fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int {
                    seen += ai
                    if (ai == 0) manager.setControl(9, 3, 4, 5)
                    return if (ai == 0) 1 else 0
                }
            }
        })

        assertEquals(0, manager.selectMovePoint(emptyList(), emptySet()))
        assertEquals(listOf(0, 9), seen)
        assertEquals(9, manager.activeAi)
    }
}
