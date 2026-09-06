// Battle
package com.jojo.game.presentation.battle.timeline

import com.jojo.game.domain.battle.PhysicalBackMove
object BattlePhysicalCallbackPlan {
    /** InvocationKind: 전투 화면 흐름에서 현재 처리 종류를 구분한다. */
    enum class InvocationKind { ACTIVE, ACTIVE_FOLLOW_UP, COUNTER, COUNTER_FOLLOW_UP }
    data class Target(
        val targetId: String,
        val harm: Int,
        val mpShieldDamage: Int = 0,
        val moneyShieldSpent: Int = 0,
        val lifeStealHealing: Int = 0,
        val qxlHealing: Int = 0,
        val playerMoneyDelta: Int = 0,
        val enemyMoneyDelta: Int = 0,
        val recoilDamage: Int = 0,
        val blockRetaliations: List<BlockRetaliation> = emptyList(),
        val automaticProperty: PropertyUse? = null,
        val backMove: PhysicalBackMove? = null,
        val hasLocalStatusSettlement: Boolean = false,
    )
    data class PropertyUse(val itemId: Int, val itemName: String = "")

    /** BlockRetaliationKind: 전투 화면 흐름에서 현재 처리 종류를 구분한다. */
    enum class BlockRetaliationKind { MENG_JI_CONFUSION, NI_FAN_PARALYSIS }
    data class BlockRetaliation(val kind: BlockRetaliationKind, val damage: Int)
    data class Invocation(
        val kind: InvocationKind,
        val attackerId: String,
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
        val counterMagic: CounterMagic? = null,
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
        data class HurtUntilComplete(
            val attackerId: String,
            val targetId: String,
            val harm: Int,
            val mpShieldDamage: Int,
            val backMove: PhysicalBackMove? = null,
        ) : Step {
            override val awaitsCallback = true
        }
        data class RecoilCommitted(val sourceId: String, val targetId: String, val damage: Int) : Step {
            override val awaitsCallback = false
        }
        data class AutomaticPropertyUntilComplete(val unitId: String, val property: PropertyUse) : Step {
            override val awaitsCallback = true
        }
        data class LocalStatusSettlementUntilComplete(val unitId: String) : Step {
            override val awaitsCallback = true
        }
        data class CounterMagicUntilComplete(val counter: CounterMagic) : Step {
            override val awaitsCallback = true
        }
        data class GlobalSettlement(val unitId: String) : Step {
            override val awaitsCallback = true
        }
    }

    /** build: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    fun build(input: Input): List<Step> = BattlePhysicalCallbackPlanner.build(input)
}
