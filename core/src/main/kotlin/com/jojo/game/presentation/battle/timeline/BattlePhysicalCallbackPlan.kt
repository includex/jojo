package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.PhysicalBackMove

/**
 * Renderer-independent public contract for the callback order in the physical
 * battle sequence. [build] delegates the ordering calculation to the internal
 * planners so this API remains a compact, stable description of its inputs and
 * observable steps.
 */
object BattlePhysicalCallbackPlan {
    enum class InvocationKind { ACTIVE, ACTIVE_FOLLOW_UP, COUNTER, COUNTER_FOLLOW_UP }

    data class Target(
        val targetId: String,
        /** Final `_attack3` n after MPFY/JQFY. Zero selects the guard branch. */
        val harm: Int,
        val mpShieldDamage: Int = 0,
        val moneyShieldSpent: Int = 0,
        /** XXGJ recovery committed before the target hurt clip starts. */
        val lifeStealHealing: Int = 0,
        /** QXL recovery committed after XXGJ and before XSJQ. */
        val qxlHealing: Int = 0,
        val playerMoneyDelta: Int = 0,
        val enemyMoneyDelta: Int = 0,
        /** FTSH committed only after the hurt clip callback. */
        val recoilDamage: Int = 0,
        /** MENG_JI then NI_FAN, each committed after the guard callback. */
        val blockRetaliations: List<BlockRetaliation> = emptyList(),
        val automaticProperty: PropertyUse? = null,
        /** TPGJ moves concurrently with hurt and publishes its tile at .08s. */
        val backMove: PhysicalBackMove? = null,
        /** Whether `_jiesuan(t, o)` has target-status work to present. */
        val hasLocalStatusSettlement: Boolean = false,
    )

    data class PropertyUse(val itemId: Int, val itemName: String = "")

    enum class BlockRetaliationKind { MENG_JI_CONFUSION, NI_FAN_PARALYSIS }

    data class BlockRetaliation(val kind: BlockRetaliationKind, val damage: Int)

    data class Invocation(
        val kind: InvocationKind,
        val attackerId: String,
        /** `countAtkHarm` target order: primary first, then CTGJ targets. */
        val targets: List<Target>,
    )

    data class CounterMagic(
        val casterId: String,
        val targetId: String,
        val magicId: Int,
        val magicName: String = "",
    )

    data class Input(
        val invocations: List<Invocation>,
        /** CLFJ, when present, belongs between active invocations and counters. */
        val counterMagic: CounterMagic? = null,
        /** Exact insertion order of source `g_charinfo.index`. */
        val globalSettlementUnitIds: List<String>,
    )

    sealed interface Step {
        val awaitsCallback: Boolean

        data class AttackUntilHit(val kind: InvocationKind, val attackerId: String) : Step {
            override val awaitsCallback = true
        }

        data class FocusTarget(val targetId: String) : Step {
            override val awaitsCallback = false
        }

        /** Source anime26 callback, including guard sound and number lifetime. */
        data class GuardUntilComplete(val attackerId: String, val targetId: String) : Step {
            override val awaitsCallback = true
        }

        data class BlockRetaliationCommitted(
            val sourceId: String,
            val targetId: String,
            val retaliation: BlockRetaliation,
        ) : Step {
            override val awaitsCallback = false
        }

        data class MpShieldCommitted(val targetId: String, val mpDamage: Int) : Step {
            override val awaitsCallback = false
        }

        data class MoneyShieldCommitted(val targetId: String, val spent: Int) : Step {
            override val awaitsCallback = false
        }

        /** HP and MP are separate because MPFY's break skips HP loss. */
        data class TargetHarmCommitted(
            val attackerId: String,
            val targetId: String,
            val hpDamage: Int,
            val mpDamage: Int,
        ) : Step {
            override val awaitsCallback = false
        }

        data class LifeStealCommitted(val attackerId: String, val healing: Int) : Step {
            override val awaitsCallback = false
        }

        data class QxlCommitted(val attackerId: String, val healing: Int) : Step {
            override val awaitsCallback = false
        }

        data class MoneyAbsorbCommitted(
            val attackerId: String,
            val playerDelta: Int,
            val enemyDelta: Int,
        ) : Step {
            override val awaitsCallback = false
        }

        /** Source anime32 callback; the harm number clears at this boundary. */
        data class HurtUntilComplete(
            val attackerId: String,
            val targetId: String,
            val harm: Int,
            val mpShieldDamage: Int,
            /** Concurrent TPGJ move whose callback may precede hurt completion. */
            val backMove: PhysicalBackMove? = null,
        ) : Step {
            override val awaitsCallback = true
        }

        data class RecoilCommitted(val sourceId: String, val targetId: String, val damage: Int) : Step {
            override val awaitsCallback = false
        }

        /** Entire `_usePro2`: rise, reparent, fade, then item effect. */
        data class AutomaticPropertyUntilComplete(val unitId: String, val property: PropertyUse) : Step {
            override val awaitsCallback = true
        }

        /** `_jiesuan(t, o)` work belonging to this target hit. */
        data class LocalStatusSettlementUntilComplete(val unitId: String) : Step {
            override val awaitsCallback = true
        }

        /** `_magic` invoked by CLFJ; success suppresses the physical counter. */
        data class CounterMagicUntilComplete(val counter: CounterMagic) : Step {
            override val awaitsCallback = true
        }

        /** One source `g_charinfo` unit, including its center/info callbacks. */
        data class GlobalSettlement(val unitId: String) : Step {
            override val awaitsCallback = true
        }
    }

    /**
     * Source order is active passes, optional CLFJ, physical counters, then
     * global settlement. Each callback-bearing step must complete before a
     * renderer advances to the next step.
     */
    fun build(input: Input): List<Step> = BattlePhysicalCallbackPlanner.build(input)
}
