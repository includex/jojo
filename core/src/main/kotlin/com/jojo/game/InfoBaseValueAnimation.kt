package com.jojo.game

import kotlin.math.abs
import kotlin.math.max

/** Production implementation of InfoBaseLayer's queued five-step numeric label animation. */
class InfoBaseValueAnimation(entries: List<Value>) {
    /**
     * data class  `Value`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Value(val index: Int, val source: Int, val destination: Int, val max: Int)

    /**
     * data class  `Update`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Update(val index: Int, val text: String)

    private val queue = entries.toMutableList()
    var current: Value? = queue.removeFirstOrNull()
        private set
    val values: MutableList<Int> = current?.let(::steps) ?: mutableListOf()

    /**
     * Mirrors `_next`, including delivery of JavaScript `undefined` when a
     * retained scheduled callback fires after the final value was consumed.
     */
    /**
     * 공개 메서드 `callback`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Update?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
     * 공개 메서드 `pending`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Value>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun pending(): List<Value> = queue.toList()

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
