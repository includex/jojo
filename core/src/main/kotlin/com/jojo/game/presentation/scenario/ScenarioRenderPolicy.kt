// Scenario
package com.jojo.game.presentation.scenario

import com.jojo.game.domain.scenario.ScenarioCompletionRoute
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.application.runtime.RuntimeScenarioOverlay

/** ScenarioRenderPhaseResult: 시나리오 렌더링 단계 결과이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal enum class ScenarioRenderPhaseResult {
    CONTINUE,
    ROUTED,
    CAPTURED,
}

/** ScenarioRenderPolicy: 시나리오 재생 상태와 오버레이 점유 여부로 현재 프레임에서 그릴 장면 단계를 결정한다. */
internal object ScenarioRenderPolicy {
    private val isolatedHallOverlays = setOf(
        RuntimeScenarioOverlay.INFO, RuntimeScenarioOverlay.GET_ITEM_EQUIPMENT,
        RuntimeScenarioOverlay.GET_ITEM_PROPERTY, RuntimeScenarioOverlay.ITEM_EQUIPMENT,
        RuntimeScenarioOverlay.ITEM_PROPERTY, RuntimeScenarioOverlay.ITEM_DISCARD_CONFIRM,
        RuntimeScenarioOverlay.MAP_INFO, RuntimeScenarioOverlay.CHOICE, RuntimeScenarioOverlay.AMBITION,
        RuntimeScenarioOverlay.ASK, RuntimeScenarioOverlay.COMMAND, RuntimeScenarioOverlay.MENU,
        RuntimeScenarioOverlay.SAVE, RuntimeScenarioOverlay.SAVE_CONFIRM,
    )

    fun shouldContinueNaturally(
        externalRuntimeOpen: Boolean,
        playbackState: PlaybackState,
        naturalSceneIndex: Int,
        menuVisible: Boolean,
        battleEndedByScript: Boolean,
        sceneJumpTarget: Int?,
    ): Boolean = !externalRuntimeOpen &&
        playbackState == PlaybackState.COMPLETE && (naturalSceneIndex == 0 || !menuVisible) &&
        !battleEndedByScript && sceneJumpTarget == null

    fun shouldRouteAfterCompletion(
        externalRuntimeOpen: Boolean,
        playbackState: PlaybackState,
        menuVisible: Boolean,
        battleEndedByScript: Boolean,
        sceneJumpTarget: Int?,
    ): Boolean = !externalRuntimeOpen &&
        ScenarioCompletionRoute.shouldRoute(
            playbackState,
            menuVisible,
            battleEndedByScript,
            sceneJumpTarget,
        )

    fun isStandaloneHallOverlay(overlay: RuntimeScenarioOverlay?): Boolean = overlay in isolatedHallOverlays

    fun autoCloseEnabled(externalRuntimeOpen: Boolean, autoCloseSettingEnabled: Boolean): Boolean =
        !externalRuntimeOpen && autoCloseSettingEnabled
}
