// Battle Combat
package com.jojo.game.presentation.battle.combat

import com.jojo.game.domain.battle.MagicTarget
import com.jojo.game.domain.battle.PhysicalAttackPass
import com.jojo.game.domain.battle.TacticalActionResult
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.presentation.battle.timeline.BattleMagicPresentation

/** BattleCombatPresentationQueueCoordinator: 전투 결과를 mutable 화면 대기열에 넣기 전 immutable pass·queue 계획으로 변환한다. */
internal object BattleCombatPresentationQueueCoordinator {
    /** UnitVisualState: queue의 시각 HP·MP 계산에 필요한 유닛의 불변 상한·현재값이다. */
    internal data class UnitVisualState(
        /**
         * `hitPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitPoints: Int,
        /**
         * `maxHitPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxHitPoints: Int,
        /**
         * `magicPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magicPoints: Int,
        /**
         * `maxMagicPoints` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val maxMagicPoints: Int,
    )

    /** VisualState: 다음 pass가 이어 받을 HP·MP snapshot이다. */
    internal data class VisualState(
        /**
         * `hitPoints` (Map<String, Int>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hitPoints: Map<String, Int>,
        /**
         * `magicPoints` (Map<String, Int>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val magicPoints: Map<String, Int>,
    )

    /** PhysicalQueuePlan: 첫 물리 pass 뒤에 사용할 queue 순서와 시각 자원 상태를 보관한다. */
    internal data class PhysicalQueuePlan(
        /**
         * `passes` (List<PhysicalAttackPass>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val passes: List<PhysicalAttackPass>,
        /**
         * `nextPassIndex` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val nextPassIndex: Int,
        /**
         * `visualState` (VisualState,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visualState: VisualState,
        /**
         * `counterMagicId` (Int?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val counterMagicId: Int?,
        /**
         * `counterMagic` (TacticalActionResult.Magic?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val counterMagic: TacticalActionResult.Magic?,
        /**
         * `counterCasterId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val counterCasterId: String,
        /**
         * `counterTargetId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val counterTargetId: String,
    ) {
        /** 첫 pass가 끝난 뒤에도 queue 소비를 계속해야 하는지 반환한다. */
        fun continuesAfterCurrentPass(): Boolean = nextPassIndex < passes.size || counterMagic != null
    }

    /** MagicQueuePlan: 첫 마법 pass 뒤의 remaining pass queue와 다음 시각에 적용할 시각 상태를 보관한다. */
    internal data class MagicQueuePlan(
        /**
         * `visualState` (VisualState,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visualState: VisualState,
        /**
         * `nextPassIndex` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val nextPassIndex: Int = 1,
    )

    /** 첫 물리 pass가 명중했을 때, 기존 순서와 MP shield 보정으로 queue 계획을 만든다. */
    fun hitPhysicalQueuePlan(
        result: TacticalActionResult.Attack,
        actorId: String,
        targetId: String,
        healthBeforeAction: Map<String, Int>,
        deferredInitialMp: Map<String, Int?>,
        units: Map<String, UnitVisualState>,
    ): PhysicalQueuePlan? {
        if (!result.hit || result.physicalPasses.firstOrNull()?.targets?.isEmpty() != false) return null
        /**
         * `visualMp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visualMp = linkedMapOf<String, Int>()
        result.physicalPasses.flatMap(PhysicalAttackPass::targets)
            .groupBy { it.targetId }
            .forEach { (id, results) ->
                /**
                 * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val unit = units[id] ?: return@forEach
                visualMp[id] = deferredInitialMp[id]
                    ?: (unit.magicPoints + results.sumOf { it.mpShieldDamage - it.automaticPropertyMpDelta })
                        .coerceIn(0, unit.maxMagicPoints)
            }
        return PhysicalQueuePlan(
            passes = result.physicalPasses,
            nextPassIndex = 1,
            visualState = VisualState(healthBeforeAction.toMap(), visualMp),
            counterMagicId = result.counterMagicId,
            counterMagic = result.counterMagic,
            counterCasterId = targetId,
            counterTargetId = actorId,
        )
    }

    /** 첫 물리 pass가 빗나갔을 때, 남은 pass와 반격 마법을 기존 순서로 대기열에 남긴다. */
    fun missedPhysicalQueuePlan(
        result: TacticalActionResult.Attack,
        actorId: String,
        targetId: String,
        healthBeforeAction: Map<String, Int>,
        deferredInitialMp: Map<String, Int?>,
        units: Map<String, UnitVisualState>,
    ): PhysicalQueuePlan? {
        /**
         * `passes` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val passes = result.physicalPasses.drop(1)
        if (passes.isEmpty() && result.counterMagic == null) return null
        /**
         * `visualMp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visualMp = listOf(actorId, targetId).mapNotNull { id ->
            units[id]?.let { unit -> id to (deferredInitialMp[id] ?: unit.magicPoints) }
        }.toMap()
        return PhysicalQueuePlan(
            passes = passes,
            nextPassIndex = 0,
            visualState = VisualState(healthBeforeAction.toMap(), visualMp),
            counterMagicId = result.counterMagicId,
            counterMagic = result.counterMagic,
            counterCasterId = targetId,
            counterTargetId = actorId,
        )
    }

    /** 한 마법 pass의 효과를 시각 snapshot에 적용한다. Screen은 반환값을 queue mutation에만 반영한다. */
    fun advanceMagicVisualState(
        pass: List<MagicTarget>,
        casterId: String,
        profile: GameDataCatalog.MagicProfile?,
        current: VisualState,
        units: Map<String, UnitVisualState>,
    ): VisualState {
        /**
         * `hp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hp = current.hitPoints.toMutableMap()
        /**
         * `mp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mp = current.magicPoints.toMutableMap()
        BattleMagicPresentation.changes(pass, casterId, profile).forEach { change ->
            /**
             * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val unit = units[change.unitId] ?: return@forEach
            hp[change.unitId] = ((hp[change.unitId] ?: unit.hitPoints) + change.hpAdd)
                .coerceIn(0, unit.maxHitPoints)
            mp[change.unitId] = ((mp[change.unitId] ?: unit.magicPoints) + change.mpAdd)
                .coerceIn(0, unit.maxMagicPoints)
        }
        return VisualState(hp, mp)
    }

    /** 첫 마법 pass 직후, 후속 pass가 있으면 immutable queue 계획을 만든다. */
    fun deferredMagicQueuePlan(
        result: TacticalActionResult.Magic,
        casterId: String,
        profile: GameDataCatalog.MagicProfile?,
        current: VisualState,
        units: Map<String, UnitVisualState>,
    ): MagicQueuePlan? {
        if (result.passes.size <= 1) return null
        return MagicQueuePlan(advanceMagicVisualState(result.passes.first(), casterId, profile, current, units))
    }
}
