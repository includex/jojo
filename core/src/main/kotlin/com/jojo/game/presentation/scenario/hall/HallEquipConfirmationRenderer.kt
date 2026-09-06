// Scenario
package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** HallEquipConfirmationRenderer: 장비 교체 전후 능력치와 확인·취소 버튼을 비교 모달에 그린다. */
internal object HallEquipConfirmationRenderer {
    /**
     * `SCALE` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private const val SCALE = .86f

    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
