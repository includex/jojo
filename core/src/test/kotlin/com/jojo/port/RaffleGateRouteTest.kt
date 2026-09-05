package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RaffleGateRouteTest {
    @Test fun `actual hall setting caller remains setting when supportAd is unavailable`() {
        val route=RaffleGateRoute();route.openHallMenu(true);route.hallMenuButton(3,true);route.settingButton(8,true,0)
        val view=route.view();assertEquals(RaffleGateRoute.Layer.SETTING,view.layer);assertEquals(0,view.supportAdCode);assertFalse(view.raffleAttached)
        assertEquals(listOf("HallLayer menu TOUCH_END","HallMenuLayer button3 TOUCH_END","SettingLayer button13(tag8) TOUCH_END"),view.input)
    }
    @Test fun `supportAd code eight attaches raffle`() {
        val route=RaffleGateRoute();route.openHallMenu(true);route.hallMenuButton(3,true);route.settingButton(8,true,8)
        assertTrue(route.view().raffleAttached);assertEquals(RaffleGateRoute.Layer.RAFFLE,route.view().layer)
    }
    @Test fun `wrong phase or touch does not invoke supportAd`() {
        val route=RaffleGateRoute();route.settingButton(8,true,8);route.openHallMenu(false);route.hallMenuButton(3,true)
        assertEquals(RaffleGateRoute.Layer.HALL,route.view().layer);assertEquals(null,route.view().supportAdCode)
    }
    @Test fun `diagnostic renderer records stable setting without raffle`() {
        val lines=RaffleGateRenderEvents.jsonl().lineSequence().filter(String::isNotEmpty).toList()
        assertEquals(60,lines.size);assertTrue(lines.first().contains("hall-raffle-gated-stable"));assertFalse(lines.any{it.contains("RaffleLayer")})
    }
}
