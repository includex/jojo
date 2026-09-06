// Battle
package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit

import java.util.*

/** Battlefield: 전투에 참여하는 유닛과 점유 타일을 관리하며, 위치 변경과 전장 복원을 수행한다. */
internal class Battlefield(initialUnits: Iterable<BattleUnit>) {
    /**
     * `activeById` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val activeById = linkedMapOf<String, BattleUnit>()
    /**
     * `retainedById` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val retainedById = linkedMapOf<String, BattleUnit>()
    /**
     * `activeView` (Map<String, BattleUnit>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val activeView: Map<String, BattleUnit> = Collections.unmodifiableMap(activeById)
    /**
     * `retainedView` (Map<String, BattleUnit>): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val retainedView: Map<String, BattleUnit> = Collections.unmodifiableMap(retainedById)

    init {
        initialUnits.forEach { activeById[it.id] = it }
    }

    /**
     * `activeMap` (Map<String, BattleUnit>): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val activeMap: Map<String, BattleUnit>
        get() = activeView


    /**
     * `activeUnit`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun activeUnit(id: String): BattleUnit? = activeById[id]


    /**
     * `unitAt`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun unitAt(tileX: Int, tileY: Int): BattleUnit? =
        activeById.values.firstOrNull { it.visible && it.tileX == tileX && it.tileY == tileY }


    /**
     * `presentationUnit`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun presentationUnit(id: String): BattleUnit? = activeById[id] ?: retainedById[id]


    /**
     * `pendingPresentationUnits`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun pendingPresentationUnits(): Collection<BattleUnit> = retainedView.values
    /**
     * `allPresentationUnits`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun allPresentationUnits(): List<BattleUnit> = buildList {
        val seen = mutableSetOf<String>()
        activeById.values.forEach { unit ->
            add(unit)
            seen += unit.id
        }
        retainedById.values.filterTo(this) { seen.add(it.id) }
    }


    /**
     * `add`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun add(unit: BattleUnit) {
        check(unit.id !in activeById) { "이미 존재하는 유닛: ${unit.id}" }
        activeById[unit.id] = unit
    }


    /**
     * `defeat`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun defeat(id: String) {
        activeById.remove(id)?.let { retainedById[id] = it }
    }


    /**
     * `clearRetained`: 현재 상태를 갱신한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun clearRetained(id: String) {
        retainedById.remove(id)
    }


    /**
     * `hideForPresentation`: 화면 표시 상태를 렌더링한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun hideForPresentation(id: String) {
        presentationUnit(id)?.visible = false
    }
    /**
     * `restore`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun restore(id: String): BattleUnit? {
        val unit = activeById[id] ?: retainedById.remove(id)?.also { activeById[id] = it } ?: return null
        unit.retreatFlag = false
        if (unit.hitPoints < 1) {
            unit.resetAfterRetreat()
        }
        unit.visible = true
        return unit
    }


    /**
     * `TopologySnapshot` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class TopologySnapshot(
        /**
         * `activeIds` (List<String>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val activeIds: List<String>,
        /**
         * `retainedIds` (List<String>,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val retainedIds: List<String>,
    )


    /**
     * `snapshotTopology`: 상태나 데이터를 조회한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun snapshotTopology(): TopologySnapshot = TopologySnapshot(
        activeIds = activeById.keys.toList(),
        retainedIds = retainedById.keys.toList(),
    )


    /**
     * `restoreTopology`: 입력을 규칙에 따라 계산·변환한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun restoreTopology(snapshot: TopologySnapshot, unitsById: Map<String, BattleUnit>) {
        activeById.clear()
        snapshot.activeIds.forEach { id -> unitsById[id]?.let { activeById[id] = it } }
        retainedById.clear()
        snapshot.retainedIds.forEach { id -> unitsById[id]?.let { retainedById[id] = it } }
    }
}
