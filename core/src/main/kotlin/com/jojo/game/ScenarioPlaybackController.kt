package com.jojo.game

/** Immutable render-facing projection of the playback presentation state. */
internal data class ScenarioViewState(
    val dialogueVisibleText: String,
    val modalVisibleText: String,
    val routedAfterCompletion: Boolean,
)

/** Owns the scenario UI's reveal, auto-close, audio, and one-shot route state. */
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

    fun updatePlayback(delta: Float, autoCloseUi: Boolean) {
        playback.update(delta, autoCloseUi)
    }

    fun updatePresentation(
        delta: Float,
        autoCloseEnabled: Boolean,
        revealDialogueForCapture: Boolean,
        onAdvance: () -> Unit,
    ) {
        syncAudio(playback.stage)
        playback.currentDialogue?.let {
            dialogueReveal.update(it.text, delta)
            if (sayAutoClose.update(dialogueReveal.isComplete, autoCloseEnabled, delta)) onAdvance()
        } ?: sayAutoClose.reset()
        if (revealDialogueForCapture) dialogueReveal.revealAllIfPending()
        playback.currentModalText?.let { text ->
            if (revealedModalSource != text) {
                revealedModalSource = text
                modalReveal.reset()
            }
            modalReveal.update(text, delta)
        } ?: run { revealedModalSource = null }
    }

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
                if (playback.currentModalKind == ScenarioInterpreter.ModalKind.AMBITION) return
                if (playback.currentModalKind in setOf(
                        ScenarioInterpreter.ModalKind.INFO,
                        ScenarioInterpreter.ModalKind.MAP_INFO
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

    fun resetDialogueReveal() = dialogueReveal.reset()

    fun routeOnce(action: () -> Unit) = routeGate.routeOnce(action)

    fun dispose() = disposeAudio()
}

internal class ScenarioRouteGate {
    var isRouted = false
        private set

    fun routeOnce(action: () -> Unit) {
        if (isRouted) return
        isRouted = true
        action()
    }
}
