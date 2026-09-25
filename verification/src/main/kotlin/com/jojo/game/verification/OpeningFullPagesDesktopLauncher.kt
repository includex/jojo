package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.infrastructure.data.ScenarioDialogueTextCatalog
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.GameEntryPoint
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState
import java.io.File
import java.security.MessageDigest

/** Captures the first three naturally completed Hall dialogue pages with the full scene visible. */
object OpeningFullPagesDesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val directory = File(args.single()).also { it.mkdirs() }
        directory.listFiles()?.forEach(File::delete)
        val expectedTexts = listOf(ScenarioDialogueTextCatalog.text("opening_scene1_page1"), ScenarioDialogueTextCatalog.text("opening_scene1_page2"), ScenarioDialogueTextCatalog.text("opening_scene1_page3"))
        val expectedSpeakers = listOf("181", "0", "157")
        val captures = JsonValue(JsonValue.ValueType.array)
        val inputs = JsonValue(JsonValue.ValueType.array)
        val partialPrefixObserved = BooleanArray(expectedTexts.size)
        val observedStrings = Array(expectedTexts.size) { linkedSetOf<String>() }
        var frame = 0
        var pageIndex = 0
        var inputCount = 0
        var advanceAfterCapture = false
        var finished = false

        val observer = RuntimeScreenObserver { _, probe ->
            if (!finished && probe is ScenarioRuntimeProbe) {
                frame++
                check(frame < 900 && probe.elapsedSeconds < 12f)
                check(probe.module == "R_00" && probe.sceneIndex == 1)
                if (probe.playback == PlaybackState.DIALOGUE) {
                    check(pageIndex in expectedTexts.indices)
                    val expectedText = expectedTexts[pageIndex]
                    check(probe.dialogueSpeakerId == expectedSpeakers[pageIndex])
                    check(expectedText.startsWith(probe.dialogueVisibleText))
                    observedStrings[pageIndex].add(probe.dialogueVisibleText)
                    if (!probe.dialogueTextComplete && probe.dialogueVisibleText.isNotEmpty()) {
                        partialPrefixObserved[pageIndex] = true
                    }

                    if (advanceAfterCapture) {
                        check(probe.dialogueTextComplete && probe.dialogueVisibleText == expectedText)
                        val event = JsonValue(JsonValue.ValueType.`object`)
                        event.addChild("frame", JsonValue(frame.toLong()))
                        event.addChild("afterPage", JsonValue((pageIndex + 1).toLong()))
                        event.addChild("kind", JsonValue("InputProcessor.keyDown/keyUp(SPACE)"))
                        event.addChild("completeBeforeInput", JsonValue(true))
                        event.addChild("textBeforeInput", JsonValue(probe.dialogueVisibleText))
                        event.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
                        val processor = checkNotNull(Gdx.input.inputProcessor)
                        check(processor.keyDown(Input.Keys.SPACE))
                        processor.keyUp(Input.Keys.SPACE)
                        inputs.addChild(event)
                        inputCount++
                        pageIndex++
                        advanceAfterCapture = false
                    } else if (probe.dialogueTextComplete) {
                        check(probe.dialogueVisibleText == expectedText)
                        check(partialPrefixObserved[pageIndex])
                        check(!probe.naturalStreetTextIsolation)
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
                        val fileName = "game-page-${pageIndex + 1}.rgba"
                        File(directory, fileName).writeBytes(bytes)
                        val capture = JsonValue(JsonValue.ValueType.`object`)
                        capture.addChild("page", JsonValue((pageIndex + 1).toLong()))
                        capture.addChild("file", JsonValue(fileName))
                        capture.addChild("sha256", JsonValue(sha256(bytes)))
                        capture.addChild("width", JsonValue(width.toLong()))
                        capture.addChild("height", JsonValue(height.toLong()))
                        capture.addChild("origin", JsonValue("bottom-left"))
                        capture.addChild("text", JsonValue(probe.dialogueVisibleText))
                        capture.addChild("speakerId", JsonValue(requireNotNull(probe.dialogueSpeakerId)))
                        capture.addChild("revision", JsonValue(probe.dialogueRevision))
                        capture.addChild("side", JsonValue(probe.dialogueSide.toLong()))
                        capture.addChild("complete", JsonValue(true))
                        capture.addChild("dialogueInputs", JsonValue(inputCount.toLong()))
                        capture.addChild("frame", JsonValue(frame.toLong()))
                        capture.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
                        capture.addChild("partialPrefixObserved", JsonValue(true))
                        val strings = JsonValue(JsonValue.ValueType.array)
                        observedStrings[pageIndex].forEach { strings.addChild(JsonValue(it)) }
                        capture.addChild("observedStrings", strings)
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
                        capture.addChild("actors", actors)
                        captures.addChild(capture)

                        if (pageIndex < expectedTexts.lastIndex) {
                            advanceAfterCapture = true
                        } else {
                            check(inputCount == 2 && inputs.size == 2 && captures.size == 3)
                            val report = JsonValue(JsonValue.ValueType.`object`)
                            report.addChild("contract", JsonValue("natural-opening-full-pages-rgba8"))
                            report.addChild("width", JsonValue(width.toLong()))
                            report.addChild("height", JsonValue(height.toLong()))
                            report.addChild("origin", JsonValue("bottom-left"))
                            report.addChild("dialogueInputs", JsonValue(inputCount.toLong()))
                            report.addChild("isolation", JsonValue(false))
                            report.addChild("captures", captures)
                            report.addChild("inputs", inputs)
                            File(directory, "game-full-pages.json").writeText(
                                report.prettyPrint(JsonWriter.OutputType.json, 120),
                            )
                            finished = true
                            Gdx.app.log("JojoGame", "OPENING_FULL_PAGES_COMPLETE frame=$frame")
                            Gdx.app.exit()
                        }
                    }
                }
            }
        }
        val game = JojoGame(
            GameLaunchConfiguration(
                entryPoint = GameEntryPoint.SCENARIO,
                initialScenario = "R_00",
                initialScenarioExplicit = true,
                runtimeScreenObserver = observer,
                automatedRun = true,
            ),
        )
        val window = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo full opening pages verification")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
            useVsync(true)
        }
        Lwjgl3Application(game, window)
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }
}
