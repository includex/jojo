// Test
package com.jojo.game.verification.title
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.presentation.title.*
import com.jojo.game.verification.title.evidence.TitleRenderEventRecorder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** TitleRenderEventRecorderTest: TitleRenderEventRecorder의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class TitleRenderEventRecorderTest {
    private val recorder = TitleRenderEventRecorder()

    @Test fun `start item route owns hidden cancel and ordered choices`() {
        val rows = StartItemRenderEvents.jsonl().lineSequence().filter(String::isNotBlank).toList()
        assertEquals(6, rows.size)
        assertTrue(rows[0].contains("Canvas/bg"))
        assertTrue(rows[1].contains("Canvas/Layer/Panel_cancel"))
        assertTrue(rows[1].contains("\"opacity\":0"))
        assertTrue(rows[1].contains("\"visible\":false"))
        (0..3).forEach { index ->
            assertTrue(rows[index + 2].contains("Canvas/Layer/bg1/button$index/Background"))
            assertTrue(rows[index + 2].contains("U_select_12-1_$index"))
        }
    }

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

    @Test fun `optional overlays retain authored route-specific event order`() {
        val signIn = recorder.record(TitleViewState(TitleMode.LOGIN, LoginOptionalOverlayRoute.SIGNIN_OPEN))
            .lineSequence().filter(String::isNotBlank).toList()
        val version = recorder.record(TitleViewState(TitleMode.LOGIN, LoginOptionalOverlayRoute.VERSION_OPEN))
            .lineSequence().filter(String::isNotBlank).toList()

        assertEquals(73, signIn.size)
        assertEquals(17, version.size)
        assertTrue(signIn[5].contains("SignInLayer"))
        assertTrue(signIn[5].contains("Canvas/Layer/Panel_cancel"))
        assertTrue(signIn[6].contains("Canvas/Layer/Logo_12-1"))
        assertTrue(version[5].contains("VersionInfoLayer"))
        assertTrue(version.last().contains("button0/Background/Label"))
    }
}
