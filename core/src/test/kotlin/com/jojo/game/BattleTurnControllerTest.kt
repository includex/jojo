package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.battle.BattleTerrainGrid

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleTurnControllerTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleTurnControllerTest {
    @Test
    fun `consecutive no-result actors expose one XD settlement per render`() {
        val gate = ConsecutiveNoResultFrameGate()

        gate.beginRender()
        assertFalse(gate.shouldYieldBefore(nextIsNoResult = true))
        gate.markCompleted()
        assertTrue(gate.shouldYieldBefore(nextIsNoResult = true))
        // A real move or command remains part of the current callback tail.
        assertFalse(gate.shouldYieldBefore(nextIsNoResult = false))

        gate.beginRender()
        assertFalse(gate.shouldYieldBefore(nextIsNoResult = true))
    }

    @Test
    fun `XD status writes round before action state and retains it when cleared`() {
        val unit = BattleUnit("u", "u", Faction.PLAYER, 0, 0)
        assertEquals(0, unit.actionStatusRound)

        unit.markActionComplete()
        assertEquals(1, unit.actionStatusRound)
        assertTrue(unit.hasActed)

        // Source setStateRound(remove XD) writes the configured XD round and
        // then returns status to NORMAL; it does not zero STATUS_ROUND.
        unit.hasActed = false
        assertEquals(1, unit.actionStatusRound)
        assertFalse(unit.hasActed)
    }

    @Test
    fun `empty AI camp exposes one entry frame before completion callback`() {
        val barrier = EmptyAiCampFrameBarrier()

        barrier.begin(hasActor = false)
        assertTrue(barrier.yieldEntryFrame())
        assertFalse(barrier.yieldEntryFrame())

        barrier.begin(hasActor = true)
        assertFalse(barrier.yieldEntryFrame())
    }

    @Test
    fun `only a committed collocated player move exposes one completion frame`() {
        val barrier = CommittedPlayerMoveFrameBarrier()

        barrier.beginActor()
        assertFalse(barrier.yieldCompletionFrame(isPlayer = false, moved = true))
        assertTrue(barrier.yieldCompletionFrame(isPlayer = true, moved = true))
        assertFalse(barrier.yieldCompletionFrame(isPlayer = true, moved = true))

        barrier.beginActor()
        assertFalse(barrier.yieldCompletionFrame(isPlayer = true, moved = false))
        assertTrue(barrier.yieldCompletionFrame(isPlayer = true, moved = true))
    }

    @Test
    fun `round advance can expose incremented round in reinforcements camp before script callback`() {
        val state = battle(withFriend = false)
        val controller = BattleTurnController(
            state,
            showCamp = {},
            runCampScript = { true },
            runAi = { AiTurnResult(0, 0, 0) },
            runRoundScript = { true },
            deferSynchronousRoundScriptCompletion = true,
        )

        controller.endPlayerTurn()
        controller.completeCampCard()

        assertEquals(BattleTurnController.Phase.ROUND_SCRIPT, controller.phase)
        assertEquals(2, state.round)
        assertEquals(Faction.REINFORCEMENTS, state.activeFaction)

        controller.completeRoundScript()
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
    }

    private fun battle(withFriend: Boolean = true) = Battle(
        units = buildList {
            add(BattleUnit("mine", "아군", Faction.PLAYER, 0, 0))
            if (withFriend) add(BattleUnit("friend", "우군", Faction.FRIEND, 2, 0))
            add(BattleUnit("enemy", "적군", Faction.ENEMY, 4, 0))
        },
        events = emptyList(),
    )

    @Test
    fun `bootstrap blocks every operation until the authored startOper handoff`() {
        val state = battle()
        val calls = mutableListOf<String>()
        val controller = BattleTurnController(
            state,
            showCamp = { calls += "card" },
            runCampScript = { calls += "script"; true },
            runAi = { calls += "ai"; AiTurnResult(0, 0, 0) },
            initialPhase = BattleTurnController.Phase.BOOTSTRAP,
        )

        assertEquals(BattleTurnController.Phase.BOOTSTRAP, controller.phase)
        assertFalse(controller.endPlayerTurn())
        assertFalse(controller.runCollocatedPlayerTurn())
        assertEquals(emptyList(), calls)
        assertEquals(Faction.PLAYER, state.activeFaction)

        controller.completeBootstrap()

        assertEquals(BattleTurnController.Phase.PLAYER_INPUT, controller.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        assertEquals(emptyList(), calls, "initial ctrl_mine(true) skips ordinary camp-start callbacks")
    }

    @Test
    fun `camp transitions remain source ordered and never consume AI in player input`() {
        val state = battle()
        val calls = mutableListOf<String>()
        val controller = BattleTurnController(
            state,
            showCamp = { calls += "card:${it.turn.activeFaction}:${it.showsRoundNumber}" },
            runCampScript = { camp -> calls += "script:$camp"; true },
            runAi = { camp -> calls += "ai:$camp"; AiTurnResult(0, 0, 1) },
        )

        assertTrue(controller.endPlayerTurn())
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
        assertEquals(listOf("script:FRIEND", "ai:FRIEND", "card:ENEMY:false"), calls)
        assertFalse(controller.endPlayerTurn())

        controller.completeCampCard()
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
        assertEquals(
            listOf(
                "script:FRIEND", "ai:FRIEND", "card:ENEMY:false",
                "script:ENEMY", "ai:ENEMY", "script:REINFORCEMENTS",
                "ai:REINFORCEMENTS", "card:PLAYER:true",
            ),
            calls,
        )

        controller.completeCampCard()
        assertEquals(BattleTurnController.Phase.PLAYER_INPUT, controller.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        assertEquals(2, state.round)
        assertEquals("script:PLAYER", calls.last())
    }

    @Test
    fun `source script pause blocks AI until explicit completion`() {
        val state = battle(withFriend = false)
        val calls = mutableListOf<String>()
        val controller = BattleTurnController(
            state,
            showCamp = { calls += "card:${it.turn.activeFaction}:${it.showsRoundNumber}" },
            runCampScript = { camp -> calls += "script:$camp"; camp != Faction.ENEMY },
            runAi = { camp -> calls += "ai:$camp"; AiTurnResult(1, 1, 0) },
        )

        assertTrue(controller.endPlayerTurn())
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
        assertEquals(listOf("script:FRIEND", "ai:FRIEND", "card:ENEMY:false"), calls)
        controller.completeCampCard()
        assertEquals(BattleTurnController.Phase.CAMP_SCRIPT, controller.phase)
        assertEquals(listOf("script:FRIEND", "ai:FRIEND", "card:ENEMY:false", "script:ENEMY"), calls)
        controller.completeCampScript()
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
        assertEquals(
            listOf(
                "script:FRIEND", "ai:FRIEND", "card:ENEMY:false", "script:ENEMY",
                "ai:ENEMY", "script:REINFORCEMENTS", "ai:REINFORCEMENTS", "card:PLAYER:true",
            ),
            calls,
        )
    }

    @Test
    fun `AI camp does not advance until its visible presentation callback completes`() {
        val state = battle(withFriend = false)
        var presenting = false
        val calls = mutableListOf<String>()
        val controller = BattleTurnController(
            state,
            showCamp = { calls += "card:${it.turn.activeFaction}" },
            runCampScript = { true },
            runAi = { presenting = it == Faction.ENEMY; calls += "ai:$it"; AiTurnResult(1, 1, 0) },
            hasPendingAiPresentation = { presenting },
        )

        controller.endPlayerTurn()
        controller.completeCampCard()
        assertEquals(BattleTurnController.Phase.AI, controller.phase)
        assertEquals(Faction.ENEMY, state.activeFaction)
        assertEquals(listOf("ai:FRIEND", "card:ENEMY", "ai:ENEMY"), calls)

        presenting = false
        controller.completeAiPresentation(AiTurnResult(1, 1, 0))
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        assertEquals(AiTurnResult(1, 1, 0), controller.lastAiResult)
        assertEquals(listOf("ai:FRIEND", "card:ENEMY", "ai:ENEMY", "ai:REINFORCEMENTS", "card:PLAYER"), calls)
    }

    @Test
    fun `AI post-action script end finishes without restore or death presentation`() {
        val state = battle(withFriend = false)
        val callbacks = mutableListOf<String>()
        var presenting = false
        val controller = BattleTurnController(
            state,
            showCamp = { callbacks += "card:${it.turn.activeFaction}" },
            runCampScript = { callbacks += "script:$it"; true },
            runAi = {
                presenting = it == Faction.ENEMY
                callbacks += "ai:$it"
                AiTurnResult(0, 1, 0)
            },
            hasPendingAiPresentation = { presenting },
            presentDeaths = { callbacks += "deaths:$it"; true },
            presentCampRestore = { callbacks += "restore:${it.faction}"; true },
        )

        // Enter S22's equivalent enemy `_ai2` completion callback.  Its
        // first post-action runBattleScript will subsequently call stage.end
        // and stage.lose, publishing this scripted outcome.
        assertTrue(controller.endPlayerTurn())
        controller.completeCampCard()
        assertEquals(BattleTurnController.Phase.AI, controller.phase)
        assertEquals(Faction.ENEMY, state.activeFaction)
        assertEquals(1, state.round)
        val callbacksBeforeScriptEnd = callbacks.toList()

        state.setScriptedOutcome(BattleOutcome.ENEMY_VICTORY)

        controller.finishScriptEndedBattle()
        assertEquals(BattleTurnController.Phase.FINISHED, controller.phase)
        assertEquals(Faction.ENEMY, state.activeFaction)
        assertEquals(1, state.round)
        assertEquals(
            callbacksBeforeScriptEnd,
            callbacks,
            "script end must not queue CAMP_RESTORE or its unitDeath/hide callback",
        )
    }

    @Test
    fun `collocated player move callback stops only for script end with outcome`() {
        assertTrue(CollocatedPlayerMoveScriptEnd.finishesAiTurn(
            camp = Faction.PLAYER,
            moveCallbackStarted = true,
            scriptState = PlaybackState.COMPLETE,
            battleEndedByScript = true,
            scriptedOutcome = BattleOutcome.ENEMY_VICTORY,
            observedOutcome = BattleOutcome.ENEMY_VICTORY,
        ))

        // stage.end() alone must retain the _ai2 action/post-script tail.
        assertFalse(CollocatedPlayerMoveScriptEnd.finishesAiTurn(
            camp = Faction.PLAYER,
            moveCallbackStarted = true,
            scriptState = PlaybackState.COMPLETE,
            battleEndedByScript = true,
            scriptedOutcome = null,
            observedOutcome = null,
        ))
        // A result that has not been propagated into Battle is not safe to
        // finish: BattleTurnController validates the observable outcome.
        assertFalse(CollocatedPlayerMoveScriptEnd.finishesAiTurn(
            camp = Faction.PLAYER,
            moveCallbackStarted = true,
            scriptState = PlaybackState.COMPLETE,
            battleEndedByScript = true,
            scriptedOutcome = BattleOutcome.ENEMY_VICTORY,
            observedOutcome = null,
        ))
        // This guard is intentionally unavailable to ordinary enemy `_ai2`.
        assertFalse(CollocatedPlayerMoveScriptEnd.finishesAiTurn(
            camp = Faction.ENEMY,
            moveCallbackStarted = true,
            scriptState = PlaybackState.COMPLETE,
            battleEndedByScript = true,
            scriptedOutcome = BattleOutcome.ENEMY_VICTORY,
            observedOutcome = BattleOutcome.ENEMY_VICTORY,
        ))
    }

    @Test
    fun `collocated player AI also waits for visible presentation`() {
        val state = battle(withFriend = false)
        var presenting = false
        val controller = BattleTurnController(
            state,
            showCamp = {},
            runCampScript = { true },
            runAi = { presenting = it == Faction.PLAYER; AiTurnResult(0, 0, 1) },
            hasPendingAiPresentation = { presenting },
        )

        assertTrue(controller.runCollocatedPlayerTurn())
        assertEquals(BattleTurnController.Phase.AI, controller.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        presenting = false
        controller.completeAiPresentation()
        assertEquals(Faction.ENEMY, state.activeFaction)
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
    }

    @Test
    fun `source lifecycle barriers prevent future camp mutations from appearing early`() {
        val mine = BattleUnit("mine", "아군", Faction.PLAYER, 0, 0)
        val enemy = BattleUnit(
            "enemy", "적군", Faction.ENEMY, 4, 0,
            hitPoints = 50,
            maxHitPoints = 100,
            statuses = linkedMapOf(BattleStatus.PARALYSIS to 2),
        )
        val eventCalls = mutableListOf<String>()
        val state = Battle(
            units = listOf(mine, enemy),
            events = listOf(BattleEvent("enemy-script", TurnTrigger(1, Faction.ENEMY)) { eventCalls += "enemy-script" }),
            terrain = BattleTerrainGrid(5, 1, listOf(intArrayOf(0, 0, 0, 0, 7))),
            terrainResumeRates = mapOf(7 to 10),
        )
        val calls = mutableListOf<String>()
        var aiPresenting = false
        val controller = BattleTurnController(
            state,
            showCamp = { calls += "card:${it.turn.activeFaction}" },
            runCampScript = { calls += "script:$it"; it != Faction.ENEMY },
            runAi = { aiPresenting = it == Faction.ENEMY; calls += "ai:$it"; AiTurnResult(0, 0, 1) },
            hasPendingAiPresentation = { aiPresenting },
            presentCampState = { calls += "state:${it.faction}"; it.faction != Faction.ENEMY },
            presentDeaths = { calls += "deaths:$it"; state.activeFaction == Faction.FRIEND },
            presentCampRestore = { calls += "restore:${it.faction}"; it.faction == Faction.FRIEND },
        )

        assertTrue(controller.endPlayerTurn())
        assertEquals(BattleTurnController.Phase.CAMP_RESTORE, controller.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        assertEquals(50, enemy.hitPoints)
        assertEquals(listOf("restore:PLAYER"), calls)

        controller.completeCampRestorePresentation()
        assertEquals(BattleTurnController.Phase.CAMP_RESTORE_DEATHS, controller.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)

        controller.completeCampRestoreDeathPresentation()
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
        assertEquals(Faction.ENEMY, state.activeFaction)
        assertEquals(50, enemy.hitPoints)
        assertEquals(emptyList(), eventCalls, "camp script must not execute behind RoundLayer")

        controller.completeCampCard()
        assertEquals(BattleTurnController.Phase.CAMP_STATE, controller.phase)
        assertEquals(60, enemy.hitPoints)
        assertEquals(1, enemy.statuses[BattleStatus.PARALYSIS])
        assertEquals(emptyList(), eventCalls, "script waits for state presentation completion")
        assertEquals(
            listOf(
                "restore:PLAYER", "deaths:CAMP_RESTORE",
                "state:FRIEND", "script:FRIEND", "deaths:CAMP_START", "ai:FRIEND",
                "restore:FRIEND", "deaths:CAMP_RESTORE", "card:ENEMY", "state:ENEMY",
            ),
            calls,
        )

        controller.completeCampStatePresentation()
        assertEquals(BattleTurnController.Phase.CAMP_SCRIPT, controller.phase)
        assertEquals(listOf("enemy-script"), eventCalls)
        assertEquals(listOf("enemy-script"), controller.lastTurn?.firedEvents)
        assertEquals("script:ENEMY", calls.last())
        controller.completeCampScript()
        assertEquals(BattleTurnController.Phase.CAMP_DEATHS, controller.phase)
        assertEquals("deaths:CAMP_START", calls.last())
        controller.completeCampDeathPresentation()
        assertEquals(BattleTurnController.Phase.AI, controller.phase)
        assertEquals("ai:ENEMY", calls.last())
    }

    @Test
    fun `new round script and death finish before weather and player card`() {
        val state = Battle(
            units = listOf(
                BattleUnit("mine", "아군", Faction.PLAYER, 0, 0),
                BattleUnit("enemy", "적군", Faction.ENEMY, 4, 0),
            ),
            events = emptyList(),
            initialWeather = BattleWeather.CLEAR,
            weatherSchedule = listOf(BattleWeather.CLEAR, BattleWeather.CLOUDY, BattleWeather.WINDY),
        )
        val calls = mutableListOf<String>()
        state.setSkillTemp("mine", 900, 5, recordedRound = 1)
        val controller = BattleTurnController(
            state,
            showCamp = { calls += "card:r${it.turn.round}:${it.turn.activeFaction}" },
            runCampScript = { true },
            runAi = { AiTurnResult(0, 0, 1) },
            presentCampRestore = { calls += "restore"; true },
            presentDeaths = { calls += "deaths:$it"; it != BattleTurnController.DeathCheckpoint.ROUND_START },
            runRoundScript = { calls += "round:${it.completedRound}->${it.round}"; false },
            presentWeather = { calls += "weather:${it.previous}->${it.current}"; false },
        )

        controller.endPlayerTurn()
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
        assertEquals("card:r1:ENEMY", calls.last())

        // Card -> state -> camp script -> deaths -> AI -> restore -> deaths ->
        // addRound. The explicit round-script barrier must stop before weather.
        controller.completeCampCard()
        assertEquals(BattleTurnController.Phase.ROUND_SCRIPT, controller.phase)
        assertEquals(2, state.round)
        assertEquals(Faction.REINFORCEMENTS, state.activeFaction)
        assertEquals(BattleWeather.CLEAR, state.weather)
        assertEquals(5, state.skillTemp("mine", 900), "resetSkillTemp is after round script/death")
        assertEquals("round:1->2", calls.last())
        assertTrue(calls.none { it.startsWith("weather:") })

        controller.completeRoundScript()
        assertEquals(BattleTurnController.Phase.ROUND_DEATHS, controller.phase)
        assertEquals(BattleWeather.CLEAR, state.weather)
        assertEquals("deaths:ROUND_START", calls.last())

        controller.completeRoundDeathPresentation()
        assertEquals(BattleTurnController.Phase.WEATHER, controller.phase)
        assertEquals(BattleWeather.WINDY, state.weather)
        assertEquals(0, state.skillTemp("mine", 900))
        assertEquals(Faction.REINFORCEMENTS, state.activeFaction)
        assertEquals("weather:CLEAR->WINDY", calls.last())

        controller.completeWeatherPresentation()
        assertEquals(BattleTurnController.Phase.CAMP_CARD, controller.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        assertEquals("card:r2:PLAYER", calls.last())
    }
}
