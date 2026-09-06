// Campaign
package com.jojo.game.domain.campaign

import com.jojo.game.*
import com.jojo.game.domain.scenario.*

import java.util.*

/** CampaignRoster: 합류 유닛 중 전투 편성을 유지하고 홀의 최소·최대 편성 규칙을 적용한다. */
class CampaignRoster internal constructor(
    private val joinedUnitIds: () -> Collection<Int>,
) {
    internal constructor() : this({ emptyList() })

    private val selectedUnitIds = mutableListOf<Int>()
    val battleRoster: List<Int> = Collections.unmodifiableList(selectedUnitIds)

    internal fun reset() {
        selectedUnitIds.clear()
    }

    /** seedStartupRoster: 시작·캡처 경로에서 홀 제한 검증 없이 초기 전투 명단을 채운다. */
    internal fun seedStartupRoster(unitIds: Iterable<Int>) {
        selectedUnitIds.clear()
        selectedUnitIds.addAll(unitIds)
    }

    /** restoreBattleRoster: 저장된 유닛 식별자 순서를 현재 전투 명단으로 복원한다. */
    internal fun restoreBattleRoster(unitIds: Iterable<Int>) {
        selectedUnitIds.clear()
        selectedUnitIds.addAll(unitIds)
    }

    /** resolveBattleEntry: 편성 제한과 현재 선택을 검사해 즉시 진입 또는 편성 화면 표시를 결정한다. */
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

    /** configureBattleRoster: 합류 유닛에서 제한을 만족하는 기본 전투 명단을 생성한다. */
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

    /** setBattleRoster: 사용자가 선택한 유닛이 인원·합류 조건을 만족할 때만 명단에 반영한다. */
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

    /** prepareImplicitSingleUnitBattle: 초기 시나리오에 편성이 없을 때 합류한 첫 유닛만으로 전투를 준비한다. */
    fun prepareImplicitSingleUnitBattle(): Boolean {
        if (selectedUnitIds.isNotEmpty()) return true
        val joined = joinedUnitIds()
        if (joined.size != 1 || 0 !in joined) return false
        selectedUnitIds += 0
        return true
    }
}
