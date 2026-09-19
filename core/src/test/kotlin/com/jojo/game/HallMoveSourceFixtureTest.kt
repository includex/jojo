package com.jojo.game

import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.application.scenario.HallMoveTimeline
import com.jojo.game.application.scenario.ScenarioStageUnitMovementAnimator
import com.jojo.game.domain.scenario.TacticalUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Actual source engine output: identity turnPos isolates action timing from screen projection. */
class HallMoveSourceFixtureTest {
    private fun fixture() = JsonReader().parse(requireNotNull(
        javaClass.classLoader.getResourceAsStream("parity/hall-move-source.json"),
    ))

    @Test
    fun `timeline matches actual source sequence boundary positions and directions`() {
        val fixture = fixture()
        assertEquals("controlled-source-hall-move-steps-v1", fixture.getString("contract"))
        assertTrue(fixture.getBoolean("controlledEvidence"))
        assertEquals(false, fixture.getBoolean("naturalRun"))
        assertTrue(fixture.get("cases").size >= 22)
        for (case in fixture.get("cases")) {
            val label = case.getString("name") + ":" + case.getString("schedule")
            val path = case.get("path").map { it.getInt(0) to it.getInt(1) }
            assertEquals(case.getDouble("actionDuration"), HallMoveTimeline.sourceDuration(path), 0.0, label)
            var elapsed = 0.0
            for (step in case.get("steps")) {
                if (step.getInt("index") > 0) elapsed += step.getDouble("dt")
                val sample = HallMoveTimeline.sample(path, elapsed)
                assertEquals(step.get("position").getDouble(0).toFloat(), sample.x, "$label x at $elapsed")
                assertEquals(step.get("position").getDouble(1).toFloat(), sample.y, "$label y at $elapsed")
                assertEquals(step.getInt("callbackDirection"), sample.direction, "$label direction at $elapsed")
                assertEquals(step.getBoolean("isDone"), elapsed >= HallMoveTimeline.sourceDuration(path), "$label completion at $elapsed")
            }
        }
    }

    @Test
    fun `animator matches source callbacks for Float input schedules`() {
        var cases = 0
        for (case in fixture().get("cases")) {
            if (!case.getString("inputKind").startsWith("Math.fround")) continue
            cases++
            for (step in case.get("steps")) {
                val delta = step.getDouble("dt")
                assertEquals(delta, delta.toFloat().toDouble(), "Float fixture must preserve every delta")
            }
            val path = case.get("path").map { it.getInt(0) to it.getInt(1) }
            val unit = TacticalUnit(181, path.first().first, path.first().second)
            val animator = ScenarioStageUnitMovementAnimator()
            val direction = case.get("steps").get(0).getInt("callbackDirection")
            animator.begin(unit, path, path.last().first, path.last().second, direction,
                case.getDouble("actionDuration").toFloat(), false) {}
            for (step in case.get("steps")) {
                animator.update(step.getDouble("dt").toFloat(), mapOf(181 to unit), false)
                assertEquals(step.getBoolean("isDone"), unit.moveDuration == 0f, case.getString("schedule"))
                assertEquals(step.get("logical").getInt(0) to step.get("logical").getInt(1), unit.x to unit.y)
                assertEquals(step.get("position").getDouble(0).toFloat(), unit.visualX)
                assertEquals(step.get("position").getDouble(1).toFloat(), unit.visualY)
            }
        }
        assertTrue(cases >= 2)
    }
}
