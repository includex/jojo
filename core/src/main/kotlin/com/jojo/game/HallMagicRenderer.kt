package com.jojo.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align

internal object HallMagicRenderer {
    private const val SCALE = .86f

    fun draw(assets: ScenarioSceneAssets, batch: SpriteBatch, view: HallMagicView) {
        fun texture(name: String): Texture? = assets.hallTexture("maps/ui/start-battle/$name.png")
        fun magicTexture(name: String): Texture? = assets.hallTexture("maps/ui/magic-layer/$name.png")
        fun patch(name: String, inset: Int = 3): NinePatch? =
            (texture(name) ?: if (name == "box3") texture("button") else null)?.let {
                NinePatch(it, inset, inset, inset, inset)
            }
        fun tiled(texture: Texture, x: Float, y: Float, width: Float, height: Float) {
            val tileWidth = texture.width * SCALE
            val tileHeight = texture.height * SCALE
            var dy = 0f
            while (dy < height - .01f) {
                val drawHeight = minOf(tileHeight, height - dy)
                val sourceHeight = (drawHeight / SCALE).toInt().coerceIn(1, texture.height)
                var dx = 0f
                while (dx < width - .01f) {
                    val drawWidth = minOf(tileWidth, width - dx)
                    val sourceWidth = (drawWidth / SCALE).toInt().coerceIn(1, texture.width)
                    batch.draw(texture, x + dx, y + dy, drawWidth, drawHeight, 0, 0, sourceWidth, sourceHeight, false, false)
                    dx += tileWidth
                }
                dy += tileHeight
            }
        }
        fun label(value: String, x: Float, y: Float, width: Float, align: Int = Align.center, wrap: Boolean = false) {
            assets.bodyFont.color = Color.BLACK
            assets.bodyFont.draw(batch, value, x * SCALE, (y + 43f) * SCALE, width * SCALE, align, wrap)
        }

        batch.color = Color.WHITE
        texture("logo9")?.let { tiled(it, 452.686f * SCALE, 130f * SCALE, 583f * SCALE, 540f * SCALE) }
        patch("box3")?.draw(batch, 452.686f * SCALE, 130f * SCALE, 583f * SCALE, 540f * SCALE)
        label(view.name, 577.509f, 604.008f, 103.8f)
        magicTexture("magic-${view.iconFrame}")?.let { batch.draw(it, 478.186f * SCALE, 562f * SCALE, 80f * SCALE, 80f * SCALE) }
        patch("box1")?.draw(batch, 465.636f * SCALE, 434f * SCALE, 340.3f * SCALE, 100f * SCALE)
        label("위력:", 476.336f, 479.826f, 80.31f)
        label("${view.power}%", 566.719f, 480.13f, 80.06f)
        label("MP 소모:", 470.776f, 436.826f, 151.43f)
        label(view.cost.toString(), 627.053f, 436.675f, 22.25f)
        patch("box2")?.draw(batch, 465.636f * SCALE, 147f * SCALE, 340.3f * SCALE, 274f * SCALE)
        label(view.intro, 470.786f, 144.76f, 330f, Align.left, wrap = true)
        patch("box1")?.draw(batch, 814.213f * SCALE, 436.061f * SCALE, 200f * SCALE, 200f * SCALE)
        texture("title")?.let { batch.draw(it, 830.713f * SCALE, 614.117f * SCALE, 167f * SCALE, 40f * SCALE) }
        label("가능 범위", 839.654f, 611.005f, 149.51f)
        magicTexture("hitarea-${view.hitAreaFrame}")?.let { batch.draw(it, 834.213f * SCALE, 450.755f * SCALE, 160f * SCALE, 160f * SCALE) }
        patch("box1")?.draw(batch, 814.213f * SCALE, 204.673f * SCALE, 200f * SCALE, 200f * SCALE)
        texture("title")?.let { batch.draw(it, 831.713f * SCALE, 384.673f * SCALE, 165f * SCALE, 40f * SCALE) }
        label("영향 범위", 839.654f, 381.561f, 149.51f)
        magicTexture("effarea-${view.effectAreaFrame}")?.let { batch.draw(it, 834.213f * SCALE, 219.367f * SCALE, 160f * SCALE, 160f * SCALE) }
        patch("box3", 8)?.draw(batch, 874.764f * SCALE, 144.022f * SCALE, 147.6f * SCALE, 50f * SCALE)
        label("확인", 898.564f, 152.022f, 100f)
        batch.color = Color.WHITE
    }
}
