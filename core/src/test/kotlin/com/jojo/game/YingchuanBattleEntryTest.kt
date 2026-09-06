// Test
package com.jojo.game

import com.jojo.game.application.battle.Battle
import com.jojo.game.application.scenario.ScenarioStage

import com.jojo.game.domain.battle.*
import com.jojo.game.presentation.battle.bootstrap.BattleInitLayer

import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** YingchuanBattleEntryTest: YingchuanBattleEntry의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class YingchuanBattleEntryTest {
    @Test
    fun `R00 implicit source entry materializes Cao Cao before S00 createMine`() {
        val campaign = CampaignState().apply { joinedUnits += 0 }
        assertTrue(campaign.roster.battleRoster.isEmpty())

        assertTrue(campaign.roster.prepareImplicitSingleUnitBattle())

        assertEquals(listOf(0), campaign.roster.battleRoster)
        val stage = ScenarioStage(campaign)
        stage.createBattleUnits(
            ScenarioUnitFaction.MINE,
            listOf(mapOf("i" to 0, "idx" to 0, "x" to 11, "y" to 20, "dir" to 2)),
        )
        assertEquals(0, stage.battleUnits.getValue("MINE:0").characterId)
    }

    @Test
    fun `empty implicit roster reproduces missing player and defeat precondition`() {
        val campaign = CampaignState()
        assertFalse(campaign.roster.prepareImplicitSingleUnitBattle())
        val stage = ScenarioStage(campaign)
        stage.createBattleUnits(
            ScenarioUnitFaction.MINE,
            listOf(mapOf("i" to 0, "idx" to 0, "x" to 11, "y" to 20, "dir" to 2)),
        )
        assertTrue(stage.battleUnits.values.none { it.faction == ScenarioUnitFaction.MINE })
    }

    @Test
    fun `explicit BattleHall selection is never overwritten`() {
        val campaign = CampaignState().apply {
            joinedUnits += listOf(0, 157)
            roster.restoreBattleRoster(listOf(157, 0))
        }
        assertTrue(campaign.roster.prepareImplicitSingleUnitBattle())
        assertEquals(listOf(157, 0), campaign.roster.battleRoster)
    }

    @Test
    fun `S00 global data keeps twenty rounds and selects authored map index`() {
        val stage = ScenarioStage().apply { setBattleGlobalData(20, -2, -1, 0, 1, 1) }
        assertEquals(20, stage.battleMaxRounds)
        assertEquals(-2, stage.battleLevelOffset)
        assertEquals(0, stage.battleMapIndex)

        val battle = Battle(listOf(
            BattleUnit("mine:0", "조조", Faction.PLAYER, 7, 20),
            BattleUnit("enemy:0", "황건군", Faction.ENEMY, 9, 11),
        ), emptyList())
        battle.setMaxRounds(stage.battleMaxRounds)
        assertNull(battle.outcome())
    }

    @Test
    fun `S00 draw boundary removes the attached battle init layer`() {
        val stage = ScenarioStage()
        val init = BattleInitLayer()
        init.onCreate(0)
        assertTrue(init.view().attached)
        assertFalse(stage.battleDrawRequested)

        stage.drawBattle()
        if (stage.battleDrawRequested && init.view().attached) init.onDestroy()

        assertTrue(stage.battleDrawRequested)
        assertFalse(init.view().attached)
    }
}
