package com.jojo.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** Stateless renderer for the BuyLayer catalog snapshot. */
internal object HallBuyCatalogRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallBuyCatalogView) {
        batch.color = Color.WHITE
        HallBuyCatalogRenderPlan.commands(view).forEach { command ->
            when (command.kind) {
                HallBuyCatalogDrawKind.PATCH -> assets.hallTexture(command.asset)?.let { texture ->
                    NinePatch(texture, command.inset, command.inset, command.inset, command.inset)
                        .draw(batch, command.x, command.y, command.width, command.height)
                }
                HallBuyCatalogDrawKind.SPRITE -> assets.hallTexture(command.asset)?.let {
                    batch.color = Color.WHITE
                    batch.draw(it, command.x, command.y, command.width, command.height)
                }
                HallBuyCatalogDrawKind.TEXT -> {
                    assets.bodyFont.color = Color.BLACK
                    assets.bodyFont.draw(batch, command.text, command.x, command.y, command.width, command.align, false)
                }
            }
        }
        batch.color = Color.WHITE
    }
}
