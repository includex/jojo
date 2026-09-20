package com.jojo.game

import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.BattleActionTransaction
import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.BattleUnitMemento
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.Faction
import com.jojo.game.presentation.battle.ManualBattleActionCommitPolicy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ManualBattleActionCommitPolicyTest {
    @Test
    fun `critical speech close does not commit final action state while attack presentation is active`() {
        val actor = BattleUnit("actor", "actor", Faction.PLAYER, 0, 0, experience = 0)
        val target = BattleUnit(
            "target", "target", Faction.ENEMY, 1, 0,
            hitPoints = 19, maxHitPoints = 19,
        )
        val before = snapshot(actor, target)
        actor.experience = 30
        actor.hasActed = true
        target.hitPoints = 0
        val after = snapshot(actor, target)
        before.states.values.forEach(BattleUnitMemento::restore)
        actor.experience = 0
        val transaction = BattleActionTransaction(
            actorId = actor.id,
            before = before,
            after = after,
            hitSideEffects = emptyList(),
            completionSideEffects = listOf({ actor.experience = 30 }),
            restoreSnapshot = { state -> state.states.values.forEach(BattleUnitMemento::restore) },
            adjustEconomy = { _, _ -> },
            presentationUnit = { id -> before.states[id]?.unit },
            activeUnit = { id -> before.states[id]?.unit },
            onCompleted = {},
        )

        val commitDuringAction = ManualBattleActionCommitPolicy.shouldCommit(
            pendingScriptPasses = 1,
            actionCommitted = false,
            scriptComplete = true,
            combatPresentationBusy = true,
        )
        if (commitDuringAction) transaction.commitAll()

        assertFalse(commitDuringAction)
        assertEquals(19, target.hitPoints)
        assertEquals(0, actor.experience)
        assertFalse(actor.hasActed)

        transaction.commitVitals(target.id, hp = 0)
        assertEquals(0, target.hitPoints)
        assertEquals(0, actor.experience)
        assertFalse(actor.hasActed)

        val commitAfterAction = ManualBattleActionCommitPolicy.shouldCommit(
            pendingScriptPasses = 1,
            actionCommitted = false,
            scriptComplete = true,
            combatPresentationBusy = false,
        )
        if (commitAfterAction) transaction.commitAll()

        assertTrue(commitAfterAction)
        assertEquals(0, target.hitPoints)
        assertEquals(30, actor.experience)
        assertTrue(actor.hasActed)
    }

    @Test
    fun `commit requires pending script completion and remains exactly once`() {
        assertFalse(
            ManualBattleActionCommitPolicy.shouldCommit(
                pendingScriptPasses = 0,
                actionCommitted = false,
                scriptComplete = true,
                combatPresentationBusy = false,
            )
        )
        assertFalse(
            ManualBattleActionCommitPolicy.shouldCommit(
                pendingScriptPasses = 1,
                actionCommitted = false,
                scriptComplete = false,
                combatPresentationBusy = false,
            )
        )
        assertFalse(
            ManualBattleActionCommitPolicy.shouldCommit(
                pendingScriptPasses = 1,
                actionCommitted = true,
                scriptComplete = true,
                combatPresentationBusy = false,
            )
        )
    }

    private fun snapshot(vararg units: BattleUnit): BattleActionSnapshot = BattleActionSnapshot(
        topology = Battlefield.TopologySnapshot(units.map(BattleUnit::id), emptyList()),
        states = units.associate { it.id to BattleUnitMemento.capture(it) },
        playerMoney = 0,
        enemyMoney = 0,
        skillTemps = emptyMap(),
        moveLength = 0,
        lastMovePaths = emptyMap(),
        traceActions = emptyList(),
    )
}
