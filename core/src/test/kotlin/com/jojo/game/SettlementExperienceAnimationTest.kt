package com.jojo.game

import com.jojo.game.domain.campaign.CampaignExperienceResult
import com.jojo.game.presentation.battle.settlement.SettlementExperienceAnimation
import kotlin.test.Test
import kotlin.test.assertEquals

class SettlementExperienceAnimationTest {
    @Test
    fun `experience bar uses the level limit rather than the awarded destination as its scale`() {
        val value = SettlementExperienceAnimation.value(
            CampaignExperienceResult(
                gained = 24,
                level = 3,
                experience = 30,
                leveledUp = false,
                oldLevel = 3,
                oldExperience = 6,
            ),
            experienceLimit = { 100 },
        )

        assertEquals(6, value.source)
        assertEquals(30, value.destination)
        assertEquals(100, value.max)
        assertEquals(.06f, value.source.toFloat() / value.max)
    }

    @Test
    fun `level crossing uses the resulting unit level scale read by MineUnitInfoLayer`() {
        val requestedLevels = mutableListOf<Int>()
        val value = SettlementExperienceAnimation.value(
            CampaignExperienceResult(
                gained = 15,
                level = 4,
                experience = 0,
                leveledUp = true,
                oldLevel = 3,
                oldExperience = 90,
            ),
            experienceLimit = { level -> requestedLevels += level; if (level == 3) 100 else 120 },
        )

        assertEquals(listOf(4), requestedLevels)
        assertEquals(120, value.max)
    }
}
