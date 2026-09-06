// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallUnitRosterRenderer: 거점 유닛 명단 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallUnitRosterRenderer {
    private const val SCALE = .86f

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallUnitRosterView) {
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
        HallUnitRosterRenderPlan.commands(view).forEach { command ->
            when (command.kind) {
                HallUnitRosterDrawKind.TILED -> assets.hallTexture(command.asset)?.let {
                    tiled(it, command.x * SCALE, command.y * SCALE, command.width * SCALE, command.height * SCALE)
                }
                HallUnitRosterDrawKind.SPRITE -> assets.hallTexture(command.asset)?.let {
                    batch.draw(it, command.x * SCALE, command.y * SCALE, command.width * SCALE, command.height * SCALE)
                }
                HallUnitRosterDrawKind.PATCH -> assets.hallTexture(command.asset)?.let { texture ->
                    NinePatch(texture, 3, 3, 3, 3).draw(
                        batch,
                        command.x * SCALE,
                        command.y * SCALE,
                        command.width * SCALE,
                        command.height * SCALE,
                    )
                }
                HallUnitRosterDrawKind.TEXT -> {
                    assets.bodyFont.color = Color.BLACK
                    assets.bodyFont.draw(batch, command.text, command.x * SCALE, command.y * SCALE, command.width * SCALE, command.align, false)
                }
            }
        }
        batch.color = Color.WHITE
    }
}
