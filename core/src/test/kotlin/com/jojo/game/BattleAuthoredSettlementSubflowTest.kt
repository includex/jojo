package com.jojo.game
import com.jojo.game.domain.campaign.*

import com.jojo.game.domain.campaign.CampaignEquipmentSlot

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `BattleAuthoredSettlementSubflowTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleAuthoredSettlementSubflowTest {
    @Test
    fun `local aura mutations remain separated in authored callback order`() {
        val caster = BattleUnit(
            "caster", "시전자", Faction.PLAYER, 0, 0,
            level = 20,
            skills = linkedMapOf(103 to 1, 208 to 10, 209 to 0, 210 to 3),
        )
        val target = BattleUnit(
            "target", "대상", Faction.PLAYER, 1, 0,
            hitPoints = 50, maxHitPoints = 100,
            magicPoints = 2, maxMagicPoints = 10,
            statuses = linkedMapOf(BattleStatus.PARALYSIS to 2),
        )
        val battle = Battle(listOf(caster, target), emptyList())

        val settlement = battle.settleActiveCampStart()
        assertTrue(settlement.subflowsCaptured)
        assertTrue(settlement.changes.none { it.unitId == target.id && BattleStatus.PARALYSIS in it.statusesAfter },
            "caster-local cleanse runs before the iterator reaches the following target")
        val auras = settlement.subflows.filterIsInstance<SettlementSubflow.LocalAura>()
        assertEquals(listOf(103, 208, 209, 210), auras.map { it.skillId })
        assertEquals(mapOf(BattleStatus.PARALYSIS to 2), auras[0].nestedChanges.single().statusesBefore)
        assertEquals(emptyMap(), auras[0].nestedChanges.single().statusesAfter)
        assertEquals(60, auras[1].nestedChanges.single().hitPointsAfter)
        assertEquals(5, auras[2].nestedChanges.single().magicPointsAfter)
        assertEquals(
            mapOf(BattleAttribute.ATTACK to 1, BattleAttribute.DEFENSE to 1),
            auras[3].nestedChanges.single().attributeLiftsAfter,
        )

        val plan = BattleSettlementPlanner.plan(settlement, battle.units) { state -> 100 + state.sourceStatusIndex }
        assertTrue(plan.sourceDataComplete)
        assertFalse(plan.fullyRepresented, "renderer must consume authoredSubflows before completing")
        val auraPlans = plan.authoredSubflows.filterIsInstance<SettlementAuthoredSubflowPlan.LocalAura>()
        assertEquals(listOf(103, 208, 209, 210), auraPlans.map { it.skillId })
        assertEquals(
            listOf(
                SettlementAuraStep.Focus(.3f), SettlementAuraStep.Sound(39),
                SettlementAuraStep.Info2(208), SettlementAuraStep.ActionFinished(30),
                SettlementAuraStep.PlayMeff("resume_hp", listOf(target.id)),
                SettlementAuraStep.NestedSettlement, SettlementAuraStep.DefaultAction,
            ),
            auraPlans[1].steps,
        )
        assertTrue(auraPlans.all { it.nestedSettlement.fullyRepresented })
    }

    @Test
    fun `restore growth retains unit and equipment level-up callback payloads`() {
        val unit = BattleUnit(
            "grow", "성장", Faction.PLAYER, 0, 0,
            skills = linkedMapOf(149 to 7, 150 to 5, 151 to 3),
            characterId = 42,
        )
        val weapon = CampaignEquipmentExperienceResult(
            42, CampaignEquipmentSlot.WEAPON, 10, 5,
            1, 2, 95, 0, 20, 25,
        )
        val armor = CampaignEquipmentExperienceResult(
            42, CampaignEquipmentSlot.ARMOR, 11, 3,
            1, 1, 10, 13, 15, 15,
        )
        val battle = Battle(
            listOf(unit), emptyList(),
            onRestoreUnitExperience = { _, amount ->
                RestoreGrowthResolution.Applied(
                    CampaignExperienceResult(amount, 2, 0, true, oldLevel = 1, oldExperience = 93),
                )
            },
            onRestoreEquipmentExperience = { _, _, slot ->
                RestoreGrowthResolution.Applied(if (slot == CampaignEquipmentSlot.WEAPON) weapon else armor)
            },
        )

        val settlement = battle.settleActiveCampEnd()
        val growth = settlement.subflows.single() as SettlementSubflow.Growth
        assertEquals(listOf(SettlementGrowthKind.UNIT_EXP, SettlementGrowthKind.WEAPON_EXP, SettlementGrowthKind.ARMOR_EXP),
            growth.grants.map { it.kind })
        assertTrue(growth.grants[0].requiresLevelUpPresentation)
        assertTrue(growth.grants[1].requiresItemUpgradeCallback)
        assertFalse(growth.grants[2].requiresItemUpgradeCallback)

        val plan = BattleSettlementPlanner.plan(settlement, battle.units) { null }
        assertTrue(plan.sourceDataComplete)
        assertFalse(plan.fullyRepresented, "growth callbacks cannot be skipped by the current renderer")
        val steps = (plan.authoredSubflows.single() as SettlementAuthoredSubflowPlan.Growth).steps
        assertTrue(SettlementGrowthStep.UnitLevelUpActionFinished in steps)
        assertTrue(steps.any { it is SettlementGrowthStep.ItemUpgradeCallback && it.result == weapon })
        assertFalse(steps.any { it is SettlementGrowthStep.ItemUpgradeCallback && it.result == armor })
    }
}
