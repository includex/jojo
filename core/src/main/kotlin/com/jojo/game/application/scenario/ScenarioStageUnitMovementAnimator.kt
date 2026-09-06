// Game
package com.jojo.game.application.scenario

import com.jojo.game.infrastructure.data.HallPathfinder

import com.jojo.game.*

import com.jojo.game.application.scenario.HallMoveTimeline

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnitMoveTimeline

import com.jojo.game.domain.scenario.TacticalUnit

/** ScenarioStageUnitMovementAnimator: 이동 계획을 적용하고 홀·전투 화면의 이동 연출을 진행한다. */
internal class ScenarioStageUnitMovementAnimator {
    /**
     * `begin`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun begin(
        unit: TacticalUnit,
        path: List<Pair<Int, Int>>,
        requestedX: Int,
        requestedY: Int,
        direction: Int,
        duration: Float,
        onScriptedDirection: (Pair<Int, Int>) -> Unit,
    ) {
        /**
         * `id` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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
        /**
         * `destination` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

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

    /**
     * `update`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `finish`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun finish(units: Map<Int, TacticalUnit>) = units.values.forEach { finish(it, refreshZIndex = false) }

    /**
     * `finish`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

/**
 * `ScenarioMovementSample` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

private data class ScenarioMovementSample(val x: Float, val y: Float, val direction: Int, val zIndex: Float)
