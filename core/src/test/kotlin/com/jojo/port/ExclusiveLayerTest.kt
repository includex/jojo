package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExclusiveLayerTest {
    @Test
    fun `equip information button routes to Global126 only on touch end`() {
        assertEquals(null, EquipExclusiveRoute.openFromInformationButton(1))
        val child = EquipExclusiveRoute.openFromInformationButton(ExclusiveLayer.TOUCH_END)
        assertEquals(ExclusiveLayer.Tab.SET_LIST, child?.selectedTab)
        assertTrue(child?.attached == true)
    }

    @Test
    fun `tabs are selected lazily by touch end and confirm closes`() {
        val layer = ExclusiveLayer()
        assertEquals(ExclusiveLayer.Tab.SET_LIST, layer.selectedTab)
        layer.onButton(1, 1)
        assertEquals(ExclusiveLayer.Tab.SET_LIST, layer.selectedTab)
        layer.onButton(1, ExclusiveLayer.TOUCH_END)
        assertEquals(ExclusiveLayer.Tab.EXCLUSIVE_LIST, layer.selectedTab)
        assertTrue(layer.attached)
        layer.onButton(2, ExclusiveLayer.TOUCH_END)
        assertFalse(layer.attached)
    }

    @Test
    fun `full canvas cancel closes on touch end`() {
        val layer = ExclusiveLayer(ExclusiveLayer.Tab.EXCLUSIVE_LIST)
        layer.onCancel(1)
        assertTrue(layer.attached)
        layer.onCancel(ExclusiveLayer.TOUCH_END)
        assertFalse(layer.attached)
    }
}
