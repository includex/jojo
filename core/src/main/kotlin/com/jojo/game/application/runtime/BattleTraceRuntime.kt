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
    /**
     * `maxSimulationSeconds` (Float,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val maxSimulationSeconds: Float,
    /**
     * `resultSceneGraceSeconds` (Float): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val resultSceneGraceSeconds: Float = 300f,
) {
    /**
     * `resultObservedAt` (Float?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var resultObservedAt: Float? = null
    /**
     * `timeoutReason`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun timeoutReason(elapsed: Float, hasOutcome: Boolean): String? {
        if (!hasOutcome) return if (elapsed >= maxSimulationSeconds) "timeout" else null
        val observedAt = resultObservedAt ?: elapsed.also { resultObservedAt = it }
        return if (elapsed - observedAt >= resultSceneGraceSeconds) "result-scene-timeout" else null
    }
}

/** BattleTraceRandomStreams: 재현 가능한 도구·전투 난수열과 호출 순서를 함께 기록하는 난수 공급원이다. */
class BattleTraceRandomStreams(toolSeed: Int, mathSeed: Long) : BattleRandomSource {
    /**
     * `Event` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Event(
        /**
         * `frame` (Long,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val frame: Long,
        /**
         * `time` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val time: Float,
        /**
         * `min` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val min: Int,
        /**
         * `max` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val max: Int,
        /**
         * `flag` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val flag: Int,
        /**
         * `before` (Long,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val before: Long,
        /**
         * `after` (Long,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val after: Long,
        /**
         * `value` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val value: Int,
    )

    /**
     * `toolState` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var toolState = toolSeed.toLong()
    /**
     * `mathState` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var mathState = mathSeed and 0xffffffffL
    /**
     * `frame` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var frame = 0L
    /**
     * `time` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var time = 0f
    /**
     * `events` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val events = mutableListOf<Event>()

    /**
     * `setClock`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setClock(frame: Long, time: Float) {
        this.frame = frame
        this.time = time
    }

    /**
     * `random`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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
    /**
     * `frames` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val frames = mutableListOf<String>()
    /**
     * `inputs` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val inputs = mutableListOf<String>()
    /**
     * `frameNumber` (Long): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var frameNumber: Long = 0
        private set
    /**
     * `recordedRowCount` (Long): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var recordedRowCount: Long = 0
        private set

    /**
     * `nextFrame`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun nextFrame(time: Float): Long {
        frameNumber++
        random.setClock(frameNumber, time)
        return frameNumber
    }

    /**
     * `upcomingFrame`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun upcomingFrame(): Long = frameNumber + 1
    /**
     * `addFrame`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addFrame(json: String) { frames += json; recordedRowCount++ }
    /**
     * `recordInput`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun recordInput(context: String) { inputs += context }
    /**
     * `write`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun write(reason: String, summaryJson: String) = Unit

    companion object {
        /**
         * `escape`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun escape(value: String) = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
        /**
         * `number`: 타입의 핵심 동작을 수행한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun number(value: Float): String =
            if (value.isFinite()) "%.6f".format(java.util.Locale.ROOT, value).trimEnd('0').trimEnd('.') else "0"
    }
}
