// Game
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

import com.jojo.game.domain.scenario.ScenarioFire
import com.jojo.game.domain.scenario.ScenarioMapObject

interface ScenarioStageWorldAccess {
    val mapObjects: LinkedHashMap<Pair<Int, Int>, ScenarioMapObject>
    val mapObjectsCalls: List<ScenarioMapObjectsCall>
    val fires: LinkedHashMap<Pair<Int, Int>, ScenarioFire>
    val itemVariables: MutableList<Pair<List<Int>, List<String>>>
    val acquiredItems: MutableList<Int>
    val unitStatuses: MutableList<Map<String, Any?>>
    val infoTransfers: MutableList<Pair<Int, String>>
    val controlledInfos: MutableList<Pair<Int, String>>

    fun setFire(enabled: Boolean, x: Int, y: Int)
    fun setFires(enabled: Boolean, positions: List<Any?>)
    fun setMapObjects(enabled: Boolean, terrainId: Int, positions: List<Any?>)
    fun setUnitStatuses(values: List<Any?>): List<Map<String, Any?>>
    fun consumeUnitStatuses(): List<Map<String, Any?>>
    fun addItemVariables(items: List<Any?>, locations: List<Any?>)
    fun controlledInfo(type: Int, text: String)
}

class ScenarioStageWorldState : ScenarioStageWorldAccess {
    override val mapObjects = linkedMapOf<Pair<Int, Int>, ScenarioMapObject>()
    private val mapObjectsCallJournal = mutableListOf<ScenarioMapObjectsCall>()
    override val mapObjectsCalls: List<ScenarioMapObjectsCall> get() = mapObjectsCallJournal
    override val fires = linkedMapOf<Pair<Int, Int>, ScenarioFire>()
    override val itemVariables = mutableListOf<Pair<List<Int>, List<String>>>()
    override val acquiredItems = mutableListOf<Int>()
    override val unitStatuses = mutableListOf<Map<String, Any?>>()
    override val infoTransfers = mutableListOf<Pair<Int, String>>()
    override val controlledInfos = mutableListOf<Pair<Int, String>>()

    override fun setFire(enabled: Boolean, x: Int, y: Int) {
        fires[x to y] = ScenarioFire(x, y, enabled)
    }

    override fun setFires(enabled: Boolean, positions: List<Any?>) {
        positions.forEach { value ->
            val pair = value as? List<Any?> ?: return@forEach
            if (pair.size >= 2) setFire(enabled, pair[0].asInt(), pair[1].asInt())
        }
    }

    override fun setMapObjects(enabled: Boolean, terrainId: Int, positions: List<Any?>) {
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

    override fun setUnitStatuses(values: List<Any?>): List<Map<String, Any?>> {
        val normalized = values.mapNotNull { value ->
            @Suppress("UNCHECKED_CAST")
            (value as? Map<*, *>)?.entries?.associate { it.key.toString() to it.value }
        }
        unitStatuses += normalized
        return normalized
    }

    override fun consumeUnitStatuses(): List<Map<String, Any?>> = unitStatuses.toList().also { unitStatuses.clear() }

    override fun addItemVariables(items: List<Any?>, locations: List<Any?>) {
        itemVariables += items.map { it.asInt() } to locations.map { it?.toString().orEmpty() }
    }

    override fun controlledInfo(type: Int, text: String) {
        controlledInfos += type to text
    }
}
