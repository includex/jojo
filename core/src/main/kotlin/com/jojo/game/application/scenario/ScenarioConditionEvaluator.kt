// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*
/** 조건 평가에 필요한 변수와 전장 조회 함수를 모은다. */
internal data class ScenarioConditionEnvironment(
    val gvars: MutableMap<Int, Any?>,
    val pvars: MutableMap<Int, Any?>,
    val battleContext: ScenarioBattleScriptContext,
    val stageUnitAttribute: (Int, Int) -> Int,
)

/**
 * `sourceUnitTypeMatches`: 타입의 핵심 동작을 수행한다.
 * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
 */

internal fun sourceUnitTypeMatches(camp: Int, selector: Int): Boolean = when (selector) {
    0, 1, 2, 3 -> camp == selector
    4 -> camp <= 1
    5 -> camp >= 2
    6 -> true
    else -> false
}

/** 시나리오의 변수, 위치, 유닛 상태 조건을 평가한다. */
internal object ScenarioConditionEvaluator {
    /**
     * `ADDRESS_INTVAR_START` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val ADDRESS_INTVAR_START = ScenarioConditionOperandResolver.ADDRESS_INTVAR_START
    /**
     * `ADDRESS_INTVAR_END` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    const val ADDRESS_INTVAR_END = ScenarioConditionOperandResolver.ADDRESS_INTVAR_END
    /**
     * `DEFAULT_CARDINAL_NEAR_OFFSETS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val DEFAULT_CARDINAL_NEAR_OFFSETS = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
    /**
     * `DEFAULT_INFANTRY_NEAR_OFFSETS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val DEFAULT_INFANTRY_NEAR_OFFSETS = DEFAULT_CARDINAL_NEAR_OFFSETS + setOf(1 to 1, -1 to 1, 1 to -1, -1 to -1)

    /** 종류와 값으로 지정된 시나리오 변수 값을 해석한다. */
    fun stageVariableValue(kind: Int, value: Int, env: ScenarioConditionEnvironment): Int =
        ScenarioConditionOperandResolver.value(kind, value, env)

    /** 시나리오 메모리 주소의 정수 값을 읽는다. */
    fun readStageAddress(address: Int, env: ScenarioConditionEnvironment): Int =
        ScenarioConditionOperandResolver.read(address, env)

    /** 시나리오 메모리 주소에 정수 값을 기록한다. */
    fun writeStageAddress(address: Int, value: Int, env: ScenarioConditionEnvironment) =
        ScenarioConditionOperandResolver.write(address, value, env)

    /** 인코딩된 피연산자 목록에 따라 변수 연산을 적용한다. */
    fun applyStageVarOperation(args: List<Any?>, env: ScenarioConditionEnvironment) {
        val targetKind = args.intAt(0)
        val targetIndex = args.intAt(1)
        val operation = args.intAt(2)
        val sourceKind = args.intAt(3)
        val sourceIndex = args.intAt(4)
        val current = when (targetKind) {
            0 -> readStageAddress(env.pvars[targetIndex].asInt(), env)
            1 -> env.pvars[targetIndex].asInt()
            2 -> env.gvars[targetIndex].asInt()
            else -> 0
        }
        val operand = stageVariableValue(sourceKind, sourceIndex, env)
        val result = when (operation) {
            0 -> current + operand
            1 -> current - operand
            2 -> operand
            3 -> current * operand
            4 -> if (operand == 0) 0 else Math.floorDiv(current, operand)
            5, 6 -> if (operand == 0) 0 else current % operand
            else -> current
        }
        when (targetKind) {
            0 -> writeStageAddress(env.pvars[targetIndex].asInt(), result, env)
            1 -> env.pvars[targetIndex] = result
            2 -> env.gvars[targetIndex] = result
        }
    }

    /** 인코딩된 비교 연산으로 두 시나리오 변수를 비교한다. */
    fun testStageVariables(args: List<Any?>, env: ScenarioConditionEnvironment): Boolean {
        val left = stageVariableValue(args.intAt(0), args.intAt(1), env)
        val right = stageVariableValue(args.intAt(3), args.intAt(4), env)
        return when (args.intAt(2)) {
            0 -> left == right
            1 -> left >= right
            2 -> left <= right
            3 -> left != right
            4 -> left < right
            5 -> left > right
            else -> false
        }
    }

    /** 두 유닛 또는 진영 선택자가 인접해 있는지 확인한다. */
    fun isNear(args: List<Any?>, env: ScenarioConditionEnvironment): Boolean {
        var firstId = args.intAt(0)
        var target = args.intAt(1)
        if (firstId >= 1024) {
            val swapped = firstId
            firstId = target
            target = swapped
        }
        val first = env.battleContext.positions[firstId] ?: return false
        val offsets = if (args.getOrNull(2).asBooleanValue()) {
            env.battleContext.infantryNearOffsets
        } else {
            env.battleContext.attackOffsets[firstId] ?: DEFAULT_CARDINAL_NEAR_OFFSETS
        }
        val covered = offsets.mapTo(hashSetOf()) { (x, y) -> first.first + x to first.second + y }
        if (target < 1024) return env.battleContext.positions[target] in covered
        val campSelector = target - 1024
        val candidates = when (campSelector) {
            1 -> listOf(0, 1).flatMap { env.battleContext.positionsByCamp[it].orEmpty() }
            2 -> listOf(2, 3).flatMap { env.battleContext.positionsByCamp[it].orEmpty() }
            else -> emptyList()
        }
        return candidates.any { it in covered }
    }

    /** 유닛 또는 진영 선택자가 지정 좌표에 있는지 확인한다. */
    fun isInPosition(args: List<Any?>, env: ScenarioConditionEnvironment): Boolean {
        val target = args.intAt(0)
        val x = args.intAt(1)
        val y = args.intAt(2)
        return if (target >= 1024) positionsForFilterSelector(target, env).any { it == (x to y) }
        else env.battleContext.positions[target] == (x to y)
    }

    /** 유닛 또는 진영 선택자가 지정 사각형 안에 있는지 확인한다. */
    fun isInRectangle(args: List<Any?>, env: ScenarioConditionEnvironment): Boolean {
        val target = args.intAt(0)
        val xRange = args.intAt(1)..args.intAt(3)
        val yRange = args.intAt(2)..args.intAt(4)
        val positions = if (target >= 1024) positionsForFilterSelector(
            target,
            env
        ) else listOfNotNull(env.battleContext.positions[target])
        return positions.any { (x, y) -> x in xRange && y in yRange }
    }

    /** 지정 진영의 생존 유닛 중 사각형 안에 있는 수를 센다. */
    fun totalRectangleUnits(args: List<Any?>, env: ScenarioConditionEnvironment): Int {
        val xRange = args.intAt(1)..args.intAt(3)
        val yRange = args.intAt(2)..args.intAt(4)
        val type = args.intAt(0)
        return env.battleContext.positions.count { (id, position) ->
            val camp = env.battleContext.campByCharacterId[id] ?: return@count false
            val hp = env.battleContext.attributes[id]?.get(7) ?: 1
            hp > 0 && sourceUnitTypeMatches(camp, type) && position.first in xRange && position.second in yRange
        }
    }

    /** 지정 진영 선택자에 속한 유닛 수를 센다. */
    fun totalUnits(type: Int, env: ScenarioConditionEnvironment): Int =
        env.battleContext.positionsByCamp.entries.sumOf { (camp, positions) ->
            if (sourceUnitTypeMatches(camp, type)) positions.size else 0
        }

    /** 필터 선택자가 가리키는 유닛 좌표 목록을 반환한다. */
    fun positionsForFilterSelector(selector: Int, env: ScenarioConditionEnvironment): List<Pair<Int, Int>> =
        when (selector) {
            1024 -> env.battleContext.positions.values.toList()
            1025 -> listOf(0, 1).flatMap { env.battleContext.positionsByCamp[it].orEmpty() }
            1026 -> listOf(2, 3).flatMap { env.battleContext.positionsByCamp[it].orEmpty() }
            1027 -> env.battleContext.clickedCharacterId?.let { env.battleContext.positions[it] }?.let(::listOf)
                .orEmpty()

            else -> emptyList()
        }

    /** 유닛 속성 조건을 평가한다. */
    fun unitStateTest(args: List<Any?>, env: ScenarioConditionEnvironment): Boolean =
        ScenarioUnitConditionRules.stateMatches(args, env)
}
