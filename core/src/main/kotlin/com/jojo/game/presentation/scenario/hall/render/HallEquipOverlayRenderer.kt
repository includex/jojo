// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.jojo.game.presentation.i18n.GameText

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallEquipOverlayRenderer: 거점 Equip 오버레이 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallEquipOverlayRenderer {
    /**
     * `drawUnequipConfirmation`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun drawUnequipConfirmation(assets: ScenarioSceneAssets, batch: SpriteBatch) {
        val draw = HallRenderPrimitives(assets, batch)
        val x = 421f
        val y = 275f
        val width = 438f
        val height = 139f
        draw.patch("box1")?.draw(batch, x, y, width, height)
        draw.patch("title", 5)?.draw(batch, x, y + height - 43f, width, 43f)
        draw.text(GameText.S_468266D639, x, y + height - 11f, width, centered = true)
        draw.text(GameText.S_A9A59D5FE3, x + 14f, y + 86f, width - 28f, centered = true)
        draw.button(GameText.S_A842629AFD, x + 18f, y + 16f, 184f, 43f, 31f)
        draw.button(GameText.S_D3B0E1A367, x + 236f, y + 16f, 184f, 43f, 31f)
        draw.resetColor()
    }
}
