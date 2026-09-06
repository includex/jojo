// Battle
package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit

import java.util.*

/** Battlefield: 전투에 참여하는 유닛과 점유 타일을 관리하며, 위치 변경과 전장 복원을 수행한다. */
internal class Battlefield(initialUnits: Iterable<BattleUnit>) {
    private val activeById = linkedMapOf<String, BattleUnit>()
    private val retainedById = linkedMapOf<String, BattleUnit>()
    private val activeView: Map<String, BattleUnit> = Collections.unmodifiableMap(activeById)
    private val retainedView: Map<String, BattleUnit> = Collections.unmodifiableMap(retainedById)

    init {
        initialUnits.forEach { activeById[it.id] = it }
    }

    val activeMap: Map<String, BattleUnit>
        get() = activeView


    fun activeUnit(id: String): BattleUnit? = activeById[id]


    fun unitAt(tileX: Int, tileY: Int): BattleUnit? =
        activeById.values.firstOrNull { it.visible && it.tileX == tileX && it.tileY == tileY }


    fun presentationUnit(id: String): BattleUnit? = activeById[id] ?: retainedById[id]


    fun pendingPresentationUnits(): Collection<BattleUnit> = retainedView.values
    fun allPresentationUnits(): List<BattleUnit> = buildList {
        val seen = mutableSetOf<String>()
        activeById.values.forEach { unit ->
            add(unit)
            seen += unit.id
        }
        retainedById.values.filterTo(this) { seen.add(it.id) }
    }


    fun add(unit: BattleUnit) {
        check(unit.id !in activeById) { "이미 존재하는 유닛: ${unit.id}" }
        activeById[unit.id] = unit
    }


    fun defeat(id: String) {
        activeById.remove(id)?.let { retainedById[id] = it }
    }


    fun clearRetained(id: String) {
        retainedById.remove(id)
    }


    fun hideForPresentation(id: String) {
        presentationUnit(id)?.visible = false
    }
    fun restore(id: String): BattleUnit? {
        val unit = activeById[id] ?: retainedById.remove(id)?.also { activeById[id] = it } ?: return null
        unit.retreatFlag = false
        if (unit.hitPoints < 1) {
            unit.resetAfterRetreat()
        }
        unit.visible = true
        return unit
    }


    data class TopologySnapshot(
        val activeIds: List<String>,
        val retainedIds: List<String>,
    )


    fun snapshotTopology(): TopologySnapshot = TopologySnapshot(
        activeIds = activeById.keys.toList(),
        retainedIds = retainedById.keys.toList(),
    )


    fun restoreTopology(snapshot: TopologySnapshot, unitsById: Map<String, BattleUnit>) {
        activeById.clear()
        snapshot.activeIds.forEach { id -> unitsById[id]?.let { activeById[id] = it } }
        retainedById.clear()
        snapshot.retainedIds.forEach { id -> unitsById[id]?.let { retainedById[id] = it } }
    }
}
