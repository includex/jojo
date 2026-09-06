// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallBuyCatalogRenderer: 구매 가능한 장비·소모품 행과 선택 강조를 거점 상점 목록에 그린다. */
internal object HallBuyCatalogRenderer {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
