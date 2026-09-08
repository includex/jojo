// Verification
package com.jojo.game.verification.cmd

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.util.*
import com.jojo.game.presentation.shared.overlay.CmdLayer
import com.jojo.game.presentation.shared.overlay.CmdProductionRoute
import com.jojo.game.JojoGame
import com.jojo.game.presentation.shared.evidence.RenderEventLog


/** CmdRoute: 검증 화면의 입력 경로를 제공하는 타입이다. */
enum class CmdRoute(val key: String) {
    DEFAULT("default"), SELECTED("selected"), INFO("info");

    companion object {

        /** parse: 외부 입력을 검증 모델로 해석한다. */
        fun parse(state: String?): CmdRoute? {
            val value = state?.removeSuffix("-fixture")?.removePrefix("login-cmd-") ?: return null
            return entries.firstOrNull { it.key == value }
        }
    }
}


/** CmdRouteScreen: 검증 화면의 입력 경로를 제공하는 타입이다. */
class CmdRouteScreen(
    /** game: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private val game: JojoGame,
    /** route: 검증 실행 계획을 담는다. */
    private val route: CmdRoute,
) : ScreenAdapter() {
    /** shapes: 검증 대상 목록을 담는다. */
    private val shapes = ShapeRenderer()
    /** parent: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private val parent = CmdProductionRoute()
    /** layer: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private val layer = CmdLayer(
        rFlag = 1,
        initialEFlag = 0,
        deviceId = "verification-device",
        unitCount = 0,
        inventory = emptyList(),
    )
    /** installed: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    private var installed = false

    /** render: 검증 화면의 현재 프레임을 렌더링한다. */
    override fun render(delta: Float) {
        if (!installed) {
            check(parent.settingTool(tag2 = 3, touchEnd = true, rFlag = 1))
            layer.onCreate()
            when (route) {
                CmdRoute.DEFAULT -> Unit
                CmdRoute.SELECTED -> {
                    layer.answer(1)
                    layer.item(1, 2)
                    check(layer.sFlag == 2)
                }

                CmdRoute.INFO -> { layer.answer(1); layer.itemInfo(0, 2) }
            }
            installed = true
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(.72f, .67f, .55f, 1f)
        shapes.rect(0f, 0f, 1280f, 688f)
        shapes.end()
        game.writeRenderEventLogIfRequested()
    }


    /** renderEventLog: 검증 화면의 렌더 이벤트 로그를 반환한다. */
    fun renderEventLog() = CmdRenderEvents.record(layer)
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() = shapes.dispose()
}
