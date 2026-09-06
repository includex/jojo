// Scenario Trace Test
package com.jojo.game.presentation.scenario.trace

import com.jojo.game.application.runtime.RuntimeScenarioCommand
import com.jojo.game.application.runtime.RuntimeScenarioDriver
import com.jojo.game.application.runtime.RuntimeScenarioFrame
import com.jojo.game.application.runtime.RuntimeScenarioOverlay
import com.jojo.game.application.runtime.RuntimeScenarioPresentation
import com.jojo.game.application.runtime.RuntimeScenarioScene
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Scenario runtime trace coordinator의 검증 정책·command 연결·probe 조립을 고정한다. */
class ScenarioRuntimeTraceCoordinatorTest {
    @Test
    fun `random trace count takes precedence over next trace setting`() {
        val calls = mutableListOf<String>()

        ScenarioRandomTraceConfiguration(stopAfterNextTrace = true, stopAfterTraceCount = 3).configure(
            stopAfterTraceCount = { calls += "count:$it" },
            stopAfterNextTrace = { calls += "next" },
        )

        assertEquals(listOf("count:3"), calls)
    }

    @Test
    fun `runtime commands use state guards and keep Screen mutations behind port`() {
        val port = FakePort(playback = PlaybackState.DIALOGUE)
        val coordinator = ScenarioRuntimeTraceCoordinator(
            driver = RuntimeScenarioDriver {
                listOf(
                    RuntimeScenarioCommand.Present(RuntimeScenarioPresentation.STREET, detail = 2),
                    RuntimeScenarioCommand.ShowOverlay(RuntimeScenarioOverlay.CHOICE),
                    RuntimeScenarioCommand.AdvanceDialogue,
                    RuntimeScenarioCommand.ResumeModal,
                    RuntimeScenarioCommand.RevealDialogue,
                )
            },
            port = port,
        )

        coordinator.applyRuntimeCommands()

        assertEquals(
            listOf("present:STREET:2", "overlay:CHOICE", "advance-dialogue", "reveal-dialogue"),
            port.events,
        )
        assertFalse(coordinator.isVerificationRun())
        assertEquals("R_00", coordinator.runtimeProbe().module)
    }

    @Test
    fun `verification status is provided by screen port`() {
        assertTrue(ScenarioRuntimeTraceCoordinator(null, FakePort(keepsOpen = true)).isVerificationRun())
    }

    private class FakePort(
        private var playback: PlaybackState = PlaybackState.COMPLETE,
        private val keepsOpen: Boolean = false,
    ) : ScenarioRuntimeTraceCoordinator.Port {
        val events = mutableListOf<String>()

        override fun runtimeFrame() = RuntimeScenarioFrame("R_00", 1.5f, playback, choiceAvailable = false)
        override fun runtimeProbeInput() = ScenarioRuntimeTraceProbeInput(
            module = "R_00",
            elapsedSeconds = 1.5f,
            playback = playback,
            options = emptyList(),
            selectedChoice = 0,
            sceneIndex = 1,
            startedScenes = listOf(0, 1),
            backgroundId = 4,
            unitIds = setOf(1),
            campaignStage = 2,
            menuVisible = false,
            dialogueText = "대사",
            hallBattleScenePending = false,
            battleButtonScreenX = 100,
            battleButtonScreenY = 200,
            choiceTrace = emptyList(),
            randomTrace = emptyList(),
            randomDrawCount = 0,
            remainingInjectedRandomCount = 0,
        )

        override fun keepsScenarioOpen(): Boolean = keepsOpen
        override fun playbackState(): PlaybackState = playback
        override fun applyPresentation(mode: RuntimeScenarioPresentation, detail: Int, scene: RuntimeScenarioScene) {
            events += "present:$mode:$detail"
        }
        override fun showOverlay(overlay: RuntimeScenarioOverlay, scene: RuntimeScenarioScene) {
            events += "overlay:$overlay"
        }
        override fun advanceDialogue() { events += "advance-dialogue" }
        override fun resumeModal() { events += "resume-modal" }
        override fun skipDelay() { events += "skip-delay" }
        override fun confirmChoice() { events += "confirm-choice" }
        override fun resetDialogueReveal() { events += "reveal-dialogue" }
    }
}
