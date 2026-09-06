// Battle
package com.jojo.game.domain.battle.settlement

import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.MagicLocalSettlement
import com.jojo.game.domain.battle.isPlayerSide
/**
 * `SettlementUnitSnapshot` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

data class SettlementUnitSnapshot(
    val id: String,
    val baseFaction: Faction,
    val skillIds: Set<Int>,
    val hasLostStatus: Boolean,
)

/**
 * `BattleSettlementPlanner` 싱글턴 객체: settlement 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

object BattleSettlementPlanner {
    /**
     * `plan`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun plan(
        settlement: CampSettlement,
        unitsById: Map<String, SettlementUnitSnapshot>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan = planInternal(settlement, unitsById, resolveMeffId, discoverPending = true)

    /**
     * `planMagicLocal`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun planMagicLocal(
        settlement: MagicLocalSettlement,
        camp: Faction,
        unitsById: Map<String, SettlementUnitSnapshot>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): BattleSettlementPlan {
        /**
         * `changes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val changes = settlement.entries.map { entry ->
            BattleUnitTurnChange(
                entry.targetId, 0, 0, 0, 0,
                entry.statusesBefore, entry.statusesAfter,
                entry.attributeLiftsBefore, entry.attributeLiftsAfter,
            )
        }
        /**
         * `normal` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val normal = planInternal(
            CampSettlement(CampSettlementStage.START_STATE, camp, changes, subflowsCaptured = true),
            unitsById, resolveMeffId, discoverPending = false,
        )
        return normal.copy(units = normal.units.zip(settlement.entries).map { (unit, entry) ->
            unit.copy(hasStatesPayload = entry.hasStatesPayload)
        })
    }

    /**
     * `planInternal`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun planInternal(
        settlement: CampSettlement,
        unitsById: Map<String, SettlementUnitSnapshot>,
        resolveMeffId: (SettlementStateChange) -> Int?,
        discoverPending: Boolean,
    ): BattleSettlementPlan {
        /**
         * `plans` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val plans = settlement.changes.map { change ->
            /**
             * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val unit = requireNotNull(unitsById[change.unitId]) { "settlement unit is unavailable: ${change.unitId}" }
            /**
             * `infoDeltas` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val infoDeltas = buildList {
                if (change.hitPointsBefore != change.hitPointsAfter) add(
                    SettlementInfoDelta(SettlementInfoKind.HP, change.hitPointsBefore, change.hitPointsAfter)
                )
                if (change.magicPointsBefore != change.magicPointsAfter) add(
                    SettlementInfoDelta(SettlementInfoKind.MP, change.magicPointsBefore, change.magicPointsAfter)
                )
            }
            /**
             * `stateChanges` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val stateChanges = stateChanges(change)
            /**
             * `beforeFaction` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val beforeFaction = effectiveFaction(unit.baseFaction, BattleStatus.LOST in change.statusesBefore)
            /**
             * `afterFaction` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val afterFaction = effectiveFaction(unit.baseFaction, BattleStatus.LOST in change.statusesAfter)
            SettlementUnitPlan(
                change.unitId, unit.baseFaction, beforeFaction, afterFaction,
                infoDeltas.takeIf { it.isNotEmpty() }?.let {
                    if (afterFaction == Faction.PLAYER) SettlementInfoPanel.MINE else SettlementInfoPanel.OTHER
                },
                infoDeltas, stateChanges,
            )
        }
        /**
         * `PendingBucket` 클래스: settlement 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class PendingBucket(val key: SettlementMeffKey, val targets: MutableList<SettlementMeffTarget> = mutableListOf())
        /**
         * `buckets` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val buckets = linkedMapOf<Int, PendingBucket>()
        plans.forEach { unit ->
            unit.stateChanges.firstNotNullOfOrNull { state -> resolveMeffId(state)?.let { state to it } }
                ?.let { (state, actualId) ->
                    /**
                     * `key` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

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

    /**
     * `stateChanges`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `authoredSubflows`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun authoredSubflows(
        settlement: CampSettlement,
        unitsById: Map<String, SettlementUnitSnapshot>,
        resolveMeffId: (SettlementStateChange) -> Int?,
    ): List<SettlementAuthoredSubflowPlan> = settlement.subflows.map { subflow -> when (subflow) {
        is SettlementSubflow.LocalAura -> {
            /**
             * `nested` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

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

    /**
     * `pendingIntegrations`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun pendingIntegrations(
        settlement: CampSettlement,
        units: Collection<SettlementUnitSnapshot>,
    ): List<SettlementPendingIntegration> = buildList {
        /**
         * `unresolvedGrowth` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unresolvedGrowth = settlement.subflows.filterIsInstance<SettlementSubflow.Growth>()
            .filter { it.grants.any { grant -> grant.unitResult == null && grant.equipmentResult == null } }
            .map { it.unitId }
        if (unresolvedGrowth.isNotEmpty()) add(SettlementPendingIntegration(SettlementPendingKind.EXPERIENCE_AND_LEVEL_UP, unresolvedGrowth))
        if (!settlement.subflowsCaptured && settlement.stage == CampSettlementStage.START_STATE &&
            settlement.faction in setOf(Faction.PLAYER, Faction.ENEMY)
        ) {
            /**
             * `playerSide` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val playerSide = settlement.faction.isPlayerSide()
            /**
             * `ids` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val ids = units.filter {
                effectiveFaction(it.baseFaction, it.hasLostStatus).isPlayerSide() == playerSide &&
                    it.skillIds.any { id -> id in setOf(103, 208, 209, 210) }
            }.map { it.id }
            if (ids.isNotEmpty()) add(SettlementPendingIntegration(SettlementPendingKind.LOCAL_AURA, ids))
        }
        if (!settlement.subflowsCaptured && settlement.stage == CampSettlementStage.END_RESTORE) {
            /**
             * `ids` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val ids = units.filter {
                effectiveFaction(it.baseFaction, it.hasLostStatus) == settlement.faction &&
                    it.skillIds.any { id -> id in setOf(149, 150, 151) }
            }.map { it.id }
            if (ids.isNotEmpty()) add(SettlementPendingIntegration(SettlementPendingKind.EXPERIENCE_AND_LEVEL_UP, ids))
        }
    }

    /**
     * `effectiveFaction`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun effectiveFaction(base: Faction, lost: Boolean): Faction =
        if (!lost) base else if (base.isPlayerSide()) Faction.REINFORCEMENTS else Faction.FRIEND
}

/**
 * `BattleStatus` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
 */

val BattleStatus.sourceIndex: Int
    get() = when (this) {
        BattleStatus.PARALYSIS -> 7
        BattleStatus.SILENCE -> 8
        BattleStatus.CONFUSION -> 9
        BattleStatus.POISON -> 10
        BattleStatus.LOST -> 13
    }
