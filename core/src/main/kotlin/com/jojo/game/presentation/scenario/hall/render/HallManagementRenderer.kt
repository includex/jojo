package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.hall.HallSellRenderer

/** Renderer dispatch for management projections already independent of Screen state. */
internal object HallManagementRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallManagementRenderView) = when (view) {
        is HallManagementRenderView.Sell -> HallSellRenderer.draw(assets, batch, view.catalog)
    }
}
