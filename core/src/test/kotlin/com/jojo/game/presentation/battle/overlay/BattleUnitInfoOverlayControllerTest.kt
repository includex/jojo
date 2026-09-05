package com.jojo.game.presentation.battle.overlay

import com.jojo.game.UnitInfoLayer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BattleUnitInfoOverlayControllerTest {
    @Test
    fun `matched button presses delegate navigation and close lifecycle`() {
        val controller = BattleUnitInfoOverlayController()
        controller.open(listOf(unit(10, "관우"), unit(20, "장비")), index = 0)

        controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerDown(1150f, 60f))
        controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerUp(1150f, 60f))
        assertEquals("장비", controller.view()?.unit?.name)

        controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerDown(850f, 80f))
        val close = controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerUp(850f, 80f))
        assertEquals(BattleUnitInfoOverlayController.Effect.Closed, close.effect)
        assertNull(controller.view())
    }

    @Test
    fun `jiqi button uses source route and keeps unit info lifecycle visible`() {
        val controller = BattleUnitInfoOverlayController()
        controller.open(listOf(unit(10, "관우")), index = 0)

        controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerDown(750f, 50f))
        val result = controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerUp(750f, 50f))

        val jiqi = assertIs<BattleUnitInfoOverlayController.Effect.JiqiOpened>(result.effect)
        assertEquals(8, jiqi.layer.rates.size)
        assertTrue(controller.isVisible())
    }

    private fun unit(id: Int, name: String) = UnitInfoLayer.Unit(
        id = id, name = name, post = "부대", level = 20, hp = 90, maxHp = 100,
        mp = 30, maxMp = 40, attack = 50, defense = 40, spirit = 30, critical = 20, morale = 60,
    )
}
