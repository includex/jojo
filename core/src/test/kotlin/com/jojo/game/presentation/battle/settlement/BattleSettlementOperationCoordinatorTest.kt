// Battle Settlement Test
package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.settlement.BattleSettlementPlan
import com.jojo.game.domain.battle.settlement.CampSettlementStage
import com.jojo.game.domain.battle.settlement.SettlementAuthoredSubflowPlan
import com.jojo.game.domain.battle.settlement.SettlementGrowthStep
import com.jojo.game.domain.battle.settlement.SettlementGrowthGrant
import com.jojo.game.domain.battle.settlement.SettlementGrowthKind
import com.jojo.game.domain.battle.settlement.SettlementInfoDelta
import com.jojo.game.domain.battle.settlement.SettlementInfoKind
import com.jojo.game.domain.battle.settlement.SettlementInfoPanel
import com.jojo.game.domain.battle.settlement.SettlementUnitPlan
import com.jojo.game.domain.campaign.CampaignExperienceResult
import kotlin.test.Test
import kotlin.test.assertEquals

/** 정산 operation 조정자가 화면 포트의 동작·효과·안내 시간 규칙으로 local 정산 지속시간을 계산하는지 검증한다. */
class BattleSettlementOperationCoordinatorTest {
    /** local 지속시간: focus·연속 동작·meff·자동 닫힘 안내 시간을 기존 순서대로 합산한다. */
    @Test
    fun `local 정산 operation의 지속시간을 포트 값으로 계산한다`() {
        val coordinator = BattleSettlementOperationCoordinator()
        val duration = coordinator.localDuration(
            listOf(
                TurnSettlementOp.Focus("caster", .3f, forceCenter = true),
                TurnSettlementOp.Actions("caster", listOf(12, 7)),
                TurnSettlementOp.Meff(31, listOf("target")),
                TurnSettlementOp.Info2("짧은 안내"),
            ),
            testPort,
        )

        assertEquals(.3f + .2f + .4f + .6f + (5 * .04f + 1f), duration, .001f)
    }

    /**
     * 원본 `_jiesuan`은 `g_charinfo.index`를 한 번 돌며 유닛마다 상태창(case 5~9) ->
     * 기본 동작(case 10) -> 승격 연출(case 11~) 순서로 재생한다. 성장 연출이 상태창보다
     * 앞서 몰려 나오면 안 된다.
     */
    @Test
    fun `성장 연출은 해당 유닛 상태창 뒤에 온다`() {
        val unit = SettlementUnitPlan(
            "caster", Faction.PLAYER, Faction.PLAYER, Faction.PLAYER,
            SettlementInfoPanel.MINE, listOf(SettlementInfoDelta(SettlementInfoKind.HP, 10, 4)), emptyList(),
        )
        val plan = BattleSettlementPlan(
            CampSettlementStage.START_STATE, Faction.PLAYER, listOf(unit), emptyList(),
            authoredSubflows = listOf(
                SettlementAuthoredSubflowPlan.Growth(
                    "caster", emptyList(), listOf(SettlementGrowthStep.UnitLevelUpInfo),
                ),
            ),
        )

        assertEquals(
            listOf("Focus", "UnitInfo", "Default", "Info2", "Refresh"),
            BattleSettlementOperationCoordinator().operations(plan, testPort).map { it::class.simpleName },
        )
    }

    /** source g_charinfo.index interleaves a growth-only actor before a damaged target. */
    @Test
    fun `source unit order keeps growth only actor before damaged target`() {
        val target = SettlementUnitPlan(
            "target", Faction.ENEMY, Faction.ENEMY, Faction.PLAYER,
            SettlementInfoPanel.OTHER, listOf(SettlementInfoDelta(SettlementInfoKind.HP, 19, 0)), emptyList(),
        )
        val grant = SettlementGrowthGrant(
            SettlementGrowthKind.UNIT_EXP, 24, CampaignExperienceResult(24, 3, 30, false),
        )
        val plan = BattleSettlementPlan(
            CampSettlementStage.START_STATE, Faction.PLAYER, listOf(target), emptyList(),
            authoredSubflows = listOf(
                SettlementAuthoredSubflowPlan.Growth(
                    "caster", listOf(grant), listOf(SettlementGrowthStep.InfoValues(listOf(grant))),
                ),
            ),
        )

        val operations = BattleSettlementOperationCoordinator().operations(
            plan, testPort, mergeGrowthFor = setOf("caster"), sourceUnitOrder = listOf("caster", "target"),
        )

        assertEquals(
            listOf("caster", "target"),
            operations.filterIsInstance<TurnSettlementOp.UnitInfo>().map { it.plan.unitId },
        )
    }

    /**
     * 원본 `_jiesuan` `:6798`의 `R && et > 0` 게이트다. 격파한 FRIEND 공격자는 경험치만
     * 올라 상태창도 승격 연출도 없으므로 성장 흐름에서 `defaultAction`을 돌리지 않는다.
     * 완료 자세는 루프 끝(`:7009-7010`)의 일괄 `defaultAction`, 즉 정산 종료까지 미룬다.
     */
    @Test
    fun `격파한 공격자는 대상 상태창 전에 완료 자세를 적용하지 않는다`() {
        val grant = SettlementGrowthGrant(
            SettlementGrowthKind.UNIT_EXP, 36, CampaignExperienceResult(36, 3, 81, false),
        )
        val operations = BattleSettlementOperationCoordinator().operations(
            killedTargetPlan(grant), friendActorPort, sourceUnitOrder = listOf("actor", "target"),
        )

        assertEquals(
            listOf("Focus", "Focus", "Focus", "UnitInfo", "Default", "Refresh"),
            operations.map { it::class.simpleName },
        )
        assertEquals(
            listOf("target"),
            operations.filterIsInstance<TurnSettlementOp.Default>().map { it.unitId },
        )
        // 원본 `:6737-6739`: `O`가 비어 있지 않은 유닛은 상태창이 없어도 0.1초를 쉰다.
        assertEquals(
            listOf("actor" to 0f, "actor" to .1f, "target" to 0f),
            operations.filterIsInstance<TurnSettlementOp.Focus>().map { it.unitId to it.seconds },
        )
    }

    /** 살아남은 대상 경로는 그대로다: HP가 바뀐 유닛은 자기 상태창 직후 `case 10` Default를 받는다. */
    @Test
    fun `살아남은 대상의 상태창 뒤 기본 동작은 그대로다`() {
        val actor = SettlementUnitPlan(
            "actor", Faction.FRIEND, Faction.FRIEND, Faction.FRIEND,
            SettlementInfoPanel.OTHER, listOf(SettlementInfoDelta(SettlementInfoKind.HP, 119, 104)), emptyList(),
        )
        val target = SettlementUnitPlan(
            "target", Faction.ENEMY, Faction.ENEMY, Faction.ENEMY,
            SettlementInfoPanel.OTHER, listOf(SettlementInfoDelta(SettlementInfoKind.HP, 97, 70)), emptyList(),
        )
        val grant = SettlementGrowthGrant(
            SettlementGrowthKind.UNIT_EXP, 12, CampaignExperienceResult(12, 3, 57, false),
        )
        val plan = BattleSettlementPlan(
            CampSettlementStage.START_STATE, Faction.FRIEND, listOf(actor, target), emptyList(),
            authoredSubflows = listOf(
                SettlementAuthoredSubflowPlan.Growth(
                    "actor", listOf(grant),
                    listOf(SettlementGrowthStep.InfoValues(listOf(grant)), SettlementGrowthStep.DefaultAction),
                ),
            ),
        )

        val operations = BattleSettlementOperationCoordinator().operations(
            plan, friendActorPort, sourceUnitOrder = listOf("actor", "target"),
        )

        assertEquals(
            listOf("Focus", "UnitInfo", "Default", "Focus", "UnitInfo", "Default", "Refresh"),
            operations.map { it::class.simpleName },
        )
        assertEquals(
            listOf("actor", "target"),
            operations.filterIsInstance<TurnSettlementOp.Default>().map { it.unitId },
        )
    }

    /** MINE 유닛이 실제 승격 연출을 재생하면 원본 `case 26`의 `defaultAction`은 유지된다. */
    @Test
    fun `승격 연출을 재생한 MINE 유닛은 성장 흐름 기본 동작을 유지한다`() {
        val grant = SettlementGrowthGrant(
            SettlementGrowthKind.UNIT_EXP, 36, CampaignExperienceResult(36, 4, 5, true),
        )
        val plan = BattleSettlementPlan(
            CampSettlementStage.START_STATE, Faction.PLAYER, emptyList(), emptyList(),
            authoredSubflows = listOf(
                SettlementAuthoredSubflowPlan.Growth(
                    "caster", listOf(grant),
                    listOf(
                        SettlementGrowthStep.InfoValues(listOf(grant)),
                        SettlementGrowthStep.UnitLevelUpInfo,
                        SettlementGrowthStep.DefaultAction,
                    ),
                ),
            ),
        )

        val operations = BattleSettlementOperationCoordinator().operations(plan, testPort)

        assertEquals(
            listOf("caster"),
            operations.filterIsInstance<TurnSettlementOp.Default>().map { it.unitId },
        )
    }

    /** 격파 구간 계획: 행동자는 경험치만, 대상은 HP 39->0이다. */
    private fun killedTargetPlan(grant: SettlementGrowthGrant): BattleSettlementPlan {
        val target = SettlementUnitPlan(
            "target", Faction.ENEMY, Faction.ENEMY, Faction.ENEMY,
            SettlementInfoPanel.OTHER, listOf(SettlementInfoDelta(SettlementInfoKind.HP, 39, 0)), emptyList(),
        )
        return BattleSettlementPlan(
            CampSettlementStage.START_STATE, Faction.FRIEND, listOf(target), emptyList(),
            authoredSubflows = listOf(
                SettlementAuthoredSubflowPlan.Growth(
                    "actor", listOf(grant),
                    listOf(SettlementGrowthStep.InfoValues(listOf(grant)), SettlementGrowthStep.DefaultAction),
                ),
            ),
        )
    }

    /** 영천 3라운드 210 구간과 같은 FRIEND 행동자·ENEMY 대상 구성이다. */
    private val friendActorPort = object : BattleSettlementOperationPort {
        private val actor = BattleUnit("actor", "보병1", Faction.FRIEND, 10, 16, direction = 3)
        private val target = BattleUnit("target", "황건군", Faction.ENEMY, 9, 17, direction = 1)
        override fun unitsById() = mapOf(actor.id to actor, target.id to target)
        override fun presentationUnit(unitId: String) = unitsById()[unitId]
        override fun statusMeff(sourceStatusIndex: Int, meffSlot: Int): Int? = null
        override fun skillName(skillId: Int) = ""
        override fun magicName(magicId: Int): String? = null
        override fun namedMeff(name: String): Int? = null
        override fun actionDuration(actionId: Int, direction: Int) = .4f
        override fun meffDuration(effectId: Int) = .6f
        override fun autoCloseInfo2(text: String) = true
    }

    /** 정산 포트: duration 계산에 필요한 최소 유닛·효과·환경 정보를 고정한다. */
    private val testPort = object : BattleSettlementOperationPort {
        private val caster = BattleUnit("caster", "시전자", Faction.PLAYER, 0, 0, direction = 2)
        private val target = BattleUnit("target", "대상", Faction.ENEMY, 1, 0, direction = 0)
        override fun unitsById() = mapOf(caster.id to caster, target.id to target)
        override fun presentationUnit(unitId: String) = unitsById()[unitId]
        override fun statusMeff(sourceStatusIndex: Int, meffSlot: Int): Int? = null
        override fun skillName(skillId: Int) = ""
        override fun magicName(magicId: Int): String? = null
        override fun namedMeff(name: String): Int? = null
        override fun actionDuration(actionId: Int, direction: Int) = if (actionId == 12) .2f else .4f
        override fun meffDuration(effectId: Int) = .6f
        override fun autoCloseInfo2(text: String) = true
    }
}
