// Scenario
package com.jojo.game.presentation.scenario.hall.render
import com.jojo.game.presentation.shared.overlay.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.hall.HallHelperView

/** HallHelperRenderer: 거점 도움말 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallHelperRenderer {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallHelperView) {
        val draw = HallRenderPrimitives(assets, batch)
        val x = 127f
        val y = 21.07f
        val width = 1025.98f
        val height = 645.86f
        batch.color = Color.WHITE
        draw.ui("logo9")?.let { draw.tiled(it, x, y, width, height) }
        draw.patch("box1")?.draw(batch, x, y, width, height)
        draw.patch("title", 5)?.draw(batch, x, y + height - 62f, width, 62f)
        assets.titleFont.color = Color(0.65f, 0f, 0.68f, 1f)
        assets.titleFont.draw(batch, "역사 정보", x + 8f, y + height - 12f)
        draw.cell(x + 12f, y + 82f, width - 24f, height - 151f)
        assets.bodyFont.color = Color.BLACK
        assets.bodyFont.draw(batch, view.text, x + 18f, y + height - 90f, width - 36f, Align.left, true)
        draw.button("확인", x + width - 145f, y + 7f, 135f)
        draw.resetColor()
    }
}
