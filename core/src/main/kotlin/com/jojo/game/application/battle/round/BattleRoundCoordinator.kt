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
import com.jojo.game.domain.battle.settlement.*
import com.jojo.game.domain.battle.BattleAttributeCalculator


/** BattleRoundCoordinator: 진영 순환·라운드 전진·날씨 변화·진영 시작과 종료 정산을 조정한다. */
object BattleRoundCoordinator {


    fun advanceToNextCamp(currentFaction: Faction, round: Int): Pair<Faction, TurnResult> {
        val nextFaction = when (currentFaction) {
            Faction.PLAYER -> Faction.FRIEND
            Faction.FRIEND -> Faction.ENEMY
            Faction.ENEMY -> Faction.REINFORCEMENTS
            Faction.REINFORCEMENTS -> Faction.PLAYER
        }
        return nextFaction to TurnResult(round, nextFaction, emptyList())
    }

    fun runActiveCampEvents(
        events: List<BattleEvent>,
        firedEventIds: MutableSet<String>,
        battle: Battle,
    ): List<String> = events
        .asSequence()
        .filter { it.id !in firedEventIds && it.matches(battle) }
        .onEach {
            firedEventIds += it.id
            it.execute(battle)
        }
        .map { it.id }
        .toList()

    fun prepareActiveCampOperation(
        faction: Faction,
        units: Collection<BattleUnit>,
        aiSortValue: (BattleUnit) -> Comparable<*>,
    ): List<String> {
        units.filter { it.effectiveFaction() == faction }.forEach {
            it.hasActed = false
            it.hasMoved = false
            it.aiValue = 0
        }
        return units.asSequence()
            .filter { it.visible && it.effectiveFaction() == faction && !it.hasActed }
            .sortedWith(compareByDescending<BattleUnit>(aiSortValue).thenBy {
                BattleAttributeCalculator.effective(
                    it,
                    BattleAttribute.DEFENSE
                )
            })
            .map { it.id }
            .toList()
    }


    fun advanceRound(activeFaction: Faction, currentRound: Int): Pair<Int, RoundAdvance> {
        check(activeFaction == Faction.REINFORCEMENTS) { "round may advance only after the reinforcements camp" }
        val completedRound = currentRound
        val newRound = currentRound + 1
        return newRound to RoundAdvance(completedRound, newRound)
    }


    fun resetCompletedRoundSkillTemps(completedRound: Int, currentRound: Int, skillTemps: BattleSkillTemp) {
        check(completedRound == currentRound - 1) { "only the just-completed round may be reset" }
        skillTemps.reset(completedRound)
    }

    fun applyScheduledWeather(
        currentRound: Int,
        weatherSchedule: List<BattleWeather>,
        weatherOffset: Int,
        currentWeather: BattleWeather,
    ): Pair<BattleWeather, WeatherTransition> {
        val previous = currentWeather
        val newWeather = if (weatherSchedule.isNotEmpty()) {
            weatherSchedule[Math.floorMod(currentRound + weatherOffset, weatherSchedule.size)]
        } else {
            currentWeather
        }
        return newWeather to WeatherTransition(previous, newWeather)
    }

    fun endTurn(
        activeFaction: () -> Faction,
        round: () -> Int,
        settleActiveCampEnd: () -> CampSettlement,
        advanceRound: () -> RoundAdvance,
        resetCompletedRoundSkillTemps: (Int) -> Unit,
        applyScheduledWeather: () -> WeatherTransition,
        advanceToNextCamp: () -> TurnResult,
        settleActiveCampStart: () -> CampSettlement,
        runActiveCampEvents: () -> List<String>,
        prepareActiveCampOperation: () -> Unit,
        units: () -> Collection<BattleUnit>,
    ): TurnResult {
        settleActiveCampEnd()
        if (activeFaction() == Faction.REINFORCEMENTS) {
            val advance = advanceRound()
            resetCompletedRoundSkillTemps(advance.completedRound)
            applyScheduledWeather()
        }
        var result: TurnResult
        val fired = mutableListOf<String>()
        do {
            result = advanceToNextCamp()
            settleActiveCampStart()
            fired += runActiveCampEvents()
            prepareActiveCampOperation()
            val currentCamp = activeFaction()
            if (currentCamp == Faction.PLAYER || units().any {
                    it.visible && it.effectiveFaction() == currentCamp
                }
            ) break
            settleActiveCampEnd()
            if (currentCamp == Faction.REINFORCEMENTS) {
                val advance = advanceRound()
                resetCompletedRoundSkillTemps(advance.completedRound)
                applyScheduledWeather()
            }
        } while (true)
        return result.copy(round = round(), activeFaction = activeFaction(), firedEvents = fired)
    }
}
