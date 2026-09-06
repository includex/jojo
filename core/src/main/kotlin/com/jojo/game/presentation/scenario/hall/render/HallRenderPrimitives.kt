// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallRenderPrimitives: 거점 렌더링 Primitives이며, 시나리오 장면을 정확히 표시하기 위한 변환·갱신 규칙을 제공한다. */
internal class HallRenderPrimitives(
    private val assets: ScenarioSceneAssets,
    private val batch: SpriteBatch,
) {
    fun ui(name: String): Texture? = assets.hallTexture("maps/ui/start-battle/$name.png")
    fun asset(path: String): Texture? = assets.hallTexture(path)
    fun patch(name: String, inset: Int = 3): NinePatch? = ui(name)?.let { NinePatch(it, inset, inset, inset, inset) }

    fun tiled(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
        val tileWidth = texture.width * .86f
        val tileHeight = texture.height * .86f
        var dy = 0f
        while (dy < height - .01f) {
            val drawHeight = minOf(tileHeight, height - dy)
            var dx = 0f
            while (dx < width - .01f) {
                val drawWidth = minOf(tileWidth, width - dx)
                batch.draw(
                    texture, x + dx, y + dy, drawWidth, drawHeight, 0, 0,
                    (drawWidth / .86f).toInt().coerceIn(1, texture.width),
                    (drawHeight / .86f).toInt().coerceIn(1, texture.height),
                    false, false,
                )
                dx += tileWidth
            }
            dy += tileHeight
        }
    }

    fun text(value: String, x: Float, y: Float, width: Float, centered: Boolean = false, small: Boolean = false) {
        val font = if (small) assets.smallUiFont else assets.bodyFont
        font.color = Color.BLACK
        font.draw(batch, value, x, y, width, if (centered) Align.center else Align.left, false)
    }

    fun title(value: String, x: Float, y: Float, width: Float, color: Color = Color.BLACK) {
        val layout = GlyphLayout(assets.titleFont, value)
        assets.titleFont.color = color
        assets.titleFont.draw(batch, layout, x + (width - layout.width) / 2f, y)
    }

    fun cell(x: Float, y: Float, width: Float, height: Float) {
        patch("box2")?.draw(batch, x, y, width, height)
    }

    fun button(
        value: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float = 51.6f,
        labelOffset: Float = height - 16.6f,
    ) {
        patch("button", 9)?.draw(batch, x, y, width, height)
        text(value, x, y + labelOffset, width, centered = true)
    }

    fun resetColor() {
        batch.color = Color.WHITE
    }
}
