// Battle
package com.jojo.game.domain.battle.settlement

import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
data class BattleSettlementPlan(
    val stage: CampSettlementStage,
    val camp: Faction,
    val units: List<SettlementUnitPlan>,
    val meffBuckets: List<SettlementMeffBucket>,
    val pendingIntegrations: List<SettlementPendingIntegration> = emptyList(),
    val authoredSubflows: List<SettlementAuthoredSubflowPlan> = emptyList(),
) {
    val sourceDataComplete: Boolean get() = pendingIntegrations.isEmpty()
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
    val meffSlot: Int get() = when {
        kind == SettlementStateChangeKind.REMOVE -> 0
        kind == SettlementStateChangeKind.LIFT && (liftAfter ?: 0) < 0 -> 0
        kind == SettlementStateChangeKind.LIFT && (liftAfter ?: 0) > 0 -> 2
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
    val hasStatesPayload: Boolean = stateChanges.isNotEmpty(),
    val preInfoDelaySeconds: Float = if (infoDeltas.isEmpty()) 0f else .1f,
    val infoCloseSeconds: Float = if (infoDeltas.isEmpty()) 0f else .3f,
) {
    val infoBarrierSeconds: Float get() = (
        kotlin.math.round((preInfoDelaySeconds.toDouble() + infoDeltas.sumOf { it.tickSeconds.toDouble() } + infoCloseSeconds) * 1_000) /
            1_000.0
        ).toFloat()
}

data class SettlementMeffKey(
    val sourceStatusIndex: Int,
    val meffSlot: Int,
    val actualMeffId: Int? = null,
)

data class SettlementMeffTarget(val unitId: String, val state: SettlementStateChange)
data class SettlementMeffBucket(
    val key: SettlementMeffKey,
    val targets: List<SettlementMeffTarget>,
    val simultaneousTargets: Boolean = true,
    val callbackTargetUnitId: String = targets.last().unitId,
)
