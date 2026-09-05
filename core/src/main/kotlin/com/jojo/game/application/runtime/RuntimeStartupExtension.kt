package com.jojo.game.application.runtime

import com.jojo.game.*

import com.badlogic.gdx.Screen
import com.jojo.game.domain.campaign.CampaignState
import com.jojo.game.domain.scenario.ScenarioJoinBattleLimit
import java.util.ServiceLoader

/** Neutral production seam for optional, externally supplied startup routes. */
data class RuntimeStartupRequest(
    val game: JojoGame,
    val state: String?,
    val campaignState: CampaignState,
    val showScreen: (Screen) -> Unit,
    val showBattlePreparation: (String, String, ScenarioJoinBattleLimit, Int) -> Unit,
)

interface RuntimeStartupExtension {
    fun route(request: RuntimeStartupRequest): Boolean
}

internal object RuntimeStartupExtensions {
    fun route(request: RuntimeStartupRequest): Boolean =
        ServiceLoader.load(RuntimeStartupExtension::class.java).any { it.route(request) }
}
