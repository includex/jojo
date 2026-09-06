// Runtime
package com.jojo.game.application.runtime

import com.jojo.game.*

import com.badlogic.gdx.Screen
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit

/** RuntimeStartupRouter: 게임 시작 인자를 RuntimeStartupRequest로 묶어 확장 시작 경로에 위임한다. */
internal class RuntimeStartupRouter(
    private val game: JojoGame,
    private val state: String?,
    private val campaign: CampaignState,
    private val showScreen: (Screen) -> Unit,
    private val showBattlePreparation: (String, String, ScenarioJoinBattleLimit, Int) -> Unit,
) {
    fun route(): Boolean = RuntimeStartupExtensions.route(
        RuntimeStartupRequest(game, state, campaign, showScreen, showBattlePreparation),
    )
}
