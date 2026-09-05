package com.jojo.game
import com.jojo.game.domain.battle.BattleActionSnapshot
import com.jojo.game.domain.battle.*

/**
 * Publishes a calculated action at animation lifecycle boundaries.
 *
 * The calculation snapshots remain internal while callers can commit only
 * the movement, hit, status, economy, or final state appropriate to the
 * presentation edge they have reached.
 */
/**
 * class  `BattleActionTransaction`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class BattleActionTransaction internal constructor(
    val actorId: String,
    private val before: BattleActionSnapshot,
    private val after: BattleActionSnapshot,
    private val hitSideEffects: List<() -> Unit>,
    private val completionSideEffects: List<() -> Unit>,
    private val restoreSnapshot: (BattleActionSnapshot) -> Unit,
    private val adjustEconomy: (playerDelta: Int, enemyDelta: Int) -> Unit,
    private val presentationUnit: (String) -> BattleUnit?,
    private val activeUnit: (String) -> BattleUnit?,
    private val onCompleted: (BattleActionTransaction) -> Unit,
) {
    private var complete = false
    private var hitEffectsCommitted = 0

    /**
     * 공개 메서드 `initialHp`
     *
     * ### 파라미터
    - `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun initialHp(id: String): Int? = before.states[id]?.hitPoints

    /**
     * 공개 메서드 `initialMp`
     *
     * ### 파라미터
    - `id` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun initialMp(id: String): Int? = before.states[id]?.magicPoints

    /** Commits the actor's calculated destination and optional action state. */
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

    /** Publishes an animation-complete position without restoring other state. */
    fun commitPosition(id: String, x: Int, y: Int) {
        if (complete) return
        val unit = before.states[id]?.unit ?: return
        unit.tileX = x
        unit.tileY = y
        unit.hasAuthoredTileX = true
        unit.hasAuthoredTileY = true
    }

    /** Publishes only the HP and MP visible at the current hit boundary. */
    fun commitVitals(id: String, hp: Int? = null, mp: Int? = null) {
        if (complete) return
        val unit = before.states[id]?.unit ?: return
        hp?.let(unit::setHpcur)
        mp?.let(unit::setMpcur)
    }

    /** Publishes callback-local money deltas before the final absolute restore. */
    fun commitEconomy(playerDelta: Int = 0, enemyDelta: Int = 0) {
        if (complete) return
        adjustEconomy(playerDelta, enemyDelta)
    }

    /** Publishes the calculated status collections for one unit. */
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

    /** Publishes one callback-local status settlement rather than the final state. */
    fun commitStatuses(entry: MagicLocalSettlementEntry) {
        if (complete) return
        restoreStatuses(
            id = entry.targetId,
            statuses = entry.statusesAfter,
            attributeLifts = entry.attributeLiftsAfter,
            attributeLiftRounds = entry.attributeLiftRoundsAfter,
        )
    }

    private fun restoreStatuses(
        id: String,
        statuses: Map<BattleStatus, Int>,
        attributeLifts: Map<BattleAttribute, Int>,
        attributeLiftRounds: Map<BattleAttribute, Int>,
    ) {
        val unit = before.states[id]?.unit ?: return
        unit.statuses.clear()
        unit.statuses.putAll(statuses)
        unit.attributeLifts.clear()
        unit.attributeLifts.putAll(attributeLifts)
        unit.attributeLiftRounds.clear()
        unit.attributeLiftRounds.putAll(attributeLiftRounds)
    }

    /**
     * 공개 메서드 `commitNextHitSideEffect`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun commitNextHitSideEffect() {
        if (complete) return
        hitSideEffects.getOrNull(hitEffectsCommitted)?.invoke()
        if (hitEffectsCommitted < hitSideEffects.size) hitEffectsCommitted++
    }

    /** Restores the final snapshot, drains effects in order, and completes once. */
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
