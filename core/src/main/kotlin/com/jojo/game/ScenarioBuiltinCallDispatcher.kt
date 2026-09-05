package com.jojo.game

import com.badlogic.gdx.utils.JsonValue

internal data class ScenarioBuiltinCallEnvironment(
    val functions: Map<String, RuntimeFunction>,
    val unhandledCalls: MutableMap<String, Int>,
    val jumpToLabel: (String) -> Unit,
    val pushFunction: (String) -> Unit,
    val eval: (JsonValue, Frame) -> Any?,
)

internal object ScenarioBuiltinCallDispatcher {
    class Result(val value: Any?)

    fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioBuiltinCallEnvironment,
    ): Result? {
        val value: Any? = when (path) {
            "label" -> 0
            "goto" -> {
                env.jumpToLabel(args.firstOrNull().asText())
                0
            }
            "call" -> {
                env.pushFunction(args.firstOrNull().asText())
                0
            }
            "hasFunc" -> env.functions.containsKey(args.firstOrNull().asText())
            "range" -> (args.firstOrNull().asInt() until args.getOrNull(1).asInt()).toList()
            "len" -> args.firstOrNull().asList().size
            "int" -> args.firstOrNull().asInt()
            "str" -> args.firstOrNull().asText()
            else -> {
                val receiver = node.field("func").takeIf { it.typeName() == "Attribute" }?.field("value")?.let { env.eval(it, frame) }
                if (path.endsWith(".push") || path.endsWith(".append")) {
                    @Suppress("UNCHECKED_CAST")
                    (receiver as? MutableList<Any?>)?.add(args.firstOrNull())
                } else if (path in env.functions) {
                    env.pushFunction(path)
                } else if (path.isNotEmpty()) {
                    env.unhandledCalls[path] = (env.unhandledCalls[path] ?: 0) + 1
                }
                0
            }
        }
        return Result(value)
    }
}
