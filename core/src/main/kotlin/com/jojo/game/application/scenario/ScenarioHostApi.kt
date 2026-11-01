package com.jojo.game.application.scenario

import com.jojo.game.domain.scenario.Choice
import com.jojo.game.infrastructure.data.ScenarioDialogueTextCatalog

/** Version 1 of the game commands exposed to a scenario language adapter. */
object ScenarioHostApiVersion {
    const val CURRENT = 1
}

/** Source identity is supplied by the script adapter, without exposing its syntax tree. */
data class ScenarioCallSite(val module: String, val function: String, val line: Int) {
    init {
        require(module.isNotBlank() && function.isNotBlank() && line > 0)
    }
}

/** Commands may suspend playback. The caller resumes when the game reports input or time completion. */
interface ScenarioHostApi {
    /** A literal string or `@dialogue:<stable-id>`; unknown references fail immediately. */
    fun say(text: String)

    /** Source stage ticks, each one tenth of a second, with the existing first-update timing. */
    fun delay(ticks: Int)

    /** Displays a choice. Selection is zero based in traces and one based in script results. */
    fun choice(options: List<String>, portraitId: Int?, site: ScenarioCallSite)

    /** One based result of the most recently confirmed choice, or null while pending. */
    val choiceResult: Int?
}

/** The game implementation shared by AST and future language adapters. */
internal class ScenarioHostCommands(
    private val dialogue: ScenarioDialogueCoordinator,
    private val choices: ScenarioChoiceCoordinator,
    private val delayAction: (Int) -> Unit,
    private val infoAction: (String) -> Unit,
) : ScenarioHostApi {
    override val choiceResult: Int? get() = choices.lastChoiceResult
    override fun say(text: String) {
        val sourceText = if (text.startsWith(ScenarioDialogueTextCatalog.REFERENCE_PREFIX)) {
            ScenarioDialogueTextCatalog.text(text.removePrefix(ScenarioDialogueTextCatalog.REFERENCE_PREFIX))
        } else text
        if (!sourceText.startsWith("&")) {
            dialogue.reset()
            infoAction(sourceText)
        } else {
            dialogue.startSay(sourceText)
        }
    }

    override fun delay(ticks: Int) = delayAction(ticks)

    override fun choice(options: List<String>, portraitId: Int?, site: ScenarioCallSite) {
        require(options.isNotEmpty()) { "Scenario choice must have at least one option." }
        choices.startChoice(Choice(options, portraitId), site)
    }
}
