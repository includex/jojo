// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.infrastructure.data.HallPathfinder

import kotlin.math.floor

/** HallMoveTimeline: 거점 Move 시간 흐름이며, 시나리오 화면의 시간별 표시 순서를 진행한다. */
object HallMoveTimeline {

    data class Segment(
        val fromX: Float, val fromY: Float,
        val toX: Float, val toY: Float,
        val startsAt: Float, val duration: Float,
        val direction: Int,
    )


    data class Sample(val x: Float, val y: Float, val direction: Int, val zIndex: Float)


    fun segments(path: List<Pair<Int, Int>>): List<Segment> {
        if (path.size < 2) return emptyList()
        val result = mutableListOf<Segment>()
        var previous = path.first()
        var segmentStart = previous
        var direction = -1
        var count = 0
        var time = 0f
        path.drop(1).forEach { point ->
            val nextDirection = HallPathfinder.direction(previous.first, previous.second, point.first, point.second)
            if (nextDirection == direction) {
                count++
            } else {
                if (direction >= 0) {
                    val duration = .04f * count
                    result += Segment(
                        segmentStart.first.toFloat(),
                        segmentStart.second.toFloat(),
                        point.first.toFloat(),
                        point.second.toFloat(),
                        time,
                        duration,
                        direction
                    )
                    time += duration
                    segmentStart = point
                }
                direction = nextDirection
                count = 1
            }
            previous = point
        }
        val duration = .04f * count
        result += Segment(
            segmentStart.first.toFloat(),
            segmentStart.second.toFloat(),
            previous.first.toFloat(),
            previous.second.toFloat(),
            time,
            duration,
            direction
        )
        return result
    }


    fun sample(path: List<Pair<Int, Int>>, elapsed: Float): Sample {
        val segments = segments(path)
        if (segments.isEmpty()) {
            val point = path.firstOrNull() ?: (0 to 0)
            return Sample(
                point.first.toFloat(),
                point.second.toFloat(),
                -1,
                z(point.first.toFloat(), point.second.toFloat())
            )
        }

        fun positionAt(time: Float): Triple<Float, Float, Int> {
            val segment = segments.firstOrNull { time < it.startsAt + it.duration } ?: segments.last()
            val progress =
                if (segment.duration <= 0f) 1f else ((time - segment.startsAt) / segment.duration).coerceIn(0f, 1f)
            return Triple(
                segment.fromX + (segment.toX - segment.fromX) * progress,
                segment.fromY + (segment.toY - segment.fromY) * progress,
                segment.direction,
            )
        }

        val current = positionAt(elapsed.coerceAtLeast(0f))
        val zTime = floor((elapsed.coerceAtLeast(0f) + 1e-6f) / .04f) * .04f
        val zPoint = positionAt(zTime)
        return Sample(current.first, current.second, current.third, z(zPoint.first, zPoint.second))
    }

    private fun z(x: Float, y: Float) = 4f * (x + y) - 424f
}
