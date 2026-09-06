// Battle Combat Test
package com.jojo.game.presentation.battle.combat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 물리 전투 연출 계획기가 대상별 시각 수치와 다음 대기열 선택을 화면 상태 없이 계산하는지 검증한다. */
class BattlePhysicalPresentationPlannerTest {
    /** 대상 계획: 흡혈 뒤 반사·반동 피해와 자동 속성 효과 종료 시각을 기존 규칙대로 계산한다. */
    @Test
    fun `대상 피격 뒤 회복 반사 자동 효과를 계산한다`() {
        val plan = BattlePhysicalPresentationPlanner.target(
            BattlePhysicalPassTargetPlanInput(
                resolvedHarm = 20,
                damage = 20,
                mpShieldDamage = 3,
                attackerHealing = 8,
                retaliationDamage = 5,
                recoilDamage = 4,
                automaticPropertyPresent = true,
                automaticPropertyHpDelta = 7,
                automaticPropertyMpDelta = -2,
                targetHpBefore = 50,
                targetMpBefore = 10,
                targetMaxHp = 70,
                targetMaxMp = 12,
                attackerHpBefore = 40,
                attackerMaxHp = 45,
                reactionStartedAt = 2f,
                reactionDuration = .4f,
            ),
        )

        assertFalse(plan.guard)
        assertEquals(32, plan.reactionAction)
        assertEquals(2.4f, plan.reactionEndsAt)
        assertEquals(30, plan.targetHpAfterHarm)
        assertEquals(7, plan.targetMpAfterHarm)
        assertEquals(45, plan.attackerHpAfterHealing)
        assertEquals(36, plan.attackerHpAfterRetaliation)
        assertEquals(3.9f, plan.automaticEndsAt)
        assertEquals(37, plan.targetHpAfterAutomaticProperty)
        assertEquals(5, plan.targetMpAfterAutomaticProperty)
    }

    /** 대기열 선택: 추가타는 생존 대상에 우선하고, 없으면 반격을 선택하며 패스 종료 조건을 보존한다. */
    @Test
    fun `추가타 반격과 다음 패스 대기열을 선택한다`() {
        assertEquals(
            BattleFollowUpCounterDecision.FOLLOW_UP,
            BattlePhysicalPresentationPlanner.followUpOrCounter(BattleFollowUpCounterPlanInput(true, true, 1, false)),
        )
        assertEquals(
            BattleFollowUpCounterDecision.COUNTER,
            BattlePhysicalPresentationPlanner.followUpOrCounter(BattleFollowUpCounterPlanInput(true, true, 0, false)),
        )
        assertEquals(
            BattleFollowUpCounterDecision.NONE,
            BattlePhysicalPresentationPlanner.followUpOrCounter(BattleFollowUpCounterPlanInput(false, true, 1, true)),
        )
        assertTrue(BattlePhysicalPresentationPlanner.shouldQueuePhysicalPass(1, 2, false))
        assertTrue(BattlePhysicalPresentationPlanner.shouldQueuePhysicalPass(2, 2, true))
        assertFalse(BattlePhysicalPresentationPlanner.shouldQueuePhysicalPass(2, 2, false))
        assertTrue(BattlePhysicalPresentationPlanner.shouldQueueCounter(0, 2, 1))
        assertFalse(BattlePhysicalPresentationPlanner.shouldQueueCounter(2, 0, 0))
    }
}
