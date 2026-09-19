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

    @Test
    fun `movement commits final path direction before arrival idle and attack facing`() {
        val actor = battleUnit("enemy", 6, 16).apply { direction = 2 }
        val before = snapshot(actor)
        actor.tileX = 9
        actor.tileY = 17
        actor.direction = 2
        actor.hasMoved = true
        val after = snapshot(actor)
        before.states.values.forEach(BattleUnitMemento::restore)
        val transaction = transaction(actor, before, after)
        val resolution = AiUnitResolution(
            actorId = actor.id,
            fromX = 6,
            fromY = 16,
            toX = 9,
            toY = 17,
            path = listOf(6 to 16, 6 to 17, 7 to 17, 8 to 17, 9 to 17),
            targetId = "target",
            result = TacticalActionResult.Attack(damage = 10, defeated = false),
        )
        val port = FakePort(mutableListOf(), resolution).apply {
            nowSeconds = 0f
            movementStarts = true
            pendingTransaction = transaction
            presentationActor = actor
            attackFacingDirection = 3
        }
        val coordinator = AiPresentationCoordinator { port }

        coordinator.beginCamp(Faction.ENEMY)
        port.nowSeconds = .31f
        coordinator.drive()

        assertEquals(AiPresentationStage.ACTION_DELAY, coordinator.stage)
        assertEquals(1, port.arrivalIdleDirection)
        assertEquals(1, actor.direction)

        port.nowSeconds = .60f
        coordinator.drive()

        assertEquals(AiPresentationStage.ACTION_DELAY, coordinator.stage)
        assertEquals(1, actor.direction)

        port.nowSeconds = .62f
        coordinator.drive()

        assertEquals(AiPresentationStage.ACTION, coordinator.stage)
        assertEquals(3, actor.direction)
    }

    private class FakePort(
        private val events: MutableList<String>,
        private val resolution: AiUnitResolution,
    ) : AiPresentationCoordinator.Port {
        private var pending = true
        var nowSeconds = 1f
        var movementStarts = false
        var pendingTransaction: BattleActionTransaction? = null
        var presentationActor: BattleUnit? = null
        var attackFacingDirection: Int? = null
        var arrivalIdleDirection: Int? = null
        override fun now() = nowSeconds
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
        override fun startMovement(resolution: AiUnitResolution) = movementStarts
        override fun movementActive() = false
        override fun finishMovement(resolution: AiUnitResolution) {
            val finalDirection = BattleUnitMoveTimeline.schedule(resolution.path, fastMove = true)
                .segments
                .last()
                .direction
            presentationActor?.direction = finalDirection
            arrivalIdleDirection = presentationActor?.direction
        }
        override fun commitMovement(resolution: AiUnitResolution, updateActionState: Boolean) {
            pendingTransaction?.commitMovement(commitActionState = updateActionState)
        }
        override fun markPlayerMove(resolution: AiUnitResolution) = Unit
        override fun scriptState() = PlaybackState.COMPLETE
        override fun runScript() = PlaybackState.COMPLETE.also { events += "run-script" }
        override fun battleEndedByScript() = false
        override fun playerMoveScriptFinished() = false
        override fun finishScriptEndedTurn() = Unit
        override fun applyAction(resolution: AiUnitResolution) {
            attackFacingDirection?.let { presentationActor?.direction = it }
        }
        override fun combatBusy() = false
        override fun yieldCounterattackIdle() = false
        override fun commitAction(actorId: String) { events += "commit" }
        override fun yieldActionStatus(hasAction: Boolean) = false
        override fun yieldPlayerMoveCompletion(isPlayer: Boolean, moved: Boolean) = false
        override fun queuePostActionDeaths() = false
        override fun presentActionSettlement() = false
        override fun settlementActive() = false
        override fun startedPostActionDeaths() = false
        override fun setSummary(camp: Faction, result: AiTurnResult) { events += "summary" }
        override fun completeCamp(result: AiTurnResult) { events += "complete" }
        override fun setActionMessage(camp: Faction, resolution: AiUnitResolution) { events += "action-message" }
        override fun beginNoResultFrameGate() = Unit
        override fun yieldBeforeNextNoResult(nextIsNoResult: Boolean) = false
        override fun markNoResultCompleted() = Unit
    }

    private fun transaction(
        actor: BattleUnit,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
    ): BattleActionTransaction = BattleActionTransaction(
        actorId = actor.id,
        before = before,
        after = after,
        hitSideEffects = emptyList(),
        completionSideEffects = emptyList(),
        restoreSnapshot = { state -> state.states.values.forEach(BattleUnitMemento::restore) },
        adjustEconomy = { _, _ -> },
        presentationUnit = { id -> before.states[id]?.unit },
        activeUnit = { id -> before.states[id]?.unit },
        onCompleted = {},
    )

    private fun snapshot(unit: BattleUnit): BattleActionSnapshot = BattleActionSnapshot(
        topology = Battlefield.TopologySnapshot(listOf(unit.id), emptyList()),
        states = mapOf(unit.id to BattleUnitMemento.capture(unit)),
        playerMoney = 0,
        enemyMoney = 0,
        skillTemps = emptyMap(),
        moveLength = 0,
        lastMovePaths = emptyMap(),
        traceActions = emptyList(),
    )

    private fun battleUnit(id: String, x: Int, y: Int): BattleUnit = BattleUnit(
        id = id,
        name = id,
        faction = Faction.ENEMY,
        tileX = x,
        tileY = y,
        hitPoints = 100,
        maxHitPoints = 100,
        magicPoints = 40,
        maxMagicPoints = 40,
    )
}
