package com.jojo.game

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
}
