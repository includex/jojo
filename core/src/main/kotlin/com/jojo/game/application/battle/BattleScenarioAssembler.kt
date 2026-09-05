package com.jojo.game.application.battle

import com.jojo.game.Battle
import com.jojo.game.BattleEvent
import com.jojo.game.BattleUnit
import com.jojo.game.GameDataCatalog
import com.jojo.game.TurnTrigger
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.BattleEquipmentExperienceKind
import com.jojo.game.domain.battle.BattlePropertyItem
import com.jojo.game.domain.battle.isPlayerSide
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.battle.settlement.RestoreGrowthResolution
import com.jojo.game.domain.scenario.ScenarioUnitFaction
import com.jojo.game.domain.scenario.battleId

/** Application entry point that combines projected scenario units and runtime hooks. */
internal object BattleScenarioAssembler {
    fun tutorialBattle(): Battle = Battle(
        units = listOf(
            BattleUnit("cao-cao", "조조", Faction.PLAYER, 3, 3),
            BattleUnit("guard", "병사", Faction.PLAYER, 2, 2),
            BattleUnit("yellow-turban", "황건적", Faction.ENEMY, 10, 5),
        ),
        events = listOf(
            BattleEvent("reinforcement-arrival", TurnTrigger(round = 2, faction = Faction.PLAYER)) { state ->
                state.addUnit(BattleUnit("reinforcement", "증원군", Faction.PLAYER, 1, 6))
            }
        )
    )

    fun materialize(request: BattleScenarioRequest): Battle {
        val scriptedByBattleId = request.units.associateBy { it.battleId }
        val projector = BattleUnitProjector(request.gameDataCatalog, request.campaign, request.enemyEquipment)
        val catalog = request.gameDataCatalog
        val campaign = request.campaign
        return Battle(
            units = request.units.map(projector::project),
            events = emptyList(),
            blockedTiles = request.blockedTiles,
            terrain = request.terrain,
            enemyMasterUnitId = request.units.firstOrNull {
                it.faction == ScenarioUnitFaction.ENEMY && it.characterId == request.enemyMasterInstanceId
            }?.battleId,
            initialWeather = request.initialWeather,
            weatherSchedule = request.weatherSchedule,
            weatherOffset = request.weatherOffset,
            terrainMagicFlags = catalog.terrainValues { terrainMagicFlag(it) },
            terrainResumeRates = catalog.terrainValues { terrainResumeHp(it) },
            terrainResumeMp = catalog.terrainValues { terrainResumeMp(it) },
            enabledFeatures = request.enabledFeatures,
            statusRoundFor = { status -> catalog?.statusRound(status) ?: 3 },
            attributeStatusRoundFor = { attribute -> catalog?.attributeStatusRound(attribute) ?: 3 },
            movementOffsets = catalog?.hitAreaProfile(0)?.offsets ?: CARDINAL_OFFSETS,
            directDestinationOffsets = catalog?.hitAreaProfile(13)?.offsets?.toList().orEmpty(),
            infantryOffsets = catalog?.hitAreaProfile(1)?.offsets ?: CARDINAL_OFFSETS,
            propertyItems = catalog?.battlePropertyItems().orEmpty()
                .map { BattlePropertyItem(it.id, it.name, it.itemType, it.value) }
                .associateBy { it.id },
            consumeProperty = campaign?.let { state -> { itemId: Int -> state.inventory.consumeItem(itemId) } } ?: { false },
            zdsyGlobalValue = (campaign?.globalVariables?.get(4035) as? Number)?.toInt() ?: 0,
            consumeAutomaticProperty = campaign?.let { state ->
                { itemId: Int -> state.inventory.consumeItem(itemId); Unit }
            } ?: {},
            onPermanentProperty = permanentProperty(campaign),
            onUnitDefeated = { _, _ -> },
            onBattleExperience = experienceAward(campaign, catalog),
            experienceLimit = { level -> catalog?.unitExperienceLimit(level) ?: 100 },
            levelLimit = catalog?.unitLevelLimit() ?: 50,
            onBattleLevelUp = { live ->
                scriptedByBattleId[live.id]?.let { scripted ->
                    live.refreshLevelDerivedState(projector.project(scripted, live.level, live.posts))
                }
            },
            onUnitRetreat = campaign?.let { state ->
                { unit: BattleUnit -> unit.characterId?.let { state.setUnitAttribute(it, 15, unit.retreatCount) } }
            } ?: {},
            onEquipmentExperienceAward = equipmentExperienceAward(campaign, catalog),
            onRestoreUnitExperience = restoreUnitExperience(campaign, catalog),
            onRestoreEquipmentExperience = restoreEquipmentExperience(campaign, catalog),
            sourceRandomStreams = request.sourceRandomStreams,
        ).also { it.initializeAllRateGauges() }
    }

    private fun GameDataCatalog?.terrainValues(value: GameDataCatalog.(Int) -> Int): Map<Int, Int> =
        this?.let { data -> (0..64).mapNotNull { id -> data.value(id).takeIf { it != 0 }?.let { id to it } }.toMap() }
            .orEmpty()

    private fun permanentProperty(campaign: com.jojo.game.domain.campaign.CampaignState?): (BattlePropertyItem, BattleUnit) -> Unit =
        campaign?.let { state -> { item, target ->
            target.characterId?.let { id ->
                when (item.itemType) {
                    42 -> state.setUnitAttribute(id, 9, target.maxHitPoints)
                    43 -> state.setUnitAttribute(id, 10, target.maxMagicPoints)
                }
            }
        } } ?: { _, _ -> }

    private fun experienceAward(
        campaign: com.jojo.game.domain.campaign.CampaignState?,
        catalog: GameDataCatalog?,
    ): (BattleUnit, Int) -> com.jojo.game.domain.campaign.CampaignExperienceResult? =
        if (campaign != null && catalog != null) { winner, amount ->
            if (winner.baseFaction != Faction.PLAYER) null else winner.characterId?.let { id ->
                val oldLevel = winner.level
                val result = campaign.grantExperience(id, oldLevel, amount, catalog)
                if (result.leveledUp) persistLevelGrowth(campaign, catalog, id, winner.posts, oldLevel, result.level)
                result
            }
        } else { _, _ -> null }

    private fun persistLevelGrowth(
        campaign: com.jojo.game.domain.campaign.CampaignState,
        catalog: GameDataCatalog,
        characterId: Int,
        posts: Int,
        oldLevel: Int,
        newLevel: Int,
    ) {
        val growth = catalog.unitLevelGrowth(characterId, posts, campaign)
        val defaults = catalog.unitLevelDerivedAttributes(characterId, posts, oldLevel, mine = true, campaign = campaign)
        growth.forEach { (attribute, perLevel) ->
            val current = campaign.unitAttribute(characterId, attribute, defaults.getValue(attribute))
            campaign.setUnitAttribute(characterId, attribute, current + perLevel * (newLevel - oldLevel))
        }
    }

    private fun equipmentExperienceAward(
        campaign: com.jojo.game.domain.campaign.CampaignState?,
        catalog: GameDataCatalog?,
    ): ((BattleUnit, BattleUnit, Int, BattleEquipmentExperienceKind) -> List<com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult>)? =
        if (campaign != null && catalog != null) { recipient, _, amount, kind ->
            recipient.characterId?.takeIf { recipient.baseFaction.isPlayerSide() }?.let { id ->
                campaign.equipmentProgression.grantExperienceAmount(
                    id, amount,
                    if (kind == BattleEquipmentExperienceKind.WEAPON) CampaignEquipmentSlot.WEAPON else CampaignEquipmentSlot.ARMOR,
                    catalog,
                )?.let(::listOf)
            }.orEmpty()
        } else null

    private fun restoreUnitExperience(
        campaign: com.jojo.game.domain.campaign.CampaignState?,
        catalog: GameDataCatalog?,
    ): (BattleUnit, Int) -> RestoreGrowthResolution<com.jojo.game.domain.campaign.CampaignExperienceResult> =
        if (campaign != null && catalog != null) { unit, amount ->
            unit.characterId?.let { id ->
                val before = catalog.learnedMagicIds(unit.posts, unit.level).toSet()
                val result = campaign.grantExperience(id, unit.level, amount, catalog)
                RestoreGrowthResolution.Applied(result.copy(learnedMagicIds =
                    catalog.learnedMagicIds(unit.posts, result.level).filterNot(before::contains)))
            } ?: RestoreGrowthResolution.Unavailable
        } else { _, _ -> RestoreGrowthResolution.Unavailable }

    private fun restoreEquipmentExperience(
        campaign: com.jojo.game.domain.campaign.CampaignState?,
        catalog: GameDataCatalog?,
    ): (BattleUnit, Int, CampaignEquipmentSlot) -> RestoreGrowthResolution<com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult> =
        if (campaign != null && catalog != null) { unit, amount, slot ->
            unit.characterId?.let { id -> campaign.equipmentProgression.grantExperienceAmount(id, amount, slot, catalog)
                ?.let { RestoreGrowthResolution.Applied(it) } ?: RestoreGrowthResolution.NotApplicable }
                ?: RestoreGrowthResolution.Unavailable
        } else { _, _, _ -> RestoreGrowthResolution.Unavailable }

    private val CARDINAL_OFFSETS = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
}
