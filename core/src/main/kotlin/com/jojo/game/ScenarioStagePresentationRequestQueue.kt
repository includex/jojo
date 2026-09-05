package com.jojo.game

import java.util.*

/** Owns renderer-facing scenario presentation requests and their delivery order. */
internal class ScenarioStagePresentationRequestQueue {
    private val unitHideRequests = ArrayDeque<ScenarioUnitHideRequest>()
    private var unitShowRequest: ScenarioUnitShowRequest? = null
    private var mapPresentationRequest: ScenarioMapPresentationRequest? = null
    private val cameraCenterRequests = ArrayDeque<ScenarioCameraCenterRequest>()
    private val scriptPresentationRequests = ArrayDeque<ScenarioScriptPresentationRequest>()

    fun requestUnitHide(unitId: Int, hideType: Int) {
        unitHideRequests.addLast(ScenarioUnitHideRequest(unitId, hideType.coerceIn(0, 2)))
    }

    fun consumeUnitHideRequest(): ScenarioUnitHideRequest? =
        if (unitHideRequests.isEmpty()) null else unitHideRequests.removeFirst()

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
        val selected = battleUnits.entries
            .filter { (_, unit) ->
                !unit.hidden && matchesAiCamp(unit, camp) &&
                        unit.x in minOf(x1, x2)..maxOf(x1, x2) &&
                        unit.y in minOf(y1, y2)..maxOf(y1, y2)
            }
            .sortedWith(compareBy({ it.value.y }, { it.value.x }))
        var effectiveHideType = hideType.coerceIn(0, 2)
        selected.forEachIndexed { index, (_, unit) ->
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

    fun completeUnitHide(
        request: ScenarioUnitHideRequest,
        battleUnits: Map<String, ScenarioBattleUnit>,
        unitProvider: (Int) -> TacticalUnit,
        setBattleUnitVisibility: (Int, Boolean) -> Unit,
    ) {
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

    fun requestUnitShow(request: ScenarioUnitShowRequest, battleUnitForCharacterId: (Int) -> ScenarioBattleUnit?) {
        check(unitShowRequest == null) { "unit show callback is already pending" }
        unitShowRequest = request
        battleUnitForCharacterId(request.unitId)?.hidden = false
    }

    fun consumeUnitShowRequest(): ScenarioUnitShowRequest? = unitShowRequest.also { unitShowRequest = null }

    fun requestMapPresentation(request: ScenarioMapPresentationRequest) {
        check(mapPresentationRequest == null) { "map presentation callback is already pending" }
        mapPresentationRequest = request
    }

    fun consumeMapPresentationRequest(): ScenarioMapPresentationRequest? =
        mapPresentationRequest.also { mapPresentationRequest = null }

    fun requestCameraCenter(x: Int, y: Int) {
        cameraCenterRequests.addLast(ScenarioCameraCenterRequest(x, y))
    }

    fun consumeCameraCenterRequests(): List<ScenarioCameraCenterRequest> =
        cameraCenterRequests.toList().also { cameraCenterRequests.clear() }

    fun requestScriptPresentation(request: ScenarioScriptPresentationRequest) {
        scriptPresentationRequests.addLast(request)
    }

    fun consumeScriptPresentationRequest(): ScenarioScriptPresentationRequest? =
        if (scriptPresentationRequests.isEmpty()) null else scriptPresentationRequests.removeFirst()

    fun consumeScriptPresentationRequests(): List<ScenarioScriptPresentationRequest> =
        scriptPresentationRequests.toList().also { scriptPresentationRequests.clear() }
}
