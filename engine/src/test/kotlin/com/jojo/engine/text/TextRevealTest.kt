package com.jojo.engine.text

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TextRevealTest {
    @Test
    fun `retained source advances through tags and restarts only when text changes`() {
        val reveal = TextReveal()
        reveal.update("<color=red>가</c>나", .04f)
        assertEquals("", reveal.visibleText)
        reveal.update("<color=red>가</c>나", .04f)
        assertEquals("가", reveal.visibleText)
        assertTrue(reveal.revealAllIfPending())
        assertEquals("가나", reveal.visibleText)
        reveal.update("다라", .04f)
        assertEquals("다", reveal.visibleText)
    }

    @Test
    fun `callback timer primes once and drops excess frame time`() {
        val reveal = TextReveal(timing = RevealTiming.COCOS_CALLBACK_TIMER)
        reveal.setSource("가나다")
        reveal.update(10f)
        assertEquals("", reveal.visibleText)
        reveal.update(10f)
        assertEquals("가", reveal.visibleText)
        reveal.update(.01f)
        assertEquals("가", reveal.visibleText)
        reveal.update(.04f)
        assertEquals("가나", reveal.visibleText)
    }

    @Test
    fun `retained-source time corrections preserve accumulated timing`() {
        val reveal = TextReveal()
        reveal.update("가나", .03f)
        reveal.update("가나", -.02f)
        reveal.update("가나", .02f)
        assertEquals("", reveal.visibleText)
        reveal.update("가나", .02f)
        assertEquals("가", reveal.visibleText)
    }
}
