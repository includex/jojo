package com.jojo.game.domain.battle.settlement

import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
import com.jojo.game.domain.campaign.CampaignExperienceResult

enum class CampSettlementStage { START_STATE, END_RESTORE }

data class BattleUnitTurnChange(
    val unitId: String,
    val hitPointsBefore: Int,
    val hitPointsAfter: Int,
    val magicPointsBefore: Int,
    val magicPointsAfter: Int,
    val statusesBefore: Map<BattleStatus, Int>,
    val statusesAfter: Map<BattleStatus, Int>,
    val attributeLiftsBefore: Map<BattleAttribute, Int>,
    val attributeLiftsAfter: Map<BattleAttribute, Int>,
    val actionCompleteBefore: Boolean = false,
    val actionCompleteAfter: Boolean = false,
    val actionStatusRoundBefore: Int = 0,
    val actionStatusRoundAfter: Int = 0,
)

data class CampSettlement(
    val stage: CampSettlementStage,
    val faction: Faction,
    val changes: List<BattleUnitTurnChange>,
    /** Authored callback subflows executed inside `_stateProcess`/`restore`. */
    val subflows: List<SettlementSubflow> = emptyList(),
    /** Distinguishes an authored empty result from a legacy caller without capture. */
    val subflowsCaptured: Boolean = false,
)

sealed interface SettlementSubflow {
    data class LocalAura(
        val casterId: String,
        val skillId: Int,
        val skillValue: Int,
        val focusDelaySeconds: Float = .3f,
        val soundIndex: Int = 39,
        val infoSkillId: Int = skillId,
        val actionId: Int = 30,
        val meffName: String? = null,
        val targets: List<String>,
        val nestedChanges: List<BattleUnitTurnChange>,
    ) : SettlementSubflow

    data class Growth(
        val unitId: String,
        val grants: List<SettlementGrowthGrant>,
    ) : SettlementSubflow
}

enum class SettlementGrowthKind { UNIT_EXP, WEAPON_EXP, ARMOR_EXP }

data class SettlementGrowthGrant(
    val kind: SettlementGrowthKind,
    val requestedAmount: Int,
    val unitResult: CampaignExperienceResult? = null,
    val equipmentResult: CampaignEquipmentExperienceResult? = null,
) {
    val requiresLevelUpPresentation: Boolean
        get() = unitResult?.leveledUp == true || equipmentResult?.leveledUp == true
    val requiresItemUpgradeCallback: Boolean get() = equipmentResult?.leveledUp == true
}

sealed interface RestoreGrowthResolution<out T> {
    data object NotApplicable : RestoreGrowthResolution<Nothing>
    data object Unavailable : RestoreGrowthResolution<Nothing>
    data class Applied<T>(val value: T) : RestoreGrowthResolution<T>
}
