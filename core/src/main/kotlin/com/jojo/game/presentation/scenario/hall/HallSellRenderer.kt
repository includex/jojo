package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** Stateless renderer for the immutable [HallSellView]. */
internal object HallSellRenderer {
    private const val SCALE = .86f

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallSellView) {
        fun tiled(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
            val tileWidth = texture.width * SCALE
            val tileHeight = texture.height * SCALE
            var dy = 0f
            while (dy < height - .01f) {
                val drawHeight = minOf(tileHeight, height - dy)
                val sourceHeight = (drawHeight / SCALE).toInt().coerceIn(1, texture.height)
                var dx = 0f
                while (dx < width - .01f) {
                    val drawWidth = minOf(tileWidth, width - dx)
                    val sourceWidth = (drawWidth / SCALE).toInt().coerceIn(1, texture.width)
                    batch.draw(texture, x + dx, y + dy, drawWidth, drawHeight, 0, 0, sourceWidth, sourceHeight, false, false)
                    dx += tileWidth
                }
                dy += tileHeight
            }
        }

        batch.color = Color.WHITE
        HallSellRenderPlan.commands(view).forEach { command ->
            when (command.kind) {
                HallSellDrawKind.TILED -> assets.hallTexture(command.asset)?.let { tiled(it, command.x, command.y, command.width, command.height) }
                HallSellDrawKind.PATCH -> assets.hallTexture(command.asset)?.let { texture ->
                    NinePatch(texture, command.inset, command.inset, command.inset, command.inset)
                        .draw(batch, command.x, command.y, command.width, command.height)
                }
                HallSellDrawKind.SPRITE -> assets.hallTexture(command.asset)?.let {
                    batch.color = Color.WHITE
                    batch.draw(it, command.x, command.y, command.width, command.height)
                }
                HallSellDrawKind.TEXT -> {
                    val font = if (command.font == HallSellFont.TITLE) assets.titleFont else assets.bodyFont
                    font.color = when (command.color) {
                        HallSellTextColor.BLACK -> Color.BLACK
                        HallSellTextColor.NOTICE -> Color(0.55f, 0.05f, 0.05f, 1f)
                    }
                    font.draw(batch, command.text, command.x, command.y, command.width, command.align, false)
                }
            }
        }
        batch.color = Color.WHITE
    }
}
