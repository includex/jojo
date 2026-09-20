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
    fun `a later block clears the held pose at hit but cannot clear a newer action`() {
        for (replace in listOf(false, true)) {
            val lifecycle = ScriptedUnitPresentationLifecycle()
            val callbacks = mutableListOf<() -> Unit>()
            lifecycle.setVisual("u", ScriptedUnitVisual(32, 0f))
            lifecycle.scheduleVisualClear("u", 1f, { _, callback -> callbacks += callback }, { true })
            assertEquals(32, lifecycle.visual("u")?.action)
            if (replace) lifecycle.setVisual("u", ScriptedUnitVisual(4, .5f))
            callbacks.single().invoke()
            assertEquals(if (replace) 4 else null, lifecycle.visual("u")?.action)
        }
    }

    @Test
    fun `scheduled hit pose starts at hit and persists until an explicit action`() {
        val lifecycle = ScriptedUnitPresentationLifecycle()
        val callbacks = mutableListOf<() -> Unit>()
        lifecycle.setVisual("u", ScriptedUnitVisual(9, 0f))
        lifecycle.scheduleVisual("u", ScriptedUnitVisual(32, 1f), { at, callback ->
            assertEquals(1f, at)
            callbacks += callback
        }, { true })
        assertEquals(9, lifecycle.visual("u")?.action, "do not show the hit pose early")
        callbacks.single().invoke()
        assertEquals(ScriptedUnitVisual(32, 1f), lifecycle.visual("u"))
        assertTrue(!lifecycle.actionBusy, "a held pose must not keep script callbacks busy")
        lifecycle.setVisual("u", ScriptedUnitVisual(4, 2f))
        assertEquals(4, lifecycle.visual("u")?.action)
        lifecycle.clearVisual("u")
        assertNull(lifecycle.visual("u"))
    }

    @Test
    fun `new visual or explicit clear cancels a delayed hit pose`() {
        for (clear in listOf(false, true)) {
            val lifecycle = ScriptedUnitPresentationLifecycle()
            val callbacks = mutableListOf<() -> Unit>()
            lifecycle.scheduleVisual("u", ScriptedUnitVisual(32, 1f), { _, callback -> callbacks += callback }, { true })
            if (clear) lifecycle.clearVisual("u") else lifecycle.setVisual("u", ScriptedUnitVisual(4, .5f))
            callbacks.single().invoke()
            assertEquals(if (clear) null else 4, lifecycle.visual("u")?.action)
        }
    }

    @Test
    fun `superseded reaction cannot install its delayed pose`() {
        val lifecycle = ScriptedUnitPresentationLifecycle()
        val callbacks = mutableListOf<() -> Unit>()
        lifecycle.setVisual("u", ScriptedUnitVisual(9, 0f))
        lifecycle.scheduleVisual("u", ScriptedUnitVisual(32, 1f), { _, callback -> callbacks += callback }, { false })
        callbacks.single().invoke()
        assertEquals(9, lifecycle.visual("u")?.action)
    }

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
    @Test
    fun `semantic default action clears a held script pose and invalidates delayed reactions`() {
        val lifecycle = ScriptedUnitPresentationLifecycle()
        val callbacks = mutableListOf<() -> Unit>()
        lifecycle.setVisual("u", ScriptedUnitVisual(8, 0f))
        lifecycle.scheduleVisual("u", ScriptedUnitVisual(32, 1f), { _, callback -> callbacks += callback }, { true })
        var visualSeenByDefault: ScriptedUnitVisual? = ScriptedUnitVisual(-1, -1f)

        lifecycle.applyDefaultAction("u") { visualSeenByDefault = lifecycle.visual("u") }
        callbacks.single().invoke()

        assertNull(visualSeenByDefault)
        assertNull(lifecycle.visual("u"))
    }

}
