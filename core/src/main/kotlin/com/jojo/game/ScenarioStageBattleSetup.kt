package com.jojo.game
import com.jojo.game.domain.campaign.*

internal const val SCENARIO_ENABLED_FEATURE_ZJHH = 8

class ScenarioStageBattleSetup(private val campaign: CampaignState) {
    private val weatherEnvironment = ScenarioStageWeatherEnvironment()

    var battleMapIndex: Int = 0
    var battleMaxRounds: Int = 99
        private set
    var battleMaxRoundsIncludesFeature: Boolean = false
        private set
    var battleLevelOffset: Int = 0
        private set
    var enemyMasterInstanceId: Int = -1
        private set
    var mineMasterInstanceId: Int = 0
        private set
    val battleWeatherType: Int get() = weatherEnvironment.type
    val battleWeatherOffset: Int get() = weatherEnvironment.offset
    var battleOperationStarted: Boolean = false
        private set
    var battleDrawRequested: Boolean = false
        private set
    var joinBattleLimit: ScenarioJoinBattleLimit? = null
        private set
    var battlePositions: List<Pair<Int, Int>> = emptyList()
        private set
    var scriptedBattleOutcome: BattleOutcome? = null
        private set
    var battleEndedByScript: Boolean = false
        private set
    var winCondition: String = ""
        private set
    var showWinConditionRequested: String? = null
        private set
    var winConditionVs: List<Int>? = null
        private set
    var winConditionTalk: List<Int>? = null
        private set
    val nearEvents = mutableListOf<List<Int>>()
    val enemyEquipment = linkedMapOf<Int, List<Int>>()
    var rewardRequest: ScenarioRewardRequest? = null
        private set

    fun setWinCondition(text: String) {
        winCondition = text
    }

    fun showWinCondition(text: String) {
        showWinConditionRequested = text
    }

    fun consumeShowWinCondition(): String? = showWinConditionRequested.also { showWinConditionRequested = null }

    fun selectBattleMap(index: Int) {
        battleMapIndex = index
    }

    fun setBattleGlobalData(
        maxRounds: Int,
        levelOffset: Int,
        enemyMaster: Int = -1,
        mineMaster: Int = 0,
        weatherType: Int = 6,
        weatherOffset: Int = 0,
    ) {
        battleMaxRounds = maxRounds.coerceAtLeast(1)
        battleMaxRoundsIncludesFeature = false
        battleLevelOffset = levelOffset
        enemyMasterInstanceId = enemyMaster
        mineMasterInstanceId = mineMaster
        weatherEnvironment.configure(weatherType, weatherOffset)
    }

    fun battleWeatherSchedule(): List<BattleWeather> = weatherEnvironment.schedule()

    fun initialBattleWeather(): BattleWeather = weatherEnvironment.initialWeather()

    fun startOperation() {
        battleOperationStarted = true
    }

    fun setMaxRound(maxRounds: Int, enabledFeatures: Int = 0): Boolean {
        val sourceValue = maxRounds + if (enabledFeatures and SCENARIO_ENABLED_FEATURE_ZJHH != 0) 4 else 0
        if (battleMaxRounds == sourceValue) {
            battleMaxRoundsIncludesFeature = true
            return false
        }
        battleMaxRounds = sourceValue
        battleMaxRoundsIncludesFeature = true
        return true
    }

    fun drawBattle() {
        battleDrawRequested = true
    }

    fun setJoinBattle(minimum: Int, maximum: Int, required: List<Any?>, excluded: List<Any?>) {
        if (minimum < 1 || maximum < 1) return
        joinBattleLimit = ScenarioJoinBattleLimit(
            minimum,
            maximum,
            required.map { it.asInt() },
            excluded.map { it.asInt() },
        ).also { campaign.roster.configureBattleRoster(it) }
    }

    fun setBattlePositions(positions: List<Any?>) {
        battlePositions = positions.mapNotNull { raw ->
            val point = raw as? List<Any?> ?: return@mapNotNull null
            point.getOrNull(0).asInt() to point.getOrNull(1).asInt()
        }
    }

    fun reward(bonusMoney: Int = 0, items: List<Any?> = emptyList(), end: Boolean = false) {
        rewardRequest = ScenarioRewardRequest(bonusMoney, items.map { it.asInt() }, end)
        scriptedBattleOutcome = BattleOutcome.PLAYER_VICTORY
    }

    fun consumeRewardRequest(): ScenarioRewardRequest? = rewardRequest.also { rewardRequest = null }
    fun lose() {
        scriptedBattleOutcome = BattleOutcome.ENEMY_VICTORY
    }

    fun endBattle() {
        battleEndedByScript = true
    }

    fun addNearEvent(values: List<Any?>, flag: Int = 0) {
        val converted = values.map { it.asInt() }
        nearEvents += converted
        if (flag and 1 != 0) winConditionTalk = converted else winConditionVs = converted
    }

    fun setEnemyEquipment(unitId: Int, values: List<Any?>) {
        enemyEquipment[unitId] = values.map { it.asInt() }
    }

    fun joinUnit(unitId: Int) {
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

    fun unitAttribute(unitId: Int, attribute: Int, default: Int = 0): Int =
        campaign.unitAttribute(unitId, attribute, default)

    fun setUnitAttribute(unitId: Int, attribute: Int, value: Int) =
        campaign.setUnitAttribute(unitId, attribute, value)

    fun changeUnitAttribute(unitId: Int, attribute: Int, operation: Int, value: Int) {
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
