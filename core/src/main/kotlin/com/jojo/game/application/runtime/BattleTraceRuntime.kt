// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.domain.battle.BattleRandomSource

/** BattleTraceRuntimeConfig: 자동 전투 재생의 시나리오·난수 시드·시간 제한을 고정하는 실행 설정이다. */
data class BattleTraceRuntimeConfig(
    val scenario: String = "S_00",
    val toolSeed: Int = 1000,
    val mathSeed: Long = 0x12345678L,
    val timeScale: Float = 8f,
    val maxSimulationSeconds: Float = 1800f,
    val driverIntervalSeconds: Float = .12f,
    val exitOnFinish: Boolean = true,
)

/** BattleTraceDeadline: 전투 결과 화면 도달 여부를 기준으로 추적 실행의 시간 초과를 판정한다. */
class BattleTraceDeadline(
    private val maxSimulationSeconds: Float,
    private val resultSceneGraceSeconds: Float = 300f,
) {
    private var resultObservedAt: Float? = null
    fun timeoutReason(elapsed: Float, hasOutcome: Boolean): String? {
        if (!hasOutcome) return if (elapsed >= maxSimulationSeconds) "timeout" else null
        val observedAt = resultObservedAt ?: elapsed.also { resultObservedAt = it }
        return if (elapsed - observedAt >= resultSceneGraceSeconds) "result-scene-timeout" else null
    }
}

/** BattleTraceRandomStreams: 재현 가능한 도구·전투 난수열과 호출 순서를 함께 기록하는 난수 공급원이다. */
class BattleTraceRandomStreams(toolSeed: Int, mathSeed: Long) : BattleRandomSource {
    data class Event(
        val frame: Long,
        val time: Float,
        val min: Int,
        val max: Int,
        val flag: Int,
        val before: Long,
        val after: Long,
        val value: Int,
    )

    private var toolState = toolSeed.toLong()
    private var mathState = mathSeed and 0xffffffffL
    private var frame = 0L
    private var time = 0f
    val events = mutableListOf<Event>()

    fun setClock(frame: Long, time: Float) {
        this.frame = frame
        this.time = time
    }

    override fun random(min: Int, max: Int, flag: Int): Int {
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

/** BattleTraceRecorder: 프레임 JSON과 입력 흔적을 누적해 전투 재현 자료로 남기는 기록기다. */
class BattleTraceRecorder(private val random: BattleTraceRandomStreams) {
    private val frames = mutableListOf<String>()
    private val inputs = mutableListOf<String>()
    var frameNumber: Long = 0
        private set
    var recordedRowCount: Long = 0
        private set

    fun nextFrame(time: Float): Long {
        frameNumber++
        random.setClock(frameNumber, time)
        return frameNumber
    }

    fun upcomingFrame(): Long = frameNumber + 1
    fun addFrame(json: String) { frames += json; recordedRowCount++ }
    fun recordInput(context: String) { inputs += context }
    fun write(reason: String, summaryJson: String) = Unit

    companion object {
        fun escape(value: String) = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
        fun number(value: Float): String =
            if (value.isFinite()) "%.6f".format(java.util.Locale.ROOT, value).trimEnd('0').trimEnd('.') else "0"
    }
}
