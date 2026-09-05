package com.jojo.game

import com.jojo.game.application.scenario.battle.ScenarioStageBattleUnitFactory
import com.jojo.game.application.scenario.battle.ScenarioStageBattleUnitSelection

import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScenarioStageUnitRegistryTest {
    @Test
    fun `battle unit factory preserves roster fallback authored coordinates and AI targets`() {
        val campaign = CampaignState().also { it.roster.restoreBattleRoster(listOf(7, 8)) }
        val factory = ScenarioStageBattleUnitFactory()

        val creation = requireNotNull(
            factory.create(
                ScenarioUnitFaction.MINE,
                mapOf("i" to 1, "idx" to 0, "x" to 4, "y" to 0, "targetId" to 9, "targetX" to 2, "targetY" to 8),
                fallbackIndex = 0,
                campaign = campaign,
                enemyBlockStart = BattleSlotLayout.enemyStart,
            ),
        )

        assertEquals(7, creation.battleUnit.characterId)
        assertTrue(creation.battleUnit.authoredX)
        assertTrue(creation.battleUnit.authoredY)
        assertEquals(0, creation.battleUnit.y)
        assertEquals(1, creation.battleUnit.ai)
        assertEquals(9 to (2 to 8), creation.battleUnit.aiTargetId to (creation.battleUnit.aiTargetX to creation.battleUnit.aiTargetY))
        assertTrue(creation.initiallyVisible)
    }

    @Test
    fun `selection keeps reinforcement camps and aggregate selectors distinct`() {
        val mine = ScenarioBattleUnit(0, 1, ScenarioUnitFaction.MINE, 2, 2)
        val friend = ScenarioBattleUnit(0, 2, ScenarioUnitFaction.FRIEND, 2, 2)
        val enemy = ScenarioBattleUnit(0, 3, ScenarioUnitFaction.ENEMY, 2, 2)
        val reinforcement = ScenarioBattleUnit(1, 4, ScenarioUnitFaction.ENEMY, 2, 2, reinforcement = true)

        assertTrue(ScenarioStageBattleUnitSelection.matchesAiCamp(mine, 0))
        assertTrue(ScenarioStageBattleUnitSelection.matchesAiCamp(friend, 1))
        assertTrue(ScenarioStageBattleUnitSelection.matchesAiCamp(enemy, 2))
        assertTrue(ScenarioStageBattleUnitSelection.matchesAiCamp(reinforcement, 3))
        assertTrue(ScenarioStageBattleUnitSelection.matchesAiCamp(friend, 4))
        assertFalse(ScenarioStageBattleUnitSelection.matchesAiCamp(enemy, 4))
        assertTrue(ScenarioStageBattleUnitSelection.matchesAiCamp(reinforcement, 5))
        assertFalse(ScenarioStageBattleUnitSelection.inRectangle(enemy, 3, 2, 4, 3))
    }

    @Test
    fun `registry assigns distinct enemy blocks while live actor remains the first character entry`() {
        val registry = ScenarioStageUnitRegistry()
        val campaign = CampaignState()
        registry.createBattleUnits(ScenarioUnitFaction.ENEMY, listOf(mapOf("i" to 0, "id" to 99, "x" to 1, "y" to 2)), campaign)
        registry.createBattleUnits(ScenarioUnitFaction.ENEMY, listOf(mapOf("i" to 0, "id" to 99, "x" to 8, "y" to 9)), campaign)

        assertEquals(listOf("ENEMY:0", "ENEMY:80"), registry.battleUnits.keys.toList())
        assertEquals(1 to 2, registry.unit(99).let { it.x to it.y })

        registry.setBattleAi(2, 0, 0, 9, 10, ai = 4, targetX = 7, targetY = 8)
        assertEquals(listOf(4, 4), registry.battleUnits.values.map { it.ai })
        assertEquals(4, registry.unit(99).ai)
        assertEquals(7 to 8, registry.unit(99).aiTargetX to registry.unit(99).aiTargetY)
    }
}
