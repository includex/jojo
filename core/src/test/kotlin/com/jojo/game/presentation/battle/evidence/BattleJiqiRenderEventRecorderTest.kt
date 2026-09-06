// Battle
package com.jojo.game.presentation.battle.evidence

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 기기 목록 증거 기록기 검증: 캡처 행의 순서와 확률 값 배치를 고정한다. */
class BattleJiqiRenderEventRecorderTest {
    /** 기록 순서: 배경부터 고정 문구·왼쪽 확률·오른쪽 확률 순으로 기록한다. */
    @Test
    fun `jiqi recorder preserves stable draw order and rate slots`() {
        val lines = BattleJiqiRenderEventRecorder.jsonl(
            BattleJiqiRenderEventView(listOf(11, 22, 33, 44, 55, 66, 77, 88))
        ).trim().lines()

        assertEquals(20, lines.size)
        assertTrue(lines[0].contains("Canvas/Layer/ScrollView/view/content/map"))
        assertTrue(lines[1].contains("Canvas/Layer/Panel_cancel"))
        assertTrue(lines[2].contains("Canvas/Layer/bg\""))
        assertTrue(lines[3].contains("Canvas/Layer/bg/box3"))
        assertTrue(lines[4].contains("명중률: "))
        assertTrue(lines[8].contains("\"text\":\"11\""))
        assertTrue(lines[11].contains("\"text\":\"44\""))
        assertTrue(lines[12].contains("Canvas/Layer/bg/label7"))
        assertTrue(lines[12].contains("\"text\":\"88\""))
        assertTrue(lines[15].contains("마법 명중률: "))
        assertTrue(lines[16].contains("\"text\":\"55\""))
        assertTrue(lines.last().contains("마법 방어율: "))
        lines.forEachIndexed { index, line -> assertTrue(line.contains("\"sequence\":$index")) }
    }
}
