// Presentation
package com.jojo.game.presentation.shared.evidence

import java.util.*

/** RenderEventLog: 프레임별 그리기 호출을 결정적 JSONL 행으로 축적해 렌더링 비교 자료를 만든다. */
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
                // 검증 픽스처는 결정적인 시간을 사용한다. 실행 시간은 공용 스키마에 포함되지만 의미적 동등성 비교에는 사용하지 않는다.
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
