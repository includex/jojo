// Battle Trace Test
package com.jojo.game.presentation.battle.trace

import com.jojo.game.application.runtime.BattleTraceRuntimeConfig
import com.jojo.game.application.runtime.RuntimeBattleCompletion
import com.jojo.game.application.runtime.RuntimeBattleFrameSnapshot
import com.jojo.game.application.runtime.RuntimeBattleObserver
import com.jojo.game.application.runtime.RuntimeBattleTraceDialogueInput
import com.jojo.game.application.runtime.RuntimeBattleTraceDriverInput
import com.jojo.game.application.runtime.RuntimeBattleTraceFrameInput
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

    /** 최소 프레임 입력: coordinator가 세션 순번만 추가하면 되는 완전한 trace 원시값을 만든다. */
    private fun frameInput(elapsed: Float, observation: String) = RuntimeBattleTraceFrameInput(
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
        mapObjectRevision = 0,
        mapObjectsJson = "null",
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
