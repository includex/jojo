package com.jojo.game.presentation.scenario

import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScenarioRenderPolicyTest {
    @Test fun `natural continuation requires a completed non-terminal scene`() {
        assertTrue(
            ScenarioRenderPolicy.shouldContinueNaturally(
                isVerificationRun = false,
                hasFrameCaptureRequest = false,
                playbackState = PlaybackState.COMPLETE,
                naturalSceneIndex = 1,
                menuVisible = false,
                battleEndedByScript = false,
                sceneJumpTarget = null,
            ),
        )
        assertFalse(
            ScenarioRenderPolicy.shouldContinueNaturally(
                isVerificationRun = false,
                hasFrameCaptureRequest = false,
                playbackState = PlaybackState.COMPLETE,
                naturalSceneIndex = 1,
                menuVisible = true,
                battleEndedByScript = false,
                sceneJumpTarget = null,
            ),
        )
    }

    @Test fun `capture and verification suppress automatic presentation actions`() {
        assertFalse(
            ScenarioRenderPolicy.autoCloseEnabled(
                isVerificationRun = false,
                hasFrameCaptureRequest = true,
                hasRenderEventLogRequest = false,
                autoCloseSettingEnabled = true,
            ),
        )
        assertFalse(
            ScenarioRenderPolicy.autoCloseEnabled(
                isVerificationRun = true,
                hasFrameCaptureRequest = false,
                hasRenderEventLogRequest = false,
                autoCloseSettingEnabled = true,
            ),
        )
        assertTrue(
            ScenarioRenderPolicy.autoCloseEnabled(
                isVerificationRun = false,
                hasFrameCaptureRequest = false,
                hasRenderEventLogRequest = false,
                autoCloseSettingEnabled = true,
            ),
        )
    }

    @Test fun `only component fixtures suppress battlefield actors`() {
        assertTrue(ScenarioRenderPolicy.isIsolatedHallOverlay("save-confirm"))
        assertFalse(ScenarioRenderPolicy.isIsolatedHallOverlay("equip"))
        assertFalse(ScenarioRenderPolicy.isIsolatedHallOverlay(null))
    }
}
