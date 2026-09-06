// Scenario
package com.jojo.game.presentation.scenario.overlay

import kotlin.math.max
import kotlin.math.min

/** MsgBox3Layer: 수량 입력 모달로, 허용 범위 안의 값만 유지하고 확인 결과를 콜백으로 전달한다. */

class MsgBox3Layer(
    val count: Double,
    titleTemplate: String,
    val confirmLabel: String,
    /** `callback` ((Double) -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val callback: (Double) -> Unit,
    /** `inputChanged` ((Double) -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val inputChanged: (Double) -> Unit = {},
) {
    /**
     * `attached` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached: Boolean = true
        private set
    /**
     * `value` (Double): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var value: Double = 1.0
        private set
    /**
     * `editText` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var editText: String = "1"
        private set
    /**
     * `title` (String): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val title: String = formatOne(titleTemplate, count)


    /**
     * `textChanged`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
        /**
         * `formatOne`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        private fun formatOne(template: String, number: Double): String =
            template.replaceFirst(Regex("%[2-9]*[ds]"), jsString(number))

        /**
         * `jsNumber`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

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

        /**
         * `jsString`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

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
