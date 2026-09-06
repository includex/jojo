// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallBuyUnitSummaryRenderer: 거점 Buy 유닛 Summary 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object HallBuyUnitSummaryRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallBuyUnitSummaryView) {
        HallBuyUnitSummaryRenderPlan.commands(view).forEach { command ->
            when (command.kind) {
                HallBuyUnitSummaryDrawKind.PORTRAIT -> assets.portraitTexture(command.portraitId)?.let {
                    batch.color = Color.WHITE
                    batch.draw(it, command.x, command.y, command.width, command.height)
                }
                HallBuyUnitSummaryDrawKind.PATCH -> assets.hallTexture(command.asset)?.let { texture ->
                    batch.color = Color.WHITE
                    NinePatch(texture, 3, 3, 3, 3).draw(batch, command.x, command.y, command.width, command.height)
                }
                HallBuyUnitSummaryDrawKind.SPRITE -> assets.hallTexture(command.asset)?.let {
                    batch.color = if (command.tint == HallBuyUnitSummaryTint.MUTED) Color(.58f, .58f, .58f, 1f) else Color.WHITE
                    batch.draw(it, command.x, command.y, command.width, command.height)
                }
                HallBuyUnitSummaryDrawKind.TEXT -> {
                    batch.color = Color.WHITE
                    assets.bodyFont.color = Color.BLACK
                    assets.bodyFont.draw(batch, command.text, command.x, command.y, command.width, command.align, false)
                }
            }
        }
        batch.color = Color.WHITE
    }
}
