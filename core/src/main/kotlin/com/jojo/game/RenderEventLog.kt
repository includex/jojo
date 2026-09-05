package com.jojo.game

import java.util.*

/** A deterministic, renderer-coordinate JSONL stream for source/game frame comparison. */
class RenderEventLog(private val frame: Int = 0, private val sequenceOffset: Int = 0) {
    private val lines = mutableListOf<String>()

    fun draw(
        phase: String,
        layer: String,
        nodePath: String,
        drawType: String,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        assetId: String? = null,
        opacity: Float = 1f,
        blend: Any = listOf(770, 771),
        visible: Boolean = true,
        text: String = "",
    ) {
        val sequence = sequenceOffset + lines.size
        lines += "{" +
                // Verification fixtures use a deterministic timestamp. Runtime
                // timing is deliberately present in the shared schema but does
                // not participate in semantic parity comparisons.
                "\"sequence\":$sequence,\"frame\":$frame,\"timestamp\":0," +
                "\"phase\":\"${escape(phase)}\",\"layer\":\"${escape(layer)}\"," +
                "\"nodePath\":\"${escape(nodePath)}\",\"drawType\":\"${escape(drawType)}\"," +
                "\"x\":${number(x)},\"y\":${number(y)},\"w\":${number(w)},\"h\":${number(h)}," +
                "\"assetId\":${assetId?.let { "\"${escape(it)}\"" } ?: "null"},\"opacity\":${number(opacity)}," +
                "\"blend\":${jsonValue(blend)},\"visible\":$visible,\"text\":${
                    if (text.isEmpty()) "null" else "\"${
                        escape(
                            text
                        )
                    }\""
                }}"
    }

    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(): String = lines.joinToString(separator = "\n", postfix = if (lines.isEmpty()) "" else "\n")

    private fun number(value: Float): String = String.format(Locale.US, "%.3f", value)

    private fun jsonValue(value: Any): String = when (value) {
        is String -> "\"${escape(value)}\""
        is Number, is Boolean -> value.toString()
        is Iterable<*> -> value.joinToString(
            prefix = "[",
            postfix = "]"
        ) { child -> if (child == null) "null" else jsonValue(child) }

        else -> "\"${escape(value.toString())}\""
    }

    private fun escape(value: String): String = buildString(value.length) {
        value.forEach { char ->
            when (char) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> append(char)
            }
        }
    }
}
