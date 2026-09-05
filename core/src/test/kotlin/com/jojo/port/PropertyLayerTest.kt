package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class PropertyLayerTest {
    private val layer = PropertyLayer(
        listOf(
            PropertyLayer.Item(1, "검", 4, 2, level = 3, exp = 20, expLimit = 20, typeName = "검"),
            PropertyLayer.Item(2, "갑옷", 20, 3),
            PropertyLayer.Item(3, "콩", 26, 4),
            PropertyLayer.Item(4, "회복약", 42, 5),
        ), mapOf(3 to 2, 4 to 0),
    )
    @Test fun `four original toggles select categories and property list excludes zero inventory`() {
        assertEquals(listOf("검"), layer.select(PropertyLayer.Tab.WEAPON).map { it.item.name })
        assertEquals(listOf("갑옷"), layer.select(PropertyLayer.Tab.ARMOR).map { it.item.name })
        assertEquals(listOf("콩"), layer.select(PropertyLayer.Tab.PROPERTY).map { it.item.name })
        assertEquals(listOf("콩", "2"), layer.rows().first().labels)
    }
    @Test fun `equipment row preserves source label ordering and max exp`() {
        val row = layer.select(PropertyLayer.Tab.WEAPON).single()
        assertEquals(listOf("검", "창고", "검", "3", "MAX"), row.labels)
        assertEquals(2, row.item.icon)
    }
}
