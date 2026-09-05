package com.jojo.game.presentation.battle

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

data class BattleHelperOverlayView(val richText: String, val buttonText: String)

data class BattleHelperOverlayAssets(
    val background: Texture?,
    val header: NinePatch?,
    val scroll: NinePatch?,
)

/** Stateless HelperLayer renderer, including its small RichText grammar. */
class BattleHelperOverlayRenderer(
    private val batch: SpriteBatch,
    private val font: BitmapFont,
    private val glyphLayout: GlyphLayout,
    private val assets: BattleHelperOverlayAssets,
) {
    fun draw(view: BattleHelperOverlayView) {
        batch.begin()
        batch.color = Color.WHITE
        drawTiledBackground()
        assets.header?.draw(batch, 147.686f, 715.5f, 1193f, 60f)
        assets.scroll?.draw(batch, 163.686f, 99f, 1161f, 616f)
        font.color = Color(0.56f, 0f, 0.62f, 1f)
        font.data.setScale(40f / 26f)
        font.draw(batch, "역사 정보", 160f, 760f)
        drawRichText(view.richText, 165.686f, 690f, 1157f)
        batch.end()

        batch.begin()
        assets.header?.draw(batch, 1172.451f, 33.187f, 147.6f, 56f)
        font.color = Color.BLACK
        font.data.setScale(40f / 26f)
        font.draw(batch, view.buttonText, 1204f, 71f)
        font.data.setScale(1f)
        font.color = Color.WHITE
        batch.end()
    }

    private fun drawRichText(richText: String, x: Float, topY: Float, width: Float) {
        data class Run(val text: String, val color: Color)

        val lines = mutableListOf<MutableList<Run>>()
        var line = mutableListOf<Run>()
        lines += line
        val colors = ArrayDeque<Color>().apply { addLast(Color.BLACK) }
        val tags = Regex("<color=(#[0-9a-fA-F]{6})>|</color>|<br\\s*/?>")
        var cursor = 0

        fun append(value: String) {
            value.replace("&amp;", "&").split('\n').forEachIndexed { index, piece ->
                if (piece.isNotEmpty()) line += Run(piece, Color(colors.last()))
                if (index < value.count { it == '\n' }) {
                    line = mutableListOf()
                    lines += line
                }
            }
        }

        tags.findAll(richText).forEach { match ->
            append(richText.substring(cursor, match.range.first))
            when {
                match.value.startsWith("<color=") -> colors.addLast(Color.valueOf(match.groupValues[1]))
                match.value == "</color>" && colors.size > 1 -> colors.removeLast()
                match.value.startsWith("<br") -> {
                    line = mutableListOf()
                    lines += line
                }
            }
            cursor = match.range.last + 1
        }
        append(richText.substring(cursor))

        font.data.setScale(40f / 26f)
        val lineHeight = 50f
        var lineIndex = 0
        lines.forEach { runs ->
            var pen = x
            runs.forEach { run ->
                font.color = run.color
                run.text.forEach { glyph ->
                    glyphLayout.setText(font, glyph.toString())
                    if (pen > x && pen + glyphLayout.width > x + width) {
                        lineIndex += 1
                        pen = x
                    }
                    if (lineIndex < 12) font.draw(batch, glyph.toString(), pen, topY - lineIndex * lineHeight)
                    pen += glyphLayout.width
                }
            }
            lineIndex += 1
        }
        font.color = Color.BLACK
    }

    private fun drawTiledBackground() {
        assets.background?.let { texture ->
            var y = 24.5f
            while (y < 775.5f) {
                var x = 147.686f
                while (x < 1340.686f) {
                    batch.draw(texture, x, y, minOf(96f, 1340.686f - x), minOf(96f, 775.5f - y))
                    x += 96f
                }
                y += 96f
            }
        }
    }
}

