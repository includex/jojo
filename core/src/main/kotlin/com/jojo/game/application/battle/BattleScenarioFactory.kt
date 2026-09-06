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
    /**
     * `tutorialBattle`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun tutorialBattle(): Battle = BattleScenarioAssembler.tutorialBattle()

    /**
     * `fromScriptedUnits`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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
