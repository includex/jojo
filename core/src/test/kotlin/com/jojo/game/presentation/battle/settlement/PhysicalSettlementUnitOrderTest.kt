package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.battle.*
import kotlin.test.Test
import kotlin.test.assertEquals

class PhysicalSettlementUnitOrderTest {
    @Test fun `enemy attacker is recorded before the friend hit and counterattack`() {
        val attack = TacticalActionResult.Attack(21, false, counterDamage = 20, physicalPasses = listOf(
            pass("enemy-477", "friend-210", PhysicalAttackPassKind.ACTIVE),
            pass("friend-210", "enemy-477", PhysicalAttackPassKind.COUNTER),
        ))
        assertEquals(listOf("enemy-477", "friend-210"), physicalSettlementUnitOrder(attack, "enemy-477", "friend-210"))
    }

    @Test fun `actual first strike and area targets retain insertion order`() {
        val attack = TacticalActionResult.Attack(10, false, physicalPasses = listOf(
            pass("defender", "initiator", PhysicalAttackPassKind.ACTIVE),
            PhysicalAttackPass(PhysicalAttackPassKind.COUNTER, "initiator", false, listOf(
                PhysicalAttackTargetResult("defender", 10, 10),
                PhysicalAttackTargetResult("splash", 5, 5),
            )),
        ))
        assertEquals(listOf("defender", "initiator", "splash"), physicalSettlementUnitOrder(attack, "initiator", "defender"))
    }

    @Test fun `legacy manual attack keeps actor before its distinct targets`() {
        val attack = TacticalActionResult.Attack(10, false, splashTargets = listOf(
            PhysicalTarget("target", 10), PhysicalTarget("splash", 5),
        ))
        assertEquals(listOf("mine", "target", "splash"), physicalSettlementUnitOrder(attack, "mine", "target"))
    }

    private fun pass(actor: String, target: String, kind: PhysicalAttackPassKind) =
        PhysicalAttackPass(kind, actor, false, listOf(PhysicalAttackTargetResult(target, 20, 20)))
}
