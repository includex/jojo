package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.settlement.*
import com.jojo.game.domain.battle.BattleAttributeCalculator

/**
 * object  `BattleRoundCoordinator`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object BattleRoundCoordinator {

    /**
     * 공개 메서드 `advanceToNextCamp`
     *
     * ### 파라미터
    - `currentFaction` (`Faction`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `round` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Pair<Faction, TurnResult>`
     * - 반환값: 동작 결과의 도메인 값입니다.
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

    /**
     * 공개 메서드 `advanceRound`
     *
     * ### 파라미터
    - `activeFaction` (`Faction`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `currentRound` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Pair<Int, RoundAdvance>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun advanceRound(activeFaction: Faction, currentRound: Int): Pair<Int, RoundAdvance> {
        check(activeFaction == Faction.REINFORCEMENTS) { "round may advance only after the reinforcements camp" }
        val completedRound = currentRound
        val newRound = currentRound + 1
        return newRound to RoundAdvance(completedRound, newRound)
    }

    /**
     * 공개 메서드 `resetCompletedRoundSkillTemps`
     *
     * ### 파라미터
    - `completedRound` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `currentRound` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `skillTemps` (`BattleSkillTemp`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
