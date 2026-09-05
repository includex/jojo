package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** Immutable texture projection for HallCommandLayer's five main controls. */
internal data class HallViewState(
    val menuTexture: Texture?,
    val battleTexture: Texture?,
    val equipTexture: Texture?,
    val buyTexture: Texture?,
    val sellTexture: Texture?,
)

/** Stateless Hall rendering which consumes only resolved assets and a batch. */
internal object HallRenderer {
    fun drawMainCommands(batch: SpriteBatch, view: HallViewState) {
        batch.color.set(1f, 1f, 1f, 1f)
        view.menuTexture?.let { batch.draw(it, 31f, 318.2f, 51.6f, 51.6f) }
        listOf(
            view.battleTexture to 936.86f,
            view.equipTexture to 1019.42f,
            view.buyTexture to 1101.98f,
            view.sellTexture to 1184.54f,
        ).forEach { (texture, centerX) ->
            texture?.let { batch.draw(it, centerX - 41.28f, 1.72f, 82.56f, 82.56f) }
        }
    }
}
