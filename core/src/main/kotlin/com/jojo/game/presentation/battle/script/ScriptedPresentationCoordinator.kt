// Battle
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioScriptPresentationRequest

/** 시간 제한이 있는 스크립트 표시 요청과 콜백 효과를 조정합니다. */
internal class ScriptedPresentationCoordinator(
    /** `timeline` (ScriptPresentationTimeline): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val timeline: ScriptPresentationTimeline,
    /** `port` (Port): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val port: Port,
) {
    /**
     * `Target`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    internal data class Target(val id: String, val direction: Int)

    /** Port: 전투 표현 계층이 외부 기능과 연결할 때 사용하는 계약이다. */
    internal interface Port {
        /**
         * `now`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun now(): Float
        /**
         * `modalActive`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun modalActive(): Boolean
        /**
         * `consumeRequest`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun consumeRequest(): ScenarioScriptPresentationRequest?
        /**
         * `clearVisual`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun clearVisual(unitId: String)
        /**
         * `defaultAction`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun defaultAction(unitId: String)
        /**
         * `playGetItemSound`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun playGetItemSound()
        /**
         * `presentItemMessage`: 화면 표시 상태를 렌더링한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun presentItemMessage(message: String)
        /**
         * `dismissUnitInfo`: 조건과 입력 상태를 검증한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun dismissUnitInfo()
        /**
         * `resumeScript`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun resumeScript()
        /**
         * `focusRectangle`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun focusRectangle(x1: Int, y1: Int, x2: Int, y2: Int)
        /**
         * `unitTarget`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun unitTarget(unitId: Int): Target?
        /**
         * `focusUnit`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun focusUnit(unitId: String)
        /**
         * `openUnitInfo`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun openUnitInfo(unitId: Int)
        /**
         * `itemTarget`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun itemTarget(selector: Int): Target?
        /**
         * `setVisual`: 현재 상태를 갱신한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun setVisual(unitId: String, action: Int, startedAt: Float)
        /**
         * `sourceActionDuration`: 타입의 핵심 동작을 수행한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun sourceActionDuration(action: Int, direction: Int): Float
        /**
         * `focusMapObjects`: 입력을 규칙에 따라 계산·변환한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

        fun focusMapObjects(request: ScenarioScriptPresentationRequest.MapObjects)
        /**
         * `statusTarget`: 상태나 데이터를 조회한다.
         * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
         */

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
