// Battle
package com.jojo.game.presentation.battle.ai

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.domain.battle.AiUnitResolution
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.domain.battle.turn.hasPhysicalCounterPass
import com.jojo.game.domain.scenario.PlaybackState

/** AI 턴의 이동·공격 콜백과 프레임별 표시 상태를 조정합니다. */
internal class AiPresentationCoordinator(
    portFactory: (AiPresentationCoordinator) -> Port,
) {
    /**
     * `state` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val state = AiPresentationState()
    /**
     * `port` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val port = portFactory(this)

    /**
     * `activeCamp` (Faction? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    internal val activeCamp: Faction? get() = state.activeCamp
    /**
     * `resolution` (AiUnitResolution? get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    internal val resolution: AiUnitResolution? get() = state.resolution
    /**
     * `stage` (AiPresentationStage get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    internal val stage: AiPresentationStage get() = state.stage
    /**
     * `unitDeathScriptPass` (Int get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    internal val unitDeathScriptPass: Int get() = state.unitDeathScriptPass
    /**
     * `hasActiveCamp` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    internal val hasActiveCamp: Boolean get() = state.hasActiveCamp
    /**
     * `playerMoveScriptStarted` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    internal val playerMoveScriptStarted: Boolean get() = state.playerMoveScriptStarted

    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        /**
         * `now`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun now(): Float
        /**
         * `resolve`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun resolve(camp: Faction): AiTurnResult
        /**
         * `lastResolution`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun lastResolution(): AiUnitResolution?
        /**
         * `hasPendingUnits`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun hasPendingUnits(): Boolean
        /**
         * `focusFirstCampUnit`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun focusFirstCampUnit(camp: Faction)
        /**
         * `beginEmptyCampBarrier`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun beginEmptyCampBarrier(hasActor: Boolean)
        /**
         * `yieldEmptyCampEntryFrame`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun yieldEmptyCampEntryFrame(): Boolean
        /**
         * `beginActorBarriers`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun beginActorBarriers(hasPhysicalCounter: Boolean)
        /**
         * `finishDeathCallbacks`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun finishDeathCallbacks()
        /**
         * `focusTile`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun focusTile(x: Float, y: Float)
        /**
         * `startMovement`: 흐름을 실행하거나 다음 단계로 전달한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun startMovement(resolution: AiUnitResolution): Boolean
        /**
         * `movementActive`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun movementActive(): Boolean
        /**
         * `finishMovement`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun finishMovement(resolution: AiUnitResolution)
        /**
         * `commitMovement`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun commitMovement(resolution: AiUnitResolution, updateActionState: Boolean)
        /**
         * `markPlayerMove`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun markPlayerMove(resolution: AiUnitResolution)
        /**
         * `scriptState`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun scriptState(): PlaybackState
        /**
         * `runScript`: 흐름을 실행하거나 다음 단계로 전달한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun runScript(): PlaybackState
        /**
         * `battleEndedByScript`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun battleEndedByScript(): Boolean
        /**
         * `playerMoveScriptFinished`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun playerMoveScriptFinished(): Boolean
        /**
         * `finishScriptEndedTurn`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun finishScriptEndedTurn()
        /**
         * `applyAction`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun applyAction(resolution: AiUnitResolution)
        /**
         * `combatBusy`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun combatBusy(): Boolean
        /**
         * `yieldCounterattackIdle`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun yieldCounterattackIdle(): Boolean
        /**
         * `commitAction`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun commitAction(actorId: String)
        /**
         * `yieldActionStatus`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun yieldActionStatus(hasAction: Boolean): Boolean
        /**
         * `yieldPlayerMoveCompletion`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun yieldPlayerMoveCompletion(isPlayer: Boolean, moved: Boolean): Boolean
        /**
         * `queuePostActionDeaths`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun queuePostActionDeaths(): Boolean
        /**
         * `startedPostActionDeaths`: 흐름을 실행하거나 다음 단계로 전달한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun startedPostActionDeaths(): Boolean
        /**
         * `setSummary`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun setSummary(camp: Faction, result: AiTurnResult)
        /**
         * `completeCamp`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun completeCamp(result: AiTurnResult)
        /**
         * `setActionMessage`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun setActionMessage(camp: Faction, resolution: AiUnitResolution)
        /**
         * `beginNoResultFrameGate`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun beginNoResultFrameGate()
        /**
         * `yieldBeforeNextNoResult`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun yieldBeforeNextNoResult(nextIsNoResult: Boolean): Boolean
        /**
         * `markNoResultCompleted`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun markNoResultCompleted()
    }

    /** 지정한 진영의 AI 턴을 시작합니다. */
    fun beginCamp(camp: Faction): AiTurnResult {
        state.beginCamp(camp)
        port.finishDeathCallbacks()
        state.stage = AiPresentationStage.COMPLETE
        if (port.hasPendingUnits()) port.focusFirstCampUnit(camp)
        val result = resolveNextActor()
        port.beginEmptyCampBarrier(state.resolution != null)
        return result
    }

    /**
     * `resolveNextActor`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /** 현재 AI 턴을 한 프레임 진행합니다. */
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

    /**
     * `finishScriptEndedTurn`: 조건과 입력 상태를 검증한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun finishScriptEndedTurn() {
        state.clearActor()
        state.finishCamp()
        port.finishDeathCallbacks()
        port.finishScriptEndedTurn()
    }
}
