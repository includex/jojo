package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleUiAssetsTest {
    @Test
    fun `magic dialog uses the recovered Cocos one-based spriteframe convention`() {
        val magic = MagicUiList.Magic(39, "소량의 보급품", 6, 28, 7, 13, 0, "")

        assertEquals("maps/magic-icons/8.png", BattleUiAssets.magicIcon(magic.icon))
        assertEquals("maps/magic-hitareas/14.png", BattleUiAssets.hitArea(magic.hit))
        assertEquals("maps/magic-effareas/1.png", BattleUiAssets.effectArea(magic.eff))
    }

    @Test
    fun `shared modal assets retain authored paths rather than placeholder surfaces`() {
        assertEquals("maps/ui/choice-panel.png", BattleUiAssets.CHOICE_PANEL)
        assertEquals("maps/ui/choice-row.png", BattleUiAssets.CHOICE_ROW)
        assertEquals("maps/marks/1.png", BattleUiAssets.MP_CURRENT_MARK)
        assertEquals("maps/marks/2.png", BattleUiAssets.MP_MAX_MARK)
    }

    @Test
    fun `magic fixture retains source sprite slots and geometry`() {
        val magic = MagicUiList.Magic(39, "소량의 보급품", 6, 28, 7, 13, 0, "")
        assertEquals(
            BattleDialogRenderContract.Sprite("maps/magic-icons/8.png", 485.259f, 562.883f, 76.8f, 76.8f),
            BattleDialogRenderContract.magicListIcon(magic, 480.186f, 505.5f),
        )
        assertEquals(
            listOf(
                "maps/magic-icons/8.png",
                "maps/magic-hitareas/14.png",
                "maps/magic-effareas/1.png",
            ),
            BattleDialogRenderContract.magicDetailSprites(magic).map { it.path },
        )
    }
}
