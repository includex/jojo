package com.jojo.game.domain.battle.turn

import com.jojo.game.AiTurnResult
import com.jojo.game.RoundAdvance
import com.jojo.game.TurnResult
import com.jojo.game.WeatherTransition
import com.jojo.game.domain.battle.settlement.CampSettlement

enum class BattleTurnPhase {
    BOOTSTRAP, PLAYER_INPUT, CAMP_CARD, CAMP_STATE, CAMP_SCRIPT, CAMP_DEATHS,
    AI, CAMP_RESTORE, CAMP_RESTORE_DEATHS, ROUND_SCRIPT, ROUND_DEATHS, WEATHER, FINISHED,
}

enum class BattleDeathCheckpoint { CAMP_START, CAMP_RESTORE, ROUND_START }

data class BattleCampCard(val turn: TurnResult, val showsRoundNumber: Boolean)

data class BattleTurnSnapshot(
    val phase: BattleTurnPhase,
    val lastTurn: TurnResult?,
    val lastAiResult: AiTurnResult?,
    val lastCampSettlement: CampSettlement?,
    val lastRoundAdvance: RoundAdvance?,
    val lastWeatherTransition: WeatherTransition?,
)
