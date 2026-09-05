package com.jojo.game.application.runtime

import com.jojo.game.*

import com.badlogic.gdx.Screen
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit

/** Delegates optional startup routes to externally supplied runtime extensions. */
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
