package com.jojo.game
import com.jojo.game.domain.battle.Battlefield

/** Owns camp settlement, round advancement, weather, and event lifecycle rules. */
internal class BattleRoundLifecycleFacade(
    private val configuration: BattleConfiguration,
    private val journal: BattleStateJournal,
    battlefield: Battlefield,
    private val units: () -> Collection<BattleUnit>,
    private val skillTemps: BattleSkillTemp,
    private val battle: Battle,
    private val aiSortValue: (BattleUnit) -> Comparable<*>,
) {
    private val environment = BattleTurnEnvironmentAssembler.build(configuration, journal, battlefield, units)

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

    fun settleActiveCampEnd(): CampSettlement =
        BattleTurnSettlementService.settleCampEnd(journal.activeFaction, environment)

    fun advanceToNextCamp(): TurnResult {
        val (next, result) = BattleRoundCoordinator.advanceToNextCamp(journal.activeFaction, journal.round)
        journal.setActiveFaction(next)
        return result
    }

    fun runActiveCampEvents(): List<String> = BattleRoundCoordinator.runActiveCampEvents(
        configuration.events,
        journal.mutableFiredEventIds(),
        battle,
    )

    fun settleActiveCampStart(): CampSettlement =
        BattleTurnSettlementService.settleCampStart(journal.activeFaction, environment)

    fun prepareActiveCampOperation() {
        journal.recordAiTurnOrder(
            BattleRoundCoordinator.prepareActiveCampOperation(
                journal.activeFaction,
                units(),
                aiSortValue,
            ),
        )
    }

    fun advanceRound(): RoundAdvance {
        val (newRound, advance) = BattleRoundCoordinator.advanceRound(journal.activeFaction, journal.round)
        journal.setRound(newRound)
        return advance
    }

    fun resetCompletedRoundSkillTemps(completedRound: Int) =
        BattleRoundCoordinator.resetCompletedRoundSkillTemps(completedRound, journal.round, skillTemps)

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
