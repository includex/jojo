// 시나리오 재생 프레임 갱신
package com.jojo.game.presentation.scenario

import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.scenario.PlaybackState

/** ScenarioPlaybackFrameUpdater: 한 프레임의 해석기 진행, 완료 전환 판정, 대사 표시 갱신 순서를 고정한다. */
internal class ScenarioPlaybackFrameUpdater(
    /** `playback` (ScenarioInterpreter): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val playback: ScenarioInterpreter,
    /** `playbackController` (ScenarioPlaybackController): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val playbackController: ScenarioPlaybackController,
    /** `navigation` (ScenarioNavigationCoordinator): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val navigation: ScenarioNavigationCoordinator,
    /** `isVerificationRun` (() -> Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val isVerificationRun: () -> Boolean,
    /** `isStreetPresentation` (() -> Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val isStreetPresentation: () -> Boolean,
    /** `autoCloseSettingEnabled` (() -> Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val autoCloseSettingEnabled: () -> Boolean,
    /** `onAdvance` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val onAdvance: () -> Unit,
) {
    /** 화면이 열린 뒤 누적된 시간으로 trace 입력 간격을 제어한다. */
    var elapsed = 0f
        private set

    /** advanceClock: 프레임 경과 시간을 누적한다. */
    fun advanceClock(delta: Float) {
        elapsed += delta
    }

    /** updatePlayback: 스크립트 진행 뒤 자연 장면 이동과 완료 경로를 판정한다. */
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

    /** updatePresentation: 텍스트 공개·자동 닫기 같은 시각 상태만 갱신한다. */
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

    /**
     * `settleStreetFixture`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun settleStreetFixture() {
        if (!isStreetPresentation()) return
        var guard = 0
        while ((playback.state == PlaybackState.MODAL || playback.state == PlaybackState.DELAY) && guard++ < 1000) {
            if (playback.state == PlaybackState.MODAL) playback.resumeModal() else playback.skipDelay()
        }
        playback.stage.finishAnimations()
    }
}
