package com.jojo.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

data class BattleTerrainValueView(val text: String, val grade: Int?)

data class BattleTerrainRowView(
    val terrainName: String,
    val icon: Texture?,
    val enabledSkills: List<Boolean>,
    val values: List<BattleTerrainValueView>,
)

data class BattleTerrainOverlayView(
    val armNames: List<String>,
    val rows: List<BattleTerrainRowView>,
)

data class BattleTerrainOverlayAssets(
    val background: Texture?,
    val panel: NinePatch?,
    val rowEven: NinePatch?,
    val rowOdd: NinePatch?,
    val verticalLine: NinePatch?,
)

/** Stateless renderer for TerrainLayer's table overlay. */
class BattleTerrainOverlayRenderer(
    private val batch: SpriteBatch,
    private val font: BitmapFont,
    private val assets: BattleTerrainOverlayAssets,
) {
    fun draw(view: BattleTerrainOverlayView) {
        batch.begin()
        batch.color = Color.WHITE
        drawTiledBackground()
        assets.panel?.draw(batch, PANEL_X + 14f, PANEL_Y + 84f, PANEL_WIDTH - 28f, 460f)
        assets.panel?.draw(batch, 285f, 111f, 197f, 60f)
        assets.panel?.draw(batch, 491f, 111f, 223f, 60f)
        assets.panel?.draw(batch, 1165f, 111f, 120f, 60f)

        font.color = Color.BLACK
        font.data.setScale(40f / 26f)
        font.draw(batch, "지형 정보 일람", PANEL_X + 10f, PANEL_Y + 586f)
        font.data.setScale(1.4f)
        font.draw(batch, "지형 효과", 304f, 147f)
        font.draw(batch, "기동력 소모", 516f, 147f)
        font.draw(batch, "확인", 1207f, 147f)
        font.data.setScale(1f)
        font.draw(batch, "이름", PANEL_X + 90f, PANEL_Y + 529f)
        view.armNames.forEachIndexed { index, name ->
            font.draw(batch, name, PANEL_X + 252f + index * COLUMN_WIDTH, PANEL_Y + 529f)
        }

        view.rows.forEachIndexed { rowIndex, row -> drawRow(rowIndex, row) }
        (0..13).forEach { index ->
            assets.verticalLine?.draw(batch, PANEL_X + 244f + index * COLUMN_WIDTH, PANEL_Y + 96f, 6f, 414f)
        }
        font.data.setScale(1f)
        font.color = Color.WHITE
        batch.color = Color.WHITE
        batch.end()
    }

    private fun drawRow(index: Int, row: BattleTerrainRowView) {
        val y = PANEL_Y + 488f - index * 75f
        (if (index % 2 == 0) assets.rowEven else assets.rowOdd)?.draw(
            batch, PANEL_X + 18f, y - 59f, PANEL_WIDTH - 36f, 75f
        )
        row.icon?.let {
            batch.color = Color.WHITE
            batch.draw(it, PANEL_X + 17f, y - 57f, 67f, 67f)
        }
        font.data.setScale(36f / 26f)
        font.color = if (index % 2 == 0) Color(1f, 0.94f, 0.78f, 1f) else Color(0.86f, 0.86f, 0.86f, 1f)
        font.draw(batch, row.terrainName, PANEL_X + 104f, y + 12f)
        font.data.setScale(50f / 26f)
        row.enabledSkills.forEachIndexed { bit, enabled ->
            font.color = if (enabled) Color(1f, 0.82f, 0.20f, 1f) else Color(0.35f, 0.35f, 0.35f, 1f)
            font.draw(batch, if (enabled) "●" else "○", PANEL_X + 172f + bit * 20f, y)
        }
        row.values.forEachIndexed { armIndex, value ->
            font.color = when (value.grade) {
                0 -> Color(0.94f, 0.56f, 0.13f, 1f)
                1 -> Color(0.94f, 0.38f, 0f, 1f)
                2 -> Color(0f, 0.56f, 0f, 1f)
                3 -> Color(0f, 0f, 0.63f, 1f)
                4 -> Color(0.44f, 0.25f, 0.5f, 1f)
                else -> Color(0.78f, 0.78f, 0.78f, 1f)
            }
            font.draw(batch, value.text, PANEL_X + 252f + armIndex * COLUMN_WIDTH, y)
        }
    }

    private fun drawTiledBackground() {
        assets.background?.let { texture ->
            var y = PANEL_Y
            while (y < PANEL_Y + PANEL_HEIGHT) {
                var x = PANEL_X
                while (x < PANEL_X + PANEL_WIDTH) {
                    batch.draw(texture, x, y, minOf(TILE, PANEL_X + PANEL_WIDTH - x), minOf(TILE, PANEL_Y + PANEL_HEIGHT - y))
                    x += TILE
                }
                y += TILE
            }
        }
    }

    private companion object {
        const val PANEL_X = 274f
        const val PANEL_Y = 100f
        const val PANEL_WIDTH = 1021f
        const val PANEL_HEIGHT = 600f
        const val TILE = 96f
        const val COLUMN_WIDTH = 53f
    }
}
