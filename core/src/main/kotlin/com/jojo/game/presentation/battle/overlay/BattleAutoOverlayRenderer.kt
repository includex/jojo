// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align

/** 자동 전투 오버레이의 표시 종류입니다. */
enum class BattleAutoOverlayKind { NONE, PROMPT, TUOGUAN }

/** 자동 전투 오버레이에 표시할 상태입니다. */
data class BattleAutoOverlayView(
    val overlay: BattleAutoOverlayKind,
    val checked: Boolean = false,
)

/** 오버레이 렌더링에 필요한 텍스처 묶음입니다. */
data class BattleAutoOverlayAssets(
    val unitInfoLogo: Texture,
    val unitInfoBox: Texture,
    val toggle: Texture,
    val checkmark: Texture,
    val banner: Texture,
    val plate: Texture,
)

/** 자동 전투 확인창과 위임 안내를 그리는 렌더러입니다. */
class BattleAutoOverlayRenderer(
    private val batch: SpriteBatch,
    private val labelFont: BitmapFont,
    private val assets: BattleAutoOverlayAssets,
) {
    /** 현재 오버레이 상태를 화면에 그립니다. */
    fun draw(view: BattleAutoOverlayView) {
        if (view.overlay == BattleAutoOverlayKind.NONE) return
        batch.begin()
        batch.color = Color.WHITE
        when (view.overlay) {
            BattleAutoOverlayKind.PROMPT -> drawPrompt(view.checked)
            BattleAutoOverlayKind.TUOGUAN -> drawTuoGuan()
            BattleAutoOverlayKind.NONE -> Unit
        }
        batch.color = Color.WHITE
        batch.end()
    }

    private fun drawPrompt(checked: Boolean) {
        for (ty in 0..3) for (tx in 0..6) {
            val width = minOf(96f, 635f - tx * 96f)
            val height = minOf(96f, 296f - ty * 96f)
            if (width > 0f && height > 0f) {
                batch.draw(assets.unitInfoLogo, 426.686f + tx * 96f, 252f + ty * 96f, width, height)
            }
        }
        batch.draw(assets.unitInfoBox, 426.686f, 252f, 635f, 296f)
        batch.draw(assets.unitInfoLogo, 453.005f, 373.951f, 106f, 124f)
        labelFont.color = Color.WHITE
        labelFont.draw(batch, "모든 부대의 명령을 종료하시겠습니까?", 573.686f, 490f, 463f, Align.center, true)
        batch.draw(assets.toggle, 518.416f, 281.197f, 28f, 28f)
        if (checked) batch.draw(assets.checkmark, 518.416f, 281.197f, 28f, 28f)
        labelFont.draw(batch, "위임", 567.257f, 313f, 73.2f, Align.center, false)
        listOf(674.536f to "비", 844.536f to "예").forEach { (x, label) ->
            batch.draw(assets.unitInfoBox, x, 270.197f, 150f, 50f)
            labelFont.draw(batch, label, x + 25f, 310f, 100f, Align.center, false)
        }
    }

    private fun drawTuoGuan() {
        batch.draw(assets.banner, 0f, 0f, 1488.372f, 264f)
        batch.draw(assets.plate, 613.686f, 25.894f, 261f, 83f)
    }
}
