package com.jojo.game

import com.badlogic.gdx.utils.JsonValue

internal data class ScenarioExpressionEnvironment(
    val vars: Map<Int, Any?>,
    val gvars: Map<Int, Any?>,
    val pvars: Map<Int, Any?>,
    val globalVariables: Map<String, Any?>,
    val invokeCall: (JsonValue, Frame) -> Any?,
)

/**
 * Evaluates Python AST expressions: literals, variables, subscript access, binary/comparison ops, and function calls.
 */
internal object ScenarioExpressionEvaluator {

    /**
     * 공개 메서드 `eval`
     *
     * ### 파라미터
    - `node` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Frame`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioExpressionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Any?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun eval(node: JsonValue, frame: Frame, env: ScenarioExpressionEnvironment): Any? = when (node.typeName()) {
        "Constant" -> node.field("value").value()
        "Name" -> lookupName(node.field("id").asString(), frame, env)
        "List", "Tuple" -> node.field("elts").children().mapTo(mutableListOf()) { eval(it, frame, env) }
        "Dict" -> node.field("keys").children().zip(node.field("values").children()).associate {
            eval(it.first, frame, env).toString() to eval(it.second, frame, env)
        }.toMutableMap()

        "Subscript" -> readSubscript(node, frame, env)
        "UnaryOp" -> when (node.field("op").typeName()) {
            "Not" -> !evalBoolean(node.field("operand"), frame, env)
            "USub" -> -eval(node.field("operand"), frame, env).asInt()
            else -> 0
        }

        "BoolOp" -> {
            val values = node.field("values").children().toList()
            if (node.field("op").typeName() == "And") values.all {
                evalBoolean(
                    it,
                    frame,
                    env
                )
            } else values.any { evalBoolean(it, frame, env) }
        }

        "Compare" -> evalCompare(node, frame, env)
        "BinOp" -> evalBinary(node, frame, env)
        "Call" -> env.invokeCall(node, frame)
        "JoinedStr" -> node.field("values").children().joinToString("") { eval(it, frame, env).asText() }
        "FormattedValue" -> eval(node.field("value"), frame, env).asText()
        "Attribute" -> 0
        else -> 0
    }

    /**
     * 공개 메서드 `evalCompare`
     *
     * ### 파라미터
    - `node` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Frame`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioExpressionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun evalCompare(node: JsonValue, frame: Frame, env: ScenarioExpressionEnvironment): Boolean {
        val left = eval(node.field("left"), frame, env)
        val right = node.field("comparators").children().firstOrNull()?.let { eval(it, frame, env) }
        return when (node.field("ops").children().firstOrNull()?.typeName()) {
            "Eq" -> left == right
            "NotEq" -> left != right
            "Lt" -> left.asInt() < right.asInt()
            "LtE" -> left.asInt() <= right.asInt()
            "Gt" -> left.asInt() > right.asInt()
            "GtE" -> left.asInt() >= right.asInt()
            "In" -> left in right.asList()
            else -> false
        }
    }

    /**
     * 공개 메서드 `evalBinary`
     *
     * ### 파라미터
    - `node` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Frame`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioExpressionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Any`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun evalBinary(node: JsonValue, frame: Frame, env: ScenarioExpressionEnvironment): Any {
        val left = eval(node.field("left"), frame, env)
        val right = eval(node.field("right"), frame, env)
        return when (node.field("op").typeName()) {
            "Add" -> if (left is String || right is String) left.asText() + right.asText() else left.asInt() + right.asInt()
            "Sub" -> left.asInt() - right.asInt()
            "Mult" -> left.asInt() * right.asInt()
            "Mod" -> left.asInt() % right.asInt()
            "LShift" -> left.asInt() shl right.asInt()
            "BitOr" -> left.asInt() or right.asInt()
            else -> 0
        }
    }

    /**
     * 공개 메서드 `evalBoolean`
     *
     * ### 파라미터
    - `node` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Frame`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioExpressionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun evalBoolean(node: JsonValue, frame: Frame, env: ScenarioExpressionEnvironment): Boolean =
        when (val value = eval(node, frame, env)) {
            is Boolean -> value
            is Number -> value.toInt() != 0
            is String -> value.isNotEmpty()
            null -> false
            else -> true
        }

    /**
     * 공개 메서드 `lookupName`
     *
     * ### 파라미터
    - `name` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Frame`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioExpressionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Any?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun lookupName(name: String, frame: Frame, env: ScenarioExpressionEnvironment): Any = when (name) {
        "vars" -> env.vars
        "gvars" -> env.gvars
        "pvars" -> env.pvars
        else -> frame.locals[name] ?: env.globalVariables[name] ?: 0
    }

    /**
     * 공개 메서드 `readSubscript`
     *
     * ### 파라미터
    - `node` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Frame`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioExpressionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Any?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun readSubscript(node: JsonValue, frame: Frame, env: ScenarioExpressionEnvironment): Any? {
        val container = eval(node.field("value"), frame, env)
        val index = eval(node.field("slice"), frame, env).asInt()
        return when (container) {
            is Map<*, *> -> container[index] ?: 0
            is List<*> -> container.getOrElse(index) { 0 }
            else -> 0
        }
    }

    /**
     * 공개 메서드 `assign`
     *
     * ### 파라미터
    - `target` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `value` (`Any?`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Frame`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioExpressionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun assign(target: JsonValue, value: Any?, frame: Frame, env: ScenarioExpressionEnvironment) {
        when (target.typeName()) {
            "Name" -> frame.locals[target.field("id").asString()] = value
            "Subscript" -> {
                val container = eval(target.field("value"), frame, env)
                val index = eval(target.field("slice"), frame, env).asInt()
                @Suppress("UNCHECKED_CAST")
                (container as? MutableMap<Int, Any?>)?.set(index, value)
            }
        }
    }

    /**
     * 공개 메서드 `evalArguments`
     *
     * ### 파라미터
    - `args` (`JsonValue`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `frame` (`Frame`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `env` (`ScenarioExpressionEnvironment`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Any?>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun evalArguments(args: JsonValue, frame: Frame, env: ScenarioExpressionEnvironment): List<Any?> =
        args.children().map { eval(it, frame, env) }.toList()
}
