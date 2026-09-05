package com.jojo.game
import com.jojo.game.domain.campaign.*

/** Minimal LibGDX-side replacement for the scenario-visible Stage state. */
class ScenarioStage(private val campaign: CampaignState = CampaignState()) {
    private val unitRegistry = ScenarioStageUnitRegistry()
    private val movementCoordinator = ScenarioStageMovementCoordinator()
    private val presentationCoordinator = ScenarioStagePresentationCoordinator()
    private val fightCoordinator = ScenarioStageFightCoordinator()
    private val battleSetup = ScenarioStageBattleSetup(campaign)
    private val worldState = ScenarioStageWorldState()

    // --- Battle setup delegated properties ---
    var battleMapIndex: Int
        get() = battleSetup.battleMapIndex
        set(value) {
            battleSetup.battleMapIndex = value
        }
    val battleMaxRounds: Int get() = battleSetup.battleMaxRounds
    val battleMaxRoundsIncludesFeature: Boolean get() = battleSetup.battleMaxRoundsIncludesFeature
    val battleLevelOffset: Int get() = battleSetup.battleLevelOffset
    val enemyMasterInstanceId: Int get() = battleSetup.enemyMasterInstanceId
    val mineMasterInstanceId: Int get() = battleSetup.mineMasterInstanceId
    val battleWeatherType: Int get() = battleSetup.battleWeatherType
    val battleWeatherOffset: Int get() = battleSetup.battleWeatherOffset
    val battleOperationStarted: Boolean get() = battleSetup.battleOperationStarted
    val battleDrawRequested: Boolean get() = battleSetup.battleDrawRequested
    val joinBattleLimit: ScenarioJoinBattleLimit? get() = battleSetup.joinBattleLimit
    val battlePositions: List<Pair<Int, Int>> get() = battleSetup.battlePositions
    val scriptedBattleOutcome: BattleOutcome? get() = battleSetup.scriptedBattleOutcome
    val battleEndedByScript: Boolean get() = battleSetup.battleEndedByScript
    val winCondition: String get() = battleSetup.winCondition
    val showWinConditionRequested: String? get() = battleSetup.showWinConditionRequested
    val winConditionVs: List<Int>? get() = battleSetup.winConditionVs
    val winConditionTalk: List<Int>? get() = battleSetup.winConditionTalk
    val rewardRequest: ScenarioRewardRequest? get() = battleSetup.rewardRequest
    val nearEvents: MutableList<List<Int>> get() = battleSetup.nearEvents
    val enemyEquipment: MutableMap<Int, List<Int>> get() = battleSetup.enemyEquipment

    // --- World state delegated properties ---
    val mapObjects get() = worldState.mapObjects
    val mapObjectsCalls get() = worldState.mapObjectsCalls
    val fires get() = worldState.fires
    val itemVariables get() = worldState.itemVariables
    val acquiredItems get() = worldState.acquiredItems
    val unitStatuses get() = worldState.unitStatuses
    val infoTransfers get() = worldState.infoTransfers
    val controlledInfos get() = worldState.controlledInfos

    // --- Scenario-local properties ---
    var backgroundId: Int = 0; private set
    var backgroundVariant: Int = 0; private set
    var eventName: String = ""; private set
    var stageName: String = ""; private set
    var menuVisible: Boolean = true; private set
    var ambition: Int = 50; private set
    val lastBattleUnitPostsRequiresPause: Boolean get() = presentationCoordinator.lastBattleUnitPostsRequiresPause
    var bottomText: String = ""; private set
    val fightInitialized: Boolean get() = fightCoordinator.fightInitialized
    val backgroundSound: Int get() = fightCoordinator.backgroundSound
    var sceneIndex: Int = 0; private set
    var face: Int = 0; private set
    var section: Pair<Int, String>? = null; private set
    var endingId: Int? = null; private set
    var sceneJumpTarget: Int? = null; private set
    var sceneJumpStage: Int? = null; private set
    val activeFightId: Long? get() = fightCoordinator.activeFightId
    val units: MutableMap<Int, TacticalUnit> get() = unitRegistry.units
    val battleUnits: MutableMap<String, ScenarioBattleUnit> get() = unitRegistry.battleUnits
    val heads: MutableMap<Int, ScenarioHead> get() = movementCoordinator.heads
    val scriptedAttacks: MutableList<ScriptedAttackAction> get() = presentationCoordinator.scriptedAttacks
    val scriptedUnitActions: MutableList<ScriptedUnitAction> get() = presentationCoordinator.scriptedUnitActions
    val joinedUnits: MutableSet<Int> get() = campaign.joinedUnits
    val joinedEquipment = linkedMapOf<Int, ScenarioJoinEquipment>()
    val unitAttributes: MutableMap<Int, MutableMap<Int, Int>> get() = campaign.unitAttributes

    // --- Simple setters ---
    fun clearUnits() = unitRegistry.clearUnits()
    fun setMenuVisible(visible: Boolean) {
        menuVisible = visible
    }

    fun setStageName(name: String) {
        stageName = name
    }

    fun addAmbition(delta: Int) {
        ambition += delta
    }

    fun setBottomText(text: String) {
        bottomText = text
    }

    fun incrementSceneIndex() {
        sceneIndex++
    }

    fun setFace(faceId: Int) {
        face = faceId
    }

    fun setSection(number: Int, name: String) {
        section = number to name
    }

    // --- Battle setup delegated methods ---
    fun setWinCondition(text: String) = battleSetup.setWinCondition(text)
    fun showWinCondition(text: String) = battleSetup.showWinCondition(text)
    fun consumeShowWinCondition(): String? = battleSetup.consumeShowWinCondition()
    fun selectBattleMap(index: Int) = battleSetup.selectBattleMap(index)
    fun setBattleGlobalData(
        maxRounds: Int,
        levelOffset: Int,
        enemyMaster: Int = -1,
        mineMaster: Int = 0,
        weatherType: Int = 6,
        weatherOffset: Int = 0
    ) = battleSetup.setBattleGlobalData(maxRounds, levelOffset, enemyMaster, mineMaster, weatherType, weatherOffset)

    fun battleWeatherSchedule(): List<BattleWeather> = battleSetup.battleWeatherSchedule()
    fun initialBattleWeather(): BattleWeather = battleSetup.initialBattleWeather()
    fun startOperation() = battleSetup.startOperation()
    fun setMaxRound(maxRounds: Int, enabledFeatures: Int = 0): Boolean =
        battleSetup.setMaxRound(maxRounds, enabledFeatures)

    fun drawBattle() = battleSetup.drawBattle()
    fun setJoinBattle(minimum: Int, maximum: Int, required: List<Any?>, excluded: List<Any?>) =
        battleSetup.setJoinBattle(minimum, maximum, required, excluded)

    fun setBattlePositions(positions: List<Any?>) = battleSetup.setBattlePositions(positions)
    fun reward(bonusMoney: Int = 0, items: List<Any?> = emptyList(), end: Boolean = false) =
        battleSetup.reward(bonusMoney, items, end)

    fun consumeRewardRequest(): ScenarioRewardRequest? = battleSetup.consumeRewardRequest()
    fun lose() = battleSetup.lose()
    fun endBattle() = battleSetup.endBattle()
    fun addNearEvent(values: List<Any?>, flag: Int = 0) = battleSetup.addNearEvent(values, flag)
    fun setEnemyEquipment(unitId: Int, values: List<Any?>) = battleSetup.setEnemyEquipment(unitId, values)
    fun joinUnit(unitId: Int) = battleSetup.joinUnit(unitId)
    fun getItem(itemId: Int, suppliedCountOrLevel: Int = 0, addToInventory: Boolean = true): String =
        battleSetup.getItem(itemId, suppliedCountOrLevel, addToInventory, acquiredItems)

    fun battleItemCompletionMessage(itemId: Int): String = battleSetup.battleItemCompletionMessage(itemId)
    fun setJoinEquip(unitId: Int, weapon: Int, weaponLevel: Int, armor: Int, armorLevel: Int, auxiliary: Int) =
        battleSetup.setJoinEquip(unitId, weapon, weaponLevel, armor, armorLevel, auxiliary, joinedEquipment)

    fun ending(id: Int) = battleSetup.ending(id) { endingId = it }
    fun infoTransfer(type: Int, payload: String, selectedUnitId: Int = 0) =
        battleSetup.infoTransfer(type, payload, selectedUnitId, infoTransfers)

    fun jumpScene(target: Int) = battleSetup.jumpScene(target, { sceneJumpTarget = it }, { sceneJumpStage = it })
    fun resetLocalVariables() = Unit
    fun unitAttribute(unitId: Int, attribute: Int, default: Int = 0): Int =
        battleSetup.unitAttribute(unitId, attribute, default)

    fun setUnitAttribute(unitId: Int, attribute: Int, value: Int) =
        battleSetup.setUnitAttribute(unitId, attribute, value)

    fun changeUnitAttribute(unitId: Int, attribute: Int, operation: Int, value: Int) =
        battleSetup.changeUnitAttribute(unitId, attribute, operation, value)

    // --- World state delegated methods ---
    fun setFire(enabled: Boolean, x: Int, y: Int) = worldState.setFire(enabled, x, y)
    fun setFires(enabled: Boolean, positions: List<Any?>) = worldState.setFires(enabled, positions)
    fun setMapObjects(enabled: Boolean, terrainId: Int, positions: List<Any?>) =
        worldState.setMapObjects(enabled, terrainId, positions)

    fun setUnitStatuses(values: List<Any?>): List<Map<String, Any?>> = worldState.setUnitStatuses(values)
    fun consumeUnitStatuses(): List<Map<String, Any?>> = worldState.consumeUnitStatuses()
    fun addItemVariables(items: List<Any?>, locations: List<Any?>) = worldState.addItemVariables(items, locations)
    fun controlledInfo(type: Int, text: String) = worldState.controlledInfo(type, text)

    // --- Presentation coordinator delegated methods ---
    fun requestUnitHide(unitId: Int, hideType: Int) = presentationCoordinator.requestUnitHide(unitId, hideType)
    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? = presentationCoordinator.consumeUnitHideRequest()
    fun requestRectUnitHide(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int, hideType: Int): Int =
        presentationCoordinator.requestRectUnitHide(
            x1,
            y1,
            x2,
            y2,
            camp,
            hideType,
            battleUnits,
            mineMasterInstanceId
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
        enabledFeatures: Int = 0
    ): CampaignUnitPostsChange? =
        presentationCoordinator.setBattleUnitPosts(
            unitId,
            posts,
            flags,
            data,
            enabledFeatures,
            campaign,
            ::unit,
            ::battleUnitForCharacterId
        )

    fun setModelUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 3,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0
    ): CampaignUnitPostsChange? =
        presentationCoordinator.setModelUnitPosts(unitId, posts, flags, data, enabledFeatures, campaign, ::unit)

    fun consumeUnitPostsRequest(): ScenarioUnitPostsRequest? = presentationCoordinator.consumeUnitPostsRequest()
    fun requestMapPresentation(request: ScenarioMapPresentationRequest) =
        presentationCoordinator.requestMapPresentation(request)

    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? =
        presentationCoordinator.consumeMapPresentationRequest()

    fun requestCameraCenter(x: Int, y: Int) = presentationCoordinator.requestCameraCenter(x, y)
    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> =
        presentationCoordinator.consumeCameraCenterRequests()

    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) =
        presentationCoordinator.requestScriptPresentation(request)

    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? =
        presentationCoordinator.consumeScriptPresentationRequest()

    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> =
        presentationCoordinator.consumeScriptPresentationRequests()

    fun setBattleUnitVisibility(unitId: Int, visible: Boolean) = unitRegistry.setBattleUnitVisibility(unitId, visible)
    fun addUnitLevels(unitId: Int, delta: Int, registeredFeatures: Int = 0): CampaignUnitLevelChange? =
        presentationCoordinator.addUnitLevels(unitId, delta, registeredFeatures, campaign)

    fun consumeScriptedUnitLevelChanges(): List<CampaignUnitLevelChange> =
        presentationCoordinator.consumeScriptedUnitLevelChanges()

    fun consumeScriptedUnitPostsChanges(): List<CampaignUnitPostsChange> =
        presentationCoordinator.consumeScriptedUnitPostsChanges()

    fun attackAction(attackerId: Int, targetId: Int, flag: Int) =
        presentationCoordinator.attackAction(attackerId, targetId, flag)

    fun setScriptedUnitAction(unitId: Int, action: Int, direction: Int = -1, loop: Boolean = false) =
        presentationCoordinator.setScriptedUnitAction(unitId, action, direction, loop, ::unit, ::setUnitDirection)

    fun consumeScriptedAttacks(): List<ScriptedAttackAction> = presentationCoordinator.consumeScriptedAttacks()
    fun consumeScriptedUnitActions(): List<ScriptedUnitAction> = presentationCoordinator.consumeScriptedUnitActions()
    fun consumeScriptedUnitDirections(): List<Pair<Int, Int>> = presentationCoordinator.consumeScriptedUnitDirections()

    // --- Fight coordinator delegated methods ---
    fun initFight() = fightCoordinator.initFight()
    fun startFight(firstUnitId: Int, secondUnitId: Int, backgroundIndex: Int): Long =
        fightCoordinator.startFight(firstUnitId, secondUnitId, backgroundIndex)

    fun enqueueFightCommand(command: ScenarioFightCommand) = fightCoordinator.enqueueFightCommand(command)
    fun consumeFightCommands(): List<ScenarioFightCommand> = fightCoordinator.consumeFightCommands()
    fun setBackgroundSound(soundId: Int) = fightCoordinator.setBackgroundSound(soundId)
    fun effectSound(soundId: Int, mode: Int = 1) = fightCoordinator.effectSound(soundId, mode)
    fun consumeSoundEffects(): List<ScenarioSoundEffect> = fightCoordinator.consumeSoundEffects()

    // --- Movement coordinator delegated methods ---
    fun enableBattleMovementTimeline() {
        movementCoordinator.battleMovementTimeline = true
    }

    fun setBattleMovePathResolver(resolver: (Int, Int, Int) -> List<Pair<Int, Int>>?) {
        movementCoordinator.battleMovePathResolver = resolver
    }

    fun head(id: Int): ScenarioHead = movementCoordinator.head(id)
    fun moveHead(id: Int, x: Int, y: Int): Float = movementCoordinator.moveHead(id, x, y)
    fun showHead(id: Int, x: Int, y: Int): Float = movementCoordinator.showHead(id, x, y)
    fun hideHead(id: Int): Float = movementCoordinator.hideHead(id)
    fun countDirection(fromId: Int, toId: Int): Int = movementCoordinator.countDirection(fromId, toId, ::unit)
    fun moveDuration(id: Int, x: Int, y: Int): Float = movementCoordinator.moveDuration(id, x, y, units)
    fun moveUnits(requests: List<ScenarioCommand.MoveUnit>): Float =
        movementCoordinator.moveUnits(requests, units) { presentationCoordinator.scriptedUnitDirections += it }

    fun updateAnimations(delta: Float) = movementCoordinator.updateAnimations(delta, units)
    fun finishAnimations() = movementCoordinator.finishAnimations(units)

    // --- Unit registry delegated methods ---
    fun createBattleUnits(faction: ScenarioUnitFaction, entries: List<Any?>) =
        unitRegistry.createBattleUnits(faction, entries, campaign)

    fun battleUnitForCharacterId(characterId: Int): ScenarioBattleUnit? =
        unitRegistry.battleUnitForCharacterId(characterId)

    fun battleUnitForSlot(battleSlot: Int): ScenarioBattleUnit? = unitRegistry.battleUnitForSlot(battleSlot)
    fun setBattleAi(
        camp: Int,
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        ai: Int,
        targetId: Int = -1,
        targetX: Int = 0,
        targetY: Int = 0
    ) = unitRegistry.setBattleAi(camp, x1, y1, x2, y2, ai, targetId, targetX, targetY)

    fun setUnitAi(unitId: Int, ai: Int, targetId: Int = -1, targetX: Int = 0, targetY: Int = 0) =
        unitRegistry.setUnitAi(unitId, ai, targetId, targetX, targetY)

    fun setUnitRetreatTextEnabled(unitId: Int, enabled: Boolean) =
        unitRegistry.setUnitRetreatTextEnabled(unitId, enabled)

    fun hideBattleRect(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int) =
        unitRegistry.hideBattleRect(x1, y1, x2, y2, camp)

    fun unit(id: Int): TacticalUnit = unitRegistry.unit(id)
    fun seedBattleUnitPosition(id: Int, x: Int, y: Int) = unitRegistry.seedBattleUnitPosition(id, x, y)
    fun setUnitDirection(id: Int, direction: Int) =
        unitRegistry.setUnitDirection(id, direction) { presentationCoordinator.scriptedUnitDirections += it }

    // --- Command dispatch ---
    fun apply(command: ScenarioCommand) {
        when (command) {
            is ScenarioCommand.LoadBackground -> {
                backgroundId = when (command.backgroundId) {
                    0 -> command.variant + 1; 1 -> 115; 2 -> command.variant + 41; else -> command.variant
                }
                backgroundVariant = command.variant
                movementCoordinator.hallPathGrid =
                    if (command.backgroundId == 2) HallPathGrid.loadOrNull(command.variant) else null
            }

            is ScenarioCommand.SetEventName -> eventName = command.name
            is ScenarioCommand.ShowUnit -> unitRegistry.setUnit(
                command.unitId,
                command.x,
                command.y,
                command.direction
            ) { presentationCoordinator.scriptedUnitDirections += it }

            is ScenarioCommand.MoveUnit -> movementCoordinator.moveUnit(
                command.unitId,
                command.x,
                command.y,
                command.direction,
                units
            ) { presentationCoordinator.scriptedUnitDirections += it }

            is ScenarioCommand.SetUnitAction -> setScriptedUnitAction(command.unitId, command.action)
            is ScenarioCommand.DialogueLine, is ScenarioCommand.Choose -> Unit
        }
    }
}
