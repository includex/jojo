package com.jojo.game.presentation.scenario

import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.application.runtime.RuntimeScenarioOverlay
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScenarioRenderPolicyTest {
    @Test fun `natural continuation requires a completed non-terminal scene`() {
        assertTrue(
            ScenarioRenderPolicy.shouldContinueNaturally(
                externalRuntimeOpen = false,
                playbackState = PlaybackState.COMPLETE,
                naturalSceneIndex = 1,
                menuVisible = false,
                battleEndedByScript = false,
                sceneJumpTarget = null,
            ),
        )
        assertFalse(
            ScenarioRenderPolicy.shouldContinueNaturally(
                externalRuntimeOpen = false,
                playbackState = PlaybackState.COMPLETE,
                naturalSceneIndex = 1,
                menuVisible = true,
                battleEndedByScript = false,
                sceneJumpTarget = null,
            ),
        )
    }

    @Test fun `external runtime suppresses automatic presentation actions`() {
        assertFalse(
            ScenarioRenderPolicy.autoCloseEnabled(
                externalRuntimeOpen = true,
                autoCloseSettingEnabled = true,
            ),
        )
        assertFalse(
            ScenarioRenderPolicy.autoCloseEnabled(
                externalRuntimeOpen = true,
                autoCloseSettingEnabled = true,
            ),
        )
        assertTrue(
            ScenarioRenderPolicy.autoCloseEnabled(
                externalRuntimeOpen = false,
                autoCloseSettingEnabled = true,
            ),
        )
    }

    @Test fun `only component overlays suppress battlefield actors`() {
        assertTrue(ScenarioRenderPolicy.isStandaloneHallOverlay(RuntimeScenarioOverlay.SAVE_CONFIRM))
        assertFalse(ScenarioRenderPolicy.isStandaloneHallOverlay(RuntimeScenarioOverlay.EQUIP))
        assertFalse(ScenarioRenderPolicy.isStandaloneHallOverlay(null))
    }
}
