// Dialogue
package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Align

/** 대화·선택지·모달의 공통 표시 밀집도를 유지하는 화면 중립 렌더러다. */
class DialogueRenderer(
    /** 화면별 원본 좌표와 크기를 보관하는 배치 설정이다. */
    private val layout: DialogueRenderLayout = DialogueRenderLayout(),
) {
    /** 대화 오버레이 모델의 모든 층을 정해진 순서로 렌더링한다. */
    fun draw(
        batch: SpriteBatch,
        shapes: ShapeRenderer,
        projection: Matrix4,
        model: DialogueOverlayModel,
        assets: DialogueRenderAssets,
    ) {
        shapes.projectionMatrix = projection
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        drawModalBackdrop(shapes, model.modal)
        shapes.end()

        batch.projectionMatrix = projection
        batch.begin()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        model.dialogue?.let { drawDialogue(batch, assets, it) }
        model.choice?.let { drawChoice(batch, assets, it) }
        model.modal?.let { drawModalText(batch, assets, it) }
        batch.end()
    }

    /** 대사창·초상화·화자·본문을 하나의 공용 레이어로 그린다. */
    private fun drawDialogue(batch: SpriteBatch, assets: DialogueRenderAssets, model: DialogueRenderModel) {
        val stage = model.componentStage
        val include = { target: DialogueRenderStage -> stage == null || stage == target || stage == DialogueRenderStage.CHARACTERS }
        val y = model.panelYOverride ?: (layout.panelY + if (model.isAtTop) layout.topOffsetY else 0f)
        val x = model.panelXOverride ?: if (model.isLeft) layout.panelLeftX else layout.panelRightX
        val texture = assets.dialoguePanel
        if (include(DialogueRenderStage.PANEL)) texture?.let { drawMirrored(batch, it, x, y, layout.panelWidth, layout.panelHeight, model.isLeft) }
        if (include(DialogueRenderStage.PORTRAIT)) {
            model.portraitId?.let(assets::portrait)?.let {
                val portraitX = if (model.isLeft) layout.portraitLeftX else layout.portraitRightX
                val bounds = DialoguePortraitGeometry.fit(it, portraitX, y - 2.15f, layout.portraitWidth, layout.portraitHeight)
                batch.draw(it, bounds.x, bounds.y, bounds.width, bounds.height)
            }
        }
        if (include(DialogueRenderStage.SPEAKER)) {
            assets.speakerFont.color = Color.WHITE
            assets.speakerFont.draw(batch, model.speaker, if (model.isLeft) layout.speakerLeftX else layout.speakerRightX, y + layout.speakerOffsetY)
        }
        if (include(DialogueRenderStage.TEXT)) {
            assets.bodyFont.color = Color.BLACK
            assets.bodyFont.draw(
                batch,
                model.visibleText,
                if (model.isLeft) layout.textLeftX else layout.textRightX,
                y + layout.textOffsetY,
                layout.textWidth,
                Align.left,
                true,
            )
        }
    }

    /** 선택지 패널과 선택 강조 표시를 대사 렌더러와 동일한 배치 흐름으로 그린다. */
    private fun drawChoice(batch: SpriteBatch, assets: DialogueRenderAssets, model: ChoiceRenderModel) {
        if (model.isConfirmation) {
            drawConfirmation(batch, assets, model)
            return
        }
        assets.choicePanel?.let { batch.draw(it, 70f, 46f, layout.width - 140f, 220f) }
        if (assets.choicePanel == null) {
            assets.titleFont.color = Color(1f, .85f, .48f, 1f)
            assets.titleFont.draw(batch, model.title, 94f, 234f)
        }
        model.options.forEachIndexed { index, option ->
            val selected = index == model.selectedIndex
            assets.choiceRow?.let { batch.draw(it, 90f, 90f + (model.options.size - index - 1) * 42f, layout.width - 180f, 38.7f) }
            assets.bodyFont.color = if (selected) Color(1f, .86f, .43f, 1f) else Color.WHITE
            assets.bodyFont.draw(batch, if (selected) "▶ $option" else "  $option", 110f, 190f - index * 42f)
        }
        assets.bodyFont.color = Color(.72f, .80f, .90f, 1f)
        assets.bodyFont.draw(batch, "↑↓ 선택 · Enter / 클릭 확정", layout.width - 430f, 72f)
    }

    /** 두 버튼 확인 상자를 선택지 모델의 특수한 변형으로 그린다. */
    private fun drawConfirmation(batch: SpriteBatch, assets: DialogueRenderAssets, model: ChoiceRenderModel) {
        assets.choicePanel?.let { batch.draw(it, 464.13f, 276.92f, 351.74f, 134.16f) }
        assets.titleFont.color = Color.BLACK
        val title = model.title.ifBlank { "확인" }
        assets.titleFont.draw(batch, title, 498.19f, 411f)
        val yes = model.options.getOrNull(0) ?: "예"
        val no = model.options.getOrNull(1) ?: "아니오"
        assets.bodyFont.color = Color.BLACK
        assets.bodyFont.draw(batch, yes, 555.51f, 328.39f)
        assets.bodyFont.draw(batch, no, 718.94f, 328.39f)
    }

    /** 모달 종류에 맞는 불투명 배경과 정보 패널을 먼저 그린다. */
    private fun drawModalBackdrop(shapes: ShapeRenderer, modal: ModalRenderModel?) {
        when (modal?.kind) {
            null -> Unit
            DialogueModalKind.MAP_INFO -> {
                shapes.color = Color(0f, 0f, 0f, 127f / 255f)
                shapes.rect(0f, 0f, layout.width, 138.46f)
            }
            DialogueModalKind.SECTION -> {
                shapes.color = Color.BLACK
                shapes.rect(0f, 0f, layout.width, layout.height)
            }
            DialogueModalKind.AMBITION -> {
                shapes.color = Color(0f, 0f, 0f, 30f / 255f)
                shapes.rect(0f, 0f, layout.width, layout.height)
            }
            DialogueModalKind.EVENT, DialogueModalKind.INFO -> Unit
            DialogueModalKind.OTHER -> {
                shapes.color = Color(.035f, .045f, .055f, .94f)
                shapes.rect(0f, 0f, layout.width, layout.height)
            }
        }
    }

    /** 모달 본문을 종류별 글꼴·위치 규칙으로 그린다. */
    private fun drawModalText(batch: SpriteBatch, assets: DialogueRenderAssets, model: ModalRenderModel) {
        val text = sanitize(model.fixedText + model.visibleText)
        when (model.kind) {
            DialogueModalKind.MAP_INFO -> {
                assets.bodyFont.color = Color.WHITE
                assets.bodyFont.draw(batch, text, 26f, 119f)
            }
            DialogueModalKind.SECTION -> {
                val glyph = GlyphLayout(assets.titleFont, model.text)
                assets.titleFont.color = Color.WHITE
                assets.titleFont.draw(batch, glyph, (layout.width - glyph.width) / 2f, (layout.height + glyph.height) / 2f)
            }
            DialogueModalKind.EVENT, DialogueModalKind.INFO, DialogueModalKind.OTHER -> {
                val glyph = GlyphLayout(assets.titleFont, sanitize(model.visibleText))
                val width = (glyph.width + 42.4f).coerceIn(64.2f, layout.width - 64.2f)
                val height = (glyph.height + 34.4f).coerceAtLeast(71.38f)
                val x = (layout.width - width) / 2f
                val y = (layout.height - height) / 2f + height * .22f
                assets.infoPanel?.draw(batch, x, y, width, height)
                    ?: assets.dialoguePanel?.let { batch.draw(it, x, y, width, height) }
                assets.titleFont.color = Color.BLACK
                assets.titleFont.draw(batch, glyph, (layout.width - glyph.width) / 2f, y + height / 2f + glyph.height / 2f)
            }
            DialogueModalKind.AMBITION -> Unit
        }
    }

    /** 좌우 말풍선 방향을 텍스처 미러링으로 보존한다. */
    private fun drawMirrored(batch: SpriteBatch, texture: Texture, x: Float, y: Float, width: Float, height: Float, flipX: Boolean) {
        batch.draw(texture, x, y, width, height, 0, 0, texture.width, texture.height, flipX, false)
    }

    /** 원본 텍스트에 남아 있는 색상 제어 토큰을 화면 문자열에서 제거한다. */
    private fun sanitize(text: String): String = text.replace(Regex("\\[C[0-9A-Fa-f]+"), "").replace('☆', '★')
}
