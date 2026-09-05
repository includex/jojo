package com.jojo.game.application.battle

import com.jojo.game.GameDataCatalog
import com.jojo.game.application.runtime.BattleTraceRandomStreams
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.ScenarioBattleUnit

/** Inputs supplied by a scenario entry point before a tactical battle starts. */
internal data class BattleScenarioRequest(
    val units: Collection<ScenarioBattleUnit>,
    val blockedTiles: Set<Pair<Int, Int>> = emptySet(),
    val gameDataCatalog: GameDataCatalog? = null,
    val terrain: BattleTerrainGrid? = null,
    val enemyMasterInstanceId: Int = -1,
    val initialWeather: BattleWeather = BattleWeather.CLEAR,
    val weatherSchedule: List<BattleWeather> = emptyList(),
    val weatherOffset: Int = 0,
    val enemyEquipment: Map<Int, List<Int>> = emptyMap(),
    val campaign: CampaignState? = null,
    val sourceRandomStreams: BattleTraceRandomStreams? = null,
    val enabledFeatures: Int = 32,
)
