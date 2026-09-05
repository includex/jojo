package com.jojo.game

/** Applies movement plans and advances their Hall/Battle visual timelines. */
internal class ScenarioStageUnitMovementAnimator {
    fun begin(
        unit: TacticalUnit,
        path: List<Pair<Int, Int>>,
        requestedX: Int,
        requestedY: Int,
        direction: Int,
        duration: Float,
        onScriptedDirection: (Pair<Int, Int>) -> Unit,
    ) {
        val id = unit.id
        unit.moveFromX = unit.visualX
        unit.moveFromY = unit.visualY
        unit.moveElapsed = 0f
        unit.animationElapsed = 0f
        unit.moveDuration = duration
        unit.movePath = path
        unit.moveZIndex = 4f * (unit.visualX + unit.visualY) - 424f
        unit.moveFinalDirection = direction
        unit.moveJustStarted = duration > 0f
        val destination = path.lastOrNull() ?: (requestedX to requestedY)
        unit.moveToX = destination.first.coerceIn(0, 99)
        unit.moveToY = destination.second.coerceIn(0, 99)
        if (duration <= 0f) {
            unit.x = unit.moveToX
            unit.y = unit.moveToY
            unit.visualX = unit.x.toFloat()
            unit.visualY = unit.y.toFloat()
            unit.action = 0
            unit.direction = direction
            onScriptedDirection(id to direction)
        } else {
            unit.action = 20
            path.getOrNull(1)?.let { next ->
                unit.direction = HallPathfinder.direction(path[0].first, path[0].second, next.first, next.second)
            }
            onScriptedDirection(id to direction)
        }
    }

    fun update(delta: Float, units: Map<Int, TacticalUnit>, battleTimeline: Boolean) {
        val elapsedDelta = delta.coerceAtLeast(0f)
        units.values.filter { it.moveDuration > 0f }.forEach { unit ->
            if (unit.moveJustStarted) unit.moveJustStarted = false
            unit.moveElapsed = (unit.moveElapsed + elapsedDelta).coerceAtMost(unit.moveDuration)
            val sample = if (battleTimeline) {
                val timeline = BattleUnitMoveTimeline.schedule(unit.movePath, fastMove = true)
                val point = BattleUnitMoveTimeline.sample(unit.movePath, timeline, unit.moveElapsed)
                ScenarioMovementSample(point.x, point.y, point.direction, 4f * (point.x + point.y) - 424f)
            } else {
                val point = HallMoveTimeline.sample(unit.movePath, unit.moveElapsed)
                ScenarioMovementSample(point.x, point.y, point.direction, point.zIndex)
            }
            unit.visualX = sample.x
            unit.visualY = sample.y
            unit.moveZIndex = sample.zIndex
            val nextDirection = sample.direction.takeIf { it >= 0 } ?: unit.direction
            if (nextDirection != unit.direction) unit.animationElapsed = 0f else unit.animationElapsed += elapsedDelta
            unit.direction = nextDirection
            if (unit.moveElapsed >= unit.moveDuration) finish(unit, refreshZIndex = true)
        }
    }

    fun finish(units: Map<Int, TacticalUnit>) = units.values.forEach { finish(it, refreshZIndex = false) }

    private fun finish(unit: TacticalUnit, refreshZIndex: Boolean) {
        if (unit.moveDuration > 0f) {
            unit.x = unit.moveToX
            unit.y = unit.moveToY
        }
        unit.visualX = unit.x.toFloat()
        unit.visualY = unit.y.toFloat()
        if (refreshZIndex) unit.moveZIndex = 4f * (unit.visualX + unit.visualY) - 424f
        unit.moveDuration = 0f
        unit.action = if (unit.movePath.isNotEmpty()) 0 else unit.action
        if (unit.movePath.isNotEmpty()) unit.direction = unit.moveFinalDirection
    }
}

private data class ScenarioMovementSample(val x: Float, val y: Float, val direction: Int, val zIndex: Float)
