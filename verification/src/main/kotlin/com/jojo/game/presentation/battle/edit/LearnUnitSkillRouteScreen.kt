package com.jojo.game.presentation.battle.edit

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.jojo.game.JojoGame
import com.jojo.game.RuntimeRenderEventLogProvider
import com.jojo.game.application.battle.LearnUnitSkillFlow
import com.jojo.game.application.battle.LearnUnitSkillRoute
import com.jojo.game.application.battle.EditRosterLearnRoute
import com.jojo.game.presentation.battle.edit.evidence.LearnUnitSkillRenderEvents

class LearnUnitSkillRouteScreen(private val game: JojoGame, private val route: LearnUnitSkillRoute) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    private val shapes = ShapeRenderer()
    private val parent = EditRosterLearnRoute(true)
    private val flow = LearnUnitSkillFlow()
    private var installed = false
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

    fun renderEventLog() = LearnUnitSkillRenderEvents.jsonl(route)
    override fun runtimeRenderEventLog(): String = renderEventLog()
    override fun dispose() {
        shapes.dispose()
    }
}
