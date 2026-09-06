// Scenario
package com.jojo.game.presentation.scenario.overlay

import com.jojo.game.domain.battle.*


/** DialogueLayer: 시나리오 대사를 화자별 페이지로 나누고, 말풍선 위치·타이핑·닫기 상태를 제공한다. */
class DialogueLayer(
    text: String,
    /** `unitName` ((Int) -> String): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val unitName: (Int) -> String,
    /** `unitY` ((Int) -> Float?): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val unitY: (Int) -> Float?,
    /** `flag` (Int): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val flag: Int = QI_PAO,
    /** `onClose` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val onClose: () -> Unit = {},
) {

    /**
     * `Page`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class Page(val speakerId: Int, val text: String)


    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(
        /**
         * `attached` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val attached: Boolean,
        /**
         * `bubble` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val bubble: Int,
        /**
         * `top` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val top: Boolean,
        /**
         * `speakerId` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val speakerId: Int,
        /**
         * `speaker` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val speaker: String,
        /**
         * `content` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val content: String,
        /**
         * `typing` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val typing: Boolean,
    )

    /**
     * `pages` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private val pages = parse(text)
    /**
     * `pageIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var pageIndex = -1
    /**
     * `page` (Page?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var page: Page? = null
    /**
     * `speakerName` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var speakerName = ""
    /**
     * `bubbleAtTop` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var bubbleAtTop = false
    /**
     * `target` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var target = ""
    /**
     * `content` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var content = ""
    /**
     * `renderedContent` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var renderedContent = ""
    /**
     * `autoCloseRemaining` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var autoCloseRemaining: Float? = null
    /**
     * `attached` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var attached = true
        private set
    /**
     * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val events = mutableListOf<String>()

    init {
        nextPage()
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(): View {
        val current = requireNotNull(page)
        return View(
            attached, bubbleIndex and 1, bubbleAtTop,
            current.speakerId, speakerName, renderedContent, content != target
        )
    }

    /** typeTick: 타이핑 속도에 따라 대사 본문에서 새로 공개할 글자를 계산한다. */
    fun typeTick(): Boolean {
        if (!attached || content == target) return false
        val start = content.length
        var end = start + 1
        if (target[start] == '<') {
            val close = target.indexOf('>', start + 1)
            if (close >= 0) end = close + 1
        }
        content = target.substring(0, end.coerceAtMost(target.length))
        renderedContent = content + if (content.contains("<color=")) "</c>" else ""
        if (content == target) enableAutoClose()
        return true
    }


    /**
     * `completeTyping`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun completeTyping() {
        if (!attached || content == target) return
        content = target
        renderedContent = target
        enableAutoClose()
    }

    /** touch: 대사를 즉시 공개하거나 다음 페이지로 넘기고, 마지막 페이지에서는 닫기 콜백을 실행한다. */
    fun touch(event: Int): Boolean {
        if (!attached || event != TOUCH_END) return false
        if (content != target) completeTyping() else {
            autoCloseRemaining = null
            nextPage()
        }
        return true
    }


    /**
     * `skip`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun skip() {
        if (attached) close()
    }


    /**
     * `advance`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun advance(seconds: Float) {
        val remaining = autoCloseRemaining ?: return
        val next = remaining - seconds.coerceAtLeast(0f)
        if (next <= 0f) {
            autoCloseRemaining = null
            nextPage()
        } else autoCloseRemaining = next
    }

    /**
     * `enableAutoClose`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun enableAutoClose() {
        if (flag and AUTO_CLOSE != 0) autoCloseRemaining = 1.6f
    }

    /**
     * `nextPage`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun nextPage() {
        pageIndex++
        if (pageIndex >= pages.size) {
            close()
            return
        }
        val next = pages[pageIndex]
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

    /**
     * `close`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun close() {
        if (!attached) return
        if (flag and QI_PAO != 0) events += "HIDE_SAY"
        attached = false
        onClose()
    }

    companion object {
        /**
         * `AUTO_CLOSE` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val AUTO_CLOSE = 1
        /**
         * `QI_PAO` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val QI_PAO = 2
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
        /**
         * `bubbleIndex` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private var bubbleIndex = 0
        /**
         * `lastSpeakerId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        private var lastSpeakerId = -1

        /**
         * `resetAlternation`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun resetAlternation() {
            bubbleIndex = 0
            lastSpeakerId = -1
        }

        /**
         * `parse`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        private fun parse(raw: String): List<Page> {
            val tokens = raw.replace("\r\n", "\n").replace('\r', '\n')
                .replace("\n", "<br/>").split("<br/>").filter(String::isNotEmpty)
            val pages = mutableListOf<Page>()
            var speaker: Int? = null
            val lines = mutableListOf<String>()


            /**
             * `flush`: 타입의 핵심 동작을 수행한다.
             * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
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
