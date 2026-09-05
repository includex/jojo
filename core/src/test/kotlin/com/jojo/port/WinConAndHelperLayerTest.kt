package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WinConBoxLayerTest {
    @Test fun `WinConBox onCreate sets bg label scroll and only end closes before callback`() {
        var calls = 0
        val layer = WinConBoxLayer()
        val created = layer.onCreate(WinConBoxLayer.CreateData("승리 조건") { calls++ })
        assertEquals("bg0", created.prefab.backgroundNode)
        assertEquals("bg0/scrollview/view/content/richtext", created.prefab.richTextNode)
        assertEquals(989f, created.prefab.backgroundWidth)
        assertEquals(670f, created.prefab.backgroundHeight)
        assertEquals(2, created.prefab.listenerPriority)
        assertEquals("승리 조건", created.label)
        assertTrue(created.scrollAtTop)
        assertTrue(created.attached)
        assertTrue(layer.onButtonTouch(1).attached)
        assertEquals(0, calls)
        assertFalse(layer.onButtonTouch(WinConBoxLayer.TOUCH_END).attached)
        assertEquals(1, calls)
    }

}
