package com.jojo.game

import com.jojo.game.application.battle.Battle

import com.jojo.game.domain.battle.*


import kotlin.test.Test
import kotlin.test.assertEquals

class BattleConfigurationTest {
    @Test
    fun `Battle constructor keeps environment settings in configuration-backed behavior`() {
        val battle = Battle(
            units = emptyList(),
            events = emptyList(),
            initialWeather = BattleWeather.CLOUDY,
            enabledFeatures = 32,
            weatherSchedule = listOf(BattleWeather.WINDY),
            weatherOffset = 0,
        )

        assertEquals(32, battle.enabledFeatureMask())
        assertEquals(BattleWeather.CLOUDY, battle.weather)
        assertEquals(1, battle.round)
        assertEquals(Faction.PLAYER, battle.activeFaction)
        assertEquals(BattleWeather.WINDY, battle.roundLifecycle.applyScheduledWeather().current)
    }

    @Test
    fun `legacy named constructor arguments remain accepted`() {
        val battle = Battle(
            units = emptyList(),
            events = emptyList(),
            terrainMagicFlags = mapOf(3 to 7),
            terrainResumeRates = mapOf(3 to 20),
            terrainResumeMp = mapOf(3 to 10),
            directDestinationOffsets = listOf(1 to 0),
            movementOffsets = setOf(1 to 0),
            infantryOffsets = setOf(1 to 0),
        )

        assertEquals(0, battle.enabledFeatureMask())
        assertEquals(emptyList(), battle.traceActions)
    }
}
