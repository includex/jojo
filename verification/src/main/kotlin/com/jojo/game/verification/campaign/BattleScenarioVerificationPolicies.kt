package com.jojo.game.verification.campaign

import com.jojo.game.application.runtime.RuntimeGridPoint

/**
 * Monotonic S_52 puzzle route recovered from its five isInRect(1025, ...)
 * gate triggers and final exit rectangle.  This state belongs only to the
 * production-input verifier; it never writes scenario variables or units.
 */
internal class AuthoredMechanicRouteTracker(private val scenario: String) {
    private data class Waypoint(val x1: Int, val y1: Int, val x2: Int, val y2: Int, val target: Pair<Int, Int>)

    private val s52Waypoints = listOf(
        Waypoint(3, 9, 6, 12, 4 to 10),
        Waypoint(8, 14, 11, 17, 9 to 15),
        Waypoint(8, 4, 11, 7, 9 to 5),
        Waypoint(13, 9, 16, 12, 14 to 10),
        Waypoint(13, 14, 16, 17, 14 to 15),
        Waypoint(12, 0, 17, 2, 14 to 1),
    )
    private var waypointIndex = 0

    /**
     * 공개 메서드 `target`
     *
     * ### 파라미터
    - `playerTiles` (`Collection<Pair<Int, Int>>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Pair<Int, Int>?`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun target(playerTiles: Collection<Pair<Int, Int>>): Pair<Int, Int>? {
        if (scenario != "S_52") return null
        while (waypointIndex < s52Waypoints.size) {
            val waypoint = s52Waypoints[waypointIndex]
            if (playerTiles.none { (x, y) -> x in waypoint.x1..waypoint.x2 && y in waypoint.y1..waypoint.y2 }) {
                return waypoint.target
            }
            waypointIndex++
        }
        return s52Waypoints.last().target
    }

    internal fun completedWaypoints(): Int = waypointIndex
}

/**
 * `_showMoveArea` includes same-camp occupied cells, but `unitMove` rejects
 * every occupied destination. A production driver must use the latter
 * acceptance contract when choosing the actual pointer destination.
 */
internal fun executableProductionMoveTiles(
    current: Pair<Int, Int>,
    reachable: Collection<Pair<Int, Int>>,
    occupied: Set<Pair<Int, Int>>,
): List<Pair<Int, Int>> = (listOf(current) + reachable).distinct().filter { it == current || it !in occupied }

/**
 * S01's loss route is driven by the surviving Mine leader, while FRIEND AI
 * performs the authored pursuit. This read-only projection chooses only a
 * tile that the ordinary move UI has already made reachable.
 */
internal fun s01SurvivalDestination(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    visibleEnemyTiles: Collection<Pair<Int, Int>>,
    alliedTiles: Collection<Pair<Int, Int>>,
): Pair<Int, Int>? {
    /**
     * 공개 메서드 `distance`
     *
     * ### 파라미터
    - `a` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `b` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun distance(a: Pair<Int, Int>, b: Pair<Int, Int>) =
        kotlin.math.abs(a.first - b.first) + kotlin.math.abs(a.second - b.second)

    /**
     * 공개 메서드 `nearestEnemy`
     *
     * ### 파라미터
    - `tile` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun nearestEnemy(tile: Pair<Int, Int>) = visibleEnemyTiles.minOfOrNull { distance(tile, it) } ?: Int.MAX_VALUE

    /**
     * 공개 메서드 `nearestAlly`
     *
     * ### 파라미터
    - `tile` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun nearestAlly(tile: Pair<Int, Int>) = alliedTiles
        .filter { it != current }
        .minOfOrNull { distance(tile, it) } ?: Int.MAX_VALUE
    return reachableLegalTiles.distinct().maxWithOrNull(
        compareBy<Pair<Int, Int>>(::nearestEnemy)
            .thenBy { -nearestAlly(it) }
            .thenBy { it.first }
            .thenByDescending { it.second },
    )
}

/** Live-only target projection for the S01 production input policy. */
internal data class S01EnemyTarget(val unitId: String, val characterId: Int?, val hitPoints: Int)

/**
 * S01's three officers advance the scenario event. A player unit attacks an
 * in-range officer first (lowest HP, then 131/129/134); only when none is in
 * range may it clear a guard while the nine-enemy withdrawal threshold has
 * not been reached.
 */
internal fun s01PreferredAttackTargets(
    attackable: Collection<S01EnemyTarget>,
    visibleEnemyCount: Int,
): List<S01EnemyTarget> {
    val leaderOrder = mapOf(131 to 0, 129 to 1, 134 to 2)
    val leaders = attackable.filter { it.characterId in leaderOrder }
        .sortedWith(compareBy<S01EnemyTarget> { it.hitPoints }
            .thenBy { leaderOrder[it.characterId] ?: Int.MAX_VALUE }.thenBy { it.unitId })
    if (leaders.isNotEmpty()) return leaders
    return attackable.takeIf { visibleEnemyCount >= 9 }.orEmpty()
        .sortedWith(compareBy<S01EnemyTarget> { it.hitPoints }
            .thenBy { it.characterId ?: Int.MAX_VALUE }.thenBy { it.unitId })
}

/**
 * Cao Cao only makes an S01 physical attack when a live event leader is
 * cardinally adjacent and a deliberately conservative counter estimate
 * (ordinary counter doubled for a possible critical) cannot defeat him.
 */
internal fun s01CaoCaoSafeLeaderAttack(
    attackerHitPoints: Int,
    attackerDefense: Int,
    attackerTile: Pair<Int, Int>,
    targetCharacterId: Int?,
    targetAttack: Int,
    targetLevel: Int,
    targetTile: Pair<Int, Int>,
): Boolean {
    if (targetCharacterId !in setOf(134, 131, 129)) return false
    val distance =
        kotlin.math.abs(attackerTile.first - targetTile.first) + kotlin.math.abs(attackerTile.second - targetTile.second)
    if (distance != 1) return false
    val counter = maxOf(1, ((targetAttack - attackerDefense) / 2) + 25 + targetLevel)
    return attackerHitPoints > counter * 2
}

/**
 * The S57 scene predicate is a rectangle, not the blocked point (16,19).
 * Pick a legal tile inside x=2..16/y=11..23 first. If none is currently
 * reachable, make real movement progress toward that rectangle; remaining on
 * the current tile is permitted only when no legal non-current move exists.
 */
internal fun s57GateDestination(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
): Pair<Int, Int>? {
    /**
     * 공개 메서드 `inGate`
     *
     * ### 파라미터
    - `tile` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun inGate(tile: Pair<Int, Int>) = tile.first in 2..16 && tile.second in 11..23

    /**
     * 공개 메서드 `rectangleDistance`
     *
     * ### 파라미터
    - `tile` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Int`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun rectangleDistance(tile: Pair<Int, Int>): Int {
        val dx = when {
            tile.first < 2 -> 2 - tile.first
            tile.first > 16 -> tile.first - 16
            else -> 0
        }
        val dy = when {
            tile.second < 11 -> 11 - tile.second
            tile.second > 23 -> tile.second - 23
            else -> 0
        }
        return dx + dy
    }

    /**
     * 공개 메서드 `fromCurrent`
     *
     * ### 파라미터
    - `tile` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun fromCurrent(tile: Pair<Int, Int>) =
        kotlin.math.abs(tile.first - current.first) + kotlin.math.abs(tile.second - current.second)

    val tieBreak = compareBy<Pair<Int, Int>>(::fromCurrent).thenBy { it.first }.thenBy { it.second }
    val legal = reachableLegalTiles.distinct()
    legal.filter(::inGate).minWithOrNull(tieBreak)?.let { return it }
    legal.filter { it != current }.minWithOrNull(
        compareBy<Pair<Int, Int>>(::rectangleDistance)
            .thenBy(::fromCurrent).thenBy { it.first }.thenBy { it.second },
    )?.let { return it }
    return current.takeIf { it in legal }
}

/** Read-only first-room target projection for the S57 production driver. */
internal data class S57FirstRoomLeader(
    val unitId: String,
    val characterId: Int,
    val hitPoints: Int,
    val tile: Pair<Int, Int>,
)

/** Lowest HP wins; equal values follow the authored escort order. */
internal fun s57FirstRoomEscortFocus(
    leaders: Collection<S57FirstRoomLeader>,
): S57FirstRoomLeader? {
    val tieOrder = mapOf(162 to 0, 169 to 1, 165 to 2)
    return leaders.asSequence()
        .filter { it.characterId in tieOrder }
        .minWithOrNull(compareBy<S57FirstRoomLeader> { it.hitPoints }
            .thenBy { tieOrder.getValue(it.characterId) }.thenBy { it.unitId })
}

/**
 * Last-resort S57 finisher: the ordinary first-room route keeps Cao Cao in
 * the rear. Release him only when this turn can finish the currently
 * focused leader through the normal physical Attack UI: the deterministic
 * base-harm preview must cover the remaining HP and an unoccupied legal
 * attack tile must already be reachable.  This only admits ordinary
 * leader-only move/Attack planning; it changes no battle state.
 */
internal fun s57FirstRoomCriticalFinisherActive(
    focusedLeaderHitPoints: Int?,
    expectedSourcePhysicalDamage: Int,
    sourceCanReachLeaderAttackTile: Boolean,
): Boolean = focusedLeaderHitPoints != null &&
        expectedSourcePhysicalDamage >= focusedLeaderHitPoints && sourceCanReachLeaderAttackTile

/**
 * A finisher is deliberately all-or-nothing: unlike the escort route, it
 * must not make source 0 merely progress toward a leader and then leave him
 * exposed.  Return only a legal tile from which the selected leader can be
 * attacked this action, with a stable UI-input tie break.
 */
internal fun s57CriticalFinisherDestination(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    focusTile: Pair<Int, Int>,
    attackAllScreen: Boolean,
    attackOffsets: Set<Pair<Int, Int>>,
): Pair<Int, Int>? {
    /**
     * 공개 메서드 `canAttack`
     *
     * ### 파라미터
    - `from` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canAttack(from: Pair<Int, Int>) = attackAllScreen ||
            (focusTile.first - from.first to focusTile.second - from.second) in attackOffsets

    /**
     * 공개 메서드 `distanceFromCurrent`
     *
     * ### 파라미터
    - `tile` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun distanceFromCurrent(tile: Pair<Int, Int>) =
        kotlin.math.abs(tile.first - current.first) + kotlin.math.abs(tile.second - current.second)
    return reachableLegalTiles.distinct().asSequence()
        .filter(::canAttack)
        .minWithOrNull(compareBy<Pair<Int, Int>>(::distanceFromCurrent).thenBy { it.first }.thenBy { it.second })
}

/**
 * An escort may attack only the focused leader. Prefer a legal tile from
 * which that attack is possible; otherwise make deterministic non-current
 * progress toward the leader and let CommandLayer WAIT consume the action.
 */
internal fun s57EscortFocusDestination(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    focusTile: Pair<Int, Int>,
    attackAllScreen: Boolean,
    attackOffsets: Set<Pair<Int, Int>>,
): Pair<Int, Int>? {
    /**
     * 공개 메서드 `canAttack`
     *
     * ### 파라미터
    - `from` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canAttack(from: Pair<Int, Int>) = attackAllScreen ||
            (focusTile.first - from.first to focusTile.second - from.second) in attackOffsets

    /**
     * 공개 메서드 `distanceToFocus`
     *
     * ### 파라미터
    - `tile` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun distanceToFocus(tile: Pair<Int, Int>) =
        kotlin.math.abs(tile.first - focusTile.first) + kotlin.math.abs(tile.second - focusTile.second)

    /**
     * 공개 메서드 `distanceFromCurrent`
     *
     * ### 파라미터
    - `tile` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun distanceFromCurrent(tile: Pair<Int, Int>) =
        kotlin.math.abs(tile.first - current.first) + kotlin.math.abs(tile.second - current.second)

    val progressOrder = compareBy<Pair<Int, Int>>(::distanceToFocus)
        .thenBy(::distanceFromCurrent).thenBy { it.first }.thenBy { it.second }
    val legal = reachableLegalTiles.distinct()
    if (current in legal && canAttack(current)) return current
    legal.filter { it != current && canAttack(it) }.minWithOrNull(progressOrder)?.let { return it }
    legal.filter { it != current }.minWithOrNull(progressOrder)?.let { return it }
    return current.takeIf { it in legal }
}

/** An enemy physically occupying the only forward step to a leader attack tile. */
internal data class S57EscortFocusBlocker(
    val unitId: String,
    val tile: Pair<Int, Int>,
    val hitPoints: Int,
    /** Live Unit.incRetreat state; a shown former casualty should lose ties to a fresh blocker. */
    val retreatCount: Int = 0,
)

/**
 * Keeps the S57 escort route leader-first, but permits one real attack when a
 * guard is the live obstruction between the accessible movement frontier and
 * an eventual legal leader-attack tile. This intentionally does not turn
 * ordinary guards into scored targets: a leader attack from any current-turn
 * legal tile always wins, and a candidate must occupy a cardinal next step
 * that reduces distance to one of the leader's attack-staging tiles.
 */
internal data class S57EscortFocusBlockerFallback(
    val blocker: S57EscortFocusBlocker,
    /** Legal tile from which the driver should issue its ordinary Attack UI input. */
    val attackFrom: Pair<Int, Int>,
)

/** Deterministic current-turn tile from which an escort can attack this guard. */
internal fun s57EscortAttackFrom(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    guardTile: Pair<Int, Int>,
    attackAllScreen: Boolean,
    attackOffsets: Set<Pair<Int, Int>>,
): Pair<Int, Int>? {
    /**
     * 공개 메서드 `canAttack`
     *
     * ### 파라미터
    - `from` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canAttack(from: Pair<Int, Int>) = attackAllScreen ||
            (guardTile.first - from.first to guardTile.second - from.second) in attackOffsets

    /**
     * 공개 메서드 `distance`
     *
     * ### 파라미터
    - `left` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `right` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun distance(left: Pair<Int, Int>, right: Pair<Int, Int>) =
        kotlin.math.abs(left.first - right.first) + kotlin.math.abs(left.second - right.second)
    return (listOf(current) + reachableLegalTiles).distinct().asSequence().filter(::canAttack)
        .minWithOrNull(compareBy<Pair<Int, Int>> { distance(it, guardTile) }
            .thenBy { distance(current, it) }.thenBy { it.first }.thenBy { it.second })
}

internal fun s57EscortFocusBlockerFallback(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    focusTile: Pair<Int, Int>,
    attackAllScreen: Boolean,
    attackOffsets: Set<Pair<Int, Int>>,
    occupiedTiles: Set<Pair<Int, Int>>,
    guards: Collection<S57EscortFocusBlocker>,
    /** Per-guard bounded post-kill route evidence (this or next move reaches staging). */
    openedStagingReachableByGuard: Map<String, Collection<Pair<Int, Int>>>,
): S57EscortFocusBlockerFallback? {
    /**
     * 공개 메서드 `canAttack`
     *
     * ### 파라미터
    - `from` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `target` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun canAttack(from: Pair<Int, Int>, target: Pair<Int, Int>) = attackAllScreen ||
            (target.first - from.first to target.second - from.second) in attackOffsets

    /**
     * 공개 메서드 `distance`
     *
     * ### 파라미터
    - `left` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `right` (`Pair<Int, Int>`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun distance(left: Pair<Int, Int>, right: Pair<Int, Int>) =
        kotlin.math.abs(left.first - right.first) + kotlin.math.abs(left.second - right.second)

    val legal = (listOf(current) + reachableLegalTiles).distinct()
    // A direct (or this-turn move-then) leader hit is never displaced by a guard.
    if (legal.any { canAttack(it, focusTile) }) return null
    if (attackAllScreen || attackOffsets.isEmpty()) return null
    val stagingTiles = attackOffsets.map { (dx, dy) -> focusTile.first - dx to focusTile.second - dy }.distinct()
    val candidates = guards.asSequence()
        .filter { it.tile in occupiedTiles }
        .mapNotNull { guard ->
            val attackFrom = s57EscortAttackFrom(
                current, legal, guard.tile, attackAllScreen, attackOffsets,
            )
                ?: return@mapNotNull null
            // `reachableLegalTiles` is the live flood-fill after occupied
            // destinations have been removed. Only its nearest live staging
            // frontier is eligible: do not turn every later corridor guard
            // into a target in the same first-room push.
            val closestStagingDistance = legal.minOf { frontier ->
                stagingTiles.minOf { staging -> distance(frontier, staging) }
            }
            val blocksProgress = legal.any { frontier ->
                stagingTiles.minOf { staging -> distance(frontier, staging) } == closestStagingDistance &&
                        stagingTiles.any { staging ->
                            distance(frontier, guard.tile) == 1 &&
                                    distance(guard.tile, staging) < distance(frontier, staging)
                        }
            }
            // Do not infer a blocker from Manhattan distance alone. It is a
            // valid exception only when removing this exact guard lets this
            // escort enter a real physical-attack staging tile on this or the
            // following movement projection. This is read-only route evidence; the
            // actual guard attack still goes through CommandLayer -> map UI.
            val opensStagingRoute = openedStagingReachableByGuard[guard.unitId]
                ?.any { it in stagingTiles } == true
            if (blocksProgress && opensStagingRoute) S57EscortFocusBlockerFallback(guard, attackFrom) else null
        }
        .toList()
    // S57's script can show a defeated guard again. Prefer a new immediate
    // obstruction whenever one exists, using only its live retreat counter.
    return candidates.filter { it.blocker.retreatCount == 0 }.ifEmpty { candidates }
        .minWithOrNull(compareBy<S57EscortFocusBlockerFallback> {
            stagingTiles.minOf { staging -> distance(it.blocker.tile, staging) }
        }.thenBy { distance(current, it.blocker.tile) }
            .thenBy { distance(current, it.attackFrom) }
            .thenBy { it.blocker.hitPoints }.thenBy { it.blocker.tile.first }
            .thenBy { it.blocker.tile.second }.thenBy { it.blocker.unitId })
}

/** Global priority for the S57 first-room production planner only. */
internal fun s57FirstRoomActionRank(
    leaderHit: Boolean,
    focusProgress: Boolean,
    blockerHit: Boolean,
): Int = when {
    leaderHit -> 0
    focusProgress -> 1
    blockerHit -> 2
    else -> 3
}

/**
 * S_57's authored second-room event is intentionally a near-defeat branch:
 * after the first room opens it requires `totalUnit(MINE) < 2` before the
 * center/status sequence runs. The production verifier must reach that branch
 * through ordinary enemy damage, not by writing HP, visibility, or scenario
 * variables. While the newly revealed Sun-family trio is present, advance
 * units normally but issue WAIT instead of an attack until attrition is real.
 */
internal fun waitForS57AuthoredAttrition(
    scenario: String,
    visiblePlayerCount: Int,
    visibleEnemySourceIds: Collection<Int>,
): Boolean = scenario == "S_57" && visiblePlayerCount >= 2 &&
        visibleEnemySourceIds.any { it in setOf(166, 167, 168) }
