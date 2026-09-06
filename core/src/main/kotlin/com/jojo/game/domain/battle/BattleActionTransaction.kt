// Battle
package com.jojo.game.domain.battle

import com.jojo.game.*
import com.jojo.game.domain.battle.BattleActionSnapshot

/** BattleActionTransaction: 전투 동작 트랜잭션으로, 애니메이션 단계에 맞춰 계산된 유닛 상태를 순서대로 반영한다. */
class BattleActionTransaction internal constructor(
    val actorId: String,
    /**
     * `before` (BattleActionSnapshot,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val before: BattleActionSnapshot,
    /**
     * `after` (BattleActionSnapshot,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val after: BattleActionSnapshot,
    /**
     * `hitSideEffects` (List<() -> Unit>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val hitSideEffects: List<() -> Unit>,
    /**
     * `completionSideEffects` (List<() -> Unit>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val completionSideEffects: List<() -> Unit>,
    /**
     * `restoreSnapshot` ((BattleActionSnapshot) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val restoreSnapshot: (BattleActionSnapshot) -> Unit,
    /**
     * `adjustEconomy` ((playerDelta: Int, enemyDelta: Int) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val adjustEconomy: (playerDelta: Int, enemyDelta: Int) -> Unit,
    /**
     * `presentationUnit` ((String) -> BattleUnit?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val presentationUnit: (String) -> BattleUnit?,
    /**
     * `activeUnit` ((String) -> BattleUnit?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val activeUnit: (String) -> BattleUnit?,
    /**
     * `onCompleted` ((BattleActionTransaction) -> Unit,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val onCompleted: (BattleActionTransaction) -> Unit,
) {
    /**
     * `complete` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var complete = false
    /**
     * `hitEffectsCommitted` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private var hitEffectsCommitted = 0

    /** 행동 전 유닛의 체력을 조회한다. */
    fun initialHp(id: String): Int? = before.states[id]?.hitPoints

    /** 행동 전 유닛의 기력을 조회한다. */
    fun initialMp(id: String): Int? = before.states[id]?.magicPoints

    /** 행동자의 계산된 이동 위치와 선택 상태를 반영한다. */
    fun commitMovement(commitActionState: Boolean = false) {
        if (complete) return
        val source = after.states[actorId] ?: return
        val target = before.states[actorId]?.unit ?: return
        val moved = target.tileX != source.tileX || target.tileY != source.tileY
        target.tileX = source.tileX
        target.tileY = source.tileY
        if (moved) {
            target.hasAuthoredTileX = true
            target.hasAuthoredTileY = true
        }
        target.direction = source.direction
        target.hasMoved = source.hasMoved
        if (commitActionState) {
            target.actionStatusRound = source.actionStatusRound
            target.hasActed = source.hasActed
        }
    }

    /** 다른 상태를 복원하지 않고 애니메이션 완료 위치만 반영한다. */
    fun commitPosition(id: String, x: Int, y: Int) {
        if (complete) return
        val unit = before.states[id]?.unit ?: return
        unit.tileX = x
        unit.tileY = y
        unit.hasAuthoredTileX = true
        unit.hasAuthoredTileY = true
    }

    /** 현재 타격 단계에서 보여야 할 체력과 기력만 반영한다. */
    fun commitVitals(id: String, hp: Int? = null, mp: Int? = null) {
        if (complete) return
        val unit = before.states[id]?.unit ?: return
        hp?.let(unit::setHpcur)
        mp?.let(unit::setMpcur)
    }

    /** 최종 상태 복원 전에 콜백 단위의 자금 변동을 반영한다. */
    fun commitEconomy(playerDelta: Int = 0, enemyDelta: Int = 0) {
        if (complete) return
        adjustEconomy(playerDelta, enemyDelta)
    }

    /** 유닛 하나의 계산된 상태 이상 목록을 반영한다. */
    fun commitStatuses(id: String) {
        if (complete) return
        val source = after.states[id] ?: return
        restoreStatuses(
            id = id,
            statuses = source.statuses,
            attributeLifts = source.attributeLifts,
            attributeLiftRounds = source.attributeLiftRounds,
        )
    }

    /** 최종 상태 대신 콜백 단위의 상태 이상 결과를 반영한다. */
    fun commitStatuses(entry: MagicLocalSettlementEntry) {
        if (complete) return
        restoreStatuses(
            id = entry.targetId,
            statuses = entry.statusesAfter,
            attributeLifts = entry.attributeLiftsAfter,
            attributeLiftRounds = entry.attributeLiftRoundsAfter,
        )
    }

    /**
     * `restoreStatuses`: 입력을 규칙에 따라 계산·변환한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private fun restoreStatuses(
        id: String,
        statuses: Map<BattleStatus, Int>,
        attributeLifts: Map<BattleAttribute, Int>,
        attributeLiftRounds: Map<BattleAttribute, Int>,
    ) {
        /**
         * `unit` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val unit = before.states[id]?.unit ?: return
        unit.statuses.clear()
        unit.statuses.putAll(statuses)
        unit.attributeLifts.clear()
        unit.attributeLifts.putAll(attributeLifts)
        unit.attributeLiftRounds.clear()
        unit.attributeLiftRounds.putAll(attributeLiftRounds)
    }

    /** 다음 타격 부수 효과를 한 번만 실행한다. */
    fun commitNextHitSideEffect() {
        if (complete) return
        hitSideEffects.getOrNull(hitEffectsCommitted)?.invoke()
        if (hitEffectsCommitted < hitSideEffects.size) hitEffectsCommitted++
    }

    /** 최종 스냅샷과 남은 효과를 순서대로 반영하고 행동을 완료한다. */
    fun commitAll() {
        if (complete) return
        val liveDirections = before.states.mapNotNull { (id, state) ->
            val direction = state.unit.direction
            if (id == actorId || direction != state.direction) id to direction else null
        }.toMap()
        val moved = before.states[actorId]?.let { beforeState ->
            after.states[actorId]?.let { afterState ->
                beforeState.tileX != afterState.tileX || beforeState.tileY != afterState.tileY
            }
        } == true

        restoreSnapshot(after)
        liveDirections.forEach { (id, direction) -> presentationUnit(id)?.direction = direction }
        if (moved) {
            activeUnit(actorId)?.hasAuthoredTileX = true
            activeUnit(actorId)?.hasAuthoredTileY = true
        }
        while (hitEffectsCommitted < hitSideEffects.size) commitNextHitSideEffect()
        completionSideEffects.forEach { it() }
        complete = true
        onCompleted(this)
    }
}
