package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MiniMapLayerTest {
    @Test
    fun `authored button toggles persistent layer without selecting the tactical map`() {
        var loaded = 0
        val layer = MiniMapLayer(setting = 0) { loaded++ }
        layer.onCreate(weather = 0, initialPoolNodes = 0)
        layer.load(120, 120)
        assertFalse(layer.shown)
        assertEquals(522, layer.bgX)
        assertEquals(1, loaded)

        layer.touch(1)
        assertFalse(layer.shown)
        layer.touch(2)
        assertTrue(layer.shown)
        assertTrue(layer.sliding)
        assertEquals(522, layer.bgX)
        layer.advance(.59f)
        assertEquals(522, layer.bgX)
        // Avoid encoding an exact binary floating-point sum in the test;
        // Cocos frame deltas also normally cross the boundary.
        layer.advance(.02f)
        assertEquals(278, layer.bgX)
        assertFalse(layer.sliding)
        layer.touch(2)
        assertFalse(layer.shown)
    }

    @Test
    fun `yingchuan markers preserve source insertion order and coordinate transforms`() {
        val layer = MiniMapLayer(setting = 16)
        layer.onCreate(weather = 0, initialPoolNodes = 0)
        layer.load(120, 120)
        layer.visible(1, "mine", "normal", "normal", false, 20, 17)
        layer.visible(2, "enemy", "normal", "normal", false, 19, 11)
        assertEquals(listOf(1, 2), layer.map.keys.toList())
        assertEquals(MiniMapLayer.Marker(60, -42, "sf0"), layer.map[1])
        assertEquals(MiniMapLayer.Marker(54, -6, "sf8"), layer.map[2])
    }

    @Test
    fun `stable shown and hidden render contracts include only actual visible submissions`() {
        val shown = MiniMapRenderEvents.jsonl(shown = true).lineSequence().filter(String::isNotBlank).toList()
        val hidden = MiniMapRenderEvents.jsonl(shown = false).lineSequence().filter(String::isNotBlank).toList()
        assertEquals(25, shown.size)
        assertEquals(2, hidden.size)
        assertTrue(shown[1].contains("\"opacity\":0.659"))
        assertTrue(shown[21].contains("\"opacity\":0.498"))
        assertTrue(hidden.all { "Canvas/Layer/bg/btn/Background" in it })
    }
}
