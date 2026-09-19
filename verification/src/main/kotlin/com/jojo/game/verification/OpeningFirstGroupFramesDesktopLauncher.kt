package com.jojo.game.verification

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Graphics
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.JsonWriter
import com.badlogic.gdx.utils.ScreenUtils
import com.jojo.game.JojoGame
import com.jojo.game.application.runtime.GameEntryPoint
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.application.runtime.RuntimeScreenObserver
import com.jojo.game.application.runtime.ScenarioActorRuntimeProbe
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.application.scenario.ScenarioInterpreter
import com.jojo.game.domain.scenario.TacticalUnit
import java.io.File
import java.lang.reflect.Proxy
import java.security.MessageDigest

/** Captures the first three-actor Hall move at controlled Float32 frame deltas. */
object OpeningFirstGroupFramesDesktopLauncher {
    private val actorIds = setOf(0, 157, 181)
    private val captureOrdinals = setOf(1, 6, 12, 13, 18, 19, 24)
    private val starts = mapOf(181 to (40 to 15), 0 to (40 to 5), 157 to (54 to 95))
    private val paths = mapOf(
        181 to (15..25).map { 40 to it },
        0 to (5..15).map { 40 to it },
        157 to (95 downTo 85).map { 54 to it },
    )
    private const val fixedDelta = 1f / 60f

    private data class Timing(
        var firstObservedMoveFrame: Int? = null,
        var firstObservedElapsedSeconds: Double? = null,
        var firstObservedMoveJustStarted: Boolean? = null,
        var primeFrame: Int? = null,
        var primeElapsedSeconds: Double? = null,
    )

    @JvmStatic
    fun main(args: Array<String>) {
        val directory = File(args.single()).also { it.mkdirs() }
        directory.listFiles()?.forEach(File::delete)
        val captures = JsonValue(JsonValue.ValueType.array)
        val ticks = JsonValue(JsonValue.ValueType.array)
        val frameDigests = JsonValue(JsonValue.ValueType.array)
        val timings = actorIds.associateWith { Timing() }
        var frame = 0
        var groupOrdinal = -1
        var groupStarted = false
        var commonPrimeAligned = false
        var finished = false
        lateinit var game: JojoGame

        val observer = RuntimeScreenObserver { delta, probe ->
            if (!finished && probe is ScenarioRuntimeProbe) {
                frame++
                check(frame < 900 && probe.elapsedSeconds < 12f)
                check(delta == fixedDelta)
                val actors = probe.actors.filter { it.id in actorIds }.associateBy { it.id }
                val playback = game.screen.javaClass.getDeclaredField("playback").let {
                    it.isAccessible = true
                    it.get(game.screen) as ScenarioInterpreter
                }
                actorIds.forEach { id ->
                    val actor = actors[id] ?: return@forEach
                    val start = starts.getValue(id)
                    val unit = requireNotNull(playback.stage.units[id])
                    if (actor.x == start.first && actor.y == start.second && actor.moveDuration > 0f) {
                        check(unit.movePath == paths.getValue(id))
                        val timing = timings.getValue(id)
                        if (timing.firstObservedMoveFrame == null) {
                            timing.firstObservedMoveFrame = frame
                            timing.firstObservedElapsedSeconds = unit.hallMoveElapsedSeconds
                            timing.firstObservedMoveJustStarted = unit.moveJustStarted
                        }
                        if (!unit.moveJustStarted && timing.primeFrame == null) {
                            timing.primeFrame = frame
                            timing.primeElapsedSeconds = unit.hallMoveElapsedSeconds
                        }
                    }
                }

                if (!groupStarted && timings.values.all { it.primeFrame != null }) {
                    check(probe.actors.map { it.id }.toSet() == actorIds)
                    val states = actorIds.map { id ->
                        val actor = requireNotNull(actors[id])
                        val unit = requireNotNull(playback.stage.units[id])
                        actor to unit
                    }
                    check(states.all { (actor, _) -> actor.moveDuration > 0f })
                    commonPrimeAligned = states.all { (_, unit) -> !unit.moveJustStarted && unit.hallMoveElapsedSeconds == 0.0 }
                    groupStarted = true
                    groupOrdinal = 0
                } else if (groupStarted) {
                    groupOrdinal++
                }

                if (groupStarted) {
                    val selectedActors = actorIds.sorted().map { requireNotNull(actors[it]) }
                    val renderUnits = renderUnits(game)
                    ticks.addChild(sample(groupOrdinal, frame, delta, selectedActors, playback, renderUnits))
                    run {
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
                        val digest = sha256(bytes)
                        val digestRow = JsonValue(JsonValue.ValueType.`object`)
                        digestRow.addChild("ordinal", JsonValue(groupOrdinal.toLong()))
                        digestRow.addChild("frame", JsonValue(frame.toLong()))
                        digestRow.addChild("width", JsonValue(width.toLong()))
                        digestRow.addChild("height", JsonValue(height.toLong()))
                        digestRow.addChild("sha256", JsonValue(digest))
                        frameDigests.addChild(digestRow)
                        if (groupOrdinal !in captureOrdinals) return@run
                        val fileName = "game-group-${groupOrdinal.toString().padStart(3, '0')}.rgba"
                        File(directory, fileName).writeBytes(bytes)
                        val row = sample(groupOrdinal, frame, delta, selectedActors, playback, renderUnits)
                        row.addChild("file", JsonValue(fileName))
                        row.addChild("sha256", JsonValue(digest))
                        row.addChild("width", JsonValue(width.toLong()))
                        row.addChild("height", JsonValue(height.toLong()))
                        captures.addChild(row)
                    }
                    if (groupOrdinal == captureOrdinals.max()) {
                        val report = JsonValue(JsonValue.ValueType.`object`)
                        report.addChild("contract", JsonValue("controlled-opening-first-group-rgba8"))
                        report.addChild("controlledEvidence", JsonValue(true))
                        report.addChild("clockMode", JsonValue("controlled-fixed-float32"))
                        report.addChild("stepDelta", JsonValue(fixedDelta.toDouble()))
                        report.addChild("stepDeltaFloat32Bits", JsonValue(java.lang.Float.floatToRawIntBits(fixedDelta).toLong()))
                        report.addChild("width", JsonValue(2560L))
                        report.addChild("height", JsonValue(1376L))
                        report.addChild("origin", JsonValue("bottom-left"))
                        report.addChild("isolation", JsonValue(false))
                        report.addChild("dialogueInputs", JsonValue(0L))
                        report.addChild("observationPhase", JsonValue("post-render"))
                        val ids = JsonValue(JsonValue.ValueType.array)
                        actorIds.sorted().forEach { ids.addChild(JsonValue(it.toLong())) }
                        report.addChild("actorIds", ids)
                        report.addChild("commonPrimeAligned", JsonValue(commonPrimeAligned))
                        val timingRows = JsonValue(JsonValue.ValueType.array)
                        actorIds.sorted().forEach { id ->
                            val timing = timings.getValue(id)
                            val row = JsonValue(JsonValue.ValueType.`object`)
                            row.addChild("id", JsonValue(id.toLong()))
                            row.addChild("firstObservedMoveFrame", JsonValue(requireNotNull(timing.firstObservedMoveFrame).toLong()))
                            row.addChild("firstObservedElapsedSeconds", JsonValue(requireNotNull(timing.firstObservedElapsedSeconds)))
                            row.addChild("firstObservedMoveJustStarted", JsonValue(requireNotNull(timing.firstObservedMoveJustStarted)))
                            row.addChild("primeFrame", JsonValue(requireNotNull(timing.primeFrame).toLong()))
                            row.addChild("primeElapsedSeconds", JsonValue(requireNotNull(timing.primeElapsedSeconds)))
                            timingRows.addChild(row)
                        }
                        report.addChild("actorTimings", timingRows)
                        report.addChild("ticks", ticks)
                        report.addChild("frameDigests", frameDigests)
                        report.addChild("captures", captures)
                        File(directory, "game-first-group.json").writeText(
                            report.prettyPrint(JsonWriter.OutputType.json, 120),
                        )
                        finished = true
                        Gdx.app.log("JojoGame", "OPENING_FIRST_GROUP_FRAMES_COMPLETE frame=$frame")
                        Gdx.app.exit()
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
        lateinit var controlledGraphics: Graphics
        val listener = object : ApplicationAdapter() {
            override fun create() {
                game.create()
                controlledGraphics = fixedDeltaGraphics(Gdx.graphics)
            }

            override fun render() {
                val nativeGraphics = Gdx.graphics
                try {
                    Gdx.graphics = controlledGraphics
                    game.render()
                } finally {
                    Gdx.graphics = nativeGraphics
                }
            }

            override fun resize(width: Int, height: Int) = game.resize(width, height)
            override fun pause() = game.pause()
            override fun resume() = game.resume()
            override fun dispose() = game.dispose()
        }
        val window = Lwjgl3ApplicationConfiguration().apply {
            setTitle("Jojo controlled opening first group verification")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
            useVsync(true)
        }
        Lwjgl3Application(listener, window)
    }

    private fun sample(
        ordinal: Int,
        frame: Int,
        delta: Float,
        actors: List<ScenarioActorRuntimeProbe>,
        playback: ScenarioInterpreter,
        renderUnits: Map<Int, Any>,
    ): JsonValue {
        val row = JsonValue(JsonValue.ValueType.`object`)
        row.addChild("ordinal", JsonValue(ordinal.toLong()))
        row.addChild("frame", JsonValue(frame.toLong()))
        row.addChild("delta", JsonValue(delta.toDouble()))
        val actorRows = JsonValue(JsonValue.ValueType.array)
        actors.forEach { actor ->
            val unit = requireNotNull(playback.stage.units[actor.id])
            val renderUnit = requireNotNull(renderUnits[actor.id])
            val actorRow = JsonValue(JsonValue.ValueType.`object`)
            actorRow.addChild("id", JsonValue(actor.id.toLong()))
            val logical = JsonValue(JsonValue.ValueType.array)
            logical.addChild(JsonValue(actor.x.toLong()))
            logical.addChild(JsonValue(actor.y.toLong()))
            actorRow.addChild("logical", logical)
            actorRow.addChild("visualX", JsonValue(actor.visualX.toDouble()))
            actorRow.addChild("visualY", JsonValue(actor.visualY.toDouble()))
            actorRow.addChild("action", JsonValue(actor.action.toLong()))
            actorRow.addChild("direction", JsonValue(actor.direction.toLong()))
            actorRow.addChild("elapsedSeconds", JsonValue(unit.hallMoveElapsedSeconds))
            actorRow.addChild("durationSeconds", JsonValue(unit.hallMoveDurationSeconds))
            actorRow.addChild("moveJustStarted", JsonValue(unit.moveJustStarted))
            val sprite = JsonValue(JsonValue.ValueType.`object`)
            sprite.addChild("frameRow", JsonValue((field(renderUnit, "frameRow") as Number).toLong()))
            sprite.addChild("textureAssetId", JsonValue((field(renderUnit, "textureAssetId") as Number).toLong()))
            sprite.addChild("flipX", JsonValue(field(renderUnit, "flipX") as Boolean))
            actorRow.addChild("sprite", sprite)
            actorRows.addChild(actorRow)
        }
        row.addChild("actors", actorRows)
        return row
    }

    private fun renderUnits(game: JojoGame): Map<Int, Any> {
        val screen = game.screen
        val view = screen.javaClass
            .getDeclaredMethod("battlefieldView", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
            .let { it.isAccessible = true; it.invoke(screen, true, true) }
        val units = view.javaClass.getDeclaredMethod("getUnits").invoke(view) as List<*>
        return units.filterNotNull().associateBy { field(it, "id") as Int }
    }

    private fun fixedDeltaGraphics(delegate: Graphics): Graphics = Proxy.newProxyInstance(
        Graphics::class.java.classLoader,
        arrayOf(Graphics::class.java),
    ) { _, method, arguments ->
        when (method.name) {
            "getDeltaTime", "getRawDeltaTime" -> fixedDelta
            else -> method.invoke(delegate, *(arguments ?: emptyArray()))
        }
    } as Graphics

    private fun field(instance: Any, name: String): Any = instance.javaClass.getDeclaredField(name).let {
        it.isAccessible = true
        it.get(instance)
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { "%02x".format(it) }
}
