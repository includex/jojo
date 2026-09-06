// Test
package com.jojo.game

import com.jojo.game.presentation.battle.timeline.BattlePhysicalCallbackPlan
import kotlin.test.Test
import kotlin.test.assertEquals

class BattlePhysicalCallbackPlannerTest {
    @Test
    fun `guard target settles locally only after every retaliation commits`() {
        val retaliation = BattlePhysicalCallbackPlan.BlockRetaliation(
            BattlePhysicalCallbackPlan.BlockRetaliationKind.MENG_JI_CONFUSION,
            damage = 12,
        )

        val steps = BattlePhysicalCallbackPlan.build(
            BattlePhysicalCallbackPlan.Input(
                invocations = listOf(
                    BattlePhysicalCallbackPlan.Invocation(
                        BattlePhysicalCallbackPlan.InvocationKind.ACTIVE,
                        attackerId = "attacker",
                        targets = listOf(
                            BattlePhysicalCallbackPlan.Target(
                                targetId = "defender",
                                harm = 0,
                                blockRetaliations = listOf(retaliation),
                                hasLocalStatusSettlement = true,
                            ),
                        ),
                    ),
                ),
                globalSettlementUnitIds = listOf("attacker", "defender"),
            ),
        )

        assertEquals(
            listOf(
                BattlePhysicalCallbackPlan.Step.AttackUntilHit(
                    BattlePhysicalCallbackPlan.InvocationKind.ACTIVE,
                    "attacker",
                ),
                BattlePhysicalCallbackPlan.Step.FocusTarget("defender"),
                BattlePhysicalCallbackPlan.Step.GuardUntilComplete("attacker", "defender"),
                BattlePhysicalCallbackPlan.Step.BlockRetaliationCommitted(
                    "defender", "attacker", retaliation,
                ),
                BattlePhysicalCallbackPlan.Step.LocalStatusSettlementUntilComplete("defender"),
                BattlePhysicalCallbackPlan.Step.GlobalSettlement("attacker"),
                BattlePhysicalCallbackPlan.Step.GlobalSettlement("defender"),
            ),
            steps,
        )
    }
}
