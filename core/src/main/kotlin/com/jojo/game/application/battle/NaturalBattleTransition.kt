// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.*


import com.jojo.game.domain.scenario.*

/** NaturalBattleTransition: 전투 결과 뒤 보상·장면 전환을 판단하고, 표현 콜백 완료 전에는 진행을 보류한다. */
object NaturalBattleTransition {

    /**
     * `CompletionAction` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    enum class CompletionAction { WAIT, RUN_SCENE1, START_SCENE2 }

    /**
     * `completionAction`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `resultScriptReadyForLoseScene`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun resultScriptReadyForLoseScene(
        outcome: BattleOutcome?,
        scriptState: PlaybackState,
        dialoguePresent: Boolean,
    ): Boolean = outcome == BattleOutcome.ENEMY_VICTORY &&
            scriptState == PlaybackState.COMPLETE && !dialoguePresent
    /**
     * `terminalReady`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun terminalReady(
        scriptState: PlaybackState,
        callbackPending: Boolean,
        scriptEnded: Boolean,
        endProcessStarted: Boolean,
    ): Boolean = scriptState == PlaybackState.COMPLETE && !callbackPending &&
            (scriptEnded || endProcessStarted)
    /**
     * `campaignLossReadyToFlush`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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
