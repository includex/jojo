package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.RuntimeScenarioCommand
import com.jojo.game.application.runtime.RuntimeScenarioFrame
import com.jojo.game.application.runtime.RuntimeScenarioPresentation
import com.jojo.game.application.runtime.RuntimeScenarioScene
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.verification.VerificationScenarioDriver
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OpeningPanelCapturePolicyTest {
    @Test fun `opening capture waits for authored dialogue and does not install a fixture`() {
        val driver = VerificationScenarioDriver("opening-panel")
        val delay = RuntimeScenarioFrame("R_00", 1f, PlaybackState.DELAY, false)
        assertTrue(driver.commands(delay).isEmpty())
        val dialogue = delay.copy(playback = PlaybackState.DIALOGUE)
        assertEquals(listOf(RuntimeScenarioCommand.Present(RuntimeScenarioPresentation.STREET, 0, RuntimeScenarioScene())), driver.commands(dialogue))
        assertTrue(driver.commands(dialogue).isEmpty())
    }
}
