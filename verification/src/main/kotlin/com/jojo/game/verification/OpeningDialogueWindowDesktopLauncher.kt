package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.BufferUtils
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
    private data class CapturePrefix(val page: Int, val length: Int)
    private data class Case(
        val contract: String,
        val scenario: String,
        val pages: List<ExpectedPage>,
        val capturePages: Set<Int>,
        val capturePrefixes: Set<CapturePrefix>,
    )

    @JvmStatic
    fun main(args: Array<String>) {
        require(args.size == 2) { "usage: OpeningDialogueWindowDesktopLauncher CASE_JSON OUTPUT_DIR" }
        val caseFile = File(args[0]).also { require(it.isFile) { "case JSON missing: $it" } }
        val case = readCase(caseFile)
        val bodyDiagnosticsEnabled = java.lang.Boolean.getBoolean("jojo.dialogueWindow.bodyDiagnostics")
        val directory = File(args[1]).also { it.mkdirs() }
        directory.listFiles()?.forEach(File::delete)

        val captures = JsonValue(JsonValue.ValueType.array)
        val prefixObservations = JsonValue(JsonValue.ValueType.array)
        val prefixCaptures = JsonValue(JsonValue.ValueType.array)
        val bodyDiagnostics = JsonValue(JsonValue.ValueType.array)
        val dialogueCompletions = JsonValue(JsonValue.ValueType.array)
        val inputs = JsonValue(JsonValue.ValueType.array)
        val pendingPixels = mutableListOf<Pair<JsonValue, ByteArray>>()
        val portraitReady = mutableMapOf<Int, Boolean>()
        val observedPrefixes = mutableMapOf<Int, MutableSet<String>>()
        val pageRevisions = mutableMapOf<Int, Long>()
        val prefixPages = case.capturePrefixes.map(CapturePrefix::page).toSet()
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
                if (probe.playback == PlaybackState.DIALOGUE) {
                    val expected = case.pages.getOrNull(nextPageIndex)
                        ?: error("unexpected dialogue after page ${case.pages.last().page}")
                    check(probe.dialogueSpeakerId == expected.speakerId) {
                        "page ${expected.page} speaker ${probe.dialogueSpeakerId}, expected ${expected.speakerId}"
                    }
                    check(expected.text.startsWith(probe.dialogueVisibleText)) {
                        "page ${expected.page} prefix ${probe.dialogueVisibleText} is not from ${expected.text}"
                    }
                    val observedSpeakerId = requireNotNull(probe.dialogueSpeakerId)
                    val observedText = probe.dialogueVisibleText
                    val observedFullText = requireNotNull(probe.dialogueText)
                    check(observedFullText == expected.text) {
                        "page ${expected.page} active full text $observedFullText, expected ${expected.text}"
                    }
                    val portraitId = dialoguePortraitId(game, observedSpeakerId.toInt())
                    val revision = probe.dialogueRevision
                    pageRevisions[expected.page]?.let { check(it == revision) { "page ${expected.page} revision changed" } }
                        ?: run {
                            check(revision !in pageRevisions.values) { "dialogue revision $revision reused by page ${expected.page}" }
                            pageRevisions[expected.page] = revision
                        }

                    if (expected.page in prefixPages && observedPrefixes
                            .getOrPut(expected.page) { linkedSetOf() }
                            .add(observedText)
                    ) {
                        val observation = prefixRow(
                            game, probe, expected.page, observedSpeakerId, observedText, revision,
                            frame, delta, portraitId, portraitReady[portraitId] == true, inputs.size, observedFullText,
                        )
                        prefixObservations.addChild(observation)
                        if (CapturePrefix(expected.page, observedText.length) in case.capturePrefixes) {
                            check(observedText == expected.text.take(observedText.length))
                            val (width, height, bytes) = frameBuffer()
                            val fileName = "game-page-${expected.page}-prefix-${observedText.length.toString().padStart(3, '0')}.rgba"
                            val capture = prefixRow(
                                game, probe, expected.page, observedSpeakerId, observedText, revision,
                                frame, delta, portraitId, portraitReady[portraitId] == true, inputs.size, observedFullText,
                            ).apply {
                                addChild("file", JsonValue(fileName))
                                addChild("width", JsonValue(width.toLong()))
                                addChild("height", JsonValue(height.toLong()))
                            }
                            prefixCaptures.addChild(capture)
                            pendingPixels += capture to bytes
                            if (bodyDiagnosticsEnabled) {
                                bodyDiagnostics.addChild(
                                    captureBodyDiagnostics(game, probe, expected.page, observedText, revision, pendingPixels),
                                )
                            }
                        }
                    }

                    if (!probe.dialogueTextComplete) return@RuntimeScreenObserver
                    check(observedText == expected.text) {
                        "page ${expected.page} completed text $observedText, expected ${expected.text}"
                    }
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
                        addChild("revision", JsonValue(revision))
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
                            addChild("revision", JsonValue(revision))
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
                            addChild("revision", JsonValue(revision))
                            addChild("kind", JsonValue("InputProcessor.keyDown/keyUp(SPACE)"))
                        }
                        val processor = checkNotNull(Gdx.input.inputProcessor)
                        check(processor.keyDown(Input.Keys.SPACE))
                        processor.keyUp(Input.Keys.SPACE)
                        inputs.addChild(event)
                    } else {
                        check(captures.size == case.capturePages.size)
                        check(prefixCaptures.size == case.capturePrefixes.size)
                        prefixPages.forEach { page ->
                            check(observedPrefixes[page]?.firstOrNull() == "") { "page $page empty prefix was not observed" }
                        }
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
                            addChild("capturePrefixes", capturePrefixArray(case.capturePrefixes))
                            addChild("dialogueCompletions", dialogueCompletions)
                            addChild("inputs", inputs)
                            addChild("captures", captures)
                            addChild("prefixObservations", prefixObservations)
                            addChild("prefixCaptures", prefixCaptures)
                            addChild("bodyDiagnosticsEnabled", JsonValue(bodyDiagnosticsEnabled))
                            addChild("bodyDiagnostics", bodyDiagnostics)
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
        val capturePrefixRows = root.get("capturePrefixes")?.let { value ->
            require(value.isArray) { "capturePrefixes must be an array" }
            generateSequence(value.child) { it.next }.toList()
        }.orEmpty()
        val capturePrefixList = capturePrefixRows.map { CapturePrefix(it.getInt("page"), it.getInt("length")) }
        val capturePrefixes = capturePrefixList.toSet()
        require(capturePrefixes.size == capturePrefixList.size) { "capturePrefixes must be unique" }
        capturePrefixes.forEach { prefix ->
            require(prefix.page in 1..pages.size) { "capture prefix page must refer to expectedPages" }
            require(prefix.length in 0 until pages[prefix.page - 1].text.length) {
                "capture prefix length must be before the completed page text"
            }
        }
        return Case(contract, scenario, pages, capturePages, capturePrefixes)
    }

    private fun JsonValue.requireArray(name: String): List<JsonValue> {
        val value = get(name) ?: error("missing case field: $name")
        require(value.isArray) { "$name must be an array" }
        return generateSequence(value.child) { it.next }.toList()
    }

    private fun intArray(values: Collection<Int>): JsonValue = JsonValue(JsonValue.ValueType.array).also { array ->
        values.sorted().forEach { array.addChild(JsonValue(it.toLong())) }
    }

    private fun capturePrefixArray(values: Collection<CapturePrefix>): JsonValue =
        JsonValue(JsonValue.ValueType.array).also { array ->
            values.sortedWith(compareBy(CapturePrefix::page, CapturePrefix::length)).forEach { value ->
                array.addChild(JsonValue(JsonValue.ValueType.`object`).apply {
                    addChild("page", JsonValue(value.page.toLong()))
                    addChild("length", JsonValue(value.length.toLong()))
                })
            }
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

    private fun prefixRow(
        game: JojoGame,
        probe: ScenarioRuntimeProbe,
        page: Int,
        speakerId: String,
        text: String,
        revision: Long,
        frame: Int,
        delta: Float,
        portraitId: Int,
        portraitReady: Boolean,
        dialogueInputs: Int,
        fullText: String,
    ): JsonValue = JsonValue(JsonValue.ValueType.`object`).apply {
        addChild("page", JsonValue(page.toLong()))
        addChild("length", JsonValue(text.length.toLong()))
        addChild("prefixLength", JsonValue(text.length.toLong()))
        addChild("speakerId", JsonValue(speakerId))
        addChild("text", JsonValue(text))
        addChild("fullText", JsonValue(fullText))
        addChild("revision", JsonValue(revision))
        addChild("frame", JsonValue(frame.toLong()))
        addChild("firstObservedFrame", JsonValue(frame.toLong()))
        addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
        addChild("delta", JsonValue(delta.toDouble()))
        addChild("observationPhase", JsonValue("post-render"))
        addChild("complete", JsonValue(probe.dialogueTextComplete))
        addChild("dialogueInputs", JsonValue(dialogueInputs.toLong()))
        addChild("portraitId", JsonValue(portraitId.toLong()))
        addChild("portraitReady", JsonValue(portraitReady))
        addChild("actors", actors(game, probe))
    }

    private fun frameBuffer(): Triple<Int, Int, ByteArray> {
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
        return Triple(width, height, bytes)
    }

    private fun captureBodyDiagnostics(
        game: JojoGame,
        probe: ScenarioRuntimeProbe,
        page: Int,
        text: String,
        revision: Long,
        pendingPixels: MutableList<Pair<JsonValue, ByteArray>>,
    ): JsonValue {
        val screen = game.screen
        val sceneAssets = field(screen, "sceneAssets")
        val streetBodyLabels = field(sceneAssets, "streetBodyLabels")
        @Suppress("UNCHECKED_CAST")
        val layouts = field(streetBodyLabels, "layouts") as Map<String, List<Any>>
        val segments = requireNotNull(layouts[text]) { "body layout was not cached for prefix $text" }
        val overlayRenderer = Class.forName("com.jojo.game.presentation.scenario.render.ScenarioOverlayRenderer")
        val dialogueRenderer = overlayRenderer.getDeclaredField("dialogueRenderer").let {
            it.isAccessible = true
            it.get(null)
        }
        val layout = field(dialogueRenderer, "layout")
        val scale = (field(layout, "bodyLabelScale") as Number).toDouble()
        val panelY = (field(layout, "panelY") as Number).toFloat() +
            if (probe.dialogueAtTop) (field(layout, "topOffsetY") as Number).toFloat() else 0f
        val sourceX = (field(
            layout,
            if (probe.dialogueSide == 0) "bodyLabelLeftSourceX" else "bodyLabelRightSourceX",
        ) as Number).toDouble()
        val sourceTop = panelY.toDouble() / scale +
            (field(layout, "bodyLabelSourceTopOffsetY") as Number).toDouble()
        val batch = field(screen, "batch")
        val projection = field(batch, "projectionMatrix") as Matrix4
        val transform = field(batch, "transformMatrix") as Matrix4
        val sourceTransform = field(dialogueRenderer, "sourceBodyTransform") as Matrix4
        val actualLastVertices = (field(dialogueRenderer, "bodyVertices") as FloatArray).copyOf()

        val segmentRows = JsonValue(JsonValue.ValueType.array)
        segments.forEachIndexed { index, segment ->
            val texture = field(segment, "texture") as Texture
            val segmentX = (field(segment, "x") as Number).toDouble()
            val segmentY = (field(segment, "y") as Number).toDouble()
            val bytes = readTextureRgba(texture)
            val fileName = "game-page-$page-prefix-${text.length.toString().padStart(3, '0')}-segment-$index-${texture.width}x${texture.height}.rgba"
            val row = JsonValue(JsonValue.ValueType.`object`).apply {
                addChild("index", JsonValue(index.toLong()))
                addChild("x", JsonValue(segmentX))
                addChild("y", JsonValue(segmentY))
                addChild("textureHandle", JsonValue(texture.textureObjectHandle.toLong()))
                addChild("textureWidth", JsonValue(texture.width.toLong()))
                addChild("textureHeight", JsonValue(texture.height.toLong()))
                addChild("minFilter", JsonValue(texture.minFilter.name))
                addChild("magFilter", JsonValue(texture.magFilter.name))
                addChild("uv", floatArrayJson(floatArrayOf(0f, 0f, 1f, 1f)))
                addChild(
                    "computedSubmittedPositionUv",
                    floatArrayJson(bodyPositionUv(sourceX + segmentX, sourceTop + segmentY, texture.width, texture.height)),
                )
                addChild("file", JsonValue(fileName))
                addChild("width", JsonValue(texture.width.toLong()))
                addChild("height", JsonValue(texture.height.toLong()))
            }
            segmentRows.addChild(row)
            pendingPixels += row to bytes
        }
        return JsonValue(JsonValue.ValueType.`object`).apply {
            addChild("page", JsonValue(page.toLong()))
            addChild("length", JsonValue(text.length.toLong()))
            addChild("text", JsonValue(text))
            addChild("revision", JsonValue(revision))
            addChild("evidenceKind", JsonValue("cached-body-texture-fbo-and-renderer-reflection"))
            addChild("bodyLabelScale", JsonValue(scale))
            addChild("sourceX", JsonValue(sourceX))
            addChild("sourceTop", JsonValue(sourceTop))
            addChild("batchProjection", floatArrayJson(projection.`val`))
            addChild("batchTransformAfterRender", floatArrayJson(transform.`val`))
            addChild("sourceBodyTransform", floatArrayJson(sourceTransform.`val`))
            addChild("actualBodyPositionUvLastSegment", floatArrayJson(positionUv(actualLastVertices)))
            addChild("segments", segmentRows)
        }
    }

    /** Mirrors DialogueRenderer's submitted local quad layout for every cached segment. */
    private fun bodyPositionUv(x: Double, y: Double, width: Int, height: Int): FloatArray {
        val left = x.toFloat()
        val bottom = y.toFloat()
        val right = (x + width).toFloat()
        val top = (y + height).toFloat()
        return floatArrayOf(
            left, top, 0f, 0f,
            left, bottom, 0f, 1f,
            right, bottom, 1f, 1f,
            right, top, 1f, 0f,
        )
    }

    private fun positionUv(spriteBatchVertices: FloatArray): FloatArray = FloatArray(16).also { result ->
        repeat(4) { vertex ->
            result[vertex * 4] = spriteBatchVertices[vertex * 5]
            result[vertex * 4 + 1] = spriteBatchVertices[vertex * 5 + 1]
            result[vertex * 4 + 2] = spriteBatchVertices[vertex * 5 + 3]
            result[vertex * 4 + 3] = spriteBatchVertices[vertex * 5 + 4]
        }
    }

    private fun readTextureRgba(texture: Texture): ByteArray {
        val previousFramebuffer = BufferUtils.newIntBuffer(1)
        Gdx.gl.glGetIntegerv(GL20.GL_FRAMEBUFFER_BINDING, previousFramebuffer)
        val framebuffer = Gdx.gl.glGenFramebuffer()
        val bytes = ByteArray(texture.width * texture.height * 4)
        val pixels = BufferUtils.newByteBuffer(bytes.size)
        try {
            Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebuffer)
            Gdx.gl.glFramebufferTexture2D(
                GL20.GL_FRAMEBUFFER,
                GL20.GL_COLOR_ATTACHMENT0,
                GL20.GL_TEXTURE_2D,
                texture.textureObjectHandle,
                0,
            )
            check(Gdx.gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER) == GL20.GL_FRAMEBUFFER_COMPLETE)
            Gdx.gl.glReadPixels(0, 0, texture.width, texture.height, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels)
            pixels.rewind()
            pixels.get(bytes)
        } finally {
            Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, previousFramebuffer.get(0))
            Gdx.gl.glDeleteFramebuffer(framebuffer)
        }
        return bytes
    }

    private fun floatArrayJson(values: FloatArray): JsonValue = JsonValue(JsonValue.ValueType.array).also { array ->
        values.forEach { array.addChild(JsonValue(it.toDouble())) }
    }

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
