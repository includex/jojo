package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BattleAiPresentationStepTest {
    @Test
    fun `BattleScreen scheduler restores saved facing after normal anime32`() {
        var victimDirection = 0
        val scheduler = BattleScreenMutationTestScheduler()

        // `_attack3` saves h=0, then the normal-hit SHOU_GONG_JI3 (anime32)
        // faces the victim toward the attacker (3) only for its reaction.
        BattleScreenHitReactionDirectionScheduler.schedule(
            sourceAction = 32,
            reactionDirection = 3,
            previousDirection = victimDirection,
            startsAt = 1f,
            endsAt = 2f,
            schedule = scheduler::schedule,
            isCurrentReaction = { true },
            setDirection = { victimDirection = it },
        )

        scheduler.advanceTo(1f)
        assertEquals(3, victimDirection)
        scheduler.advanceTo(2f)
        assertEquals(0, victimDirection)
    }

    @Test
    fun `BattleScreen scheduler keeps reaction facing after blocked anime26`() {
        var victimDirection = 0
        val scheduler = BattleScreenMutationTestScheduler()

        // The blocked `_attack3` branch plays FANG_YU (anime26), then calls
        // defaultAction(-1); it must retain the reaction-facing direction.
        BattleScreenHitReactionDirectionScheduler.schedule(
            sourceAction = 26,
            reactionDirection = 3,
            previousDirection = victimDirection,
            startsAt = 1f,
            endsAt = 2f,
            schedule = scheduler::schedule,
            isCurrentReaction = { true },
            setDirection = { victimDirection = it },
        )

        scheduler.advanceTo(1f)
        assertEquals(3, victimDirection)
        scheduler.advanceTo(2f)
        assertEquals(3, victimDirection)
    }

    @Test
    fun `TPGJ exposes each backMove callback without leaking eager final tile`() {
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "attacker", "밀치기", Faction.PLAYER, 0, 0, morale = 1,
                    skills = mapOf(7 to 0, 92 to 0, 221 to 0, 226 to 0, 270 to 0),
                ),
                BattleUnit(
                    "defender", "방어", Faction.ENEMY, 1, 0,
                    hitPoints = 1_000, maxHitPoints = 1_000, morale = 100,
                ),
            ),
            events = emptyList(),
        )

        val attack = assertIs<TacticalActionResult.Attack>(battle.attackForPresentation("attacker", "defender"))
        val moves = attack.physicalPasses.map { assertNotNull(it.targets.single().backMove) }
        val callbackMoves = attack.toPhysicalCallbackInvocations().map { invocation ->
            assertNotNull(invocation.targets.single().backMove)
        }
        val deferred = assertNotNull(battle.pendingActionTransaction)

        assertEquals(listOf(1 to 2, 2 to 3), moves.map { it.fromX to it.toX })
        assertEquals(moves, callbackMoves)
        val hurtSteps = BattlePhysicalCallbackPlan.build(BattlePhysicalCallbackPlan.Input(
            attack.toPhysicalCallbackInvocations(), globalSettlementUnitIds = emptyList(),
        )).filterIsInstance<BattlePhysicalCallbackPlan.Step.HurtUntilComplete>()
        assertEquals(moves, hurtSteps.map { assertNotNull(it.backMove) })
        assertEquals(1 to 0, battle.units.getValue("defender").tileX to battle.units.getValue("defender").tileY)
        deferred.commitPosition("defender", moves[0].toX, moves[0].toY)
        assertEquals(2 to 0, battle.units.getValue("defender").tileX to battle.units.getValue("defender").tileY)
        deferred.commitPosition("defender", moves[1].toX, moves[1].toY)
        assertEquals(3 to 0, battle.units.getValue("defender").tileX to battle.units.getValue("defender").tileY)
    }

    @Test
    fun `physical attack retains exact target-local status settlement payload`() {
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "attacker", "공격", Faction.PLAYER, 0, 0,
                    skills = mapOf(92 to 0, 170 to 0, 226 to 0),
                ),
                BattleUnit("defender", "방어", Faction.ENEMY, 1, 0),
            ),
            events = emptyList(),
        )

        val attack = assertIs<TacticalActionResult.Attack>(battle.attackForPresentation("attacker", "defender"))
        val hit = attack.physicalPasses.single().targets.single()
        val local = hit.localStatusSettlement.entries.single()

        assertTrue(hit.hasLocalStatusSettlement)
        assertEquals("defender", local.targetId)
        assertEquals(emptyMap(), local.attributeLiftsBefore)
        assertEquals(-1, local.attributeLiftsAfter[BattleAttribute.ATTACK])
        assertTrue(local.hasStatesPayload)
        // Eager status mutation remains hidden until `_jiesuan(t, o)` starts.
        assertEquals(emptyMap(), battle.units.getValue("defender").attributeLifts)
        assertNotNull(battle.pendingActionTransaction).commitStatuses(local)
        assertEquals(-1, battle.units.getValue("defender").attributeLifts[BattleAttribute.ATTACK])
    }

    @Test
    fun `physical hit publishes source healing and economy once before final settlement`() {
        val battle = Battle(
            units = listOf(
                BattleUnit(
                    "attacker", "흡수", Faction.PLAYER, 0, 0,
                    hitPoints = 10, maxHitPoints = 100, attack = 100,
                    skills = mapOf(92 to 0, 226 to 0, 237 to 2, 238 to 50, 298 to 0),
                ),
                BattleUnit(
                    "defender", "방어", Faction.ENEMY, 1, 0,
                    hitPoints = 100, maxHitPoints = 100,
                ),
            ),
            events = emptyList(),
            initialPlayerMoney = 100,
            initialEnemyMoney = 100,
        )

        val attack = assertIs<TacticalActionResult.Attack>(battle.attackForPresentation("attacker", "defender"))
        val hit = attack.physicalPasses.single().targets.single()
        val deferred = assertNotNull(battle.pendingActionTransaction)
        val attackerAtHit = 10 + hit.lifeStealHealing + hit.qxlHealing
        val defenderAtHit = 100 - hit.damage

        assertTrue(hit.damage > 0)
        assertTrue(hit.lifeStealHealing > 0)
        assertTrue(hit.qxlHealing > 0)
        assertTrue(hit.playerMoneyDelta > 0)
        assertTrue(hit.enemyMoneyDelta < 0)

        // Eager resolution is hidden until the authored attack hit callback.
        assertEquals(10, battle.units.getValue("attacker").hitPoints)
        assertEquals(100, battle.units.getValue("defender").hitPoints)
        assertEquals(100 to 100, battle.playerMoney to battle.enemyMoney)

        deferred.commitVitals("defender", hp = defenderAtHit)
        deferred.commitVitals("attacker", hp = attackerAtHit)
        deferred.commitEconomy(hit.playerMoneyDelta, hit.enemyMoneyDelta)

        assertEquals(attackerAtHit, battle.units.getValue("attacker").hitPoints)
        assertEquals(defenderAtHit, battle.units.getValue("defender").hitPoints)
        assertEquals(
            100 + hit.playerMoneyDelta to 100 + hit.enemyMoneyDelta,
            battle.playerMoney to battle.enemyMoney,
        )

        deferred.commitAll()
        // commitAll restores the absolute resolved snapshot. It must not add
        // the callback-local values a second time.
        assertEquals(attackerAtHit, battle.units.getValue("attacker").hitPoints)
        assertEquals(defenderAtHit, battle.units.getValue("defender").hitPoints)
        assertEquals(
            100 + hit.playerMoneyDelta to 100 + hit.enemyMoneyDelta,
            battle.playerMoney to battle.enemyMoney,
        )
    }

    @Test
    fun `physical counter yields one idle frame before global action settlement`() {
        val barrier = CounterattackSettlementFrameBarrier()

        val activePass = PhysicalAttackPass(
            kind = PhysicalAttackPassKind.ACTIVE,
            attackerId = "attacker",
            critical = false,
            targets = emptyList(),
        )
        val counterPass = PhysicalAttackPass(
            kind = PhysicalAttackPassKind.COUNTER,
            attackerId = "defender",
            critical = false,
            targets = emptyList(),
        )
        val ordinaryAttack = TacticalActionResult.Attack(
            damage = 1,
            defeated = false,
            physicalPasses = listOf(activePass),
        )
        val counterAttack = ordinaryAttack.copy(physicalPasses = listOf(activePass, counterPass))

        assertFalse(ordinaryAttack.hasPhysicalCounterPass())
        assertTrue(counterAttack.hasPhysicalCounterPass())

        barrier.beginActor(hasPhysicalCounter = counterAttack.hasPhysicalCounterPass())
        assertTrue(barrier.yieldIdleBeforeCommit())
        assertFalse(barrier.yieldIdleBeforeCommit())

        // Ordinary physical attacks and every non-counter action keep the
        // existing direct action-completion -> settlement path.
        barrier.beginActor(hasPhysicalCounter = ordinaryAttack.hasPhysicalCounterPass())
        assertFalse(barrier.yieldIdleBeforeCommit())
    }

    @Test
    fun `camp script movement completion yields once before camp first focus`() {
        val barrier = ScriptedMovementCampTransitionFrameBarrier()

        barrier.observe(
            inCampScript = true,
            scriptWasPending = true,
            scriptCompleted = true,
            movementWasActive = true,
            movementIsActive = false,
        )
        assertTrue(barrier.yieldBeforeCampTransition())
        assertFalse(barrier.yieldBeforeCampTransition())
    }

    @Test
    fun `camp transition barrier does not delay unrelated script completions`() {
        val barrier = ScriptedMovementCampTransitionFrameBarrier()
        val unrelatedEdges = listOf(
            // No authored move occurred.
            listOf(true, true, true, false, false),
            // The move is still running.
            listOf(true, true, true, true, true),
            // Movement completed but the script continued into another wait.
            listOf(true, true, false, true, false),
            // Bootstrap/round-script movement is not a camp-first transition.
            listOf(false, true, true, true, false),
        )
        unrelatedEdges.forEach { edge ->
            barrier.observe(
                inCampScript = edge[0],
                scriptWasPending = edge[1],
                scriptCompleted = edge[2],
                movementWasActive = edge[3],
                movementIsActive = edge[4],
            )
            assertFalse(barrier.yieldBeforeCampTransition())
        }
    }

    @Test
    fun `action status barrier exposes clip completion and XD settlement before next actor`() {
        val barrier = ActionStatusFrameBarrier()
        barrier.beginActor()

        // The terminal action row contains the synchronous XD settlement;
        // only its next resume may select another actor.
        assertTrue(barrier.yieldAfterCommit(hasAction = true))
        assertFalse(barrier.yieldAfterCommit(hasAction = true))

        // A no-result actor settles directly through _shifudu/_jiesuan and
        // must not inherit the action-only frame delay.
        barrier.beginActor()
        assertFalse(barrier.yieldAfterCommit(hasAction = false))
    }

    @Test
    fun `final AI action callback publishes XD state and round atomically`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 1, 0, hitPoints = 500, maxHitPoints = 500),
                BattleUnit("enemy", "적군", Faction.ENEMY, 0, 0, ai = 0, attack = 100),
            ),
            events = emptyList(),
        )
        battle.endTurn()
        battle.resolveAiTurn(maxUnits = 1, deferMutations = true)
        val actor = battle.units.getValue("enemy")
        val deferred = assertNotNull(battle.pendingActionTransaction)

        assertEquals(false to 0, actor.hasActed to actor.actionStatusRound)
        deferred.commitAll()

        // Source _jiesuan(g_charinfo) writes STATUS_ROUND and XD before the
        // final attack callback's rendered frame is sampled.  There is no
        // intermediate frame containing only one half of this transition.
        assertEquals(true to 1, actor.hasActed to actor.actionStatusRound)
        assertEquals(null, battle.pendingActionTransaction)
    }

    @Test
    fun `final deferred commit preserves presentation attack facing`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격", Faction.PLAYER, 0, 0, direction = 2, attack = 100),
                BattleUnit("target", "방어", Faction.ENEMY, 1, 0, hitPoints = 500, maxHitPoints = 500),
            ),
            events = emptyList(),
        )
        assertIs<TacticalActionResult.Attack>(battle.attackForPresentation("attacker", "target"))
        val actor = battle.units.getValue("attacker")
        val target = battle.units.getValue("target")
        // BattleScreen.sourceActionAnimation/countDir publishes these while
        // the precomputed logical snapshot still contains direction 2.
        actor.direction = 1
        target.direction = 3

        assertNotNull(battle.pendingActionTransaction).commitAll()

        assertEquals(1, battle.units.getValue("attacker").direction)
        assertEquals(3, battle.presentationUnit("target")?.direction)
    }

    @Test
    fun `move-only AI commits XD round and state with final movement callback`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 10, 0),
                BattleUnit("enemy", "적", Faction.ENEMY, 0, 0, ai = 1, movement = 3),
            ),
            events = emptyList(),
        )
        battle.endTurn()
        battle.resolveAiTurn(maxUnits = 1, deferMutations = true)
        val resolution = assertNotNull(battle.lastAiUnitResolution)
        assertEquals(null, resolution.result)
        val actor = battle.units.getValue("enemy")
        assertFalse(actor.hasActed)
        assertEquals(0, actor.actionStatusRound)

        assertNotNull(battle.pendingActionTransaction).commitMovement(commitActionState = true)

        assertTrue(actor.hasActed)
        assertEquals(1, actor.actionStatusRound)
        assertEquals(resolution.toX to resolution.toY, actor.tileX to actor.tileY)
    }

    @Test
    fun `player movement keeps logical position at source until move callback`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0, movement = 3),
                BattleUnit("enemy", "적", Faction.ENEMY, 5, 0),
            ),
            events = emptyList(),
        )

        val deferredMove = battle.moveUnitForPresentation("player", 2, 0)
        assertEquals(TacticalActionResult.Success, deferredMove.result)
        assertTrue(deferredMove.path.size >= 2)
        assertEquals(0 to 0, battle.units.getValue("player").tileX to battle.units.getValue("player").tileY)

        val deferred = assertNotNull(battle.pendingActionTransaction)
        deferred.commitMovement()
        assertEquals(2 to 0, battle.units.getValue("player").tileX to battle.units.getValue("player").tileY)
        deferred.commitAll()
    }

    @Test
    fun `player physical action defers HP callbacks and lethal removal`() {
        var damageCallbacks = 0
        var defeatedCallbacks = 0
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0, attack = 999, morale = 999, critical = 999),
                BattleUnit("enemy", "적", Faction.ENEMY, 1, 0, hitPoints = 1, maxHitPoints = 1, morale = 1),
            ),
            events = emptyList(),
            onPhysicalDamage = { _, _, _ -> damageCallbacks++ },
            onUnitDefeated = { _, _ -> defeatedCallbacks++ },
        )

        val attack = assertIs<TacticalActionResult.Attack>(battle.attackForPresentation("player", "enemy"))
        assertTrue(attack.hit)
        assertEquals(1, battle.units.getValue("enemy").hitPoints)
        assertEquals(0, damageCallbacks)
        assertEquals(0, defeatedCallbacks)

        val deferred = assertNotNull(battle.pendingActionTransaction)
        deferred.commitVitals("enemy", hp = 0)
        deferred.commitNextHitSideEffect()
        assertEquals(0, battle.units.getValue("enemy").hitPoints)
        assertEquals(1, damageCallbacks)
        assertTrue("enemy" in battle.units)

        deferred.commitAll()
        assertFalse("enemy" in battle.units)
        assertEquals(1, defeatedCallbacks)
    }

    @Test
    fun `player property defers recovery inventory and acted state until item animation completes`() {
        var inventoryConsumptions = 0
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("ally", "우군", Faction.PLAYER, 1, 0, hitPoints = 40, maxHitPoints = 100),
                BattleUnit("enemy", "적", Faction.ENEMY, 5, 0),
            ),
            events = emptyList(),
            propertyItems = mapOf(7 to BattlePropertyItem(7, "회복약", 26, 30)),
            consumeProperty = { inventoryConsumptions++; true },
        )

        assertIs<TacticalActionResult.Item>(battle.usePropertyForPresentation("player", "ally", 7))
        assertEquals(40, battle.units.getValue("ally").hitPoints)
        assertFalse(battle.units.getValue("player").hasActed)
        assertEquals(0, battle.units.getValue("player").actionStatusRound)
        assertEquals(0, inventoryConsumptions)

        assertNotNull(battle.pendingActionTransaction).commitAll()
        assertEquals(70, battle.units.getValue("ally").hitPoints)
        assertTrue(battle.units.getValue("player").hasActed)
        assertEquals(1, battle.units.getValue("player").actionStatusRound)
        assertEquals(1, inventoryConsumptions)
    }

    @Test
    fun `runtime AI defers movement HP and completion to presentation callbacks`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 4, 0, hitPoints = 500, maxHitPoints = 500, critical = 1),
                BattleUnit("enemy", "적", Faction.ENEMY, 0, 0, ai = 1, movement = 3, critical = 100),
            ),
            events = emptyList(),
        )
        battle.endTurn()

        battle.resolveAiTurn(maxUnits = 1, deferMutations = true)
        val resolution = assertNotNull(battle.lastAiUnitResolution)
        val attack = assertIs<TacticalActionResult.Attack>(resolution.result)
        val deferred = assertNotNull(battle.pendingActionTransaction)

        assertEquals(0 to 0, battle.units.getValue("enemy").tileX to battle.units.getValue("enemy").tileY)
        assertEquals(500, battle.units.getValue("player").hitPoints)
        assertFalse(battle.units.getValue("enemy").hasActed)
        assertEquals(0, battle.units.getValue("enemy").actionStatusRound)

        deferred.commitMovement()
        assertEquals(resolution.toX to resolution.toY,
            battle.units.getValue("enemy").tileX to battle.units.getValue("enemy").tileY)
        assertEquals(500, battle.units.getValue("player").hitPoints)

        deferred.commitVitals("player", hp = 500 - attack.damage)
        assertEquals(500 - attack.damage, battle.units.getValue("player").hitPoints)
        assertFalse(battle.units.getValue("enemy").hasActed)

        deferred.commitAll()
        assertTrue(battle.units.getValue("enemy").hasActed)
        assertEquals(1, battle.units.getValue("enemy").actionStatusRound)
        assertEquals(null, battle.pendingActionTransaction)
    }

    @Test
    fun `lethal AI target remains live until death callback completes`() {
        var physicalDamageCallbacks = 0
        var equipmentExperienceCallbacks = 0
        var defeatedCallbacks = 0
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 1, 0, hitPoints = 1, maxHitPoints = 1),
                BattleUnit("enemy", "적", Faction.ENEMY, 0, 0, ai = 0, attack = 500, critical = 500),
            ),
            events = emptyList(),
            onPhysicalDamage = { _, _, _ -> physicalDamageCallbacks++ },
            onEquipmentExperience = { _, _, _ ->
                equipmentExperienceCallbacks++
                emptyList()
            },
            onUnitDefeated = { _, _ -> defeatedCallbacks++ },
        )
        battle.endTurn()

        battle.resolveAiTurn(maxUnits = 1, deferMutations = true)
        val deferred = assertNotNull(battle.pendingActionTransaction)
        assertTrue("player" in battle.units)
        assertEquals(1, battle.units.getValue("player").hitPoints)
        assertEquals(0, physicalDamageCallbacks)
        assertEquals(0, equipmentExperienceCallbacks)
        assertEquals(0, defeatedCallbacks)
        assertTrue(battle.traceActions.isEmpty())

        deferred.commitVitals("player", hp = 0)
        assertTrue("player" in battle.units)
        deferred.commitNextHitSideEffect()
        assertEquals(1, physicalDamageCallbacks)
        assertEquals(1, equipmentExperienceCallbacks)
        assertEquals(0, defeatedCallbacks)
        deferred.commitAll()
        assertFalse("player" in battle.units)
        assertEquals(1, defeatedCallbacks)
        assertTrue(battle.traceActions.isNotEmpty())
    }

    @Test
    fun `AI resolves exactly one actor and retains its movement and attack presentation payload`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("player", "아군", Faction.PLAYER, 4, 0, hitPoints = 500, maxHitPoints = 500, critical = 1),
                BattleUnit("enemy-a", "적A", Faction.ENEMY, 0, 0, ai = 1, movement = 3, critical = 100),
                BattleUnit("enemy-b", "적B", Faction.ENEMY, 0, 2, ai = 0),
            ),
            events = emptyList(),
        )
        battle.endTurn()

        val first = battle.resolveAiTurn(maxUnits = 1)
        val presentation = assertNotNull(battle.lastAiUnitResolution)

        assertEquals(1, first.moves)
        assertEquals(1, first.attacks)
        assertEquals("enemy-a", presentation.actorId)
        assertEquals(0 to 0, presentation.fromX to presentation.fromY)
        assertEquals(3 to 0, presentation.toX to presentation.toY)
        assertTrue(presentation.path.size >= 2)
        assertTrue(presentation.moveArea.isNotEmpty())
        assertTrue(4 to 0 in presentation.actionArea)
        assertEquals("player", presentation.targetId)
        assertIs<TacticalActionResult.Attack>(presentation.result)
        assertEquals(500, presentation.healthBeforeAction.getValue("player"))
        assertTrue(battle.units.getValue("enemy-a").hasActed)
        assertFalse(battle.units.getValue("enemy-b").hasActed)
        assertTrue(battle.hasPendingAiUnits())

        battle.resolveAiTurn(maxUnits = 1)
        assertEquals("enemy-b", assertNotNull(battle.lastAiUnitResolution).actorId)
        assertFalse(battle.hasPendingAiUnits())
    }
}

private class BattleScreenMutationTestScheduler {
    private data class Mutation(val at: Float, val run: () -> Unit)

    private val pending = mutableListOf<Mutation>()

    fun schedule(at: Float, mutation: () -> Unit) {
        pending += Mutation(at, mutation)
        pending.sortBy(Mutation::at)
    }

    fun advanceTo(now: Float) {
        while (pending.firstOrNull()?.at?.let { now >= it } == true) {
            pending.removeAt(0).run()
        }
    }
}
