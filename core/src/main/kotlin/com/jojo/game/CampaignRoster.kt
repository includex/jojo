package com.jojo.game

import java.util.Collections

/** Owns the ordered battle selection and its authored Hall entry rules. */
class CampaignRoster internal constructor(
    private val joinedUnitIds: () -> Collection<Int>,
) {
    internal constructor() : this({ emptyList() })

    private val selectedUnitIds = mutableListOf<Int>()
    val battleRoster: List<Int> = Collections.unmodifiableList(selectedUnitIds)

    internal fun reset() {
        selectedUnitIds.clear()
    }

    /** Seeds a deterministic startup or capture roster without Hall validation. */
    internal fun seedStartupRoster(unitIds: Iterable<Int>) {
        selectedUnitIds.clear()
        selectedUnitIds.addAll(unitIds)
    }

    /** Rehydrates persisted roster order after aggregate reset. */
    internal fun restoreBattleRoster(unitIds: Iterable<Int>) {
        selectedUnitIds.clear()
        selectedUnitIds.addAll(unitIds)
    }

    /** Resolves Hall selection bounds while preserving the authored direct-battle fast path. */
    fun resolveBattleEntry(limit: ScenarioJoinBattleLimit): ScenarioBattleEntryPlan {
        val excluded = limit.excludedUnitIds.distinct()
        val available = joinedUnitIds().filterNot { it in excluded }
        val mandatory = buildList {
            if (0 in available) add(0)
            limit.requiredUnitIds.forEach { id ->
                if (id in available && id !in this) add(id)
            }
        }
        val hallMaximum = minOf(limit.maximum.coerceAtLeast(0), available.size)
        // The direct comparison uses the authored maximum before UI availability and size caps.
        val direct = mandatory.takeIf { it.size >= limit.maximum }
        val uiMaximum = minOf(hallMaximum, 20)
        val uiMinimum = if (uiMaximum > 0) maxOf(1, 2 * (uiMaximum / 3)) else 0
        return ScenarioBattleEntryPlan(
            selectionLimit = ScenarioJoinBattleLimit(uiMinimum, uiMaximum, mandatory, excluded),
            directBattleRoster = direct,
        )
    }

    fun configureBattleRoster(limit: ScenarioJoinBattleLimit): ScenarioBattleEntryPlan {
        val plan = resolveBattleEntry(limit)
        val effective = plan.selectionLimit
        val available = joinedUnitIds().filterNot { it in effective.excludedUnitIds }
        val roster = plan.directBattleRoster ?: (effective.requiredUnitIds +
            available.filterNot { it in effective.requiredUnitIds }).take(effective.maximum)
        selectedUnitIds.clear()
        selectedUnitIds += roster
        return plan
    }

    fun setBattleRoster(selection: Collection<Int>, limit: ScenarioJoinBattleLimit): Boolean {
        val distinct = selection.distinct()
        val available = (joinedUnitIds() + limit.requiredUnitIds)
            .filterNot { it in limit.excludedUnitIds }
            .toSet()
        if (distinct.size !in limit.minimum..limit.maximum ||
            !distinct.containsAll(limit.requiredUnitIds) ||
            distinct.any { it !in available }
        ) return false
        selectedUnitIds.clear()
        selectedUnitIds += distinct
        return true
    }

    /** Fills the R_00 single-unit roster without replacing an existing selection. */
    fun prepareImplicitSingleUnitBattle(): Boolean {
        if (selectedUnitIds.isNotEmpty()) return true
        val joined = joinedUnitIds()
        if (joined.size != 1 || 0 !in joined) return false
        selectedUnitIds += 0
        return true
    }
}
