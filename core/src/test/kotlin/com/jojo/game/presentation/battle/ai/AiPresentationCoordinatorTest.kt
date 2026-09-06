// Test
package com.jojo.game.presentation.battle.ai

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.AiTurnResult
import com.jojo.game.domain.battle.AiUnitResolution
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.scenario.PlaybackState
import kotlin.test.Test
import kotlin.test.assertEquals

class AiPresentationCoordinatorTest {
    @Test
    fun `camp entry resolves one actor after focus and callback barrier`() {
        val events = mutableListOf<String>()
        val resolution = AiUnitResolution("enemy", 2, 3, 2, 3, emptyList())
        val port = FakePort(events, resolution)
        val coordinator = AiPresentationCoordinator { port }

        val result = coordinator.beginCamp(Faction.ENEMY)

        assertEquals(AiTurnResult(0, 0, 1), result)
        assertEquals(
            listOf("death", "focus", "resolve", "death", "barriers", "action-message", "empty"),
            events,
        )
        assertEquals(resolution, coordinator.resolution)
        assertEquals(AiPresentationStage.COMPLETE, coordinator.stage)
        assertEquals(Faction.ENEMY, coordinator.activeCamp)
    }

    @Test
    fun `no-result actor commits before script pass and camp completion`() {
        val events = mutableListOf<String>()
        val port = FakePort(events, AiUnitResolution("enemy", 2, 3, 2, 3, emptyList()))
        val coordinator = AiPresentationCoordinator { port }

        coordinator.beginCamp(Faction.ENEMY)
        coordinator.drive()

        assertEquals(listOf("commit", "run-script", "death", "summary", "complete"), events.takeLast(5))
        assertEquals(false, coordinator.hasActiveCamp)
    }

    private class FakePort(
        private val events: MutableList<String>,
        private val resolution: AiUnitResolution,
    ) : AiPresentationCoordinator.Port {
        private var pending = true
        override fun now() = 1f
        override fun resolve(camp: Faction) = AiTurnResult(0, 0, 1).also {
            events += "resolve"
            pending = false
        }
        override fun lastResolution() = resolution
        override fun hasPendingUnits() = pending
        override fun focusFirstCampUnit(camp: Faction) { events += "focus" }
        override fun beginEmptyCampBarrier(hasActor: Boolean) { events += "empty" }
        override fun yieldEmptyCampEntryFrame() = false
        override fun beginActorBarriers(hasPhysicalCounter: Boolean) { events += "barriers" }
        override fun finishDeathCallbacks() { events += "death" }
        override fun focusTile(x: Float, y: Float) = Unit
        override fun startMovement(resolution: AiUnitResolution) = false
        override fun movementActive() = false
        override fun finishMovement(resolution: AiUnitResolution) = Unit
        override fun commitMovement(resolution: AiUnitResolution, updateActionState: Boolean) = Unit
        override fun markPlayerMove(resolution: AiUnitResolution) = Unit
        override fun scriptState() = PlaybackState.COMPLETE
        override fun runScript() = PlaybackState.COMPLETE.also { events += "run-script" }
        override fun battleEndedByScript() = false
        override fun playerMoveScriptFinished() = false
        override fun finishScriptEndedTurn() = Unit
        override fun applyAction(resolution: AiUnitResolution) = Unit
        override fun combatBusy() = false
        override fun yieldCounterattackIdle() = false
        override fun commitAction(actorId: String) { events += "commit" }
        override fun yieldActionStatus(hasAction: Boolean) = false
        override fun yieldPlayerMoveCompletion(isPlayer: Boolean, moved: Boolean) = false
        override fun queuePostActionDeaths() = false
        override fun startedPostActionDeaths() = false
        override fun setSummary(camp: Faction, result: AiTurnResult) { events += "summary" }
        override fun completeCamp(result: AiTurnResult) { events += "complete" }
        override fun setActionMessage(camp: Faction, resolution: AiUnitResolution) { events += "action-message" }
        override fun beginNoResultFrameGate() = Unit
        override fun yieldBeforeNextNoResult(nextIsNoResult: Boolean) = false
        override fun markNoResultCompleted() = Unit
    }
}
