package com.jojo.game

import java.util.ArrayDeque

private const val SCENARIO_ENABLED_FEATURE_ZJHH = 8
private const val JUMP_OFFSET_GLOBAL = 4051


/** Minimal LibGDX-side replacement for the scenario-visible Stage state. */
class ScenarioStage(private val campaign: CampaignState = CampaignState()) {
    private var hallPathGrid: HallPathGrid? = null
    private var battleMovementTimeline: Boolean = false
    private var battleMovePathResolver: ((Int, Int, Int) -> List<Pair<Int, Int>>?)? = null
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
    /** stage.showWinCondition is a separate modal request, not setWinCondition. */
    var showWinConditionRequested: String? = null
        private set
    var rewardRequest: ScenarioRewardRequest? = null
        private set
    private val unitHideRequests = ArrayDeque<ScenarioUnitHideRequest>()
    private var unitShowRequest: ScenarioUnitShowRequest? = null
    private val unitPostsRequests = ArrayDeque<ScenarioUnitPostsRequest>()
    /** Result of the immediately preceding Stage/BattleUnit setPosts call. */
    var lastBattleUnitPostsRequiresPause: Boolean = false
        private set
    private var mapPresentationRequest: ScenarioMapPresentationRequest? = null
    private val cameraCenterRequests = ArrayDeque<ScenarioCameraCenterRequest>()
    private val scriptPresentationRequests = ArrayDeque<ScenarioScriptPresentationRequest>()
    /** BattleScreen._winConditions fields, assembled by winConProcess. */
    var winConditionVs: List<Int>? = null
        private set
    var winConditionTalk: List<Int>? = null
        private set
    var bottomText: String = ""
        private set
    var battleMapIndex: Int = 0
    /** BattleScreen.setGlobalData's first argument, the authoritative turn cap. */
    var battleMaxRounds: Int = 99
        private set
    var battleMaxRoundsIncludesFeature: Boolean = false
        private set
    var battleLevelOffset: Int = 0
        private set
    /**
     * Historical game name retained for callers.  BattleScreen stores a
     * character ID here and resolves it through `_unitIds`, never an `i` or
     * global `_unitSet` index.
     */
    var enemyMasterInstanceId: Int = -1
        private set
    var mineMasterInstanceId: Int = 0
        private set
    var battleWeatherType: Int = 6
        private set
    var battleWeatherOffset: Int = 0
        private set
    var fightInitialized: Boolean = false
        private set
    /** BattleScreen START_OPER flag. It is distinct from initFight(). */
    var battleOperationStarted: Boolean = false
        private set
    /**
     * BattleScreen.draw() is the source boundary that removes the opaque
     * BattleInitLayer after scene0's preparation delay.  Keep it as stage
     * state so rendering and input observe the same transition as the script.
     */
    var battleDrawRequested: Boolean = false
        private set
    var backgroundSound: Int = -1
        private set
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
    /** Source StageLayer.jumpScene argument (zero-based stage scene number). */
    var sceneJumpTarget: Int? = null
        private set
    /**
     * Model stage written eagerly by StageLayer/HallLayer.jumpScene.  The
     * source increments the script argument, applies and clears Global4051,
     * then calls Model.setStage before the replacement screen is entered.
     */
    var sceneJumpStage: Int? = null
        private set
    /** Source StageLayer end/reward/lose calls, including non-annihilation battle endings. */
    var scriptedBattleOutcome: BattleOutcome? = null
        private set
    var battleEndedByScript: Boolean = false
        private set
    private val pendingSoundEffects = mutableListOf<ScenarioSoundEffect>()
    private val fightCommands = ArrayDeque<ScenarioFightCommand>()
    private var nextFightId = 1L
    private var activeFightPreviousBackgroundSound: Int? = null
    var activeFightId: Long? = null
        private set
    /** Mirrors StageLayer._effSoundIdx: every new scripted effect releases its prior active instance. */
    private var activeEffectSoundId: Int = -1
    val units = linkedMapOf<Int, TacticalUnit>()
    /** Keys are camp-local source-slot offsets: ENEMY:0, ENEMY:80, ENEMY:160. */
    val battleUnits = linkedMapOf<String, ScenarioBattleUnit>()
    /** BattleScreen._unitIds: a character ID always resolves to its first actor. */
    private val firstBattleUnitKeyByCharacterId = linkedMapOf<Int, String>()
    val mapObjects = linkedMapOf<Pair<Int, Int>, ScenarioMapObject>()
    /**
     * Append-only source-call journal. Unlike scriptPresentationRequests it
     * includes pre-draw construction and disabled type 0..3 calls which
     * intentionally have no visible tween.
     */
    private val mapObjectsCallJournal = mutableListOf<ScenarioMapObjectsCall>()
    val mapObjectsCalls: List<ScenarioMapObjectsCall> get() = mapObjectsCallJournal
    val enemyEquipment = linkedMapOf<Int, List<Int>>()
    val itemVariables = mutableListOf<Pair<List<Int>, List<String>>>()
    val acquiredItems = mutableListOf<Int>()
    val nearEvents = mutableListOf<List<Int>>()
    val heads = linkedMapOf<Int, ScenarioHead>()
    val fires = linkedMapOf<Pair<Int, Int>, ScenarioFire>()
    val unitStatuses = mutableListOf<Map<String, Any?>>()
    val scriptedAttacks = mutableListOf<ScriptedAttackAction>()
    /** Ordered visual actions emitted by StageLayer.unit(id).setAction(). */
    val scriptedUnitActions = mutableListOf<ScriptedUnitAction>()
    private val scriptedUnitLevelChanges = ArrayDeque<CampaignUnitLevelChange>()
    private val scriptedUnitPostsChanges = ArrayDeque<CampaignUnitPostsChange>()
    /**
     * Directions which an event script explicitly changed.  Do not infer this
     * from every TacticalUnit: a Stage proxy is also created for bookkeeping
     * and its default must not overwrite BattleScreen's authored spawn facing.
     */
    private val scriptedUnitDirections = mutableListOf<Pair<Int, Int>>()
    val joinedUnits: MutableSet<Int> get() = campaign.joinedUnits
    val infoTransfers = mutableListOf<Pair<Int, String>>()
    /** Model.info(INFO_CTRL, text): persistent journal route with no modal pause. */
    val controlledInfos = mutableListOf<Pair<Int, String>>()
    val joinedEquipment = linkedMapOf<Int, ScenarioJoinEquipment>()
    /** Original Model.unitAttr2-compatible persistent unit attributes keyed by unit id and attribute index. */
    val unitAttributes: MutableMap<Int, MutableMap<Int, Int>> get() = campaign.unitAttributes

    fun clearUnits() = units.clear()
    fun setMenuVisible(visible: Boolean) { menuVisible = visible }
    fun setStageName(name: String) { stageName = name }
    fun addAmbition(delta: Int) { ambition += delta }
    fun setWinCondition(text: String) { winCondition = text }
    fun showWinCondition(text: String) { showWinConditionRequested = text }
    fun consumeShowWinCondition(): String? = showWinConditionRequested.also { showWinConditionRequested = null }
    fun requestUnitHide(unitId: Int, hideType: Int) {
        unitHideRequests.addLast(ScenarioUnitHideRequest(unitId, hideType.coerceIn(0, 2)))
    }
    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? =
        if (unitHideRequests.isEmpty()) null else unitHideRequests.removeFirst()

    /**
     * Source searchUnitByRect returns visible instances sorted by y*100+x.
     * The enclosing setRectUnitHide pauses only when that list is non-empty,
     * then ctrlUnitHide serializes every dialogue/action under one callback.
     */
    fun requestRectUnitHide(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int, hideType: Int): Int {
        val selected = battleUnits.entries
            .filter { (_, unit) ->
                !unit.hidden && unit.matchesAiCamp(camp) &&
                    unit.x in minOf(x1, x2)..maxOf(x1, x2) &&
                    unit.y in minOf(y1, y2)..maxOf(y1, y2)
            }
            .sortedWith(compareBy({ it.value.y }, { it.value.x }))
        var effectiveHideType = hideType.coerceIn(0, 2)
        selected.forEachIndexed { index, (_, unit) ->
            val showsRetireMessage = effectiveHideType == 1
            // unitHide mutates its shared `r` after the master's optional
            // retire line; every later unit therefore uses DEATH as well.
            if (showsRetireMessage && unit.faction == ScenarioUnitFaction.MINE &&
                unit.characterId == mineMasterInstanceId
            ) effectiveHideType = 2
            unitHideRequests.addLast(
                ScenarioUnitHideRequest(
                    unitId = unit.characterId,
                    hideType = effectiveHideType,
                    battleUnitId = unit.battleId,
                    resumesScript = index == selected.lastIndex,
                    showsRetireMessage = showsRetireMessage,
                ),
            )
        }
        return selected.size
    }

    fun completeUnitHide(request: ScenarioUnitHideRequest) {
        val exact = request.battleUnitId
        if (exact == null) {
            setBattleUnitVisibility(request.unitId, false)
            return
        }
        battleUnits.values.firstOrNull {
            it.battleId == exact
        }?.hidden = true
        unit(request.unitId).visible = battleUnits.values.any {
            it.characterId == request.unitId && !it.hidden
        }
    }
    fun requestUnitShow(request: ScenarioUnitShowRequest) {
        check(unitShowRequest == null) { "unit show callback is already pending" }
        unitShowRequest = request
        battleUnitForCharacterId(request.unitId)?.hidden = false
    }
    fun consumeUnitShowRequest(): ScenarioUnitShowRequest? = unitShowRequest.also { unitShowRequest = null }

    /**
     * `stage.unit(id).setPosts` dispatches through the live BattleUnit, whose
     * default flags are 19.  `testAvatar()` is synchronous: only an actual
     * old/new group change creates a callback request (and therefore a pause).
     */
    fun setBattleUnitPosts(unitId: Int, posts: Int, flags: Int = 19, data: GameDataCatalog = GameDataCatalog.load(), enabledFeatures: Int = 0): CampaignUnitPostsChange? {
        lastBattleUnitPostsRequiresPause = false
        val oldPosts = campaign.unitAttribute(unitId, 17, data.unitProfile(unitId)?.posts ?: 0)
        val change = campaign.setUnitPosts(unitId, posts, flags, data, enabledFeatures) ?: return null
        unit(unitId).posts = posts
        scriptedUnitPostsChanges.addLast(change)
        val battleUnit = battleUnitForCharacterId(unitId) ?: return change
        val oldAvatar = battleAvatarId(battleUnit, oldPosts, data)
        val newAvatar = battleAvatarId(battleUnit, posts, data)
        // BattleUnit.testAvatar() also returns false before its Cocos node is
        // created.  A Stage battle record represents the corresponding live
        // node; no record means there is no avatar callback to await.
        if (oldAvatar != null && newAvatar != null && oldAvatar != newAvatar) {
            val pausesScript = flags and 16 != 0
            unitPostsRequests.addLast(ScenarioUnitPostsRequest(unitId, oldAvatar, newAvatar, pausesScript))
            lastBattleUnitPostsRequiresPause = pausesScript
        }
        return change
    }

    /** Direct `model.unit(id).setPosts`: Unit's source default is 3, not 19. */
    fun setModelUnitPosts(
        unitId: Int,
        posts: Int,
        flags: Int = 3,
        data: GameDataCatalog = GameDataCatalog.load(),
        enabledFeatures: Int = 0,
    ): CampaignUnitPostsChange? {
        val change = campaign.setUnitPosts(unitId, posts, flags, data, enabledFeatures) ?: return null
        unit(unitId).posts = posts
        scriptedUnitPostsChanges.addLast(change)
        return change
    }

    fun consumeUnitPostsRequest(): ScenarioUnitPostsRequest? =
        if (unitPostsRequests.isEmpty()) null else unitPostsRequests.removeFirst()
    fun requestMapPresentation(request: ScenarioMapPresentationRequest) {
        check(mapPresentationRequest == null) { "map presentation callback is already pending" }
        mapPresentationRequest = request
    }
    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? =
        mapPresentationRequest.also { mapPresentationRequest = null }
    fun requestCameraCenter(x: Int, y: Int) {
        cameraCenterRequests.addLast(ScenarioCameraCenterRequest(x, y))
    }
    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> =
        cameraCenterRequests.toList().also { cameraCenterRequests.clear() }
    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) {
        scriptPresentationRequests.addLast(request)
    }
    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? =
        if (scriptPresentationRequests.isEmpty()) null else scriptPresentationRequests.removeFirst()
    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> =
        scriptPresentationRequests.toList().also { scriptPresentationRequests.clear() }
    fun setBattleUnitVisibility(unitId: Int, visible: Boolean) {
        unit(unitId).visible = visible
        battleUnitForCharacterId(unitId)?.hidden = !visible
    }
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
    fun initFight() { fightInitialized = true }
    /** BattleUnit.move2 uses .08 s/tile plus its authored .1 s idle callback. */
    fun enableBattleMovementTimeline() { battleMovementTimeline = true }

    /**
     * BattleUnit.move uses BattleScreen.findEmptyPos + its terrain-weighted AStar.
     * Keep HallPathfinder as the hall-scene implementation, but let the owning
     * battle provide that source path without committing model coordinates.
     */
    fun setBattleMovePathResolver(resolver: (Int, Int, Int) -> List<Pair<Int, Int>>?) {
        battleMovePathResolver = resolver
    }
    fun startOperation() { battleOperationStarted = true }
    /** BattleScreen.setMaxRound: ZJHH adds four, and setProperty skips an equal write. */
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
        campaign.addUnitLevels(unitId, delta, GameDataCatalog.load(), registeredFeatures)?.also(scriptedUnitLevelChanges::addLast)

    fun consumeScriptedUnitLevelChanges(): List<CampaignUnitLevelChange> =
        scriptedUnitLevelChanges.toList().also { scriptedUnitLevelChanges.clear() }
    fun consumeScriptedUnitPostsChanges(): List<CampaignUnitPostsChange> =
        scriptedUnitPostsChanges.toList().also { scriptedUnitPostsChanges.clear() }

    private fun battleAvatarId(unit: ScenarioBattleUnit, posts: Int, data: GameDataCatalog): Int? {
        val faction = when (unit.faction) {
            ScenarioUnitFaction.MINE -> Faction.PLAYER
            ScenarioUnitFaction.FRIEND -> Faction.FRIEND
            ScenarioUnitFaction.ENEMY -> if (unit.reinforcement) Faction.REINFORCEMENTS else Faction.ENEMY
        }
        val armId = if (posts < 60) posts.floorDiv(3) else posts - 40
        return BattleAvatarResolver.resolve(data, unit.characterId, posts, armId, faction)
    }
    fun startFight(firstUnitId: Int, secondUnitId: Int, backgroundIndex: Int): Long {
        check(activeFightId == null) { "a scripted fight is already active" }
        val fightId = nextFightId++
        activeFightId = fightId
        activeFightPreviousBackgroundSound = backgroundSound
        fightCommands.addLast(ScenarioFightCommand.Start(
            fightId = fightId,
            firstUnitId = firstUnitId,
            secondUnitId = secondUnitId,
            backgroundIndex = backgroundIndex,
            previousBackgroundSound = backgroundSound,
        ))
        // BattleScreen.startFight switches to ENTER_DANTIAO until fight.end().
        backgroundSound = 8
        return fightId
    }
    fun enqueueFightCommand(command: ScenarioFightCommand) {
        check(activeFightId == command.fightId) { "fight command does not target the active fight" }
        fightCommands.addLast(command)
        if (command is ScenarioFightCommand.End) {
            activeFightPreviousBackgroundSound?.let { backgroundSound = it }
            activeFightPreviousBackgroundSound = null
            activeFightId = null
        }
    }
    fun consumeFightCommands(): List<ScenarioFightCommand> = fightCommands.toList().also { fightCommands.clear() }
    fun drawBattle() { battleDrawRequested = true }
    fun setBackgroundSound(soundId: Int) { backgroundSound = soundId }
    /** Retains StageLayer.effectSound calls until the LibGDX presentation consumes them. */
    fun effectSound(soundId: Int, mode: Int = 1) {
        if (activeEffectSoundId != -1) pendingSoundEffects += ScenarioSoundEffect(activeEffectSoundId, 0)
        activeEffectSoundId = if (mode == 0) -1 else soundId
        if (mode > 0) pendingSoundEffects += ScenarioSoundEffect(soundId, mode)
    }
    fun consumeSoundEffects(): List<ScenarioSoundEffect> = pendingSoundEffects.toList().also { pendingSoundEffects.clear() }
    fun incrementSceneIndex() { sceneIndex++ }
    fun setFace(faceId: Int) { face = faceId }
    fun setSection(number: Int, name: String) { section = number to name }
    fun joinUnit(unitId: Int) {
        campaign.joinedUnits += unitId
        // Source Unit construction equips concrete default Item instances
        // before Model.unitJoin.  Persist that initial item level now so a
        // later unit level-up resets item skills without selecting stronger
        // default equipment again.
        campaign.inventory.ensureDefaultEquipment(unitId, GameDataCatalog.load())
    }
    fun setJoinBattle(minimum: Int, maximum: Int, required: List<Any?>, excluded: List<Any?>) {
        // HallLayer.setJoinBattle ignores invalid limits instead of replacing
        // a previously configured entry limit.
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
            // ItemStore.pushItem → Model.refTreasure.  The TreasureLayer reads
            // that independent UserDefault set rather than ItemStore contents.
            campaign.inventory.discoverTreasure(itemId, data)
            acquiredItems += itemId
        }
        return "얻었다 ${item?.name ?: "아이템 $itemId"} ${if (property) "X" else "Lv"}$displayValue"
    }
    /** BattleScreen.getItem's InfoLayer text differs from HallLayer.getItem. */
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
    fun setUnitAttribute(unitId: Int, attribute: Int, value: Int) {
        campaign.setUnitAttribute(unitId, attribute, value)
    }
    fun changeUnitAttribute(unitId: Int, attribute: Int, operation: Int, value: Int) {
        val current = unitAttribute(unitId, attribute)
        setUnitAttribute(unitId, attribute, when (operation) {
            0 -> value
            1 -> current + value
            2 -> current - value
            else -> current
        })
    }
    fun head(id: Int): ScenarioHead = heads.getOrPut(id) { ScenarioHead(id) }

    /** Head.move duration is 0.01 * Cocos converted-position Euclidean distance. */
    fun moveHead(id: Int, x: Int, y: Int): Float {
        val head = head(id)
        val dx = x - head.visualX
        val dy = y - head.visualY
        val duration = kotlin.math.sqrt(dx * dx + dy * dy) * 0.01f
        head.moveFromX = head.visualX
        head.moveFromY = head.visualY
        head.moveElapsed = 0f
        head.moveDuration = duration
        head.x = x
        head.y = y
        head.visible = true
        if (duration <= 0f) {
            head.visualX = x.toFloat()
            head.visualY = y.toFloat()
        }
        return duration
    }

    /** New Head nodes start transparent and pause HallLayer through fadeIn(1). */
    fun showHead(id: Int, x: Int, y: Int): Float {
        val existing = heads[id]
        if (existing != null && existing.visible) {
            existing.x = x
            existing.y = y
            existing.visualX = x.toFloat()
            existing.visualY = y.toFloat()
            existing.moveDuration = 0f
            existing.visible = true
            return 0f
        }
        // headHide deletes the source dictionary entry before its old node
        // fades out. Once that one-second pause ends, showing the same actor
        // allocates a fresh transparent Head and fades it in again.
        heads[id] = ScenarioHead(id, x, y).apply {
            visualX = x.toFloat()
            visualY = y.toFloat()
            opacity = 0f
            fadeFrom = 0f
            fadeTo = 1f
            fadeElapsed = 0f
            fadeDuration = 1f
        }
        return 1f
    }

    /** headHide removes the dictionary entry immediately, but its node fades for one second. */
    fun hideHead(id: Int): Float {
        val head = heads[id] ?: return 0f
        head.visible = false
        head.fadeFrom = head.opacity
        head.fadeTo = 0f
        head.fadeElapsed = 0f
        head.fadeDuration = 1f
        return 1f
    }
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
    fun attackAction(attackerId: Int, targetId: Int, flag: Int) { scriptedAttacks += ScriptedAttackAction(attackerId, targetId, flag) }
    fun setScriptedUnitAction(unitId: Int, action: Int, direction: Int = -1, loop: Boolean = false) {
        unit(unitId).action = action
        if (direction >= 0) setUnitDirection(unitId, direction)
        scriptedUnitActions += ScriptedUnitAction(unitId, action, direction, loop)
    }
    fun consumeScriptedAttacks(): List<ScriptedAttackAction> = scriptedAttacks.toList().also { scriptedAttacks.clear() }
    fun consumeScriptedUnitActions(): List<ScriptedUnitAction> = scriptedUnitActions.toList().also { scriptedUnitActions.clear() }
    fun consumeScriptedUnitDirections(): List<Pair<Int, Int>> = scriptedUnitDirections.toList().also { scriptedUnitDirections.clear() }
    /** setUnitStatus is an immediate BattleScreen operation, so consume each command once. */
    fun consumeUnitStatuses(): List<Map<String, Any?>> = unitStatuses.toList().also { unitStatuses.clear() }
    fun addItemVariables(items: List<Any?>, locations: List<Any?>) {
        itemVariables += items.map { it.asIntOr(0) } to locations.map { it?.toString().orEmpty() }
    }
    /** BattleScreen.nearEvent(values, flag): bit 1 is talk; otherwise vs. */
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
    fun countDirection(fromId: Int, toId: Int): Int {
        val from = unit(fromId)
        val to = unit(toId)
        // BattleUnit.countDir: a self target retains the current direction;
        // ties are horizontal, and the source map axes map left/right to
        // 3/1 and up/down to 0/2 respectively.
        if (fromId == toId) return from.direction
        val dx = kotlin.math.abs(to.x - from.x)
        return if (kotlin.math.abs(to.y - from.y) > dx) {
            if (from.y > to.y) 0 else 2
        } else if (from.x > to.x) 3 else 1
    }

    fun createBattleUnits(faction: ScenarioUnitFaction, entries: List<Any?>) {
        // BattleScreen.createEnemy selects one block per invocation by probing
        // only its anchor. A sparse first call with no i=0 therefore reuses
        // slot 60 on the next call.
        val enemyBlockStart = if (faction == ScenarioUnitFaction.ENEMY) {
            generateSequence(BattleSlotLayout.enemyStart) { it + BattleSlotLayout.enemyBlockLength }
                .first { candidate -> battleUnits.values.none { it.battleSlot == candidate } }
        } else null
        entries.forEachIndexed { fallbackIndex, raw ->
            @Suppress("UNCHECKED_CAST")
            val entry = raw as? Map<String, Any?> ?: return@forEachIndexed
            val instanceId = entry["i"].asIntOr(fallbackIndex)
            val rosterIndex = entry["idx"].asIntOr(instanceId)
            val characterId = if (faction == ScenarioUnitFaction.MINE && "id" !in entry) {
                // BattleScreen.createMine resolves idx exclusively through the
                // already-selected BattleHall roster.  It stops when that
                // roster has no matching slot; inventing an ID here would
                // produce a playable unit absent from the scenario.
                campaign.roster.battleRoster.getOrNull(rosterIndex) ?: return@forEachIndexed
            } else entry["id"].asIntOr(instanceId)
            val initialAi = when {
                faction == ScenarioUnitFaction.MINE -> 1
                "ai" in entry -> entry["ai"].asIntOr(0)
                else -> 2
            }
            val battleSlot = BattleSlotLayout.slotFor(
                faction,
                if (faction == ScenarioUnitFaction.MINE) rosterIndex else instanceId,
                enemyBlockStart ?: BattleSlotLayout.enemyStart,
            )
            val battleUnit = ScenarioBattleUnit(
                instanceId = instanceId,
                characterId = characterId,
                faction = faction,
                x = entry["x"].asIntOr(0),
                y = entry["y"].asIntOr(0),
                authoredX = "x" in entry,
                authoredY = "y" in entry,
                direction = entry["dir"].asIntOr(2),
                level = entry["lv"].asIntOr(0),
                reinforcement = faction == ScenarioUnitFaction.ENEMY && entry["yj"].asIntOr(0) != 0,
                hidden = entry["hide"].asIntOr(0) != 0,
                // BattleScreen._truncUnitData: controllable units are active;
                // omitted AI is hold-position for friend/enemy, while an
                // explicit 0 remains passive attack.
                ai = initialAi,
                // BattleUnit.setAI stores all three target fields on the
                // unit.  Initial battle rows use the same names, so preserve
                // them before the first ControlManager is constructed.
                aiTargetId = entry["targetId"].asIntOr(-1),
                aiTargetX = entry["targetX"].asIntOr(0),
                aiTargetY = entry["targetY"].asIntOr(0),
                battleSlot = battleSlot,
            )
            battleUnits[battleUnit.stageKey] = battleUnit
            // pushUnit only sets `_unitIds[characterId]` when absent.  The
            // StageLayer `unit(characterId)` API consequently continues to
            // address the first actor even if later blocks reuse that ID.
            if (firstBattleUnitKeyByCharacterId.putIfAbsent(characterId, battleUnit.stageKey) == null) unit(characterId).apply {
                // This proxy represents the live BattleUnit, not a lazily
                // placed HallUnit. Omitted battle coordinates use the battle
                // runtime's zero default. Retaining TacticalUnit's synthetic
                // 16+/20+ fallback here made initial sync move hidden Cao Cao
                // from authored (7,0) to (7,20) before show().
                x = battleUnit.x
                y = battleUnit.y
                moveToX = battleUnit.x
                moveToY = battleUnit.y
                visualX = battleUnit.x.toFloat()
                visualY = battleUnit.y.toFloat()
                direction = battleUnit.direction
                visible = entry["hide"].asIntOr(0) == 0
                // BattleScreen's later live-state synchronization reads this
                // proxy.  Leaving it at its default zero overwrote the
                // ScenarioBattleUnit AI selected above.
                ai = initialAi
                aiTargetId = battleUnit.aiTargetId
                aiTargetX = battleUnit.aiTargetX
                aiTargetY = battleUnit.aiTargetY
            }
        }
    }

    /** Resolves the first actor registered for a character. */
    fun battleUnitForCharacterId(characterId: Int): ScenarioBattleUnit? =
        firstBattleUnitKeyByCharacterId[characterId]?.let(battleUnits::get)
            // Focused tests and import adapters can seed battleUnits
            // directly; insertion order remains the actor registration order.
            ?: battleUnits.values.firstOrNull { it.characterId == characterId }

    /** Resolves an actor directly by its stable battle-instance slot. */
    fun battleUnitForSlot(battleSlot: Int): ScenarioBattleUnit? =
        battleUnits.values.firstOrNull { it.battleSlot == battleSlot }

    /**
     * Mirrors BattleScreen.setAI's rectangle/camp selection, including its
     * optional character and coordinate target.  The source accepts both
     * individual camps (0..3) and aggregate selectors 4..6.
     */
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
    ) {
        battleUnits.values.filter { it.matchesAiCamp(camp) && it.x in x1..x2 && it.y in y1..y2 }
            .forEach {
                it.ai = ai
                it.aiTargetId = targetId
                it.aiTargetX = targetX
                it.aiTargetY = targetY
                if (battleUnitForCharacterId(it.characterId) === it) unit(it.characterId).apply {
                    this.ai = ai
                    aiTargetId = targetId
                    aiTargetX = targetX
                    aiTargetY = targetY
                }
            }
    }

    /** Mirrors BattleUnit.setAI(type, targetId, targetX, targetY). */
    fun setUnitAi(unitId: Int, ai: Int, targetId: Int = -1, targetX: Int = 0, targetY: Int = 0) {
        unit(unitId).apply {
            this.ai = ai
            aiTargetId = targetId
            aiTargetX = targetX
            aiTargetY = targetY
        }
        battleUnitForCharacterId(unitId)?.let {
            it.ai = ai
            it.aiTargetId = targetId
            it.aiTargetX = targetX
            it.aiTargetY = targetY
        }
    }

    fun setUnitRetreatTextEnabled(unitId: Int, enabled: Boolean) {
        battleUnitForCharacterId(unitId)?.deathMessageEnabled = enabled
    }

    fun hideBattleRect(x1: Int, y1: Int, x2: Int, y2: Int, camp: Int) {
        battleUnits.values.filter { it.matchesAiCamp(camp) && it.x in x1..x2 && it.y in y1..y2 }
            .forEach {
                it.hidden = true
                if (battleUnitForCharacterId(it.characterId) === it) unit(it.characterId).visible = false
            }
    }

    private fun ScenarioBattleUnit.toScriptCamp(): Int = when (faction) {
        ScenarioUnitFaction.MINE -> 0
        ScenarioUnitFaction.FRIEND -> 1
        ScenarioUnitFaction.ENEMY -> if (reinforcement) 3 else 2
    }

    private fun ScenarioBattleUnit.matchesAiCamp(camp: Int): Boolean = when (camp) {
        0, 1, 2, 3 -> toScriptCamp() == camp
        // BattleScreen._testUnitType uses 4 for Mine+Friend and 5 for
        // Enemy+Reinforcements.
        4 -> faction != ScenarioUnitFaction.ENEMY
        5 -> faction == ScenarioUnitFaction.ENEMY
        6 -> true
        else -> false
    }

    private fun Any?.asIntOr(default: Int): Int = when (this) {
        is Number -> toInt()
        is Boolean -> if (this) 1 else 0
        is String -> toIntOrNull() ?: default
        else -> default
    }

    fun apply(command: ScenarioCommand) {
        when (command) {
            is ScenarioCommand.LoadBackground -> {
                // HallLayer._setBg3(sceneType, sceneIdx): the script's first
                // argument is a scene type, not the Mmap texture number.
                // Cocos resolves type 0→idx+1, type 1→115, type 2→idx+41.
                backgroundId = when (command.backgroundId) {
                    0 -> command.variant + 1
                    1 -> 115
                    2 -> command.variant + 41
                    else -> command.variant
                }
                backgroundVariant = command.variant
                hallPathGrid = if (command.backgroundId == 2) HallPathGrid.loadOrNull(command.variant) else null
            }
            is ScenarioCommand.SetEventName -> eventName = command.name
            is ScenarioCommand.ShowUnit -> setUnit(command.unitId, command.x, command.y, command.direction)
            is ScenarioCommand.MoveUnit -> moveUnit(command.unitId, command.x, command.y, command.direction)
            is ScenarioCommand.SetUnitAction -> {
                setScriptedUnitAction(command.unitId, command.action)
            }
            is ScenarioCommand.DialogueLine, is ScenarioCommand.Choose -> Unit
        }
    }

    fun unit(id: Int): TacticalUnit = units.getOrPut(id) {
        TacticalUnit(id, 16 + id % 40, 20 + (id * 7) % 55)
    }

    /** Bind a lazy script proxy to the live BattleUnit before a scene runs. */
    fun seedBattleUnitPosition(id: Int, x: Int, y: Int) {
        unit(id).apply {
            this.x = x
            this.y = y
            moveToX = x
            moveToY = y
            visualX = x.toFloat()
            visualY = y.toFloat()
            moveFromX = visualX
            moveFromY = visualY
            moveElapsed = 0f
            moveDuration = 0f
            movePath = emptyList()
        }
    }

    fun setUnitDirection(id: Int, direction: Int) {
        unit(id).direction = direction
        scriptedUnitDirections += id to direction
    }

    private fun setUnit(id: Int, x: Int, y: Int, direction: Int) {
        unit(id).apply {
            this.x = x
            this.y = y
            moveToX = x
            moveToY = y
            visualX = x.toFloat()
            visualY = y.toFloat()
            moveDuration = 0f
            movePath = emptyList()
        }
        setUnitDirection(id, direction)
    }

    fun moveDuration(id: Int, x: Int, y: Int): Float {
        return movePath(id, x, y)?.let(::moveDuration) ?: 0f
    }

    private fun moveDuration(path: List<Pair<Int, Int>>): Float {
        val edges = (path.size - 1).coerceAtLeast(0)
        if (edges == 0) return 0f
        return if (battleMovementTimeline) edges * 0.08f + 0.1f else edges * 0.04f
    }

    private fun movePath(id: Int, x: Int, y: Int): List<Pair<Int, Int>>? {
        val unit = unit(id)
        // BattleUnit.move begins with `if (!isExist()) break`; units removed
        // by setRectUnitHide keep their script proxy but must not move, turn,
        // or create an animation episode afterward.
        if (!unit.visible) return null
        battleMovePathResolver?.let { return it(id, x, y) }
        return movePath(unit, x, y, units.values.map { it.x to it.y }.toSet())
    }

    private fun movePath(unit: TacticalUnit, x: Int, y: Int, occupied: Set<Pair<Int, Int>>): List<Pair<Int, Int>>? {
        return HallPathfinder.find(
            unit.x, unit.y, x.coerceIn(0, 99), y.coerceIn(0, 99), hallPathGrid,
            occupied,
        )
    }

    private fun moveUnit(id: Int, x: Int, y: Int, direction: Int) {
        val unit = unit(id)
        val path = movePath(id, x, y) ?: return
        beginMove(unit, path, x, y, direction)
    }

    /** HallLayer.unitsMove starts every A* request before any HallUnit reaches
     * its destination, so each request sees the same set of origin cells. */
    fun moveUnits(requests: List<ScenarioCommand.MoveUnit>): Float {
        val occupiedOrigins = units.values.map { it.x to it.y }.toSet()
        val planned = requests.mapNotNull { request ->
            val moving = units[request.unitId] ?: return@mapNotNull null
            if (!moving.visible) return@mapNotNull null
            val path = battleMovePathResolver?.invoke(moving.id, request.x, request.y)
                ?: if (battleMovePathResolver == null) movePath(moving, request.x, request.y, occupiedOrigins) else null
            path?.let { Triple(request, moving, it) }
        }
        planned.forEach { (request, moving, path) -> beginMove(moving, path, request.x, request.y, request.direction) }
        return planned.maxOfOrNull { (_, _, path) -> moveDuration(path) } ?: 0f
    }

    private fun beginMove(unit: TacticalUnit, path: List<Pair<Int, Int>>, x: Int, y: Int, direction: Int) {
        val id = unit.id
        val duration = moveDuration(path)
        unit.moveFromX = unit.visualX
        unit.moveFromY = unit.visualY
        unit.moveElapsed = 0f
        unit.animationElapsed = 0f
        unit.moveDuration = duration
        unit.movePath = path
        unit.moveZIndex = 4f * (unit.visualX + unit.visualY) - 424f
        unit.moveFinalDirection = direction
        unit.moveJustStarted = duration > 0f
        // BattleScreen.findEmptyPos may move an occupied authored destination
        // to the first source-ordered free tile. The final model commit must
        // use that resolved path endpoint, not the occupied request.
        val resolvedDestination = path.lastOrNull() ?: (x to y)
        unit.moveToX = resolvedDestination.first.coerceIn(0, 99)
        unit.moveToY = resolvedDestination.second.coerceIn(0, 99)
        if (duration <= 0f) {
            unit.x = unit.moveToX
            unit.y = unit.moveToY
            unit.visualX = unit.x.toFloat()
            unit.visualY = unit.y.toFloat()
            unit.action = 0
            setUnitDirection(id, direction)
        } else {
            unit.action = 20
            path.getOrNull(1)?.let { next -> unit.direction = HallPathfinder.direction(path[0].first, path[0].second, next.first, next.second) }
            // Battle presentation consumes authored event directions through
            // this queue even while HallUnit temporarily faces along its path.
            scriptedUnitDirections += id to direction
        }
    }

    fun updateAnimations(delta: Float) {
        units.values.filter { it.moveDuration > 0f }.forEach { unit ->
            if (unit.moveJustStarted) {
                // beginMove is reached from runUntilInput only after this
                // method has already run for the render.  That render is the
                // source-observable initial anime20 frame.  On the following
                // render the Python DELAY and the Cocos move action must
                // consume the same delta; returning here let the script
                // callback run one frame before move2 committed _x/_y.
                unit.moveJustStarted = false
            }
            val elapsedDelta = delta.coerceAtLeast(0f)
            unit.moveElapsed = (unit.moveElapsed + elapsedDelta).coerceAtMost(unit.moveDuration)
            val (sampleX, sampleY, sampleDirection, sampleZ) = if (battleMovementTimeline) {
                val timeline = BattleUnitMoveTimeline.schedule(unit.movePath, fastMove = true)
                val sample = BattleUnitMoveTimeline.sample(unit.movePath, timeline, unit.moveElapsed)
                listOf(sample.x, sample.y, sample.direction.toFloat(), 4f * (sample.x + sample.y) - 424f)
            } else {
                val sample = HallMoveTimeline.sample(unit.movePath, unit.moveElapsed)
                listOf(sample.x, sample.y, sample.direction.toFloat(), sample.zIndex)
            }
            unit.visualX = sampleX
            unit.visualY = sampleY
            unit.moveZIndex = sampleZ
            val nextDirection = sampleDirection.toInt().takeIf { it >= 0 } ?: unit.direction
            if (nextDirection != unit.direction) unit.animationElapsed = 0f else unit.animationElapsed += elapsedDelta
            unit.direction = nextDirection
            if (unit.moveElapsed >= unit.moveDuration) {
                // Source HallUnit._move2 and BattleUnit.move2 update _x/_y
                // from the final node position only in their callFunc. Until
                // this edge queries and traces must continue to see the old
                // logical tile while rendering follows visualX/visualY.
                unit.x = unit.moveToX
                unit.y = unit.moveToY
                unit.visualX = unit.x.toFloat()
                unit.visualY = unit.y.toFloat()
                unit.moveZIndex = 4f * (unit.visualX + unit.visualY) - 424f
                unit.moveDuration = 0f
                unit.action = 0
                unit.direction = unit.moveFinalDirection
            }
        }
        heads.values.forEach { head ->
            if (head.moveDuration > 0f) {
                head.moveElapsed = (head.moveElapsed + delta.coerceAtLeast(0f)).coerceAtMost(head.moveDuration)
                val progress = head.moveElapsed / head.moveDuration
                head.visualX = head.moveFromX + (head.x - head.moveFromX) * progress
                head.visualY = head.moveFromY + (head.y - head.moveFromY) * progress
                if (progress >= 1f) head.moveDuration = 0f
            }
            if (head.fadeDuration > 0f) {
                head.fadeElapsed = (head.fadeElapsed + delta.coerceAtLeast(0f)).coerceAtMost(head.fadeDuration)
                val progress = head.fadeElapsed / head.fadeDuration
                head.opacity = head.fadeFrom + (head.fadeTo - head.fadeFrom) * progress
                if (progress >= 1f) head.fadeDuration = 0f
            }
        }
    }

    fun finishAnimations() {
        units.values.forEach { unit ->
            if (unit.moveDuration > 0f) {
                unit.x = unit.moveToX
                unit.y = unit.moveToY
            }
            unit.visualX = unit.x.toFloat()
            unit.visualY = unit.y.toFloat()
            unit.moveDuration = 0f
            unit.action = if (unit.movePath.isNotEmpty()) 0 else unit.action
            if (unit.movePath.isNotEmpty()) unit.direction = unit.moveFinalDirection
        }
        heads.values.forEach { head ->
            head.visualX = head.x.toFloat()
            head.visualY = head.y.toFloat()
            head.moveDuration = 0f
            head.opacity = head.fadeTo
            head.fadeDuration = 0f
        }
    }
}

class ScenarioPlayback(val timeline: ScenarioTimeline) {
    val stage = ScenarioStage()
    var state: PlaybackState = PlaybackState.COMPLETE
        private set
    var currentDialogue: Dialogue? = null
        private set
    var currentChoice: Choice? = null
        private set
    var selectedChoice: Int = 0
        private set
    var chosenOption: String? = null
        private set
    private var nextCommandIndex = 0

    init {
        runUntilInput()
    }

    fun advanceDialogue() {
        check(state == PlaybackState.DIALOGUE) { "대기 중인 대사가 없습니다." }
        currentDialogue = null
        runUntilInput()
    }

    fun selectPrevious() {
        val options = currentChoice?.options ?: return
        selectedChoice = Math.floorMod(selectedChoice - 1, options.size)
    }

    fun selectNext() {
        val options = currentChoice?.options ?: return
        selectedChoice = Math.floorMod(selectedChoice + 1, options.size)
    }

    fun confirmChoice() {
        check(state == PlaybackState.CHOICE) { "대기 중인 선택지가 없습니다." }
        chosenOption = currentChoice!!.options[selectedChoice]
        currentChoice = null
        state = PlaybackState.COMPLETE
    }

    private fun runUntilInput() {
        while (nextCommandIndex < timeline.commands.size) {
            when (val command = timeline.commands[nextCommandIndex++]) {
                is ScenarioCommand.DialogueLine -> {
                    currentDialogue = command.dialogue
                    state = PlaybackState.DIALOGUE
                    return
                }
                is ScenarioCommand.Choose -> {
                    currentChoice = command.choice
                    selectedChoice = 0
                    state = PlaybackState.CHOICE
                    return
                }
                else -> stage.apply(command)
            }
        }
        state = PlaybackState.COMPLETE
    }
}
