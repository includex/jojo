package com.jojo.game.presentation.scenario

import com.jojo.game.domain.scenario.ScenarioCompletionRoute
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.application.runtime.RuntimeScenarioOverlay

/** The terminal outcome of a render phase; callers must stop this frame for non-continuations. */
internal enum class ScenarioRenderPhaseResult {
    CONTINUE,
    ROUTED,
    CAPTURED,
}

/** Pure predicates that keep ScenarioScreen's ordered frame phases explicit. */
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
