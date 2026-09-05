package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals

class BattleHarmBarTest {
    @Test
    fun `damage uses bar0 current and bar2 post effect with lower clamp`() {
        assertEquals(
            BattleHarmBar.View(bar0 = .30f, bar2 = 0f, amountText = "30", hitRateText = "75%"),
            BattleHarmBar.show(hp = 30, maxHp = 100, mp = 0, maxMp = 1, hpAdd = -80, hitRate = 75.8),
        )
    }

    @Test
    fun `healing uses bar2 current and bar1 post effect with upper clamp`() {
        assertEquals(
            BattleHarmBar.View(bar1 = 1f, bar2 = .70f, amountText = "30"),
            BattleHarmBar.show(hp = 70, maxHp = 100, mp = 0, maxMp = 1, hpAdd = 50),
        )
    }

    @Test
    fun `MP_ADD overwrites HP_ADD exactly like original info-key precedence`() {
        assertEquals(
            BattleHarmBar.View(bar1 = .70f, bar2 = .20f, amountText = "5"),
            BattleHarmBar.show(hp = 80, maxHp = 100, mp = 2, maxMp = 10, hpAdd = -70, mpAdd = 5),
        )
    }

    @Test
    fun `BattleUnit retains source preview until next target-info update`() {
        val unit = BattleUnit("u", "u", Faction.PLAYER, 0, 0, hitPoints = 40, maxHitPoints = 100)
        unit.showHarmBar(hpAdd = -15, hitRate = 88)
        assertEquals(BattleHarmBar.View(bar0 = .40f, bar2 = .25f, amountText = "15", hitRateText = "88%"), unit.harmBarPreview)
    }
}
