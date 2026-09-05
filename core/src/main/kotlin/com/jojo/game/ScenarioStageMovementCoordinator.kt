package com.jojo.game

internal class ScenarioStageMovementCoordinator {
    var hallPathGrid: HallPathGrid? = null
    var battleMovementTimeline: Boolean = false
    var battleMovePathResolver: ((Int, Int, Int) -> List<Pair<Int, Int>>?)? = null
    val heads = linkedMapOf<Int, ScenarioHead>()

    fun head(id: Int): ScenarioHead = heads.getOrPut(id) { ScenarioHead(id) }

    /** Head.move duration is 0.01 * Cocos converted-position Euclidean distance. */
    fun moveHead(id: Int, x: Int, y: Int): Float {
        val head = head(id)
        val dx = x - head.visualX
        val dy = y - head.visualY
        val duration = kotlin.math.sqrt(dx * dx + dy * dy) * 0.01f
        head.moveFromX = head.visualX
        head.moveFromY = head.visualY
        head.moveElapsed = 0f
        head.moveDuration = duration
        head.x = x
        head.y = y
        head.visible = true
        if (duration <= 0f) {
            head.visualX = x.toFloat()
            head.visualY = y.toFloat()
        }
        return duration
    }

    /** New Head nodes start transparent and pause HallLayer through fadeIn(1). */
    fun showHead(id: Int, x: Int, y: Int): Float {
        val existing = heads[id]
        if (existing != null && existing.visible) {
            existing.x = x
            existing.y = y
            existing.visualX = x.toFloat()
            existing.visualY = y.toFloat()
            existing.moveDuration = 0f
            existing.visible = true
            return 0f
        }
        heads[id] = ScenarioHead(id, x, y).apply {
            visualX = x.toFloat()
            visualY = y.toFloat()
            opacity = 0f
            fadeFrom = 0f
            fadeTo = 1f
            fadeElapsed = 0f
            fadeDuration = 1f
        }
        return 1f
    }

    /** headHide removes the dictionary entry immediately, but its node fades for one second. */
    fun hideHead(id: Int): Float {
        val head = heads[id] ?: return 0f
        head.visible = false
        head.fadeFrom = head.opacity
        head.fadeTo = 0f
        head.fadeElapsed = 0f
        head.fadeDuration = 1f
        return 1f
    }

    fun countDirection(fromId: Int, toId: Int, unitProvider: (Int) -> TacticalUnit): Int {
        val from = unitProvider(fromId)
        val to = unitProvider(toId)
        if (fromId == toId) return from.direction
        val dx = kotlin.math.abs(to.x - from.x)
        return if (kotlin.math.abs(to.y - from.y) > dx) {
            if (from.y > to.y) 0 else 2
        } else if (from.x > to.x) 3 else 1
    }

    fun moveDuration(id: Int, x: Int, y: Int, units: Map<Int, TacticalUnit>): Float =
        movePath(id, x, y, units)?.let(::moveDuration) ?: 0f

    fun moveDuration(path: List<Pair<Int, Int>>): Float {
        val edges = (path.size - 1).coerceAtLeast(0)
        if (edges == 0) return 0f
        return if (battleMovementTimeline) edges * 0.08f + 0.1f else edges * 0.04f
    }

    fun movePath(id: Int, x: Int, y: Int, units: Map<Int, TacticalUnit>): List<Pair<Int, Int>>? {
        val unit = units[id] ?: return null
        if (!unit.visible) return null
        battleMovePathResolver?.let { return it(id, x, y) }
        return movePath(unit, x, y, units.values.map { it.x to it.y }.toSet())
    }

    private fun movePath(unit: TacticalUnit, x: Int, y: Int, occupied: Set<Pair<Int, Int>>): List<Pair<Int, Int>>? =
        HallPathfinder.find(
            unit.x, unit.y, x.coerceIn(0, 99), y.coerceIn(0, 99), hallPathGrid,
            occupied,
        )

    fun moveUnit(
        id: Int,
        x: Int,
        y: Int,
        direction: Int,
        units: Map<Int, TacticalUnit>,
        onScriptedDirection: (Pair<Int, Int>) -> Unit,
    ) {
        val unit = units[id] ?: return
        val path = movePath(id, x, y, units) ?: return
        beginMove(unit, path, x, y, direction, onScriptedDirection)
    }

    fun moveUnits(
        requests: List<ScenarioCommand.MoveUnit>,
        units: Map<Int, TacticalUnit>,
        onScriptedDirection: (Pair<Int, Int>) -> Unit,
    ): Float {
        val occupiedOrigins = units.values.map { it.x to it.y }.toSet()
        val planned = requests.mapNotNull { request ->
            val moving = units[request.unitId] ?: return@mapNotNull null
            if (!moving.visible) return@mapNotNull null
            val path = battleMovePathResolver?.invoke(moving.id, request.x, request.y)
                ?: if (battleMovePathResolver == null) movePath(moving, request.x, request.y, occupiedOrigins) else null
            path?.let { Triple(request, moving, it) }
        }
        planned.forEach { (request, moving, path) ->
            beginMove(moving, path, request.x, request.y, request.direction, onScriptedDirection)
        }
        return planned.maxOfOrNull { (_, _, path) -> moveDuration(path) } ?: 0f
    }

    private fun beginMove(
        unit: TacticalUnit,
        path: List<Pair<Int, Int>>,
        x: Int,
        y: Int,
        direction: Int,
        onScriptedDirection: (Pair<Int, Int>) -> Unit,
    ) {
        val id = unit.id
        val duration = moveDuration(path)
        unit.moveFromX = unit.visualX
        unit.moveFromY = unit.visualY
        unit.moveElapsed = 0f
        unit.animationElapsed = 0f
        unit.moveDuration = duration
        unit.movePath = path
        unit.moveZIndex = 4f * (unit.visualX + unit.visualY) - 424f
        unit.moveFinalDirection = direction
        unit.moveJustStarted = duration > 0f
        val resolvedDestination = path.lastOrNull() ?: (x to y)
        unit.moveToX = resolvedDestination.first.coerceIn(0, 99)
        unit.moveToY = resolvedDestination.second.coerceIn(0, 99)
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

    fun updateAnimations(delta: Float, units: Map<Int, TacticalUnit>) {
        units.values.filter { it.moveDuration > 0f }.forEach { unit ->
            if (unit.moveJustStarted) {
                unit.moveJustStarted = false
            }
            val elapsedDelta = delta.coerceAtLeast(0f)
            unit.moveElapsed = (unit.moveElapsed + elapsedDelta).coerceAtMost(unit.moveDuration)
            val (sampleX, sampleY, sampleDirection, sampleZ) = if (battleMovementTimeline) {
                val timeline = BattleUnitMoveTimeline.schedule(unit.movePath, fastMove = true)
                val sample = BattleUnitMoveTimeline.sample(unit.movePath, timeline, unit.moveElapsed)
                listOf(sample.x, sample.y, sample.direction.toFloat(), 4f * (sample.x + sample.y) - 424f)
            } else {
                val sample = HallMoveTimeline.sample(unit.movePath, unit.moveElapsed)
                listOf(sample.x, sample.y, sample.direction.toFloat(), sample.zIndex)
            }
            unit.visualX = sampleX
            unit.visualY = sampleY
            unit.moveZIndex = sampleZ
            val nextDirection = sampleDirection.toInt().takeIf { it >= 0 } ?: unit.direction
            if (nextDirection != unit.direction) unit.animationElapsed = 0f else unit.animationElapsed += elapsedDelta
            unit.direction = nextDirection
            if (unit.moveElapsed >= unit.moveDuration) {
                unit.x = unit.moveToX
                unit.y = unit.moveToY
                unit.visualX = unit.x.toFloat()
                unit.visualY = unit.y.toFloat()
                unit.moveZIndex = 4f * (unit.visualX + unit.visualY) - 424f
                unit.moveDuration = 0f
                unit.action = 0
                unit.direction = unit.moveFinalDirection
            }
        }
        heads.values.forEach { head ->
            if (head.moveDuration > 0f) {
                head.moveElapsed = (head.moveElapsed + delta.coerceAtLeast(0f)).coerceAtMost(head.moveDuration)
                val progress = head.moveElapsed / head.moveDuration
                head.visualX = head.moveFromX + (head.x - head.moveFromX) * progress
                head.visualY = head.moveFromY + (head.y - head.moveFromY) * progress
                if (progress >= 1f) head.moveDuration = 0f
            }
            if (head.fadeDuration > 0f) {
                head.fadeElapsed = (head.fadeElapsed + delta.coerceAtLeast(0f)).coerceAtMost(head.fadeDuration)
                val progress = head.fadeElapsed / head.fadeDuration
                head.opacity = head.fadeFrom + (head.fadeTo - head.fadeFrom) * progress
                if (progress >= 1f) head.fadeDuration = 0f
            }
        }
    }

    fun finishAnimations(units: Map<Int, TacticalUnit>) {
        units.values.forEach { unit ->
            if (unit.moveDuration > 0f) {
                unit.x = unit.moveToX
                unit.y = unit.moveToY
            }
            unit.visualX = unit.x.toFloat()
            unit.visualY = unit.y.toFloat()
            unit.moveDuration = 0f
            unit.action = if (unit.movePath.isNotEmpty()) 0 else unit.action
            if (unit.movePath.isNotEmpty()) unit.direction = unit.moveFinalDirection
        }
        heads.values.forEach { head ->
            head.visualX = head.x.toFloat()
            head.visualY = head.y.toFloat()
            head.moveDuration = 0f
            head.opacity = head.fadeTo
            head.fadeDuration = 0f
        }
    }
}
