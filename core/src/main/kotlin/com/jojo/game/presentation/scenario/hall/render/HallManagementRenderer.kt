// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.hall.HallSellRenderer

/** HallManagementRenderer: 거점 Management 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallManagementRenderer {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallManagementRenderView, viewport: Viewport? = null) = when (view) {
        is HallManagementRenderView.Equip -> HallEquipRenderer.draw(assets, batch, requireNotNull(viewport), view.view)
        is HallManagementRenderView.Sell -> HallSellRenderer.draw(assets, batch, view.catalog)
    }
}
