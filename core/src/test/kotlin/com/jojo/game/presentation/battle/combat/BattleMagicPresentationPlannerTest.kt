// Battle Combat Test
package com.jojo.game.presentation.battle.combat

import com.jojo.game.domain.battle.MagicTarget
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

/** magic presentation planner가 Screen mutation 전에 시간·반응·시각 자원 계획을 고정하는지 검증한다. */
class BattleMagicPresentationPlannerTest {
    @Test
    fun `effect hit timing and target damage become immutable pass plan`() {
        val target = MagicTarget(
            targetId = "target",
            damage = 20,
            hitRate = 100,
            hit = true,
            defeated = false,
        )

        val plan = BattleMagicPresentationPlanner.plan(
            BattleMagicPresentationPlanner.Input(
                casterId = "caster",
                profile = null,
                effectStartedAt = 10f,
                effectEndsAt = 14f,
                effectHitOffset = 1.5f,
                targets = listOf(BattleMagicPresentationPlanner.TargetInput(target, reactionDuration = 2f)),
                visualState = BattleCombatPresentationQueueCoordinator.VisualState(
                    hitPoints = mapOf("target" to 100),
                    magicPoints = mapOf("target" to 30),
                ),
                units = mapOf(
                    "target" to BattleMagicPresentationPlanner.UnitState(
                        hitPoints = 100,
                        maxHitPoints = 100,
                        magicPoints = 30,
                        maxMagicPoints = 30,
                        direction = 2,
                    ),
                ),
            ),
        )

        assertEquals(11.5f, plan.effectAt)
        assertFalse(plan.mcall)
        assertNull(plan.primaryFocusId)
        assertEquals(10f, plan.targetFocusAt)
        assertEquals(3, plan.reactions.single().sourceAction)
        assertEquals(14f, plan.reactions.single().endsAt)
        assertEquals(-20, plan.changes.single().hpAdd)
        assertEquals(80, plan.changes.single().hpAfter)
        assertEquals(80, plan.nextVisualState.hitPoints["target"])
    }
}
