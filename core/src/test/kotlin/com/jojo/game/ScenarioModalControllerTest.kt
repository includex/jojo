package com.jojo.game

import com.jojo.game.application.scenario.ScenarioModalController
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.application.scenario.ScenarioStage
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioModalControllerTest {
    @Test
    fun infoTypingPrimesThenRevealsOneUnitAndDropsExcessElapsedTime() {
        val controller = controller()
        controller.suspendForInfo("<color=#fff>AB</c>", ScenarioModalKind.EVENT)

        controller.update(1f, autoCloseUi = false)
        assertEquals("", controller.currentModalVisibleText, "first update only primes the source timer")

        controller.update(.2f, autoCloseUi = false)
        assertEquals("", controller.currentModalVisibleText, "one rich-text tag is one scheduler unit")
        controller.update(.2f, autoCloseUi = false)
        assertEquals("A", controller.currentModalVisibleText, "large deltas still reveal only one unit")
        controller.update(.039f, autoCloseUi = false)
        assertEquals("A", controller.currentModalVisibleText, "the previous excess delta was discarded")
        controller.update(.002f, autoCloseUi = false)
        assertEquals("AB", controller.currentModalVisibleText)
        assertEquals(false, controller.currentModalTextComplete, "the closing tag is a final scheduler unit")
        controller.update(.041f, autoCloseUi = false)
        assertEquals(true, controller.currentModalTextComplete)
    }

    @Test
    fun shortEventAutoCloseStartsAfterTheCompletionUpdate() {
        var state = PlaybackState.MODAL
        var resumed = 0
        val controller = ScenarioModalController(ScenarioStage(), { state = it }, { resumed++ })
        controller.suspendForInfo("A", ScenarioModalKind.EVENT, postTypingDelaySeconds = 1f)

        controller.update(5f, autoCloseUi = false)
        controller.update(5f, autoCloseUi = false)
        assertEquals("A", controller.currentModalVisibleText)
        assertEquals(PlaybackState.MODAL, state, "completion-frame delta must not also consume close delay")

        controller.update(.999f, autoCloseUi = false)
        assertEquals(PlaybackState.MODAL, state)
        controller.update(.002f, autoCloseUi = false)
        assertEquals(PlaybackState.COMPLETE, state)
        assertEquals(1, resumed)
    }

    @Test
    fun longInfoDoesNotAutoCloseWhenTheSettingIsDisabled() {
        var state = PlaybackState.MODAL
        val controller = ScenarioModalController(ScenarioStage(), { state = it }, {})
        controller.suspendForInfo("1234567890", ScenarioModalKind.INFO)
        controller.completeModalTyping()

        controller.update(10f, autoCloseUi = false)

        assertEquals(PlaybackState.MODAL, state)
        assertEquals("1234567890", controller.currentModalVisibleText)
        assertEquals(true, controller.currentModalTextComplete)
    }

    @Test
    fun inputScheduledClosePrimesOnTheNextUpdateBeforeCountingDelay() {
        var state = PlaybackState.MODAL
        val controller = ScenarioModalController(ScenarioStage(), { state = it }, {})
        controller.suspendForInfo("AB", ScenarioModalKind.EVENT)
        controller.completeModalTyping()
        controller.update(10f, autoCloseUi = false)
        assertEquals(PlaybackState.MODAL, state, "the first update primes an input-scheduled timer")
        controller.update(.5f, autoCloseUi = false)
        assertEquals(PlaybackState.MODAL, state)
        controller.update(.5f, autoCloseUi = false)
        assertEquals(PlaybackState.COMPLETE, state)
    }

    @Test
    fun fixtureModalKeepsItsExplicitTotalRemainingTime() {
        var state = PlaybackState.MODAL
        val controller = ScenarioModalController(ScenarioStage(), { state = it }, {})
        controller.setModalPresentation("AB", ScenarioModalKind.INFO, remainingSeconds = .5f)

        controller.update(.3f, autoCloseUi = true)
        assertEquals(PlaybackState.MODAL, state)
        controller.update(.21f, autoCloseUi = true)
        assertEquals(PlaybackState.COMPLETE, state)
    }

    private fun controller() = ScenarioModalController(ScenarioStage(), {}, {})
}
