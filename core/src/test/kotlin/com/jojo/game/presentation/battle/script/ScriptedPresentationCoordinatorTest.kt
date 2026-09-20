package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.scenario.ScenarioScriptPresentationRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class ScriptedPresentationCoordinatorTest {
    @Test
    fun `rectangle status settlement defaults all resolved targets before resuming`() {
        val calls = mutableListOf<String>()
        var now = 1f
        var request: ScenarioScriptPresentationRequest? = ScenarioScriptPresentationRequest.UnitStatusSettlement(
            listOf(mapOf("camp" to 0, "x1" to 0, "y1" to 0, "x2" to 20, "y2" to 20, "status" to 9)),
        )
        val port = object : ScriptedPresentationCoordinator.Port {
            override fun now() = now
            override fun modalActive() = false
            override fun consumeRequest() = request.also { request = null }
            override fun clearVisual(unitId: String) { calls += "clear:$unitId" }
            override fun defaultAction(unitId: String) { calls += "default:$unitId" }
            override fun playGetItemSound() = Unit
            override fun presentItemMessage(message: String) = Unit
            override fun dismissUnitInfo() = Unit
            override fun resumeScript() { calls += "resume" }
            override fun focusRectangle(x1: Int, y1: Int, x2: Int, y2: Int) = Unit
            override fun unitTarget(unitId: Int) = null
            override fun focusUnit(unitId: String) { calls += "focus:$unitId" }
            override fun openUnitInfo(unitId: Int) = Unit
            override fun itemTarget(selector: Int) = null
            override fun setVisual(unitId: String, action: Int, startedAt: Float) = Unit
            override fun sourceActionDuration(action: Int, direction: Int) = 0f
            override fun focusMapObjects(request: ScenarioScriptPresentationRequest.MapObjects) = Unit
            override fun statusTarget(values: List<Map<String, Any?>>) = null
            override fun statusTargets(values: List<Map<String, Any?>>) = listOf(
                ScriptedPresentationCoordinator.Target("146", 2),
                ScriptedPresentationCoordinator.Target("484", 1),
            )
        }
        val coordinator = ScriptedPresentationCoordinator(ScriptPresentationTimeline(), port)

        coordinator.drive()
        assertEquals(emptyList(), calls)
        now = 1.6f
        coordinator.drive()

        assertEquals(
            listOf("clear:146", "default:146", "clear:484", "default:484", "resume"),
            calls,
        )
    }
}
