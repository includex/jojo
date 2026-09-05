package com.jojo.game.presentation.battle.timeline

import com.jojo.game.*
import com.jojo.game.domain.battle.*

/**
 * Renderer-independent implementation of the await/callback order in
 * `BattleScreen._attack2`, `_attack3`, `_attack6`, and the caller's final
 * `_jiesuan(g_charinfo)`.
 *
 * The battle model is allowed to resolve values eagerly, but a renderer must
 * consume these steps in order and may advance past a step with
 * [Step.awaitsCallback] only when the matching source animation/panel callback
 * has completed. In particular, XXGJ/QXL/XSJQ mutate the source model (and
 * therefore its live HP bar/money state) before the hurt clip; only their
 * accumulated settlement/info rows wait for [GlobalSettlement].
 *
 * Source authority:
 * - recovered BattleLayer.js `_attack2` cases 8..20: attack hit callback,
 *   sequential target `_attack3` calls, then the next continuous pass.
 * - `_attack3` cases 0..9: focus; block/hurt callback; recoil; ZDSY callback;
 *   local status settlement.
 * - `_attack6`: CLFJ magic counter completes before a physical counter is
 *   considered.
 * - `_ai2` / player operation tail: `_jiesuan(g_charinfo)` occurs only after
 *   the complete active/counter chain.
 */
/**
 * object  `BattlePhysicalCallbackPlan`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

object BattlePhysicalCallbackPlan {
    /**
     * enum class  `InvocationKind`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class InvocationKind { ACTIVE, ACTIVE_FOLLOW_UP, COUNTER, COUNTER_FOLLOW_UP }

    /**
     * data class  `Target`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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
        /** TPGJ moveTo runs concurrently with hurt and publishes its tile at .08s. */
        val backMove: PhysicalBackMove? = null,
        /** Whether `_jiesuan(t, o)` has target-status work to present. */
        val hasLocalStatusSettlement: Boolean = false,
    )

    /**
     * data class  `PropertyUse`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class PropertyUse(val itemId: Int, val itemName: String = "")

    /**
     * enum class  `BlockRetaliationKind`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class BlockRetaliationKind { MENG_JI_CONFUSION, NI_FAN_PARALYSIS }

    /**
     * data class  `BlockRetaliation`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class BlockRetaliation(val kind: BlockRetaliationKind, val damage: Int)

    /**
     * data class  `Invocation`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Invocation(
        val kind: InvocationKind,
        val attackerId: String,
        /** countAtkHarm target order: primary first, then CTGJ targets. */
        val targets: List<Target>,
    )

    /**
     * data class  `CounterMagic`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class CounterMagic(
        val casterId: String,
        val targetId: String,
        val magicId: Int,
        val magicName: String = "",
    )

    /**
     * data class  `Input`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class Input(
        val invocations: List<Invocation>,
        /** CLFJ, when present, belongs between active invocations and counters. */
        val counterMagic: CounterMagic? = null,
        /** Exact insertion order of source g_charinfo.index. */
        val globalSettlementUnitIds: List<String>,
    )

    sealed interface Step {
        val awaitsCallback: Boolean

        /**
         * data class  `AttackUntilHit`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class AttackUntilHit(
            val kind: InvocationKind,
            val attackerId: String,
        ) : Step {
            override val awaitsCallback = true
        }

        /**
         * data class  `FocusTarget`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class FocusTarget(val targetId: String) : Step {
            override val awaitsCallback = false
        }

        /** Source anime26 `func`, including block sound/number lifetime. */
        data class GuardUntilComplete(val attackerId: String, val targetId: String) : Step {
            override val awaitsCallback = true
        }

        /**
         * data class  `BlockRetaliationCommitted`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class BlockRetaliationCommitted(
            val sourceId: String,
            val targetId: String,
            val retaliation: BlockRetaliation,
        ) : Step {
            override val awaitsCallback = false
        }

        /**
         * data class  `MpShieldCommitted`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class MpShieldCommitted(val targetId: String, val mpDamage: Int) : Step {
            override val awaitsCallback = false
        }

        /**
         * data class  `MoneyShieldCommitted`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

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

        /**
         * data class  `LifeStealCommitted`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class LifeStealCommitted(val attackerId: String, val healing: Int) : Step {
            override val awaitsCallback = false
        }

        /**
         * data class  `QxlCommitted`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class QxlCommitted(val attackerId: String, val healing: Int) : Step {
            override val awaitsCallback = false
        }

        /**
         * data class  `MoneyAbsorbCommitted`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class MoneyAbsorbCommitted(
            val attackerId: String,
            val playerDelta: Int,
            val enemyDelta: Int,
        ) : Step {
            override val awaitsCallback = false
        }

        /** Source anime32 `func`; harm number is cleared at this boundary. */
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

        /**
         * data class  `RecoilCommitted`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class RecoilCommitted(
            val sourceId: String,
            val targetId: String,
            val damage: Int,
        ) : Step {
            override val awaitsCallback = false
        }

        /** Entire `_usePro2`: rise .5s, reparent, fade 1s, then item effect. */
        data class AutomaticPropertyUntilComplete(
            val unitId: String,
            val property: PropertyUse,
        ) : Step {
            override val awaitsCallback = true
        }

        /** `_jiesuan(t, o)` for status changes belonging to this target hit. */
        data class LocalStatusSettlementUntilComplete(val unitId: String) : Step {
            override val awaitsCallback = true
        }

        /** `_magic` invoked by CLFJ; a successful one suppresses physical counter. */
        data class CounterMagicUntilComplete(val counter: CounterMagic) : Step {
            override val awaitsCallback = true
        }

        /** One source g_charinfo unit, including its center/info callbacks. */
        data class GlobalSettlement(val unitId: String) : Step {
            override val awaitsCallback = true
        }
    }

    /**
     * 공개 메서드 `build`
     *
     * ### 파라미터
    - `input` (`Input`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `List<Step>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun build(input: Input): List<Step> = buildList {
        val firstCounter = input.invocations.indexOfFirst {
            it.kind == InvocationKind.COUNTER || it.kind == InvocationKind.COUNTER_FOLLOW_UP
        }.let { if (it < 0) input.invocations.size else it }

        /**
         * 공개 메서드 `appendInvocation`
         *
         * ### 파라미터
        - `invocation` (`Invocation`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `Unit`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun appendInvocation(invocation: Invocation) {
            add(Step.AttackUntilHit(invocation.kind, invocation.attackerId))
            invocation.targets.forEach { target ->
                add(Step.FocusTarget(target.targetId))
                if (target.harm == 0 && target.mpShieldDamage == 0) {
                    add(Step.GuardUntilComplete(invocation.attackerId, target.targetId))
                    target.blockRetaliations.forEach { retaliation ->
                        add(Step.BlockRetaliationCommitted(target.targetId, invocation.attackerId, retaliation))
                    }
                } else {
                    if (target.mpShieldDamage > 0) {
                        add(Step.MpShieldCommitted(target.targetId, target.mpShieldDamage))
                    } else if (target.moneyShieldSpent > 0) {
                        add(Step.MoneyShieldCommitted(target.targetId, target.moneyShieldSpent))
                    }
                    add(
                        Step.TargetHarmCommitted(
                            invocation.attackerId,
                            target.targetId,
                            hpDamage = if (target.mpShieldDamage > 0) 0 else target.harm,
                            mpDamage = target.mpShieldDamage,
                        ),
                    )
                    // MPFY's outer break skips all three source benefits.
                    if (target.mpShieldDamage == 0) {
                        if (target.lifeStealHealing > 0) add(
                            Step.LifeStealCommitted(
                                invocation.attackerId,
                                target.lifeStealHealing
                            )
                        )
                        if (target.qxlHealing > 0) add(Step.QxlCommitted(invocation.attackerId, target.qxlHealing))
                        if (target.playerMoneyDelta != 0 || target.enemyMoneyDelta != 0) {
                            add(
                                Step.MoneyAbsorbCommitted(
                                    invocation.attackerId,
                                    target.playerMoneyDelta,
                                    target.enemyMoneyDelta
                                )
                            )
                        }
                    }
                    add(
                        Step.HurtUntilComplete(
                            invocation.attackerId, target.targetId, target.harm, target.mpShieldDamage, target.backMove,
                        )
                    )
                    if (target.recoilDamage > 0) {
                        add(Step.RecoilCommitted(target.targetId, invocation.attackerId, target.recoilDamage))
                    }
                    target.automaticProperty?.let {
                        add(Step.AutomaticPropertyUntilComplete(target.targetId, it))
                    }
                }
                if (target.hasLocalStatusSettlement) {
                    add(Step.LocalStatusSettlementUntilComplete(target.targetId))
                }
            }
        }

        input.invocations.take(firstCounter).forEach(::appendInvocation)
        input.counterMagic?.let { add(Step.CounterMagicUntilComplete(it)) }
        // A successful CLFJ call suppresses physical counter entirely in
        // `_attack6`; callers must therefore not include counter invocations
        // when counterMagic is present.
        if (input.counterMagic == null) input.invocations.drop(firstCounter).forEach(::appendInvocation)
        input.globalSettlementUnitIds.forEach { add(Step.GlobalSettlement(it)) }
    }
}

