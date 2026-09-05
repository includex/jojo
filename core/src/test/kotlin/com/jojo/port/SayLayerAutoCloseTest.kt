package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SayLayerAutoCloseTest {
    @Test fun `source timer advances one second after text completes`() {
        val timer = SayLayerAutoClose()
        assertFalse(timer.update(textComplete = false, enabled = true, delta = 10f))
        assertFalse(timer.update(textComplete = true, enabled = true, delta = .75f))
        assertFalse(timer.update(textComplete = true, enabled = true, delta = .99f))
        assertTrue(timer.update(textComplete = true, enabled = true, delta = .01f))
    }

    @Test fun `new page manual advance and disabled setting cancel callback`() {
        val timer = SayLayerAutoClose()
        assertFalse(timer.update(true, true, 0f))
        assertFalse(timer.update(false, true, 2f))
        assertFalse(timer.update(true, true, 0f))
        timer.reset()
        assertFalse(timer.update(true, true, 2f))
        assertFalse(timer.update(true, false, 2f))
        assertFalse(timer.update(true, true, 0f))
    }
}
