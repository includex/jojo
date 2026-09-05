package com.jojo.game.presentation.battle

import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleUnitMoveTimeline

import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.domain.battle.AiUnitResolution
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.turn.CollocatedPlayerMoveScriptEnd
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.presentation.battle.ai.AiPresentationCoordinator

/** BattleScreen adapter for the production-neutral AI presentation coordinator. */
internal class BattleAiPresentationPort(
    private val screen: BattleScreen,
    private val coordinator: AiPresentationCoordinator,
) : AiPresentationCoordinator.Port {
    override fun now() = screen.animationClock()
    override fun resolve(camp: Faction): AiTurnResult = screen.battle.ai.resolveTurn(maxUnits = 1, deferMutations = true)
    override fun lastResolution() = screen.battle.lastAiUnitResolution
    override fun hasPendingUnits() = screen.battle.presentation.hasPendingAiUnits()
    override fun focusFirstCampUnit(camp: Faction) { screen.focusFirstCampCameraUnit(camp) }
    override fun beginEmptyCampBarrier(hasActor: Boolean) { screen.emptyAiCampFrameBarrier.begin(hasActor) }
    override fun yieldEmptyCampEntryFrame() = screen.emptyAiCampFrameBarrier.yieldEntryFrame()
    override fun beginActorBarriers(hasPhysicalCounter: Boolean) {
        screen.committedPlayerMoveFrameBarrier.beginActor()
        screen.actionStatusFrameBarrier.beginActor()
        screen.counterattackSettlementFrameBarrier.beginActor(hasPhysicalCounter)
    }
    override fun finishDeathCallbacks() { screen.deathTimeline.finishPostActionCallbacks() }
    override fun focusTile(x: Float, y: Float) { screen.focusCameraOnTile(x, y) }
    override fun startMovement(resolution: AiUnitResolution): Boolean {
        screen.movementAnimation = resolution.path.takeIf { it.size >= 2 }?.let { path ->
            val actor = screen.battle.presentation.presentationUnit(resolution.actorId)
            UnitMoveAnimation(resolution.actorId, path, BattleUnitMoveTimeline.schedule(path, actor?.fastMove ?: true), screen.animationClock())
        }
        return screen.movementAnimation != null
    }
    override fun movementActive() = screen.movementAnimation?.let { screen.animationClock() < it.endsAt } == true
    override fun finishMovement(resolution: AiUnitResolution) {
        val direction = screen.movementAnimation?.timeline?.segments?.lastOrNull()?.direction
        screen.movementAnimation = null
        direction?.let { screen.battle.presentation.presentationUnit(resolution.actorId)?.direction = it }
    }
    override fun commitMovement(resolution: AiUnitResolution, updateActionState: Boolean) {
        screen.battle.pendingActionTransaction?.commitMovement(commitActionState = updateActionState)
    }
    override fun markPlayerMove(resolution: AiUnitResolution) {
        if (coordinator.activeCamp == Faction.PLAYER && resolution.path.size >= 2 && (resolution.fromX != resolution.toX || resolution.fromY != resolution.toY)) {
            screen.playerMoveCommitted = true
            val actor = screen.battle.presentation.presentationUnit(resolution.actorId)?.characterId ?: -1
            screen.committedPlayerMove = "$actor:${resolution.fromX},${resolution.fromY}->${resolution.toX},${resolution.toY}"
        }
    }
    override fun scriptState() = screen.scriptRuntime.state
    override fun runScript(): PlaybackState { screen.runBattleScript(); return screen.scriptRuntime.state }
    override fun battleEndedByScript() = screen.scriptRuntime.stage.battleEndedByScript
    override fun playerMoveScriptFinished() = CollocatedPlayerMoveScriptEnd.finishesAiTurn(
        coordinator.activeCamp ?: Faction.PLAYER, coordinator.playerMoveScriptStarted, screen.scriptRuntime.state,
        screen.scriptRuntime.stage.battleEndedByScript, screen.scriptRuntime.stage.scriptedBattleOutcome, screen.battle.outcome(),
    )
    override fun finishScriptEndedTurn() { screen.turnController.finishScriptEndedBattle() }
    override fun applyAction(resolution: AiUnitResolution) = screen.applyAction(
        result = requireNotNull(resolution.result), unitName = screen.battle.presentation.presentationUnit(resolution.actorId)?.name ?: resolution.actorId,
        actorId = resolution.actorId, magicId = resolution.magicId, targetId = resolution.targetId,
        healthBeforeAction = resolution.healthBeforeAction, continueBattleScript = false,
    )
    override fun combatBusy() = screen.combatPresentationBusy()
    override fun yieldCounterattackIdle() = screen.counterattackSettlementFrameBarrier.yieldIdleBeforeCommit()
    override fun commitAction(actorId: String) { screen.commitDeferredBattleAction(actorId) }
    override fun yieldActionStatus(hasAction: Boolean) = screen.actionStatusFrameBarrier.yieldAfterCommit(hasAction)
    override fun yieldPlayerMoveCompletion(isPlayer: Boolean, moved: Boolean) = screen.committedPlayerMoveFrameBarrier.yieldCompletionFrame(isPlayer, moved)
    override fun queuePostActionDeaths() = screen.deathTimeline.queuePostAction(screen.collectDyingPresentationUnits())
    override fun startedPostActionDeaths() = screen.deathTimeline.startedPostActionDeaths()
    override fun setSummary(camp: Faction, result: AiTurnResult) { screen.eventMessage = "${camp.presentationLabel()}: 이동 ${result.moves} · 공격 ${result.attacks} · 대기 ${result.holds}" }
    override fun completeCamp(result: AiTurnResult) { screen.turnController.completeAiPresentation(result) }
    override fun setActionMessage(camp: Faction, resolution: AiUnitResolution) { screen.movementAnimation = null; screen.eventMessage = "${camp.presentationLabel()}: ${screen.battle.presentation.presentationUnit(resolution.actorId)?.name ?: resolution.actorId} 행동" }
    override fun beginNoResultFrameGate() { screen.consecutiveNoResultFrameGate.beginRender() }
    override fun yieldBeforeNextNoResult(nextIsNoResult: Boolean) = screen.consecutiveNoResultFrameGate.shouldYieldBefore(nextIsNoResult)
    override fun markNoResultCompleted() { screen.consecutiveNoResultFrameGate.markCompleted() }
}

private fun Faction.presentationLabel(): String = when (this) {
    Faction.PLAYER -> "아군"
    Faction.FRIEND -> "우군"
    Faction.ENEMY -> "적군"
    Faction.REINFORCEMENTS -> "적 증원군"
}
