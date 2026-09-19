package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.application.runtime.RenderCaptureConfiguration
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState
import java.security.MessageDigest

/** Observes rendered natural prefixes; never sets text, advances input or stops the playback clock. */
internal class OpeningDialoguePrefixCapture(output: RenderCaptureConfiguration) {
    private val directory = Gdx.files.absolute(requireNotNull(output.rawCapturePath)).parent()
    private val fullText = JsonReader().parse(Gdx.files.internal("street-body-labels/manifest.json"))
        .get("catalog").get("pages").getString(0)
    private val sampleLengths = if (output.state == "opening-prefixes-all") (1..fullText.length).toList()
        else listOf(5, 9, fullText.length)
    private val expected = sampleLengths.map { fullText.take(it) }
    private val captured = linkedSetOf<String>()
    private val records = JsonValue(JsonValue.ValueType.array)
    private var frame = 0
    private var finished = false
    private val resizeDuringOpening = output.state == "opening-prefixes-resize"
    private val initialWidth = Gdx.graphics.width
    private val initialHeight = Gdx.graphics.height
    private val initialBufferWidth = Gdx.graphics.backBufferWidth
    private val initialBufferHeight = Gdx.graphics.backBufferHeight
    private val resizeObservations = JsonValue(JsonValue.ValueType.array)

    fun onFrame(probe: ScenarioRuntimeProbe) {
        if (finished) return
        frame++
        if (resizeDuringOpening) {
            when (frame) {
                10 -> check(Gdx.graphics.setWindowedMode(initialWidth / 2, initialHeight / 2))
                20 -> check(Gdx.graphics.setWindowedMode(initialWidth, initialHeight))
                12, 22 -> {
                    val divisor = if (frame == 12) 2 else 1
                    check(Gdx.graphics.backBufferWidth == initialBufferWidth / divisor &&
                        Gdx.graphics.backBufferHeight == initialBufferHeight / divisor) { "Backbuffer resize did not complete" }
                    val observation = JsonValue(JsonValue.ValueType.`object`)
                    observation.addChild("width", JsonValue(Gdx.graphics.backBufferWidth.toLong()))
                    observation.addChild("height", JsonValue(Gdx.graphics.backBufferHeight.toLong()))
                    resizeObservations.addChild(observation)
                }
            }
        }
        check(probe.elapsedSeconds < 12f) { "Timed out waiting for natural prefixes; missing=${expected - captured}" }
        if (probe.playback != PlaybackState.DIALOGUE || !probe.naturalStreetTextIsolation) return
        check(probe.module == "R_00" && probe.sceneIndex == 1 && probe.dialogueSpeakerId == "181") {
            "Prefix capture left the first soldier dialogue"
        }
        val text = probe.dialogueVisibleText
        check(fullText.startsWith(text)) { "Unexpected first dialogue text: $text" }
        if (text in expected && captured.add(text)) {
            val pixels = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight)
            val width = pixels.width
            val height = pixels.height
            check(width == 2560 && height == 1376) { "Unexpected framebuffer size $width x $height" }
            val bytes = ByteArray(width * height * 4)
            pixels.pixels.rewind(); pixels.pixels.get(bytes); pixels.dispose()
            val file = "game-prefix-${text.length.toString().padStart(3, '0')}.rgba"
            directory.mkdirs()
            directory.child(file).writeBytes(bytes, false)
            val digest = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
            val entry = JsonValue(JsonValue.ValueType.`object`)
            entry.addChild("text", JsonValue(text))
            entry.addChild("file", JsonValue(file))
            entry.addChild("sha256", JsonValue(digest))
            entry.addChild("frame", JsonValue(frame.toLong()))
            entry.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
            entry.addChild("complete", JsonValue(probe.dialogueTextComplete))
            entry.addChild("naturalStreetTextIsolation", JsonValue(true))
            records.addChild(entry)
            Gdx.app.log("JojoGame", "OPENING_PREFIX_CAPTURE: length=${text.length} frame=$frame text=$text")
        }
        if (probe.dialogueTextComplete) {
            check(captured.containsAll(expected)) { "Natural prefixes were skipped: ${expected - captured}" }
            val manifest = JsonValue(JsonValue.ValueType.`object`)
            manifest.addChild("contract", JsonValue("natural-first-dialogue-prefixes-rgba8"))
            manifest.addChild("width", JsonValue(2560L))
            manifest.addChild("height", JsonValue(1376L))
            manifest.addChild("origin", JsonValue("bottom-left"))
            manifest.addChild("fullText", JsonValue(fullText))
            manifest.addChild("dialogueInputs", JsonValue(0L))
            val lengths = JsonValue(JsonValue.ValueType.array)
            sampleLengths.forEach { lengths.addChild(JsonValue(it.toLong())) }
            manifest.addChild("sampleLengths", lengths)
            manifest.addChild("scope", JsonValue("selected visible strings; readback affects timing; no typing-speed equivalence claim"))
            manifest.addChild("resizeObservations", resizeObservations)
            manifest.addChild("captures", records)
            directory.child("game-prefixes.json").writeString(manifest.prettyPrint(JsonWriter.OutputType.json, 120), false)
            finished = true
            Gdx.app.exit()
        }
    }
}
