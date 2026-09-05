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
    fun consumeEquipmentUpgrade(): CampaignEquipmentExperienceResult? = equipmentUpgrades.removeFirstOrNull()
    /** BattleScreen._addWeaponExp entry used by settlement and deterministic route tests. */
    fun addEquipmentExperience(attackerId: String, targetId: String, damage: Int) {
        val attacker = units[attackerId] ?: return
        val target = units[targetId] ?: return
        val apply = {
            val results = onEquipmentExperienceAward?.let { award ->
                buildList {
                    addAll(award(attacker, target, equipmentExperienceAmount(attacker, target, damage, BattleEquipmentExperienceKind.WEAPON), BattleEquipmentExperienceKind.WEAPON))
                    addAll(award(target, attacker, equipmentExperienceAmount(target, attacker, damage, BattleEquipmentExperienceKind.ARMOR), BattleEquipmentExperienceKind.ARMOR))
                }
            } ?: onEquipmentExperience(attacker, target, damage)
            results.filterTo(equipmentUpgrades) { it.leveledUp }
            Unit
        }
        stagedHitSideEffects?.add(apply) ?: apply()
    }
    private fun notifyPhysicalDamage(attacker: BattleUnit, target: BattleUnit, damage: Int) {
        val apply = {
            onPhysicalDamage(attacker, target, damage)
            // Older callers use this as a per-hit presentation callback.
            // Production uses onEquipmentExperienceAward, settled below by
            // max slot reward, so it must not mutate campaign EXP here.
            if (onEquipmentExperienceAward == null) {
                onEquipmentExperience(attacker, target, damage).filterTo(equipmentUpgrades) { it.leveledUp }
            }
            Unit
        }
        stagedHitSideEffects?.add(apply) ?: apply()
    }
    private fun notifyEquipmentExperienceAward(
        recipient: BattleUnit,
        opponent: BattleUnit,
        amount: Int,
        kind: BattleEquipmentExperienceKind,
    ) {
        val award = onEquipmentExperienceAward ?: return
        val apply = { award(recipient, opponent, amount, kind).filterTo(equipmentUpgrades) { it.leveledUp }; Unit }
        stagedCompletionSideEffects?.add(apply) ?: apply()
    }
    /** Calculates the per-hit equipment experience before recipient-level max merging. */
    private fun equipmentExperienceAmount(
        recipient: BattleUnit,
        opponent: BattleUnit,
        resolvedHarm: Int,
        kind: BattleEquipmentExperienceKind,
    ): Int = when (kind) {
        BattleEquipmentExperienceKind.WEAPON -> if (resolvedHarm == 0) 1 else if (recipient.level <= opponent.level) 3 else 2
        BattleEquipmentExperienceKind.ARMOR -> if (resolvedHarm == 0) 1 else if (recipient.level <= opponent.level) 4 else 3
    }
    private fun notifyUnitDefeated(winner: BattleUnit, defeated: BattleUnit) {
        val apply = { onUnitDefeated(winner, defeated) }
        stagedCompletionSideEffects?.add(apply) ?: apply()
    }
    private fun notifyBattleExperience(unit: BattleUnit, amount: Int) {
        if (amount <= 0) return
        val apply = {
            val oldLevel = unit.level
            val persistent = onBattleExperience(unit, amount)
            if (persistent != null) {
                unit.level = persistent.level
                unit.experience = persistent.experience
            } else {
                var remaining = amount
                while (remaining > 0) {
                    val limit = experienceLimit(unit.level).coerceAtLeast(1)
                    val gained = minOf(remaining, (limit - unit.experience).coerceAtLeast(0))
                    unit.experience += gained
                    remaining -= gained
                    if (unit.experience >= limit && unit.level < levelLimit) {
                        unit.level++
                        unit.experience = 0
                    } else break
                }
            }
            if (unit.level != oldLevel) onBattleLevelUp(unit)
            Unit
        }
        stagedCompletionSideEffects?.add(apply) ?: apply()
    }

    /** BattleScreen.count_exp, before g_charinfo's per-attacker EXP_ADD max merge. */
    private fun battleExperience(attacker: BattleUnit, target: BattleUnit, defeated: Boolean): Int {
        val difference = kotlin.math.abs(target.level - attacker.level)
        var result = if (target.level >= attacker.level) 8 + maxOf(1, 2 * difference)
        else maxOf(1, 8 - difference)
        if (defeated) {
            result *= 4
            if (target.id == enemyMasterUnitId) result *= 2
        }
        attacker.skills[67]?.and(255)?.takeIf { it != 255 }?.let { result += it }
        return result
    }
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
    private fun runtimeSnapshot(): BattleActionSnapshot {
        val all = linkedMapOf<String, BattleUnit>().apply {
            putAll(units)
            battlefield.pendingPresentationUnits().forEach { unit -> put(unit.id, unit) }
        }
        return BattleActionSnapshot(
            topology = battlefield.snapshotTopology(),
            states = all.mapValues { (_, unit) -> BattleUnitMemento.capture(unit) },
            playerMoney = playerMoney,
            enemyMoney = enemyMoney,
            skillTemps = skillTemps.snapshot(),
            moveLength = moveLength,
            lastMovePaths = lastMovePaths.mapValues { it.value.toList() },
            traceActions = traceActions.toList(),
        )
    }

    private fun restoreRuntime(snapshot: BattleActionSnapshot) {
        snapshot.states.values.forEach(BattleUnitMemento::restore)
        battlefield.restoreTopology(snapshot.topology, snapshot.states.mapValues { it.value.unit })
        playerMoney = snapshot.playerMoney
        enemyMoney = snapshot.enemyMoney
        skillTemps.restore(snapshot.skillTemps)
        moveLength = snapshot.moveLength
        lastMovePaths.clear(); lastMovePaths.putAll(snapshot.lastMovePaths)
        traceActions.clear(); traceActions.addAll(snapshot.traceActions)
    }

    private fun createActionTransaction(
        actorId: String,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
        hitSideEffects: List<() -> Unit>,
        completionSideEffects: List<() -> Unit>,
    ): BattleActionTransaction = BattleActionTransaction(
        actorId = actorId,
        before = before,
        after = after,
        hitSideEffects = hitSideEffects,
        completionSideEffects = completionSideEffects,
        restoreSnapshot = ::restoreRuntime,
        adjustEconomy = { playerDelta, enemyDelta ->
            playerMoney += playerDelta
            enemyMoney += enemyDelta
        },
        presentationUnit = battlefield::presentationUnit,
        activeUnit = battlefield::activeUnit,
        onCompleted = { transaction ->
            if (pendingActionTransaction === transaction) pendingActionTransaction = null
        },
    )

    data class DeferredMoveResult(
        val result: TacticalActionResult,
        val path: List<Pair<Int, Int>>,
    )

    /**
     * Player commands use the same calculate-then-callback-commit boundary as
     * `_ai2`. The calculation consumes RNG once, while all mutable battle
     * state and external callbacks stay hidden until the authored animation
     * edge commits [pendingActionTransaction].
     */
    private fun <T : TacticalActionResult> resolveDeferredAction(
        actorId: String,
        resolve: () -> T,
    ): T {
        check(pendingActionTransaction == null) { "previous deferred battle action has not completed" }
        val before = runtimeSnapshot()
        stagedHitSideEffects = mutableListOf()
        stagedCompletionSideEffects = mutableListOf()
        val result = try {
            resolve()
        } catch (failure: Throwable) {
            stagedHitSideEffects = null
            stagedCompletionSideEffects = null
            restoreRuntime(before)
            throw failure
        }
        val hitSideEffects = stagedHitSideEffects.orEmpty().toList()
        val completionSideEffects = stagedCompletionSideEffects.orEmpty().toList()
        stagedHitSideEffects = null
        stagedCompletionSideEffects = null
        if (result is TacticalActionResult.Rejected) {
            restoreRuntime(before)
            return result
        }
        val after = runtimeSnapshot()
        restoreRuntime(before)
        pendingActionTransaction = createActionTransaction(actorId, before, after, hitSideEffects, completionSideEffects)
        return result
    }

    fun moveUnitForPresentation(id: String, targetX: Int, targetY: Int): DeferredMoveResult {
        var path = emptyList<Pair<Int, Int>>()
        val result = resolveDeferredAction(id) {
            moveUnit(id, targetX, targetY).also {
                if (it !is TacticalActionResult.Rejected) path = lastMovePath(id).toList()
            }
        }
        return DeferredMoveResult(result, path)
    }

    fun attackForPresentation(attackerId: String, targetId: String): TacticalActionResult =
        resolveDeferredAction(attackerId) { attack(attackerId, targetId) }

    fun castMagicForPresentation(attackerId: String, targetId: String, magicId: Int): TacticalActionResult =
        resolveDeferredAction(attackerId) { castMagic(attackerId, targetId, magicId) }

    fun usePropertyForPresentation(userId: String, targetId: String, itemId: Int): TacticalActionResult =
        resolveDeferredAction(userId) { useProperty(userId, targetId, itemId) }

    fun hasPendingAiUnits(): Boolean = outcome() == null && units.values.any {
        it.visible && it.effectiveFaction() == activeFaction && !it.hasActed
    }
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
    var maxRounds: Int = 99
        private set
    var weather: BattleWeather = initialWeather
        private set
    private var scriptedOutcome: BattleOutcome? = null

    /**
     * Compatibility wrapper for model-only callers.  Production turn flow
     * uses the individual lifecycle methods below so every source coroutine
     * barrier can be presented before the following mutation is applied.
     */
    fun endTurn(): TurnResult {
        settleActiveCampEnd()
        if (activeFaction == Faction.REINFORCEMENTS) {
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
            // Legacy/model callers have no visual callback to observe an
            // empty FRIEND or REINFORCEMENTS pass. Consume that no-op pass
            // atomically, while the production BattleTurnController still
            // exposes all four source coroutine phases.
            if (activeFaction == Faction.PLAYER || units.values.any {
                    it.visible && it.effectiveFaction() == activeFaction
                }
            ) break
            settleActiveCampEnd()
            if (activeFaction == Faction.REINFORCEMENTS) {
                val advance = advanceRound()
                resetCompletedRoundSkillTemps(advance.completedRound)
                applyScheduledWeather()
            }
        } while (true)
        return result.copy(round = round, activeFaction = activeFaction, firedEvents = fired)
    }

    /** BattleScreen.restore, before its nested unitDeath callback. */
    fun settleActiveCampEnd(): CampSettlement = captureSettlement(
        stage = CampSettlementStage.END_RESTORE,
        faction = activeFaction,
    ) { subflows ->
        // ctrl_mine clears XD for the entire allied side after FRIEND's AI,
        // and for the entire enemy side after REINFORCEMENTS, through an
        // authored _jiesuan before restore. It is not a _setOper mutation.
        if (activeFaction == Faction.FRIEND || activeFaction == Faction.REINFORCEMENTS) {
            val side = activeFaction.isPlayerSide()
            units.values.filter { it.effectiveFaction().isPlayerSide() == side }.forEach {
                it.hasActed = false
                it.presentation.refreshStatus(it.statuses, it.attributeLifts)
            }
        }
        processEndOfTurn(activeFaction, subflows)
    }

    /**
     * `_setOper` changes curCamp before RoundLayer and before `_stateProcess`.
     * This method deliberately does not apply state, reset actors, or weather.
     */
    fun advanceToNextCamp(): TurnResult {
        activeFaction = when (activeFaction) {
            Faction.PLAYER -> Faction.FRIEND
            Faction.FRIEND -> Faction.ENEMY
            Faction.ENEMY -> Faction.REINFORCEMENTS
            Faction.REINFORCEMENTS -> Faction.PLAYER
        }
        return TurnResult(round, activeFaction, emptyList())
    }

    /** First run_script inside unitDeath, after `_stateProcess` presentation. */
    fun runActiveCampEvents(): List<String> = events
        .asSequence()
        .filter { it.id !in firedEventIds && it.matches(this) }
        .onEach {
            firedEventIds += it.id
            it.execute(this)
        }
        .map { it.id }
        .toList()

    /** BattleScreen._stateProcess; mutations occur only after RoundLayer closes. */
    fun settleActiveCampStart(): CampSettlement = captureSettlement(
        stage = CampSettlementStage.START_STATE,
        faction = activeFaction,
    ) { subflows -> processStartOfTurn(activeFaction, subflows) }

    /**
     * Source `_ai2` captures its actor order after state settlement/death.
     * Resetting and sorting here prevents a future camp from being observable
     * while the preceding card or state animation is still on screen.
     */
    fun prepareActiveCampOperation() {
        units.values.filter { it.effectiveFaction() == activeFaction }.forEach {
            it.hasActed = false
            it.hasMoved = false
            it.aiValue = 0
        }
        // _ai2 captures this list only after _stateProcess, battle script, and
        // the first unitDeath pass have all completed.
        aiTurnOrder = units.values.asSequence()
            .filter { it.visible && it.effectiveFaction() == activeFaction && !it.hasActed }
            .sortedWith(compareByDescending<BattleUnit>(::aiSortValue).thenBy { BattleAttributeCalculator.effective(it, BattleAttribute.DEFENSE) })
            .map { it.id }
            .toList()
    }

    /** `addRound`, before the new-round battle script. */
    fun advanceRound(): RoundAdvance {
        check(activeFaction == Faction.REINFORCEMENTS) { "round may advance only after the reinforcements camp" }
        val completedRound = round
        round++
        return RoundAdvance(completedRound, round)
    }

    /** `resetSkillTemp(T)`, after new-round script/unitDeath and before weather. */
    fun resetCompletedRoundSkillTemps(completedRound: Int) {
        check(completedRound == round - 1) { "only the just-completed round may be reset" }
        skillTemps.reset(completedRound)
    }

    /** `_countCurrentWeather`/`_switchWeather`, after new-round script/death. */
    fun applyScheduledWeather(): WeatherTransition {
        val previous = weather
        if (weatherSchedule.isNotEmpty()) {
            weather = weatherSchedule[Math.floorMod(round + weatherOffset, weatherSchedule.size)]
        }
        return WeatherTransition(previous, weather)
    }

    private data class UnitTurnSnapshot(
        val hp: Int,
        val mp: Int,
        val statuses: Map<BattleStatus, Int>,
        val lifts: Map<BattleAttribute, Int>,
        val actionComplete: Boolean,
        val actionStatusRound: Int,
    )

    private fun captureSettlement(
        stage: CampSettlementStage,
        faction: Faction,
        settle: (MutableList<SettlementSubflow>) -> List<BattleUnitTurnChange>?,
    ): CampSettlement {
        val before = turnSnapshot()
        val subflows = mutableListOf<SettlementSubflow>()
        val primaryChanges = settle(subflows)
        val changes = primaryChanges ?: turnChanges(before)
        return CampSettlement(stage, faction, changes, subflows, subflowsCaptured = true)
    }

    private fun turnSnapshot(): Map<String, UnitTurnSnapshot> = units.mapValues { (_, unit) ->
        UnitTurnSnapshot(
            hp = unit.hitPoints,
            mp = unit.magicPoints,
            statuses = unit.statuses.toMap(),
            lifts = unit.attributeLifts.toMap(),
            actionComplete = unit.hasActed,
            actionStatusRound = unit.actionStatusRound,
        )
    }

    private fun turnChanges(before: Map<String, UnitTurnSnapshot>): List<BattleUnitTurnChange> =
        before.mapNotNull { (id, old) ->
            val unit = battlefield.presentationUnit(id) ?: return@mapNotNull null
            val changed = old.hp != unit.hitPoints || old.mp != unit.magicPoints ||
                old.statuses != unit.statuses || old.lifts != unit.attributeLifts ||
                old.actionComplete != unit.hasActed || old.actionStatusRound != unit.actionStatusRound
            if (!changed) return@mapNotNull null
            BattleUnitTurnChange(
                unitId = id,
                hitPointsBefore = old.hp,
                hitPointsAfter = unit.hitPoints,
                magicPointsBefore = old.mp,
                magicPointsAfter = unit.magicPoints,
                statusesBefore = old.statuses,
                statusesAfter = unit.statuses.toMap(),
                attributeLiftsBefore = old.lifts,
                attributeLiftsAfter = unit.attributeLifts.toMap(),
                actionCompleteBefore = old.actionComplete,
                actionCompleteAfter = unit.hasActed,
                actionStatusRoundBefore = old.actionStatusRound,
                actionStatusRoundAfter = unit.actionStatusRound,
            )
        }

    fun unitAt(tileX: Int, tileY: Int): BattleUnit? = battlefield.unitAt(tileX, tileY)

    fun outcome(): BattleOutcome? {
        scriptedOutcome?.let { return it }
        // BATTLE_UNIT_FALG.HIDE changes rendering/targeting, not isExist().
        // Yingchuan's only Mine actor is hidden until round two and must still
        // prevent the opening cut-scene from being adjudicated as a loss.
        val playerRemaining = units.values.any { it.effectiveFaction().isPlayerSide() }
        val enemyRemaining = units.values.any { it.effectiveFaction().isEnemySide() }
        return when {
            round >= maxRounds -> BattleOutcome.ENEMY_VICTORY
            !enemyRemaining && playerRemaining -> BattleOutcome.PLAYER_VICTORY
            !playerRemaining && enemyRemaining -> BattleOutcome.ENEMY_VICTORY
            else -> null
        }
    }

    /** BattleScreen.setMaxRound: ZJHH contributes exactly four turns. */
    fun setMaxRounds(value: Int) {
        maxRounds = (value + if (enabledFeatures and ENABLED_FEATURE_ZJHH != 0) 4 else 0).coerceAtLeast(1)
    }

    /** A ScenarioStage setMaxRound value has already applied BattleScreen.eFlag(). */
    fun setResolvedMaxRounds(value: Int) { maxRounds = value.coerceAtLeast(1) }

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

    /**
     * The same weighted flood-fill used by BattleScreen._showMoveArea.  This
     * is exposed to the renderer so the desktop client can show the original
     * selectable movement area instead of accepting invisible movement.
     */
    fun reachableTiles(id: String): Map<Pair<Int, Int>, Int> {
        val unit = units[id] ?: return emptyMap()
        // BattleScreen.canMovePoints exits before seeding psAry for MaBi.
        if (!unit.visible || BattleStatus.PARALYSIS in unit.statuses || unit.hasMoved || unit.hasActed) return emptyMap()
        // BattleScreen.canMovePoints seeds its walk with BattleUnit.mov_final,
        // which includes the active weather penalty.  The selectable overlay
        // must use that value too; using only the attribute lift shortened
        // S_00 unit 210's source range by five tiles.
        val movement = BattleAttributeCalculator.finalMovement(unit, weather)
        // The source returns every psHash entry to `_showMoveArea`, including
        // the actor's current tile and same-camp occupied tiles.  Move
        // execution separately rejects occupied destinations, but omitting
        // them here made the rendered range differ from the original.
        return movePoints(unit, movement).points
            .mapValuesTo(linkedMapOf()) { (_, point) -> movement - point.remaining }
    }

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
    ): Boolean {
        val unit = units[id] ?: return false
        if (!unit.visible || targetTiles.isEmpty() || moves < 1) return false
        val movement = BattleAttributeCalculator.finalMovement(unit, weather)
        var frontier = linkedSetOf(start)
        repeat(moves) {
            val next = linkedSetOf<Pair<Int, Int>>()
            frontier.forEach { origin ->
                movePoints(unit, movement, ignoredEnemyId, origin).points.keys.forEach { tile ->
                    // `movePoints` retains same-camp occupants for the source
                    // overlay. A real next command cannot end on one; nor can
                    // it end on a still-live enemy other than the removed guard.
                    val occupant = unitAt(tile.first, tile.second)
                    if (tile == origin || occupant == null || occupant.id == ignoredEnemyId) next += tile
                }
            }
            if (next.any { it in targetTiles }) return true
            frontier = next
            if (frontier.isEmpty()) return false
        }
        return false
    }
    /** Scenario scripts can end a battle through reward()/lose() without eliminating every enemy. */
    fun setScriptedOutcome(value: BattleOutcome) { scriptedOutcome = value }

    /**
     * Mirrors a ScenarioStage result without clearing an outcome on ordinary
     * scene1 passes which have not called reward/lose.  Script callbacks can
     * publish this after the initial BattleScreen script invocation.
     */
    fun syncScriptedOutcome(value: BattleOutcome?) {
        value?.let { scriptedOutcome = it }
    }

    fun moveUnit(id: String, targetX: Int, targetY: Int, maxDistance: Int? = null): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val unit = units[id] ?: return TacticalActionResult.Rejected("유닛이 없습니다.")
        if (!unit.visible) return TacticalActionResult.Rejected("아직 등장하지 않은 유닛입니다.")
        if (BattleStatus.PARALYSIS in unit.statuses || BattleStatus.CONFUSION in unit.statuses) return TacticalActionResult.Rejected("행동할 수 없는 상태입니다.")
        if (unit.effectiveFaction() != activeFaction) return TacticalActionResult.Rejected("현재 진영의 유닛만 조작할 수 있습니다.")
        if (unit.hasActed) return TacticalActionResult.Rejected("이미 행동한 유닛입니다.")
        if (unit.hasMoved) return TacticalActionResult.Rejected("이미 이동한 유닛입니다.")
        if (targetX < 0 || targetY < 0 || terrain?.let { targetX >= it.width || targetY >= it.height } == true) {
            return TacticalActionResult.Rejected("맵 밖으로 이동할 수 없습니다.")
        }
        if (targetX to targetY in blockedTiles) return TacticalActionResult.Rejected("장애물이 있는 칸입니다.")
        if (unitAt(targetX, targetY) != null) return TacticalActionResult.Rejected("다른 유닛이 있는 칸입니다.")
        val route = movePoints(unit, maxDistance ?: BattleAttributeCalculator.finalMovement(unit, weather))
        val destination = targetX to targetY
        if (destination !in route.points) return TacticalActionResult.Rejected("이동 범위를 벗어났습니다.")
        val path = route.pathTo(destination)
        // BattleScreen.unitMove returns `s.length`, and sends that exact same
        // start-inclusive `s` array into BattleUnit.move2.
        val nodes = path.size
        // move2 synchronously runs its first segment's setAction2 callback;
        // it does not face toward the dominant destination axis up front.
        path.getOrNull(1)?.let { first ->
            unit.direction = facingDirection(unit.tileX, unit.tileY, first.first, first.second)
        }
        unit.tileX = targetX
        unit.tileY = targetY
        moveLength = nodes
        lastMovePaths[id] = path
        unit.hasMoved = true
        return TacticalActionResult.Success
    }

    fun attack(attackerId: String, targetId: String, damage: Int? = null): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val attacker = units[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val target = units[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        if (!attacker.visible || !target.visible) return TacticalActionResult.Rejected("아직 등장하지 않은 유닛입니다.")
        if (attacker.effectiveFaction() != activeFaction) return TacticalActionResult.Rejected("현재 진영의 유닛만 조작할 수 있습니다.")
        if (BattleStatus.PARALYSIS in attacker.statuses || BattleStatus.CONFUSION in attacker.statuses) return TacticalActionResult.Rejected("행동할 수 없는 상태입니다.")
        if (areAllied(attacker, target)) return TacticalActionResult.Rejected("아군을 공격할 수 없습니다.")
        if (attacker.hasActed) return TacticalActionResult.Rejected("이미 행동한 유닛입니다.")
        val offset = target.tileX - attacker.tileX to target.tileY - attacker.tileY
        if (!attacker.attackAllScreen && offset !in attacker.attackOffsets) return TacticalActionResult.Rejected("공격 범위를 벗어난 적입니다.")
        // _attack2 first determines its one/two-hit loop, then creates
        // `o = e.getAtkStatus()` before entering the per-target hit loop.
        // Keep that random order and retain the result for the second pass.
        val plannedContinuousAttack = probabilityResolver.continuousAttack(attacker, target)
        // BattleScreen._attack2 creates `o = e.getAtkStatus()` before the
        // per-target hit loop.  The same records are then passed to every
        // _attack3 call in this attack sequence.
        val attackStatusBatch = rollAttackStatusBatch(attacker)
        // `_attack2` advances the opposed critical gauges before
        // countAtkHarm performs its hit check. A miss still changes BJL and
        // BBJL, which is observable on the following attack.
        val criticalRoll = damage == null && probabilityResolver.criticalHit(attacker, target)
        // BattleUnit.countBaseHarm/count_hitRate in the original client.
        // Skill, state and terrain modifiers are added separately; the base
        // arm restraint is already supplied by the original arms table.
        val hitRate = probabilityResolver.physicalHitRate(attacker, target)
        val hit = probabilityResolver.physicalHit(attacker, target, hitRate)
        val baseDamage = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            basePhysicalDamageContext(attacker, target, splash = false),
        )
        val critical = hit && criticalRoll &&
            !(target.skills[49]?.and(255)?.let { it != 255 } == true && attacker.skills[227]?.and(255)?.let { it != 255 } != true)
        // count_attackHarm consumes XU_SHI while constructing this active
        // attack's target records, before hit animation/result resolution.
        val xuShiDamage = if (damage == null) consumeXuShiDamage(attacker) else 0
        val specialDamage = if (hit && damage == null) mrspDamage(attacker, target) else null
        val resolvedDamage = if (hit) {
            specialDamage ?: run {
                // The optional value is the source attack's starting harm,
                // not an already-settled result.  `_attack3` still applies
                // the ordinary percentage resistance and flat skill chain
                // to scripted/test supplied harm before HP is changed.
                if (damage == 0) return@run 0
                var normalDamage = damage?.coerceAtLeast(0)
                    ?: maxOf(1, baseDamage * PhysicalDamageCalculator.physicalArmRestraint(attacker, target) / 100)
                normalDamage = normalDamage * PhysicalDamageCalculator.physicalDamageRate(
                    attacker,
                    target,
                    physicalDamageRateContext(attacker, target),
                ) / 100
                normalDamage = BattleAttributeCalculator.physicalDamageAfterResistance(normalDamage, attacker, target)
                normalDamage += PhysicalDamageCalculator.physicalFlatSkillDamage(
                    attacker,
                    target,
                    flatPhysicalDamageContext(attacker, activeAttack = true),
                ) + xuShiDamage
                normalDamage = maxOf(1, normalDamage)
                normalDamage = PhysicalDamageCalculator.armorPiercingMinimumDamage(attacker, target, normalDamage)
                normalDamage = PhysicalDamageCalculator.cappedPhysicalDamage(target, normalDamage)
                maxOf(
                    PhysicalDamageCalculator.physicalMinimumDamage(attacker, visibleFamousPlayerCount()),
                    normalDamage * PhysicalDamageCalculator.physicalCriticalRate(
                        attacker,
                        target,
                        physicalCriticalRateContext(attacker, target, critical),
                    ) / 100,
                )
            }
        } else 0
        if (hit && specialDamage == null) consumeMpAttackSkill(attacker)
        // `countAtkHarm` materializes the complete target array before the
        // attack animation reaches its hit callback.  In particular, TPGJ
        // movement or a death callback from primary `_attack3` must not
        // change this pass's already-selected CTGJ targets.
        val primarySplashHarms = if (damage == null) {
            // countAtkHarm receives the raw CRIT flag. A primary miss does
            // not clear it before CTGJ harm is calculated.
            computePhysicalSplashHarms(attacker, target, criticalRoll)
        } else {
            emptyList()
        }
        val physicalPasses = mutableListOf<PhysicalAttackPass>()
        // g_charinfo stores one EXP_ADD entry per attacker and merges repeated
        // target/pass writes with max(), not sum(). Apply only after the full
        // active/counter action has settled so a level-up cannot affect a
        // follow-up that was already part of the same source `_attack`.
        // BattleUnit is a mutable data class, so it must never be a hash key.
        val experienceByAttacker = linkedMapOf<String, Pair<BattleUnit, Int>>()
        fun recordExperience(source: BattleUnit, victim: BattleUnit, victimDefeated: Boolean) {
            val reward = battleExperience(source, victim, victimDefeated)
            experienceByAttacker[source.id] = source to maxOf(experienceByAttacker[source.id]?.second ?: 0, reward)
        }
        data class EquipmentExperienceRecord(
            val recipient: BattleUnit,
            val opponent: BattleUnit,
            val kind: BattleEquipmentExperienceKind,
            val amount: Int,
        )
        val equipmentByRecipient = linkedMapOf<Pair<String, BattleEquipmentExperienceKind>, EquipmentExperienceRecord>()
        fun recordEquipment(recipient: BattleUnit, opponent: BattleUnit, resolvedHarm: Int, kind: BattleEquipmentExperienceKind) {
            val amount = equipmentExperienceAmount(recipient, opponent, resolvedHarm, kind)
            val key = recipient.id to kind
            if (amount > (equipmentByRecipient[key]?.amount ?: 0)) {
                equipmentByRecipient[key] = EquipmentExperienceRecord(recipient, opponent, kind, amount)
            }
        }
        fun recordPhysicalEquipment(source: BattleUnit, victim: BattleUnit, resolvedHarm: Int) {
            // Every resolved hit awards armor experience to the defender.
            // Weapon experience is suppressed only for a civil attacker.
            recordEquipment(victim, source, resolvedHarm, BattleEquipmentExperienceKind.ARMOR)
            if (source.armType != 1) recordEquipment(source, victim, resolvedHarm, BattleEquipmentExperienceKind.WEAPON)
        }
        val splashTargets = mutableListOf<PhysicalTarget>()
        var moneyShieldSpent = 0
        var blockRetaliationDamage = 0
        var lifeStealHealing = 0
        var qxlHealing = 0
        var recoilDamage = 0
        var playerMoneyDelta = 0
        var enemyMoneyDelta = 0
        var automaticProperty: TacticalActionResult.Item? = null
        var counterLifeStealHealing = 0
        fun recordResolution(result: PhysicalAttackTargetResult, counter: Boolean = false) {
            moneyShieldSpent += result.moneyShieldSpent
            blockRetaliationDamage += result.blockRetaliations.sumOf { it.damage }
            if (counter) counterLifeStealHealing += result.lifeStealHealing else lifeStealHealing += result.lifeStealHealing
            qxlHealing += result.qxlHealing
            recoilDamage += result.recoilDamage
            playerMoneyDelta += result.playerMoneyDelta
            enemyMoneyDelta += result.enemyMoneyDelta
            if (automaticProperty == null) automaticProperty = result.automaticProperty
        }
        val primaryPassTargets = mutableListOf<PhysicalAttackTargetResult>()
        val primaryTransfer = if (hit) physicalDamageTransfer(attacker, target, resolvedDamage) else null
        val primarySourceHarm = resolvedDamage - (primaryTransfer?.second ?: 0)
        val primaryResolution = resolvePhysicalTarget(
            attacker, target, primarySourceHarm, attackStatusBatch, activeAttack = damage == null,
        ).also(::recordResolution)
        recordExperience(attacker, target, primaryResolution.defeated)
        recordPhysicalEquipment(attacker, target, primaryResolution.resolvedHarm)
        primaryPassTargets += primaryResolution
        primaryTransfer?.let { (affected, harm) ->
            val result = resolvePhysicalTarget(attacker, affected, harm, attackStatusBatch, activeAttack = damage == null)
            recordResolution(result)
            primaryPassTargets += result
            recordExperience(attacker, affected, result.defeated)
            recordPhysicalEquipment(attacker, affected, result.resolvedHarm)
        }
        primarySplashHarms.forEach { (affected, harm) ->
            val result = resolvePhysicalTarget(attacker, affected, harm, attackStatusBatch, activeAttack = true)
            recordResolution(result)
            primaryPassTargets += result
            // countAtkHarm's CTGJ record keeps the calculated `o` payload;
            // _attack3 clamps only the later HP mutation.  Preserve that
            // legacy payload here while physicalPasses exposes settled damage.
            splashTargets += PhysicalTarget(result.targetId, harm)
            recordExperience(attacker, affected, result.defeated)
            recordPhysicalEquipment(attacker, affected, result.resolvedHarm)
        }
        // countAtkHarm returns its incoming CRIT flag even when the hit is
        // guarded/missed; source checkCrit/say4 is gated by that raw flag.
        val primaryCriticalSpeech = resolveCriticalSpeech(attacker, criticalRoll)
        physicalPasses += PhysicalAttackPass(
            kind = PhysicalAttackPassKind.ACTIVE,
            attackerId = attacker.id,
            // `_attack2` chooses anime21 from its CRIT flag before
            // countAtkHarm can turn the hit into FYZMGJ guard/miss.
            critical = criticalRoll,
            targets = primaryPassTargets,
            primaryTargetId = target.id,
            criticalSpeech = primaryCriticalSpeech,
        )
        // Keep Attack.damage's established count_attackHarm payload: normal
        // HP harm is the calculated value even when the victim has less HP.
        // `_attack3`-local shields are the exceptions and expose 0/1.  The
        // per-target pass result separately retains the actual clamped HP
        // delta required by the renderer.
        val primaryHpDamage = primaryResolution.let { resolution ->
            when {
                resolution.mpShieldDamage > 0 -> 0
                resolution.moneyShieldSpent > 0 -> resolution.damage
                else -> primarySourceHarm
            }
        }
        val mpShieldDamage = primaryResolution.mpShieldDamage
        attacker.markActionComplete()
        var defeated = target.hitPoints <= 0
        var followUpDamage = 0
        var followUpMpShieldDamage = 0
        var followUpCritical = false
        // BattleScreen._attack2 starts with the count_sjl decision, but a
        // landed critical with BJBLJ changes its loop limit from one to two
        // before the next iteration.  This is not a separate proc: it is the
        // same two-pass loop, so it must also work when the ordinary SJL roll
        // did not grant a follow-up.
        val criticalFollowUp = criticalRoll && attacker.skills[7]?.and(255)?.let { it != 255 } == true // BJBLJ
        if (attacker.hitPoints > 0 && !defeated && (plannedContinuousAttack || criticalFollowUp)) {
            val followUpCriticalRoll = probabilityResolver.criticalHit(attacker, target)
            val followUpHit = target.skills[47]?.and(255)?.let { it != 255 } != true && probabilityResolver.physicalHit(attacker, target, hitRate)
            val followUpIsCritical = followUpHit && followUpCriticalRoll
            val followUpPassTargets = mutableListOf<PhysicalAttackTargetResult>()
            followUpCritical = followUpIsCritical
            val followUpSpecialDamage = if (followUpHit) mrspDamage(attacker, target) else null
            val followUpSourceHarm = if (followUpHit) {
                followUpSpecialDamage ?: run {
                    var raw = maxOf(1, baseDamage * PhysicalDamageCalculator.physicalArmRestraint(attacker, target) / 100)
                    raw = raw * PhysicalDamageCalculator.physicalDamageRate(
                        attacker,
                        target,
                        physicalDamageRateContext(attacker, target),
                    ) / 100
                    raw = BattleAttributeCalculator.physicalDamageAfterResistance(raw, attacker, target) +
                        PhysicalDamageCalculator.physicalFlatSkillDamage(
                            attacker,
                            target,
                            flatPhysicalDamageContext(attacker, activeAttack = false),
                        )
                    raw = maxOf(1, raw)
                    raw = PhysicalDamageCalculator.armorPiercingMinimumDamage(attacker, target, raw)
                    maxOf(
                        PhysicalDamageCalculator.physicalMinimumDamage(attacker, visibleFamousPlayerCount()),
                        PhysicalDamageCalculator.cappedPhysicalDamage(target, raw) *
                            PhysicalDamageCalculator.physicalCriticalRate(
                                attacker,
                                target,
                                physicalCriticalRateContext(attacker, target, followUpIsCritical, continuous = true),
                            ) / 100,
                    )
                }
            } else 0
            if (followUpHit && followUpSpecialDamage == null) consumeMpAttackSkill(attacker)
            val followUpSplashHarms = if (damage == null) {
                computePhysicalSplashHarms(attacker, target, followUpCriticalRoll, continuous = true)
            } else {
                emptyList()
            }
            val transfer = if (followUpHit) physicalDamageTransfer(attacker, target, followUpSourceHarm) else null
            val primaryHarm = followUpSourceHarm - (transfer?.second ?: 0)
            val followUpPrimary = resolvePhysicalTarget(
                attacker,
                target,
                primaryHarm,
                attackStatusBatch,
                activeAttack = damage == null,
            )
            recordExperience(attacker, target, followUpPrimary.defeated)
            recordPhysicalEquipment(attacker, target, followUpPrimary.resolvedHarm)
            recordResolution(followUpPrimary)
            followUpPassTargets += followUpPrimary
            followUpDamage = followUpPrimary.damage
            followUpMpShieldDamage = followUpPrimary.mpShieldDamage
            transfer?.let { (affected, harm) ->
                val result = resolvePhysicalTarget(attacker, affected, harm, attackStatusBatch, activeAttack = damage == null)
                recordResolution(result)
                followUpPassTargets += result
                recordExperience(attacker, affected, result.defeated)
                recordPhysicalEquipment(attacker, affected, result.resolvedHarm)
            }
            followUpSplashHarms.forEach { (affected, harm) ->
                val result = resolvePhysicalTarget(attacker, affected, harm, attackStatusBatch, activeAttack = true)
                recordResolution(result)
                followUpPassTargets += result
                splashTargets += PhysicalTarget(result.targetId, harm)
                recordExperience(attacker, affected, result.defeated)
                recordPhysicalEquipment(attacker, affected, result.resolvedHarm)
            }
            val followUpCriticalSpeech = resolveCriticalSpeech(attacker, followUpCriticalRoll)
            physicalPasses += PhysicalAttackPass(
                kind = PhysicalAttackPassKind.ACTIVE_FOLLOW_UP,
                attackerId = attacker.id,
                critical = followUpCriticalRoll,
                targets = followUpPassTargets,
                primaryTargetId = target.id,
                criticalSpeech = followUpCriticalSpeech,
            )
            defeated = target.hitPoints <= 0
        }
        // BattleScreen._attack6 gives CLFJ its configured magic counter first.
        // A legal `_magic` result suppresses physical retaliation entirely.
        val counterMagic = target.skills[13]?.and(255)?.takeIf { it != 255 }
            ?.let { magicId -> castMagic(target.id, attacker.id, magicId, reaction = true) as? TacticalActionResult.Magic }
        val canCounter = counterMagic == null && attacker.hitPoints > 0 && !defeated && target.visible && attacker.skills[226]?.and(255)?.let { it == 255 } != false && canAttack(target, attacker) &&
            BattleStatus.PARALYSIS !in target.statuses && BattleStatus.CONFUSION !in target.statuses
        var counterDamage = 0
        var counterFollowUpDamage = 0
        var counterMpShieldDamage = 0
        var counterFollowUpMpShieldDamage = 0
        var counterCriticalResult = false
        var counterFollowUpCritical = false
        if (canCounter) {
            val counterStatusBatch = rollAttackStatusBatch(target)
            val counterHitRate = probabilityResolver.physicalHitRate(target, attacker)
            val counterCriticalRoll = probabilityResolver.criticalHit(target, attacker)
            val counterHit = probabilityResolver.physicalHit(target, attacker, counterHitRate)
            val counterBase = PhysicalDamageCalculator.basePhysicalDamage(
                target,
                attacker,
                basePhysicalDamageContext(target, attacker, splash = false),
            )
            val counterCritical = counterHit && counterCriticalRoll
            val counterPassTargets = mutableListOf<PhysicalAttackTargetResult>()
            counterCriticalResult = counterCritical
            val counterSourceHarm = if (counterHit) {
                var counterRaw = maxOf(1, counterBase * PhysicalDamageCalculator.physicalArmRestraint(target, attacker) / 100)
                counterRaw = counterRaw * PhysicalDamageCalculator.physicalDamageRate(
                    target,
                    attacker,
                    physicalDamageRateContext(target, attacker),
                ) / 100
                counterRaw = BattleAttributeCalculator.physicalDamageAfterResistance(counterRaw, target, attacker) +
                    PhysicalDamageCalculator.physicalFlatSkillDamage(
                        target,
                        attacker,
                        flatPhysicalDamageContext(target, activeAttack = false),
                    )
                counterRaw = maxOf(1, counterRaw)
                counterRaw = PhysicalDamageCalculator.armorPiercingMinimumDamage(target, attacker, counterRaw)
                maxOf(
                    PhysicalDamageCalculator.physicalMinimumDamage(target, visibleFamousPlayerCount()),
                    PhysicalDamageCalculator.cappedPhysicalDamage(attacker, counterRaw) *
                        PhysicalDamageCalculator.physicalCriticalRate(
                            target,
                            attacker,
                            physicalCriticalRateContext(target, attacker, counterCritical, counter = true),
                        ) / 100,
                )
            } else 0
            if (counterHit) {
                consumeMpAttackSkill(target)
            }
            val counterSplashHarms = computePhysicalSplashHarms(
                attacker = target,
                primaryTarget = attacker,
                critical = counterCriticalRoll,
                activeAttack = false,
                counter = true,
            )
            val transfer = if (counterHit) physicalDamageTransfer(target, attacker, counterSourceHarm) else null
            val primaryHarm = counterSourceHarm - (transfer?.second ?: 0)
            val counterPrimary = resolvePhysicalTarget(target, attacker, primaryHarm, counterStatusBatch, activeAttack = false)
            recordExperience(target, attacker, counterPrimary.defeated)
            recordPhysicalEquipment(target, attacker, counterPrimary.resolvedHarm)
            recordResolution(counterPrimary, counter = true)
            counterPassTargets += counterPrimary
            counterDamage = counterPrimary.damage
            counterMpShieldDamage = counterPrimary.mpShieldDamage
            transfer?.let { (affected, harm) ->
                val result = resolvePhysicalTarget(target, affected, harm, counterStatusBatch, activeAttack = false)
                recordResolution(result, counter = true)
                counterPassTargets += result
                recordExperience(target, affected, result.defeated)
                recordPhysicalEquipment(target, affected, result.resolvedHarm)
            }
            counterSplashHarms.forEach { (affected, harm) ->
                val result = resolvePhysicalTarget(target, affected, harm, counterStatusBatch, activeAttack = false)
                recordResolution(result, counter = true)
                counterPassTargets += result
                recordExperience(target, affected, result.defeated)
                recordPhysicalEquipment(target, affected, result.resolvedHarm)
            }
            val counterCriticalSpeech = resolveCriticalSpeech(target, counterCriticalRoll)
            physicalPasses += PhysicalAttackPass(
                kind = PhysicalAttackPassKind.COUNTER,
                attackerId = target.id,
                critical = counterCriticalRoll,
                targets = counterPassTargets,
                primaryTargetId = attacker.id,
                criticalSpeech = counterCriticalSpeech,
            )
            // _attack2 applies BJBLJ from the raw critical roll even when
            // countAtkHarm settles that pass as a zero-harm guard/miss.
            val forcedCounterFollowUp =
                listOf(197, 43).any { target.skills[it]?.and(255)?.let { value -> value != 255 } == true } ||
                    (counterCriticalRoll && target.skills[7]?.and(255)?.let { value -> value != 255 } == true)
            if (attacker.hitPoints > 0 && forcedCounterFollowUp) {
                val secondCriticalRoll = probabilityResolver.criticalHit(target, attacker)
                val secondHit = probabilityResolver.physicalHit(target, attacker, counterHitRate)
                val counterFollowUpTargets = mutableListOf<PhysicalAttackTargetResult>()
                counterFollowUpCritical = secondHit && secondCriticalRoll
                val counterFollowUpSourceHarm = if (secondHit) {
                    var raw = maxOf(1, counterBase * PhysicalDamageCalculator.physicalArmRestraint(target, attacker) / 100)
                    raw = raw * PhysicalDamageCalculator.physicalDamageRate(
                        target,
                        attacker,
                        physicalDamageRateContext(target, attacker),
                    ) / 100
                    raw = BattleAttributeCalculator.physicalDamageAfterResistance(raw, target, attacker) +
                        PhysicalDamageCalculator.physicalFlatSkillDamage(
                            target,
                            attacker,
                            flatPhysicalDamageContext(target, activeAttack = false),
                        )
                    raw = maxOf(1, raw)
                    raw = PhysicalDamageCalculator.armorPiercingMinimumDamage(target, attacker, raw)
                    maxOf(
                        PhysicalDamageCalculator.physicalMinimumDamage(target, visibleFamousPlayerCount()),
                        PhysicalDamageCalculator.cappedPhysicalDamage(attacker, raw) *
                            PhysicalDamageCalculator.physicalCriticalRate(
                                target,
                                attacker,
                                physicalCriticalRateContext(
                                    target,
                                    attacker,
                                    secondCriticalRoll,
                                    counter = true,
                                    continuous = true,
                                ),
                            ) / 100,
                    )
                } else 0
                if (secondHit) consumeMpAttackSkill(target)
                val counterFollowUpSplashHarms = computePhysicalSplashHarms(
                    attacker = target,
                    primaryTarget = attacker,
                    critical = secondCriticalRoll,
                    activeAttack = false,
                    counter = true,
                    continuous = true,
                )
                val transfer = if (secondHit) physicalDamageTransfer(target, attacker, counterFollowUpSourceHarm) else null
                val primaryHarm = counterFollowUpSourceHarm - (transfer?.second ?: 0)
                val secondPrimary = resolvePhysicalTarget(target, attacker, primaryHarm, counterStatusBatch, activeAttack = false)
                recordExperience(target, attacker, secondPrimary.defeated)
                recordPhysicalEquipment(target, attacker, secondPrimary.resolvedHarm)
                recordResolution(secondPrimary, counter = true)
                counterFollowUpTargets += secondPrimary
                counterFollowUpDamage = secondPrimary.damage
                counterFollowUpMpShieldDamage = secondPrimary.mpShieldDamage
                transfer?.let { (affected, harm) ->
                    val result = resolvePhysicalTarget(target, affected, harm, counterStatusBatch, activeAttack = false)
                    recordResolution(result, counter = true)
                    counterFollowUpTargets += result
                    recordExperience(target, affected, result.defeated)
                    recordPhysicalEquipment(target, affected, result.resolvedHarm)
                }
                counterFollowUpSplashHarms.forEach { (affected, harm) ->
                    val result = resolvePhysicalTarget(target, affected, harm, counterStatusBatch, activeAttack = false)
                    recordResolution(result, counter = true)
                    counterFollowUpTargets += result
                    recordExperience(target, affected, result.defeated)
                    recordPhysicalEquipment(target, affected, result.resolvedHarm)
                }
                val counterFollowUpCriticalSpeech = resolveCriticalSpeech(target, secondCriticalRoll)
                physicalPasses += PhysicalAttackPass(
                    kind = PhysicalAttackPassKind.COUNTER_FOLLOW_UP,
                    attackerId = target.id,
                    critical = secondCriticalRoll,
                    targets = counterFollowUpTargets,
                    primaryTargetId = attacker.id,
                    criticalSpeech = counterFollowUpCriticalSpeech,
                )
            }
        }
        val attackerDefeated = attacker.hitPoints <= 0
        if (attackerDefeated) battlefield.defeat(attacker.id)
        experienceByAttacker.values.forEach { (unit, reward) -> notifyBattleExperience(unit, reward) }
        equipmentByRecipient.values.forEach { record ->
            notifyEquipmentExperienceAward(record.recipient, record.opponent, record.amount, record.kind)
        }
        return TacticalActionResult.Attack(
            damage = primaryHpDamage,
            defeated = defeated,
            hitRate = hitRate,
            hit = hit,
            critical = critical,
            counterDamage = counterDamage,
            attackerDefeated = attackerDefeated,
            lifeStealHealing = lifeStealHealing,
            followUpDamage = followUpDamage,
            followUpMpShieldDamage = followUpMpShieldDamage,
            counterFollowUpDamage = counterFollowUpDamage,
            counterMpShieldDamage = counterMpShieldDamage,
            counterFollowUpMpShieldDamage = counterFollowUpMpShieldDamage,
            counterLifeStealHealing = counterLifeStealHealing,
            followUpCritical = followUpCritical,
            counterCritical = counterCriticalResult,
            counterFollowUpCritical = counterFollowUpCritical,
            splashTargets = splashTargets,
            mpShieldDamage = mpShieldDamage,
            qxlHealing = qxlHealing,
            recoilDamage = recoilDamage,
            blockRetaliationDamage = blockRetaliationDamage,
            moneyShieldSpent = moneyShieldSpent,
            playerMoneyDelta = playerMoneyDelta,
            enemyMoneyDelta = enemyMoneyDelta,
            counterMagic = counterMagic,
            counterMagicId = counterMagic?.let { target.skills[13]?.and(255) },
            automaticProperty = automaticProperty,
            physicalPasses = physicalPasses,
        )
    }

    private fun basePhysicalDamageContext(
        attacker: BattleUnit,
        target: BattleUnit,
        splash: Boolean,
        defenseRule: PhysicalDefenseRule = PhysicalDefenseRule.ATTACKER_AWARE,
    ): BasePhysicalDamageContext = BasePhysicalDamageContext(
        attackTerrainImpact = attacker.terrainImpacts[terrain?.terrainAt(attacker.tileX, attacker.tileY)] ?: 100,
        defenseTerrainImpact = target.terrainImpacts[terrain?.terrainAt(target.tileX, target.tileY)] ?: 100,
        visiblePlayerUnitCount = units.values.count { it.visible && it.isPlayerSide() },
        splash = splash,
        defenseRule = defenseRule,
    )

    private fun flatPhysicalDamageContext(attacker: BattleUnit, activeAttack: Boolean): FlatPhysicalDamageContext =
        FlatPhysicalDamageContext(
            activeAttack = activeAttack,
            charge = if (activeAttack) skillTemp(attacker.id, 26) else 0,
            moveLength = moveLength,
            adjacentOccupiedCount = infantryOffsets.count { (dx, dy) ->
                unitAt(attacker.tileX + dx, attacker.tileY + dy)?.visible == true
            },
        )

    private fun visibleFamousPlayerCount(): Int =
        units.values.count { it.visible && it.isPlayerSide() && it.famous }

    /** BattleUnit._countAttackHarmAdd: MPGJ consumes one MP for a normal hit. */
    private fun consumeMpAttackSkill(attacker: BattleUnit) {
        if (attacker.skills[4]?.and(255)?.let { it != 255 } == true) attacker.addMpcur(-1)
    }

    /** BattleUnit.count_attackHarm's one-shot XU_SHI(243) addition. */
    private fun consumeXuShiDamage(attacker: BattleUnit): Int {
        val effect = attacker.skills[243]?.and(255)?.takeIf { it != 255 } ?: return 0
        val stored = skillTemp(attacker.id, 243)
        if (stored < 1) return 0
        setSkillTemp(attacker.id, 243, 0)
        return stored * effect
    }

    /** BattleScreen._jiesuan's CHGJ increment for a ZHUDONG/CTGJ defender. */
    private fun accumulateChargeWhenHit(defender: BattleUnit, activeAttack: Boolean) {
        if (activeAttack && defender.skills[26]?.and(255)?.let { it != 255 } == true) {
            incSkillTemp(defender.id, 26)
        }
    }

    /** BattleUnit.count_attackHarm's MRSP: a five-step max-HP damage roll. */
    private fun mrspDamage(attacker: BattleUnit, target: BattleUnit): Int? {
        if (attacker.skills[156]?.and(255)?.let { it != 255 } != true) return null
        return target.maxHitPoints * BattleMrspDamage.percent(probabilityResolver.random100()) / 100
    }

    private fun physicalDamageRateContext(attacker: BattleUnit, target: BattleUnit): PhysicalDamageRateContext {
        val targetIsPlayerSide = target.isPlayerSide()
        val targetHasNearbyAlly = infantryOffsets.any { (dx, dy) ->
            unitAt(target.tileX + dx, target.tileY + dy)?.let { it.isPlayerSide() == targetIsPlayerSide } == true
        }
        val hasBackPosition = backPosition(target, attacker) != null
        val skill292RandomBonus = attacker.skills[292]?.and(255)?.takeIf { it != 255 }
            ?.let { probabilityResolver.flagRandom(0, 5) }
        return PhysicalDamageRateContext(
            targetHasNearbyAlly = targetHasNearbyAlly,
            targetFinalMovement = BattleAttributeCalculator.finalMovement(target, weather),
            hasSplashTarget = hasPhysicalEffectTargets(attacker, target),
            hasBackPosition = hasBackPosition,
            incomingDirection = facingDirection(attacker.tileX, attacker.tileY, target.tileX, target.tileY),
            skill292RandomBonus = skill292RandomBonus,
        )
    }

    private fun physicalCriticalRateContext(
        attacker: BattleUnit,
        target: BattleUnit,
        critical: Boolean,
        counter: Boolean = false,
        continuous: Boolean = false,
        splash: Boolean = false,
    ): PhysicalCriticalRateContext {
        var counterSkill46Bonus = 0
        if (counter) {
            attacker.skills[46]?.and(255)?.takeIf { it != 255 }?.let { bonus ->
                if (skillTemp(attacker.id, 46) != 0) {
                    setSkillTemp(attacker.id, 46, 0)
                    counterSkill46Bonus = bonus
                }
            }
        }
        return PhysicalCriticalRateContext(
            critical = critical,
            counter = counter,
            continuous = continuous,
            splash = splash,
            incomingDirection = facingDirection(attacker.tileX, attacker.tileY, target.tileX, target.tileY),
            counterSkill46Bonus = counterSkill46Bonus,
        )
    }

    /**
     * BattleScreen.showUseProperty + _usePro2 for the portable combat
     * consumables.  The original permits selecting an allied target in the
     * infantry hit area; this tactical context uses the same adjacent area.
     */
    fun useProperty(userId: String, targetId: String, itemId: Int): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val user = units[userId] ?: return TacticalActionResult.Rejected("사용 유닛이 없습니다.")
        val target = units[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        val item = propertyItems[itemId] ?: return TacticalActionResult.Rejected("사용할 수 없는 아이템입니다.")
        if (user.effectiveFaction() != activeFaction || user.hasActed) return TacticalActionResult.Rejected("현재 행동할 수 없는 유닛입니다.")
        if (!areAllied(user, target)) return TacticalActionResult.Rejected("아군에게만 사용할 수 있습니다.")
        val offset = target.tileX - user.tileX to target.tileY - user.tileY
        if (target != user && offset !in movementOffsets) return TacticalActionResult.Rejected("아이템 사용 범위를 벗어났습니다.")
        val applied = applyProperty(item, target) { consumeSelectedProperty(itemId) }
            ?: return TacticalActionResult.Rejected("아이템을 사용할 수 없습니다.")
        user.markActionComplete()
        return applied
    }

    /**
     * BattleScreen._usePro2's state mutation, shared by the player-selected
     * path and `_attack3` ZDSY.  The caller owns inventory mutation because
     * ZDSY uses ItemStore.pushProperty directly before entering _usePro2.
     */
    private fun applyProperty(
        item: BattlePropertyItem,
        target: BattleUnit,
        consume: () -> Boolean,
    ): TacticalActionResult.Item? {
        val effect = when (item.itemType) {
            26 -> {
                if (target.hitPoints >= target.maxHitPoints || !consume()) return null
                val amount = if (item.value == 255) target.maxHitPoints else item.value
                val recovered = minOf(amount, target.maxHitPoints - target.hitPoints)
                target.addHpcur(recovered)
                "HP ${recovered} 회복"
            }
            27 -> {
                if (target.magicPoints >= target.maxMagicPoints || !consume()) return null
                val amount = if (item.value == 255) target.maxMagicPoints else item.value
                val recovered = minOf(amount, target.maxMagicPoints - target.magicPoints)
                target.addMpcur(recovered)
                "MP ${recovered} 회복"
            }
            28, 29, 30, 31 -> {
                val status = listOf(BattleStatus.CONFUSION, BattleStatus.POISON, BattleStatus.PARALYSIS, BattleStatus.SILENCE)[item.itemType - 28]
                if (status !in target.statuses || !consume()) return null
                target.statuses.remove(status)
                target.presentation.refreshStatus(target.statuses, target.attributeLifts)
                "${status.label()} 치료"
            }
            32 -> {
                if (target.statuses.isEmpty() || !consume()) return null
                target.statuses.clear()
                target.presentation.refreshStatus(target.statuses, target.attributeLifts)
                "모든 이상 상태 치료"
            }
            33, 34, 35, 36, 37 -> {
                // _usePro2: WL, ZL, TS, MJ, YQ -> ATT, SPR, DEF, CRI, MOR.
                val attribute = listOf(BattleAttribute.ATTACK, BattleAttribute.SPIRIT, BattleAttribute.DEFENSE, BattleAttribute.CRITICAL, BattleAttribute.MORALE)[item.itemType - 33]
                if (!consume()) return null
                target.applyAttributeLift(attribute, 1, 3)
                "${attribute.label()} 상승"
            }
            42 -> {
                if (!consume()) return null
                target.maxHitPoints += item.value
                target.addHpcur(item.value)
                notifyPermanentProperty(item, target)
                "최대 HP ${item.value} 증가"
            }
            43 -> {
                if (!consume()) return null
                target.maxMagicPoints += item.value
                target.addMpcur(item.value)
                notifyPermanentProperty(item, target)
                "최대 MP ${item.value} 증가"
            }
            else -> return null
        }
        return TacticalActionResult.Item(item.name, target.id, effect)
    }

    /** BattleScreen.attackAction: scripted/cinematic attack outside normal turn input. */
    fun forcedAttack(attackerId: String, targetId: String): TacticalActionResult {
        val attacker = units[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val target = units[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        if (!attacker.visible || !target.visible || areAllied(attacker, target)) return TacticalActionResult.Rejected("강제 공격 대상을 찾을 수 없습니다.")
        val hitRate = probabilityResolver.physicalHitRate(attacker, target)
        val criticalRoll = probabilityResolver.criticalHit(attacker, target)
        val hit = probabilityResolver.physicalHit(attacker, target, hitRate)
        val base = PhysicalDamageCalculator.basePhysicalDamage(
            attacker,
            target,
            basePhysicalDamageContext(attacker, target, splash = false),
        )
        val critical = hit && criticalRoll &&
            !(target.skills[49]?.and(255)?.let { it != 255 } == true && attacker.skills[227]?.and(255)?.let { it != 255 } != true)
        val specialDamage = if (hit) mrspDamage(attacker, target) else null
        val damage = if (hit) {
            specialDamage ?: run {
                var raw = maxOf(1, base * PhysicalDamageCalculator.physicalArmRestraint(attacker, target) / 100)
                raw = raw * PhysicalDamageCalculator.physicalDamageRate(
                    attacker,
                    target,
                    physicalDamageRateContext(attacker, target),
                ) / 100
                raw = BattleAttributeCalculator.physicalDamageAfterResistance(raw, attacker, target)
                raw += PhysicalDamageCalculator.physicalFlatSkillDamage(
                    attacker,
                    target,
                    flatPhysicalDamageContext(attacker, activeAttack = false),
                )
                raw = maxOf(1, raw)
                raw = PhysicalDamageCalculator.armorPiercingMinimumDamage(attacker, target, raw)
                maxOf(
                    PhysicalDamageCalculator.physicalMinimumDamage(attacker, visibleFamousPlayerCount()),
                    PhysicalDamageCalculator.cappedPhysicalDamage(target, raw) *
                        PhysicalDamageCalculator.physicalCriticalRate(
                            attacker,
                            target,
                            physicalCriticalRateContext(attacker, target, critical),
                        ) / 100,
                )
            }
        } else 0
        if (hit && specialDamage == null) consumeMpAttackSkill(attacker)
        target.addHpcur(-damage)
        val lifeStealHealing = attacker.skills[238]?.and(255)?.takeIf { it != 255 && damage > 0 }
            ?.let { minOf(attacker.maxHitPoints - attacker.hitPoints, it * damage / 100) } ?: 0
        attacker.addHpcur(lifeStealHealing)
        val defeated = target.hitPoints <= 0
        if (hit) notifyPhysicalDamage(attacker, target, damage)
        if (defeated) {
            notifyUnitDefeated(attacker, target)
            battlefield.defeat(target.id)
        }
        return TacticalActionResult.Attack(damage, defeated, hitRate, hit, critical, lifeStealHealing = lifeStealHealing)
    }

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
    ): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val attacker = units[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val target = units[targetId] ?: return TacticalActionResult.Rejected("대상 유닛이 없습니다.")
        val magic = attacker.magic.firstOrNull { it.id == magicId } ?: return TacticalActionResult.Rejected("사용할 수 없는 전략입니다.")
        if (!attacker.visible || !target.visible || (!reaction && (attacker.effectiveFaction() != activeFaction || attacker.hasActed))) return TacticalActionResult.Rejected("현재 유닛은 전략을 사용할 수 없습니다.")
        if (BattleStatus.PARALYSIS in attacker.statuses || BattleStatus.CONFUSION in attacker.statuses || BattleStatus.SILENCE in attacker.statuses) return TacticalActionResult.Rejected("현재 상태에서는 전략을 사용할 수 없습니다.")
        if (magic.target == 2) {
            if (attacker.magicPoints < magic.expendMp) return TacticalActionResult.Rejected("MP가 부족합니다.")
            attacker.addMpcur(-magic.expendMp)
            if (!reaction) attacker.markActionComplete()
            weather = when (magic.id) {
                58 -> BattleWeather.HEAVY_RAIN // HAOYU
                59 -> BattleWeather.CLEAR // QINGMING
                60 -> BattleWeather.CLOUDY // YINGTIAN
                else -> weather
            }
            return TacticalActionResult.Magic(magic.name, magic.expendMp, emptyList())
        }
        val targetsAllies = magic.target == 1
        val targetsAny = magic.target == 3
        if (!targetsAny && ((targetsAllies && !areAllied(attacker, target)) || (!targetsAllies && areAllied(attacker, target)))) {
            return TacticalActionResult.Rejected(if (targetsAllies) "아군만 대상으로 할 수 있는 전략입니다." else "적군만 대상으로 할 수 있는 전략입니다.")
        }
        val offset = target.tileX - attacker.tileX to target.tileY - attacker.tileY
        if (magic.category !in setOf(1, 29) && !magic.hitArea.allScreen && offset !in magic.hitArea.offsets) return TacticalActionResult.Rejected("전략 범위를 벗어났습니다.")
        if (!magicTerrainAllowed(magic, target)) return TacticalActionResult.Rejected("이 지형에서는 사용할 수 없는 전략입니다.")
        if (!bypassCondition) magicConditionReason(attacker, magic)?.let { return TacticalActionResult.Rejected(it) }
        if (attacker.magicPoints < magic.expendMp) return TacticalActionResult.Rejected("MP가 부족합니다.")
        attacker.addMpcur(-magic.expendMp)
        if (!reaction) attacker.markActionComplete()
        // BattleScreen._magic performs one morale critical check against the
        // selected primary target before processing any effect targets.  The
        // resulting flag is shared by both CLLJ passes and advances BJL/BBJL
        // even when the critical check fails.
        val magicCritical = magic.harmType != 4 && if (attacker.skills[269]?.and(255)?.let { it != 255 } == true) {
            true // ZMYJCL bypasses countRate entirely.
        } else {
            probabilityResolver.criticalHit(attacker, target)
        }
        val offsets = magic.effectOffsets + (0 to 0)
        // Keep object references even when a lethal magic result moves the
        // victim out of the active unit map before global settlement.
        val experienceTargets = (units.values + battlefield.pendingPresentationUnits()).associateBy { it.id }
        data class MagicEquipmentExperienceRecord(
            val recipient: BattleUnit,
            val opponent: BattleUnit,
            val kind: BattleEquipmentExperienceKind,
            val amount: Int,
        )
        val magicEquipmentByRecipient = linkedMapOf<Pair<String, BattleEquipmentExperienceKind>, MagicEquipmentExperienceRecord>()
        fun recordMagicEquipment(recipient: BattleUnit, opponent: BattleUnit, resolvedHarm: Int, kind: BattleEquipmentExperienceKind) {
            val amount = equipmentExperienceAmount(recipient, opponent, resolvedHarm, kind)
            val key = recipient.id to kind
            if (amount > (magicEquipmentByRecipient[key]?.amount ?: 0)) {
                magicEquipmentByRecipient[key] = MagicEquipmentExperienceRecord(recipient, opponent, kind, amount)
            }
        }
        val effectCandidates = units.values.filter { unit ->
            unit.visible && magicTerrainAllowed(magic, unit) &&
                (targetsAny || areAllied(unit, attacker) == targetsAllies) &&
                (unit.tileX - target.tileX to (unit.tileY - target.tileY)) in offsets
        }.toList()
        // filterMagicHitareaUnit promotes SB/BH to the original all-screen
        // target area.  QL (청룡) then chooses five targets from its effect
        // area with replacement, exactly as BattleScreen._magicAttack does.
        // `_magic` invokes `_magicProcess` twice for CLLJ.  Keep passes
        // separate: each gets its own playMeff group and only pass two has
        // ATTACK_FLAG.LIANJI's 90% count_magicHarm modifier.
        val repeatCount = if (attacker.skills[16]?.and(255)?.let { it != 255 } == true) 2 else 1
        val criticalSpeeches = mutableListOf<String?>()
        val localSettlements = mutableListOf<MagicLocalSettlement>()
        val resultPasses = buildList {
            repeat(repeatCount) { pass ->
                // Source runs checkCrit/getCritTxt before the preparation
                // action and before _magicAttack selects random QL targets.
                criticalSpeeches += resolveCriticalSpeech(attacker, magicCritical)
                val affectedUnits = when (magic.category) {
                    1, 29 -> units.values.filter { unit ->
                        unit.visible && (targetsAny || areAllied(unit, attacker) == targetsAllies)
                    }.toList()
                    26 -> if (effectCandidates.isEmpty()) emptyList() else List(5) {
                        effectCandidates[probabilityResolver.defaultRandom(0, effectCandidates.lastIndex)]
                    }
                    else -> effectCandidates
                }
                val localEntries = mutableListOf<MagicLocalSettlementEntry>()
                add(affectedUnits.map { victim ->
                val statusesBefore = victim.statuses.toMap()
                val liftsBefore = victim.attributeLifts.toMap()
                val liftRoundsBefore = victim.attributeLiftRounds.toMap()
            fun local(result: MagicTarget): MagicTarget {
                // Source only calls setCharInfoBykey(h, ..., STATES, P) on
                // the non-miss branch.  P may nevertheless be empty.
                if (result.hit) localEntries += MagicLocalSettlementEntry(
                    victim.id,
                    statusesBefore,
                    victim.statuses.toMap(),
                    liftsBefore,
                    victim.attributeLifts.toMap(),
                    hasStatesPayload = true,
                    attributeLiftRoundsBefore = liftRoundsBefore,
                    attributeLiftRoundsAfter = victim.attributeLiftRounds.toMap(),
                )
                return result
            }
            fun magicHarm(value: Int): Int {
                var result = if (pass > 0) kotlin.math.floor(value * .9).toInt() else value
                if (magicCritical) result += kotlin.math.floor(result * .5).toInt()
                return result
            }
            // BattleScreen._magicProcess handles these exceptional strategy
            // types before the generic damage/status calculation.
            if (magic.type == 22) { // HUIGUI: restore an already-acted unit
                victim.hasActed = false
                attacker.ai = 0
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = true, defeated = false))
            }
            if (magic.type == 25 && magic.category == 29) { // SISHEN / BH
                val healing = victim.maxHitPoints - victim.hitPoints
                victim.setCurHp(victim.maxHitPoints)
                victim.statuses.clear()
                victim.presentation.refreshStatus(victim.statuses, victim.attributeLifts)
                return@map local(MagicTarget(victim.id, damage = 0, healing = healing, hitRate = 100, hit = true, defeated = false))
            }
            if (magic.type == 26 || magic.type == 28) { // BAQI / SHUAIQI
                val lift = if (magic.type == 26) 1 else -1
                val attributes = listOf(BattleAttribute.ATTACK, BattleAttribute.DEFENSE, BattleAttribute.SPIRIT, BattleAttribute.CRITICAL, BattleAttribute.MORALE)
                    .associateWith { attribute -> victim.applyAttributeLift(attribute, lift, 3) }
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = true, defeated = false, attributes = attributes))
            }
            if (magic.type == 27) { // QIANGXING
                val applied = victim.applyAttributeLift(BattleAttribute.MOVEMENT, 1, 3)
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = true, defeated = false, attribute = BattleAttribute.MOVEMENT, lift = applied))
            }
            if (magic.type == 6) { // XISHOU_MP
                val hitRate = probabilityResolver.magicHitRate(attacker, victim, magic)
                val hit = probabilityResolver.magicHit(attacker, victim, hitRate)
                val base = maxOf(1, (BattleAttributeCalculator.effective(attacker, BattleAttribute.SPIRIT) - BattleAttributeCalculator.effective(victim, BattleAttribute.SPIRIT)) / 3 + 25 + attacker.level)
                val drained = if (hit) minOf(victim.magicPoints, maxOf(1, magicHarm(base * magic.power / 100))) else 0
                victim.addMpcur(-drained)
                val recovered = minOf(attacker.maxMagicPoints - attacker.magicPoints, drained)
                attacker.addMpcur(recovered)
                return@map local(MagicTarget(victim.id, damage = 0, magicRecovery = recovered, magicDrain = drained, hitRate = hitRate, hit = hit, defeated = false))
            }
            val status = magic.statusEffect()
            var appliedStatus: BattleStatus? = null
            if (status != null) {
                val hitRate = probabilityResolver.magicHitRate(attacker, victim, magic)
                val hit = probabilityResolver.magicHit(attacker, victim, hitRate)
                if (hit) {
                    victim.statuses[status] = statusDuration(status, victim)
                    victim.presentation.refreshStatus(victim.statuses, victim.attributeLifts)
                    appliedStatus = status
                }
                // Source _magicProcess does not stop after applying an
                // abnormal state.  A spell with harmType != NO performs a
                // second, independent accumulated hit check and then deals
                // damage as well (for example magic 33, 독연).
                if (magic.harmType == 4) {
                    return@map local(MagicTarget(victim.id, damage = 0, status = appliedStatus, hitRate = hitRate, hit = hit, defeated = false))
                }
            }
            val attributeChange = magic.attributeChange()
            if (magic.type == 21) { // JUEXING: remove only abnormal states, not stat lifts.
                val hadStatus = victim.statuses.isNotEmpty()
                victim.statuses.clear()
                victim.presentation.refreshStatus(victim.statuses, victim.attributeLifts)
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = hadStatus, defeated = false))
            }
            if (magic.type == 7 || magic.type == 11) { // NLXJ / TSNL: martial ATT, civil SPR, all-rounder both.
                val lift = if (magic.type == 7) -1 else 1
                val attributes = when (victim.armType) {
                    1 -> mapOf(BattleAttribute.SPIRIT to lift)
                    2 -> mapOf(BattleAttribute.ATTACK to lift)
                    else -> mapOf(BattleAttribute.ATTACK to lift, BattleAttribute.SPIRIT to lift)
                }.mapValues { (attribute, value) -> victim.applyAttributeLift(attribute, value, 3) }
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = true, defeated = false, attributes = attributes))
            }
            if (attributeChange != null) {
                val (attribute, lift) = attributeChange
                val hitRate = probabilityResolver.magicHitRate(attacker, victim, magic)
                val hit = probabilityResolver.magicHit(attacker, victim, hitRate)
                var appliedLift = 0
                if (hit) {
                    appliedLift = victim.applyAttributeLift(attribute, lift, 3)
                }
                return@map local(MagicTarget(
                    targetId = victim.id, damage = 0, hitRate = hitRate, hit = hit, defeated = false,
                    attribute = attribute.takeIf { hit }, lift = appliedLift,
                ))
            }
            if (magic.type == 19) {
                // BattleUnit.count_magicHarm(JHP): percentage of caster HP plus
                // strategist bonus (the original special-cases ids 39/41).
                // The percentage is the caster's current HP, not the target's.
                val base = attacker.hitPoints * magic.power / 100 + if (magic.id == 39 || magic.id == 41) attacker.spirit / 10 else attacker.spirit / 2
                val healingRate = healingTerrainRate(attacker, magic)
                val healing = minOf(victim.maxHitPoints - victim.hitPoints, maxOf(0, magicHarm(base * healingRate / 100)))
                victim.addHpcur(healing)
                return@map local(MagicTarget(victim.id, damage = 0, healing = healing, hitRate = 100, hit = true, defeated = false))
            }
            if (magic.type == 20 && magic.category == 24) { // MX: target HP → caster MP
                val transferred = minOf(40, maxOf(0, victim.hitPoints - 1))
                if (transferred > 0 && attacker.magicPoints < attacker.maxMagicPoints) {
                    victim.addHpcur(-transferred, keepAlive = true)
                    val recovered = minOf(attacker.maxMagicPoints - attacker.magicPoints, transferred * 5 / 8)
                    attacker.addMpcur(recovered)
                    return@map local(MagicTarget(victim.id, damage = transferred, magicRecovery = recovered, hitRate = 100, hit = true, defeated = false))
                }
                return@map local(MagicTarget(victim.id, damage = 0, hitRate = 100, hit = false, defeated = false))
            }
            if (magic.type == 20) {
                // BattleUnit.count_magicHarm(JMP) returns the original spell's MP value.
                val healing = minOf(victim.maxMagicPoints - victim.magicPoints, magicHarm(magic.expendMp))
                victim.addMpcur(healing)
                return@map local(MagicTarget(victim.id, damage = 0, magicRecovery = healing, hitRate = 100, hit = true, defeated = false))
            }
            val hitRate = probabilityResolver.magicHitRate(attacker, victim, magic)
            val hit = probabilityResolver.magicHit(attacker, victim, hitRate)
            val assassination = magic.type == 4 && magic.category == 2
            val base = if (assassination) {
                // AN_SHA under the YH category uses a percentage of target HP.
                victim.maxHitPoints * magic.power / 100
            } else {
                maxOf(1, (BattleAttributeCalculator.effective(attacker, BattleAttribute.SPIRIT) - BattleAttributeCalculator.effective(victim, BattleAttribute.SPIRIT)) / 3 + 25 + attacker.level)
            }
            val damage = if (hit) {
                if (assassination) maxOf(1, magicHarm(base))
                else {
                    var value = maxOf(1, base * magic.power / 100 * victim.magicHarmRate / 100)
                    value += magicFlatSkillDamage(attacker, magic)
                    value = maxOf(1, value * magicSkillDamageRate(attacker, victim, magic) / 100)
                    value = value * magicWeatherRate(magic) / 100
            value = value * offensiveMagicTerrainRate(victim, magic) / 100
                    // BattleUnit.isMine() is true for both MINE and FRIEND;
                    // count_magicHarm's minimum applies to ENEMY only.
                    val enemyMinimum = if (!attacker.isPlayerSide()) {
                        maxOf(1, (minOf(7, units.values.count { it.visible && it.isPlayerSide() }) * attacker.maxMagicPoints) / 100)
                    } else 1
                    magicHarm(maxOf(enemyMinimum, value))
                }
            } else 0
            victim.addHpcur(-damage)
            val casterHealing = if (magic.type == 5 && damage > 0) {
                minOf(attacker.maxHitPoints - attacker.hitPoints, damage).also { attacker.addHpcur(it) }
            } else 0
            val defeated = victim.hitPoints <= 0
            if (defeated) { battlefield.defeat(victim.id); notifyUnitDefeated(attacker, victim) }
            local(MagicTarget(
                targetId = victim.id,
                damage = damage,
                healing = 0,
                hitRate = hitRate,
                hit = hit,
                defeated = defeated,
                casterHealing = casterHealing,
                status = appliedStatus,
            ))
                })
                localSettlements += MagicLocalSettlement(localEntries)
            }
        }
        val results = resultPasses.flatten()
        // `_magicProcess` writes EXP_ADD for every resolved target after its
        // harm/no-harm branch.  Support/status magic is therefore not exempt.
        // The write occurs before `_jiesuan`, so a magic kill still uses the
        // ordinary (non-defeat) reward and repeated targets max-merge.
        val reward = results.mapNotNull { result ->
            experienceTargets[result.targetId]?.let { victim ->
                battleExperience(attacker, victim, defeated = false)
            }
        }.maxOrNull()
        if (reward != null) notifyBattleExperience(attacker, reward)
        results.forEach { result ->
            val victim = experienceTargets[result.targetId] ?: return@forEach
            // `_magicProcess`'s U is the MP drain for XISHOU_MP and harm
            // otherwise. HJ_EXP_ADD is inside the harmType != NO branch,
            // while the caster's WQ_EXP_ADD is deliberately outside it.
            val resolvedHarm = result.magicDrain.takeIf { it > 0 } ?: result.damage
            if (magic.harmType != 4) {
                recordMagicEquipment(victim, attacker, resolvedHarm, BattleEquipmentExperienceKind.ARMOR)
            }
            if (attacker.armType != 2) {
                recordMagicEquipment(attacker, victim, resolvedHarm, BattleEquipmentExperienceKind.WEAPON)
            }
        }
        magicEquipmentByRecipient.values.forEach { record ->
            notifyEquipmentExperienceAward(record.recipient, record.opponent, record.amount, record.kind)
        }
        return TacticalActionResult.Magic(
            magic.name, magic.expendMp, results, resultPasses,
            critical = magicCritical, criticalSpeeches = criticalSpeeches,
            localSettlements = localSettlements,
        )
    }

    /** Coordinate-target special magic.  SHUN_YI moves its caster to a vacant tile. */
    fun castMagicAt(attackerId: String, targetX: Int, targetY: Int, magicId: Int): TacticalActionResult {
        if (outcome() != null) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val attacker = units[attackerId] ?: return TacticalActionResult.Rejected("공격 유닛이 없습니다.")
        val magic = attacker.magic.firstOrNull { it.id == magicId } ?: return TacticalActionResult.Rejected("사용할 수 없는 전략입니다.")
        if (magic.type != 37) return TacticalActionResult.Rejected("좌표를 대상으로 할 수 없는 전략입니다.")
        if (!attacker.visible || attacker.effectiveFaction() != activeFaction || attacker.hasActed) return TacticalActionResult.Rejected("현재 유닛은 전략을 사용할 수 없습니다.")
        if (attacker.magicPoints < magic.expendMp) return TacticalActionResult.Rejected("MP가 부족합니다.")
        if (unitAt(targetX, targetY) != null || targetX < 0 || targetY < 0 || terrain?.let { targetX >= it.width || targetY >= it.height } == true) return TacticalActionResult.Rejected("이동할 수 없는 칸입니다.")
        val offset = targetX - attacker.tileX to targetY - attacker.tileY
        if (!magic.hitArea.allScreen && offset !in magic.hitArea.offsets) return TacticalActionResult.Rejected("전략 범위를 벗어났습니다.")
        attacker.addMpcur(-magic.expendMp)
        attacker.tileX = targetX
        attacker.tileY = targetY
        attacker.markActionComplete()
        return TacticalActionResult.Magic(magic.name, magic.expendMp, emptyList())
    }

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
    private fun aiSortValue(unit: BattleUnit): Double {
        val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
        val resumeHp = terrainResumeRates[terrain?.terrainAt(unit.tileX, unit.tileY)] ?: 0
        var value = when {
            resumeHp > 0 && !wounded -> 110.0
            wounded -> 30.0
            else -> 0.0
        }
        if (BattleStatus.CONFUSION in unit.statuses) value -= 20.0
        if (BattleStatus.PARALYSIS in unit.statuses) value -= 10.0
        value += when (unit.armType) {
            2 -> (if (unit.remoteAttack) 25 else 10) + 100.0 * unit.hitPoints / unit.maxHitPoints.coerceAtLeast(1)
            0 -> 20 + 100.0 * unit.hitPoints / unit.maxHitPoints.coerceAtLeast(1)
            else -> 30 + 100.0 * (unit.maxHitPoints - unit.hitPoints) / unit.maxHitPoints.coerceAtLeast(1)
        }
        return value + 15 - BattleAttributeCalculator.effectiveMovement(unit)
    }

    /**
     * Cocos BattleConfg.AI 0..9 dispatch, without presentation delays.  The
     * scripted target id/coordinates are retained from BattleUnit.setAI.
     */
    @JvmOverloads
    fun resolveAiTurn(maxUnits: Int = Int.MAX_VALUE, deferMutations: Boolean = false): AiTurnResult {
        require(maxUnits > 0)
        if (outcome() != null) return AiTurnResult(0, 0, 0)
        check(!deferMutations || maxUnits == 1) { "deferred AI playback resolves exactly one _ai2 actor" }
        check(!deferMutations || pendingActionTransaction == null) { "previous deferred AI actor has not completed" }
        val beforeResolution = if (deferMutations) runtimeSnapshot() else null
        if (deferMutations) {
            stagedHitSideEffects = mutableListOf()
            stagedCompletionSideEffects = mutableListOf()
        }
        lastAiUnitResolution = null
        var moves = 0
        var attacks = 0
        var holds = 0
        var resolvedUnits = 0
        var currentActor: BattleUnit? = null
        var currentFromX = 0
        var currentFromY = 0
        var currentHealthBefore: Map<String, Int> = emptyMap()
        var currentMoveArea: List<Pair<Int, Int>> = emptyList()
        fun record(unit: BattleUnit, targetId: String? = null, magicId: Int? = null, result: TacticalActionResult? = null) {
            val actionArea = when (result) {
                is TacticalActionResult.Attack -> if (unit.attackAllScreen) {
                    terrain?.let { grid -> (0 until grid.width).flatMap { x -> (0 until grid.height).map { y -> x to y } } }.orEmpty()
                } else (unit.attackOffsets + unit.attackEffectOffsets).map { (dx, dy) -> unit.tileX + dx to unit.tileY + dy }
                is TacticalActionResult.Magic -> unit.magic.firstOrNull { it.id == magicId }
                    ?.hitArea?.offsets?.map { (dx, dy) -> unit.tileX + dx to unit.tileY + dy }.orEmpty()
                else -> emptyList()
            }
            lastAiUnitResolution = AiUnitResolution(
                actorId = unit.id,
                fromX = currentFromX,
                fromY = currentFromY,
                toX = unit.tileX,
                toY = unit.tileY,
                path = lastMovePath(unit.id).takeIf { unit.tileX != currentFromX || unit.tileY != currentFromY }.orEmpty(),
                targetId = targetId,
                magicId = magicId,
                result = result,
                healthBeforeAction = currentHealthBefore,
                moveArea = currentMoveArea,
                actionArea = actionArea,
            )
            resolvedUnits++
        }
        fun hold(unit: BattleUnit) {
            // BattleScreen._ai2 puts UNIT_STATUS2.XD in g_charinfo before
            // ControlManager selects a point.  A controller that only moves
            // or finds no action therefore still consumes this unit's turn.
            unit.markActionComplete()
            holds++
            check(currentActor === unit)
            record(unit)
        }
        // The first sort is already in flight when state settlement begins.
        // Every later `_ai2` iteration recomputes s_AISortUnit from live HP,
        // terrain and status values after the previous actor finishes.
        var firstPlannedId = aiTurnOrder?.firstOrNull()
        aiTurnOrder = null
        var tracedAiSort = false
        while (resolvedUnits < maxUnits) {
            // `ctrl_mine` tests isEnd after every completed unit callback.
            // A batch resolver must not let the rest of the camp move after
            // the preceding actor has produced a terminal roster.
            if (outcome() != null) break
            val remaining = units.values.asSequence()
                .filter { it.visible && it.effectiveFaction() == activeFaction && !it.hasActed }
                .sortedWith(compareByDescending<BattleUnit>(::aiSortValue).thenBy { BattleAttributeCalculator.effective(it, BattleAttribute.DEFENSE) })
                .toList()
            if (!tracedAiSort && round == 2 && activeFaction == Faction.ENEMY) {
                traceActions += "sort-r2-enemy:" + remaining.joinToString(";") {
                    "${it.characterId}=v${aiSortValue(it)},hp${it.hitPoints}/${it.maxHitPoints},arm${it.armType},remote${it.remoteAttack},mov${BattleAttributeCalculator.effectiveMovement(it)},def${BattleAttributeCalculator.effective(it, BattleAttribute.DEFENSE)},terrain${terrain?.terrainAt(it.tileX, it.tileY)},resume${terrainResumeRates[terrain?.terrainAt(it.tileX, it.tileY)] ?: 0},status${it.statuses}"
                }
                tracedAiSort = true
            }
            val unit = firstPlannedId?.let(units::get)
                ?.takeIf { it.visible && it.effectiveFaction() == activeFaction && !it.hasActed }
                ?: remaining.firstOrNull()
                ?: break
            firstPlannedId = null
            currentActor = unit
            currentFromX = unit.tileX
            currentFromY = unit.tileY
            currentHealthBefore = units.mapValues { it.value.hitPoints }
            currentMoveArea = emptyList()
            // CtrlJSYD (AI 2) still constructs ControlManager and evaluates
            // attacks/magic from the current tile; it only suppresses movement.
            // BattleScreen._ai2 exits before `_process` for HUN_LUAN
            // (CONFUSION) only.  Paralysis deliberately enters Control,
            // whose _process1 then changes its temporary controller to
            // JIAN_SHOU_YUAN_DI.  Keeping those two source stages distinct
            // matters because _ai2 has already written XD at this point.
            if (BattleStatus.CONFUSION in unit.statuses) {
                hold(unit)
                continue
            }
            // CtrlGJWJ/CtrlGSWJ resolve their retained target through
            // Battle.unit(index, 1), not through the enemy-only list.  A
            // missing target re-enters active AI; a close friendly follow
            // target re-enters passive AI.  The old context silently treated
            // both as an arbitrary enemy target.
            val retainedTarget = units.values.firstOrNull {
                it.visible && it.characterId == unit.aiTargetCharacterId
            }
            when (unit.ai) {
                3 -> when {
                    retainedTarget == null -> unit.ai = 1 // CtrlGJWJ
                    areAllied(unit, retainedTarget) && distance(unit, retainedTarget) < 3 -> unit.ai = 0
                    !areAllied(unit, retainedTarget) && !hasAttackCandidate(unit, retainedTarget) -> {
                        // CtrlGJWJ._ganlu(2) enters CtrlYDDZDDGJ, whose
                        // _psAryIter yields only the selected route point.
                        unit.ai = 9
                        unit.aiTargetX = retainedTarget.tileX
                        unit.aiTargetY = retainedTarget.tileY
                    }
                }
                5 -> when {
                    retainedTarget == null -> unit.ai = 1 // CtrlGSWJ
                    distance(unit, retainedTarget) < 3 -> unit.ai = 0
                    else -> {
                        // CtrlGSWJ._ganlu(0) enters CtrlYDDZDDJS.
                        unit.ai = 7
                        unit.aiTargetX = retainedTarget.tileX
                        unit.aiTargetY = retainedTarget.tileY
                    }
                }
            }
            val opponents = units.values.filter { it.visible && !areAllied(it, unit) }
            val targetById = retainedTarget?.takeIf { !areAllied(it, unit) }
            val nearestOpponent = opponents.minByOrNull { distance(unit, it) }
            // BattleScreen._ai scores every point in _process(...).psAry,
            // including the current tile, then keeps the best action nested
            // under that point.  This replaces the former nearest-enemy
            // shortcut so terrain, designated targets and reachable attacks
            // participate in the same decision.
            // Control.selectMovePoint is the source entry point.  Its
            // _process1 short-circuits paralysis/complete surrounding before
            // Control._AIProcess scores a single reachable point.
            var selectedByControl: AiDecision? = null
            lateinit var controlManager: ControlManager
            controlManager = ControlManager(
                state = object : ControlManager.UnitState {
                    override fun isControlled() = false
                    override fun ai() = unit.ai
                    override fun targetIndex() = unit.aiTargetCharacterId
                    override fun targetX() = unit.aiTargetX
                    override fun targetY() = unit.aiTargetY
                    // ControlManager.js checks battle.unit(targetId) only;
                    // CtrlGSWJ deliberately follows same-camp targets too.
                    override fun targetExists(index: Int) = units.values.any { it.visible && it.characterId == index }
                },
                factory = object : ControlManager.Factory {
                    override fun create(ai: Int): ControlManager.Driver = object : ControlManager.Driver {
                        // `ai` is the manager's current (possibly temporary)
                        // controller.  It is intentionally distinct from
                        // unit.ai, which ControlManager.js does not rewrite.
                        private val controllerAi = ai
                        private val controller = ControlControllerFactory.create(ai)
                        private var data = ControlData()
                        override fun setManager(manager: ControlManager) = Unit
                        override fun setWithData(targetIndex: Int, x: Int, y: Int) {
                            data = ControlData(targetIndex, Control.Point(x, y))
                        }
                        override fun selectMovePoint(points: List<Control.Point>, pointHash: Set<Control.Point>): Int {
                            // Control's _AStar/_zdmdd/findEmptyPos consume the
                            // psHash captured for this exact _process call.
                            // Rebuilding it can observe a later unit state and
                            // make a just-revealed AI6 actor fall through to
                            // an otherwise spurious hold.
                            val capturedMovePoints = pointHash.mapTo(linkedSetOf()) { it.x to it.y }
                            val context = object : BattleControlContext {
                                override fun currentPoint() = Control.Point(unit.tileX, unit.tileY)
                                override fun isParalyzed() = BattleStatus.PARALYSIS in unit.statuses
                                override fun isSurrounded() = movementOffsets.all { (dx, dy) -> unitAt(unit.tileX + dx, unit.tileY + dy) != null }
                                override fun isMine() = unit.isPlayerSide()
                                override fun setPersistentAi(ai: Int) { unit.ai = ai }
                                override fun target(index: Int) = units.values.firstOrNull { it.visible && it.characterId == index }?.let {
                                    ControlTarget(index, Control.Point(it.tileX, it.tileY), it.isPlayerSide(), distance(unit, it))
                                }
                                override fun hasAttackTargets(targetIndex: Int?): Boolean {
                                    val candidates = if (targetIndex == null) opponents else opponents.filter { it.characterId == targetIndex }
                                    return linkedSetOf(unit.tileX to unit.tileY).apply { addAll(reachableTiles(unit.id).keys) }
                                        .any { (x, y) -> candidates.any { canAttackFrom(unit, x, y, it) } }
                                }
                                // Direct `_cxpl` adapter.  Like the source,
                                // this returns a temporary ControlManager
                                // transition; it never writes unit.ai.
                                override fun exhaustedRetreat(): ControlTransition? {
                                    val weakThreshold = unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
                                    if (unit.hitPoints >= weakThreshold) return null
                                    val resume = points.asSequence()
                                        .filter { point -> terrainResumeRates[terrain?.terrainAt(point.x, point.y)] ?: 0 > 0 }
                                        .filter { point -> unitAt(point.x, point.y)?.let { it.id == unit.id } != false }
                                        .maxByOrNull { point -> terrainResumeRates[terrain?.terrainAt(point.x, point.y)] ?: 0 }
                                    if (resume != null) return ControlTransition(ControlAi.MOVE_MAGIC, ControlData(-1, resume))
                                    val master = enemyMasterUnitId?.let(units::get)
                                        ?.takeIf { !unit.isPlayerSide() && it.visible && it.id != unit.id }
                                    val friend = units.values.asSequence()
                                        .filter { it.visible && it.id != unit.id && areAllied(it, unit) }
                                        .minByOrNull { distance(unit, it) }
                                    return (master ?: friend)?.let { target ->
                                        ControlTransition(ControlAi.RETREAT_TO, ControlData(-1, Control.Point(target.tileX, target.tileY)))
                                    }
                                }
                                override fun nearestOpponent() = opponents.mapNotNull { opponent ->
                                    findMovementPath(unit, opponent.tileX, opponent.tileY)?.let { path -> opponent to path.size }
                                }.minByOrNull { it.second }?.first?.let {
                                    ControlTarget(it.characterId ?: -1, Control.Point(it.tileX, it.tileY), it.isPlayerSide(), distance(unit, it))
                                }
                                override fun winRectCentre(): Control.Point? = null
                                // Control._zdmdd does not choose the reachable
                                // point nearest a remote target. It is entered
                                // only when the authored target itself exists in
                                // psHash; otherwise CtrlDZDD/CtrlTZZDD must fall
                                // through to _ganlu's AStar(flags=9) route. If
                                // the target is occupied, source probes
                                // MO_YU_JIAN3 in its authored order.
                                override fun destinationPoint(target: Control.Point): Control.Point? {
                                    val targetPoint = target.x to target.y
                                    if (targetPoint !in capturedMovePoints) return null
                                    return sequenceOf(targetPoint)
                                        .plus(directDestinationOffsets.asSequence().map { (dx, dy) ->
                                            target.x + dx to target.y + dy
                                        })
                                        .firstOrNull { point ->
                                            point in capturedMovePoints && unitAt(point.first, point.second) == null
                                        }
                                        ?.let { Control.Point(it.first, it.second) }
                                }
                                override fun nearPoint(target: Control.Point): Control.Point? {
                                    val route = findMovementPath(
                                        unit,
                                        target.x,
                                        target.y,
                                        avoidEnemies = true,
                                        allowEnemyOnTarget = true,
                                    ) ?: return null
                                    val lastReachableIndex = route.indexOfFirst { it !in capturedMovePoints }
                                        .let { if (it < 0) route.lastIndex else it - 1 }
                                    for (index in lastReachableIndex downTo 1) {
                                        findReachableEmptyPosition(unit, route[index], capturedMovePoints)?.let { point ->
                                            return Control.Point(point.first, point.second)
                                        }
                                    }
                                    return Control.Point(unit.tileX, unit.tileY)
                                }
                                override fun blockingEnemy(target: Control.Point): Int? {
                                    // Control._ganlu falls back from AStar(9)
                                    // to AStar(5). Bit 4 keeps opposing units
                                    // in the route with a +255 cost, then the
                                    // first such unit becomes CtrlGJWJ's
                                    // designated target.
                                    val route = findMovementPath(
                                        unit,
                                        target.x,
                                        target.y,
                                        avoidEnemies = true,
                                        penalizeEnemyTiles = true,
                                    ) ?: return null
                                    return route.asSequence()
                                        .mapNotNull { point -> unitAt(point.first, point.second) }
                                        .firstOrNull { occupant -> !areAllied(occupant, unit) }
                                        ?.characterId
                                }
                                override fun chooseAi(mode: Int): Control.Result? {
                                    val controllerPoints = when (controllerAi) {
                                        ControlAi.HOLD -> listOf(Control.Point(unit.tileX, unit.tileY))
                                        ControlAi.MOVE_ATTACK, ControlAi.MOVE_MAGIC, ControlAi.MOVE_ATTACK_UNIT -> listOf(data.target)
                                        else -> points
                                    }
                                    selectedByControl = chooseAiDecision(
                                        unit = unit,
                                        opponents = opponents,
                                        designated = targetById,
                                        aiMode = controllerAi,
                                        aiFlags = mode,
                                        forcedTarget = data.target,
                                        candidatePoints = controllerPoints.map { it.x to it.y },
                                    )
                                    return selectedByControl?.let { choice -> Control.Result(choice.x, choice.y, kind = if (choice.magicId == null) "attack" else "magic", value = choice.value) }
                                }
                            }
                            val step = controller.step(context, data)
                            step.transition?.let { transition ->
                                // ControlManager.setControl replaces only its
                                // live controller/data.  It must not rewrite
                                // the unit's persistent AI fields: the source
                                // writes those only through Ctrl*.setAI().
                                controlManager.setControl(transition.ai, transition.data.targetIndex, transition.data.target.x, transition.data.target.y)
                            }
                            step.result?.let(controlManager::setResult)
                            return step.status
                        }
                    }
                },
            )
            // `_process(unit)` passes canMovePoints' ordered psAry and psHash
            // to ControlManager.  Supplying an empty synthetic set here made
            // every controller score a reconstructed approximation instead.
            // BattleScreen.canMovePoints starts from BattleUnit.mov_final(),
            // including the active windy/heavy-rain penalty. Using the raw
            // lifted MOV here admitted an extra zero-remaining ring into AI
            // scoring (for example S_00 unit 258 could incorrectly use
            // (11,11) in round 3).
            val moveArea = movePoints(unit, BattleAttributeCalculator.finalMovement(unit, weather))
            val sourcePoints = moveArea.points.keys.map { (x, y) -> Control.Point(x, y) }
            currentMoveArea = sourcePoints.map { it.x to it.y }
            val sourceHash = sourcePoints.toCollection(linkedSetOf())
            val controlStatus = controlManager.selectMovePoint(sourcePoints, sourceHash)
            if (controlStatus != 0) {
                hold(unit)
                continue
            }
            val decision = selectedByControl
            if (decision == null) {
                hold(unit)
                continue
            }
            // Base Control._AIProcess4 is empty. Only CtrlZDCJ (AI 1) and
            // CtrlJSYD (AI 2) override it to persist `info.value`; passive
            // and temporary movement controllers must leave AIValue at the
            // camp-start value of zero. Use ControlManager's final controller
            // rather than unit.ai because a retry may have replaced it.
            if (controlManager.activeAi in setOf(ControlAi.ACTIVE, ControlAi.HOLD)) {
                unit.aiValue = decision.actionValue
            }
            val traceFrom = "${unit.tileX},${unit.tileY}"
            val diagnosticPoints = if (unit.characterId == 474 && round == 1) sourcePoints.joinToString(";") { "${it.x},${it.y}" } else ""
            traceActions += "r$round/${activeFaction.name}/${unit.characterId}:$traceFrom->${decision.x},${decision.y}:target=${decision.targetId?.let(units::get)?.characterId}:magic=${decision.magicId}:score=${decision.actionValue}:points=$diagnosticPoints"
            if (decision.x != unit.tileX || decision.y != unit.tileY) {
                if (moveUnit(unit.id, decision.x, decision.y) is TacticalActionResult.Success) moves++ else {
                    hold(unit)
                    continue
                }
            }
            // CtrlDZDD/CtrlTZZDD keep the authored persistent AI after a
            // movement reaches its destination.  The source writes passive
            // only when that controller is entered again on a later turn and
            // observes that the actor already starts on the target tile.
            val selected = decision.targetId?.let(units::get)
            if (selected != null && decision.magicId != null) {
                val profile = unit.magic.firstOrNull { it.id == decision.magicId }
                val bypassCondition = profile?.aiUse == 13
                val magicResult = castMagic(unit.id, selected.id, decision.magicId, bypassCondition = bypassCondition)
                if (unit.characterId == 146 && round == 2) {
                    val profileText = profile?.let { "id=${it.id},type=${it.type},target=${it.target},area=${it.effectAreaId},power=${it.power},harm=${it.harmType},category=${it.category},limit=${it.hitRateLimit}" }
                    traceActions += "diagMagic146:profile=$profileText:targetArm=${selected.armId},magicHarm=${selected.magicHarmRate}:result=$magicResult"
                }
                if (magicResult is TacticalActionResult.Magic) {
                    attacks++
                    record(unit, selected.id, decision.magicId, magicResult)
                } else hold(unit)
            } else if (selected != null && selected.visible && canAttack(unit, selected)) {
                val attackResult = attack(unit.id, selected.id)
                if ((unit.characterId in setOf(0, 32, 258, 259, 477, 479) && round == 3) ||
                    (unit.characterId == 3 && round == 4)
                ) traceActions += "diagAttack${unit.characterId}r$round:offsets=${unit.attackOffsets}:statuses=${unit.statuses}:result=$attackResult"
                if (attackResult is TacticalActionResult.Attack) {
                    attacks++
                    record(unit, selected.id, result = attackResult)
                } else hold(unit)
            } else hold(unit)
        }
        if (deferMutations && lastAiUnitResolution != null) {
            val afterResolution = runtimeSnapshot()
            val before = requireNotNull(beforeResolution)
            val hitSideEffects = stagedHitSideEffects.orEmpty().toList()
            val completionSideEffects = stagedCompletionSideEffects.orEmpty().toList()
            stagedHitSideEffects = null
            stagedCompletionSideEffects = null
            restoreRuntime(before)
            pendingActionTransaction = createActionTransaction(
                lastAiUnitResolution!!.actorId, before, afterResolution, hitSideEffects, completionSideEffects,
            )
        } else if (deferMutations) {
            stagedHitSideEffects = null
            stagedCompletionSideEffects = null
        }
        return AiTurnResult(moves, attacks, holds)
    }

    private data class AiDecision(
        val x: Int,
        val y: Int,
        val targetId: String?,
        val magicId: Int?,
        val value: Int,
        /** Control._AIProcess stores info.value, not terrain-inclusive value. */
        val actionValue: Int = 0,
    )

    private companion object {
        const val DEFAULT_TERRAIN_SIZE = 100
        const val IMPASSABLE_TERRAIN_COST = 255
        /** Config.ENABLED_FEATURE.ZJHH. */
        const val ENABLED_FEATURE_ZJHH = 8
        /** Config.ENABLED_FEATURE.ZDBHSW. */
        const val ENABLED_FEATURE_ZDBHSW = 32
    }

    /** Source BattleScreen._ai's point/action maximization for physical actions. */
    private fun chooseAiDecision(
        unit: BattleUnit,
        opponents: List<BattleUnit>,
        designated: BattleUnit?,
        aiMode: Int = unit.ai,
        /** Control._AIProcess(t), notably CtrlYDDZDDBM's t=2. */
        aiFlags: Int = 0,
        forcedTarget: Control.Point = Control.Point(unit.aiTargetX, unit.aiTargetY),
        candidatePoints: Collection<Pair<Int, Int>>? = null,
    ): AiDecision? {
        // BattleScreen._process supplies controller-specific psAry. Passive
        // and hold controllers receive only their current point; destination
        // controllers receive the reachable point closest to the script's
        // target. Active controllers retain the complete reachable set.
        val reachable = reachableTiles(unit.id).keys
        val points = candidatePoints?.toCollection(linkedSetOf()) ?: when (aiMode) {
            // CtrlBDCJ._selectMovePoint2 first calls _aiHaveAttackTargets(),
            // which scans every psAry movement point.  It stops the turn
            // only when no one of those points can attack an opponent.
            0 -> linkedSetOf(unit.tileX to unit.tileY).apply {
                val allPoints = linkedSetOf(unit.tileX to unit.tileY).apply { addAll(reachable) }
                val hasAttackTarget = allPoints.any { (x, y) ->
                    opponents.any { target -> canAttackFrom(unit, x, y, target) }
                }
                if (hasAttackTarget) addAll(reachable)
            }
            // CtrlJSYD._psAryIter yields its current point only.
            2 -> linkedSetOf(unit.tileX to unit.tileY)
            4, 6 -> {
                val destination = reachable.minByOrNull { (x, y) ->
                    kotlin.math.abs(x - unit.aiTargetX) + kotlin.math.abs(y - unit.aiTargetY)
                }
                // CtrlDZDD/CtrlTZZDD call _zdmdd then _ganlu.  Both methods
                // replace the controller with a forced-destination control;
                // they do not submit the current tile to BattleScreen._ai for
                // a terrain-score comparison.
                destination?.let { linkedSetOf(it) } ?: linkedSetOf(unit.tileX to unit.tileY)
            }
            // CtrlYDDZDDJS/CtrlYDDZDDBM/CtrlYDDZDDGJ override
            // _psAryIter and submit only their forced destination point.
            7, 8, 9 -> reachable.minByOrNull { (x, y) ->
                kotlin.math.abs(x - unit.aiTargetX) + kotlin.math.abs(y - unit.aiTargetY)
            }?.let { destination -> linkedSetOf(destination) } ?: linkedSetOf(unit.tileX to unit.tileY)
            else -> linkedSetOf(unit.tileX to unit.tileY).apply { addAll(reachable) }
        }
        var best: AiDecision? = null
        val diagnosticScores = mutableListOf<String>()
        val originalX = unit.tileX
        val originalY = unit.tileY
        // Control._AIProcess clears flag 2 when the actor owns WFJGJ.
        val effectiveAiFlags = if (aiFlags and 2 != 0 && unit.skills[226]?.and(255)?.let { it != 255 } == true) aiFlags and 2.inv() else aiFlags
        points.forEach { (x, y) ->
            // Control._AIProcess calls searchUnitByPos(s,l,0).  psAry may
            // contain a friendly-occupied routing node, but only the acting
            // unit itself is evaluated from such a position.
            unitAt(x, y)?.takeIf { it !== unit }?.let { return@forEach }
            unit.tileX = x
            unit.tileY = y
            // Control._AIProcess: floor(i.terrainImpact() / 5), rather than
            // the raw 100-based terrain percentage.
            var value = (unit.terrainImpacts[terrain?.terrainAt(x, y)] ?: 100) / 5
            // `_AIProcess` adds cover pressure only for a civil officer,
            // ranged arm, or wounded actor, then adds the terrain's
            // RESUMEHP value only for the wounded case.  Controller modes
            // do not receive an implicit destination/target bonus here.
            val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
            if (unit.armType == 1 || unit.remoteAttack || wounded) {
                units.values.filter { it.visible && it != unit }.forEach { other ->
                    // Control calls BattleUnit.distance(other, 1) here. In
                    // the source, flag bit 1 retains only diagonal adjacency.
                    val d = ControlScoring.coverDistance(
                        unit.tileX, unit.tileY, other.tileX, other.tileY,
                    )
                    if (d in 1..4) {
                        value += ControlScoring.coverPressure(d, areAllied(unit, other))
                    }
                }
            }
            if (wounded) value += terrainResumeRates[terrain?.terrainAt(x, y)] ?: 0
            // BattleScreen.filterHitAreaUnit walks the authored hit-area `ps`
            // array, not Battle's unit insertion order.  Equal scores
            // retain the first target in that offset order (straight tiles
            // precede diagonals for infantry).
            val physicalTargets = if (unit.attackAllScreen) opponents else unit.attackOffsets
                .mapNotNull { (dx, dy) -> unitAt(unit.tileX + dx, unit.tileY + dy) }
                .distinct()
                .filter { candidate -> candidate in opponents }
            if ((unit.characterId == 474 && round == 1 && (x to y) in setOf(8 to 17, 9 to 17)) ||
                (unit.characterId in setOf(258, 259) && round in 2..3 && physicalTargets.isNotEmpty())) {
                diagnosticScores += "$x,$y=" + physicalTargets.joinToString("|") {
                    "${it.characterId}:${estimatedAttackValue(unit, it)}:hp=${it.hitPoints}/${it.maxHitPoints}:harm=${PhysicalDamageCalculator.basePhysicalDamage(unit, it, basePhysicalDamageContext(unit, it, splash = false))}:rate=${probabilityResolver.physicalHitRate(unit, it)}"
                }
            }
            val scoredPhysicalTargets = physicalTargets.filter { candidate ->
                canAttack(unit, candidate) &&
                    // `_AIProcess(2)` skips a physical target that can
                    // already attack the actor; CtrlYDDZDDBM uses this when
                    // travelling to a magic destination.
                    (effectiveAiFlags and 2 == 0 || !canAttack(candidate, unit))
            }.mapNotNull { target ->
                val rawValue = estimatedAttackValue(unit, target)
                // Control._AIProcess rejects `_countAttackValue` below one
                // before `_AIProcess2` adds the controller's GJZDWJ bonus.
                // Applying the bonus first incorrectly revives attacks whose
                // counterattack makes their original score non-positive.
                if (rawValue < 1) null else target to rawValue
            }
            val scoredTarget = scoredPhysicalTargets.maxByOrNull { (target, rawValue) ->
                // Control._AIProcess overwrites its temporary FZGJ/distance
                // value with _countAttackValue before comparison.
                rawValue + if (
                    aiMode in setOf(ControlAi.ATTACK_UNIT, ControlAi.MOVE_ATTACK_UNIT) && target === designated
                ) 110 else 0
            }
            val target = scoredTarget?.first
            val physicalValue = scoredTarget?.second ?: Int.MIN_VALUE
            // CtrlGJWJ and CtrlYDDZDDGJ override `_AIProcess2`: the retained
            // target receives Config.AI_VALUE.GJZDWJ (110) after its normal
            // attack score.  No other controller receives this bonus.
            val designatedBonus = if (aiMode in setOf(ControlAi.ATTACK_UNIT, ControlAi.MOVE_ATTACK_UNIT) && target === designated) 110 else 0
            val scoredPhysicalValue = if (physicalValue == Int.MIN_VALUE) physicalValue else physicalValue + designatedBonus
            val magic = bestAiMagic(unit, opponents, designated, aiMode)
            val useMagic = magic != null && magic.third > scoredPhysicalValue
            val actionValue = if (useMagic) magic!!.third else scoredPhysicalValue
            if (actionValue != Int.MIN_VALUE) value += actionValue + 30
            val candidate = AiDecision(
                x, y,
                if (useMagic) magic!!.first.id else target?.id,
                if (useMagic) magic!!.second.id else null,
                value,
                actionValue.takeIf { it != Int.MIN_VALUE } ?: 0,
            )
            if (best == null || candidate.value > best!!.value) best = candidate
        }
        unit.tileX = originalX
        unit.tileY = originalY
        if (diagnosticScores.isNotEmpty()) {
            val friend234 = units.values.firstOrNull { it.characterId == 234 }
            traceActions += "diag${unit.characterId}:u234=${friend234?.tileX},${friend234?.tileY},v=${friend234?.visible},acted=${friend234?.hasActed}:arm=${unit.armType},remote=${unit.remoteAttack}:offsets=${unit.attackOffsets.joinToString("|") { "${it.first},${it.second}" }}:skills=${unit.skills.keys.joinToString("|")}:${diagnosticScores.joinToString(";")}"
        }
        return best
    }

    /**
     * Captures the actual AI scorer for one source character without running
     * a turn, moving a unit, or injecting an expected choice.  Cocos
     * BattleScreen._ai accepts an explicit point array; the source evidence
     * for R_00 unit 474 supplied only its current point, so callers pass the
     * same constrained candidate set here.
     */
    fun traceAiPlannerAtCurrentPoint(characterId: Int, aiFlags: Int = 1): AiPlannerTrace? {
        val unit = units.values.firstOrNull { it.visible && it.characterId == characterId } ?: return null
        // This is the direct BattleScreen._ai path, not Control._AIProcess:
        // direct _ai adds raw terrainImpact (100-based) and flag bit 1 skips
        // all physical/magic action scoring.  The ordinary turn runner still
        // calls chooseAiDecision through ControlManager for _AIProcess.
        val value = cocosAiBaseValueAt(unit, unit.tileX, unit.tileY)
        return AiPlannerTrace(
            characterId = characterId,
            ai = unit.ai,
            x = unit.tileX,
            y = unit.tileY,
            value = value,
            actionValue = null,
            targetId = null,
            magicId = null,
        )
    }

    /** Direct Cocos BattleScreen._ai's `C += t.terrainImpact()` base score. */
    private fun cocosAiBaseValueAt(unit: BattleUnit, x: Int, y: Int): Int {
        var value = unit.terrainImpacts[terrain?.terrainAt(x, y)] ?: 100
        val wounded = unit.hitPoints < unit.maxHitPoints * (if (unit.famous) 4 else 2) / 10
        if (unit.armType == 1 || unit.remoteAttack || wounded) {
            val originalX = unit.tileX
            val originalY = unit.tileY
            unit.tileX = x
            unit.tileY = y
            units.values.filter { it.visible && it !== unit }.forEach { other ->
                val d = ControlScoring.coverDistance(
                    unit.tileX, unit.tileY, other.tileX, other.tileY,
                )
                value += ControlScoring.coverPressure(d, areAllied(unit, other))
            }
            unit.tileX = originalX
            unit.tileY = originalY
        }
        if (wounded) value += terrainResumeRates[terrain?.terrainAt(x, y)] ?: 0
        return value
    }

    /** Source Control._AIProcess magic branch, evaluated after moving the actor to each candidate tile. */
    private fun bestAiMagic(
        attacker: BattleUnit,
        opponents: List<BattleUnit>,
        designated: BattleUnit?,
        aiMode: Int = attacker.ai,
    ): Triple<BattleUnit, GameDataCatalog.MagicProfile, Int>? {
        // `_AIProcess` owns one cache for all candidates at this point.
        val scoreCache = linkedMapOf<String, Int>()
        val candidates = attacker.magic.asSequence()
        // Control._AIProcess enters the strategy scorer only when
        // AIIsUse()!=13 and magicConditionTest returns zero.  Value 13 marks
        // a player-only strategy; it does not bypass the condition gate.
        .filter { it.aiUse != 13 && magicConditionReason(attacker, it) == null }
        .filter { attacker.magicPoints >= it.expendMp }
        .flatMap { magic ->
            val targets = when (magic.target) {
                1 -> units.values.filter { it.visible && areAllied(it, attacker) }
                2 -> listOf(attacker)
                3 -> units.values.filter { it.visible }
                else -> opponents
            }
            targets.asSequence()
                .filter { target ->
                    magic.category in setOf(1, 29) || magic.hitArea.allScreen ||
                        (target.tileX - attacker.tileX to target.tileY - attacker.tileY) in magic.hitArea.offsets
                }
                .map { target ->
                    var score = estimatedMagicValue(attacker, target, magic, scoreCache)
                    // Control skips the entire candidate, including effect
                    // tiles, when the selected primary has no positive value.
                    if (score >= 1) {
                        if (!areAllied(attacker, target)) score += distance(attacker, target)
                        // Control._AIProcess subtracts `et` for every candidate
                        // before effect-area additions: floor(expendMp *
                        // AI_VALUE.HP_MP_RATE / unit.mp()).
                        score -= magic.expendMp * 100 / attacker.maxMagicPoints.coerceAtLeast(1)
                        if (aiMode in setOf(ControlAi.ATTACK_UNIT, ControlAi.MOVE_ATTACK_UNIT) && target === designated) score += 110
                        magic.effectOffsets.mapNotNull { (dx, dy) -> unitAt(target.tileX + dx, target.tileY + dy) }
                            .filter { affected ->
                                affected !== target && affected.visible && when (magic.target) {
                                    0 -> !areAllied(affected, attacker) // MAGIC_TARGET.ENEMY
                                    1 -> areAllied(affected, attacker)  // MAGIC_TARGET.MINE
                                    else -> true
                                }
                            }
                            .forEach { affected -> score += estimatedMagicValue(attacker, affected, magic, scoreCache) }
                    }
                    Triple(target, magic, score)
                }
        }
        .filter { it.third > 0 }
        .toList()
        val diagnosticMagicActor = when {
            attacker.characterId == 147 && round == 6 -> "147r6"
            attacker.characterId == 22 && round == 4 -> "22r4"
            else -> null
        }
        if (diagnosticMagicActor != null &&
            traceActions.none { it.startsWith("diagMagicScores$diagnosticMagicActor:") }) {
            traceActions += "diagMagicScores$diagnosticMagicActor:" + candidates.joinToString(";") { (target, magic, score) ->
                "m${magic.id}/t${target.characterId}/s$score/c${magic.category}/h${magic.harmType}/p${magic.power}/mp${magic.expendMp}/ai${magic.aiUse}"
            }
        }
        return candidates.maxByOrNull { it.third }
    }

    /** Injectable `Control._countAttackValue` preview for one primary target. */
    fun previewAiAttackValue(attackerId: String, targetId: String): Int {
        val attacker = units[attackerId] ?: return 0
        val target = units[targetId] ?: return 0
        return estimatedAttackValue(attacker, target)
    }

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
            basePhysicalDamageContext(attacker, target, splash = false),
        )
    }

    /**
     * Live adapter for attack-value scoring. Keeping the score engine
     * separate is useful for exhaustive tests while production AI supplies
     * the same base-damage records used by tactical preview.
     */
    private fun estimatedAttackValue(attacker: BattleUnit, target: BattleUnit): Int {
        return ControlScoring.attackValue(
            AiScoringUnit(attacker),
            AiScoringUnit(target),
            counter = true,
        )
    }

    /** Direct BattleUnit wrapper used by the Control.js scoring context. */
    private inner class AiScoringUnit(val source: BattleUnit) : ControlScoring.Unit {
        override val index: Int get() = source.characterId ?: source.id.hashCode()
        override val hp: Int get() = source.maxHitPoints
        override val hpCur: Int get() = source.hitPoints
        override val mp: Int get() = source.maxMagicPoints
        override val mpCur: Int get() = source.magicPoints
        override val armType: Int get() = source.armType
        override val isRemote: Boolean get() = source.remoteAttack
        override val famous: Boolean get() = source.famous
        override val mine: Boolean get() = source.isPlayerSide()
        override val ai: Int get() = source.ai
        override val aiValue: Int get() = source.aiValue
        override fun skill(id: Int): Int = source.skills[id]?.and(255) ?: 255
        override fun status(index: Int): Int = when (index) {
            0, 1, 2, 3, 4, 5 -> when {
                (source.attributeLifts[BattleAttribute.entries[index]] ?: 0) < 0 -> ControlScoring.Lift.DOWN
                (source.attributeLifts[BattleAttribute.entries[index]] ?: 0) > 0 -> ControlScoring.Lift.UP
                else -> ControlScoring.Lift.NORMAL
            }
            7 -> if (BattleStatus.PARALYSIS in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            8 -> if (BattleStatus.SILENCE in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            9 -> if (BattleStatus.CONFUSION in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            10 -> if (BattleStatus.POISON in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            13 -> if (BattleStatus.LOST in source.statuses) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            14 -> if (source.hasActed) ControlScoring.Lift.DOWN else ControlScoring.Lift.NORMAL
            else -> ControlScoring.Lift.NORMAL
        }
        override fun isCanXue(): Boolean = source.hitPoints < source.maxHitPoints * (if (source.famous) 4 else 2) / 10
        override fun isCanLan(): Boolean = source.magicPoints < source.maxMagicPoints * (if (source.famous) 4 else 2) / 10
        override fun attackHarms(target: ControlScoring.Unit): List<ControlScoring.AttackHarm> {
            val primary = (target as? AiScoringUnit)?.source ?: return emptyList()
            // BattleUnit.countAtkHarm2 begins with BattleScreen.testUnit.
            // This guard is also reached recursively while estimating a
            // counterattack, so a confused or out-of-range defender must
            // contribute no retaliation score.
            if (!source.visible || BattleStatus.CONFUSION in source.statuses ||
                !primary.visible || !canAttack(source, primary)) return emptyList()
            // BattleUnit.countAtkHarm2: filterEffAreaUnit(..., 42), then
            // unshift(target).  The filter contains opposing live units only.
            val affected = buildList {
                add(primary to false)
                physicalEffectPositions(source, primary).asSequence()
                    .mapNotNull { (x, y) -> unitAt(x, y) }
                    .filter { it !== primary && it.visible && !areAllied(source, it) }
                    .forEach { add(it to true) }
            }
            var flag = when {
                primary.skills.keys.any { it in intArrayOf(226, 44, 251, 50) && primary.skills[it]?.and(255) != 255 } -> 0
                canAttack(primary, source) -> 1
                else -> 0
            }
            flag = flag or when (source.armType) { 0 -> 8; 1 -> 16; else -> 0 }
            return affected.map { (victim, splash) ->
                val harm = PhysicalDamageCalculator.basePhysicalDamage(
                    source,
                    victim,
                    basePhysicalDamageContext(source, victim, splash),
                )
                if (victim.famous) flag = flag or 2
                if (harm >= victim.hitPoints) flag = flag or 4
                val hitRate = if (BattleStatus.CONFUSION in victim.statuses) 100 else probabilityResolver.physicalHitRate(source, victim)
                ControlScoring.AttackHarm(harm, AiScoringUnit(victim), flag, hitRate)
            }
        }
        override fun magicHarm(magic: ControlScoring.Magic, target: ControlScoring.Unit): Int {
            val profile = (magic as? AiMagic)?.source ?: return 0
            val victim = (target as? AiScoringUnit)?.source ?: return 0
            val base = maxOf(1, (BattleAttributeCalculator.effective(source, BattleAttribute.SPIRIT) - BattleAttributeCalculator.effective(victim, BattleAttribute.SPIRIT)) / 3 + 25 + source.level)
            return when (profile.type) {
                // count_magicHarm's dedicated restoration formulas.
                19 -> source.hitPoints * profile.power / 100 + if (profile.id == 39 || profile.id == 41) source.spirit / 10 else source.spirit / 2
                20 -> profile.expendMp
                4 -> if (profile.category == 2) victim.maxHitPoints * profile.power / 100 else offensiveMagicHarm(base, profile, victim)
                else -> offensiveMagicHarm(base, profile, victim)
            }
        }

        private fun offensiveMagicHarm(base: Int, magic: GameDataCatalog.MagicProfile, victim: BattleUnit): Int {
            var value = maxOf(1, base * magic.power / 100 * victim.magicHarmRate / 100)
            value += magicFlatSkillDamage(source, magic)
            value = maxOf(1, value * magicSkillDamageRate(source, victim, magic) / 100)
            value = value * magicWeatherRate(magic) / 100
            value = value * offensiveMagicTerrainRate(source, magic) / 100
            val minimum = if (!source.isPlayerSide()) {
                maxOf(1, minOf(7, units.values.count { it.visible && it.isPlayerSide() }) * source.maxMagicPoints / 100)
            } else 1
            return maxOf(minimum, value)
        }
    }

    private data class AiMagic(val source: GameDataCatalog.MagicProfile) : ControlScoring.Magic {
        override val id get() = source.id
        override val category get() = source.category
        override val type get() = source.type
        override val harmType get() = source.harmType
        override val expendMp get() = source.expendMp
    }

    /** Live adapter for Control._countMagicValue's status/HP/MP scoring. */
    private fun estimatedMagicValue(
        attacker: BattleUnit,
        target: BattleUnit,
        magic: GameDataCatalog.MagicProfile,
        cache: MutableMap<String, Int>,
    ): Int = ControlScoring.magicValue(
        AiMagic(magic), AiScoringUnit(attacker), AiScoringUnit(target), cache,
        hitRate = { _, _, _ -> probabilityResolver.magicHitRate(attacker, target, magic) },
    )

    private fun canAttack(attacker: BattleUnit, target: BattleUnit): Boolean =
        attacker.attackAllScreen || ((target.tileX - attacker.tileX) to (target.tileY - attacker.tileY)) in attacker.attackOffsets

    /** Control._aiHaveAttackTargets evaluated for a candidate `psAry` tile. */
    private fun canAttackFrom(attacker: BattleUnit, x: Int, y: Int, target: BattleUnit): Boolean =
        attacker.attackAllScreen || ((target.tileX - x) to (target.tileY - y)) in attacker.attackOffsets

    /** Exact position half of BattleScreen.filterEffAreaUnit(attacker, target, effarea, 42). */
    private fun physicalEffectPositions(attacker: BattleUnit, target: BattleUnit): Set<Pair<Int, Int>> {
        val effectArea = attacker.attackEffectAreaId ?: return attacker.attackEffectOffsets.mapTo(linkedSetOf()) { (dx, dy) ->
            target.tileX + dx to target.tileY + dy
        }
        // BattleScreen.filterEffAreaUnit explicitly assigns `f = []` for
        // ZHUORE and leaves YUANZHEN in that same empty default branch.
        if (effectArea == 0 || effectArea == 12) return emptySet()
        fun sign(value: Int) = value.compareTo(0)
        val dx = sign(target.tileX - attacker.tileX)
        val dy = sign(target.tileY - attacker.tileY)
        val dynamic = when (effectArea) {
            4, 5, 7 -> List(if (effectArea == 4) 1 else if (effectArea == 5) 5 else 2) { index ->
                target.tileX + dx * (index + 1) to target.tileY + dy * (index + 1)
            }
            9 -> when {
                dx == 0 && dy == 0 -> emptyList()
                dx == 0 -> listOf(target.tileX - 1 to target.tileY, target.tileX + 1 to target.tileY)
                dy == 0 -> listOf(target.tileX to target.tileY - 1, target.tileX to target.tileY + 1)
                else -> listOf(target.tileX + dx to target.tileY, target.tileX to target.tileY + dy)
            }
            11 -> {
                val side = when {
                    dx == 0 && dy == 0 -> emptyList()
                    dx == 0 -> listOf(target.tileX - 1 to target.tileY, target.tileX + 1 to target.tileY)
                    dy == 0 -> listOf(target.tileX to target.tileY - 1, target.tileX to target.tileY + 1)
                    else -> listOf(target.tileX + dx to target.tileY, target.tileX to target.tileY + dy)
                }
                side + List(2) { index -> target.tileX + dx * (index + 1) to target.tileY + dy * (index + 1) }
            }
            else -> emptyList()
        }
        if (dynamic.isNotEmpty()) return dynamic.toCollection(linkedSetOf())
        // KUANGWU (10) anchors the table pattern at attacker; all ordinary
        // static patterns anchor at target. ZHUORE (0) intentionally empty.
        val anchor = if (effectArea == 10) attacker else target
        return attacker.attackEffectOffsets.mapTo(linkedSetOf()) { (x, y) -> anchor.tileX + x to anchor.tileY + y }
    }

    /**
     * Once an attack has hit, skill 277 lets the defender redirect a percentage of
     * the pending harm to the
     * lowest-HP unit on the opposing side inside its own attack pattern.
     * The attacker is removed from that candidate list.
     */
    private fun physicalDamageTransfer(
        attacker: BattleUnit,
        defender: BattleUnit,
        resolvedHarm: Int,
    ): Pair<BattleUnit, Int>? {
        val percent = defender.skills[277]?.and(255)?.takeIf { it != 255 } ?: return null
        if (resolvedHarm < defender.level || BattleStatus.CONFUSION in defender.statuses) return null
        val candidates = (if (defender.attackAllScreen) {
            units.values.asSequence()
        } else {
            defender.attackOffsets.asSequence().mapNotNull { (dx, dy) ->
                unitAt(defender.tileX + dx, defender.tileY + dy)
            }
        }).distinct()
            .filter { it !== attacker && !areAllied(defender, it) }
            .toList()
            .let { found -> if (found.size > 1) found.sortedBy { it.hitPoints } else found }
        val recipient = candidates.firstOrNull() ?: return null
        return recipient to (resolvedHarm * percent / 100)
    }

    /** Constructs all splash records before resolving the primary hit callback. */
    private fun computePhysicalSplashHarms(
        attacker: BattleUnit,
        primaryTarget: BattleUnit,
        critical: Boolean,
        activeAttack: Boolean = true,
        counter: Boolean = false,
        continuous: Boolean = false,
    ): List<Pair<BattleUnit, Int>> = physicalEffectPositions(attacker, primaryTarget).asSequence()
        .mapNotNull { (x, y) -> unitAt(x, y) }
        .filter { it !== primaryTarget && it.visible && !areAllied(attacker, it) }
        .map { affected ->
            val special = mrspDamage(attacker, affected)
            val harm = special ?: run {
                val base = PhysicalDamageCalculator.basePhysicalDamage(
                    attacker,
                    affected,
                    basePhysicalDamageContext(
                        attacker,
                        affected,
                        splash = false,
                        defenseRule = PhysicalDefenseRule.INTRINSIC,
                    ),
                )
                var value = maxOf(1, base * PhysicalDamageCalculator.physicalArmRestraint(attacker, affected) / 100)
                value = value * PhysicalDamageCalculator.physicalDamageRate(
                    attacker,
                    affected,
                    physicalDamageRateContext(attacker, affected),
                ) / 100
                value = BattleAttributeCalculator.physicalDamageAfterResistance(value, attacker, affected)
                value += PhysicalDamageCalculator.physicalFlatSkillDamage(
                    attacker,
                    affected,
                    flatPhysicalDamageContext(attacker, activeAttack),
                )
                value = maxOf(1, value)
                value = PhysicalDamageCalculator.armorPiercingMinimumDamage(attacker, affected, value)
                value = PhysicalDamageCalculator.cappedPhysicalDamage(affected, value)
                maxOf(
                    PhysicalDamageCalculator.physicalMinimumDamage(attacker, visibleFamousPlayerCount()),
                    value * PhysicalDamageCalculator.physicalCriticalRate(
                        attacker,
                        affected,
                        physicalCriticalRateContext(
                            attacker,
                            affected,
                            critical,
                            counter = counter,
                            continuous = continuous,
                            splash = true,
                        ),
                    ) / 100,
                )
            }
            if (special == null) consumeMpAttackSkill(attacker)
            affected to harm
        }
        .toList()

    /** BattleUnit.countAtkHarm's `a.length > 0` HAVE_CT predicate. */
    private fun hasPhysicalEffectTargets(attacker: BattleUnit, target: BattleUnit): Boolean =
        physicalEffectPositions(attacker, target).asSequence()
            .mapNotNull { (x, y) -> unitAt(x, y) }
            .any { it !== target && it.visible && !areAllied(attacker, it) }

    /** CtrlGJWJ._aiHaveAttackTargets(targetIndex) across every psAry point. */
    private fun hasAttackCandidate(attacker: BattleUnit, target: BattleUnit): Boolean =
        linkedSetOf(attacker.tileX to attacker.tileY).apply { addAll(reachableTiles(attacker.id).keys) }
            .any { (x, y) -> canAttackFrom(attacker, x, y, target) }

    private fun areAllied(left: Faction, right: Faction): Boolean =
        left.isPlayerSide() == right.isPlayerSide()

    private fun areAllied(left: BattleUnit, right: BattleUnit): Boolean =
        areAllied(left.effectiveFaction(), right.effectiveFaction())

    /** BattleUnit.checkCrit increments for countAtkHarm's retained CRIT flag, then shows every other one. */
    private fun resolveCriticalSpeech(unit: BattleUnit, criticalFlag: Boolean): String? {
        if (!criticalFlag) return null
        val show = unit.criticalSpeechChecks % 2 == 0
        unit.criticalSpeechChecks++
        if (!show) return null
        val speech = unit.criticalSpeech
        if (speech.texts.isEmpty()) return null
        val index = when {
            !speech.randomized || speech.texts.size == 1 -> 0
            speech.flagRandom -> probabilityResolver.flagRandom(0, speech.texts.lastIndex)
            else -> probabilityResolver.defaultRandom(0, speech.texts.lastIndex)
        }
        return speech.texts[index]
    }

    private fun distance(a: BattleUnit, b: BattleUnit): Int = kotlin.math.abs(a.tileX - b.tileX) + kotlin.math.abs(a.tileY - b.tileY)

    /** BattleUnit.canBack/backMove: one tile directly away from the attacker. */
    private fun backPosition(defender: BattleUnit, attacker: BattleUnit): Pair<Int, Int>? {
        val dx = when {
            defender.tileX < attacker.tileX -> -1
            defender.tileX > attacker.tileX -> 1
            else -> 0
        }
        val dy = when {
            defender.tileY < attacker.tileY -> -1
            defender.tileY > attacker.tileY -> 1
            else -> 0
        }
        val point = defender.tileX + dx to defender.tileY + dy
        if (point.first < 0 || point.second < 0) return null
        if (terrain?.let { point.first >= it.width || point.second >= it.height } == true) return null
        if (point in blockedTiles || unitAt(point.first, point.second) != null) return null
        val terrainId = terrain?.terrainAt(point.first, point.second)
        if (terrainId?.let { defender.terrainMovementCosts[it] ?: 255 } ?: 1 >= 255) return null
        return point
    }

    private fun moveToward(unit: BattleUnit, goalX: Int, goalY: Int): Boolean {
        val candidates = movePoints(unit, BattleAttributeCalculator.finalMovement(unit, weather)).points.keys
            .asSequence()
            .filter { it != unit.tileX to unit.tileY }
            .sortedBy { kotlin.math.abs(goalX - it.first) + kotlin.math.abs(goalY - it.second) }
        val target = candidates.firstOrNull { (x, y) -> x to y !in blockedTiles && unitAt(x, y) == null } ?: return false
        return moveUnit(unit.id, target.first, target.second) is TacticalActionResult.Success
    }

    /** BattleUnit.countDir: 0 up, 1 right, 2 down, 3 left. */
    private fun facingDirection(fromX: Int, fromY: Int, toX: Int, toY: Int): Int {
        val dx = kotlin.math.abs(toX - fromX)
        val dy = kotlin.math.abs(toY - fromY)
        return if (dy > dx) {
            if (fromY > toY) 0 else 2
        } else if (fromX > toX) 3 else 1
    }

    /** Adapts live battle state to the pure movement planner. */
    private fun movePoints(
        unit: BattleUnit,
        movement: Int,
        ignoredEnemyId: String? = null,
        startOverride: Pair<Int, Int>? = null,
    ) = movementPlanner.movePoints(
        actor = unit,
        movement = movement,
        rules = movementRules(unit),
        ignoredEnemyId = ignoredEnemyId,
        startOverride = startOverride ?: (unit.tileX to unit.tileY),
    )

    private fun movementRules(unit: BattleUnit): BattleMovementPlanner.MovementRules {
        // Config.SKILL_TYPE: CYYD(29), ELYD(35), TJEL(219), TJYD(220).
        val ignoresTerrain = unit.skills[29]?.and(255)?.let { it != 255 } == true
        val ignoresTerrainAndEnemyNear = unit.skills[219]?.and(255)?.let { it != 255 } == true
        val oneTerrainCost = !ignoresTerrainAndEnemyNear && unit.skills[35]?.and(255)?.let { it != 255 } == true
        val canLeaveEnemyNear = ignoresTerrainAndEnemyNear || unit.skills[220]?.and(255)?.let { it != 255 } == true
        return BattleMovementPlanner.MovementRules(
            ignoresTerrain = ignoresTerrain,
            treatsEveryTerrainAsOne = ignoresTerrainAndEnemyNear || oneTerrainCost,
            ignoresEnemyNear = canLeaveEnemyNear,
        )
    }

    /** Stable weighted movement route using the authored traversal order. */
    private fun findMovementPath(
        unit: BattleUnit,
        targetX: Int,
        targetY: Int,
        avoidEnemies: Boolean = false,
        penalizeEnemyTiles: Boolean = false,
        allowEnemyOnTarget: Boolean = false,
    ): List<Pair<Int, Int>>? =
        movementPlanner.findPath(
            actor = unit,
            start = unit.tileX to unit.tileY,
            target = targetX to targetY,
            rules = BattleMovementPlanner.PathRules(
                avoidEnemies = avoidEnemies,
                penalizeEnemyTiles = penalizeEnemyTiles,
                allowEnemyOnTarget = allowEnemyOnTarget,
                treatsEveryTerrainAsOne = unit.skills.keys.any {
                    it in setOf(35, 219) && unit.skills[it]?.and(255) != 255
                },
            ),
        )

    /**
     * Read-only path used by authored `stage.unit(...).move(...)` commands.
     * Mirrors BattleUnit.move: findEmptyPos first, then BattleScreen.AStar with
     * flags=0. Coordinate mutation remains at the presentation callback.
     */
    fun scriptedMovePath(characterId: Int, targetX: Int, targetY: Int): List<Pair<Int, Int>>? {
        val unit = battlefield.allPresentationUnits()
            .firstOrNull { it.characterId == characterId } ?: return null
        val clamped = targetX.coerceIn(0, (terrain?.width ?: 100) - 1) to
            targetY.coerceIn(0, (terrain?.height ?: 100) - 1)
        val destination = movementPlanner.findScriptedDestination(unit, clamped, ::isInsideDefaultTerrainBounds)
        return destination?.let { findMovementPath(unit, it.first, it.second) }
    }

    /** BattleScreen.findEmptyPos constrained to ControlManager.psHash. */
    private fun findReachableEmptyPosition(
        unit: BattleUnit,
        seed: Pair<Int, Int>,
        reachable: Set<Pair<Int, Int>>,
    ): Pair<Int, Int>? = movementPlanner.findEmptyPosition(unit, seed, reachable, ::isInsideDefaultTerrainBounds)

    private fun isInsideDefaultTerrainBounds(point: Pair<Int, Int>): Boolean =
        point.first >= 0 && point.second >= 0 &&
            point.first < (terrain?.width ?: DEFAULT_TERRAIN_SIZE) &&
            point.second < (terrain?.height ?: DEFAULT_TERRAIN_SIZE)

    private fun GameDataCatalog.MagicProfile.statusEffect(): BattleStatus? = when (category) {
        8 -> BattleStatus.CONFUSION
        9 -> BattleStatus.POISON
        10 -> BattleStatus.PARALYSIS
        11 -> BattleStatus.SILENCE
        else -> null
    }

    /**
     * getMagicTerrainRate affects efficiency rather than target legality.
     * Offensive count_magicHarm floors this rate to 85%; only JHP healing
     * retains a zero rate without CLWSDX (19).
     */
    private fun magicTerrainAllowed(magic: GameDataCatalog.MagicProfile, target: BattleUnit): Boolean = true
    /** BattleUnit.getMagicWeatherRate; a bypassed weather restriction is 85% efficient. */
    private fun magicWeatherRate(magic: GameDataCatalog.MagicProfile): Int {
        val allowed = when (magic.condition) {
            0 -> weather in setOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY)
            2 -> weather in setOf(BattleWeather.HEAVY_RAIN, BattleWeather.SNOW)
            3 -> weather == BattleWeather.CLEAR
            4 -> weather == BattleWeather.CLOUDY
            else -> true
        }
        return if (allowed) 100 else 85
    }

    /** Offensive count_magicHarm floors unsuitable elemental terrain at 85%. */
    private fun offensiveMagicTerrainRate(target: BattleUnit, magic: GameDataCatalog.MagicProfile): Int {
        if (magic.type !in 0..3) return 100
        val terrainId = terrain?.terrainAt(target.tileX, target.tileY) ?: return 100
        val flag = terrainMagicFlags[terrainId] ?: 0
        return if (flag and (1 shl magic.type) != 0) 100 else 85
    }
    private fun healingTerrainRate(attacker: BattleUnit, magic: GameDataCatalog.MagicProfile): Int {
        if (magic.type !in 0..3 || terrainMagicFlags.isEmpty()) return 100
        val terrainId = terrain?.terrainAt(attacker.tileX, attacker.tileY) ?: return 100
        val flag = terrainMagicFlags[terrainId] ?: return 100
        if (flag and (1 shl magic.type) != 0) return 100
        return if (attacker.skills[19]?.and(255)?.let { it != 255 } == true) 85 else 0
    }

    /** BattleUnit._count_magic_add's direct, non-aura additions. */
    private fun magicFlatSkillDamage(attacker: BattleUnit, magic: GameDataCatalog.MagicProfile): Int {
        fun effect(skill: Int) = attacker.skills[skill]?.and(255)?.takeIf { it != 255 }
        var addition = effect(141)?.let { BattleAttributeCalculator.effective(attacker, BattleAttribute.ATTACK) * it / 100 } ?: 0 // LRHY
        if (magic.type == 0) addition += effect(107) ?: 0 // HXCLZS, Gong Huo
        return addition
    }

    /** BattleUnit._count_magic_rate. */
    private fun magicSkillDamageRate(attacker: BattleUnit, target: BattleUnit, magic: GameDataCatalog.MagicProfile): Int {
        fun effect(unit: BattleUnit, skill: Int) = unit.skills[skill]?.and(255)?.takeIf { it != 255 }
        var rate = 100
        // MRSP2 is the first operation in source _count_magic_rate and uses
        // the flag-random stream. Omitting the draw changed both damage and
        // every later combat decision in long battles.
        effect(attacker, 292)?.let { rate += 10 + probabilityResolver.flagRandom(0, 5) }
        if (magic.type in 0..3) rate += effect(attacker, 75) ?: 0 // FZSLCL
        if (magic.type == 0 && magic.effectAreaId == 0) rate += effect(attacker, 128) ?: 0 // JING_CE
        if (magic.type in 4..18) rate += effect(attacker, 62) ?: 0 // FZFACL
        effect(attacker, 145)?.takeIf { attacker.hitPoints >= attacker.magicPoints / 2 }?.let { rate += it } // MAI_DONG
        rate -= effect(target, 115) ?: 0 // JQCLSH
        effect(target, 245)?.let { rate -= target.hitPoints.coerceAtMost(target.maxHitPoints).let { hp -> (target.maxHitPoints - hp) * 100 / target.maxHitPoints.coerceAtLeast(1) } }
        return maxOf(1, rate)
    }

    /** BattleScreen.magicConditionTest, including CLWSTQ (20) and KYJZ (136). */
    private fun magicConditionReason(attacker: BattleUnit, magic: GameDataCatalog.MagicProfile): String? {
        fun active(skill: Int) = attacker.skills[skill]?.and(255)?.let { it != 255 } == true
        if (magic.condition in 2..5 && active(136)) return null // KYJZ: condition restriction bypass
        if (magic.condition == 1 && attacker.hitPoints < 40) return "HP가 40 미만이면 사용할 수 없는 전략입니다."
        val weatherAllowed = when (magic.condition) {
            0 -> weather in setOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY)
            2 -> weather in setOf(BattleWeather.HEAVY_RAIN, BattleWeather.SNOW)
            3 -> weather == BattleWeather.CLEAR
            4 -> weather == BattleWeather.CLOUDY
            else -> true
        }
        if (!weatherAllowed && !active(20)) return "현재 날씨에서는 사용할 수 없는 전략입니다."
        // Source condition 5 sets the special-condition bit only; KYJZ is
        // therefore required even though no weather bit is checked.
        return if (magic.condition == 5) "이 전략의 특수 사용 조건을 충족하지 못했습니다." else null
    }

    /** Control._magicValue: non-damage strategy categories map to status lifts. */
    private fun GameDataCatalog.MagicProfile.attributeChange(): Pair<BattleAttribute, Int>? = when (category) {
        4 -> BattleAttribute.CRITICAL to -1 // JDMJ
        5 -> BattleAttribute.MORALE to -1 // JDSQ
        6 -> BattleAttribute.ATTACK to -1 // JDNL (martial) / spirit for civil officers
        7 -> BattleAttribute.DEFENSE to -1 // JDFY
        16 -> BattleAttribute.MOVEMENT to 1 // ZJYDL
        17 -> BattleAttribute.CRITICAL to 1 // ZJMJ
        18 -> BattleAttribute.MORALE to 1 // ZJSQ
        19 -> BattleAttribute.ATTACK to 1 // ZJNL (martial) / spirit for civil officers
        20 -> BattleAttribute.DEFENSE to 1 // ZJFY
        else -> null
    }

    /** BattleUnit.setStateRound: enemy HL/MB override GAME_CFG.status.round. */
    private fun statusDuration(status: BattleStatus, unit: BattleUnit): Int = when {
        !unit.isPlayerSide() && status == BattleStatus.CONFUSION -> 1
        !unit.isPlayerSide() && status == BattleStatus.PARALYSIS -> 2
        else -> statusRoundFor(status)
    }.coerceIn(0, 3) // source stores each round in a two-bit field

    /** BattleScreen._stateProcess: decrement states and apply terrain recovery. */
    private fun processStartOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
    ): List<BattleUnitTurnChange> {
        // Source runs _stateProcess only for MINE and ENEMY.  MINE's
        // isMine() predicate includes FRIEND, so allied durations decrement
        // together before the player camp; the later FRIEND camp does not
        // decrement them a second time.
        val processedSide = when (faction) {
            Faction.PLAYER -> true
            Faction.ENEMY -> false
            Faction.FRIEND, Faction.REINFORCEMENTS -> null
        }
        val orderedUnits = units.values.filter {
            processedSide != null && it.effectiveFaction().isPlayerSide() == processedSide
        }.sortedWith(compareBy<BattleUnit> { it.tileY }.thenBy { it.tileX })
        if (processedSide == null) return emptyList()
        val primaryChanges = mutableListOf<BattleUnitTurnChange>()
        orderedUnits.forEach { unit ->
            val ordinaryBefore = turnSnapshot()
            unit.statuses.entries.toList().forEach { (status, rounds) ->
                if (rounds <= 1) unit.statuses.remove(status) else unit.statuses[status] = rounds - 1
            }
            unit.presentation.refreshStatus(unit.statuses, unit.attributeLifts)
            // subStateRound skips NORMAL attribute slots. When an active
            // lift expires, source setStateRound(remove) first writes the
            // status table's default round and then changes the state to
            // NORMAL, leaving that packed counter intact indefinitely.
            unit.attributeLifts.keys.toList().forEach { attribute ->
                val rounds = unit.attributeLiftRounds[attribute] ?: 0
                if (rounds <= 1) {
                    unit.attributeLifts.remove(attribute)
                    unit.attributeLiftRounds[attribute] = attributeStatusRoundFor(attribute)
                } else unit.attributeLiftRounds[attribute] = rounds - 1
            }
            unit.presentation.refreshAttributeStatusIcons(unit.attributeLifts)
            val terrainId = terrain?.terrainAt(unit.tileX, unit.tileY)
            if (unit.hitPoints < unit.maxHitPoints) {
                val resumeHp = terrainResumeRates[terrainId] ?: 0
                if (resumeHp != 0) unit.addHpcur(unit.maxHitPoints * resumeHp / 100)
            }
            if (unit.magicPoints < unit.maxMagicPoints) {
                val resumeMp = terrainResumeMp[terrainId] ?: 0
                if (resumeMp != 0) unit.addMpcur(resumeMp)
            }
            if (unit.hitPoints <= 0) battlefield.defeat(unit.id)
            primaryChanges += turnChanges(ordinaryBefore)
            if (unit.hitPoints <= 0) return@forEach

            // The four authored local branches run immediately after this
            // caster's ordinary state work, before the iterator advances.
            val caster = unit
            fun effect(skillId: Int) = caster.skills[skillId]?.and(255)?.takeIf { it != 255 }
            fun nearby(): List<BattleUnit> = infantryOffsets.mapNotNull { (dx, dy) ->
                units.values.firstOrNull { target ->
                    target.tileX == caster.tileX + dx && target.tileY == caster.tileY + dy
                }
            }.filter { it.isPlayerSide() == processedSide }.distinctBy { it.id }
            fun record(
                skillId: Int,
                value: Int,
                meffName: String? = null,
                targetOrder: List<BattleUnit>,
                mutate: () -> Unit,
            ) {
                val before = turnSnapshot()
                mutate()
                val order = targetOrder.mapIndexed { index, target -> target.id to index }.toMap()
                val nested = turnChanges(before).sortedBy { order[it.unitId] ?: Int.MAX_VALUE }
                if (targetOrder.isNotEmpty()) subflows += SettlementSubflow.LocalAura(
                    casterId = caster.id,
                    skillId = skillId,
                    skillValue = value,
                    meffName = meffName,
                    targets = targetOrder.map { it.id },
                    nestedChanges = nested,
                )
            }

            effect(103)?.let { value ->
                val targets = nearby().filter { target ->
                    listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                        .any(target.statuses::containsKey)
                }
                record(103, value, targetOrder = targets) {
                    targets.forEach { target ->
                        listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                            .forEach(target.statuses::remove)
                    }
                }
            }
            effect(208)?.let { value ->
                val targets = nearby().filter { it.hitPoints < it.maxHitPoints }
                record(208, value, "resume_hp", targets) {
                    targets.forEach { target ->
                        target.addHpcur(target.maxHitPoints * value / 100)
                    }
                }
            }
            effect(209)?.let { value ->
                val targets = nearby().filter { it.magicPoints < it.maxMagicPoints }
                record(209, value, "resume_mp", targets) {
                    targets.forEach { target ->
                        val addition = if (value == 0) (caster.level + 10) / 10
                        else target.maxMagicPoints * value / 100
                        target.addMpcur(addition)
                    }
                }
            }
            effect(210)?.takeIf { it and 31 != 0 }?.let { mask ->
                val targets = nearby()
                record(210, mask, targetOrder = targets) {
                    targets.forEach { target ->
                        BattleAttribute.entries.take(5).forEachIndexed { index, attribute ->
                            if (mask and (1 shl index) != 0) {
                                target.applyAttributeLift(attribute, 1, 3)
                            }
                        }
                    }
                }
            }
        }
        return primaryChanges
    }

    /** BattleScreen.restore: poison is settled after that exact camp acts. */
    private fun processEndOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
    ): List<BattleUnitTurnChange> {
        units.values.filter { it.effectiveFaction() == faction }.forEach { unit ->
            val grants = buildList {
                unit.skills[149]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = onRestoreUnitExperience(unit, amount)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.UNIT_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            unit.level = resolution.value.level
                            if (resolution.value.gained > 0) add(SettlementGrowthGrant(SettlementGrowthKind.UNIT_EXP, amount, unitResult = resolution.value))
                        }
                    }
                }
                unit.skills[150]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = onRestoreEquipmentExperience(unit, amount, CampaignEquipmentSlot.WEAPON)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.WEAPON_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            val result = resolution.value
                            if (result.gained > 0) add(SettlementGrowthGrant(SettlementGrowthKind.WEAPON_EXP, amount, equipmentResult = result))
                            if (result.leveledUp) equipmentUpgrades += result
                        }
                    }
                }
                unit.skills[151]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = onRestoreEquipmentExperience(unit, amount, CampaignEquipmentSlot.ARMOR)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.ARMOR_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            val result = resolution.value
                            if (result.gained > 0) add(SettlementGrowthGrant(SettlementGrowthKind.ARMOR_EXP, amount, equipmentResult = result))
                            if (result.leveledUp) equipmentUpgrades += result
                        }
                    }
                }
            }
            if (grants.isNotEmpty()) subflows += SettlementSubflow.Growth(unit.id, grants)
        }
        val poisonBefore = turnSnapshot()
        val lethalPoison = enabledFeatures and ENABLED_FEATURE_ZDBHSW != 0
        units.values.filter { it.effectiveFaction() == faction && BattleStatus.POISON in it.statuses }
            .toList()
            .forEach { unit ->
                // With ZDBHSW enabled the source deliberately permits poison
                // death. Only the legacy-disabled branch preserves one HP.
                if (!lethalPoison && unit.hitPoints < 2) return@forEach
                val rate = if (weather == BattleWeather.CLOUDY) 15 else 10
                var damage = unit.maxHitPoints * rate / 100
                if (!lethalPoison) damage = minOf(unit.hitPoints - 1, damage)
                unit.addHpcur(-damage)
                if (unit.hitPoints <= 0) battlefield.defeat(unit.id)
            }
        return turnChanges(poisonBefore)
    }

    private data class AttackStatusBatch(
        val statuses: Set<BattleStatus>,
        val downAttributes: Set<BattleAttribute>,
    )

    /** BattleUnit.getAtkStatus plus _attack2's two random supplementary lists. */
    private fun rollAttackStatusBatch(attacker: BattleUnit): AttackStatusBatch {
        val statuses = linkedSetOf<BattleStatus>()
        fun chance(skillId: Int, status: BattleStatus) {
            attacker.skills[skillId]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                if (probabilityResolver.random100() < rate) statuses += status
            }
        }
        chance(105, BattleStatus.CONFUSION)
        chance(144, BattleStatus.PARALYSIS)
        chance(127, BattleStatus.SILENCE)
        attacker.skills[272]?.and(255)?.takeIf { it != 255 }?.let { rate ->
            if (probabilityResolver.random100() <= rate) statuses += BattleStatus.POISON
        }
        if (attacker.skills[204]?.and(255)?.let { it != 255 } == true) {
            listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                .filterNot(statuses::contains)
                .forEach { status -> if (probabilityResolver.random100() > 70) statuses += status }
        }
        val staticAttributes = linkedSetOf<BattleAttribute>()
        mapOf(170 to BattleAttribute.ATTACK, 169 to BattleAttribute.DEFENSE, 171 to BattleAttribute.SPIRIT,
            168 to BattleAttribute.CRITICAL, 172 to BattleAttribute.MORALE, 173 to BattleAttribute.MOVEMENT)
            .forEach { (skill, attribute) -> if (attacker.skills[skill]?.and(255)?.let { it != 255 } == true) staticAttributes += attribute }
        val down = staticAttributes.toMutableSet()
        if (attacker.skills[203]?.and(255)?.let { it != 255 } == true) {
            var threshold = 60
            BattleAttribute.entries.forEach { attribute ->
                if (attribute !in staticAttributes && probabilityResolver.random100() > threshold) down += attribute
                threshold += 5
            }
        }
        return AttackStatusBatch(statuses, down)
    }

    /** `_attack3` setCharInfoBykey application of a precomputed attack batch. */
    private fun applyIncomingAttackStatuses(batch: AttackStatusBatch, target: BattleUnit) {
        val newlyApplied = batch.statuses.filterTo(linkedSetOf()) { it !in target.statuses }
        batch.statuses.forEach { status -> target.statuses[status] = statusDuration(status, target) }
        if (target.skills[42]?.and(255)?.let { it != 255 } == true) newlyApplied.forEach(target.statuses::remove)
        if (target.skills[122]?.and(255)?.let { it != 255 } != true) {
            batch.downAttributes.forEach { attribute ->
                target.applyAttributeLift(attribute, -1, 3)
            }
        }
        target.presentation.refreshStatus(target.statuses, target.attributeLifts)
    }

    /** Resolves one physical target, including every target-local secondary effect. */
    private fun resolvePhysicalTarget(
        attacker: BattleUnit,
        target: BattleUnit,
        resolvedHarm: Int,
        statuses: AttackStatusBatch,
        activeAttack: Boolean,
    ): PhysicalAttackTargetResult {
        val targetXBefore = target.tileX
        val targetYBefore = target.tileY
        val statusesBefore = target.statuses.toMap()
        val liftsBefore = target.attributeLifts.toMap()
        val liftRoundsBefore = target.attributeLiftRounds.toMap()
        var n = resolvedHarm.coerceAtLeast(0)
        val blockRetaliations = mutableListOf<BattlePhysicalCallbackPlan.BlockRetaliation>()
        var mpShieldDamage = 0
        var moneyShieldSpent = 0
        var hpDamage = 0
        var lifeStealHealing = 0
        var qxlHealing = 0
        var playerMoneyDelta = 0
        var enemyMoneyDelta = 0

        if (n == 0) {
            target.skills[153]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                val harm = attacker.maxHitPoints * rate / 100
                attacker.addHpcur(-harm)
                attacker.statuses[BattleStatus.CONFUSION] = statusDuration(BattleStatus.CONFUSION, attacker)
                blockRetaliations += BattlePhysicalCallbackPlan.BlockRetaliation(
                    BattlePhysicalCallbackPlan.BlockRetaliationKind.MENG_JI_CONFUSION,
                    harm,
                )
            }
            target.skills[161]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                val harm = attacker.maxHitPoints * rate / 100
                attacker.addHpcur(-harm)
                attacker.statuses[BattleStatus.PARALYSIS] = statusDuration(BattleStatus.PARALYSIS, attacker)
                blockRetaliations += BattlePhysicalCallbackPlan.BlockRetaliation(
                    BattlePhysicalCallbackPlan.BlockRetaliationKind.NI_FAN_PARALYSIS,
                    harm,
                )
            }
            attacker.presentation.refreshStatus(attacker.statuses, attacker.attributeLifts)
        } else {
            // `_attack3` records incoming statuses before its MPFY/HP branch.
            applyIncomingAttackStatuses(statuses, target)
            if (target.skills[2]?.and(255)?.let { it != 255 } == true && target.magicPoints > 0) {
                n = n.coerceIn(0, target.magicPoints)
                mpShieldDamage = n
                target.addMpcur(-n)
                // MPFY's break skips JQFY, HP, XXGJ, QXL and XSJQ.
            } else {
                target.skills[125]?.and(255)?.takeIf { it != 255 }?.let { costPerDamage ->
                    if (target.hitPoints >= costPerDamage) {
                        val price = kotlin.math.abs(n) * costPerDamage
                        val available = if (target.isPlayerSide()) playerMoney else enemyMoney
                        if (available >= price) {
                            if (target.isPlayerSide()) playerMoney -= price else enemyMoney -= price
                            moneyShieldSpent = price
                            n = 1
                        }
                    }
                }
                n = n.coerceIn(0, target.hitPoints)
                hpDamage = n
                target.addHpcur(-n)

                attacker.skills[238]?.and(255)?.takeIf { it != 255 }?.let { rate ->
                    var resolvedRate = rate
                    if (!canAttack(attacker, target)) resolvedRate /= 2
                    var healing = resolvedRate * n / 100
                    val attackerIsMine = attacker.isPlayerSide()
                    val currentCampIsMine = activeFaction.isPlayerSide()
                    if (attackerIsMine != currentCampIsMine) healing = minOf(rate, healing)
                    lifeStealHealing = minOf(attacker.maxHitPoints - attacker.hitPoints, healing)
                    attacker.addHpcur(lifeStealHealing)
                }
                attacker.skills[298]?.and(255)?.takeIf { it != 255 }?.let {
                    qxlHealing = minOf(attacker.maxHitPoints - attacker.hitPoints, n)
                    attacker.addHpcur(qxlHealing)
                }
                attacker.skills[237]?.and(255)?.takeIf { it != 255 }?.let { effect ->
                    val amount = n * effect
                    if (amount >= 1) {
                        if (attacker.isPlayerSide()) {
                            playerMoney += amount
                            enemyMoney -= amount
                            playerMoneyDelta = amount
                            enemyMoneyDelta = -amount
                        } else {
                            playerMoney -= amount
                            enemyMoney += amount
                            playerMoneyDelta = -amount
                            enemyMoneyDelta = amount
                        }
                    }
                }
            }

            if (attacker.skills[221]?.and(255)?.let { it != 255 } == true) {
                backPosition(target, attacker)?.let { (x, y) ->
                    target.tileX = x
                    target.tileY = y
                }
            }
            accumulateChargeWhenHit(target, activeAttack)
        }

        // The hurt/guard callback is the first externally committed effect
        // for this target. ZDSY inventory/property callbacks must remain
        // behind it in BattleActionTransaction's staged list.
        notifyPhysicalDamage(attacker, target, n)

        val recoilDamage = target.skills[40]?.and(255)?.takeIf { it != 255 && n > 0 }
            ?.let { n * it / 100 }
            ?.takeIf { it >= 1 }
            ?: 0
        if (recoilDamage > 0) attacker.addHpcur(-recoilDamage, keepAlive = true)

        // Guard case 2 jumps directly to case 8 in `_attack3`; it never
        // enters the ZDSY cases 5..6 even if the defender was already hurt.
        var automaticPropertyId: Int? = null
        var automaticPropertyHpDelta = 0
        var automaticPropertyMpDelta = 0
        var automaticPropertyCallbackCount = 0
        val automaticProperty = if (n > 0) {
            target.skills[284]?.and(255)?.takeIf { itemId ->
                itemId != 255 && target.hitPoints > 0 && target.hitPoints < target.maxHitPoints
            }?.let { itemId ->
                automaticPropertyId = itemId
                val hpBeforeProperty = target.hitPoints
                val mpBeforeProperty = target.magicPoints
                if (target.faction == Faction.PLAYER && zdsyGlobalValue == 0) {
                    notifyConsumeAutomaticProperty(itemId)
                    automaticPropertyCallbackCount++
                }
                propertyItems[itemId]?.let { item -> applyProperty(item, target) { true } }.also {
                    automaticPropertyHpDelta = target.hitPoints - hpBeforeProperty
                    automaticPropertyMpDelta = target.magicPoints - mpBeforeProperty
                    if (it != null && propertyItems[itemId]?.itemType in setOf(42, 43)) automaticPropertyCallbackCount++
                }
            }
        } else {
            null
        }

        val defeated = target.hitPoints <= 0
        if (defeated) {
            battlefield.defeat(target.id)
            notifyUnitDefeated(attacker, target)
        }
        val backMove = if (target.tileX != targetXBefore || target.tileY != targetYBefore) {
            PhysicalBackMove(targetXBefore, targetYBefore, target.tileX, target.tileY)
        } else null
        val localStatusSettlement = if (n > 0 &&
            (statuses.statuses.isNotEmpty() || statuses.downAttributes.isNotEmpty())
        ) {
            MagicLocalSettlement(listOf(MagicLocalSettlementEntry(
                targetId = target.id,
                statusesBefore = statusesBefore,
                statusesAfter = target.statuses.toMap(),
                attributeLiftsBefore = liftsBefore,
                attributeLiftsAfter = target.attributeLifts.toMap(),
                hasStatesPayload = true,
                attributeLiftRoundsBefore = liftRoundsBefore,
                attributeLiftRoundsAfter = target.attributeLiftRounds.toMap(),
            )))
        } else MagicLocalSettlement(emptyList())
        return PhysicalAttackTargetResult(
            targetId = target.id,
            resolvedHarm = n,
            damage = hpDamage,
            mpShieldDamage = mpShieldDamage,
            moneyShieldSpent = moneyShieldSpent,
            lifeStealHealing = lifeStealHealing,
            qxlHealing = qxlHealing,
            recoilDamage = recoilDamage,
            blockRetaliations = blockRetaliations,
            playerMoneyDelta = playerMoneyDelta,
            enemyMoneyDelta = enemyMoneyDelta,
            automaticPropertyId = automaticPropertyId,
            automaticProperty = automaticProperty,
            automaticPropertyHpDelta = automaticPropertyHpDelta,
            automaticPropertyMpDelta = automaticPropertyMpDelta,
            automaticPropertyCallbackCount = automaticPropertyCallbackCount,
            backMove = backMove,
            localStatusSettlement = localStatusSettlement,
            hasLocalStatusSettlement = localStatusSettlement.entries.isNotEmpty(),
            defeated = defeated,
        )
    }

}

private fun BattleStatus.label(): String = when (this) {
    BattleStatus.PARALYSIS -> "마비"
    BattleStatus.SILENCE -> "금주"
    BattleStatus.CONFUSION -> "혼란"
    BattleStatus.POISON -> "중독"
    BattleStatus.LOST -> "길 잃음"
}

private fun BattleAttribute.label(): String = when (this) {
    BattleAttribute.ATTACK -> "공격력"
    BattleAttribute.DEFENSE -> "방어력"
    BattleAttribute.SPIRIT -> "정신력"
    BattleAttribute.CRITICAL -> "폭발력"
    BattleAttribute.MORALE -> "사기"
    BattleAttribute.MOVEMENT -> "이동력"
}
