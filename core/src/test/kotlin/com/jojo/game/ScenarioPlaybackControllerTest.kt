// Test
package com.jojo.game

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.scenario.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioPlaybackControllerTest {
    @Test
    fun routeGateRunsOnlyTheFirstRouteCallback() {
        val gate = ScenarioRouteGate()
        var routes = 0

        gate.routeOnce { routes++ }
        gate.routeOnce { routes++ }

        assertEquals(1, routes)
        assertEquals(true, gate.isRouted)
    }

    @Test
    fun viewStateIsAnImmutableValueProjection() {
        val state = ScenarioViewState("대화", "정보", routedAfterCompletion = false)

        assertEquals("대화", state.dialogueVisibleText)
        assertEquals("정보", state.modalVisibleText)
        assertEquals(false, state.routedAfterCompletion)
    }

    @Test
    fun eventModalFirstClickRevealsTextAndSecondClickClosesIt() {
        val runtime = ScenarioInterpreter.load("R_00")
        runtime.start("scene1")
        val controller = ScenarioPlaybackController(runtime, {}, {})
        controller.updatePresentation(0f, autoCloseEnabled = false, revealDialogueImmediately = false) {}

        assertEquals(PlaybackState.MODAL, runtime.state)
        assertEquals(ScenarioModalKind.EVENT, runtime.currentModalKind)
        assertEquals("재능의 첫 징후", runtime.currentModalText)
        assertEquals("", controller.viewState.modalVisibleText)

        controller.advance({}, { false }, { false }, {})

        assertEquals(PlaybackState.MODAL, runtime.state)
        assertEquals("재능의 첫 징후", controller.viewState.modalVisibleText)
        assertEquals(true, controller.viewState.modalTextComplete)

        controller.advance({}, { false }, { false }, {})

        assertEquals(null, runtime.currentModalText)
        assertEquals(null, runtime.currentModalKind)
    }
}
