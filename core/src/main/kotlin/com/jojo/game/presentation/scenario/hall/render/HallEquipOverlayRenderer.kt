// Scenario
package com.jojo.game.presentation.scenario.hall.render

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallEquipOverlayRenderer: 거점 Equip 오버레이 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallEquipOverlayRenderer {
    fun drawUnequipConfirmation(assets: ScenarioSceneAssets, batch: SpriteBatch) {
        val draw = HallRenderPrimitives(assets, batch)
        val x = 421f
        val y = 275f
        val width = 438f
        val height = 139f
        draw.patch("box1")?.draw(batch, x, y, width, height)
        draw.patch("title", 5)?.draw(batch, x, y + height - 43f, width, 43f)
        draw.text("확인", x, y + height - 11f, width, centered = true)
        draw.text("모두에게 장비를 해제하도록 확정하시겠습니까?", x + 14f, y + 86f, width - 28f, centered = true)
        draw.button("예", x + 18f, y + 16f, 184f, 43f, 31f)
        draw.button("비", x + 236f, y + 16f, 184f, 43f, 31f)
        draw.resetColor()
    }
}
