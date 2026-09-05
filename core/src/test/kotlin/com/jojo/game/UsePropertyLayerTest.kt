package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UsePropertyLayerTest {
    private val peach = UsePropertyLayer.Property(150, "회복용 복숭아", "HP 회복", 3, 47)
    private val tonic = UsePropertyLayer.Property(151, "정신력 회복약", "MP 회복", 2, 48)

    @Test
    fun `short press selects source-order item and removes list`() {
        val selected = mutableListOf<UsePropertyLayer.Property?>()
        val inspected = mutableListOf<UsePropertyLayer.Property>()
        val layer = UsePropertyLayer(listOf(peach, tonic), selected::add, inspected::add)

        layer.touchStart(1)
        layer.update(.999f)
        layer.touchEnd(1)

        assertEquals(1, selected.size)
        assertEquals(tonic, selected.single())
        assertTrue(inspected.isEmpty())
        assertFalse(layer.attached)
    }

    @Test
    fun `one second press opens detail and release no longer selects`() {
        val selected = mutableListOf<UsePropertyLayer.Property?>()
        val inspected = mutableListOf<UsePropertyLayer.Property>()
        val layer = UsePropertyLayer(listOf(peach), selected::add, inspected::add)

        layer.touchStart(0)
        layer.update(1f)
        layer.touchEnd(0)

        assertEquals(listOf(peach), inspected)
        assertTrue(selected.isEmpty())
        assertTrue(layer.attached)
    }

    @Test
    fun `cancel only clears timer while close returns undefined equivalent`() {
        val selected = mutableListOf<UsePropertyLayer.Property?>()
        val inspected = mutableListOf<UsePropertyLayer.Property>()
        val layer = UsePropertyLayer(listOf(peach), selected::add, inspected::add)

        layer.touchStart(0)
        layer.update(.75f)
        layer.touchCancel()
        layer.update(2f)
        assertTrue(inspected.isEmpty())
        assertTrue(layer.attached)

        layer.closeTouchEnd()
        layer.closeTouchEnd()
        assertEquals(1, selected.size)
        assertEquals(null, selected.single())
        assertFalse(layer.attached)
    }
}
