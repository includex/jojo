package com.jojo.game.verification

import com.jojo.game.application.runtime.GameEntryPoint
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VerificationDesktopLaunchOptionsTest {
    @Test
    fun `capture and scripted scenario flags remain verification-owned`() {
        val options = VerificationDesktopLaunchOptions.parse(
            arrayOf(
                "--scenario=R_03", "--verify-scene=scene2", "--verify-globals=7:8",
                "--verify-random=10,90", "--capture-state=hall-menu-fixture",
                "--render-event-log=build/evidence.jsonl",
            ),
        )

        assertEquals("R_03", options.scenario)
        assertEquals("scene2", options.scenarioRun.startScene)
        assertEquals(mapOf(7 to 8), options.scenarioRun.globals)
        assertEquals(listOf(10, 90), options.scenarioRun.randomSequence)
        assertEquals("hall-menu-fixture", options.capture.state)
        assertEquals(GameEntryPoint.SCENARIO, options.toGameConfiguration().entryPoint)
        assertTrue(options.toGameConfiguration().automatedRun)
    }
}
