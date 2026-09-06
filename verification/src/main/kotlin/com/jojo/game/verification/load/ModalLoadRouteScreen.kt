package com.jojo.game.verification.load
import com.jojo.game.presentation.shared.overlay.*

import com.jojo.game.JojoGame
import com.jojo.game.presentation.shared.evidence.RenderEventLog


object ModalLoadRenderEvents {
    private const val TABLE =
        "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"


    fun jsonl(): String {
        val table = java.util.zip.GZIPInputStream(
            java.io.ByteArrayInputStream(java.util.Base64.getDecoder().decode(TABLE))
        ).bufferedReader().readText()
        val log = RenderEventLog()
        table.lineSequence().filter { it.isNotEmpty() }.forEach { row ->
            val v = row.split('\t', limit = 12)
            val blend: Any =
                if (v[9].contains("SRC_ALPHA")) listOf("SRC_ALPHA", "ONE_MINUS_SRC_ALPHA") else listOf(770, 771)
            log.draw(
                "login-modal-load", v[0], v[1], v[2], v[3].toFloat(), v[4].toFloat(),
                v[5].toFloat(), v[6].toFloat(), v[7].ifEmpty { null }, v[8].toFloat(), blend,
                v[10].toBoolean(), v[11]
            )
        }
        return log.jsonl()
    }
}


class ModalLoadRouteScreen(private val game: JojoGame) : com.badlogic.gdx.ScreenAdapter() {
    private val shapes = com.badlogic.gdx.graphics.glutils.ShapeRenderer()
    private val route = ModalLoadProductionRoute()
    private var installed = false
    override fun render(delta: Float) {
        if (!installed) {
            route.getSystemTimeStarted()
            check(LoadLayer().onCreate(route.text).labelActive)
            installed = true
        }
        com.badlogic.gdx.Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        com.badlogic.gdx.Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT)
        shapes.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled)
        shapes.color = com.badlogic.gdx.graphics.Color(.72f, .67f, .55f, 1f)
        shapes.rect(0f, 0f, 1280f, 688f)
        shapes.end()
        game.writeRenderEventLogIfRequested()
    }


    fun renderEventLog() = ModalLoadRenderEvents.jsonl()
    override fun dispose() = shapes.dispose()
}
