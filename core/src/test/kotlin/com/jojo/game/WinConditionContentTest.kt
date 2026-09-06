// Test
package com.jojo.game

import com.jojo.game.presentation.battle.overlay.WinConditionContent

import kotlin.test.Test
import kotlin.test.assertEquals

/** WinConditionContentTest: WinConditionContent의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class WinConditionContentTest {
    @Test fun `winConProcess preserves source section order pairs and hidden item test`() {
        val text = WinConditionContent.build(
            text = "승리", vs = listOf(1, 2), talk = listOf(3, 4),
            items = listOf(WinConditionContent.HiddenItem(20, "(1,2)"), WinConditionContent.HiddenItem(21, "(3,4)")),
            unitName = { mapOf(1 to "A", 2 to "B", 3 to "C", 4 to "D")[it] },
            variable = { if (it == 20) 1 else 0 },
        )
        assertEquals("승리\n\n일대일 대결\nA VS B\n\n대화\nC -> D\n\n아이템\n(3,4),", text)
    }
}
