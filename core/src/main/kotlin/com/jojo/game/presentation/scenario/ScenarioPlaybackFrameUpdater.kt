package com.jojo.game.presentation.scenario

import com.jojo.game.JojoGame
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.application.scenario.ScenarioModalKind
import com.jojo.game.domain.scenario.PlaybackState

/** Advances source playback and deterministic capture routes before rendering each frame. */
internal class ScenarioPlaybackFrameUpdater(
    private val game: JojoGame,
    private val playback: ScenarioInterpreter,
    private val playbackController: ScenarioPlaybackController,
    private val navigation: ScenarioNavigationCoordinator,
    private val isVerificationRun: () -> Boolean,
    private val streetCaptureStage: String?,
    private val autoCloseSettingEnabled: () -> Boolean,
    private val onAdvance: () -> Unit,
) {
    var elapsed = 0f
        private set

    fun advanceClock(delta: Float) {
        elapsed += delta
    }

    fun updatePlayback(delta: Float): ScenarioRenderPhaseResult {
        settleStreetFixture()
        playbackController.updatePlayback(delta, autoCloseSettingEnabled())
        if (navigation.hallBattleScenePending && playback.state == PlaybackState.COMPLETE && !playback.stage.menuVisible) {
            navigation.routeAfterScenario()
            return ScenarioRenderPhaseResult.ROUTED
        }
        navigation.continueNaturally(isVerificationRun(), game.hasFrameCaptureRequest())
        navigation.driveYingchuanEntryFlow(elapsed, onAdvance)
        if (navigation.routedAfterCompletion) return ScenarioRenderPhaseResult.ROUTED
        settleRequestedCaptureState()
        if (ScenarioRenderPolicy.shouldRouteAfterCompletion(
                isVerificationRun = isVerificationRun(),
                hasFrameCaptureRequest = game.hasFrameCaptureRequest(),
                playbackState = playback.state,
                menuVisible = playback.stage.menuVisible,
                battleEndedByScript = playback.stage.battleEndedByScript,
                sceneJumpTarget = playback.stage.sceneJumpTarget,
            )) {
            navigation.routeAfterScenario()
            return ScenarioRenderPhaseResult.ROUTED
        }
        return ScenarioRenderPhaseResult.CONTINUE
    }

    fun updatePresentation(delta: Float) {
        playbackController.updatePresentation(
            delta = delta,
            autoCloseEnabled = ScenarioRenderPolicy.autoCloseEnabled(
                isVerificationRun = isVerificationRun(),
                hasFrameCaptureRequest = game.hasFrameCaptureRequest(),
                hasRenderEventLogRequest = game.hasRenderEventLogRequest(),
                autoCloseSettingEnabled = autoCloseSettingEnabled(),
            ),
            revealDialogueForCapture = streetCaptureStage != null && game.hasRenderEventLogRequest(),
            onAdvance = onAdvance,
        )
    }

    private fun settleStreetFixture() {
        if (streetCaptureStage == null) return
        var fixtureGuard = 0
        while ((playback.state == PlaybackState.MODAL || playback.state == PlaybackState.DELAY) && fixtureGuard++ < 1000) {
            if (playback.state == PlaybackState.MODAL) playback.resumeModal() else playback.skipDelay()
        }
        playback.stage.finishAnimations()
    }

    private fun settleRequestedCaptureState() {
        when (game.requestedCaptureState()) {
            "scenario-dialogue" -> settleScenarioDialogue()
            "map-info" -> settleMapInfo()
        }
    }

    private fun settleScenarioDialogue() {
        var guard = 0
        while (playback.state != PlaybackState.DIALOGUE && playback.state != PlaybackState.COMPLETE && guard++ < 1000) {
            when (playback.state) {
                PlaybackState.MODAL -> playback.resumeModal()
                PlaybackState.DELAY -> playback.skipDelay()
                PlaybackState.CHOICE -> playback.confirmChoice()
                PlaybackState.DIALOGUE, PlaybackState.COMPLETE -> Unit
            }
        }
    }

    private fun settleMapInfo() {
        var guard = 0
        while (!(playback.state == PlaybackState.MODAL && playback.currentModalKind == ScenarioModalKind.MAP_INFO) &&
            playback.state != PlaybackState.COMPLETE && guard++ < 1000
        ) {
            when (playback.state) {
                PlaybackState.DIALOGUE -> playback.advanceDialogue()
                PlaybackState.CHOICE -> playback.confirmChoice()
                PlaybackState.DELAY -> playback.skipDelay()
                PlaybackState.MODAL -> playback.resumeModal()
                PlaybackState.COMPLETE -> Unit
            }
        }
    }
}
