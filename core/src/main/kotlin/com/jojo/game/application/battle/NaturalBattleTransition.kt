package com.jojo.game.application.battle

import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.turn.BattleTurnPhase

import com.jojo.game.domain.scenario.*

/** Decisions shared by the normal BattleScreen route and its end-to-end transition test. */
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

    /**
     * A result may be observed while BattleScreen is still waiting for a
     * presentation callback. The next script pass must not open a SayLayer
     * until that callback has released its previous layer.
     */
    fun resultScriptReadyForContinuation(
        scriptState: PlaybackState,
        callbackPending: Boolean,
    ): Boolean = scriptState == PlaybackState.COMPLETE && !callbackPending

    /**
     * A tactical outcome begins result handling; it is not itself the source
     * `stage.end` callback. Full traces may finish once that callback has run,
     * or once BattleScreen's source-equivalent end-process scene is active.
     */
    fun terminalReady(
        scriptState: PlaybackState,
        callbackPending: Boolean,
        scriptEnded: Boolean,
        endProcessStarted: Boolean,
    ): Boolean = scriptState == PlaybackState.COMPLETE && !callbackPending &&
            (scriptEnded || endProcessStarted)

    /**
     * Campaign traces normally flush from the victory save prompt.  A loss
     * instead transfers ownership to Lose.scene, whose delayed prompt must
     * remain live even after the trace has been written.
     */
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
