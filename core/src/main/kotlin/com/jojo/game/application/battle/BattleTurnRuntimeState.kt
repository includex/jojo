// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.domain.battle.RoundAdvance
import com.jojo.game.domain.battle.TurnResult
import com.jojo.game.domain.battle.WeatherTransition
import com.jojo.game.domain.battle.settlement.CampSettlement
import com.jojo.game.domain.battle.turn.BattleTurnPhase
import com.jojo.game.domain.battle.turn.BattleTurnSnapshot

/** BattleTurnRuntimeState: 전투 턴의 가변 관측값을 보관하고, 소비자에게는 불변 스냅샷만 제공한다. */
internal class BattleTurnRuntimeState(initialPhase: BattleTurnPhase) {
    /**
     * `phase` (BattleTurnPhase): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var phase: BattleTurnPhase = initialPhase
    /**
     * `lastTurn` (TurnResult?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var lastTurn: TurnResult? = null
    /**
     * `lastAiResult` (AiTurnResult?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var lastAiResult: AiTurnResult? = null
    /**
     * `lastCampSettlement` (CampSettlement?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var lastCampSettlement: CampSettlement? = null
    /**
     * `lastRoundAdvance` (RoundAdvance?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var lastRoundAdvance: RoundAdvance? = null
    /**
     * `lastWeatherTransition` (WeatherTransition?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var lastWeatherTransition: WeatherTransition? = null

    /**
     * `snapshot`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun snapshot() = BattleTurnSnapshot(
        phase, lastTurn, lastAiResult, lastCampSettlement, lastRoundAdvance, lastWeatherTransition,
    )
}
