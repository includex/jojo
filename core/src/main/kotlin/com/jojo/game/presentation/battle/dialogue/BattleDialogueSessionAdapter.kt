// Battle Dialogue
package com.jojo.game.presentation.battle.dialogue

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.shared.dialogue.DialogueChoice
import com.jojo.game.presentation.shared.dialogue.DialogueMessage
import com.jojo.game.presentation.shared.dialogue.DialogueModal
import com.jojo.game.presentation.shared.dialogue.DialogueModalKind
import com.jojo.game.presentation.shared.dialogue.DialogueSession
import com.jojo.game.presentation.shared.dialogue.DialogueSessionInput
import com.jojo.game.presentation.shared.dialogue.DialogueSessionTransition
import com.jojo.game.presentation.shared.dialogue.DialogueSessionView

/**
 * 전투 대화 세션 어댑터: 기존 `ScenarioInterpreter` 상태를 공용 대화 세션의 렌더·입력 계약으로 투영한다.
 *
 * `BattleScreen`은 이 어댑터의 전이 결과에 맞춰 기존 `advanceDialogue`, `confirmChoice`,
 * `resumeModal` 호출만 수행하면 되므로 전투 대기열과 스크립트 상태 소유권은 바뀌지 않는다.
 */
internal class BattleDialogueSessionAdapter(
    /** 대화·선택지·모달의 공용 표시와 입력 상태를 보관한다. */
    private val session: DialogueSession = DialogueSession(),
) {
    /** 현재 전투 대화 화면을 그릴 수 있는 공용 스냅샷이다. */
    val view: DialogueSessionView get() = session.view

    /** 전투 스크립트 상태를 세션에 동기화한다. */
    fun synchronize(runtime: ScenarioInterpreter) {
        when (runtime.state) {
            PlaybackState.DIALOGUE -> runtime.currentDialogue?.let { dialogue ->
                session.presentDialogue(
                    DialogueMessage(
                        revision = runtime.dialogueRevision,
                        speakerId = dialogue.speakerId,
                        text = dialogue.text,
                    ),
                )
            } ?: session.clear()

            PlaybackState.CHOICE -> runtime.currentChoice?.let { choice ->
                session.presentChoice(
                    DialogueChoice(
                        revision = choiceRevision(choice.options, choice.faceId, runtime.isAskChoice),
                        options = choice.options,
                        selectedIndex = runtime.selectedChoice,
                        isConfirmation = runtime.isAskChoice,
                        portraitId = choice.faceId?.toString(),
                    ),
                )
            } ?: session.clear()

            PlaybackState.MODAL -> runtime.currentModalText?.let { text ->
                session.presentModal(
                    DialogueModal(
                        revision = modalRevision(text, runtime.currentModalKind, runtime.currentModalFixedText),
                        kind = runtime.currentModalKind.toDialogueModalKind(),
                        text = text,
                        fixedText = runtime.currentModalFixedText,
                    ),
                )
            } ?: session.clear()

            PlaybackState.DELAY, PlaybackState.COMPLETE -> session.clear()
        }
    }

    /** 프레임 시간을 반영하고 자동 진행 전이를 반환한다. */
    fun update(deltaSeconds: Float, autoAdvanceEnabled: Boolean): DialogueSessionTransition =
        session.update(deltaSeconds, autoAdvanceEnabled)

    /** 전투 입력을 공용 대화 입력 전이로 변환한다. */
    fun dispatch(input: DialogueSessionInput): DialogueSessionTransition = session.dispatch(input)

    /** 대사 표시 진행을 초기화해 다음 스크립트 대사를 처음부터 공개하게 한다. */
    fun resetDialogueReveal() = session.clear()

    /** 선택지와 확인형 여부로 안정적인 공용 세션 갱신 번호를 만든다. */
    private fun choiceRevision(options: List<String>, faceId: Int?, isAsk: Boolean): Long =
        31L * options.hashCode() + 17L * (faceId ?: -1) + if (isAsk) 1L else 0L

    /** 모달 본문·종류·고정 본문으로 새 페이지를 구별할 공용 세션 갱신 번호를 만든다. */
    private fun modalRevision(text: String, kind: ScenarioModalKind?, fixedText: String): Long =
        31L * (31L * text.hashCode() + (kind?.ordinal ?: -1)) + fixedText.hashCode()

    /** 시나리오 스크립트 모달 종류를 공용 렌더 계약의 종류로 변환한다. */
    private fun ScenarioModalKind?.toDialogueModalKind(): DialogueModalKind = when (this) {
        ScenarioModalKind.EVENT -> DialogueModalKind.EVENT
        ScenarioModalKind.INFO -> DialogueModalKind.INFO
        ScenarioModalKind.MAP_INFO -> DialogueModalKind.MAP_INFO
        ScenarioModalKind.SECTION -> DialogueModalKind.SECTION
        ScenarioModalKind.AMBITION -> DialogueModalKind.AMBITION
        null -> DialogueModalKind.OTHER
    }
}
