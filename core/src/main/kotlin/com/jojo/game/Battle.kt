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
    val experience by lazy { BattleExperienceFacade(configuration, journal) { this.units } }
    val presentation by lazy {
        BattlePresentationTransactionFacade(
            battlefield = battlefield,
            units = { this.units },
            skillTemps = skillTemps,
            journal = journal,
            moveUnitOperation = { id, x, y -> movement.moveUnit(id, x, y, null) },
            lastMovePath = ::lastMovePath,
            attackOperation = { attackerId, targetId -> combat.attack(attackerId, targetId, null) },
            castMagicOperation = { attackerId, targetId, magicId ->
                combat.castMagic(attackerId, targetId, magicId, false, false)
            },
            usePropertyOperation = { userId, targetId, itemId ->
                combat.useProperty(userId, targetId, itemId)
            },
            isBattleEnded = { outcome() != null },
            activeFaction = { activeFaction },
            onUnitRetreat = configuration.onUnitRetreat,
        )
    }
    val combat by lazy { BattleCombatFacade(this) }
    val ai by lazy { BattleAiFacade(this) }
    val movement by lazy {
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
    val roundLifecycle by lazy {
        BattleRoundLifecycleFacade(
            configuration,
            journal,
            battlefield,
            { this.units.values },
            skillTemps,
            this,
            { unit -> BattleAiScorer.aiSortValue(unit, configuration.terrain, configuration.terrainResumeRates) }
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

    /** Scenario scripts can end a battle through reward()/lose() without eliminating every enemy. */
    fun setScriptedOutcome(value: BattleOutcome) = outcomeCoordinator.setScriptedOutcome(value)

    /**
     * Mirrors a ScenarioStage result without clearing an outcome on ordinary
     * scene1 passes which have not called reward/lose.  Script callbacks can
     * publish this after the initial BattleScreen script invocation.
     */
    fun syncScriptedOutcome(value: BattleOutcome?) = outcomeCoordinator.syncScriptedOutcome(value)

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

    internal fun canAttack(attacker: BattleUnit, target: BattleUnit): Boolean =
        BattleAiScorer.canAttack(attacker, target)

    internal fun areAllied(left: Faction, right: Faction): Boolean =
        left.isPlayerSide() == right.isPlayerSide()

    internal fun areAllied(left: BattleUnit, right: BattleUnit): Boolean =
        areAllied(left.effectiveFaction(), right.effectiveFaction())


}
