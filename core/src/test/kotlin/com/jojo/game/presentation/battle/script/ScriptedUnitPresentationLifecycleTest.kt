// Test
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioMapPresentationRequest
import com.jojo.game.domain.scenario.ScenarioUnitHideRequest
import com.jojo.game.domain.scenario.ScenarioUnitShowRequest
import com.jojo.game.domain.scenario.ScriptedUnitAction
import com.jojo.game.presentation.battle.unit.ScriptedUnitVisual
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScriptedUnitPresentationLifecycleTest {
    @Test
    fun `hide dialogue and animation callbacks are owned by lifecycle`() {
        val lifecycle = ScriptedUnitPresentationLifecycle()
        val request = ScenarioUnitHideRequest(7, hideType = 1)

        lifecycle.awaitHideDialogue(request, "u7")
        assertTrue(lifecycle.hideBusy)
        assertEquals("u7", lifecycle.takeHideDialogue()?.battleUnitId)
        assertNull(lifecycle.awaitingHideDialogue)

        lifecycle.startHide(request, "u7", endsAt = 2f, originalHp = 80)
        assertEquals(2f, lifecycle.activeHide?.endsAt)
        lifecycle.finishHide()
        assertNull(lifecycle.activeHide)
    }

    @Test
    fun `visual commands and map show action state remain independent`() {
        val lifecycle = ScriptedUnitPresentationLifecycle()
        lifecycle.setVisual("u1", ScriptedUnitVisual(action = 46, startedAt = 1f))
        assertEquals(46, lifecycle.visual("u1")?.action)
        lifecycle.clearVisual("u1")
        assertNull(lifecycle.visual("u1"))

        lifecycle.startMap(ScenarioMapPresentationRequest(2, 3, 1f), endsAt = 4f)
        lifecycle.startShow(ScenarioUnitShowRequest(7), "u7", endsAt = .2f)
        lifecycle.startAction(ScriptedUnitAction(7, action = 46), "u7", endsAt = 1f)
        assertEquals(4f, lifecycle.activeMap?.endsAt)
        assertEquals(.2f, lifecycle.activeShow?.endsAt)
        assertTrue(lifecycle.actionBusy)
        lifecycle.finishMap()
        lifecycle.finishShow()
        lifecycle.finishAction()
        assertNull(lifecycle.activeMap)
        assertNull(lifecycle.activeShow)
        assertNull(lifecycle.activeAction)
    }
}
