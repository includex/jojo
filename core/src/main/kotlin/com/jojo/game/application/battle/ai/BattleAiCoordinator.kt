package com.jojo.game.application.battle.ai

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.combat.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.BattleAiScorer
import com.jojo.game.domain.battle.BattleMovementPlanner
import com.jojo.game.domain.battle.BattleProbabilityResolver
import com.jojo.game.domain.battle.BattleRateGauge

internal data class BattleAiCoordinatorEnvironment(
    val units: () -> Map<String, BattleUnit>,
    val unitAt: (Int, Int) -> BattleUnit?,
    val areAllied: (BattleUnit, BattleUnit) -> Boolean,
    val weather: () -> BattleWeather,
    val round: () -> Int,
    val terrain: BattleTerrainGrid?,
    val terrainResumeRates: Map<Int, Int>,
    val terrainMagicFlags: Map<Int, Int>,
    val probabilityResolver: BattleProbabilityResolver,
    val basePhysicalDamageContext: (BattleUnit, BattleUnit, Boolean) -> BasePhysicalDamageContext,
    val reachableTiles: (String) -> Map<Pair<Int, Int>, Int>,
    val traceActions: MutableList<String>,
    val movementOffsets: Set<Pair<Int, Int>>,
    val enemyMasterUnitId: String?,
    val findMovementPath: (BattleUnit, Int, Int, Boolean, Boolean, Boolean) -> List<Pair<Int, Int>>?,
    val findReachableEmptyPosition: (BattleUnit, Pair<Int, Int>, Set<Pair<Int, Int>>) -> Pair<Int, Int>?,
    val movePoints: (BattleUnit, Int) -> BattleMovementPlanner.MovePoints,
    val outcome: () -> BattleOutcome?,
    val activeFaction: () -> Faction,
    val moveUnit: (String, Int, Int) -> TacticalActionResult,
    val attack: (String, String) -> TacticalActionResult,
    val castMagic: (String, String, Int, Boolean, Boolean) -> TacticalActionResult,
    val lastMovePath: (String) -> List<Pair<Int, Int>>,
    val aiTurnOrder: () -> List<String>?,
    val clearAiTurnOrder: () -> Unit,
    val setLastAiUnitResolution: (AiUnitResolution?) -> Unit,
    val lastAiUnitResolution: () -> AiUnitResolution?,
    val runtimeSnapshot: () -> BattleActionSnapshot,
    val restoreRuntime: (BattleActionSnapshot) -> Unit,
    val setPendingActionTransaction: (BattleActionTransaction?) -> Unit,
    val pendingActionTransaction: () -> BattleActionTransaction?,
    val stagedHitSideEffects: () -> MutableList<() -> Unit>?,
    val setStagedHitSideEffects: (MutableList<() -> Unit>?) -> Unit,
    val stagedCompletionSideEffects: () -> MutableList<() -> Unit>?,
    val setStagedCompletionSideEffects: (MutableList<() -> Unit>?) -> Unit,
    val createActionTransaction: (String, BattleActionSnapshot, BattleActionSnapshot, List<() -> Unit>, List<() -> Unit>) -> BattleActionTransaction,
)

/**
 * Coordinates AI turn resolution, scoring, controller execution, and decision planning.
 */
internal object BattleAiCoordinator {

    fun resolveAiTurn(
        maxUnits: Int = Int.MAX_VALUE,
        deferMutations: Boolean = false,
        env: BattleAiCoordinatorEnvironment,
    ): AiTurnResult {
        val turnEnv = createTurnEnvironment(env)
        return BattleAiTurnResolver.resolveAiTurn(maxUnits, deferMutations, turnEnv)
    }

    fun traceAiPlannerAtCurrentPoint(
        characterId: Int,
        aiFlags: Int = 1,
        env: BattleAiCoordinatorEnvironment,
    ): AiPlannerTrace? {
        val unit = env.units().values.firstOrNull { it.visible && it.characterId == characterId } ?: return null
        val value = BattleAiScorer.cocosAiBaseValueAt(
            unit, unit.tileX, unit.tileY, env.units().values, env.terrain, env.terrainResumeRates, env.areAllied,
        )
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

    fun previewAiAttackValue(
        attackerId: String,
        targetId: String,
        env: BattleAiCoordinatorEnvironment,
    ): Int {
        val attacker = env.units()[attackerId] ?: return 0
        val target = env.units()[targetId] ?: return 0
        return BattleAiScorer.estimatedAttackValue(attacker, target, createScoringEnvironment(env))
    }


    fun createScoringEnvironment(env: BattleAiCoordinatorEnvironment): BattleAiScoringEnvironment =
        BattleAiScoringEnvironment(
            units = { env.units().values },
            unitAt = env.unitAt,
            areAllied = env.areAllied,
            weather = env.weather,
            terrain = env.terrain,
            terrainMagicFlags = env.terrainMagicFlags,
            probabilityResolver = env.probabilityResolver,
            basePhysicalDamageContext = env.basePhysicalDamageContext,
        )


    fun createDecisionEnvironment(env: BattleAiCoordinatorEnvironment): BattleAiDecisionEnvironment =
        BattleAiDecisionEnvironment(
            scoringEnv = createScoringEnvironment(env),
            reachableTiles = env.reachableTiles,
            terrainResumeRates = env.terrainResumeRates,
            weather = env.weather,
            round = env.round,
            onRecordDiagnostic = { env.traceActions += it },
            hasDiagnosticEntry = { prefix -> env.traceActions.any { it.startsWith(prefix) } },
        )


    fun createControllerEnvironment(env: BattleAiCoordinatorEnvironment): BattleAiControllerEnvironment =
        BattleAiControllerEnvironment(
            units = { env.units().values },
            unitAt = env.unitAt,
            areAllied = env.areAllied,
            movementOffsets = env.movementOffsets,
            terrain = env.terrain,
            terrainResumeRates = env.terrainResumeRates,
            enemyMasterUnitId = env.enemyMasterUnitId,
            reachableTiles = env.reachableTiles,
            findMovementPath = env.findMovementPath,
            findReachableEmptyPosition = env.findReachableEmptyPosition,
            movePoints = env.movePoints,
            weather = env.weather,
            decisionEnv = createDecisionEnvironment(env),
        )


    fun createTurnEnvironment(env: BattleAiCoordinatorEnvironment): BattleAiTurnEnvironment =
        BattleAiTurnEnvironment(
            outcome = env.outcome,
            activeFaction = env.activeFaction,
            round = env.round,
            units = env.units,
            terrain = env.terrain,
            terrainResumeRates = env.terrainResumeRates,
            areAllied = env.areAllied,
            hasAttackCandidate = { attacker, target ->
                linkedSetOf(attacker.tileX to attacker.tileY).apply { addAll(env.reachableTiles(attacker.id).keys) }
                    .any { (x, y) -> BattleAiScorer.canAttackFrom(attacker, x, y, target) }
            },
            moveUnit = env.moveUnit,
            attack = env.attack,
            castMagic = env.castMagic,
            lastMovePath = env.lastMovePath,
            traceActions = env.traceActions,
            aiTurnOrder = env.aiTurnOrder,
            clearAiTurnOrder = env.clearAiTurnOrder,
            setLastAiUnitResolution = env.setLastAiUnitResolution,
            lastAiUnitResolution = env.lastAiUnitResolution,
            runtimeSnapshot = env.runtimeSnapshot,
            restoreRuntime = env.restoreRuntime,
            setPendingActionTransaction = env.setPendingActionTransaction,
            pendingActionTransaction = env.pendingActionTransaction,
            stagedHitSideEffects = env.stagedHitSideEffects,
            setStagedHitSideEffects = env.setStagedHitSideEffects,
            stagedCompletionSideEffects = env.stagedCompletionSideEffects,
            setStagedCompletionSideEffects = env.setStagedCompletionSideEffects,
            createActionTransaction = env.createActionTransaction,
            controllerEnv = createControllerEnvironment(env),
        )
}
