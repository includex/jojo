// Test
package com.jojo.game

import com.jojo.game.application.battle.NaturalBattleTransition

import com.jojo.game.domain.battle.*


import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** NaturalBattleTransitionTest: NaturalBattleTransition의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

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
        assertFalse(NaturalBattleTransition.terminalReady(
            PlaybackState.COMPLETE, callbackPending = false,
            scriptEnded = false, endProcessStarted = false,
        ))
        assertFalse(NaturalBattleTransition.terminalReady(
            PlaybackState.MODAL, callbackPending = false,
            scriptEnded = true, endProcessStarted = true,
        ))
        assertFalse(NaturalBattleTransition.terminalReady(
            PlaybackState.COMPLETE, callbackPending = true,
            scriptEnded = true, endProcessStarted = true,
        ))
        assertTrue(NaturalBattleTransition.terminalReady(
            PlaybackState.COMPLETE, callbackPending = false,
            scriptEnded = true, endProcessStarted = false,
        ))
        assertTrue(NaturalBattleTransition.terminalReady(
            PlaybackState.COMPLETE, callbackPending = false,
            scriptEnded = false, endProcessStarted = true,
        ))
    }

    @Test fun `campaign loss trace flushes only after stable Lose scene handoff`() {
        assertFalse(NaturalBattleTransition.campaignLossReadyToFlush(
            exitOnFinish = false, outcome = BattleOutcome.ENEMY_VICTORY, loseSceneActive = false,
            scriptState = PlaybackState.COMPLETE, callbackPending = false, scriptEnded = true,
        ))
        assertFalse(NaturalBattleTransition.campaignLossReadyToFlush(
            exitOnFinish = false, outcome = BattleOutcome.ENEMY_VICTORY, loseSceneActive = true,
            scriptState = PlaybackState.COMPLETE, callbackPending = true, scriptEnded = true,
        ))
        assertFalse(NaturalBattleTransition.campaignLossReadyToFlush(
            exitOnFinish = true, outcome = BattleOutcome.ENEMY_VICTORY, loseSceneActive = true,
            scriptState = PlaybackState.COMPLETE, callbackPending = false, scriptEnded = true,
        ))
        assertTrue(NaturalBattleTransition.campaignLossReadyToFlush(
            exitOnFinish = false, outcome = BattleOutcome.ENEMY_VICTORY, loseSceneActive = true,
            scriptState = PlaybackState.COMPLETE, callbackPending = false, scriptEnded = true,
        ))
    }
}
