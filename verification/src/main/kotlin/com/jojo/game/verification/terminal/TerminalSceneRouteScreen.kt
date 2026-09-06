// Verification
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
import com.jojo.game.presentation.title.TerminalSceneFlow

/** TerminalSceneRoute: 실제 Welcome·End 수명 주기 후 Login 그리기 제출을 수행하는 화면이다. */
enum class TerminalSceneRoute(val state: String, val phase: String) {
    WELCOME_CREATE("terminal-welcome-create", "terminal-welcome-create"),
    END_CREATE("terminal-end-create", "terminal-end-create"),
    END_EVENT3("terminal-end-event3", "terminal-end-event3"),
    END_EVENT5("terminal-end-event5", "terminal-end-event5");

    companion object {

        /** parse: 외부 입력을 검증 모델로 해석한다. */
        fun parse(value: String?): TerminalSceneRoute? {
            val state = value?.removeSuffix("-fixture") ?: return null
            return entries.firstOrNull { it.state == state }
        }
    }
}


/** TerminalSceneRouteScreen: 종료 장면 수명주기 뒤 타이틀 렌더링 제출을 검증 경로로 실행하는 화면이다. */
class TerminalSceneRouteScreen(
    /** game: 게임 인스턴스 상태를 검증 흐름에 전달한다. */
    private val game: JojoGame,
    /** route: 검증 시나리오 경로를 담는다. */
    private val route: TerminalSceneRoute,
) : ScreenAdapter() {
    /** viewport: 검증 화면 상태를 담는다. */
    private val viewport = ScreenViewport(OrthographicCamera())
    /** batch: 렌더 배치 상태를 검증 흐름에 전달한다. */
    private val batch = SpriteBatch()
    /** background: 배경 값을 보관한다. */
    private val background = Texture(Gdx.files.internal("maps/ui/title/background.jpg"))
    /** logo0: logo0 상태를 검증 흐름에 전달한다. */
    private val logo0 = Texture(Gdx.files.internal("maps/ui/title/logo0.png"))
    /** buttons: 버튼 목록 상태를 검증 흐름에 전달한다. */
    private val buttons = (0..3).map { Texture(Gdx.files.internal("maps/ui/title/button$it.png")) }
    /** flow: 진행 흐름 상태를 검증 흐름에 전달한다. */
    private val flow = TerminalSceneFlow(
        if (route == TerminalSceneRoute.WELCOME_CREATE) TerminalSceneFlow.Kind.WELCOME else TerminalSceneFlow.Kind.END,
    )
    /** lifecycleApplied: 수명 주기 적용 여부 상태를 검증 흐름에 전달한다. */
    private var lifecycleApplied = false

    /** render: 검증 렌더 이벤트를 구성하고 반환한다. */
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


    /** renderEventLog: 검증 렌더 이벤트를 구성하고 반환한다. */
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

    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        background.dispose()
        logo0.dispose()
        buttons.forEach(Texture::dispose)
        batch.dispose()
    }
}
