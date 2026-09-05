package com.jojo.game

import com.jojo.game.domain.battle.PhysicalAttackPass
import com.jojo.game.domain.battle.PhysicalAttackPassKind
import com.jojo.game.domain.battle.PhysicalAttackTargetResult
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.presentation.battle.timeline.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * class  `BattlePhysicalCallbackPlanTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattlePhysicalCallbackPlanTest {
    @Test
    fun `hit callback economy combines target-camp JQFY spend with XSJQ transfer`() {
        val result = PhysicalAttackTargetResult(
            targetId = "defender",
            resolvedHarm = 1,
            damage = 1,
            moneyShieldSpent = 90,
            playerMoneyDelta = 20,
            enemyMoneyDelta = -20,
        )

        assertEquals(-70 to -20, result.hitCallbackEconomyDelta(targetIsPlayerSide = true))
        assertEquals(20 to -110, result.hitCallbackEconomyDelta(targetIsPlayerSide = false))
    }

    @Test
    fun `physical pass pipeline intentionally excludes the separate surround attack coroutine`() {
        assertEquals(
            listOf("ACTIVE", "ACTIVE_FOLLOW_UP", "COUNTER", "COUNTER_FOLLOW_UP"),
            PhysicalAttackPassKind.entries.map { it.name },
        )
    }

    @Test
    fun `attack result adapter preserves pass target and local callback order`() {
        val result = TacticalActionResult.Attack(
            damage = 20,
            defeated = false,
            physicalPasses = listOf(
                PhysicalAttackPass(
                    PhysicalAttackPassKind.ACTIVE,
                    "attacker",
                    critical = false,
                    targets = listOf(
                        PhysicalAttackTargetResult("primary", resolvedHarm = 20, damage = 20),
                        PhysicalAttackTargetResult(
                            "splash", resolvedHarm = 10, damage = 10,
                            recoilDamage = 2,
                            automaticPropertyId = 7,
                            automaticProperty = TacticalActionResult.Item("약", "splash", "HP 10 회복"),
                            automaticPropertyHpDelta = 10,
                            hasLocalStatusSettlement = true,
                        ),
                    ),
                ),
                PhysicalAttackPass(
                    PhysicalAttackPassKind.ACTIVE_FOLLOW_UP,
                    "attacker",
                    critical = true,
                    targets = listOf(PhysicalAttackTargetResult("primary", resolvedHarm = 8, damage = 8)),
                ),
            ),
        )

        val invocations = result.toPhysicalCallbackInvocations()
        val steps = BattlePhysicalCallbackPlan.build(
            BattlePhysicalCallbackPlan.Input(invocations, globalSettlementUnitIds = emptyList()),
        )

        assertEquals(listOf("primary", "splash"), invocations[0].targets.map(BattlePhysicalCallbackPlan.Target::targetId))
        val auto = steps.indexOfFirst { it is BattlePhysicalCallbackPlan.Step.AutomaticPropertyUntilComplete }
        val local = steps.indexOfFirst { it is BattlePhysicalCallbackPlan.Step.LocalStatusSettlementUntilComplete }
        val follow = steps.indexOfFirst {
            it is BattlePhysicalCallbackPlan.Step.AttackUntilHit &&
                it.kind == BattlePhysicalCallbackPlan.InvocationKind.ACTIVE_FOLLOW_UP
        }
        assertTrue(auto < local)
        assertTrue(local < follow)
    }

    @Test
    fun `hit awaits hurt before recoil and automatic property then globally settles`() {
        val property = BattlePhysicalCallbackPlan.PropertyUse(77, "콩")
        val steps = BattlePhysicalCallbackPlan.build(
            BattlePhysicalCallbackPlan.Input(
                invocations = listOf(
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.ACTIVE,
                        attackerId = "cao-cao",
                        targets = listOf(
                            BattlePhysicalCallbackPlan.Target(
                                targetId = "enemy",
                                harm = 30,
                                moneyShieldSpent = 90,
                                lifeStealHealing = 6,
                                qxlHealing = 30,
                                playerMoneyDelta = 60,
                                enemyMoneyDelta = -60,
                                recoilDamage = 9,
                                automaticProperty = property,
                                hasLocalStatusSettlement = true,
                            ),
                        ),
                    ),
                ),
                globalSettlementUnitIds = listOf("cao-cao", "enemy"),
            ),
        )

        assertEquals(
            listOf(
                BattlePhysicalCallbackPlan.Step.AttackUntilHit(BattlePhysicalCallbackPlan.InvocationKind.ACTIVE, "cao-cao"),
                BattlePhysicalCallbackPlan.Step.FocusTarget("enemy"),
                BattlePhysicalCallbackPlan.Step.MoneyShieldCommitted("enemy", 90),
                BattlePhysicalCallbackPlan.Step.TargetHarmCommitted("cao-cao", "enemy", 30, 0),
                BattlePhysicalCallbackPlan.Step.LifeStealCommitted("cao-cao", 6),
                BattlePhysicalCallbackPlan.Step.QxlCommitted("cao-cao", 30),
                BattlePhysicalCallbackPlan.Step.MoneyAbsorbCommitted("cao-cao", 60, -60),
                BattlePhysicalCallbackPlan.Step.HurtUntilComplete("cao-cao", "enemy", 30, 0),
                BattlePhysicalCallbackPlan.Step.RecoilCommitted("enemy", "cao-cao", 9),
                BattlePhysicalCallbackPlan.Step.AutomaticPropertyUntilComplete("enemy", property),
                BattlePhysicalCallbackPlan.Step.LocalStatusSettlementUntilComplete("enemy"),
                BattlePhysicalCallbackPlan.Step.GlobalSettlement("cao-cao"),
                BattlePhysicalCallbackPlan.Step.GlobalSettlement("enemy"),
            ),
            steps,
        )
        assertTrue(steps[0].awaitsCallback)
        assertTrue(steps[7].awaitsCallback)
        assertTrue(steps[9].awaitsCallback)
        assertTrue(steps[10].awaitsCallback)
    }

    @Test
    fun `guard callback precedes block retaliation and next continuous attack`() {
        val steps = BattlePhysicalCallbackPlan.build(
            BattlePhysicalCallbackPlan.Input(
                invocations = listOf(
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.ACTIVE,
                        "attacker",
                        listOf(
                            BattlePhysicalCallbackPlan.Target(
                                "defender",
                                0,
                                blockRetaliations = listOf(
                                    BattlePhysicalCallbackPlan.BlockRetaliation(
                                        BattlePhysicalCallbackPlan.BlockRetaliationKind.MENG_JI_CONFUSION,
                                        20,
                                    ),
                                    BattlePhysicalCallbackPlan.BlockRetaliation(
                                        BattlePhysicalCallbackPlan.BlockRetaliationKind.NI_FAN_PARALYSIS,
                                        30,
                                    ),
                                ),
                            ),
                        ),
                    ),
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.ACTIVE_FOLLOW_UP,
                        "attacker",
                        listOf(BattlePhysicalCallbackPlan.Target("defender", 10)),
                    ),
                ),
                globalSettlementUnitIds = listOf("attacker", "defender"),
            ),
        )

        assertEquals(BattlePhysicalCallbackPlan.Step.GuardUntilComplete("attacker", "defender"), steps[2])
        assertEquals(
            BattlePhysicalCallbackPlan.Step.BlockRetaliationCommitted(
                "defender",
                "attacker",
                BattlePhysicalCallbackPlan.BlockRetaliation(
                    BattlePhysicalCallbackPlan.BlockRetaliationKind.MENG_JI_CONFUSION,
                    20,
                ),
            ),
            steps[3],
        )
        assertEquals(
            BattlePhysicalCallbackPlan.Step.BlockRetaliationCommitted(
                "defender",
                "attacker",
                BattlePhysicalCallbackPlan.BlockRetaliation(
                    BattlePhysicalCallbackPlan.BlockRetaliationKind.NI_FAN_PARALYSIS,
                    30,
                ),
            ),
            steps[4],
        )
        assertEquals(
            BattlePhysicalCallbackPlan.Step.AttackUntilHit(BattlePhysicalCallbackPlan.InvocationKind.ACTIVE_FOLLOW_UP, "attacker"),
            steps[5],
        )
    }

    @Test
    fun `MPFY loses only MP and its outer break skips heal and money effects`() {
        val steps = BattlePhysicalCallbackPlan.build(
            BattlePhysicalCallbackPlan.Input(
                invocations = listOf(
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.ACTIVE,
                        "attacker",
                        listOf(
                            BattlePhysicalCallbackPlan.Target(
                                targetId = "defender",
                                harm = 10,
                                mpShieldDamage = 10,
                                // Invalid legacy aggregates must not leak
                                // through a source branch that never runs.
                                lifeStealHealing = 4,
                                qxlHealing = 10,
                                playerMoneyDelta = 10,
                                enemyMoneyDelta = -10,
                                recoilDamage = 5,
                            ),
                        ),
                    ),
                ),
                globalSettlementUnitIds = listOf("attacker", "defender"),
            ),
        )

        assertTrue(BattlePhysicalCallbackPlan.Step.MpShieldCommitted("defender", 10) in steps)
        assertTrue(BattlePhysicalCallbackPlan.Step.TargetHarmCommitted("attacker", "defender", 0, 10) in steps)
        assertTrue(steps.none { it is BattlePhysicalCallbackPlan.Step.LifeStealCommitted })
        assertTrue(steps.none { it is BattlePhysicalCallbackPlan.Step.QxlCommitted })
        assertTrue(steps.none { it is BattlePhysicalCallbackPlan.Step.MoneyAbsorbCommitted })
        // FTSH is outside the MPFY do/while and still uses the final n.
        assertTrue(BattlePhysicalCallbackPlan.Step.RecoilCommitted("defender", "attacker", 5) in steps)
    }

    @Test
    fun `CTGJ targets finish sequentially before the follow up pass`() {
        val steps = BattlePhysicalCallbackPlan.build(
            BattlePhysicalCallbackPlan.Input(
                invocations = listOf(
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.ACTIVE,
                        "attacker",
                        listOf(
                            BattlePhysicalCallbackPlan.Target("primary", 20),
                            BattlePhysicalCallbackPlan.Target("splash-a", 15),
                            BattlePhysicalCallbackPlan.Target("splash-b", 10),
                        ),
                    ),
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.ACTIVE_FOLLOW_UP,
                        "attacker",
                        listOf(BattlePhysicalCallbackPlan.Target("primary", 18)),
                    ),
                ),
                globalSettlementUnitIds = listOf("attacker", "primary", "splash-a", "splash-b"),
            ),
        )

        val splashBReaction = steps.indexOfFirst {
            it is BattlePhysicalCallbackPlan.Step.HurtUntilComplete && it.targetId == "splash-b"
        }
        val followUp = steps.indexOf(
            BattlePhysicalCallbackPlan.Step.AttackUntilHit(BattlePhysicalCallbackPlan.InvocationKind.ACTIVE_FOLLOW_UP, "attacker"),
        )
        assertTrue(splashBReaction < followUp)
    }

    @Test
    fun `CLFJ callback suppresses physical counter and precedes global settlement`() {
        val counterMagic = BattlePhysicalCallbackPlan.CounterMagic("defender", "attacker", 13, "반격 책략")
        val steps = BattlePhysicalCallbackPlan.build(
            BattlePhysicalCallbackPlan.Input(
                invocations = listOf(
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.ACTIVE,
                        "attacker",
                        listOf(BattlePhysicalCallbackPlan.Target("defender", 20)),
                    ),
                    // Deliberately supplied to prove the source suppression rule.
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.COUNTER,
                        "defender",
                        listOf(BattlePhysicalCallbackPlan.Target("attacker", 12)),
                    ),
                ),
                counterMagic = counterMagic,
                globalSettlementUnitIds = listOf("attacker", "defender"),
            ),
        )

        assertTrue(BattlePhysicalCallbackPlan.Step.CounterMagicUntilComplete(counterMagic) in steps)
        assertTrue(steps.none { it is BattlePhysicalCallbackPlan.Step.AttackUntilHit && it.kind == BattlePhysicalCallbackPlan.InvocationKind.COUNTER })
        assertTrue(steps.indexOf(BattlePhysicalCallbackPlan.Step.CounterMagicUntilComplete(counterMagic)) < steps.indexOf(BattlePhysicalCallbackPlan.Step.GlobalSettlement("attacker")))
    }

    @Test
    fun `empty missed active pass still reaches CLFJ only after its attack callback`() {
        val counterMagic = BattlePhysicalCallbackPlan.CounterMagic("defender", "attacker", 99)
        val steps = BattlePhysicalCallbackPlan.build(
            BattlePhysicalCallbackPlan.Input(
                invocations = listOf(
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.ACTIVE,
                        "attacker",
                        targets = emptyList(),
                    ),
                ),
                counterMagic = counterMagic,
                globalSettlementUnitIds = emptyList(),
            ),
        )

        assertEquals(
            listOf(
                BattlePhysicalCallbackPlan.Step.AttackUntilHit(BattlePhysicalCallbackPlan.InvocationKind.ACTIVE, "attacker"),
                BattlePhysicalCallbackPlan.Step.CounterMagicUntilComplete(counterMagic),
            ),
            steps,
        )
    }
}
