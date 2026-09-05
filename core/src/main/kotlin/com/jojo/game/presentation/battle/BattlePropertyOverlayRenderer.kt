package com.jojo.game.presentation.battle

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

data class BattlePropertyRowView(
    val icon: Texture?,
    val label: String,
    val selected: Boolean,
)

data class BattlePropertyOverlayView(
    val selectedTab: Int,
    val firstRow: Int,
    val rows: List<BattlePropertyRowView>,
)

data class BattlePropertyOverlayAssets(
    val background: Texture?,
    val panel: NinePatch?,
    val rowEven: NinePatch?,
    val rowOdd: NinePatch?,
    val verticalLine: NinePatch?,
)

/** Stateless PropertyLayer table renderer; tab/scroll/item commands stay in the screen. */
class BattlePropertyOverlayRenderer(
    private val batch: SpriteBatch,
    private val font: BitmapFont,
    private val assets: BattlePropertyOverlayAssets,
) {
    fun draw(view: BattlePropertyOverlayView) {
        val x = 247f
        val y = 48f
        val w = 994f
        val h = 706f
        val scale = 40f / 26f
        batch.begin()
        batch.color = Color.WHITE
        drawTiledBackground(x, y, w, h)
        assets.panel?.draw(batch, 249f, 117f, 990f, 524f)
        listOf(
            floatArrayOf(251.2f, 637.9f, 376.9f),
            floatArrayOf(628.6f, 638f, 195.1f),
            floatArrayOf(824.7f, 638f, 106.9f),
            floatArrayOf(931.4f, 638f, 101.2f),
            floatArrayOf(1032f, 638f, 206.4f),
        ).forEach { (headerX, headerY, headerWidth) -> assets.panel?.draw(batch, headerX, headerY, headerWidth, 60f) }
        font.color = Color.BLACK
        font.data.setScale(scale)
        font.draw(batch, "창고 일람", x + 430f, 740f)
        listOf("이름" to 400f, "속성" to 687f, "레벨" to 838f, "경험치" to 930f, "소지자" to 1083f)
            .forEach { (label, position) -> font.draw(batch, label, position, 680f) }
        listOf(628.468f, 823.971f, 930.065f, 1032.026f)
            .forEach { lineX -> assets.verticalLine?.draw(batch, lineX, 122.75f, 6f, 515.38f) }
        view.rows.drop(view.firstRow).take(7).forEachIndexed { index, row ->
            val rowY = y + 540f - index * 72f
            (if (index % 2 == 0) assets.rowEven else assets.rowOdd)?.draw(batch, x + 9f, rowY - 59f, w - 18f, 72f)
            font.color = if (row.selected) Color(0.05f, .35f, .95f, 1f) else Color.BLACK
            row.icon?.let { batch.draw(it, x + 22f, rowY - 47f, 48f, 48f) }
            font.draw(batch, row.label, x + 86f, rowY)
        }
        font.data.setScale(scale)
        listOf("무기", "방어구", "보조", "아이템").forEachIndexed { index, label ->
            val active = index == view.selectedTab
            font.color = if (active) Color(0.05f, .48f, .94f, 1f) else Color(.2f, .2f, .2f, 1f)
            font.draw(batch, if (active) "●" else "○", x + 28f + index * 146f, y + 30f)
            font.color = Color.BLACK
            font.draw(batch, label, x + 56f + index * 146f, y + 30f)
        }
        assets.panel?.draw(batch, x + w - 158f, y + 10f, 140f, 54f)
        font.color = Color.BLACK
        font.draw(batch, "확인", x + w - 116f, y + 30f)
        font.color = Color.WHITE
        font.data.setScale(1f)
        batch.end()
    }

    private fun drawTiledBackground(x: Float, y: Float, width: Float, height: Float) {
        assets.background?.let { texture ->
            var ty = y
            while (ty < y + height) {
                var tx = x
                while (tx < x + width) {
                    batch.draw(texture, tx, ty, minOf(96f, x + width - tx), minOf(96f, y + height - ty))
                    tx += 96f
                }
                ty += 96f
            }
        }
    }
}

