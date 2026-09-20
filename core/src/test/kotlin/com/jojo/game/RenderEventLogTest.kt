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

    @Test
    fun `outline is its own schema field so a wrong LabelOutline can fail a gate`() {
        // `cc.LabelOutline`은 노드와 별개 부품이라 노드 색만 적으면 테두리가 틀려도 통과한다.
        val log = RenderEventLog()
        log.draw("content", "MsgBox4", "Canvas/Layer/bg0/label", "label", 0f, 0f, 1f, 1f,
            text = "모든 부대의 명령을 종료하시겠습니까?", color = "#936100", outline = "#ffe26e")
        log.draw("content", "MsgBox4", "Canvas/Layer/bg0", "tiled-sprite", 0f, 0f, 1f, 1f, color = "#ffffff")

        val rows = log.jsonl().lines().filter(String::isNotEmpty)
        assertTrue(rows[0].contains("\"outline\":\"#ffe26e\""))
        // 테두리가 없는 그리기는 null로 남아 비교에서 한쪽만 적힌 항목으로 빠진다.
        assertTrue(rows[1].contains("\"outline\":null"))
    }
}
