package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.*

/** Stable, read-only screen markers used before and after driver input. */
internal object CampaignE2eScreenObservation {
    fun of(screen: RuntimeScreenProbe): String = when (screen) {
        is ScenarioRuntimeProbe -> screen.let {
            "screen=ScenarioScreen;module=${it.module};scene=${it.sceneIndex};playback=${it.playback};" +
                    "menuVisible=${it.menuVisible};choice=${it.selectedChoice};hallBattleScenePending=${it.hallBattleScenePending}"
        }

        is BattlePreparationRuntimeProbe -> screen.let {
            "screen=BattlePreparationScreen;return=${it.returnScenario};source=${it.sourceScenario};" +
                    "selected=${it.selectedCount};canStart=${it.canStart};cursorSelected=${it.cursorSelected}"
        }

        is TitleRuntimeProbe -> "screen=TitleScreen"
        is OtherRuntimeProbe -> "screen=${screen.screenName}"
        is BattleRuntimeScreenProbe -> "screen=BattleScreen;scenario=${screen.scenario};playback=${screen.playback};" +
                "phase=${screen.turnPhase};round=${screen.round};battleMenuOpen=${screen.battleMenuOpen};" +
                "battleCommandOpen=${screen.battleCommandOpen};targetSelectionOpen=${screen.battleTargetSelectionOpen};" +
                "magickListOpen=${screen.magickListOpen};magicTargetSelection=${screen.magicTargetSelection};" +
                "autoBattleOverlay=${screen.autoBattleOverlay};autoBattleChecked=${screen.autoBattleChecked};" +
                "collocation=${screen.collocation};rewardOpen=${screen.rewardOpen};savePromptOpen=${screen.savePromptOpen};" +
                "losePromptOpen=${screen.losePromptOpen};outcome=${screen.outcome}"
    }
}
