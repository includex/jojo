// Game
package com.jojo.game.application.scenario

import com.jojo.game.infrastructure.data.HallPathGrid

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

import com.jojo.game.domain.scenario.ScenarioHead
import com.jojo.game.domain.scenario.TacticalUnit

/**
 * `ScenarioStageMovementCoordinator` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal class ScenarioStageMovementCoordinator {
    /**
     * `planner` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val planner = ScenarioStageMovementPlanner()
    /**
     * `unitAnimator` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val unitAnimator = ScenarioStageUnitMovementAnimator()
    /**
     * `headCoordinator` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val headCoordinator = ScenarioStageHeadCoordinator()

    /**
     * `hallPathGrid` (HallPathGrid?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var hallPathGrid: HallPathGrid?
        get() = planner.hallPathGrid
        set(value) {
            planner.hallPathGrid = value
        }
    /**
     * `battleMovementTimeline` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var battleMovementTimeline: Boolean
        get() = planner.battleMovementTimeline
        set(value) {
            planner.battleMovementTimeline = value
        }
    /**
     * `battleMovePathResolver` (((Int, Int, Int) -> List<Pair<Int, Int>>?)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var battleMovePathResolver: ((Int, Int, Int) -> List<Pair<Int, Int>>?)?
        get() = planner.battleMovePathResolver
        set(value) {
            planner.battleMovePathResolver = value
        }
    /**
     * `heads` (MutableMap<Int, ScenarioHead> get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val heads: MutableMap<Int, ScenarioHead> get() = headCoordinator.heads

    /**
     * `head`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun head(id: Int): ScenarioHead = headCoordinator.head(id)
    /**
     * `moveHead`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun moveHead(id: Int, x: Int, y: Int): Float = headCoordinator.move(id, x, y)
    /**
     * `showHead`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun showHead(id: Int, x: Int, y: Int): Float = headCoordinator.show(id, x, y)
    /**
     * `hideHead`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hideHead(id: Int): Float = headCoordinator.hide(id)

    /**
     * `countDirection`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun countDirection(fromId: Int, toId: Int, unitProvider: (Int) -> TacticalUnit): Int =
        planner.countDirection(fromId, toId, unitProvider)

    /**
     * `moveDuration`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun moveDuration(id: Int, x: Int, y: Int, units: Map<Int, TacticalUnit>): Float =
        planner.pathFor(id, x, y, units)?.let(planner::duration) ?: 0f

    /**
     * `moveDuration`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun moveDuration(path: List<Pair<Int, Int>>): Float = planner.duration(path)

    /**
     * `movePath`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun movePath(id: Int, x: Int, y: Int, units: Map<Int, TacticalUnit>): List<Pair<Int, Int>>? =
        planner.pathFor(id, x, y, units)

    /**
     * `moveUnit`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun moveUnit(
        id: Int,
        x: Int,
        y: Int,
        direction: Int,
        units: Map<Int, TacticalUnit>,
        onScriptedDirection: (Pair<Int, Int>) -> Unit,
    ) {
        /**
         * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unit = units[id] ?: return
        /**
         * `path` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val path = planner.pathFor(id, x, y, units) ?: return
        unitAnimator.begin(unit, path, x, y, direction, planner.duration(path), onScriptedDirection)
    }

    /**
     * `moveUnits`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun moveUnits(
        requests: List<ScenarioCommand.MoveUnit>,
        units: Map<Int, TacticalUnit>,
        onScriptedDirection: (Pair<Int, Int>) -> Unit,
    ): Float {
        /**
         * `planned` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val planned = planner.plan(requests, units)
        planned.forEach { movement ->
            unitAnimator.begin(
                movement.unit,
                movement.path,
                movement.request.x,
                movement.request.y,
                movement.request.direction,
                planner.duration(movement.path),
                onScriptedDirection,
            )
        }
        return planned.maxOfOrNull { planner.duration(it.path) } ?: 0f
    }

    /**
     * `updateAnimations`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun updateAnimations(delta: Float, units: Map<Int, TacticalUnit>) {
        unitAnimator.update(delta, units, battleMovementTimeline)
        headCoordinator.update(delta)
    }

    /**
     * `finishAnimations`: 조건과 입력 상태를 검증한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun finishAnimations(units: Map<Int, TacticalUnit>) {
        unitAnimator.finish(units)
        headCoordinator.finish()
    }
}
