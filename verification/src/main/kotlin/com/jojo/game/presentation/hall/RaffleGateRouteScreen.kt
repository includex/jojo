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

class RaffleGateRouteScreen(private val game: JojoGame) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    private val shapes = ShapeRenderer()
    private val route = RaffleGateRoute()
    private var entered = false
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

    fun renderEventLog(): String = RaffleGateRenderEvents.jsonl()
    override fun runtimeRenderEventLog(): String = renderEventLog()
    override fun dispose() {
        shapes.dispose()
    }
}
