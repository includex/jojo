package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModalLoadRouteTest {
    @Test fun `getSystemTime owns actual modal load lifecycle`() {
        val route = ModalLoadProductionRoute()
        route.getSystemTimeStarted()
        assertTrue(route.attached)
        assertEquals("검증 중……", route.text)
        route.requestCompleted()
        assertFalse(route.attached)
    }

    @Test fun `load layer exposes label only when caller supplies text`() {
        val layer = LoadLayer()
        assertEquals(LoadLayer.View(true, "검증 중……", "rotateBy:2:360:repeatForever"), layer.onCreate("검증 중……"))
        assertFalse(layer.onCreate(null).labelActive)
    }
}
