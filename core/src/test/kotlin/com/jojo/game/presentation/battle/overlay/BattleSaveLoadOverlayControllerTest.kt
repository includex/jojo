// Test
package com.jojo.game.presentation.battle.overlay
import com.jojo.game.presentation.shared.overlay.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BattleSaveLoadOverlayControllerTest {
    @Test
    fun `save confirmation keeps completion tip before reporting a committed close`() {
        val savedSlots = mutableListOf<Int>()
        val controller = controller(savedSlots)

        controller.openSave()
        assertEquals(BattleSaveLoadOverlayKind.SAVE, controller.view(BattleSaveLoadOverlayController.Mode.SAVE)?.kind)

        controller.dispatch(down(300f, 590f))
        controller.dispatch(up(300f, 590f))
        assertTrue(controller.view(BattleSaveLoadOverlayController.Mode.SAVE)?.pendingSave == true)

        controller.dispatch(down(600f, 330f))
        controller.dispatch(up(600f, 330f))
        assertEquals(listOf(0), savedSlots)
        assertTrue(controller.view(BattleSaveLoadOverlayController.Mode.SAVE)?.saveCompletionTip == true)

        controller.dispatch(down(600f, 330f))
        val result = controller.dispatch(up(600f, 330f))
        assertEquals(BattleSaveLoadOverlayController.Effect.Closed(BattleSaveLoadOverlayController.Mode.SAVE, saved = true), result.effect)
        assertNull(controller.view(BattleSaveLoadOverlayController.Mode.SAVE))
    }

    @Test
    fun `load release outside the panel closes only the load overlay`() {
        val controller = controller(mutableListOf())
        controller.openLoad()

        controller.dispatch(down(100f, 100f))
        val result = controller.dispatch(up(100f, 100f))

        val effect = assertIs<BattleSaveLoadOverlayController.Effect.Closed>(result.effect)
        assertEquals(BattleSaveLoadOverlayController.Mode.LOAD, effect.mode)
        assertNull(controller.view(BattleSaveLoadOverlayController.Mode.LOAD))
    }

    private fun controller(savedSlots: MutableList<Int>) = BattleSaveLoadOverlayController(
        saveRepository = object : SaveLayer.Repository {
            override fun load(index: Int): String? = null
            override fun save(index: Int) {
                savedSlots += index
            }
        },
        loadRepository = object : LoadGameLayer.Repository {
            override fun load(index: Int): String? = null
            override fun savedPage() = 0
            override fun savePage(page: Int) = Unit
            override fun featureEnabled(name: String) = name == "ZDBHSW"
            override fun versionCode() = 1
            override fun restore(index: Int, raw: String, route: LoadGameLayer.RestoreRoute) = true
        },
    )

    private fun down(x: Float, y: Float) = BattleSaveLoadOverlayController.Intent.PointerDown(x, y)
    private fun up(x: Float, y: Float) = BattleSaveLoadOverlayController.Intent.PointerUp(x, y)
}
