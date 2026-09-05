package com.jojo.game.application.runtime

import com.jojo.game.domain.scenario.PlaybackState

/** External scenario input source; core owns validation and command application. */
fun interface RuntimeScenarioDriver {
    fun commands(frame: RuntimeScenarioFrame): List<RuntimeScenarioCommand>
}

/** Immutable scenario state available to an external driver. */
data class RuntimeScenarioFrame(
    val module: String,
    val elapsedSeconds: Float,
    val playback: PlaybackState,
    val choiceAvailable: Boolean,
)

/** Bounded, input-equivalent playback commands. */
sealed interface RuntimeScenarioCommand {
    data class SetPresentation(
        val mode: RuntimeScenarioPresentation,
        val detail: Int = -1,
    ) : RuntimeScenarioCommand
    data object AdvanceDialogue : RuntimeScenarioCommand
    data object ResumeModal : RuntimeScenarioCommand
    data object SkipDelay : RuntimeScenarioCommand
    data object ConfirmChoice : RuntimeScenarioCommand
    data object RevealDialogue : RuntimeScenarioCommand
}

/** Renderer-neutral visual mode selected by an external runtime. */
enum class RuntimeScenarioPresentation { STANDARD, STREET, PALACE, SECTION }
