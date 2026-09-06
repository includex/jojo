package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioMapPresentationRequest

/** 스크립트 전투 명령의 맵 이동과 카메라 중심 이동을 조정합니다. */
internal class ScriptedUnitTimedCoordinator(
    private val lifecycle: ScriptedUnitPresentationLifecycle,
    private val port: Port,
) {
    internal interface Port {
        fun now(): Float
        fun consumeMap(): ScenarioMapPresentationRequest?
        fun focusMap(x: Int, y: Int)
        fun consumeCameraCenters(): List<CameraCenter>
        fun centerCamera(request: CameraCenter)
        fun resumeScript()
    }

    internal data class CameraCenter(val x: Int, val y: Int)

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
