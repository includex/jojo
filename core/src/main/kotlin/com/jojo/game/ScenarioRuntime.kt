package com.jojo.game

private const val SCENARIO_ENABLED_FEATURE_ZJHH = 8
private const val JUMP_OFFSET_GLOBAL = 4051

/** Minimal LibGDX-side replacement for the scenario-visible Stage state. */
class ScenarioStage(private val campaign: CampaignState = CampaignState()) {
    private val unitRegistry = ScenarioStageUnitRegistry()
    private val movementCoordinator = ScenarioStageMovementCoordinator()
    private val presentationCoordinator = ScenarioStagePresentationCoordinator()
    private val fightCoordinator = ScenarioStageFightCoordinator()

    var backgroundId: Int = 0
        private set
    var backgroundVariant: Int = 0
        private set
    var eventName: String = ""
        private set
    var stageName: String = ""
        private set
    var menuVisible: Boolean = true
        private set
    var ambition: Int = 50
        private set
    var winCondition: String = ""
        private set
    var showWinConditionRequested: String? = null
        private set
    var rewardRequest: ScenarioRewardRequest? = null
        private set
    val lastBattleUnitPostsRequiresPause: Boolean
        get() = presentationCoordinator.lastBattleUnitPostsRequiresPause

    var winConditionVs: List<Int>? = null
        private set
    var winConditionTalk: List<Int>? = null
        private set
    var bottomText: String = ""
        private set
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
    var battleWeatherType: Int = 6
        private set
    var battleWeatherOffset: Int = 0
        private set
    val fightInitialized: Boolean get() = fightCoordinator.fightInitialized
    var battleOperationStarted: Boolean = false
        private set
    var battleDrawRequested: Boolean = false
        private set
    val backgroundSound: Int get() = fightCoordinator.backgroundSound
    var sceneIndex: Int = 0
        private set
    var face: Int = 0
        private set
    var section: Pair<Int, String>? = null
        private set
    var joinBattleLimit: ScenarioJoinBattleLimit? = null
        private set
    var battlePositions: List<Pair<Int, Int>> = emptyList()
        private set
    var endingId: Int? = null
        private set
    var sceneJumpTarget: Int? = null
        private set
    var sceneJumpStage: Int? = null
        private set
    var scriptedBattleOutcome: BattleOutcome? = null
        private set
    var battleEndedByScript: Boolean = false
        private set
    val activeFightId: Long? get() = fightCoordinator.activeFightId

    val units: MutableMap<Int, TacticalUnit> get() = unitRegistry.units
    val battleUnits: MutableMap<String, ScenarioBattleUnit> get() = unitRegistry.battleUnits
    val mapObjects = linkedMapOf<Pair<Int, Int>, ScenarioMapObject>()
    private val mapObjectsCallJournal = mutableListOf<ScenarioMapObjectsCall>()
    val mapObjectsCalls: List<ScenarioMapObjectsCall> get() = mapObjectsCallJournal
    val enemyEquipment = linkedMapOf<Int, List<Int>>()
    val itemVariables = mutableListOf<Pair<List<Int>, List<String>>>()
    val acquiredItems = mutableListOf<Int>()
    val nearEvents = mutableListOf<List<Int>>()
    val heads: MutableMap<Int, ScenarioHead> get() = movementCoordinator.heads
    val fires = linkedMapOf<Pair<Int, Int>, ScenarioFire>()
    val unitStatuses = mutableListOf<Map<String, Any?>>()
    val scriptedAttacks: MutableList<ScriptedAttackAction> get() = presentationCoordinator.scriptedAttacks
    val scriptedUnitActions: MutableList<ScriptedUnitAction> get() = presentationCoordinator.scriptedUnitActions
    val joinedUnits: MutableSet<Int> get() = campaign.joinedUnits
    val infoTransfers = mutableListOf<Pair<Int, String>>()
    val controlledInfos = mutableListOf<Pair<Int, String>>()
    val joinedEquipment = linkedMapOf<Int, ScenarioJoinEquipment>()
    val unitAttributes: MutableMap<Int, MutableMap<Int, Int>> get() = campaign.unitAttributes

    fun clearUnits() = unitRegistry.clearUnits()
    fun setMenuVisible(visible: Boolean) { menuVisible = visible }
    fun setStageName(name: String) { stageName = name }
    fun addAmbition(delta: Int) { ambition += delta }
    fun setWinCondition(text: String) { winCondition = text }
    fun showWinCondition(text: String) { showWinConditionRequested = text }
    fun consumeShowWinCondition(): String? = showWinConditionRequested.also { showWinConditionRequested = null }
    fun requestUnitHide(unitId: Int, hideType: Int) = presentationCoordinator.requestUnitHide(unitId, hideType)
    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? = presentationCoordinator.consumeUnitHideRequest()

    fun requestRectUnitHide(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int, hideType: Int): Int =
        presentationCoordinator.requestRectUnitHide(
            x1, y1, x2, y2, camp, hideType, battleUnits, mineMasterInstanceId,
        ) { unit, c -> with(unitRegistry) { unit.matchesAiCamp(c) } }

    fun completeUnitHide(request: ScenarioUnitHideRequest) =
        presentationCoordinator.completeUnitHide(request, battleUnits, ::unit, ::setBattleUnitVisibility)

    fun requestUnitShow(request: ScenarioUnitShowRequest) =
        presentationCoordinator.requestUnitShow(request, ::battleUnitForCharacterId)

    fun consumeUnitShowRequest(): ScenarioUnitShowRequest? = presentationCoordinator.consumeUnitShowRequest()

    fun setBattleUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 19,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0,
    ): CampaignUnitPostsChange? =
        presentationCoordinator.setBattleUnitPosts(
            unitId, posts, flags, data, enabledFeatures, campaign, ::unit, ::battleUnitForCharacterId,
        )

    fun setModelUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 3,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0,
    ): CampaignUnitPostsChange? =
        presentationCoordinator.setModelUnitPosts(unitId, posts, flags, data, enabledFeatures, campaign, ::unit)

    fun consumeUnitPostsRequest(): ScenarioUnitPostsRequest? = presentationCoordinator.consumeUnitPostsRequest()
    fun requestMapPresentation(request: ScenarioMapPresentationRequest) = presentationCoordinator.requestMapPresentation(request)
    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? = presentationCoordinator.consumeMapPresentationRequest()
    fun requestCameraCenter(x: Int, y: Int) = presentationCoordinator.requestCameraCenter(x, y)
    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> = presentationCoordinator.consumeCameraCenterRequests()
    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) = presentationCoordinator.requestScriptPresentation(request)
    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? = presentationCoordinator.consumeScriptPresentationRequest()
    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> = presentationCoordinator.consumeScriptPresentationRequests()
    fun setBattleUnitVisibility(unitId: Int, visible: Boolean) = unitRegistry.setBattleUnitVisibility(unitId, visible)
    fun setBottomText(text: String) { bottomText = text }
    fun selectBattleMap(index: Int) { battleMapIndex = index }

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
        battleWeatherType = weatherType
        battleWeatherOffset = weatherOffset
    }

    fun battleWeatherSchedule(): List<BattleWeather> = when (battleWeatherType) {
        0 -> listOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY, BattleWeather.WINDY, BattleWeather.WINDY, BattleWeather.HEAVY_RAIN)
        1 -> listOf(BattleWeather.CLEAR, BattleWeather.CLEAR, BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY, BattleWeather.HEAVY_RAIN)
        2 -> listOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY, BattleWeather.HEAVY_RAIN, BattleWeather.HEAVY_RAIN, BattleWeather.HEAVY_RAIN)
        3 -> listOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.CLOUDY, BattleWeather.CLOUDY, BattleWeather.WINDY, BattleWeather.HEAVY_RAIN)
        4 -> listOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY, BattleWeather.SNOW, BattleWeather.SNOW, BattleWeather.SNOW)
        5 -> listOf(BattleWeather.WINDY)
        7 -> listOf(BattleWeather.HEAVY_RAIN)
        8 -> listOf(BattleWeather.CLOUDY)
        else -> listOf(BattleWeather.CLEAR)
    }

    fun initialBattleWeather(): BattleWeather {
        val sequence = battleWeatherSchedule()
        return sequence[Math.floorMod(1 + battleWeatherOffset, sequence.size)]
    }

    fun initFight() = fightCoordinator.initFight()
    fun enableBattleMovementTimeline() { movementCoordinator.battleMovementTimeline = true }
    fun setBattleMovePathResolver(resolver: (Int, Int, Int) -> List<Pair<Int, Int>>?) {
        movementCoordinator.battleMovePathResolver = resolver
    }

    fun startOperation() { battleOperationStarted = true }

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

    fun addUnitLevels(unitId: Int, delta: Int, registeredFeatures: Int = 0): CampaignUnitLevelChange? =
        presentationCoordinator.addUnitLevels(unitId, delta, registeredFeatures, campaign)

    fun consumeScriptedUnitLevelChanges(): List<CampaignUnitLevelChange> =
        presentationCoordinator.consumeScriptedUnitLevelChanges()

    fun consumeScriptedUnitPostsChanges(): List<CampaignUnitPostsChange> =
        presentationCoordinator.consumeScriptedUnitPostsChanges()

    fun startFight(firstUnitId: Int, secondUnitId: Int, backgroundIndex: Int): Long =
        fightCoordinator.startFight(firstUnitId, secondUnitId, backgroundIndex)

    fun enqueueFightCommand(command: ScenarioFightCommand) = fightCoordinator.enqueueFightCommand(command)
    fun consumeFightCommands(): List<ScenarioFightCommand> = fightCoordinator.consumeFightCommands()
    fun drawBattle() { battleDrawRequested = true }
    fun setBackgroundSound(soundId: Int) = fightCoordinator.setBackgroundSound(soundId)
    fun effectSound(soundId: Int, mode: Int = 1) = fightCoordinator.effectSound(soundId, mode)
    fun consumeSoundEffects(): List<ScenarioSoundEffect> = fightCoordinator.consumeSoundEffects()
    fun incrementSceneIndex() { sceneIndex++ }
    fun setFace(faceId: Int) { face = faceId }
    fun setSection(number: Int, name: String) { section = number to name }

    fun joinUnit(unitId: Int) {
        campaign.joinedUnits += unitId
        campaign.inventory.ensureDefaultEquipment(unitId, GameDataCatalog.load())
    }

    fun setJoinBattle(minimum: Int, maximum: Int, required: List<Any?>, excluded: List<Any?>) {
        if (minimum < 1 || maximum < 1) return
        joinBattleLimit = ScenarioJoinBattleLimit(
            minimum,
            maximum,
            required.map { it.asIntOr(0) },
            excluded.map { it.asIntOr(0) },
        ).also { campaign.roster.configureBattleRoster(it) }
    }

    fun setBattlePositions(positions: List<Any?>) {
        battlePositions = positions.mapNotNull { raw ->
            val point = raw as? List<Any?> ?: return@mapNotNull null
            point.getOrNull(0).asIntOr(0) to point.getOrNull(1).asIntOr(0)
        }
    }

    fun infoTransfer(type: Int, payload: String, selectedUnitId: Int = 0) {
        infoTransfers += type to payload
        campaign.applyInfoTransfer(type, payload, selectedUnitId)
    }

    fun controlledInfo(type: Int, text: String) { controlledInfos += type to text }

    fun getItem(itemId: Int, suppliedCountOrLevel: Int = 0, addToInventory: Boolean = true): String {
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

    fun setJoinEquip(unitId: Int, weapon: Int, weaponLevel: Int, armor: Int, armorLevel: Int, auxiliary: Int) {
        joinedEquipment[unitId] = ScenarioJoinEquipment(unitId, weapon, weaponLevel, armor, armorLevel, auxiliary)
        campaign.inventory.setEquipment(unitId, weapon, weaponLevel, armor, armorLevel, auxiliary)
    }

    fun ending(id: Int) { endingId = id; campaign.applyInfoTransfer(22, id.toString()) }

    fun reward(bonusMoney: Int = 0, items: List<Any?> = emptyList(), end: Boolean = false) {
        rewardRequest = ScenarioRewardRequest(bonusMoney, items.map { it.asIntOr(0) }, end)
        scriptedBattleOutcome = BattleOutcome.PLAYER_VICTORY
    }

    fun consumeRewardRequest(): ScenarioRewardRequest? = rewardRequest.also { rewardRequest = null }
    fun lose() { scriptedBattleOutcome = BattleOutcome.ENEMY_VICTORY }
    fun endBattle() { battleEndedByScript = true }

    fun jumpScene(target: Int) {
        sceneJumpTarget = target
        val jumpOffset = (campaign.globalVariables[JUMP_OFFSET_GLOBAL] as? Number)?.toInt() ?: 0
        if (jumpOffset != 0) campaign.globalVariables[JUMP_OFFSET_GLOBAL] = 0
        sceneJumpStage = target + 1 + 200 * jumpOffset
    }

    fun resetLocalVariables() = Unit
    fun unitAttribute(unitId: Int, attribute: Int, default: Int = 0): Int =
        campaign.unitAttribute(unitId, attribute, default)

    fun setUnitAttribute(unitId: Int, attribute: Int, value: Int) =
        campaign.setUnitAttribute(unitId, attribute, value)

    fun changeUnitAttribute(unitId: Int, attribute: Int, operation: Int, value: Int) {
        val current = unitAttribute(unitId, attribute)
        setUnitAttribute(unitId, attribute, when (operation) {
            0 -> value
            1 -> current + value
            2 -> current - value
            else -> current
        })
    }

    fun head(id: Int): ScenarioHead = movementCoordinator.head(id)
    fun moveHead(id: Int, x: Int, y: Int): Float = movementCoordinator.moveHead(id, x, y)
    fun showHead(id: Int, x: Int, y: Int): Float = movementCoordinator.showHead(id, x, y)
    fun hideHead(id: Int): Float = movementCoordinator.hideHead(id)
    fun setFire(enabled: Boolean, x: Int, y: Int) { fires[x to y] = ScenarioFire(x, y, enabled) }
    fun setFires(enabled: Boolean, positions: List<Any?>) {
        positions.forEach { value ->
            val pair = value as? List<Any?> ?: return@forEach
            if (pair.size >= 2) setFire(enabled, pair[0].asIntOr(0), pair[1].asIntOr(0))
        }
    }

    fun setUnitStatuses(values: List<Any?>): List<Map<String, Any?>> {
        val normalized = values.mapNotNull { value ->
            @Suppress("UNCHECKED_CAST")
            (value as? Map<*, *>)?.entries?.associate { it.key.toString() to it.value }
        }
        unitStatuses += normalized
        return normalized
    }

    fun attackAction(attackerId: Int, targetId: Int, flag: Int) =
        presentationCoordinator.attackAction(attackerId, targetId, flag)

    fun setScriptedUnitAction(unitId: Int, action: Int, direction: Int = -1, loop: Boolean = false) =
        presentationCoordinator.setScriptedUnitAction(unitId, action, direction, loop, ::unit, ::setUnitDirection)

    fun consumeScriptedAttacks(): List<ScriptedAttackAction> = presentationCoordinator.consumeScriptedAttacks()
    fun consumeScriptedUnitActions(): List<ScriptedUnitAction> = presentationCoordinator.consumeScriptedUnitActions()
    fun consumeScriptedUnitDirections(): List<Pair<Int, Int>> = presentationCoordinator.consumeScriptedUnitDirections()
    fun consumeUnitStatuses(): List<Map<String, Any?>> = unitStatuses.toList().also { unitStatuses.clear() }

    fun addItemVariables(items: List<Any?>, locations: List<Any?>) {
        itemVariables += items.map { it.asIntOr(0) } to locations.map { it?.toString().orEmpty() }
    }

    fun addNearEvent(values: List<Any?>, flag: Int = 0) {
        val converted = values.map { it.asIntOr(0) }
        nearEvents += converted
        if (flag and 1 != 0) winConditionTalk = converted else winConditionVs = converted
    }

    fun setEnemyEquipment(unitId: Int, values: List<Any?>) { enemyEquipment[unitId] = values.map { it.asIntOr(0) } }

    fun setMapObjects(enabled: Boolean, terrainId: Int, positions: List<Any?>) {
        val objects = positions.mapNotNull { raw ->
            @Suppress("UNCHECKED_CAST")
            val values = raw as? List<Any?> ?: return@mapNotNull null
            if (values.size < 3) return@mapNotNull null
            ScenarioMapObjectsCall.Object(
                objectId = values[0].asIntOr(0),
                x = values[1].asIntOr(0),
                y = values[2].asIntOr(0),
            )
        }
        mapObjectsCallJournal += ScenarioMapObjectsCall(enabled, terrainId, objects)
        objects.forEach { objectValue ->
            mapObjects[objectValue.x to objectValue.y] = ScenarioMapObject(
                objectValue.x, objectValue.y, objectValue.objectId, terrainId, enabled,
            )
        }
    }

    fun countDirection(fromId: Int, toId: Int): Int = movementCoordinator.countDirection(fromId, toId, ::unit)

    fun createBattleUnits(faction: ScenarioUnitFaction, entries: List<Any?>) =
        unitRegistry.createBattleUnits(faction, entries, campaign)

    fun battleUnitForCharacterId(characterId: Int): ScenarioBattleUnit? =
        unitRegistry.battleUnitForCharacterId(characterId)

    fun battleUnitForSlot(battleSlot: Int): ScenarioBattleUnit? =
        unitRegistry.battleUnitForSlot(battleSlot)

    fun setBattleAi(
        camp: Int,
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        ai: Int,
        targetId: Int = -1,
        targetX: Int = 0,
        targetY: Int = 0,
    ) = unitRegistry.setBattleAi(camp, x1, y1, x2, y2, ai, targetId, targetX, targetY)

    fun setUnitAi(unitId: Int, ai: Int, targetId: Int = -1, targetX: Int = 0, targetY: Int = 0) =
        unitRegistry.setUnitAi(unitId, ai, targetId, targetX, targetY)

    fun setUnitRetreatTextEnabled(unitId: Int, enabled: Boolean) =
        unitRegistry.setUnitRetreatTextEnabled(unitId, enabled)

    fun hideBattleRect(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int) =
        unitRegistry.hideBattleRect(x1, y1, x2, y2, camp)

    fun apply(command: ScenarioCommand) {
        when (command) {
            is ScenarioCommand.LoadBackground -> {
                backgroundId = when (command.backgroundId) {
                    0 -> command.variant + 1
                    1 -> 115
                    2 -> command.variant + 41
                    else -> command.variant
                }
                backgroundVariant = command.variant
                movementCoordinator.hallPathGrid = if (command.backgroundId == 2) HallPathGrid.loadOrNull(command.variant) else null
            }
            is ScenarioCommand.SetEventName -> eventName = command.name
            is ScenarioCommand.ShowUnit -> unitRegistry.setUnit(command.unitId, command.x, command.y, command.direction) {
                presentationCoordinator.scriptedUnitDirections += it
            }
            is ScenarioCommand.MoveUnit -> moveUnit(command.unitId, command.x, command.y, command.direction)
            is ScenarioCommand.SetUnitAction -> setScriptedUnitAction(command.unitId, command.action)
            is ScenarioCommand.DialogueLine, is ScenarioCommand.Choose -> Unit
        }
    }

    fun unit(id: Int): TacticalUnit = unitRegistry.unit(id)
    fun seedBattleUnitPosition(id: Int, x: Int, y: Int) = unitRegistry.seedBattleUnitPosition(id, x, y)
    fun setUnitDirection(id: Int, direction: Int) = unitRegistry.setUnitDirection(id, direction) {
        presentationCoordinator.scriptedUnitDirections += it
    }

    fun moveDuration(id: Int, x: Int, y: Int): Float = movementCoordinator.moveDuration(id, x, y, units)
    fun moveUnits(requests: List<ScenarioCommand.MoveUnit>): Float =
        movementCoordinator.moveUnits(requests, units) { presentationCoordinator.scriptedUnitDirections += it }

    private fun moveUnit(id: Int, x: Int, y: Int, direction: Int) =
        movementCoordinator.moveUnit(id, x, y, direction, units) { presentationCoordinator.scriptedUnitDirections += it }

    fun updateAnimations(delta: Float) = movementCoordinator.updateAnimations(delta, units)
    fun finishAnimations() = movementCoordinator.finishAnimations(units)

    private fun Any?.asIntOr(default: Int): Int = when (this) {
        is Number -> toInt()
        is Boolean -> if (this) 1 else 0
        is String -> toIntOrNull() ?: default
        else -> default
    }
}
