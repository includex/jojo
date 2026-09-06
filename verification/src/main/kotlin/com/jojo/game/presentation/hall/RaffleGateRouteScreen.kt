// Verification
package com.jojo.game.presentation.hall

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.jojo.game.JojoGame
import com.jojo.game.application.navigation.RaffleGateRoute
import com.jojo.game.presentation.hall.evidence.RaffleGateRenderEvents

/** RaffleGateRouteScreen: 검증 화면의 상태와 입력·렌더링 동작을 제공하는 타입이다. */
class RaffleGateRouteScreen(private val game: JojoGame) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    /** shapes: 검증 흐름에서 사용하는 값을 담는다. */
    private val shapes = ShapeRenderer()
    /** route: 검증 흐름에서 사용하는 값을 담는다. */
    private val route = RaffleGateRoute()
    /** entered: 검증 흐름에서 사용하는 값을 담는다. */
    private var entered = false
    /** render: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun render(delta: Float) {
        if (!entered) {
            route.openHallMenu(true); route.hallMenuButton(3, true); route.settingButton(
                8,
                true,
                0
            ); check(route.view().layer == RaffleGateRoute.Layer.SETTING); entered = true
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color = Color(.25f, .22f, .17f, 1f); shapes.rect(
            0f,
            0f,
            1488.372f,
            800f
        ); shapes.color = Color(.72f, .67f, .55f, 1f); shapes.rect(195.686f, 41f, 1097f, 718f); shapes.end()
        game.writeRenderEventLogIfRequested()
    }

    /** renderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    fun renderEventLog(): String = RaffleGateRenderEvents.jsonl()
    /** runtimeRenderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun runtimeRenderEventLog(): String = renderEventLog()
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        shapes.dispose()
    }
}
