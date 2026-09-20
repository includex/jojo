// Test
package com.jojo.game
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import com.jojo.game.presentation.scenario.*
import com.jojo.game.verification.scenario.evidence.*

import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * 아래 SHA-256 값은 `RenderEventLog` 한 행의 모든 바이트를 묶어 둔다. 스키마에
 * `color` 항목이 더해지면서(색을 적지 않는 호출은 `"color":null`) 세 값이 한 번 바뀌었고,
 * `cc.LabelOutline` 색을 나르는 `outline` 항목이 더해지면서(`"outline":null`) 한 번 더 바뀌었다.
 * 그리는 내용이 달라진 것이 아니라 기록 항목이 늘어난 것이다.
 */
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
        assertEquals("ce203b7c34c309e79ca64d4eaa2207fd6f285b0c890b436aacb5e507e26590bf", sha256(json))
        assertTrue(json.lineSequence().first().contains("\"nodePath\":\"Canvas/Layer/bg\""))
        assertTrue(json.lineSequence().filter(String::isNotBlank).last().contains("\"text\":\"확인\""))
    }

    @Test fun `terrain traversal keeps its complete JSONL order and values`() {
        val json = RenderEventLog().also {
            ScenarioTerrainEvidenceRecorder().append(it, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.TERRAIN))
        }.jsonl()

        assertEquals(613, json.lineSequence().count { it.isNotBlank() })
        assertEquals("65558df77ade312e59cf8ee2319cd073cb57b9e5b3b54263825ef3f123c2e10c", sha256(json))
        assertTrue(json.lineSequence().first().contains("\"assetId\":\"Logo_9-1\""))
        assertTrue(json.lineSequence().filter(String::isNotBlank).last().contains("\"text\":\"확인\""))
    }

    @Test fun `treasure traversal keeps its complete JSONL order and values`() {
        val json = RenderEventLog().also {
            ScenarioTreasureEvidenceRecorder().append(it, ScenarioStaticHallEvidenceView(ScenarioStaticHallEvidenceKind.TREASURE))
        }.jsonl()

        assertEquals(258, json.lineSequence().count { it.isNotBlank() })
        assertEquals("9d02ccd9d6498a55d5e2b13d9be95d2ac116233c5c42e292f82482a7532cdf83", sha256(json))
        assertTrue(json.lineSequence().first().contains("\"nodePath\":\"Canvas/Layer/bg1\""))
        assertTrue(json.lineSequence().filter(String::isNotBlank).last().contains("\"visible\":false"))
    }

    /** digest: 이벤트 순서를 포함한 결정적 JSONL 계약의 모든 바이트가 유지되는지 확인한다. */
    private fun sha256(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray())
        .joinToString("") { "%02x".format(it) }

    /** JSON 문자열은 바이트 단위 증거이므로 포함 여부뿐 아니라 원본 순회 순서까지 검증한다. */
    private fun assertOrdered(json: String, vararg fragments: String) {
        fragments.fold(-1) { previous, fragment ->
            val next = json.indexOf(fragment, previous + 1)
            assertTrue(next > previous, "expected $fragment after byte $previous")
            next
        }
    }
}
