package com.jojo.game.presentation.battle.ai

import com.jojo.game.AiTurnResult
import com.jojo.game.AiUnitResolution
import com.jojo.game.domain.battle.Faction

/** Visible callback stages for one actor in the source `_ai2` coroutine. */
internal enum class AiPresentationStage {
    FOCUS_DELAY,
    MOVING,
    ACTION_DELAY,
    ACTION,
    COMPLETE,
}

/**
 * Mutable continuation state kept outside BattleScreen.  This is deliberately
 * presentation-only: domain resolution remains behind [AiPresentationCoordinator.Port].
 */
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

    fun add(result: AiTurnResult) {
        turnMoves += result.moves
        turnAttacks += result.attacks
        turnHolds += result.holds
    }

    fun clearActor() {
        resolution = null
        actionStarted = false
        actionCommitted = false
        playerMoveScriptStarted = false
        stage = AiPresentationStage.COMPLETE
    }

    fun finishCamp() {
        activeCamp = null
        resolution = null
        unitDeathScriptPass = 0
        clearActor()
    }
}
