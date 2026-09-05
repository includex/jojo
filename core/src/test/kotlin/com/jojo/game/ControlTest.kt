package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `ControlTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
