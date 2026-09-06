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
    /**
     * `activeCamp` (Faction?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var activeCamp: Faction? = null
        private set
    /**
     * `resolution` (AiUnitResolution?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var resolution: AiUnitResolution? = null
        private set
    /**
     * `stage` (AiPresentationStage): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var stage: AiPresentationStage = AiPresentationStage.COMPLETE
    /**
     * `stageStartedAt` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var stageStartedAt: Float = 0f
    /**
     * `actionStarted` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var actionStarted: Boolean = false
    /**
     * `actionCommitted` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var actionCommitted: Boolean = false
    /**
     * `playerMoveScriptStarted` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var playerMoveScriptStarted: Boolean = false
    /**
     * `unitDeathScriptPass` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var unitDeathScriptPass: Int = 0
    /**
     * `turnMoves` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var turnMoves: Int = 0
        private set
    /**
     * `turnAttacks` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var turnAttacks: Int = 0
        private set
    /**
     * `turnHolds` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var turnHolds: Int = 0
        private set

    /**
     * `hasActiveCamp` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

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
