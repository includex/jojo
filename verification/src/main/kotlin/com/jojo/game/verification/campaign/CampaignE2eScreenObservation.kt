// Verification
package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.*

/** CampaignE2eScreenObservation: 입력 전후에 사용하는 안정적인 읽기 전용 화면 표식이다. */
internal object CampaignE2eScreenObservation {
    /** of: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
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
