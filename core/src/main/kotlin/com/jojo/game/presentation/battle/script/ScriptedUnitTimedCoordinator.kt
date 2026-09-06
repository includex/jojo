// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioMapPresentationRequest

/** 스크립트 전투 명령의 맵 이동과 카메라 중심 이동을 조정합니다. */
internal class ScriptedUnitTimedCoordinator(
    /** `lifecycle` (ScriptedUnitPresentationLifecycle): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val lifecycle: ScriptedUnitPresentationLifecycle,
    /** `port` (Port): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val port: Port,
) {
    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        /**
         * `now`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun now(): Float
        /**
         * `consumeMap`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun consumeMap(): ScenarioMapPresentationRequest?
        /**
         * `focusMap`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun focusMap(x: Int, y: Int)
        /**
         * `consumeCameraCenters`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun consumeCameraCenters(): List<CameraCenter>
        /**
         * `centerCamera`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun centerCamera(request: CameraCenter)
        /**
         * `resumeScript`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun resumeScript()
    }
    /**
     * `CameraCenter`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class CameraCenter(val x: Int, val y: Int)

    /**
     * `busy` (Boolean get()): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val busy: Boolean get() = lifecycle.activeMap != null

    /** 예약된 맵 표시 요청을 진행합니다. */
    fun driveMap() {
        val active = lifecycle.activeMap
        if (active != null) {
            if (port.now() < active.endsAt) return
            lifecycle.finishMap()
            port.resumeScript()
            return
        }
        val request = port.consumeMap() ?: return
        port.focusMap(request.x, request.y)
        lifecycle.startMap(request, port.now() + request.duration)
    }

    /** 대기 중인 카메라 중심 이동을 적용합니다. */
    fun driveCameraCenters() {
        port.consumeCameraCenters().forEach(port::centerCamera)
    }
}
