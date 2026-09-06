// Battle
package com.jojo.game.application.battle.round

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.BattleSkillTemp
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.application.battle.BattleTurnSettlementService
import com.jojo.game.domain.battle.settlement.*

/** BattleRoundLifecycleFacade: 전투 라운드 수명 주기 진입점이며, 관련 전투 기능을 묶어 안정적인 호출 경로를 제공한다. */
class BattleRoundLifecycleFacade internal constructor(
    /**
     * `configuration` (BattleConfiguration,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val configuration: BattleConfiguration,
    /**
     * `journal` (BattleStateJournal,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val journal: BattleStateJournal,
    battlefield: Battlefield,
    /**
     * `units` (() -> Collection<BattleUnit>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val units: () -> Collection<BattleUnit>,
    /**
     * `skillTemps` (BattleSkillTemp,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val skillTemps: BattleSkillTemp,
    /**
     * `battle` (Battle,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val battle: Battle,
    /**
     * `aiSortValue` ((BattleUnit) -> Comparable<*>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val aiSortValue: (BattleUnit) -> Comparable<*>,
) {
    /**
     * `environment` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val environment = BattleTurnEnvironmentAssembler.build(configuration, journal, battlefield, units)

    /**
     * `endTurn`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun endTurn(): TurnResult = BattleRoundCoordinator.endTurn(
        activeFaction = { journal.activeFaction },
        round = { journal.round },
        settleActiveCampEnd = ::settleActiveCampEnd,
        advanceRound = ::advanceRound,
        resetCompletedRoundSkillTemps = ::resetCompletedRoundSkillTemps,
        applyScheduledWeather = ::applyScheduledWeather,
        advanceToNextCamp = ::advanceToNextCamp,
        settleActiveCampStart = ::settleActiveCampStart,
        runActiveCampEvents = ::runActiveCampEvents,
        prepareActiveCampOperation = { prepareActiveCampOperation() },
        units = units,
    )

    /**
     * `settleActiveCampEnd`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun settleActiveCampEnd(): CampSettlement =
        BattleTurnSettlementService.settleCampEnd(journal.activeFaction, environment)

    /**
     * `advanceToNextCamp`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun advanceToNextCamp(): TurnResult {
        val (next, result) = BattleRoundCoordinator.advanceToNextCamp(journal.activeFaction, journal.round)
        journal.setActiveFaction(next)
        return result
    }

    /**
     * `runActiveCampEvents`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun runActiveCampEvents(): List<String> = BattleRoundCoordinator.runActiveCampEvents(
        configuration.events,
        journal.mutableFiredEventIds(),
        battle,
    )

    /**
     * `settleActiveCampStart`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun settleActiveCampStart(): CampSettlement =
        BattleTurnSettlementService.settleCampStart(journal.activeFaction, environment)

    /**
     * `prepareActiveCampOperation`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun prepareActiveCampOperation() {
        journal.recordAiTurnOrder(
            BattleRoundCoordinator.prepareActiveCampOperation(
                journal.activeFaction,
                units(),
                aiSortValue,
            ),
        )
    }

    /**
     * `advanceRound`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun advanceRound(): RoundAdvance {
        val (newRound, advance) = BattleRoundCoordinator.advanceRound(journal.activeFaction, journal.round)
        journal.setRound(newRound)
        return advance
    }

    /**
     * `resetCompletedRoundSkillTemps`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun resetCompletedRoundSkillTemps(completedRound: Int) =
        BattleRoundCoordinator.resetCompletedRoundSkillTemps(completedRound, journal.round, skillTemps)

    /**
     * `applyScheduledWeather`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun applyScheduledWeather(): WeatherTransition {
        val (newWeather, transition) = BattleRoundCoordinator.applyScheduledWeather(
            journal.round,
            configuration.weatherSchedule,
            configuration.weatherOffset,
            journal.weather,
        )
        journal.setWeather(newWeather)
        return transition
    }
}
