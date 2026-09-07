// Verification
package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.jojo.game.application.runtime.BattleRuntimeScreenProbe
import com.jojo.game.application.runtime.RuntimeBattleCommand
import com.jojo.game.application.runtime.RuntimeBattleDriver
import com.jojo.game.application.runtime.RuntimeBattleFrame
import com.jojo.game.domain.scenario.PlaybackState

/**
 * FullBattleTraceDriver: 전체 전투 추적 실행을 위임 전투로 몰아주는 운영 입력 구동기다.
 *
 * 추적 봉투는 조작 출처가 운영 InputProcessor를 통과했음을 요구한다. 그래서 화면 상태를 직접
 * 건드리지 않고, 탐침이 알려 준 화면 좌표에 실제 touchDown/touchUp을 넣어 플레이어와 같은 경로로
 * 메뉴를 연다: 전투 메뉴 → 라운드 종료 → 위임 토글 → 확인. 위임이 켜지면 이후 라운드는 AI가
 * 양측을 모두 굴리므로 구동기는 손을 뗀다.
 */
internal class FullBattleTraceDriver : RuntimeBattleDriver {
    /** 다음 조작을 넣을 수 있는 가장 이른 시각이다. 한 프레임에 여러 번 누르지 않게 막는다. */
    private var nextTapAt = Float.NEGATIVE_INFINITY

    /** 마지막으로 수행한 단계다. 같은 상태에서 같은 조작을 반복하지 않도록 쓴다. */
    private var lastStage: String? = null

    override fun commands(frame: RuntimeBattleFrame, probe: BattleRuntimeScreenProbe): List<RuntimeBattleCommand> {
        if (probe.outcome != null) return emptyList()
        if (frame.elapsed < nextTapAt) return emptyList()

        // 전투 스크립트의 대사는 플레이어가 눌러야 넘어간다. 아무도 넘기지 않으면 대본이
        // 대사 하나에서 멈춘 채 전투가 시작되지도 끝나지도 않는다.
        if (probe.playback == PlaybackState.DIALOGUE) {
            // 대사 진행도 운영 입력으로 넣는다. 대사 캡처는 위치와 무관하게 최우선이라 아무 지점이나
            // 눌러도 되지만, 입력 표면이 받을 상태인지는 표면이 판단하게 둔다.
            tap(probe.battleMenuButtonScreenX, probe.battleMenuButtonScreenY + DIALOGUE_TAP_OFFSET_Y)
            nextTapAt = frame.elapsed + DIALOGUE_INTERVAL_SECONDS
            lastStage = null
            return emptyList()
        }

        // 라운드 시작 승리 조건 안내는 모달로 대본을 멈춘다. 위임 전투 중에도 닫아 줄 주체가
        // 없으면 여기서 진행이 끊긴다.
        if (probe.winConditionsOpen) {
            tap(probe.winConditionButtonScreenX, probe.winConditionButtonScreenY)
            nextTapAt = frame.elapsed + TAP_INTERVAL_SECONDS
            lastStage = null
            return emptyList()
        }

        if (!probe.bootstrapComplete) return emptyList()
        // 위임이 켜진 뒤에는 AI가 진행한다.
        if (probe.collocation) return emptyList()

        val stage = stageOf(probe) ?: return emptyList()
        if (stage == lastStage) return emptyList()

        val point = when (stage) {
            OPEN_MENU -> probe.battleMenuButtonScreenX to probe.battleMenuButtonScreenY
            END_ROUND -> probe.menuEndRoundScreenX to probe.menuEndRoundScreenY
            TOGGLE -> probe.autoBattleToggleScreenX to probe.autoBattleToggleScreenY
            else -> probe.autoBattleConfirmScreenX to probe.autoBattleConfirmScreenY
        }
        tap(point.first, point.second)
        lastStage = stage
        nextTapAt = frame.elapsed + TAP_INTERVAL_SECONDS
        return emptyList()
    }

    /** 현재 화면 상태가 요구하는 다음 조작 단계를 고른다. */
    private fun stageOf(probe: BattleRuntimeScreenProbe): String? = when {
        probe.autoBattleOverlay == "PROMPT" && !probe.autoBattleChecked -> TOGGLE
        probe.autoBattleOverlay == "PROMPT" -> CONFIRM
        probe.autoBattleOverlay != "NONE" -> null
        probe.battleMenuOpen -> END_ROUND
        probe.turnPhase == "PLAYER_INPUT" -> OPEN_MENU
        else -> null
    }

    /** 운영 InputProcessor에 실제 눌림·뗌을 넣는다. */
    private fun tap(x: Int, y: Int) {
        val processor = Gdx.input.inputProcessor ?: return
        processor.touchDown(x, y, 0, Input.Buttons.LEFT)
        processor.touchUp(x, y, 0, Input.Buttons.LEFT)
    }

    private companion object {
        const val OPEN_MENU = "open-menu"
        const val END_ROUND = "end-round"
        const val TOGGLE = "toggle"
        const val CONFIRM = "confirm"

        /** 같은 조작이 연속 프레임에 몰리지 않도록 두는 최소 간격이다. */
        const val TAP_INTERVAL_SECONDS = .2f

        /** 대사 넘김 간격이다. 글자 공개와 다음 대사 진행에 각각 한 번씩 눌린다. */
        const val DIALOGUE_INTERVAL_SECONDS = .35f

        /** 대사 넘김 탭이 전투 메뉴 버튼과 겹치지 않도록 아래로 내리는 화면 오프셋이다. */
        const val DIALOGUE_TAP_OFFSET_Y = 200
    }
}
