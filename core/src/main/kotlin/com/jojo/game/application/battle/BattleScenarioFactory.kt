// Battle
package com.jojo.game.application.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.application.runtime.BattleTraceRandomStreams
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.ScenarioBattleUnit

/** BattleScenarioFactory: 작성된 시나리오에서 전투를 생성하는 호환 진입점으로, 기존 호출 경로를 유지한다. */
object BattleScenarioFactory {
    fun tutorialBattle(): Battle = BattleScenarioAssembler.tutorialBattle()

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
        sourceRandomStreams: BattleTraceRandomStreams? = null,
        enabledFeatures: Int = 32,
    ): Battle = BattleScenarioAssembler.materialize(
        BattleScenarioRequest(
            units, blockedTiles, gameDataCatalog, terrain, enemyMasterInstanceId,
            initialWeather, weatherSchedule, weatherOffset, enemyEquipment, campaign,
            sourceRandomStreams, enabledFeatures,
        )
    )
}
