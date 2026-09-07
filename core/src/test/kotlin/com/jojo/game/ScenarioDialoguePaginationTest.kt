// Test
package com.jojo.game

import com.jojo.game.application.scenario.ScenarioDialogueCoordinator
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * ScenarioDialoguePaginationTest: 대사창이 원본과 같은 세 줄 페이지 단위로 끊기는지 검증한다.
 *
 * 원본 `SayLayer._next`는 `_line`이 3에 이르면 본문을 비우고 새 페이지를 시작하고, 화자
 * 표식(`&NNN`)을 만나면 그 자리에서 페이지를 끊는다. 대사창 자체는 늘어나지 않으므로 이
 * 페이지 분할이 없으면 긴 대사가 창을 넘는다.
 */
class ScenarioDialoguePaginationTest {
    @Test fun `long speaker block is split into three line pages`() {
        val raw = "&0\n첫째 줄\n둘째 줄\n셋째 줄\n넷째 줄\n다섯째 줄"
        val blocks = ScenarioDialogueCoordinator.parseDialogueBlocks(raw)
        assertEquals(2, blocks.size)
        assertEquals("0", blocks[0].speakerId)
        assertEquals("첫째 줄\n둘째 줄\n셋째 줄", blocks[0].text)
        assertEquals("0", blocks[1].speakerId)
        assertEquals("넷째 줄\n다섯째 줄", blocks[1].text)
    }

    @Test fun `speaker change starts a new page even mid page`() {
        val raw = "&0\n한 줄\n&1\n다른 화자\n둘째 줄"
        val blocks = ScenarioDialogueCoordinator.parseDialogueBlocks(raw)
        assertEquals(2, blocks.size)
        assertEquals("0", blocks[0].speakerId)
        assertEquals("한 줄", blocks[0].text)
        assertEquals("1", blocks[1].speakerId)
        assertEquals("다른 화자\n둘째 줄", blocks[1].text)
    }

    @Test fun `narration without a speaker tag pages the same way`() {
        val raw = "가\n나\n다\n라"
        val blocks = ScenarioDialogueCoordinator.parseDialogueBlocks(raw)
        assertEquals(2, blocks.size)
        assertEquals(null, blocks[0].speakerId)
        assertEquals("가\n나\n다", blocks[0].text)
        assertEquals("라", blocks[1].text)
    }
}
