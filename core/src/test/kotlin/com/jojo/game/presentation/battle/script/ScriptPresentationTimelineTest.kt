// Test
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioScriptPresentationRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ScriptPresentationTimelineTest {
    @Test
    fun `item action crosses icon and modal callbacks in source order`() {
        val request = ScenarioScriptPresentationRequest.GetItem(7, 1, true, 1025, 4, "획득")
        val timeline = ScriptPresentationTimeline()
        timeline.startItem(request, now = 2f, actionDuration = .5f, battleUnitId = "u1")

        assertTrue(timeline.advance(2.49f, modalActive = false).effects.isEmpty())
        val icon = timeline.advance(2.5f, modalActive = false)
        assertIs<ScriptPresentationTimeline.Effect.FinishUnitAction>(icon.effects[0])
        assertEquals(ScriptPresentationTimeline.Effect.PlayGetItemSound, icon.effects[1])
        assertEquals(ScriptPresentationTimeline.Phase.ITEM_ICON, timeline.snapshot()?.phase)

        val modal = timeline.advance(3.3f, modalActive = false)
        assertIs<ScriptPresentationTimeline.Effect.PresentItemMessage>(modal.effects.single())
        assertEquals(ScriptPresentationTimeline.Phase.ITEM_MODAL, timeline.snapshot()?.phase)
        assertFalse(timeline.advance(3.4f, modalActive = true).acceptsNewRequest)
        assertTrue(timeline.advance(3.5f, modalActive = false).acceptsNewRequest)
        assertFalse(timeline.isActive())
    }

    @Test
    fun `timed unit highlight dismisses info before resuming script`() {
        val timeline = ScriptPresentationTimeline()
        timeline.startTimed(
            ScenarioScriptPresentationRequest.UnitHighlight(3),
            now = 1f,
            duration = 2f,
            battleUnitId = "u3",
        )

        val completed = timeline.advance(3f, modalActive = false)
        assertEquals(
            listOf(
                ScriptPresentationTimeline.Effect.DismissUnitInfo,
                ScriptPresentationTimeline.Effect.ResumeScript,
            ),
            completed.effects,
        )
        assertFalse(completed.acceptsNewRequest)
        assertFalse(timeline.isActive())
    }
}
