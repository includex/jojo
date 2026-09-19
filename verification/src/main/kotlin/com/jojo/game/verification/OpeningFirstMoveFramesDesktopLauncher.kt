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
import com.jojo.game.application.runtime.ScenarioRuntimeProbe
import com.jojo.game.application.scenario.ScenarioInterpreter
import java.io.File
import java.lang.reflect.Proxy
import java.security.MessageDigest

/** Captures production Hall movement at controlled Float32 frame deltas. */
object OpeningFirstMoveFramesDesktopLauncher {
    private val captureOrdinals = setOf(1, 6, 12, 13, 18, 19, 24)
    private const val fixedDelta = 1f / 60f

    @JvmStatic
    fun main(args: Array<String>) {
        val directory = File(args.single()).also { it.mkdirs() }
        directory.listFiles()?.forEach(File::delete)
        val captures = JsonValue(JsonValue.ValueType.array)
        val ticks = JsonValue(JsonValue.ValueType.array)
        var frame = 0
        var moveOrdinal = -1
        var moveStarted = false
        var finished = false
        lateinit var game: JojoGame

        val observer = RuntimeScreenObserver { delta, probe ->
            if (!finished && probe is ScenarioRuntimeProbe) {
                frame++
                check(frame < 900 && probe.elapsedSeconds < 12f)
                check(delta == fixedDelta)
                val actor = probe.actors.firstOrNull { it.id == 181 }
                if (actor != null && actor.moveDuration > 0f) {
                    val playback = game.screen.javaClass.getDeclaredField("playback").let {
                        it.isAccessible = true
                        it.get(game.screen) as ScenarioInterpreter
                    }
                    val unit = requireNotNull(playback.stage.units[181])
                    if (!moveStarted) {
                        if (unit.moveJustStarted) return@RuntimeScreenObserver
                        check(unit.hallMoveElapsedSeconds == 0.0)
                        moveStarted = true
                        moveOrdinal = 0
                    } else {
                        moveOrdinal++
                    }
                    val renderUnit = renderUnit(game, actor.id)
                    ticks.addChild(sample(moveOrdinal, frame, delta, actor, unit, renderUnit))
                    if (moveOrdinal in captureOrdinals) {
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
                        val fileName = "game-move-${moveOrdinal.toString().padStart(3, '0')}.rgba"
                        File(directory, fileName).writeBytes(bytes)
                        val row = sample(moveOrdinal, frame, delta, actor, unit, renderUnit)
                        row.addChild("file", JsonValue(fileName))
                        row.addChild("sha256", JsonValue(sha256(bytes)))
                        row.addChild("width", JsonValue(width.toLong()))
                        row.addChild("height", JsonValue(height.toLong()))
                        captures.addChild(row)
                    }
                    if (moveOrdinal == captureOrdinals.max()) {
                        val report = JsonValue(JsonValue.ValueType.`object`)
                        report.addChild("contract", JsonValue("controlled-opening-first-move-rgba8"))
                        report.addChild("controlledEvidence", JsonValue(true))
                        report.addChild("clockMode", JsonValue("controlled-fixed-float32"))
                        report.addChild("stepDelta", JsonValue(fixedDelta.toDouble()))
                        report.addChild("stepDeltaFloat32Bits", JsonValue(java.lang.Float.floatToRawIntBits(fixedDelta).toLong()))
                        report.addChild("width", JsonValue(2560L))
                        report.addChild("height", JsonValue(1376L))
                        report.addChild("origin", JsonValue("bottom-left"))
                        report.addChild("isolation", JsonValue(false))
                        report.addChild("dialogueInputs", JsonValue(0L))
                        report.addChild("ticks", ticks)
                        report.addChild("captures", captures)
                        File(directory, "game-first-move.json").writeText(
                            report.prettyPrint(JsonWriter.OutputType.json, 120),
                        )
                        finished = true
                        Gdx.app.log("JojoGame", "OPENING_FIRST_MOVE_FRAMES_COMPLETE frame=$frame")
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
            setTitle("Jojo controlled opening first move verification")
            setWindowedMode(1280, 688)
            setForegroundFPS(60)
            useVsync(true)
        }
        Lwjgl3Application(listener, window)
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

    private fun renderUnit(game: JojoGame, actorId: Int): Any {
        val screen = game.screen
        val view = screen.javaClass
            .getDeclaredMethod("battlefieldView", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
            .let { it.isAccessible = true; it.invoke(screen, true, true) }
        val units = view.javaClass.getDeclaredMethod("getUnits").invoke(view) as List<*>
        return requireNotNull(units.filterNotNull().firstOrNull { field(it, "id") == actorId })
    }

    private fun sample(
        ordinal: Int,
        frame: Int,
        delta: Float,
        actor: com.jojo.game.application.runtime.ScenarioActorRuntimeProbe,
        unit: com.jojo.game.domain.scenario.TacticalUnit,
        renderUnit: Any,
    ): JsonValue {
        val row = JsonValue(JsonValue.ValueType.`object`)
        row.addChild("ordinal", JsonValue(ordinal.toLong()))
        row.addChild("frame", JsonValue(frame.toLong()))
        row.addChild("delta", JsonValue(delta.toDouble()))
        row.addChild("elapsedSeconds", JsonValue(unit.hallMoveElapsedSeconds))
        val actorValue = JsonValue(JsonValue.ValueType.`object`)
        actorValue.addChild("id", JsonValue(actor.id.toLong()))
        val logical = JsonValue(JsonValue.ValueType.array)
        logical.addChild(JsonValue(actor.x.toLong()))
        logical.addChild(JsonValue(actor.y.toLong()))
        actorValue.addChild("logical", logical)
        actorValue.addChild("visualX", JsonValue(actor.visualX.toDouble()))
        actorValue.addChild("visualY", JsonValue(actor.visualY.toDouble()))
        actorValue.addChild("action", JsonValue(actor.action.toLong()))
        actorValue.addChild("direction", JsonValue(actor.direction.toLong()))
        actorValue.addChild("moveJustStarted", JsonValue(unit.moveJustStarted))
        row.addChild("actor", actorValue)
        val sprite = JsonValue(JsonValue.ValueType.`object`)
        sprite.addChild("frameRow", JsonValue((field(renderUnit, "frameRow") as Number).toLong()))
        sprite.addChild("textureAssetId", JsonValue((field(renderUnit, "textureAssetId") as Number).toLong()))
        sprite.addChild("flipX", JsonValue(field(renderUnit, "flipX") as Boolean))
        row.addChild("sprite", sprite)
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
