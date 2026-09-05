package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.*

import com.jojo.game.*

/**
 * Lossless bridge from [Battle]'s eager physical result to the renderer's
 * source-ordered callback plan.  Keeping this outside BattleScreen lets the
 * calculation tests enforce `_attack2` ordering without a LibGDX window.
 */
fun TacticalActionResult.Attack.toPhysicalCallbackInvocations(): List<BattlePhysicalCallbackPlan.Invocation> =
    physicalPasses.map { pass ->
        BattlePhysicalCallbackPlan.Invocation(
            kind = when (pass.kind) {
                PhysicalAttackPassKind.ACTIVE -> BattlePhysicalCallbackPlan.InvocationKind.ACTIVE
                PhysicalAttackPassKind.ACTIVE_FOLLOW_UP -> BattlePhysicalCallbackPlan.InvocationKind.ACTIVE_FOLLOW_UP
                PhysicalAttackPassKind.COUNTER -> BattlePhysicalCallbackPlan.InvocationKind.COUNTER
                PhysicalAttackPassKind.COUNTER_FOLLOW_UP -> BattlePhysicalCallbackPlan.InvocationKind.COUNTER_FOLLOW_UP
            },
            attackerId = pass.attackerId,
            targets = pass.targets.map { target ->
                BattlePhysicalCallbackPlan.Target(
                    targetId = target.targetId,
                    harm = target.resolvedHarm,
                    mpShieldDamage = target.mpShieldDamage,
                    moneyShieldSpent = target.moneyShieldSpent,
                    lifeStealHealing = target.lifeStealHealing,
                    qxlHealing = target.qxlHealing,
                    playerMoneyDelta = target.playerMoneyDelta,
                    enemyMoneyDelta = target.enemyMoneyDelta,
                    recoilDamage = target.recoilDamage,
                    blockRetaliations = target.blockRetaliations.map { retaliation ->
                        BattlePhysicalCallbackPlan.BlockRetaliation(
                            kind = when (retaliation.kind) {
                                PhysicalBlockRetaliationKind.MENG_JI_CONFUSION ->
                                    BattlePhysicalCallbackPlan.BlockRetaliationKind.MENG_JI_CONFUSION
                                PhysicalBlockRetaliationKind.NI_FAN_PARALYSIS ->
                                    BattlePhysicalCallbackPlan.BlockRetaliationKind.NI_FAN_PARALYSIS
                            },
                            damage = retaliation.damage,
                        )
                    },
                    automaticProperty = target.automaticPropertyId?.let { itemId ->
                        BattlePhysicalCallbackPlan.PropertyUse(itemId, target.automaticProperty?.name.orEmpty())
                    },
                    backMove = target.backMove,
                    hasLocalStatusSettlement = target.hasLocalStatusSettlement,
                )
            },
        )
    }

/**
 * Money mutations owned by one source `_attack3` hit callback. JQFY spends
 * the struck unit's camp money, then XSJQ transfers its independently
 * recorded player/enemy deltas before the hurt animation starts.
 */
internal fun PhysicalAttackTargetResult.hitCallbackEconomyDelta(
    targetIsPlayerSide: Boolean,
): Pair<Int, Int> =
    (playerMoneyDelta - if (targetIsPlayerSide) moneyShieldSpent else 0) to
            (enemyMoneyDelta - if (targetIsPlayerSide) 0 else moneyShieldSpent)

