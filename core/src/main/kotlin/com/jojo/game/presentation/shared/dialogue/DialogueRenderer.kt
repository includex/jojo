// Dialogue
package com.jojo.game.presentation.shared.dialogue

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Gdx
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
    /**
     * 알파 채널을 따로 누적할지 여부다.
     *
     * 원본 Cocos는 색상과 알파의 블렌드 인자가 다르다. raw framebuffer 대조에서 알파까지
     * 맞춰야 하는 화면은 이 분리 블렌드를 쓰고, 기존 캡처가 단일 블렌드로 고정된 화면은
     * 끈다. 화면별로 검증된 값을 보존하기 위한 명시적 선택이다.
     */
    private val separateAlphaBlend: Boolean = true,
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
        // ShapeRenderer는 블렌딩을 스스로 켜지 않는다. 장면 렌더러가 GL_BLEND를 끈 채로
        // 넘겨주면 반투명 배경막(야망 30/255, 지도 안내 127/255)이 불투명한 검정으로 찍혀
        // 뒤의 장면을 통째로 가린다. 원본 Cocos 레이어는 언제나 알파 합성이므로 이 패스
        // 동안에만 블렌딩을 켜고 이전 상태로 되돌린다.
        val blendWasEnabled = Gdx.gl.glIsEnabled(GL20.GL_BLEND)
        if (!blendWasEnabled) Gdx.gl.glEnable(GL20.GL_BLEND)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        drawModalBackdrop(shapes, model.modal)
        shapes.end()
        if (!blendWasEnabled) Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.projectionMatrix = projection
        batch.begin()
        if (separateAlphaBlend) {
            batch.setBlendFunctionSeparate(
                GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA,
                GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA,
            )
        } else {
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        }
        model.dialogue?.let { drawDialogue(batch, assets, it) }
        model.choice?.let { drawChoice(batch, assets, it) }
        model.modal?.let { drawModalText(batch, assets, it) }
        batch.end()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
    }

    /**
     * 대사창·초상화·화자·본문을 하나의 공용 레이어로 그린다.
     *
     * 좌표는 언제나 [DialogueComponentPlacement] 하나로 정리한 뒤 사용한다. 전투처럼 화자를
     * 따라 창 전체가 움직이는 화면은 배치를 직접 넘기고, 시나리오처럼 좌우 고정 말풍선을 쓰는
     * 화면은 [layout]에서 배치를 만든다. 두 경우 모두 이 함수 하나가 그린다.
     */
    private fun drawDialogue(batch: SpriteBatch, assets: DialogueRenderAssets, model: DialogueRenderModel) {
        val stage = model.componentStage
        val include = { target: DialogueRenderStage -> stage == null || stage.includes(target) }
        val placement = resolvePlacement(model)
        if (include(DialogueRenderStage.PANEL)) {
            assets.dialoguePanel?.let {
                batch.color = Color.WHITE
                drawMirrored(batch, it, placement.panelX, placement.panelY, placement.panelWidth, placement.panelHeight, placement.mirrorPanel)
            }
        }
        if (include(DialogueRenderStage.PORTRAIT)) {
            (model.portraitTexture ?: model.portraitId?.let(assets::portrait))?.let {
                batch.color = Color.WHITE
                val bounds = DialoguePortraitGeometry.fit(
                    it,
                    placement.portraitX,
                    placement.portraitY,
                    placement.portraitWidth,
                    placement.portraitHeight,
                )
                batch.draw(it, bounds.x, bounds.y, bounds.width, bounds.height)
            }
        }
        if (include(DialogueRenderStage.SPEAKER)) drawSpeaker(batch, assets, model, placement)
        if (include(DialogueRenderStage.TEXT)) drawBody(batch, assets, model, placement)
    }

    /** 화자명을 원본 라벨처럼 채움색과 외곽선으로 그린다. */
    private fun drawSpeaker(
        batch: SpriteBatch,
        assets: DialogueRenderAssets,
        model: DialogueRenderModel,
        placement: DialogueComponentPlacement,
    ) {
        model.speakerOverlay?.let {
            drawOverlay(batch, it)
            return
        }
        val style = model.speakerStyle
        val font = assets.speakerFont
        // 화면별 글꼴은 이미 자체 배율을 구워 둘 수 있으므로 곱해서 적용하고 원래 값으로 되돌린다.
        val baseScaleX = font.data.scaleX
        val baseScaleY = font.data.scaleY
        font.data.setScale(baseScaleX * style.scaleX, baseScaleY * style.scaleY)
        // 외곽선 글꼴을 쓰지 않는 화면은 여덟 방향 오프셋으로 같은 두께의 테두리를 만든다.
        style.outlineColor?.takeIf { style.outlineWidth > 0f }?.let { outline ->
            val straight = style.outlineWidth
            val diagonal = style.outlineWidth * DIAGONAL_RATIO
            font.color = outline
            listOf(
                -straight to 0f, straight to 0f, 0f to -straight, 0f to straight,
                -diagonal to -diagonal, -diagonal to diagonal,
                diagonal to -diagonal, diagonal to diagonal,
            ).forEach { (dx, dy) ->
                font.draw(batch, model.speaker, placement.speakerX + dx, placement.speakerDrawY + dy)
            }
        }
        font.color = style.fillColor
        font.draw(batch, model.speaker, placement.speakerX, placement.speakerDrawY)
        font.data.setScale(baseScaleX, baseScaleY)
    }

    /** 본문을 줄바꿈 폭 안에서 그린다. 원본 래스터가 주어지면 글꼴 대신 그것을 그린다. */
    private fun drawBody(
        batch: SpriteBatch,
        assets: DialogueRenderAssets,
        model: DialogueRenderModel,
        placement: DialogueComponentPlacement,
    ) {
        model.bodyOverlay?.let {
            drawOverlay(batch, it)
            return
        }
        val font = assets.bodyFont
        val baseScaleX = font.data.scaleX
        val baseScaleY = font.data.scaleY
        font.data.setScale(baseScaleX, baseScaleY * model.bodyScaleY)
        font.color = Color.BLACK
        font.draw(batch, model.visibleText, placement.textX, placement.textDrawY, placement.textWidth, Align.left, true)
        font.data.setScale(baseScaleX, baseScaleY)
    }

    /** 글꼴 대신 넘어온 원본 래스터 조각을 그린다. */
    private fun drawOverlay(batch: SpriteBatch, overlay: DialogueTextureOverlay) {
        batch.color = overlay.tint
        batch.draw(overlay.texture, overlay.x, overlay.y, overlay.width, overlay.height)
        batch.color = Color.WHITE
    }

    /** 모델이 준 절대 배치를 그대로 쓰거나, 좌우 고정 레이아웃에서 배치를 만든다. */
    private fun resolvePlacement(model: DialogueRenderModel): DialogueComponentPlacement {
        model.componentPlacement?.let { return it }
        val y = model.panelYOverride ?: (layout.panelY + if (model.isAtTop) layout.topOffsetY else 0f)
        val x = model.panelXOverride ?: if (model.isLeft) layout.panelLeftX else layout.panelRightX
        return DialogueComponentPlacement(
            panelX = x,
            panelY = y,
            panelWidth = layout.panelWidth,
            panelHeight = layout.panelHeight,
            portraitX = if (model.isLeft) layout.portraitLeftX else layout.portraitRightX,
            portraitY = y + layout.portraitOffsetY,
            portraitWidth = layout.portraitWidth,
            portraitHeight = layout.portraitHeight,
            speakerX = if (model.isLeft) layout.speakerLeftX else layout.speakerRightX,
            speakerDrawY = y + layout.speakerOffsetY,
            textX = if (model.isLeft) layout.textLeftX else layout.textRightX,
            textDrawY = y + layout.textOffsetY,
            textWidth = layout.textWidth,
            mirrorPanel = model.isLeft,
        )
    }

    /** 선택지 패널과 선택 강조 표시를 대사 렌더러와 동일한 배치 흐름으로 그린다. */
    private fun drawChoice(batch: SpriteBatch, assets: DialogueRenderAssets, model: ChoiceRenderModel) {
        if (model.isConfirmation) {
            drawConfirmation(batch, assets, model)
            return
        }
        // 원본 ChooseLayer는 대사와 같은 말풍선 패널에 항목을 쌓고, 패널 왼쪽 바깥에 얼굴을
        // 둔다. 제목 문자열, 선택 화살표, 조작 안내 문구는 원본에 없으므로 그리지 않는다.
        batch.color = Color.WHITE
        assets.choicePanel?.let {
            batch.draw(it, layout.choicePanelX, layout.choicePanelY, layout.choicePanelWidth, layout.choicePanelHeight)
        }
        model.portraitId?.let(assets::portrait)?.let { texture ->
            val bounds = DialoguePortraitGeometry.fit(
                texture,
                layout.choicePortraitX,
                layout.choicePortraitY,
                layout.portraitWidth,
                layout.portraitHeight,
            )
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height)
        }
        val firstRowY = layout.choicePanelY + layout.choicePanelHeight - layout.choiceRowTopInset
        // 원본 ChooseLayer는 항목을 모두 만든 뒤 `view`(169) 안에서만 보여 주고 나머지는
        // 스크롤로 닿게 한다. 여기서는 보이는 창의 첫 항목만 옮겨 같은 접근성을 만든다.
        val first = model.firstVisibleIndex.coerceIn(0, maxOf(0, model.options.size - layout.choiceVisibleRows))
        model.options.drop(first).take(layout.choiceVisibleRows).forEachIndexed { row, option ->
            val index = first + row
            val rowY = firstRowY - row * layout.choiceRowSpacing
            assets.choiceRow?.let {
                batch.color = Color.WHITE
                batch.draw(it, layout.choiceRowX, rowY, layout.choiceRowWidth, layout.choiceRowHeight)
            }
            // 원본은 터치 전용이라 선택 표시가 없다. 키보드 조작을 위해 선택 항목만 원본 화자
            // 라벨과 같은 파란색으로 구분하고, 나머지는 원본처럼 검은색으로 그린다.
            assets.bodyFont.color =
                if (index == model.selectedIndex) Color(35f / 255f, 2f / 255f, 234f / 255f, 1f) else Color.BLACK
            assets.bodyFont.draw(batch, option, layout.choiceTextX, rowY + layout.choiceTextOffsetY)
        }
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

    private companion object {
        /** 여덟 방향 외곽선에서 대각선 오프셋이 갖는 비율이다. 원본 라벨의 둥근 테두리를 따른다. */
        const val DIAGONAL_RATIO = 0.707f

    }
}
