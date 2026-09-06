// Runtime
package com.jojo.game.application.runtime

import com.badlogic.gdx.Gdx

/** BattleTraceRuntimeSession: 검증 전투 추적의 난수·프레임 기록·완료 통지를 한 실행 단위로 소유한다. */
internal class BattleTraceRuntimeSession(
    private val configuration: BattleTraceRuntimeConfig,
    private val observer: RuntimeBattleObserver?,
) {
    /** randomSource: 추적 실행에서 전투에 주입할 결정적 원본 난수열이다. */
    val randomSource = BattleTraceRandomStreams(configuration.toolSeed, configuration.mathSeed)

    private val recorder = BattleTraceRecorder(randomSource)
    private var finished = false
    private var lastInput: String? = null
    private var lastMenuTap: String? = null

    val timeScale: Float get() = configuration.timeScale
    val exitOnFinish: Boolean get() = configuration.exitOnFinish
    val isFinished: Boolean get() = finished

    /** nextFrame: 일반 렌더 프레임 또는 그 직전 콜백의 trace 식별자를 반환한다. */
    fun nextFrame(elapsed: Float, advanceFrame: Boolean): Long =
        if (advanceFrame) recorder.nextFrame(elapsed) else recorder.upcomingFrame()

    /** record: 화면이 투영한 불변 trace view를 기록하고 선택적 관찰기에 전달한다. */
    fun record(view: RuntimeBattleTraceView) {
        recorder.addFrame(view.toString())
        observer?.onFrame(RuntimeBattleFrameSnapshot(view.frame, view.elapsed, view.delta, traceView = view))
    }

    /** recordInput: 수락된 입력 문맥을 현재 trace 행에 연결할 수 있도록 보존한다. */
    fun recordInput(context: String) {
        lastInput = context
        recorder.recordInput(context)
    }

    /** recordMenuTap: 메뉴 누름·해제 좌표를 trace 전용 진단값으로 저장한다. */
    fun recordMenuTap(pressed: Int?, released: Int?, x: Float, y: Float) {
        lastMenuTap = "${pressed ?: -1}/${released ?: -1}@${BattleTraceRecorder.number(x)},${BattleTraceRecorder.number(y)}"
    }

    /** driverInput: 화면이 구성한 trace view에 최근 자동 입력 관측값을 추가한다. */
    fun driverInput(
        selectedUnitId: String?,
        commandPhase: String,
        eventMessage: String,
        autoOverlay: String,
    ) = RuntimeBattleTraceDriverInput(
        selectedUnitId,
        commandPhase,
        lastInput,
        lastMenuTap,
        eventMessage,
        autoOverlay,
    )

    /** finish: 완료 통지는 한 번만 발생시키고 기존 종료 요청 계약을 보존한다. */
    fun finish(reason: String) {
        if (finished) return
        finished = true
        observer?.onCompleted(
            RuntimeBattleCompletion(reason, recorder.recordedRowCount, null, configuration.exitOnFinish)
        )
        Gdx.app.log("JojoGame", "BATTLE_TRACE: frames=${recorder.recordedRowCount}; reason=$reason")
    }
}
