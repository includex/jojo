package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
