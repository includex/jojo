// Battle
package com.jojo.game.presentation.battle.unit

import com.jojo.game.domain.battle.BattleAttribute
import com.jojo.game.domain.battle.BattleStatus
import com.jojo.game.domain.battle.BattleUnit

/** 전술 유닛별 화면 상태를 보관하고 도메인 값과 동기화합니다. */
class BattleUnitPresentationStore {
    /** DerivedState: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
    private data class DerivedState(
        val hitPoints: Int,
        val maxHitPoints: Int,
        val statuses: Map<BattleStatus, Int>,
        val attributeLifts: Map<BattleAttribute, Int>,
    )

    private val states = linkedMapOf<String, BattleUnitPresentationState>()
    private val derivedStates = linkedMapOf<String, DerivedState>()

    /** 유닛 상태를 동기화한 뒤 현재 화면용 투영 상태를 반환합니다. */
    fun stateFor(unit: BattleUnit): BattleUnitPresentationState {
        val state = states.getOrPut(unit.id) {
            BattleUnitPresentationState(unit.hitPoints, unit.maxHitPoints)
        }
        val next = DerivedState(
            hitPoints = unit.hitPoints,
            maxHitPoints = unit.maxHitPoints,
            statuses = unit.statuses.toMap(),
            attributeLifts = unit.attributeLifts.toMap(),
        )
        val previous = derivedStates[unit.id]
        if (previous == null ||
            previous.hitPoints != next.hitPoints || previous.maxHitPoints != next.maxHitPoints
        ) {
            state.refreshHpBar(next.hitPoints, next.maxHitPoints)
        }
        // 상태가 바뀌지 않았을 때 숨겨진 효과를 다시 활성화하지 않도록
        // 속성 아이콘만 별도로 갱신합니다.
        if (previous == null || previous.statuses != next.statuses) {
            state.refreshStatus(next.statuses, next.attributeLifts)
        } else if (previous.attributeLifts != next.attributeLifts) {
            state.refreshAttributeStatusIcons(next.attributeLifts)
        }
        derivedStates[unit.id] = next
        return state
    }

    /** 현재 프레임에 투영된 유닛들을 동기화합니다. */
    fun synchronize(units: Iterable<BattleUnit>) {
        val retainedIds = linkedSetOf<String>()
        units.forEach { unit ->
            retainedIds += unit.id
            stateFor(unit)
        }
        // 사망 콜백 중에는 잠시 남을 수 있지만, 더 이상 투영되지 않는
        // 유닛의 화면 전용 상태는 제거합니다.
        states.keys.retainAll(retainedIds)
        derivedStates.keys.retainAll(retainedIds)
    }

    /** 도메인 값이 같아도 원본 표시 새로고침을 실행합니다. */
    fun refresh(unit: BattleUnit): BattleUnitPresentationState {
        derivedStates.remove(unit.id)
        return stateFor(unit)
    }

    /** 전투 화면이 해제될 때 모든 화면 상태를 비웁니다. */
    fun clear() {
        states.clear()
        derivedStates.clear()
    }
}
