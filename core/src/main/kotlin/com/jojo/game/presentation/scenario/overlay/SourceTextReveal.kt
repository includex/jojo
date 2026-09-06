// Scenario
package com.jojo.game.presentation.scenario.overlay

/** SourceTextReveal: 원본 문자열 표시이며, 시나리오 장면을 정확히 표시하기 위한 변환·갱신 규칙을 제공한다. */

class SourceTextReveal {
    private var source = ""
    private var cursor = 0
    private var accumulator = 0f

    val isComplete: Boolean get() = cursor >= source.length
    val visibleText: String get() = source.substring(0, cursor).replace(RICH_TEXT_TAG, "")


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


    fun reset() {
        source = ""
        cursor = 0
        accumulator = 0f
    }

    private fun revealNextSourceUnit() {
        if (source[cursor] != '<') {
            cursor++
            return
        }
        val end = source.indexOf('>', cursor)
        cursor = if (end == -1) cursor + 1 else end + 1
    }

    private companion object {
        const val CHARACTER_INTERVAL = 0.04f
        val RICH_TEXT_TAG = Regex("<[^>]*>")
    }
}
