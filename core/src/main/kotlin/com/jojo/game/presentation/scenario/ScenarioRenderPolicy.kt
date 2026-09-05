package com.jojo.game.presentation.scenario

import com.jojo.game.domain.scenario.ScenarioCompletionRoute
import com.jojo.game.domain.scenario.PlaybackState

/** The terminal outcome of a render phase; callers must stop this frame for non-continuations. */
internal enum class ScenarioRenderPhaseResult {
    CONTINUE,
    ROUTED,
    CAPTURED,
}

/** Pure predicates that keep ScenarioScreen's ordered frame phases explicit. */
internal object ScenarioRenderPolicy {
    private val isolatedHallOverlays = setOf(
        "info",
        "get-item-equipment",
        "get-item-property",
        "item-equipment",
        "item-property",
        "item-discard-confirm",
        "map-info",
        "choice",
        "ambition",
        "ask",
        "command",
        "menu",
        "save",
        "save-confirm",
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

    fun isStandaloneHallOverlay(overlay: String?): Boolean = overlay in isolatedHallOverlays

    fun autoCloseEnabled(externalRuntimeOpen: Boolean, autoCloseSettingEnabled: Boolean): Boolean =
        !externalRuntimeOpen && autoCloseSettingEnabled
}
