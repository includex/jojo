// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align

/** 자동 전투 오버레이의 표시 종류입니다. */
enum class BattleAutoOverlayKind { NONE, PROMPT, TUOGUAN }

/** 자동 전투 오버레이에 표시할 상태입니다. */
data class BattleAutoOverlayView(
    val overlay: BattleAutoOverlayKind,
    val checked: Boolean = false,
    val offersDelegation: Boolean = true,
)

/** Shared authored geometry for rendering and hit-testing MsgBox/MsgBox4 prompts. */
internal object BattleAutoPromptGeometry {
    const val PANEL_X = 426.686f
    const val PANEL_Y = 252f
    const val PANEL_WIDTH = 635f
    const val PANEL_HEIGHT = 296f

    fun buttonAt(x: Float, y: Float, offersDelegation: Boolean): Int? =
        if (offersDelegation) when {
            x in 844.536f..994.536f && y in 270.197f..320.197f -> 0
            x in 674.536f..824.536f && y in 270.197f..320.197f -> 1
            else -> null
        } else when {
            x in 754.186f..934.186f && y in 271.285f..321.285f -> 0
            x in 554.186f..734.186f && y in 271.285f..321.285f -> 1
            else -> null
        }

    fun toggleAt(x: Float, y: Float, offersDelegation: Boolean): Boolean =
        offersDelegation && x in 518.416f..640.457f && y in 267.997f..322.397f

    fun confirmCenter(offersDelegation: Boolean): Pair<Float, Float> =
        if (offersDelegation) 919.536f to 295.197f else 844.186f to 296.285f

    fun panelCancelAt(x: Float, y: Float, offersDelegation: Boolean): Boolean =
        offersDelegation || x !in PANEL_X..(PANEL_X + PANEL_WIDTH) || y !in PANEL_Y..(PANEL_Y + PANEL_HEIGHT)
}

/** 오버레이 렌더링에 필요한 텍스처 묶음입니다. */
data class BattleAutoOverlayAssets(
    val unitInfoLogo: Texture,
    val unitInfoBox: Texture,
    val toggle: Texture,
    val checkmark: Texture,
    val banner: Texture,
    val plate: Texture,
    /** 원본 MsgBox4 `bg0/Logo_3-1`(53×62, 2배). 없으면 바탕 무늬로 대신 그린다. */
    val logo: Texture? = null,
    /** Plain MsgBox background and sliced frame; separate from the manual MsgBox4 assets. */
    val plainBackground: Texture? = null,
    val plainBox: NinePatch? = null,
    val plainLogo: Texture? = null,
)

/** 자동 전투 확인창과 위임 안내를 그리는 렌더러입니다. */
class BattleAutoOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `labelFont` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val labelFont: BitmapFont,
    /** `assets` (BattleAutoOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattleAutoOverlayAssets,
    /** 원본 MsgBox4 프리팹 라벨 글꼴. 없으면 `labelFont`로 그린다. */
    private val promptFonts: PromptFonts? = null,
    private val plainPromptFonts: PlainPromptFonts? = null,
) {
    /**
     * 원본 MsgBox4 프리팹: 모든 라벨 fontSize 40, LabelOutline 2px.
     * 본문 (147,97,0)/(255,250,110), 위임 (0,5,255)/(115,238,255), 비 (252,0,0)/(255,212,212), 예 (2,110,0)/(124,255,153).
     */
    class PromptFonts(val message: BitmapFont, val toggle: BitmapFont, val no: BitmapFont, val yes: BitmapFont)
    class PlainPromptFonts(val message: BitmapFont, val no: BitmapFont, val yes: BitmapFont)

    /** 현재 오버레이 상태를 화면에 그립니다. */
    fun draw(view: BattleAutoOverlayView) {
        if (view.overlay == BattleAutoOverlayKind.NONE) return
        batch.begin()
        batch.color = Color.WHITE
        when (view.overlay) {
            BattleAutoOverlayKind.PROMPT -> if (view.offersDelegation) drawPrompt(view.checked) else drawPlainPrompt()
            BattleAutoOverlayKind.TUOGUAN -> drawTuoGuan()
            BattleAutoOverlayKind.NONE -> Unit
        }
        batch.color = Color.WHITE
        batch.end()
    }

    /**
     * `drawPrompt`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawPrompt(checked: Boolean) {
        for (ty in 0..3) for (tx in 0..6) {
            val width = minOf(96f, 635f - tx * 96f)
            val height = minOf(96f, 296f - ty * 96f)
            if (width > 0f && height > 0f) {
                batch.draw(assets.unitInfoLogo, 426.686f + tx * 96f, 252f + ty * 96f, width, height)
            }
        }
        batch.draw(assets.unitInfoBox, 426.686f, 252f, 635f, 296f)
        batch.draw(assets.logo ?: assets.unitInfoLogo, 453.005f, 373.951f, 106f, 124f)
        val fonts = promptFonts
        if (fonts == null) {
            labelFont.color = Color.WHITE
            labelFont.draw(batch, "모든 부대의 명령을 종료하시겠습니까?", 573.686f, 490f, 463f, Align.center, true)
        } else {
            // label 노드 (573.686, 335) 463×190, lineHeight 42, 세로 가운데 정렬.
            val message = "모든 부대의 명령을 종료하시겠습니까?"
            val layout = GlyphLayout(fonts.message, message, fonts.message.color, 463f, Align.center, true)
            fonts.message.draw(batch, message, 573.686f, 335f + 95f + layout.height / 2f, 463f, Align.center, true)
        }
        batch.draw(assets.toggle, 518.416f, 281.197f, 28f, 28f)
        if (checked) batch.draw(assets.checkmark, 518.416f, 281.197f, 28f, 28f)
        // tuoguan/label 노드 (567.257, 267.997) 73.2×54.4; 단추 Label 노드 (699.536/869.536, 278.042) 100×40.
        val toggleFont = fonts?.toggle ?: labelFont
        toggleFont.draw(batch, "위임", 567.257f, 267.997f + 27.2f + toggleFont.capHeight / 2f, 73.2f, Align.center, false)
        listOf(Triple(674.536f, "비", fonts?.no ?: labelFont), Triple(844.536f, "예", fonts?.yes ?: labelFont)).forEach { (x, label, font) ->
            batch.draw(assets.unitInfoBox, x, 270.197f, 150f, 50f)
            font.draw(batch, label, x + 25f, 278.042f + 20f + font.capHeight / 2f, 100f, Align.center, false)
        }
    }

    /** Source automatic all-units-acted prompt uses plain MsgBox, with no delegation toggle. */
    private fun drawPlainPrompt() {
        assets.plainBackground?.let { background ->
            for (ty in 0..3) for (tx in 0..6) {
                val width = minOf(96f, 635f - tx * 96f)
                val height = minOf(96f, 296f - ty * 96f)
                if (width > 0f && height > 0f) {
                    batch.draw(background, 426.686f + tx * 96f, 252f + ty * 96f, width, height)
                }
            }
        }
        assets.plainBox?.draw(batch, 426.686f, 252f, 635f, 296f)
        assets.plainLogo?.let { batch.draw(it, 453.005f, 373.951f, 106f, 124f) }
        val fonts = plainPromptFonts ?: return
        val message = "모든 부대의 명령을 종료하시겠습니까?"
        val layout = com.badlogic.gdx.graphics.g2d.GlyphLayout(
            fonts.message, message, fonts.message.color, 463f, Align.left, true,
        )
        fonts.message.draw(batch, message, 573.686f, 335f + 95f + layout.height / 2f, 463f, Align.left, true)
        listOf(
            Triple(554.186f, "비", fonts.no),
            Triple(754.186f, "예", fonts.yes),
        ).forEach { (x, label, font) ->
            assets.plainBox?.draw(batch, x, 271.285f, 180f, 50f)
            font.draw(batch, label, x, 271.285f + 25f + font.capHeight / 2f, 180f, Align.center, false)
        }
    }

    /**
     * `drawTuoGuan`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawTuoGuan() {
        batch.draw(assets.banner, 0f, 0f, 1488.372f, 264f)
        batch.draw(assets.plate, 613.686f, 25.894f, 261f, 83f)
    }
}
