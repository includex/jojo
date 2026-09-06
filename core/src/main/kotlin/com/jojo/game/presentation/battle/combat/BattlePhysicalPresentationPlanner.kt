// Battle Combat
package com.jojo.game.presentation.battle.combat

/** 물리 패스 대상 계획 입력: 한 대상의 시각 체력·기력과 정산 결과를 화면 효과 없이 고정한다. */
internal data class BattlePhysicalPassTargetPlanInput(
    val resolvedHarm: Int,
    val damage: Int,
    val mpShieldDamage: Int,
    val attackerHealing: Int,
    val retaliationDamage: Int,
    val recoilDamage: Int,
    val automaticPropertyPresent: Boolean,
    val automaticPropertyHpDelta: Int,
    val automaticPropertyMpDelta: Int,
    val targetHpBefore: Int,
    val targetMpBefore: Int,
    val targetMaxHp: Int,
    val targetMaxMp: Int,
    val attackerHpBefore: Int,
    val attackerMaxHp: Int,
    val reactionStartedAt: Float,
    val reactionDuration: Float,
)

/** 물리 패스 대상 계획: 피격·회복·반사·자동 효과의 체력 변화와 다음 실행 시각을 불변 값으로 정의한다. */
internal data class BattlePhysicalPassTargetPlan(
    val guard: Boolean,
    val reactionAction: Int,
    val reactionEndsAt: Float,
    val targetHpAfterHarm: Int,
    val targetMpAfterHarm: Int,
    val attackerHealing: Int,
    val attackerHpAfterHealing: Int,
    val attackerHpAfterRetaliation: Int,
    val automaticEndsAt: Float,
    val targetHpAfterAutomaticProperty: Int,
    val targetMpAfterAutomaticProperty: Int,
) {
    /** 피해 숫자 표시 여부: 막기 외의 피격 결과에서만 숫자 표현을 시작한다. */
    val showsHarmNumber: Boolean get() = !guard

    /** 피해 숫자 값: MP 방어막 피해가 있으면 그 값을 우선 표시하고, 아니면 실제 피해를 표시한다. */
    fun harmNumberAmount(resolvedHarm: Int, mpShieldDamage: Int): Int =
        if (mpShieldDamage > 0) mpShieldDamage else resolvedHarm

    /** HP 숫자 여부: MP 방어막 피해가 없을 때만 HP 색상으로 표시한다. */
    fun harmNumberIsHp(mpShieldDamage: Int): Boolean = mpShieldDamage == 0
}

/** 물리 전투 연출 계획기: 대상별 체력 변화와 추가타·반격 대기열 선택을 순수 규칙으로 결정한다. */
internal object BattlePhysicalPresentationPlanner {
    /** 대상 계획: 피격 뒤 체력·기력·흡혈·반사·자동 효과의 결과와 반응 종료 시각을 계산한다. */
    fun target(input: BattlePhysicalPassTargetPlanInput): BattlePhysicalPassTargetPlan {
        val guard = input.resolvedHarm == 0 && input.mpShieldDamage == 0
        val reactionEndsAt = input.reactionStartedAt + input.reactionDuration
        val targetHpAfterHarm = (input.targetHpBefore - input.damage).coerceAtLeast(0)
        val targetMpAfterHarm = (input.targetMpBefore - input.mpShieldDamage).coerceAtLeast(0)
        val attackerHpAfterHealing = (input.attackerHpBefore + input.attackerHealing)
            .coerceIn(0, input.attackerMaxHp)
        val afterRetaliation = (attackerHpAfterHealing - input.retaliationDamage).coerceAtLeast(0)
        val attackerHpAfterRetaliation = if (input.recoilDamage > 0) {
            maxOf(1, afterRetaliation - input.recoilDamage)
        } else {
            afterRetaliation
        }
        val automaticEndsAt = if (input.automaticPropertyPresent) reactionEndsAt + 1.5f else reactionEndsAt
        return BattlePhysicalPassTargetPlan(
            guard = guard,
            reactionAction = if (guard) 26 else 32,
            reactionEndsAt = reactionEndsAt,
            targetHpAfterHarm = targetHpAfterHarm,
            targetMpAfterHarm = targetMpAfterHarm,
            attackerHealing = input.attackerHealing,
            attackerHpAfterHealing = attackerHpAfterHealing,
            attackerHpAfterRetaliation = attackerHpAfterRetaliation,
            automaticEndsAt = automaticEndsAt,
            targetHpAfterAutomaticProperty = (targetHpAfterHarm + input.automaticPropertyHpDelta)
                .coerceIn(0, input.targetMaxHp),
            targetMpAfterAutomaticProperty = (targetMpAfterHarm + input.automaticPropertyMpDelta)
                .coerceIn(0, input.targetMaxMp),
        )
    }

    /** 물리 패스 대기열 여부: 남은 패스 또는 반격 마법이 있으면 현재 패스를 끝낸 뒤 대기열을 유지한다. */
    fun shouldQueuePhysicalPass(nextPassIndex: Int, passCount: Int, hasCounterMagic: Boolean): Boolean =
        nextPassIndex < passCount || hasCounterMagic

    /** 후속 연출 선택: 살아남은 대상의 추가타를 우선하고, 없으면 반격 가능 여부를 선택한다. */
    fun followUpOrCounter(input: BattleFollowUpCounterPlanInput): BattleFollowUpCounterDecision = when {
        input.hasFollowUp && input.targetHpAfterPrimary > 0 -> BattleFollowUpCounterDecision.FOLLOW_UP
        input.hasCounter && !input.defeated -> BattleFollowUpCounterDecision.COUNTER
        else -> BattleFollowUpCounterDecision.NONE
    }

    /** 반격 대기열 여부: 대상이 살아 있고 반격 피해 또는 MP 방어막 피해가 있을 때만 반격을 예약한다. */
    fun shouldQueueCounter(damage: Int, mpShieldDamage: Int, targetHpAfterHit: Int): Boolean =
        (damage > 0 || mpShieldDamage > 0) && targetHpAfterHit > 0
}

/** 추가타·반격 선택 입력: 첫 타 뒤 생존 여부와 각 후속 피해의 존재를 고정한다. */
internal data class BattleFollowUpCounterPlanInput(
    val hasFollowUp: Boolean,
    val hasCounter: Boolean,
    val targetHpAfterPrimary: Int,
    val defeated: Boolean,
)

/** 추가타·반격 선택 결과: 현재 공격 뒤 예약할 다음 단일 연출 종류를 나타낸다. */
internal enum class BattleFollowUpCounterDecision { FOLLOW_UP, COUNTER, NONE }
