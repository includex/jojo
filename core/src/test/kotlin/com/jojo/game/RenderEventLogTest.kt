package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * class  `RenderEventLogTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
}
