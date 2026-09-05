package com.jojo.game.application.runtime

import com.jojo.game.BattleOutcome
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.scenario.PlaybackState
import com.jojo.game.application.scenario.ScenarioChoiceTrace
import com.jojo.game.application.scenario.ScenarioRandomTrace
import com.jojo.game.presentation.title.TitleViewState

/**
 * Read-only application boundary for external diagnostics that need to drive
 * the already-installed production input processor.  It deliberately exposes
 * state, not screens, so production never depends on a diagnostics module.
 */
fun interface RuntimeScreenObserver {
    fun update(delta: Float, screen: RuntimeScreenProbe)

    fun scenarioStarted(module: String, index: Int) = Unit

    /** External driver explicitly requests that ScenarioScreen not auto-route. */
    val keepsScenarioOpen: Boolean get() = false
}

sealed interface RuntimeScreenProbe {
    val screenName: String
}

/** Immutable title presentation state for external runtime observers. */
data class TitleRuntimeProbe(
    val view: TitleViewState,
) : RuntimeScreenProbe {
    override val screenName: String = "TitleScreen"
}

data class ScenarioRuntimeProbe(
    val module: String,
    val playback: PlaybackState,
    val options: List<String>,
    val selectedChoice: Int,
    val sceneIndex: Int,
    val startedScenes: List<Int>,
    val backgroundId: Int,
    val unitIds: Set<Int>,
    val campaignStage: Int,
    val menuVisible: Boolean,
    val dialogueText: String?,
    val hallBattleScenePending: Boolean,
    val battleButtonScreenX: Int,
    val battleButtonScreenY: Int,
    /** Immutable diagnostic evidence; external observers own serialization. */
    val choiceTrace: List<ScenarioChoiceTrace> = emptyList(),
    /** Immutable diagnostic evidence; external observers own serialization. */
    val randomTrace: List<ScenarioRandomTrace> = emptyList(),
    val randomDrawCount: Int = 0,
    val remainingInjectedRandomCount: Int = 0,
) : RuntimeScreenProbe {
    override val screenName: String = "ScenarioScreen"
}

data class BattlePreparationRuntimeProbe(
    val returnScenario: String,
    val sourceScenario: String,
    val campaignStage: Int,
    val selectedCount: Int,
    val minimum: Int,
    val maximum: Int,
    val cursorSelected: Boolean,
    val canStart: Boolean,
) : RuntimeScreenProbe {
    override val screenName: String = "BattlePreparationScreen"
}

data class BattleRuntimeScreenProbe(
    val scenario: String,
    val playback: PlaybackState,
    val outcome: BattleOutcome?,
    val bootstrapComplete: Boolean,
    val initialScene1Started: Boolean,
    val resultScene1Started: Boolean,
    val scene2Started: Boolean,
    val rewardOpen: Boolean,
    val winConditionsOpen: Boolean,
    val savePromptOpen: Boolean,
    val losePromptOpen: Boolean,
    val loseTitleScreenX: Int,
    val loseTitleScreenY: Int,
    val playerMoveCommitted: Boolean,
    val campaignStage: Int,
    val turnPhase: String,
    val battleMenuOpen: Boolean,
    val battleCommandOpen: Boolean,
    val battleTargetSelectionOpen: Boolean,
    val magickListOpen: Boolean,
    val magicTargetSelection: Boolean,
    val commandWaitScreenX: Int,
    val commandWaitScreenY: Int,
    val menuEndRoundScreenX: Int,
    val menuEndRoundScreenY: Int,
    val battleMenuButtonScreenX: Int,
    val battleMenuButtonScreenY: Int,
    val autoBattleToggleScreenX: Int,
    val autoBattleToggleScreenY: Int,
    val autoBattleConfirmScreenX: Int,
    val autoBattleConfirmScreenY: Int,
    val autoBattleOverlay: String,
    val autoBattleChecked: Boolean,
    val collocation: Boolean,
    val committedPlayerMove: String?,
    val selectedChoice: Int,
    val selectedUnitId: String?,
    val battle: BattleRuntimeProbe,
) : RuntimeScreenProbe {
    override val screenName: String = "BattleScreen"
    val round: Int get() = battle.snapshot.round
    val activeFaction: Faction get() = battle.snapshot.activeFaction
}

data class OtherRuntimeProbe(override val screenName: String) : RuntimeScreenProbe
