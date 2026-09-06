// Battle
package com.jojo.game.domain.battle

/** BattleUnitMoveTimeline: 유닛 이동 경로를 시간별 화면 좌표로 변환해 이동 애니메이션에 제공한다. */
object BattleUnitMoveTimeline {

    /**
     * `Segment` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Segment(
        /**
         * `direction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val direction: Int,
        /**
         * `startIndex` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val startIndex: Int,
        /**
         * `endIndex` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val endIndex: Int,
        /**
         * `startedAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val startedAt: Float,
        /**
         * `duration` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val duration: Float,
    )


    /**
     * `Timeline` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Timeline(
        /**
         * `secondsPerTile` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val secondsPerTile: Float,
        /**
         * `segments` (List<Segment>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val segments: List<Segment>,
        /**
         * `idleAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val idleAt: Float,
        /**
         * `movementTicks` (List<Float>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val movementTicks: List<Float>,
    )
    /**
     * `Sample` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Sample(
        /**
         * `x` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Float,
        /**
         * `y` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Float,
        /**
         * `direction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val direction: Int,
        /**
         * `moving` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val moving: Boolean,
    )

    /**
     * `schedule`: 해당 흐름을 실행하거나 다음 단계로 전달한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
        // 원본 스케줄 함수는 첫 호출 뒤의 반복 횟수를 받는다.
        // 따라서 목적지에 도착하는 시점을 포함해 i부터 (path.size - 1) * i까지
        // 총 o - 1회 콜백을 호출한다.
        val ticks = (1 until path.size).map { it * seconds }
        return Timeline(seconds, segments, elapsed + .1f, ticks)
    }

    /**
     * `sample`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
