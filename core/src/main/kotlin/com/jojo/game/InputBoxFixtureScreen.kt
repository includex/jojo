package com.jojo.game

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
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport

/** Diagnostic route for the shipped Global137 InputBox prefab. */
class InputBoxFixtureScreen(private val game: JojoGame, private val state: String) : ScreenAdapter() {
    private val viewport = ExtendViewport(1280f, 800f, OrthographicCamera())
    private val batch = SpriteBatch()
    private val textures = mutableListOf<Texture>()
    private fun texture(path: String): Texture? = Gdx.files.internal(path).takeIf { it.exists() }
        ?.let(::Texture)?.also { textures += it }

    private val background = texture("maps/71.jpg")
    private val logo9 = texture("maps/ui/unit-info/logo9.png")
    private val bg1 = texture("maps/ui/input-box/bg1.png")
    private val box1 = texture("maps/ui/input-box/box1.png")?.let { NinePatch(it, 3, 3, 3, 3) }
    private val button = texture("maps/ui/input-box/box3.png")?.let { NinePatch(it, 9, 9, 7, 11) }
    private val font: BitmapFont = KoreanFont.create(40, "모드 다운로드웹 주소를 입력하세요확인취소https://example.invalid/mod.zip")
    private val initial = if (state == "input-box-filled") "https://example.invalid/mod.zip" else null
    private var persisted: String? = initial
    private var callbackValue: String? = null
    private val model = InputBoxRenderOracle(initial, { persisted = it }, { callbackValue = it })

    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, buttonCode: Int): Boolean {
                val p = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                if (p.y in 312f..362f && p.x in 916.163f..1116.163f) model.touchButton(0, 2)
                else if (p.y in 312f..362f && p.x in 698.334f..898.334f) model.touchButton(1, 2)
                else model.touchOutside()
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
        background?.let { batch.draw(it, 0f, 0f, viewport.worldWidth, 800f) }
        if (model.attached) drawInputBox()
        batch.end()
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    private fun drawInputBox() {
        logo9?.let { batch.draw(it, 344.186f, 286f, 800f, 228f) }
        bg1?.let { batch.draw(it, 344.186f, 464f, 800f, 50f) }
        font.color = Color.BLACK
        font.draw(batch, "모드 다운로드", 349.186f, 506f, 218.71f, Align.center, false)
        box1?.draw(batch, 344.186f, 286f, 800f, 228f)
        box1?.draw(batch, 373.186f, 386.5f, 742f, 61f)
        val editorText = if (model.value.isEmpty()) "웹 주소를 입력하세요" else model.value
        font.color = if (model.value.isEmpty()) Color(134f / 255f, 102f / 255f, 137f / 255f, 1f) else Color.BLACK
        font.draw(batch, editorText, 383.586f, 395f, 720f, Align.left, false)
        font.color = Color.BLACK
        button?.draw(batch, 698.334f, 312f, 200f, 50f)
        font.draw(batch, "취소", 748.334f, 356f, 100f, Align.center, false)
        button?.draw(batch, 916.163f, 312f, 200f, 50f)
        font.draw(batch, "확인", 966.163f, 356f, 100f, Align.center, false)
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

    fun renderEventLog(): String = InputBoxRenderEvents.jsonl(state, model.value, model.attached)
    internal fun persistedValue(): String? = persisted
    internal fun callbackResult(): String? = callbackValue

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    override fun dispose() {
        batch.dispose()
        font.dispose()
        textures.distinct().forEach(Texture::dispose)
    }
}

/** Capture-only state for a registered prefab that has no recovered runtime caller. */
private class InputBoxRenderOracle(
    savedValue: String?,
    private val persist: (String) -> Unit,
    private val callback: (String?) -> Unit,
) {
    var value = savedValue ?: ""
        private set
    var attached = true
        private set

    /**
     * 공개 메서드 `touchButton`
     *
     * ### 파라미터
    - `button` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touchButton(button: Int, event: Int) {
        if (!attached || event != 2 || button !in 0..1) return
        attached = false
        if (button == 0) {
            persist(value); callback(value)
        } else callback(null)
    }

    /**
     * 공개 메서드 `touchOutside`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun touchOutside() = attached
}

/** Filled with exact source draw records after the actual addLayer oracle runs. */
object InputBoxRenderEvents {
    /**
     * 공개 메서드 `jsonl`
     *
     * ### 파라미터
    - `state` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `attached` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun jsonl(state: String, value: String, attached: Boolean): String {
        if (!attached) return ""
        val log = RenderEventLog()
        val phase = "hall-$state-stable"
        fun draw(
            path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, text: String = "", layer: String = "InputBox"
        ) =
            log.draw(
                phase, layer, path, type, x, y, w, h, asset,
                blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
                text = text
            )
        draw(
            "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>",
            layer = "HallLayer"
        )
        draw("Canvas/Layer/bg0", "tiled-sprite", 344.186f, 286f, 800f, 228f, "Logo_9-1")
        draw("Canvas/Layer/bg0/bg1", "sprite", 344.186f, 464f, 800f, 50f, "bg1")
        draw("Canvas/Layer/bg0/bg1/label", "label", 349.186f, 463.8f, 218.71f, 50.4f, text = "모드 다운로드")
        draw("Canvas/Layer/bg0/box3", "sliced-sprite", 344.186f, 286f, 800f, 228f, "box1")
        draw("Canvas/Layer/bg0/box1", "sliced-sprite", 373.186f, 386.5f, 742f, 61f, "box1")
        draw(
            "Canvas/Layer/bg0/box1/editbox/${if (value.isEmpty()) "PLACEHOLDER_LABEL" else "TEXT_LABEL"}", "label",
            383.686f, 387.14f, 723.2f, 50f, text = value.ifEmpty { "웹 주소를 입력하세요" })
        draw("Canvas/Layer/bg0/button0/Background", "sliced-sprite", 916.163f, 312f, 200f, 50f, "box3")
        draw("Canvas/Layer/bg0/button0/Background/Label", "label", 966.163f, 317.844f, 100f, 40f, text = "확인")
        draw("Canvas/Layer/bg0/button1/Background", "sliced-sprite", 698.334f, 312f, 200f, 50f, "box3")
        draw("Canvas/Layer/bg0/button1/Background/Label", "label", 748.334f, 317.844f, 100f, 40f, text = "취소")
        return log.jsonl()
    }
}
