package com.jojo.game.application.scenario

import com.jojo.game.*

import com.badlogic.gdx.utils.JsonValue

/** 표현식 평가에 필요한 변수 범위와 호출 함수를 모은다. */
internal data class ScenarioExpressionEnvironment(
    val vars: Map<Int, Any?>,
    val gvars: Map<Int, Any?>,
    val pvars: Map<Int, Any?>,
    val globalVariables: Map<String, Any?>,
    val invokeCall: (JsonValue, Frame) -> Any?,
)

/** 시나리오 AST의 리터럴, 변수, 연산, 호출 표현식을 평가한다. */
internal object ScenarioExpressionEvaluator {

    /** AST 표현식 하나를 현재 실행 프레임에서 평가한다. */
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

    /** 비교 AST를 평가한다. */
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

    /** 이항 연산 AST를 평가한다. */
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

    /** 표현식 결과를 시나리오의 불리언 규칙으로 변환한다. */
    fun evalBoolean(node: JsonValue, frame: Frame, env: ScenarioExpressionEnvironment): Boolean =
        when (val value = eval(node, frame, env)) {
            is Boolean -> value
            is Number -> value.toInt() != 0
            is String -> value.isNotEmpty()
            null -> false
            else -> true
        }

    /** 현재 프레임과 전역 범위에서 이름을 해석한다. */
    fun lookupName(name: String, frame: Frame, env: ScenarioExpressionEnvironment): Any = when (name) {
        "vars" -> env.vars
        "gvars" -> env.gvars
        "pvars" -> env.pvars
        else -> frame.locals[name] ?: env.globalVariables[name] ?: 0
    }

    /** 첨자 접근 AST에서 컨테이너 값을 읽는다. */
    fun readSubscript(node: JsonValue, frame: Frame, env: ScenarioExpressionEnvironment): Any? {
        val container = eval(node.field("value"), frame, env)
        val index = eval(node.field("slice"), frame, env).asInt()
        return when (container) {
            is Map<*, *> -> container[index] ?: 0
            is List<*> -> container.getOrElse(index) { 0 }
            else -> 0
        }
    }

    /** 대입 대상 AST에 계산된 값을 기록한다. */
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

    /** 호출 인수 목록을 순서대로 평가한다. */
    fun evalArguments(args: JsonValue, frame: Frame, env: ScenarioExpressionEnvironment): List<Any?> =
        args.children().map { eval(it, frame, env) }.toList()
}
