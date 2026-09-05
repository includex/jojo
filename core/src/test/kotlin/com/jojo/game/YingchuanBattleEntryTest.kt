package com.jojo.game
import com.jojo.game.domain.battle.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * class  `YingchuanBattleEntryTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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
