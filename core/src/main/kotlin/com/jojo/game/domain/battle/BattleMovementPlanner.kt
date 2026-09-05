package com.jojo.game.domain.battle

/**
 * Pure tactical movement algorithms. Mutable battle state is observed only
 * through the collaborators supplied at construction time.
 */
internal class BattleMovementPlanner<Actor : Any>(
    private val isInside: (Point) -> Boolean,
    private val terrainCost: (Actor, Point) -> Int,
    private val isBlocked: (Point) -> Boolean,
    private val occupantAt: (Point) -> Actor?,
    private val actorId: (Actor) -> String,
    private val isSameActor: (Actor, Actor) -> Boolean,
    private val areAllied: (Actor, Actor) -> Boolean,
    private val orderedMovementOffsets: List<Point>,
    private val enemyNearOffsets: Collection<Point>,
) {
    /**
     * data class  `MovementRules`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class MovementRules(
        val ignoresTerrain: Boolean = false,
        val treatsEveryTerrainAsOne: Boolean = false,
        val ignoresEnemyNear: Boolean = false,
    )

    /**
     * data class  `PathRules`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class PathRules(
        val avoidEnemies: Boolean = false,
        val penalizeEnemyTiles: Boolean = false,
        val allowEnemyOnTarget: Boolean = false,
        val treatsEveryTerrainAsOne: Boolean = false,
    )

    /**
     * data class  `MovePoint`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class MovePoint(val remaining: Int, val parent: Point?)

    /**
     * data class  `MovePoints`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class MovePoints(val points: Map<Point, MovePoint>, val start: Point) {
        /**
         * 공개 메서드 `pathTo`
         *
         * ### 파라미터
        - `destination` (`Point`): 구현 기준으로 역할 및 허용 값 정의 필요
         *
         * ### 응답 스펙
         * - 반환 타입: `List<Point>`
         * - 반환값: 동작 결과의 도메인 값입니다.
         */

        fun pathTo(destination: Point): List<Point> {
            val path = mutableListOf(destination)
            var point = requireNotNull(points[destination]).parent
            while (point != null) {
                path += point
                if (point == start) break
                point = requireNotNull(points[point]).parent
            }
            return path.asReversed()
        }
    }

    /**
     * Remaining-movement flood fill. A tile adjacent to an enemy is retained
     * as a destination but is not expanded unless [MovementRules] allows it.
     */
    fun movePoints(
        actor: Actor,
        movement: Int,
        rules: MovementRules,
        ignoredEnemyId: String? = null,
        startOverride: Point? = null,
    ): MovePoints {
        /**
         * data class  `Queued`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Queued(
            val point: Point,
            val remaining: Int,
            val parent: Point?,
            val blockedByEnemyNear: Boolean = false,
        )

        val start = startOverride ?: error("A movement start is required")
        val remainingByPoint = linkedMapOf(start to movement)
        val queue = ArrayDeque<Queued>()
        queue += Queued(start, movement, null)
        val processed = linkedMapOf<Point, MovePoint>()
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            // Reinsert revisits so final iteration order matches the authored
            // FIFO pop order rather than the first insertion order.
            processed.remove(current.point)
            processed[current.point] = MovePoint(current.remaining, current.parent)
            if (current.blockedByEnemyNear) continue
            orderedMovementOffsets.forEach { offset ->
                val next = current.point + offset
                if (!isInside(next) || isBlocked(next)) return@forEach
                val occupant = occupantAt(next)
                if (occupant != null &&
                    actorId(occupant) != ignoredEnemyId &&
                    !isSameActor(occupant, actor) &&
                    !areAllied(occupant, actor)
                ) return@forEach
                val cost = if (rules.ignoresTerrain || rules.treatsEveryTerrainAsOne) 1 else terrainCost(actor, next)
                if (cost >= IMPASSABLE_COST || current.remaining < cost) return@forEach
                val remaining = current.remaining - cost
                if (remainingByPoint[next]?.let { remaining <= it } == true) return@forEach
                val enemyNear = !rules.ignoresEnemyNear && enemyNearOffsets.any { nearOffset ->
                    occupantAt(next + nearOffset)?.let { nearActor ->
                        actorId(nearActor) != ignoredEnemyId && !areAllied(nearActor, actor)
                    } == true
                }
                remainingByPoint[next] = remaining
                queue += Queued(next, remaining, current.point, enemyNear)
            }
        }
        return MovePoints(processed, start)
    }

    /**
     * Stable weighted path search. Equal costs keep [orderedMovementOffsets]
     * order while [rules] describes how occupied enemy tiles are handled.
     */
    fun findPath(
        actor: Actor,
        start: Point,
        target: Point,
        rules: PathRules,
    ): List<Point>? {
        /**
         * data class  `Node`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Node(val point: Point, val cost: Int, val parent: Point?, val order: Long)

        val queue = mutableListOf(Node(start, 0, null, 0))
        val visited = linkedSetOf(start)
        val parents = linkedMapOf<Point, Point?>()
        var sequence = 1L
        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            parents[current.point] = current.parent
            if (current.point == target) return buildPath(current.point, parents)
            orderedMovementOffsets.forEach { offset ->
                val next = current.point + offset
                // Marking a coordinate before validation is intentional and
                // matches the authored traversal's visited-set behavior.
                if (!visited.add(next)) return@forEach
                if (!isInside(next) || isBlocked(next)) return@forEach
                var cost = terrainCost(actor, next)
                if (cost >= IMPASSABLE_COST) return@forEach
                if (rules.treatsEveryTerrainAsOne) cost = 1
                if (rules.avoidEnemies && !(rules.allowEnemyOnTarget && next == target)) {
                    val occupant = occupantAt(next)
                    if (occupant != null && !areAllied(occupant, actor)) {
                        if (rules.penalizeEnemyTiles) cost += IMPASSABLE_COST else return@forEach
                    }
                }
                queue += Node(next, current.cost + cost, current.point, sequence++)
            }
            queue.sortWith(compareBy<Node>({ it.cost }, { it.order }))
        }
        return null
    }

    /** Finds the first passable, unoccupied tile in authored FIFO order. */
    fun findScriptedDestination(
        actor: Actor,
        seed: Point,
        isInsideSearchArea: (Point) -> Boolean = isInside,
    ): Point? {
        val queue = ArrayDeque<Point>()
        val visited = linkedSetOf(seed)
        queue += seed
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            val occupant = occupantAt(point)
            if ((occupant == null || isSameActor(occupant, actor)) && terrainCost(actor, point) < IMPASSABLE_COST) {
                return point
            }
            orderedMovementOffsets.forEach { offset ->
                val next = point + offset
                if (isInsideSearchArea(next) && visited.add(next)) queue += next
            }
        }
        return null
    }

    /**
     * Weighted empty-position lookup constrained to the current reachable
     * set. Accumulated terrain cost wins before authored insertion order.
     */
    fun findEmptyPosition(
        actor: Actor,
        seed: Point,
        reachable: Set<Point>,
        isInsideSearchArea: (Point) -> Boolean = isInside,
    ): Point? {
        /**
         * data class  `Node`
         *
         * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
         *
         * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
         */

        data class Node(val point: Point, val totalExpend: Int, val order: Long)

        val queue = mutableListOf(Node(seed, 0, 0))
        val visited = linkedSetOf(seed)
        var sequence = 1L
        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            val point = current.point
            val occupant = occupantAt(point)
            if ((occupant == null || isSameActor(occupant, actor)) && terrainCost(actor, point) < IMPASSABLE_COST) {
                return point
            }
            orderedMovementOffsets.forEach { offset ->
                val next = point + offset
                if (next !in reachable || !visited.add(next) || !isInsideSearchArea(next)) return@forEach
                queue += Node(next, current.totalExpend + terrainCost(actor, next), sequence++)
            }
            queue.sortWith(compareBy<Node>({ it.totalExpend }, { it.order }))
        }
        return null
    }

    private fun buildPath(destination: Point, parents: Map<Point, Point?>): List<Point> {
        val path = mutableListOf<Point>()
        var cursor: Point? = destination
        while (cursor != null) {
            path += cursor
            cursor = parents[cursor]
        }
        return path.asReversed()
    }

    private operator fun Point.plus(other: Point): Point = first + other.first to second + other.second

    private companion object {
        const val IMPASSABLE_COST = 255
    }
}

private typealias Point = Pair<Int, Int>
