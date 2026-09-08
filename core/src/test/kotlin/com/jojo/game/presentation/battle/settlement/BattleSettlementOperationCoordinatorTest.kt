// Battle Settlement Test
package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.settlement.BattleSettlementPlan
import com.jojo.game.domain.battle.settlement.CampSettlementStage
import com.jojo.game.domain.battle.settlement.SettlementAuthoredSubflowPlan
import com.jojo.game.domain.battle.settlement.SettlementGrowthStep
import com.jojo.game.domain.battle.settlement.SettlementInfoDelta
import com.jojo.game.domain.battle.settlement.SettlementInfoKind
import com.jojo.game.domain.battle.settlement.SettlementInfoPanel
import com.jojo.game.domain.battle.settlement.SettlementUnitPlan
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

    /** 정산 포트: duration 계산에 필요한 최소 유닛·효과·환경 정보를 고정한다. */
    private val testPort = object : BattleSettlementOperationPort {
        private val caster = BattleUnit("caster", "시전자", Faction.PLAYER, 0, 0, direction = 2)
        override fun unitsById() = mapOf(caster.id to caster)
        override fun presentationUnit(unitId: String) = caster.takeIf { it.id == unitId }
        override fun statusMeff(sourceStatusIndex: Int, meffSlot: Int): Int? = null
        override fun skillName(skillId: Int) = ""
        override fun magicName(magicId: Int): String? = null
        override fun namedMeff(name: String): Int? = null
        override fun actionDuration(actionId: Int, direction: Int) = if (actionId == 12) .2f else .4f
        override fun meffDuration(effectId: Int) = .6f
        override fun autoCloseInfo2(text: String) = true
    }
}
