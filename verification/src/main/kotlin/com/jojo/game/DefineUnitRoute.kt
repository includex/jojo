// Verification
package com.jojo.game

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/** DefineUnitFlow: HallLayer.reqEffect(0/1)에서 Global133으로 이어지는 흐름을 재현한다. */
class DefineUnitFlow(private val resume: () -> Unit = {}) {
    /** Prompt: 사용자 확인이 필요한 편집 흐름의 상태이다. */
    enum class Prompt { NONE, RESET, FINISH }

    /** paused: 검증 흐름에서 사용하는 값을 담는다. */
    var paused = false; private set
    /** attached: 검증 흐름에서 사용하는 값을 담는다. */
    var attached = false; private set
    /** prompt: 검증 흐름에서 사용하는 값을 담는다. */
    var prompt = Prompt.NONE; private set
    /** score: 검증 흐름에서 사용하는 값을 담는다. */
    var score = 25; private set
    /** name: 이름을 담는다. */
    var name = "조조"; private set
    /** abilities: 검증 흐름에서 사용하는 값을 담는다. */
    val abilities = mutableListOf(41, 49, 46, 40, 42)

    /** reqEffect: 지정된 효과 화면을 열고 입력을 일시 중지한다. */
    fun reqEffect(effect: Int): Boolean {
        if (effect > 1) return false
        paused = true; attached = true
        return true
    }

    /** touchButton: 버튼 입력을 현재 확인 대화 상태로 변환한다. */
    fun touchButton(tag: Int, touchEnd: Boolean): Boolean {
        if (!attached || !touchEnd) return false
        prompt = when (tag) {
            0 -> if (name.trim().isNotEmpty()) Prompt.FINISH else Prompt.NONE; 1 -> Prompt.RESET; else -> return false
        }
        return prompt != Prompt.NONE
    }

    /** answer: 확인 결과에 따라 능력치를 초기화하거나 흐름을 종료한다. */
    fun answer(yes: Boolean) {
        when (prompt) {
            Prompt.RESET -> if (yes) {
                listOf(41, 49, 46, 40, 42).forEachIndexed { i, value -> abilities[i] = value }; score = 25
            }

            Prompt.FINISH -> if (yes) {
                attached = false; paused = false; resume()
            }

            Prompt.NONE -> return
        }
        prompt = Prompt.NONE
    }
}

/** DefineUnitRoute: 편집 화면에서 검증할 상태 경로를 정의한다. */
enum class DefineUnitRoute(val key: String) {
    DEFAULT("default"), RESET_PROMPT("reset-prompt"), FINISH_PROMPT("finish-prompt");

    companion object {
        /** parse: 외부 상태 문자열을 편집 경로로 변환한다. */
        fun parse(state: String?): DefineUnitRoute? {
            val value = state?.removeSuffix("-fixture")?.removePrefix("hall-define-unit-") ?: return null
            return entries.firstOrNull { it.key == value }
        }
    }
}

class DefineUnitRouteScreen(private val game: JojoGame, private val route: DefineUnitRoute) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    /** shapes: 검증 흐름에서 사용하는 값을 담는다. */
    private val shapes = ShapeRenderer()
    /** flow: 검증 흐름에서 사용하는 값을 담는다. */
    private val flow = DefineUnitFlow()
    /** entered: 검증 흐름에서 사용하는 값을 담는다. */
    private var entered = false
    /** render: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun render(delta: Float) {
        if (!entered) {
            check(flow.reqEffect(0)); when (route) {
                DefineUnitRoute.RESET_PROMPT -> check(
                    flow.touchButton(
                        1,
                        true
                    )
                ); DefineUnitRoute.FINISH_PROMPT -> check(flow.touchButton(0, true)); else -> {}
            }; entered = true
        }; Gdx.gl.glClearColor(
            0f,
            0f,
            0f,
            1f
        ); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color =
            Color(.7f, .64f, .48f, 1f); shapes.rect(
            241f,
            125f,
            1006f,
            549f
        ); if (flow.prompt != DefineUnitFlow.Prompt.NONE) {
            shapes.color = Color(0f, 0f, 0f, .4f); shapes.rect(0f, 0f, 1280f, 800f)
        }; shapes.end(); game.writeRenderEventLogIfRequested()
    }

    /** renderEventLog: 현재 편집 경로의 렌더 이벤트를 반환한다. */
    fun renderEventLog() = com.jojo.game.verification.evidence.DefineUnitRenderEvents.record(flow, route)
    /** runtimeRenderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun runtimeRenderEventLog(): String = renderEventLog()
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        shapes.dispose()
    }
}
