package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `DialogueLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class DialogueLayerTest {
    private val names = mapOf(0 to "조조", 157 to "하후돈")

    @Test fun `tap completes typewriter before advancing and alternates speaker bubble`() {
        DialogueLayer.resetAlternationForTest()
        val layer = DialogueLayer("&0\n첫째\n둘째\n셋째\n&157\n대답", { names.getValue(it) }, { if (it == 157) -60f else 0f })
        assertEquals(0, layer.view().bubble)
        assertTrue(layer.touch(DialogueLayer.TOUCH_END))
        assertEquals("첫째<br/>둘째<br/>셋째", layer.view().content)
        assertEquals(0, layer.view().speakerId)
        layer.touch(DialogueLayer.TOUCH_END)
        assertEquals(1, layer.view().bubble)
        assertEquals(157, layer.view().speakerId)
        assertTrue(layer.view().top)
        assertEquals(listOf("SHOW_SAY:0", "SHOW_SAY:157"), layer.events)
    }

    @Test fun `skip closes immediately and dispatches hide say`() {
        DialogueLayer.resetAlternationForTest()
        var closed = 0
        val layer = DialogueLayer("&0\n문장", { names.getValue(it) }, { 0f }, onClose = { closed++ })
        layer.skip()
        assertFalse(layer.attached)
        assertEquals(1, closed)
        assertEquals(listOf("SHOW_SAY:0", "HIDE_SAY"), layer.events)
    }

    @Test fun `auto close waits after typing completion`() {
        DialogueLayer.resetAlternationForTest()
        val layer = DialogueLayer("&0\n문장", { names.getValue(it) }, { 0f }, flag = DialogueLayer.AUTO_CLOSE)
        layer.completeTyping()
        layer.advance(1.599f)
        assertTrue(layer.attached)
        layer.advance(.01f)
        assertFalse(layer.attached)
        assertTrue(layer.events.isEmpty())
    }

    @Test fun `missing Hall unit retains bubble and typewriter balances color markup`() {
        DialogueLayer.resetAlternationForTest()
        val first = DialogueLayer("&157\n대답", { names.getValue(it) }, { -60f })
        assertEquals(1, first.view().bubble)

        val missing = DialogueLayer("&999\n<color=#fff>글자", { "미등록" }, { null })
        assertEquals(1, missing.view().bubble)
        missing.typeTick()
        assertTrue(missing.view().content.endsWith("</c>"))
        missing.completeTyping()
        assertEquals("<color=#fff>글자", missing.view().content)
    }
}
