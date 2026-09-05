package com.jojo.game.application.scenario

import com.jojo.game.*

internal data class ScenarioConditionEnvironment(
    val gvars: MutableMap<Int, Any?>,
    val pvars: MutableMap<Int, Any?>,
    val battleContext: ScenarioInterpreter.BattleScriptContext,
    val stageUnitAttribute: (Int, Int) -> Int,
)

internal fun sourceUnitTypeMatches(camp: Int, selector: Int): Boolean = when (selector) {
    0, 1, 2, 3 -> camp == selector
    4 -> camp <= 1
    5 -> camp >= 2
    6 -> true
    else -> false
}

/**
 * Evaluates game script conditions: variable arithmetic, memory address read/write,
 * spatial adjacency (isNear), positional containment, and unit attribute tests.
 */
internal object ScenarioConditionEvaluator {
    const val ADDRESS_INTVAR_START = 5_251_072
    const val ADDRESS_INTVAR_END = 5_255_168
    val DEFAULT_CARDINAL_NEAR_OFFSETS = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
    val DEFAULT_INFANTRY_NEAR_OFFSETS = DEFAULT_CARDINAL_NEAR_OFFSETS + setOf(1 to 1, -1 to 1, 1 to -1, -1 to -1)

    /**
     * 공개 메서드 `stageVariableValue`
     *
     * ### 파라미터
    - `kind` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `value` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun stageVariableValue(kind: Int, value: Int, env: ScenarioConditionEnvironment): Int = when (kind) {
        0 -> value
        1 -> readStageAddress(env.pvars[value].asInt(), env)
        2 -> env.pvars[value].asInt()
        4 -> env.gvars[value].asInt()
        5 -> ADDRESS_INTVAR_START + 4 * value
        else -> 0
    }

    /**
     * 공개 메서드 `readStageAddress`
     *
     * ### 파라미터
    - `address` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun readStageAddress(address: Int, env: ScenarioConditionEnvironment): Int =
        if (address in ADDRESS_INTVAR_START until ADDRESS_INTVAR_END) {
            env.gvars[(address - ADDRESS_INTVAR_START) / 4].asInt()
        } else 0

    /**
     * 공개 메서드 `writeStageAddress`
     *
     * ### 파라미터
    - `address` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `value` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun writeStageAddress(address: Int, value: Int, env: ScenarioConditionEnvironment) {
        if (address in ADDRESS_INTVAR_START until ADDRESS_INTVAR_END) {
            env.gvars[(address - ADDRESS_INTVAR_START) / 4] = value
        }
    }

    /**
     * 공개 메서드 `applyStageVarOperation`
     *
     * ### 파라미터
    - `args` (`List<Any?>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `testStageVariables`
     *
     * ### 파라미터
    - `args` (`List<Any?>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `isNear`
     *
     * ### 파라미터
    - `args` (`List<Any?>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `isInPosition`
     *
     * ### 파라미터
    - `args` (`List<Any?>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun isInPosition(args: List<Any?>, env: ScenarioConditionEnvironment): Boolean {
        val target = args.intAt(0)
        val x = args.intAt(1)
        val y = args.intAt(2)
        return if (target >= 1024) positionsForFilterSelector(target, env).any { it == (x to y) }
        else env.battleContext.positions[target] == (x to y)
    }

    /**
     * 공개 메서드 `isInRectangle`
     *
     * ### 파라미터
    - `args` (`List<Any?>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `totalRectangleUnits`
     *
     * ### 파라미터
    - `args` (`List<Any?>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `totalUnits`
     *
     * ### 파라미터
    - `type` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun totalUnits(type: Int, env: ScenarioConditionEnvironment): Int =
        env.battleContext.positionsByCamp.entries.sumOf { (camp, positions) ->
            if (sourceUnitTypeMatches(camp, type)) positions.size else 0
        }

    /**
     * 공개 메서드 `positionsForFilterSelector`
     *
     * ### 파라미터
    - `selector` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Pair<Int, Int>>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun positionsForFilterSelector(selector: Int, env: ScenarioConditionEnvironment): List<Pair<Int, Int>> =
        when (selector) {
            1024 -> env.battleContext.positions.values.toList()
            1025 -> listOf(0, 1).flatMap { env.battleContext.positionsByCamp[it].orEmpty() }
            1026 -> listOf(2, 3).flatMap { env.battleContext.positionsByCamp[it].orEmpty() }
            1027 -> env.battleContext.clickedCharacterId?.let { env.battleContext.positions[it] }?.let(::listOf)
                .orEmpty()

            else -> emptyList()
        }

    /**
     * 공개 메서드 `unitStateTest`
     *
     * ### 파라미터
    - `args` (`List<Any?>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioConditionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun unitStateTest(args: List<Any?>, env: ScenarioConditionEnvironment): Boolean {
        val unitId = args.intAt(0)
        val attribute = args.intAt(1)
        val compared = args.intAt(2)
        val mode = args.intAt(3)
        val value = env.battleContext.attributes[unitId]?.get(attribute)
            ?: env.stageUnitAttribute(unitId, attribute)
        return when (mode) {
            0 -> value >= compared
            1 -> value < compared
            2 -> value == compared
            3 -> value != compared
            else -> false
        }
    }
}
