package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioMapPresentationRequest

/** Owns the map-center callback boundary used by scripted battle commands. */
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

    fun driveCameraCenters() {
        port.consumeCameraCenters().forEach(port::centerCamera)
    }
}
