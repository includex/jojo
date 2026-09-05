package com.jojo.game

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

/**
 * Isolated live rendering fixture for the read-only Cocos R_00 Hall
 * InfoLayer observation.  Values are intentionally pinned to the source
 * subtree artifact; this is not a generic replacement for InfoLayer yet.
 */
/**
 * class  `InfoLayerFixtureScreen`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class InfoLayerFixtureScreen(private val game: JojoGame) : ScreenAdapter() {
    private val requestedState = game.requestedCaptureState()
    private val skipped = requestedState == "info-layer-r00-skip"

    // Both the natural full-reveal capture and the real Panel_cancel event
    // leave the original InfoLayer in this rendered auto-close-pending state.
    private val fullAutoPending = requestedState == "info-layer-r00-full-autopending" ||
            requestedState == "info-layer-r00-panel-touch"
    private val viewport = ExtendViewport(1280f, 800f, OrthographicCamera())
    private val batch = SpriteBatch()
    private val font = KoreanFont.create(40, "재능의 첫 징후")
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

    override fun show() {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
    }

    override fun resize(width: Int, height: Int) = viewport.update(width, height, true)

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        if (skipped) {
            // The source SKIP listener calls _next() directly; the InfoLayer
            // is removed before the next rendered frame.
            batch.end()
            game.captureFrameIfRequested()
            return
        }
        // Cocos Canvas origin is its centre; source runtime Canvas is
        // 1488.372093×800.  bg anchor=(.5,.28), local=(0,0), size=74.6×83.
        val canvasX = 744.18605f
        val canvasY = 400f
        val bgW = if (fullAutoPending) 235.23f else 74.6f
        val bgH = 83f
        batch.draw(panel, canvasX - bgW / 2f, canvasY - bgH * .28f, bgW, bgH)
        // Source richtext centre=(0,18.5), bounds=34.6×63, fontSize=40.
        font.color = Color.WHITE
        val text = if (fullAutoPending) "재능의 첫 징후" else "재"
        val textWidth = if (fullAutoPending) 229.83f else 34.6f
        font.draw(batch, text, canvasX - textWidth / 2f, canvasY + 18.5f)
        batch.end()
        game.captureFrameIfRequested()
    }

    /**
     * 공개 메서드 `compositionTrace`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `String`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    override fun dispose() {
        batch.dispose(); font.dispose(); panel.dispose()
    }
}
