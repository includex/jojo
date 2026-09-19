package com.jojo.game.verification

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.jojo.game.application.runtime.RenderCaptureConfiguration
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.domain.scenario.PlaybackState

/** Observe normal frames without pixel readback, isolation, or generated input. */
internal class OpeningEventTimingCapture(output: RenderCaptureConfiguration) {
    private val file = Gdx.files.absolute(requireNotNull(output.rawCapturePath)).parent().child("game-event-timing.json")
    private val rows = JsonValue(JsonValue.ValueType.array)
    private var frame = 0
    private var finished = false
    private var sawEvent = false
    private var sawComplete = false
    private val startedNanos = System.nanoTime()

    fun onFrame(screen: Screen?, probe: ScenarioRuntimeProbe) {
        if (finished) return
        frame++
        check(frame < 900 && probe.elapsedSeconds < 10f) { "Natural event timing timed out" }
        check(probe.module == "R_00" && probe.sceneIndex == 1)
        val row = JsonValue(JsonValue.ValueType.`object`)
        row.addChild("frame", JsonValue(frame.toLong()))
        row.addChild("deltaSeconds", JsonValue(Gdx.graphics.deltaTime.toDouble()))
        row.addChild("elapsedSeconds", JsonValue(probe.elapsedSeconds.toDouble()))
        row.addChild("wallElapsedSeconds", JsonValue((System.nanoTime() - startedNanos) / 1e9))
        row.addChild("playback", JsonValue(probe.playback.name))
        row.addChild("modalKind", JsonValue(probe.modalKind))
        row.addChild("text", JsonValue(probe.modalVisibleText))
        row.addChild("complete", JsonValue(probe.modalTextComplete))
        row.addChild("dialogueText", JsonValue(probe.dialogueVisibleText))
        row.addChild("dialogueSpeakerId", JsonValue(probe.dialogueSpeakerId))
        fun field(owner: Any, name: String): Any = owner.javaClass.getDeclaredField(name).let {
            it.isAccessible = true; requireNotNull(it.get(owner))
        }
        val playback = field(requireNotNull(screen), "playback")
        val modal = field(playback, "modalController")
        row.addChild("delayRemainingSeconds", JsonValue((field(playback, "delayRemainingSeconds") as Number).toDouble()))
        val actors = JsonValue(JsonValue.ValueType.array)
        probe.actors.forEach { actor ->
            val value = JsonValue(JsonValue.ValueType.`object`)
            value.addChild("id", JsonValue(actor.id.toLong()))
            value.addChild("x", JsonValue(actor.x.toLong()))
            value.addChild("y", JsonValue(actor.y.toLong()))
            value.addChild("visualX", JsonValue(actor.visualX.toDouble()))
            value.addChild("visualY", JsonValue(actor.visualY.toDouble()))
            value.addChild("moveElapsed", JsonValue(actor.moveElapsed.toDouble()))
            value.addChild("moveDuration", JsonValue(actor.moveDuration.toDouble()))
            value.addChild("direction", JsonValue(actor.direction.toLong()))
            value.addChild("action", JsonValue(actor.action.toLong()))
            value.addChild("visible", JsonValue(actor.visible))
            actors.addChild(value)
        }
        row.addChild("actors", actors)
        row.addChild("modalRemainingSeconds", JsonValue((field(modal, "modalRemainingSeconds") as Number).toDouble()))
        rows.addChild(row)
        if (probe.modalKind == "EVENT") {
            sawEvent = true
            check(probe.modalText == "재능의 첫 징후")
            sawComplete = sawComplete || probe.modalTextComplete
        }
        if (probe.playback != PlaybackState.DIALOGUE || probe.dialogueVisibleText.isEmpty()) return
        check(sawEvent && sawComplete && probe.dialogueSpeakerId == "181")
        val result = JsonValue(JsonValue.ValueType.`object`)
        result.addChild("contract", JsonValue("natural-opening-event-timing-game/v1"))
        result.addChild("clock", JsonValue("render delta passed to playback; elapsedSeconds is accumulated Float; wall clock is diagnostic only"))
        result.addChild("dialogueInputs", JsonValue(0L))
        result.addChild("pixelReadback", JsonValue(false))
        result.addChild("isolation", JsonValue(false))
        result.addChild("frames", rows)
        file.parent().mkdirs()
        file.writeString(result.prettyPrint(JsonWriter.OutputType.json, 120), false)
        Gdx.app.log("JojoGame", "OPENING_EVENT_TIMING_COMPLETE frames=$frame")
        finished = true
        Gdx.app.exit()
    }
}
