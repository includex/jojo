package com.jojo.game.verification

import com.jojo.game.application.runtime.RuntimeScenarioCommand
import com.jojo.game.application.runtime.RuntimeScenarioDriver
import com.jojo.game.application.runtime.RuntimeScenarioFrame
import com.jojo.game.application.runtime.RuntimeScenarioPresentation
import com.jojo.game.application.runtime.RuntimeScenarioOverlay
import com.jojo.game.application.runtime.RuntimeScenarioCommand.Present
import com.jojo.game.application.runtime.RuntimeScenarioCommand.ShowOverlay
import com.jojo.game.verification.scenario.ScenarioFixtureInstaller
import com.jojo.game.domain.scenario.PlaybackState

/** Verification-owned playback settling policy. Route spelling remains outside core. */
class VerificationScenarioDriver(private val state: String?) : RuntimeScenarioDriver {
    private var presentationSent = false

    override fun commands(frame: RuntimeScenarioFrame): List<RuntimeScenarioCommand> {
        presentationCommand()?.let { command ->
            if (!presentationSent) {
                presentationSent = true
                return listOf(command)
            }
        }
        if (state !in settlingStates) return emptyList()
        return when (frame.playback) {
            PlaybackState.DIALOGUE -> listOf(RuntimeScenarioCommand.AdvanceDialogue)
            PlaybackState.MODAL -> listOf(RuntimeScenarioCommand.ResumeModal)
            PlaybackState.DELAY -> listOf(RuntimeScenarioCommand.SkipDelay)
            PlaybackState.CHOICE -> listOf(RuntimeScenarioCommand.ConfirmChoice)
            PlaybackState.COMPLETE -> emptyList()
        }
    }

    private companion object {
        val settlingStates = setOf("scenario-dialogue", "map-info")
        val streetStages = listOf("panel", "portrait", "speaker", "text", "background", "characters")
        val overlayNames = RuntimeScenarioOverlay.values().associateBy { it.name.lowercase().replace('_', '-') }
    }

    private fun presentationCommand(): RuntimeScenarioCommand? = when (state) {
        "hall-palace-fixture" -> Present(RuntimeScenarioPresentation.PALACE, scene = ScenarioFixtureInstaller.palaceScene())
        "hall-section-fixture" -> Present(RuntimeScenarioPresentation.SECTION, scene = ScenarioFixtureInstaller.sectionScene())
        else -> state?.removePrefix("street-")?.let(streetStages::indexOf)
            ?.takeIf { it >= 0 }
            ?.let { Present(RuntimeScenarioPresentation.STREET, it) }
            ?: overlayCommand()
    }

    private fun overlayCommand(): RuntimeScenarioCommand? = state
        ?.removePrefix("hall-")
        ?.removeSuffix("-fixture")
        ?.let { name -> overlayNames[name]?.let { ShowOverlay(it, ScenarioFixtureInstaller.scene(it).copy(modal = ScenarioFixtureInstaller.modal(it))) } }
        ?: state?.let { name -> overlayNames[name]?.let { ShowOverlay(it, ScenarioFixtureInstaller.scene(it).copy(modal = ScenarioFixtureInstaller.modal(it))) } }

}
