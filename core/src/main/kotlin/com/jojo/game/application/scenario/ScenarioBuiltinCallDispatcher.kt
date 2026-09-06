// Scenario
package com.jojo.game.application.scenario

import com.jojo.game.*
import com.jojo.game.domain.scenario.*

import com.badlogic.gdx.utils.JsonValue

/** 내장 시나리오 호출에 필요한 실행 의존성을 모은다. */
internal data class ScenarioBuiltinCallEnvironment(
    val functions: Map<String, RuntimeFunction>,
    val unhandledCalls: MutableMap<String, Int>,
    val jumpToLabel: (String) -> Unit,
    val pushFunction: (String) -> Unit,
    val eval: (JsonValue, Frame) -> Any?,
)

/** 스크립트의 기본 내장 호출을 처리한다. */
internal object ScenarioBuiltinCallDispatcher {
    /** 내장 호출의 계산 결과를 전달한다. */
    class Result(val value: Any?)

    /**
     * `dispatch`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun dispatch(
        path: String,
        node: JsonValue,
        args: List<Any?>,
        frame: Frame,
        env: ScenarioBuiltinCallEnvironment,
    ): Result {
        /**
         * `value` (Any): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Any = when (path) {
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
                /**
                 * `receiver` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val receiver = node.field("func").takeIf { it.typeName() == "Attribute" }?.field("value")
                    ?.let { env.eval(it, frame) }
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
