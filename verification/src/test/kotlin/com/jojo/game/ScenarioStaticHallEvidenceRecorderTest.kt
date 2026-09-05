package com.jojo.game

import com.jojo.game.presentation.scenario.*
import com.jojo.game.verification.scenario.evidence.*

import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScenarioStaticHallEvidenceRecorderTest {
    @Test fun `forces and helper source JSONL keep their authored byte ordering`() {
        val recorder = ScenarioStaticHallInfoEvidenceRecorder()
        val forces = RenderEventLog().also(recorder::appendForces).jsonl()
        val helper = RenderEventLog().also(recorder::appendHelper).jsonl()

        assertOrdered(
            forces,
            "\"nodePath\":\"Canvas/Layer/bg1\"",
            "\"text\":\"조조\"",
            "\"text\":\"무장명\"",
            "\"text\":\"폐쇄\"",
        )
        assertOrdered(
            helper,
            "\"nodePath\":\"Canvas/Layer/Logo_12-1\"",
            "\"text\":\"6\"",
            "\"text\":\" [단축키 설명\"",
            "\"text\":\"확인\"",
        )
    }

    @Test fun `property traversal keeps its complete JSONL order and values`() {
        val json = RenderEventLog().also {
            ScenarioPropertyEvidenceRecorder().append(it, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.PROPERTY))
        }.jsonl()

        assertEquals(54, json.lineSequence().count { it.isNotBlank() })
        assertEquals("0f5e01a557b3aba6481e55be8f989b8bc4de08d9ed78b909736732a130ce891e", sha256(json))
        assertTrue(json.lineSequence().first().contains("\"nodePath\":\"Canvas/Layer/bg\""))
        assertTrue(json.lineSequence().filter(String::isNotBlank).last().contains("\"text\":\"확인\""))
    }

    @Test fun `terrain traversal keeps its complete JSONL order and values`() {
        val json = RenderEventLog().also {
            ScenarioTerrainEvidenceRecorder().append(it, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.TERRAIN))
        }.jsonl()

        assertEquals(613, json.lineSequence().count { it.isNotBlank() })
        assertEquals("68a7dfca0c5ae9f6393acd51bddacc3f69b6c1fc214b9d79e530c324264fd3be", sha256(json))
        assertTrue(json.lineSequence().first().contains("\"assetId\":\"Logo_9-1\""))
        assertTrue(json.lineSequence().filter(String::isNotBlank).last().contains("\"text\":\"확인\""))
    }

    @Test fun `treasure traversal keeps its complete JSONL order and values`() {
        val json = RenderEventLog().also {
            ScenarioTreasureEvidenceRecorder().append(it, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.TREASURE))
        }.jsonl()

        assertEquals(258, json.lineSequence().count { it.isNotBlank() })
        assertEquals("da88cec39ba925e54c942a5db43904fcf3a44c7c8dfddbec16f252b65afa2bc6", sha256(json))
        assertTrue(json.lineSequence().first().contains("\"nodePath\":\"Canvas/Layer/bg1\""))
        assertTrue(json.lineSequence().filter(String::isNotBlank).last().contains("\"visible\":false"))
    }

    /** A digest pins every byte of the deterministic JSONL contract, including event order. */
    private fun sha256(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray())
        .joinToString("") { "%02x".format(it) }

    /** JSON strings are byte-level evidence; this guards source traversal order, not just membership. */
    private fun assertOrdered(json: String, vararg fragments: String) {
        fragments.fold(-1) { previous, fragment ->
            val next = json.indexOf(fragment, previous + 1)
            assertTrue(next > previous, "expected $fragment after byte $previous")
            next
        }
    }
}
