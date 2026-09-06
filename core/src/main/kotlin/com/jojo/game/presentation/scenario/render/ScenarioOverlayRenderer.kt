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
import com.jojo.game.presentation.scenario.story.toDialogueRenderModel
import com.jojo.game.presentation.shared.dialogue.ChoiceRenderModel
import com.jojo.game.presentation.shared.dialogue.DialogueModalKind
import com.jojo.game.presentation.shared.dialogue.DialogueOverlayModel
import com.jojo.game.presentation.shared.dialogue.DialogueRenderer
import com.jojo.game.presentation.shared.dialogue.DialogueScene2dHost
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
        scene2dHost: DialogueScene2dHost? = null,
    ) {
        val model = dialogueOverlayModel(view)
        if (scene2dHost != null) {
            scene2dHost.present(model)
            scene2dHost.render()
        } else {
            dialogueRenderer.draw(batch, shapes, projection, model, ScenarioDialogueRendererAssetsAdapter(assets))
        }
    }

    /** 시나리오 상태를 두 렌더링 구현이 공유하는 공용 모델로 변환한다. */
    private fun dialogueOverlayModel(view: ScenarioOverlayRenderView): DialogueOverlayModel = DialogueOverlayModel(
        dialogue = if (view.state == ScenarioOverlayState.DIALOGUE) view.dialogue?.toDialogueRenderModel() else null,
        choice = if (view.state == ScenarioOverlayState.CHOICE) view.choice?.let {
            ChoiceRenderModel(
                title = if (it.isAsk) "확인" else "전술 선택",
                options = it.options.take(3),
                selectedIndex = 0,
                portraitId = it.portraitId,
                isConfirmation = it.isAsk,
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

    /** 이전 화면별 렌더링 규칙을 보존하기 위한 호환 진입점이다. */
    @Suppress("UNUSED_PRIVATE_MEMBER")
    private fun drawLegacyOverlay(assets: ScenarioSceneAssets, batch: SpriteBatch, shapes: ShapeRenderer, projection: Matrix4, view: ScenarioOverlayRenderView) {
        shapes.projectionMatrix = projection
        Gdx.gl.glEnable(GL20.GL_BLEND); Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        drawBackdrop(assets, batch, shapes, projection, view.modal)
        shapes.end(); Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.projectionMatrix = projection; batch.begin()
        when (view.state) {
            ScenarioOverlayState.DIALOGUE -> view.dialogue?.let { ScenarioStoryRenderer.drawStreetDialogue(assets, batch, it, 3) }
            ScenarioOverlayState.CHOICE -> view.choice?.let { drawChoice(assets, batch, it) }
            ScenarioOverlayState.MODAL -> view.modal?.let { drawModalText(assets, batch, it) }
            ScenarioOverlayState.DELAY -> Unit
        }
        batch.end()
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
     * `drawChoice`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawChoice(assets: ScenarioSceneAssets, batch: SpriteBatch, view: ScenarioChoiceRenderView) {
        if (view.isAsk) { drawAskBox(assets, batch); return }
        batch.color = Color.WHITE; assets.choicePanelTexture?.let { batch.draw(it, 423.71f, 265.01f, 642.42f, 157.98f) }
        assets.choiceRowTexture?.let { texture -> repeat(view.options.take(3).size) { batch.draw(texture, 463.44f, 377.11f - it * 42.14f, 593.92f, 38.7f) } }
        view.portraitId?.let(assets::portraitTexture)?.let { batch.draw(it, 231.08f, 240.21f, 165.12f, 206.4f) }
        view.options.take(3).forEachIndexed { index, option -> assets.bodyFont.color = Color(.06f, .06f, .06f, 1f); assets.bodyFont.draw(batch, option, 482.88f, 407f - index * 42.14f) }
    }

    /**
     * `drawAskBox`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawAskBox(assets: ScenarioSceneAssets, batch: SpriteBatch) {
        val logo = assets.hallTexture("maps/ui/start-battle/logo9.png")
        val panel = assets.hallTexture("maps/ui/hall-menu/inner.png")?.let { NinePatch(it, 3, 3, 3, 3) }
        val title = assets.hallTexture("maps/ui/hall-menu/panel.png")
        val button = assets.hallTexture("maps/ui/hall-menu/button.png")?.let { NinePatch(it, 9, 9, 7, 11) }
        batch.color = Color.WHITE; logo?.let { batch.draw(it, 464.13f, 276.92f, 351.74f, 134.16f) }; title?.let { batch.draw(it, 464.13f, 368.08f, 351.74f, 43f) }
        val layout = GlyphLayout(); assets.titleFont.color = Color.BLACK
        /**
         * `label`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun label(text: String, x: Float, y: Float) { layout.setText(assets.titleFont, text); assets.titleFont.draw(batch, layout, x - layout.width / 2f, y + layout.height / 2f) }
        label("확인", 498.19f, 389.58f); panel?.draw(batch, 464.13f, 276.92f, 351.74f, 134.16f); button?.draw(batch, 482.84f, 306.16f, 145.34f, 43f)
        label("예", 555.51f, 328.39f); button?.draw(batch, 646.27f, 306.16f, 145.34f, 43f); label("비", 718.94f, 328.39f)
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
