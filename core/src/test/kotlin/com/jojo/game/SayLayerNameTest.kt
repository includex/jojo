package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class SayLayerNameTest {
    @Test
    fun `SayLayer removes from the first numeric instance marker`() {
        assertEquals("황건군 ", GameDataCatalog.sayLayerUnitName("황건군 1"))
        assertEquals("궁병", GameDataCatalog.sayLayerUnitName("궁병12"))
        assertEquals("조조", GameDataCatalog.sayLayerUnitName("조조"))
    }
}
