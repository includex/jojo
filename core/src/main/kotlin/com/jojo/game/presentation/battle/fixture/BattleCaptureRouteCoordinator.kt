// Battle Fixture
package com.jojo.game.presentation.battle.fixture

/** 전투 캡처 경로 정책: 화면과 외부 IO 없이 로그·sidecar·프레임 캡처 명령의 시점과 순서를 계산한다. */
internal object BattleCaptureRouteCoordinator {
    /** 렌더 이벤트 로그를 시도할 조건을 계산하는 입력이다. */
    data class RenderEventLogInput(
        /** 현재 경과 시간: 초기 화면이 안정화된 뒤인지 판별한다. */
        val elapsed: Float,
        /** 전용 캡처 경로 여부: 렌더 이벤트 로그를 요구하는 경로인지 나타낸다. */
        val dedicatedCaptureRoute: Boolean,
    )

    /** 프레임 캡처 후속 작업을 계산하는 입력이다. */
    data class FrameCaptureInput(
        /** 현재 경과 시간: 캡처 기준 시각을 지났는지 판별한다. */
        val elapsed: Float,
        /** 캡처 기준 시각: 원본 화면이 기록되어야 하는 최소 시각이다. */
        val captureAt: Float,
        /** 맵 전용 캡처 여부: 맵 사각형 후보 sidecar를 함께 기록한다. */
        val mapOnlyCapture: Boolean,
        /** 메뉴 경로 여부: 메뉴 레이어 상태를 capture stack에 기록한다. */
        val battleMenuRoute: Boolean,
        /** 승리 조건 모달 경로 여부: 승리 조건 레이어 상태를 capture stack에 기록한다. */
        val winModalRoute: Boolean,
        /** 메뉴 표시 여부: 메뉴 capture stack의 현재 표시 상태다. */
        val battleMenuOpen: Boolean,
        /** 승리 조건 표시 여부: 승리 조건 capture stack의 현재 표시 상태다. */
        val winConditionOpen: Boolean,
        /** 승리 조건 레이어 생성 여부: 승리 조건 레이어의 존재를 나타낸다. */
        val winConditionLayerPresent: Boolean,
        /** 스크립트 승리 조건 모달 수: 현재 열린 모달 개수다. */
        val scriptWinConditionModalCount: Int,
    )

    /** 화면이 수행해야 하는 캡처 전용 명령이다. */
    sealed interface Command {
        /** 맵 quad 후보 sidecar를 기록한다. */
        data object WriteMapQuadCandidateSidecar : Command

        /** 캡처 stack 상태를 기록한다. */
        data class WriteCaptureStack(
            /** 검증기가 요구한 레이어 이름이다. */
            val requested: String,
            /** 요구 레이어가 현재 표시 중인지 나타낸다. */
            val requestedPresent: Boolean,
            /** 대사 레이어 표시 여부다. */
            val dialogue: Boolean,
            /** 선택지 레이어 표시 여부다. */
            val choice: Boolean,
            /** 모달 레이어 개수다. */
            val modalCount: Int,
        ) : Command

        /** 프레임 캡처 요청을 처리한다. */
        data object CaptureFrame : Command
    }

    /** 안정화 이후 전용 경로의 렌더 이벤트 로그 시도를 허용한다. */
    fun shouldWriteRenderEventLog(input: RenderEventLogInput): Boolean =
        input.dedicatedCaptureRoute && input.elapsed > RENDER_EVENT_LOG_DELAY

    /** 현재 시각에 수행할 캡처 명령을 원본 순서대로 반환한다. */
    fun frameCaptureCommands(input: FrameCaptureInput): List<Command> {
        if (input.elapsed <= input.captureAt) return emptyList()
        return buildList {
            if (input.mapOnlyCapture) add(Command.WriteMapQuadCandidateSidecar)
            when {
                input.battleMenuRoute -> add(
                    Command.WriteCaptureStack(
                        requested = MENU_LAYER,
                        requestedPresent = input.battleMenuOpen,
                        dialogue = false,
                        choice = false,
                        modalCount = 0,
                    ),
                )

                input.winModalRoute -> add(
                    Command.WriteCaptureStack(
                        requested = WIN_CONDITION_LAYER,
                        requestedPresent = input.winConditionOpen && input.winConditionLayerPresent,
                        dialogue = false,
                        choice = false,
                        modalCount = input.scriptWinConditionModalCount,
                    ),
                )
            }
            add(Command.CaptureFrame)
        }
    }

    private const val RENDER_EVENT_LOG_DELAY = .25f
    private const val MENU_LAYER = "MenuLayer"
    private const val WIN_CONDITION_LAYER = "WinConBoxLayer"
}
