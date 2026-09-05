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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport

/** Reusable global system-overlay renderer. */
class SystemOverlayRenderer {
    /**
     * data class  `MsgBox`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class MsgBox(val text: String, val flag: Int)

    /**
     * data class  `Toast`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Toast(val text: String)
}

/**
 * class  `SystemOverlayFixtureScreen`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SystemOverlayFixtureScreen(private val game: JojoGame, private val state: String) : ScreenAdapter() {
    private val viewport = ExtendViewport(1280f, 800f, OrthographicCamera())
    private val batch = SpriteBatch()
    private val shapes = ShapeRenderer()
    private val background = Texture(Gdx.files.internal("maps/71.jpg"))
    private val logo9 = Texture(Gdx.files.internal("maps/ui/start-battle/logo9.png"))
    private val logo3 = Texture(Gdx.files.internal("maps/ui/win-condition/logo3.png"))
    private val loadingTexture = Texture(Gdx.files.internal("maps/ui/system-overlay/uiloading.png"))
    private val boxTexture = Texture(Gdx.files.internal("maps/ui/start-battle/button.png"))
    private val box = NinePatch(boxTexture, 9, 9, 7, 11)
    private val font: BitmapFont =
        KoreanFont.create(34, "저장 완료.게임 저장하시겠습니까?예비원본 공통 알림 UI 비교자원 로딩 중이 완료되면 접속하는 것이 빠를 거예요!")
    private val msg = if (state == "msgbox-ok") SystemOverlayRenderer.MsgBox("저장 완료.", 1)
    else if (state == "msgbox-confirm") SystemOverlayRenderer.MsgBox("게임 저장하시겠습니까?", 3) else null
    private val toast = if (state == "toast-stable") SystemOverlayRenderer.Toast("원본 공통 알림 UI 비교") else null
    private val progress = state.removePrefix("progress-").takeIf { state.startsWith("progress-") }?.let {
        ProgressRenderOracle().also { layer -> layer.onProgress(it.toInt() / 100f) }
    }
    private val loading = state.takeIf { it.startsWith("loading-") }?.let {
        val flag = when {
            it == "loading-flag2-hidden" -> 2
            it.startsWith("loading-flag1-") -> 1
            else -> 0
        }
        LoadingLayer(flag).also { layer -> if (it == "loading-flag1-after5") layer.advance(5f) }
    }
    private var spinnerAngle = 0f

    override fun show() {
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val p = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                if (progress != null || loading != null) return true
                return msg != null && p.y in 271f..322f && p.x in 550f..935f
            }
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply(); batch.projectionMatrix = viewport.camera.combined
        batch.begin(); batch.color = Color.WHITE
        batch.draw(background, 0f, 0f, viewport.worldWidth, 800f)
        msg?.let(::drawMsgBox)
        progress?.let(::drawProgress)
        loading?.let {
            it.advance(delta)
            if (it.imageVisible) spinnerAngle = (spinnerAngle + delta * 360f) % 360f
            drawLoading(it)
        }
        batch.end()
        toast?.let(::drawToast)
        if (game.writeRenderEventLogIfRequested()) return
        game.captureFrameIfRequested()
    }

    private fun drawMsgBox(model: SystemOverlayRenderer.MsgBox) {
        batch.draw(logo9, 426.686f, 252f, 635f, 296f)
        box.draw(batch, 426.686f, 252f, 635f, 296f)
        batch.draw(logo3, 453.005f, 373.951f, 106f, 124f)
        font.color = Color.WHITE
        font.draw(batch, model.text, 573.686f, 490f, 463f, Align.center, true)
        val buttons = if (model.flag and 2 != 0) listOf(554.186f to "비", 754.186f to "예") else listOf(654.186f to "예")
        buttons.forEach { (x, label) ->
            box.draw(batch, x, 271.285f, 180f, 50f)
            font.draw(batch, label, x, 311f, 180f, Align.center, false)
        }
    }

    private fun drawToast(model: SystemOverlayRenderer.Toast) {
        shapes.projectionMatrix = viewport.camera.combined
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(.08f, .08f, .08f, .92f)
        shapes.rect(285.686f, 367f, 917f, 66f)
        shapes.end()
        batch.begin(); font.color = Color.WHITE
        font.draw(batch, model.text, 272.226f, 414f, 943.92f, Align.center, false)
        batch.end()
    }

    private fun dim(opacity: Float) {
        if (opacity <= 0f) return
        batch.color = Color(0f, 0f, 0f, opacity)
        batch.draw(boxTexture, 0f, 0f, viewport.worldWidth, 800f)
        batch.color = Color.WHITE
    }

    private fun drawSpinner(x: Float, y: Float, size: Float, angle: Float) {
        batch.draw(
            loadingTexture, x, y, size / 2f, size / 2f, size, size, 1f, 1f, angle,
            0, 0, loadingTexture.width, loadingTexture.height, false, false
        )
    }

    private fun drawProgress(model: ProgressRenderOracle) {
        dim(.392f)
        drawSpinner(674.186f, 460.5f, 140f, spinnerAngle)
        font.color = Color.WHITE
        font.draw(batch, model.label, 370.186f, 360f, 748f, Align.center, false)
        font.draw(batch, ProgressRenderOracle.TIPS, 294.186f, 305f, 900f, Align.center, false)
    }

    private fun drawLoading(model: LoadingLayer) {
        dim(model.blockerOpacity)
        if (model.imageVisible) drawSpinner(709.186f, 365f, 70f, spinnerAngle)
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
        val log = RenderEventLog()
        val phase = "hall-$state-stable"
        fun draw(
            layer: String, path: String, type: String, x: Float, y: Float, w: Float, h: Float,
            asset: String? = null, opacity: Float = 1f, visible: Boolean = true, text: String = ""
        ) =
            log.draw(
                phase, layer, path, type, x, y, w, h, asset, opacity = opacity,
                blend = if (type == "label") listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771),
                visible = visible, text = text
            )
        draw(
            "HallLayer", "Canvas/Layer/map", "sprite", 0f, 0f, 1488.372f, 800f,
            "assets/Game/native/c6/c6b7d3e4-8590-4fb6-85a5-7967e64abc3e.8e84f.jpg#<unnamed-frame>"
        )
        progress?.let { model ->
            draw(
                "HallLayer", "Canvas/Layer/Panel_cancel", "sprite", 0f, 0f, 1488.372f, 800f,
                "default_sprite_splash", opacity = .392f
            )
            draw("HallLayer", "Canvas/Layer/anime", "sprite", 674.186f, 460.5f, 140f, 140f, "uiloading")
            draw("HallLayer", "Canvas/Layer/label", "label", 370.186f, 306.586f, 748f, 62.7f, text = model.label)
            draw(
                "HallLayer", "Canvas/Layer/label_tips", "label", 294.186f, 260.861f, 900f, 51.36f,
                text = ProgressRenderOracle.TIPS
            )
            return log.jsonl()
        }
        loading?.let { model ->
            draw(
                "HallLayer", "Canvas/Layer/cancel_panel", "sprite", 0f, 0f, 1488.372f, 800f,
                if (model.imageVisible) "default_sprite_splash"
                else "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
                opacity = model.blockerOpacity, visible = model.imageVisible
            )
            if (model.imageVisible) draw(
                "LoadingLayer",
                "Canvas/Layer/bg/img",
                "sprite",
                709.186f,
                365f,
                70f,
                70f,
                "uiloading"
            )
            return log.jsonl()
        }
        if (toast != null) {
            draw(
                "HallLayer", "Canvas/Layer/sprite_bg", "sprite", 285.686f, 367f, 917f, 66f,
                "assets/main/native/54/54f14842-1332-459f-b7aa-cb75067d180c.7637b.png#img_bg2"
            )
            draw(
                "HallLayer",
                "Canvas/Layer/sprite_bg/label",
                "label",
                272.226f,
                370.8f,
                943.92f,
                58.4f,
                text = toast.text
            )
            return log.jsonl()
        }
        draw(
            "HallLayer",
            "Canvas/Layer/Panel_cancel",
            "sprite",
            0f,
            0f,
            1488.372f,
            800f,
            "assets/internal/native/02/0275e94c-56a7-410f-bd1a-fc7483f7d14a.cea68.png#default_sprite_splash",
            opacity = 0f,
            visible = false
        )
        val model = requireNotNull(msg)
        draw("MsgBox", "Canvas/Layer/bg0", "tiled-sprite", 426.686f, 252f, 635f, 296f, "Logo_9-1")
        draw("MsgBox", "Canvas/Layer/bg0/box3", "sliced-sprite", 426.686f, 252f, 635f, 296f, "box3")
        draw("MsgBox", "Canvas/Layer/bg0/Logo_3-1", "sprite", 453.005f, 373.951f, 106f, 124f, "Logo_3-1")
        draw("MsgBox", "Canvas/Layer/bg0/label", "label", 573.686f, 335f, 463f, 190f, text = model.text)
        if (model.flag and 2 != 0) {
            draw(
                "MsgBox",
                "Canvas/Layer/bg0/btns/button1/Background",
                "sliced-sprite",
                554.186f,
                271.285f,
                180f,
                50f,
                "box3"
            )
            draw(
                "MsgBox",
                "Canvas/Layer/bg0/btns/button1/Background/Label",
                "label",
                557.336f,
                279.085f,
                168.1f,
                40f,
                text = "비"
            )
            draw(
                "MsgBox",
                "Canvas/Layer/bg0/btns/button0/Background",
                "sliced-sprite",
                754.186f,
                271.285f,
                180f,
                50f,
                "box3"
            )
            draw(
                "MsgBox",
                "Canvas/Layer/bg0/btns/button0/Background/Label",
                "label",
                757.586f,
                279.085f,
                169.4f,
                40f,
                text = "예"
            )
        } else {
            draw(
                "MsgBox",
                "Canvas/Layer/bg0/btns/button0/Background",
                "sliced-sprite",
                654.186f,
                271.285f,
                180f,
                50f,
                "box3"
            )
            draw(
                "MsgBox",
                "Canvas/Layer/bg0/btns/button0/Background/Label",
                "label",
                657.586f,
                279.085f,
                169.4f,
                40f,
                text = "예"
            )
        }
        return log.jsonl()
    }

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    override fun dispose() {
        batch.dispose(); shapes.dispose(); font.dispose(); background.dispose(); logo9.dispose(); logo3.dispose(); loadingTexture.dispose(); boxTexture.dispose()
    }
}

/** Registered Global100 prefab oracle; recovered source has no production caller. */
private class ProgressRenderOracle {
    var progress = 0f
        private set
    val label get() = "자원 로딩 중${kotlin.math.truncate(100f * progress).toInt()}%"

    /**
     * 공개 메서드 `onProgress`
     *
     * ### 파라미터
    - `value` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun onProgress(value: Float) {
        progress = value
    }

    companion object {
        const val TIPS = "자원 로딩이 완료되면 게임에 접속하는 것이 빠를 거예요!"
    }
}
