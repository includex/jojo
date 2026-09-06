// Game
package com.jojo.game.application.scenario

import com.jojo.game.infrastructure.data.HallPathGrid

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

import com.jojo.game.domain.scenario.ScenarioHead
import com.jojo.game.domain.scenario.TacticalUnit

internal class ScenarioStageMovementCoordinator {
    private val planner = ScenarioStageMovementPlanner()
    private val unitAnimator = ScenarioStageUnitMovementAnimator()
    private val headCoordinator = ScenarioStageHeadCoordinator()

    var hallPathGrid: HallPathGrid?
        get() = planner.hallPathGrid
        set(value) {
            planner.hallPathGrid = value
        }
    var battleMovementTimeline: Boolean
        get() = planner.battleMovementTimeline
        set(value) {
            planner.battleMovementTimeline = value
        }
    var battleMovePathResolver: ((Int, Int, Int) -> List<Pair<Int, Int>>?)?
        get() = planner.battleMovePathResolver
        set(value) {
            planner.battleMovePathResolver = value
        }
    val heads: MutableMap<Int, ScenarioHead> get() = headCoordinator.heads

    fun head(id: Int): ScenarioHead = headCoordinator.head(id)
    fun moveHead(id: Int, x: Int, y: Int): Float = headCoordinator.move(id, x, y)
    fun showHead(id: Int, x: Int, y: Int): Float = headCoordinator.show(id, x, y)
    fun hideHead(id: Int): Float = headCoordinator.hide(id)

    fun countDirection(fromId: Int, toId: Int, unitProvider: (Int) -> TacticalUnit): Int =
        planner.countDirection(fromId, toId, unitProvider)

    fun moveDuration(id: Int, x: Int, y: Int, units: Map<Int, TacticalUnit>): Float =
        planner.pathFor(id, x, y, units)?.let(planner::duration) ?: 0f

    fun moveDuration(path: List<Pair<Int, Int>>): Float = planner.duration(path)

    fun movePath(id: Int, x: Int, y: Int, units: Map<Int, TacticalUnit>): List<Pair<Int, Int>>? =
        planner.pathFor(id, x, y, units)

    fun moveUnit(
        id: Int,
        x: Int,
        y: Int,
        direction: Int,
        units: Map<Int, TacticalUnit>,
        onScriptedDirection: (Pair<Int, Int>) -> Unit,
    ) {
        val unit = units[id] ?: return
        val path = planner.pathFor(id, x, y, units) ?: return
        unitAnimator.begin(unit, path, x, y, direction, planner.duration(path), onScriptedDirection)
    }

    fun moveUnits(
        requests: List<ScenarioCommand.MoveUnit>,
        units: Map<Int, TacticalUnit>,
        onScriptedDirection: (Pair<Int, Int>) -> Unit,
    ): Float {
        val planned = planner.plan(requests, units)
        planned.forEach { movement ->
            unitAnimator.begin(
                movement.unit,
                movement.path,
                movement.request.x,
                movement.request.y,
                movement.request.direction,
                planner.duration(movement.path),
                onScriptedDirection,
            )
        }
        return planned.maxOfOrNull { planner.duration(it.path) } ?: 0f
    }

    fun updateAnimations(delta: Float, units: Map<Int, TacticalUnit>) {
        unitAnimator.update(delta, units, battleMovementTimeline)
        headCoordinator.update(delta)
    }

    fun finishAnimations(units: Map<Int, TacticalUnit>) {
        unitAnimator.finish(units)
        headCoordinator.finish()
    }
}
