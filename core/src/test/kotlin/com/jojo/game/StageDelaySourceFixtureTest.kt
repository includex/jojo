package com.jojo.game

import com.badlogic.gdx.utils.JsonReader
import com.jojo.game.application.scenario.ScenarioDelayCoordinator
import com.jojo.game.application.scenario.ScenarioDialogueCoordinator
import com.jojo.game.application.scenario.ScenarioModalController
import com.jojo.game.application.scenario.ScenarioStage
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Actual Cocos CallbackTimer output replayed through the production delay coordinator. */
class StageDelaySourceFixtureTest {
    private fun fixture() = JsonReader().parse(requireNotNull(
        javaClass.classLoader.getResourceAsStream("parity/stage-delay-source.json"),
    ))

    @Test
    fun `stage delay resume steps match controlled source timer modes`() {
        val fixture = fixture()
        assertEquals("controlled-source-stage-delay-steps-v1", fixture.getString("contract"))
        assertTrue(fixture.getBoolean("controlledEvidence"))
        assertEquals(false, fixture.getBoolean("naturalRun"))
        assertEquals(0.1f.toDouble(), fixture.getDouble("floatPointOne"), 0.0)

        for (case in fixture.get("cases")) {
            val mode = case.getString("mode")
            val deltas = case.get("deltas").map { value ->
                val sourceDelta = value.asDouble()
                assertEquals(sourceDelta, sourceDelta.toFloat().toDouble(), "$mode delta must be a Float roundtrip")
                sourceDelta.toFloat()
            }
            val expectedResumeSteps = case.get("events")
                .filter { it.getString("kind") == "resume" }
                .map { it.getInt("stepIndex") }

            val stage = ScenarioStage()
            var state = PlaybackState.COMPLETE
            var remaining = 0f
            var stepIndex = -1
            var callbacks = 0
            val actualResumeSteps = mutableListOf<Int>()
            val dialogue = ScenarioDialogueCoordinator(stage, { state = it }, {}, { remaining = it })
            val modal = ScenarioModalController(stage, { state = it }, {})
            lateinit var delay: ScenarioDelayCoordinator
            delay = ScenarioDelayCoordinator(
                stage, dialogue, modal,
                getState = { state }, onSetState = { state = it },
                onResumeExecution = {
                    callbacks++
                    if (mode == "inside-update" && callbacks == 1) {
                        delay.suspendForStageDelay(3)
                    } else {
                        actualResumeSteps += stepIndex
                        if (mode == "chain" && callbacks == 1) delay.suspendForStageDelay(3)
                        else state = PlaybackState.COMPLETE
                    }
                },
                getDelayRemainingSeconds = { remaining }, onSetDelayRemainingSeconds = { remaining = it },
            )

            if (mode == "inside-update") delay.suspendFor(0f) else delay.suspendForStageDelay(3)
            for ((index, delta) in deltas.withIndex()) {
                stepIndex = index
                delay.update(delta)
                val sourceTimerActive = case.get("steps").get(index).get("timers").size > 0
                assertEquals(sourceTimerActive, state == PlaybackState.DELAY, "$mode active state at step $index")
            }
            assertEquals(expectedResumeSteps, actualResumeSteps, mode)
        }
    }
}
