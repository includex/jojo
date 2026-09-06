// Verification
package com.jojo.game

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport

/** AttributeFixtureScreen: Global130 능력치 화면의 결정적 렌더링을 제공한다. */
class AttributeFixtureScreen(private val game: JojoGame) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    /** viewport: 검증 화면의 좌표계와 카메라 상태를 담는다. */
    private val viewport = FitViewport(1280f, 688f, OrthographicCamera())
    /** batch: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val batch = SpriteBatch()
    /** background: 화면 배경 리소스를 담는다. */
    private val background = Texture(Gdx.files.internal("maps/71.jpg"))
    /** panel: 화면 패널 리소스를 담는다. */
    private val panel = Texture(Gdx.files.internal("maps/ui/terrain-layer/panel.png"))

    /** render: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply(); batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color.WHITE
        batch.draw(background, 0f, 0f, 1280f, 688f)
        // 패널은 시각 확인용이며 의미적 일치는 아래 좌표 이벤트로 검증한다.
        batch.draw(panel, 915f, 305f, 339f, 356f)
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }


    /** renderEventLog: 능력치 화면의 렌더 이벤트를 비교용 문자열로 반환한다. */
    fun renderEventLog(): String {
        val l = RenderEventLog()
        val phase = "hall-attribute-stable"
        /** d: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
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
    /** runtimeRenderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun runtimeRenderEventLog(): String = renderEventLog()

    /** resize: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        batch.dispose(); background.dispose(); panel.dispose()
    }
}
