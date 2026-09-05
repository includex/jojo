package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `ItemLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
