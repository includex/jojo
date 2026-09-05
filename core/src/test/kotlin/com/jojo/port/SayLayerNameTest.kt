package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class SayLayerNameTest {
    @Test
    fun `SayLayer removes from the first numeric instance marker`() {
        assertEquals("황건군 ", OriginalGameData.sayLayerUnitName("황건군 1"))
        assertEquals("궁병", OriginalGameData.sayLayerUnitName("궁병12"))
        assertEquals("조조", OriginalGameData.sayLayerUnitName("조조"))
    }
}
