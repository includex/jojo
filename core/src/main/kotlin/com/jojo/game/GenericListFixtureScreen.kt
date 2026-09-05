package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport

/**
 * Isolated ListLayer fixture.  The source ListLayer is a generic table
 * overlay, so this screen deliberately keeps the source canvas coordinates
 * in the event stream (1488.372 x 800) while using the normal LibGDX
 * viewport for the visual smoke run.
 */
class GenericListFixtureScreen(private val game: JojoGame) : ScreenAdapter() {
    private val viewport = FitViewport(1280f, 688f, OrthographicCamera())
    private val batch = SpriteBatch()
    private val background = Texture(Gdx.files.internal("maps/71.jpg"))
    private val panel = Texture(Gdx.files.internal("maps/ui/terrain-layer/panel.png"))

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply(); batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color.WHITE
        batch.draw(background, 0f, 0f, 1280f, 688f)
        batch.draw(panel, 140f, 24f, 1000f, 560f)
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    fun renderEventLog(): String {
        val l = RenderEventLog(); val p = "hall-generic-list-stable"
        fun d(path: String, type: String, x: Float, y: Float, w: Float, h: Float,
              asset: String? = null, text: String = "", opacity: Float = 1f) = l.draw(
            p, "HallLayer", path, type, x, y, w, h, asset, opacity,
            if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
            text = text)
        d("Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>")
        d("Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f, "default_sprite_splash", opacity = .392f)
        d("Canvas/Layer/Logo_12-1", "tiled-sprite", 163.186f, 76f, 1162f, 648f, "Logo_9-1")
        d("Canvas/Layer/Logo_12-1/box4", "sliced-sprite", 163.186f, 76f, 1162f, 648f, "box4")
        d("Canvas/Layer/Logo_12-1/bg1", "sprite", 163.186f, 664f, 1162f, 60f, "bg1")
        d("Canvas/Layer/Logo_12-1/bg1/box3", "sliced-sprite", 163.186f, 664f, 1162f, 60f, "box3")
        d("Canvas/Layer/Logo_12-1/bg1/label", "label", 703.186f, 669.8f, 71.2f, 52.4f, text = "목록")
        d("Canvas/Layer/Logo_12-1/scrollview", "tiled-sprite", 167.331f, 150.985f, 1152.07f, 443.01f, "Logo_12-1")
        d("Canvas/Layer/Logo_12-1/scrollview/box2", "tiled-sprite", 167.331f, 150.985f, 1152.07f, 443.01f, "box2")
        val names = listOf("첫 번째 항목", "두 번째 항목", "세 번째 항목")
        val values = listOf("10", "20", "30")
        val rowAssets = listOf("bg2", "885a69b4-08ed-4c78-8896-ffb04eb2bd20", "bg2")
        names.forEachIndexed { i, name ->
            val y = 519.995f - i * 74f
            val q = "Canvas/Layer/Logo_12-1/scrollview/view/content/item"
            d(q, "sprite", 172.266f, y, 1142f, 70f, rowAssets[i])
            d("$q/toggle/Background", "sprite", 185.641f, y + 21f, 28f, 28f, "default_toggle_normal")
            d("$q/label0", "label", 229.266f, y + 7.8f, 199.23f, 54.4f, text = name)
            d("$q/label1", "label", 514.021f, y + 7.8f, 48.49f, 54.4f, text = values[i])
            d("$q/label2", "label", 568.406f, y + 7.8f, 179.72f, 54.4f, text = "undefined")
            d("$q/label3", "label", 688.406f, y + 7.8f, 179.72f, 54.4f, text = "undefined")
        }
        listOf(473.366f, 593.366f, 714.366f, 834.366f, 955.366f, 1075.366f, 1195.366f).forEachIndexed { i, x ->
            d("Canvas/Layer/Logo_12-1/scrollview/vline", "sprite", x, if (i == 6) 155.94f else 152.94f, 6f, 439.1f, "vline")
        }
        d("Canvas/Layer/Logo_12-1/button1/Background", "sliced-sprite", 167.786f, 594f, 310.4f, 70f, "bg1")
        d("Canvas/Layer/Logo_12-1/button1/Background/box3", "sliced-sprite", 167.786f, 594f, 310.4f, 70f, "box3")
        d("Canvas/Layer/Logo_12-1/button1/Background/Label", "label", 272.986f, 609f, 100f, 40f, text = "이름")
        d("Canvas/Layer/Logo_12-1/button2/Background", "sliced-sprite", 478.186f, 594f, 120f, 70f, "bg1")
        d("Canvas/Layer/Logo_12-1/button2/Background/box3", "sliced-sprite", 478.186f, 594f, 120f, 70f, "box3")
        d("Canvas/Layer/Logo_12-1/button2/Background/Label", "label", 488.186f, 609f, 100f, 40f, text = "수치")
        d("Canvas/Layer/Logo_12-1/button0/Background", "sliced-sprite", 1166.834f, 88.191f, 147.6f, 56f, "box3")
        d("Canvas/Layer/Logo_12-1/button0/Background/Label", "label", 1190.634f, 97.191f, 100f, 40f, text = "확인")
        d("Canvas/Layer/Logo_12-1/button10/Background", "sliced-sprite", 1014.733f, 88.191f, 147.6f, 56f, "box3")
        d("Canvas/Layer/Logo_12-1/button10/Background/Label", "label", 1038.533f, 97.191f, 100f, 40f, text = "취소")
        return l.jsonl()
    }

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    override fun dispose() { batch.dispose(); background.dispose(); panel.dispose() }
}
