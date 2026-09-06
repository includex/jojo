// Battle
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

/**
 * `BattleAiCoordinatorEnvironment` 클래스: ai 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

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
 * `BattleAiCoordinator` 싱글턴 객체: ai 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal object BattleAiCoordinator {

    /**
     * `resolveAiTurn`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun resolveAiTurn(
        maxUnits: Int = Int.MAX_VALUE,
        deferMutations: Boolean = false,
        env: BattleAiCoordinatorEnvironment,
    ): AiTurnResult {
        /**
         * `turnEnv` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val turnEnv = createTurnEnvironment(env)
        return BattleAiTurnResolver.resolveAiTurn(maxUnits, deferMutations, turnEnv)
    }

    /**
     * `traceAiPlannerAtCurrentPoint`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun traceAiPlannerAtCurrentPoint(
        characterId: Int,
        aiFlags: Int = 1,
        env: BattleAiCoordinatorEnvironment,
    ): AiPlannerTrace? {
        /**
         * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unit = env.units().values.firstOrNull { it.visible && it.characterId == characterId } ?: return null
        /**
         * `value` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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

    /**
     * `previewAiAttackValue`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun previewAiAttackValue(
        attackerId: String,
        targetId: String,
        env: BattleAiCoordinatorEnvironment,
    ): Int {
        /**
         * `attacker` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attacker = env.units()[attackerId] ?: return 0
        /**
         * `target` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val target = env.units()[targetId] ?: return 0
        return BattleAiScorer.estimatedAttackValue(attacker, target, createScoringEnvironment(env))
    }


    /**
     * `createScoringEnvironment`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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


    /**
     * `createDecisionEnvironment`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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


    /**
     * `createControllerEnvironment`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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


    /**
     * `createTurnEnvironment`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
