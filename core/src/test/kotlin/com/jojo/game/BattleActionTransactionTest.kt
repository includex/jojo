package com.jojo.game
import com.jojo.game.domain.battle.BattleUnitMemento
import com.jojo.game.domain.battle.Battlefield
import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * class  `BattleActionTransactionTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleActionTransactionTest {
    @Test
    fun `partial commits publish movement position vitals economy and statuses`() {
        val actor = unit("actor", 0, 0).apply {
            hasAuthoredTileX = false
            hasAuthoredTileY = false
        }
        val target = unit("target", 1, 0)
        val before = snapshot(actor, target, playerMoney = 10, enemyMoney = 20)
        actor.tileX = 3
        actor.tileY = 4
        actor.direction = 1
        actor.hasMoved = true
        actor.hasActed = true
        actor.actionStatusRound = 1
        target.statuses[BattleStatus.PARALYSIS] = 2
        target.attributeLifts[BattleAttribute.DEFENSE] = -1
        target.attributeLiftRounds[BattleAttribute.DEFENSE] = 3
        val after = snapshot(actor, target, playerMoney = 13, enemyMoney = 16)
        before.states.values.forEach(BattleUnitMemento::restore)
        actor.hasAuthoredTileX = false
        actor.hasAuthoredTileY = false
        var playerMoney = before.playerMoney
        var enemyMoney = before.enemyMoney
        val transaction = transaction(
            actorId = actor.id,
            before = before,
            after = after,
            adjustEconomy = { player, enemy -> playerMoney += player; enemyMoney += enemy },
        )

        assertEquals(100, transaction.initialHp(actor.id))
        assertEquals(40, transaction.initialMp(actor.id))
        transaction.commitMovement(commitActionState = true)
        transaction.commitPosition(target.id, 7, 8)
        transaction.commitVitals(target.id, hp = 65, mp = 12)
        transaction.commitEconomy(playerDelta = 3, enemyDelta = -4)
        transaction.commitStatuses(target.id)

        assertEquals(3 to 4, actor.tileX to actor.tileY)
        assertEquals(1, actor.direction)
        assertTrue(actor.hasMoved)
        assertTrue(actor.hasActed)
        assertEquals(1, actor.actionStatusRound)
        assertTrue(actor.hasAuthoredTileX)
        assertTrue(actor.hasAuthoredTileY)
        assertEquals(7 to 8, target.tileX to target.tileY)
        assertTrue(target.hasAuthoredTileX)
        assertTrue(target.hasAuthoredTileY)
        assertEquals(65, target.hitPoints)
        assertEquals(12, target.magicPoints)
        assertEquals(13, playerMoney)
        assertEquals(16, enemyMoney)
        assertEquals(mapOf(BattleStatus.PARALYSIS to 2), target.statuses)
        assertEquals(mapOf(BattleAttribute.DEFENSE to -1), target.attributeLifts)
        assertEquals(mapOf(BattleAttribute.DEFENSE to 3), target.attributeLiftRounds)
        assertEquals(listOf(0, 0), target.presentation.stateAnimation.current()?.textureIndices)

        transaction.commitStatuses(MagicLocalSettlementEntry(
            targetId = target.id,
            statusesBefore = target.statuses.toMap(),
            statusesAfter = mapOf(BattleStatus.POISON to 1),
            attributeLiftsBefore = target.attributeLifts.toMap(),
            attributeLiftsAfter = mapOf(BattleAttribute.ATTACK to 1),
            hasStatesPayload = true,
            attributeLiftRoundsBefore = target.attributeLiftRounds.toMap(),
            attributeLiftRoundsAfter = mapOf(BattleAttribute.ATTACK to 2),
        ))
        assertEquals(mapOf(BattleStatus.POISON to 1), target.statuses)
        assertEquals(mapOf(BattleAttribute.ATTACK to 1), target.attributeLifts)
    }

    @Test
    fun `commit all drains remaining effects in order and completes exactly once`() {
        val actor = unit("actor", 0, 0).apply {
            hasAuthoredTileX = false
            hasAuthoredTileY = false
        }
        val before = snapshot(actor)
        actor.tileX = 2
        actor.hitPoints = 55
        actor.direction = 1
        val after = snapshot(actor)
        before.states.values.forEach(BattleUnitMemento::restore)
        actor.hasAuthoredTileX = false
        actor.hasAuthoredTileY = false
        actor.direction = 3
        val events = mutableListOf<String>()
        var completed = 0
        val transaction = transaction(
            actorId = actor.id,
            before = before,
            after = after,
            hitSideEffects = listOf({ events += "hit-1" }, { events += "hit-2" }),
            completionSideEffects = listOf({ events += "complete-1" }, { events += "complete-2" }),
            restoreSnapshot = { snapshot ->
                events += "restore"
                snapshot.states.values.forEach(BattleUnitMemento::restore)
            },
            onCompleted = { completed++ },
        )

        transaction.commitNextHitSideEffect()
        transaction.commitAll()

        assertEquals(listOf("hit-1", "restore", "hit-2", "complete-1", "complete-2"), events)
        assertEquals(1, completed)
        assertEquals(2, actor.tileX)
        assertEquals(55, actor.hitPoints)
        assertEquals(3, actor.direction)
        assertTrue(actor.hasAuthoredTileX)
        assertTrue(actor.hasAuthoredTileY)

        transaction.commitMovement(commitActionState = true)
        transaction.commitPosition(actor.id, 9, 9)
        transaction.commitVitals(actor.id, hp = 1, mp = 1)
        transaction.commitEconomy(4, 5)
        transaction.commitStatuses(actor.id)
        transaction.commitNextHitSideEffect()
        transaction.commitAll()

        assertEquals(listOf("hit-1", "restore", "hit-2", "complete-1", "complete-2"), events)
        assertEquals(1, completed)
        assertEquals(2, actor.tileX)
        assertEquals(55, actor.hitPoints)
    }

    @Test
    fun `unknown actor and unit IDs leave live state unchanged`() {
        val known = unit("known", 1, 2)
        val before = snapshot(known)
        val after = snapshot(known)
        var economyCalls = 0
        val transaction = transaction(
            actorId = "missing",
            before = before,
            after = after,
            adjustEconomy = { _, _ -> economyCalls++ },
        )

        assertNull(transaction.initialHp("missing"))
        assertNull(transaction.initialMp("missing"))
        transaction.commitMovement(commitActionState = true)
        transaction.commitPosition("missing", 8, 9)
        transaction.commitVitals("missing", hp = 1, mp = 1)
        transaction.commitStatuses("missing")
        transaction.commitStatuses(MagicLocalSettlementEntry(
            targetId = "missing",
            statusesBefore = emptyMap(),
            statusesAfter = mapOf(BattleStatus.POISON to 1),
            attributeLiftsBefore = emptyMap(),
            attributeLiftsAfter = emptyMap(),
            hasStatesPayload = true,
        ))

        assertEquals(1 to 2, known.tileX to known.tileY)
        assertEquals(100, known.hitPoints)
        assertEquals(40, known.magicPoints)
        assertTrue(known.statuses.isEmpty())
        assertEquals(0, economyCalls)
    }

    private fun transaction(
        actorId: String,
        before: BattleActionSnapshot,
        after: BattleActionSnapshot,
        hitSideEffects: List<() -> Unit> = emptyList(),
        completionSideEffects: List<() -> Unit> = emptyList(),
        restoreSnapshot: (BattleActionSnapshot) -> Unit = { snapshot ->
            snapshot.states.values.forEach(BattleUnitMemento::restore)
        },
        adjustEconomy: (Int, Int) -> Unit = { _, _ -> },
        onCompleted: (BattleActionTransaction) -> Unit = {},
    ): BattleActionTransaction = BattleActionTransaction(
        actorId = actorId,
        before = before,
        after = after,
        hitSideEffects = hitSideEffects,
        completionSideEffects = completionSideEffects,
        restoreSnapshot = restoreSnapshot,
        adjustEconomy = adjustEconomy,
        presentationUnit = { id -> before.states[id]?.unit },
        activeUnit = { id -> before.states[id]?.unit },
        onCompleted = onCompleted,
    )

    private fun snapshot(
        vararg units: BattleUnit,
        playerMoney: Int = 0,
        enemyMoney: Int = 0,
    ): BattleActionSnapshot = BattleActionSnapshot(
        topology = Battlefield.TopologySnapshot(units.map { it.id }, emptyList()),
        states = units.associate { it.id to BattleUnitMemento.capture(it) },
        playerMoney = playerMoney,
        enemyMoney = enemyMoney,
        skillTemps = emptyMap(),
        moveLength = 0,
        lastMovePaths = emptyMap(),
        traceActions = emptyList(),
    )

    private fun unit(id: String, x: Int, y: Int): BattleUnit = BattleUnit(
        id = id,
        name = id,
        faction = Faction.PLAYER,
        tileX = x,
        tileY = y,
        hitPoints = 100,
        maxHitPoints = 100,
        magicPoints = 40,
        maxMagicPoints = 40,
    )
}
