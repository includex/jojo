package com.jojo.game.presentation.battle.timeline

import com.jojo.game.presentation.battle.timeline.BattlePhysicalCallbackPlan.Invocation
import com.jojo.game.presentation.battle.timeline.BattlePhysicalCallbackPlan.InvocationKind
import com.jojo.game.presentation.battle.timeline.BattlePhysicalCallbackPlan.Step

/** Coordinates physical passes, the optional magic counter, and final settlement. */
internal object BattlePhysicalCallbackPlanner {
    fun build(input: BattlePhysicalCallbackPlan.Input): List<Step> = buildList {
        val firstCounter = input.invocations.indexOfFirst { invocation -> invocation.isCounter() }
            .let { index -> if (index < 0) input.invocations.size else index }

        input.invocations.take(firstCounter).forEach { invocation ->
            BattlePhysicalTargetCallbackPlanner.append(this, invocation)
        }
        input.counterMagic?.let { add(Step.CounterMagicUntilComplete(it)) }
        // A successful CLFJ call suppresses physical counters in `_attack6`.
        if (input.counterMagic == null) {
            input.invocations.drop(firstCounter).forEach { invocation ->
                BattlePhysicalTargetCallbackPlanner.append(this, invocation)
            }
        }
        input.globalSettlementUnitIds.forEach { unitId -> add(Step.GlobalSettlement(unitId)) }
    }

    private fun Invocation.isCounter(): Boolean =
        kind == InvocationKind.COUNTER || kind == InvocationKind.COUNTER_FOLLOW_UP
}

/** Builds one `_attack2` pass, preserving the sequential `_attack3` target callbacks. */
internal object BattlePhysicalTargetCallbackPlanner {
    fun append(steps: MutableList<Step>, invocation: Invocation) {
        steps += Step.AttackUntilHit(invocation.kind, invocation.attackerId)
        invocation.targets.forEach { target -> appendTarget(steps, invocation, target) }
    }

    private fun appendTarget(
        steps: MutableList<Step>,
        invocation: Invocation,
        target: BattlePhysicalCallbackPlan.Target,
    ) {
        steps += Step.FocusTarget(target.targetId)
        if (target.harm == 0 && target.mpShieldDamage == 0) appendGuard(steps, invocation, target)
        else appendHit(steps, invocation, target)
        if (target.hasLocalStatusSettlement) {
            steps += Step.LocalStatusSettlementUntilComplete(target.targetId)
        }
    }

    private fun appendGuard(
        steps: MutableList<Step>,
        invocation: Invocation,
        target: BattlePhysicalCallbackPlan.Target,
    ) {
        steps += Step.GuardUntilComplete(invocation.attackerId, target.targetId)
        target.blockRetaliations.forEach { retaliation ->
            steps += Step.BlockRetaliationCommitted(target.targetId, invocation.attackerId, retaliation)
        }
    }

    private fun appendHit(
        steps: MutableList<Step>,
        invocation: Invocation,
        target: BattlePhysicalCallbackPlan.Target,
    ) {
        appendShieldAndHarm(steps, invocation, target)
        if (target.mpShieldDamage == 0) appendPreHurtBenefits(steps, invocation, target)
        steps += Step.HurtUntilComplete(
            invocation.attackerId, target.targetId, target.harm, target.mpShieldDamage, target.backMove,
        )
        if (target.recoilDamage > 0) {
            steps += Step.RecoilCommitted(target.targetId, invocation.attackerId, target.recoilDamage)
        }
        target.automaticProperty?.let { property ->
            steps += Step.AutomaticPropertyUntilComplete(target.targetId, property)
        }
    }

    private fun appendShieldAndHarm(
        steps: MutableList<Step>,
        invocation: Invocation,
        target: BattlePhysicalCallbackPlan.Target,
    ) {
        when {
            target.mpShieldDamage > 0 -> steps += Step.MpShieldCommitted(target.targetId, target.mpShieldDamage)
            target.moneyShieldSpent > 0 -> steps += Step.MoneyShieldCommitted(target.targetId, target.moneyShieldSpent)
        }
        steps += Step.TargetHarmCommitted(
            invocation.attackerId,
            target.targetId,
            hpDamage = if (target.mpShieldDamage > 0) 0 else target.harm,
            mpDamage = target.mpShieldDamage,
        )
    }

    private fun appendPreHurtBenefits(
        steps: MutableList<Step>,
        invocation: Invocation,
        target: BattlePhysicalCallbackPlan.Target,
    ) {
        if (target.lifeStealHealing > 0) {
            steps += Step.LifeStealCommitted(invocation.attackerId, target.lifeStealHealing)
        }
        if (target.qxlHealing > 0) steps += Step.QxlCommitted(invocation.attackerId, target.qxlHealing)
        if (target.playerMoneyDelta != 0 || target.enemyMoneyDelta != 0) {
            steps += Step.MoneyAbsorbCommitted(
                invocation.attackerId, target.playerMoneyDelta, target.enemyMoneyDelta,
            )
        }
    }
}
