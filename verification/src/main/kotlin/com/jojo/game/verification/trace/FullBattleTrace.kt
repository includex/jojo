// Verification
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


/** FullBattleTraceConfig: 전체 전투 재현의 출력 위치·난수 시드·시간 배율·종료 규칙을 지정한다. */
data class FullBattleTraceConfig(
    /** outputPath: 검증 산출물 저장 경로를 담는다. */
    val outputPath: String,
    /** scenario: 요청된 운영 시나리오이며 실행 증거로 추적에 기록한다. */
    val scenario: String = "S_00",
    /** toolSeed: 도구 난수 시드 상태를 검증 흐름에 전달한다. */
    val toolSeed: Int = 1000,
    /** mathSeed: 수학 난수 시드 상태를 검증 흐름에 전달한다. */
    val mathSeed: Long = 0x12345678L,
    /** timeScale: 시간 배율 값을 보관한다. */
    val timeScale: Float = 8f,
    /** maxSimulationSeconds: 최대 시뮬레이션 시간 값을 보관한다. */
    val maxSimulationSeconds: Float = 1800f,
    /** driverIntervalSeconds: 드라이버 간격 값을 보관한다. */
    val driverIntervalSeconds: Float = .12f,
    /** exitOnFinish: 단독 전투 검증은 종료하지만 캠페인 E2E는 R_01로 계속 이동해야 한다. */
    val exitOnFinish: Boolean = true,
)


/** FullBattleTraceDeadline: 전술 안전 제한 시간과 원본 결과 장면의 진행을 분리한다. 전투는 [maxSimulationSeconds] 직전에 라운드 제한 결과에 도달할 수 있으며, 원본은 이후 scene1 대화와 콜백을 계속 실행한다. 이를 전술 시간 초과로 처리하면 전체 전투 추적이 검증해야 할 지점에서 원본 경로가 끊긴다. */
class FullBattleTraceDeadline(
    /** maxSimulationSeconds: 최대 시뮬레이션 시간 값을 보관한다. */
    private val maxSimulationSeconds: Float,
    /** resultSceneGraceSeconds: 검증 결과를 담는다. */
    private val resultSceneGraceSeconds: Float = 300f,
) {
    /** resultObservedAt: 검증 결과를 담는다. */
    private var resultObservedAt: Float? = null


    /** timeoutReason: 경과 시간과 결과 장면에 따른 제한 사유를 반환한다. */
    fun timeoutReason(elapsed: Float, hasOutcome: Boolean): String? {
        if (!hasOutcome) return if (elapsed >= maxSimulationSeconds) "timeout" else null
        val observedAt = resultObservedAt ?: elapsed.also { resultObservedAt = it }
        return if (elapsed - observedAt >= resultSceneGraceSeconds) "result-scene-timeout" else null
    }
}

/** SourceRandomStreams: Tool.random과 하니스의 Math.random 대체 동작에 대응하는 결정적 난수 구현이다. */
class SourceRandomStreams(toolSeed: Int, mathSeed: Long) {

    /** Event: 원본 난수 호출의 프레임·시각·범위·결과를 재현용으로 기록한다. */
    data class Event(
        /** frame: 프레임 번호 상태를 검증 흐름에 전달한다. */
        val frame: Long, val time: Float, val min: Int, val max: Int, val flag: Int,
        /** before: 이전 상태 상태를 검증 흐름에 전달한다. */
        val before: Long, val after: Long, val value: Int,
    )

    /** toolState: 현재 검증 상태를 담는다. */
    private var toolState = toolSeed.toLong()
    /** mathState: 현재 검증 상태를 담는다. */
    private var mathState = mathSeed and 0xffffffffL
    /** frame: 프레임 번호 상태를 검증 흐름에 전달한다. */
    private var frame = 0L
    /** time: 시각 값을 보관한다. */
    private var time = 0f
    /** events: 검증 이벤트 목록을 담는다. */
    val events = mutableListOf<Event>()


    /** setClock: 검증 상태를 입력에 맞게 갱신한다. */
    fun setClock(frame: Long, time: Float) {
        this.frame = frame; this.time = time
    }


    /** random: 결정적 난수를 생성하고 호출을 기록한다. */
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

/** FullBattleTraceRecorder: 렌더러와 분리된 프레임 로그 작성기로 실패·시간 초과 실행의 증거를 보존한다. */
class FullBattleTraceRecorder(
    /** config: 검증 실행 설정을 담는다. */
    private val config: FullBattleTraceConfig,
    /** random: 난수 생성기 상태를 검증 흐름에 전달한다. */
    private val random: SourceRandomStreams,
    /** frameMemoryLimitBytes: 프레임 메모리 한도 값을 보관한다. */
    private val frameMemoryLimitBytes: Int = 8 * 1024 * 1024,
) {
    /** bufferedFrames: 버퍼 프레임 목록 상태를 검증 흐름에 전달한다. */
    private val bufferedFrames = mutableListOf<String>()
    /** bufferedFrameBytes: 버퍼 크기 값을 보관한다. */
    private var bufferedFrameBytes = 0L
    /** frameSpoolPath: 검증 산출물 저장 경로를 담는다. */
    private var frameSpoolPath: Path? = null
    /** frameSpoolWriter: 프레임 임시 기록기 상태를 검증 흐름에 전달한다. */
    private var frameSpoolWriter: BufferedWriter? = null
    /** inputs: 검증 입력 정보를 담는다. */
    private val inputs = mutableListOf<String>()
    /** written: 기록 완료 여부 여부를 나타낸다. */
    private var written = false
    /**
     * `frameNumber` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var frameNumber = 0L
        private set

    /**
     * `recordedRowCount` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var recordedRowCount = 0L
        private set


    /** nextFrame: 다음 프레임 번호를 예약한다. */
    fun nextFrame(time: Float): Long {
        frameNumber++
        random.setClock(frameNumber, time)
        return frameNumber
    }

    /** upcomingFrame: RAF 행보다 먼저 발생한 콜백 관찰에 사용하는 동일 렌더 ID이다. */
    fun upcomingFrame(): Long = frameNumber + 1


    /** addFrame: 프레임 JSON을 추적 버퍼에 추가한다. */
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

    /** openFrameSpool: 프레임 임시 저장소를 연다. */
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

    /** recordInput: 설치된 운영 InputProcessor가 이벤트를 수락한 뒤에만 호출된다. */
    fun recordInput(context: String) {
        inputs += context
    }


    /** write: 검증 이벤트와 산출물을 기록한다. */
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


                /** emitFrame: 런타임 이벤트를 받아 검증 산출물을 갱신한다. */
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

    /** outputWriter: 검증 출력 기록기를 연다. */
    private fun outputWriter(path: Path): BufferedWriter = if (path.fileName.toString().endsWith(".gz")) {
        BufferedWriter(OutputStreamWriter(GZIPOutputStream(Files.newOutputStream(path)), Charsets.UTF_8))
    } else {
        Files.newBufferedWriter(path)
    }

    companion object {

        /** escape: JSON 특수 문자를 이스케이프한다. */
        fun escape(value: String) = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")


        /** number: 문자열에서 수치 값을 읽는다. */
        fun number(value: Float): String =
            if (value.isFinite()) "%.6f".format(java.util.Locale.ROOT, value).trimEnd('0').trimEnd('.') else "0"
    }
}
