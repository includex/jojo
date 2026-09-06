// Verification
package com.jojo.game

import com.jojo.game.presentation.shared.KoreanFont

import com.jojo.game.application.runtime.RuntimeCompositionTraceProvider

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import java.nio.file.Files
import java.nio.file.Path


/** InfoLayerFixtureScreen: R_00 회관의 읽기 전용 InfoLayer 상태와 닫기 동작을 검증한다. */
class InfoLayerFixtureScreen(private val game: JojoGame) : ScreenAdapter(), RuntimeCompositionTraceProvider {
    /** requestedState: 검증 실행의 현재 상태를 담는다. */
    private val requestedState = game.requestedCaptureState()
    /** skipped: 검증 흐름에서 사용하는 값을 담는다. */
    private val skipped = requestedState == "info-layer-r00-skip"

    // 전체 공개 캡처와 Panel_cancel 이벤트 모두 자동 닫기 대기 상태를 남긴다.
    /** fullAutoPending: full auto pending 값을 보관해 검증 흐름에서 사용한다. */
    private val fullAutoPending = requestedState == "info-layer-r00-full-autopending" ||
            requestedState == "info-layer-r00-panel-touch"
    /** viewport: 검증 화면의 좌표계와 카메라 상태를 담는다. */
    private val viewport = ExtendViewport(1280f, 800f, OrthographicCamera())
    /** batch: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val batch = SpriteBatch()
    /** font: 검증 화면 렌더링에 사용하는 리소스를 담는다. */
    private val font = KoreanFont.create(40, "재능의 첫 징후")
    /** panel: 화면 패널 리소스를 담는다. */
    private val panel: Texture by lazy {
        val bytes = Files.readAllBytes(
            Path.of(
                "/Users/ain/workspace/jojo/.verification-work/raw-framebuffer-common-space/" +
                        "infolayer-subtree-observation/source-hall-infolayer-bg-frame.rgba",
            )
        )
        require(bytes.size == 19 * 17 * 4) { "source InfoLayer DynamicAtlas crop is not 19x17 RGBA" }
        val pixmap = Pixmap(19, 17, Pixmap.Format.RGBA8888)
        pixmap.pixels.put(bytes).flip()
        Texture(pixmap).also { texture ->
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            pixmap.dispose()
        }
    }

    /** show: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
    }

    /** resize: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)

    /** render: 검증 대상의 현재 렌더 이벤트를 출력한다. */
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        if (skipped) {
            // SKIP 입력은 다음 단계로 바로 이동하므로 다음 프레임 전에 제거된다.
            batch.end()
            game.captureFrameIfRequested()
            return
        }
        // 원본 캔버스 중심 좌표와 배경 앵커를 LibGDX 좌표로 변환한다.
        val canvasX = 744.18605f
        val canvasY = 400f
        val bgW = if (fullAutoPending) 235.23f else 74.6f
        val bgH = 83f
        batch.draw(panel, canvasX - bgW / 2f, canvasY - bgH * .28f, bgW, bgH)
        // 원본 richtext의 중심·범위·글꼴 크기를 유지한다.
        font.color = Color.WHITE
        val text = if (fullAutoPending) "재능의 첫 징후" else "재"
        val textWidth = if (fullAutoPending) 229.83f else 34.6f
        font.draw(batch, text, canvasX - textWidth / 2f, canvasY + 18.5f)
        batch.end()
        game.captureFrameIfRequested()
    }


    /** compositionTrace: InfoLayer 구성 이벤트를 비교용 문자열로 반환한다. */
    fun compositionTrace(): String {
        val state = when (requestedState) {
            "info-layer-r00-skip" -> "skip"
            "info-layer-r00-panel-touch" -> "panel-touch"
            "info-layer-r00-full-autopending" -> "full-autopending"
            else -> "first-tick"
        }
        val key = "info-layer-r00-$state"
        val artifact = when (state) {
            "skip" -> "source-hall-infolayer-skip.json"
            "panel-touch" -> "source-hall-infolayer-panel-touch.json"
            "full-autopending" -> "source-hall-infolayer-full-autopending-subtree.json"
            else -> "source-hall-infolayer-subtree.json"
        }
        val text = when {
            skipped -> ""; fullAutoPending -> "재능의 첫 징후"; else -> "재"
        }
        val remaining = when {
            skipped -> "재능의 첫 징후"; fullAutoPending -> null; else -> "능의 첫 징후"
        }
        val bgWidth = if (fullAutoPending) 235.23 else 74.6
        val richWidth = if (fullAutoPending) 229.83 else 34.6
        val active = !fullAutoPending && !skipped
        val remainingJson = remaining?.let { "\"$it\"" } ?: "null"
        return """{"state":"R_00/Hall/InfoLayer/$state","scenarioKey":"$key","oracle":"isolated-libgdx-runtime","sourceArtifact":"$artifact","records":[{"address":"Hall/Canvas/Layer/Panel_cancel","kind":"Sprite","active":$active,"opacity":0,"size":[1488.372093,800]},{"address":"Hall/Canvas/Layer/bg","kind":"Sprite","active":$active,"frame":"bg","sourceDynamicAtlasFrame":{"rect":[2,2,19,17],"atlas":[2048,2048],"sha256":"35796a9f8ded6af912b95968fc822a42ee115af0b54b034c80954e8bad3cd569"},"size":[$bgWidth,83],"anchor":[0.5,0.28],"position":[0,0]},{"address":"Hall/Canvas/Layer/bg/richtext","kind":"RichText","active":$active,"text":"$text","fullText":"재능의 첫 징후","remainingText":$remainingJson,"fontSize":40,"lineHeight":50,"size":[$richWidth,63],"position":[0,18.5],"typewriterActive":$active,"autoClosePending":$fullAutoPending}]}"""
    }
    /** runtimeCompositionTrace: runtime composition trace에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    override fun runtimeCompositionTrace(): String = compositionTrace()

    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        batch.dispose(); font.dispose(); panel.dispose()
    }
}
