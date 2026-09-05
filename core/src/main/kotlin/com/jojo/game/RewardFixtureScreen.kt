package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport

/** Deterministic RewardLayer frames reached by scenario reward callbacks. */
class RewardFixtureScreen(private val game: JojoGame, private val state: String) : ScreenAdapter() {
    private val scale = .86f
    private val viewport = FitViewport(1280f, 688f, OrthographicCamera())
    private val batch = SpriteBatch()
    private val textures = mutableListOf<Texture>()
    private fun texture(path: String): Texture? = Gdx.files.internal(path).takeIf { it.exists() }?.let(::Texture)?.also { textures += it }
    private val background = texture("maps/71.jpg")
    private val box = texture("maps/ui/start-battle/button.png")?.let { NinePatch(it, 9, 9, 7, 11) }
    private val icon88 = texture("maps/item-icons/88.png")
    private val icon89 = texture("maps/item-icons/89.png")
    private val dim = Pixmap(1, 1, Pixmap.Format.RGBA8888).let { pixmap ->
        pixmap.setColor(Color.BLACK); pixmap.fill()
        Texture(pixmap).also { textures += it }.also { pixmap.dispose() }
    }
    private val titleFont = KoreanFont.create(100, "전투 종료보상금전리품★☆ 900")
    private val itemFont = KoreanFont.create(38, "회복용 콩밀")

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        background?.let { batch.draw(it, 0f, 0f, 1280f, 688f) }
        batch.color = Color(1f, 1f, 1f, 50f / 255f)
        batch.draw(dim, 0f, 0f, 1280f, 688f)
        batch.color = Color.WHITE
        if (state == "reward-basic") drawBasic() else drawCards(if (state == "reward-card-2") 2 else 1)
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    private fun drawBasic() {
        fun shadowed(value: String, x: Float, y: Float) {
            titleFont.color = Color(0.3f, 0.3f, 0.3f, 1f); titleFont.draw(batch, value, (x + 8f) * scale, (y - 8f) * scale)
            titleFont.color = Color.WHITE; titleFont.draw(batch, value, x * scale, y * scale)
        }
        shadowed("전투 종료", 519.916f, 627.594f)
        shadowed("보상금", 274.533f, 405.6f)
        shadowed("900", 958.035f, 405.6f)
        shadowed("★  ★  ★", 521.806f, 207.313f)
    }

    private fun drawCards(count: Int) {
        titleFont.color = Color(0.3f, 0.3f, 0.3f, 1f); titleFont.draw(batch, "전리품", 596.73f * scale, 726.144f * scale)
        titleFont.color = Color.WHITE; titleFont.draw(batch, "전리품", 588.486f * scale, 739.142f * scale)
        repeat(count) { index ->
            val y = 433.5f - index * 157f
            box?.draw(batch, 499.686f * scale, y * scale, 489f * scale, 101f * scale)
            (if (index == 0) icon88 else icon89)?.let { batch.draw(it, 534.974f * scale, (y + 18.5f) * scale, 64f * scale, 64f * scale) }
            itemFont.color = Color.WHITE
            itemFont.draw(batch, if (index == 0) "회복용 콩" else "회복용 밀", 648.999f * scale, (y + 73f) * scale)
        }
    }

    fun renderEventLog(): String {
        val log = RenderEventLog()
        val phase = "hall-$state-stable"
        val sprites = listOf(770, 771)
        val labels = listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA")
        fun draw(layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
                 asset: String? = null, opacity: Float = 1f, text: String = "") =
            log.draw(phase, layer, path, type, x * scale, y * scale, w * scale, h * scale, asset,
                opacity = opacity, blend = if (type == "label") labels else sprites, text = text)
        draw("HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>")
        draw("HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", .196f)
        if (state == "reward-basic") {
            listOf(
                floatArrayOf(527.747f, 464.417f, 448.54f, 151.2f) to "전투 종료",
                floatArrayOf(519.916f, 476.394f, 448.54f, 151.2f) to "전투 종료",
                floatArrayOf(282.777f, 248.492f, 311.4f, 151.2f) to "보상금",
                floatArrayOf(274.533f, 254.4f, 311.4f, 151.2f) to "보상금",
                floatArrayOf(967.617f, 247.807f, 200.21f, 151.2f) to "900",
                floatArrayOf(958.035f, 254.4f, 200.21f, 151.2f) to "900",
                floatArrayOf(531.389f, 52.817f, 444.76f, 151.2f) to "★  ★  ★",
                floatArrayOf(521.806f, 56.113f, 444.76f, 151.2f) to "★  ★  ★",
            ).forEach { (g, value) -> draw("RewardLayer", "Canvas/Layer/bg0/${if (value == "900") "label0${if (g[0] > 960f) 2 else 1}" else if (value.startsWith("★")) "label1${if (g[0] > 530f) 2 else 1}" else "label"}", "label", g[0], g[1], g[2], g[3], text = value) }
        } else {
            draw("RewardLayer", "Canvas/Layer/bg1/label", "label", 596.73f, 574.944f, 311.4f, 151.2f, text = "전리품")
            draw("RewardLayer", "Canvas/Layer/bg1/label", "label", 588.486f, 587.942f, 311.4f, 151.2f, text = "전리품")
            val count = if (state == "reward-card-2") 2 else 1
            repeat(count) { index ->
                val path = "Canvas/Layer/bg1/item$index"; val y = 433.5f - index * 157f
                draw("RewardLayer", path, "tiled-sprite", 499.686f, y, 489f, 101f, "Mark_47-1")
                draw("RewardLayer", "$path/box3", "sliced-sprite", 499.686f, y, 489f, 101f, "box3")
                draw("RewardLayer", "$path/icon", "sprite", 534.974f, y + 18.5f, 64f, 64f, "${88 + index}-1")
                draw("RewardLayer", "$path/label", "label", 648.999f, y + 22.78f, 164.46f, 55.44f, text = if (index == 0) "회복용 콩" else "회복용 밀")
            }
        }
        return log.jsonl()
    }

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    override fun dispose() { batch.dispose(); titleFont.dispose(); itemFont.dispose(); textures.distinct().forEach(Texture::dispose) }
}
