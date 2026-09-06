// Battle
package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.*

import com.jojo.game.*

/** 전투 물리 결과를 원본 콜백 순서의 렌더 계획으로 변환합니다. */
/** 공격 결과의 각 패스를 표시 콜백 호출 목록으로 변환합니다. */
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

/** 한 타격 콜백에서 발생하는 플레이어·적 자금 변화를 계산합니다. */
internal fun PhysicalAttackTargetResult.hitCallbackEconomyDelta(
    targetIsPlayerSide: Boolean,
): Pair<Int, Int> =
    (playerMoneyDelta - if (targetIsPlayerSide) moneyShieldSpent else 0) to
            (enemyMoneyDelta - if (targetIsPlayerSide) 0 else moneyShieldSpent)
