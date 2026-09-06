// Test
package com.jojo.game
import com.jojo.game.infrastructure.data.GameDataCatalog

import com.jojo.game.application.battle.Battle

import com.jojo.game.application.battle.BattleScenarioFactory
import com.jojo.game.application.scenario.ScenarioStage

import com.jojo.game.domain.battle.*

import com.jojo.game.presentation.battle.timeline.*
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.scenario.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertSame

/** BattleDefeatPresentationTest: BattleDefeatPresentation의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleDefeatPresentationTest {
    @Test
    fun `defeated unit leaves combat immediately but remains available for attack presentation`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0, skills = mapOf(92 to 0)),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, hitPoints = 10, maxHitPoints = 10),
            ),
            events = emptyList(),
        )

        val result = battle.combat.attack("attacker", "target", damage = 10) as TacticalActionResult.Attack

        assertEquals(true, result.defeated)
        assertFalse("target" in battle.units)
        assertEquals(10, assertNotNull(battle.presentation.presentationUnit("target")).maxHitPoints)
        assertEquals(0, battle.presentation.presentationUnit("target")!!.hitPoints)
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택 (SHOU_GONG_JI3)을 검증한다.
        battle.presentation.clearPresentationUnit("target")
        assertNull(battle.presentation.presentationUnit("target"))
    }

    @Test
    fun `trace presentation collection retains defeated node through death callbacks`() {
        val battle = Battle(
            units = listOf(
                BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0),
                BattleUnit("target", "대상", Faction.ENEMY, 1, 0, hitPoints = 10, maxHitPoints = 10),
            ),
            events = emptyList(),
        )

        battle.combat.attack("attacker", "target", damage = 10)

        assertFalse("target" in battle.units)
        assertTrue(battle.presentation.presentationUnits().any { it.id == "target" && it.hitPoints == 0 })
        battle.presentation.completeScriptedUnitHide("target")
        assertTrue(battle.presentation.presentationUnits().any { it.id == "target" && !it.visible })
    }

    @Test
    fun `ZDBHSW poison settlement can kill and retains victim for unitDeath`() {
        val victim = BattleUnit(
            "victim", "중독 유닛", Faction.PLAYER, 0, 0,
            hitPoints = 9, maxHitPoints = 100,
            statuses = linkedMapOf(BattleStatus.POISON to 2),
        )
        val battle = Battle(listOf(victim), emptyList(), enabledFeatures = 32)

        val settlement = battle.roundLifecycle.settleActiveCampEnd()

        assertEquals(9, settlement.changes.single { it.unitId == "victim" }.hitPointsBefore)
        assertEquals(0, settlement.changes.single { it.unitId == "victim" }.hitPointsAfter)
        assertFalse("victim" in battle.units)
        assertSame(victim, battle.presentation.pendingPresentationUnits().single { it.id == "victim" })
        assertEquals(listOf(victim), UnitDeathPresentation.sortedDying(battle.presentation.presentationUnits()))
    }

    @Test
    fun `legacy poison settlement preserves one HP when ZDBHSW is disabled`() {
        val victim = BattleUnit(
            "victim", "중독 유닛", Faction.PLAYER, 0, 0,
            hitPoints = 9, maxHitPoints = 100,
            statuses = linkedMapOf(BattleStatus.POISON to 2),
        )
        val battle = Battle(listOf(victim), emptyList(), enabledFeatures = 0)

        battle.roundLifecycle.settleActiveCampEnd()

        assertEquals(1, victim.hitPoints)
        assertSame(victim, battle.units["victim"])
    }

    @Test
    fun `scripted hide preserves unit object for a later source show`() {
        val unit = BattleUnit("mine-0", "조조", Faction.PLAYER, 1, 2)
        val battle = Battle(listOf(unit), emptyList())

        battle.presentation.completeScriptedUnitHide(unit.id)

        assertNotNull(battle.presentation.presentationUnit(unit.id))
        assertFalse(battle.presentation.presentationUnit(unit.id)!!.visible)
    }

    @Test
    fun `scripted battle factory wires original retreat text only to death message enabled units`() {
        val data = GameDataCatalog.load()
        val mine = ScenarioBattleUnit(0, 0, ScenarioUnitFaction.MINE, 1, 2)
        val enemy = ScenarioBattleUnit(0, 1, ScenarioUnitFaction.ENEMY, 3, 4)

        val battle = BattleScenarioFactory.fromScriptedUnits(listOf(mine, enemy), gameDataCatalog = data)

        assertEquals("하늘이 나를 돕지 않는 건가... 너무 분해서 못 참겠다!", battle.units.getValue("mine-0").retireMessage)
        assertTrue(battle.units.getValue("mine-0").deathMessageEnabled)
        assertEquals("맹덕, 미안해……", battle.units.getValue("enemy-0").retireMessage)
        assertFalse(battle.units.getValue("enemy-0").deathMessageEnabled)
    }

    @Test
    fun `retreatTxt toggles the source death message flag independently of visibility`() {
        val stage = ScenarioStage()
        stage.createBattleUnits(ScenarioUnitFaction.MINE, listOf(mapOf("id" to 0, "i" to 0, "x" to 1, "y" to 2)))

        stage.setUnitRetreatTextEnabled(0, false)

        assertFalse(stage.battleUnits.getValue("MINE:0").deathMessageEnabled)
        assertTrue(stage.unit(0).visible)
    }

    @Test
    fun `retained defeated unit can return to the active combat roster`() {
        val target = BattleUnit(
            "target", "대상", Faction.ENEMY, 1, 0, hitPoints = 10,
            magicPoints = 5, statuses = linkedMapOf(BattleStatus.POISON to 2),
            attributeLifts = linkedMapOf(BattleAttribute.ATTACK to -1),
            attributeLiftRounds = linkedMapOf(BattleAttribute.ATTACK to 2),
            retreatFlag = true, hasMoved = true,
        )
        val battle = Battle(
            listOf(BattleUnit("attacker", "공격자", Faction.PLAYER, 0, 0), target),
            emptyList(),
        )
        battle.combat.attack("attacker", "target", 10)

        val restored = battle.presentation.restorePresentationUnit("target")

        assertSame(target, restored)
        assertSame(target, battle.units["target"])
        assertTrue(battle.presentation.pendingPresentationUnits().none { it.id == "target" })
        assertEquals(10, target.hitPoints)
        assertEquals(5, target.magicPoints)
        assertFalse(target.retreatFlag)
        assertFalse(target.hasMoved)
        assertTrue(target.statuses.isEmpty())
        assertTrue(target.attributeLifts.isEmpty())
    }

    @Test
    fun `retreat count loads from and writes to persistent unit attribute 15`() {
        val campaign = CampaignState().also { it.setUnitAttribute(0, 15, 4) }
        val battle = BattleScenarioFactory.fromScriptedUnits(
            listOf(ScenarioBattleUnit(0, 0, ScenarioUnitFaction.MINE, 1, 2)),
            campaign = campaign,
        )
        val unit = battle.units.getValue("mine-0")

        battle.presentation.incrementUnitRetreat(unit)

        assertEquals(5, unit.retreatCount)
        assertEquals(5, campaign.unitAttribute(0, 15))
    }
}
