package com.jojo.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align

/** Stateless renderer for the immutable equipment confirmation overlay. */
internal object HallEquipConfirmationRenderer {
    private const val SCALE = .86f

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallEquipConfirmationView) {
        HallEquipConfirmationRenderPlan.commands(view).forEach { command ->
            when (command.kind) {
                HallEquipConfirmationDrawKind.OVERLAY -> {
                    batch.color = Color(0f, 0f, 0f, 40f / 255f)
                    batch.draw(assets.overlayPixel, 0f, 0f, 1280f, 688f)
                    batch.color = Color.WHITE
                }
                HallEquipConfirmationDrawKind.PATCH -> assets.hallTexture(command.asset)?.let { texture ->
                    NinePatch(texture, 3, 3, 3, 3).draw(
                        batch,
                        command.x * SCALE,
                        command.y * SCALE,
                        command.width * SCALE,
                        command.height * SCALE,
                    )
                }
                HallEquipConfirmationDrawKind.TEXT -> {
                    assets.bodyFont.color = when (command.color) {
                        HallEquipConfirmationTextColor.BLACK -> Color.BLACK
                        HallEquipConfirmationTextColor.GREEN -> Color(12f / 255f, 125f / 255f, 0f, 1f)
                        HallEquipConfirmationTextColor.RED -> Color(185f / 255f, 6f / 255f, 6f / 255f, 1f)
                    }
                    if (command.width == 0f) {
                        assets.bodyFont.draw(batch, command.text, command.x * SCALE, command.y * SCALE)
                    } else {
                        assets.bodyFont.draw(batch, command.text, command.x * SCALE, command.y * SCALE, command.width * SCALE, Align.center, false)
                    }
                }
            }
        }
        batch.color = Color.WHITE
    }
}
