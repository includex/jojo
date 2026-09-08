// Scenario
package com.jojo.game.presentation.scenario.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets
import com.jojo.game.presentation.scenario.story.ScenarioDialogueRendererAssetsAdapter
import com.jojo.game.presentation.scenario.story.ScenarioStoryRenderer
import com.jojo.game.presentation.scenario.story.ScenarioStreetDialogueStages
import com.jojo.game.presentation.scenario.story.ScenarioStreetDialogueView
import com.jojo.game.presentation.scenario.story.toDialogueRenderModel
import com.jojo.game.presentation.shared.dialogue.ChoiceRenderModel
import com.jojo.game.presentation.shared.dialogue.DialogueModalKind
import com.jojo.game.presentation.shared.dialogue.DialogueOverlayModel
import com.jojo.game.presentation.shared.dialogue.DialogueRenderModel
import com.jojo.game.presentation.shared.dialogue.DialogueRenderStage
import com.jojo.game.presentation.shared.dialogue.DialogueRenderer
import com.jojo.game.presentation.shared.dialogue.ChoiceScene2dLayer
import com.jojo.game.presentation.shared.dialogue.ModalRenderModel

/** ScenarioOverlayRenderer: 대사·선택·모달처럼 장면 위에 겹치는 시나리오 오버레이를 그린다. */
internal object ScenarioOverlayRenderer {
    /** 시나리오와 전투가 공유하는 대화·선택·모달 표시기다. */
    private val dialogueRenderer = DialogueRenderer()

    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun draw(
        assets: ScenarioSceneAssets,
        batch: SpriteBatch,
        shapes: ShapeRenderer,
        projection: Matrix4,
        view: ScenarioOverlayRenderView,
        choiceLayer: ChoiceScene2dLayer? = null,
    ) {
        // 대사와 모달은 원본 좌표를 재현하는 공용 렌더러가 그린다. Scene2D의 일반 위젯 배치는
        // 원본 말풍선·야망 표시 구조와 맞지 않고, 특히 모달은 내부 표시용 문자열을 그대로 찍어
        // 버린다. 선택창만 [ChoiceScene2dLayer]가 맡는데, 그 계층은 일반 위젯 배치가 아니라
        // 그리기와 같은 원본 좌표를 쓰므로 화면이 달라지지 않는다.
        val model = dialogueOverlayModel(view)
        val rendererAssets = ScenarioDialogueRendererAssetsAdapter(assets)
        val handledByLayer = choiceLayer?.handles(model.choice) == true
        choiceLayer?.present(model.choice, rendererAssets)
        dialogueRenderer.draw(
            batch, shapes, projection,
            if (handledByLayer) model.copy(choice = null) else model,
            rendererAssets,
        )
    }

    /**
     * 거리 대사 캡처 단계를 실행 화면과 동일한 공용 렌더러로 그린다.
     *
     * 원본 캡처가 노드 가시성을 하나씩 켜며 누적하는 것과 같은 순서를 [DialogueRenderStage]로
     * 표현한다. 실행 화면과 캡처가 같은 코드·같은 좌표를 쓰므로 캡처 대조 결과가 곧 실행
     * 화면의 결과다.
     */
    fun drawStreetStage(
        assets: ScenarioSceneAssets,
        batch: SpriteBatch,
        shapes: ShapeRenderer,
        projection: Matrix4,
        view: ScenarioStreetDialogueView,
        stageIndex: Int,
    ) {
        val stage = when (ScenarioStreetDialogueStages.nameAt(stageIndex)) {
            "panel" -> DialogueRenderStage.PANEL
            "portrait" -> DialogueRenderStage.PORTRAIT
            "speaker" -> DialogueRenderStage.SPEAKER
            "text" -> DialogueRenderStage.TEXT
            "background" -> DialogueRenderStage.BACKGROUND
            "characters" -> DialogueRenderStage.CHARACTERS
            else -> return
        }
        val dialogue = view.toDialogueRenderModel()?.copy(componentStage = stage)
            // 대사가 아직 없어도 원본은 말풍선 패널을 먼저 보여 준다.
            ?: DialogueRenderModel(speaker = "", visibleText = "", isLeft = view.isLeft, isAtTop = view.isAtTop, componentStage = DialogueRenderStage.PANEL)
        dialogueRenderer.draw(
            batch,
            shapes,
            projection,
            DialogueOverlayModel(dialogue = dialogue),
            ScenarioDialogueRendererAssetsAdapter(assets),
        )
    }

    /** 시나리오 상태를 두 렌더링 구현이 공유하는 공용 모델로 변환한다. */
    private fun dialogueOverlayModel(view: ScenarioOverlayRenderView): DialogueOverlayModel = DialogueOverlayModel(
        dialogue = if (view.state == ScenarioOverlayState.DIALOGUE) view.dialogue?.toDialogueRenderModel() else null,
        choice = if (view.state == ScenarioOverlayState.CHOICE) view.choice?.let {
            ChoiceRenderModel(
                title = if (it.isAsk) "확인" else "전술 선택",
                options = it.options,
                selectedIndex = it.selectedIndex,
                portraitId = it.portraitId,
                isConfirmation = it.isAsk,
                firstVisibleIndex = it.firstVisibleIndex,
            )
        } else null,
        modal = if (view.state == ScenarioOverlayState.MODAL) view.modal?.let {
            ModalRenderModel(
                kind = it.kind.toDialogueModalKind(),
                text = it.text,
                visibleText = it.visibleText.ifEmpty { it.text.take(1) },
                fixedText = it.fixedText,
            )
        } else null,
    )

    /** 시나리오 모달 종류를 공용 Scene2D·SpriteBatch 모달 종류로 변환한다. */
    private fun ScenarioOverlayModalKind.toDialogueModalKind(): DialogueModalKind = when (this) {
        ScenarioOverlayModalKind.EVENT -> DialogueModalKind.EVENT
        ScenarioOverlayModalKind.INFO -> DialogueModalKind.INFO
        ScenarioOverlayModalKind.MAP_INFO -> DialogueModalKind.MAP_INFO
        ScenarioOverlayModalKind.SECTION -> DialogueModalKind.SECTION
        ScenarioOverlayModalKind.AMBITION -> DialogueModalKind.AMBITION
        ScenarioOverlayModalKind.OTHER -> DialogueModalKind.OTHER
    }

    /** 화면 상태를 공용 대화 렌더 모델로 변환해 한 번에 그린다. */
    private fun drawSharedDialogueOverlay(
        assets: ScenarioSceneAssets,
        batch: SpriteBatch,
        shapes: ShapeRenderer,
        projection: Matrix4,
        view: ScenarioOverlayRenderView,
    ) {
        dialogueRenderer.draw(batch, shapes, projection, dialogueOverlayModel(view), ScenarioDialogueRendererAssetsAdapter(assets))
    }

    /**
     * `drawBackdrop`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawBackdrop(assets: ScenarioSceneAssets, batch: SpriteBatch, shapes: ShapeRenderer, projection: Matrix4, modal: ScenarioModalRenderView?) {
        when (modal?.kind) {
            null -> Unit
            ScenarioOverlayModalKind.EVENT, ScenarioOverlayModalKind.INFO -> {
                val text = sanitize(modal.visibleText.ifEmpty { modal.text.take(1) }); val layout = GlyphLayout(assets.titleFont, text)
                val width = when (modal.variant) { com.jojo.game.application.runtime.RuntimeScenarioOverlay.GET_ITEM_EQUIPMENT -> (259.72f + 40f) * .86f; com.jojo.game.application.runtime.RuntimeScenarioOverlay.GET_ITEM_PROPERTY -> (324.47f + 40f) * .86f; else -> (layout.width + 42.4f).coerceIn(64.2f, 1120f) }
                val height = if (modal.variant in setOf(com.jojo.game.application.runtime.RuntimeScenarioOverlay.GET_ITEM_EQUIPMENT, com.jojo.game.application.runtime.RuntimeScenarioOverlay.GET_ITEM_PROPERTY)) 83f * .86f else (layout.height + 34.4f).coerceAtLeast(71.38f)
                val x = (1280f - width) / 2f; val y = (688f - height) / 2f + height * .22f
                shapes.end(); batch.projectionMatrix = projection; batch.begin(); batch.color = Color.WHITE
                assets.infoPanelPatch?.draw(batch, x, y, width, height) ?: assets.dialoguePanelTexture?.let { batch.draw(it, x, y, width, height) }
                batch.end(); shapes.begin(ShapeRenderer.ShapeType.Filled)
            }
            ScenarioOverlayModalKind.MAP_INFO -> { shapes.color = Color(0f, 0f, 0f, 127f / 255f); shapes.rect(0f, 0f, 1280f, 138.46f) }
            ScenarioOverlayModalKind.SECTION -> { shapes.color = Color.BLACK; shapes.rect(0f, 0f, 1280f, 688f) }
            ScenarioOverlayModalKind.AMBITION -> { shapes.color = Color(0f, 0f, 0f, 30f / 255f); shapes.rect(0f, 0f, 1280f, 688f) }
            ScenarioOverlayModalKind.OTHER -> { shapes.color = Color(.035f, .045f, .055f, .94f); shapes.rect(0f, 0f, 1280f, 688f) }
        }
    }

    /**
     * `drawModalText`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawModalText(assets: ScenarioSceneAssets, batch: SpriteBatch, modal: ScenarioModalRenderView) {
        when (modal.kind) {
            ScenarioOverlayModalKind.SECTION -> { val l = GlyphLayout(assets.sectionFont, modal.text); val x = (1280f-l.width)/2f; val y=(688f+l.height)/2f; assets.sectionFont.color=Color(.58f,.58f,.58f,1f); assets.sectionFont.draw(batch,l,x+1.72f,y-1.72f); assets.sectionFont.color=Color.WHITE; assets.sectionFont.draw(batch,l,x,y) }
            ScenarioOverlayModalKind.INFO, ScenarioOverlayModalKind.EVENT, ScenarioOverlayModalKind.OTHER -> { val l=GlyphLayout(assets.titleFont,sanitize(modal.visibleText.ifEmpty{modal.text.take(1)})); assets.titleFont.color=Color.BLACK; assets.titleFont.draw(batch,l,(1280f-l.width)/2f,(688f+l.height)/2f+15.91f) }
            ScenarioOverlayModalKind.MAP_INFO -> { assets.streetDialogueFont.color=Color.WHITE; assets.streetDialogueFont.draw(batch,sanitize(modal.fixedText+modal.visibleText.ifEmpty{modal.text.take(1)}),26f,119f) }
            ScenarioOverlayModalKind.AMBITION -> Unit
        }
    }
    /**
     * `sanitize`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun sanitize(text: String): String = text.replace(Regex("\\[C[0-9A-Fa-f]+"), "").replace('☆', '★')
}
