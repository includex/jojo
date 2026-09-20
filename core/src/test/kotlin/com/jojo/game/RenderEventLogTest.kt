// Test
package com.jojo.game
import com.jojo.game.presentation.shared.evidence.RenderEventLog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** RenderEventLogTest: RenderEventLog의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class RenderEventLogTest {
    @Test
    fun `render events use deterministic sequence and escape JSONL text`() {
        val log = RenderEventLog(frame = 7)
        log.draw("content", "Layer", "Canvas/node", "text", 1.25f, 2f, 3f, 4f,
            assetId = "font:test", text = "가\n\"나\"")
        log.draw("overlay", "Layer", "Canvas/hidden", "shape", 0f, 0f, 0f, 0f, visible = false)

        val rows = log.jsonl().lines().filter(String::isNotEmpty)
        assertEquals(2, rows.size)
        assertTrue(rows[0].contains("\"sequence\":0,\"frame\":7"))
        assertTrue(rows[0].contains("\"x\":1.250"))
        assertTrue(rows[0].contains("\"text\":\"가\\n\\\"나\\\"\""))
        assertTrue(rows[1].contains("\"sequence\":1"))
        assertTrue(rows[1].contains("\"visible\":false"))
    }

    @Test
    fun `color is part of the schema and is null when a draw does not record it`() {
        // 색이 스키마에 없으면 검정 라벨과 흰 라벨이 같은 행으로 기록된다.
        // 그래서 비교기가 볼 수 있도록 색을 정식 항목으로 남긴다.
        val log = RenderEventLog()
        log.draw("content", "MenuLayer", "Canvas/label", "label", 0f, 0f, 1f, 1f,
            text = "턴 수", color = "#ffffffff")
        log.draw("content", "MenuLayer", "Canvas/plate", "sliced-sprite", 0f, 0f, 1f, 1f)

        val rows = log.jsonl().lines().filter(String::isNotEmpty)
        assertTrue(rows[0].contains("\"color\":\"#ffffffff\""))
        assertTrue(rows[1].contains("\"color\":null"))
    }
}
