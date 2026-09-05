package com.jojo.game

import com.jojo.game.presentation.scenario.hall.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HallItemRendererTest {
    @Test
    fun itemViewKeepsOptionalItemLayerActionsAsImmutableInputs() {
        val view = HallItemView(
            itemName = "검", category = 0, level = "1", experience = 4, experienceLimit = 10,
            typeName = "무기", price = "100", effect = "공격력 +1", intro = "설명",
            postNames = listOf("보병"), canDrop = true, discardConfirmationOpen = false,
            logoTexture = null, buttonTexture = null, box1Texture = null, box2Texture = null,
            titleTexture = null, itemIconTexture = null,
        )

        assertEquals(true, view.canDrop)
        assertEquals(false, view.discardConfirmationOpen)
        assertEquals("검", view.itemName)
    }
}
