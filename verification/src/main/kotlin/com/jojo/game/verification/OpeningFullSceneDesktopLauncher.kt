package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.infrastructure.data.ScenarioDialogueTextCatalog
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.*
import com.jojo.game.domain.scenario.PlaybackState
import java.io.File
import java.security.MessageDigest

/** Capture the first naturally completed Hall dialogue with the entire scene visible. */
object OpeningFullSceneDesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val directory = File(args.single()).also { it.mkdirs() }
        listOf("game-full.json", "game-full.rgba").forEach { File(directory, it).delete() }
        var frame = 0
        var finished = false
        lateinit var game: JojoGame
        val observer = RuntimeScreenObserver { delta, probe ->
            if (!finished && probe is ScenarioRuntimeProbe) {
                frame++
                check(frame < 900 && probe.elapsedSeconds < 12f)
                check(probe.module == "R_00" && probe.sceneIndex == 1)
                if (probe.playback == PlaybackState.DIALOGUE && probe.dialogueTextComplete) {
                    check(probe.dialogueSpeakerId == "181" && probe.dialogueVisibleText == ScenarioDialogueTextCatalog.text("opening_scene1_page1"))
                    check(!probe.naturalStreetTextIsolation)
                    check(probe.actors.size == 4 && probe.actors.all { it.moveDuration == 0f })
                    val width = Gdx.graphics.backBufferWidth
                    val height = Gdx.graphics.backBufferHeight
                    check(width == 2560 && height == 1376)
                    val pixels = ScreenUtils.getFrameBufferPixmap(0, 0, width, height)
                    val bytes = ByteArray(width * height * 4)
                    try { pixels.pixels.rewind(); pixels.pixels.get(bytes) } finally { pixels.dispose() }
                    File(directory, "game-full.rgba").writeBytes(bytes)
                    val report = JsonValue(JsonValue.ValueType.`object`)
                    fun put(key: String, value: String) = report.addChild(key, JsonValue(value))
                    put("contract", "natural-opening-full-scene-game/v1")
                    put("origin", "bottom-left")
                    put("file", "game-full.rgba")
                    put("sha256", MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) })
                    put("text", probe.dialogueVisibleText)
                    put("speakerId", requireNotNull(probe.dialogueSpeakerId))
                    report.addChild("width", JsonValue(width.toLong()))
                    report.addChild("height", JsonValue(height.toLong()))
                    report.addChild("frame", JsonValue(frame.toLong()))
                    report.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
                    report.addChild("delta", JsonValue(delta.toDouble()))
                    report.addChild("complete", JsonValue(true))
                    report.addChild("isolation", JsonValue(false))
                    report.addChild("dialogueInputs", JsonValue(0L))
                    report.addChild("backgroundId", JsonValue(probe.backgroundId.toLong()))
                    val actors = JsonValue(JsonValue.ValueType.array)
                    probe.actors.forEach { actor ->
                        val row = JsonValue(JsonValue.ValueType.`object`)
                        row.addChild("id", JsonValue(actor.id.toLong()))
                        row.addChild("visualX", JsonValue(actor.visualX.toDouble()))
                        row.addChild("visualY", JsonValue(actor.visualY.toDouble()))
                        row.addChild("direction", JsonValue(actor.direction.toLong()))
                        row.addChild("action", JsonValue(actor.action.toLong()))
                        row.addChild("visible", JsonValue(actor.visible))
                        row.addChild("moveDuration", JsonValue(actor.moveDuration.toDouble()))
                        actors.addChild(row)
                    }
                    report.addChild("actors", actors)
                    val screen = game.screen
                    val view = screen.javaClass.getDeclaredMethod("battlefieldView", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType).let {
                        it.isAccessible = true; it.invoke(screen, true, true)
                    }
                    val units = view.javaClass.getDeclaredMethod("getUnits").invoke(view) as List<*>
                    val renderUnits = JsonValue(JsonValue.ValueType.array)
                    for (unit in units.filterNotNull()) {
                        val row = JsonValue(JsonValue.ValueType.`object`)
                        for (name in listOf("id", "visualX", "visualY", "zIndex", "siblingOrder", "textureAssetId", "frameRow", "flipX", "showSpeechBubble")) {
                            val field = unit.javaClass.getDeclaredField(name).also { it.isAccessible = true }
                            val value = field.get(unit)
                            row.addChild(name, if (value is Boolean) JsonValue(value) else JsonValue((value as Number).toDouble()))
                        }
                        renderUnits.addChild(row)
                    }
                    report.addChild("renderPlanAtCapture", renderUnits)
                    File(directory, "game-full.json").writeText(report.prettyPrint(JsonWriter.OutputType.json, 120))
                    OpeningUnitTextureCapture.capture(screen, directory)
                    finished = true
                    Gdx.app.log("JojoGame", "OPENING_FULL_SCENE_COMPLETE frame=$frame")
                    Gdx.app.exit()
                }
            }
        }
        val window = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo full opening scene verification")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
            useVsync(true)
        }
        game = JojoGame(GameLaunchConfiguration(
            entryPoint = GameEntryPoint.SCENARIO, initialScenario = "R_00", initialScenarioExplicit = true,
            runtimeScreenObserver = observer, automatedRun = true,
        ))
        Lwjgl3Application(game, window)
    }
}
