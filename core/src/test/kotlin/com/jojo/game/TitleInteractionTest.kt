// Test
package com.jojo.game
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.title.TitleInteraction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** TitleInteractionTest: TitleInteraction의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class TitleInteractionTest {
    @Test fun `four authored button rectangles dispatch exact title routes`() {
        val events = mutableListOf<String>()
        val routes = object : TitleInteraction.MainRoutes {
            override fun newGame(moduleName: String) { events += "new:$moduleName" }
            override fun openLoad() { events += "load" }
            override fun openSettings() { events += "settings" }
            override fun requestExit() { events += "exit" }
        }
        listOf(
            1097 to 500 to TitleInteraction.MainAction.NEW_GAME,
            1097 to 392 to TitleInteraction.MainAction.LOAD,
            1097 to 283 to TitleInteraction.MainAction.SETTINGS,
            1097 to 174 to TitleInteraction.MainAction.EXIT,
        ).forEach { (point, expected) ->
            val action = TitleInteraction.mainActionAt(point.first, point.second)
            assertEquals(expected, action)
            TitleInteraction.dispatch(requireNotNull(action), routes)
        }
        assertEquals(listOf("new:R_00", "load", "settings", "exit"), events)
        assertNull(TitleInteraction.mainActionAt(944, 500))
    }

    @Test fun `load overlay maps row confirm cancel and close without image input`() {
        assertEquals(TitleInteraction.LoadAction.SelectVisualRow(0), TitleInteraction.loadActionAt(640, 500, false))
        assertEquals(TitleInteraction.LoadAction.SelectVisualRow(7), TitleInteraction.loadActionAt(640, 180, false))
        assertEquals(TitleInteraction.LoadAction.CloseOverlay, TitleInteraction.loadActionAt(950, 120, false))
        assertEquals(TitleInteraction.LoadAction.CancelConfirmation, TitleInteraction.loadActionAt(550, 250, true))
        assertEquals(TitleInteraction.LoadAction.ConfirmLoad, TitleInteraction.loadActionAt(700, 250, true))
        assertNull(TitleInteraction.loadActionAt(950, 120, true))
    }

    @Test fun `natural title settings pointer route mutates store and closes through production contracts`() {
/** Store: 테스트에서 사용하는 입력·상태 조합을 표현하거나 대상 기능의 경계 조건을 보조 검증한다. */

        class Store : SettingLayer.Store {
            val values = mutableMapOf<String, Int>()
            override fun getInt(key: String, default: Int) = values[key] ?: default
            override fun putInt(key: String, value: Int) { values[key] = value }
        }
        val events = mutableListOf<String>()
        val store = Store()
        val setting = SettingLayer(store)
        val routes = object : TitleInteraction.MainRoutes {
            override fun newGame(moduleName: String) { events += "new:$moduleName" }
            override fun openLoad() { events += "load" }
            override fun openSettings() { setting.onCreate(); events += "settings:open" }
            override fun requestExit() { events += "exit" }
        }

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        TitleInteraction.dispatch(requireNotNull(TitleInteraction.mainActionAt(1097, 283)), routes)
        assertEquals(7, setting.view().flags)
        assertEquals(false, TitleInteraction.applySetting(requireNotNull(TitleInteraction.settingActionAt(220, 550)), setting))
        assertEquals(6, store.values[SettingLayer.GAME_SETTING])
        TitleInteraction.applySetting(requireNotNull(TitleInteraction.settingActionAt(920, 480)), setting)
        TitleInteraction.applySetting(requireNotNull(TitleInteraction.settingActionAt(980, 120)), setting)
        TitleInteraction.applySetting(requireNotNull(TitleInteraction.settingActionAt(850, 370)), setting)
        assertEquals(2, store.values[SettingLayer.MSG_SPEED])
        assertEquals(2, store.values[SettingLayer.BG_INDEX])
        assertTrue(TitleInteraction.applySetting(requireNotNull(TitleInteraction.settingActionAt(1040, 60)), setting))
        assertEquals(40, store.values[SettingLayer.GAME_SPEED])
        assertEquals(listOf("settings:open"), events)
        assertEquals(false, setting.view().attached)
    }
}
