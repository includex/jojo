package com.jojo.game

import java.util.Collections

/**
 * Owns the ordered unit topology used by tactical and presentation queries.
 *
 * Unit objects remain mutable battle entities; only structural changes to the
 * active and retained collections are confined to this class.
 */
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

    /** Active units win duplicate IDs and retained-only units follow in insertion order. */
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

    /** Restores a retained unit without changing an already-active duplicate. */
    fun restore(id: String): BattleUnit? {
        val unit = activeById[id] ?: retainedById.remove(id)?.also { activeById[id] = it } ?: return null
        unit.retreatFlag = false
        if (unit.hitPoints < 1) {
            unit.setHpcur(unit.maxHitPoints)
            unit.setMpcur(unit.maxMagicPoints)
            unit.statuses.clear()
            unit.attributeLifts.clear()
            unit.attributeLiftRounds.clear()
            unit.hasMoved = false
            unit.presentation.refreshStatus(unit.statuses, unit.attributeLifts)
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
