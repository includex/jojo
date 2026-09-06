// Battle
package com.jojo.game.presentation.battle.render

import com.jojo.game.presentation.shared.overlay.MagicUiList
import com.jojo.game.presentation.battle.assets.BattleUiAssets
object BattleDialogRenderContract {
    data class Sprite(val path: String, val x: Float, val y: Float, val width: Float, val height: Float)


    fun magicListIcon(magic: MagicUiList.Magic, x: Float, y: Float) =
        Sprite(BattleUiAssets.magicIcon(magic.icon), x + 5.073f, y + 57.383f, 76.8f, 76.8f)


    fun magicDetailSprites(magic: MagicUiList.Magic) = listOf(
        Sprite(BattleUiAssets.magicIcon(magic.icon), 478.186f, 562f, 80f, 80f),
        Sprite(BattleUiAssets.hitArea(magic.hit), 834.213f, 450.755f, 160f, 160f),
        Sprite(BattleUiAssets.effectArea(magic.eff), 834.213f, 219.367f, 160f, 160f),
    )
}
