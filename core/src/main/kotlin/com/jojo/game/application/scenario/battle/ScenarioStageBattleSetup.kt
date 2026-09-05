package com.jojo.game.application.scenario.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.GameDataCatalog
import com.jojo.game.ScenarioStageWeatherEnvironment
import com.jojo.game.asInt
import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.campaign.*

internal const val SCENARIO_ENABLED_FEATURE_ZJHH = 8

interface ScenarioStageBattleAccess {
    var battleMapIndex: Int
    val battleMaxRounds: Int
    val battleMaxRoundsIncludesFeature: Boolean
    val battleLevelOffset: Int
    val enemyMasterInstanceId: Int
    val mineMasterInstanceId: Int
    val battleWeatherType: Int
    val battleWeatherOffset: Int
    val battleOperationStarted: Boolean
    val battleDrawRequested: Boolean
    val joinBattleLimit: ScenarioJoinBattleLimit?
    val battlePositions: List<Pair<Int, Int>>
    val scriptedBattleOutcome: BattleOutcome?
    val battleEndedByScript: Boolean
    val winCondition: String
    val showWinConditionRequested: String?
    val winConditionVs: List<Int>?
    val winConditionTalk: List<Int>?
    val rewardRequest: ScenarioRewardRequest?
    val nearEvents: MutableList<List<Int>>
    val enemyEquipment: MutableMap<Int, List<Int>>

    fun setWinCondition(text: String)
    fun showWinCondition(text: String)
    fun consumeShowWinCondition(): String?
    fun selectBattleMap(index: Int)
    fun setBattleGlobalData(
        maxRounds: Int,
        levelOffset: Int,
        enemyMaster: Int = -1,
        mineMaster: Int = 0,
        weatherType: Int = 6,
        weatherOffset: Int = 0,
    )
    fun battleWeatherSchedule(): List<BattleWeather>
    fun initialBattleWeather(): BattleWeather
    fun startOperation()
    fun setMaxRound(maxRounds: Int, enabledFeatures: Int = 0): Boolean
    fun drawBattle()
    fun setJoinBattle(minimum: Int, maximum: Int, required: List<Any?>, excluded: List<Any?>)
    fun setBattlePositions(positions: List<Any?>)
    fun reward(bonusMoney: Int = 0, items: List<Any?> = emptyList(), end: Boolean = false)
    fun consumeRewardRequest(): ScenarioRewardRequest?
    fun lose()
    fun endBattle()
    fun addNearEvent(values: List<Any?>, flag: Int = 0)
    fun setEnemyEquipment(unitId: Int, values: List<Any?>)
    fun joinUnit(unitId: Int)
    fun unitAttribute(unitId: Int, attribute: Int, default: Int = 0): Int
    fun setUnitAttribute(unitId: Int, attribute: Int, value: Int)
    fun changeUnitAttribute(unitId: Int, attribute: Int, operation: Int, value: Int)
}

class ScenarioStageBattleSetup(private val campaign: CampaignState) : ScenarioStageBattleAccess {
    private val weatherEnvironment = ScenarioStageWeatherEnvironment()

    override var battleMapIndex: Int = 0
    override var battleMaxRounds: Int = 99
        private set
    override var battleMaxRoundsIncludesFeature: Boolean = false
        private set
    override var battleLevelOffset: Int = 0
        private set
    override var enemyMasterInstanceId: Int = -1
        private set
    override var mineMasterInstanceId: Int = 0
        private set
    override val battleWeatherType: Int get() = weatherEnvironment.type
    override val battleWeatherOffset: Int get() = weatherEnvironment.offset
    override var battleOperationStarted: Boolean = false
        private set
    override var battleDrawRequested: Boolean = false
        private set
    override var joinBattleLimit: ScenarioJoinBattleLimit? = null
        private set
    override var battlePositions: List<Pair<Int, Int>> = emptyList()
        private set
    override var scriptedBattleOutcome: BattleOutcome? = null
        private set
    override var battleEndedByScript: Boolean = false
        private set
    override var winCondition: String = ""
        private set
    override var showWinConditionRequested: String? = null
        private set
    override var winConditionVs: List<Int>? = null
        private set
    override var winConditionTalk: List<Int>? = null
        private set
    override val nearEvents = mutableListOf<List<Int>>()
    override val enemyEquipment = linkedMapOf<Int, List<Int>>()
    override var rewardRequest: ScenarioRewardRequest? = null
        private set

    override fun setWinCondition(text: String) {
        winCondition = text
    }

    override fun showWinCondition(text: String) {
        showWinConditionRequested = text
    }

    override fun consumeShowWinCondition(): String? = showWinConditionRequested.also { showWinConditionRequested = null }

    override fun selectBattleMap(index: Int) {
        battleMapIndex = index
    }

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

    override fun battleWeatherSchedule(): List<BattleWeather> = weatherEnvironment.schedule()

    override fun initialBattleWeather(): BattleWeather = weatherEnvironment.initialWeather()

    override fun startOperation() {
        battleOperationStarted = true
    }

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

    override fun drawBattle() {
        battleDrawRequested = true
    }

    override fun setJoinBattle(minimum: Int, maximum: Int, required: List<Any?>, excluded: List<Any?>) {
        if (minimum < 1 || maximum < 1) return
        joinBattleLimit = ScenarioJoinBattleLimit(
            minimum,
            maximum,
            required.map { it.asInt() },
            excluded.map { it.asInt() },
        ).also { campaign.roster.configureBattleRoster(it) }
    }

    override fun setBattlePositions(positions: List<Any?>) {
        battlePositions = positions.mapNotNull { raw ->
            val point = raw as? List<Any?> ?: return@mapNotNull null
            point.getOrNull(0).asInt() to point.getOrNull(1).asInt()
        }
    }

    override fun reward(bonusMoney: Int, items: List<Any?>, end: Boolean) {
        rewardRequest = ScenarioRewardRequest(bonusMoney, items.map { it.asInt() }, end)
        scriptedBattleOutcome = BattleOutcome.PLAYER_VICTORY
    }

    override fun consumeRewardRequest(): ScenarioRewardRequest? = rewardRequest.also { rewardRequest = null }
    override fun lose() {
        scriptedBattleOutcome = BattleOutcome.ENEMY_VICTORY
    }

    override fun endBattle() {
        battleEndedByScript = true
    }

    override fun addNearEvent(values: List<Any?>, flag: Int) {
        val converted = values.map { it.asInt() }
        nearEvents += converted
        if (flag and 1 != 0) winConditionTalk = converted else winConditionVs = converted
    }

    override fun setEnemyEquipment(unitId: Int, values: List<Any?>) {
        enemyEquipment[unitId] = values.map { it.asInt() }
    }

    override fun joinUnit(unitId: Int) {
        campaign.joinedUnits += unitId
        campaign.inventory.ensureDefaultEquipment(unitId, GameDataCatalog.load())
    }

    fun getItem(
        itemId: Int,
        suppliedCountOrLevel: Int = 0,
        addToInventory: Boolean = true,
        acquiredItems: MutableList<Int>
    ): String {
        val data = GameDataCatalog.load()
        val item = data.equipmentProfile(itemId)
        val property = itemId in 150..254
        val displayValue = if (property) {
            suppliedCountOrLevel.coerceAtLeast(1)
        } else if (suppliedCountOrLevel < 1) {
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

    fun battleItemCompletionMessage(itemId: Int): String =
        "얻었다${GameDataCatalog.load().equipmentProfile(itemId)?.name ?: "아이템 $itemId"}!"

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

    fun ending(id: Int, setEndingId: (Int) -> Unit) {
        setEndingId(id); campaign.applyInfoTransfer(22, id.toString())
    }

    fun infoTransfer(
        type: Int,
        payload: String,
        selectedUnitId: Int = 0,
        infoTransfers: MutableList<Pair<Int, String>>
    ) {
        infoTransfers += type to payload
        campaign.applyInfoTransfer(type, payload, selectedUnitId)
    }

    fun jumpScene(target: Int, setSceneJumpTarget: (Int) -> Unit, setSceneJumpStage: (Int) -> Unit) {
        setSceneJumpTarget(target)
        val jumpOffset = (campaign.globalVariables[JUMP_OFFSET_GLOBAL] as? Number)?.toInt() ?: 0
        if (jumpOffset != 0) campaign.globalVariables[JUMP_OFFSET_GLOBAL] = 0
        setSceneJumpStage(target + 1 + 200 * jumpOffset)
    }

    override fun unitAttribute(unitId: Int, attribute: Int, default: Int): Int =
        campaign.unitAttribute(unitId, attribute, default)

    override fun setUnitAttribute(unitId: Int, attribute: Int, value: Int) =
        campaign.setUnitAttribute(unitId, attribute, value)

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
        private const val JUMP_OFFSET_GLOBAL = 4051
    }
}
