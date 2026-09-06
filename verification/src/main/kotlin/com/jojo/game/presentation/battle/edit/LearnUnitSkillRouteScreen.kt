// Verification
package com.jojo.game.presentation.battle.edit

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.jojo.game.JojoGame
import com.jojo.game.application.battle.LearnUnitSkillFlow
import com.jojo.game.application.battle.LearnUnitSkillRoute
import com.jojo.game.application.battle.EditRosterLearnRoute
import com.jojo.game.presentation.battle.edit.evidence.LearnUnitSkillRenderEvents

/** LearnUnitSkillRouteScreen: 검증 화면의 상태와 입력·렌더링 동작을 제공하는 타입이다. */
class LearnUnitSkillRouteScreen(private val game: JojoGame, private val route: LearnUnitSkillRoute) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    /** shapes: 검증 흐름에서 사용하는 값을 담는다. */
    private val shapes = ShapeRenderer()
    /** parent: 검증 흐름에서 사용하는 값을 담는다. */
    private val parent = EditRosterLearnRoute(true)
    /** flow: 검증 흐름에서 사용하는 값을 담는다. */
    private val flow = LearnUnitSkillFlow()
    /** installed: 검증 흐름에서 사용하는 값을 담는다. */
    private var installed = false
    /** render: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun render(delta: Float) {
        if (!installed) {
            check(parent.button(4, true)); when (route) {
                LearnUnitSkillRoute.SELECT -> check(
                    flow.panelButton(0, 0).single() is LearnUnitSkillFlow.Effect.OpenSelectList
                ); LearnUnitSkillRoute.APPLY -> {
                    check(
                        flow.panelButton(0, 0).single() is LearnUnitSkillFlow.Effect.OpenSelectList
                    ); flow.selectListResult(1001); check(flow.save() == listOf(LearnUnitSkillFlow.Effect.SetUnit0(1001)))
                }; LearnUnitSkillRoute.CANCEL -> {
                    flow.close(); parent.close()
                }; else -> {}
            }; installed = true
        }; Gdx.gl.glClearColor(
            0f,
            0f,
            0f,
            1f
        ); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); shapes.begin(ShapeRenderer.ShapeType.Filled); shapes.color =
            Color(.72f, .67f, .55f, 1f); if (route != LearnUnitSkillRoute.CANCEL) shapes.rect(
            0f,
            0f,
            1280f,
            688f
        ); shapes.end(); game.writeRenderEventLogIfRequested()
    }

    /** renderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    fun renderEventLog() = LearnUnitSkillRenderEvents.jsonl(route)
    /** runtimeRenderEventLog: 검증 대상의 현재 화면 또는 렌더 이벤트를 출력한다. */
    override fun runtimeRenderEventLog(): String = renderEventLog()
    /** dispose: 화면과 렌더링 리소스를 해제한다. */
    override fun dispose() {
        shapes.dispose()
    }
}
