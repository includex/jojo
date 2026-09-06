// 시나리오 거점 오버레이 입력 판별기
package com.jojo.game.presentation.scenario.input

import com.jojo.game.presentation.scenario.hall.HallLayerTapIntent

/** ScenarioHallSaveInputRouter: 저장 화면의 원본 좌표 터치를 저장 확인·행 선택 명령으로 판별한다. */
internal object ScenarioHallSaveInputRouter {
    /** Command: 저장 오버레이가 처리할 결과를 화면 독립 명령으로 표현한다. */
    sealed interface Command {
        /** CompletionTip: 저장 완료 안내를 닫는 확인 입력이다. */
        data object CompletionTip : Command
        /** Confirm: 저장 덮어쓰기 확인에서 사용자가 수락했는지 함께 전달한다. */
        data class Confirm(val accepted: Boolean) : Command
        /** Cancel: 저장 화면을 닫는 입력이다. */
        data object Cancel : Command
        /** SelectRow: 선택한 저장 슬롯 행 번호다. */
        data class SelectRow(val index: Int) : Command
        /** None: 어느 저장 화면 조작에도 해당하지 않는 입력이다. */
        data object None : Command
    }

    /** route: 화면 좌표를 원본 배율로 환산한 뒤 현재 모달 단계에 맞는 저장 명령을 반환한다. */
    fun route(x: Float, y: Float, completionTip: Boolean, pending: Boolean, rows: Int): Command {
        val sourceX = x / .86f; val sourceY = y / .86f
        if (completionTip) return if (sourceX in 654.186f..834.186f && sourceY in 271.285f..321.285f) Command.CompletionTip else Command.None
        if (pending) return when {
            sourceX in 554.186f..734.186f && sourceY in 271.285f..321.285f -> Command.Confirm(true)
            sourceX in 754.186f..934.186f && sourceY in 271.285f..321.285f -> Command.Confirm(false)
            else -> Command.None
        }
        if (sourceX in 1045.855f..1193.455f && sourceY in 100.162f..156.162f) return Command.Cancel
        if (sourceX !in 289.186f..1197.186f) return Command.None
        val row = (0 until minOf(rows, 8)).firstOrNull { sourceY in (547.534f - it * 52f)..(597.534f - it * 52f) }
        return row?.let(Command::SelectRow) ?: Command.None
    }
}

/** ScenarioExclusiveInputRouter: 전용 장비 화면의 탭·닫기 의도를 화면 전환 명령으로 축소한다. */
internal object ScenarioExclusiveInputRouter {
    /** Command: 전용 장비 화면이 처리할 두 탭, 닫기와 무입력 상태다. */
    enum class Command { SET_LIST, EXCLUSIVE_LIST, CLOSE, NONE }
    /** route: 공용 레이어 탭 의도를 전용 장비 화면의 명령으로 매핑한다. */
    fun route(intent: HallLayerTapIntent): Command = when (intent) {
        HallLayerTapIntent.PRIMARY -> Command.SET_LIST
        HallLayerTapIntent.SECONDARY -> Command.EXCLUSIVE_LIST
        HallLayerTapIntent.CLOSE, HallLayerTapIntent.CANCEL -> Command.CLOSE
        HallLayerTapIntent.NONE -> Command.NONE
    }
}
