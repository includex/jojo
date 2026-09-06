// Scenario
package com.jojo.game.presentation.scenario.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** ScenarioBattlefieldRenderer: 시나리오 Battlefield 렌더러이며, 시나리오 화면에 표시할 요소를 그린다. */
internal object ScenarioBattlefieldRenderer {
    fun draw(
        assets: ScenarioSceneAssets,
        batch: SpriteBatch,
        shapes: ShapeRenderer,
        camera: Camera,
        view: ScenarioBattlefieldRenderView,
    ) {
        val background = assets.backgroundTexture(view.backgroundId)
        batch.projectionMatrix = camera.combined
        batch.begin()
        background?.let { batch.color = Color.WHITE; batch.draw(it, 0f, 0f, 1280f, 688f) }
        batch.end()
        shapes.projectionMatrix = camera.combined
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        if (background == null) {
            shapes.color = if (view.backgroundId == 2) Color(0.49f, .39f, .24f, 1f) else Color(.25f, .33f, .28f, 1f)
            shapes.rect(0f, 0f, 1280f, 688f)
        }
        if (view.drawUnits) view.units.filter { it.visible && assets.unitTexture(it.textureAssetId) == null }.forEach { drawFallback(shapes, it) }
        shapes.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.projectionMatrix = camera.combined
        batch.begin()
        val entries = buildList {
            if (view.drawCharacters) {
                if (view.drawUnits) view.units.filter { it.visible }.forEach { add(Entry(it.zIndex, it.siblingOrder, unit = it)) }
                view.heads.filter { it.opacity > 0f }.forEach { add(Entry(it.zIndex, it.siblingOrder, head = it)) }
            }
        }
        entries.sortedWith(compareBy<Entry> { it.zIndex }.thenBy { it.siblingOrder }).forEach { entry ->
            entry.head?.let { drawHead(assets, batch, camera, it) }
            entry.unit?.let { drawUnit(assets, batch, it) }
        }
        batch.color = Color.WHITE
        batch.end()
    }

    private fun drawFallback(shapes: ShapeRenderer, unit: ScenarioBattlefieldUnitView) {
        val x = ScenarioBattlefieldRenderGeometry.mapX(unit.visualX, unit.visualY); val y = ScenarioBattlefieldRenderGeometry.mapY(unit.visualX, unit.visualY)
        val color = when (unit.id) { 0 -> Color(.23f, .45f, .20f, 1f); 157 -> Color(.32f, .24f, .60f, 1f); else -> Color(.72f, .12f, .10f, 1f) }
        shapes.color = Color(.05f, .05f, .06f, .38f); shapes.circle(x + 30f, y - 4f, 26f)
        shapes.color = color; shapes.circle(x + 30f, y + 30f, 25f); shapes.rect(x + 8f, y, 44f, 38f)
    }

    private fun drawHead(assets: ScenarioSceneAssets, batch: SpriteBatch, camera: Camera, head: ScenarioBattlefieldHeadView) {
        assets.portraitTexture(head.portraitId)?.let { texture ->
            val centerX = ScenarioBattlefieldRenderGeometry.headCenterX(head.visualX)
            val centerY = ScenarioBattlefieldRenderGeometry.headCenterY(head.visualY)
            batch.color = Color(1f, 1f, 1f, head.opacity)
            val scissors = Rectangle()
            ScissorStack.calculateScissors(camera, batch.transformMatrix, Rectangle(centerX - 55.04f, centerY - 68.8f, 110.08f, 137.6f), scissors)
            batch.flush()
            if (ScissorStack.pushScissors(scissors)) {
                batch.draw(texture, centerX - 68.8f, centerY - 86f, 137.6f, 172f)
                batch.flush(); ScissorStack.popScissors()
            }
        }
    }

    private fun drawUnit(assets: ScenarioSceneAssets, batch: SpriteBatch, unit: ScenarioBattlefieldUnitView) {
        assets.unitTexture(unit.textureAssetId)?.let { texture ->
            val x = ScenarioBattlefieldRenderGeometry.mapX(unit.visualX, unit.visualY); val y = ScenarioBattlefieldRenderGeometry.mapY(unit.visualX, unit.visualY)
            batch.color = Color.WHITE
            batch.draw(texture, x - 41.28f, y - 55.04f, 82.56f, 110.08f, 0, unit.frameRow * 64, 48, 64, unit.flipX, false)
            if (unit.showSpeechBubble) assets.streetSpeechBubbleTexture?.let { batch.draw(it, x + 20.64f, y + 34.4f, 41.28f, 41.28f) }
        }
    }

    private data class Entry(val zIndex: Float, val siblingOrder: Int, val unit: ScenarioBattlefieldUnitView? = null, val head: ScenarioBattlefieldHeadView? = null)
}
