package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AchievementsRouteTest {
    @Test fun `setting tag7 requires visible tools and saved rewards`() {
        val empty = SettingAchievementsRoute(emptyMap())
        assertTrue(empty.touch(7, true).isEmpty())
        empty.exposeToolsPanel()
        assertEquals(
            listOf(SettingAchievementsRoute.Effect.Toast("저장된 게임에서 다시 확인해 주세요./현재 업적이 없습니다.")),
            empty.touch(7, true),
        )

        val saved = SettingAchievementsRoute(mapOf(3 to StageAchievement(4, 5, 60, 7)))
        saved.exposeToolsPanel()
        assertTrue(saved.touch(7, false).isEmpty())
        assertEquals(listOf(SettingAchievementsRoute.Effect.OpenAchievements), saved.touch(7, true))
        assertFalse(saved.toolsPanelVisible)
    }

    @Test fun `rows use source battle name and low three star bits`() {
        val flow = AchievementsFlow(
            linkedMapOf(
                3 to StageAchievement(round = 4, level = 5, gold = 60, stars = 5),
                8 to StageAchievement(round = 9, level = 2, gold = 0, stars = 2),
            ),
        ) { id -> mapOf(3 to "영천전투", 8 to "낙양전투").getValue(id) }
        assertEquals(
            listOf(
                AchievementRow("4 영천전투", "Lv:5 Gold:60", "★  ☆  ★"),
                AchievementRow("9 낙양전투", "Lv:2 Gold:0", "☆  ★  ☆"),
            ),
            flow.rows,
        )
    }

    @Test fun `only button0 touch end closes`() {
        val flow = AchievementsFlow(emptyMap()) { "" }
        assertFalse(flow.touch(0, false))
        assertFalse(flow.touch(1, true))
        assertTrue(flow.touch(0, true))
    }
}
