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
    private val isolatedHallOverlayFixtures = setOf(
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

    fun shouldInstallHallFixture(
        externalState: String?,
        externalPresentationActive: Boolean,
        hallOverlayFixture: String?,
    ): Boolean = !externalPresentationActive && (externalState in setOf(
        "hall-fixture",
    ) || hallOverlayFixture != null)

    fun shouldContinueNaturally(
        isVerificationRun: Boolean,
        hasFrameCaptureRequest: Boolean,
        playbackState: PlaybackState,
        naturalSceneIndex: Int,
        menuVisible: Boolean,
        battleEndedByScript: Boolean,
        sceneJumpTarget: Int?,
    ): Boolean = !isVerificationRun && !hasFrameCaptureRequest &&
        playbackState == PlaybackState.COMPLETE && (naturalSceneIndex == 0 || !menuVisible) &&
        !battleEndedByScript && sceneJumpTarget == null

    fun shouldRouteAfterCompletion(
        isVerificationRun: Boolean,
        hasFrameCaptureRequest: Boolean,
        playbackState: PlaybackState,
        menuVisible: Boolean,
        battleEndedByScript: Boolean,
        sceneJumpTarget: Int?,
    ): Boolean = !isVerificationRun && !hasFrameCaptureRequest &&
        ScenarioCompletionRoute.shouldRoute(
            playbackState,
            menuVisible,
            battleEndedByScript,
            sceneJumpTarget,
        )

    fun isIsolatedHallOverlay(fixture: String?): Boolean = fixture in isolatedHallOverlayFixtures

    fun autoCloseEnabled(
        isVerificationRun: Boolean,
        hasFrameCaptureRequest: Boolean,
        hasRenderEventLogRequest: Boolean,
        autoCloseSettingEnabled: Boolean,
    ): Boolean = !isVerificationRun && !hasFrameCaptureRequest &&
        !hasRenderEventLogRequest && autoCloseSettingEnabled
}
