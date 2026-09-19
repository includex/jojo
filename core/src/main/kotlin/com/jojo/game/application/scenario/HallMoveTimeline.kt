// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.infrastructure.data.HallPathfinder

import kotlin.math.floor

/** HallMoveTimeline: 거점 Move 시간 흐름이며, 시나리오 화면의 시간별 표시 순서를 진행한다. */
object HallMoveTimeline {
    /** cc.ActionInterval.initWithDuration가 zero duration에 치환하는 값이다. */
    const val SOURCE_ACTION_EPSILON_SECONDS: Double = 1.192092896e-7

    private sealed interface SourceAction {
        val duration: Double

        data object Call : SourceAction { override val duration: Double = 0.0 }
        data class Move(
            val fromX: Double,
            val fromY: Double,
            val toX: Double,
            val toY: Double,
            override val duration: Double,
            val direction: Int,
        ) : SourceAction
    }

    private data class ScheduledMove(val action: SourceAction.Move, val startsAt: Double)
    private data class SourceSchedule(val duration: Double, val moves: List<ScheduledMove>)

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

    data class Sample(
        val x: Float,
        val y: Float,
        val direction: Int,
        val zIndex: Float,
        /** Cocos node interpolation followed by its parent world transform, before Float32 vertex storage. */
        val sourceWorldX: Double,
        val sourceWorldY: Double,
    )

    data class SourceWorldPosition(val x: Double, val y: Double)

    /**
     * Replays HallLayer.turnPos and the map node's world transform in their source operation order.
     * The source interpolates these node coordinates as JavaScript Numbers, rather than interpolating
     * grid coordinates and converting the rounded Float result during rendering.
     */
    fun sourceWorldPosition(x: Double, y: Double): SourceWorldPosition {
        val node = sourceNodePosition(x, y)
        return sourceWorldPosition(node)
    }

    private fun sourceNodePosition(x: Double, y: Double): SourceNodePosition {
        val r = x - 50.0
        val n = 50.0 - y
        var nodeX = r * SOURCE_NODE_TILE_X
        var nodeY = -r * SOURCE_NODE_TILE_Y
        nodeX += n * SOURCE_NODE_TILE_X
        nodeY += n * SOURCE_NODE_TILE_Y
        nodeX += 2.0 * SOURCE_NODE_TILE_X
        nodeY += SOURCE_NODE_Y_OFFSET
        return SourceNodePosition(nodeX, nodeY)
    }

    private fun sourceWorldPosition(node: SourceNodePosition): SourceWorldPosition {
        return SourceWorldPosition(
            node.x * SOURCE_PARENT_SCALE + SOURCE_PARENT_X,
            node.y * SOURCE_PARENT_SCALE + SOURCE_PARENT_Y,
        )
    }


    /**
     * `segments`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun segments(path: List<Pair<Int, Int>>): List<Segment> {
        return sourceSchedule(path).moves.map { scheduled ->
            val move = scheduled.action
            Segment(
                move.fromX.toFloat(), move.fromY.toFloat(), move.toX.toFloat(), move.toY.toFloat(),
                scheduled.startsAt.toFloat(), move.duration.toFloat(), move.direction,
            )
        }
    }

    /** 원본 HallUnit._move2가 만드는 action 목록을 cc.sequence의 left fold 규칙으로 예약한다. */
    private fun sourceSchedule(path: List<Pair<Int, Int>>): SourceSchedule {
        if (path.isEmpty()) return SourceSchedule(0.0, emptyList())
        val actions = mutableListOf<SourceAction>()
        var previous = path.first()
        var segmentStart = previous
        var direction = -1
        var count = 0
        path.drop(1).forEach { point ->
            val nextDirection = HallPathfinder.direction(previous.first, previous.second, point.first, point.second)
            if (nextDirection == direction) {
                count++
            } else {
                if (direction >= 0) {
                    actions += SourceAction.Move(
                        segmentStart.first.toDouble(), segmentStart.second.toDouble(),
                        point.first.toDouble(), point.second.toDouble(), .04 * count, direction,
                    )
                    segmentStart = point
                }
                actions += SourceAction.Call
                direction = nextDirection
                count = 1
            }
            previous = point
        }
        actions += SourceAction.Call
        actions += SourceAction.Move(
            segmentStart.first.toDouble(), segmentStart.second.toDouble(),
            previous.first.toDouble(), previous.second.toDouble(),
            (.04 * count).takeIf { it > 0.0 } ?: SOURCE_ACTION_EPSILON_SECONDS,
            direction,
        )
        actions += SourceAction.Call

        var sequenceDuration = actions.first().duration
        val moves = mutableListOf<ScheduledMove>()
        actions.drop(1).forEach { action ->
            if (action is SourceAction.Move) moves += ScheduledMove(action, sequenceDuration)
            val sum = sequenceDuration + action.duration
            sequenceDuration = if (sum == 0.0) SOURCE_ACTION_EPSILON_SECONDS else sum
        }
        return SourceSchedule(sequenceDuration, moves)
    }

    fun sourceDuration(path: List<Pair<Int, Int>>): Double = sourceSchedule(path).duration


    /**
     * `sample`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun sample(path: List<Pair<Int, Int>>, elapsed: Float): Sample {
        return sample(path, elapsed.toDouble())
    }

    fun sample(path: List<Pair<Int, Int>>, elapsed: Double): Sample {
        val schedule = sourceSchedule(path)
        if (schedule.moves.isEmpty()) {
            val point = path.firstOrNull() ?: (0 to 0)
            return Sample(
                point.first.toFloat(),
                point.second.toFloat(),
                -1,
                z(point.first.toFloat(), point.second.toFloat()),
                sourceWorldPosition(point.first.toDouble(), point.second.toDouble()).x,
                sourceWorldPosition(point.first.toDouble(), point.second.toDouble()).y,
            )
        }

        /**
         * `positionAt`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun positionAt(time: Double): PositionSample {
            val scheduled = schedule.moves.firstOrNull { time < it.startsAt + it.action.duration }
                ?: schedule.moves.last()
            val segment = scheduled.action
            val progress =
                if (segment.duration <= 0.0) 1.0
                else ((time - scheduled.startsAt) / segment.duration).coerceIn(0.0, 1.0)
            val gridX = segment.fromX + (segment.toX - segment.fromX) * progress
            val gridY = segment.fromY + (segment.toY - segment.fromY) * progress
            val fromNode = sourceNodePosition(segment.fromX, segment.fromY)
            val toNode = sourceNodePosition(segment.toX, segment.toY)
            // cc.MoveBy.update computes delta * progress and then adds the start node position.
            val sourceNode = SourceNodePosition(
                (toNode.x - fromNode.x) * progress + fromNode.x,
                (toNode.y - fromNode.y) * progress + fromNode.y,
            )
            val sourceWorld = sourceWorldPosition(sourceNode)
            return PositionSample(
                gridX.toFloat(), gridY.toFloat(), segment.direction, sourceWorld.x, sourceWorld.y,
            )
        }

        /**
         * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val current = positionAt(elapsed.coerceAtLeast(0.0))
        /**
         * `zTime` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zTime = floor((elapsed.coerceAtLeast(0.0) + 1e-6) / .04) * .04
        /**
         * `zPoint` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val zPoint = positionAt(zTime)
        return Sample(current.x, current.y, current.direction, z(zPoint.x, zPoint.y), current.sourceWorldX, current.sourceWorldY)
    }

    /**
     * `z`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun z(x: Float, y: Float) = 4f * (x + y) - 424f

    private data class PositionSample(
        val x: Float,
        val y: Float,
        val direction: Int,
        val sourceWorldX: Double,
        val sourceWorldY: Double,
    )

    private data class SourceNodePosition(val x: Double, val y: Double)

    private val SOURCE_CANVAS_WIDTH = 1280.0 / 0.86
    private val SOURCE_NODE_TILE_X = SOURCE_CANVAS_WIDTH / 160.0
    private const val SOURCE_NODE_TILE_Y = 4.0
    private const val SOURCE_NODE_Y_OFFSET = 24.0
    private const val SOURCE_PARENT_SCALE = 2.0
    private val SOURCE_PARENT_X = SOURCE_CANVAS_WIDTH / 2.0
    private const val SOURCE_PARENT_Y = 400.0
}
