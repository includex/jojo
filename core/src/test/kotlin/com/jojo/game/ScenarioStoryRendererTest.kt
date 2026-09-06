// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.story.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioStoryRendererTest {
    @Test
    fun streetStagesKeepTheSourceLayerOrder() {
        assertEquals(0, ScenarioStreetDialogueStages.indexOf("panel"))
        assertEquals(1, ScenarioStreetDialogueStages.indexOf("portrait"))
        assertEquals(2, ScenarioStreetDialogueStages.indexOf("speaker"))
        assertEquals(3, ScenarioStreetDialogueStages.indexOf("text"))
        assertEquals(4, ScenarioStreetDialogueStages.backgroundIndex())
        assertEquals(5, ScenarioStreetDialogueStages.charactersIndex())
    }

    @Test
    fun streetViewIsACompleteImmutableRenderInput() {
        val view = ScenarioStreetDialogueView(true, 9, "조조", "대화", isLeft = true, isAtTop = false)

        assertEquals(true, view.hasDialogue)
        assertEquals(9, view.portraitId)
        assertEquals("조조", view.speaker)
        assertEquals("대화", view.visibleText)
    }
}
