// Test
package com.jojo.game

import com.jojo.game.domain.battle.command.Control
import kotlin.test.Test
import kotlin.test.assertEquals

/** ControlTest: Control의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class ControlTest {
    private class Manager(
        var paralyzed: Boolean = false,
        var surrounded: Boolean = false,
        var aiResult: Control.Result? = null,
    ) : Control.Manager {
        val results = mutableListOf<Control.Result>()
        val controls = mutableListOf<List<Int>>()
        override fun currentPoint() = Control.Point(3, 4)
        override fun isParalyzed() = paralyzed
        override fun isSurrounded() = surrounded
        override fun setControl(ai: Int, targetIndex: Int, x: Int, y: Int) { controls += listOf(ai, targetIndex, x, y) }
        override fun setResult(result: Control.Result) { results += result }
        override fun selectByAi() = aiResult
    }

    @Test
    fun `Control keeps source target data and initializes result at current point`() {
        val manager = Manager(aiResult = Control.Result(4, 4, targetIndex = 7, kind = "attack", value = 90))
        val control = Control().apply { setManager(manager); setWithData(7, 12, 14) }

        assertEquals(0, control.selectMovePoint())
        assertEquals(7, control.targetUnitIndex())
        assertEquals(12, control.targetX)
        assertEquals(14, control.targetY)
        assertEquals(listOf(Control.Result(3, 4), Control.Result(4, 4, 7, "attack", 90)), manager.results)
    }

    @Test
    fun `Control process1 holds before AI for paralysis and surrounding`() {
        listOf(Manager(paralyzed = true), Manager(surrounded = true)).forEach { manager ->
            val control = Control().apply { setManager(manager) }
            assertEquals(1, control.selectMovePoint())
            assertEquals(listOf(Control.Result(3, 4)), manager.results)
            assertEquals(listOf(listOf(2, -1, -1, -1)), manager.controls)
        }
    }
}
