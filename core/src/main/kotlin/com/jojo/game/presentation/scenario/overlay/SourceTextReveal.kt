// Scenario
package com.jojo.game.presentation.scenario.overlay

/** SourceTextReveal: 원본 문자열 표시이며, 시나리오 장면을 정확히 표시하기 위한 변환·갱신 규칙을 제공한다. */

class SourceTextReveal {
    /**
     * `source` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var source = ""
    /**
     * `cursor` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var cursor = 0
    /**
     * `accumulator` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var accumulator = 0f

    /**
     * `isComplete` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val isComplete: Boolean get() = cursor >= source.length
    /**
     * `visibleText` (String get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val visibleText: String get() = source.substring(0, cursor).replace(RICH_TEXT_TAG, "")


    /**
     * `update`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun update(text: String, delta: Float) {
        if (text != source) {
            source = text
            cursor = 0
            accumulator = 0f
        }
        accumulator += delta
        while (accumulator >= CHARACTER_INTERVAL && !isComplete) {
            accumulator -= CHARACTER_INTERVAL
            revealNextSourceUnit()
        }
    }

    /** revealAllIfPending: 진행 중인 글자 표시가 있으면 남은 문장을 즉시 모두 공개한다. */
    fun revealAllIfPending(): Boolean {
        if (isComplete) return false
        cursor = source.length
        accumulator = 0f
        return true
    }


    /**
     * `reset`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun reset() {
        source = ""
        cursor = 0
        accumulator = 0f
    }

    /**
     * `revealNextSourceUnit`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun revealNextSourceUnit() {
        if (source[cursor] != '<') {
            cursor++
            return
        }
        val end = source.indexOf('>', cursor)
        cursor = if (end == -1) cursor + 1 else end + 1
    }

    private companion object {
        /**
         * `CHARACTER_INTERVAL` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val CHARACTER_INTERVAL = 0.04f
        /**
         * `RICH_TEXT_TAG` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val RICH_TEXT_TAG = Regex("<[^>]*>")
    }
}
