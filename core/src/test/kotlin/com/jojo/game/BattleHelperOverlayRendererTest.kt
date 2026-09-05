package com.jojo.game
import com.jojo.game.presentation.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleHelperOverlayRendererTest {
    @Test
    fun `helper snapshot preserves rich text markup and authored button`() {
        val view = BattleHelperOverlayView(
            richText = "<color=#c30000>주의</color><br/>다음 줄 &amp; 설명",
            buttonText = "확인",
        )

        assertEquals("<color=#c30000>주의</color><br/>다음 줄 &amp; 설명", view.richText)
        assertEquals("확인", view.buttonText)
    }
}
