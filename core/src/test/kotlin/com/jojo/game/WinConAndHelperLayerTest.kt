package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `WinConBoxLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class WinConBoxLayerTest {
    @Test fun `WinConBox onCreate sets bg label scroll and only end closes before callback`() {
        var calls = 0
        val layer = WinConBoxLayer()
        val created = layer.onCreate(WinConBoxLayer.CreateData("승리 조건") { calls++ })
        assertEquals("bg0", created.prefab.backgroundNode)
        assertEquals("bg0/scrollview/view/content/richtext", created.prefab.richTextNode)
        assertEquals(989f, created.prefab.backgroundWidth)
        assertEquals(670f, created.prefab.backgroundHeight)
        assertEquals(2, created.prefab.listenerPriority)
        assertEquals("승리 조건", created.label)
        assertTrue(created.scrollAtTop)
        assertTrue(created.attached)
        assertTrue(layer.onButtonTouch(1).attached)
        assertEquals(0, calls)
        assertFalse(layer.onButtonTouch(WinConBoxLayer.TOUCH_END).attached)
        assertEquals(1, calls)
    }

}
