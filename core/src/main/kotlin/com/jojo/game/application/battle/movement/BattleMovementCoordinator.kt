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
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleMovementPlanner
import com.jojo.game.domain.battle.BattleAttributeCalculator

internal data class BattleMovementEnvironment(
    val units: () -> Map<String, BattleUnit>,
    val unitAt: (Int, Int) -> BattleUnit?,
    val activeFaction: () -> Faction,
    val weather: () -> BattleWeather,
    val terrain: BattleTerrainGrid?,
    val blockedTiles: Set<Pair<Int, Int>>,
    val movementPlanner: BattleMovementPlanner<BattleUnit>,
    val allPresentationUnits: () -> Collection<BattleUnit>,
    val isBattleEnded: () -> Boolean,
    val onMoveExecuted: (id: String, path: List<Pair<Int, Int>>, nodes: Int) -> Unit,
)

internal object BattleMovementCoordinator {
    private const val DEFAULT_TERRAIN_SIZE = 100


    fun distance(a: BattleUnit, b: BattleUnit): Int =
        kotlin.math.abs(a.tileX - b.tileX) + kotlin.math.abs(a.tileY - b.tileY)


    fun facingDirection(fromX: Int, fromY: Int, toX: Int, toY: Int): Int {
        val dx = kotlin.math.abs(toX - fromX)
        val dy = kotlin.math.abs(toY - fromY)
        return if (dy > dx) {
            if (fromY > toY) 0 else 2
        } else if (fromX > toX) 3 else 1
    }


    fun isInsideDefaultTerrainBounds(point: Pair<Int, Int>, terrain: BattleTerrainGrid?): Boolean =
        point.first >= 0 && point.second >= 0 &&
                point.first < (terrain?.width ?: DEFAULT_TERRAIN_SIZE) &&
                point.second < (terrain?.height ?: DEFAULT_TERRAIN_SIZE)

    fun backPosition(
        defender: BattleUnit,
        attacker: BattleUnit,
        terrain: BattleTerrainGrid?,
        blockedTiles: Set<Pair<Int, Int>>,
        unitAt: (Int, Int) -> BattleUnit?,
    ): Pair<Int, Int>? {
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
        val point = Pair(defender.tileX + dx, defender.tileY + dy)
        if (point.first < 0 || point.second < 0) return null
        if (terrain?.let { point.first >= it.width || point.second >= it.height } == true) return null
        if (point in blockedTiles || unitAt(point.first, point.second) != null) return null
        val terrainId = terrain?.terrainAt(point.first, point.second)
        if (terrainId?.let { defender.terrainMovementCosts[it] ?: 255 } ?: 1 >= 255) return null
        return point
    }


    fun movementRules(unit: BattleUnit): BattleMovementPlanner.MovementRules {
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

    fun movePoints(
        unit: BattleUnit,
        movement: Int,
        planner: BattleMovementPlanner<BattleUnit>,
        ignoredEnemyId: String? = null,
        startOverride: Pair<Int, Int>? = null,
    ): BattleMovementPlanner.MovePoints = planner.movePoints(
        actor = unit,
        movement = movement,
        rules = movementRules(unit),
        ignoredEnemyId = ignoredEnemyId,
        startOverride = startOverride ?: (unit.tileX to unit.tileY),
    )

    fun findMovementPath(
        unit: BattleUnit,
        targetX: Int,
        targetY: Int,
        planner: BattleMovementPlanner<BattleUnit>,
        avoidEnemies: Boolean = false,
        penalizeEnemyTiles: Boolean = false,
        allowEnemyOnTarget: Boolean = false,
    ): List<Pair<Int, Int>>? =
        planner.findPath(
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

    fun findReachableEmptyPosition(
        unit: BattleUnit,
        seed: Pair<Int, Int>,
        reachable: Set<Pair<Int, Int>>,
        planner: BattleMovementPlanner<BattleUnit>,
        terrain: BattleTerrainGrid?,
    ): Pair<Int, Int>? =
        planner.findEmptyPosition(unit, seed, reachable) { isInsideDefaultTerrainBounds(it, terrain) }

    fun scriptedMovePath(
        characterId: Int,
        targetX: Int,
        targetY: Int,
        presentationUnits: Collection<BattleUnit>,
        planner: BattleMovementPlanner<BattleUnit>,
        terrain: BattleTerrainGrid?,
    ): List<Pair<Int, Int>>? {
        val unit = presentationUnits.firstOrNull { it.characterId == characterId } ?: return null
        val clamped = targetX.coerceIn(0, (terrain?.width ?: 100) - 1) to
                targetY.coerceIn(0, (terrain?.height ?: 100) - 1)
        val destination = planner.findScriptedDestination(unit, clamped) { isInsideDefaultTerrainBounds(it, terrain) }
        return destination?.let { findMovementPath(unit, it.first, it.second, planner) }
    }

    fun reachableTiles(
        id: String,
        env: BattleMovementEnvironment,
    ): Map<Pair<Int, Int>, Int> {
        val unit = env.units()[id] ?: return emptyMap()
        if (!unit.visible || BattleStatus.PARALYSIS in unit.statuses || unit.hasMoved || unit.hasActed) return emptyMap()
        val movement = BattleAttributeCalculator.finalMovement(unit, env.weather())
        return movePoints(unit, movement, env.movementPlanner).points
            .mapValuesTo(linkedMapOf()) { (_, point) -> movement - point.remaining }
    }

    fun canEnterTilesIgnoringEnemyWithinMoves(
        id: String,
        ignoredEnemyId: String,
        start: Pair<Int, Int>,
        targetTiles: Set<Pair<Int, Int>>,
        moves: Int = 2,
        env: BattleMovementEnvironment,
    ): Boolean {
        val unit = env.units()[id] ?: return false
        if (!unit.visible || targetTiles.isEmpty() || moves < 1) return false
        val movement = BattleAttributeCalculator.finalMovement(unit, env.weather())
        var frontier = linkedSetOf(start)
        repeat(moves) {
            val next = linkedSetOf<Pair<Int, Int>>()
            frontier.forEach { origin ->
                movePoints(unit, movement, env.movementPlanner, ignoredEnemyId, origin).points.keys.forEach { tile ->
                    val occupant = env.unitAt(tile.first, tile.second)
                    if (tile == origin || occupant == null || occupant.id == ignoredEnemyId) next += tile
                }
            }
            if (next.any { it in targetTiles }) return true
            frontier = next
            if (frontier.isEmpty()) return false
        }
        return false
    }

    fun moveUnit(
        id: String,
        targetX: Int,
        targetY: Int,
        maxDistance: Int? = null,
        env: BattleMovementEnvironment,
    ): TacticalActionResult {
        if (env.isBattleEnded()) return TacticalActionResult.Rejected("전투가 종료되었습니다.")
        val unit = env.units()[id] ?: return TacticalActionResult.Rejected("유닛이 없습니다.")
        if (!unit.visible) return TacticalActionResult.Rejected("아직 등장하지 않은 유닛입니다.")
        if (BattleStatus.PARALYSIS in unit.statuses || BattleStatus.CONFUSION in unit.statuses) return TacticalActionResult.Rejected(
            "행동할 수 없는 상태입니다."
        )
        if (unit.effectiveFaction() != env.activeFaction()) return TacticalActionResult.Rejected("현재 진영의 유닛만 조작할 수 있습니다.")
        if (unit.hasActed) return TacticalActionResult.Rejected("이미 행동한 유닛입니다.")
        if (unit.hasMoved) return TacticalActionResult.Rejected("이미 이동한 유닛입니다.")
        if (targetX < 0 || targetY < 0 || env.terrain?.let { targetX >= it.width || targetY >= it.height } == true) {
            return TacticalActionResult.Rejected("맵 밖으로 이동할 수 없습니다.")
        }
        if (targetX to targetY in env.blockedTiles) return TacticalActionResult.Rejected("장애물이 있는 칸입니다.")
        if (env.unitAt(targetX, targetY) != null) return TacticalActionResult.Rejected("다른 유닛이 있는 칸입니다.")
        val route = movePoints(
            unit,
            maxDistance ?: BattleAttributeCalculator.finalMovement(unit, env.weather()),
            env.movementPlanner
        )
        val destination = targetX to targetY
        if (destination !in route.points) return TacticalActionResult.Rejected("이동 범위를 벗어났습니다.")
        val path = route.pathTo(destination)
        val nodes = path.size
        path.getOrNull(1)?.let { first ->
            unit.direction = facingDirection(unit.tileX, unit.tileY, first.first, first.second)
        }
        unit.tileX = targetX
        unit.tileY = targetY
        unit.hasMoved = true
        env.onMoveExecuted(id, path, nodes)
        return TacticalActionResult.Success
    }

    fun moveToward(
        unit: BattleUnit,
        goalX: Int,
        goalY: Int,
        env: BattleMovementEnvironment,
    ): Boolean {
        val candidates = movePoints(
            unit,
            BattleAttributeCalculator.finalMovement(unit, env.weather()),
            env.movementPlanner
        ).points.keys
            .asSequence()
            .filter { it != (unit.tileX to unit.tileY) }
            .sortedBy { kotlin.math.abs(goalX - it.first) + kotlin.math.abs(goalY - it.second) }
        val target = candidates.firstOrNull { (x, y) -> (x to y) !in env.blockedTiles && env.unitAt(x, y) == null }
            ?: return false
        return moveUnit(unit.id, target.first, target.second, null, env) is TacticalActionResult.Success
    }
}
