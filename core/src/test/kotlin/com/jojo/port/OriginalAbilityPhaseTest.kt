package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class OriginalAbilityPhaseTest {
    @Test
    fun `battle profile uses source runtime ability phase thresholds`() {
        val profile = requireNotNull(OriginalGameData.load().battleProfile(unitId = 32, scriptLevel = 6))

        // Source Unit.wuWeiPhase: raw [39,36,38,37,50], posts bonus 3,
        // phase thresholds [127,45,35,25], level 7. Equipment is merged
        // later when the scenario creates its BattleUnit.
        assertEquals(60, profile.attack)
        assertEquals(57, profile.defense)
        assertEquals(59, profile.spirit)
        assertEquals(58, profile.critical)
        assertEquals(71, profile.morale)
    }
}
