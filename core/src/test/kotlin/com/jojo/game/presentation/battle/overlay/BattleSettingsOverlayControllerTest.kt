package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BattleSettingsOverlayControllerTest {
    @Test
    fun `confirmed pointer gesture delegates setting changes and close lifecycle`() {
        val store = mutableMapOf<String, Int>(SettingLayer.GAME_SETTING to 0)
        val controller = BattleSettingsOverlayController(
            SettingLayer(object : SettingLayer.Store {
                override fun getInt(key: String, default: Int) = store[key] ?: default
                override fun putInt(key: String, value: Int) {
                    store[key] = value
                }
            }),
        )
        controller.open()

        controller.dispatch(BattleSettingsOverlayController.Intent.PointerDown(250f, 643f))
        controller.dispatch(BattleSettingsOverlayController.Intent.PointerUp(250f, 643f))
        assertEquals(1, controller.view()?.flags)

        controller.dispatch(BattleSettingsOverlayController.Intent.PointerDown(1160f, 70f))
        val result = controller.dispatch(BattleSettingsOverlayController.Intent.PointerUp(1160f, 70f))
        assertEquals(BattleSettingsOverlayController.Effect.Closed, result.effect)
        assertNull(controller.view())
    }
}
