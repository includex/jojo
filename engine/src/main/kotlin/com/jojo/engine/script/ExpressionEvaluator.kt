package com.jojo.engine.script

import com.badlogic.gdx.utils.JsonValue

/** 표현식 평가에 필요한 변수 범위와 호출 함수를 모은다. */
data class ExpressionEnvironment(
    val bindings: Map<String, Any?> = emptyMap(),
    val globalVariables: Map<String, Any?>,
    val invokeCall: (JsonValue, ScriptFrame) -> Any?,
)

/** 호스트 컨테이너의 이름 기반 첨자 접근을 제공한다. */
interface NamedIndexAccess {
    fun getNamedValue(name: String): Any?
    fun setNamedValue(name: String, value: Any?)
}

/** 스크립트 AST의 리터럴, 변수, 연산, 호출 표현식을 평가한다. */
object ExpressionEvaluator {

    /** AST 표현식 하나를 현재 실행 프레임에서 평가한다. */
    fun eval(node: JsonValue, frame: ScriptFrame, env: ExpressionEnvironment): Any? = when (node.typeName()) {
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
    fun evalCompare(node: JsonValue, frame: ScriptFrame, env: ExpressionEnvironment): Boolean {
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
    fun evalBinary(node: JsonValue, frame: ScriptFrame, env: ExpressionEnvironment): Any {
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

    /** 표현식 결과를 스크립트의 불리언 규칙으로 변환한다. */
    fun evalBoolean(node: JsonValue, frame: ScriptFrame, env: ExpressionEnvironment): Boolean =
        when (val value = eval(node, frame, env)) {
            is Boolean -> value
            is Number -> value.toInt() != 0
            is String -> value.isNotEmpty()
            null -> false
            else -> true
        }

    /** 현재 프레임과 전역 범위에서 이름을 해석한다. */
    fun lookupName(name: String, frame: ScriptFrame, env: ExpressionEnvironment): Any =
        if (name in env.bindings) env.bindings[name] ?: 0
        else frame.locals[name] ?: env.globalVariables[name] ?: 0

    /** 첨자 접근 AST에서 컨테이너 값을 읽는다. */
    fun readSubscript(node: JsonValue, frame: ScriptFrame, env: ExpressionEnvironment): Any? {
        val container = eval(node.field("value"), frame, env)
        val index = eval(node.field("slice"), frame, env)
        return when {
            container is NamedIndexAccess && index is String -> container.getNamedValue(index) ?: 0
            container is Map<*, *> -> container[if (index is String) index else index.asInt()] ?: 0
            container is List<*> -> container.getOrElse(index.asInt()) { 0 }
            else -> 0
        }
    }

    /** 대입 대상 AST에 계산된 값을 기록한다. */
    fun assign(target: JsonValue, value: Any?, frame: ScriptFrame, env: ExpressionEnvironment) {
        when (target.typeName()) {
            "Name" -> frame.locals[target.field("id").asString()] = value
            "Subscript" -> {
                val container = eval(target.field("value"), frame, env)
                val index = eval(target.field("slice"), frame, env)
                if (container is NamedIndexAccess && index is String) {
                    container.setNamedValue(index, value)
                } else {
                    @Suppress("UNCHECKED_CAST")
                    (container as? MutableMap<Any?, Any?>)?.set(if (index is String) index else index.asInt(), value)
                }
            }
        }
    }

    /** 호출 인수 목록을 순서대로 평가한다. */
    fun evalArguments(args: JsonValue, frame: ScriptFrame, env: ExpressionEnvironment): List<Any?> =
        args.children().map { eval(it, frame, env) }.toList()
}
