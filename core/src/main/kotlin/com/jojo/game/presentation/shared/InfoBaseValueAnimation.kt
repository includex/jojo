// Game
package com.jojo.game.presentation.shared

import kotlin.math.abs
import kotlin.math.max

/** InfoBaseValueAnimation: 정보창 숫자 라벨의 다섯 단계 대기 애니메이션을 구현한다. */
class InfoBaseValueAnimation(entries: List<Value>) {

    /**
     * `Value`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Value(val index: Int, val source: Int, val destination: Int, val max: Int)


    /**
     * `Update`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Update(val index: Int, val text: String)

    /**
     * `queue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val queue = entries.toMutableList()
    /**
     * `current` (Value?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var current: Value? = queue.removeFirstOrNull()
        private set
    /**
     * `values` (MutableList<Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val values: MutableList<Int> = current?.let(::steps) ?: mutableListOf()

    /** 마지막 값 이후 예약 콜백이 실행되어도 undefined에 해당하는 상태를 전달하는 원본 _next 동작을 재현한다. */

    fun callback(): Update? {
        val value = current ?: return null
        val text = if (values.isNotEmpty()) values.removeAt(0).toString() else "undefined"
        if (values.isEmpty() && queue.isNotEmpty()) {
            current = queue.removeFirst()
            values += steps(requireNotNull(current))
        }
        return Update(value.index, text)
    }


    /**
     * `pending`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun pending(): List<Value> = queue.toList()

    /**
     * `steps`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun steps(value: Value): MutableList<Int> {
        var remaining = abs(value.source - value.destination)
        val sign = if (value.destination > value.source) 1 else -1
        val step = max(remaining / 5, 1)
        val deltas = mutableListOf<Int>()
        var count = 0
        while (count < 4 && remaining > 0) {
            deltas.add(0, step * sign); remaining -= step; count++
        }
        if (remaining > 0) deltas.add(0, remaining * sign)
        var currentValue = value.source
        return deltas.mapTo(mutableListOf()) { currentValue += it; currentValue }
    }
}
