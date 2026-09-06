package com.jojo.game.presentation.scenario.overlay

/**
 * Cocos InfoLayer reveals one printable character every .04 seconds.  Its
 * RichText tags are consumed as one unit, rather than being shown literally.
 */

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

    /** Returns true when a click was consumed to reveal the current line. */
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
        // Match InfoLayer: consume a whole RichText tag during this tick.
        val end = source.indexOf('>', cursor)
        cursor = if (end == -1) cursor + 1 else end + 1
    }

    private companion object {
        const val CHARACTER_INTERVAL = 0.04f
        val RICH_TEXT_TAG = Regex("<[^>]*>")
    }
}
