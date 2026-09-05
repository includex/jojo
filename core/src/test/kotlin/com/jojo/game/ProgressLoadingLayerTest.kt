package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProgressLoadingLayerTest {
    @Test fun `loading flag one reveals at five seconds`() {
        val layer = LoadingLayer(1)
        assertFalse(layer.imageVisible)
        assertEquals(0f, layer.blockerOpacity)
        layer.advance(4.999f)
        assertFalse(layer.imageVisible)
        layer.advance(.001f)
        assertTrue(layer.imageVisible)
        assertEquals(.392f, layer.blockerOpacity)
    }

    @Test fun `loading default is visible and flag two remains hidden`() {
        assertTrue(LoadingLayer().imageVisible)
        val hidden = LoadingLayer(2)
        hidden.advance(10f)
        assertFalse(hidden.imageVisible)
        assertEquals(0f, hidden.blockerOpacity)
    }

    @Test fun `natural login registration route attaches loading until async callback then removes it`() {
        var cleared = 0
        var registered = 0
        var callback: ((Boolean) -> Unit)? = null
        val flow = LoginRegistrationCheckFlow(
            pending = true,
            clearPending = { cleared++ },
            requestCheck = { callback = it },
            onRegistered = { registered++ },
        )
        flow.start()
        assertEquals(LoginRegistrationCheckFlow.State.CHECKING, flow.state)
        assertTrue(flow.loading?.imageVisible == true)
        assertEquals(1, cleared)
        callback?.invoke(true)
        assertEquals(LoginRegistrationCheckFlow.State.COMPLETE, flow.state)
        assertEquals(null, flow.loading)
        assertTrue(flow.registered)
        assertEquals(1, registered)
    }

    @Test fun `login without one-shot registration marker never adds loading`() {
        var requested = 0
        val flow = LoginRegistrationCheckFlow(false, {}, { requested++ })
        flow.start()
        assertEquals(LoginRegistrationCheckFlow.State.COMPLETE, flow.state)
        assertEquals(null, flow.loading)
        assertEquals(0, requested)
    }
}
