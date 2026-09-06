// Test
package com.jojo.game.presentation.battle.script

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.scenario.Dialogue
import com.jojo.game.domain.scenario.ScenarioUnitHideRequest
import com.jojo.game.domain.scenario.ScenarioUnitShowRequest
import com.jojo.game.domain.scenario.ScenarioUnitPostsRequest
import com.jojo.game.domain.scenario.ScriptedUnitAction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ScriptedUnitCoordinatorTest {
    @Test
    fun `hide clears lifecycle before synchronous resume callback`() {
        val lifecycle = ScriptedUnitPresentationLifecycle()
        val unit = BattleUnit("u", "unit", Faction.PLAYER, 1, 1)
        var now = 0f
        val events = mutableListOf<String>()
        val port = object : ScriptedUnitCallbackCoordinator.Port {
            override fun now() = now
            override fun consumeHide() = ScenarioUnitHideRequest(1, hideType = 0, battleUnitId = "u").takeIf { events.none { e -> e == "consumed" } }
                ?.also { events += "consumed" }
            override fun consumeShow(): ScenarioUnitShowRequest? = null
            override fun consumePosts(): ScenarioUnitPostsRequest? = null
            override fun dialogueIsActive() = false
            override fun presentDialogue(dialogue: Dialogue) = Unit
            override fun hideUnit(request: ScenarioUnitHideRequest) = unit
            override fun showUnit(request: ScenarioUnitShowRequest) = null
            override fun postsUnit(request: ScenarioUnitPostsRequest) = null
            override fun isMineMaster(unitId: String) = false
            override fun focus(unit: BattleUnit) = Unit
            override fun sourceActionDuration(action: Int, direction: Int) = 1f
            override fun beginHideModel(unit: BattleUnit, request: ScenarioUnitHideRequest, originalHp: Int) { events += "begin" }
            override fun registerHideAnimation(unit: BattleUnit, sourceAction: Int, startedAt: Float, endsAt: Float) { events += "start" }
            override fun removeHideAnimation(unitId: String) { events += "remove" }
            override fun completeHideModel(unit: BattleUnit, request: ScenarioUnitHideRequest, originalHp: Int) { events += "model" }
            override fun completeUnitHide(request: ScenarioUnitHideRequest) { events += "stage" }
            override fun prepareShow(unit: BattleUnit, request: ScenarioUnitShowRequest) =
                ScriptedUnitCallbackCoordinator.ShowStart(unit.id, 0f)
            override fun finishShow(unitId: String, request: ScenarioUnitShowRequest) = Unit
            override fun setVisibleWhenShowUnitMissing(unitId: Int) = Unit
            override fun setOldAvatar(unitId: String, avatarId: Int) = Unit
            override fun publishLoadedAvatar(unitId: String, avatarId: Int) = Unit
            override fun resumeScript() {
                events += "resume"
                assertNull(lifecycle.activeHide)
            }
        }
        val coordinator = ScriptedUnitCallbackCoordinator(lifecycle, port)

        coordinator.driveHide()
        now = 1f
        coordinator.driveHide()

        assertEquals(listOf("consumed", "begin", "start", "remove", "stage", "model", "resume"), events)
    }

    @Test
    fun `action callback restores pose before synchronous resume`() {
        val lifecycle = ScriptedUnitPresentationLifecycle()
        val action = ScriptedUnitAction(1, action = 46)
        val unit = BattleUnit("u", "unit", Faction.PLAYER, 1, 1)
        var now = 0f
        val events = mutableListOf<String>()
        val port = object : ScriptedUnitActionCoordinator.Port {
            override fun now() = now
            override fun consumeActions() = listOf(action).takeIf { events.isEmpty() }.orEmpty()
            override fun unit(action: ScriptedUnitAction) = unit
            override fun applyDirection(unit: BattleUnit, direction: Int) = Unit
            override fun clearVisual(unitId: String) { events += "clear" }
            override fun setVisual(unitId: String, action: Int, startedAt: Float) { events += "set" }
            override fun startSourceAction(unit: BattleUnit, action: Int) { events += "source" }
            override fun actionDuration(action: Int, direction: Int) = 1f
            override fun focus(unit: BattleUnit) { events += "focus" }
            override fun clearSourceAction(unitId: String) { events += "source-clear" }
            override fun defaultAction(unitId: String) { events += "default" }
            override fun resumeScript() {
                events += "resume"
                assertNull(lifecycle.activeAction)
            }
        }
        val coordinator = ScriptedUnitActionCoordinator(lifecycle, port)

        coordinator.consumeStarts()
        now = 1f
        coordinator.driveCallback()

        assertEquals(listOf("set", "focus", "source-clear", "default", "resume"), events)
    }
}
