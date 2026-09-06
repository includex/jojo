// Verification
package com.jojo.game

import com.jojo.game.presentation.shared.KoreanFont

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider

import com.jojo.game.presentation.scenario.overlay.*
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport


/** DialogueFixtureScreen: 대화 패널의 진행·건너뛰기·자동 닫기 상태를 재현하는 검증 화면이다. */
class DialogueFixtureScreen(private val game: JojoGame, private val state: String) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    /** viewport: 검증 화면의 좌표계와 카메라 상태를 담는다. */
    private val viewport = ExtendViewport(1280f, 800f, OrthographicCamera())
    /** batch: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val batch = SpriteBatch()
    /** textures: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val textures = mutableListOf<Texture>()
    /** texture: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun texture(path: String): Texture? = Gdx.files.internal(path).takeIf { it.exists() }
        ?.let(::Texture)?.also { textures += it }

    /** background: 화면 배경 리소스를 담는다. */
    private val background = texture("maps/71.jpg")
    /** leftPanel: left panel 값을 보관해 검증 흐름에서 사용한다. */
    private val leftPanel = texture("maps/ui/choice-panel.png")
    /** rightPanel: right panel 값을 보관해 검증 흐름에서 사용한다. */
    private val rightPanel = texture("maps/ui/dialogue-panel.png")
    /** face1: 검증 흐름에서 사용하는 값을 담는다. */
    private val face1 = texture("maps/heads/1.png")
    /** face214: 검증 흐름에서 사용하는 값을 담는다. */
    private val face214 = texture("maps/heads/214.png")
    /** textFont: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val textFont: BitmapFont = KoreanFont.create(36, "왼쪽 첫 문장둘째 줄오른쪽 대사")
    /** speakerFont: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val speakerFont: BitmapFont = KoreanFont.create(
        36,
        "조조허자장",
        borderWidth = 2f,
        borderColor = Color(1f, 1f, 154f / 255f, 1f),
        fillColor = Color(233f / 255f, 253f / 255f, 255f / 255f, 1f),
    )
    /** dialogue: 검증 흐름에서 사용하는 값을 담는다. */
    private val dialogue = createModel(state)

    /** createModel: 검증 실행에 필요한 객체와 상태를 구성한다. */
    private fun createModel(state: String): DialogueLayer {
        DialogueLayer.resetAlternation()
        val names = mapOf(0 to "조조", 157 to "허자장")
        val layer = DialogueLayer(
            text = when (state) {
                "dialogue-right" -> "&0\n왼쪽 대사\n&157\n오른쪽 대사"
                "dialogue-auto-close" -> "&0\n자동 종료 대사"
                else -> "&0\n왼쪽 첫 문장\n둘째 줄"
            },
            unitName = { names[it] ?: "" },
            unitY = { 0f },
            flag = if (state == "dialogue-auto-close") DialogueLayer.AUTO_CLOSE else DialogueLayer.QI_PAO,
        )
        when (state) {
            "dialogue-right" -> repeat(3) { layer.touch(DialogueLayer.TOUCH_END) }
            "dialogue-skip" -> layer.skip()
            "dialogue-auto-close" -> {
                layer.touch(DialogueLayer.TOUCH_END)
                layer.advance(1.6f)
            }

            else -> layer.touch(DialogueLayer.TOUCH_END)
        }
        return layer
    }

    /** show: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        Gdx.input.inputProcessor = object : InputAdapter() {
            /** touchUp: 검증 입력을 현재 화면 상태에 반영한다. */
            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                return dialogue.touch(DialogueLayer.TOUCH_END)
            }
        }
    }

    /** render: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE
        background?.let { batch.draw(it, 0f, 0f, viewport.worldWidth, 800f) }
        if (dialogue.attached) drawDialogue(dialogue.view())
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    /** drawDialogue: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    private fun drawDialogue(view: DialogueLayer.View) {
        if (view.bubble == 0) {
            face1?.let { batch.draw(it, 98.628f, 62f, 192f, 240f) }
            leftPanel?.let { batch.draw(it, 319.233f, 64.5f, 798f, 191f) }
            textFont.color = Color.WHITE
            textFont.draw(batch, "왼쪽 첫 문장", 382.487f, 206.734f)
            textFont.draw(batch, "둘째 줄", 382.487f, 164.734f)
            speakerFont.draw(batch, "조조", 403.896f, 250f)
        } else {
            face214?.let { batch.draw(it, 1197.993f, 62f, 192f, 240f) }
            rightPanel?.let { batch.draw(it, 367.917f, 64.5f, 798f, 191f) }
            textFont.color = Color.WHITE
            textFont.draw(batch, "오른쪽 대사", 396.655f, 206.734f)
            speakerFont.draw(batch, "허자장", 422.459f, 250f)
        }
    }


    /** renderEventLog: 대화 화면의 렌더 이벤트를 비교용 문자열로 반환한다. */
    fun renderEventLog(): String {
        val log = RenderEventLog()
        val phase = "hall-$state-stable"
        /** draw: 검증 대상의 현재 렌더 이벤트를 출력한다. */
        fun draw(
            layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, visible: Boolean = true, opacity: Float = 1f, text: String = ""
        ) =
            log.draw(
                phase, layer, path, type, x, y, w, h, asset,
                opacity = opacity,
                blend = if (type == "label" || type == "rich-text")
                    listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
                visible = visible,
                text = text
            )
        draw(
            "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        if (!dialogue.attached) return log.jsonl()
        draw(
            "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
            visible = false, opacity = 0f
        )
        if (dialogue.view().bubble == 0) {
            draw("DialogueLayer", "Canvas/Layer/bg0/face", "sprite", 98.628f, 62f, 192f, 240f, "1")
            draw("DialogueLayer", "Canvas/Layer/bg0/bg2", "sprite", 319.233f, 64.5f, 798f, 191f, "U_select_10-1")
            draw(
                "DialogueLayer", "Canvas/Layer/bg0/bg2/richtext", "rich-text", 382.487f, 111.814f, 728f, 94.92f,
                text = "왼쪽 첫 문장<br/>둘째 줄"
            )
            draw(
                "DialogueLayer",
                "Canvas/Layer/bg0/bg2/richtext/RICHTEXT_CHILD",
                "label",
                382.487f,
                153.814f,
                175.7f,
                52.92f,
                text = "왼쪽 첫 문장"
            )
            draw(
                "DialogueLayer",
                "Canvas/Layer/bg0/bg2/richtext/RICHTEXT_CHILD",
                "label",
                382.487f,
                111.814f,
                103.42f,
                52.92f,
                text = "둘째 줄"
            )
            draw("DialogueLayer", "Canvas/Layer/bg0/label", "label", 403.896f, 199.52f, 66.28f, 54.4f, text = "조조")
        } else {
            draw("DialogueLayer", "Canvas/Layer/bg1/face", "sprite", 1197.993f, 62f, 192f, 240f, "214")
            draw("DialogueLayer", "Canvas/Layer/bg1/bg2", "sprite", 367.917f, 64.5f, 798f, 191f, "U_select_11-1")
            draw(
                "DialogueLayer", "Canvas/Layer/bg1/bg2/richtext", "rich-text", 396.655f, 153.814f, 728f, 52.92f,
                text = "오른쪽 대사"
            )
            draw(
                "DialogueLayer",
                "Canvas/Layer/bg1/bg2/richtext/RICHTEXT_CHILD",
                "label",
                396.655f,
                153.814f,
                165.7f,
                52.92f,
                text = "오른쪽 대사"
            )
            draw("DialogueLayer", "Canvas/Layer/bg1/label", "label", 422.459f, 199.228f, 97.42f, 54.4f, text = "허자장")
        }
        return log.jsonl()
    }
    /** runtimeRenderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun runtimeRenderEventLog(): String = renderEventLog()

    /** resize: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)

    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        batch.dispose()
        textFont.dispose()
        speakerFont.dispose()
        textures.distinct().forEach(Texture::dispose)
    }
}
