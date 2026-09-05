package com.jojo.game.verification.trace

import com.jojo.game.domain.battle.*


import com.jojo.game.domain.scenario.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FullBattleTraceEvidenceSessionTest {
    private fun session() = FullBattleTraceEvidenceSession(
        FullBattleTraceConfig(outputPath = "build/test-full-trace.json", driverIntervalSeconds = 0f),
        FullBattleTraceRecorder(FullBattleTraceConfig(outputPath = "build/test-full-trace.json"), SourceRandomStreams(1, 2)),
    )

    @Test
    fun `map callbacks drain before repeated snapshots and terminal completion waits three frames`() {
        val session = session()
        assertEquals(
            "[[7,3,2,1]]",
            session.mapSnapshot(listOf(FullBattleTraceMapObject(7, 3, 2, 1, true))).json,
        )
        assertEquals(
            "null",
            session.mapSnapshot(listOf(FullBattleTraceMapObject(7, 3, 2, 1, true))).json,
        )
        val calls = listOf(FullBattleTraceMapObjectsCall(true, 3, listOf(FullBattleTraceMapObjectCall(7, 2, 1))))
        assertEquals(
            listOf("transition:objects:1:3:7,2,1"),
            session.mapObjectCallObservations(calls),
        )
        assertTrue(session.mapObjectCallObservations(calls).isEmpty())

        repeat(2) { index -> session.drive(terminalSnapshot(index.toFloat())) }
        assertEquals(null, session.consumeFinishAfterFrame())
        session.drive(terminalSnapshot(2f))
        assertEquals("battle-end", session.consumeFinishAfterFrame())
    }

    @Test
    fun `fight serializer preserves prefab slot order and scalar schema`() {
        val fighter = FullBattleTraceFighter(12, true, 3, .5f, 1f, -1f, 2f, 3f, 1f, .75f, 4, false)
        val json = FullBattleTraceFightEvidence.json(
            FullBattleTraceFightSnapshot(
                1, 0, true, true, .25f, fighter, fighter,
                FullBattleTraceSpeech(true, "mine"), FullBattleTraceSpeech(false, "enemy"),
            )
        )
        assertTrue(json.contains("\"mineIndex\":1"))
        assertTrue(json.contains("\"backgrounds\":[[true,0.75],[true,0.25]]"))
        assertTrue(json.contains("\"units\":[[12,true,3,0.5,1,-1,2,3,1,0.75,4,false]"))
        assertTrue(json.endsWith("[[true,\"mine\"],[false,\"enemy\"]]}"))
    }

    private fun terminalSnapshot(elapsed: Float) = FullBattleTraceDriveSnapshot(
        elapsed = elapsed,
        outcome = BattleOutcome.PLAYER_VICTORY,
        scriptState = PlaybackState.COMPLETE,
        traceBarrierOpen = false,
        lossSceneActive = false,
        callbackPending = false,
        scriptEnded = true,
        endProcessStarted = true,
    )
}
