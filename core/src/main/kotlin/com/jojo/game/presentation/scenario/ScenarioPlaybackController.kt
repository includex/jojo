// Scenario
package com.jojo.game.presentation.scenario

import com.jojo.game.presentation.scenario.overlay.*
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.application.scenario.ScenarioStage

import com.jojo.game.domain.scenario.PlaybackState

/** 재생 상태에서 화면에 필요한 값만 모은 불변 모델입니다. */
internal data class ScenarioViewState(
    val dialogueVisibleText: String,
    val modalVisibleText: String,
    val routedAfterCompletion: Boolean,
)

/** 시나리오 텍스트 표시, 자동 닫기, 오디오, 일회성 이동을 조정합니다. */
internal class ScenarioPlaybackController(
    val playback: ScenarioInterpreter,
    private val syncAudio: (ScenarioStage) -> Unit,
    private val disposeAudio: () -> Unit,
) {
    private val dialogueReveal = SourceTextReveal()
    private val sayAutoClose = SayLayerAutoClose()
    private val modalReveal = SourceTextReveal()
    private val routeGate = ScenarioRouteGate()
    private var revealedModalSource: String? = null

    val viewState: ScenarioViewState
        get() = ScenarioViewState(
            dialogueVisibleText = dialogueReveal.visibleText,
            modalVisibleText = modalReveal.visibleText,
            routedAfterCompletion = routeGate.isRouted,
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
        playback.currentDialogue?.let {
            dialogueReveal.update(it.text, delta)
            if (sayAutoClose.update(dialogueReveal.isComplete, autoCloseEnabled, delta)) onAdvance()
        } ?: sayAutoClose.reset()
        if (revealDialogueImmediately) dialogueReveal.revealAllIfPending()
        playback.currentModalText?.let { text ->
            if (revealedModalSource != text) {
                revealedModalSource = text
                modalReveal.reset()
            }
            modalReveal.update(text, delta)
        } ?: run { revealedModalSource = null }
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
                if (dialogueReveal.revealAllIfPending()) {
                    sayAutoClose.reset()
                    return
                }
                sayAutoClose.reset()
                playback.advanceDialogue()
                dialogueReveal.reset()
            }

            PlaybackState.CHOICE -> onConfirmChoice()
            PlaybackState.DELAY -> Unit
            PlaybackState.MODAL -> {
                if (playback.currentModalKind == ScenarioModalKind.AMBITION) return
                if (playback.currentModalKind in setOf(
                        ScenarioModalKind.INFO,
                        ScenarioModalKind.MAP_INFO
                    ) &&
                    modalReveal.revealAllIfPending()
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
    fun resetDialogueReveal() = dialogueReveal.reset()

    /** 이동 콜백이 한 번만 실행되도록 보장합니다. */
    fun routeOnce(action: () -> Unit) = routeGate.routeOnce(action)

    /** 시나리오 오디오 자원을 해제합니다. */
    fun dispose() = disposeAudio()
}

/** 시나리오 완료 후 이동 콜백의 중복 실행을 막습니다. */
internal class ScenarioRouteGate {
    var isRouted = false
        private set

    /** 아직 이동하지 않았다면 콜백을 실행합니다. */
    fun routeOnce(action: () -> Unit) {
        if (isRouted) return
        isRouted = true
        action()
    }
}
