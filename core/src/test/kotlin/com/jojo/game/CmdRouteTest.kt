package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `CmdRouteTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
