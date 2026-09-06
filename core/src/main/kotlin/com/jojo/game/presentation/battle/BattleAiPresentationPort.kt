// Battle
package com.jojo.game.presentation.battle

import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleUnitMoveTimeline

import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.domain.battle.AiUnitResolution
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.turn.CollocatedPlayerMoveScriptEnd
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.battle.timeline.UnitMoveAnimation
import com.jojo.game.presentation.battle.ai.AiPresentationCoordinator
/** 화면 전투 상태와 AI 연출 조정기를 연결하는 어댑터이다. */
internal class BattleAiPresentationPort(
    /** `screen` (BattleScreen): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val screen: BattleScreen,
    /** `coordinator` (AiPresentationCoordinator): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val coordinator: AiPresentationCoordinator,
) : AiPresentationCoordinator.Port {
    /**
     * `now`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun now() = screen.animationClock()
    /**
     * `resolve`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun resolve(camp: Faction): AiTurnResult = screen.battle.ai.resolveTurn(maxUnits = 1, deferMutations = true)
    /**
     * `lastResolution`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun lastResolution() = screen.battle.lastAiUnitResolution
    /**
     * `hasPendingUnits`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun hasPendingUnits() = screen.battle.presentation.hasPendingAiUnits()
    /**
     * `focusFirstCampUnit`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun focusFirstCampUnit(camp: Faction) { screen.focusFirstCampCameraUnit(camp) }
    /**
     * `beginEmptyCampBarrier`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun beginEmptyCampBarrier(hasActor: Boolean) { screen.emptyAiCampFrameBarrier.begin(hasActor) }
    /**
     * `yieldEmptyCampEntryFrame`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun yieldEmptyCampEntryFrame() = screen.emptyAiCampFrameBarrier.yieldEntryFrame()
    /**
     * `beginActorBarriers`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun beginActorBarriers(hasPhysicalCounter: Boolean) {
        screen.committedPlayerMoveFrameBarrier.beginActor()
        screen.actionStatusFrameBarrier.beginActor()
        screen.counterattackSettlementFrameBarrier.beginActor(hasPhysicalCounter)
    }
    /**
     * `finishDeathCallbacks`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun finishDeathCallbacks() { screen.deathTimeline.finishPostActionCallbacks() }
    /**
     * `focusTile`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun focusTile(x: Float, y: Float) { screen.focusCameraOnTile(x, y) }
    /**
     * `startMovement`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun startMovement(resolution: AiUnitResolution): Boolean {
        screen.movementAnimation = resolution.path.takeIf { it.size >= 2 }?.let { path ->
            val actor = screen.battle.presentation.presentationUnit(resolution.actorId)
            UnitMoveAnimation(resolution.actorId, path, BattleUnitMoveTimeline.schedule(path, actor?.fastMove ?: true), screen.animationClock())
        }
        return screen.movementAnimation != null
    }
    /**
     * `movementActive`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun movementActive() = screen.movementAnimation?.let { screen.animationClock() < it.endsAt } == true
    /**
     * `finishMovement`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun finishMovement(resolution: AiUnitResolution) {
        val direction = screen.movementAnimation?.timeline?.segments?.lastOrNull()?.direction
        screen.movementAnimation = null
        direction?.let { screen.battle.presentation.presentationUnit(resolution.actorId)?.direction = it }
    }
    /**
     * `commitMovement`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun commitMovement(resolution: AiUnitResolution, updateActionState: Boolean) {
        screen.battle.pendingActionTransaction?.commitMovement(commitActionState = updateActionState)
    }
    /**
     * `markPlayerMove`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun markPlayerMove(resolution: AiUnitResolution) {
        if (coordinator.activeCamp == Faction.PLAYER && resolution.path.size >= 2 && (resolution.fromX != resolution.toX || resolution.fromY != resolution.toY)) {
            screen.playerMoveCommitted = true
            val actor = screen.battle.presentation.presentationUnit(resolution.actorId)?.characterId ?: -1
            screen.committedPlayerMove = "$actor:${resolution.fromX},${resolution.fromY}->${resolution.toX},${resolution.toY}"
        }
    }
    /**
     * `scriptState`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun scriptState() = screen.scriptRuntime.state
    /**
     * `runScript`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun runScript(): PlaybackState { screen.runBattleScript(); return screen.scriptRuntime.state }
    /**
     * `battleEndedByScript`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun battleEndedByScript() = screen.scriptRuntime.stage.battleEndedByScript
    /**
     * `playerMoveScriptFinished`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun playerMoveScriptFinished() = CollocatedPlayerMoveScriptEnd.finishesAiTurn(
        coordinator.activeCamp ?: Faction.PLAYER, coordinator.playerMoveScriptStarted, screen.scriptRuntime.state,
        screen.scriptRuntime.stage.battleEndedByScript, screen.scriptRuntime.stage.scriptedBattleOutcome, screen.battle.outcome(),
    )
    /**
     * `finishScriptEndedTurn`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun finishScriptEndedTurn() { screen.turnController.finishScriptEndedBattle() }
    /**
     * `applyAction`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun applyAction(resolution: AiUnitResolution) = screen.applyAction(
        result = requireNotNull(resolution.result), unitName = screen.battle.presentation.presentationUnit(resolution.actorId)?.name ?: resolution.actorId,
        actorId = resolution.actorId, magicId = resolution.magicId, targetId = resolution.targetId,
        healthBeforeAction = resolution.healthBeforeAction, continueBattleScript = false,
    )
    /**
     * `combatBusy`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun combatBusy() = screen.combatPresentationBusy()
    /**
     * `yieldCounterattackIdle`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun yieldCounterattackIdle() = screen.counterattackSettlementFrameBarrier.yieldIdleBeforeCommit()
    /**
     * `commitAction`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun commitAction(actorId: String) { screen.commitDeferredBattleAction(actorId) }
    /**
     * `yieldActionStatus`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun yieldActionStatus(hasAction: Boolean) = screen.actionStatusFrameBarrier.yieldAfterCommit(hasAction)
    /**
     * `yieldPlayerMoveCompletion`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun yieldPlayerMoveCompletion(isPlayer: Boolean, moved: Boolean) = screen.committedPlayerMoveFrameBarrier.yieldCompletionFrame(isPlayer, moved)
    /**
     * `queuePostActionDeaths`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun queuePostActionDeaths() = screen.deathTimeline.queuePostAction(screen.collectDyingPresentationUnits())
    /**
     * `startedPostActionDeaths`: 흐름을 실행하거나 다음 단계로 전달한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun startedPostActionDeaths() = screen.deathTimeline.startedPostActionDeaths()
    /**
     * `setSummary`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun setSummary(camp: Faction, result: AiTurnResult) { screen.eventMessage = "${camp.presentationLabel()}: 이동 ${result.moves} · 공격 ${result.attacks} · 대기 ${result.holds}" }
    /**
     * `completeCamp`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun completeCamp(result: AiTurnResult) { screen.turnController.completeAiPresentation(result) }
    /**
     * `setActionMessage`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun setActionMessage(camp: Faction, resolution: AiUnitResolution) { screen.movementAnimation = null; screen.eventMessage = "${camp.presentationLabel()}: ${screen.battle.presentation.presentationUnit(resolution.actorId)?.name ?: resolution.actorId} 행동" }
    /**
     * `beginNoResultFrameGate`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun beginNoResultFrameGate() { screen.consecutiveNoResultFrameGate.beginRender() }
    /**
     * `yieldBeforeNextNoResult`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun yieldBeforeNextNoResult(nextIsNoResult: Boolean) = screen.consecutiveNoResultFrameGate.shouldYieldBefore(nextIsNoResult)
    /**
     * `markNoResultCompleted`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun markNoResultCompleted() { screen.consecutiveNoResultFrameGate.markCompleted() }
}

/**
 * `Faction`: 타입의 핵심 동작을 수행한다.
 * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
 */

private fun Faction.presentationLabel(): String = when (this) {
    Faction.PLAYER -> "아군"
    Faction.FRIEND -> "우군"
    Faction.ENEMY -> "적군"
    Faction.REINFORCEMENTS -> "적 증원군"
}
