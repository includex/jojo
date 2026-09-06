// Battle
package com.jojo.game.presentation.battle.fight

internal class FightSpeechLifecycle {
    val mine = FightSpeechPresentation()
    val enemy = FightSpeechPresentation()

    fun panel(side: FightSide): FightSpeechPresentation = if (side == FightSide.MINE) mine else enemy

    fun begin(side: FightSide, text: String) {
        panel(side).apply {
            active = true
            sourceText = text
            content = ""
            renderedText = ""
        }
    }

    fun applyContent(side: FightSide, content: String): FightPresentationEvent.TextChanged {
        val rendered = renderedRichText(content)
        panel(side).content = content
        panel(side).renderedText = rendered
        return FightPresentationEvent.TextChanged(side, rendered)
    }

    fun deactivateAll() { mine.active = false; enemy.active = false }

    fun duration(text: String, tickSeconds: Float, closeSeconds: Float): Float = typingContents(text).size * tickSeconds + closeSeconds

    companion object {
        fun typingContents(text: String): List<String> {
            var remaining = text
            var content = ""
            val result = mutableListOf<String>()
            do {
                var tagDepth = 0
                while (remaining.isNotEmpty()) {
                    val next = remaining.substring(0, 1)
                    remaining = remaining.substring(1)
                    content += next
                    if (next == "<") tagDepth++ else if (tagDepth > 0 && next == ">") tagDepth--
                    if (tagDepth == 0) break
                }
                result += content
            } while (remaining.isNotEmpty())
            return result
        }

        fun renderedRichText(content: String): String = if ("<color=" in content) "$content</c>" else content
    }
}
