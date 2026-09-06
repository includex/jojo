// Verification
package com.jojo.game.presentation.overlay.fixture

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider
import com.jojo.game.presentation.shared.overlay.*
import com.jojo.game.presentation.shared.KoreanFont
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.*

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

/** SystemOverlayRenderer: 공용 시스템 오버레이를 재사용 가능한 형태로 렌더링한다. */
class SystemOverlayRenderer {

    /** MsgBox: msg box 관련 검증 상태와 동작을 제공하는 타입이다. */
    data class MsgBox(val text: String, val flag: Int)

    /** Toast: toast 관련 검증 상태와 동작을 제공하는 타입이다. */
    data class Toast(val text: String)
}

/** SystemOverlayFixtureScreen: 검증 화면의 상태와 입력·렌더링 동작을 제공하는 타입이다. */
class SystemOverlayFixtureScreen(private val game: JojoGame, private val state: String) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    /** viewport: 검증 화면의 좌표계와 카메라 상태를 담는다. */
    private val viewport = ExtendViewport(1280f, 800f, OrthographicCamera())
    /** batch: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val batch = SpriteBatch()
    /** shapes: 검증 흐름에서 사용하는 값을 담는다. */
    private val shapes = ShapeRenderer()
    /** background: 화면 배경 리소스를 담는다. */
    private val background = Texture(Gdx.files.internal("maps/71.jpg"))
    /** logo9: 검증 흐름에서 사용하는 값을 담는다. */
    private val logo9 = Texture(Gdx.files.internal("maps/ui/start-battle/logo9.png"))
    /** logo3: 검증 흐름에서 사용하는 값을 담는다. */
    private val logo3 = Texture(Gdx.files.internal("maps/ui/win-condition/logo3.png"))
    /** loadingTexture: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val loadingTexture = Texture(Gdx.files.internal("maps/ui/system-overlay/uiloading.png"))
    /** boxTexture: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val boxTexture = Texture(Gdx.files.internal("maps/ui/start-battle/button.png"))
    /** box: 검증 흐름에서 사용하는 값을 담는다. */
    private val box = NinePatch(boxTexture, 9, 9, 7, 11)
    /** font: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val font: BitmapFont =
        KoreanFont.create(34, "저장 완료.게임 저장하시겠습니까?예비원본 공통 알림 UI 비교자원 로딩 중이 완료되면 접속하는 것이 빠를 거예요!")
    /** msg: 검증 흐름에서 사용하는 값을 담는다. */
    private val msg = if (state == "msgbox-ok") SystemOverlayRenderer.MsgBox("저장 완료.", 1)
    else if (state == "msgbox-confirm") SystemOverlayRenderer.MsgBox("게임 저장하시겠습니까?", 3) else null
    /** toast: 검증 흐름에서 사용하는 값을 담는다. */
    private val toast = if (state == "toast-stable") SystemOverlayRenderer.Toast("원본 공통 알림 UI 비교") else null
    /** progress: 검증 흐름에서 사용하는 값을 담는다. */
    private val progress = state.removePrefix("progress-").takeIf { state.startsWith("progress-") }?.let {
        ProgressRenderOracle().also { layer -> layer.onProgress(it.toInt() / 100f) }
    }
    /** loading: 검증 흐름에서 사용하는 값을 담는다. */
    private val loading = state.takeIf { it.startsWith("loading-") }?.let {
        /**
         * `flag` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val flag = when {
            it == "loading-flag2-hidden" -> 2
            it.startsWith("loading-flag1-") -> 1
            else -> 0
        }
        LoadingLayer(flag).also { layer -> if (it == "loading-flag1-after5") layer.advance(5f) }
    }
    /** spinnerAngle: spinner angle 값을 보관해 검증 흐름에서 사용한다. */
    private var spinnerAngle = 0f

    /** show: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun show() {
        Gdx.input.inputProcessor = object : InputAdapter() {
            /** touchDown: 검증 입력을 현재 화면 상태에 반영한다. */
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val p = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                if (progress != null || loading != null) return true
                return msg != null && p.y in 271f..322f && p.x in 550f..935f
            }
        }
    }

    /** render: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
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

    /** drawMsgBox: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
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

    /** drawToast: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
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

    /** dim: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    private fun dim(opacity: Float) {
        if (opacity <= 0f) return
        batch.color = Color(0f, 0f, 0f, opacity)
        batch.draw(boxTexture, 0f, 0f, viewport.worldWidth, 800f)
        batch.color = Color.WHITE
    }

    /** drawSpinner: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    private fun drawSpinner(x: Float, y: Float, size: Float, angle: Float) {
        batch.draw(
            loadingTexture, x, y, size / 2f, size / 2f, size, size, 1f, 1f, angle,
            0, 0, loadingTexture.width, loadingTexture.height, false, false
        )
    }

    /** drawProgress: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    private fun drawProgress(model: ProgressRenderOracle) {
        dim(.392f)
        drawSpinner(674.186f, 460.5f, 140f, spinnerAngle)
        font.color = Color.WHITE
        font.draw(batch, model.label, 370.186f, 360f, 748f, Align.center, false)
        font.draw(batch, ProgressRenderOracle.TIPS, 294.186f, 305f, 900f, Align.center, false)
    }

    /** drawLoading: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    private fun drawLoading(model: LoadingLayer) {
        dim(model.blockerOpacity)
        if (model.imageVisible) drawSpinner(709.186f, 365f, 70f, spinnerAngle)
    }

    /** renderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    fun renderEventLog(): String {
        val log = RenderEventLog()
        val phase = "hall-$state-stable"
        /** draw: 검증 대상의 현재 렌더 이벤트를 출력한다. */
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
    /** runtimeRenderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun runtimeRenderEventLog(): String = renderEventLog()

    /** resize: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        batch.dispose(); shapes.dispose(); font.dispose(); background.dispose(); logo9.dispose(); logo3.dispose(); loadingTexture.dispose(); boxTexture.dispose()
    }
}

/** ProgressRenderOracle: 호출 경로가 복원되지 않은 Global100 프리팹의 검증용 기준 구현이다. */
private class ProgressRenderOracle {
    /** progress: 검증 흐름에서 사용하는 값을 담는다. */
    var progress = 0f
        private set
    /** label: 검증 흐름에서 사용하는 값을 담는다. */
    val label get() = "자원 로딩 중${kotlin.math.truncate(100f * progress).toInt()}%"

    /** onProgress: on progress에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    fun onProgress(value: Float) {
        progress = value
    }

    companion object {
        /**
         * `TIPS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val TIPS = "자원 로딩이 완료되면 게임에 접속하는 것이 빠를 거예요!"
    }
}
