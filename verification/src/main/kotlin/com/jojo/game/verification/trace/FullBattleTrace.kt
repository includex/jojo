package com.jojo.game.verification.trace

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * data class  `FullBattleTraceConfig`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

data class FullBattleTraceConfig(
    val outputPath: String,
    /** Requested production scenario; carried into the trace as run evidence. */
    val scenario: String = "S_00",
    val toolSeed: Int = 1000,
    val mathSeed: Long = 0x12345678L,
    val timeScale: Float = 8f,
    val maxSimulationSeconds: Float = 1800f,
    val driverIntervalSeconds: Float = .12f,
    /** Standalone battle verification exits; campaign E2E must keep routing to R_01. */
    val exitOnFinish: Boolean = true,
)

/**
 * Keeps the tactical safety deadline separate from the authored result scene.
 *
 * A battle may legitimately reach its round-limit result immediately before
 * [maxSimulationSeconds].  The original then continues through scene1
 * dialogue/callbacks.  Treating that dialogue as the tactical timeout cuts the
 * source route at exactly the point the full-battle trace is meant to verify.
 */
/**
 * class  `FullBattleTraceDeadline`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class FullBattleTraceDeadline(
    private val maxSimulationSeconds: Float,
    private val resultSceneGraceSeconds: Float = 300f,
) {
    private var resultObservedAt: Float? = null

    /**
     * 공개 메서드 `timeoutReason`
     *
     * ### 파라미터
    - `elapsed` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `hasOutcome` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `String?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun timeoutReason(elapsed: Float, hasOutcome: Boolean): String? {
        if (!hasOutcome) return if (elapsed >= maxSimulationSeconds) "timeout" else null
        val observedAt = resultObservedAt ?: elapsed.also { resultObservedAt = it }
        return if (elapsed - observedAt >= resultSceneGraceSeconds) "result-scene-timeout" else null
    }
}

/** Exact deterministic counterparts of Tool.random and the harness Math.random override. */
class SourceRandomStreams(toolSeed: Int, mathSeed: Long) {
    /**
     * data class  `Event`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Event(
        val frame: Long, val time: Float, val min: Int, val max: Int, val flag: Int,
        val before: Long, val after: Long, val value: Int,
    )

    private var toolState = toolSeed.toLong()
    private var mathState = mathSeed and 0xffffffffL
    private var frame = 0L
    private var time = 0f
    val events = mutableListOf<Event>()

    /**
     * 공개 메서드 `setClock`
     *
     * ### 파라미터
    - `frame` (`Long`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `time` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun setClock(frame: Long, time: Float) {
        this.frame = frame; this.time = time
    }

    /**
     * 공개 메서드 `random`
     *
     * ### 파라미터
    - `min` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `max` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `flag` (`Int = 0`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun random(min: Int, max: Int, flag: Int = 0): Int {
        val width = max - min + 1
        require(width > 0) { "invalid source random range $min..$max" }
        val before = if (flag == 1) mathState else toolState
        val fraction = if (flag == 1) {
            mathState = (1664525L * mathState + 1013904223L) and 0xffffffffL
            mathState.toDouble() / 4294967296.0
        } else {
            toolState = (9301L * toolState + 49297L) % 233280L
            toolState.toDouble() / 233280.0
        }
        val value = kotlin.math.floor(fraction * (100 + width)).toInt() % width + min
        events += Event(frame, time, min, max, flag, before, if (flag == 1) mathState else toolState, value)
        return value
    }
}

/** Frame log writer kept independent of the renderer so failed/timeout runs retain evidence. */
class FullBattleTraceRecorder(
    private val config: FullBattleTraceConfig,
    private val random: SourceRandomStreams,
    private val frameMemoryLimitBytes: Int = 8 * 1024 * 1024,
) {
    private val bufferedFrames = mutableListOf<String>()
    private var bufferedFrameBytes = 0L
    private var frameSpoolPath: Path? = null
    private var frameSpoolWriter: BufferedWriter? = null
    private val inputs = mutableListOf<String>()
    private var written = false
    var frameNumber = 0L
        private set

    /** Source renderer's `frameCount`: number of recorded rows, including micro-observations. */
    var recordedRowCount = 0L
        private set

    /**
     * 공개 메서드 `nextFrame`
     *
     * ### 파라미터
    - `time` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Long`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun nextFrame(time: Float): Long {
        frameNumber++
        random.setClock(frameNumber, time)
        return frameNumber
    }

    /** Same render id used by callback observations emitted before its RAF row. */
    fun upcomingFrame(): Long = frameNumber + 1

    /**
     * 공개 메서드 `addFrame`
     *
     * ### 파라미터
    - `json` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun addFrame(json: String) {
        if (frameSpoolWriter == null && bufferedFrameBytes + json.length <= frameMemoryLimitBytes) {
            bufferedFrames += json
            bufferedFrameBytes += json.length
        } else {
            val spool = frameSpoolWriter ?: openFrameSpool()
            spool.write(json)
            spool.newLine()
        }
        recordedRowCount++
    }

    private fun openFrameSpool(): BufferedWriter {
        val path = Files.createTempFile("jojo-full-battle-frames-", ".jsonl.gz")
        frameSpoolPath = path
        val writer = BufferedWriter(
            OutputStreamWriter(
                GZIPOutputStream(
                    Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING),
                ), Charsets.UTF_8
            )
        )
        bufferedFrames.forEach { frame ->
            writer.write(frame)
            writer.newLine()
        }
        bufferedFrames.clear()
        bufferedFrameBytes = 0L
        frameSpoolWriter = writer
        return writer
    }

    /** Called only after the installed production InputProcessor accepted the event. */
    fun recordInput(context: String) {
        inputs += context
    }

    /**
     * 공개 메서드 `write`
     *
     * ### 파라미터
    - `reason` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `summaryJson` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun write(reason: String, summaryJson: String) {
        if (written) return
        written = true
        val output = Path.of(config.outputPath).toAbsolutePath()
        output.parent?.let(Files::createDirectories)
        frameSpoolWriter?.close()
        frameSpoolWriter = null
        try {
            outputWriter(output).use { writer ->
                writer.write("{\"format\":\"jojo-yingchuan-full-battle-trace/v1\",\"engine\":\"jojo-game\",\"config\":{\"scenario\":\"")
                writer.write(escape(config.scenario))
                writer.write(
                    "\",\"toolSeed\":${config.toolSeed},\"mathSeed\":${config.mathSeed},\"timeScale\":${
                        number(
                            config.timeScale
                        )
                    },\"maxSimulationSeconds\":${number(config.maxSimulationSeconds)},\"driver\":\"production-input\"},\"reason\":\""
                )
                writer.write(escape(reason))
                writer.write("\",\"inputs\":[")
                inputs.forEachIndexed { index, input ->
                    if (index > 0) writer.write(",")
                    writer.write("\"")
                    writer.write(escape(input))
                    writer.write("\"")
                }
                writer.write("],\"frames\":[")
                var firstFrame = true

                /**
                 * 공개 메서드 `emitFrame`
                 *
                 * ### 파라미터
                - `frame` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
                 *
                 * ### 응답 스펙
                 * - 반환 타입: `Unit`
                 * - 반환값: 동작 결과의 도메인 값입니다.
                 */

                fun emitFrame(frame: String) {
                    if (!firstFrame) writer.write(",")
                    writer.write(frame)
                    firstFrame = false
                }
                frameSpoolPath?.let { path ->
                    BufferedReader(InputStreamReader(GZIPInputStream(Files.newInputStream(path)), Charsets.UTF_8))
                        .useLines { lines -> lines.forEach(::emitFrame) }
                } ?: bufferedFrames.forEach(::emitFrame)
                writer.write("],\"rng\":[")
                random.events.forEachIndexed { index, event ->
                    if (index > 0) writer.write(",")
                    writer.write("{\"f\":${event.frame},\"t\":${number(event.time)},\"min\":${event.min},\"max\":${event.max},\"flag\":${event.flag},\"before\":${event.before},\"after\":${event.after},\"value\":${event.value}}")
                }
                writer.write("],\"summary\":")
                writer.write(summaryJson)
                writer.write("}")
            }
        } finally {
            frameSpoolPath?.let(Files::deleteIfExists)
            frameSpoolPath = null
        }
    }

    private fun outputWriter(path: Path): BufferedWriter = if (path.fileName.toString().endsWith(".gz")) {
        BufferedWriter(OutputStreamWriter(GZIPOutputStream(Files.newOutputStream(path)), Charsets.UTF_8))
    } else {
        Files.newBufferedWriter(path)
    }

    companion object {
        /**
         * 공개 메서드 `escape`
         *
         * ### 파라미터
        - `value` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun escape(value: String) = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")

        /**
         * 공개 메서드 `number`
         *
         * ### 파라미터
        - `value` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `String`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun number(value: Float): String =
            if (value.isFinite()) "%.6f".format(java.util.Locale.ROOT, value).trimEnd('0').trimEnd('.') else "0"
    }
}
