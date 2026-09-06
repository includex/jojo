// Battle
package com.jojo.game.application.battle.movement

import com.jojo.game.domain.battle.*
import com.jojo.game.*
import com.jojo.game.application.battle.*
import com.jojo.game.application.battle.ai.*
import com.jojo.game.application.battle.combat.*
import com.jojo.game.application.battle.experience.*
import com.jojo.game.application.battle.movement.*
import com.jojo.game.application.battle.presentation.*
import com.jojo.game.application.battle.round.*
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.BattleMovementPlanner

/** BattleMovementQueryFacade: 전투 이동 조회 진입점이며, 관련 전투 기능을 묶어 안정적인 호출 경로를 제공한다. */
class BattleMovementQueryFacade internal constructor(
    /**
     * `configuration` (BattleConfiguration,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val configuration: BattleConfiguration,
    /**
     * `journal` (BattleStateJournal,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val journal: BattleStateJournal,
    /**
     * `battlefield` (Battlefield,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val battlefield: Battlefield,
    /**
     * `units` (() -> Map<String, BattleUnit>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val units: () -> Map<String, BattleUnit>,
    /**
     * `activeFaction` (() -> Faction,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val activeFaction: () -> Faction,
    /**
     * `weather` (() -> BattleWeather,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val weather: () -> BattleWeather,
    /**
     * `isBattleEnded` (() -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val isBattleEnded: () -> Boolean,
    areAllied: (Faction, Faction) -> Boolean,
) {
    /**
     * `orderedOffsets` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val orderedOffsets = buildList {
        /**
         * `sourceOrder` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sourceOrder = listOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
        sourceOrder.filterTo(this) { it in configuration.movementOffsets }
        configuration.movementOffsets.filterTo(this) { it !in sourceOrder }
    }

    /**
     * `planner` (BattleMovementPlanner<BattleUnit> by lazy): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    internal val planner: BattleMovementPlanner<BattleUnit> by lazy {
        BattleMovementPlanner<BattleUnit>(
            isInside = { (x, y) ->
                x >= 0 && y >= 0 && configuration.terrain?.let { x < it.width && y < it.height } != false
            },
            terrainCost = { unit, (x, y) ->
                configuration.terrain?.terrainAt(x, y)?.let {
                    unit.terrainMovementCosts[it] ?: 255
                } ?: 1
            },
            isBlocked = { it in journal.blockedTiles() },
            occupantAt = { (x, y) -> battlefield.unitAt(x, y) },
            actorId = BattleUnit::id,
            isSameActor = { left, right -> left === right },
            areAllied = { left, right -> areAllied(left.effectiveFaction(), right.effectiveFaction()) },
            orderedMovementOffsets = orderedOffsets,
            enemyNearOffsets = configuration.movementOffsets,
        )
    }

    /**
     * `environment` (BattleMovementEnvironment by lazy): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val environment: BattleMovementEnvironment by lazy {
        BattleMovementEnvironmentAssembler.build(
            units = units,
            unitAt = battlefield::unitAt,
            activeFaction = activeFaction,
            weather = weather,
            terrain = configuration.terrain,
            blockedTiles = journal.mutableBlockedTiles(),
            movementPlanner = planner,
            allPresentationUnits = battlefield::allPresentationUnits,
            isBattleEnded = isBattleEnded,
            recordMove = journal::recordMove,
        )
    }

    /**
     * `reachableTiles`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun reachableTiles(id: String): Map<Pair<Int, Int>, Int> =
        BattleMovementCoordinator.reachableTiles(id, environment)

    /**
     * `canEnterTilesIgnoringEnemyWithinMoves`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun canEnterTilesIgnoringEnemyWithinMoves(
        id: String,
        ignoredEnemyId: String,
        start: Pair<Int, Int>,
        targetTiles: Set<Pair<Int, Int>>,
        moves: Int = 2,
    ): Boolean = BattleMovementCoordinator.canEnterTilesIgnoringEnemyWithinMoves(
        id, ignoredEnemyId, start, targetTiles, moves, environment,
    )

    /**
     * `moveUnit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun moveUnit(id: String, targetX: Int, targetY: Int, maxDistance: Int? = null): TacticalActionResult =
        BattleMovementCoordinator.moveUnit(id, targetX, targetY, maxDistance, environment)

    /**
     * `findMovementPath`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun findMovementPath(
        unit: BattleUnit,
        targetX: Int,
        targetY: Int,
        avoidEnemies: Boolean,
        penalizeEnemyTiles: Boolean,
        allowEnemyOnTarget: Boolean,
    ): List<Pair<Int, Int>>? = BattleMovementCoordinator.findMovementPath(
        unit, targetX, targetY, planner, avoidEnemies, penalizeEnemyTiles, allowEnemyOnTarget,
    )

    /**
     * `scriptedMovePath`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun scriptedMovePath(characterId: Int, targetX: Int, targetY: Int): List<Pair<Int, Int>>? =
        BattleMovementCoordinator.scriptedMovePath(
            characterId, targetX, targetY, battlefield.allPresentationUnits(), planner, configuration.terrain,
        )

    /**
     * `findReachableEmptyPosition`: 상태나 데이터를 조회한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun findReachableEmptyPosition(
        unit: BattleUnit,
        seed: Pair<Int, Int>,
        reachable: Set<Pair<Int, Int>>,
    ): Pair<Int, Int>? = BattleMovementCoordinator.findReachableEmptyPosition(
        unit, seed, reachable, planner, configuration.terrain,
    )

    /**
     * `backPosition`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun backPosition(defender: BattleUnit, attacker: BattleUnit, unitAt: (Int, Int) -> BattleUnit?): Pair<Int, Int>? =
        BattleMovementCoordinator.backPosition(
            defender, attacker, configuration.terrain, journal.blockedTiles(), unitAt,
        )

    /**
     * `facingDirection`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun facingDirection(fromX: Int, fromY: Int, toX: Int, toY: Int): Int =
        BattleMovementCoordinator.facingDirection(fromX, fromY, toX, toY)

    /**
     * `movePoints`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    internal fun movePoints(
        unit: BattleUnit,
        movement: Int,
        ignoredEnemyId: String?,
        startOverride: Pair<Int, Int>?,
    ): BattleMovementPlanner.MovePoints = BattleMovementCoordinator.movePoints(
        unit, movement, planner, ignoredEnemyId, startOverride,
    )
}
