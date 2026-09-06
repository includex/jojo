// Battle
package com.jojo.game.presentation.battle.fight

/**
 * `FightSpeechLifecycle`: 관련 상태와 동작을 묶는 class다.
 * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
 */

internal class FightSpeechLifecycle {
    /**
     * `mine` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val mine = FightSpeechPresentation()
    /**
     * `enemy` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val enemy = FightSpeechPresentation()

    /**
     * `panel`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun panel(side: FightSide): FightSpeechPresentation = if (side == FightSide.MINE) mine else enemy

    /**
     * `begin`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun begin(side: FightSide, text: String) {
        panel(side).apply {
            active = true
            sourceText = text
            content = ""
            renderedText = ""
        }
    }

    /**
     * `applyContent`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun applyContent(side: FightSide, content: String): FightPresentationEvent.TextChanged {
        val rendered = renderedRichText(content)
        panel(side).content = content
        panel(side).renderedText = rendered
        return FightPresentationEvent.TextChanged(side, rendered)
    }

    /**
     * `deactivateAll`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun deactivateAll() { mine.active = false; enemy.active = false }

    /**
     * `duration`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun duration(text: String, tickSeconds: Float, closeSeconds: Float): Float = typingContents(text).size * tickSeconds + closeSeconds

    companion object {
        /**
         * `typingContents`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

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

        /**
         * `renderedRichText`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun renderedRichText(content: String): String = if ("<color=" in content) "$content</c>" else content
    }
}
