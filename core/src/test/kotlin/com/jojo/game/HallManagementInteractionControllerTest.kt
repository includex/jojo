package com.jojo.game

import com.jojo.game.presentation.scenario.hall.HallItemInputIntent
import com.jojo.game.presentation.scenario.hall.HallBuyInputIntent
import com.jojo.game.presentation.scenario.hall.HallEquipConfirmationInputIntent
import com.jojo.game.presentation.scenario.hall.HallEquipInputIntent
import com.jojo.game.presentation.scenario.hall.HallManagementInteractionController
import com.jojo.game.presentation.scenario.hall.HallSellInputIntent
import com.jojo.game.presentation.scenario.hall.HallUnequipConfirmationInputIntent
import kotlin.test.Test
import kotlin.test.assertEquals

class HallManagementInteractionControllerTest {
    private val controller = HallManagementInteractionController()

    @Test
    fun `equipment, buy, and sell hit testing return stable typed intents`() {
        assertEquals(
            HallEquipInputIntent.SelectTab(1),
            controller.equipTap(300f, 580f),
        )
        assertEquals(
            HallEquipInputIntent.RequestWeaponUnequip,
            controller.equipTap(800f, 100f),
        )
        assertEquals(
            HallBuyInputIntent.SelectTab(1),
            controller.buyTap(400f, 540f, 0),
        )
        assertEquals(
            HallBuyInputIntent.Row(0),
            controller.buyTap(300f, 500f, 0),
        )
        assertEquals(HallBuyInputIntent.None, controller.buyTap(600f, 540f, 0))
        assertEquals(
            HallSellInputIntent.Cell(1, 1),
            controller.sellTap(700f, 300f),
        )
        assertEquals(
            HallUnequipConfirmationInputIntent.CONFIRM,
            controller.unequipConfirmationTap(500f, 300f),
        )
        assertEquals(
            HallEquipConfirmationInputIntent.CONFIRM,
            controller.equipConfirmationTap(500f, 230f),
        )
    }

    @Test
    fun `item layer hit testing preserves source scale and confirmation order`() {
        assertEquals(HallItemInputIntent.REQUEST_DISCARD, controller.itemTap(false, 950f * .86f, 120f * .86f))
        assertEquals(HallItemInputIntent.DISCARD_YES, controller.itemTap(true, 600f * .86f, 290f * .86f))
    }
}
