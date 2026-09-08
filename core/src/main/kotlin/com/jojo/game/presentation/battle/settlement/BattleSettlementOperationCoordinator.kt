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
import com.jojo.game.domain.battle.settlement.SettlementGrowthGrant
import com.jojo.game.domain.battle.settlement.SettlementInfoKind
import com.jojo.game.domain.battle.settlement.SettlementInfoPanel
import com.jojo.game.domain.battle.settlement.SettlementUnitPlan
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
    fun turnSettlement(
        settlement: CampSettlement,
        port: BattleSettlementOperationPort,
        mergeGrowthFor: Set<String> = emptySet(),
    ): BattleSettlementOperationPlan {
        val plan = BattleSettlementPlanningAdapter.plan(settlement, port.unitsById()) { state ->
            port.statusMeff(state.sourceStatusIndex, state.meffSlot)
        }
        if (!plan.sourceDataComplete) {
            val missing = plan.pendingIntegrations.joinToString { pending ->
                "${pending.kind}:${pending.unitIds.joinToString("/")}"
            }
            error("Incomplete authored settlement payload: $missing")
        }
        return BattleSettlementOperationPlan(plan, operations(plan, port, mergeGrowthFor))
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
    /**
     * operation 계획: authored subflow와 정산 대상 변화를 화면 실행 순서의 명령 목록으로 조립한다.
     *
     * 순서는 원본 `_jiesuan`을 따른다. 원본은 `g_charinfo.index`를 한 번 돌면서 유닛마다
     * 상태창(case 5~9) -> 기본 동작(case 10) -> 승격·법술·장비 연출(case 11~)을 차례로
     * 재생한다. 그래서 성장 연출은 유닛 목록 앞에 몰아 내보내지 않고 해당 유닛의 상태창
     * 바로 뒤에 붙인다. 지역 오라(`LocalAura`)는 그 앞의 별도 흐름이라 위치를 유지한다.
     *
     * [mergeGrowthFor]에 든 유닛은 체력·기력과 경험치를 한 장의 상태창으로 합쳐 보여 준다.
     * 원본은 유닛마다 `O` 하나를 만들어 `MineUnitInfoLayer`를 한 번만 띄우므로, 성장 흐름의
     * `InfoValues` 단계는 따로 내보내지 않고 그 유닛의 `UnitInfo`에 붙인다.
     */
    fun operations(
        plan: BattleSettlementPlan,
        port: BattleSettlementOperationPort,
        mergeGrowthFor: Set<String> = emptySet(),
    ): List<TurnSettlementOp> = buildList {
        val mergedGrants = linkedMapOf<String, List<SettlementGrowthGrant>>()
        if (mergeGrowthFor.isNotEmpty()) {
            plan.authoredSubflows.filterIsInstance<SettlementAuthoredSubflowPlan.Growth>()
                .filter { it.unitId in mergeGrowthFor }
                .forEach { subflow ->
                    subflow.steps.filterIsInstance<SettlementGrowthStep.InfoValues>()
                        .flatMap { it.grants }
                        .takeIf { it.isNotEmpty() }
                        ?.let { mergedGrants[subflow.unitId] = it }
                }
        }
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

                // 성장 흐름은 해당 유닛의 상태창 뒤에 붙이므로 여기서는 내보내지 않는다.
                is SettlementAuthoredSubflowPlan.Growth -> Unit
            }
        }
        val growthByUnit = plan.authoredSubflows.filterIsInstance<SettlementAuthoredSubflowPlan.Growth>()
            .groupBy({ it.unitId }, { it.steps })
            .mapValues { (_, steps) -> steps.flatten() }
        val settledUnits = mutableSetOf<String>()
        plan.units.forEach { unit ->
            settledUnits += unit.unitId
            add(TurnSettlementOp.Focus(unit.unitId, 0f, forceCenter = false))
            if (unit.hasStatesPayload) add(TurnSettlementOp.HideState(listOf(unit.unitId)))
            val grants = mergedGrants[unit.unitId].orEmpty()
            if (unit.infoDeltas.isNotEmpty() || grants.isNotEmpty()) {
                add(TurnSettlementOp.UnitInfo(unit, grants))
                if (unit.infoDeltas.any { it.kind == SettlementInfoKind.HP }) add(TurnSettlementOp.Default(unit.unitId))
            }
            addGrowth(unit.unitId, growthByUnit[unit.unitId].orEmpty(), mergedGrants, port)
        }
        // 체력·기력 변화 없이 경험치만 받은 유닛도 원본은 상태창을 한 번 띄운다.
        growthByUnit.forEach { (unitId, steps) ->
            if (unitId in settledUnits) return@forEach
            add(TurnSettlementOp.Focus(unitId, 0f, forceCenter = false))
            mergedGrants[unitId]?.let { grants ->
                add(
                    TurnSettlementOp.UnitInfo(
                        SettlementUnitPlan(
                            unitId,
                            port.presentationUnit(unitId)?.faction ?: Faction.PLAYER,
                            Faction.PLAYER, Faction.PLAYER,
                            SettlementInfoPanel.MINE, emptyList(), emptyList(),
                        ),
                        grants,
                    ),
                )
            }
            addGrowth(unitId, steps, mergedGrants, port)
        }
        plan.meffBuckets.forEach { bucket ->
            bucket.key.actualMeffId?.let { effectId -> add(TurnSettlementOp.Meff(effectId, bucket.targets.map { it.unitId })) }
        }
        val refreshIds = plan.units.map { it.unitId }
        if (refreshIds.isNotEmpty()) add(TurnSettlementOp.Refresh(refreshIds))
    }

    /**
     * `addGrowth`: 유닛 하나의 성장 연출을 원본 `_jiesuan` case 11 이후 순서대로 덧붙인다.
     *
     * 상태창에 합쳐 보여 준 유닛은 `InfoValues`를 다시 내보내지 않는다.
     */
    private fun MutableList<TurnSettlementOp>.addGrowth(
        unitId: String,
        steps: List<SettlementGrowthStep>,
        mergedGrants: Map<String, List<SettlementGrowthGrant>>,
        port: BattleSettlementOperationPort,
    ) {
        steps.forEach { step ->
            when (step) {
                is SettlementGrowthStep.InfoValues ->
                    if (unitId !in mergedGrants) add(TurnSettlementOp.GrowthInfo(unitId, step.grants))
                is SettlementGrowthStep.AbilityLevelUp -> add(TurnSettlementOp.Info2("${step.attribute.name} 상승"))
                SettlementGrowthStep.UnitLevelUpActionFinished -> add(TurnSettlementOp.Actions(unitId, listOf(11)))
                SettlementGrowthStep.UnitLevelUpInfo -> {
                    val unit = port.presentationUnit(unitId)
                    add(TurnSettlementOp.Info2("${unit?.name.orEmpty()} 승격하여${unit?.level ?: 0}레벨"))
                }
                is SettlementGrowthStep.LearnedMagicInfo -> add(
                    TurnSettlementOp.Info2("법술 「${port.magicName(step.magicId) ?: step.magicId}」！"),
                )
                is SettlementGrowthStep.EquipmentLevelUpAction -> add(
                    TurnSettlementOp.Actions(
                        unitId,
                        if (step.result.slot == CampaignEquipmentSlot.WEAPON) listOf(12, 7) else listOf(12, 33),
                    ),
                )
                is SettlementGrowthStep.EquipmentLevelUpInfo -> add(
                    TurnSettlementOp.Info2(
                        if (step.result.slot == CampaignEquipmentSlot.WEAPON) "무기레벨 상승!" else "보구레벨 상승!",
                    ),
                )
                is SettlementGrowthStep.ItemUpgradeCallback -> add(TurnSettlementOp.ItemUpgrade(unitId, step.result))
                SettlementGrowthStep.DefaultAction -> add(TurnSettlementOp.Default(unitId))
            }
        }
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
