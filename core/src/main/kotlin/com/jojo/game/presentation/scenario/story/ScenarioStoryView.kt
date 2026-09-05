package com.jojo.game.presentation.scenario.story

/** Immutable input for the source DialogueLayer street composition. */
internal data class ScenarioStreetDialogueView(
    val hasDialogue: Boolean,
    val portraitId: Int?,
    val speaker: String,
    val visibleText: String,
    val isLeft: Boolean,
    val isAtTop: Boolean,
)

/** Immutable upper-dialogue projection for the source Palace fixture. */
internal data class ScenarioPalaceFixtureView(
    val dialogueText: String,
    val portraitId: Int,
    val speaker: String,
)

internal object ScenarioStreetDialogueStages {
    private val order = listOf("panel", "portrait", "speaker", "text", "background", "characters")

    fun indexOf(stage: String): Int = order.indexOf(stage)

    fun nameAt(index: Int): String? = order.getOrNull(index)

    fun backgroundIndex(): Int = order.indexOf("background")

    fun charactersIndex(): Int = order.indexOf("characters")
}
