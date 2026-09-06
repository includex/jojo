package com.jojo.game

import com.jojo.game.application.runtime.RuntimeRenderEventLogProvider
import com.jojo.game.presentation.battle.edit.*

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/** HallMenu(8)에서 EditLayer4로 이어지는 편성 경로를 검증한다. */
class EditRosterRouteScreen(private val game: JojoGame, private val route: EditRosterRoute) : ScreenAdapter(), RuntimeRenderEventLogProvider {
    private val shapes = ShapeRenderer()
    private val menu = HallEditRosterRoute(editEnabled = true)
    private val roster = EditRosterFlow(
        listOf(
            EditRosterFlow.UnitRow(0, "조조", false),
            EditRosterFlow.UnitRow(157, "허자장", false),
            EditRosterFlow.UnitRow(181, "병사 ", false)
        ),
        List(256) { "무장 $it" },
    )
    private var installed = false

    override fun render(delta: Float) {
        if (!installed) {
            check(menu.touch(8, true)) { "HallMenu button8 did not open EditLayer4" }
            if (route == EditRosterRoute.SELECT) check(
                roster.button(1).single() is EditRosterFlow.Effect.OpenUnitSelector
            )
            installed = true
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapes.begin(ShapeRenderer.ShapeType.Filled)
        shapes.color = Color(0f, 0f, 0f, .314f); shapes.rect(
            0f,
            0f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        )
        shapes.color = Color(.72f, .67f, .55f, 1f); shapes.rect(362f, 19f, 557f, 649f)
        shapes.end()
        game.writeRenderEventLogIfRequested()
    }

    /** 편성 화면의 렌더 이벤트를 비교용 문자열로 반환한다. */

    fun renderEventLog(): String = EditRosterRenderEvents.jsonl(route)
    override fun runtimeRenderEventLog(): String = renderEventLog()
    override fun dispose() {
        shapes.dispose()
    }
}
