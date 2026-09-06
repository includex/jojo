// Battle
package com.jojo.game.domain.battle.turn

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.domain.battle.RoundAdvance
import com.jojo.game.domain.battle.TurnResult
import com.jojo.game.domain.battle.WeatherTransition
import com.jojo.game.domain.battle.settlement.CampSettlement

/** BattleTurnPhase: 초기화부터 종료까지 전투 턴 제어기가 진행하는 세부 단계를 나타낸다. */
enum class BattleTurnPhase {
    BOOTSTRAP, PLAYER_INPUT, CAMP_CARD, CAMP_STATE, CAMP_SCRIPT, CAMP_DEATHS,
    AI, CAMP_RESTORE, CAMP_RESTORE_DEATHS, ROUND_SCRIPT, ROUND_DEATHS, WEATHER, FINISHED,
}

/** BattleDeathCheckpoint: 사망 표현을 확인하는 진영 시작·복원·라운드 시작 시점을 구분한다. */
enum class BattleDeathCheckpoint { CAMP_START, CAMP_RESTORE, ROUND_START }

/** BattleCampCard: 진영 전환 때 표시할 턴 결과와 라운드 번호 노출 여부를 보관한다. */
data class BattleCampCard(val turn: TurnResult, val showsRoundNumber: Boolean)

/** BattleTurnSnapshot: 표현 계층과 테스트가 읽는 현재 턴 단계 및 최근 처리 결과의 불변 상태이다. */
data class BattleTurnSnapshot(
    val phase: BattleTurnPhase,
    val lastTurn: TurnResult?,
    val lastAiResult: AiTurnResult?,
    val lastCampSettlement: CampSettlement?,
    val lastRoundAdvance: RoundAdvance?,
    val lastWeatherTransition: WeatherTransition?,
)
