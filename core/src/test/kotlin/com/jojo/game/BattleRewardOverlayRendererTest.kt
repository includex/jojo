package com.jojo.game
import com.jojo.game.presentation.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleRewardOverlayRendererTest {
    @Test
    fun `reward snapshot keeps phase content and section visibility independent`() {
        val view = BattleRewardOverlayView(
            worldWidth = 1488.372f,
            worldHeight = 800f,
            phase = BattleRewardOverlayPhase.ITEMS,
            money = 120,
            stars = "★  ☆  ★",
            items = listOf(
                BattleRewardItemView("청룡검", null),
                BattleRewardItemView("현무갑", null),
            ),
            sectionVisible = true,
        )

        assertEquals(BattleRewardOverlayPhase.ITEMS, view.phase)
        assertEquals(120, view.money)
        assertEquals("★  ☆  ★", view.stars)
        assertEquals(listOf("청룡검", "현무갑"), view.items.map(BattleRewardItemView::name))
        assertEquals(true, view.sectionVisible)
    }

    @Test
    fun `complete phase still permits the section layer after reward content`() {
        val view = BattleRewardOverlayView(
            worldWidth = 1488.372f,
            worldHeight = 800f,
            phase = BattleRewardOverlayPhase.COMPLETE,
            sectionVisible = true,
        )

        assertEquals(BattleRewardOverlayPhase.COMPLETE, view.phase)
        assertEquals(true, view.sectionVisible)
        assertEquals(emptyList(), view.items)
    }
}
