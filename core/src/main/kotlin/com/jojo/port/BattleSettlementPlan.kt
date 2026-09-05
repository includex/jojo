package com.jojo.port

/** Renderer-independent `_jiesuan` plan. Logical values remain lossless. */
data class BattleSettlementPlan(
    val stage: CampSettlementStage,
    val camp: Faction,
    val units: List<SettlementUnitPlan>,
    /** Played only after every unit's info callback has completed. */
    val meffBuckets: List<SettlementMeffBucket>,
    /** Source branches whose mutations/presentation are not in CampSettlement. */
    val pendingIntegrations: List<SettlementPendingIntegration> = emptyList(),
    /** Always consumed before [units]/[meffBuckets], matching both source callers. */
    val authoredSubflows: List<SettlementAuthoredSubflowPlan> = emptyList(),
) {
    /** True when model capture retained every required source payload. */
    val sourceDataComplete: Boolean get() = pendingIntegrations.isEmpty()
    /**
     * Compatibility gate consumed by the current renderer. Authored subflows
     * remain false until its next integration teaches BattleLayer to execute
     * [authoredSubflows], preventing a silent callback skip in the meantime.
     */
    val fullyRepresented: Boolean get() = sourceDataComplete && authoredSubflows.isEmpty()
}

sealed interface SettlementAuthoredSubflowPlan {
    data class LocalAura(
        val casterId: String,
        val skillId: Int,
        val skillValue: Int,
        val steps: List<SettlementAuraStep>,
        val nestedSettlement: BattleSettlementPlan,
    ) : SettlementAuthoredSubflowPlan

    data class Growth(
        val unitId: String,
        val grants: List<SettlementGrowthGrant>,
        /** No duration is invented: every entry is an authored callback boundary. */
        val steps: List<SettlementGrowthStep>,
    ) : SettlementAuthoredSubflowPlan
}

sealed interface SettlementAuraStep {
    data class Focus(val seconds: Float) : SettlementAuraStep
    data class Sound(val soundIndex: Int) : SettlementAuraStep
    data class Info2(val skillId: Int) : SettlementAuraStep
    data class ActionFinished(val actionId: Int) : SettlementAuraStep
    data class PlayMeff(val semanticName: String, val targetIds: List<String>) : SettlementAuraStep
    data object NestedSettlement : SettlementAuraStep
    data object DefaultAction : SettlementAuraStep
}

sealed interface SettlementGrowthStep {
    data class InfoValues(val grants: List<SettlementGrowthGrant>) : SettlementGrowthStep
    data class AbilityLevelUp(val attribute: BattleAttribute) : SettlementGrowthStep
    data object UnitLevelUpActionFinished : SettlementGrowthStep
    data object UnitLevelUpInfo : SettlementGrowthStep
    data class LearnedMagicInfo(val magicId: Int) : SettlementGrowthStep
    data class EquipmentLevelUpAction(val result: CampaignEquipmentExperienceResult) : SettlementGrowthStep
    data class EquipmentLevelUpInfo(val result: CampaignEquipmentExperienceResult) : SettlementGrowthStep
    data class ItemUpgradeCallback(val result: CampaignEquipmentExperienceResult) : SettlementGrowthStep
    data object DefaultAction : SettlementGrowthStep
}

enum class SettlementPendingKind { LOCAL_AURA, EXPERIENCE_AND_LEVEL_UP }
data class SettlementPendingIntegration(val kind: SettlementPendingKind, val unitIds: List<String>)

enum class SettlementInfoKind { HP, MP }
enum class SettlementInfoPanel { MINE, OTHER }
enum class SettlementStateChangeKind { ADD, REMOVE, ROUND_UPDATE, LIFT }

data class SettlementInfoDelta(
    val kind: SettlementInfoKind,
    val before: Int,
    val after: Int,
) {
    val tickCount: Int get() = minOf(kotlin.math.abs(after - before), 5)
    val tickSeconds: Float get() = tickCount * .2f
}

data class SettlementStateChange(
    val sourceStatusIndex: Int,
    val kind: SettlementStateChangeKind,
    val roundBefore: Int? = null,
    val roundAfter: Int? = null,
    val liftBefore: Int? = null,
    val liftAfter: Int? = null,
) {
    /** stateExInfo `meff{slot}`; removal always uses MEFF0. */
    val meffSlot: Int get() = when {
        kind == SettlementStateChangeKind.REMOVE -> 0
        kind == SettlementStateChangeKind.LIFT -> when {
            (liftAfter ?: 0) < 0 -> 0
            (liftAfter ?: 0) > 0 -> 2
            else -> 1
        }
        else -> 1
    }
}

data class SettlementUnitPlan(
    val unitId: String,
    val baseFaction: Faction,
    val effectiveFactionBefore: Faction,
    val effectiveFactionAfter: Faction,
    val infoPanel: SettlementInfoPanel?,
    val infoDeltas: List<SettlementInfoDelta>,
    val stateChanges: List<SettlementStateChange>,
    /** True when source `STATES` key existed, even if its map was empty. */
    val hasStatesPayload: Boolean = stateChanges.isNotEmpty(),
    /** `_jiesuan` schedules this even when the info panel feature is hidden. */
    val preInfoDelaySeconds: Float = if (infoDeltas.isEmpty()) 0f else .1f,
    /** InfoBaseLayer `_over`. */
    val infoCloseSeconds: Float = if (infoDeltas.isEmpty()) 0f else .3f,
) {
    /** Excludes authored level-up subflows and the later state-MEFF buckets. */
    val infoBarrierSeconds: Float get() {
        val raw = preInfoDelaySeconds.toDouble() +
            infoDeltas.sumOf { it.tickSeconds.toDouble() } + infoCloseSeconds.toDouble()
        // Keep authored tenths stable instead of exposing accumulated Float
        // noise (for example 1.6000001) to timeline gates and trace output.
        return (kotlin.math.round(raw * 1_000.0) / 1_000.0).toFloat()
    }
}

/** Symbolic bucket key; the renderer resolves the actual GAME_CFG meff ID. */
data class SettlementMeffKey(
    val sourceStatusIndex: Int,
    val meffSlot: Int,
    val actualMeffId: Int? = null,
)
data class SettlementMeffTarget(val unitId: String, val state: SettlementStateChange)
data class SettlementMeffBucket(
    val key: SettlementMeffKey,
    val targets: List<SettlementMeffTarget>,
    /** Source starts all targets together and awaits the last target callback. */
    val simultaneousTargets: Boolean = true,
    val callbackTargetUnitId: String = targets.last().unitId,
)

object BattleSettlementPlanner {
    fun plan(
        settlement: CampSettlement,
        unitsById: Map<String, BattleUnit>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan = planInternal(settlement, unitsById, resolveMeffId, discoverPending = true)

    /**
     * `_magicProcess` builds a private `h` table and immediately invokes
     * `_jiesuan(h)`.  It has no camp lifecycle semantics, but it uses the
     * exact same unit/state operation plan.  HP/MP were already shown by the
     * preceding target/caster `playMeff` groups, so keep their deltas absent.
     */
    fun planMagicLocal(
        settlement: MagicLocalSettlement,
        camp: Faction,
        unitsById: Map<String, BattleUnit>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan {
        val changes = settlement.entries.map { entry ->
            BattleUnitTurnChange(
                unitId = entry.targetId,
                hitPointsBefore = 0,
                hitPointsAfter = 0,
                magicPointsBefore = 0,
                magicPointsAfter = 0,
                statusesBefore = entry.statusesBefore,
                statusesAfter = entry.statusesAfter,
                attributeLiftsBefore = entry.attributeLiftsBefore,
                attributeLiftsAfter = entry.attributeLiftsAfter,
            )
        }
        val normal = planInternal(
            CampSettlement(CampSettlementStage.START_STATE, camp, changes, subflowsCaptured = true),
            unitsById,
            resolveMeffId,
            discoverPending = false,
        )
        return normal.copy(units = normal.units.zip(settlement.entries).map { (unit, entry) ->
            unit.copy(hasStatesPayload = entry.hasStatesPayload)
        })
    }

    private fun planInternal(
        settlement: CampSettlement,
        unitsById: Map<String, BattleUnit>,
        resolveMeffId: (SettlementStateChange) -> Int?,
        discoverPending: Boolean,
    ): BattleSettlementPlan {
        val plans = settlement.changes.map { change ->
            val unit = requireNotNull(unitsById[change.unitId]) { "settlement unit is unavailable: ${change.unitId}" }
            val infos = buildList {
                if (change.hitPointsBefore != change.hitPointsAfter) add(SettlementInfoDelta(
                    SettlementInfoKind.HP, change.hitPointsBefore, change.hitPointsAfter,
                ))
                if (change.magicPointsBefore != change.magicPointsAfter) add(SettlementInfoDelta(
                    SettlementInfoKind.MP, change.magicPointsBefore, change.magicPointsAfter,
                ))
            }
            val states = buildList {
                val statusOrder = linkedSetOf<BattleStatus>().apply {
                    addAll(change.statusesBefore.keys)
                    addAll(change.statusesAfter.keys)
                }
                statusOrder.forEach { status ->
                    val before = change.statusesBefore[status]
                    val after = change.statusesAfter[status]
                    if (before != after) add(SettlementStateChange(
                        sourceStatusIndex = status.sourceIndex,
                        kind = when {
                            before == null -> SettlementStateChangeKind.ADD
                            after == null -> SettlementStateChangeKind.REMOVE
                            else -> SettlementStateChangeKind.ROUND_UPDATE
                        },
                        roundBefore = before,
                        roundAfter = after,
                    ))
                }
                BattleAttribute.entries.forEach { attribute ->
                    val before = change.attributeLiftsBefore[attribute] ?: 0
                    val after = change.attributeLiftsAfter[attribute] ?: 0
                    if (before != after) add(SettlementStateChange(
                        sourceStatusIndex = attribute.ordinal,
                        kind = SettlementStateChangeKind.LIFT,
                        liftBefore = before,
                        liftAfter = after,
                    ))
                }
                if (change.actionCompleteBefore != change.actionCompleteAfter ||
                    change.actionStatusRoundBefore != change.actionStatusRoundAfter
                ) add(SettlementStateChange(
                    sourceStatusIndex = 14,
                    kind = when {
                        !change.actionCompleteBefore && change.actionCompleteAfter -> SettlementStateChangeKind.ADD
                        change.actionCompleteBefore && !change.actionCompleteAfter -> SettlementStateChangeKind.REMOVE
                        else -> SettlementStateChangeKind.ROUND_UPDATE
                    },
                    roundBefore = change.actionStatusRoundBefore,
                    roundAfter = change.actionStatusRoundAfter,
                ))
            }
            val beforeFaction = effectiveFaction(unit.baseFaction, change.statusesBefore)
            val afterFaction = effectiveFaction(unit.baseFaction, change.statusesAfter)
            SettlementUnitPlan(
                unitId = change.unitId,
                baseFaction = unit.baseFaction,
                effectiveFactionBefore = beforeFaction,
                effectiveFactionAfter = afterFaction,
                infoPanel = infos.takeIf { it.isNotEmpty() }?.let {
                    if (afterFaction == Faction.PLAYER) SettlementInfoPanel.MINE else SettlementInfoPanel.OTHER
                },
                infoDeltas = infos,
                stateChanges = states,
            )
        }

        // Linked insertion order matches `_jiesuan`'s first-seen meff order.
        data class PendingBucket(
            val key: SettlementMeffKey,
            val targets: MutableList<SettlementMeffTarget> = mutableListOf(),
        )
        val buckets = linkedMapOf<Any, PendingBucket>()
        plans.forEach { unit ->
            // Original `_jiesuan` visually emits at most one state meff per
            // unit/STATES batch, while retaining every logical mutation.
            unit.stateChanges.firstNotNullOfOrNull { state ->
                resolveMeffId(state)?.let { state to it }
            }?.let { (state, actualId) ->
                val key = SettlementMeffKey(state.sourceStatusIndex, state.meffSlot, actualId)
                val groupingKey: Any = actualId
                buckets.getOrPut(groupingKey) { PendingBucket(key) }.targets +=
                    SettlementMeffTarget(unit.unitId, state)
            }
        }
        return BattleSettlementPlan(
            settlement.stage,
            settlement.faction,
            plans,
            buckets.values.map { SettlementMeffBucket(it.key, it.targets) },
            if (discoverPending) pendingIntegrations(settlement, unitsById.values) else emptyList(),
            authoredSubflows(settlement, unitsById, resolveMeffId),
        )
    }

    private fun authoredSubflows(
        settlement: CampSettlement,
        unitsById: Map<String, BattleUnit>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): List<SettlementAuthoredSubflowPlan> = settlement.subflows.map { subflow ->
        when (subflow) {
            is SettlementSubflow.LocalAura -> {
                val nested = planInternal(
                    CampSettlement(settlement.stage, settlement.faction, subflow.nestedChanges, subflowsCaptured = true),
                    unitsById,
                    resolveMeffId,
                    discoverPending = false,
                )
                SettlementAuthoredSubflowPlan.LocalAura(
                    subflow.casterId,
                    subflow.skillId,
                    subflow.skillValue,
                    buildList {
                        add(SettlementAuraStep.Focus(subflow.focusDelaySeconds))
                        add(SettlementAuraStep.Sound(subflow.soundIndex))
                        add(SettlementAuraStep.Info2(subflow.infoSkillId))
                        add(SettlementAuraStep.ActionFinished(subflow.actionId))
                        subflow.meffName?.let { add(SettlementAuraStep.PlayMeff(it, subflow.targets)) }
                        add(SettlementAuraStep.NestedSettlement)
                        add(SettlementAuraStep.DefaultAction)
                    },
                    nested,
                )
            }
            is SettlementSubflow.Growth -> SettlementAuthoredSubflowPlan.Growth(
                subflow.unitId,
                subflow.grants,
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
        }
    }

    private fun pendingIntegrations(
        settlement: CampSettlement,
        units: Collection<BattleUnit>,
    ): List<SettlementPendingIntegration> = buildList {
        val unresolvedGrowth = settlement.subflows.filterIsInstance<SettlementSubflow.Growth>()
            .filter { flow -> flow.grants.any { it.unitResult == null && it.equipmentResult == null } }
            .map { it.unitId }
        if (unresolvedGrowth.isNotEmpty()) add(SettlementPendingIntegration(
            SettlementPendingKind.EXPERIENCE_AND_LEVEL_UP,
            unresolvedGrowth,
        ))
        if (!settlement.subflowsCaptured && settlement.stage == CampSettlementStage.START_STATE &&
            settlement.faction in setOf(Faction.PLAYER, Faction.ENEMY)
        ) {
            val playerSide = settlement.faction.isPlayerSide()
            val ids = units.filter { unit ->
                unit.isPlayerSide() == playerSide &&
                    unit.skills.keys.any { it in setOf(103, 208, 209, 210) }
            }.map { it.id }
            if (ids.isNotEmpty()) add(SettlementPendingIntegration(SettlementPendingKind.LOCAL_AURA, ids))
        }
        if (!settlement.subflowsCaptured && settlement.stage == CampSettlementStage.END_RESTORE) {
            val ids = units.filter { unit ->
                unit.effectiveFaction() == settlement.faction &&
                    unit.skills.keys.any { it in setOf(149, 150, 151) }
            }.map { it.id }
            if (ids.isNotEmpty()) add(SettlementPendingIntegration(
                SettlementPendingKind.EXPERIENCE_AND_LEVEL_UP,
                ids,
            ))
        }
    }

    private fun effectiveFaction(base: Faction, statuses: Map<BattleStatus, Int>): Faction =
        if (BattleStatus.LOST !in statuses) base
        else if (base.isPlayerSide()) Faction.REINFORCEMENTS else Faction.FRIEND
}

val BattleStatus.sourceIndex: Int get() = when (this) {
    BattleStatus.PARALYSIS -> 7
    BattleStatus.SILENCE -> 8
    BattleStatus.CONFUSION -> 9
    BattleStatus.POISON -> 10
    BattleStatus.LOST -> 13
}
