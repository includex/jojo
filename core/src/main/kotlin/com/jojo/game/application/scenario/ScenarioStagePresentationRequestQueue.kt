// Game
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.scenario.TacticalUnit

import java.util.*

/** ScenarioStagePresentationRequestQueue: 렌더러에 전달할 시나리오 표시 요청과 순서를 관리한다. */
internal class ScenarioStagePresentationRequestQueue {
    /**
     * `unitHideRequests` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val unitHideRequests = ArrayDeque<ScenarioUnitHideRequest>()
    /**
     * `unitShowRequest` (ScenarioUnitShowRequest?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var unitShowRequest: ScenarioUnitShowRequest? = null
    /**
     * `mapPresentationRequest` (ScenarioMapPresentationRequest?): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var mapPresentationRequest: ScenarioMapPresentationRequest? = null
    /**
     * `cameraCenterRequests` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val cameraCenterRequests = ArrayDeque<ScenarioCameraCenterRequest>()
    /**
     * `scriptPresentationRequests` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val scriptPresentationRequests = ArrayDeque<ScenarioScriptPresentationRequest>()

    /**
     * `requestUnitHide`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestUnitHide(unitId: Int, hideType: Int) {
        unitHideRequests.addLast(ScenarioUnitHideRequest(unitId, hideType.coerceIn(0, 2)))
    }

    /**
     * `consumeUnitHideRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? =
        if (unitHideRequests.isEmpty()) null else unitHideRequests.removeFirst()

    /**
     * `requestRectUnitHide`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun requestRectUnitHide(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        camp: Int,
        hideType: Int,
        battleUnits: Map<String, ScenarioBattleUnit>,
        mineMasterInstanceId: Int,
        matchesAiCamp: (ScenarioBattleUnit, Int) -> Boolean,
    ): Int {
        /**
         * `selected` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val selected = battleUnits.entries
            .filter { (_, unit) ->
                !unit.hidden && matchesAiCamp(unit, camp) &&
                        unit.x in minOf(x1, x2)..maxOf(x1, x2) &&
                        unit.y in minOf(y1, y2)..maxOf(y1, y2)
            }
            .sortedWith(compareBy({ it.value.y }, { it.value.x }))
        /**
         * `effectiveHideType` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var effectiveHideType = hideType.coerceIn(0, 2)
        selected.forEachIndexed { index, (_, unit) ->
            /**
             * `showsRetireMessage` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val showsRetireMessage = effectiveHideType == 1
            if (showsRetireMessage && unit.faction == ScenarioUnitFaction.MINE &&
                unit.characterId == mineMasterInstanceId
            ) effectiveHideType = 2
            unitHideRequests.addLast(
                ScenarioUnitHideRequest(
                    unitId = unit.characterId,
                    hideType = effectiveHideType,
                    battleUnitId = unit.battleId,
                    resumesScript = index == selected.lastIndex,
                    showsRetireMessage = showsRetireMessage,
                ),
            )
        }
        return selected.size
    }

    /**
     * `completeUnitHide`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun completeUnitHide(
        request: ScenarioUnitHideRequest,
        battleUnits: Map<String, ScenarioBattleUnit>,
        unitProvider: (Int) -> TacticalUnit,
        setBattleUnitVisibility: (Int, Boolean) -> Unit,
    ) {
        /**
         * `exact` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val exact = request.battleUnitId
        if (exact == null) {
            setBattleUnitVisibility(request.unitId, false)
            return
        }
        battleUnits.values.firstOrNull { it.battleId == exact }?.hidden = true
        unitProvider(request.unitId).visible = battleUnits.values.any {
            it.characterId == request.unitId && !it.hidden
        }
    }

    /**
     * `requestUnitShow`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestUnitShow(request: ScenarioUnitShowRequest, battleUnitForCharacterId: (Int) -> ScenarioBattleUnit?) {
        check(unitShowRequest == null) { "unit show callback is already pending" }
        unitShowRequest = request
        battleUnitForCharacterId(request.unitId)?.hidden = false
    }

    /**
     * `consumeUnitShowRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitShowRequest(): ScenarioUnitShowRequest? = unitShowRequest.also { unitShowRequest = null }

    /**
     * `requestMapPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestMapPresentation(request: ScenarioMapPresentationRequest) {
        check(mapPresentationRequest == null) { "map presentation callback is already pending" }
        mapPresentationRequest = request
    }

    /**
     * `consumeMapPresentationRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? =
        mapPresentationRequest.also { mapPresentationRequest = null }

    /**
     * `requestCameraCenter`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestCameraCenter(x: Int, y: Int) {
        cameraCenterRequests.addLast(ScenarioCameraCenterRequest(x, y))
    }

    /**
     * `consumeCameraCenterRequests`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> =
        cameraCenterRequests.toList().also { cameraCenterRequests.clear() }

    /**
     * `requestScriptPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) {
        scriptPresentationRequests.addLast(request)
    }

    /**
     * `consumeScriptPresentationRequest`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? =
        if (scriptPresentationRequests.isEmpty()) null else scriptPresentationRequests.removeFirst()

    /**
     * `consumeScriptPresentationRequests`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> =
        scriptPresentationRequests.toList().also { scriptPresentationRequests.clear() }
}
