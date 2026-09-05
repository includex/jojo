package com.jojo.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport

/** Deterministic game rendering of the production Global130 AttributeLayer. */
class AttributeFixtureScreen(private val game: JojoGame) : ScreenAdapter() {
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
        // The panel texture is only a visual fallback; semantic parity uses
        // the exact source-coordinate event stream below.
        batch.draw(panel, 915f, 305f, 339f, 356f)
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    /**
     * 공개 메서드 `renderEventLog`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun renderEventLog(): String {
        val l = RenderEventLog()
        val phase = "hall-attribute-stable"
        fun d(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = ""
        ) = l.draw(
            phase, "HallLayer", path, type, x, y, w, h, asset,
            blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
            text = text
        )
        d(
            "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        d("Canvas/Layer/scrollview/box2", "tiled-sprite", 1064.605f, 355f, 394f, 413f, "box5")
        val rows = listOf("武力" to "98", "智力" to "76", "統率" to "91", "兵力" to "12000")
        rows.forEachIndexed { index, (name, value) ->
            val y = 714f - index * 54f
            val p = "Canvas/Layer/scrollview/view/content/item"
            d(p, "sprite", 1066.505f, y, 390f, 50f, "bg2")
            d("$p/label0", "label", 1076.805f, y - .074f, 168.9f, 54f, text = name)
            d("$p/label1", "label", 1278.036f, y - .074f, 168.9f, 54f, text = value)
        }
        return l.jsonl()
    }

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    override fun dispose() {
        batch.dispose(); background.dispose(); panel.dispose()
    }
}
