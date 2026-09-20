package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.campaign.CampaignExperienceResult
import com.jojo.game.presentation.shared.InfoBaseValueAnimation

/** Builds the MineUnitInfoLayer EXP animation against the unit's full experience scale. */
internal object SettlementExperienceAnimation {
    fun value(
        result: CampaignExperienceResult,
        experienceLimit: (level: Int) -> Int,
    ): InfoBaseValueAnimation.Value = InfoBaseValueAnimation.Value(
        index = 2,
        source = result.oldExperience,
        destination = result.oldExperience + result.gained,
        max = experienceLimit(result.level).coerceAtLeast(1),
    )
}
