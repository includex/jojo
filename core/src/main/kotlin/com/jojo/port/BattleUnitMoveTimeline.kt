package com.jojo.port

/** Pure port of BattleUnit.move2's directional action schedule. */
object BattleUnitMoveTimeline {
    data class Segment(
        val direction: Int,
        /** Index in the source path, which includes the start point. */
        val startIndex: Int,
        val endIndex: Int,
        val startedAt: Float,
        val duration: Float,
    )

    data class Timeline(
        val secondsPerTile: Float,
        val segments: List<Segment>,
        /** move2's final delayTime(.1), after the final moveTo. */
        val idleAt: Float,
        /** Scheduled terrain/movement update times from Animation.schedule. */
        val movementTicks: List<Float>,
    )

    /** The position/action state of the Cocos moveTo sequence at [elapsed]. */
    data class Sample(
        val x: Float,
        val y: Float,
        /** BattleUnit.setAction2(MOVE, { dir, loop: true }) direction. */
        val direction: Int,
        /** False only during move2's final delayTime(.1). */
        val moving: Boolean,
    )

    /**
     * [path] is BattleLayer.unitMove's `s` array: it contains start followed
     * by every point selected by A*. `fastMove` is BattleUnit.moveSpeed().
     */
    fun schedule(path: List<Pair<Int, Int>>, fastMove: Boolean): Timeline {
        require(path.size >= 2) { "move2 needs a start and destination point" }
        val seconds = if (fastMove) .08f else .16f
        val segments = mutableListOf<Segment>()
        var direction = -1
        var start = 0
        var elapsed = 0f
        for (index in 1 until path.size) {
            val (fromX, fromY) = path[index - 1]
            val (toX, toY) = path[index]
            val nextDirection = if (toX == fromX) {
                if (toY > fromY) 2 else 0
            } else if (toX > fromX) 1 else 3
            if (direction != -1 && direction != nextDirection) {
                val count = index - start - 1
                segments += Segment(direction, start, index - 1, elapsed, count * seconds)
                elapsed += count * seconds
                start = index - 1
            }
            direction = nextDirection
        }
        val count = path.lastIndex - start
        segments += Segment(direction, start, path.lastIndex, elapsed, count * seconds)
        elapsed += count * seconds
        // Cocos schedule(handle, i, o - 2, i) treats repeat as the number of
        // repeats *after* the first invocation.  It therefore invokes the
        // callback o - 1 times: i through (path.size - 1) * i, including the
        // instant at which the final moveTo reaches the destination.
        val ticks = (1 until path.size).map { it * seconds }
        return Timeline(seconds, segments, elapsed + .1f, ticks)
    }

    /**
     * Samples the same piecewise-linear cc.moveTo sequence constructed by
     * move2. The final .1s delay holds the destination while defaultAction is
     * restored, so it is intentionally not interpolated as another tile.
     */
    fun sample(path: List<Pair<Int, Int>>, timeline: Timeline, elapsed: Float): Sample {
        require(path.size >= 2) { "move2 needs a start and destination point" }
        val segment = timeline.segments.firstOrNull { elapsed >= it.startedAt && elapsed < it.startedAt + it.duration }
        if (segment != null) {
            val from = path[segment.startIndex]
            val to = path[segment.endIndex]
            val progress = ((elapsed - segment.startedAt) / segment.duration).coerceIn(0f, 1f)
            return Sample(
                x = from.first + (to.first - from.first) * progress,
                y = from.second + (to.second - from.second) * progress,
                direction = segment.direction,
                moving = true,
            )
        }
        val end = path.last()
        return Sample(end.first.toFloat(), end.second.toFloat(), timeline.segments.last().direction, moving = false)
    }
}
