// Scenario
package com.jojo.game.presentation.scenario.overlay

import kotlin.math.max
import kotlin.math.min

/** MsgBox3Layer: 수량 입력 모달로, 허용 범위 안의 값만 유지하고 확인 결과를 콜백으로 전달한다. */

class MsgBox3Layer(
    val count: Double,
    titleTemplate: String,
    val confirmLabel: String,
    private val callback: (Double) -> Unit,
    private val inputChanged: (Double) -> Unit = {},
) {
    var attached: Boolean = true
        private set
    var value: Double = 1.0
        private set
    var editText: String = "1"
        private set
    val title: String = formatOne(titleTemplate, count)


    fun textChanged(text: String) {
        val parsed = jsNumber(text)
        val next = max(1.0, min(parsed, count))
        val normalized = jsString(next)
        editText = if (normalized != text) normalized else text
        if (value != next) {
            value = next
            inputChanged(next)
        }
    }

    /** touchButton: 확인 또는 취소 버튼 터치를 처리하고, 확정한 수량을 콜백으로 전달한다. */
    fun touchButton(tag: Int, eventType: Int) {
        if (eventType != 2) return
        attached = false
        callback(if (tag == 0) value else 0.0)
    }

    /** touchOutside: 모달 바깥쪽 터치로 입력 창을 취소할지 판정한다. */
    fun touchOutside() = Unit

    companion object {
        private fun formatOne(template: String, number: Double): String =
            template.replaceFirst(Regex("%[2-9]*[ds]"), jsString(number))

        internal fun jsNumber(raw: String): Double {
            val text = raw.trim()
            if (text.isEmpty()) return 0.0
            val sign = when {
                text.startsWith("-") -> -1.0
                else -> 1.0
            }
            val unsigned = if (text.startsWith("+") || text.startsWith("-")) text.drop(1) else text
            return when {
                unsigned.startsWith("0x", true) -> unsigned.drop(2).toLongOrNull(16)?.times(sign) ?: Double.NaN
                unsigned.startsWith("0b", true) -> unsigned.drop(2).toLongOrNull(2)?.times(sign) ?: Double.NaN
                unsigned.startsWith("0o", true) -> unsigned.drop(2).toLongOrNull(8)?.times(sign) ?: Double.NaN
                else -> text.toDoubleOrNull() ?: Double.NaN
            }
        }

        internal fun jsString(value: Double): String = when {
            value.isNaN() -> "NaN"
            value == Double.POSITIVE_INFINITY -> "Infinity"
            value == Double.NEGATIVE_INFINITY -> "-Infinity"
            value == 0.0 -> "0"
            value % 1.0 == 0.0 -> value.toLong().toString()
            else -> value.toString()
        }
    }
}
