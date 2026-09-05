package com.jojo.game.application.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.isPlayerSide
import com.jojo.game.domain.battle.settlement.BattleUnitTurnChange
import com.jojo.game.domain.battle.settlement.CampSettlement
import com.jojo.game.domain.battle.settlement.CampSettlementStage
import com.jojo.game.domain.battle.settlement.RestoreGrowthResolution
import com.jojo.game.domain.battle.settlement.SettlementGrowthGrant
import com.jojo.game.domain.battle.settlement.SettlementGrowthKind
import com.jojo.game.domain.battle.settlement.SettlementSubflow
import com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.campaign.CampaignExperienceResult

data class BattleTurnSettlementEnvironment(
    val units: () -> Collection<BattleUnit>,
    val presentationUnit: (String) -> BattleUnit?,
    val defeatUnit: (String) -> Unit,
    val terrain: BattleTerrainGrid?,
    val terrainResumeRates: Map<Int, Int>,
    val terrainResumeMp: Map<Int, Int>,
    val weather: () -> BattleWeather,
    val enabledFeatures: () -> Int,
    val infantryOffsets: Set<Pair<Int, Int>> = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
    val statusRoundFor: (BattleStatus) -> Int,
    val attributeStatusRoundFor: (BattleAttribute) -> Int,
    val onRestoreUnitExperience: (BattleUnit, Int) -> RestoreGrowthResolution<CampaignExperienceResult>,
    val onRestoreEquipmentExperience: (BattleUnit, Int, CampaignEquipmentSlot) -> RestoreGrowthResolution<CampaignEquipmentExperienceResult>,
    val onEquipmentUpgrade: (CampaignEquipmentExperienceResult) -> Unit,
)

/** Application coordinator: mutates live battle units and records pure settlement DTOs. */
object BattleTurnSettlementService {
    private const val ENABLED_FEATURE_ZDBHSW = 32

    data class UnitTurnSnapshot(
        val hp: Int,
        val mp: Int,
        val statuses: Map<BattleStatus, Int>,
        val lifts: Map<BattleAttribute, Int>,
        val actionComplete: Boolean,
        val actionStatusRound: Int,
    )

    fun turnSnapshot(units: Collection<BattleUnit>): Map<String, UnitTurnSnapshot> = units.associate { unit ->
        unit.id to UnitTurnSnapshot(
            unit.hitPoints, unit.magicPoints, unit.statuses.toMap(), unit.attributeLifts.toMap(),
            unit.hasActed, unit.actionStatusRound,
        )
    }

    fun turnChanges(
        before: Map<String, UnitTurnSnapshot>,
        presentationUnit: (String) -> BattleUnit?,
    ): List<BattleUnitTurnChange> = before.mapNotNull { (id, old) ->
        val unit = presentationUnit(id) ?: return@mapNotNull null
        if (old.hp == unit.hitPoints && old.mp == unit.magicPoints && old.statuses == unit.statuses &&
            old.lifts == unit.attributeLifts && old.actionComplete == unit.hasActed &&
            old.actionStatusRound == unit.actionStatusRound
        ) return@mapNotNull null
        BattleUnitTurnChange(
            id, old.hp, unit.hitPoints, old.mp, unit.magicPoints,
            old.statuses, unit.statuses.toMap(), old.lifts, unit.attributeLifts.toMap(),
            old.actionComplete, unit.hasActed, old.actionStatusRound, unit.actionStatusRound,
        )
    }

    fun captureSettlement(
        stage: CampSettlementStage,
        faction: Faction,
        env: BattleTurnSettlementEnvironment,
        settle: (MutableList<SettlementSubflow>) -> List<BattleUnitTurnChange>?,
    ): CampSettlement {
        val before = turnSnapshot(env.units())
        val subflows = mutableListOf<SettlementSubflow>()
        return CampSettlement(stage, faction, settle(subflows) ?: turnChanges(before, env.presentationUnit), subflows, true)
    }

    fun settleCampStart(faction: Faction, env: BattleTurnSettlementEnvironment): CampSettlement =
        captureSettlement(CampSettlementStage.START_STATE, faction, env) { processStartOfTurn(faction, it, env) }

    fun settleCampEnd(faction: Faction, env: BattleTurnSettlementEnvironment): CampSettlement =
        captureSettlement(CampSettlementStage.END_RESTORE, faction, env) { subflows ->
            if (faction == Faction.FRIEND || faction == Faction.REINFORCEMENTS) {
                val side = faction.isPlayerSide()
                env.units().filter { it.effectiveFaction().isPlayerSide() == side }.forEach { it.hasActed = false }
            }
            processEndOfTurn(faction, subflows, env)
        }

    fun processStartOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
        env: BattleTurnSettlementEnvironment,
    ): List<BattleUnitTurnChange> {
        val processedSide = when (faction) {
            Faction.PLAYER -> true
            Faction.ENEMY -> false
            Faction.FRIEND, Faction.REINFORCEMENTS -> null
        }
        val allUnits = env.units()
        if (processedSide == null) return emptyList()
        val changes = mutableListOf<BattleUnitTurnChange>()
        allUnits.filter { it.effectiveFaction().isPlayerSide() == processedSide }
            .sortedWith(compareBy<BattleUnit> { it.tileY }.thenBy { it.tileX })
            .forEach { caster ->
                val before = turnSnapshot(allUnits)
                caster.statuses.entries.toList().forEach { (status, rounds) ->
                    if (rounds <= 1) caster.statuses.remove(status) else caster.statuses[status] = rounds - 1
                }
                caster.attributeLifts.keys.toList().forEach { attribute ->
                    val rounds = caster.attributeLiftRounds[attribute] ?: 0
                    if (rounds <= 1) {
                        caster.attributeLifts.remove(attribute)
                        caster.attributeLiftRounds[attribute] = env.attributeStatusRoundFor(attribute)
                    } else caster.attributeLiftRounds[attribute] = rounds - 1
                }
                env.terrain?.terrainAt(caster.tileX, caster.tileY)?.let { terrainId ->
                    env.terrainResumeRates[terrainId]?.takeIf { it != 0 }?.let { caster.addHpcur(caster.maxHitPoints * it / 100) }
                    env.terrainResumeMp[terrainId]?.takeIf { it != 0 }?.let { caster.addMpcur(it) }
                }
                if (caster.hitPoints <= 0) env.defeatUnit(caster.id)
                changes += turnChanges(before, env.presentationUnit)
                if (caster.hitPoints <= 0) return@forEach

                fun nearby() = env.infantryOffsets.mapNotNull { (dx, dy) ->
                    allUnits.firstOrNull { it.tileX == caster.tileX + dx && it.tileY == caster.tileY + dy }
                }.filter { it.isPlayerSide() == processedSide }.distinctBy { it.id }
                fun record(skillId: Int, value: Int, meffName: String? = null, targets: List<BattleUnit>, mutate: () -> Unit) {
                    val nestedBefore = turnSnapshot(allUnits)
                    mutate()
                    val order = targets.mapIndexed { index, target -> target.id to index }.toMap()
                    val nested = turnChanges(nestedBefore, env.presentationUnit).sortedBy { order[it.unitId] ?: Int.MAX_VALUE }
                    if (targets.isNotEmpty()) subflows += SettlementSubflow.LocalAura(
                        caster.id, skillId, value, meffName = meffName, targets = targets.map { it.id }, nestedChanges = nested,
                    )
                }
                fun effect(id: Int) = caster.skills[id]?.and(255)?.takeIf { it != 255 }
                effect(103)?.let { value ->
                    val targets = nearby().filter { target ->
                        listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                            .any(target.statuses::containsKey)
                    }
                    record(103, value, targets = targets) { targets.forEach { target ->
                        listOf(BattleStatus.PARALYSIS, BattleStatus.SILENCE, BattleStatus.CONFUSION, BattleStatus.POISON)
                            .forEach(target.statuses::remove)
                    } }
                }
                effect(208)?.let { value ->
                    val targets = nearby().filter { it.hitPoints < it.maxHitPoints }
                    record(208, value, "resume_hp", targets) { targets.forEach { it.addHpcur(it.maxHitPoints * value / 100) } }
                }
                effect(209)?.let { value ->
                    val targets = nearby().filter { it.magicPoints < it.maxMagicPoints }
                    record(209, value, "resume_mp", targets) { targets.forEach { target ->
                        target.addMpcur(if (value == 0) (caster.level + 10) / 10 else target.maxMagicPoints * value / 100)
                    } }
                }
                effect(210)?.takeIf { it and 31 != 0 }?.let { mask ->
                    val targets = nearby()
                    record(210, mask, targets = targets) { targets.forEach { target ->
                        BattleAttribute.entries.take(5).forEachIndexed { index, attribute ->
                            if (mask and (1 shl index) != 0) target.applyAttributeLift(attribute, 1, 3)
                        }
                    } }
                }
            }
        return changes
    }

    fun processEndOfTurn(
        faction: Faction,
        subflows: MutableList<SettlementSubflow>,
        env: BattleTurnSettlementEnvironment,
    ): List<BattleUnitTurnChange> {
        val allUnits = env.units()
        allUnits.filter { it.effectiveFaction() == faction }.forEach { unit ->
            val grants = buildList {
                unit.skills[149]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = env.onRestoreUnitExperience(unit, amount)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.UNIT_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            unit.level = resolution.value.level
                            if (resolution.value.gained > 0) add(
                                SettlementGrowthGrant(SettlementGrowthKind.UNIT_EXP, amount, unitResult = resolution.value)
                            )
                        }
                    }
                }
                unit.skills[150]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = env.onRestoreEquipmentExperience(unit, amount, CampaignEquipmentSlot.WEAPON)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.WEAPON_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            val result = resolution.value
                            if (result.gained > 0) add(
                                SettlementGrowthGrant(SettlementGrowthKind.WEAPON_EXP, amount, equipmentResult = result)
                            )
                            if (result.leveledUp) env.onEquipmentUpgrade(result)
                        }
                    }
                }
                unit.skills[151]?.and(255)?.takeIf { it != 255 }?.let { amount ->
                    when (val resolution = env.onRestoreEquipmentExperience(unit, amount, CampaignEquipmentSlot.ARMOR)) {
                        RestoreGrowthResolution.Unavailable -> add(SettlementGrowthGrant(SettlementGrowthKind.ARMOR_EXP, amount))
                        RestoreGrowthResolution.NotApplicable -> Unit
                        is RestoreGrowthResolution.Applied -> {
                            val result = resolution.value
                            if (result.gained > 0) add(
                                SettlementGrowthGrant(SettlementGrowthKind.ARMOR_EXP, amount, equipmentResult = result)
                            )
                            if (result.leveledUp) env.onEquipmentUpgrade(result)
                        }
                    }
                }
            }
            if (grants.isNotEmpty()) subflows += SettlementSubflow.Growth(unit.id, grants)
        }
        val poisonBefore = turnSnapshot(allUnits)
        val lethalPoison = env.enabledFeatures() and ENABLED_FEATURE_ZDBHSW != 0
        allUnits.filter { it.effectiveFaction() == faction && BattleStatus.POISON in it.statuses }.forEach { unit ->
            if (!lethalPoison && unit.hitPoints < 2) return@forEach
            val rate = if (env.weather() == BattleWeather.CLOUDY) 15 else 10
            var damage = unit.maxHitPoints * rate / 100
            if (!lethalPoison) damage = minOf(unit.hitPoints - 1, damage)
            unit.addHpcur(-damage)
            if (unit.hitPoints <= 0) env.defeatUnit(unit.id)
        }
        return turnChanges(poisonBefore, env.presentationUnit)
    }

}
