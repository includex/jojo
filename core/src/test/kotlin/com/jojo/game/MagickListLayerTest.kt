package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `MagickListLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class MagickListLayerTest {
    private val cheap = MagicUiList.Magic(39, "소량의 보급품", 6, 28, 7, 13, 0, "치료")
    private val costly = MagicUiList.Magic(45, "책사 추천", 48, 28, 8, 13, 0, "추천")

    @Test
    fun `actual command list gates MP and short release selects`() {
        val layer = MagicUiList(24, 58, listOf(costly, cheap), emptyMap())
        assertEquals(listOf(39, 45), layer.rows.map { it.id })
        assertTrue(layer.enabled(0))
        assertFalse(layer.enabled(1))
        layer.start(1)
        layer.end(1)
        assertTrue(layer.attached)
        assertTrue(layer.events.isEmpty())

        layer.start(0)
        assertEquals(18f / 58f, layer.preview)
        layer.end(0)
        assertFalse(layer.attached)
        assertEquals(1, layer.uses[39])
        assertEquals(listOf("remove", "selected:39"), layer.events)
    }

    @Test
    fun `one second hold opens detail and release cannot also select`() {
        val layer = MagicUiList(24, 58, listOf(cheap), emptyMap())
        layer.start(0)
        assertEquals(cheap, layer.tick())
        assertEquals(24f / 58f, layer.preview)
        layer.end(0)
        assertTrue(layer.attached)
        assertNull(layer.uses[39])
        assertEquals(listOf("layer:MagicLayer:39"), layer.events)
    }

    @Test
    fun `Panel cancel only handles TOUCH_END and calls cancelled route`() {
        val layer = MagicUiList(24, 58, listOf(cheap), emptyMap())
        layer.cancel(1)
        assertTrue(layer.attached)
        layer.cancel(MagicUiList.TOUCH_END)
        assertFalse(layer.attached)
        assertEquals(listOf("remove", "cancelled"), layer.events)
    }
}
