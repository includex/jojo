package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BattleRegisterRouteTest {
    @Test fun `six actual title touch ends attach Global139`() {
        val route = BattleRegisterRoute()
        repeat(5) { route.titleTouchEnd() }
        assertFalse(route.view().registerAttached)
        route.titleTouchEnd()
        assertTrue(route.view().registerAttached)
        assertEquals(6, route.view().titleTouchCount)
    }

    @Test fun `seventh touch wraps recovered counter`() {
        val route = BattleRegisterRoute()
        repeat(7) { route.titleTouchEnd() }
        assertEquals(0, route.view().titleTouchCount)
        assertTrue(route.view().registerAttached)
    }

    @Test fun `register cancel removes the attached layer`() {
        val route = BattleRegisterRoute()
        repeat(6) { route.titleTouchEnd() }
        route.cancelTouchEnd()
        assertFalse(route.view().registerAttached)
    }
}
