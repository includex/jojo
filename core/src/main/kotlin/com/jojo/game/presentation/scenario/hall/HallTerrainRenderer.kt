// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallTerrainRenderer: 거점 지형 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallTerrainRenderer {
    private const val SCALE = .86f

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallTerrainView) {
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
        HallTerrainRenderPlan.commands(view).forEach { command ->
            when (command.kind) {
                HallTerrainDrawKind.TILED -> assets.hallTexture(command.asset)?.let { tiled(it, command.x, command.y, command.width, command.height) }
                HallTerrainDrawKind.PATCH -> assets.hallTexture(command.asset)?.let { texture ->
                    NinePatch(texture, command.inset, command.inset, command.inset, command.inset)
                        .draw(batch, command.x, command.y, command.width, command.height)
                }
                HallTerrainDrawKind.SPRITE -> assets.hallTexture(command.asset)?.let {
                    batch.color = Color.WHITE
                    batch.draw(it, command.x, command.y, command.width, command.height)
                }
                HallTerrainDrawKind.TEXT -> {
                    val font = when (command.font) {
                        HallTerrainFont.TITLE -> assets.titleFont
                        HallTerrainFont.BODY -> assets.bodyFont
                        HallTerrainFont.SMALL -> assets.smallUiFont
                    }
                    font.color = when (command.color) {
                        HallTerrainTextColor.BLACK -> Color.BLACK
                        HallTerrainTextColor.ORANGE -> Color(1f, .36f, 0f, 1f)
                        HallTerrainTextColor.GREEN -> Color(0f, .58f, .05f, 1f)
                    }
                    font.draw(batch, command.text, command.x, command.y, command.width, command.align, false)
                }
            }
        }
        batch.color = Color.WHITE
    }
}
