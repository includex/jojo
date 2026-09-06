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


    /**
     * `advanceToNextCamp`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun advanceToNextCamp(currentFaction: Faction, round: Int): Pair<Faction, TurnResult> {
        val nextFaction = when (currentFaction) {
            Faction.PLAYER -> Faction.FRIEND
            Faction.FRIEND -> Faction.ENEMY
            Faction.ENEMY -> Faction.REINFORCEMENTS
            Faction.REINFORCEMENTS -> Faction.PLAYER
        }
        return nextFaction to TurnResult(round, nextFaction, emptyList())
    }

    /**
     * `runActiveCampEvents`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `prepareActiveCampOperation`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `advanceRound`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun advanceRound(activeFaction: Faction, currentRound: Int): Pair<Int, RoundAdvance> {
        check(activeFaction == Faction.REINFORCEMENTS) { "round may advance only after the reinforcements camp" }
        val completedRound = currentRound
        val newRound = currentRound + 1
        return newRound to RoundAdvance(completedRound, newRound)
    }


    /**
     * `resetCompletedRoundSkillTemps`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun resetCompletedRoundSkillTemps(completedRound: Int, currentRound: Int, skillTemps: BattleSkillTemp) {
        check(completedRound == currentRound - 1) { "only the just-completed round may be reset" }
        skillTemps.reset(completedRound)
    }

    /**
     * `applyScheduledWeather`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun applyScheduledWeather(
        currentRound: Int,
        weatherSchedule: List<BattleWeather>,
        weatherOffset: Int,
        currentWeather: BattleWeather,
    ): Pair<BattleWeather, WeatherTransition> {
        /**
         * `previous` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val previous = currentWeather
        /**
         * `newWeather` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val newWeather = if (weatherSchedule.isNotEmpty()) {
            weatherSchedule[Math.floorMod(currentRound + weatherOffset, weatherSchedule.size)]
        } else {
            currentWeather
        }
        return newWeather to WeatherTransition(previous, newWeather)
    }

    /**
     * `endTurn`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
            /**
             * `advance` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val advance = advanceRound()
            resetCompletedRoundSkillTemps(advance.completedRound)
            applyScheduledWeather()
        }
        /**
         * `result` (TurnResult): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var result: TurnResult
        /**
         * `fired` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val fired = mutableListOf<String>()
        do {
            result = advanceToNextCamp()
            settleActiveCampStart()
            fired += runActiveCampEvents()
            prepareActiveCampOperation()
            /**
             * `currentCamp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val currentCamp = activeFaction()
            if (currentCamp == Faction.PLAYER || units().any {
                    it.visible && it.effectiveFaction() == currentCamp
                }
            ) break
            settleActiveCampEnd()
            if (currentCamp == Faction.REINFORCEMENTS) {
                /**
                 * `advance` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val advance = advanceRound()
                resetCompletedRoundSkillTemps(advance.completedRound)
                applyScheduledWeather()
            }
        } while (true)
        return result.copy(round = round(), activeFaction = activeFaction(), firedEvents = fired)
    }
}
