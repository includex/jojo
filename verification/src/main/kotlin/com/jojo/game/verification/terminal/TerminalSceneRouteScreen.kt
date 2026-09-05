package com.jojo.game.verification.terminal

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.jojo.game.JojoGame
import com.jojo.game.presentation.shared.evidence.RenderEventLog
import com.jojo.game.TerminalSceneFlow

/** Actual Welcome/End lifecycle followed by the Login draw submissions. */
enum class TerminalSceneRoute(val state: String, val phase: String) {
    WELCOME_CREATE("terminal-welcome-create", "terminal-welcome-create"),
    END_CREATE("terminal-end-create", "terminal-end-create"),
    END_EVENT3("terminal-end-event3", "terminal-end-event3"),
    END_EVENT5("terminal-end-event5", "terminal-end-event5");

    companion object {
        /**
         * 공개 메서드 `parse`
         *
         * ### 파라미터
        - `value` (`String?`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `TerminalSceneRoute?`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun parse(value: String?): TerminalSceneRoute? {
            val state = value?.removeSuffix("-fixture") ?: return null
            return entries.firstOrNull { it.state == state }
        }
    }
}

/**
 * class  `TerminalSceneRouteScreen`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class TerminalSceneRouteScreen(
    private val game: JojoGame,
    private val route: TerminalSceneRoute,
) : ScreenAdapter() {
    private val viewport = ScreenViewport(OrthographicCamera())
    private val batch = SpriteBatch()
    private val background = Texture(Gdx.files.internal("maps/ui/title/background.jpg"))
    private val logo0 = Texture(Gdx.files.internal("maps/ui/title/logo0.png"))
    private val buttons = (0..3).map { Texture(Gdx.files.internal("maps/ui/title/button$it.png")) }
    private val flow = TerminalSceneFlow(
        if (route == TerminalSceneRoute.WELCOME_CREATE) TerminalSceneFlow.Kind.WELCOME else TerminalSceneFlow.Kind.END,
    )
    private var lifecycleApplied = false

    override fun render(delta: Float) {
        if (!lifecycleApplied) {
            when (route) {
                TerminalSceneRoute.WELCOME_CREATE, TerminalSceneRoute.END_CREATE -> flow.onCreate()
                TerminalSceneRoute.END_EVENT3 -> flow.onEvent(3)
                TerminalSceneRoute.END_EVENT5 -> flow.onEvent(5)
            }
            lifecycleApplied = true
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        viewport.apply()
        if (flow.drainRequests().isNotEmpty() || route != TerminalSceneRoute.END_EVENT5) {
            batch.projectionMatrix = viewport.camera.combined
            batch.enableBlending()
            batch.begin()
            batch.draw(background, 0f, 0f, 1280f, 688f)
            if (route != TerminalSceneRoute.WELCOME_CREATE) {
                batch.draw(logo0, 370.980f, 75.356f, 98.040f, 98.040f)
            }
            val sourceY = floatArrayOf(538f, 412f, 285f, 159f)
            buttons.forEachIndexed { index, texture ->
                batch.draw(texture, 945.460f, sourceY[index] * .86f, 302.720f, 75.680f)
            }
            batch.end()
        }
        game.writeRenderEventLogIfRequested()
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
        if (route == TerminalSceneRoute.END_EVENT5) return ""
        val log = RenderEventLog()
        log.draw(
            route.phase, "HallLayer", "Canvas/bg", "sprite", 0f, 0f, 1280f, 688f,
            "assets/resources/native/4d/4debf9ca-54d9-48e2-855c-34ef06c80bc4.5e28d.jpg#Logo_1-1"
        )
        if (route != TerminalSceneRoute.WELCOME_CREATE) {
            log.draw(
                route.phase,
                "HallLayer",
                "Canvas/Logo0",
                "sprite",
                370.980f,
                75.356f,
                98.040f,
                98.040f,
                "Logo_2-1"
            )
        }
        val sourceY = floatArrayOf(538f, 412f, 285f, 159f)
        sourceY.forEachIndexed { index, y ->
            log.draw(
                route.phase, "Login", "Canvas/Layer/bg1/button$index/Background", "sliced-sprite",
                945.460f, y * .86f, 302.720f, 75.680f, "U_select_12-1_$index"
            )
        }
        return log.jsonl()
    }

    override fun dispose() {
        background.dispose()
        logo0.dispose()
        buttons.forEach(Texture::dispose)
        batch.dispose()
    }
}
