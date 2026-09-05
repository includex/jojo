package com.jojo.game

/** Immutable input for the source DialogueLayer street composition. */
internal data class ScenarioStreetDialogueView(
    val hasDialogue: Boolean,
    val portraitId: Int?,
    val speaker: String,
    val visibleText: String,
    val isLeft: Boolean,
    val isAtTop: Boolean,
)

internal object ScenarioStreetDialogueStages {
    private val order = listOf("panel", "portrait", "speaker", "text", "background", "characters")

    fun indexOf(stage: String): Int = order.indexOf(stage)

    fun backgroundIndex(): Int = order.indexOf("background")

    fun charactersIndex(): Int = order.indexOf("characters")
}
