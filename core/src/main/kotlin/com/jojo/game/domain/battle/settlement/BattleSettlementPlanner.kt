package com.jojo.game.domain.battle.settlement

import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.MagicLocalSettlement
import com.jojo.game.domain.battle.isPlayerSide

/** Immutable unit data required by the pure settlement planner. */
data class SettlementUnitSnapshot(
    val id: String,
    val baseFaction: Faction,
    val skillIds: Set<Int>,
    val hasLostStatus: Boolean,
)

object BattleSettlementPlanner {
    fun plan(
        settlement: CampSettlement,
        unitsById: Map<String, SettlementUnitSnapshot>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan = planInternal(settlement, unitsById, resolveMeffId, discoverPending = true)

    fun planMagicLocal(
        settlement: MagicLocalSettlement,
        camp: Faction,
        unitsById: Map<String, SettlementUnitSnapshot>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan {
        val changes = settlement.entries.map { entry ->
            BattleUnitTurnChange(
                entry.targetId, 0, 0, 0, 0,
                entry.statusesBefore, entry.statusesAfter,
                entry.attributeLiftsBefore, entry.attributeLiftsAfter,
            )
        }
        val normal = planInternal(
            CampSettlement(CampSettlementStage.START_STATE, camp, changes, subflowsCaptured = true),
            unitsById, resolveMeffId, discoverPending = false,
        )
        return normal.copy(units = normal.units.zip(settlement.entries).map { (unit, entry) ->
            unit.copy(hasStatesPayload = entry.hasStatesPayload)
        })
    }

    private fun planInternal(
        settlement: CampSettlement,
        unitsById: Map<String, SettlementUnitSnapshot>,
        resolveMeffId: (SettlementStateChange) -> Int?,
        discoverPending: Boolean,
    ): BattleSettlementPlan {
        val plans = settlement.changes.map { change ->
            val unit = requireNotNull(unitsById[change.unitId]) { "settlement unit is unavailable: ${change.unitId}" }
            val infoDeltas = buildList {
                if (change.hitPointsBefore != change.hitPointsAfter) add(
                    SettlementInfoDelta(SettlementInfoKind.HP, change.hitPointsBefore, change.hitPointsAfter)
                )
                if (change.magicPointsBefore != change.magicPointsAfter) add(
                    SettlementInfoDelta(SettlementInfoKind.MP, change.magicPointsBefore, change.magicPointsAfter)
                )
            }
            val stateChanges = stateChanges(change)
            val beforeFaction = effectiveFaction(unit.baseFaction, BattleStatus.LOST in change.statusesBefore)
            val afterFaction = effectiveFaction(unit.baseFaction, BattleStatus.LOST in change.statusesAfter)
            SettlementUnitPlan(
                change.unitId, unit.baseFaction, beforeFaction, afterFaction,
                infoDeltas.takeIf { it.isNotEmpty() }?.let {
                    if (afterFaction == Faction.PLAYER) SettlementInfoPanel.MINE else SettlementInfoPanel.OTHER
                },
                infoDeltas, stateChanges,
            )
        }
        data class PendingBucket(val key: SettlementMeffKey, val targets: MutableList<SettlementMeffTarget> = mutableListOf())
        val buckets = linkedMapOf<Int, PendingBucket>()
        plans.forEach { unit ->
            unit.stateChanges.firstNotNullOfOrNull { state -> resolveMeffId(state)?.let { state to it } }
                ?.let { (state, actualId) ->
                    val key = SettlementMeffKey(state.sourceStatusIndex, state.meffSlot, actualId)
                    buckets.getOrPut(actualId) { PendingBucket(key) }.targets += SettlementMeffTarget(unit.unitId, state)
                }
        }
        return BattleSettlementPlan(
            settlement.stage, settlement.faction, plans,
            buckets.values.map { SettlementMeffBucket(it.key, it.targets) },
            if (discoverPending) pendingIntegrations(settlement, unitsById.values) else emptyList(),
            authoredSubflows(settlement, unitsById, resolveMeffId),
        )
    }

    private fun stateChanges(change: BattleUnitTurnChange): List<SettlementStateChange> = buildList {
        linkedSetOf<BattleStatus>().apply {
            addAll(change.statusesBefore.keys)
            addAll(change.statusesAfter.keys)
        }.forEach { status ->
            val before = change.statusesBefore[status]
            val after = change.statusesAfter[status]
            if (before != after) add(
                SettlementStateChange(
                    status.sourceIndex,
                    when { before == null -> SettlementStateChangeKind.ADD; after == null -> SettlementStateChangeKind.REMOVE; else -> SettlementStateChangeKind.ROUND_UPDATE },
                    roundBefore = before, roundAfter = after,
                )
            )
        }
        BattleAttribute.entries.forEach { attribute ->
            val before = change.attributeLiftsBefore[attribute] ?: 0
            val after = change.attributeLiftsAfter[attribute] ?: 0
            if (before != after) add(SettlementStateChange(attribute.ordinal, SettlementStateChangeKind.LIFT, liftBefore = before, liftAfter = after))
        }
        if (change.actionCompleteBefore != change.actionCompleteAfter ||
            change.actionStatusRoundBefore != change.actionStatusRoundAfter
        ) add(
            SettlementStateChange(
                14,
                when {
                    !change.actionCompleteBefore && change.actionCompleteAfter -> SettlementStateChangeKind.ADD
                    change.actionCompleteBefore && !change.actionCompleteAfter -> SettlementStateChangeKind.REMOVE
                    else -> SettlementStateChangeKind.ROUND_UPDATE
                },
                change.actionStatusRoundBefore, change.actionStatusRoundAfter,
            )
        )
    }

    private fun authoredSubflows(
        settlement: CampSettlement,
        unitsById: Map<String, SettlementUnitSnapshot>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): List<SettlementAuthoredSubflowPlan> = settlement.subflows.map { subflow -> when (subflow) {
        is SettlementSubflow.LocalAura -> {
            val nested = planInternal(
                CampSettlement(settlement.stage, settlement.faction, subflow.nestedChanges, subflowsCaptured = true),
                unitsById, resolveMeffId, discoverPending = false,
            )
            SettlementAuthoredSubflowPlan.LocalAura(
                subflow.casterId, subflow.skillId, subflow.skillValue,
                buildList {
                    add(SettlementAuraStep.Focus(subflow.focusDelaySeconds))
                    add(SettlementAuraStep.Sound(subflow.soundIndex))
                    add(SettlementAuraStep.Info2(subflow.infoSkillId))
                    add(SettlementAuraStep.ActionFinished(subflow.actionId))
                    subflow.meffName?.let { add(SettlementAuraStep.PlayMeff(it, subflow.targets)) }
                    add(SettlementAuraStep.NestedSettlement)
                    add(SettlementAuraStep.DefaultAction)
                }, nested,
            )
        }
        is SettlementSubflow.Growth -> SettlementAuthoredSubflowPlan.Growth(
            subflow.unitId, subflow.grants,
            buildList {
                add(SettlementGrowthStep.InfoValues(subflow.grants))
                if (subflow.grants.any { it.unitResult?.leveledUp == true }) {
                    add(SettlementGrowthStep.UnitLevelUpActionFinished)
                    add(SettlementGrowthStep.UnitLevelUpInfo)
                    subflow.grants.flatMap { it.unitResult?.learnedMagicIds.orEmpty() }
                        .forEach { add(SettlementGrowthStep.LearnedMagicInfo(it)) }
                }
                subflow.grants.mapNotNull { it.equipmentResult }.filter { it.leveledUp }.forEach { result ->
                    add(SettlementGrowthStep.EquipmentLevelUpAction(result))
                    add(SettlementGrowthStep.EquipmentLevelUpInfo(result))
                    add(SettlementGrowthStep.ItemUpgradeCallback(result))
                }
                add(SettlementGrowthStep.DefaultAction)
            },
        )
    } }

    private fun pendingIntegrations(
        settlement: CampSettlement,
        units: Collection<SettlementUnitSnapshot>,
    ): List<SettlementPendingIntegration> = buildList {
        val unresolvedGrowth = settlement.subflows.filterIsInstance<SettlementSubflow.Growth>()
            .filter { it.grants.any { grant -> grant.unitResult == null && grant.equipmentResult == null } }
            .map { it.unitId }
        if (unresolvedGrowth.isNotEmpty()) add(SettlementPendingIntegration(SettlementPendingKind.EXPERIENCE_AND_LEVEL_UP, unresolvedGrowth))
        if (!settlement.subflowsCaptured && settlement.stage == CampSettlementStage.START_STATE &&
            settlement.faction in setOf(Faction.PLAYER, Faction.ENEMY)
        ) {
            val playerSide = settlement.faction.isPlayerSide()
            val ids = units.filter {
                effectiveFaction(it.baseFaction, it.hasLostStatus).isPlayerSide() == playerSide &&
                    it.skillIds.any { id -> id in setOf(103, 208, 209, 210) }
            }.map { it.id }
            if (ids.isNotEmpty()) add(SettlementPendingIntegration(SettlementPendingKind.LOCAL_AURA, ids))
        }
        if (!settlement.subflowsCaptured && settlement.stage == CampSettlementStage.END_RESTORE) {
            val ids = units.filter {
                effectiveFaction(it.baseFaction, it.hasLostStatus) == settlement.faction &&
                    it.skillIds.any { id -> id in setOf(149, 150, 151) }
            }.map { it.id }
            if (ids.isNotEmpty()) add(SettlementPendingIntegration(SettlementPendingKind.EXPERIENCE_AND_LEVEL_UP, ids))
        }
    }

    private fun effectiveFaction(base: Faction, lost: Boolean): Faction =
        if (!lost) base else if (base.isPlayerSide()) Faction.REINFORCEMENTS else Faction.FRIEND
}

val BattleStatus.sourceIndex: Int
    get() = when (this) {
        BattleStatus.PARALYSIS -> 7
        BattleStatus.SILENCE -> 8
        BattleStatus.CONFUSION -> 9
        BattleStatus.POISON -> 10
        BattleStatus.LOST -> 13
    }
