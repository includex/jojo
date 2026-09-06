// Runtime
package com.jojo.game.application.runtime

import com.badlogic.gdx.Screen

/** RuntimeArtifactEvent: 실행 화면에서 수집한 검증·캡처 산출물을 관찰기에 전달하는 이벤트 묶음이다. */
sealed interface RuntimeArtifactEvent {
    val state: String?

    data class Frame(
        override val state: String?,
        val screen: Screen?,
    ) : RuntimeArtifactEvent

    data class EventLog(
        override val state: String?,
        val screen: Screen?,
    ) : RuntimeArtifactEvent

    data class MapSidecar(override val state: String?) : RuntimeArtifactEvent

    data class OverlayStack(
        override val state: String?,
        val requested: String,
        val requestedPresent: Boolean,
        val dialogue: Boolean,
        val choice: Boolean,
        val modalCount: Int,
    ) : RuntimeArtifactEvent
}

/** RuntimeArtifactObserver: 실행 중 생성된 프레임과 진단 산출물을 외부 검증기로 전달하는 확장 계약이다. */
interface RuntimeArtifactObserver {
    val wantsFrame: Boolean get() = false
    val wantsEventLog: Boolean get() = false
    fun onArtifact(event: RuntimeArtifactEvent)

    /** onFrame: 화면 프레임마다 탐침 상태를 관찰하여 필요한 검증 자료를 기록한다. */
    fun onFrame(screen: Screen?, probe: RuntimeScreenProbe) = Unit
}

/** RuntimeBattleFrameSnapshot: 전투 한 프레임의 시간·추적 정보를 관찰기용으로 고정한 값 객체다. */
data class RuntimeBattleFrameSnapshot(
    val frame: Long,
    val elapsed: Float,
    val delta: Float,
    val payload: String = "",
    val traceView: RuntimeBattleTraceView? = null,
)

/** RuntimeBattleCompletion: 전투 실행이 끝난 이유와 누적 프레임 수를 전달하는 완료 결과다. */
data class RuntimeBattleCompletion(
    val reason: String,
    val frameCount: Long,
    val payload: String? = null,
    val exitRequested: Boolean = false,
)

/** RuntimeBattleObserver: 전투 프레임과 종료 결과를 독립된 검증 도구에 알리는 콜백 계약이다. */
interface RuntimeBattleObserver {
    fun onFrame(snapshot: RuntimeBattleFrameSnapshot) {}
    fun onCompleted(completion: RuntimeBattleCompletion) {}
}
