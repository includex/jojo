package com.jojo.game

import com.jojo.game.presentation.scenario.ScenarioScreen
import com.jojo.game.presentation.title.TitleScreen
import com.jojo.game.presentation.battle.preparation.BattlePreparationScreen

import com.badlogic.gdx.Screen

/** Stable, read-only screen markers used before and after driver input. */
internal object CampaignE2eScreenObservation {
    fun of(screen: Screen?): String = when (screen) {
        is ScenarioScreen -> screen.campaignE2eState().let {
            "screen=ScenarioScreen;module=${it.module};scene=${it.sceneIndex};playback=${it.playback};" +
                    "menuVisible=${it.menuVisible};choice=${it.selectedChoice};hallBattleScenePending=${it.hallBattleScenePending}"
        }

        is BattlePreparationScreen -> screen.campaignE2eState().let {
            "screen=BattlePreparationScreen;return=${it.returnScenario};source=${it.sourceScenario};" +
                    "selected=${it.selectedCount};canStart=${it.canStart};cursorSelected=${it.cursorSelected}"
        }

        is TitleScreen -> "screen=TitleScreen"
        null -> "screen=null"
        else -> "screen=${screen.javaClass.simpleName}"
    }
}
