package com.jojo.game

/** Behavioral implementation of Global1 `ui/DialogueLayer.js` (not Battle SayLayer). */
class DialogueLayer(
    text: String,
    private val unitName: (Int) -> String,
    private val unitY: (Int) -> Float?,
    private val flag: Int = QI_PAO,
    private val onClose: () -> Unit = {},
) {
    data class Page(val speakerId: Int, val text: String)
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

    init { nextPage() }

    fun view(): View {
        val current = requireNotNull(page)
        return View(attached, bubbleIndex and 1, bubbleAtTop,
            current.speakerId, speakerName, renderedContent, content != target)
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

    fun skip() {
        if (attached) close()
    }

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

        internal fun resetAlternationForTest() {
            bubbleIndex = 0
            lastSpeakerId = -1
        }

        private fun parse(raw: String): List<Page> {
            val tokens = raw.replace("\r\n", "\n").replace('\r', '\n')
                .replace("\n", "<br/>").split("<br/>").filter(String::isNotEmpty)
            val pages = mutableListOf<Page>()
            var speaker: Int? = null
            val lines = mutableListOf<String>()
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
