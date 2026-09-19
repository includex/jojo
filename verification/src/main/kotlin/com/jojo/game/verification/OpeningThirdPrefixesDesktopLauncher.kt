package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.GameEntryPoint
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState
import java.io.File
import java.security.MessageDigest

/** Captures every naturally observed prefix of the third Hall dialogue after two normal completed-dialogue inputs with the full scene visible. */
object OpeningThirdPrefixesDesktopLauncher {
    private const val fullText = "잠시만 기다려 주세요!"

    @JvmStatic
    fun main(args: Array<String>) {
        val directory = File(args.single()).also { it.mkdirs() }
        directory.listFiles()?.forEach(File::delete)
        val captures = JsonValue(JsonValue.ValueType.array)
        val firstDialogueFrames = JsonValue(JsonValue.ValueType.array)
        val inputs = JsonValue(JsonValue.ValueType.array)
        val dialogueCompletions = JsonValue(JsonValue.ValueType.array)
        val pendingPixels = mutableListOf<Pair<JsonValue, ByteArray>>()
        val portraitReady = mutableMapOf<Int, Boolean>()
        var frame = 0
        var nextPrefixLength = 0
        var portraitObserverInstalled = false
        var dialogueInputs = 0
        var finished = false
        lateinit var game: JojoGame

        val observer = RuntimeScreenObserver { delta, probe ->
            if (!finished && probe is ScenarioRuntimeProbe) {
                frame++
                check(frame < 900 && probe.elapsedSeconds < 12f)
                check(probe.module == "R_00" && probe.sceneIndex == 1)
                if (!portraitObserverInstalled) {
                    installPortraitObserver(game, portraitReady)
                    portraitObserverInstalled = true
                }
                if (probe.playback == PlaybackState.DIALOGUE && dialogueInputs < 2 && probe.dialogueTextComplete) {
                    val expectedTexts = listOf("대장님, 서둘러야 해요!", "알아!")
                    val expectedSpeakers = listOf("181", "0")
                    check(probe.dialogueVisibleText == expectedTexts[dialogueInputs])
                    check(probe.dialogueSpeakerId == expectedSpeakers[dialogueInputs])
                    val event = JsonValue(JsonValue.ValueType.`object`)
                    event.addChild("frame", JsonValue(frame.toLong()))
                    event.addChild("afterPage", JsonValue((dialogueInputs + 1).toLong()))
                    event.addChild("kind", JsonValue("InputProcessor.keyDown/keyUp(SPACE)"))
                    event.addChild("completeBeforeInput", JsonValue(true))
                    event.addChild("textBeforeInput", JsonValue(probe.dialogueVisibleText))
                    event.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
                    val completion = JsonValue(JsonValue.ValueType.`object`)
                    completion.addChild("page", JsonValue((dialogueInputs + 1).toLong()))
                    completion.addChild("speakerId", JsonValue(requireNotNull(probe.dialogueSpeakerId)))
                    completion.addChild("text", JsonValue(probe.dialogueVisibleText))
                    completion.addChild("frame", JsonValue(frame.toLong()))
                    completion.addChild("complete", JsonValue(true))
                    val processor = checkNotNull(Gdx.input.inputProcessor)
                    check(processor.keyDown(Input.Keys.SPACE))
                    processor.keyUp(Input.Keys.SPACE)
                    inputs.addChild(event)
                    dialogueCompletions.addChild(completion)
                    dialogueInputs++
                } else if (probe.playback == PlaybackState.DIALOGUE && probe.dialogueRevision == 3L) {
                    check(dialogueInputs == 2)
                    check(probe.dialogueSpeakerId == "157")
                    check(fullText.startsWith(probe.dialogueVisibleText))
                    val portraitId = dialoguePortraitId(game, 157)
                    if (firstDialogueFrames.size < 3) {
                        val observation = JsonValue(JsonValue.ValueType.`object`)
                        observation.addChild("frame", JsonValue(frame.toLong()))
                        observation.addChild("delta", JsonValue(delta.toDouble()))
                        observation.addChild("text", JsonValue(probe.dialogueVisibleText))
                        observation.addChild("complete", JsonValue(probe.dialogueTextComplete))
                        observation.addChild("portraitId", JsonValue(portraitId.toLong()))
                        observation.addChild("portraitReady", JsonValue(portraitReady[portraitId] == true))
                        observation.addChild("actor157", actor(game, probe, 157))
                        firstDialogueFrames.addChild(observation)
                    }
                    val prefixLength = probe.dialogueVisibleText.length
                    if (prefixLength >= nextPrefixLength) {
                        check(prefixLength == nextPrefixLength) {
                            "third dialogue skipped prefix $nextPrefixLength and reached $prefixLength"
                        }
                        check(probe.dialogueVisibleText == fullText.take(prefixLength))
                        check(!probe.naturalStreetTextIsolation)
                        val expectedActors = mapOf(0 to (40 to 50), 157 to (54 to 50), 181 to (40 to 60), 182 to (40 to 40))
                        check(probe.actors.associate { it.id to (it.x to it.y) } == expectedActors)
                        val expectedDirections = mapOf(0 to 2, 157 to 3, 181 to 2, 182 to 2)
                        check(probe.actors.associate { it.id to it.direction } == expectedDirections)
                        check(probe.actors.all { it.action == 0 && it.moveDuration == 0f && it.visible })

                        val width = Gdx.graphics.backBufferWidth
                        val height = Gdx.graphics.backBufferHeight
                        check(width == 2560 && height == 1376)
                        val pixels = ScreenUtils.getFrameBufferPixmap(0, 0, width, height)
                        val bytes = ByteArray(width * height * 4)
                        try {
                            pixels.pixels.rewind()
                            pixels.pixels.get(bytes)
                        } finally {
                            pixels.dispose()
                        }
                        val fileName = "game-prefix-${prefixLength.toString().padStart(2, '0')}.rgba"
                        val capture = JsonValue(JsonValue.ValueType.`object`)
                        capture.addChild("prefixLength", JsonValue(prefixLength.toLong()))
                        capture.addChild("text", JsonValue(probe.dialogueVisibleText))
                        capture.addChild("speakerId", JsonValue(requireNotNull(probe.dialogueSpeakerId)))
                        capture.addChild("frame", JsonValue(frame.toLong()))
                        capture.addChild("firstObservedFrame", JsonValue(frame.toLong()))
                        capture.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
                        capture.addChild("delta", JsonValue(delta.toDouble()))
                        capture.addChild("observationPhase", JsonValue("post-render"))
                        capture.addChild("complete", JsonValue(probe.dialogueTextComplete))
                        capture.addChild("portraitId", JsonValue(portraitId.toLong()))
                        capture.addChild("portraitReady", JsonValue(portraitReady[portraitId] == true))
                        capture.addChild("actors", actors(game, probe))
                        capture.addChild("file", JsonValue(fileName))
                        capture.addChild("width", JsonValue(width.toLong()))
                        capture.addChild("height", JsonValue(height.toLong()))
                        captures.addChild(capture)
                        pendingPixels += capture to bytes
                        nextPrefixLength++

                        if (nextPrefixLength > fullText.length) {
                            check(captures.size == fullText.length + 1)
                            pendingPixels.forEach { (pendingCapture, pendingBytes) ->
                                File(directory, pendingCapture.getString("file")).writeBytes(pendingBytes)
                                pendingCapture.addChild("sha256", JsonValue(sha256(pendingBytes)))
                            }
                            val report = JsonValue(JsonValue.ValueType.`object`)
                            report.addChild("contract", JsonValue("natural-third-dialogue-full-prefixes-rgba8"))
                            report.addChild("width", JsonValue(width.toLong()))
                            report.addChild("height", JsonValue(height.toLong()))
                            report.addChild("origin", JsonValue("bottom-left"))
                            report.addChild("fullText", JsonValue(fullText))
                            val sampleLengths = JsonValue(JsonValue.ValueType.array)
                            (0..fullText.length).forEach { sampleLengths.addChild(JsonValue(it.toLong())) }
                            report.addChild("sampleLengths", sampleLengths)
                            report.addChild("dialogueInputs", JsonValue(dialogueInputs.toLong()))
                            report.addChild("isolation", JsonValue(false))
                            report.addChild("clockMode", JsonValue("natural"))
                            report.addChild("observationPhase", JsonValue("post-render"))
                            report.addChild("firstDialogueFrames", firstDialogueFrames)
                            report.addChild("captures", captures)
                            report.addChild("inputs", inputs)
                            report.addChild("dialogueCompletions", dialogueCompletions)
                            File(directory, "game-third-prefixes.json").writeText(
                                report.prettyPrint(JsonWriter.OutputType.json, 120),
                            )
                            finished = true
                            Gdx.app.log("JojoGame", "OPENING_THIRD_PREFIXES_COMPLETE frame=$frame")
                            Gdx.app.exit()
                        }
                    }
                }
            }
        }
        game = JojoGame(
            GameLaunchConfiguration(
                entryPoint = GameEntryPoint.SCENARIO,
                initialScenario = "R_00",
                initialScenarioExplicit = true,
                runtimeScreenObserver = observer,
                automatedRun = true,
            ),
        )
        val window = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo third opening dialogue prefixes verification")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
            useVsync(true)
        }
        Lwjgl3Application(game, window)
    }

    private fun installPortraitObserver(game: JojoGame, ready: MutableMap<Int, Boolean>) {
        val assets = game.screen.javaClass.getDeclaredField("sceneAssets").let {
            it.isAccessible = true
            it.get(game.screen)
        }
        val callback: (Int, TextureRegion?, Boolean) -> Unit = { id, region, _ ->
            ready[id] = region != null
        }
        assets.javaClass.getDeclaredField("portraitRegionObserver").let {
            it.isAccessible = true
            it.set(assets, callback)
        }
    }

    private fun dialoguePortraitId(game: JojoGame, characterId: Int): Int = game.screen.javaClass
        .getDeclaredMethod("dialoguePortraitId", Int::class.javaPrimitiveType)
        .let { it.isAccessible = true; it.invoke(game.screen, characterId) as Int }

    private fun actors(game: JojoGame, probe: ScenarioRuntimeProbe): JsonValue {
        val rows = JsonValue(JsonValue.ValueType.array)
        probe.actors.forEach { actor -> rows.addChild(actor(game, probe, actor.id)) }
        return rows
    }

    private fun actor(game: JojoGame, probe: ScenarioRuntimeProbe, actorId: Int): JsonValue {
        val screen = game.screen
        val view = screen.javaClass
            .getDeclaredMethod("battlefieldView", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
            .let { it.isAccessible = true; it.invoke(screen, true, true) }
        val renderUnits = (view.javaClass.getDeclaredMethod("getUnits").invoke(view) as List<*>)
            .filterNotNull()
            .associateBy { field(it, "id") as Int }
        val actor = requireNotNull(probe.actors.firstOrNull { it.id == actorId })
        val renderUnit = requireNotNull(renderUnits[actor.id])
        val row = JsonValue(JsonValue.ValueType.`object`)
        row.addChild("id", JsonValue(actor.id.toLong()))
        val logical = JsonValue(JsonValue.ValueType.array)
        logical.addChild(JsonValue(actor.x.toLong()))
        logical.addChild(JsonValue(actor.y.toLong()))
        row.addChild("logical", logical)
        row.addChild("direction", JsonValue(actor.direction.toLong()))
        row.addChild("action", JsonValue(actor.action.toLong()))
        row.addChild("frameRow", JsonValue((field(renderUnit, "frameRow") as Number).toLong()))
        row.addChild("textureAssetId", JsonValue((field(renderUnit, "textureAssetId") as Number).toLong()))
        row.addChild("flipX", JsonValue(field(renderUnit, "flipX") as Boolean))
        row.addChild("showSpeechBubble", JsonValue(field(renderUnit, "showSpeechBubble") as Boolean))
        return row
    }

    private fun field(instance: Any, name: String): Any = instance.javaClass.getDeclaredField(name).let {
        it.isAccessible = true
        it.get(instance)
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }
}
