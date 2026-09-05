package com.jojo.game

import com.jojo.game.presentation.title.*
import com.jojo.game.presentation.title.evidence.TitleRenderEventRecorder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `TitleRenderEventRecorderTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class TitleRenderEventRecorderTest {
    private val recorder = TitleRenderEventRecorder()

    @Test fun `login events retain hidden panel and authored button order`() {
        val lines = recorder.record(TitleViewState(TitleMode.LOGIN, null)).lineSequence().filter(String::isNotBlank).toList()

        assertEquals(6, lines.size)
        assertTrue(lines[1].contains("Canvas/Layer/Panel_cancel"))
        assertTrue(lines[1].contains("\"visible\":false"))
        (0..3).forEach { index -> assertTrue(lines[index + 2].contains("button$index/Background")) }
    }

    @Test fun `load snapshot records rows before confirmation in draw order`() {
        val log = recorder.record(TitleViewState(
            mode = TitleMode.LOAD,
            optionalOverlayRoute = null,
            loadRows = listOf(TitleLoadRow("No.  1", "전역1", "조조", true)),
            loadConfirmationMessage = "진행도 No.1:조조불러올 수 있나요?",
        ))

        assertTrue(log.indexOf("No.  1") < log.indexOf("진행도 No.1"))
        assertTrue(log.contains("LoadGameLayer"))
        assertTrue(log.contains("\"opacity\":0.392"))
    }

    @Test fun `settings snapshot is value only and controls selected events`() {
        val log = recorder.record(TitleViewState(
            mode = TitleMode.SETTING,
            optionalOverlayRoute = null,
            settings = TitleSettingsView(flags = 1, messageSpeed = 2, notificationLevel = 0, background = 3, gameSpeed = .4f),
        ))

        assertTrue(log.contains("button0/toggle/checkmark"))
        assertFalse(log.contains("button1/toggle/checkmark"))
        assertTrue(log.contains("panel0/toggleContainer/toggle2/checkmark"))
        assertTrue(log.contains("panel3/item3/box6"))
    }
}
