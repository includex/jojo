package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Choose2LayerTest {
    @Test fun `literal and actual newlines split into one based rows including blanks`() {
        val layer = ChoiceLayer(plainNewline = true)
        layer.onCreate("첫째\\n둘째\n\n넷째", -1) {}

        assertEquals(
            listOf(
                ChoiceLayer.Row(1, "첫째"),
                ChoiceLayer.Row(2, "둘째"),
                ChoiceLayer.Row(3, ""),
                ChoiceLayer.Row(4, "넷째"),
            ),
            layer.rows(),
        )
        assertEquals(null, layer.requestedFace)
        assertEquals(100, layer.zIndex)
    }

    @Test fun `touch end closes before returning one based row selection`() {
        val layer = ChoiceLayer(plainNewline = true)
        val observations = mutableListOf<Pair<Boolean, Int>>()
        layer.onCreate("진격\n대기\n퇴각", -1) { observations += layer.attached() to it }

        layer.onRowTouch(2, 1)
        assertTrue(layer.attached())
        assertTrue(observations.isEmpty())

        layer.onRowTouch(2, 2)
        assertFalse(layer.attached())
        assertEquals(listOf(false to 2), observations)
    }
}
