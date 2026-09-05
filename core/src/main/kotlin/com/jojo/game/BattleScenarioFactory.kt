package com.jojo.game

import com.jojo.game.domain.scenario.*
import com.jojo.game.domain.battle.settlement.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.*
import com.jojo.game.application.battle.toBattleMagicProfile
import com.jojo.game.domain.campaign.CampaignEquipmentSlot

/**
 * object  `BattleScenarioFactory`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object BattleScenarioFactory {
    /**
     * 공개 메서드 `tutorialBattle`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Battle`
     * - 반환값: 동작 결과의 도메인 값입니다.
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

    fun fromScriptedUnits(
        units: Collection<ScenarioBattleUnit>,
        blockedTiles: Set<Pair<Int, Int>> = emptySet(),
        gameDataCatalog: GameDataCatalog? = null,
        terrain: BattleTerrainGrid? = null,
        enemyMasterInstanceId: Int = -1,
        initialWeather: BattleWeather = BattleWeather.CLEAR,
        weatherSchedule: List<BattleWeather> = emptyList(),
        weatherOffset: Int = 0,
        enemyEquipment: Map<Int, List<Int>> = emptyMap(),
        campaign: CampaignState? = null,
        sourceRandomStreams: SourceRandomStreams? = null,
        /** Production Config enables ZDBHSW; focused callers may override. */
        enabledFeatures: Int = 32,
    ): Battle {
        val scriptedByBattleId = units.associateBy { it.battleId }

        /**
         * 공개 메서드 `projectUnit`
         *
         * ### 파라미터
        - `unit` (`ScenarioBattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `forcedLevel` (`Int? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
        - `forcedPosts` (`Int? = null`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `BattleUnit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun projectUnit(unit: ScenarioBattleUnit, forcedLevel: Int? = null, forcedPosts: Int? = null): BattleUnit {
            val persistentAttributes = campaign?.unitAttributes?.get(unit.characterId).orEmpty()
            // Scenario unit levels are zero-based script values, whereas the
            // persisted Unit.LV attribute is the displayed one-based level.
            val requestedLevel = forcedLevel?.minus(1)?.coerceAtLeast(0)
                ?: persistentAttributes[18]?.minus(1)?.coerceAtLeast(0)
                ?: unit.level
            val battleProfile = gameDataCatalog?.battleProfile(
                unit.characterId,
                requestedLevel,
                forcedPosts ?: persistentAttributes[17],
            )
            val equippedValues = if (unit.faction == ScenarioUnitFaction.MINE) {
                // setEnemyEquip is a legacy Stage API name; source scripts
                // also use it for named friendly/player actors. Campaign hall
                // equipment overrides that scenario fallback when present.
                campaign?.inventory?.equipment?.get(unit.characterId)?.asScriptValues()
                    ?: enemyEquipment[unit.characterId].orEmpty()
            } else enemyEquipment[unit.characterId].orEmpty()
            val defaultEquipmentValues = gameDataCatalog
                ?.defaultEquipment(battleProfile?.posts ?: 0, battleProfile?.level ?: 1)
                ?.asScriptValues().orEmpty()
            val effectiveEquipmentValues = listOf(
                if (equippedValues.getOrElse(0) { 0 } > 1) equippedValues[0] else defaultEquipmentValues.getOrElse(0) { 1 },
                if (equippedValues.getOrElse(0) { 0 } > 1) equippedValues.getOrElse(1) { 0 } else defaultEquipmentValues.getOrElse(
                    1
                ) { 1 },
                if (equippedValues.getOrElse(2) { 0 } > 1) equippedValues[2] else defaultEquipmentValues.getOrElse(2) { 1 },
                if (equippedValues.getOrElse(2) { 0 } > 1) equippedValues.getOrElse(3) { 0 } else defaultEquipmentValues.getOrElse(
                    3
                ) { 1 },
                if (equippedValues.getOrElse(4) { 0 } > 1) equippedValues[4] else defaultEquipmentValues.getOrElse(4) { 1 },
            )
            // Battle units equip their post's default weapon and armor first;
            // setEnemyEquip then replaces only explicitly supplied slots.
            // A five-value call containing only an accessory (R_00 unit 146)
            // must therefore retain both default stat bonuses.
            val equipment = gameDataCatalog?.equipmentBonus(effectiveEquipmentValues, battleProfile?.level ?: 1)
            val profile = battleProfile?.unit
            val arm = battleProfile?.arm
            val resolvedSkills = gameDataCatalog?.mergeSkills(
                gameDataCatalog.skillsForUnit(unit.characterId, battleProfile?.posts ?: 0, campaign),
                gameDataCatalog.equipmentSkills(effectiveEquipmentValues, battleProfile?.level ?: 1),
            ).orEmpty()

            /**
             * 공개 메서드 `passive`
             *
             * ### 파라미터
            - `base` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `skillId` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Int`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun passive(base: Int, skillId: Int): Int =
                gameDataCatalog?.passiveAbility(base, skillId, resolvedSkills) ?: base

            /**
             * 공개 메서드 `divineFloor`
             *
             * ### 파라미터
            - `base` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `sourceBase` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Int`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun divineFloor(base: Int, sourceBase: Int): Int {
                val growth = resolvedSkills[190]?.and(255)?.takeIf { it != 255 } ?: return base
                // Unit._baseBility: original raw ability + SMFT × level is a
                // lower bound before the ordinary passive ability modifiers.
                return maxOf(base, sourceBase + growth * (battleProfile?.level ?: 1))
            }

            /**
             * 공개 메서드 `ability`
             *
             * ### 파라미터
            - `base` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `sourceBase` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
            - `passiveSkill` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
             *
             * ### 응답 스펙
             * - 반환 타입: `Int`
             * - 반환값: 동작 결과의 도메인 값입니다.
             */

            fun ability(base: Int, sourceBase: Int, passiveSkill: Int): Int =
                passive(divineFloor(base, sourceBase), passiveSkill)

            val baseMaxHitPoints = persistentAttributes[9] ?: battleProfile?.maxHitPoints ?: 100
            val baseMaxMagicPoints = persistentAttributes[10] ?: battleProfile?.maxMagicPoints ?: 0
            // Unit.hitarea(): YJGJ replaces the post's normal hit-area ID.
            // The source can subsequently upgrade that pattern through
            // YJGJ_GJ; direct table patterns already cover the base override.
            val rangeSkill = resolvedSkills[258]?.and(255) ?: 255
            val attackHitArea = rangeSkill.takeIf { it != 255 }
                ?.let { gameDataCatalog?.hitAreaProfile(it) }
                ?: battleProfile?.hitArea
            val upgradedAttackHitArea = if ((resolvedSkills[260]?.and(255) ?: 255) != rangeSkill) {
                attackHitArea?.upgradeId?.let { gameDataCatalog?.hitAreaProfile(it) } ?: attackHitArea
            } else attackHitArea
            val learnedMagic = buildList {
                addAll(battleProfile?.magic.orEmpty())
                campaign?.extraMagic?.values
                    ?.filter { it.unitId == unit.characterId && it.learnLevel <= (battleProfile?.level ?: 1) }
                    ?.mapNotNull { learned -> gameDataCatalog?.magicProfile(learned.magicId) }
                    ?.forEach { magic -> if (none { it.id == magic.id }) add(magic) }
                // Unit.magics(): XHCL grants original strategy families by
                // bit flag, in addition to post/character learned tactics.
                val xhcl = resolvedSkills[244]?.and(255) ?: 255
                if (xhcl != 255) gameDataCatalog?.allMagicProfiles()
                    ?.filter { magic ->
                        (xhcl and 1 != 0 && magic.type in 0..3) ||
                                (xhcl and 2 != 0 && magic.type in 7..10) ||
                                (xhcl and 4 != 0 && (magic.type in 7..10 || magic.type in 15..18)) ||
                                (xhcl and 8 != 0 && magic.type == 19) ||
                                (xhcl and 16 != 0 && (magic.type in 11..14 || magic.type == 27)) ||
                                (xhcl and 32 != 0 && magic.type == 23) ||
                                (xhcl and 64 != 0 && magic.type == 24) ||
                                (xhcl and 128 != 0 && magic.type == 25)
                    }
                    ?.forEach { magic -> if (none { it.id == magic.id }) add(magic) }
            }.map { magic ->
                // BattleUnit.magicHitArea(): YJGJ_CL upgrades each strategy's
                // cast range through the same original hit-area table.
                if ((resolvedSkills[259]?.and(255) ?: 255) != 255) {
                    val upgraded = magic.hitArea.upgradeId.let { gameDataCatalog?.hitAreaProfile(it) }
                    if (upgraded != null) magic.copy(hitArea = upgraded) else magic
                } else magic
            }.map { magic ->
                // BattleUnit.magicEffarea(): this is independent from the
                // cast-range upgrade above and expands affected neighbours.
                if ((resolvedSkills[264]?.and(255) ?: 255) != 255) {
                    gameDataCatalog?.upgradedEffectArea(magic.effectAreaId)?.let { (id, offsets) ->
                        magic.copy(effectAreaId = id, effectOffsets = offsets)
                    } ?: magic
                } else magic
            }.map { it.toBattleMagicProfile() }
            return BattleUnit(
                id = unit.battleId,
                name = campaign?.unitNames?.get(unit.characterId) ?: profile?.name ?: "유닛 ${unit.characterId}",
                faction = when (unit.faction) {
                    ScenarioUnitFaction.MINE -> Faction.PLAYER
                    ScenarioUnitFaction.FRIEND -> Faction.FRIEND
                    ScenarioUnitFaction.ENEMY -> if (unit.reinforcement) Faction.REINFORCEMENTS else Faction.ENEMY
                },
                tileX = unit.x,
                tileY = unit.y,
                visible = !unit.hidden,
                direction = unit.direction,
                characterId = unit.characterId,
                battleSlot = unit.battleSlot,
                famous = profile?.famous == true,
                hasAuthoredTileX = unit.authoredX,
                hasAuthoredTileY = unit.authoredY,
                hitPoints = passive(baseMaxHitPoints, 52),
                maxHitPoints = passive(baseMaxHitPoints, 52),
                magicPoints = passive(baseMaxMagicPoints, 53),
                maxMagicPoints = passive(baseMaxMagicPoints, 53),
                level = battleProfile?.level ?: 1,
                experience = if (unit.faction == ScenarioUnitFaction.MINE) persistentAttributes[19] ?: 0 else 0,
                posts = battleProfile?.posts ?: profile?.posts ?: 0,
                // Unit._baseBility adds equipped item values after reading
                // the persisted base ability.  Keep the addition outside the
                // Elvis expression; otherwise every campaign-backed unit
                // silently loses its weapon/armor bonus.
                attack = ability(
                    (persistentAttributes[2] ?: (battleProfile?.attack ?: 45)) + (equipment?.attack ?: 0),
                    profile?.attack ?: 45,
                    65
                ),
                defense = ability(
                    (persistentAttributes[3] ?: (battleProfile?.defense ?: 25)) + (equipment?.defense ?: 0),
                    profile?.defense ?: 25,
                    61
                ),
                spirit = ability(
                    (persistentAttributes[4] ?: (battleProfile?.spirit ?: 35)) + (equipment?.spirit ?: 0),
                    profile?.spirit ?: 35,
                    68
                ),
                critical = ability(
                    persistentAttributes[5] ?: battleProfile?.critical ?: 35,
                    profile?.critical ?: 35,
                    54
                ),
                morale = ability(persistentAttributes[6] ?: battleProfile?.morale ?: 35, profile?.morale ?: 35, 73),
                martial = profile?.attack ?: battleProfile?.attack ?: 45,
                armId = arm?.id ?: 0,
                armType = arm?.type ?: 0,
                remoteAttack = arm?.remote ?: false,
                armMoveSound = arm?.moveSound ?: 0,
                fastMove = arm?.fastMove ?: true,
                attackDelay = arm?.attackDelay ?: false,
                armRestraints = buildMap {
                    (0 until 40).forEach { targetArm ->
                        put(
                            targetArm,
                            arm?.restraintAgainst(targetArm) ?: 100
                        )
                    }
                },
                terrainImpacts = buildMap {
                    (0 until 30).forEach { terrainId ->
                        put(
                            terrainId,
                            arm?.terrainImpact(terrainId) ?: 100
                        )
                    }
                },
                terrainMovementCosts = buildMap {
                    (0 until 30).forEach { terrainId ->
                        put(
                            terrainId,
                            arm?.terrainMoveCost(terrainId) ?: 1
                        )
                    }
                },
                magicHarmRate = arm?.magicHarmRate ?: 100,
                attackOffsets = upgradedAttackHitArea?.offsets ?: setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
                // Unit.effarea(): ZHUORE(0), or the CTGJ skill's effect
                // area ID.  This is deliberately separate from hit-area.
                attackEffectOffsets = gameDataCatalog?.effectAreaOffsets(
                    resolvedSkills[32]?.and(255)?.takeIf { it != 255 } ?: 0).orEmpty(),
                attackEffectAreaId = resolvedSkills[32]?.and(255)?.takeIf { it != 255 } ?: 0,
                attackAllScreen = upgradedAttackHitArea?.allScreen ?: false,
                magic = learnedMagic,
                skills = resolvedSkills,
                movement = (battleProfile?.movement ?: 3) + (resolvedSkills[77]?.and(255)?.takeIf { it != 255 } ?: 0),
                ai = unit.ai,
                aiTargetCharacterId = unit.aiTargetId,
                aiTargetX = unit.aiTargetX,
                aiTargetY = unit.aiTargetY,
                retireMessage = unit.characterId.let { gameDataCatalog?.retreatText(it) },
                criticalSpeech = profile?.criticalSpeech
                    ?: GameDataCatalog.CriticalSpeechProfile(emptyList(), randomized = false),
                deathMessageEnabled = unit.deathMessageEnabled,
                retreatCount = persistentAttributes[15] ?: 0,
            )
        }
        return Battle(
            units = units.map { projectUnit(it) },
            events = emptyList(),
            blockedTiles = blockedTiles,
            terrain = terrain,
            // BattleScreen.enemyMasterId() first resolves this authored value via
            // `_unitIds[characterId]`; it is not an enemy `i`/slot index.
            enemyMasterUnitId = units.firstOrNull {
                it.faction == ScenarioUnitFaction.ENEMY && it.characterId == enemyMasterInstanceId
            }?.battleId,
            initialWeather = initialWeather,
            weatherSchedule = weatherSchedule,
            weatherOffset = weatherOffset,
            terrainMagicFlags = gameDataCatalog?.let { data ->
                buildMap {
                    (0..64).forEach { terrainId ->
                        data.terrainMagicFlag(terrainId).takeIf { it != 0 }?.let { put(terrainId, it) }
                    }
                }
            }.orEmpty(),
            terrainResumeRates = gameDataCatalog?.let { data ->
                buildMap {
                    (0..64).forEach { terrainId ->
                        data.terrainResumeHp(terrainId).takeIf { it != 0 }?.let { put(terrainId, it) }
                    }
                }
            }.orEmpty(),
            terrainResumeMp = gameDataCatalog?.let { data ->
                buildMap {
                    (0..64).forEach { terrainId ->
                        data.terrainResumeMp(terrainId).takeIf { it != 0 }?.let { put(terrainId, it) }
                    }
                }
            }.orEmpty(),
            enabledFeatures = enabledFeatures,
            statusRoundFor = { status -> gameDataCatalog?.statusRound(status) ?: 3 },
            attributeStatusRoundFor = { attribute -> gameDataCatalog?.attributeStatusRound(attribute) ?: 3 },
            movementOffsets = gameDataCatalog?.hitAreaProfile(0)?.offsets ?: setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
            directDestinationOffsets = gameDataCatalog?.hitAreaProfile(13)?.offsets?.toList().orEmpty(),
            infantryOffsets = gameDataCatalog?.hitAreaProfile(1)?.offsets ?: setOf(0 to 1, 1 to 0, -1 to 0, 0 to -1),
            // _attack3 ZDSY passes an item ID directly to _usePro2.  Its enemy
            // branch has no ItemStore lookup, so all usable original items—not
            // merely the player's current inventory—must be available here.
            propertyItems = gameDataCatalog?.battlePropertyItems().orEmpty()
                .map { BattlePropertyItem(it.id, it.name, it.itemType, it.value) }
                .associateBy { it.id },
            consumeProperty = campaign?.let { state -> { itemId: Int -> state.inventory.consumeItem(itemId) } }
                ?: { false },
            zdsyGlobalValue = (campaign?.globalVariables?.get(4035) as? Number)?.toInt() ?: 0,
            consumeAutomaticProperty = campaign?.let { state -> { itemId: Int -> state.inventory.consumeItem(itemId); Unit } }
                ?: {},
            onPermanentProperty = campaign?.let { state ->
                { item: BattlePropertyItem, target: BattleUnit ->
                    target.characterId?.let { characterId ->
                        when (item.itemType) {
                            42 -> state.setUnitAttribute(characterId, 9, target.maxHitPoints)
                            43 -> state.setUnitAttribute(characterId, 10, target.maxMagicPoints)
                        }
                    }
                }
            } ?: { _, _ -> },
            // Unit EXP is settled for every resolved physical/magic target,
            // including non-lethal and zero-harm guard records (but not true
            // misses, which never enter source `_attack3`). Keep defeat notification independent
            // so a kill cannot grant the same reward twice.
            onUnitDefeated = { _, _ -> },
            onBattleExperience = if (campaign != null && gameDataCatalog != null) experience@{ winner, amount ->
                if (winner.baseFaction != Faction.PLAYER) return@experience null
                val characterId = winner.characterId ?: return@experience null
                val oldLevel = winner.level
                val result = campaign.grantExperience(characterId, oldLevel, amount, gameDataCatalog)
                if (result.leveledUp) {
                    // Unit.setLevel's normal path incrementally persists ATT..MP
                    // before rebuilding BattleUnit's derived caches.
                    val growth = gameDataCatalog.unitLevelGrowth(characterId, winner.posts, campaign)
                    val defaults = gameDataCatalog.unitLevelDerivedAttributes(
                        characterId, winner.posts, oldLevel, mine = true, campaign = campaign,
                    )
                    growth.forEach { (attribute, perLevel) ->
                        val current = campaign.unitAttribute(characterId, attribute, defaults.getValue(attribute))
                        campaign.setUnitAttribute(
                            characterId,
                            attribute,
                            current + perLevel * (result.level - oldLevel)
                        )
                    }
                }
                result
            } else { _, _ -> null },
            experienceLimit = { level -> gameDataCatalog?.unitExperienceLimit(level) ?: 100 },
            levelLimit = gameDataCatalog?.unitLevelLimit() ?: 50,
            onBattleLevelUp = refresh@{ live ->
                val scripted = scriptedByBattleId[live.id] ?: return@refresh
                live.refreshLevelDerivedState(projectUnit(scripted, forcedLevel = live.level, forcedPosts = live.posts))
            },
            onUnitRetreat = campaign?.let { state ->
                { unit: BattleUnit ->
                    unit.characterId?.let { state.setUnitAttribute(it, 15, unit.retreatCount) }
                }
            } ?: {},
            onEquipmentExperienceAward = if (campaign != null && gameDataCatalog != null) { recipient, _, amount, kind ->
                recipient.characterId
                    ?.takeIf { recipient.baseFaction.isPlayerSide() }
                    ?.let { id ->
                        campaign.equipmentProgression.grantExperienceAmount(
                            id,
                            amount,
                            if (kind == BattleEquipmentExperienceKind.WEAPON) CampaignEquipmentSlot.WEAPON else CampaignEquipmentSlot.ARMOR,
                            gameDataCatalog,
                        )
                    }
                    ?.let(::listOf)
                    ?: emptyList()
            } else null,
            onRestoreUnitExperience = if (campaign != null && gameDataCatalog != null) { unit, amount ->
                unit.characterId?.let { id ->
                    val beforeMagic = gameDataCatalog.learnedMagicIds(unit.posts, unit.level).toSet()
                    val result = campaign.grantExperience(id, unit.level, amount, gameDataCatalog)
                    RestoreGrowthResolution.Applied(
                        result.copy(
                            learnedMagicIds = gameDataCatalog.learnedMagicIds(unit.posts, result.level)
                                .filterNot(beforeMagic::contains),
                        )
                    )
                } ?: RestoreGrowthResolution.Unavailable
            } else { _, _ -> RestoreGrowthResolution.Unavailable },
            onRestoreEquipmentExperience = if (campaign != null && gameDataCatalog != null) { unit, amount, slot ->
                unit.characterId?.let { id ->
                    campaign.equipmentProgression.grantExperienceAmount(id, amount, slot, gameDataCatalog)
                        ?.let { RestoreGrowthResolution.Applied(it) }
                        ?: RestoreGrowthResolution.NotApplicable
                } ?: RestoreGrowthResolution.Unavailable
            } else { _, _, _ -> RestoreGrowthResolution.Unavailable },
            sourceRandomStreams = sourceRandomStreams,
        ).also { it.initializeAllRateGauges() }
    }
}
