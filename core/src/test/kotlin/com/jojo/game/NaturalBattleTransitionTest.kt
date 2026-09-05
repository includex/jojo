package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NaturalBattleTransitionTest {
    @Test fun `loss dialogue completes before Lose scene takes input ownership`() {
        assertFalse(NaturalBattleTransition.resultScriptReadyForLoseScene(
            BattleOutcome.ENEMY_VICTORY, PlaybackState.DIALOGUE, dialoguePresent = true,
        ))
        assertFalse(NaturalBattleTransition.resultScriptReadyForLoseScene(
            BattleOutcome.ENEMY_VICTORY, PlaybackState.MODAL, dialoguePresent = false,
        ))
        assertTrue(NaturalBattleTransition.resultScriptReadyForLoseScene(
            BattleOutcome.ENEMY_VICTORY, PlaybackState.COMPLETE, dialoguePresent = false,
        ))
    }

    @Test fun `result script waits for the presentation callback that owns the previous layer`() {
        assertFalse(NaturalBattleTransition.resultScriptReadyForContinuation(
            PlaybackState.DIALOGUE, callbackPending = false,
        ))
        assertFalse(NaturalBattleTransition.resultScriptReadyForContinuation(
            PlaybackState.COMPLETE, callbackPending = true,
        ))
        assertTrue(NaturalBattleTransition.resultScriptReadyForContinuation(
            PlaybackState.COMPLETE, callbackPending = false,
        ))
    }

    @Test fun `full trace waits for source result completion rather than tactical outcome alone`() {
        assertFalse(NaturalBattleTransition.fullTraceTerminalReady(
            PlaybackState.COMPLETE, callbackPending = false,
            scriptEnded = false, endProcessStarted = false,
        ))
        assertFalse(NaturalBattleTransition.fullTraceTerminalReady(
            PlaybackState.MODAL, callbackPending = false,
            scriptEnded = true, endProcessStarted = true,
        ))
        assertFalse(NaturalBattleTransition.fullTraceTerminalReady(
            PlaybackState.COMPLETE, callbackPending = true,
            scriptEnded = true, endProcessStarted = true,
        ))
        assertTrue(NaturalBattleTransition.fullTraceTerminalReady(
            PlaybackState.COMPLETE, callbackPending = false,
            scriptEnded = true, endProcessStarted = false,
        ))
        assertTrue(NaturalBattleTransition.fullTraceTerminalReady(
            PlaybackState.COMPLETE, callbackPending = false,
            scriptEnded = false, endProcessStarted = true,
        ))
    }

    @Test fun `campaign loss trace flushes only after stable Lose scene handoff`() {
        assertFalse(NaturalBattleTransition.campaignLossTraceReadyToFlush(
            exitOnFinish = false, outcome = BattleOutcome.ENEMY_VICTORY, loseSceneActive = false,
            scriptState = PlaybackState.COMPLETE, callbackPending = false, scriptEnded = true,
        ))
        assertFalse(NaturalBattleTransition.campaignLossTraceReadyToFlush(
            exitOnFinish = false, outcome = BattleOutcome.ENEMY_VICTORY, loseSceneActive = true,
            scriptState = PlaybackState.COMPLETE, callbackPending = true, scriptEnded = true,
        ))
        assertFalse(NaturalBattleTransition.campaignLossTraceReadyToFlush(
            exitOnFinish = true, outcome = BattleOutcome.ENEMY_VICTORY, loseSceneActive = true,
            scriptState = PlaybackState.COMPLETE, callbackPending = false, scriptEnded = true,
        ))
        assertTrue(NaturalBattleTransition.campaignLossTraceReadyToFlush(
            exitOnFinish = false, outcome = BattleOutcome.ENEMY_VICTORY, loseSceneActive = true,
            scriptState = PlaybackState.COMPLETE, callbackPending = false, scriptEnded = true,
        ))
    }
}
