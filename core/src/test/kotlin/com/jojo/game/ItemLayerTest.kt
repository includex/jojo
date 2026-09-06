// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.hall.ItemLayer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** ItemLayerTest: ItemLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class ItemLayerTest {
    @Test fun `details close only on touch end and property cannot open discard`() {
        val property = ItemLayer(150, "회복용 콩", false, object : ItemLayer.Repository {
            override fun discard(itemId: Int) = true
        })
        assertFalse(property.onButton(0, 1)); assertTrue(property.attached)
        assertFalse(property.onButton(1, ItemLayer.TOUCH_END)); assertFalse(property.discardConfirmationOpen)
        assertTrue(property.onButton(0, ItemLayer.TOUCH_END)); assertFalse(property.attached)
    }

    @Test fun `discard cancel retains item and confirm mutates repository then closes`() {
        val discarded = mutableListOf<Int>()
        val layer = ItemLayer(4, "긴 창", true, object : ItemLayer.Repository {
            override fun discard(itemId: Int): Boolean { discarded += itemId; return true }
        })
        assertTrue(layer.onButton(1, ItemLayer.TOUCH_END)); assertTrue(layer.discardConfirmationOpen)
        assertFalse(layer.onDiscardAnswer(1)); assertTrue(layer.attached); assertTrue(discarded.isEmpty())
        assertTrue(layer.onButton(1, ItemLayer.TOUCH_END)); assertTrue(layer.onDiscardAnswer(0))
        assertEquals(listOf(4), discarded); assertEquals("긴 창 이미 버렸습니다...", layer.toast); assertFalse(layer.attached)
    }
}
