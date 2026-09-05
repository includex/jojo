package com.jojo.game.presentation.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.presentation.battle.unit.BattleUnitPresentationState

/**
 * Per-[BattleScreen] visual state for tactical units.
 *
 * Battle units remain domain objects.  This store owns the corresponding
 * renderer state for one screen instance and projects HP, abnormal statuses,
 * and attribute lifts just before the screen consumes them.
 */
class BattleUnitPresentationStore {
    private data class DerivedState(
        val hitPoints: Int,
        val maxHitPoints: Int,
        val statuses: Map<BattleStatus, Int>,
        val attributeLifts: Map<BattleAttribute, Int>,
    )

    private val states = linkedMapOf<String, BattleUnitPresentationState>()
    private val derivedStates = linkedMapOf<String, DerivedState>()

    /** Returns this screen's state for [unit], synchronizing domain-derived visuals first. */
    fun stateFor(unit: BattleUnit): BattleUnitPresentationState {
        val state = states.getOrPut(unit.id) {
            BattleUnitPresentationState(unit.hitPoints, unit.maxHitPoints)
        }
        val next = DerivedState(
            hitPoints = unit.hitPoints,
            maxHitPoints = unit.maxHitPoints,
            statuses = unit.statuses.toMap(),
            attributeLifts = unit.attributeLifts.toMap(),
        )
        val previous = derivedStates[unit.id]
        if (previous == null ||
            previous.hitPoints != next.hitPoints || previous.maxHitPoints != next.maxHitPoints
        ) {
            state.refreshHpBar(next.hitPoints, next.maxHitPoints)
        }
        // Re-running refreshStatus with an unchanged map re-enables a hidden
        // effect. Preserve HideState until the authored Refresh operation or
        // a genuine abnormal-status change reaches the screen. Attribute-only
        // projection updates its icons without touching that effect.
        if (previous == null || previous.statuses != next.statuses) {
            state.refreshStatus(next.statuses, next.attributeLifts)
        } else if (previous.attributeLifts != next.attributeLifts) {
            state.refreshAttributeStatusIcons(next.attributeLifts)
        }
        derivedStates[unit.id] = next
        return state
    }

    /** Synchronizes each unit that is projected in this frame. */
    fun synchronize(units: Iterable<BattleUnit>) {
        val retainedIds = linkedSetOf<String>()
        units.forEach { unit ->
            retainedIds += unit.id
            stateFor(unit)
        }
        // Presentation-only units may remain while a death callback runs, but
        // after BattleScreen no longer projects them their screen-local state
        // must not survive the removed battle actor.
        states.keys.retainAll(retainedIds)
        derivedStates.keys.retainAll(retainedIds)
    }

    /** Executes an authored presentation refresh even when domain values are unchanged. */
    fun refresh(unit: BattleUnit): BattleUnitPresentationState {
        derivedStates.remove(unit.id)
        return stateFor(unit)
    }

    /** Releases every visual projection when its owning BattleScreen is disposed. */
    fun clear() {
        states.clear()
        derivedStates.clear()
    }
}
