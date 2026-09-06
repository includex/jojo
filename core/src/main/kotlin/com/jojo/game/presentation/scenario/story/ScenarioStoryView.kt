// Scenario
package com.jojo.game.presentation.scenario.story

import com.jojo.game.presentation.scenario.overlay.*

/** ScenarioStreetDialogueView: 시나리오 Street Dialogue 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
internal data class ScenarioStreetDialogueView(
    val hasDialogue: Boolean,
    val portraitId: Int?,
    val speaker: String,
    val visibleText: String,
    val isLeft: Boolean,
    val isAtTop: Boolean,
)

/** ScenarioPalaceFixtureView: 시나리오 Palace Fixture 표시 정보이며, 해당 화면에 표시할 텍스트·아이콘·선택 상태를 불변 값으로 전달한다. */
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
