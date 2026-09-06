// Battle
package com.jojo.game.presentation.battle.overlay

import com.jojo.game.*

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/** BattleRewardOverlayPhase: 보상 모달이 금전·아이템·종료·완료 중 어디를 표시하는지 구분한다. */
enum class BattleRewardOverlayPhase { MONEY, ITEMS, END, COMPLETE }

/** BattleRewardItemView: 보상 아이템의 이름과 아이콘 텍스처를 화면에 전달한다. */
data class BattleRewardItemView(val name: String, val icon: Texture?)

/** BattleRewardOverlayView: 현재 보상 단계와 표시할 금전·아이템·문구를 묶은 불변 화면 상태이다. */
data class BattleRewardOverlayView(
    val worldWidth: Float,
    val worldHeight: Float,
    val phase: BattleRewardOverlayPhase?,
    val money: Int = 0,
    val stars: String = "",
    val items: List<BattleRewardItemView> = emptyList(),
    val sectionVisible: Boolean = false,
)

/** BattleRewardOverlayAssets: 보상 모달 배경과 아이콘을 그릴 때 사용하는 텍스처 묶음이다. */
data class BattleRewardOverlayAssets(
    val rewardItemTexture: Texture?,
    val winConditionBoxPatch: NinePatch?,
    val sectionBackgroundTexture: Texture?,
)

/** BattleRewardOverlayRenderer: 전투 보상 모달을 그리며, 단계에 따라 금전·아이템·종료 안내를 배치한다. */
class BattleRewardOverlayRenderer(
    /** `batch` (SpriteBatch): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val batch: SpriteBatch,
    /** `shapes` (ShapeRenderer): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val shapes: ShapeRenderer,
    /** `titleFont` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val titleFont: BitmapFont,
    /** `bodyFont` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val bodyFont: BitmapFont,
    /** `sectionTitleFont` (BitmapFont): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val sectionTitleFont: BitmapFont,
    /** `assets` (BattleRewardOverlayAssets): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val assets: BattleRewardOverlayAssets,
) {
    /**
     * `draw`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `drawMoney`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawMoney(view: BattleRewardOverlayView) {
        labelPair("전투 종료", 527.747f, 615.617f, 519.916f, 627.594f)
        labelPair("보상금", 282.777f, 399.692f, 274.533f, 405.6f)
        labelPair(view.money.toString(), 967.617f, 399.007f, 958.035f, 405.6f)
        labelPair(view.stars, 531.389f, 204.017f, 521.806f, 207.313f)
    }

    /**
     * `drawItems`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `drawEnd`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawEnd(view: BattleRewardOverlayView) {
        labelPair("전투 종료", 527.747f, 488.023f, 519.916f, 500f)
        labelPair(view.money.toString(), 658f, 322f, 650f, 330f)
    }

    /**
     * `drawSection`: 화면 표시 상태를 렌더링한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun drawSection() {
        batch.color = Color.WHITE
        assets.sectionBackgroundTexture?.let { batch.draw(it, 0f, 0f, 1488.3721f, 800f) }
        sectionTitleFont.color = Color(0.28f, 0.28f, 0.28f, 1f)
        sectionTitleFont.draw(batch, "영천의 전투", 431.986f, 478.2f)
        sectionTitleFont.color = Color.WHITE
        sectionTitleFont.draw(batch, "영천의 전투", 421.986f, 488.2f)
        batch.color = Color.WHITE
    }

    /**
     * `labelPair`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun labelPair(text: String, shadowX: Float, shadowBaseline: Float, x: Float, baseline: Float) {
        titleFont.color = Color(.3f, .3f, .3f, 1f)
        titleFont.draw(batch, text, shadowX, shadowBaseline)
        titleFont.color = Color.WHITE
        titleFont.draw(batch, text, x, baseline)
    }
}
