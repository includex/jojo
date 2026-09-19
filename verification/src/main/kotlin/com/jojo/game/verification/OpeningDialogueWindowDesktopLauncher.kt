package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.JsonReader
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

/** Captures a configured window of naturally completed opening dialogue pages. */
object OpeningDialogueWindowDesktopLauncher {
    private data class ExpectedPage(val page: Int, val speakerId: String, val text: String)
    private data class Case(
        val contract: String,
        val scenario: String,
        val pages: List<ExpectedPage>,
        val capturePages: Set<Int>,
    )

    @JvmStatic
    fun main(args: Array<String>) {
        require(args.size == 2) { "usage: OpeningDialogueWindowDesktopLauncher CASE_JSON OUTPUT_DIR" }
        val caseFile = File(args[0]).also { require(it.isFile) { "case JSON missing: $it" } }
        val case = readCase(caseFile)
        val directory = File(args[1]).also { it.mkdirs() }
        directory.listFiles()?.forEach(File::delete)

        val captures = JsonValue(JsonValue.ValueType.array)
        val dialogueCompletions = JsonValue(JsonValue.ValueType.array)
        val inputs = JsonValue(JsonValue.ValueType.array)
        val pendingPixels = mutableListOf<Pair<JsonValue, ByteArray>>()
        val portraitReady = mutableMapOf<Int, Boolean>()
        var portraitObserverInstalled = false
        var nextPageIndex = 0
        var frame = 0
        var finished = false
        lateinit var game: JojoGame

        val observer = RuntimeScreenObserver { delta, probe ->
            if (!finished && probe is ScenarioRuntimeProbe) {
                frame++
                check(frame < 1800 && probe.elapsedSeconds < 30f) { "dialogue window timed out" }
                check(probe.module == case.scenario && probe.sceneIndex == 1)
                if (!portraitObserverInstalled) {
                    installPortraitObserver(game, portraitReady)
                    portraitObserverInstalled = true
                }
                if (probe.playback == PlaybackState.DIALOGUE && probe.dialogueTextComplete) {
                    val expected = case.pages.getOrNull(nextPageIndex)
                        ?: error("unexpected completed dialogue after page ${case.pages.last().page}")
                    check(probe.dialogueSpeakerId == expected.speakerId) {
                        "page ${expected.page} speaker ${probe.dialogueSpeakerId}, expected ${expected.speakerId}"
                    }
                    check(probe.dialogueVisibleText == expected.text) {
                        "page ${expected.page} text ${probe.dialogueVisibleText}, expected ${expected.text}"
                    }
                    val observedSpeakerId = requireNotNull(probe.dialogueSpeakerId)
                    val observedText = probe.dialogueVisibleText
                    val portraitId = dialoguePortraitId(game, observedSpeakerId.toInt())
                    val actorRows = actors(game, probe)
                    val completion = JsonValue(JsonValue.ValueType.`object`).apply {
                        addChild("page", JsonValue(expected.page.toLong()))
                        addChild("speakerId", JsonValue(observedSpeakerId))
                        addChild("text", JsonValue(observedText))
                        addChild("frame", JsonValue(frame.toLong()))
                        addChild("firstObservedFrame", JsonValue(frame.toLong()))
                        addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
                        addChild("delta", JsonValue(delta.toDouble()))
                        addChild("observationPhase", JsonValue("post-render"))
                        addChild("complete", JsonValue(true))
                        addChild("portraitId", JsonValue(portraitId.toLong()))
                        addChild("portraitReady", JsonValue(portraitReady[portraitId] == true))
                        addChild("actors", actorRows)
                    }
                    dialogueCompletions.addChild(completion)

                    if (expected.page in case.capturePages) {
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
                        val fileName = "game-page-${expected.page}.rgba"
                        val capture = JsonValue(JsonValue.ValueType.`object`).apply {
                            addChild("page", JsonValue(expected.page.toLong()))
                            addChild("speakerId", JsonValue(observedSpeakerId))
                            addChild("text", JsonValue(observedText))
                            addChild("frame", JsonValue(frame.toLong()))
                            addChild("firstObservedFrame", JsonValue(frame.toLong()))
                            addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
                            addChild("delta", JsonValue(delta.toDouble()))
                            addChild("observationPhase", JsonValue("post-render"))
                            addChild("complete", JsonValue(true))
                            addChild("portraitId", JsonValue(portraitId.toLong()))
                            addChild("portraitReady", JsonValue(portraitReady[portraitId] == true))
                            addChild("actors", actors(game, probe))
                            addChild("file", JsonValue(fileName))
                            addChild("width", JsonValue(width.toLong()))
                            addChild("height", JsonValue(height.toLong()))
                        }
                        captures.addChild(capture)
                        pendingPixels += capture to bytes
                    }

                    nextPageIndex++
                    if (nextPageIndex < case.pages.size) {
                        val event = JsonValue(JsonValue.ValueType.`object`).apply {
                            addChild("afterPage", JsonValue(expected.page.toLong()))
                            addChild("frame", JsonValue(frame.toLong()))
                            addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
                            addChild("completeBeforeInput", JsonValue(true))
                            addChild("textBeforeInput", JsonValue(probe.dialogueVisibleText))
                            addChild("speakerId", JsonValue(observedSpeakerId))
                            addChild("kind", JsonValue("InputProcessor.keyDown/keyUp(SPACE)"))
                        }
                        val processor = checkNotNull(Gdx.input.inputProcessor)
                        check(processor.keyDown(Input.Keys.SPACE))
                        processor.keyUp(Input.Keys.SPACE)
                        inputs.addChild(event)
                    } else {
                        check(captures.size == case.capturePages.size)
                        check(inputs.size == case.pages.size - 1)
                        pendingPixels.forEach { (capture, bytes) ->
                            File(directory, capture.getString("file")).writeBytes(bytes)
                            capture.addChild("sha256", JsonValue(sha256(bytes)))
                        }
                        val report = JsonValue(JsonValue.ValueType.`object`).apply {
                            addChild("contract", JsonValue("natural-opening-dialogue-window-rgba8"))
                            addChild("caseContract", JsonValue(case.contract))
                            addChild("caseFile", JsonValue(caseFile.absolutePath))
                            addChild("caseSha256", JsonValue(sha256(caseFile.readBytes())))
                            addChild("scenario", JsonValue(case.scenario))
                            addChild("width", JsonValue(2560L))
                            addChild("height", JsonValue(1376L))
                            addChild("origin", JsonValue("bottom-left"))
                            addChild("clockMode", JsonValue("natural"))
                            addChild("isolation", JsonValue(false))
                            addChild("observationPhase", JsonValue("post-render"))
                            addChild("dialogueInputs", JsonValue(inputs.size.toLong()))
                            addChild("capturePages", intArray(case.capturePages))
                            addChild("dialogueCompletions", dialogueCompletions)
                            addChild("inputs", inputs)
                            addChild("captures", captures)
                        }
                        File(directory, "game-dialogue-window.json").writeText(
                            report.prettyPrint(JsonWriter.OutputType.json, 120),
                        )
                        finished = true
                        Gdx.app.log("JojoGame", "OPENING_DIALOGUE_WINDOW_COMPLETE frame=$frame")
                        Gdx.app.exit()
                    }
                }
            }
        }
        game = JojoGame(
            GameLaunchConfiguration(
                entryPoint = GameEntryPoint.SCENARIO,
                initialScenario = case.scenario,
                initialScenarioExplicit = true,
                runtimeScreenObserver = observer,
                automatedRun = true,
            ),
        )
        val window = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo opening dialogue window verification")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
            useVsync(true)
        }
        Lwjgl3Application(game, window)
    }

    private fun readCase(file: File): Case {
        val root = JsonReader().parse(file.readText())
        val contract = root.getString("contract")
        require(contract == "opening-dialogue-window-case-v1") { "unsupported case contract: $contract" }
        val scenario = root.getString("scenario", "R_00")
        val pages = root.requireArray("expectedPages").map { row ->
            ExpectedPage(row.getInt("page"), row.getString("speakerId"), row.getString("text"))
        }
        require(pages.isNotEmpty() && pages.map(ExpectedPage::page) == (1..pages.size).toList()) {
            "case expectedPages must be ordered and contiguous from 1"
        }
        require(pages.all { it.speakerId.toIntOrNull() != null && it.text.isNotEmpty() })
        val capturePageList = root.requireArray("capturePages").map(JsonValue::asInt)
        val capturePages = capturePageList.toSet()
        require(capturePages.isNotEmpty() && capturePages.size == capturePageList.size) {
            "capturePages must be nonempty and unique"
        }
        require(capturePages.all { it in 1..pages.size }) { "capturePages must refer to expectedPages" }
        return Case(contract, scenario, pages, capturePages)
    }

    private fun JsonValue.requireArray(name: String): List<JsonValue> {
        val value = get(name) ?: error("missing case field: $name")
        require(value.isArray) { "$name must be an array" }
        return generateSequence(value.child) { it.next }.toList()
    }

    private fun intArray(values: Collection<Int>): JsonValue = JsonValue(JsonValue.ValueType.array).also { array ->
        values.sorted().forEach { array.addChild(JsonValue(it.toLong())) }
    }

    private fun installPortraitObserver(game: JojoGame, ready: MutableMap<Int, Boolean>) {
        val assets = game.screen.javaClass.getDeclaredField("sceneAssets").let {
            it.isAccessible = true
            it.get(game.screen)
        }
        val callback: (Int, TextureRegion?, Boolean) -> Unit = { id, region, _ -> ready[id] = region != null }
        assets.javaClass.getDeclaredField("portraitRegionObserver").let {
            it.isAccessible = true
            it.set(assets, callback)
        }
    }

    private fun dialoguePortraitId(game: JojoGame, characterId: Int): Int = game.screen.javaClass
        .getDeclaredMethod("dialoguePortraitId", Int::class.javaPrimitiveType)
        .let { it.isAccessible = true; it.invoke(game.screen, characterId) as Int }

    private fun actors(game: JojoGame, probe: ScenarioRuntimeProbe): JsonValue {
        val view = game.screen.javaClass
            .getDeclaredMethod("battlefieldView", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
            .let { it.isAccessible = true; it.invoke(game.screen, true, true) }
        val renderUnits = (view.javaClass.getDeclaredMethod("getUnits").invoke(view) as List<*>)
            .filterNotNull()
            .associateBy { field(it, "id") as Int }
        return JsonValue(JsonValue.ValueType.array).also { rows ->
            probe.actors.sortedBy { it.id }.forEach { actor ->
                val renderUnit = requireNotNull(renderUnits[actor.id])
                rows.addChild(JsonValue(JsonValue.ValueType.`object`).apply {
                    addChild("id", JsonValue(actor.id.toLong()))
                    addChild("logical", JsonValue(JsonValue.ValueType.array).also {
                        it.addChild(JsonValue(actor.x.toLong()))
                        it.addChild(JsonValue(actor.y.toLong()))
                    })
                    addChild("visualX", JsonValue((field(renderUnit, "visualX") as Number).toDouble()))
                    addChild("visualY", JsonValue((field(renderUnit, "visualY") as Number).toDouble()))
                    addChild("action", JsonValue(actor.action.toLong()))
                    addChild("direction", JsonValue(actor.direction.toLong()))
                    addChild("frameRow", JsonValue((field(renderUnit, "frameRow") as Number).toLong()))
                    addChild("textureAssetId", JsonValue((field(renderUnit, "textureAssetId") as Number).toLong()))
                    addChild("flipX", JsonValue(field(renderUnit, "flipX") as Boolean))
                    addChild("showSpeechBubble", JsonValue(field(renderUnit, "showSpeechBubble") as Boolean))
                })
            }
        }
    }

    private fun field(instance: Any, name: String): Any = instance.javaClass.getDeclaredField(name).let {
        it.isAccessible = true
        it.get(instance)
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }
}
