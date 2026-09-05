package com.jojo.game.application.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.GameDataCatalog
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.ScenarioBattleUnit
import com.jojo.game.domain.scenario.ScenarioUnitFaction
import com.jojo.game.domain.scenario.battleId

/** Projects authored and persisted unit data into one live tactical unit. */
internal class BattleUnitProjector(
    private val catalog: GameDataCatalog?,
    private val campaign: CampaignState?,
    private val enemyEquipment: Map<Int, List<Int>>,
) {
    fun project(
        unit: ScenarioBattleUnit,
        forcedLevel: Int? = null,
        forcedPosts: Int? = null,
    ): BattleUnit {
        val persistent = campaign?.unitAttributes?.get(unit.characterId).orEmpty()
        val requestedLevel = forcedLevel?.minus(1)?.coerceAtLeast(0)
            ?: persistent[18]?.minus(1)?.coerceAtLeast(0)
            ?: unit.level
        val battleProfile = catalog?.battleProfile(unit.characterId, requestedLevel, forcedPosts ?: persistent[17])
        val profile = battleProfile?.unit
        val arm = battleProfile?.arm
        val equipmentValues = effectiveEquipmentValues(unit, battleProfile?.posts ?: 0, battleProfile?.level ?: 1)
        val equipment = catalog?.equipmentBonus(equipmentValues, battleProfile?.level ?: 1)
        val skills = catalog?.mergeSkills(
            catalog.skillsForUnit(unit.characterId, battleProfile?.posts ?: 0, campaign),
            catalog.equipmentSkills(equipmentValues, battleProfile?.level ?: 1),
        ).orEmpty()
        val abilities = BattleAbilityProjection(catalog, skills, battleProfile?.level ?: 1)
        val attackArea = attackArea(battleProfile, skills)

        return BattleUnit(
            id = unit.battleId,
            name = campaign?.unitNames?.get(unit.characterId) ?: profile?.name ?: "유닛 ${unit.characterId}",
            faction = factionFor(unit),
            tileX = unit.x,
            tileY = unit.y,
            visible = !unit.hidden,
            direction = unit.direction,
            characterId = unit.characterId,
            battleSlot = unit.battleSlot,
            famous = profile?.famous == true,
            hasAuthoredTileX = unit.authoredX,
            hasAuthoredTileY = unit.authoredY,
            hitPoints = abilities.passive(persistent[9] ?: battleProfile?.maxHitPoints ?: 100, 52),
            maxHitPoints = abilities.passive(persistent[9] ?: battleProfile?.maxHitPoints ?: 100, 52),
            magicPoints = abilities.passive(persistent[10] ?: battleProfile?.maxMagicPoints ?: 0, 53),
            maxMagicPoints = abilities.passive(persistent[10] ?: battleProfile?.maxMagicPoints ?: 0, 53),
            level = battleProfile?.level ?: 1,
            experience = if (unit.faction == ScenarioUnitFaction.MINE) persistent[19] ?: 0 else 0,
            posts = battleProfile?.posts ?: profile?.posts ?: 0,
            attack = abilities.ability(
                (persistent[2] ?: battleProfile?.attack ?: 45) + (equipment?.attack ?: 0), profile?.attack ?: 45, 65,
            ),
            defense = abilities.ability(
                (persistent[3] ?: battleProfile?.defense ?: 25) + (equipment?.defense ?: 0), profile?.defense ?: 25, 61,
            ),
            spirit = abilities.ability(
                (persistent[4] ?: battleProfile?.spirit ?: 35) + (equipment?.spirit ?: 0), profile?.spirit ?: 35, 68,
            ),
            critical = abilities.ability(persistent[5] ?: battleProfile?.critical ?: 35, profile?.critical ?: 35, 54),
            morale = abilities.ability(persistent[6] ?: battleProfile?.morale ?: 35, profile?.morale ?: 35, 73),
            martial = profile?.attack ?: battleProfile?.attack ?: 45,
            armId = arm?.id ?: 0,
            armType = arm?.type ?: 0,
            remoteAttack = arm?.remote ?: false,
            armMoveSound = arm?.moveSound ?: 0,
            fastMove = arm?.fastMove ?: true,
            attackDelay = arm?.attackDelay ?: false,
            armRestraints = (0 until 40).associateWith { arm?.restraintAgainst(it) ?: 100 },
            terrainImpacts = (0 until 30).associateWith { arm?.terrainImpact(it) ?: 100 },
            terrainMovementCosts = (0 until 30).associateWith { arm?.terrainMoveCost(it) ?: 1 },
            magicHarmRate = arm?.magicHarmRate ?: 100,
            attackOffsets = attackArea?.offsets ?: CARDINAL_OFFSETS,
            attackEffectOffsets = catalog?.effectAreaOffsets(skills.skillValue(32) ?: 0).orEmpty(),
            attackEffectAreaId = skills.skillValue(32) ?: 0,
            attackAllScreen = attackArea?.allScreen ?: false,
            magic = BattleUnitMagicProjector(catalog, campaign, skills).project(unit.characterId, battleProfile),
            skills = skills,
            movement = (battleProfile?.movement ?: 3) + (skills.skillValue(77) ?: 0),
            ai = unit.ai,
            aiTargetCharacterId = unit.aiTargetId,
            aiTargetX = unit.aiTargetX,
            aiTargetY = unit.aiTargetY,
            retireMessage = catalog?.retreatText(unit.characterId),
            criticalSpeech = profile?.criticalSpeech
                ?: GameDataCatalog.CriticalSpeechProfile(emptyList(), randomized = false),
            deathMessageEnabled = unit.deathMessageEnabled,
            retreatCount = persistent[15] ?: 0,
        )
    }

    private fun effectiveEquipmentValues(unit: ScenarioBattleUnit, posts: Int, level: Int): List<Int> {
        val equipped = if (unit.faction == ScenarioUnitFaction.MINE) {
            campaign?.inventory?.equipment?.get(unit.characterId)?.asScriptValues()
                ?: enemyEquipment[unit.characterId].orEmpty()
        } else enemyEquipment[unit.characterId].orEmpty()
        val defaults = catalog?.defaultEquipment(posts, level)?.asScriptValues().orEmpty()
        return listOf(
            equipped.valueOrDefault(0, defaults), equipped.valueOrDefault(1, defaults),
            equipped.valueOrDefault(2, defaults), equipped.valueOrDefault(3, defaults),
            equipped.valueOrDefault(4, defaults),
        )
    }

    private fun List<Int>.valueOrDefault(index: Int, defaults: List<Int>): Int {
        val primaryIndex = if (index in 0..1) 0 else if (index in 2..3) 2 else 4
        return if (getOrElse(primaryIndex) { 0 } > 1) getOrElse(index) { 0 }
        else defaults.getOrElse(index) { 1 }
    }

    private fun attackArea(
        profile: GameDataCatalog.BattleProfile?,
        skills: Map<Int, Int>,
    ): GameDataCatalog.HitAreaProfile? {
        val rangeSkill = skills.skillValue(258)
        val base = rangeSkill?.let { catalog?.hitAreaProfile(it) } ?: profile?.hitArea
        return if (skills.skillValue(260) != rangeSkill) {
            base?.upgradeId?.let { catalog?.hitAreaProfile(it) } ?: base
        } else base
    }

    private fun factionFor(unit: ScenarioBattleUnit) = when (unit.faction) {
        ScenarioUnitFaction.MINE -> Faction.PLAYER
        ScenarioUnitFaction.FRIEND -> Faction.FRIEND
        ScenarioUnitFaction.ENEMY -> if (unit.reinforcement) Faction.REINFORCEMENTS else Faction.ENEMY
    }

    private fun Map<Int, Int>.skillValue(id: Int): Int? =
        get(id)?.and(255)?.takeIf { it != 255 }

    private companion object {
        val CARDINAL_OFFSETS = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
    }
}

private class BattleAbilityProjection(
    private val catalog: GameDataCatalog?,
    private val skills: Map<Int, Int>,
    private val level: Int,
) {
    fun passive(base: Int, skillId: Int): Int = catalog?.passiveAbility(base, skillId, skills) ?: base

    fun ability(base: Int, sourceBase: Int, passiveSkill: Int): Int =
        passive(divineFloor(base, sourceBase), passiveSkill)

    private fun divineFloor(base: Int, sourceBase: Int): Int {
        val growth = skills[190]?.and(255)?.takeIf { it != 255 } ?: return base
        return maxOf(base, sourceBase + growth * level)
    }
}
