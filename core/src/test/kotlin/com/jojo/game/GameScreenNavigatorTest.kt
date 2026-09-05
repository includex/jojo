package com.jojo.game

import com.jojo.game.application.navigation.CampaignRestoreDestination
import com.jojo.game.application.navigation.campaignRestoreDestination
import kotlin.test.Test
import kotlin.test.assertEquals

class GameScreenNavigatorTest {
    @Test fun `restored save route preserves source destination semantics`() {
        assertEquals(CampaignRestoreDestination.BATTLE, campaignRestoreDestination(LoadGameLayer.RestoreRoute.BATTLE))
        assertEquals(CampaignRestoreDestination.HALL, campaignRestoreDestination(LoadGameLayer.RestoreRoute.HALL))
        assertEquals(CampaignRestoreDestination.HALL_AFTER_BATTLE, campaignRestoreDestination(LoadGameLayer.RestoreRoute.HALL_AFTER_BATTLE))
    }
}
