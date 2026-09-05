package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.hall.HallForcesRenderer
import com.jojo.game.presentation.scenario.hall.HallTerrainRenderer
import com.jojo.game.presentation.scenario.hall.HallTreasureRenderer

/** Hall information renderer dispatch over read-only display projections. */
internal object HallInfoRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallInfoRenderView) = when (view) {
        is HallInfoRenderView.Forces -> HallForcesRenderer.draw(assets, batch, view.view)
        is HallInfoRenderView.Terrain -> HallTerrainRenderer.draw(assets, batch, view.view)
        is HallInfoRenderView.Treasure -> HallTreasureRenderer.draw(assets, batch, view.view)
    }
}
