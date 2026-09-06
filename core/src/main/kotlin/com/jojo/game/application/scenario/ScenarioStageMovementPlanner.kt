// Game
package com.jojo.game.application.scenario

import com.jojo.game.infrastructure.data.HallPathGrid
import com.jojo.game.infrastructure.data.HallPathfinder

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

import com.jojo.game.domain.scenario.TacticalUnit

/**
 * `PlannedScenarioMovement` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

internal data class PlannedScenarioMovement(
    val request: ScenarioCommand.MoveUnit,
    val unit: TacticalUnit,
    val path: List<Pair<Int, Int>>,
)

/** ScenarioStageMovementPlanner: 스크립트 이동 경로 선택과 콜백 지속 시간을 관리한다. */
internal class ScenarioStageMovementPlanner {
    /**
     * `hallPathGrid` (HallPathGrid?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var hallPathGrid: HallPathGrid? = null
    /**
     * `battleMovementTimeline` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var battleMovementTimeline: Boolean = false
    /**
     * `battleMovePathResolver` (((Int, Int, Int) -> List<Pair<Int, Int>>?)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var battleMovePathResolver: ((Int, Int, Int) -> List<Pair<Int, Int>>?)? = null

    /**
     * `countDirection`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun countDirection(fromId: Int, toId: Int, unitProvider: (Int) -> TacticalUnit): Int {
        val from = unitProvider(fromId)
        val to = unitProvider(toId)
        if (fromId == toId) return from.direction
        val dx = kotlin.math.abs(to.x - from.x)
        return if (kotlin.math.abs(to.y - from.y) > dx) {
            if (from.y > to.y) 0 else 2
        } else if (from.x > to.x) 3 else 1
    }

    /**
     * `duration`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun duration(path: List<Pair<Int, Int>>): Float {
        val edges = (path.size - 1).coerceAtLeast(0)
        if (edges == 0) return 0f
        return if (battleMovementTimeline) edges * 0.08f + 0.1f else edges * 0.04f
    }

    /**
     * `pathFor`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun pathFor(id: Int, x: Int, y: Int, units: Map<Int, TacticalUnit>): List<Pair<Int, Int>>? {
        val unit = units[id] ?: return null
        if (!unit.visible) return null
        battleMovePathResolver?.let { return it(id, x, y) }
        return pathFor(unit, x, y, units.values.map { it.x to it.y }.toSet())
    }

    /**
     * `plan`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun plan(requests: List<ScenarioCommand.MoveUnit>, units: Map<Int, TacticalUnit>): List<PlannedScenarioMovement> {
        val occupiedOrigins = units.values.map { it.x to it.y }.toSet()
        return requests.mapNotNull { request ->
            val moving = units[request.unitId] ?: return@mapNotNull null
            if (!moving.visible) return@mapNotNull null
            val path = battleMovePathResolver?.invoke(moving.id, request.x, request.y)
                ?: if (battleMovePathResolver == null) pathFor(moving, request.x, request.y, occupiedOrigins) else null
            path?.let { PlannedScenarioMovement(request, moving, it) }
        }
    }

    /**
     * `pathFor`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun pathFor(
        unit: TacticalUnit,
        x: Int,
        y: Int,
        occupied: Set<Pair<Int, Int>>,
    ): List<Pair<Int, Int>>? = HallPathfinder.find(
        unit.x,
        unit.y,
        x.coerceIn(0, 99),
        y.coerceIn(0, 99),
        hallPathGrid,
        occupied,
    )
}
