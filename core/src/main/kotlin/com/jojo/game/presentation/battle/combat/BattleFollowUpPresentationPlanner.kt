// Battle Combat
package com.jojo.game.presentation.battle.combat

import com.jojo.game.domain.battle.BattleAttackSequence

/** 후속 물리 연출 입력: 추가타·반격의 공격 시각, 대상 수치, 반응 지속시간을 화면 상태 없이 고정한다. */
internal data class BattleFollowUpPresentationPlanInput(
    val critical: Boolean,
    val attackDelay: Boolean,
    val animationStartedAt: Float,
    val hitOffset: Float,
    val reactionDuration: Float,
    val targetHpBefore: Int,
    val harm: Int,
    val targetMpBefore: Int?,
    val mpShieldDamage: Int,
)

/** 후속 물리 연출 계획: 선택 동작, 타격·반응 시각, 타격 뒤 HP/MP 결과를 불변 값으로 정의한다. */
internal data class BattleFollowUpPresentationPlan(
    val sourceAction: Int,
    val hitAt: Float,
    val reactionEndsAt: Float,
    val targetHpAfter: Int,
    val targetMpAfter: Int?,
) {
    /** HP 반영 여부: 실제 HP 피해가 있을 때만 health timeline을 갱신한다. */
    fun commitsHp(harm: Int): Boolean = harm > 0

    /** MP 반영 여부: 방어막 피해와 원본 MP 값이 모두 있을 때만 기력을 갱신한다. */
    fun commitsMp(mpShieldDamage: Int): Boolean = mpShieldDamage > 0 && targetMpAfter != null
}

/** 후속 물리 연출 계획기: 추가타·반격의 공통 공격 시간선과 대상 HP/MP 결과를 계산한다. */
internal object BattleFollowUpPresentationPlanner {
    /** 공격 동작 선택: 치명타와 공격 지연 상태에 맞는 원본 action 식별자를 반환한다. */
    fun sourceAction(critical: Boolean, attackDelay: Boolean): Int =
        BattleAttackSequence.selectAttackAction(critical, attackDelay)

    /** 시간선 계획: 공격 action의 hit 시각, 피격 반응 종료, HP/MP 감소 결과를 함께 계산한다. */
    fun plan(input: BattleFollowUpPresentationPlanInput): BattleFollowUpPresentationPlan {
        val hitAt = input.animationStartedAt + input.hitOffset
        return BattleFollowUpPresentationPlan(
            sourceAction = sourceAction(input.critical, input.attackDelay),
            hitAt = hitAt,
            reactionEndsAt = hitAt + input.reactionDuration,
            targetHpAfter = (input.targetHpBefore - input.harm).coerceAtLeast(0),
            targetMpAfter = input.targetMpBefore?.minus(input.mpShieldDamage)?.coerceAtLeast(0),
        )
    }

    /** 후속 예약 여부: 현재 타격 뒤 대상이 살아 있고 피해 또는 방어막 피해가 있을 때만 다음 타격을 예약한다. */
    fun shouldQueueNext(harm: Int, mpShieldDamage: Int, targetHpAfter: Int): Boolean =
        BattlePhysicalPresentationPlanner.shouldQueueCounter(harm, mpShieldDamage, targetHpAfter)
}
