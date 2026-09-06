// Battle Combat Test
package com.jojo.game.presentation.battle.combat

import com.jojo.game.domain.battle.BattleAttackSequence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 후속 물리 연출 계획기가 공격 종류·시간선·HP/MP 결과와 다음 예약 조건을 순수하게 계산하는지 검증한다. */
class BattleFollowUpPresentationPlannerTest {
    /** 시간선 계획: 지연 치명타 action과 hit·반응 시각, HP/MP 감소 결과를 보존한다. */
    @Test
    fun `후속 타격 시간선과 대상 수치를 계산한다`() {
        val plan = BattleFollowUpPresentationPlanner.plan(
            BattleFollowUpPresentationPlanInput(
                critical = true,
                attackDelay = true,
                animationStartedAt = 3f,
                hitOffset = .25f,
                reactionDuration = .4f,
                targetHpBefore = 30,
                harm = 40,
                targetMpBefore = 7,
                mpShieldDamage = 3,
            ),
        )

        assertEquals(BattleAttackSequence.HIT_ATTACK_DELAY, plan.sourceAction)
        assertEquals(3.25f, plan.hitAt)
        assertEquals(3.65f, plan.reactionEndsAt)
        assertEquals(0, plan.targetHpAfter)
        assertEquals(4, plan.targetMpAfter)
        assertTrue(plan.commitsHp(40))
        assertTrue(plan.commitsMp(3))
    }

    /** 다음 예약: 대상 생존과 피해 존재가 모두 충족될 때만 반격 또는 추가타를 이어서 예약한다. */
    @Test
    fun `다음 후속 타격 예약 조건을 계산한다`() {
        assertTrue(BattleFollowUpPresentationPlanner.shouldQueueNext(1, 0, 1))
        assertTrue(BattleFollowUpPresentationPlanner.shouldQueueNext(0, 1, 1))
        assertFalse(BattleFollowUpPresentationPlanner.shouldQueueNext(1, 0, 0))
        assertFalse(BattleFollowUpPresentationPlanner.shouldQueueNext(0, 0, 1))
    }
}
