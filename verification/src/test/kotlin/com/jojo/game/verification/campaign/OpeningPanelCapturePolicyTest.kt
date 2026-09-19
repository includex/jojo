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
        for ((index, stage) in listOf("panel", "portrait", "speaker", "text").withIndex()) {
            val driver = VerificationScenarioDriver("opening-$stage")
            val delay = RuntimeScenarioFrame("R_00", 1f, PlaybackState.DELAY, false)
            assertTrue(driver.commands(delay).isEmpty())
            val revealing = delay.copy(playback = PlaybackState.DIALOGUE)
            if (stage == "text") assertTrue(driver.commands(revealing).isEmpty())
            val dialogue = revealing.copy(dialogueTextComplete = true)
            assertEquals(listOf(RuntimeScenarioCommand.Present(RuntimeScenarioPresentation.STREET, index, RuntimeScenarioScene())), driver.commands(dialogue))
            assertTrue(driver.commands(dialogue).isEmpty())
        }
    }
}
