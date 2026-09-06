// Scenario
package com.jojo.game.application.scenario.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.application.scenario.ScenarioStageWeatherEnvironment
import com.jojo.game.application.scenario.asInt
import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.campaign.*

/**
 * `SCENARIO_ENABLED_FEATURE_ZJHH` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
 */

internal const val SCENARIO_ENABLED_FEATURE_ZJHH = 8

/**
 * `ScenarioStageBattleAccess` 계약 인터페이스: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

interface ScenarioStageBattleAccess {
    /**
     * `battleMapIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var battleMapIndex: Int
    /**
     * `battleMaxRounds` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleMaxRounds: Int
    /**
     * `battleMaxRoundsIncludesFeature` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleMaxRoundsIncludesFeature: Boolean
    /**
     * `battleLevelOffset` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleLevelOffset: Int
    /**
     * `enemyMasterInstanceId` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val enemyMasterInstanceId: Int
    /**
     * `mineMasterInstanceId` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val mineMasterInstanceId: Int
    /**
     * `battleWeatherType` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleWeatherType: Int
    /**
     * `battleWeatherOffset` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleWeatherOffset: Int
    /**
     * `battleOperationStarted` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleOperationStarted: Boolean
    /**
     * `battleDrawRequested` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleDrawRequested: Boolean
    /**
     * `joinBattleLimit` (ScenarioJoinBattleLimit?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val joinBattleLimit: ScenarioJoinBattleLimit?
    /**
     * `battlePositions` (List<Pair<Int, Int>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battlePositions: List<Pair<Int, Int>>
    /**
     * `scriptedBattleOutcome` (BattleOutcome?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val scriptedBattleOutcome: BattleOutcome?
    /**
     * `battleEndedByScript` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val battleEndedByScript: Boolean
    /**
     * `winCondition` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val winCondition: String
    /**
     * `showWinConditionRequested` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val showWinConditionRequested: String?
    /**
     * `winConditionVs` (List<Int>?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val winConditionVs: List<Int>?
    /**
     * `winConditionTalk` (List<Int>?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val winConditionTalk: List<Int>?
    /**
     * `rewardRequest` (ScenarioRewardRequest?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val rewardRequest: ScenarioRewardRequest?
    /**
     * `nearEvents` (MutableList<List<Int>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val nearEvents: MutableList<List<Int>>
    /**
     * `enemyEquipment` (MutableMap<Int, List<Int>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val enemyEquipment: MutableMap<Int, List<Int>>

    /**
     * `setWinCondition`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setWinCondition(text: String)
    /**
     * `showWinCondition`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun showWinCondition(text: String)
    /**
     * `consumeShowWinCondition`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeShowWinCondition(): String?
    /**
     * `selectBattleMap`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun selectBattleMap(index: Int)
    /**
     * `setBattleGlobalData`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setBattleGlobalData(
        maxRounds: Int,
        levelOffset: Int,
        enemyMaster: Int = -1,
        mineMaster: Int = 0,
        weatherType: Int = 6,
        weatherOffset: Int = 0,
    )
    /**
     * `battleWeatherSchedule`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleWeatherSchedule(): List<BattleWeather>
    /**
     * `initialBattleWeather`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun initialBattleWeather(): BattleWeather
    /**
     * `startOperation`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun startOperation()
    /**
     * `setMaxRound`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setMaxRound(maxRounds: Int, enabledFeatures: Int = 0): Boolean
    /**
     * `drawBattle`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun drawBattle()
    /**
     * `setJoinBattle`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setJoinBattle(minimum: Int, maximum: Int, required: List<Any?>, excluded: List<Any?>)
    /**
     * `setBattlePositions`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setBattlePositions(positions: List<Any?>)
    /**
     * `reward`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun reward(bonusMoney: Int = 0, items: List<Any?> = emptyList(), end: Boolean = false)
    /**
     * `consumeRewardRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeRewardRequest(): ScenarioRewardRequest?
    /**
     * `lose`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun lose()
    /**
     * `endBattle`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun endBattle()
    /**
     * `addNearEvent`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addNearEvent(values: List<Any?>, flag: Int = 0)
    /**
     * `setEnemyEquipment`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setEnemyEquipment(unitId: Int, values: List<Any?>)
    /**
     * `joinUnit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun joinUnit(unitId: Int)
    /**
     * `unitAttribute`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitAttribute(unitId: Int, attribute: Int, default: Int = 0): Int
    /**
     * `setUnitAttribute`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnitAttribute(unitId: Int, attribute: Int, value: Int)
    /**
     * `changeUnitAttribute`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun changeUnitAttribute(unitId: Int, attribute: Int, operation: Int, value: Int)
}

/**
 * `ScenarioStageBattleSetup` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class ScenarioStageBattleSetup(private val campaign: CampaignState) : ScenarioStageBattleAccess {
    /**
     * `weatherEnvironment` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val weatherEnvironment = ScenarioStageWeatherEnvironment()

    /**
     * `battleMapIndex` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var battleMapIndex: Int = 0
    /**
     * `battleMaxRounds` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var battleMaxRounds: Int = 99
        private set
    /**
     * `battleMaxRoundsIncludesFeature` (Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var battleMaxRoundsIncludesFeature: Boolean = false
        private set
    /**
     * `battleLevelOffset` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var battleLevelOffset: Int = 0
        private set
    /**
     * `enemyMasterInstanceId` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var enemyMasterInstanceId: Int = -1
        private set
    /**
     * `mineMasterInstanceId` (Int): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var mineMasterInstanceId: Int = 0
        private set
    /**
     * `battleWeatherType` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val battleWeatherType: Int get() = weatherEnvironment.type
    /**
     * `battleWeatherOffset` (Int get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val battleWeatherOffset: Int get() = weatherEnvironment.offset
    /**
     * `battleOperationStarted` (Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var battleOperationStarted: Boolean = false
        private set
    /**
     * `battleDrawRequested` (Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var battleDrawRequested: Boolean = false
        private set
    /**
     * `joinBattleLimit` (ScenarioJoinBattleLimit?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var joinBattleLimit: ScenarioJoinBattleLimit? = null
        private set
    /**
     * `battlePositions` (List<Pair<Int, Int>>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var battlePositions: List<Pair<Int, Int>> = emptyList()
        private set
    /**
     * `scriptedBattleOutcome` (BattleOutcome?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var scriptedBattleOutcome: BattleOutcome? = null
        private set
    /**
     * `battleEndedByScript` (Boolean): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var battleEndedByScript: Boolean = false
        private set
    /**
     * `winCondition` (String): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var winCondition: String = ""
        private set
    /**
     * `showWinConditionRequested` (String?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var showWinConditionRequested: String? = null
        private set
    /**
     * `winConditionVs` (List<Int>?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var winConditionVs: List<Int>? = null
        private set
    /**
     * `winConditionTalk` (List<Int>?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var winConditionTalk: List<Int>? = null
        private set
    /**
     * `nearEvents` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val nearEvents = mutableListOf<List<Int>>()
    /**
     * `enemyEquipment` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val enemyEquipment = linkedMapOf<Int, List<Int>>()
    /**
     * `rewardRequest` (ScenarioRewardRequest?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override var rewardRequest: ScenarioRewardRequest? = null
        private set

    /**
     * `setWinCondition`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun setWinCondition(text: String) {
        winCondition = text
    }

    /**
     * `showWinCondition`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun showWinCondition(text: String) {
        showWinConditionRequested = text
    }

    /**
     * `consumeShowWinCondition`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun consumeShowWinCondition(): String? = showWinConditionRequested.also { showWinConditionRequested = null }

    /**
     * `selectBattleMap`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun selectBattleMap(index: Int) {
        battleMapIndex = index
    }

    /**
     * `setBattleGlobalData`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    override fun setBattleGlobalData(
        maxRounds: Int,
        levelOffset: Int,
        enemyMaster: Int,
        mineMaster: Int,
        weatherType: Int,
        weatherOffset: Int,
    ) {
        battleMaxRounds = maxRounds.coerceAtLeast(1)
        battleMaxRoundsIncludesFeature = false
        battleLevelOffset = levelOffset
        enemyMasterInstanceId = enemyMaster
        mineMasterInstanceId = mineMaster
        weatherEnvironment.configure(weatherType, weatherOffset)
    }

    /**
     * `battleWeatherSchedule`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun battleWeatherSchedule(): List<BattleWeather> = weatherEnvironment.schedule()

    /**
     * `initialBattleWeather`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun initialBattleWeather(): BattleWeather = weatherEnvironment.initialWeather()

    /**
     * `startOperation`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun startOperation() {
        battleOperationStarted = true
    }

    /**
     * `setMaxRound`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun setMaxRound(maxRounds: Int, enabledFeatures: Int): Boolean {
        val sourceValue = maxRounds + if (enabledFeatures and SCENARIO_ENABLED_FEATURE_ZJHH != 0) 4 else 0
        if (battleMaxRounds == sourceValue) {
            battleMaxRoundsIncludesFeature = true
            return false
        }
        battleMaxRounds = sourceValue
        battleMaxRoundsIncludesFeature = true
        return true
    }

    /**
     * `drawBattle`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun drawBattle() {
        battleDrawRequested = true
    }

    /**
     * `setJoinBattle`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun setJoinBattle(minimum: Int, maximum: Int, required: List<Any?>, excluded: List<Any?>) {
        if (minimum < 1 || maximum < 1) return
        joinBattleLimit = ScenarioJoinBattleLimit(
            minimum,
            maximum,
            required.map { it.asInt() },
            excluded.map { it.asInt() },
        ).also { campaign.roster.configureBattleRoster(it) }
    }

    /**
     * `setBattlePositions`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun setBattlePositions(positions: List<Any?>) {
        battlePositions = positions.mapNotNull { raw ->
            val point = raw as? List<Any?> ?: return@mapNotNull null
            point.getOrNull(0).asInt() to point.getOrNull(1).asInt()
        }
    }

    /**
     * `reward`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun reward(bonusMoney: Int, items: List<Any?>, end: Boolean) {
        rewardRequest = ScenarioRewardRequest(bonusMoney, items.map { it.asInt() }, end)
        scriptedBattleOutcome = BattleOutcome.PLAYER_VICTORY
    }

    /**
     * `consumeRewardRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun consumeRewardRequest(): ScenarioRewardRequest? = rewardRequest.also { rewardRequest = null }
    /**
     * `lose`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun lose() {
        scriptedBattleOutcome = BattleOutcome.ENEMY_VICTORY
    }

    /**
     * `endBattle`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun endBattle() {
        battleEndedByScript = true
    }

    /**
     * `addNearEvent`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun addNearEvent(values: List<Any?>, flag: Int) {
        val converted = values.map { it.asInt() }
        nearEvents += converted
        if (flag and 1 != 0) winConditionTalk = converted else winConditionVs = converted
    }

    /**
     * `setEnemyEquipment`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun setEnemyEquipment(unitId: Int, values: List<Any?>) {
        enemyEquipment[unitId] = values.map { it.asInt() }
    }

    /**
     * `joinUnit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun joinUnit(unitId: Int) {
        campaign.joinedUnits += unitId
        campaign.inventory.ensureDefaultEquipment(unitId, GameDataCatalog.load())
    }

    /**
     * `getItem`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun getItem(
        itemId: Int,
        suppliedCountOrLevel: Int = 0,
        addToInventory: Boolean = true,
        acquiredItems: MutableList<Int>
    ): String {
        /**
         * `data` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val data = GameDataCatalog.load()
        /**
         * `item` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val item = data.equipmentProfile(itemId)
        /**
         * `property` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val property = itemId in 150..254
        /**
         * `displayValue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val displayValue = if (property) {
            suppliedCountOrLevel.coerceAtLeast(1)
        } else if (suppliedCountOrLevel < 1) {
            /**
             * `levelField` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val levelField = (data.unitLevelLimit() / 10).coerceAtLeast(1)
            (campaign.averageJoinedLevel() / levelField).coerceIn(0, 8) + 1
        } else {
            suppliedCountOrLevel
        }
        if (addToInventory) {
            campaign.inventory.addItem(
                itemId,
                count = displayValue.takeIf { property } ?: 1,
                level = displayValue.takeUnless { property } ?: 1,
            )
            campaign.inventory.discoverTreasure(itemId, data)
            acquiredItems += itemId
        }
        return "얻었다 ${item?.name ?: "아이템 $itemId"} ${if (property) "X" else "Lv"}$displayValue"
    }

    /**
     * `battleItemCompletionMessage`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun battleItemCompletionMessage(itemId: Int): String =
        "얻었다${GameDataCatalog.load().equipmentProfile(itemId)?.name ?: "아이템 $itemId"}!"

    /**
     * `setJoinEquip`: 현재 상태를 갱신한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun setJoinEquip(
        unitId: Int,
        weapon: Int,
        weaponLevel: Int,
        armor: Int,
        armorLevel: Int,
        auxiliary: Int,
        joinedEquipment: MutableMap<Int, ScenarioJoinEquipment>
    ) {
        joinedEquipment[unitId] = ScenarioJoinEquipment(unitId, weapon, weaponLevel, armor, armorLevel, auxiliary)
        campaign.inventory.setEquipment(unitId, weapon, weaponLevel, armor, armorLevel, auxiliary)
    }

    /**
     * `ending`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun ending(id: Int, setEndingId: (Int) -> Unit) {
        setEndingId(id); campaign.applyInfoTransfer(22, id.toString())
    }

    /**
     * `infoTransfer`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun infoTransfer(
        type: Int,
        payload: String,
        selectedUnitId: Int = 0,
        infoTransfers: MutableList<Pair<Int, String>>
    ) {
        infoTransfers += type to payload
        campaign.applyInfoTransfer(type, payload, selectedUnitId)
    }

    /**
     * `jumpScene`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun jumpScene(target: Int, setSceneJumpTarget: (Int) -> Unit, setSceneJumpStage: (Int) -> Unit) {
        setSceneJumpTarget(target)
        val jumpOffset = (campaign.globalVariables[JUMP_OFFSET_GLOBAL] as? Number)?.toInt() ?: 0
        if (jumpOffset != 0) campaign.globalVariables[JUMP_OFFSET_GLOBAL] = 0
        setSceneJumpStage(target + 1 + 200 * jumpOffset)
    }

    /**
     * `unitAttribute`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun unitAttribute(unitId: Int, attribute: Int, default: Int): Int =
        campaign.unitAttribute(unitId, attribute, default)

    /**
     * `setUnitAttribute`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun setUnitAttribute(unitId: Int, attribute: Int, value: Int) =
        campaign.setUnitAttribute(unitId, attribute, value)

    /**
     * `changeUnitAttribute`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun changeUnitAttribute(unitId: Int, attribute: Int, operation: Int, value: Int) {
        val current = unitAttribute(unitId, attribute)
        setUnitAttribute(
            unitId, attribute, when (operation) {
                0 -> value
                1 -> current + value
                2 -> current - value
                else -> current
            }
        )
    }

    companion object {
        /**
         * `JUMP_OFFSET_GLOBAL` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        private const val JUMP_OFFSET_GLOBAL = 4051
    }
}
