package com.jojo.game

class ScenarioStageWorldState {
    val mapObjects = linkedMapOf<Pair<Int, Int>, ScenarioMapObject>()
    private val mapObjectsCallJournal = mutableListOf<ScenarioMapObjectsCall>()
    val mapObjectsCalls: List<ScenarioMapObjectsCall> get() = mapObjectsCallJournal
    val fires = linkedMapOf<Pair<Int, Int>, ScenarioFire>()
    val itemVariables = mutableListOf<Pair<List<Int>, List<String>>>()
    val acquiredItems = mutableListOf<Int>()
    val unitStatuses = mutableListOf<Map<String, Any?>>()
    val infoTransfers = mutableListOf<Pair<Int, String>>()
    val controlledInfos = mutableListOf<Pair<Int, String>>()

    fun setFire(enabled: Boolean, x: Int, y: Int) {
        fires[x to y] = ScenarioFire(x, y, enabled)
    }

    fun setFires(enabled: Boolean, positions: List<Any?>) {
        positions.forEach { value ->
            val pair = value as? List<Any?> ?: return@forEach
            if (pair.size >= 2) setFire(enabled, pair[0].asInt(), pair[1].asInt())
        }
    }

    fun setMapObjects(enabled: Boolean, terrainId: Int, positions: List<Any?>) {
        val objects = positions.mapNotNull { raw ->
            @Suppress("UNCHECKED_CAST")
            val values = raw as? List<Any?> ?: return@mapNotNull null
            if (values.size < 3) return@mapNotNull null
            ScenarioMapObjectsCall.Object(
                objectId = values[0].asInt(),
                x = values[1].asInt(),
                y = values[2].asInt(),
            )
        }
        mapObjectsCallJournal += ScenarioMapObjectsCall(enabled, terrainId, objects)
        objects.forEach { objectValue ->
            mapObjects[objectValue.x to objectValue.y] = ScenarioMapObject(
                objectValue.x, objectValue.y, objectValue.objectId, terrainId, enabled,
            )
        }
    }

    fun setUnitStatuses(values: List<Any?>): List<Map<String, Any?>> {
        val normalized = values.mapNotNull { value ->
            @Suppress("UNCHECKED_CAST")
            (value as? Map<*, *>)?.entries?.associate { it.key.toString() to it.value }
        }
        unitStatuses += normalized
        return normalized
    }

    fun consumeUnitStatuses(): List<Map<String, Any?>> = unitStatuses.toList().also { unitStatuses.clear() }

    fun addItemVariables(items: List<Any?>, locations: List<Any?>) {
        itemVariables += items.map { it.asInt() } to locations.map { it?.toString().orEmpty() }
    }

    fun controlledInfo(type: Int, text: String) {
        controlledInfos += type to text
    }
}
