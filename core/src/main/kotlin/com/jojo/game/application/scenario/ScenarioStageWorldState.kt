// Game
package com.jojo.game.application.scenario

import com.jojo.game.*

import com.jojo.game.domain.scenario.*

import com.jojo.game.domain.scenario.ScenarioFire
import com.jojo.game.domain.scenario.ScenarioMapObject

/**
 * `ScenarioStageWorldAccess` 계약 인터페이스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

interface ScenarioStageWorldAccess {
    /**
     * `mapObjects` (LinkedHashMap<Pair<Int, Int>, ScenarioMapObject>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val mapObjects: LinkedHashMap<Pair<Int, Int>, ScenarioMapObject>
    /**
     * `mapObjectsCalls` (List<ScenarioMapObjectsCall>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val mapObjectsCalls: List<ScenarioMapObjectsCall>
    /**
     * `fires` (LinkedHashMap<Pair<Int, Int>, ScenarioFire>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val fires: LinkedHashMap<Pair<Int, Int>, ScenarioFire>
    /**
     * `itemVariables` (MutableList<Pair<List<Int>, List<String>>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val itemVariables: MutableList<Pair<List<Int>, List<String>>>
    /**
     * `acquiredItems` (MutableList<Int>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val acquiredItems: MutableList<Int>
    /**
     * `unitStatuses` (MutableList<Map<String, Any?>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val unitStatuses: MutableList<Map<String, Any?>>
    /**
     * `infoTransfers` (MutableList<Pair<Int, String>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val infoTransfers: MutableList<Pair<Int, String>>
    /**
     * `controlledInfos` (MutableList<Pair<Int, String>>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val controlledInfos: MutableList<Pair<Int, String>>

    /**
     * `setFire`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setFire(enabled: Boolean, x: Int, y: Int)
    /**
     * `setFires`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setFires(enabled: Boolean, positions: List<Any?>)
    /**
     * `setMapObjects`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setMapObjects(enabled: Boolean, terrainId: Int, positions: List<Any?>)
    /**
     * `setUnitStatuses`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun setUnitStatuses(values: List<Any?>): List<Map<String, Any?>>
    /**
     * `consumeUnitStatuses`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun consumeUnitStatuses(): List<Map<String, Any?>>
    /**
     * `addItemVariables`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun addItemVariables(items: List<Any?>, locations: List<Any?>)
    /**
     * `controlledInfo`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun controlledInfo(type: Int, text: String)
}

/**
 * `ScenarioStageWorldState` 클래스: scenario 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

class ScenarioStageWorldState : ScenarioStageWorldAccess {
    /**
     * `mapObjects` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val mapObjects = linkedMapOf<Pair<Int, Int>, ScenarioMapObject>()
    /**
     * `mapObjectsCallJournal` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val mapObjectsCallJournal = mutableListOf<ScenarioMapObjectsCall>()
    /**
     * `mapObjectsCalls` (List<ScenarioMapObjectsCall> get()): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val mapObjectsCalls: List<ScenarioMapObjectsCall> get() = mapObjectsCallJournal
    /**
     * `fires` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val fires = linkedMapOf<Pair<Int, Int>, ScenarioFire>()
    /**
     * `itemVariables` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val itemVariables = mutableListOf<Pair<List<Int>, List<String>>>()
    /**
     * `acquiredItems` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val acquiredItems = mutableListOf<Int>()
    /**
     * `unitStatuses` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val unitStatuses = mutableListOf<Map<String, Any?>>()
    /**
     * `infoTransfers` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val infoTransfers = mutableListOf<Pair<Int, String>>()
    /**
     * `controlledInfos` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    override val controlledInfos = mutableListOf<Pair<Int, String>>()

    /**
     * `setFire`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun setFire(enabled: Boolean, x: Int, y: Int) {
        fires[x to y] = ScenarioFire(x, y, enabled)
    }

    /**
     * `setFires`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun setFires(enabled: Boolean, positions: List<Any?>) {
        positions.forEach { value ->
            val pair = value as? List<Any?> ?: return@forEach
            if (pair.size >= 2) setFire(enabled, pair[0].asInt(), pair[1].asInt())
        }
    }

    /**
     * `setMapObjects`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `setUnitStatuses`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun setUnitStatuses(values: List<Any?>): List<Map<String, Any?>> {
        val normalized = values.mapNotNull { value ->
            @Suppress("UNCHECKED_CAST")
            (value as? Map<*, *>)?.entries?.associate { it.key.toString() to it.value }
        }
        unitStatuses += normalized
        return normalized
    }

    /**
     * `consumeUnitStatuses`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun consumeUnitStatuses(): List<Map<String, Any?>> = unitStatuses.toList().also { unitStatuses.clear() }

    /**
     * `addItemVariables`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun addItemVariables(items: List<Any?>, locations: List<Any?>) {
        itemVariables += items.map { it.asInt() } to locations.map { it?.toString().orEmpty() }
    }

    /**
     * `controlledInfo`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    override fun controlledInfo(type: Int, text: String) {
        controlledInfos += type to text
    }
}
