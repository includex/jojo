// Scenario Dialogue
package com.jojo.game.presentation.scenario

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.presentation.shared.dialogue.RuntimeDialogueSessionAdapter
import com.jojo.game.presentation.shared.dialogue.DialoguePlacement
import com.jojo.engine.text.RevealTiming
import com.jojo.game.presentation.shared.dialogue.DialogueSession
import com.jojo.game.presentation.shared.dialogue.DialogueSessionInput
import com.jojo.game.presentation.shared.dialogue.DialogueSessionTransition
import com.jojo.game.presentation.shared.dialogue.DialogueSessionView

/**
 * 시나리오 대화 세션 어댑터: 스크립트 실행 상태를 공용 대화 세션의 표시·입력 계약으로 변환한다.
 * 스크립트 재개와 선택 결과 반영은 `ScenarioPlaybackController`가 전이 결과를 받아 기존 API로 처리한다.
 */
internal class ScenarioDialogueSessionAdapter(
    /** 시나리오와 전투가 함께 쓰는 대화 세션이다. */
    private val session: DialogueSession = DialogueSession(
        dialogueRevealTiming = RevealTiming.COCOS_CALLBACK_TIMER,
    ),
) {
    /** 원본 DialogueLayer의 화자 교대와 Hall unit 위치 판정을 재현하는 정책이다. */
    private val placementPolicy = ScenarioDialoguePlacementPolicy()
    /** 시나리오 오버레이 렌더러에 전달할 공용 대화 스냅샷이다. */
    val view: DialogueSessionView get() = adapter.view

    private val adapter = RuntimeDialogueSessionAdapter(session) { playback, message ->
        val placement = placementPolicy.resolve(message.speakerId?.toIntOrNull()) { speakerId ->
            playback.stage.units[speakerId]?.let { unit ->
                424f - 4f * (unit.visualX + unit.visualY)
            }
        }
        message.copy(
            placement = placement.toDialoguePlacement(),
            side = placement.side,
            atTop = placement.atTop,
        )
    }

    fun synchronize(playback: ScenarioInterpreter) = adapter.synchronize(playback)

    fun update(deltaSeconds: Float, autoAdvanceEnabled: Boolean): DialogueSessionTransition =
        adapter.update(deltaSeconds, autoAdvanceEnabled)

    fun dispatch(input: DialogueSessionInput): DialogueSessionTransition = adapter.dispatch(input)

    fun resetDialogueReveal() = adapter.resetDialogueReveal()

    /** 정책의 좌우·상하 결과를 공용 대화 배치 힌트로 변환한다. */
    private fun ScenarioDialoguePlacement.toDialoguePlacement(): DialoguePlacement = when {
        atTop && side == 0 -> DialoguePlacement.TOP_LEFT
        atTop -> DialoguePlacement.TOP_RIGHT
        side == 0 -> DialoguePlacement.BOTTOM_LEFT
        else -> DialoguePlacement.BOTTOM_RIGHT
    }
}
