package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.*
import java.io.File

/** Observe actual portrait region selection, without causing a texture lookup or pixel readback. */
object OpeningPortraitReadinessDesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val output = File(args.single()).also { it.delete() }
        val rows = JsonValue(JsonValue.ValueType.array)
        val draws = JsonValue(JsonValue.ValueType.array)
        val inputs = JsonValue(JsonValue.ValueType.array)
        val texts = listOf("대장님, 서둘러야 해요!", "알아!", "잠시만 기다려 주세요!")
        val speakers = listOf("181", "0", "157")
        var frame = 0
        var installed = false
        var finished = false
        var pendingAdvance = false
        var page = 0
        lateinit var game: JojoGame
        val observer = RuntimeScreenObserver { delta, probe ->
            if (!finished && probe is ScenarioRuntimeProbe) {
                frame++
                check(frame < 900 && probe.elapsedSeconds < 12f)
                check(probe.module == "R_00" && probe.sceneIndex == 1)
                if (!installed) {
                    val screen = game.screen
                    val assets = screen.javaClass.getDeclaredField("sceneAssets").let { it.isAccessible = true; it.get(screen) }
                    val callback: (Int, TextureRegion?, Boolean) -> Unit = { id, region, cached ->
                        val row = JsonValue(JsonValue.ValueType.`object`)
                        row.addChild("frame", JsonValue((frame + 1).toLong()))
                        row.addChild("portraitId", JsonValue(id.toLong()))
                        row.addChild("cacheHit", JsonValue(cached))
                        row.addChild("regionReturned", JsonValue(region != null))
                        row.addChild("textureHandle", JsonValue((region?.texture?.textureObjectHandle ?: 0).toLong()))
                        row.addChild("width", JsonValue((region?.regionWidth ?: 0).toLong()))
                        row.addChild("height", JsonValue((region?.regionHeight ?: 0).toLong()))
                        row.addChild("regionX", JsonValue((region?.regionX ?: 0).toLong()))
                        row.addChild("regionY", JsonValue((region?.regionY ?: 0).toLong()))
                        draws.addChild(row)
                    }
                    assets.javaClass.getDeclaredField("portraitRegionObserver").let { it.isAccessible = true; it.set(assets, callback) }
                    installed = true
                }
                if (probe.playback.name == "DIALOGUE") {
                    val row = JsonValue(JsonValue.ValueType.`object`)
                    row.addChild("frame", JsonValue(frame.toLong()))
                    row.addChild("delta", JsonValue(delta.toDouble()))
                    row.addChild("speaker", JsonValue(probe.dialogueSpeakerId))
                    row.addChild("revision", JsonValue(probe.dialogueRevision))
                    row.addChild("side", JsonValue(probe.dialogueSide.toLong()))
                    row.addChild("text", JsonValue(probe.dialogueVisibleText))
                    row.addChild("complete", JsonValue(probe.dialogueTextComplete))
                    rows.addChild(row)
                    if (pendingAdvance) {
                        check(probe.dialogueVisibleText == texts[page] && probe.dialogueTextComplete)
                        val event = JsonValue(JsonValue.ValueType.`object`)
                        event.addChild("frame", JsonValue(frame.toLong()))
                        event.addChild("afterPage", JsonValue((page + 1).toLong()))
                        event.addChild("completeBeforeInput", JsonValue(true))
                        event.addChild("kind", JsonValue("InputProcessor.keyDown/keyUp(SPACE)"))
                        val processor = checkNotNull(Gdx.input.inputProcessor)
                        check(processor.keyDown(Input.Keys.SPACE))
                        processor.keyUp(Input.Keys.SPACE)
                        inputs.addChild(event)
                        page++
                        pendingAdvance = false
                    } else {
                        check(probe.dialogueSpeakerId == speakers[page])
                        check(texts[page].startsWith(probe.dialogueVisibleText))
                        if (probe.dialogueTextComplete) {
                            if (page < 2) pendingAdvance = true
                            else {
                                check(inputs.size == 2)
                                val report = JsonValue(JsonValue.ValueType.`object`)
                                report.addChild("contract", JsonValue("natural-opening-portrait-readiness-game/v1"))
                                report.addChild("pixelReadback", JsonValue(false))
                                report.addChild("isolation", JsonValue(false))
                                report.addChild("dialogueInputs", JsonValue(2L))
                                report.addChild("frames", rows)
                                report.addChild("portraitRegionSelections", draws)
                                report.addChild("inputs", inputs)
                                output.parentFile.mkdirs()
                                output.writeText(report.prettyPrint(JsonWriter.OutputType.json, 120))
                                finished = true
                                Gdx.app.log("JojoGame", "OPENING_PORTRAITS_COMPLETE frames=$frame")
                                Gdx.app.exit()
                            }
                        }
                    }
                }
            }
        }
        game = JojoGame(GameLaunchConfiguration(
            entryPoint = GameEntryPoint.SCENARIO, initialScenario = "R_00", initialScenarioExplicit = true,
            runtimeScreenObserver = observer, automatedRun = true,
        ))
        val window = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo portrait readiness verification")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
            useVsync(true)
        }
        Lwjgl3Application(game, window)
    }
}
