package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import java.nio.file.Files
import java.util.zip.GZIPInputStream

class FullBattleTraceRecorderTest {
    @Test fun `battle deadline does not cut off an authored result scene`() {
        val deadline = FullBattleTraceDeadline(maxSimulationSeconds = 100f, resultSceneGraceSeconds = 20f)

        assertEquals(null, deadline.timeoutReason(elapsed = 99f, hasOutcome = false))
        assertEquals(null, deadline.timeoutReason(elapsed = 100f, hasOutcome = true))
        assertEquals(null, deadline.timeoutReason(elapsed = 119.99f, hasOutcome = true))
        assertEquals("result-scene-timeout", deadline.timeoutReason(elapsed = 120f, hasOutcome = true))
    }

    @Test fun `battle deadline still stops tactical play without an outcome`() {
        val deadline = FullBattleTraceDeadline(maxSimulationSeconds = 100f)

        assertEquals("timeout", deadline.timeoutReason(elapsed = 100f, hasOutcome = false))
    }

    @Test fun `callback observation and following render share one frame id`() {
        val recorder = FullBattleTraceRecorder(
            FullBattleTraceConfig(outputPath = "unused.json"),
            SourceRandomStreams(toolSeed = 1000, mathSeed = 1),
        )

        val callbackFrame = recorder.upcomingFrame()
        recorder.addFrame("{\"f\":$callbackFrame,\"observation\":\"transition:camera\"}")
        val renderFrame = recorder.nextFrame(.133f)
        recorder.addFrame("{\"f\":$renderFrame}")
        assertEquals(1L, callbackFrame)
        assertEquals(callbackFrame, renderFrame)
        assertEquals(2L, recorder.upcomingFrame())
        assertEquals(1L, recorder.frameNumber)
        assertEquals(2L, recorder.recordedRowCount)
    }

    @Test fun `requested scenario is persisted in trace config`() {
        val output = Files.createTempFile("jojo-full-battle-", ".json")
        try {
            val recorder = FullBattleTraceRecorder(
                FullBattleTraceConfig(outputPath = output.toString(), scenario = "S_57"),
                SourceRandomStreams(toolSeed = 1000, mathSeed = 1),
            )
            recorder.write("timeout", "{\"frameCount\":0}")
            assertContains(Files.readString(output), "\"scenario\":\"S_57\"")
        } finally {
            Files.deleteIfExists(output)
        }
    }

    @Test fun `large traces spill frames without changing JSON order`() {
        val output = Files.createTempFile("jojo-full-battle-spool-", ".json")
        try {
            val recorder = FullBattleTraceRecorder(
                FullBattleTraceConfig(outputPath = output.toString(), scenario = "S_18"),
                SourceRandomStreams(toolSeed = 1000, mathSeed = 1),
                frameMemoryLimitBytes = 1,
            )
            recorder.addFrame("{\"f\":1}")
            recorder.addFrame("{\"f\":2}")
            recorder.write("timeout", "{\"frameCount\":2}")

            val json = Files.readString(output)
            assertContains(json, "\"frames\":[{\"f\":1},{\"f\":2}]")
            assertContains(json, "\"summary\":{\"frameCount\":2}")
            assertEquals(2L, recorder.recordedRowCount)
        } finally {
            Files.deleteIfExists(output)
        }
    }

    @Test fun `gzip output streams the complete trace schema`() {
        val directory = Files.createTempDirectory("jojo-full-battle-gzip-")
        val output = directory.resolve("S_18.json.gz")
        try {
            val recorder = FullBattleTraceRecorder(
                FullBattleTraceConfig(outputPath = output.toString(), scenario = "S_18"),
                SourceRandomStreams(toolSeed = 1000, mathSeed = 1),
                frameMemoryLimitBytes = 1,
            )
            recorder.addFrame("{\"f\":1}")
            recorder.write("battle-end", "{\"frameCount\":1,\"end\":true}")

            val json = GZIPInputStream(Files.newInputStream(output)).bufferedReader().use { it.readText() }
            assertContains(json, "\"reason\":\"battle-end\"")
            assertContains(json, "\"frames\":[{\"f\":1}]")
            assertContains(json, "\"summary\":{\"frameCount\":1,\"end\":true}")
        } finally {
            Files.deleteIfExists(output)
            Files.deleteIfExists(directory)
        }
    }
}
