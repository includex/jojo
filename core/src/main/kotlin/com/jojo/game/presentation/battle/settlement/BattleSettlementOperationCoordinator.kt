// Battle Settlement
package com.jojo.game.presentation.battle.settlement

import com.jojo.game.domain.battle.BattleUnit
import com.jojo.game.domain.battle.Faction
import com.jojo.game.domain.battle.MagicLocalSettlement
import com.jojo.game.domain.battle.settlement.BattleSettlementPlan
import com.jojo.game.domain.battle.settlement.CampSettlement
import com.jojo.game.domain.battle.settlement.SettlementAuthoredSubflowPlan
import com.jojo.game.domain.battle.settlement.SettlementAuraStep
import com.jojo.game.domain.battle.settlement.SettlementGrowthStep
import com.jojo.game.domain.battle.settlement.SettlementInfoKind
import com.jojo.game.application.battle.BattleSettlementPlanningAdapter
import com.jojo.game.domain.campaign.CampaignEquipmentSlot

/** 정산 계획 화면 포트: operation 계획이 참조할 현재 유닛, 원본 명칭, 애니메이션 지속시간만 제공한다. */
internal interface BattleSettlementOperationPort {
    /** 정산 대상 유닛: 전장과 보류 표현 유닛을 ID별로 조회할 수 있는 현재 스냅샷이다. */
    fun unitsById(): Map<String, BattleUnit>

    /** 표현 유닛 조회: 성장 안내와 동작 지속시간에 사용할 현재 프레젠테이션 유닛을 반환한다. */
    fun presentationUnit(unitId: String): BattleUnit?

    /** 상태 효과 조회: 상태 원본 인덱스와 슬롯에 대응하는 meff 식별자를 반환한다. */
    fun statusMeff(sourceStatusIndex: Int, meffSlot: Int): Int?

    /** 특기 이름 조회: local aura 안내에 표시할 원본 특기명을 반환한다. */
    fun skillName(skillId: Int): String

    /** 법술 이름 조회: 성장 보상 안내에 표시할 원본 법술명을 반환한다. */
    fun magicName(magicId: Int): String?

    /** 이름 기반 효과 조회: authored aura 의미 이름에 대응하는 meff 식별자를 반환한다. */
    fun namedMeff(name: String): Int?

    /** 동작 지속시간 조회: 유닛 방향에서 action atlas가 끝나는 시각을 반환한다. */
    fun actionDuration(actionId: Int, direction: Int): Float

    /** 효과 지속시간 조회: meff 애니메이션이 끝나는 시각을 반환한다. */
    fun meffDuration(effectId: Int): Float?

    /** 안내 자동 닫기: 현재 설정에서 Info2 안내가 자동으로 닫히는지 반환한다. */
    fun autoCloseInfo2(text: String): Boolean
}

/** 정산 operation 조정자: 전투·마법 정산 원시 결과를 화면 실행기가 소비할 순차 operation 계획으로 변환한다. */
internal class BattleSettlementOperationCoordinator {
    /** 일반 정산 계획: 상태 payload를 검증한 뒤 화면 operation과 함께 반환한다. */
    fun turnSettlement(settlement: CampSettlement, port: BattleSettlementOperationPort): BattleSettlementOperationPlan {
        val plan = BattleSettlementPlanningAdapter.plan(settlement, port.unitsById()) { state ->
            port.statusMeff(state.sourceStatusIndex, state.meffSlot)
        }
        if (!plan.sourceDataComplete) {
            val missing = plan.pendingIntegrations.joinToString { pending ->
                "${pending.kind}:${pending.unitIds.joinToString("/")}"
            }
            error("Incomplete authored settlement payload: $missing")
        }
        return BattleSettlementOperationPlan(plan, operations(plan, port))
    }

    /** 마법 local 정산 계획: 시전자 진영과 현재 유닛 정보를 결합해 화면 operation과 함께 반환한다. */
    fun magicLocalSettlement(
        settlement: MagicLocalSettlement,
        casterId: String,
        port: BattleSettlementOperationPort,
    ): BattleSettlementOperationPlan {
        /**
         * `camp` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val camp = port.presentationUnit(casterId)?.effectiveFaction() ?: Faction.PLAYER
        /**
         * `plan` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val plan = BattleSettlementPlanningAdapter.planMagicLocal(settlement, camp, port.unitsById()) { state ->
            port.statusMeff(state.sourceStatusIndex, state.meffSlot)
        }
        return BattleSettlementOperationPlan(plan, operations(plan, port))
    }

    /** operation 계획: authored subflow와 정산 대상 변화를 화면 실행 순서의 명령 목록으로 조립한다. */
    fun operations(plan: BattleSettlementPlan, port: BattleSettlementOperationPort): List<TurnSettlementOp> = buildList {
        plan.authoredSubflows.forEach { subflow ->
            when (subflow) {
                is SettlementAuthoredSubflowPlan.LocalAura -> subflow.steps.forEach { step ->
                    when (step) {
                        is SettlementAuraStep.Focus -> add(TurnSettlementOp.Focus(subflow.casterId, step.seconds, forceCenter = true))
                        is SettlementAuraStep.Sound -> add(TurnSettlementOp.Sound(step.soundIndex))
                        is SettlementAuraStep.Info2 -> add(
                            TurnSettlementOp.Info2(port.skillName(step.skillId).ifBlank { "특기 ${step.skillId}" }),
                        )
                        is SettlementAuraStep.ActionFinished -> add(TurnSettlementOp.Actions(subflow.casterId, listOf(step.actionId)))
                        is SettlementAuraStep.PlayMeff -> port.namedMeff(step.semanticName)?.let { effectId ->
                            add(TurnSettlementOp.Meff(effectId, step.targetIds))
                        } ?: error("GAME_CFG.meff.${step.semanticName} is missing")
                        SettlementAuraStep.NestedSettlement -> addAll(operations(subflow.nestedSettlement, port))
                        SettlementAuraStep.DefaultAction -> add(TurnSettlementOp.Default(subflow.casterId))
                    }
                }

                is SettlementAuthoredSubflowPlan.Growth -> subflow.steps.forEach { step ->
                    when (step) {
                        is SettlementGrowthStep.InfoValues -> add(TurnSettlementOp.GrowthInfo(subflow.unitId, step.grants))
                        is SettlementGrowthStep.AbilityLevelUp -> add(TurnSettlementOp.Info2("${step.attribute.name} 상승"))
                        SettlementGrowthStep.UnitLevelUpActionFinished -> add(TurnSettlementOp.Actions(subflow.unitId, listOf(11)))
                        SettlementGrowthStep.UnitLevelUpInfo -> {
                            val unit = port.presentationUnit(subflow.unitId)
                            add(TurnSettlementOp.Info2("${unit?.name.orEmpty()} 승격하여${unit?.level ?: 0}레벨"))
                        }
                        is SettlementGrowthStep.LearnedMagicInfo -> add(
                            TurnSettlementOp.Info2("법술 「${port.magicName(step.magicId) ?: step.magicId}」！"),
                        )
                        is SettlementGrowthStep.EquipmentLevelUpAction -> add(
                            TurnSettlementOp.Actions(
                                subflow.unitId,
                                if (step.result.slot == CampaignEquipmentSlot.WEAPON) listOf(12, 7) else listOf(12, 33),
                            ),
                        )
                        is SettlementGrowthStep.EquipmentLevelUpInfo -> add(
                            TurnSettlementOp.Info2(
                                if (step.result.slot == CampaignEquipmentSlot.WEAPON) "무기레벨 상승!" else "보구레벨 상승!",
                            ),
                        )
                        is SettlementGrowthStep.ItemUpgradeCallback -> add(TurnSettlementOp.ItemUpgrade(subflow.unitId, step.result))
                        SettlementGrowthStep.DefaultAction -> add(TurnSettlementOp.Default(subflow.unitId))
                    }
                }
            }
        }
        plan.units.forEach { unit ->
            add(TurnSettlementOp.Focus(unit.unitId, 0f, forceCenter = false))
            if (unit.hasStatesPayload) add(TurnSettlementOp.HideState(listOf(unit.unitId)))
            if (unit.infoDeltas.isNotEmpty()) {
                add(TurnSettlementOp.UnitInfo(unit))
                if (unit.infoDeltas.any { it.kind == SettlementInfoKind.HP }) add(TurnSettlementOp.Default(unit.unitId))
            }
        }
        plan.meffBuckets.forEach { bucket ->
            bucket.key.actualMeffId?.let { effectId -> add(TurnSettlementOp.Meff(effectId, bucket.targets.map { it.unitId })) }
        }
        val refreshIds = plan.units.map { it.unitId }
        if (refreshIds.isNotEmpty()) add(TurnSettlementOp.Refresh(refreshIds))
    }

    /** local 정산 시간: 예약된 operation이 화면 실행기를 점유할 최대 시간을 action·meff·안내 규칙으로 계산한다. */
    fun localDuration(operations: List<TurnSettlementOp>, port: BattleSettlementOperationPort): Float = operations.sumOf { operation ->
        when (operation) {
            is TurnSettlementOp.Focus -> operation.seconds.toDouble()
            is TurnSettlementOp.Actions -> operation.actionIds.sumOf { actionId ->
                port.presentationUnit(operation.unitId)?.let { unit -> port.actionDuration(actionId, unit.direction).toDouble() } ?: 0.0
            }
            is TurnSettlementOp.UnitInfo -> operation.plan.infoBarrierSeconds.toDouble()
            is TurnSettlementOp.GrowthInfo -> {
                val ticks = operation.grants.sumOf { grant ->
                    val delta = grant.unitResult?.gained ?: grant.equipmentResult?.gained ?: 0
                    minOf(kotlin.math.abs(delta), 5)
                }
                (.1f + ticks * .2f + .3f).toDouble()
            }
            is TurnSettlementOp.Meff -> port.meffDuration(operation.effectId)?.toDouble() ?: 0.0
            is TurnSettlementOp.Info2 -> if (port.autoCloseInfo2(operation.text)) operation.text.length * .04 + 1.0 else Double.POSITIVE_INFINITY
            is TurnSettlementOp.ItemUpgrade -> Double.POSITIVE_INFINITY
            is TurnSettlementOp.Sound,
            is TurnSettlementOp.HideState,
            is TurnSettlementOp.Refresh,
            is TurnSettlementOp.Default -> 0.0
        }
    }.toFloat()
}

/** 정산 operation 계획: 실행기가 시작할 정산 상태 계획과 순차 표시 명령 목록을 함께 보관한다. */
internal data class BattleSettlementOperationPlan(
    /** 정산 상태 계획: 완료 시 유닛 반영과 후속 처리를 위한 원시 정산 결과다. */
    val settlementPlan: BattleSettlementPlan,
    /** 표시 operation: focus·동작·효과·정보창을 실행 순서대로 정의한다. */
    val operations: List<TurnSettlementOp>,
)
