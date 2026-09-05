package com.jojo.game
import com.jojo.game.domain.campaign.*
import com.jojo.game.domain.battle.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertSame

/**
 * class  `BattleDefeatPresentationTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

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

        val result = battle.attack("attacker", "target", damage = 10) as TacticalActionResult.Attack

        assertEquals(true, result.defeated)
        assertFalse("target" in battle.units)
        assertEquals(10, assertNotNull(battle.presentationUnit("target")).maxHitPoints)
        assertEquals(0, battle.presentationUnit("target")!!.hitPoints)
        // unitDeath removes this retained node only after its authored
        // SHOU_GONG_JI3/death animation completes.
        battle.clearPresentationUnit("target")
        assertNull(battle.presentationUnit("target"))
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

        battle.attack("attacker", "target", damage = 10)

        assertFalse("target" in battle.units)
        assertTrue(battle.presentationUnits().any { it.id == "target" && it.hitPoints == 0 })
        battle.completeScriptedUnitHide("target")
        assertTrue(battle.presentationUnits().any { it.id == "target" && !it.visible })
    }

    @Test
    fun `ZDBHSW poison settlement can kill and retains victim for unitDeath`() {
        val victim = BattleUnit(
            "victim", "중독 유닛", Faction.PLAYER, 0, 0,
            hitPoints = 9, maxHitPoints = 100,
            statuses = linkedMapOf(BattleStatus.POISON to 2),
        )
        val battle = Battle(listOf(victim), emptyList(), enabledFeatures = 32)

        val settlement = battle.settleActiveCampEnd()

        assertEquals(9, settlement.changes.single { it.unitId == "victim" }.hitPointsBefore)
        assertEquals(0, settlement.changes.single { it.unitId == "victim" }.hitPointsAfter)
        assertFalse("victim" in battle.units)
        assertSame(victim, battle.pendingPresentationUnits().single { it.id == "victim" })
        assertEquals(listOf(victim), UnitDeathPresentation.sortedDying(battle.presentationUnits()))
    }

    @Test
    fun `legacy poison settlement preserves one HP when ZDBHSW is disabled`() {
        val victim = BattleUnit(
            "victim", "중독 유닛", Faction.PLAYER, 0, 0,
            hitPoints = 9, maxHitPoints = 100,
            statuses = linkedMapOf(BattleStatus.POISON to 2),
        )
        val battle = Battle(listOf(victim), emptyList(), enabledFeatures = 0)

        battle.settleActiveCampEnd()

        assertEquals(1, victim.hitPoints)
        assertSame(victim, battle.units["victim"])
    }

    @Test
    fun `scripted hide preserves unit object for a later source show`() {
        val unit = BattleUnit("mine-0", "조조", Faction.PLAYER, 1, 2)
        val battle = Battle(listOf(unit), emptyList())

        battle.completeScriptedUnitHide(unit.id)

        assertNotNull(battle.presentationUnit(unit.id))
        assertFalse(battle.presentationUnit(unit.id)!!.visible)
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
        battle.attack("attacker", "target", 10)

        val restored = battle.restorePresentationUnit("target")

        assertSame(target, restored)
        assertSame(target, battle.units["target"])
        assertTrue(battle.pendingPresentationUnits().none { it.id == "target" })
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

        battle.incrementUnitRetreat(unit)

        assertEquals(5, unit.retreatCount)
        assertEquals(5, campaign.unitAttribute(0, 15))
    }
}
