package com.jojo.game.domain.battle.turn

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleOutcome
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.PhysicalAttackPassKind
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.domain.battle.isPlayerSide
import com.jojo.game.domain.scenario.PlaybackState

data class BattleTurnEntryRequest(
    val phase: BattleTurnPhase,
    val activeFaction: Faction,
    val outcome: BattleOutcome?,
)

data class BattleCampTransitionRequest(val previous: Faction, val current: Faction)

/** Pure source-order decisions used by the mutable turn coordinator. */
object BattleTurnPolicy {
    fun acceptsPlayerEnd(request: BattleTurnEntryRequest): Boolean =
        request.phase == BattleTurnPhase.PLAYER_INPUT &&
            request.activeFaction == Faction.PLAYER && request.outcome == null

    fun campCardFor(request: BattleCampTransitionRequest): Boolean =
        request.previous.isPlayerSide() != request.current.isPlayerSide()
}

internal object CollocatedPlayerMoveScriptEnd {
    fun finishesAiTurn(
        camp: Faction,
        moveCallbackStarted: Boolean,
        scriptState: PlaybackState,
        battleEndedByScript: Boolean,
        scriptedOutcome: BattleOutcome?,
        observedOutcome: BattleOutcome?,
    ): Boolean = camp == Faction.PLAYER && moveCallbackStarted &&
        scriptState == PlaybackState.COMPLETE && battleEndedByScript &&
        scriptedOutcome != null && observedOutcome != null
}

internal fun TacticalActionResult?.hasPhysicalCounterPass(): Boolean =
    (this as? TacticalActionResult.Attack)?.physicalPasses?.any {
        it.kind == PhysicalAttackPassKind.COUNTER || it.kind == PhysicalAttackPassKind.COUNTER_FOLLOW_UP
    } == true
