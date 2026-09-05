package com.jojo.game.presentation.scenario

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.scenario.PlaybackState

/** Advances source playback before rendering each frame. */
internal class ScenarioPlaybackFrameUpdater(
    private val playback: ScenarioInterpreter,
    private val playbackController: ScenarioPlaybackController,
    private val navigation: ScenarioNavigationCoordinator,
    private val isVerificationRun: () -> Boolean,
    private val isStreetPresentation: () -> Boolean,
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
        navigation.continueNaturally(isVerificationRun())
        navigation.driveYingchuanEntryFlow(elapsed, onAdvance)
        if (navigation.routedAfterCompletion) return ScenarioRenderPhaseResult.ROUTED
        if (ScenarioRenderPolicy.shouldRouteAfterCompletion(
                externalRuntimeOpen = isVerificationRun(),
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
                externalRuntimeOpen = isVerificationRun(),
                autoCloseSettingEnabled = autoCloseSettingEnabled(),
            ),
            revealDialogueImmediately = isStreetPresentation(),
            onAdvance = onAdvance,
        )
    }

    private fun settleStreetFixture() {
        if (!isStreetPresentation()) return
        var guard = 0
        while ((playback.state == PlaybackState.MODAL || playback.state == PlaybackState.DELAY) && guard++ < 1000) {
            if (playback.state == PlaybackState.MODAL) playback.resumeModal() else playback.skipDelay()
        }
        playback.stage.finishAnimations()
    }
}
