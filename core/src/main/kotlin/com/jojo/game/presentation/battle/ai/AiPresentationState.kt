// Battle
package com.jojo.game.presentation.battle.ai

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.domain.battle.AiUnitResolution
import com.jojo.game.domain.battle.Faction

/** 한 AI 유닛의 화면 콜백 진행 단계입니다. */
internal enum class AiPresentationStage {
    FOCUS_DELAY,
    MOVING,
    ACTION_DELAY,
    ACTION,
    COMPLETE,
}

/** 전투 화면 밖에서 유지하는 AI 표시 이어하기 상태입니다. */
internal class AiPresentationState {
    var activeCamp: Faction? = null
        private set
    var resolution: AiUnitResolution? = null
        private set
    var stage: AiPresentationStage = AiPresentationStage.COMPLETE
    var stageStartedAt: Float = 0f
    var actionStarted: Boolean = false
    var actionCommitted: Boolean = false
    var playerMoveScriptStarted: Boolean = false
    var unitDeathScriptPass: Int = 0
    var turnMoves: Int = 0
        private set
    var turnAttacks: Int = 0
        private set
    var turnHolds: Int = 0
        private set

    val hasActiveCamp: Boolean get() = activeCamp != null

    /** 진영의 AI 턴을 초기화합니다. */
    fun beginCamp(camp: Faction) {
        activeCamp = camp
        resolution = null
        stage = AiPresentationStage.COMPLETE
        actionStarted = false
        actionCommitted = false
        playerMoveScriptStarted = false
        unitDeathScriptPass = 0
        turnMoves = 0
        turnAttacks = 0
        turnHolds = 0
    }

    /** 다음 AI 유닛의 행동 상태를 시작합니다. */
    fun beginActor(next: AiUnitResolution?) {
        resolution = next
        actionStarted = false
        actionCommitted = false
        playerMoveScriptStarted = false
        stage = if (next == null || (next.path.size < 2 && next.result == null)) {
            AiPresentationStage.COMPLETE
        } else {
            AiPresentationStage.FOCUS_DELAY
        }
    }

    /** AI 턴 누적 결과에 행동 수를 더합니다. */
    fun add(result: AiTurnResult) {
        turnMoves += result.moves
        turnAttacks += result.attacks
        turnHolds += result.holds
    }

    /** 현재 유닛 행동 상태를 초기화합니다. */
    fun clearActor() {
        resolution = null
        actionStarted = false
        actionCommitted = false
        playerMoveScriptStarted = false
        stage = AiPresentationStage.COMPLETE
    }

    /** 진영 AI 턴을 종료하고 잔여 상태를 비웁니다. */
    fun finishCamp() {
        activeCamp = null
        resolution = null
        unitDeathScriptPass = 0
        clearActor()
    }
}
