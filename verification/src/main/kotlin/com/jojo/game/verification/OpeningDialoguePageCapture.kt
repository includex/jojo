package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.application.runtime.RenderCaptureConfiguration
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState
import java.security.MessageDigest

/** Captures natural completion, then advances through the production input processor. */
internal class OpeningDialoguePageCapture(output: RenderCaptureConfiguration) {
    private val directory = Gdx.files.absolute(requireNotNull(output.rawCapturePath)).parent()
    private val texts = listOf("대장님, 서둘러야 해요!", "알아!", "잠시만 기다려 주세요!")
    private val speakers = listOf("181", "0", "157")
    private val records = JsonValue(JsonValue.ValueType.array)
    private val inputs = JsonValue(JsonValue.ValueType.array)
    private val observed = linkedSetOf<String>()
    private var page = 0
    private var frame = 0
    private var finished = false
    private var pendingAdvance = false

    fun onFrame(probe: ScenarioRuntimeProbe) {
        if (finished) return
        frame++
        check(probe.elapsedSeconds < 20f) { "Timed out waiting for natural opening page ${page + 1}" }
        if (probe.playback != PlaybackState.DIALOGUE || !probe.naturalStreetTextIsolation) return
        check(probe.module == "R_00" && probe.sceneIndex == 1)
        if (pendingAdvance) {
            check(probe.dialogueTextComplete && probe.dialogueVisibleText == texts[page - 1]) {
                "Dialogue changed before queued advance"
            }
            val input = JsonValue(JsonValue.ValueType.`object`)
            input.addChild("afterPage", JsonValue(page.toLong()))
            input.addChild("frame", JsonValue(frame.toLong()))
            input.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
            input.addChild("kind", JsonValue("InputProcessor.keyDown/keyUp(SPACE)"))
            input.addChild("completeBeforeInput", JsonValue(true))
            input.addChild("textBeforeInput", JsonValue(texts[page - 1]))
            val processor = checkNotNull(Gdx.input.inputProcessor)
            check(processor.keyDown(Input.Keys.SPACE)) { "Dialogue advance input was not handled" }
            processor.keyUp(Input.Keys.SPACE)
            inputs.addChild(input)
            pendingAdvance = false
            return
        }
        val text = probe.dialogueVisibleText
        // Input synchronization may leave the previous rendered probe visible until the next update.
        if (page > 0 && text == texts[page - 1]) return
        check(probe.dialogueSpeakerId == speakers[page]) { "Unexpected speaker ${probe.dialogueSpeakerId} on page ${page + 1}" }
        check(texts[page].startsWith(text)) { "Unexpected text on page ${page + 1}: $text" }
        observed.add(text)
        if (!probe.dialogueTextComplete) return
        check(text == texts[page])
        check(observed.any { it.isNotEmpty() && it != text }) { "No natural partial text observed on page ${page + 1}" }
        val width = Gdx.graphics.backBufferWidth
        val height = Gdx.graphics.backBufferHeight
        check(width == 2560 && height == 1376)
        val pixels = ScreenUtils.getFrameBufferPixmap(0, 0, width, height)
        val bytes = ByteArray(width * height * 4)
        try { pixels.pixels.rewind(); pixels.pixels.get(bytes) } finally { pixels.dispose() }
        val file = "game-page-${page + 1}.rgba"
        directory.mkdirs()
        directory.child(file).writeBytes(bytes, false)
        val row = JsonValue(JsonValue.ValueType.`object`)
        row.addChild("page", JsonValue((page + 1).toLong()))
        row.addChild("text", JsonValue(text))
        row.addChild("speakerId", JsonValue(probe.dialogueSpeakerId))
        row.addChild("complete", JsonValue(true))
        row.addChild("side", JsonValue(if (probe.dialogueSide == 0) "left" else "right"))
        row.addChild("atTop", JsonValue(probe.dialogueAtTop))
        row.addChild("frame", JsonValue(frame.toLong()))
        row.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
        row.addChild("dialogueInputs", JsonValue(inputs.size.toLong()))
        row.addChild("naturalStreetTextIsolation", JsonValue(true))
        row.addChild("file", JsonValue(file))
        row.addChild("sha256", JsonValue(MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }))
        val strings = JsonValue(JsonValue.ValueType.array)
        observed.forEach { strings.addChild(JsonValue(it)) }
        row.addChild("observedStrings", strings)
        records.addChild(row)
        Gdx.app.log("JojoGame", "OPENING_PAGE_CAPTURE: page=${page + 1} speaker=${probe.dialogueSpeakerId} frame=$frame text=$text")
        page++
        observed.clear()
        if (page < texts.size) {
            pendingAdvance = true
        } else {
            val manifest = JsonValue(JsonValue.ValueType.`object`)
            manifest.addChild("contract", JsonValue("natural-opening-pages-rgba8"))
            manifest.addChild("width", JsonValue(width.toLong()))
            manifest.addChild("height", JsonValue(height.toLong()))
            manifest.addChild("origin", JsonValue("bottom-left"))
            manifest.addChild("dialogueInputs", JsonValue(inputs.size.toLong()))
            manifest.addChild("inputs", inputs)
            manifest.addChild("captures", records)
            directory.child("game-pages.json").writeString(manifest.prettyPrint(JsonWriter.OutputType.json, 120), false)
            finished = true
            Gdx.app.exit()
        }
    }
}
