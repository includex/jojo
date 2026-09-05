package com.jojo.game
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.settlement.*
import com.jojo.game.domain.campaign.CampaignEquipmentSlot

import java.util.*

/**
 * Immutable, non-progressive inputs for a [Battle].
 *
 * Keeping these values together is intentional: they describe the rules and
 * source data of a battle, while turn progress and presentation hand-off
 * state live in [BattleStateJournal].  The public Battle constructor remains
 * source compatible; it assembles this value once at the aggregate boundary.
 */
internal class BattleConfiguration(
    val events: List<BattleEvent>,
    val terrain: BattleTerrainGrid?,
    val enemyMasterUnitId: String?,
    val weatherSchedule: List<BattleWeather>,
    val weatherOffset: Int,
    val terrainMagicFlags: Map<Int, Int>,
    val terrainResumeRates: Map<Int, Int>,
    val terrainResumeMp: Map<Int, Int>,
    val enabledFeatures: Int,
    val skillTempResetTypes: Map<Int, BattleSkillTemp.ResetType>,
    val statusRoundFor: (BattleStatus) -> Int,
    val attributeStatusRoundFor: (BattleAttribute) -> Int,
    val movementOffsets: Set<Pair<Int, Int>>,
    val infantryOffsets: Set<Pair<Int, Int>>,
    val propertyItems: Map<Int, BattlePropertyItem>,
    val consumeProperty: (Int) -> Boolean,
    val zdsyGlobalValue: Int,
    val consumeAutomaticProperty: (Int) -> Unit,
    val onPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit,
    val onUnitDefeated: (BattleUnit, BattleUnit) -> Unit,
    val onBattleExperience: (BattleUnit, Int) -> CampaignExperienceResult?,
    val experienceLimit: (Int) -> Int,
    val levelLimit: Int,
    val onBattleLevelUp: (BattleUnit) -> Unit,
    val onPhysicalDamage: (BattleUnit, BattleUnit, Int) -> Unit,
    val onEquipmentExperienceAward: ((BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> List<CampaignEquipmentExperienceResult>)?,
    val onEquipmentExperience: (BattleUnit, BattleUnit, Int) -> List<CampaignEquipmentExperienceResult>,
    val onRestoreUnitExperience: (BattleUnit, Int) -> RestoreGrowthResolution<CampaignExperienceResult>,
    val onRestoreEquipmentExperience: (BattleUnit, Int, CampaignEquipmentSlot) -> RestoreGrowthResolution<CampaignEquipmentExperienceResult>,
    val random: Random,
    val sourceRandomStreams: SourceRandomStreams?,
    val onUnitRetreat: (BattleUnit) -> Unit,
)

/** Keeps the public Battle constructor as the compatibility boundary. */
internal fun buildBattleConfiguration(
    events: List<BattleEvent>,
    terrain: BattleTerrainGrid?,
    enemyMasterUnitId: String?,
    weatherSchedule: List<BattleWeather>,
    weatherOffset: Int,
    terrainMagicFlags: Map<Int, Int>,
    terrainResumeRates: Map<Int, Int>,
    terrainResumeMp: Map<Int, Int>,
    enabledFeatures: Int,
    skillTempResetTypes: Map<Int, BattleSkillTemp.ResetType>,
    statusRoundFor: (BattleStatus) -> Int,
    attributeStatusRoundFor: (BattleAttribute) -> Int,
    movementOffsets: Set<Pair<Int, Int>>,
    infantryOffsets: Set<Pair<Int, Int>>,
    propertyItems: Map<Int, BattlePropertyItem>,
    consumeProperty: (Int) -> Boolean,
    zdsyGlobalValue: Int,
    consumeAutomaticProperty: (Int) -> Unit,
    onPermanentProperty: (BattlePropertyItem, BattleUnit) -> Unit,
    onUnitDefeated: (BattleUnit, BattleUnit) -> Unit,
    onBattleExperience: (BattleUnit, Int) -> CampaignExperienceResult?,
    experienceLimit: (Int) -> Int,
    levelLimit: Int,
    onBattleLevelUp: (BattleUnit) -> Unit,
    onPhysicalDamage: (BattleUnit, BattleUnit, Int) -> Unit,
    onEquipmentExperienceAward: ((BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> List<CampaignEquipmentExperienceResult>)?,
    onEquipmentExperience: (BattleUnit, BattleUnit, Int) -> List<CampaignEquipmentExperienceResult>,
    onRestoreUnitExperience: (BattleUnit, Int) -> RestoreGrowthResolution<CampaignExperienceResult>,
    onRestoreEquipmentExperience: (BattleUnit, Int, CampaignEquipmentSlot) -> RestoreGrowthResolution<CampaignEquipmentExperienceResult>,
    random: Random,
    sourceRandomStreams: SourceRandomStreams?,
    onUnitRetreat: (BattleUnit) -> Unit,
): BattleConfiguration = BattleConfiguration(
    events, terrain, enemyMasterUnitId, weatherSchedule, weatherOffset, terrainMagicFlags,
    terrainResumeRates, terrainResumeMp, enabledFeatures, skillTempResetTypes,
    statusRoundFor, attributeStatusRoundFor, movementOffsets, infantryOffsets, propertyItems,
    consumeProperty, zdsyGlobalValue, consumeAutomaticProperty, onPermanentProperty, onUnitDefeated,
    onBattleExperience, experienceLimit, levelLimit, onBattleLevelUp, onPhysicalDamage,
    onEquipmentExperienceAward, onEquipmentExperience, onRestoreUnitExperience,
    onRestoreEquipmentExperience, random, sourceRandomStreams, onUnitRetreat,
)
