package com.jojo.game

import com.jojo.game.domain.scenario.*

/** Decisions shared by the normal BattleScreen route and its end-to-end transition test. */
object NaturalBattleTransition {
    /**
     * enum class  `CompletionAction`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class CompletionAction { WAIT, RUN_SCENE1, START_SCENE2 }

    fun shouldStartInitialScene1(
        presentationReady: Boolean,
        outcome: BattleOutcome?,
        phase: BattleTurnController.Phase,
        scriptState: PlaybackState,
        alreadyStarted: Boolean,
    ): Boolean = !alreadyStarted && presentationReady && outcome == null &&
            phase == BattleTurnController.Phase.PLAYER_INPUT && scriptState == PlaybackState.COMPLETE

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
    fun fullTraceTerminalReady(
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
    fun campaignLossTraceReadyToFlush(
        exitOnFinish: Boolean,
        outcome: BattleOutcome?,
        loseSceneActive: Boolean,
        scriptState: PlaybackState,
        callbackPending: Boolean,
        scriptEnded: Boolean,
    ): Boolean = !exitOnFinish && outcome == BattleOutcome.ENEMY_VICTORY && loseSceneActive &&
            fullTraceTerminalReady(scriptState, callbackPending, scriptEnded, endProcessStarted = true)
}
