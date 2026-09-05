package com.jojo.game.verification

/** Canonical JSON fragments shared by the Edit mutation scenario runners. */
object EditMutationTraceJson {
    fun quote(value: String): String =
        "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""

    fun array(values: List<String>): String = values.joinToString(",", "[", "]")

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
