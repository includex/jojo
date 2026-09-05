package com.jojo.game.application.battle

import com.jojo.game.AiTurnResult
import com.jojo.game.RoundAdvance
import com.jojo.game.TurnResult
import com.jojo.game.WeatherTransition
import com.jojo.game.domain.battle.settlement.CampSettlement
import com.jojo.game.domain.battle.turn.BattleTurnPhase
import com.jojo.game.domain.battle.turn.BattleTurnSnapshot

/** Owns mutable lifecycle observations; consumers receive only [BattleTurnSnapshot]. */
internal class BattleTurnRuntimeState(initialPhase: BattleTurnPhase) {
    var phase: BattleTurnPhase = initialPhase
    var lastTurn: TurnResult? = null
    var lastAiResult: AiTurnResult? = null
    var lastCampSettlement: CampSettlement? = null
    var lastRoundAdvance: RoundAdvance? = null
    var lastWeatherTransition: WeatherTransition? = null

    fun snapshot() = BattleTurnSnapshot(
        phase, lastTurn, lastAiResult, lastCampSettlement, lastRoundAdvance, lastWeatherTransition,
    )
}
