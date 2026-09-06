// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallForcesRenderer: 거점 부대 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallForcesRenderer {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallForcesView) {
        val titleLayout = GlyphLayout()
        batch.color = Color.WHITE
        HallForcesRenderPlan.commands(view).forEach { command ->
            when (command.kind) {
                HallForcesDrawKind.TILED -> assets.hallTexture(command.asset)?.let {
                    tiled(batch, it, command.x, command.y, command.width, command.height)
                }
                HallForcesDrawKind.PATCH -> assets.hallTexture(command.asset)?.let { texture ->
                    NinePatch(texture, command.inset, command.inset, command.inset, command.inset)
                        .draw(batch, command.x, command.y, command.width, command.height)
                }
                HallForcesDrawKind.TITLE -> {
                    assets.titleFont.color = Color.BLACK
                    titleLayout.setText(assets.titleFont, command.text)
                    assets.titleFont.draw(batch, titleLayout, command.x + (command.width - titleLayout.width) / 2f, command.y)
                }
                HallForcesDrawKind.TEXT -> {
                    assets.bodyFont.color = Color.BLACK
                    assets.bodyFont.draw(batch, command.text, command.x, command.y, command.width, command.align, false)
                }
            }
        }
        batch.color = Color.WHITE
    }

    /**
     * `tiled`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun tiled(batch: SpriteBatch, texture: Texture, x: Float, y: Float, width: Float, height: Float) {
        val tileWidth = texture.width * .86f
        val tileHeight = texture.height * .86f
        var dy = 0f
        while (dy < height - .01f) {
            val drawHeight = minOf(tileHeight, height - dy)
            val sourceHeight = (drawHeight / .86f).toInt().coerceIn(1, texture.height)
            var dx = 0f
            while (dx < width - .01f) {
                val drawWidth = minOf(tileWidth, width - dx)
                val sourceWidth = (drawWidth / .86f).toInt().coerceIn(1, texture.width)
                batch.draw(texture, x + dx, y + dy, drawWidth, drawHeight, 0, 0, sourceWidth, sourceHeight, false, false)
                dx += tileWidth
            }
            dy += tileHeight
        }
    }
}
