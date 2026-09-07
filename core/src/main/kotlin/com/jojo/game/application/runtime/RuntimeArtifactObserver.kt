// Runtime
package com.jojo.game.application.runtime

import com.badlogic.gdx.Screen

/** RuntimeArtifactEvent: 실행 화면에서 수집한 검증·캡처 산출물을 관찰기에 전달하는 이벤트 묶음이다. */
sealed interface RuntimeArtifactEvent {
    /**
     * `state` (String?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val state: String?

    /**
     * `Frame` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class Frame(
        /**
         * `state` (String?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val state: String?,
        /**
         * `screen` (Screen?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val screen: Screen?,
    ) : RuntimeArtifactEvent

    /**
     * `EventLog` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class EventLog(
        /**
         * `state` (String?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val state: String?,
        /**
         * `screen` (Screen?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val screen: Screen?,
    ) : RuntimeArtifactEvent

    /**
     * `MapSidecar` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class MapSidecar(override val state: String?) : RuntimeArtifactEvent

    /**
     * `OverlayStack` 클래스: runtime 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class OverlayStack(
        /**
         * `state` (String?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        override val state: String?,
        /**
         * `requested` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val requested: String,
        /**
         * `requestedPresent` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val requestedPresent: Boolean,
        /**
         * `dialogue` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val dialogue: Boolean,
        /**
         * `choice` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val choice: Boolean,
        /**
         * `modalCount` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val modalCount: Int,
    ) : RuntimeArtifactEvent
}

/** RuntimeArtifactObserver: 실행 중 생성된 프레임과 진단 산출물을 외부 검증기로 전달하는 확장 계약이다. */
interface RuntimeArtifactObserver {
    /**
     * `wantsFrame` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val wantsFrame: Boolean get() = false
    /**
     * `wantsEventLog` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val wantsEventLog: Boolean get() = false
    /**
     * `onArtifact`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

/** RuntimeBattleTraceFinish: 추적 종료 시점의 요약 근거를 화면 구현을 노출하지 않고 관찰기로 넘기는 값 객체다. */
data class RuntimeBattleTraceFinish(
    /** scenario: 실제로 적재된 전투 스크립트 이름이다. */
    val scenario: String,
    /** requestedScenario: 실행기가 요청한 시나리오 이름이다. */
    val requestedScenario: String,
    /** round: 종료 시점의 전투 진행 차수다. */
    val round: Int,
    /** camp: 종료 시점의 활성 진영 서수다. */
    val camp: Int,
    /** ended: 전투가 종국에 도달했는지 나타낸다. */
    val ended: Boolean,
    /** outcome: 승패 결과이며 미결이면 null이다. */
    val outcome: String?,
    /** seededUnitIds: 스크립트가 배치한 유닛 식별자 목록이다. */
    val seededUnitIds: List<Int>,
    /** loadedMapIndex: 적재된 전장 지도 번호다. */
    val loadedMapIndex: Int,
    /** mapName: 적재된 전장 지도 텍스처 이름이다. */
    val mapName: String,
    /** mapWidth: 전장 지도의 가로 픽셀 크기다. */
    val mapWidth: Int,
    /** mapHeight: 전장 지도의 세로 픽셀 크기다. */
    val mapHeight: Int,
)

/** RuntimeBattleCompletion: 전투 실행이 끝난 이유와 누적 프레임 수를 전달하는 완료 결과다. */
data class RuntimeBattleCompletion(
    val reason: String,
    val frameCount: Long,
    val payload: String? = null,
    val exitRequested: Boolean = false,
    /** inputs: 운영 입력 처리기를 통과한 조작 흔적이며 추적 봉투의 입력 출처가 된다. */
    val inputs: List<String> = emptyList(),
    /** finish: 추적 봉투의 summary를 구성할 종료 근거이며, 없으면 요약을 만들 수 없다. */
    val finish: RuntimeBattleTraceFinish? = null,
)

/** RuntimeBattleObserver: 전투 프레임과 종료 결과를 독립된 검증 도구에 알리는 콜백 계약이다. */
interface RuntimeBattleObserver {
    /**
     * `onFrame`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun onFrame(snapshot: RuntimeBattleFrameSnapshot) {}
    /**
     * `onCompleted`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun onCompleted(completion: RuntimeBattleCompletion) {}
}
