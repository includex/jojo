// Battle Combat
package com.jojo.game.presentation.battle.combat

import com.jojo.game.domain.battle.MagicTarget
import com.jojo.game.infrastructure.data.GameDataCatalog
import com.jojo.game.presentation.battle.timeline.BattleMagicPresentation

/** BattleMagicPresentationPlanner: 한 마법 pass의 effect 시간·피격 반응·시각 HP/MP 변화를 immutable timeline으로 계산한다. */
internal object BattleMagicPresentationPlanner {
    /** UnitState: 마법 pass 계산에 필요한 대상의 현재 자원과 방향을 고정한다. */
    internal data class UnitState(
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
        /**
         * `direction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val direction: Int,
    )

    /** TargetInput: 대상 결과와 원본 sprite에서 조회한 반응 지속 시간을 묶는다. */
    internal data class TargetInput(
        /**
         * `target` (MagicTarget,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val target: MagicTarget,
        /**
         * `reactionDuration` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val reactionDuration: Float,
    )

    /** Input: Screen이 조회한 effect·sprite·unit snapshot을 planner가 소비할 불변 값으로 전달한다. */
    internal data class Input(
        /**
         * `casterId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val casterId: String,
        /**
         * `profile` (GameDataCatalog.MagicProfile?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val profile: GameDataCatalog.MagicProfile?,
        /**
         * `effectStartedAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val effectStartedAt: Float,
        /**
         * `effectEndsAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val effectEndsAt: Float,
        /**
         * `effectHitOffset` (Float?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val effectHitOffset: Float?,
        /**
         * `targets` (List<TargetInput>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targets: List<TargetInput>,
        /**
         * `visualState` (BattleCombatPresentationQueueCoordinator.VisualState,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visualState: BattleCombatPresentationQueueCoordinator.VisualState,
        /**
         * `units` (Map<String, UnitState>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val units: Map<String, UnitState>,
    )

    /** Reaction: 대상 action·방향·시작·종료를 실제 animation mutation 전에 고정한다. */
    internal data class Reaction(
        /**
         * `targetId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetId: String,
        /**
         * `sourceAction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val sourceAction: Int,
        /**
         * `direction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val direction: Int,
        /**
         * `startsAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val startsAt: Float,
        /**
         * `endsAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val endsAt: Float,
    )

    /** Change: 한 대상의 시각 자원 전후값과 harm number가 사용할 action을 보관한다. */
    internal data class Change(
        /**
         * `unitId` (String,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unitId: String,
        /**
         * `hpAdd` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hpAdd: Int,
        /**
         * `mpAdd` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mpAdd: Int,
        /**
         * `hpBefore` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hpBefore: Int,
        /**
         * `hpAfter` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val hpAfter: Int,
        /**
         * `mpBefore` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mpBefore: Int,
        /**
         * `mpAfter` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mpAfter: Int,
        /**
         * `harmNumberValue` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val harmNumberValue: Int,
        /**
         * `harmNumberIsHp` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val harmNumberIsHp: Boolean,
        /**
         * `harmNumberAction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val harmNumberAction: Int,
    )

    /** Plan: Screen이 mutation·health timeline·harm number를 예약할 완성된 한 pass timeline이다. */
    internal data class Plan(
        /**
         * `effectAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val effectAt: Float,
        /**
         * `effectEndsAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val effectEndsAt: Float,
        /**
         * `mcall` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val mcall: Boolean,
        /**
         * `primaryFocusId` (String?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val primaryFocusId: String?,
        /**
         * `targetFocusAt` (Float,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetFocusAt: Float,
        /**
         * `targetIds` (List<String>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val targetIds: List<String>,
        /**
         * `reactions` (List<Reaction>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val reactions: List<Reaction>,
        /**
         * `changes` (List<Change>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val changes: List<Change>,
        /**
         * `nextVisualState` (BattleCombatPresentationQueueCoordinator.VisualState,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val nextVisualState: BattleCombatPresentationQueueCoordinator.VisualState,
    )

    /** plan: 기존 magic presentation 규칙으로 시간선과 자원 상태를 계산하며 Screen state를 변경하지 않는다. */
    fun plan(input: Input): Plan {
        val effectAt = input.effectStartedAt + (input.effectHitOffset ?: (input.effectEndsAt - input.effectStartedAt))
        val mcall = (input.profile?.effectId ?: 0) in 100..254
        val targetIds = input.targets.map { it.target.targetId }
        val reactions = input.targets.mapNotNull { targetInput ->
            val unit = input.units[targetInput.target.targetId] ?: return@mapNotNull null
            val sourceAction = if (targetInput.target.hit) 3 else 26
            Reaction(
                targetId = targetInput.target.targetId,
                sourceAction = sourceAction,
                direction = unit.direction,
                startsAt = effectAt,
                endsAt = maxOf(input.effectEndsAt, effectAt + targetInput.reactionDuration),
            )
        }
        val hp = input.visualState.hitPoints.toMutableMap()
        val mp = input.visualState.magicPoints.toMutableMap()
        val changes = BattleMagicPresentation.changes(input.targets.map(TargetInput::target), input.casterId, input.profile)
            .mapNotNull { change ->
                val unit = input.units[change.unitId] ?: return@mapNotNull null
                val hpBefore = hp[change.unitId] ?: unit.hitPoints - change.hpAdd
                val hpAfter = (hpBefore + change.hpAdd).coerceIn(0, unit.maxHitPoints)
                val mpBefore = mp[change.unitId] ?: unit.magicPoints
                val mpAfter = (mpBefore + change.mpAdd).coerceIn(0, unit.maxMagicPoints)
                if (change.hpAdd != 0) hp[change.unitId] = hpAfter
                if (change.mpAdd != 0) mp[change.unitId] = mpAfter
                val value = if (change.mpAdd != 0) change.mpAdd else change.hpAdd
                Change(
                    unitId = change.unitId,
                    hpAdd = change.hpAdd,
                    mpAdd = change.mpAdd,
                    hpBefore = hpBefore,
                    hpAfter = hpAfter,
                    mpBefore = mpBefore,
                    mpAfter = mpAfter,
                    harmNumberValue = value,
                    harmNumberIsHp = change.mpAdd == 0,
                    harmNumberAction = if (change.mpAdd != 0) 3 else 32,
                )
            }
        return Plan(
            effectAt = effectAt,
            effectEndsAt = input.effectEndsAt,
            mcall = mcall,
            primaryFocusId = targetIds.firstOrNull().takeIf { mcall },
            targetFocusAt = if (mcall) input.effectEndsAt else input.effectStartedAt,
            targetIds = targetIds,
            reactions = reactions,
            changes = changes,
            nextVisualState = BattleCombatPresentationQueueCoordinator.VisualState(hp, mp),
        )
    }
}
