package com.jojo.game

import com.jojo.game.application.battle.BattleScenarioAssembler
import com.jojo.game.application.battle.BattleScenarioRequest
import com.jojo.game.domain.battle.BattleWeather
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.scenario.ScenarioBattleUnit
import com.jojo.game.domain.scenario.ScenarioUnitFaction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BattleScenarioFactoryDelegationTest {
    @Test
    fun `compatibility facade retains authored unit projection`() {
        val scripted = ScenarioBattleUnit(
            instanceId = 3,
            characterId = 77,
            faction = ScenarioUnitFaction.ENEMY,
            x = 5,
            y = 7,
            direction = 1,
            hidden = true,
        )

        val unit = BattleScenarioFactory.fromScriptedUnits(listOf(scripted)).units.values.single()

        assertEquals("enemy-3", unit.id)
        assertEquals(Faction.ENEMY, unit.faction)
        assertEquals(5 to 7, unit.tileX to unit.tileY)
        assertEquals(1, unit.direction)
        assertFalse(unit.visible)
    }

    @Test
    fun `application assembler owns tutorial and request runtime setup`() {
        val request = BattleScenarioRequest(
            units = listOf(ScenarioBattleUnit(2, 11, ScenarioUnitFaction.MINE, 1, 2)),
            blockedTiles = setOf(3 to 4),
            initialWeather = BattleWeather.HEAVY_RAIN,
            weatherSchedule = listOf(BattleWeather.CLEAR),
            weatherOffset = 1,
        )

        val battle = BattleScenarioAssembler.materialize(request)

        assertEquals(setOf(3 to 4), battle.journal.blockedTiles())
        assertEquals(BattleWeather.HEAVY_RAIN, battle.weather)
        assertEquals(listOf("cao-cao", "guard", "yellow-turban"),
            BattleScenarioAssembler.tutorialBattle().units.keys.toList())
    }
}
