package com.jojo.game

/**
 * Cocos InfoLayer reveals one printable character every .04 seconds.  Its
 * RichText tags are consumed as one unit, rather than being shown literally.
 */
/**
 * class  `SourceTextReveal`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SourceTextReveal {
    private var source = ""
    private var cursor = 0
    private var accumulator = 0f

    val isComplete: Boolean get() = cursor >= source.length
    val visibleText: String get() = source.substring(0, cursor).replace(RICH_TEXT_TAG, "")

    /**
     * 공개 메서드 `update`
     *
     * ### 파라미터
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `delta` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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

    /** Returns true when a click was consumed to reveal the current line. */
    fun revealAllIfPending(): Boolean {
        if (isComplete) return false
        cursor = source.length
        accumulator = 0f
        return true
    }

    /**
     * 공개 메서드 `reset`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
        // Match InfoLayer: consume a whole RichText tag during this tick.
        val end = source.indexOf('>', cursor)
        cursor = if (end == -1) cursor + 1 else end + 1
    }

    private companion object {
        const val CHARACTER_INTERVAL = 0.04f
        val RICH_TEXT_TAG = Regex("<[^>]*>")
    }
}
