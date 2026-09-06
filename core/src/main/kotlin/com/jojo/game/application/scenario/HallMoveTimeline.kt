// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.infrastructure.data.HallPathfinder

import kotlin.math.floor

/** HallMoveTimeline: 거점 Move 시간 흐름이며, 시나리오 화면의 시간별 표시 순서를 진행한다. */
object HallMoveTimeline {

    /**
     * `Segment` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Segment(
        /**
         * `fromX` (Float, val fromY: Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val fromX: Float, val fromY: Float,
        /**
         * `toX` (Float, val toY: Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val toX: Float, val toY: Float,
        /**
         * `startsAt` (Float, val duration: Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val startsAt: Float, val duration: Float,
        /**
         * `direction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val direction: Int,
    )


    /**
     * `Sample` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Sample(val x: Float, val y: Float, val direction: Int, val zIndex: Float)


    /**
     * `segments`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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


    /**
     * `sample`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

        /**
         * `positionAt`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

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

        /**
         * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val current = positionAt(elapsed.coerceAtLeast(0f))
        /**
         * `zTime` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zTime = floor((elapsed.coerceAtLeast(0f) + 1e-6f) / .04f) * .04f
        /**
         * `zPoint` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zPoint = positionAt(zTime)
        return Sample(current.first, current.second, current.third, z(zPoint.first, zPoint.second))
    }

    /**
     * `z`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun z(x: Float, y: Float) = 4f * (x + y) - 424f
}
