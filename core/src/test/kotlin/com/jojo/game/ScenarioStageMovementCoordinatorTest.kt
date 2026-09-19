// Test
package com.jojo.game

import com.jojo.game.domain.scenario.TacticalUnit
import com.jojo.game.domain.scenario.ScenarioCommand
import com.jojo.game.application.scenario.ScenarioStageHeadCoordinator
import com.jojo.game.application.scenario.ScenarioStage
import com.jojo.game.application.scenario.ScenarioStageMovementPlanner
import com.jojo.game.application.scenario.ScenarioStageUnitMovementAnimator
import com.jojo.game.application.scenario.HallMoveTimeline

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScenarioStageMovementCoordinatorTest {
    @Test
    fun `same destination Hall move primes then completes its source epsilon action`() {
        val stage = ScenarioStage()
        stage.apply(ScenarioCommand.ShowUnit(9, 4, 7, 2))

        assertEquals(HallMoveTimeline.SOURCE_ACTION_EPSILON_SECONDS.toFloat(), stage.moveDuration(9, 4, 7))
        stage.apply(ScenarioCommand.MoveUnit(9, 4, 7, 1))
        val unit = stage.unit(9)
        assertTrue(unit.moveDuration > 0f)

        stage.updateAnimations(1f)
        assertTrue(unit.moveDuration > 0f, "the first update only primes the source action")
        assertEquals(4 to 7, unit.x to unit.y)

        stage.updateAnimations(1f / 60f)
        assertEquals(0f, unit.moveDuration)
        assertEquals(1, unit.direction)
    }

    @Test
    fun `Hall move group includes same destination action until it actually completes`() {
        val stage = ScenarioStage()
        stage.apply(ScenarioCommand.ShowUnit(1, 4, 7, 2))
        stage.apply(ScenarioCommand.ShowUnit(2, 8, 7, 2))

        stage.moveUnits(listOf(
            ScenarioCommand.MoveUnit(1, 4, 7, 1),
            ScenarioCommand.MoveUnit(2, 9, 7, 1),
        ))
        stage.updateAnimations(0f)
        stage.updateAnimations(HallMoveTimeline.SOURCE_ACTION_EPSILON_SECONDS.toFloat())

        assertTrue(stage.unit(1).moveDuration > 0f, "Float epsilon rounds just below the source Double duration")
        stage.updateAnimations(1e-12f)
        assertEquals(0f, stage.unit(1).moveDuration)
        assertTrue(stage.unit(2).moveDuration > 0f)
    }

    @Test
    fun `planner keeps authored resolver path and battle callback duration`() {
        val planner = ScenarioStageMovementPlanner().apply {
            battleMovementTimeline = true
            battleMovePathResolver = { id, x, y ->
                assertEquals(4, id)
                assertEquals(9 to 3, x to y)
                listOf(6 to 1, 7 to 1, 7 to 2, 8 to 2)
            }
        }
        val units = mapOf(4 to TacticalUnit(4, 6, 1))

        val path = requireNotNull(planner.pathFor(4, 9, 3, units))

        assertEquals(listOf(6 to 1, 7 to 1, 7 to 2, 8 to 2), path)
        assertEquals(0.34f, planner.duration(path), 0.001f)
    }

    @Test
    fun `unit animator preserves logical tile until timeline completion`() {
        val animator = ScenarioStageUnitMovementAnimator()
        val unit = TacticalUnit(2, 1, 1, direction = 3)
        val directions = mutableListOf<Pair<Int, Int>>()
        val path = listOf(1 to 1, 2 to 1, 2 to 2)

        animator.begin(unit, path, 9, 9, direction = 2, duration = 0.08f, battleTimeline = false) { directions += it }
        animator.update(0.04f, mapOf(unit.id to unit), battleTimeline = false)

        assertEquals(0f, unit.moveElapsed, "the first Hall action tick only primes elapsed time")
        assertEquals(1 to 1, unit.x to unit.y)
        assertTrue(unit.visualX >= 1f)
        assertEquals(20, unit.action)
        assertEquals(listOf(2 to 2), directions)

        animator.update(0.04f, mapOf(unit.id to unit), battleTimeline = false)
        assertEquals(1 to 1, unit.x to unit.y)
        animator.update(0.04f, mapOf(unit.id to unit), battleTimeline = false)
        assertEquals(1 to 1, unit.x to unit.y, "Float frame deltas remain below the source Double boundary")
        animator.update(0.000001f, mapOf(unit.id to unit), battleTimeline = false)
        assertEquals(2 to 2, unit.x to unit.y)
        assertEquals(2, unit.direction)
        assertEquals(0, unit.action)
        assertEquals(0f, unit.moveDuration)
    }

    @Test
    fun `battle animator keeps consuming its first callback tick`() {
        val animator = ScenarioStageUnitMovementAnimator()
        val unit = TacticalUnit(2, 1, 1)
        animator.begin(
            unit, listOf(1 to 1, 2 to 1), 2, 1,
            direction = 1, duration = 0.18f, battleTimeline = true,
        ) {}

        animator.update(0.08f, mapOf(unit.id to unit), battleTimeline = true)

        assertEquals(0.08f, unit.moveElapsed, 0.0001f)
    }

    @Test
    fun `head coordinator advances fade and finish commits visual state`() {
        val heads = ScenarioStageHeadCoordinator()

        assertEquals(1f, heads.show(8, 4, 5))
        heads.update(0.5f)
        assertEquals(0.5f, heads.head(8).opacity, 0.001f)
        assertEquals(0.05f, heads.move(8, 7, 9), 0.001f)
        heads.finish()

        val head = heads.head(8)
        assertEquals(7f to 9f, head.visualX to head.visualY)
        assertEquals(0f, head.moveDuration)
        assertFalse(head.fadeDuration > 0f)
    }
}
