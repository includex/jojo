package com.jojo.game

import com.jojo.game.presentation.battle.overlay.BattleHudMissions
import kotlin.test.*

class BattleHudMissionsTest {
    @Test fun `authored summary is presentation only and absent mission falls back`() {
        val summaries = BattleHudMissions.parse("""
            version: 1
            missions:
              S_90:
                victory: "임충 탈출"
                defeat: "9턴 종료까지 미탈출"
              invalid: 3
        """.trimIndent())
        assertEquals("임충 탈출", summaries["S_90"]?.victory)
        assertEquals("9턴 종료까지 미탈출", summaries["S_90"]?.defeat)
        assertNull(summaries["S_other"])
        assertNull(summaries["invalid"])
        assertTrue(BattleHudMissions.parse("version: 1").isEmpty())
    }
}
