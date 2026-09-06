package com.jojo.game

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport

/** 업적 화면의 원본 좌표와 닫기 동작을 검증하는 전용 화면이다. */

class AchievementsFixtureScreen(private val game: JojoGame) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    private val viewport = FitViewport(1280f, 688f, OrthographicCamera())
    private val batch = SpriteBatch()
    private val background = Texture(Gdx.files.internal("maps/71.jpg"))

    // 원본 패널과 대응하는 타일 프레임을 사용한다.
    private val panel = Texture(Gdx.files.internal("maps/ui/terrain-layer/panel.png"))
    private val font = KoreanFont.create(34, "업적 없음 돈 확인 도움말 ★ ☆ 1. 여포")
    private var removed = false

    override fun show() {
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                // 원본 button0은 오른쪽 닫기 버튼이다.
                val p = viewport.unproject(com.badlogic.gdx.math.Vector2(screenX.toFloat(), screenY.toFloat()))
                if (p.x > 975f && p.y < 100f) removed = true
                return true
            }
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        batch.draw(background, 0f, 0f, 1280f, 688f)
        // 시각 렌더링은 패널 아트를 사용하고, 정확한 비교는 이벤트 로그로 수행한다.
        batch.draw(panel, 147.5f * .86f, 24.5f * .86f, 1193f * .86f, 751f * .86f)
        font.color = Color.WHITE
        font.draw(batch, "업적", 180f * .86f, 408f * .86f)
        if (!removed) {
            font.draw(batch, "없음", 190f * .86f, 320f * .86f)
            font.draw(batch, "돈: 99999", 480f * .86f, 320f * .86f)
            font.draw(batch, "★  ★  ★", 760f * .86f, 320f * .86f)
            font.draw(batch, "확인", 1030f * .86f, 60f * .86f, 100f * .86f, Align.center, false)
            font.draw(batch, "도움말", 875f * .86f, 60f * .86f, 100f * .86f, Align.center, false)
        }
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    /** 업적 화면의 렌더 이벤트를 비교 가능한 문자열로 반환한다. */

    fun renderEventLog(): String {
        val log = RenderEventLog()
        val phase = "hall-achievements-stable"
        fun draw(
            layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", opacity: Float = 1f, visible: Boolean = true
        ) =
            log.draw(
                phase, layer, path, type, x, y, w, h, asset, opacity,
                blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
                visible = visible, text = text
            )
        draw(
            "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        draw(
            "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            "default_sprite_splash", opacity = .392f
        )
        draw("HallLayer", "Canvas/Layer/Logo_12-1", "tiled-sprite", 147.686f, 24.5f, 1193f, 751f, "Logo_9-1")
        draw("HallLayer", "Canvas/Layer/Logo_12-1/box4", "sliced-sprite", 147.686f, 24.5f, 1193f, 751f, "box4")
        draw("HallLayer", "Canvas/Layer/Logo_12-1/bg1", "sprite", 147.686f, 715.5f, 1193f, 60f, "bg1")
        draw("HallLayer", "Canvas/Layer/Logo_12-1/bg1/box3", "sliced-sprite", 147.686f, 715.5f, 1193f, 60f, "box3")
        draw("HallLayer", "Canvas/Layer/Logo_12-1/bg1/label", "label", 744.186f, 721.3f, 71.2f, 52.4f, text = "업적")
        draw("HallLayer", "Canvas/Layer/Logo_12-1/scrollview", "tiled-sprite", 161.686f, 99f, 1161f, 616f, "Logo_12-1")
        draw("HallLayer", "Canvas/Layer/Logo_12-1/scrollview/box2", "tiled-sprite", 161.686f, 99f, 1161f, 616f, "box2")
        if (!removed) {
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0",
                "sprite",
                161.686f,
                643f,
                1161f,
                70f,
                "bg1"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0/box3",
                "sliced-sprite",
                161.686f,
                643f,
                1161f,
                70f,
                "box3"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0/label0",
                "label",
                180.186f,
                652.091f,
                540f,
                54f,
                text = "R 첫 전투"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0/label1",
                "label",
                848.962f,
                651.891f,
                230.82f,
                54.4f,
                text = "Lv:2 Gold:30"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0/label2",
                "label",
                1080.806f,
                641.534f,
                226.38f,
                79.6f,
                text = "★  ☆  ★"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0",
                "sprite",
                161.686f,
                569f,
                1161f,
                70f,
                "bg1"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0/box3",
                "sliced-sprite",
                161.686f,
                569f,
                1161f,
                70f,
                "box3"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0/label0",
                "label",
                180.186f,
                578.091f,
                540f,
                54f,
                text = "S 영천 전투"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0/label1",
                "label",
                871.212f,
                577.891f,
                208.57f,
                54.4f,
                text = "Lv:9 Gold:0"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/scrollview/view/content/item0/label2",
                "label",
                1080.806f,
                567.534f,
                226.38f,
                79.6f,
                text = "☆  ★  ☆"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/button0/Background",
                "sliced-sprite",
                1172.451f,
                32.187f,
                147.6f,
                56f,
                "box3"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/button0/Background/Label",
                "label",
                1196.251f,
                41.187f,
                100f,
                40f,
                text = "확인"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/button1/Background",
                "sliced-sprite",
                1007.406f,
                32.187f,
                147.6f,
                56f,
                "box3"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/Logo_12-1/button1/Background/Label",
                "label",
                1031.206f,
                41.187f,
                100f,
                40f,
                text = "도움말"
            )
        }
        return log.jsonl()
    }
    override fun runtimeRenderEventLog(): String = renderEventLog()

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    override fun dispose() {
        batch.dispose(); background.dispose(); panel.dispose(); font.dispose()
    }
}
