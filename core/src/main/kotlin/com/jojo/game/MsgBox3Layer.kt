package com.jojo.game

import kotlin.math.max
import kotlin.math.min

/**
 * Cocos-free behavioral implementation of the recovered Global118 MsgBox3 prefab.
 *
 * The production callers are BuyLayer's property purchase route and
 * SellLayer's property sale route.  Values intentionally remain Double:
 * JavaScript's Number/edit-box path accepts fractional and NaN values even
 * though both normal callers provide integer limits.
 */
/**
 * class  `MsgBox3Layer`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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

    /**
     * 공개 메서드 `textChanged`
     *
     * ### 파라미터
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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

    /** The recovered prefab reacts only to Cocos TOUCH_END (event type 2). */
    fun touchButton(tag: Int, eventType: Int) {
        if (eventType != 2) return
        attached = false
        callback(if (tag == 0) value else 0.0)
    }

    /** Panel_cancel is inactive; it is a prefab blocker, not a close action. */
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
