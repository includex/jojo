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

/** Owns movement planning dependencies and all movement queries for a battle. */
class BattleMovementQueryFacade internal constructor(
    private val configuration: BattleConfiguration,
    private val journal: BattleStateJournal,
    private val battlefield: Battlefield,
    private val units: () -> Map<String, BattleUnit>,
    private val activeFaction: () -> Faction,
    private val weather: () -> BattleWeather,
    private val isBattleEnded: () -> Boolean,
    areAllied: (Faction, Faction) -> Boolean,
) {
    private val orderedOffsets = buildList {
        val sourceOrder = listOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
        sourceOrder.filterTo(this) { it in configuration.movementOffsets }
        configuration.movementOffsets.filterTo(this) { it !in sourceOrder }
    }

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

    fun reachableTiles(id: String): Map<Pair<Int, Int>, Int> =
        BattleMovementCoordinator.reachableTiles(id, environment)

    fun canEnterTilesIgnoringEnemyWithinMoves(
        id: String,
        ignoredEnemyId: String,
        start: Pair<Int, Int>,
        targetTiles: Set<Pair<Int, Int>>,
        moves: Int = 2,
    ): Boolean = BattleMovementCoordinator.canEnterTilesIgnoringEnemyWithinMoves(
        id, ignoredEnemyId, start, targetTiles, moves, environment,
    )

    fun moveUnit(id: String, targetX: Int, targetY: Int, maxDistance: Int? = null): TacticalActionResult =
        BattleMovementCoordinator.moveUnit(id, targetX, targetY, maxDistance, environment)

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

    fun scriptedMovePath(characterId: Int, targetX: Int, targetY: Int): List<Pair<Int, Int>>? =
        BattleMovementCoordinator.scriptedMovePath(
            characterId, targetX, targetY, battlefield.allPresentationUnits(), planner, configuration.terrain,
        )

    fun findReachableEmptyPosition(
        unit: BattleUnit,
        seed: Pair<Int, Int>,
        reachable: Set<Pair<Int, Int>>,
    ): Pair<Int, Int>? = BattleMovementCoordinator.findReachableEmptyPosition(
        unit, seed, reachable, planner, configuration.terrain,
    )

    fun backPosition(defender: BattleUnit, attacker: BattleUnit, unitAt: (Int, Int) -> BattleUnit?): Pair<Int, Int>? =
        BattleMovementCoordinator.backPosition(
            defender, attacker, configuration.terrain, journal.blockedTiles(), unitAt,
        )

    fun facingDirection(fromX: Int, fromY: Int, toX: Int, toY: Int): Int =
        BattleMovementCoordinator.facingDirection(fromX, fromY, toX, toY)

    internal fun movePoints(
        unit: BattleUnit,
        movement: Int,
        ignoredEnemyId: String?,
        startOverride: Pair<Int, Int>?,
    ): BattleMovementPlanner.MovePoints = BattleMovementCoordinator.movePoints(
        unit, movement, planner, ignoredEnemyId, startOverride,
    )
}
