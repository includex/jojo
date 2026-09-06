// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.application.battle.Battle
import com.jojo.game.application.battle.BattleEvent
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.battle.TurnTrigger
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.BattleEquipmentExperienceKind
import com.jojo.game.domain.battle.BattlePropertyItem
import com.jojo.game.domain.battle.isPlayerSide
import com.jojo.game.domain.campaign.CampaignEquipmentSlot
import com.jojo.game.domain.battle.settlement.RestoreGrowthResolution
import com.jojo.game.domain.scenario.ScenarioUnitFaction
import com.jojo.game.domain.scenario.battleId

/** BattleScenarioAssembler: 시나리오의 유닛·지형·이벤트를 조합해 실행 가능한 전투 집합을 만든다. */
internal object BattleScenarioAssembler {
    /**
     * `tutorialBattle`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `materialize`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

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

    /**
     * `GameDataCatalog`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun GameDataCatalog?.terrainValues(value: GameDataCatalog.(Int) -> Int): Map<Int, Int> =
        this?.let { data -> (0..64).mapNotNull { id -> data.value(id).takeIf { it != 0 }?.let { id to it } }.toMap() }
            .orEmpty()

    /**
     * `permanentProperty`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun permanentProperty(campaign: com.jojo.game.domain.campaign.CampaignState?): (BattlePropertyItem, BattleUnit) -> Unit =
        campaign?.let { state -> { item, target ->
            target.characterId?.let { id ->
                when (item.itemType) {
                    42 -> state.setUnitAttribute(id, 9, target.maxHitPoints)
                    43 -> state.setUnitAttribute(id, 10, target.maxMagicPoints)
                }
            }
        } } ?: { _, _ -> }

    /**
     * `experienceAward`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun experienceAward(
        campaign: com.jojo.game.domain.campaign.CampaignState?,
        catalog: GameDataCatalog?,
    ): (BattleUnit, Int) -> com.jojo.game.domain.campaign.CampaignExperienceResult? =
        if (campaign != null && catalog != null) { winner, amount ->
            if (winner.baseFaction != Faction.PLAYER) null else winner.characterId?.let { id ->
                /**
                 * `oldLevel` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val oldLevel = winner.level
                /**
                 * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val result = campaign.grantExperience(id, oldLevel, amount, catalog)
                if (result.leveledUp) persistLevelGrowth(campaign, catalog, id, winner.posts, oldLevel, result.level)
                result
            }
        } else { _, _ -> null }

    /**
     * `persistLevelGrowth`: 조건과 입력 상태를 검증한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun persistLevelGrowth(
        campaign: com.jojo.game.domain.campaign.CampaignState,
        catalog: GameDataCatalog,
        characterId: Int,
        posts: Int,
        oldLevel: Int,
        newLevel: Int,
    ) {
        /**
         * `growth` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val growth = catalog.unitLevelGrowth(characterId, posts, campaign)
        /**
         * `defaults` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val defaults = catalog.unitLevelDerivedAttributes(characterId, posts, oldLevel, mine = true, campaign = campaign)
        growth.forEach { (attribute, perLevel) ->
            /**
             * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val current = campaign.unitAttribute(characterId, attribute, defaults.getValue(attribute))
            campaign.setUnitAttribute(characterId, attribute, current + perLevel * (newLevel - oldLevel))
        }
    }

    /**
     * `equipmentExperienceAward`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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

    /**
     * `restoreUnitExperience`: 입력을 규칙에 따라 계산·변환한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun restoreUnitExperience(
        campaign: com.jojo.game.domain.campaign.CampaignState?,
        catalog: GameDataCatalog?,
    ): (BattleUnit, Int) -> RestoreGrowthResolution<com.jojo.game.domain.campaign.CampaignExperienceResult> =
        if (campaign != null && catalog != null) { unit, amount ->
            unit.characterId?.let { id ->
                /**
                 * `before` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val before = catalog.learnedMagicIds(unit.posts, unit.level).toSet()
                /**
                 * `result` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val result = campaign.grantExperience(id, unit.level, amount, catalog)
                RestoreGrowthResolution.Applied(result.copy(learnedMagicIds =
                    catalog.learnedMagicIds(unit.posts, result.level).filterNot(before::contains)))
            } ?: RestoreGrowthResolution.Unavailable
        } else { _, _ -> RestoreGrowthResolution.Unavailable }

    /**
     * `restoreEquipmentExperience`: 입력을 규칙에 따라 계산·변환한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun restoreEquipmentExperience(
        campaign: com.jojo.game.domain.campaign.CampaignState?,
        catalog: GameDataCatalog?,
    ): (BattleUnit, Int, CampaignEquipmentSlot) -> RestoreGrowthResolution<com.jojo.game.domain.campaign.CampaignEquipmentExperienceResult> =
        if (campaign != null && catalog != null) { unit, amount, slot ->
            unit.characterId?.let { id -> campaign.equipmentProgression.grantExperienceAmount(id, amount, slot, catalog)
                ?.let { RestoreGrowthResolution.Applied(it) } ?: RestoreGrowthResolution.NotApplicable }
                ?: RestoreGrowthResolution.Unavailable
        } else { _, _, _ -> RestoreGrowthResolution.Unavailable }

    /**
     * `CARDINAL_OFFSETS` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val CARDINAL_OFFSETS = setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1)
}
