// Battle Trace Test
package com.jojo.game.presentation.battle.trace

import com.jojo.game.application.runtime.BattleTraceRuntimeConfig
import com.jojo.game.application.runtime.RuntimeBattleCompletion
import com.jojo.game.application.runtime.RuntimeBattleFrameSnapshot
import com.jojo.game.application.runtime.RuntimeBattleObserver
import com.jojo.game.application.runtime.RuntimeBattleTraceDialogueInput
import com.jojo.game.application.runtime.RuntimeBattleTraceDriverInput
import com.jojo.game.application.runtime.RuntimeBattleTraceFrameInput
import com.jojo.game.application.scenario.ScenarioStageWorldAccess
import com.jojo.game.application.scenario.ScenarioStageWorldState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/** 런타임 trace 조정자가 화면에서 전달한 불변 프레임을 기존 세션 형식으로 기록하는지 검증한다. */
class BattleRuntimeTraceCoordinatorTest {
    /** 프레임 기록: 세션의 순번을 입력에 부여하고 대화·관찰값을 변경 없이 관찰기에 전달한다. */
    @Test
    fun `불변 프레임 입력을 세션 순번으로 기록한다`() {
        var captured: RuntimeBattleFrameSnapshot? = null
        val coordinator = BattleRuntimeTraceCoordinator(
            BattleTraceRuntimeConfig(),
            object : RuntimeBattleObserver {
                override fun onFrame(snapshot: RuntimeBattleFrameSnapshot) {
                    captured = snapshot
                }

                override fun onCompleted(completion: RuntimeBattleCompletion) = Unit
            },
        )

        coordinator.recordFrame(frameInput(elapsed = 12.5f, observation = "transition:test"), advanceFrame = true)

        val trace = assertNotNull(captured?.traceView)
        assertEquals(1L, trace.frame)
        assertEquals(12.5f, trace.elapsed)
        assertEquals("대사", trace.dialogueText)
        assertEquals("transition:test", trace.observation)
    }

    /**
     * 지도 객체 기록 배선: 대본이 stage에 남긴 불(stage.fires)과 객체(stage.mapObjects)를 실제로 읽어
     * 원본 gate 행 `[TYPE, TERRAIN, X, Y]`로 기록된 프레임까지 이어지는지 확인한다.
     * 관측을 멈추거나 상수를 넣으면 trace에 남는 mapObjects가 그대로 비므로 이 검사가 깨진다.
     */
    @Test
    fun `대본이 남긴 불과 객체를 프레임의 지도 행으로 기록한다`() {
        var captured: RuntimeBattleFrameSnapshot? = null
        val coordinator = BattleRuntimeTraceCoordinator(
            BattleTraceRuntimeConfig(),
            object : RuntimeBattleObserver {
                override fun onFrame(snapshot: RuntimeBattleFrameSnapshot) {
                    captured = snapshot
                }

                override fun onCompleted(completion: RuntimeBattleCompletion) = Unit
            },
        )
        val stage = ScenarioStageWorldState()

        coordinator.recordFrame(frameInput(coordinator, stage, elapsed = 1f), advanceFrame = true)
        assertEquals(1, assertNotNull(captured?.traceView).mapObjectRevision)
        assertEquals("[]", assertNotNull(captured?.traceView).mapObjectsJson)

        // 원본 stage.setFires는 setObject2(TYPE=0, TERRAIN.HUO)를 칸마다 한 번씩 부른다.
        stage.setFires(true, listOf(listOf(7, 5), listOf(6, 4)))
        stage.setMapObjects(true, 13, listOf(listOf(4, 2, 1)))
        coordinator.recordFrame(frameInput(coordinator, stage, elapsed = 2f), advanceFrame = true)
        assertEquals(2, assertNotNull(captured?.traceView).mapObjectRevision)
        assertEquals(
            "[[4,13,2,1],[0,26,6,4],[0,26,7,5]]",
            assertNotNull(captured?.traceView).mapObjectsJson,
        )

        // 변화가 없는 프레임은 원본과 같이 revision을 유지한 채 null을 남긴다.
        coordinator.recordFrame(frameInput(coordinator, stage, elapsed = 3f), advanceFrame = true)
        assertEquals(2, assertNotNull(captured?.traceView).mapObjectRevision)
        assertEquals("null", assertNotNull(captured?.traceView).mapObjectsJson)

        // 꺼진 칸은 원본에서 gate 칸 자체가 비므로 행에서 빠진다.
        stage.setFire(false, 6, 4)
        coordinator.recordFrame(frameInput(coordinator, stage, elapsed = 4f), advanceFrame = true)
        assertEquals(3, assertNotNull(captured?.traceView).mapObjectRevision)
        assertEquals("[[4,13,2,1],[0,26,7,5]]", assertNotNull(captured?.traceView).mapObjectsJson)
    }

    /** 최소 프레임 입력: coordinator가 세션 순번만 추가하면 되는 완전한 trace 원시값을 만든다. */
    private fun frameInput(elapsed: Float, observation: String) =
        frameInput(null, null, elapsed, observation)

    /** 지도 행 포함 프레임 입력: 화면이 하듯 coordinator에게 stage를 읽혀 지도 행을 실은 원시값을 만든다. */
    private fun frameInput(
        coordinator: BattleRuntimeTraceCoordinator?,
        stage: ScenarioStageWorldAccess?,
        elapsed: Float,
        observation: String? = null,
    ): RuntimeBattleTraceFrameInput {
        val mapObjects = if (coordinator != null && stage != null) coordinator.observeMapObjects(stage) else null
        return RuntimeBattleTraceFrameInput(
            frame = 0L,
            elapsed = elapsed,
            delta = .25f,
            round = 2,
            camp = 0,
            maxRounds = 20,
            playerCount = 1,
            friendCount = 0,
            enemyCount = 1,
            paused = false,
            ended = false,
            collocation = false,
            dialogue = RuntimeBattleTraceDialogueInput(true, 4L, "원문", "unit-1", "대사"),
            phase = "PLAYER",
            script = "DIALOGUE",
            bootstrapBusy = emptyList(),
            cameraX = 10f,
            cameraY = 20f,
            mapObjectRevision = mapObjects?.revision ?: 0,
            mapObjectsJson = mapObjects?.json ?: "null",
            fightJson = "null",
            aiPresentation = null,
            actions = emptyList(),
            units = emptyList(),
            driver = RuntimeBattleTraceDriverInput(null, "NONE", null, null, "", "NONE"),
            observation = observation,
            scriptEnded = false,
            scriptedOutcome = null,
            resultFlow = "NONE",
            modalKind = null,
            pendingScriptPasses = 0,
            pendingAiDeathPass = 0,
            postActionDeaths = false,
            pendingAiResolution = false,
            activeAiCamp = null,
            roundLayer = false,
            turnSettlement = false,
            combatPresentation = false,
        )
    }
}
