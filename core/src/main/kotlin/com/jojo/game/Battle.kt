package com.jojo.game

import java.util.Random


/**
 * Deterministic tactical state and rule coordinator.
 *
 * The state is independent of rendering so battle scripts and extracted rule
 * services can be tested without a LibGDX window.
 */
class Battle(
    units: List<BattleUnit>,
    private val events: List<BattleEvent>,
    blockedTiles: Set<Pair<Int, Int>> = emptySet(),
    private val terrain: BattleTerrainGrid? = null,
    private val enemyMasterUnitId: String? = null,
    initialWeather: BattleWeather = BattleWeather.CLEAR,
    private val weatherSchedule: List<BattleWeather> = emptyList(),
    private val weatherOffset: Int = 0,
    private val terrainMagicFlags: Map<Int, Int> = emptyMap(),
    /** GAME_CFG.terrain[n].resumeHP, used by Control._cxpl. */
    private val terrainResumeRates: Map<Int, Int> = emptyMap(),
    /** GAME_CFG.terrain[n].resumeMP, applied during BattleScreen._stateProcess. */
    private val terrainResumeMp: Map<Int, Int> = emptyMap(),
    /** BattleScreen.eFlag(), injected from the scenario/game feature mask. */
    private val enabledFeatures: Int = 0,
    /** defineSkillAttr(skill, RESET_TYPE, RESET), supplied by original data. */
    private val skillTempResetTypes: Map<Int, BattleSkillTemp.ResetType> = emptyMap(),
    /** Model.stateExInfoByIdx(status, ROUND, 3), injected from GAME_CFG.status. */
    private val statusRoundFor: (BattleStatus) -> Int = { 3 },
    /** Same packed status-table round for ATT..MOV slots 0..5. */
    private val attributeStatusRoundFor: (BattleAttribute) -> Int = { 3 },
    private val movementOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    /** Config.HITAREA.MO_YU_JIAN3, in authored order for Control._zdmdd. */
    private val directDestinationOffsets: List<Pair<Int, Int>> = listOf(
        0 to 1, 1 to 0, -1 to 0, 0 to -1,
        0 to 2, 1 to 1, -1 to 1, 2 to 0, -2 to 0, 1 to -1, -1 to -1, 0 to -2,
        0 to 3, 1 to 2, -1 to 2, 2 to 1, -2 to 1, 3 to 0, -3 to 0,
        2 to -1, -2 to -1, 1 to -2, -1 to -2, 0 to -3,
        0 to 4, 1 to 3, -1 to 3, 2 to 2, -2 to 2, 3 to 1, -3 to 1,
        4 to 0, -4 to 0, 3 to -1, -3 to -1, 2 to -2, -2 to -2,
        1 to -3, -1 to -3, 0 to -4,
    ),
    /** Config.HITAREA.BU_BING, used by BattleUnit.count_attackHarm JDGJ. */
    private val infantryOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    private val propertyItems: Map<Int, BattlePropertyItem> = emptyMap(),
    private val consumeProperty: (Int) -> Boolean = { false },
    /** Game.getGVars(GLOBAL_VAR.ZDSY, 0), injected from the source save state. */
    private val zdsyGlobalValue: Int = 0,
    /** ItemStore.pushProperty(id, -1), deliberately separate from player input consumption. */
    private val consumeAutomaticProperty: (Int) -> Unit = {},
    private val onPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit = { _, _ -> },
    private val onUnitDefeated: (BattleUnit, BattleUnit) -> Unit = { _, _ -> },
    /** Mine-only persistence hook. Enemy/Friend EXP remains in [BattleUnit.experience]. */
    private val onBattleExperience: (BattleUnit, Int) -> CampaignExperienceResult? = { _, _ -> null },
    private val experienceLimit: (Int) -> Int = { 100 },
    private val levelLimit: Int = 50,
    /** Rebuild Unit.setLevel's derived battle projection after EXP raises LV. */
    private val onBattleLevelUp: (BattleUnit) -> Unit = {},
    private val onPhysicalDamage: (BattleUnit, BattleUnit, Int) -> Unit = { _, _, _ -> },
    /**
     * Exact BattleScreen g_charinfo equipment settlement.  Unlike the legacy
     * physical callback below, this receives one already max-merged slot
     * award after the complete outer action has resolved.
     */
    private val onEquipmentExperienceAward: ((BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> List<CampaignEquipmentExperienceResult>)? = null,
    /** Compatibility hook for presentation/tests that observe each _attack3 hit. */
    private val onEquipmentExperience: (BattleUnit, BattleUnit, Int) -> List<CampaignEquipmentExperienceResult> = { _, _, _ -> emptyList() },
    /** restore() skills 149..151, kept separate from attack-earned EXP. */
    private val onRestoreUnitExperience: (BattleUnit, Int) -> RestoreGrowthResolution<CampaignExperienceResult> = { _, _ -> RestoreGrowthResolution.Unavailable },
    private val onRestoreEquipmentExperience: (BattleUnit, Int, CampaignEquipmentSlot) -> RestoreGrowthResolution<CampaignEquipmentExperienceResult> = { _, _, _ -> RestoreGrowthResolution.Unavailable },
    random: Random = Random(0),
    /** Opt-in exact Tool.random/Math.random streams for full source replay. */
    sourceRandomStreams: SourceRandomStreams? = null,
    /** Game.money() at battle entry, injected because BattleScreen owns it. */
    initialPlayerMoney: Int = 0,
    /** Battle attribute ENEMY_MONEY at battle entry. */
    initialEnemyMoney: Int = 0,
    private val onUnitRetreat: (BattleUnit) -> Unit = {},
) {
    /** Scripted gates can open/close after scene0, so this cannot stay immutable. */
    private val blockedTiles = blockedTiles.toMutableSet()
    /** hitarea[QUN_XIONG].ps is an authored array; Set iteration must not alter A* FIFO ties. */
    private val orderedMovementOffsets = buildList {
        val sourceOrder = listOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
        sourceOrder.filterTo(this) { it in movementOffsets }
        movementOffsets.filterTo(this) { it !in sourceOrder }
    }
    private val skillTemps = BattleSkillTemp { skillTempResetTypes[it] ?: BattleSkillTemp.ResetType.RESET }
    private val probabilityResolver = BattleProbabilityResolver(random, sourceRandomStreams)
    /** BattleScreen._move_len, assigned from unitMove's path-node array length. */
    private var moveLength: Int = 0
    /** The start-inclusive route passed from BattleScreen.unitMove to BattleUnit.move2. */
    private val lastMovePaths = linkedMapOf<String, List<Pair<Int, Int>>>()
    fun lastMovePath(id: String): List<Pair<Int, Int>> = lastMovePaths[id].orEmpty()
    private val battlefield = Battlefield(units)
    /** Live read-only view of tactically active units in insertion order. */
    val units: Map<String, BattleUnit> = battlefield.activeMap
    private val movementPlanner by lazy {
        BattleMovementPlanner<BattleUnit>(
            isInside = { (x, y) ->
                x >= 0 && y >= 0 && terrain?.let { x < it.width && y < it.height } != false
            },
            terrainCost = { unit, (x, y) ->
                terrain?.terrainAt(x, y)?.let { unit.terrainMovementCosts[it] ?: IMPASSABLE_TERRAIN_COST } ?: 1
            },
            isBlocked = { it in this@Battle.blockedTiles },
            occupantAt = { (x, y) -> battlefield.unitAt(x, y) },
            actorId = BattleUnit::id,
            isSameActor = { left, right -> left === right },
            areAllied = ::areAllied,
            orderedMovementOffsets = orderedMovementOffsets,
            enemyNearOffsets = movementOffsets,
        )
    }
    /** Ordered AI decisions retained for deterministic full-battle diagnostics. */
    val traceActions = mutableListOf<String>()
    /** First s_AISortUnit result captured before asynchronous state settlement. */
    private var aiTurnOrder: List<String>? = null
    /** Most recently resolved `_ai2` actor; consumed by BattleScreen presentation. */
    var lastAiUnitResolution: AiUnitResolution? = null
        private set
    /**
     * One `_ai2` result calculated ahead of rendering but kept out of the
     * live model until BattleScreen reaches the matching source callbacks.
     */
    var pendingActionTransaction: BattleActionTransaction? = null
        private set
    /** BattleScreen's two money stores, exposed for injected source-parity tests. */
    var playerMoney: Int = initialPlayerMoney
        private set
    var enemyMoney: Int = initialEnemyMoney
        private set
    /** Ordered Global113 requests produced by the real physical-damage path. */
    private val equipmentUpgrades = ArrayDeque<CampaignEquipmentExperienceResult>()
    /** Non-model callbacks produced while precomputing one visible AI actor. */
    private var stagedHitSideEffects: MutableList<() -> Unit>? = null
    private var stagedCompletionSideEffects: MutableList<() -> Unit>? = null
    private fun battleExperienceEnvironment(): BattleExperienceEnvironment = BattleExperienceEnvironment(
        units = { units },
        onEquipmentExperienceAward = onEquipmentExperienceAward,
        onEquipmentExperience = onEquipmentExperience,
        onPhysicalDamage = onPhysicalDamage,
        onUnitDefeated = onUnitDefeated,
        onBattleExperience = onBattleExperience,
        experienceLimit = experienceLimit,
        levelLimit = levelLimit,
        onBattleLevelUp = onBattleLevelUp,
        enemyMasterUnitId = enemyMasterUnitId,
        equipmentUpgrades = equipmentUpgrades,
        stagedHitSideEffects = { stagedHitSideEffects },
        stagedCompletionSideEffects = { stagedCompletionSideEffects },
    )

    fun consumeEquipmentUpgrade(): CampaignEquipmentExperienceResult? =
        BattleExperienceCoordinator.consumeEquipmentUpgrade(equipmentUpgrades)

    /** BattleScreen._addWeaponExp entry used by settlement and deterministic route tests. */
    fun addEquipmentExperience(attackerId: String, targetId: String, damage: Int) =
        BattleExperienceCoordinator.addEquipmentExperience(attackerId, targetId, damage, battleExperienceEnvironment())

    private fun notifyPhysicalDamage(attacker: BattleUnit, target: BattleUnit, damage: Int) =
        BattleExperienceCoordinator.notifyPhysicalDamage(attacker, target, damage, battleExperienceEnvironment())

    private fun notifyEquipmentExperienceAward(
        recipient: BattleUnit,
        opponent: BattleUnit,
        amount: Int,
        kind: BattleEquipmentExperienceKind,
    ) = BattleExperienceCoordinator.notifyEquipmentExperienceAward(recipient, opponent, amount, kind, battleExperienceEnvironment())

    /** Calculates the per-hit equipment experience before recipient-level max merging. */
    private fun equipmentExperienceAmount(
        recipient: BattleUnit,
        opponent: BattleUnit,
        resolvedHarm: Int,
        kind: BattleEquipmentExperienceKind,
    ): Int = BattleExperienceCoordinator.equipmentExperienceAmount(recipient, opponent, resolvedHarm, kind)

    private fun notifyUnitDefeated(winner: BattleUnit, defeated: BattleUnit) =
        BattleExperienceCoordinator.notifyUnitDefeated(winner, defeated, battleExperienceEnvironment())

    private fun notifyBattleExperience(unit: BattleUnit, amount: Int) =
        BattleExperienceCoordinator.notifyBattleExperience(unit, amount, battleExperienceEnvironment())

    /** BattleScreen.count_exp, before g_charinfo's per-attacker EXP_ADD max merge. */
    private fun battleExperience(attacker: BattleUnit, target: BattleUnit, defeated: Boolean): Int =
        BattleExperienceCoordinator.battleExperience(attacker, target, defeated, enemyMasterUnitId)
    private fun notifyConsumeAutomaticProperty(itemId: Int) {
        val apply = { consumeAutomaticProperty(itemId) }
        stagedHitSideEffects?.add(apply) ?: apply()
    }
    private fun notifyPermanentProperty(item: BattlePropertyItem, target: BattleUnit) {
        val apply = { onPermanentProperty(item, target) }
        stagedHitSideEffects?.add(apply) ?: apply()
    }
    private fun consumeSelectedProperty(itemId: Int): Boolean {
        val completion = stagedCompletionSideEffects
        if (completion != null) {
            // The production UI only offers positive-count rows. Preserve
            // that selection during calculation and consume on _usePro2's
            // post-animation callback instead of leaking inventory early.
            completion += { consumeProperty(itemId); Unit }
            return true
        }
        return consumeProperty(itemId)
    }
    fun presentationUnit(id: String): BattleUnit? = battlefield.presentationUnit(id)
    fun pendingPresentationUnits(): Collection<BattleUnit> = battlefield.pendingPresentationUnits()
    /**
     * Source traces keep a defeated BattleUnit node through anime23/24 and
     * its final hidden callback. Tactical queries must still use [units],
     * while render/trace observers need both collections in stable order.
     */
    fun presentationUnits(): List<BattleUnit> = battlefield.allPresentationUnits()
    fun clearPresentationUnit(id: String) { battlefield.clearRetained(id) }
    fun completeScriptedUnitHide(id: String) {
        battlefield.hideForPresentation(id)
    }
    /** BattleUnit.show makes a retained defeated unit participate in combat again. */
    fun restorePresentationUnit(id: String): BattleUnit? = battlefield.restore(id)
    fun incrementUnitRetreat(unit: BattleUnit) {
        unit.retreatCount++
        onUnitRetreat(unit)
    }
    data class DeferredMoveResult(
        val result: TacticalActionResult,
        val path: List<Pair<Int, Int>>,
    )

    private fun presentationEnvironment(): BattlePresentationEnvironment = BattlePresentationEnvironment(
        battlefield = battlefield,
        units = { units },
        playerMoney = { playerMoney },
        setPlayerMoney = { playerMoney = it },
        enemyMoney = { enemyMoney },
        setEnemyMoney = { enemyMoney = it },
        skillTemps = skillTemps,
        moveLength = { moveLength },
        setMoveLength = { moveLength = it },
        lastMovePaths = lastMovePaths,
        traceActions = traceActions,
        getPendingActionTransaction = { pendingActionTransaction },
        setPendingActionTransaction = { pendingActionTransaction = it },
        getStagedHitSideEffects = { stagedHitSideEffects },
        setStagedHitSideEffects = { stagedHitSideEffects = it },
        getStagedCompletionSideEffects = { stagedCompletionSideEffects },
        setStagedCompletionSideEffects = { stagedCompletionSideEffects = it },
    )

    private fun runtimeSnapshot(): BattleActionSnapshot =
        BattlePresentationCoordinator.runtimeSnapshot(presentationEnvironment())

    private fun restoreRuntime(snapshot: BattleActionSnapshot) =
        BattlePresentationCoordinator.restoreRuntime(snapshot, presentationEnvironment())

    private fun createActionTransaction(
        actorId: String,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
        hitSideEffects: List<() -> Unit>,
        completionSideEffects: List<() -> Unit>,
    ): BattleActionTransaction = BattlePresentationCoordinator.createActionTransaction(
        actorId, before, after, hitSideEffects, completionSideEffects, presentationEnvironment(),
    )

    fun moveUnitForPresentation(id: String, targetX: Int, targetY: Int): DeferredMoveResult {
        val (res, path) = BattlePresentationCoordinator.moveUnitForPresentation(
            id, targetX, targetY,
            moveUnit = { uId, x, y -> moveUnit(uId, x, y) },
            lastMovePath = ::lastMovePath,
            env = presentationEnvironment(),
        )
        return DeferredMoveResult(res, path)
    }

    fun attackForPresentation(attackerId: String, targetId: String): TacticalActionResult =
        BattlePresentationCoordinator.attackForPresentation(attackerId, targetId, ::attack, presentationEnvironment())

    fun castMagicForPresentation(attackerId: String, targetId: String, magicId: Int): TacticalActionResult =
        BattlePresentationCoordinator.castMagicForPresentation(attackerId, targetId, magicId, ::castMagic, presentationEnvironment())

    fun usePropertyForPresentation(userId: String, targetId: String, itemId: Int): TacticalActionResult =
        BattlePresentationCoordinator.usePropertyForPresentation(userId, targetId, itemId, ::useProperty, presentationEnvironment())

    fun hasPendingAiUnits(): Boolean =
        BattlePresentationCoordinator.hasPendingAiUnits(outcome() != null, activeFaction, units.values)
    val firedEventIds = linkedSetOf<String>()
    var round: Int = 1
        private set
    var activeFaction: Faction = Faction.PLAYER
        private set

    /** Selects the controllable allied camp used by deterministic actual-route verification. */
    internal fun selectVerificationFaction(faction: Faction) {
        require(faction.isPlayerSide()) { "Verification routes may only select an allied camp." }
        activeFaction = faction
    }
    private val outcomeCoordinator = BattleOutcomeCoordinator(
        units = { this.units.values },
        getRound = { round },
        enabledFeatures = { enabledFeatures },
        initialMaxRounds = 99,
    )
    val maxRounds: Int get() = outcomeCoordinator.maxRounds
    val scriptedOutcome: BattleOutcome? get() = outcomeCoordinator.scriptedOutcome
    var weather: BattleWeather = initialWeather
        private set

    private fun battleTurnSettlementEnvironment(): BattleTurnSettlementEnvironment = BattleTurnSettlementEnvironment(
        units = { units.values },
        presentationUnit = battlefield::presentationUnit,
        defeatUnit = battlefield::defeat,
        terrain = terrain,
        terrainResumeRates = terrainResumeRates,
        terrainResumeMp = terrainResumeMp,
        weather = { weather },
        enabledFeatures = { enabledFeatures },
        infantryOffsets = infantryOffsets,
        statusRoundFor = statusRoundFor,
        attributeStatusRoundFor = attributeStatusRoundFor,
        onRestoreUnitExperience = onRestoreUnitExperience,
        onRestoreEquipmentExperience = onRestoreEquipmentExperience,
        onEquipmentUpgrade = { equipmentUpgrades += it },
    )

    /**
     * Compatibility wrapper for model-only callers.  Production turn flow
     * uses the individual lifecycle methods below so every source coroutine
     * barrier can be presented before the following mutation is applied.
     */
    fun endTurn(): TurnResult = BattleRoundCoordinator.endTurn(
        activeFaction = { activeFaction },
        round = { round },
        settleActiveCampEnd = ::settleActiveCampEnd,
        advanceRound = ::advanceRound,
        resetCompletedRoundSkillTemps = ::resetCompletedRoundSkillTemps,
        applyScheduledWeather = ::applyScheduledWeather,
        advanceToNextCamp = ::advanceToNextCamp,
        settleActiveCampStart = ::settleActiveCampStart,
        runActiveCampEvents = ::runActiveCampEvents,
        prepareActiveCampOperation = ::prepareActiveCampOperation,
        units = { units.values },
    )

    /** BattleScreen.restore, before its nested unitDeath callback. */
    fun settleActiveCampEnd(): CampSettlement =
        BattleTurnSettlementService.settleCampEnd(activeFaction, battleTurnSettlementEnvironment())

    /**
     * `_setOper` changes curCamp before RoundLayer and before `_stateProcess`.
     * This method deliberately does not apply state, reset actors, or weather.
     */
    fun advanceToNextCamp(): TurnResult {
        val (next, result) = BattleRoundCoordinator.advanceToNextCamp(activeFaction, round)
        activeFaction = next
        return result
    }

    /** First run_script inside unitDeath, after `_stateProcess` presentation. */
    fun runActiveCampEvents(): List<String> =
        BattleRoundCoordinator.runActiveCampEvents(events, firedEventIds, this)

    /** BattleScreen._stateProcess; mutations occur only after RoundLayer closes. */
    fun settleActiveCampStart(): CampSettlement =
        BattleTurnSettlementService.settleCampStart(activeFaction, battleTurnSettlementEnvironment())

    /**
     * Source `_ai2` captures its actor order after state settlement/death.
     * Resetting and sorting here prevents a future camp from being observable
     * while the preceding card or state animation is still on screen.
     */
    fun prepareActiveCampOperation() {
        aiTurnOrder = BattleRoundCoordinator.prepareActiveCampOperation(
            activeFaction,
            units.values,
            ::aiSortValue,
        )
    }

    /** `addRound`, before the new-round battle script. */
    fun advanceRound(): RoundAdvance {
        val (newRound, advance) = BattleRoundCoordinator.advanceRound(activeFaction, round)
        round = newRound
        return advance
    }

    /** `resetSkillTemp(T)`, after new-round script/unitDeath and before weather. */
    fun resetCompletedRoundSkillTemps(completedRound: Int) =
        BattleRoundCoordinator.resetCompletedRoundSkillTemps(completedRound, round, skillTemps)

    /** `_countCurrentWeather`/`_switchWeather`, after new-round script/death. */
    fun applyScheduledWeather(): WeatherTransition {
        val (newWeather, transition) = BattleRoundCoordinator.applyScheduledWeather(
            round,
            weatherSchedule,
            weatherOffset,
            weather,
        )
        weather = newWeather
        return transition
    }

    fun unitAt(tileX: Int, tileY: Int): BattleUnit? = battlefield.unitAt(tileX, tileY)

    fun outcome(): BattleOutcome? = outcomeCoordinator.outcome()

    /** BattleScreen.setMaxRound: ZJHH contributes exactly four turns. */
    fun setMaxRounds(value: Int) = outcomeCoordinator.setMaxRounds(value)

    /** A ScenarioStage setMaxRound value has already applied BattleScreen.eFlag(). */
    fun setResolvedMaxRounds(value: Int) = outcomeCoordinator.setResolvedMaxRounds(value)

    fun enabledFeatureMask(): Int = enabledFeatures

    /** Recovered BattleScreen.setWeather/setRound entry points used by EditLayer2. */
    fun applyEditedWeather(value: Int) { weather = BattleWeather.entries[value.coerceIn(BattleWeather.entries.indices)] }
    fun applyEditedRound(value: Int) { round = value.coerceAtLeast(1) }

    /** BattleScreen.skillTemp/setSkillTemp/incSkillTemp, exposed for scripts. */
    fun skillTemp(unitId: String, skillId: Int, default: Int = 0): Int = skillTemps.value(unitId, skillId, default)
    fun setSkillTemp(unitId: String, skillId: Int, amount: Int, recordedRound: Int = round) =
        skillTemps.set(unitId, skillId, amount, recordedRound)
    fun incSkillTemp(unitId: String, skillId: Int): Int = skillTemps.increment(unitId, skillId, round)
    fun setBlockedTiles(values: Collection<Pair<Int, Int>>) {
        blockedTiles.clear()
        blockedTiles.addAll(values)
    }

    private fun movementEnvironment(): BattleMovementEnvironment = BattleMovementEnvironment(
        units = { units },
        unitAt = ::unitAt,
        activeFaction = { activeFaction },
        weather = { weather },
        terrain = terrain,
        blockedTiles = blockedTiles,
        movementPlanner = movementPlanner,
        allPresentationUnits = battlefield::allPresentationUnits,
        isBattleEnded = { outcome() != null },
        onMoveExecuted = { id, path, nodes ->
            moveLength = nodes
            lastMovePaths[id] = path
        },
    )

    /**
     * The same weighted flood-fill used by BattleScreen._showMoveArea.  This
     * is exposed to the renderer so the desktop client can show the original
     * selectable movement area instead of accepting invisible movement.
     */
    fun reachableTiles(id: String): Map<Pair<Int, Int>, Int> =
        BattleMovementCoordinator.reachableTiles(id, movementEnvironment())

    /**
     * Read-only S57 route probe after an attackable guard is removed. The
     * guard attack itself consumes the current action; source0/escort policy
     * may therefore use the following or the next real movement turn to enter
     * a leader's physical attack-staging tile. This deliberately projects no
     * more than those two turns and never mutates a BattleUnit.
     */
    fun canEnterTilesIgnoringEnemyWithinMoves(
        id: String,
        ignoredEnemyId: String,
        start: Pair<Int, Int>,
        targetTiles: Set<Pair<Int, Int>>,
        moves: Int = 2,
    ): Boolean = BattleMovementCoordinator.canEnterTilesIgnoringEnemyWithinMoves(
        id, ignoredEnemyId, start, targetTiles, moves, movementEnvironment(),
    )
    /** Scenario scripts can end a battle through reward()/lose() without eliminating every enemy. */
    fun setScriptedOutcome(value: BattleOutcome) = outcomeCoordinator.setScriptedOutcome(value)

    /**
     * Mirrors a ScenarioStage result without clearing an outcome on ordinary
     * scene1 passes which have not called reward/lose.  Script callbacks can
     * publish this after the initial BattleScreen script invocation.
     */
    fun syncScriptedOutcome(value: BattleOutcome?) = outcomeCoordinator.syncScriptedOutcome(value)

    fun moveUnit(id: String, targetX: Int, targetY: Int, maxDistance: Int? = null): TacticalActionResult =
        BattleMovementCoordinator.moveUnit(id, targetX, targetY, maxDistance, movementEnvironment())

    private fun tacticalActionEnvironment(): BattleTacticalActionEnvironment = BattleTacticalActionEnvironment(
        outcome = ::outcome,
        units = { units },
        activeFaction = { activeFaction },
        areAllied = ::areAllied,
        movementOffsets = movementOffsets,
        propertyItems = propertyItems,
        consumeSelectedProperty = ::consumeSelectedProperty,
        notifyPermanentProperty = ::notifyPermanentProperty,
        physicalCombatEnvironment = ::physicalCombatEnvironment,
        magicEnvironment = ::magicEnvironment,
    )

    fun attack(attackerId: String, targetId: String, damage: Int? = null): TacticalActionResult =
        BattleTacticalActionExecutor.attack(attackerId, targetId, damage, tacticalActionEnvironment())


    private fun physicalContextEnvironment(): BattlePhysicalContextEnvironment = BattlePhysicalContextEnvironment(
        units = { units.values },
        unitAt = ::unitAt,
        terrain = terrain,
        weather = { weather },
        infantryOffsets = infantryOffsets,
        skillTemp = ::skillTemp,
        setSkillTemp = ::setSkillTemp,
        incSkillTemp = ::incSkillTemp,
        moveLength = { moveLength },
        backPosition = ::backPosition,
        facingDirection = ::facingDirection,
        hasPhysicalEffectTargets = { attacker, target ->
            PhysicalAttackAreaResolver.hasPhysicalEffectTargets(attacker, target, ::unitAt, ::areAllied)
        },
        probabilityResolver = probabilityResolver,
    )


    /**
     * BattleScreen.showUseProperty + _usePro2 for the portable combat
     * consumables.  The original permits selecting an allied target in the
     * infantry hit area; this tactical context uses the same adjacent area.
     */
    fun useProperty(userId: String, targetId: String, itemId: Int): TacticalActionResult =
        BattleTacticalActionExecutor.useProperty(userId, targetId, itemId, tacticalActionEnvironment())

    /**
     * BattleScreen._usePro2's state mutation, shared by the player-selected
     * path and `_attack3` ZDSY.  The caller owns inventory mutation because
     * ZDSY uses ItemStore.pushProperty directly before entering _usePro2.
     */
    private fun applyProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        consume: () -> Boolean,
    ): TacticalActionResult.Item? =
        BattleTacticalActionExecutor.applyProperty(item, target, consume, ::notifyPermanentProperty)

    /** BattleScreen.attackAction: scripted/cinematic attack outside normal turn input. */
    fun forcedAttack(attackerId: String, targetId: String): TacticalActionResult =
        BattleTacticalActionExecutor.forcedAttack(attackerId, targetId, tacticalActionEnvironment())

    /**
     * Original offensive-strategy baseline: range/MP/area, magic hit rate,
     * spirit formula and defender arm magic resistance. Status/weather/skill
     * modifiers are resolved by the higher-level script layer.
     */
    fun castMagic(
        attackerId: String,
        targetId: String,
        magicId: Int,
        reaction: Boolean = false,
        bypassCondition: Boolean = false,
    ): TacticalActionResult = BattleTacticalActionExecutor.castMagic(
        attackerId, targetId, magicId, reaction, bypassCondition, tacticalActionEnvironment(),
    )

    /** Coordinate-target special magic.  SHUN_YI moves its caster to a vacant tile. */
    fun castMagicAt(attackerId: String, targetX: Int, targetY: Int, magicId: Int): TacticalActionResult =
        BattleTacticalActionExecutor.castMagicAt(attackerId, targetX, targetY, magicId, tacticalActionEnvironment())

    private fun combatEnvironmentContext(): BattleCombatEnvironmentContext = BattleCombatEnvironmentContext(
        units = { units.values },
        pendingPresentationUnits = { battlefield.pendingPresentationUnits() },
        unitAt = ::unitAt,
        areAllied = ::areAllied,
        weather = { weather },
        setWeather = { weather = it },
        terrain = terrain,
        terrainMagicFlags = terrainMagicFlags,
        activeFaction = { activeFaction },
        isBattleEnded = { outcome() != null },
        statusRoundFor = statusRoundFor,
        probabilityResolver = probabilityResolver,
        battleExperience = { attacker, target, defeated -> battleExperience(attacker, target, defeated) },
        equipmentExperienceAmount = { recipient, opponent, harm, kind -> equipmentExperienceAmount(recipient, opponent, harm, kind) },
        notifyBattleExperience = ::notifyBattleExperience,
        notifyEquipmentExperienceAward = ::notifyEquipmentExperienceAward,
        notifyPhysicalDamage = ::notifyPhysicalDamage,
        notifyUnitDefeated = ::notifyUnitDefeated,
        onDefeat = { id -> battlefield.defeat(id) },
        canAttack = ::canAttack,
        backPosition = ::backPosition,
        facingDirection = ::facingDirection,
        getPlayerMoney = { playerMoney },
        setPlayerMoney = { playerMoney = it },
        getEnemyMoney = { enemyMoney },
        setEnemyMoney = { enemyMoney = it },
        propertyItem = propertyItems::get,
        zdsyGlobalValue = zdsyGlobalValue,
        notifyConsumeAutomaticProperty = ::notifyConsumeAutomaticProperty,
        incSkillTemp = ::incSkillTemp,
        applyProperty = ::applyProperty,
        visibleFamousPlayerCount = { BattlePhysicalContextBuilder.visibleFamousPlayerCount(physicalContextEnvironment()) },
        basePhysicalDamageContext = { attacker, target, splash, rule ->
            BattlePhysicalContextBuilder.basePhysicalDamageContext(attacker, target, splash, rule, physicalContextEnvironment())
        },
        physicalDamageRateContext = { attacker, target ->
            BattlePhysicalContextBuilder.physicalDamageRateContext(attacker, target, physicalContextEnvironment())
        },
        physicalCriticalRateContext = { attacker, target, critical, counter, continuous, splash ->
            BattlePhysicalContextBuilder.physicalCriticalRateContext(attacker, target, critical, counter, continuous, splash, physicalContextEnvironment())
        },
        flatPhysicalDamageContext = { attacker, activeAttack ->
            BattlePhysicalContextBuilder.flatPhysicalDamageContext(attacker, activeAttack, physicalContextEnvironment())
        },
        castReactionMagic = { caster, target, magicId ->
            castMagic(caster.id, target.id, magicId, reaction = true) as? TacticalActionResult.Magic
        },
        consumeXuShiDamage = { attacker ->
            BattlePhysicalContextBuilder.consumeXuShiDamage(attacker, physicalContextEnvironment())
        },
        consumeMpAttackSkill = { attacker ->
            BattlePhysicalContextBuilder.consumeMpAttackSkill(attacker)
        },
        mrspDamage = { attacker, target ->
            BattlePhysicalContextBuilder.mrspDamage(attacker, target) { probabilityResolver.random100() }
        },
    )

    private fun magicEnvironment(): MagicEnvironment =
        BattleCombatEnvironmentBuilder.buildMagicEnvironment(combatEnvironmentContext())

    fun addUnit(unit: BattleUnit) {
        battlefield.add(unit)
        initializeRateGauges(unit)
    }

    /** BattleScreen._truncUnitData seeds JQ_BDMZL through JQ_BBJL inclusively. */
    fun initializeRateGauges(unit: BattleUnit) = probabilityResolver.initializeRateGauges(unit)

    /** Initial scripted units pass through the same _truncUnitData seeding. */
    fun initializeAllRateGauges() = units.values.forEach(::initializeRateGauges)

    /** BattleUnit.setStateRound when an event explicitly supplies a status. */
    fun rollStatusDuration(): Int = probabilityResolver.rollStatusDuration()

    /** Exact numeric key built by BattleScreen.s_AISortUnit. */
    private fun aiSortValue(unit: BattleUnit): Double =
        BattleAiScorer.aiSortValue(unit, terrain, terrainResumeRates)

    /**
     * Cocos BattleConfg.AI 0..9 dispatch, without presentation delays.  The
     * scripted target id/coordinates are retained from BattleUnit.setAI.
     */
    @JvmOverloads
    fun resolveAiTurn(maxUnits: Int = Int.MAX_VALUE, deferMutations: Boolean = false): AiTurnResult =
        BattleAiCoordinator.resolveAiTurn(maxUnits, deferMutations, aiCoordinatorEnvironment())

    private companion object {
        const val DEFAULT_TERRAIN_SIZE = 100
        const val IMPASSABLE_TERRAIN_COST = 255
        /** Config.ENABLED_FEATURE.ZJHH. */
        const val ENABLED_FEATURE_ZJHH = 8
        /** Config.ENABLED_FEATURE.ZDBHSW. */
        const val ENABLED_FEATURE_ZDBHSW = 32
    }

    /**
     * Captures the actual AI scorer for one source character without running
     * a turn, moving a unit, or injecting an expected choice.
     */
    fun traceAiPlannerAtCurrentPoint(characterId: Int, aiFlags: Int = 1): AiPlannerTrace? =
        BattleAiCoordinator.traceAiPlannerAtCurrentPoint(characterId, aiFlags, aiCoordinatorEnvironment())

    /** Injectable `Control._countAttackValue` preview for one primary target. */
    fun previewAiAttackValue(attackerId: String, targetId: String): Int =
        BattleAiCoordinator.previewAiAttackValue(attackerId, targetId, aiCoordinatorEnvironment())

    /**
     * Read-only ordinary physical-harm preview for input planning.  This is
     * the source `countBaseHarm` value before hit, critical, and the
     * move-dependent attack effects are rolled, so asking for it cannot
     * consume a skill temp or advance either unit's combat state.
     */
    fun previewPhysicalDamage(attackerId: String, targetId: String): Int {
        val attacker = units[attackerId] ?: return 0
        val target = units[targetId] ?: return 0
        return PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            BattlePhysicalContextBuilder.basePhysicalDamageContext(
                attacker, target, splash = false, env = physicalContextEnvironment(),
            ),
        )
    }

    private fun aiCoordinatorEnvironment(): BattleAiCoordinatorEnvironment = BattleAiCoordinatorEnvironment(
        units = { units },
        unitAt = ::unitAt,
        areAllied = ::areAllied,
        weather = { weather },
        round = { round },
        terrain = terrain,
        terrainResumeRates = terrainResumeRates,
        terrainMagicFlags = terrainMagicFlags,
        probabilityResolver = probabilityResolver,
        basePhysicalDamageContext = { attacker, target, splash ->
            BattlePhysicalContextBuilder.basePhysicalDamageContext(
                attacker, target, splash, env = physicalContextEnvironment(),
            )
        },
        reachableTiles = ::reachableTiles,
        traceActions = traceActions,
        movementOffsets = movementOffsets,
        enemyMasterUnitId = enemyMasterUnitId,
        findMovementPath = { unit, targetX, targetY, avoidEnemies, penalizeEnemyTiles, allowEnemyOnTarget ->
            findMovementPath(unit, targetX, targetY, avoidEnemies, penalizeEnemyTiles, allowEnemyOnTarget)
        },
        findReachableEmptyPosition = ::findReachableEmptyPosition,
        movePoints = { unit, movement -> movePoints(unit, movement) },
        outcome = ::outcome,
        activeFaction = { activeFaction },
        moveUnit = { id, targetX, targetY -> moveUnit(id, targetX, targetY) },
        attack = { attackerId, targetId -> attack(attackerId, targetId) },
        castMagic = { attackerId, targetId, magicId, reaction, bypassCondition ->
            castMagic(attackerId, targetId, magicId, reaction, bypassCondition)
        },
        lastMovePath = ::lastMovePath,
        aiTurnOrder = { aiTurnOrder },
        clearAiTurnOrder = { aiTurnOrder = null },
        setLastAiUnitResolution = { lastAiUnitResolution = it },
        lastAiUnitResolution = { lastAiUnitResolution },
        runtimeSnapshot = ::runtimeSnapshot,
        restoreRuntime = ::restoreRuntime,
        setPendingActionTransaction = { pendingActionTransaction = it },
        pendingActionTransaction = { pendingActionTransaction },
        stagedHitSideEffects = { stagedHitSideEffects },
        setStagedHitSideEffects = { stagedHitSideEffects = it },
        stagedCompletionSideEffects = { stagedCompletionSideEffects },
        setStagedCompletionSideEffects = { stagedCompletionSideEffects = it },
        createActionTransaction = { actorId, before, after, hitSideEffects, completionSideEffects ->
            createActionTransaction(actorId, before, after, hitSideEffects, completionSideEffects)
        },
    )

    private fun canAttack(attacker: BattleUnit, target: BattleUnit): Boolean =
        BattleAiScorer.canAttack(attacker, target)

    private fun areAllied(left: Faction, right: Faction): Boolean =
        left.isPlayerSide() == right.isPlayerSide()

    private fun areAllied(left: BattleUnit, right: BattleUnit): Boolean =
        areAllied(left.effectiveFaction(), right.effectiveFaction())




    private fun findMovementPath(
        unit: BattleUnit,
        targetX: Int,
        targetY: Int,
        avoidEnemies: Boolean = false,
        penalizeEnemyTiles: Boolean = false,
        allowEnemyOnTarget: Boolean = false,
    ): List<Pair<Int, Int>>? = BattleMovementCoordinator.findMovementPath(
        unit, targetX, targetY, movementPlanner, avoidEnemies, penalizeEnemyTiles, allowEnemyOnTarget,
    )

    fun scriptedMovePath(characterId: Int, targetX: Int, targetY: Int): List<Pair<Int, Int>>? =
        BattleMovementCoordinator.scriptedMovePath(characterId, targetX, targetY, battlefield.allPresentationUnits(), movementPlanner, terrain)

    private fun findReachableEmptyPosition(
        unit: BattleUnit,
        seed: Pair<Int, Int>,
        reachable: Set<Pair<Int, Int>>,
    ): Pair<Int, Int>? = BattleMovementCoordinator.findReachableEmptyPosition(unit, seed, reachable, movementPlanner, terrain)

    private fun backPosition(defender: BattleUnit, attacker: BattleUnit): Pair<Int, Int>? =
        BattleMovementCoordinator.backPosition(defender, attacker, terrain, blockedTiles, ::unitAt)

    private fun facingDirection(fromX: Int, fromY: Int, toX: Int, toY: Int): Int =
        BattleMovementCoordinator.facingDirection(fromX, fromY, toX, toY)

    private fun movePoints(
        unit: BattleUnit,
        movement: Int,
        ignoredEnemyId: String? = null,
        startOverride: Pair<Int, Int>? = null,
    ) = BattleMovementCoordinator.movePoints(unit, movement, movementPlanner, ignoredEnemyId, startOverride)


    private fun physicalCombatEnvironment(): PhysicalCombatEnvironment =
        BattleCombatEnvironmentBuilder.buildPhysicalCombatEnvironment(combatEnvironmentContext())

}
