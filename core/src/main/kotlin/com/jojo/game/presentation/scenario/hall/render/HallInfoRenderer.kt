// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.hall.HallForcesRenderer
import com.jojo.game.presentation.scenario.hall.HallTerrainRenderer
import com.jojo.game.presentation.scenario.hall.HallTreasureRenderer

/** HallInfoRenderer: 거점 Info 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallInfoRenderer {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallInfoRenderView) = when (view) {
        is HallInfoRenderView.Forces -> HallForcesRenderer.draw(assets, batch, view.view)
        is HallInfoRenderView.Property -> HallPropertyRenderer.draw(assets, batch, view.view)
        is HallInfoRenderView.Terrain -> HallTerrainRenderer.draw(assets, batch, view.view)
        is HallInfoRenderView.Treasure -> HallTreasureRenderer.draw(assets, batch, view.view)
        is HallInfoRenderView.Helper -> HallHelperRenderer.draw(assets, batch, view.view)
    }
}
