// Battle
package com.jojo.game.domain.battle

/** BattleMovementPlanner: 이동력·지형 비용·점유 상태를 고려해 이동 범위와 실제 경로를 계산한다. */
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

    data class MovementRules(
        val ignoresTerrain: Boolean = false,
        val treatsEveryTerrainAsOne: Boolean = false,
        val ignoresEnemyNear: Boolean = false,
    )


    data class PathRules(
        val avoidEnemies: Boolean = false,
        val penalizeEnemyTiles: Boolean = false,
        val allowEnemyOnTarget: Boolean = false,
        val treatsEveryTerrainAsOne: Boolean = false,
    )


    data class MovePoint(val remaining: Int, val parent: Point?)


    data class MovePoints(val points: Map<Point, MovePoint>, val start: Point) {

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
    fun movePoints(
        actor: Actor,
        movement: Int,
        rules: MovementRules,
        ignoredEnemyId: String? = null,
        startOverride: Point? = null,
    ): MovePoints {

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
            // 재방문 좌표를 다시 넣어, 최초 삽입 순서가 아닌 원본의 선입선출 꺼냄 순서를 유지한다.
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

    /** findPath: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    fun findPath(
        actor: Actor,
        start: Point,
        target: Point,
        rules: PathRules,
    ): List<Point>? {

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
                // 검증 전에 좌표를 방문 처리하는 것은 의도된 동작으로, 원본 순회의 방문 집합 규칙과 같다.
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

    /** findScriptedDestination: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
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

    /** findEmptyPosition: 입력 조건과 전투 규칙에 맞는 결과를 계산한다. */
    fun findEmptyPosition(
        actor: Actor,
        seed: Point,
        reachable: Set<Point>,
        isInsideSearchArea: (Point) -> Boolean = isInside,
    ): Point? {

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
