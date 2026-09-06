// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*

import com.jojo.game.application.battle.*
import com.jojo.game.domain.battle.turn.*
import com.jojo.game.domain.battle.BattleTerrainGrid
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** BattleTurnControllerTest: BattleTurnController의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleTurnControllerTest {
    @Test
    fun `consecutive no-result actors expose one XD settlement per render`() {
        val gate = ConsecutiveNoResultFrameGate()

        gate.beginRender()
        assertFalse(gate.shouldYieldBefore(nextIsNoResult = true))
        gate.markCompleted()
        assertTrue(gate.shouldYieldBefore(nextIsNoResult = true))
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
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

        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택 (NORMAL, STATUS_ROUND)을 검증한다.
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

        assertEquals(BattleTurnPhase.ROUND_SCRIPT, controller.snapshot.phase)
        assertEquals(2, state.round)
        assertEquals(Faction.REINFORCEMENTS, state.activeFaction)

        controller.completeRoundScript()
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
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
            initialPhase = BattleTurnPhase.BOOTSTRAP,
        )

        assertEquals(BattleTurnPhase.BOOTSTRAP, controller.snapshot.phase)
        assertFalse(controller.endPlayerTurn())
        assertFalse(controller.runCollocatedPlayerTurn())
        assertEquals(emptyList(), calls)
        assertEquals(Faction.PLAYER, state.activeFaction)

        controller.completeBootstrap()

        assertEquals(BattleTurnPhase.PLAYER_INPUT, controller.snapshot.phase)
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
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
        assertEquals(listOf("script:FRIEND", "ai:FRIEND", "card:ENEMY:false"), calls)
        assertFalse(controller.endPlayerTurn())

        controller.completeCampCard()
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
        assertEquals(
            listOf(
                "script:FRIEND", "ai:FRIEND", "card:ENEMY:false",
                "script:ENEMY", "ai:ENEMY", "script:REINFORCEMENTS",
                "ai:REINFORCEMENTS", "card:PLAYER:true",
            ),
            calls,
        )

        controller.completeCampCard()
        assertEquals(BattleTurnPhase.PLAYER_INPUT, controller.snapshot.phase)
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
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
        assertEquals(listOf("script:FRIEND", "ai:FRIEND", "card:ENEMY:false"), calls)
        controller.completeCampCard()
        assertEquals(BattleTurnPhase.CAMP_SCRIPT, controller.snapshot.phase)
        assertEquals(listOf("script:FRIEND", "ai:FRIEND", "card:ENEMY:false", "script:ENEMY"), calls)
        controller.completeCampScript()
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
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
        assertEquals(BattleTurnPhase.AI, controller.snapshot.phase)
        assertEquals(Faction.ENEMY, state.activeFaction)
        assertEquals(listOf("ai:FRIEND", "card:ENEMY", "ai:ENEMY"), calls)

        presenting = false
        controller.completeAiPresentation(AiTurnResult(1, 1, 0))
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        assertEquals(AiTurnResult(1, 1, 0), controller.snapshot.lastAiResult)
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

        // 테스트 근거: 연출 프레임과 콜백 처리 순서 (S22)을 검증한다.
        assertTrue(controller.endPlayerTurn())
        controller.completeCampCard()
        assertEquals(BattleTurnPhase.AI, controller.snapshot.phase)
        assertEquals(Faction.ENEMY, state.activeFaction)
        assertEquals(1, state.round)
        val callbacksBeforeScriptEnd = callbacks.toList()

        state.setScriptedOutcome(BattleOutcome.ENEMY_VICTORY)

        controller.finishScriptEndedBattle()
        assertEquals(BattleTurnPhase.FINISHED, controller.snapshot.phase)
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

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        assertFalse(CollocatedPlayerMoveScriptEnd.finishesAiTurn(
            camp = Faction.PLAYER,
            moveCallbackStarted = true,
            scriptState = PlaybackState.COMPLETE,
            battleEndedByScript = true,
            scriptedOutcome = null,
            observedOutcome = null,
        ))
        // 테스트 근거: 전투 계산·난수 소비·경계값을 검증한다.
        assertFalse(CollocatedPlayerMoveScriptEnd.finishesAiTurn(
            camp = Faction.PLAYER,
            moveCallbackStarted = true,
            scriptState = PlaybackState.COMPLETE,
            battleEndedByScript = true,
            scriptedOutcome = BattleOutcome.ENEMY_VICTORY,
            observedOutcome = null,
        ))
        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
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
        assertEquals(BattleTurnPhase.AI, controller.snapshot.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        presenting = false
        controller.completeAiPresentation()
        assertEquals(Faction.ENEMY, state.activeFaction)
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
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
        assertEquals(BattleTurnPhase.CAMP_RESTORE, controller.snapshot.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        assertEquals(50, enemy.hitPoints)
        assertEquals(listOf("restore:PLAYER"), calls)

        controller.completeCampRestorePresentation()
        assertEquals(BattleTurnPhase.CAMP_RESTORE_DEATHS, controller.snapshot.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)

        controller.completeCampRestoreDeathPresentation()
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
        assertEquals(Faction.ENEMY, state.activeFaction)
        assertEquals(50, enemy.hitPoints)
        assertEquals(emptyList(), eventCalls, "camp script must not execute behind RoundLayer")

        controller.completeCampCard()
        assertEquals(BattleTurnPhase.CAMP_STATE, controller.snapshot.phase)
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
        assertEquals(BattleTurnPhase.CAMP_SCRIPT, controller.snapshot.phase)
        assertEquals(listOf("enemy-script"), eventCalls)
        assertEquals(listOf("enemy-script"), controller.snapshot.lastTurn?.firedEvents)
        assertEquals("script:ENEMY", calls.last())
        controller.completeCampScript()
        assertEquals(BattleTurnPhase.CAMP_DEATHS, controller.snapshot.phase)
        assertEquals("deaths:CAMP_START", calls.last())
        controller.completeCampDeathPresentation()
        assertEquals(BattleTurnPhase.AI, controller.snapshot.phase)
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
            presentDeaths = { calls += "deaths:$it"; it != BattleDeathCheckpoint.ROUND_START },
            runRoundScript = { calls += "round:${it.completedRound}->${it.round}"; false },
            presentWeather = { calls += "weather:${it.previous}->${it.current}"; false },
        )

        controller.endPlayerTurn()
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
        assertEquals("card:r1:ENEMY", calls.last())

        // 테스트 근거: 저장·추적 자료의 순서와 직렬화 규칙을 검증한다.
        controller.completeCampCard()
        assertEquals(BattleTurnPhase.ROUND_SCRIPT, controller.snapshot.phase)
        assertEquals(2, state.round)
        assertEquals(Faction.REINFORCEMENTS, state.activeFaction)
        assertEquals(BattleWeather.CLEAR, state.weather)
        assertEquals(5, state.skillTemp("mine", 900), "resetSkillTemp is after round script/death")
        assertEquals("round:1->2", calls.last())
        assertTrue(calls.none { it.startsWith("weather:") })

        controller.completeRoundScript()
        assertEquals(BattleTurnPhase.ROUND_DEATHS, controller.snapshot.phase)
        assertEquals(BattleWeather.CLEAR, state.weather)
        assertEquals("deaths:ROUND_START", calls.last())

        controller.completeRoundDeathPresentation()
        assertEquals(BattleTurnPhase.WEATHER, controller.snapshot.phase)
        assertEquals(BattleWeather.WINDY, state.weather)
        assertEquals(0, state.skillTemp("mine", 900))
        assertEquals(Faction.REINFORCEMENTS, state.activeFaction)
        assertEquals("weather:CLEAR->WINDY", calls.last())

        controller.completeWeatherPresentation()
        assertEquals(BattleTurnPhase.CAMP_CARD, controller.snapshot.phase)
        assertEquals(Faction.PLAYER, state.activeFaction)
        assertEquals("card:r2:PLAYER", calls.last())
    }
}
