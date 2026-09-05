package com.jojo.game

internal data class PlannedScenarioMovement(
    val request: ScenarioCommand.MoveUnit,
    val unit: TacticalUnit,
    val path: List<Pair<Int, Int>>,
)

/** Owns authored movement path selection and callback-duration rules. */
internal class ScenarioStageMovementPlanner {
    var hallPathGrid: HallPathGrid? = null
    var battleMovementTimeline: Boolean = false
    var battleMovePathResolver: ((Int, Int, Int) -> List<Pair<Int, Int>>?)? = null

    fun countDirection(fromId: Int, toId: Int, unitProvider: (Int) -> TacticalUnit): Int {
        val from = unitProvider(fromId)
        val to = unitProvider(toId)
        if (fromId == toId) return from.direction
        val dx = kotlin.math.abs(to.x - from.x)
        return if (kotlin.math.abs(to.y - from.y) > dx) {
            if (from.y > to.y) 0 else 2
        } else if (from.x > to.x) 3 else 1
    }

    fun duration(path: List<Pair<Int, Int>>): Float {
        val edges = (path.size - 1).coerceAtLeast(0)
        if (edges == 0) return 0f
        return if (battleMovementTimeline) edges * 0.08f + 0.1f else edges * 0.04f
    }

    fun pathFor(id: Int, x: Int, y: Int, units: Map<Int, TacticalUnit>): List<Pair<Int, Int>>? {
        val unit = units[id] ?: return null
        if (!unit.visible) return null
        battleMovePathResolver?.let { return it(id, x, y) }
        return pathFor(unit, x, y, units.values.map { it.x to it.y }.toSet())
    }

    fun plan(requests: List<ScenarioCommand.MoveUnit>, units: Map<Int, TacticalUnit>): List<PlannedScenarioMovement> {
        val occupiedOrigins = units.values.map { it.x to it.y }.toSet()
        return requests.mapNotNull { request ->
            val moving = units[request.unitId] ?: return@mapNotNull null
            if (!moving.visible) return@mapNotNull null
            val path = battleMovePathResolver?.invoke(moving.id, request.x, request.y)
                ?: if (battleMovePathResolver == null) pathFor(moving, request.x, request.y, occupiedOrigins) else null
            path?.let { PlannedScenarioMovement(request, moving, it) }
        }
    }

    private fun pathFor(
        unit: TacticalUnit,
        x: Int,
        y: Int,
        occupied: Set<Pair<Int, Int>>,
    ): List<Pair<Int, Int>>? = HallPathfinder.find(
        unit.x,
        unit.y,
        x.coerceIn(0, 99),
        y.coerceIn(0, 99),
        hallPathGrid,
        occupied,
    )
}
