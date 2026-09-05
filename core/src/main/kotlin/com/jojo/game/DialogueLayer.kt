package com.jojo.game

import com.jojo.game.domain.battle.*


/** Behavioral implementation of Global1 `ui/DialogueLayer.js` (not Battle SayLayer). */
class DialogueLayer(
    text: String,
    private val unitName: (Int) -> String,
    private val unitY: (Int) -> Float?,
    private val flag: Int = QI_PAO,
    private val onClose: () -> Unit = {},
) {
    /**
     * data class  `Page`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Page(val speakerId: Int, val text: String)

    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class View(
        val attached: Boolean,
        val bubble: Int,
        val top: Boolean,
        val speakerId: Int,
        val speaker: String,
        val content: String,
        val typing: Boolean,
    )

    private val pages = parse(text)
    private var pageIndex = -1
    private var page: Page? = null
    private var speakerName = ""
    private var bubbleAtTop = false
    private var target = ""
    private var content = ""
    private var renderedContent = ""
    private var autoCloseRemaining: Float? = null
    var attached = true
        private set
    val events = mutableListOf<String>()

    init {
        nextPage()
    }

    /**
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view(): View {
        val current = requireNotNull(page)
        return View(
            attached, bubbleIndex and 1, bubbleAtTop,
            current.speakerId, speakerName, renderedContent, content != target
        )
    }

    /** One source typewriter callback: one character, or one complete markup tag. */
    fun typeTick(): Boolean {
        if (!attached || content == target) return false
        val start = content.length
        var end = start + 1
        if (target[start] == '<') {
            val close = target.indexOf('>', start + 1)
            if (close >= 0) end = close + 1
        }
        content = target.substring(0, end.coerceAtMost(target.length))
        // DialogueLayer temporarily balances an opening RichText color tag on
        // every typewriter callback.  A panel tap bypasses this and publishes
        // the untouched full source string.
        renderedContent = content + if (content.contains("<color=")) "</c>" else ""
        if (content == target) enableAutoClose()
        return true
    }

    /**
     * 공개 메서드 `completeTyping`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun completeTyping() {
        if (!attached || content == target) return
        content = target
        renderedContent = target
        enableAutoClose()
    }

    /** Full-panel TOUCH_END: finish typing first, then advance on the next tap. */
    fun touch(event: Int): Boolean {
        if (!attached || event != TOUCH_END) return false
        if (content != target) completeTyping() else {
            autoCloseRemaining = null
            nextPage()
        }
        return true
    }

    /**
     * 공개 메서드 `skip`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun skip() {
        if (attached) close()
    }

    /**
     * 공개 메서드 `advance`
     *
     * ### 파라미터
    - `seconds` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun advance(seconds: Float) {
        val remaining = autoCloseRemaining ?: return
        val next = remaining - seconds.coerceAtLeast(0f)
        if (next <= 0f) {
            autoCloseRemaining = null
            nextPage()
        } else autoCloseRemaining = next
    }

    private fun enableAutoClose() {
        if (flag and AUTO_CLOSE != 0) autoCloseRemaining = 1.6f
    }

    private fun nextPage() {
        pageIndex++
        if (pageIndex >= pages.size) {
            close()
            return
        }
        val next = pages[pageIndex]
        // The source only updates its static alternation state when Hall.unit
        // resolves.  Missing units retain the currently selected bubble.
        val y = unitY(next.speakerId)
        if (y != null) {
            if (next.speakerId != lastSpeakerId) bubbleIndex++
            if (next.speakerId == 0) bubbleIndex = 0
            lastSpeakerId = next.speakerId
        }
        page = next
        speakerName = unitName(next.speakerId)
        bubbleAtTop = y != null && y < -50f
        target = next.text
        content = ""
        renderedContent = ""
        if (flag and QI_PAO != 0) events += "SHOW_SAY:${next.speakerId}"
    }

    private fun close() {
        if (!attached) return
        if (flag and QI_PAO != 0) events += "HIDE_SAY"
        attached = false
        onClose()
    }

    companion object {
        const val AUTO_CLOSE = 1
        const val QI_PAO = 2
        const val TOUCH_END = 2
        private var bubbleIndex = 0
        private var lastSpeakerId = -1

        fun resetAlternation() {
            bubbleIndex = 0
            lastSpeakerId = -1
        }

        private fun parse(raw: String): List<Page> {
            val tokens = raw.replace("\r\n", "\n").replace('\r', '\n')
                .replace("\n", "<br/>").split("<br/>").filter(String::isNotEmpty)
            val pages = mutableListOf<Page>()
            var speaker: Int? = null
            val lines = mutableListOf<String>()

            /**
             * 공개 메서드 `flush`
             *
             * ### 파라미터
            - 입력 파라미터: 없음
             *
             * ### 응답 스펙
             * - 반환 타입: `Unit`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun flush() {
                val id = speaker ?: return
                while (lines.isNotEmpty()) {
                    pages += Page(id, lines.take(3).joinToString("<br/>"))
                    repeat(minOf(3, lines.size)) { lines.removeAt(0) }
                }
            }
            tokens.forEach { token ->
                if (token.startsWith("&")) {
                    flush()
                    speaker = token.drop(1).toInt()
                } else if (speaker != null) lines += token
            }
            flush()
            return pages
        }
    }
}
