package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class RoundLayerTest {
    @Test
    fun `RoundLayer mirrors round labels, final turn and two-second completion`() {
        var removed = 0
        var completed = 0
        val layer = RoundLayer({ removed++ }, { completed++ })

        layer.onCreate(round = 3, max = 20)
        assertEquals(RoundLayer.View(true, false, "제3턴"), layer.view)
        layer.elapsed(1.99f)
        assertEquals(0, completed)
        layer.elapsed(2f)
        layer.elapsed(3f)
        assertEquals(1, removed)
        assertEquals(1, completed)

        val final = RoundLayer({}, {})
        final.onCreate(round = 21, max = 20)
        assertEquals(RoundLayer.View(true, false, "최종 턴"), final.view)

        val camp = RoundLayer({}, {})
        camp.onCreate(round = null, max = null)
        assertEquals(RoundLayer.View(false, true, ""), camp.view)
    }

    @Test
    fun `RoundLayer uses property presence and JavaScript missing-max comparison semantics`() {
        val layer = RoundLayer({}, {})

        // `round in t` is true even for zero, and `0 > undefined` is false.
        layer.onCreate(RoundLayer.CreateArgs(roundPresent = true, round = 0, max = null))
        assertEquals(RoundLayer.View(true, false, "제0턴"), layer.view)

        // An absent property selects the camp-label pair irrespective of max.
        layer.onCreate(RoundLayer.CreateArgs(roundPresent = false, max = 20))
        assertEquals(RoundLayer.View(false, true, ""), layer.view)
    }
}
