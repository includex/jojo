package com.jojo.game

import com.jojo.game.application.scenario.HallMoveTimeline
import com.jojo.game.application.scenario.ScenarioStageUnitMovementAnimator
import com.jojo.game.application.scenario.ScenarioStageUnitRegistry
import com.jojo.game.domain.scenario.TacticalUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScenarioHallSourcePositionTest {
    @Test fun `teleport invalidates the prior Hall movement render position`() {
        val registry = ScenarioStageUnitRegistry()
        registry.setUnit(157, 54, 85, 0) {}
        registry.unit(157).apply {
            hallSourceWorldX = 123.0
            hallSourceWorldY = 456.0
        }

        registry.setUnit(157, 11, 12, 2) {}

        val unit = registry.unit(157)
        assertEquals(11f, unit.visualX)
        assertEquals(12f, unit.visualY)
        assertTrue(unit.hallSourceWorldX.isNaN())
        assertTrue(unit.hallSourceWorldY.isNaN())
    }

    @Test fun `zero duration Hall move snaps its source render position to the destination`() {
        val unit = TacticalUnit(157, 4, 5)
        val animator = ScenarioStageUnitMovementAnimator()

        animator.begin(unit, emptyList(), 7, 9, 3, 0f, battleTimeline = false) {}

        val expected = HallMoveTimeline.sourceWorldPosition(7.0, 9.0)
        assertEquals(7f, unit.visualX)
        assertEquals(9f, unit.visualY)
        assertEquals(expected.x, unit.hallSourceWorldX, 0.0)
        assertEquals(expected.y, unit.hallSourceWorldY, 0.0)
    }
}
