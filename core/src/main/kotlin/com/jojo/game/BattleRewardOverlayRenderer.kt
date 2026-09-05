package com.jojo.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

enum class BattleRewardOverlayPhase { MONEY, ITEMS, END, COMPLETE }

data class BattleRewardItemView(val name: String, val icon: Texture?)

data class BattleRewardOverlayView(
    val worldWidth: Float,
    val worldHeight: Float,
    val phase: BattleRewardOverlayPhase?,
    val money: Int = 0,
    val stars: String = "",
    val items: List<BattleRewardItemView> = emptyList(),
    val sectionVisible: Boolean = false,
)

data class BattleRewardOverlayAssets(
    val rewardItemTexture: Texture?,
    val winConditionBoxPatch: NinePatch?,
    val sectionBackgroundTexture: Texture?,
)

/** Stateless reward/section renderer; mutable battle and flow state stay in BattleScreen. */
class BattleRewardOverlayRenderer(
    private val batch: SpriteBatch,
    private val shapes: ShapeRenderer,
    private val titleFont: BitmapFont,
    private val bodyFont: BitmapFont,
    private val sectionTitleFont: BitmapFont,
    private val assets: BattleRewardOverlayAssets,
) {
    fun draw(view: BattleRewardOverlayView) {
        view.phase?.let { phase ->
            shapes.projectionMatrix = batch.projectionMatrix
            shapes.begin(ShapeRenderer.ShapeType.Filled)
            shapes.color = Color(0f, 0f, 0f, 50f / 255f)
            shapes.rect(0f, 0f, view.worldWidth, view.worldHeight)
            shapes.end()
            batch.begin()
            when (phase) {
                BattleRewardOverlayPhase.MONEY -> drawMoney(view)
                BattleRewardOverlayPhase.ITEMS -> drawItems(view)
                BattleRewardOverlayPhase.END -> drawEnd(view)
                BattleRewardOverlayPhase.COMPLETE -> Unit
            }
            batch.end()
        }
        if (view.sectionVisible) {
            batch.begin()
            drawSection()
            batch.end()
        }
    }

    private fun drawMoney(view: BattleRewardOverlayView) {
        labelPair("전투 종료", 527.747f, 615.617f, 519.916f, 627.594f)
        labelPair("보상금", 282.777f, 399.692f, 274.533f, 405.6f)
        labelPair(view.money.toString(), 967.617f, 399.007f, 958.035f, 405.6f)
        labelPair(view.stars, 531.389f, 204.017f, 521.806f, 207.313f)
    }

    private fun drawItems(view: BattleRewardOverlayView) {
        labelPair("전리품", 596.73f, 726.144f, 588.486f, 739.142f)
        view.items.forEachIndexed { index, item ->
            val y = 433.5f - index * 157f
            assets.rewardItemTexture?.let { batch.draw(it, 499.686f, y, 489f, 101f) }
            assets.winConditionBoxPatch?.draw(batch, 499.686f, y, 489f, 101f)
            item.icon?.let { batch.draw(it, 534.974f, y + 18.5f, 64f, 64f) }
            bodyFont.color = Color.WHITE
            bodyFont.draw(batch, item.name, 648.999f, y + 78.22f)
        }
        bodyFont.color = Color.WHITE
    }

    private fun drawEnd(view: BattleRewardOverlayView) {
        labelPair("전투 종료", 527.747f, 488.023f, 519.916f, 500f)
        labelPair(view.money.toString(), 658f, 322f, 650f, 330f)
    }

    private fun drawSection() {
        batch.color = Color.WHITE
        assets.sectionBackgroundTexture?.let { batch.draw(it, 0f, 0f, 1488.3721f, 800f) }
        sectionTitleFont.color = Color(0.28f, 0.28f, 0.28f, 1f)
        sectionTitleFont.draw(batch, "영천의 전투", 431.986f, 478.2f)
        sectionTitleFont.color = Color.WHITE
        sectionTitleFont.draw(batch, "영천의 전투", 421.986f, 488.2f)
        batch.color = Color.WHITE
    }

    private fun labelPair(text: String, shadowX: Float, shadowBaseline: Float, x: Float, baseline: Float) {
        titleFont.color = Color(.3f, .3f, .3f, 1f)
        titleFont.draw(batch, text, shadowX, shadowBaseline)
        titleFont.color = Color.WHITE
        titleFont.draw(batch, text, x, baseline)
    }
}
