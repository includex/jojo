// Test
package com.jojo.game.verification.cmd
import com.jojo.game.presentation.shared.overlay.*


import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** CmdRouteTest: CmdRoute의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class CmdRouteTest {
    @Test fun `setting button8 is the gated production caller`() {
        val route = CmdProductionRoute()
        assertFalse(route.settingTool(3, true, 0))
        assertFalse(route.settingTool(2, true, 1))
        assertFalse(route.settingTool(3, false, 1))
        assertTrue(route.settingTool(3, true, 1))
        assertEquals(CmdProductionRoute.State.CMD, route.state)
        assertEquals(listOf("SettingLayer.button8 TOUCH_END"), route.input)
    }

    @Test fun `close only accepts the authored touch-end`() {
        val route = CmdProductionRoute()
        assertTrue(route.settingTool(3, true, 1))
        assertFalse(route.close(false))
        assertTrue(route.close(true))
        assertEquals(CmdProductionRoute.State.CLOSED, route.state)
    }

    @Test fun `global cmd route is separate from battle command state`() {
        assertEquals(CmdRoute.DEFAULT, CmdRoute.parse("login-cmd-default-fixture"))
        assertEquals(CmdRoute.SELECTED, CmdRoute.parse("login-cmd-selected"))
        assertEquals(CmdRoute.INFO, CmdRoute.parse("login-cmd-info"))
        assertEquals(null, CmdRoute.parse("battle-command-initial"))
    }

    @Test fun `selection retains source unregistered row semantics`() {
        val layer = CmdLayer(1, 0, "device", 0, emptyList())
        layer.onCreate()
        layer.answer(1)
        layer.item(1, 2)
        assertEquals(2, layer.sFlag)
        assertEquals("선택했습니다1항, 총5원", layer.label)
    }
}
