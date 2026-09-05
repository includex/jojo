package com.jojo.game
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleAiScorer
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge
import com.jojo.game.domain.campaign.CampaignEquipmentSlot

import java.util.*


/**
 * Deterministic tactical state and rule coordinator.
 *
 * The state is independent of rendering so battle scripts and extracted rule
 * services can be tested without a LibGDX window.
 */
class Battle(
    units: List<BattleUnit>,
    events: List<BattleEvent>,
    blockedTiles: Set<Pair<Int, Int>> = emptySet(),
    terrain: BattleTerrainGrid? = null,
    enemyMasterUnitId: String? = null,
    initialWeather: BattleWeather = BattleWeather.CLEAR,
    weatherSchedule: List<BattleWeather> = emptyList(),
    weatherOffset: Int = 0,
    terrainMagicFlags: Map<Int, Int> = emptyMap(),
    /** GAME_CFG.terrain[n].resumeHP, used by Control._cxpl. */
    terrainResumeRates: Map<Int, Int> = emptyMap(),
    /** GAME_CFG.terrain[n].resumeMP, applied during BattleScreen._stateProcess. */
    terrainResumeMp: Map<Int, Int> = emptyMap(),
    /** BattleScreen.eFlag(), injected from the scenario/game feature mask. */
    enabledFeatures: Int = 0,
    /** defineSkillAttr(skill, RESET_TYPE, RESET), supplied by original data. */
    skillTempResetTypes: Map<Int, BattleSkillTemp.ResetType> = emptyMap(),
    /** Model.stateExInfoByIdx(status, ROUND, 3), injected from GAME_CFG.status. */
    statusRoundFor: (BattleStatus) -> Int = { 3 },
    /** Same packed status-table round for ATT..MOV slots 0..5. */
    attributeStatusRoundFor: (BattleAttribute) -> Int = { 3 },
    movementOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    /** Retained as a source-compatible named argument; routing owns this data. */
    directDestinationOffsets: List<Pair<Int, Int>> = emptyList(),
    /** Config.HITAREA.BU_BING, used by BattleUnit.count_attackHarm JDGJ. */
    infantryOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    propertyItems: Map<Int, BattlePropertyItem> = emptyMap(),
    consumeProperty: (Int) -> Boolean = { false },
    /** Game.getGVars(GLOBAL_VAR.ZDSY, 0), injected from the source save state. */
    zdsyGlobalValue: Int = 0,
    /** ItemStore.pushProperty(id, -1), deliberately separate from player input consumption. */
    consumeAutomaticProperty: (Int) -> Unit = {},
    onPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit = { _, _ -> },
    onUnitDefeated: (BattleUnit, BattleUnit) -> Unit = { _, _ -> },
    /** Mine-only persistence hook. Enemy/Friend EXP remains in [BattleUnit.experience]. */
    onBattleExperience: (BattleUnit, Int) -> CampaignExperienceResult? = { _, _ -> null },
    experienceLimit: (Int) -> Int = { 100 },
    levelLimit: Int = 50,
    /** Rebuild Unit.setLevel's derived battle projection after EXP raises LV. */
    onBattleLevelUp: (BattleUnit) -> Unit = {},
    onPhysicalDamage: (BattleUnit, BattleUnit, Int) -> Unit = { _, _, _ -> },
    /**
     * Exact BattleScreen g_charinfo equipment settlement.  Unlike the legacy
     * physical callback below, this receives one already max-merged slot
     * award after the complete outer action has resolved.
     */
    onEquipmentExperienceAward: ((BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> List<CampaignEquipmentExperienceResult>)? = null,
    /** Compatibility hook for presentation/tests that observe each _attack3 hit. */
    onEquipmentExperience: (BattleUnit, BattleUnit, Int) -> List<CampaignEquipmentExperienceResult> = { _, _, _ -> emptyList() },
    /** restore() skills 149..151, kept separate from attack-earned EXP. */
    onRestoreUnitExperience: (BattleUnit, Int) -> RestoreGrowthResolution<CampaignExperienceResult> = { _, _ -> RestoreGrowthResolution.Unavailable },
    onRestoreEquipmentExperience: (BattleUnit, Int, CampaignEquipmentSlot) -> RestoreGrowthResolution<CampaignEquipmentExperienceResult> = { _, _, _ -> RestoreGrowthResolution.Unavailable },
    random: Random = Random(0),
    /** Opt-in exact Tool.random/Math.random streams for full source replay. */
    sourceRandomStreams: SourceRandomStreams? = null,
    /** Game.money() at battle entry, injected because BattleScreen owns it. */
    initialPlayerMoney: Int = 0,
    /** Battle attribute ENEMY_MONEY at battle entry. */
    initialEnemyMoney: Int = 0,
    onUnitRetreat: (BattleUnit) -> Unit = {},
) {
    internal val configuration = buildBattleConfiguration(
        events, terrain, enemyMasterUnitId, weatherSchedule, weatherOffset, terrainMagicFlags,
        terrainResumeRates, terrainResumeMp, enabledFeatures, skillTempResetTypes,
        statusRoundFor, attributeStatusRoundFor, movementOffsets, infantryOffsets, propertyItems,
        consumeProperty, zdsyGlobalValue, consumeAutomaticProperty, onPermanentProperty, onUnitDefeated,
        onBattleExperience, experienceLimit, levelLimit, onBattleLevelUp, onPhysicalDamage,
        onEquipmentExperienceAward, onEquipmentExperience, onRestoreUnitExperience,
        onRestoreEquipmentExperience, random, sourceRandomStreams, onUnitRetreat,
    )
    internal val journal = BattleStateJournal(initialWeather, initialPlayerMoney, initialEnemyMoney, blockedTiles)
    internal val skillTemps =
        BattleSkillTemp { configuration.skillTempResetTypes[it] ?: BattleSkillTemp.ResetType.RESET }
    internal val probabilityResolver = BattleProbabilityResolver(random, sourceRandomStreams)

    /** The start-inclusive route passed from BattleScreen.unitMove to BattleUnit.move2. */
    fun lastMovePath(id: String): List<Pair<Int, Int>> = journal.lastMovePath(id)
    internal val battlefield = Battlefield(units)

    /** Live read-only view of tactically active units in insertion order. */
    val units: Map<String, BattleUnit> = battlefield.activeMap
    private val experienceFacade by lazy { BattleExperienceFacade(configuration, journal) { this.units } }
    private val presentationTransactions by lazy {
        BattlePresentationTransactionFacade(battlefield, { this.units }, skillTemps, journal)
    }
    private val combatFacade by lazy { BattleCombatFacade(this) }
    private val aiFacade by lazy { BattleAiFacade(this) }
    internal val movementQueries by lazy {
        BattleMovementQueryFacade(
            configuration = configuration,
            journal = journal,
            battlefield = battlefield,
            units = { this.units },
            activeFaction = { activeFaction },
            weather = { weather },
            isBattleEnded = { outcome() != null },
            areAllied = ::areAllied,
        )
    }
    private val roundLifecycle by lazy {
        BattleRoundLifecycleFacade(
            configuration,
            journal,
            battlefield,
            { this.units.values },
            skillTemps,
            this,
            ::aiSortValue
        )
    }

    /** Ordered AI decisions retained for deterministic full-battle diagnostics. */
    val traceActions: MutableList<String> get() = journal.mutableTraceActions()

    /** Most recently resolved `_ai2` actor; consumed by BattleScreen presentation. */
    var lastAiUnitResolution: AiUnitResolution?
        get() = journal.lastAiUnitResolution
        private set(value) {
            journal.recordLastAiUnitResolution(value)
        }

    /**
     * One `_ai2` result calculated ahead of rendering but kept out of the
     * live model until BattleScreen reaches the matching source callbacks.
     */
    var pendingActionTransaction: BattleActionTransaction?
        get() = journal.pendingActionTransaction
        private set(value) {
            journal.recordPendingActionTransaction(value)
        }

    /** BattleScreen's two money stores, exposed for injected source-parity tests. */
    var playerMoney: Int
        get() = journal.playerMoney
        private set(value) {
            journal.setPlayerMoney(value)
        }
    var enemyMoney: Int
        get() = journal.enemyMoney
        private set(value) {
            journal.setEnemyMoney(value)
        }

    fun consumeEquipmentUpgrade(): CampaignEquipmentExperienceResult? =
        experienceFacade.consumeEquipmentUpgrade()

    /** BattleScreen._addWeaponExp entry used by settlement and deterministic route tests. */
    fun addEquipmentExperience(attackerId: String, targetId: String, damage: Int) =
        experienceFacade.addEquipmentExperience(attackerId, targetId, damage)

    internal fun notifyPhysicalDamage(attacker: BattleUnit, target: BattleUnit, damage: Int) =
        experienceFacade.notifyPhysicalDamage(attacker, target, damage)

    internal fun notifyEquipmentExperienceAward(
        recipient: BattleUnit,
        opponent: BattleUnit,
        amount: Int,
        kind: BattleEquipmentExperienceKind,
    ) = experienceFacade.notifyEquipmentExperienceAward(recipient, opponent, amount, kind)

    /** Calculates the per-hit equipment experience before recipient-level max merging. */
    internal fun equipmentExperienceAmount(
        recipient: BattleUnit,
        opponent: BattleUnit,
        resolvedHarm: Int,
        kind: BattleEquipmentExperienceKind,
    ): Int = experienceFacade.equipmentExperienceAmount(recipient, opponent, resolvedHarm, kind)

    internal fun notifyUnitDefeated(winner: BattleUnit, defeated: BattleUnit) =
        experienceFacade.notifyUnitDefeated(winner, defeated)

    internal fun notifyBattleExperience(unit: BattleUnit, amount: Int) =
        experienceFacade.notifyBattleExperience(unit, amount)

    /** BattleScreen.count_exp, before g_charinfo's per-attacker EXP_ADD max merge. */
    internal fun battleExperience(attacker: BattleUnit, target: BattleUnit, defeated: Boolean): Int =
        experienceFacade.battleExperience(attacker, target, defeated)

    internal fun notifyConsumeAutomaticProperty(itemId: Int) {
        experienceFacade.notifyConsumeAutomaticProperty(itemId, configuration.consumeAutomaticProperty)
    }

    internal fun notifyPermanentProperty(item: BattlePropertyItem, target: BattleUnit) {
        experienceFacade.notifyPermanentProperty(item, target, configuration.onPermanentProperty)
    }

    internal fun consumeSelectedProperty(itemId: Int): Boolean {
        return experienceFacade.consumeSelectedProperty(itemId, configuration.consumeProperty)
    }

    fun presentationUnit(id: String): BattleUnit? = battlefield.presentationUnit(id)
    fun pendingPresentationUnits(): Collection<BattleUnit> = battlefield.pendingPresentationUnits()

    /**
     * Source traces keep a defeated BattleUnit node through anime23/24 and
     * its final hidden callback. Tactical queries must still use [units],
     * while render/trace observers need both collections in stable order.
     */
    fun presentationUnits(): List<BattleUnit> = battlefield.allPresentationUnits()
    fun clearPresentationUnit(id: String) {
        battlefield.clearRetained(id)
    }

    fun completeScriptedUnitHide(id: String) {
        battlefield.hideForPresentation(id)
    }

    /** BattleUnit.show makes a retained defeated unit participate in combat again. */
    fun restorePresentationUnit(id: String): BattleUnit? = battlefield.restore(id)
    fun incrementUnitRetreat(unit: BattleUnit) {
        unit.retreatCount++
        configuration.onUnitRetreat(unit)
    }

    data class DeferredMoveResult(
        val result: TacticalActionResult,
        val path: List<Pair<Int, Int>>,
    )

    internal fun runtimeSnapshot(): BattleActionSnapshot =
        presentationTransactions.runtimeSnapshot()

    internal fun restoreRuntime(snapshot: BattleActionSnapshot) =
        presentationTransactions.restoreRuntime(snapshot)

    internal fun createActionTransaction(
        actorId: String,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
        hitSideEffects: List<() -> Unit>,
        completionSideEffects: List<() -> Unit>,
    ): BattleActionTransaction = presentationTransactions.createActionTransaction(
        actorId, before, after, hitSideEffects, completionSideEffects,
    )

    fun moveUnitForPresentation(id: String, targetX: Int, targetY: Int): DeferredMoveResult {
        val (res, path) = presentationTransactions.moveUnit(
            id, targetX, targetY,
            moveUnit = { uId, x, y -> moveUnit(uId, x, y) },
            lastMovePath = ::lastMovePath,
        )
        return DeferredMoveResult(res, path)
    }

    fun attackForPresentation(attackerId: String, targetId: String): TacticalActionResult =
        presentationTransactions.attack(attackerId, targetId, ::attack)

    fun castMagicForPresentation(attackerId: String, targetId: String, magicId: Int): TacticalActionResult =
        presentationTransactions.castMagic(attackerId, targetId, magicId, ::castMagic)

    fun usePropertyForPresentation(userId: String, targetId: String, itemId: Int): TacticalActionResult =
        presentationTransactions.useProperty(userId, targetId, itemId, ::useProperty)

    fun hasPendingAiUnits(): Boolean =
        presentationTransactions.hasPendingAiUnits(outcome() != null, activeFaction, units.values)

    val firedEventIds: LinkedHashSet<String> get() = journal.mutableFiredEventIds()
    var round: Int
        get() = journal.round
        private set(value) {
            journal.setRound(value)
        }
    var activeFaction: Faction
        get() = journal.activeFaction
        private set(value) {
            journal.setActiveFaction(value)
        }

    /** Selects the controllable allied camp used by deterministic actual-route verification. */
    internal fun selectVerificationFaction(faction: Faction) {
        require(faction.isPlayerSide()) { "Verification routes may only select an allied camp." }
        activeFaction = faction
    }

    private val outcomeCoordinator = BattleOutcomeCoordinator(
        units = { this.units.values },
        getRound = { round },
        enabledFeatures = { configuration.enabledFeatures },
        initialMaxRounds = 99,
    )
    val maxRounds: Int get() = outcomeCoordinator.maxRounds
    val scriptedOutcome: BattleOutcome? get() = outcomeCoordinator.scriptedOutcome
    var weather: BattleWeather
        get() = journal.weather
        private set(value) {
            journal.setWeather(value)
        }

    internal fun setWeatherFromCombat(value: BattleWeather) {
        weather = value
    }

    internal fun setPlayerMoneyFromEnvironment(value: Int) {
        playerMoney = value
    }

    internal fun setEnemyMoneyFromEnvironment(value: Int) {
        enemyMoney = value
    }

    /**
     * Compatibility wrapper for model-only callers.  Production turn flow
     * uses the individual lifecycle methods below so every source coroutine
     * barrier can be presented before the following mutation is applied.
     */
    fun endTurn(): TurnResult = roundLifecycle.endTurn()

    /** BattleScreen.restore, before its nested unitDeath callback. */
    fun settleActiveCampEnd(): CampSettlement =
        roundLifecycle.settleActiveCampEnd()

    /**
     * `_setOper` changes curCamp before RoundLayer and before `_stateProcess`.
     * This method deliberately does not apply state, reset actors, or weather.
     */
    fun advanceToNextCamp(): TurnResult {
        return roundLifecycle.advanceToNextCamp()
    }

    /** First run_script inside unitDeath, after `_stateProcess` presentation. */
    fun runActiveCampEvents(): List<String> =
        roundLifecycle.runActiveCampEvents()

    /** BattleScreen._stateProcess; mutations occur only after RoundLayer closes. */
    fun settleActiveCampStart(): CampSettlement =
        roundLifecycle.settleActiveCampStart()

    /**
     * Source `_ai2` captures its actor order after state settlement/death.
     * Resetting and sorting here prevents a future camp from being observable
     * while the preceding card or state animation is still on screen.
     */
    fun prepareActiveCampOperation() {
        roundLifecycle.prepareActiveCampOperation()
    }

    /** `addRound`, before the new-round battle script. */
    fun advanceRound(): RoundAdvance {
        return roundLifecycle.advanceRound()
    }

    /** `resetSkillTemp(T)`, after new-round script/unitDeath and before weather. */
    fun resetCompletedRoundSkillTemps(completedRound: Int) =
        roundLifecycle.resetCompletedRoundSkillTemps(completedRound)

    /** `_countCurrentWeather`/`_switchWeather`, after new-round script/death. */
    fun applyScheduledWeather(): WeatherTransition {
        return roundLifecycle.applyScheduledWeather()
    }

    fun unitAt(tileX: Int, tileY: Int): BattleUnit? = battlefield.unitAt(tileX, tileY)

    fun outcome(): BattleOutcome? = outcomeCoordinator.outcome()

    /** BattleScreen.setMaxRound: ZJHH contributes exactly four turns. */
    fun setMaxRounds(value: Int) = outcomeCoordinator.setMaxRounds(value)

    /** A ScenarioStage setMaxRound value has already applied BattleScreen.eFlag(). */
    fun setResolvedMaxRounds(value: Int) = outcomeCoordinator.setResolvedMaxRounds(value)

    fun enabledFeatureMask(): Int = configuration.enabledFeatures

    /** Recovered BattleScreen.setWeather/setRound entry points used by EditLayer2. */
    fun applyEditedWeather(value: Int) {
        weather = BattleWeather.entries[value.coerceIn(BattleWeather.entries.indices)]
    }

    fun applyEditedRound(value: Int) {
        round = value.coerceAtLeast(1)
    }

    /** BattleScreen.skillTemp/setSkillTemp/incSkillTemp, exposed for scripts. */
    fun skillTemp(unitId: String, skillId: Int, default: Int = 0): Int = skillTemps.value(unitId, skillId, default)
    fun setSkillTemp(unitId: String, skillId: Int, amount: Int, recordedRound: Int = round) =
        skillTemps.set(unitId, skillId, amount, recordedRound)

    fun incSkillTemp(unitId: String, skillId: Int): Int = skillTemps.increment(unitId, skillId, round)
    fun setBlockedTiles(values: Collection<Pair<Int, Int>>) {
        journal.clearBlockedTiles()
        journal.addBlockedTiles(values)
    }

    /**
     * The same weighted flood-fill used by BattleScreen._showMoveArea.  This
     * is exposed to the renderer so the desktop client can show the original
     * selectable movement area instead of accepting invisible movement.
     */
    fun reachableTiles(id: String): Map<Pair<Int, Int>, Int> =
        movementQueries.reachableTiles(id)

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
    ): Boolean = movementQueries.canEnterTilesIgnoringEnemyWithinMoves(
        id, ignoredEnemyId, start, targetTiles, moves,
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
        movementQueries.moveUnit(id, targetX, targetY, maxDistance)

    fun attack(attackerId: String, targetId: String, damage: Int? = null): TacticalActionResult =
        combatFacade.attack(attackerId, targetId, damage)


    /**
     * BattleScreen.showUseProperty + _usePro2 for the portable combat
     * consumables.  The original permits selecting an allied target in the
     * infantry hit area; this tactical context uses the same adjacent area.
     */
    fun useProperty(userId: String, targetId: String, itemId: Int): TacticalActionResult =
        combatFacade.useProperty(userId, targetId, itemId)

    /**
     * BattleScreen._usePro2's state mutation, shared by the player-selected
     * path and `_attack3` ZDSY.  The caller owns inventory mutation because
     * ZDSY uses ItemStore.pushProperty directly before entering _usePro2.
     */
    internal fun applyProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        consume: () -> Boolean,
    ): TacticalActionResult.Item? =
        combatFacade.applyProperty(item, target, consume, ::notifyPermanentProperty)

    /** BattleScreen.attackAction: scripted/cinematic attack outside normal turn input. */
    fun forcedAttack(attackerId: String, targetId: String): TacticalActionResult =
        combatFacade.forcedAttack(attackerId, targetId)

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
    ): TacticalActionResult = combatFacade.castMagic(attackerId, targetId, magicId, reaction, bypassCondition)

    /** Coordinate-target special magic.  SHUN_YI moves its caster to a vacant tile. */
    fun castMagicAt(attackerId: String, targetX: Int, targetY: Int, magicId: Int): TacticalActionResult =
        combatFacade.castMagicAt(attackerId, targetX, targetY, magicId)

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
        BattleAiScorer.aiSortValue(unit, configuration.terrain, configuration.terrainResumeRates)

    /**
     * Cocos BattleConfg.AI 0..9 dispatch, without presentation delays.  The
     * scripted target id/coordinates are retained from BattleUnit.setAI.
     */
    @JvmOverloads
    fun resolveAiTurn(maxUnits: Int = Int.MAX_VALUE, deferMutations: Boolean = false): AiTurnResult =
        aiFacade.resolveTurn(maxUnits, deferMutations)

    /**
     * Captures the actual AI scorer for one source character without running
     * a turn, moving a unit, or injecting an expected choice.
     */
    fun traceAiPlannerAtCurrentPoint(characterId: Int, aiFlags: Int = 1): AiPlannerTrace? =
        aiFacade.tracePlanner(characterId, aiFlags)

    /** Injectable `Control._countAttackValue` preview for one primary target. */
    fun previewAiAttackValue(attackerId: String, targetId: String): Int =
        aiFacade.previewAttackValue(attackerId, targetId)

    /**
     * Read-only ordinary physical-harm preview for input planning.  This is
     * the source `countBaseHarm` value before hit, critical, and the
     * move-dependent attack effects are rolled, so asking for it cannot
     * consume a skill temp or advance either unit's combat state.
     */
    fun previewPhysicalDamage(attackerId: String, targetId: String): Int {
        return combatFacade.physicalDamagePreview(attackerId, targetId)
    }

    internal fun canAttack(attacker: BattleUnit, target: BattleUnit): Boolean =
        BattleAiScorer.canAttack(attacker, target)

    internal fun areAllied(left: Faction, right: Faction): Boolean =
        left.isPlayerSide() == right.isPlayerSide()

    internal fun areAllied(left: BattleUnit, right: BattleUnit): Boolean =
        areAllied(left.effectiveFaction(), right.effectiveFaction())


    internal fun findMovementPath(
        unit: BattleUnit,
        targetX: Int,
        targetY: Int,
        avoidEnemies: Boolean = false,
        penalizeEnemyTiles: Boolean = false,
        allowEnemyOnTarget: Boolean = false,
    ): List<Pair<Int, Int>>? = movementQueries.findMovementPath(
        unit, targetX, targetY, avoidEnemies, penalizeEnemyTiles, allowEnemyOnTarget,
    )

    fun scriptedMovePath(characterId: Int, targetX: Int, targetY: Int): List<Pair<Int, Int>>? =
        movementQueries.scriptedMovePath(characterId, targetX, targetY)

    internal fun findReachableEmptyPosition(
        unit: BattleUnit,
        seed: Pair<Int, Int>,
        reachable: Set<Pair<Int, Int>>,
    ): Pair<Int, Int>? = movementQueries.findReachableEmptyPosition(unit, seed, reachable)

    internal fun backPosition(defender: BattleUnit, attacker: BattleUnit): Pair<Int, Int>? =
        movementQueries.backPosition(defender, attacker, ::unitAt)

    internal fun facingDirection(fromX: Int, fromY: Int, toX: Int, toY: Int): Int =
        movementQueries.facingDirection(fromX, fromY, toX, toY)

    internal fun movePoints(
        unit: BattleUnit,
        movement: Int,
        ignoredEnemyId: String? = null,
        startOverride: Pair<Int, Int>? = null,
    ) = movementQueries.movePoints(unit, movement, ignoredEnemyId, startOverride)

}
