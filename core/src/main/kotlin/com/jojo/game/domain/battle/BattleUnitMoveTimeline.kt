// Battle
package com.jojo.game.domain.battle

/** BattleUnitMoveTimeline: 유닛 이동 경로를 시간별 화면 좌표로 변환해 이동 애니메이션에 제공한다. */
object BattleUnitMoveTimeline {

    data class Segment(
        val direction: Int,
        val startIndex: Int,
        val endIndex: Int,
        val startedAt: Float,
        val duration: Float,
    )


    data class Timeline(
        val secondsPerTile: Float,
        val segments: List<Segment>,
        val idleAt: Float,
        val movementTicks: List<Float>,
    )
    data class Sample(
        val x: Float,
        val y: Float,
        val direction: Int,
        val moving: Boolean,
    )

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
        // 원본 스케줄 함수는 첫 호출 뒤의 반복 횟수를 받는다.
        // 따라서 목적지에 도착하는 시점을 포함해 i부터 (path.size - 1) * i까지
        // 총 o - 1회 콜백을 호출한다.
        val ticks = (1 until path.size).map { it * seconds }
        return Timeline(seconds, segments, elapsed + .1f, ticks)
    }

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
