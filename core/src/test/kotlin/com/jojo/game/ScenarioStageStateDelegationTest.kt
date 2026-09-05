package com.jojo.game

import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.domain.scenario.ScenarioMapObject
import kotlin.test.Test
import kotlin.test.assertEquals

class ScenarioStageStateDelegationTest {
    @Test
    fun `stage preserves weather and world mutation through their owners`() {
        val stage = ScenarioStage()

        stage.setBattleGlobalData(maxRounds = 12, levelOffset = 0, weatherType = 7, weatherOffset = 0)
        assertEquals(BattleWeather.HEAVY_RAIN, stage.initialBattleWeather())

        stage.setMapObjects(enabled = true, terrainId = 27, positions = listOf(listOf(4, 6, 8)))
        assertEquals(ScenarioMapObject(6, 8, 4, 27, true), stage.mapObjects.getValue(6 to 8))
        assertEquals(1, stage.mapObjectsCalls.size)
    }
}
