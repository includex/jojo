// Scenario Dialogue
package com.jojo.game.presentation.scenario

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.shared.dialogue.DialogueChoice
import com.jojo.game.presentation.shared.dialogue.DialogueMessage
import com.jojo.game.presentation.shared.dialogue.DialogueModal
import com.jojo.game.presentation.shared.dialogue.DialogueModalKind
import com.jojo.game.presentation.shared.dialogue.DialoguePlacement
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
    private val session: DialogueSession = DialogueSession(),
) {
    /** 원본 DialogueLayer의 화자 교대와 Hall unit 위치 판정을 재현하는 정책이다. */
    private val placementPolicy = ScenarioDialoguePlacementPolicy()
    /** 시나리오 오버레이 렌더러에 전달할 공용 대화 스냅샷이다. */
    val view: DialogueSessionView get() = session.view

    /** 현재 스크립트 입력 상태를 공용 세션에 동기화한다. */
    fun synchronize(playback: ScenarioInterpreter) {
        when (playback.state) {
            PlaybackState.DIALOGUE -> playback.currentDialogue?.let { dialogue ->
                val placement = placementPolicy.resolve(dialogue.speakerId?.toIntOrNull()) { speakerId ->
                    playback.stage.units[speakerId]?.let { unit ->
                        424f - 4f * (unit.visualX + unit.visualY)
                    }
                }
                session.presentDialogue(
                    DialogueMessage(
                        revision = playback.dialogueRevision,
                        speakerId = dialogue.speakerId,
                        text = dialogue.text,
                        placement = placement.toDialoguePlacement(),
                        side = placement.side,
                        atTop = placement.atTop,
                    ),
                )
            } ?: session.clear()

            PlaybackState.CHOICE -> playback.currentChoice?.let { choice ->
                session.presentChoice(
                    DialogueChoice(
                        revision = choiceRevision(choice.options, choice.faceId, playback.isAskChoice),
                        options = choice.options,
                        selectedIndex = playback.selectedChoice,
                        isConfirmation = playback.isAskChoice,
                        portraitId = choice.faceId?.toString(),
                    ),
                )
            } ?: session.clear()

            PlaybackState.MODAL -> playback.currentModalText?.let { text ->
                session.presentModal(
                    DialogueModal(
                        revision = modalRevision(text, playback.currentModalKind, playback.currentModalFixedText),
                        kind = playback.currentModalKind.toDialogueModalKind(),
                        text = text,
                        fixedText = playback.currentModalFixedText,
                    ),
                )
            } ?: session.clear()

            PlaybackState.DELAY, PlaybackState.COMPLETE -> session.clear()
        }
    }

    /** 프레임 시간을 반영하고 자동 진행 전이를 반환한다. */
    fun update(deltaSeconds: Float, autoAdvanceEnabled: Boolean): DialogueSessionTransition =
        session.update(deltaSeconds, autoAdvanceEnabled)

    /** 화면 입력을 공용 대화 세션 전이로 변환한다. */
    fun dispatch(input: DialogueSessionInput): DialogueSessionTransition = session.dispatch(input)

    /** 현재 대사 표시 진행만 초기화해 다음 대사를 처음부터 공개한다. */
    fun resetDialogueReveal() = session.clear()

    /** 선택지 본문과 확인형 여부를 안정적인 세션 갱신 번호로 변환한다. */
    private fun choiceRevision(options: List<String>, faceId: Int?, isAsk: Boolean): Long =
        31L * options.hashCode() + 17L * (faceId ?: -1) + if (isAsk) 1L else 0L

    /** 모달 본문·종류·고정 본문을 새 페이지 판별용 갱신 번호로 변환한다. */
    private fun modalRevision(text: String, kind: ScenarioModalKind?, fixedText: String): Long =
        31L * (31L * text.hashCode() + (kind?.ordinal ?: -1)) + fixedText.hashCode()

    /** 시나리오 전용 모달 종류를 공용 대화 렌더 계약의 종류로 변환한다. */
    private fun ScenarioModalKind?.toDialogueModalKind(): DialogueModalKind = when (this) {
        ScenarioModalKind.EVENT -> DialogueModalKind.EVENT
        ScenarioModalKind.INFO -> DialogueModalKind.INFO
        ScenarioModalKind.MAP_INFO -> DialogueModalKind.MAP_INFO
        ScenarioModalKind.SECTION -> DialogueModalKind.SECTION
        ScenarioModalKind.AMBITION -> DialogueModalKind.AMBITION
        null -> DialogueModalKind.OTHER
    }

    /** 정책의 좌우·상하 결과를 공용 대화 배치 힌트로 변환한다. */
    private fun ScenarioDialoguePlacement.toDialoguePlacement(): DialoguePlacement = when {
        atTop && side == 0 -> DialoguePlacement.TOP_LEFT
        atTop -> DialoguePlacement.TOP_RIGHT
        side == 0 -> DialoguePlacement.BOTTOM_LEFT
        else -> DialoguePlacement.BOTTOM_RIGHT
    }
}
