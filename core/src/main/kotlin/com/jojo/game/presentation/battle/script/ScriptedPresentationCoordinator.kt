// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioScriptPresentationRequest

/** 시간 제한이 있는 스크립트 표시 요청과 콜백 효과를 조정합니다. */
internal class ScriptedPresentationCoordinator(
    private val timeline: ScriptPresentationTimeline,
    private val port: Port,
) {
    internal data class Target(val id: String, val direction: Int)

    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        fun now(): Float
        fun modalActive(): Boolean
        fun consumeRequest(): ScenarioScriptPresentationRequest?
        fun clearVisual(unitId: String)
        fun defaultAction(unitId: String)
        fun playGetItemSound()
        fun presentItemMessage(message: String)
        fun dismissUnitInfo()
        fun resumeScript()
        fun focusRectangle(x1: Int, y1: Int, x2: Int, y2: Int)
        fun unitTarget(unitId: Int): Target?
        fun focusUnit(unitId: String)
        fun openUnitInfo(unitId: Int)
        fun itemTarget(selector: Int): Target?
        fun setVisual(unitId: String, action: Int, startedAt: Float)
        fun sourceActionDuration(action: Int, direction: Int): Float
        fun focusMapObjects(request: ScenarioScriptPresentationRequest.MapObjects)
        fun statusTarget(values: List<Map<String, Any?>>): Target?
    }

    /** 현재 시각에 맞춰 효과를 처리하고 새 요청을 수락합니다. */
    fun drive() {
        val advance = timeline.advance(port.now(), port.modalActive())
        advance.effects.forEach { effect ->
            when (effect) {
                is ScriptPresentationTimeline.Effect.FinishUnitAction -> {
                    port.clearVisual(effect.battleUnitId)
                    port.defaultAction(effect.battleUnitId)
                }
                ScriptPresentationTimeline.Effect.PlayGetItemSound -> port.playGetItemSound()
                is ScriptPresentationTimeline.Effect.PresentItemMessage -> port.presentItemMessage(effect.message)
                ScriptPresentationTimeline.Effect.DismissUnitInfo -> port.dismissUnitInfo()
                ScriptPresentationTimeline.Effect.ResumeScript -> port.resumeScript()
            }
        }
        if (!advance.acceptsNewRequest) return
        val request = port.consumeRequest() ?: return
        val now = port.now()
        when (request) {
            is ScenarioScriptPresentationRequest.RectangleHighlight -> {
                port.focusRectangle(request.x1, request.y1, request.x2, request.y2)
                timeline.startTimed(request, now, request.durationSeconds)
            }
            is ScenarioScriptPresentationRequest.UnitHighlight -> {
                val target = port.unitTarget(request.unitId)
                if (target == null) return port.resumeScript()
                port.focusUnit(target.id)
                if (request.opensUnitInfo) port.openUnitInfo(request.unitId)
                timeline.startTimed(request, now, request.durationSeconds, target.id)
            }
            is ScenarioScriptPresentationRequest.MapObjects -> {
                port.focusMapObjects(request)
                timeline.startTimed(request, now, request.durationSeconds)
            }
            is ScenarioScriptPresentationRequest.GetItem -> {
                val target = port.itemTarget(request.unitSelector)
                if (target == null) return port.resumeScript()
                port.focusUnit(target.id)
                port.setVisual(target.id, request.action, now)
                timeline.startItem(request, now, port.sourceActionDuration(request.action, target.direction), target.id)
            }
            is ScenarioScriptPresentationRequest.UnitStatusSettlement -> {
                val target = port.statusTarget(request.values)
                target?.let { port.focusUnit(it.id) }
                val duration = request.values.maxOfOrNull { change ->
                    val hp = kotlin.math.abs((change["hp"] as? Number)?.toInt() ?: 0)
                    val mp = kotlin.math.abs((change["mp"] as? Number)?.toInt() ?: 0)
                    minOf(maxOf(hp, mp), 5) * .2f +
                        if (change.containsKey("status") || change.containsKey("hStatus")) .6f else 0f
                }?.coerceAtLeast(request.minimumDurationSeconds) ?: request.minimumDurationSeconds
                timeline.startTimed(request, now, duration, target?.id)
            }
        }
    }
}
