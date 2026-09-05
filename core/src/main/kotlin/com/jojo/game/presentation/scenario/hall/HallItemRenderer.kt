package com.jojo.game.presentation.scenario.hall

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.jojo.game.presentation.scenario.assets.ScenarioSceneAssets

/** Immutable data and resolved assets for ItemLayer's rendered overlay. */
internal data class HallItemView(
    val itemName: String,
    val category: Int,
    val level: String,
    val experience: Int,
    val experienceLimit: Int,
    val typeName: String,
    val price: String,
    val effect: String,
    val intro: String,
    val postNames: List<String>,
    val canDrop: Boolean,
    val discardConfirmationOpen: Boolean,
    val logoTexture: Texture?,
    val buttonTexture: Texture?,
    val box1Texture: Texture?,
    val box2Texture: Texture?,
    val titleTexture: Texture?,
    val itemIconTexture: Texture?,
)

/** Stateless draw sequence for ItemLayer. */
internal object HallItemRenderer {
    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallItemView) {
        fun patch(texture: Texture?, inset: Int = 3): NinePatch? =
            texture?.let { NinePatch(it, inset, inset, inset, inset) }

        fun rect(texture: Texture?, x: Float, y: Float, w: Float, h: Float) =
            patch(texture)?.draw(batch, x * SCALE, y * SCALE, w * SCALE, h * SCALE)

        fun label(value: String, x: Float, y: Float, w: Float, center: Boolean = false) {
            assets.bodyFont.color = Color.BLACK
            assets.bodyFont.draw(
                batch,
                value,
                x * SCALE,
                y * SCALE + 35f,
                w * SCALE,
                if (center) Align.center else Align.left,
                false
            )
        }

        batch.color = Color.WHITE
        view.logoTexture?.let { batch.draw(it, 253.186f * SCALE, 80f * SCALE, 982f * SCALE, 640f * SCALE) }
        rect(view.buttonTexture, 253.186f, 80f, 982f, 640f)
        label(view.itemName, 420.186f, 658.8f, 203.1f)
        if (view.category <= 1) {
            label("Lv", 673.411f, 658.483f, 42.25f)
            label(view.level, 723.411f, 658.483f, 60f)
            label("Exp", 420.186f, 604.8f, 68.93f)
            rect(view.box2Texture, 500.965f, 614.855f, 204f, 24f)
            val progress = if (view.experienceLimit == 0) 0f else view.experience.toFloat() / view.experienceLimit
            rect(view.box2Texture, 502.965f, 616.855f, 200f * progress, 20f)
            label(
                if (view.experience >= view.experienceLimit) "MAX" else "${view.experience}/${view.experienceLimit}",
                552.915f,
                606.655f,
                100.1f,
                true
            )
        }
        rect(view.box2Texture, 265.778f, 564.802f, 144f, 144f)
        view.itemIconTexture?.let { batch.draw(it, 273.778f * SCALE, 572.802f * SCALE, 128f * SCALE, 128f * SCALE) }
        rect(view.box1Texture, 420.536f, 498.55f, 343.5f, 100.9f)
        label("속성:", 432.137f, 548.543f, 80.31f)
        label(view.typeName, 522.525f, 548.543f, 180f)
        label("가격:", 432.137f, 503.543f, 80.31f)
        label(view.price, 522.525f, 503.543f, 180f)
        rect(view.box1Texture, 261.686f, 92.5f, 501f, 377f)
        view.titleTexture?.let { batch.draw(it, 470.286f * SCALE, 447.7f * SCALE, 83.8f * SCALE, 40f * SCALE) }
        label("효과", 477.586f, 442.5f, 69.2f, true)
        label(view.effect, 265.686f, 345.966f, 493f, true)
        rect(view.box2Texture, 770.186f, 157.5f, 448f, 247f)
        view.titleTexture?.let { batch.draw(it, 943.336f * SCALE, 369.55f * SCALE, 89.7f * SCALE, 40.9f * SCALE) }
        label("설명", 953.586f, 378.8f, 69.2f, true)
        assets.bodyFont.color = Color.BLACK
        assets.bodyFont.draw(batch, view.intro, 774.186f * SCALE, 366f * SCALE, 440f * SCALE, Align.left, true)
        rect(view.box1Texture, 770.186f, 427f, 448f, 260f)
        view.titleTexture?.let { batch.draw(it, 871.686f * SCALE, 664.273f * SCALE, 245f * SCALE, 45f * SCALE) }
        label("장착 가능한 부대입니다.", 804.516f, 661.573f, 379.34f, true)
        view.postNames.take(36).chunked(3).forEachIndexed { row, names ->
            val sy = 609.55f - row * 52f
            rect(if (row % 2 == 0) view.box2Texture else view.box1Texture, 772.186f, sy, 444f, 50f)
            names.forEachIndexed { col, name -> label(name, 780.186f + col * 143f, sy + 4.84f, 134f, true) }
        }
        rect(view.buttonTexture, 1065.827f, 97.824f, 150f, 50f)
        label("확인", 1090.827f, 104.824f, 100f, true)
        if (view.canDrop) {
            rect(view.buttonTexture, 901.312f, 97.824f, 150f, 50f)
            label("버리기", 926.312f, 104.824f, 100f, true)
        }
        if (view.discardConfirmationOpen) {
            view.logoTexture?.let { batch.draw(it, 426.686f * SCALE, 252f * SCALE, 635f * SCALE, 296f * SCALE) }
            rect(view.buttonTexture, 426.686f, 252f, 635f, 296f)
            label("버릴 것을 결정하시겠습니까?${view.itemName}?", 573.686f, 335f, 463f)
            rect(view.buttonTexture, 554.186f, 271.285f, 180f, 50f)
            label("비", 557.336f, 279.085f, 168.1f, true)
            rect(view.buttonTexture, 754.186f, 271.285f, 180f, 50f)
            label("예", 757.586f, 279.085f, 169.4f, true)
        }
        batch.color = Color.WHITE
    }

    private const val SCALE = .86f
}
