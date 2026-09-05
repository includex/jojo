package com.jojo.game.presentation.scenario.hall.render

import com.jojo.game.presentation.scenario.overlay.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** Stateless source HallCommandLayer renderer. */
internal object HallCommandRenderer {
    fun draw(batch: SpriteBatch, view: HallCommandRenderView) {
        batch.color = Color.WHITE
        view.menuTexture?.let { batch.draw(it, 31f, 318.2f, 51.6f, 51.6f) }
        listOf(view.battleTexture to 936.86f, view.equipTexture to 1019.42f, view.buyTexture to 1101.98f, view.sellTexture to 1184.54f).forEach { (texture, x) -> texture?.let { batch.draw(it, x - 41.28f, 1.72f, 82.56f, 82.56f) } }
    }
}
