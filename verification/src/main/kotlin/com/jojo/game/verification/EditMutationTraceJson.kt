// Verification
package com.jojo.game.verification

/** EditMutationTraceJson: Edit 변이 시나리오 실행기가 공유하는 기준 JSON 조각이다. */
object EditMutationTraceJson {
    /** quote: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    fun quote(value: String): String =
        "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""

    /** array: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    fun array(values: List<String>): String = values.joinToString(",", "[", "]")

    /** snapshot: 현재 추적 상태를 스냅샷으로 만든다. */
    fun snapshot(
        step: String,
        attached: Boolean,
        layers: List<String>,
        toasts: List<String>,
        dispatch: List<Pair<String, Int>>,
        tail: String
    ): String = """
        {"step":${quote(step)},"attached":$attached,"layers":${array(layers.map(::quote))},"toasts":${array(toasts.map(::quote))},"dispatch":${
            dispatch.joinToString(",", "[", "]") { "[${quote(it.first)},${it.second}]" }
        },$tail}
    """.trimIndent().replace("\n", "")
}
