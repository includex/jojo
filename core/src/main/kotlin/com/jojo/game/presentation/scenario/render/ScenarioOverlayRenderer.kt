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
import com.jojo.game.presentation.scenario.story.ScenarioStoryRenderer

/** ScenarioOverlayRenderer: 시나리오 오버레이 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object ScenarioOverlayRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, shapes: ShapeRenderer, projection: Matrix4, view: ScenarioOverlayRenderView) {
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

    private fun drawChoice(assets: ScenarioSceneAssets, batch: SpriteBatch, view: ScenarioChoiceRenderView) {
        if (view.isAsk) { drawAskBox(assets, batch); return }
        batch.color = Color.WHITE; assets.choicePanelTexture?.let { batch.draw(it, 423.71f, 265.01f, 642.42f, 157.98f) }
        assets.choiceRowTexture?.let { texture -> repeat(view.options.take(3).size) { batch.draw(texture, 463.44f, 377.11f - it * 42.14f, 593.92f, 38.7f) } }
        view.portraitId?.let(assets::portraitTexture)?.let { batch.draw(it, 231.08f, 240.21f, 165.12f, 206.4f) }
        view.options.take(3).forEachIndexed { index, option -> assets.bodyFont.color = Color(.06f, .06f, .06f, 1f); assets.bodyFont.draw(batch, option, 482.88f, 407f - index * 42.14f) }
    }

    private fun drawAskBox(assets: ScenarioSceneAssets, batch: SpriteBatch) {
        val logo = assets.hallTexture("maps/ui/start-battle/logo9.png")
        val panel = assets.hallTexture("maps/ui/hall-menu/inner.png")?.let { NinePatch(it, 3, 3, 3, 3) }
        val title = assets.hallTexture("maps/ui/hall-menu/panel.png")
        val button = assets.hallTexture("maps/ui/hall-menu/button.png")?.let { NinePatch(it, 9, 9, 7, 11) }
        batch.color = Color.WHITE; logo?.let { batch.draw(it, 464.13f, 276.92f, 351.74f, 134.16f) }; title?.let { batch.draw(it, 464.13f, 368.08f, 351.74f, 43f) }
        val layout = GlyphLayout(); assets.titleFont.color = Color.BLACK
        fun label(text: String, x: Float, y: Float) { layout.setText(assets.titleFont, text); assets.titleFont.draw(batch, layout, x - layout.width / 2f, y + layout.height / 2f) }
        label("확인", 498.19f, 389.58f); panel?.draw(batch, 464.13f, 276.92f, 351.74f, 134.16f); button?.draw(batch, 482.84f, 306.16f, 145.34f, 43f)
        label("예", 555.51f, 328.39f); button?.draw(batch, 646.27f, 306.16f, 145.34f, 43f); label("비", 718.94f, 328.39f)
    }

    private fun drawModalText(assets: ScenarioSceneAssets, batch: SpriteBatch, modal: ScenarioModalRenderView) {
        when (modal.kind) {
            ScenarioOverlayModalKind.SECTION -> { val l = GlyphLayout(assets.sectionFont, modal.text); val x = (1280f-l.width)/2f; val y=(688f+l.height)/2f; assets.sectionFont.color=Color(.58f,.58f,.58f,1f); assets.sectionFont.draw(batch,l,x+1.72f,y-1.72f); assets.sectionFont.color=Color.WHITE; assets.sectionFont.draw(batch,l,x,y) }
            ScenarioOverlayModalKind.INFO, ScenarioOverlayModalKind.EVENT, ScenarioOverlayModalKind.OTHER -> { val l=GlyphLayout(assets.titleFont,sanitize(modal.visibleText.ifEmpty{modal.text.take(1)})); assets.titleFont.color=Color.BLACK; assets.titleFont.draw(batch,l,(1280f-l.width)/2f,(688f+l.height)/2f+15.91f) }
            ScenarioOverlayModalKind.MAP_INFO -> { assets.streetDialogueFont.color=Color.WHITE; assets.streetDialogueFont.draw(batch,sanitize(modal.fixedText+modal.visibleText.ifEmpty{modal.text.take(1)}),26f,119f) }
            ScenarioOverlayModalKind.AMBITION -> Unit
        }
    }
    private fun sanitize(text: String): String = text.replace(Regex("\\[C[0-9A-Fa-f]+"), "").replace('☆', '★')
}
