package com.jojo.game.verification

import com.jojo.game.BattleOutcome
import com.jojo.game.BattleScenarioFactory
import com.jojo.game.BattleTerrainGrid
import com.jojo.game.CampaignState
import com.jojo.game.Faction
import com.jojo.game.GameDataCatalog
import com.jojo.game.ScenarioInterpreter
import com.jojo.game.ScenarioJoinBattleLimit
import com.jojo.game.isEnemySide

/** Exercises the real R_00 → S_00 campaign and scripted victory route. */
internal class YingchuanRouteVerifier(private val gameData: GameDataCatalog) {
    fun verify(): String {
        val campaign = CampaignState()
        val prelude = ScenarioInterpreter.load("R_00", campaign).apply { start("scene1") }
        ScenarioRuntimeDrain.toCompletion(
            prelude,
            chooseGameStart = true,
            failureMessage = "R_00 영천 진입 이벤트를 끝까지 재생하지 못했습니다.",
        )
        check(campaign.joinedUnits.isNotEmpty()) { "R_00 영입 유닛이 생성되지 않았습니다." }
        if (campaign.roster.battleRoster.isEmpty()) configureInitialRoster(campaign)

        val runtime = ScenarioInterpreter.load("S_00", campaign).apply { start("scene0") }
        ScenarioRuntimeDrain.toCompletion(runtime, failureMessage = "영천 이벤트 대사가 종료되지 않았습니다.")
        runtime.setBattleContext(ScenarioInterpreter.BattleScriptContext(round = 1, camp = -1))
        runtime.start("scene1")
        ScenarioRuntimeDrain.toCompletion(runtime, failureMessage = "영천 첫 턴 이벤트가 종료되지 않았습니다.")
        verifyBattleOperationStart(campaign)

        val terrain = BattleTerrainGrid.load(runtime.stage.battleMapIndex)
        val battle = BattleScenarioFactory.fromScriptedUnits(
            runtime.stage.battleUnits.values,
            runtime.stage.mapObjects.values
                .filter { it.enabled && it.objectId > 3 }
                .mapTo(linkedSetOf()) { it.x to it.y },
            gameData,
            terrain,
            runtime.stage.enemyMasterInstanceId,
            runtime.stage.initialBattleWeather(),
            runtime.stage.battleWeatherSchedule(),
            runtime.stage.battleWeatherOffset,
            runtime.stage.enemyEquipment,
            campaign,
        )
        check(battle.units.values.any { it.faction == Faction.PLAYER }) {
            "R_00→S_00 플레이어 전투 유닛이 생성되지 않았습니다."
        }

        var aiActions = 0
        for (ignored in 0 until 3) {
            val turn = battle.endTurn()
            if (turn.activeFaction == Faction.PLAYER) break
            val ai = battle.resolveAiTurn()
            aiActions += ai.moves + ai.attacks + ai.holds
        }
        check(
            battle.units.values.any { it.faction == Faction.FRIEND } &&
                battle.units.values.any { it.faction.isEnemySide() },
        ) { "영천 전투 자동 진영이 생성되지 않았습니다." }

        val bossIds = listOf(146, 147)
        val attacker = battle.units.values.firstOrNull {
            it.visible && (it.faction == Faction.PLAYER || it.faction == Faction.FRIEND)
        } ?: error("영천 전투 시작 후 사용할 수 있는 아군 전투 유닛이 없습니다.")
        bossIds.forEach { bossId ->
            repeat(256) {
                val boss = battle.units.values.firstOrNull { it.characterId == bossId } ?: return@repeat
                battle.forcedAttack(attacker.id, boss.id)
            }
            check(battle.units.values.none { it.characterId == bossId }) {
                val boss = battle.units.values.first { it.characterId == bossId }
                "영천 보스 $bossId 가 실제 전투 피해로 격파되지 않았습니다: " +
                    "hp=${boss.hitPoints}/${boss.maxHitPoints}, attacker=${attacker.hitPoints}/${attacker.maxHitPoints}, " +
                    "visible=${attacker.visible}"
            }
        }

        val defeatedBossAttributes = bossIds.associateWith { bossId ->
            val hp = battle.units.values.firstOrNull { it.characterId == bossId }?.hitPoints ?: 0
            mapOf(7 to hp)
        }
        val victoryContext = ScenarioInterpreter.BattleScriptContext(
            round = 2,
            camp = 2,
            attributes = defeatedBossAttributes,
        )
        runtime.setBattleContext(victoryContext)
        runtime.start("scene1")
        ScenarioRuntimeDrain.toCompletion(runtime, failureMessage = "영천 보스 격파 분기가 종료되지 않았습니다.")
        check(!runtime.stage.unit(146).visible && !runtime.stage.unit(147).visible) {
            "영천 보스 격파 상태가 stage.unitStateTest 분기에 반영되지 않았습니다."
        }
        runtime.setBattleContext(victoryContext)
        runtime.start("scene1")
        ScenarioRuntimeDrain.toCompletion(runtime, failureMessage = "영천 승리 분기가 종료되지 않았습니다.")
        check(
            runtime.stage.scriptedBattleOutcome == BattleOutcome.PLAYER_VICTORY &&
                runtime.stage.battleEndedByScript,
        ) { "영천 승리 조건(146/147 격파) 이후 reward/end에 도달하지 못했습니다." }

        val visibleCamps = Faction.entries.joinToString { faction ->
            "$faction=${battle.units.values.count { it.visible && it.faction == faction }}"
        }
        return "VERIFY_YINGCHUAN_ROUTE_OK: roster=${campaign.roster.battleRoster.size}, " +
            "units=${battle.units.size}, visible=[$visibleCamps], aiActions=$aiActions, bossWin=end"
    }

    private fun configureInitialRoster(campaign: CampaignState) {
        val selection = campaign.joinedUnits.take(15)
        check(
            campaign.roster.setBattleRoster(
                selection,
                ScenarioJoinBattleLimit(
                    minimum = 1,
                    maximum = selection.size,
                    requiredUnitIds = emptyList(),
                    excludedUnitIds = emptyList(),
                ),
            ),
        ) { "R_00 전투 명단을 구성하지 못했습니다." }
    }

    private fun verifyBattleOperationStart(campaign: CampaignState) {
        val runtime = ScenarioInterpreter.load("S_00", campaign).apply { start("scene0") }
        ScenarioRuntimeDrain.toCompletion(runtime, failureMessage = "영천 입력 개방 검증 초기화가 종료되지 않았습니다.")
        runtime.setBattleContext(ScenarioInterpreter.BattleScriptContext(round = 1, camp = -1))
        runtime.start("scene1")
        ScenarioRuntimeDrain.toCompletion(runtime, failureMessage = "영천 입력 개방 검증 첫 턴이 종료되지 않았습니다.")
        runtime.setBattleContext(ScenarioInterpreter.BattleScriptContext(round = 1, camp = 2))
        runtime.start("scene1")
        ScenarioRuntimeDrain.toCompletion(runtime, failureMessage = "영천 증원 진입 이벤트가 종료되지 않았습니다.")
        runtime.setBattleContext(ScenarioInterpreter.BattleScriptContext(round = 2, camp = 2))
        runtime.start("scene1")
        ScenarioRuntimeDrain.toCompletion(runtime, failureMessage = "영천 전투 도입 대사가 종료되지 않았습니다.")
        check(runtime.stage.battleOperationStarted) { "S_00 startOper 이전에 전술 입력이 열렸습니다." }
    }
}
