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
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport

/** Choose2FixtureScreen: StageLayer.choice2 경로로 진입하는 Global131 화면을 검증한다. */
class Choose2FixtureScreen(private val game: JojoGame, private val state: String) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    /** Geometry: geometry 관련 검증 상태와 동작을 제공하는 타입이다. */
    private data class Geometry(val x: Float, val y: Float, val text: String, val labelX: Float, val labelY: Float)

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
    /** bg2: 검증 흐름에서 사용하는 값을 담는다. */
    private val bg2 = texture("maps/ui/choose2/bg2.png")
    /** box5Texture: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val box5Texture = texture("maps/ui/choose2/box5.png")
    /** bg6: 검증 흐름에서 사용하는 값을 담는다. */
    private val bg6 = texture("maps/ui/choose2/bg6.png")
    /** box6Texture: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val box6Texture = texture("maps/ui/choose2/box6.png")
    /** box5: 검증 흐름에서 사용하는 값을 담는다. */
    private val box5 = box5Texture?.let { NinePatch(it, 5, 5, 5, 5) }
    /** box6: 검증 흐름에서 사용하는 값을 담는다. */
    private val box6 = box6Texture?.let { NinePatch(it, 5, 5, 5, 5) }
    /** font: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val font: BitmapFont = KoreanFont.create(
        46,
        "진격대기퇴각",
        borderWidth = 2f,
        borderColor = Color(1f, 1f, 230f / 255f, 1f),
        fillColor = Color(235f / 255f, 236f / 255f, 203f / 255f, 1f),
    )
    /** model: 검증 흐름에서 사용하는 값을 담는다. */
    private val model = ChoiceLayer(plainNewline = true)
    /** selected: 검증 흐름에서 사용하는 값을 담는다. */
    private var selected: Int? = null
    /** rows: 검증 흐름에서 사용하는 값을 담는다. */
    private val rows = listOf(
        Geometry(280.171f, 237.5f, "진격", 290.659f, 239f),
        Geometry(768.171f, 237.5f, "대기", 778.659f, 239f),
        Geometry(280.171f, 159.5f, "퇴각", 290.659f, 161f),
    )

    init {
        model.onCreate("진격\\n대기\n퇴각", -1) { selected = it }
        if (state == "choose2-select") model.onRowTouch(2, TOUCH_END)
    }

    /** show: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        Gdx.input.inputProcessor = object : InputAdapter() {
            /** touchUp: 검증 입력을 현재 화면 상태에 반영한다. */
            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val p = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                val index = rows.indexOfFirst { p.x in it.x..(it.x + 480f) && p.y in it.y..(it.y + 70f) }
                if (index < 0) return false
                model.onRowTouch(index + 1, TOUCH_END)
                return true
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
        if (model.attached()) {
            bg2?.let { batch.draw(it, 255.453f, 64.5f, 1017f, 249f) }
            box5?.draw(batch, 255.453f, 64.5f, 1017f, 249f)
            rows.forEach { row ->
                bg6?.let { batch.draw(it, row.x, row.y, 480f, 70f) }
                box6?.draw(batch, row.x - .218f, row.y, 480f, 70f)
                font.draw(batch, row.text, row.labelX, row.labelY + 56f)
            }
        }
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }


    /** renderEventLog: 선택 화면의 렌더 이벤트를 비교용 문자열로 반환한다. */
    fun renderEventLog(): String {
        val log = RenderEventLog()
        val phase = "hall-$state-stable"
        /** draw: 검증 대상의 현재 렌더 이벤트를 출력한다. */
        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = ""
        ) =
            log.draw(
                phase, "HallLayer", path, type, x, y, w, h, asset,
                blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
                text = text
            )
        draw(
            "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        if (!model.attached()) return log.jsonl()
        draw("Canvas/Layer/scrollview", "sprite", 255.453f, 64.5f, 1017f, 249f, "bg2")
        draw("Canvas/Layer/scrollview/box5", "sliced-sprite", 255.453f, 64.5f, 1017f, 249f, "box5")
        rows.forEach { row ->
            val base = "Canvas/Layer/scrollview/view/content/item/bg6"
            draw(base, "sprite", row.x, row.y, 480f, 70f, "bg6")
            draw("$base/box6", "sliced-sprite", row.x - .218f, row.y, 480f, 70f, "box6")
            draw("$base/Label", "label", row.labelX, row.labelY, 83.58f, 67f, text = row.text)
        }
        return log.jsonl()
    }
    /** runtimeRenderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun runtimeRenderEventLog(): String = renderEventLog()

    /** selectedRow: selected row에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    internal fun selectedRow(): Int? = selected
    /** resize: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        batch.dispose()
        font.dispose()
        textures.distinct().forEach(Texture::dispose)
    }

    private companion object {
        const val TOUCH_END = 2
    }
}
