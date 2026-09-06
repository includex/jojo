package com.jojo.game.domain.campaign

import com.jojo.game.*
import com.jojo.game.domain.scenario.*

import java.util.*

/** 전투 편성과 홀 진입 규칙을 관리한다. */
class CampaignRoster internal constructor(
    private val joinedUnitIds: () -> Collection<Int>,
) {
    internal constructor() : this({ emptyList() })

    private val selectedUnitIds = mutableListOf<Int>()
    val battleRoster: List<Int> = Collections.unmodifiableList(selectedUnitIds)

    internal fun reset() {
        selectedUnitIds.clear()
    }

    /** 홀 검증 없이 시작 또는 캡처용 편성을 설정한다. */
    internal fun seedStartupRoster(unitIds: Iterable<Int>) {
        selectedUnitIds.clear()
        selectedUnitIds.addAll(unitIds)
    }

    /** 저장된 편성 순서를 복원한다. */
    internal fun restoreBattleRoster(unitIds: Iterable<Int>) {
        selectedUnitIds.clear()
        selectedUnitIds.addAll(unitIds)
    }

    /** 홀 선택 제한과 즉시 전투 진입 조건을 계산한다. */
    fun resolveBattleEntry(limit: ScenarioJoinBattleLimit): ScenarioBattleEntryPlan {
        val excluded = limit.excludedUnitIds.distinct()
        val available = joinedUnitIds().filterNot { it in excluded }
        val mandatory = buildList {
            if (0 in available) add(0)
            limit.requiredUnitIds.forEach { id ->
                if (id in available && id !in this) add(id)
            }
        }
        val hallMaximum = minOf(limit.maximum.coerceAtLeast(0), available.size)
        // 즉시 진입 여부는 UI 제한을 적용하기 전 원본 최대 인원으로 판단한다.
        val direct = mandatory.takeIf { it.size >= limit.maximum }
        val uiMaximum = minOf(hallMaximum, 20)
        val uiMinimum = if (uiMaximum > 0) maxOf(1, 2 * (uiMaximum / 3)) else 0
        return ScenarioBattleEntryPlan(
            selectionLimit = ScenarioJoinBattleLimit(uiMinimum, uiMaximum, mandatory, excluded),
            directBattleRoster = direct,
        )
    }

    /** 제한에 맞는 기본 전투 편성을 구성한다. */
    fun configureBattleRoster(limit: ScenarioJoinBattleLimit): ScenarioBattleEntryPlan {
        val plan = resolveBattleEntry(limit)
        val effective = plan.selectionLimit
        val available = joinedUnitIds().filterNot { it in effective.excludedUnitIds }
        val roster = plan.directBattleRoster ?: (effective.requiredUnitIds +
                available.filterNot { it in effective.requiredUnitIds }).take(effective.maximum)
        selectedUnitIds.clear()
        selectedUnitIds += roster
        return plan
    }

    /** 선택한 편성이 제한 조건을 만족하면 저장한다. */
    fun setBattleRoster(selection: Collection<Int>, limit: ScenarioJoinBattleLimit): Boolean {
        val distinct = selection.distinct()
        val available = (joinedUnitIds() + limit.requiredUnitIds)
            .filterNot { it in limit.excludedUnitIds }
            .toSet()
        if (distinct.size !in limit.minimum..limit.maximum ||
            !distinct.containsAll(limit.requiredUnitIds) ||
            distinct.any { it !in available }
        ) return false
        selectedUnitIds.clear()
        selectedUnitIds += distinct
        return true
    }

    /** 기존 편성이 없을 때 R_00 단독 전투 편성을 채운다. */
    fun prepareImplicitSingleUnitBattle(): Boolean {
        if (selectedUnitIds.isNotEmpty()) return true
        val joined = joinedUnitIds()
        if (joined.size != 1 || 0 !in joined) return false
        selectedUnitIds += 0
        return true
    }
}
