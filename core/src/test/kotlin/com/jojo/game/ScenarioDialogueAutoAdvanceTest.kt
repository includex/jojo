package com.jojo.game

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.scenario.ScenarioPlaybackController
import kotlin.test.Test
import kotlin.test.assertEquals

/** R_00's first dialogue through the production presentation and advance bridge. */
class ScenarioDialogueAutoAdvanceTest {
    private class Harness {
        val playback = ScenarioInterpreter.load("R_00").also { it.start("scene1") }
        val controller = ScenarioPlaybackController(playback, {}, {})
        var advances = 0

        init {
            // Earlier movement and EVENT timing have separate source fixtures.
            repeat(30) {
                when (playback.state) {
                    PlaybackState.DIALOGUE -> return@repeat
                    PlaybackState.MODAL -> playback.resumeModal()
                    PlaybackState.DELAY -> playback.skipDelay()
                    else -> error("Unexpected opening state ${playback.state}")
                }
            }
            assertEquals("181", playback.currentDialogue?.speakerId)
        }

        fun update(delta: Float, enabled: Boolean = true) {
            controller.updatePresentation(delta, enabled, false) {
                advances++
                confirm()
            }
        }

        fun confirm() = controller.advance({}, { false }, { false }, {})
    }

    @Test
    fun `natural completion waits source sixteen tenths before advancing to Cao Cao`() {
        val h = Harness()
        h.update(10f) // Creation update primes the glyph timer.
        repeat(13) { h.update(.05f) }
        assertEquals(true, h.controller.viewState.dialogueTextComplete)
        repeat(15) {
            h.update(.1f)
            assertEquals("181", h.playback.currentDialogue?.speakerId)
            assertEquals(0, h.advances)
        }
        h.update(.1f)
        assertEquals("0", h.playback.currentDialogue?.speakerId)
        assertEquals(1, h.advances)
        assertEquals("", h.controller.viewState.dialogueVisibleText)
        h.update(.05f)
        assertEquals("알", h.controller.viewState.dialogueVisibleText)
    }

    @Test
    fun `manual reveal primes on following update and second confirm cancels old timer`() {
        val h = Harness()
        h.update(0f)
        h.confirm()
        assertEquals(true, h.controller.viewState.dialogueTextComplete)
        h.update(10f) // The input-created auto timer primes here.
        assertEquals("181", h.playback.currentDialogue?.speakerId)
        h.confirm()
        assertEquals("0", h.playback.currentDialogue?.speakerId)
        repeat(20) { h.update(.1f, enabled = false) }
        assertEquals("0", h.playback.currentDialogue?.speakerId)
        assertEquals(0, h.advances)
    }
}
