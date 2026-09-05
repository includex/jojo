package com.jojo.game.domain.scenario

/** Outbound, framework-free presentation requests emitted by a source scenario. */
data class ScenarioSoundEffect(val soundId: Int, val mode: Int)
data class ScenarioMapPresentationRequest(val x: Int, val y: Int, val duration: Float, val magicCallId: Int? = null)
data class ScenarioCameraCenterRequest(val x: Int, val y: Int)

sealed class ScenarioScriptPresentationRequest {
    data class RectangleHighlight(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int,
        val durationSeconds: Float = 2.4f,
    ) : ScenarioScriptPresentationRequest()

    data class UnitHighlight(
        val unitId: Int,
        val opensUnitInfo: Boolean = true,
        val durationSeconds: Float = 2.4f,
    ) : ScenarioScriptPresentationRequest()

    data class GetItem(
        val itemId: Int,
        val suppliedCountOrLevel: Int,
        val addToInventory: Boolean,
        val unitSelector: Int,
        val action: Int,
        val completionMessage: String,
    ) : ScenarioScriptPresentationRequest()

    data class MapObjects(
        val enabled: Boolean,
        val terrainId: Int,
        val objects: List<Object>,
        val soundOnFirstObjectOnly: Boolean,
        val durationSeconds: Float = if (objects.any { it.objectId >= 4 }) 3.5f else 1f,
    ) : ScenarioScriptPresentationRequest() {
        data class Object(val objectId: Int, val x: Int, val y: Int)
    }

    data class UnitStatusSettlement(
        val values: List<Map<String, Any?>>,
        val minimumDurationSeconds: Float = .1f,
    ) : ScenarioScriptPresentationRequest()
}
