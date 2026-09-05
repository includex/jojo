package com.jojo.game.domain.battle

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleUnit

import java.util.*

/**
 * Owns the ordered unit topology used by tactical and presentation queries.
 *
 * Unit objects remain mutable battle entities; only structural changes to the
 * active and retained collections are confined to this class.
 */
internal class Battlefield(initialUnits: Iterable<BattleUnit>) {
    private val activeById = linkedMapOf<String, BattleUnit>()
    private val retainedById = linkedMapOf<String, BattleUnit>()
    private val activeView: Map<String, BattleUnit> = Collections.unmodifiableMap(activeById)
    private val retainedView: Map<String, BattleUnit> = Collections.unmodifiableMap(retainedById)

    init {
        initialUnits.forEach { activeById[it.id] = it }
    }

    val activeMap: Map<String, BattleUnit>
        get() = activeView

    /**
     * 공개 메서드 `activeUnit`
     *
     * ### 파라미터
    - `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `BattleUnit?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun activeUnit(id: String): BattleUnit? = activeById[id]

    /**
     * 공개 메서드 `unitAt`
     *
     * ### 파라미터
    - `tileX` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `tileY` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `BattleUnit?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun unitAt(tileX: Int, tileY: Int): BattleUnit? =
        activeById.values.firstOrNull { it.visible && it.tileX == tileX && it.tileY == tileY }

    /**
     * 공개 메서드 `presentationUnit`
     *
     * ### 파라미터
    - `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `BattleUnit?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun presentationUnit(id: String): BattleUnit? = activeById[id] ?: retainedById[id]

    /**
     * 공개 메서드 `pendingPresentationUnits`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Collection<BattleUnit>`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun pendingPresentationUnits(): Collection<BattleUnit> = retainedView.values

    /** Active units win duplicate IDs and retained-only units follow in insertion order. */
    fun allPresentationUnits(): List<BattleUnit> = buildList {
        val seen = mutableSetOf<String>()
        activeById.values.forEach { unit ->
            add(unit)
            seen += unit.id
        }
        retainedById.values.filterTo(this) { seen.add(it.id) }
    }

    /**
     * 공개 메서드 `add`
     *
     * ### 파라미터
    - `unit` (`BattleUnit`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun add(unit: BattleUnit) {
        check(unit.id !in activeById) { "이미 존재하는 유닛: ${unit.id}" }
        activeById[unit.id] = unit
    }

    /**
     * 공개 메서드 `defeat`
     *
     * ### 파라미터
    - `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun defeat(id: String) {
        activeById.remove(id)?.let { retainedById[id] = it }
    }

    /**
     * 공개 메서드 `clearRetained`
     *
     * ### 파라미터
    - `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun clearRetained(id: String) {
        retainedById.remove(id)
    }

    /**
     * 공개 메서드 `hideForPresentation`
     *
     * ### 파라미터
    - `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun hideForPresentation(id: String) {
        presentationUnit(id)?.visible = false
    }

    /** Restores a retained unit without changing an already-active duplicate. */
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
     * data class  `TopologySnapshot`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class TopologySnapshot(
        val activeIds: List<String>,
        val retainedIds: List<String>,
    )

    /**
     * 공개 메서드 `snapshotTopology`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `TopologySnapshot`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun snapshotTopology(): TopologySnapshot = TopologySnapshot(
        activeIds = activeById.keys.toList(),
        retainedIds = retainedById.keys.toList(),
    )

    /**
     * 공개 메서드 `restoreTopology`
     *
     * ### 파라미터
    - `snapshot` (`TopologySnapshot`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `unitsById` (`Map<String, BattleUnit>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun restoreTopology(snapshot: TopologySnapshot, unitsById: Map<String, BattleUnit>) {
        activeById.clear()
        snapshot.activeIds.forEach { id -> unitsById[id]?.let { activeById[id] = it } }
        retainedById.clear()
        snapshot.retainedIds.forEach { id -> unitsById[id]?.let { retainedById[id] = it } }
    }
}
