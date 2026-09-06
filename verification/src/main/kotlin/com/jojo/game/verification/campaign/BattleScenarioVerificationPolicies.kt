// Verification
package com.jojo.game.verification.campaign

import com.jojo.game.presentation.scenario.overlay.*

import com.jojo.game.application.runtime.RuntimeGridPoint

/** AuthoredMechanicRouteTracker: 다섯 번의 isInRect(1025, ...) 관문과 마지막 탈출 영역에서 복원한 S_52 퍼즐 경로이다. 운영 입력 검증기만 이 상태를 소유하며 시나리오 변수나 유닛을 쓰지 않는다. */
internal class AuthoredMechanicRouteTracker(private val scenario: String) {
    /** Waypoint: 검증 시나리오의 상태와 동작을 제공하는 타입이다. */
    private data class Waypoint(val x1: Int, val y1: Int, val x2: Int, val y2: Int, val target: Pair<Int, Int>)

    /** s52Waypoints: 검증 대상 목록을 담는다. */
    private val s52Waypoints = listOf(
        Waypoint(3, 9, 6, 12, 4 to 10),
        Waypoint(8, 14, 11, 17, 9 to 15),
        Waypoint(8, 4, 11, 7, 9 to 5),
        Waypoint(13, 9, 16, 12, 14 to 10),
        Waypoint(13, 14, 16, 17, 14 to 15),
        Waypoint(12, 0, 17, 2, 14 to 1),
    )
    /** waypointIndex: 이동 경로 계산 값을 담는다. */
    private var waypointIndex = 0


    /** target: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
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

    /** completedWaypoints: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    internal fun completedWaypoints(): Int = waypointIndex
}

/** executableProductionMoveTiles: _showMoveArea에는 같은 진영 점유 칸도 포함되지만 unitMove는 모든 점유 목적지를 거부한다. 운영 입력기는 실제 포인터 목적지를 정할 때 후자의 수락 계약을 사용해야 한다. */
internal fun executableProductionMoveTiles(
    current: Pair<Int, Int>,
    reachable: Collection<Pair<Int, Int>>,
    occupied: Set<Pair<Int, Int>>,
): List<Pair<Int, Int>> = (listOf(current) + reachable).distinct().filter { it == current || it !in occupied }

/** s01SurvivalDestination: S01 패배 경로는 생존한 Mine 지휘관이 진행하고 FRIEND AI가 원본 추격을 수행한다. 이 읽기 전용 투영은 일반 이동 UI가 이미 도달 가능하게 만든 칸만 선택한다. */
internal fun s01SurvivalDestination(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    visibleEnemyTiles: Collection<Pair<Int, Int>>,
    alliedTiles: Collection<Pair<Int, Int>>,
): Pair<Int, Int>? {

    /** distance: 두 위치 사이의 이동 거리를 계산한다. */
    fun distance(a: Pair<Int, Int>, b: Pair<Int, Int>) =
        kotlin.math.abs(a.first - b.first) + kotlin.math.abs(a.second - b.second)


    /** nearestEnemy: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun nearestEnemy(tile: Pair<Int, Int>) = visibleEnemyTiles.minOfOrNull { distance(tile, it) } ?: Int.MAX_VALUE


    /** nearestAlly: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
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

/** S01EnemyTarget: S01 운영 입력 정책에서 현재 생존 대상만 투영한다. */
internal data class S01EnemyTarget(val unitId: String, val characterId: Int?, val hitPoints: Int)

/** s01PreferredAttackTargets: S01의 세 장수가 시나리오 이벤트를 진행한다. 플레이어 유닛은 사거리 안의 장수를 먼저 공격하며 체력이 낮은 순서와 131·129·134 순서를 따른다. 사거리 안에 장수가 없고 9명 철수 조건 전이면 그때만 호위병을 정리한다. */
internal fun s01PreferredAttackTargets(
    attackable: Collection<S01EnemyTarget>,
    visibleEnemyCount: Int,
): List<S01EnemyTarget> {
    /**
     * `leaderOrder` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val leaderOrder = mapOf(131 to 0, 129 to 1, 134 to 2)
    /**
     * `leaders` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val leaders = attackable.filter { it.characterId in leaderOrder }
        .sortedWith(compareBy<S01EnemyTarget> { it.hitPoints }
            .thenBy { leaderOrder[it.characterId] ?: Int.MAX_VALUE }.thenBy { it.unitId })
    if (leaders.isNotEmpty()) return leaders
    return attackable.takeIf { visibleEnemyCount >= 9 }.orEmpty()
        .sortedWith(compareBy<S01EnemyTarget> { it.hitPoints }
            .thenBy { it.characterId ?: Int.MAX_VALUE }.thenBy { it.unitId })
}

/** s01CaoCaoSafeLeaderAttack: 조조는 생존 이벤트 지휘관이 상하좌우로 인접하고, 치명타 가능성을 고려해 두 배로 계산한 보수적인 반격 추정치로 자신을 쓰러뜨릴 수 없을 때만 S01 물리 공격을 수행한다. */
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
    /**
     * `distance` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val distance =
        kotlin.math.abs(attackerTile.first - targetTile.first) + kotlin.math.abs(attackerTile.second - targetTile.second)
    if (distance != 1) return false
    /**
     * `counter` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val counter = maxOf(1, ((targetAttack - attackerDefense) / 2) + 25 + targetLevel)
    return attackerHitPoints > counter * 2
}

/** s57GateDestination: S57 장면 조건은 막힌 점 (16,19)이 아니라 영역이다. 먼저 x=2..16, y=11..23 안의 합법 칸을 고르고, 현재 도달할 수 없으면 그 영역을 향해 실제 이동을 진행한다. 합법적인 다른 칸이 없을 때만 현재 칸에 남는다. */
internal fun s57GateDestination(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
): Pair<Int, Int>? {

    /** inGate: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun inGate(tile: Pair<Int, Int>) = tile.first in 2..16 && tile.second in 11..23


    /** rectangleDistance: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
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


    /** fromCurrent: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun fromCurrent(tile: Pair<Int, Int>) =
        kotlin.math.abs(tile.first - current.first) + kotlin.math.abs(tile.second - current.second)

    /**
     * `tieBreak` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val tieBreak = compareBy<Pair<Int, Int>>(::fromCurrent).thenBy { it.first }.thenBy { it.second }
    /**
     * `legal` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val legal = reachableLegalTiles.distinct()
    legal.filter(::inGate).minWithOrNull(tieBreak)?.let { return it }
    legal.filter { it != current }.minWithOrNull(
        compareBy<Pair<Int, Int>>(::rectangleDistance)
            .thenBy(::fromCurrent).thenBy { it.first }.thenBy { it.second },
    )?.let { return it }
    return current.takeIf { it in legal }
}

/** S57FirstRoomLeader: S57 운영 입력기의 첫 방 대상을 읽기 전용으로 투영한다. */
internal data class S57FirstRoomLeader(
    /** unitId: 전투 무장 상태를 담는다. */
    val unitId: String,
    /** characterId: 전투 무장 상태를 담는다. */
    val characterId: Int,
    /** hitPoints: 검증 대상 목록을 담는다. */
    val hitPoints: Int,
    /** tile: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val tile: Pair<Int, Int>,
)

/** s57FirstRoomEscortFocus: 체력이 가장 낮은 대상을 고르고, 같으면 원본 호위 순서를 따른다. */
internal fun s57FirstRoomEscortFocus(
    leaders: Collection<S57FirstRoomLeader>,
): S57FirstRoomLeader? {
    /**
     * `tieOrder` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val tieOrder = mapOf(162 to 0, 169 to 1, 165 to 2)
    return leaders.asSequence()
        .filter { it.characterId in tieOrder }
        .minWithOrNull(compareBy<S57FirstRoomLeader> { it.hitPoints }
            .thenBy { tieOrder.getValue(it.characterId) }.thenBy { it.unitId })
}

/** s57FirstRoomCriticalFinisherActive: S57의 최후 마무리 경로이다. 일반 첫 방 경로에서는 조조를 후방에 두며, 현재 턴에 일반 물리 공격 UI로 선택 지휘관을 끝낼 수 있을 때만 이동시킨다. 결정적인 기본 피해가 남은 체력 이상이고 비어 있는 합법 공격 칸에 도달할 수 있어야 하며, 지휘관 전용 이동·공격 계획만 허용하고 전투 상태는 바꾸지 않는다. */
internal fun s57FirstRoomCriticalFinisherActive(
    focusedLeaderHitPoints: Int?,
    expectedSourcePhysicalDamage: Int,
    sourceCanReachLeaderAttackTile: Boolean,
): Boolean = focusedLeaderHitPoints != null &&
        expectedSourcePhysicalDamage >= focusedLeaderHitPoints && sourceCanReachLeaderAttackTile

/** s57CriticalFinisherDestination: 마무리 행동은 전부 수행하거나 하지 않는 방식이다. 호위 경로와 달리 source 0을 지휘관 쪽으로만 이동시켜 노출시키면 안 된다. 이번 행동에 선택 지휘관을 공격할 수 있는 합법 칸만 반환하고 UI 입력 순서를 안정적으로 유지한다. */
internal fun s57CriticalFinisherDestination(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    focusTile: Pair<Int, Int>,
    attackAllScreen: Boolean,
    attackOffsets: Set<Pair<Int, Int>>,
): Pair<Int, Int>? {

    /** canAttack: 공격 가능 여부를 전투 규칙으로 판정한다. */
    fun canAttack(from: Pair<Int, Int>) = attackAllScreen ||
            (focusTile.first - from.first to focusTile.second - from.second) in attackOffsets


    /** distanceFromCurrent: 현재 위치에서 대상까지의 거리를 계산한다. */
    fun distanceFromCurrent(tile: Pair<Int, Int>) =
        kotlin.math.abs(tile.first - current.first) + kotlin.math.abs(tile.second - current.second)
    return reachableLegalTiles.distinct().asSequence()
        .filter(::canAttack)
        .minWithOrNull(compareBy<Pair<Int, Int>>(::distanceFromCurrent).thenBy { it.first }.thenBy { it.second })
}

/** s57EscortFocusDestination: 호위 유닛은 선택된 지휘관만 공격할 수 있다. 공격 가능한 합법 칸을 우선하고, 없으면 지휘관을 향해 현재 칸이 아닌 곳으로 결정적으로 전진한 뒤 CommandLayer WAIT로 행동을 소비한다. */
internal fun s57EscortFocusDestination(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    focusTile: Pair<Int, Int>,
    attackAllScreen: Boolean,
    attackOffsets: Set<Pair<Int, Int>>,
): Pair<Int, Int>? {

    /** canAttack: 공격 가능 여부를 전투 규칙으로 판정한다. */
    fun canAttack(from: Pair<Int, Int>) = attackAllScreen ||
            (focusTile.first - from.first to focusTile.second - from.second) in attackOffsets


    /** distanceToFocus: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
    fun distanceToFocus(tile: Pair<Int, Int>) =
        kotlin.math.abs(tile.first - focusTile.first) + kotlin.math.abs(tile.second - focusTile.second)


    /** distanceFromCurrent: 현재 위치에서 대상까지의 거리를 계산한다. */
    fun distanceFromCurrent(tile: Pair<Int, Int>) =
        kotlin.math.abs(tile.first - current.first) + kotlin.math.abs(tile.second - current.second)

    /**
     * `progressOrder` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val progressOrder = compareBy<Pair<Int, Int>>(::distanceToFocus)
        .thenBy(::distanceFromCurrent).thenBy { it.first }.thenBy { it.second }
    /**
     * `legal` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val legal = reachableLegalTiles.distinct()
    if (current in legal && canAttack(current)) return current
    legal.filter { it != current && canAttack(it) }.minWithOrNull(progressOrder)?.let { return it }
    legal.filter { it != current }.minWithOrNull(progressOrder)?.let { return it }
    return current.takeIf { it in legal }
}

/** S57EscortFocusBlocker: 지휘관 공격 칸으로 향하는 유일한 전진 칸을 실제로 점유한 적이다. */
internal data class S57EscortFocusBlocker(
    /** unitId: 전투 무장 상태를 담는다. */
    val unitId: String,
    /** tile: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val tile: Pair<Int, Int>,
    /** hitPoints: 검증 대상 목록을 담는다. */
    val hitPoints: Int,
    /** retreatCount: 현재 Unit.incRetreat 상태이며, 표시만 남은 이전 전사자는 새 장애물보다 우선순위가 낮다. */
    val retreatCount: Int = 0,
)

/** S57EscortFocusBlockerFallback: S57 호위 경로의 지휘관 우선 원칙을 유지하면서, 이동 가능한 경계와 최종 지휘관 공격 칸 사이를 막는 호위병이 있을 때만 실제 공격을 한 번 허용한다. 일반 호위병을 점수 대상에 포함하지 않으며, 현재 턴의 합법 칸에서 지휘관을 공격할 수 있으면 항상 그 행동이 우선이다. 후보는 지휘관 공격 준비 칸까지의 거리를 줄이는 상하좌우 다음 칸을 점유해야 한다. */
internal data class S57EscortFocusBlockerFallback(
    /** blocker: 검증 실행 문맥에서 사용하는 상태 값을 담는다. */
    val blocker: S57EscortFocusBlocker,
    /** attackFrom: 입력기가 일반 공격 UI를 실행해야 하는 합법 칸이다. */
    val attackFrom: Pair<Int, Int>,
)

/** s57EscortAttackFrom: 호위 유닛이 현재 턴에 이 호위병을 공격할 수 있는 결정적 칸이다. */
internal fun s57EscortAttackFrom(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    guardTile: Pair<Int, Int>,
    attackAllScreen: Boolean,
    attackOffsets: Set<Pair<Int, Int>>,
): Pair<Int, Int>? {

    /** canAttack: 공격 가능 여부를 전투 규칙으로 판정한다. */
    fun canAttack(from: Pair<Int, Int>) = attackAllScreen ||
            (guardTile.first - from.first to guardTile.second - from.second) in attackOffsets


    /** distance: 두 위치 사이의 이동 거리를 계산한다. */
    fun distance(left: Pair<Int, Int>, right: Pair<Int, Int>) =
        kotlin.math.abs(left.first - right.first) + kotlin.math.abs(left.second - right.second)
    return (listOf(current) + reachableLegalTiles).distinct().asSequence().filter(::canAttack)
        .minWithOrNull(compareBy<Pair<Int, Int>> { distance(it, guardTile) }
            .thenBy { distance(current, it) }.thenBy { it.first }.thenBy { it.second })
}

/** s57EscortFocusBlockerFallback: 해당 검증 단계의 입력을 처리해 상태를 갱신한다. */
internal fun s57EscortFocusBlockerFallback(
    current: Pair<Int, Int>,
    reachableLegalTiles: Collection<Pair<Int, Int>>,
    focusTile: Pair<Int, Int>,
    attackAllScreen: Boolean,
    attackOffsets: Set<Pair<Int, Int>>,
    occupiedTiles: Set<Pair<Int, Int>>,
    guards: Collection<S57EscortFocusBlocker>,
    openedStagingReachableByGuard: Map<String, Collection<Pair<Int, Int>>>,
): S57EscortFocusBlockerFallback? {

    /** canAttack: 공격 가능 여부를 전투 규칙으로 판정한다. */
    fun canAttack(from: Pair<Int, Int>, target: Pair<Int, Int>) = attackAllScreen ||
            (target.first - from.first to target.second - from.second) in attackOffsets


    /** distance: 두 위치 사이의 이동 거리를 계산한다. */
    fun distance(left: Pair<Int, Int>, right: Pair<Int, Int>) =
        kotlin.math.abs(left.first - right.first) + kotlin.math.abs(left.second - right.second)

    /**
     * `legal` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val legal = (listOf(current) + reachableLegalTiles).distinct()
    // 현재 턴의 직접 공격이나 이동 후 장수 공격은 경비병 때문에 우선순위가 바뀌지 않는다.
    if (legal.any { canAttack(it, focusTile) }) return null
    if (attackAllScreen || attackOffsets.isEmpty()) return null
    /**
     * `stagingTiles` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val stagingTiles = attackOffsets.map { (dx, dy) -> focusTile.first - dx to focusTile.second - dy }.distinct()
    /**
     * `candidates` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val candidates = guards.asSequence()
        .filter { it.tile in occupiedTiles }
        .mapNotNull { guard ->
            /**
             * `attackFrom` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val attackFrom = s57EscortAttackFrom(
                current, legal, guard.tile, attackAllScreen, attackOffsets,
            )
                ?: return@mapNotNull null
            // `reachableLegalTiles`는 점유 칸을 제외한 실제 플러드 필 결과다. 가장 가까운 실제 공격 대기 경계만 대상이 될 수 있으며,
            // 같은 첫 방 진입에서 이후 복도의 모든 경비병을 대상으로 만들지 않는다.
            val closestStagingDistance = legal.minOf { frontier ->
                stagingTiles.minOf { staging -> distance(frontier, staging) }
            }
            /**
             * `blocksProgress` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val blocksProgress = legal.any { frontier ->
                stagingTiles.minOf { staging -> distance(frontier, staging) } == closestStagingDistance &&
                        stagingTiles.any { staging ->
                            distance(frontier, guard.tile) == 1 &&
                                    distance(guard.tile, staging) < distance(frontier, staging)
                        }
            }
            // 맨해튼 거리만으로 방해자를 추론하지 않는다. 이 경비병을 제거했을 때 이번 또는 다음 이동 투영에서 호위대가 실제 물리 공격 대기 칸에 들어갈 수 있을 때만 예외다.
            // 이는 읽기 전용 경로 근거이며, 실제 경비병 공격은 계속 CommandLayer -> 맵 UI를 거친다.
            val opensStagingRoute = openedStagingReachableByGuard[guard.unitId]
                ?.any { it in stagingTiles } == true
            if (blocksProgress && opensStagingRoute) S57EscortFocusBlockerFallback(guard, attackFrom) else null
        }
        .toList()
    // S57 스크립트는 패배한 경비병을 다시 보이게 할 수 있으므로, 실제 후퇴 횟수만 이용해 새 즉시 방해자를 우선한다.
    return candidates.filter { it.blocker.retreatCount == 0 }.ifEmpty { candidates }
        .minWithOrNull(compareBy<S57EscortFocusBlockerFallback> {
            stagingTiles.minOf { staging -> distance(it.blocker.tile, staging) }
        }.thenBy { distance(current, it.blocker.tile) }
            .thenBy { distance(current, it.attackFrom) }
            .thenBy { it.blocker.hitPoints }.thenBy { it.blocker.tile.first }
            .thenBy { it.blocker.tile.second }.thenBy { it.blocker.unitId })
}

/** s57FirstRoomActionRank: S57 첫 방 운영 계획기에서만 사용하는 전역 우선순위이다. */
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

/** waitForS57AuthoredAttrition: S_57의 원본 두 번째 방 이벤트는 의도적인 거의 패배 분기이다. 첫 방이 열리면 중앙·상태 순서 전에 totalUnit(MINE) < 2 조건이 필요하다. 운영 검증기는 HP·가시성·시나리오 변수를 직접 쓰지 않고 일반 적 피해로 이 분기에 도달해야 한다. 새로 드러난 Sun 계열 세 유닛이 있는 동안에는 정상 이동하되, 실제 소모가 발생할 때까지 공격 대신 WAIT를 실행한다. */
internal fun waitForS57AuthoredAttrition(
    scenario: String,
    visiblePlayerCount: Int,
    visibleEnemySourceIds: Collection<Int>,
): Boolean = scenario == "S_57" && visiblePlayerCount >= 2 &&
        visibleEnemySourceIds.any { it in setOf(166, 167, 168) }
