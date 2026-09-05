package com.jojo.game.presentation.battle.ai

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.domain.battle.AiUnitResolution
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.domain.battle.turn.hasPhysicalCounterPass
import com.jojo.game.domain.scenario.PlaybackState

/**
 * Runs the visible `_ai2` callback chain.  BattleScreen supplies a narrow
 * presentation port; AI resolution and model commits therefore remain in the
 * domain while this type owns the frame-by-frame callback state.
 */
internal class AiPresentationCoordinator(
    portFactory: (AiPresentationCoordinator) -> Port,
) {
    private val state = AiPresentationState()
    private val port = portFactory(this)

    internal val activeCamp: Faction? get() = state.activeCamp
    internal val resolution: AiUnitResolution? get() = state.resolution
    internal val stage: AiPresentationStage get() = state.stage
    internal val unitDeathScriptPass: Int get() = state.unitDeathScriptPass
    internal val hasActiveCamp: Boolean get() = state.hasActiveCamp
    internal val playerMoveScriptStarted: Boolean get() = state.playerMoveScriptStarted

    internal interface Port {
        fun now(): Float
        fun resolve(camp: Faction): AiTurnResult
        fun lastResolution(): AiUnitResolution?
        fun hasPendingUnits(): Boolean
        fun focusFirstCampUnit(camp: Faction)
        fun beginEmptyCampBarrier(hasActor: Boolean)
        fun yieldEmptyCampEntryFrame(): Boolean
        fun beginActorBarriers(hasPhysicalCounter: Boolean)
        fun finishDeathCallbacks()
        fun focusTile(x: Float, y: Float)
        fun startMovement(resolution: AiUnitResolution): Boolean
        fun movementActive(): Boolean
        fun finishMovement(resolution: AiUnitResolution)
        fun commitMovement(resolution: AiUnitResolution, updateActionState: Boolean)
        fun markPlayerMove(resolution: AiUnitResolution)
        fun scriptState(): PlaybackState
        fun runScript(): PlaybackState
        fun battleEndedByScript(): Boolean
        fun playerMoveScriptFinished(): Boolean
        fun finishScriptEndedTurn()
        fun applyAction(resolution: AiUnitResolution)
        fun combatBusy(): Boolean
        fun yieldCounterattackIdle(): Boolean
        fun commitAction(actorId: String)
        fun yieldActionStatus(hasAction: Boolean): Boolean
        fun yieldPlayerMoveCompletion(isPlayer: Boolean, moved: Boolean): Boolean
        fun queuePostActionDeaths(): Boolean
        fun startedPostActionDeaths(): Boolean
        fun setSummary(camp: Faction, result: AiTurnResult)
        fun completeCamp(result: AiTurnResult)
        fun setActionMessage(camp: Faction, resolution: AiUnitResolution)
        fun beginNoResultFrameGate()
        fun yieldBeforeNextNoResult(nextIsNoResult: Boolean): Boolean
        fun markNoResultCompleted()
    }

    fun beginCamp(camp: Faction): AiTurnResult {
        state.beginCamp(camp)
        port.finishDeathCallbacks()
        state.stage = AiPresentationStage.COMPLETE
        if (port.hasPendingUnits()) port.focusFirstCampUnit(camp)
        val result = resolveNextActor()
        port.beginEmptyCampBarrier(state.resolution != null)
        return result
    }

    private fun resolveNextActor(): AiTurnResult {
        val camp = state.activeCamp ?: return AiTurnResult(0, 0, 0)
        val result = port.resolve(camp)
        state.add(result)
        state.beginActor(port.lastResolution())
        port.finishDeathCallbacks()
        val resolution = state.resolution
        port.beginActorBarriers(resolution?.result?.hasPhysicalCounterPass() == true)
        if (resolution != null) {
            state.stageStartedAt = port.now()
            port.setActionMessage(camp, resolution)
        }
        return result
    }

    fun drive() {
        if (port.scriptState() != PlaybackState.COMPLETE) return
        val camp = state.activeCamp ?: return
        if (state.resolution == null && port.yieldEmptyCampEntryFrame()) return
        port.beginNoResultFrameGate()
        while (true) {
            val resolution = state.resolution
            if (resolution == null) {
                if (port.hasPendingUnits()) {
                    resolveNextActor()
                    if (port.yieldBeforeNextNoResult(
                            state.resolution != null && state.resolution!!.path.size < 2 && state.resolution!!.result == null,
                        )
                    ) return
                    continue
                }
                val total = AiTurnResult(state.turnMoves, state.turnAttacks, state.turnHolds)
                port.setSummary(camp, total)
                state.unitDeathScriptPass = 0
                state.finishCamp()
                port.completeCamp(total)
                return
            }
            when (state.stage) {
                AiPresentationStage.FOCUS_DELAY -> {
                    if (port.now() - state.stageStartedAt < .3f) return
                    port.focusTile(resolution.fromX.toFloat(), resolution.fromY.toFloat())
                    val moving = port.startMovement(resolution)
                    if (!moving) port.commitMovement(resolution, updateActionState = resolution.result == null)
                    state.stage = when {
                        moving -> AiPresentationStage.MOVING
                        resolution.result is TacticalActionResult.Attack -> AiPresentationStage.ACTION_DELAY
                        resolution.result != null -> AiPresentationStage.ACTION
                        else -> AiPresentationStage.COMPLETE
                    }
                    state.stageStartedAt = port.now()
                }
                AiPresentationStage.MOVING -> {
                    if (port.movementActive()) return
                    port.finishMovement(resolution)
                    port.commitMovement(resolution, updateActionState = resolution.result == null)
                    port.markPlayerMove(resolution)
                    if (camp == Faction.PLAYER && !state.playerMoveScriptStarted) {
                        state.playerMoveScriptStarted = true
                        if (port.runScript() != PlaybackState.COMPLETE) return
                    }
                    if (camp == Faction.PLAYER && state.playerMoveScriptStarted && port.playerMoveScriptFinished()) {
                        finishScriptEndedTurn()
                        return
                    }
                    state.stage = when {
                        resolution.result is TacticalActionResult.Attack -> AiPresentationStage.ACTION_DELAY
                        resolution.result != null -> AiPresentationStage.ACTION
                        else -> AiPresentationStage.COMPLETE
                    }
                    state.stageStartedAt = port.now()
                }
                AiPresentationStage.ACTION_DELAY -> {
                    if (camp == Faction.PLAYER && !state.playerMoveScriptStarted) {
                        state.playerMoveScriptStarted = true
                    }
                    if (port.now() - state.stageStartedAt < .3f) return
                    state.stage = AiPresentationStage.ACTION
                }
                AiPresentationStage.ACTION -> {
                    if (!state.actionStarted) {
                        state.actionStarted = true
                        port.applyAction(resolution)
                        return
                    }
                    if (port.combatBusy()) return
                    if (!state.actionCommitted) {
                        if (port.yieldCounterattackIdle()) return
                        port.commitAction(resolution.actorId)
                        state.actionCommitted = true
                        state.stage = AiPresentationStage.COMPLETE
                        if (port.yieldActionStatus(resolution.result != null)) return
                    }
                    state.stage = AiPresentationStage.COMPLETE
                }
                AiPresentationStage.COMPLETE -> {
                    val noResult = resolution.path.size < 2 && resolution.result == null
                    if (camp == Faction.PLAYER && !state.playerMoveScriptStarted) {
                        state.playerMoveScriptStarted = true
                    }
                    if (!state.actionCommitted) {
                        port.commitAction(resolution.actorId)
                        state.actionCommitted = true
                    }
                    if (port.yieldPlayerMoveCompletion(
                            camp == Faction.PLAYER,
                            resolution.path.size >= 2 &&
                                (resolution.fromX != resolution.toX || resolution.fromY != resolution.toY),
                        )
                    ) return
                    if (port.scriptState() != PlaybackState.COMPLETE) return
                    if (state.unitDeathScriptPass == 0) {
                        state.unitDeathScriptPass = 1
                        if (port.runScript() != PlaybackState.COMPLETE) return
                    }
                    if (state.unitDeathScriptPass == 1 && port.battleEndedByScript()) {
                        finishScriptEndedTurn()
                        return
                    }
                    if (state.unitDeathScriptPass == 1 && !port.startedPostActionDeaths()) {
                        if (port.queuePostActionDeaths()) return
                        state.unitDeathScriptPass = 2
                    }
                    if (state.unitDeathScriptPass == 1 && port.combatBusy()) return
                    if (state.unitDeathScriptPass == 1) {
                        state.unitDeathScriptPass = 2
                        if (port.runScript() != PlaybackState.COMPLETE) return
                    }
                    state.clearActor()
                    port.finishDeathCallbacks()
                    if (noResult) port.markNoResultCompleted()
                }
            }
        }
    }

    private fun finishScriptEndedTurn() {
        state.clearActor()
        state.finishCamp()
        port.finishDeathCallbacks()
        port.finishScriptEndedTurn()
    }
}
