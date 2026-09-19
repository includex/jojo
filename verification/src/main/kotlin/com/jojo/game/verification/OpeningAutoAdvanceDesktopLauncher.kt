package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.*
import java.io.File

/** Enable the real auto-close setting in a process-local store; observe without dialogue input. */
object OpeningAutoAdvanceDesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val output = File(args.single())
        output.delete()
        val frames = JsonValue(JsonValue.ValueType.array)
        var frame = 0
        var finished = false
        val observer = RuntimeScreenObserver { delta, screen ->
            if (!finished && screen is ScenarioRuntimeProbe) {
                frame++
                check(frame < 900 && screen.elapsedSeconds < 12f) { "Opening auto advance timed out" }
                check(screen.module == "R_00" && screen.sceneIndex == 1)
                val row = JsonValue(JsonValue.ValueType.`object`)
                row.addChild("frame", JsonValue(frame.toLong()))
                row.addChild("delta", JsonValue(delta.toDouble()))
                row.addChild("playback", JsonValue(screen.playback.name))
                row.addChild("speaker", JsonValue(screen.dialogueSpeakerId))
                row.addChild("revision", JsonValue(screen.dialogueRevision))
                row.addChild("text", JsonValue(screen.dialogueVisibleText))
                row.addChild("complete", JsonValue(screen.dialogueTextComplete))
                frames.addChild(row)
                if (screen.dialogueSpeakerId == "0" && screen.dialogueVisibleText.isNotEmpty()) {
                    val report = JsonValue(JsonValue.ValueType.`object`)
                    report.addChild("contract", JsonValue("natural-opening-dialogue-auto-game/v1"))
                    report.addChild("autoCloseSetting", JsonValue(true))
                    report.addChild("persistentSettingsChanged", JsonValue(false))
                    report.addChild("dialogueInputs", JsonValue(0L))
                    report.addChild("pixelReadback", JsonValue(false))
                    report.addChild("isolation", JsonValue(false))
                    report.addChild("frames", frames)
                    output.parentFile.mkdirs()
                    output.writeText(report.prettyPrint(JsonWriter.OutputType.json, 120))
                    finished = true
                    Gdx.app.log("JojoGame", "OPENING_AUTO_ADVANCE_COMPLETE frames=$frame")
                    Gdx.app.exit()
                }
            }
        }
        val game = JojoGame(GameLaunchConfiguration(
            entryPoint = GameEntryPoint.SCENARIO, initialScenario = "R_00",
            initialScenarioExplicit = true, runtimeScreenObserver = observer, automatedRun = true,
        ))
        // This provider uses only InMemoryPreferences when automatedRun=true.
        val provider = game.javaClass.getDeclaredField("preferenceProvider").let {
            it.isAccessible = true
            it.get(game)
        }
        val settings = provider.javaClass.getDeclaredMethod("settings").let {
            it.isAccessible = true
            it.invoke(provider) as Preferences
        }
        settings.putInteger("GAME_SETTING", settings.getInteger("GAME_SETTING", 7) or 8)
        val window = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo opening auto advance verification")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
            useVsync(true)
        }
        Lwjgl3Application(game, window)
    }
}
