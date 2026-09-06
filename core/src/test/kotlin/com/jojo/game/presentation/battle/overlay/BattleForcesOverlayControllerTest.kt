// Test
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.presentation.battle.overlay.ForcesListLayer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BattleForcesOverlayControllerTest {
    @Test
    fun `matched row press selects source unit and produces immutable renderer view`() {
        val controller = BattleForcesOverlayController()
        controller.open(mine = listOf(unit(10, "관우")), enemy = listOf(unit(20, "장료")), flag = 1)

        controller.dispatch(BattleForcesOverlayController.Intent.PointerDown(200f, 600f))
        val result = controller.dispatch(BattleForcesOverlayController.Intent.PointerUp(200f, 600f))

        val selected = assertIs<BattleForcesOverlayController.Effect.UnitSelected>(result.effect).unit
        assertEquals(10, selected.characterId)
        assertEquals(listOf("관우", "부대", "20", "90/100"), controller.view()?.rows?.single()?.values?.take(4))
    }

    @Test
    fun `tab and close require matching release target`() {
        val controller = BattleForcesOverlayController()
        controller.open(mine = listOf(unit(10, "관우")), enemy = listOf(unit(20, "장료")), flag = 1)

        controller.dispatch(BattleForcesOverlayController.Intent.PointerDown(400f, 100f))
        controller.dispatch(BattleForcesOverlayController.Intent.PointerUp(400f, 100f))
        assertEquals(1, controller.view()?.selectedTab)

        controller.dispatch(BattleForcesOverlayController.Intent.PointerDown(1150f, 100f))
        controller.dispatch(BattleForcesOverlayController.Intent.PointerUp(200f, 100f))
        assertTrue(controller.isVisible())

        controller.dispatch(BattleForcesOverlayController.Intent.PointerDown(1150f, 100f))
        val close = controller.dispatch(BattleForcesOverlayController.Intent.PointerUp(1150f, 100f))
        assertEquals(BattleForcesOverlayController.Effect.Closed, close.effect)
        assertFalse(controller.isVisible())
        assertNull(controller.view())
    }

    private fun unit(id: Int, name: String) = ForcesListLayer.Unit(
        id = id, name = name, post = "부대", level = 20, hp = 90, maxHp = 100,
        mp = 30, maxMp = 40, attack = 50, defense = 40, spirit = 30, critical = 20, morale = 60,
    )
}
