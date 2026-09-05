package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `NoticeInfoLayerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class NoticeInfoLayerTest {
    @Test
    fun `hidden layer ignores messages and authored touch reaches shown endpoint`() {
        val layer = NoticeInfoLayer()
        assertFalse(layer.noticeMessage("숨김"))
        assertEquals(emptyList(), layer.view().messages)
        assertFalse(layer.touch(1))
        assertTrue(layer.touch(NoticeInfoLayer.TOUCH_END))
        assertTrue(layer.view().sliding)
        layer.advance(.59f)
        assertTrue(layer.view().bgY > NoticeInfoLayer.HIDDEN_Y)
        layer.advance(.02f)
        assertEquals(NoticeInfoLayer.SHOWN_Y, layer.view().bgY)
        assertFalse(layer.view().sliding)
    }

    @Test
    fun `NOTICE_MSG retains newest fifty and hide clears rows back into pool`() {
        val layer = NoticeInfoLayer()
        layer.touch(NoticeInfoLayer.TOUCH_END)
        layer.advance(.6f)
        repeat(52) { assertTrue(layer.noticeMessage("알림 ${it + 1}")) }
        assertEquals((3..52).map { "알림 $it" }, layer.view().messages)
        assertEquals(2, layer.view().poolSize)

        layer.touch(NoticeInfoLayer.TOUCH_END)
        assertEquals(emptyList(), layer.view().messages)
        assertEquals(52, layer.view().poolSize)
        layer.advance(.6f)
        assertEquals(NoticeInfoLayer.HIDDEN_Y, layer.view().bgY)
    }

    @Test
    fun `actual composition matches hidden shown and capped viewport contracts`() {
        val hidden = NoticeInfoLayer()
        val hiddenDraws = NoticeInfoRenderer.commands(hidden.view())
        assertEquals(2, hiddenDraws.size)
        assertEquals(listOf("bg1", "tool10"), hiddenDraws.map { it.asset })

        val shown = NoticeInfoLayer().also { it.touch(2); it.advance(.6f) }
        assertEquals(3, NoticeInfoRenderer.commands(shown.view()).size)

        repeat(52) { shown.noticeMessage("알림 ${String.format("%02d", it + 1)}") }
        val messageDraws = NoticeInfoRenderer.commands(shown.view())
        assertEquals(22, messageDraws.size) // bg + 10 labels + 9 lines + 2 button sprites
        assertEquals("알림 43", messageDraws.first { it.type == "label" }.text)
        assertEquals("알림 52", messageDraws.last { it.type == "label" }.text)
        assertEquals(-7.6f, messageDraws.first { it.type == "label" }.y)
        assertEquals(359.24f, messageDraws.last { it.type == "label" }.y)
    }

    @Test
    fun `actual battle init route owns layer 25 and all four logs are deterministic`() {
        val expectedCounts = mapOf(
            "notice-hidden" to 42,
            "notice-shown" to 43,
            "notice-messages" to 63,
            "notice-hidden-clear" to 43,
        )
        expectedCounts.forEach { (state, count) ->
            val route = BattleNoticeRoute.initialize(state)
            assertEquals(listOf(1, 25), route.attachedLayerIds)
            val log = NoticeInfoBattleRenderEvents.jsonl(state, route)
            assertEquals(count, log.trimEnd().lineSequence().count())
            assertEquals(log, NoticeInfoBattleRenderEvents.jsonl(state, route))
        }
        val messages = BattleNoticeRoute.initialize("notice-messages").notice.view()
        assertEquals(50, messages.messages.size)
        assertEquals("알림 03", messages.messages.first())
        assertEquals("알림 52", messages.messages.last())
        val cleared = BattleNoticeRoute.initialize("notice-hidden-clear").notice.view()
        assertFalse(cleared.shown)
        assertEquals(emptyList(), cleared.messages)
        assertEquals(4, cleared.poolSize)
    }
}
