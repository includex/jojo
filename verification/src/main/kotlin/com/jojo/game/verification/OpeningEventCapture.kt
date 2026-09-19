package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.application.runtime.RenderCaptureConfiguration
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState
import java.security.MessageDigest

/** Observe the actual opening event notice, without input or changing its closing timer. */
internal class OpeningEventCapture(output: RenderCaptureConfiguration) {
    private val directory = Gdx.files.absolute(requireNotNull(output.rawCapturePath)).parent()
    private val expected = "재능의 첫 징후"
    private val manifest = JsonValue(JsonValue.ValueType.`object`)
    private val observed = linkedSetOf<String>()
    private val prefixTexts = linkedSetOf<String>()
    private val prefixes = JsonValue(JsonValue.ValueType.array)
    private val capturePrefixes = System.getenv("JOJO_CAPTURE_EVENT_PREFIXES") == "1"
    private var frame = 0
    private var fullCaptured = false
    private var finished = false

    fun onFrame(probe: ScenarioRuntimeProbe) {
        if (finished) return
        frame++
        check(probe.elapsedSeconds < 10f) { "Opening event capture timed out" }
        if (probe.playback != PlaybackState.MODAL) return
        check(probe.module == "R_00" && probe.sceneIndex == 1 && probe.modalKind == "EVENT" && probe.modalText == expected)
        check(expected.startsWith(probe.modalVisibleText))
        observed.add(probe.modalVisibleText)
        if (capturePrefixes && probe.modalVisibleText.isNotEmpty() && !probe.naturalModalIsolation && prefixTexts.add(probe.modalVisibleText)) {
            val width = Gdx.graphics.backBufferWidth
            val height = Gdx.graphics.backBufferHeight
            check(width == 2560 && height == 1376)
            val pixels = ScreenUtils.getFrameBufferPixmap(0, 0, width, height)
            val bytes = ByteArray(width * height * 4)
            try { pixels.pixels.rewind(); pixels.pixels.get(bytes) } finally { pixels.dispose() }
            directory.mkdirs()
            val file = "game-event-prefix-${prefixTexts.size}.rgba"
            directory.child(file).writeBytes(bytes, false)
            val prefix = JsonValue(JsonValue.ValueType.`object`)
            prefix.addChild("file", JsonValue(file))
            prefix.addChild("sha256", JsonValue(MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }))
            prefix.addChild("text", JsonValue(probe.modalVisibleText))
            prefix.addChild("complete", JsonValue(probe.modalTextComplete))
            prefix.addChild("naturalModalIsolation", JsonValue(probe.naturalModalIsolation))
            prefix.addChild("width", JsonValue(width.toLong())); prefix.addChild("height", JsonValue(height.toLong()))
            prefix.addChild("frame", JsonValue(frame.toLong())); prefix.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
            prefixes.addChild(prefix)
        }
        if (!probe.modalTextComplete) return
        check(probe.modalVisibleText == expected)
        val isolated = probe.naturalModalIsolation
        check(isolated == fullCaptured) { "Expected full event frame before isolated frame" }
        val width = Gdx.graphics.backBufferWidth
        val height = Gdx.graphics.backBufferHeight
        check(width == 2560 && height == 1376)
        val pixels = ScreenUtils.getFrameBufferPixmap(0, 0, width, height)
        val bytes = ByteArray(width * height * 4)
        try { pixels.pixels.rewind(); pixels.pixels.get(bytes) } finally { pixels.dispose() }
        directory.mkdirs()
        val file = if (isolated) "game-event-isolated.rgba" else "game-event-full.rgba"
        directory.child(file).writeBytes(bytes, false)
        val row = JsonValue(JsonValue.ValueType.`object`)
        row.addChild("file", JsonValue(file))
        row.addChild("sha256", JsonValue(MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }))
        row.addChild("frame", JsonValue(frame.toLong()))
        row.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
        row.addChild("text", JsonValue(probe.modalVisibleText))
        row.addChild("complete", JsonValue(probe.modalTextComplete))
        row.addChild("naturalModalIsolation", JsonValue(isolated))
        manifest.addChild(if (isolated) "isolatedOverlay" else "fullFrame", row)
        Gdx.app.log("JojoGame", "OPENING_EVENT_CAPTURE: isolated=$isolated frame=$frame text=${probe.modalVisibleText}")
        if (!isolated) {
            if (System.getenv("JOJO_CAPTURE_BACKGROUND_TEXTURE") == "1") OpeningBackgroundCapture.capture(directory, probe.backgroundId)
            if (System.getenv("JOJO_CAPTURE_PANEL_TEXTURE") == "1") OpeningPanelTextureCapture.capture(directory)
            fullCaptured = true
            return
        }
        manifest.addChild("contract", JsonValue("natural-opening-event-rgba8"))
        manifest.addChild("width", JsonValue(width.toLong()))
        manifest.addChild("height", JsonValue(height.toLong()))
        manifest.addChild("origin", JsonValue("bottom-left"))
        manifest.addChild("eventText", JsonValue(expected))
        manifest.addChild("dialogueInputs", JsonValue(0L))
        val strings = JsonValue(JsonValue.ValueType.array)
        observed.forEach { strings.addChild(JsonValue(it)) }
        manifest.addChild("observedStrings", strings)
        if (capturePrefixes) {
            check(prefixTexts.toList() == (1..expected.length).map { expected.take(it) }) { "Natural event prefix capture missed a string: $prefixTexts" }
            manifest.addChild("prefixes", prefixes)
        }
        val clear = JsonValue(JsonValue.ValueType.array)
        listOf(0, 0, 0, 1).forEach { clear.addChild(JsonValue(it.toLong())) }
        row.addChild("clearColor", clear)
        directory.child("game-event.json").writeString(manifest.prettyPrint(JsonWriter.OutputType.json, 120), false)
        finished = true
        Gdx.app.exit()
    }
}
