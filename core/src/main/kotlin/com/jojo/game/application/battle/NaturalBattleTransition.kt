// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.turn.BattleTurnPhase

import com.jojo.game.domain.scenario.*

/** NaturalBattleTransition: 전투 결과 뒤 보상·장면 전환을 판단하고, 표현 콜백 완료 전에는 진행을 보류한다. */
object NaturalBattleTransition {

    enum class CompletionAction { WAIT, RUN_SCENE1, START_SCENE2 }

    fun shouldStartInitialScene1(
        presentationReady: Boolean,
        outcome: BattleOutcome?,
        phase: BattleTurnPhase,
        scriptState: PlaybackState,
        alreadyStarted: Boolean,
    ): Boolean = !alreadyStarted && presentationReady && outcome == null &&
            phase == BattleTurnPhase.PLAYER_INPUT && scriptState == PlaybackState.COMPLETE

    fun completionAction(
        outcome: BattleOutcome?,
        presentationBusy: Boolean,
        scriptState: PlaybackState,
        rewardOpen: Boolean,
        endedByScript: Boolean,
        outcomeScriptStarted: Boolean,
    ): CompletionAction = when {
        outcome != BattleOutcome.PLAYER_VICTORY || presentationBusy -> CompletionAction.WAIT
        scriptState != PlaybackState.COMPLETE || rewardOpen -> CompletionAction.WAIT
        endedByScript -> CompletionAction.START_SCENE2
        !outcomeScriptStarted -> CompletionAction.RUN_SCENE1
        else -> CompletionAction.WAIT
    }

    fun resultScriptReadyForLoseScene(
        outcome: BattleOutcome?,
        scriptState: PlaybackState,
        dialoguePresent: Boolean,
    ): Boolean = outcome == BattleOutcome.ENEMY_VICTORY &&
            scriptState == PlaybackState.COMPLETE && !dialoguePresent
    fun resultScriptReadyForContinuation(
        scriptState: PlaybackState,
        callbackPending: Boolean,
    ): Boolean = scriptState == PlaybackState.COMPLETE && !callbackPending
    fun terminalReady(
        scriptState: PlaybackState,
        callbackPending: Boolean,
        scriptEnded: Boolean,
        endProcessStarted: Boolean,
    ): Boolean = scriptState == PlaybackState.COMPLETE && !callbackPending &&
            (scriptEnded || endProcessStarted)
    fun campaignLossReadyToFlush(
        exitOnFinish: Boolean,
        outcome: BattleOutcome?,
        loseSceneActive: Boolean,
        scriptState: PlaybackState,
        callbackPending: Boolean,
        scriptEnded: Boolean,
    ): Boolean = !exitOnFinish && outcome == BattleOutcome.ENEMY_VICTORY && loseSceneActive &&
            terminalReady(scriptState, callbackPending, scriptEnded, endProcessStarted = true)
}
