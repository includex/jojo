package com.jojo.game

import kotlin.math.floor

/** Exact HallUnit._move2 straight-run grouping and 40 ms z-order scheduler. */
object HallMoveTimeline {
    /**
     * data class  `Segment`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Segment(
        val fromX: Float, val fromY: Float,
        val toX: Float, val toY: Float,
        val startsAt: Float, val duration: Float,
        val direction: Int,
    )

    /**
     * data class  `Sample`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Sample(val x: Float, val y: Float, val direction: Int, val zIndex: Float)

    /**
     * 공개 메서드 `segments`
     *
     * ### 파라미터
    - `path` (`List<Pair<Int, Int>>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Segment>`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `sample`
     *
     * ### 파라미터
    - `path` (`List<Pair<Int, Int>>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `elapsed` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Sample`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
         * 공개 메서드 `positionAt`
         *
         * ### 파라미터
        - `time` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Triple<Float, Float, Int>`
         * - 반환값: 동작 결과의 도메인 값입니다.
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

        val current = positionAt(elapsed.coerceAtLeast(0f))
        // HallUnit schedules _moveDuring at 40 ms and otherwise retains the
        // previous zIndex even though moveTo continues interpolating.
        val zTime = floor((elapsed.coerceAtLeast(0f) + 1e-6f) / .04f) * .04f
        val zPoint = positionAt(zTime)
        return Sample(current.first, current.second, current.third, z(zPoint.first, zPoint.second))
    }

    private fun z(x: Float, y: Float) = 4f * (x + y) - 424f
}
