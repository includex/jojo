// Battle Combat Test
package com.jojo.game.presentation.battle.combat

import com.jojo.game.domain.battle.MagicTarget
import com.jojo.game.domain.battle.PhysicalAttackPass
import com.jojo.game.domain.battle.PhysicalAttackPassKind
import com.jojo.game.domain.battle.PhysicalAttackTargetResult
import com.jojo.game.domain.battle.TacticalActionResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** combat queue coordinator가 Screen mutation 전 pass 순서와 시각 HP·MP snapshot을 고정하는지 검증한다. */
class BattleCombatPresentationQueueCoordinatorTest {
    @Test
    fun `hit physical plan preserves pass order shield state and counter continuation`() {
        val result = TacticalActionResult.Attack(
            damage = 5,
            defeated = false,
            physicalPasses = listOf(
                pass("actor", PhysicalAttackTargetResult("target", 5, 5, mpShieldDamage = 3, automaticPropertyMpDelta = 1)),
                pass("actor", PhysicalAttackTargetResult("target", 4, 4)),
            ),
            counterMagic = magic(),
        )

        val plan = assertNotNull(
            BattleCombatPresentationQueueCoordinator.hitPhysicalQueuePlan(
                result,
                actorId = "actor",
                targetId = "target",
                healthBeforeAction = mapOf("target" to 40),
                deferredInitialMp = mapOf("target" to null),
                units = mapOf("target" to unit(mp = 10, maxMp = 20)),
            ),
        )

        assertEquals(1, plan.nextPassIndex)
        assertEquals(2, plan.passes.size)
        assertEquals(12, plan.visualState.magicPoints["target"])
        assertTrue(plan.continuesAfterCurrentPass())
    }

    @Test
    fun `missed physical plan omits queue when no remaining pass or counter exists`() {
        val result = TacticalActionResult.Attack(
            damage = 0,
            defeated = false,
            hit = false,
            physicalPasses = listOf(pass("actor", PhysicalAttackTargetResult("target", 0, 0))),
        )

        assertNull(
            BattleCombatPresentationQueueCoordinator.missedPhysicalQueuePlan(
                result, "actor", "target", emptyMap(), emptyMap(), emptyMap(),
            ),
        )
    }

    @Test
    fun `deferred magic plan carries first pass visual damage into next pass`() {
        val magic = TacticalActionResult.Magic(
            name = "마법",
            cost = 5,
            targets = listOf(target("target", 20)),
            passes = listOf(listOf(target("target", 20)), listOf(target("target", 10))),
        )

        val plan = assertNotNull(
            BattleCombatPresentationQueueCoordinator.deferredMagicQueuePlan(
                magic,
                casterId = "caster",
                profile = null,
                current = BattleCombatPresentationQueueCoordinator.VisualState(
                    hitPoints = mapOf("target" to 100),
                    magicPoints = emptyMap(),
                ),
                units = mapOf("target" to unit(hp = 100, maxHp = 100)),
            ),
        )

        assertEquals(1, plan.nextPassIndex)
        assertEquals(80, plan.visualState.hitPoints["target"])
    }

    private fun pass(attackerId: String, target: PhysicalAttackTargetResult) = PhysicalAttackPass(
        kind = PhysicalAttackPassKind.ACTIVE,
        attackerId = attackerId,
        critical = false,
        targets = listOf(target),
    )

    private fun magic() = TacticalActionResult.Magic("반격마법", 0, emptyList())

    private fun target(id: String, damage: Int) = MagicTarget(
        targetId = id,
        damage = damage,
        hitRate = 100,
        hit = true,
        defeated = false,
    )

    private fun unit(
        hp: Int = 1,
        maxHp: Int = 1,
        mp: Int = 0,
        maxMp: Int = 0,
    ) = BattleCombatPresentationQueueCoordinator.UnitVisualState(hp, maxHp, mp, maxMp)
}
