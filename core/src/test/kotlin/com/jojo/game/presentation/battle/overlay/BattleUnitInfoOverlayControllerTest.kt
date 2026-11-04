// Test
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.presentation.shared.overlay.UnitInfoLayer
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

        val next = BattleUnitInfoLayout.next
        controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerDown(next.x + next.width/2, next.y + next.height/2))
        controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerUp(next.x + next.width/2, next.y + next.height/2))
        assertEquals("장비", controller.view()?.unit?.name)

        val closeRect = BattleUnitInfoLayout.close
        controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerDown(closeRect.x + closeRect.width/2, closeRect.y + closeRect.height/2))
        val close = controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerUp(closeRect.x + closeRect.width/2, closeRect.y + closeRect.height/2))
        assertEquals(BattleUnitInfoOverlayController.Effect.Closed, close.effect)
        assertNull(controller.view())
    }

    @Test
    fun `jiqi button uses source route and keeps unit info lifecycle visible`() {
        val controller = BattleUnitInfoOverlayController()
        controller.open(listOf(unit(10, "관우")), index = 0)

        val rect=BattleUnitInfoLayout.rates
        val x=rect.x+rect.width/2;val y=rect.y+rect.height/2
        controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerDown(x,y))
        val result = controller.dispatch(BattleUnitInfoOverlayController.Intent.PointerUp(x,y))

        val jiqi = assertIs<BattleUnitInfoOverlayController.Effect.JiqiOpened>(result.effect)
        assertEquals(8, jiqi.layer.rates.size)
        assertTrue(controller.isVisible())
    }

    private fun unit(id: Int, name: String) = UnitInfoLayer.Unit(
        id = id, name = name, post = "부대", level = 20, hp = 90, maxHp = 100,
        mp = 30, maxMp = 40, attack = 50, defense = 40, spirit = 30, critical = 20, morale = 60,
    )
}
