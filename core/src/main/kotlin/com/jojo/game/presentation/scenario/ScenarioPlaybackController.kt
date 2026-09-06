// Scenario
package com.jojo.game.presentation.scenario

import com.jojo.game.presentation.scenario.overlay.*
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.application.scenario.ScenarioStage
import com.jojo.game.presentation.shared.dialogue.DialogueSessionInput
import com.jojo.game.presentation.shared.dialogue.DialogueSessionTransition

import com.jojo.game.domain.scenario.PlaybackState

/** 재생 상태에서 화면에 필요한 값만 모은 불변 모델입니다. */
internal data class ScenarioViewState(
    val dialogueVisibleText: String,
    val modalVisibleText: String,
    val routedAfterCompletion: Boolean,
    /** 공용 세션이 계산한 현재 대화창 좌우 순번이다. */
    val dialogueSide: Int = 0,
    /** 공용 세션이 Hall unit YPos로 계산한 상단 배치 여부다. */
    val dialogueAtTop: Boolean = false,
)

/** 시나리오 텍스트 표시, 자동 닫기, 오디오, 일회성 이동을 조정합니다. */
internal class ScenarioPlaybackController(
    val playback: ScenarioInterpreter,
    /** `syncAudio` ((ScenarioStage) -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val syncAudio: (ScenarioStage) -> Unit,
    /** `disposeAudio` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val disposeAudio: () -> Unit,
) {
    /** 공용 대화 세션으로 스크립트 표시 상태와 입력 전이를 연결한다. */
    private val dialogueSession = ScenarioDialogueSessionAdapter()
    /**
     * `routeGate` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val routeGate = ScenarioRouteGate()
    /**
     * `viewState` (ScenarioViewState): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val viewState: ScenarioViewState
        get() = ScenarioViewState(
            dialogueVisibleText = dialogueSession.view.dialogueVisibleText,
            modalVisibleText = dialogueSession.view.modalVisibleText,
            routedAfterCompletion = routeGate.isRouted,
            dialogueSide = dialogueSession.view.dialogue?.side ?: 0,
            dialogueAtTop = dialogueSession.view.dialogue?.atTop ?: false,
        )

    /** 시나리오 해석기의 재생 시간을 진행합니다. */
    fun updatePlayback(delta: Float, autoCloseUi: Boolean) {
        playback.update(delta, autoCloseUi)
    }

    /** 재생 상태를 대화·모달 표시와 오디오 상태에 반영합니다. */
    fun updatePresentation(
        delta: Float,
        autoCloseEnabled: Boolean,
        revealDialogueImmediately: Boolean,
        onAdvance: () -> Unit,
    ) {
        syncAudio(playback.stage)
        dialogueSession.synchronize(playback)
        if (dialogueSession.update(delta, autoCloseEnabled) == DialogueSessionTransition.AutoAdvance) onAdvance()
        if (revealDialogueImmediately) dialogueSession.dispatch(DialogueSessionInput.RevealAll)
    }

    /** 현재 입력에 따라 대화, 선택, 모달 또는 다음 화면으로 진행합니다. */
    fun advance(
        onConfirmChoice: () -> Unit,
        closeHallMenu: () -> Boolean,
        beginHallBattleScene: () -> Boolean,
        onRoute: () -> Unit,
    ) {
        when (playback.state) {
            PlaybackState.DIALOGUE -> {
                if (dialogueSession.dispatch(DialogueSessionInput.Confirm) == DialogueSessionTransition.TextRevealed) return
                playback.advanceDialogue()
                dialogueSession.resetDialogueReveal()
            }

            PlaybackState.CHOICE -> onConfirmChoice()
            PlaybackState.DELAY -> Unit
            PlaybackState.MODAL -> {
                if (playback.currentModalKind == ScenarioModalKind.AMBITION) return
                if (playback.currentModalKind in setOf(
                        ScenarioModalKind.INFO,
                        ScenarioModalKind.MAP_INFO
                    ) &&
                    dialogueSession.dispatch(DialogueSessionInput.RevealAll) == DialogueSessionTransition.TextRevealed
                ) {
                    playback.completeModalTyping()
                    return
                }
                playback.resumeModal()
            }

            PlaybackState.COMPLETE -> if (playback.stage.menuVisible) {
                if (!closeHallMenu() && !beginHallBattleScene()) onRoute()
            } else onRoute()
        }
    }

    /** 대화 텍스트 표시 진행을 초기화합니다. */
    fun resetDialogueReveal() = dialogueSession.resetDialogueReveal()

    /** 이동 콜백이 한 번만 실행되도록 보장합니다. */
    fun routeOnce(action: () -> Unit) = routeGate.routeOnce(action)

    /** 시나리오 오디오 자원을 해제합니다. */
    fun dispose() = disposeAudio()
}

/** 시나리오 완료 후 이동 콜백의 중복 실행을 막습니다. */
internal class ScenarioRouteGate {
    /**
     * `isRouted` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var isRouted = false
        private set

    /** 아직 이동하지 않았다면 콜백을 실행합니다. */
    fun routeOnce(action: () -> Unit) {
        if (isRouted) return
        isRouted = true
        action()
    }
}
