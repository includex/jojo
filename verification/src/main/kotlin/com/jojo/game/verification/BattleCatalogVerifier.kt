// Verification
package com.jojo.game.verification
import com.jojo.game.application.scenario.*

import com.badlogic.gdx.Gdx
import com.jojo.game.application.battle.BattleScenarioFactory
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.infrastructure.data.BattleTerrainLoader
import com.jojo.game.domain.battle.Faction
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.infrastructure.data.ScenarioCatalog
import com.jojo.game.domain.scenario.ScenarioUnitFaction
import com.jojo.game.domain.scenario.battleId

/** BattleCatalogVerificationResult: 검증 대상 목록과 조회 규칙을 제공하는 타입이다. */
internal data class BattleCatalogVerificationResult(
    /** marker: 검증 흐름에서 사용하는 값을 담는다. */
    val marker: String,
    /** astGapMarker: ast gap marker 값을 보관해 검증 흐름에서 사용한다. */
    val astGapMarker: String,
)

/** BattleCatalogVerifier: 모든 전투 스크립트를 구체화하고 게임 데이터 투영을 검증한다. */
internal class BattleCatalogVerifier(private val gameData: GameDataCatalog) {
    /** dataDiagnostics: 전투 데이터 표의 진단 정보를 반환한다. */
    fun dataDiagnostics(): List<String> {
        val magicProfiles = gameData.allMagicProfiles()
        return listOf(
            "GAME_DATA_MAGIC_COVERAGE: ${magicProfiles.size} strategies; types=" +
                magicProfiles.groupingBy { it.type }.eachCount().toSortedMap().entries
                    .joinToString { "${it.key}:${it.value}" } +
                "; categories=" + magicProfiles.groupingBy { it.category }.eachCount().toSortedMap().entries
                    .joinToString { "${it.key}:${it.value}" },
            "GAME_DATA_MAGIC_SPECIAL_TYPES: " + magicProfiles.filter { it.type >= 21 }
                .groupBy { it.type }.toSortedMap().entries.joinToString("; ") { (type, spells) ->
                    "$type=${spells.joinToString { "${it.id}:${it.name}/c${it.category}" }}"
                },
            "GAME_DATA_TERRAIN_MAGIC_FLAGS: " + (0..20).joinToString { "$it:${gameData.terrainMagicFlag(it)}" },
            "GAME_DATA_CONFIG_KEYS: ${gameData.configTopLevelKeys()}",
            "GAME_DATA_MOVEMENT_OFFSETS: ${gameData.hitAreaProfile(0)?.offsets}",
        )
    }

    /** verify: 전투 카탈로그와 초기 미처리 호출 집계를 함께 검증한다. */
    fun verify(initialUnhandledCalls: Map<String, Int>): BattleCatalogVerificationResult {
        val modules = ScenarioCatalog.sModuleNames()
        val unhandledCalls = linkedMapOf<String, Int>().apply { putAll(initialUnhandledCalls) }
        var initializedBattles = 0
        var mappedBattles = 0
        var liveBattleEvents = 0
        var routedBattleEndings = 0
        var materializedBattleUnits = 0
        var equippedBattleUnits = 0
        var automatedCampResolutions = 0

        modules.forEach { module ->
            val runtime = ScenarioInterpreter.load(module)
            check("scene0" in runtime.functionNames) { "$module scene0이 없습니다." }
            runtime.start("scene0")
            mergeUnhandled(unhandledCalls, runtime.unhandledCalls)
            if (runtime.stage.battleUnits.isNotEmpty()) initializedBattles++
            val terrain = BattleTerrainLoader.load(runtime.stage.battleMapIndex)
            val tacticalState = BattleScenarioFactory.fromScriptedUnits(
                runtime.stage.battleUnits.values,
                runtime.stage.mapObjects.values.filter { it.enabled }.mapTo(linkedSetOf()) { it.x to it.y },
                gameData,
                terrain,
                runtime.stage.enemyMasterInstanceId,
                runtime.stage.initialBattleWeather(),
                runtime.stage.battleWeatherSchedule(),
                runtime.stage.battleWeatherOffset,
                runtime.stage.enemyEquipment,
            )
            check(tacticalState.units.size == runtime.stage.battleUnits.size) {
                "$module 전투 유닛 변환 수가 다릅니다."
            }
            runtime.stage.battleUnits.values.forEach { scripted ->
                val profile = requireNotNull(gameData.unitProfile(scripted.characterId)) {
                    "$module 캐릭터 데이터가 없습니다: ${scripted.characterId}"
                }
                val battleProfile = requireNotNull(gameData.battleProfile(scripted.characterId, scripted.level)) {
                    "$module 전투 능력치를 만들지 못했습니다: ${scripted.characterId}"
                }
                val tactical = requireNotNull(tacticalState.units[scripted.battleId])
                val equippedValues = runtime.stage.enemyEquipment[scripted.characterId].orEmpty()
                val scriptedEquipment = gameData.equipmentBonus(equippedValues, battleProfile.level)
                val defaultEquipment = gameData.defaultEquipmentBonus(battleProfile.posts, battleProfile.level)
                val equipment = GameDataCatalog.EquipmentBonus(
                    attack = if (equippedValues.getOrElse(0) { 0 } > 1) scriptedEquipment.attack else defaultEquipment.attack,
                    defense = if (equippedValues.getOrElse(2) { 0 } > 1) scriptedEquipment.defense else defaultEquipment.defense,
                    spirit = if (equippedValues.getOrElse(0) { 0 } > 1) scriptedEquipment.spirit else defaultEquipment.spirit,
                )
                val mergedSkills = gameData.mergeSkills(
                    gameData.skillsForUnit(scripted.characterId, battleProfile.posts),
                    gameData.equipmentSkills(equippedValues, battleProfile.level),
                )
                val rangeSkill = mergedSkills[258]?.and(255) ?: 255
                val sourceHitArea = rangeSkill.takeIf { it != 255 }
                    ?.let(gameData::hitAreaProfile)
                    ?: battleProfile.hitArea
                val expectedHitArea = if ((mergedSkills[260]?.and(255) ?: 255) != rangeSkill) {
                    gameData.hitAreaProfile(sourceHitArea.upgradeId) ?: sourceHitArea
                } else sourceHitArea

                /** expectedAbility: 검증 흐름에 필요한 동작을 실행하고 결과를 반환한다. */
                fun expectedAbility(base: Int, sourceBase: Int, passiveSkill: Int): Int {
                    val smft = mergedSkills[190]?.and(255)?.takeIf { it != 255 } ?: 0
                    return gameData.passiveAbility(
                        maxOf(base, sourceBase + smft * battleProfile.level),
                        passiveSkill,
                        mergedSkills,
                    )
                }
                /**
                 * `conversionMatches` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val conversionMatches = tactical.id == scripted.battleId &&
                    tactical.characterId == scripted.characterId &&
                    tactical.faction == scriptedFaction(scripted.faction, scripted.reinforcement) &&
                    tactical.tileX == scripted.x && tactical.tileY == scripted.y &&
                    tactical.direction == scripted.direction && tactical.visible == !scripted.hidden &&
                    tactical.ai == scripted.ai &&
                    tactical.aiTargetCharacterId == scripted.aiTargetId &&
                    tactical.aiTargetX == scripted.aiTargetX && tactical.aiTargetY == scripted.aiTargetY &&
                    tactical.name == profile.name &&
                    tactical.maxHitPoints == gameData.passiveAbility(battleProfile.maxHitPoints, 52, mergedSkills) &&
                    tactical.maxMagicPoints == gameData.passiveAbility(battleProfile.maxMagicPoints, 53, mergedSkills) &&
                    tactical.attack == expectedAbility(battleProfile.attack + equipment.attack, profile.attack, 65) &&
                    tactical.defense == expectedAbility(battleProfile.defense + equipment.defense, profile.defense, 61) &&
                    tactical.spirit == expectedAbility(battleProfile.spirit + equipment.spirit, profile.spirit, 68) &&
                    tactical.critical == expectedAbility(battleProfile.critical, profile.critical, 54) &&
                    tactical.morale == expectedAbility(battleProfile.morale, profile.morale, 73) &&
                    tactical.movement == battleProfile.movement +
                    (mergedSkills[77]?.and(255)?.takeIf { it != 255 } ?: 0) &&
                    tactical.attackOffsets == expectedHitArea.offsets &&
                    tactical.attackAllScreen == expectedHitArea.allScreen &&
                    tactical.magic.map { it.id } == battleProfile.magic.map { it.id } &&
                    mergedSkills.all { (skillId, effect) -> tactical.skills[skillId] == effect } &&
                    tactical.armRestraints[battleProfile.arm.id] ==
                    battleProfile.arm.restraintAgainst(battleProfile.arm.id)
                check(conversionMatches) {
                    "$module 능력치 변환이 일치하지 않습니다: ${scripted.characterId}; " +
                        "actual=hp${tactical.maxHitPoints}/mp${tactical.maxMagicPoints}/att${tactical.attack}/" +
                        "def${tactical.defense}/spr${tactical.spirit}/cri${tactical.critical}/" +
                        "mor${tactical.morale}/mov${tactical.movement}/magic${tactical.magic.map { it.id }}/" +
                        "offsets${tactical.attackOffsets}; expected=hp" +
                        "${gameData.passiveAbility(battleProfile.maxHitPoints, 52, mergedSkills)}/mp" +
                        "${gameData.passiveAbility(battleProfile.maxMagicPoints, 53, mergedSkills)}/att" +
                        "${expectedAbility(battleProfile.attack + equipment.attack, profile.attack, 65)}/def" +
                        "${expectedAbility(battleProfile.defense + equipment.defense, profile.defense, 61)}/spr" +
                        "${expectedAbility(battleProfile.spirit + equipment.spirit, profile.spirit, 68)}/cri" +
                        "${expectedAbility(battleProfile.critical, profile.critical, 54)}/mor" +
                        "${expectedAbility(battleProfile.morale, profile.morale, 73)}/mov" +
                        "${battleProfile.movement + (mergedSkills[77]?.and(255)?.takeIf { it != 255 } ?: 0)}/" +
                        "magic${battleProfile.magic.map { it.id }}/offsets${expectedHitArea.offsets}"
                }
                if (runtime.stage.enemyEquipment.containsKey(scripted.characterId)) equippedBattleUnits++
                materializedBattleUnits++
            }

            for (ignored in 0 until 3) {
                /**
                 * `turn` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val turn = tacticalState.roundLifecycle.endTurn()
                if (turn.activeFaction == Faction.PLAYER) break
                tacticalState.ai.resolveTurn()
                automatedCampResolutions++
            }
            /**
             * `mapId` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val mapId = runtime.stage.battleMapIndex + 1
            if (sequenceOf("jpg", "png", "webp").any {
                    Gdx.files.internal("maps/battle-maps/$mapId.$it").exists()
                } && terrain.width > 0 && terrain.height > 0
            ) mappedBattles++
            if ("scene1" in runtime.functionNames) {
                runtime.setBattleContext(ScenarioBattleScriptContext(round = 1, camp = 1))
                runtime.start("scene1")
                if (runtime.state == PlaybackState.DIALOGUE || runtime.state == PlaybackState.CHOICE) {
                    liveBattleEvents++
                }
                mergeUnhandled(unhandledCalls, runtime.unhandledCalls)
            }
            if ("scene2" in runtime.functionNames) {
                runtime.start("scene2")
                ScenarioRuntimeDrain.toCompletion(
                    runtime,
                    limit = 1_000,
                    failureMessage = "$module 전투 종료 전환이 완료되지 않았습니다.",
                )
                if (runtime.stage.sceneJumpTarget != null) routedBattleEndings++
                mergeUnhandled(unhandledCalls, runtime.unhandledCalls)
            }
        }
        check(initializedBattles == modules.size) {
            "유닛을 생성하지 못한 S 전투 스크립트: ${modules.size - initializedBattles}개"
        }
        check(mappedBattles == modules.size) {
            "맵 이미지를 찾지 못한 S 전투 스크립트: ${modules.size - mappedBattles}개"
        }
        /**
         * `topUnhandled` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val topUnhandled = unhandledCalls.entries.sortedByDescending { it.value }.take(12)
            .joinToString { "${it.key}=${it.value}" }
        return BattleCatalogVerificationResult(
            marker = "VERIFY_ALL_BATTLES_OK: ${modules.size} S battle scripts initialized units and game maps; " +
                "$liveBattleEvents emitted a live round-1 dialogue/choice event; " +
                "$routedBattleEndings exercised source scene2 jump routes; " +
                "$materializedBattleUnits units received verified game-data profiles; " +
                "$equippedBattleUnits scripted equipment profiles were applied; " +
                "$automatedCampResolutions automatic FRIEND/ENEMY/REINFORCEMENTS camp entries resolved",
            astGapMarker = "AST_API_GAPS: ${if (topUnhandled.isBlank()) "none" else topUnhandled}",
        )
    }

    /** scriptedFaction: scripted faction에 필요한 검증 동작을 실행하고 결과를 반환한다. */
    private fun scriptedFaction(faction: ScenarioUnitFaction, reinforcement: Boolean): Faction = when (faction) {
        ScenarioUnitFaction.MINE -> Faction.PLAYER
        ScenarioUnitFaction.FRIEND -> Faction.FRIEND
        ScenarioUnitFaction.ENEMY -> if (reinforcement) Faction.REINFORCEMENTS else Faction.ENEMY
    }
}
