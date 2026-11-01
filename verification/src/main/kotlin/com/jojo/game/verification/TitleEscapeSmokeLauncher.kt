package com.jojo.game.verification

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.GameLaunchConfiguration

/** Real-window regression: Escape stays on login and dismisses title overlays. */
object TitleEscapeSmokeLauncher {
    @JvmStatic fun main(args: Array<String>) {
        val game = JojoGame(GameLaunchConfiguration())
        var frame = 0
        var title: com.badlogic.gdx.Screen? = null
        fun escape() { checkNotNull(Gdx.input.inputProcessor).keyDown(Input.Keys.ESCAPE) }
        fun mode(): String = game.screen.javaClass.getDeclaredField("mode")
            .also { it.isAccessible = true }.get(game.screen).toString()
        Lwjgl3Application(object : ApplicationAdapter() {
            override fun create() = game.create()
            override fun resize(width: Int, height: Int) = game.resize(width, height)
            override fun render() {
                game.render()
                when (++frame) {
                    10 -> { title = game.screen; check(mode() == "LOGIN"); escape() }
                    11 -> { check(game.screen === title); check(mode() == "LOGIN"); game.showTitleSettingScreen() }
                    12 -> { check(mode() == "SETTING"); escape() }
                    13 -> { check(mode() == "LOGIN"); game.showTitleLoadScreen() }
                    14 -> { check(mode() == "LOAD"); escape() }
                    15 -> { check(mode() == "LOGIN"); println("TITLE_ESCAPE_PASS login,setting,load"); Gdx.app.exit() }
                }
            }
            override fun dispose() = game.dispose()
        }, Lwjgl3ApplicationConfiguration().apply {
            setTitle("임충전 · Escape regression")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
        })
    }
}
