// Battle Fixture Test
package com.jojo.game.presentation.battle.fixture

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 전투 캡처 경로 정책이 화면 IO 없이 시점과 명령 순서를 계산하는지 검증한다. */
class BattleCaptureRouteCoordinatorTest {
    @Test
    fun `렌더 이벤트 로그는 안정화 시각 이후 전용 경로에서만 허용된다`() {
        assertFalse(
            BattleCaptureRouteCoordinator.shouldWriteRenderEventLog(
                BattleCaptureRouteCoordinator.RenderEventLogInput(.25f, true),
            ),
        )
        assertTrue(
            BattleCaptureRouteCoordinator.shouldWriteRenderEventLog(
                BattleCaptureRouteCoordinator.RenderEventLogInput(.26f, true),
            ),
        )
        assertFalse(
            BattleCaptureRouteCoordinator.shouldWriteRenderEventLog(
                BattleCaptureRouteCoordinator.RenderEventLogInput(1f, false),
            ),
        )
    }

    @Test
    fun `맵 캡처는 sidecar 뒤에 프레임 명령을 둔다`() {
        assertEquals(
            listOf(
                BattleCaptureRouteCoordinator.Command.WriteMapQuadCandidateSidecar,
                BattleCaptureRouteCoordinator.Command.CaptureFrame,
            ),
            BattleCaptureRouteCoordinator.frameCaptureCommands(
                BattleCaptureRouteCoordinator.FrameCaptureInput(
                    elapsed = 1.01f,
                    captureAt = 1f,
                    mapOnlyCapture = true,
                    battleMenuRoute = false,
                    winModalRoute = false,
                    battleMenuOpen = false,
                    winConditionOpen = false,
                    winConditionLayerPresent = false,
                    scriptWinConditionModalCount = 0,
                ),
            ),
        )
    }

    @Test
    fun `모달 캡처는 요청 레이어 상태와 모달 수를 프레임보다 먼저 기록한다`() {
        assertEquals(
            listOf(
                BattleCaptureRouteCoordinator.Command.WriteCaptureStack(
                    requested = "WinConBoxLayer",
                    requestedPresent = true,
                    dialogue = false,
                    choice = false,
                    modalCount = 1,
                ),
                BattleCaptureRouteCoordinator.Command.CaptureFrame,
            ),
            BattleCaptureRouteCoordinator.frameCaptureCommands(
                BattleCaptureRouteCoordinator.FrameCaptureInput(
                    elapsed = 2f,
                    captureAt = 1f,
                    mapOnlyCapture = false,
                    battleMenuRoute = false,
                    winModalRoute = true,
                    battleMenuOpen = false,
                    winConditionOpen = true,
                    winConditionLayerPresent = true,
                    scriptWinConditionModalCount = 1,
                ),
            ),
        )
    }

    @Test
    fun `캡처 기준 시각까지는 명령을 생성하지 않는다`() {
        assertTrue(
            BattleCaptureRouteCoordinator.frameCaptureCommands(
                BattleCaptureRouteCoordinator.FrameCaptureInput(
                    elapsed = 1f,
                    captureAt = 1f,
                    mapOnlyCapture = true,
                    battleMenuRoute = true,
                    winModalRoute = false,
                    battleMenuOpen = true,
                    winConditionOpen = false,
                    winConditionLayerPresent = false,
                    scriptWinConditionModalCount = 0,
                ),
            ).isEmpty(),
        )
    }
}
