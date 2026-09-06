// Battle
package com.jojo.game.domain.battle

/** BattleMovementPlanner: 이동력·지형 비용·점유 상태를 고려해 이동 범위와 실제 경로를 계산한다. */
internal class BattleMovementPlanner<Actor : Any>(
    /**
     * `isInside` ((Point) -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val isInside: (Point) -> Boolean,
    /**
     * `terrainCost` ((Actor, Point) -> Int,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val terrainCost: (Actor, Point) -> Int,
    /**
     * `isBlocked` ((Point) -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val isBlocked: (Point) -> Boolean,
    /**
     * `occupantAt` ((Point) -> Actor?,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val occupantAt: (Point) -> Actor?,
    /**
     * `actorId` ((Actor) -> String,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val actorId: (Actor) -> String,
    /**
     * `isSameActor` ((Actor, Actor) -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val isSameActor: (Actor, Actor) -> Boolean,
    /**
     * `areAllied` ((Actor, Actor) -> Boolean,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val areAllied: (Actor, Actor) -> Boolean,
    /**
     * `orderedMovementOffsets` (List<Point>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val orderedMovementOffsets: List<Point>,
    /**
     * `enemyNearOffsets` (Collection<Point>,): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
     */

    private val enemyNearOffsets: Collection<Point>,
) {

    /**
     * `MovementRules` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class MovementRules(
        /**
         * `ignoresTerrain` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val ignoresTerrain: Boolean = false,
        /**
         * `treatsEveryTerrainAsOne` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val treatsEveryTerrainAsOne: Boolean = false,
        /**
         * `ignoresEnemyNear` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val ignoresEnemyNear: Boolean = false,
    )


    /**
     * `PathRules` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class PathRules(
        /**
         * `avoidEnemies` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val avoidEnemies: Boolean = false,
        /**
         * `penalizeEnemyTiles` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val penalizeEnemyTiles: Boolean = false,
        /**
         * `allowEnemyOnTarget` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val allowEnemyOnTarget: Boolean = false,
        /**
         * `treatsEveryTerrainAsOne` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val treatsEveryTerrainAsOne: Boolean = false,
    )


    /**
     * `MovePoint` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class MovePoint(val remaining: Int, val parent: Point?)


    /**
     * `MovePoints` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    data class MovePoints(val points: Map<Point, MovePoint>, val start: Point) {

        /**
         * `pathTo`: 입력을 규칙에 따라 계산·변환한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
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
     * `movePoints`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun movePoints(
        actor: Actor,
        movement: Int,
        rules: MovementRules,
        ignoredEnemyId: String? = null,
        startOverride: Point? = null,
    ): MovePoints {

        /**
         * `Queued` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class Queued(
            /**
             * `point` (Point,): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val point: Point,
            /**
             * `remaining` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val remaining: Int,
            /**
             * `parent` (Point?,): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val parent: Point?,
            /**
             * `blockedByEnemyNear` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val blockedByEnemyNear: Boolean = false,
        )

        /**
         * `start` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val start = startOverride ?: error("A movement start is required")
        /**
         * `remainingByPoint` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val remainingByPoint = linkedMapOf(start to movement)
        /**
         * `queue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val queue = ArrayDeque<Queued>()
        queue += Queued(start, movement, null)
        /**
         * `processed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val processed = linkedMapOf<Point, MovePoint>()
        while (queue.isNotEmpty()) {
            /**
             * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val current = queue.removeFirst()
            // 재방문 좌표를 다시 넣어, 최초 삽입 순서가 아닌 원본의 선입선출 꺼냄 순서를 유지한다.
            processed.remove(current.point)
            processed[current.point] = MovePoint(current.remaining, current.parent)
            if (current.blockedByEnemyNear) continue
            orderedMovementOffsets.forEach { offset ->
                /**
                 * `next` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val next = current.point + offset
                if (!isInside(next) || isBlocked(next)) return@forEach
                /**
                 * `occupant` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val occupant = occupantAt(next)
                if (occupant != null &&
                    actorId(occupant) != ignoredEnemyId &&
                    !isSameActor(occupant, actor) &&
                    !areAllied(occupant, actor)
                ) return@forEach
                /**
                 * `cost` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val cost = if (rules.ignoresTerrain || rules.treatsEveryTerrainAsOne) 1 else terrainCost(actor, next)
                if (cost >= IMPASSABLE_COST || current.remaining < cost) return@forEach
                /**
                 * `remaining` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val remaining = current.remaining - cost
                if (remainingByPoint[next]?.let { remaining <= it } == true) return@forEach
                /**
                 * `enemyNear` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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

        /**
         * `Node` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class Node(val point: Point, val cost: Int, val parent: Point?, val order: Long)

        /**
         * `queue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val queue = mutableListOf(Node(start, 0, null, 0))
        /**
         * `visited` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visited = linkedSetOf(start)
        /**
         * `parents` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val parents = linkedMapOf<Point, Point?>()
        /**
         * `sequence` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var sequence = 1L
        while (queue.isNotEmpty()) {
            /**
             * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val current = queue.removeAt(0)
            parents[current.point] = current.parent
            if (current.point == target) return buildPath(current.point, parents)
            orderedMovementOffsets.forEach { offset ->
                /**
                 * `next` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val next = current.point + offset
                // 검증 전에 좌표를 방문 처리하는 것은 의도된 동작으로, 원본 순회의 방문 집합 규칙과 같다.
                if (!visited.add(next)) return@forEach
                if (!isInside(next) || isBlocked(next)) return@forEach
                /**
                 * `cost` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                var cost = terrainCost(actor, next)
                if (cost >= IMPASSABLE_COST) return@forEach
                if (rules.treatsEveryTerrainAsOne) cost = 1
                if (rules.avoidEnemies && !(rules.allowEnemyOnTarget && next == target)) {
                    /**
                     * `occupant` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                     */

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
        /**
         * `queue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val queue = ArrayDeque<Point>()
        /**
         * `visited` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visited = linkedSetOf(seed)
        queue += seed
        while (queue.isNotEmpty()) {
            /**
             * `point` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val point = queue.removeFirst()
            /**
             * `occupant` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val occupant = occupantAt(point)
            if ((occupant == null || isSameActor(occupant, actor)) && terrainCost(actor, point) < IMPASSABLE_COST) {
                return point
            }
            orderedMovementOffsets.forEach { offset ->
                /**
                 * `next` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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

        /**
         * `Node` 클래스: battle 패키지의 관련 상태와 동작을 묶는다.
         * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
         */

        data class Node(val point: Point, val totalExpend: Int, val order: Long)

        /**
         * `queue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val queue = mutableListOf(Node(seed, 0, 0))
        /**
         * `visited` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val visited = linkedSetOf(seed)
        /**
         * `sequence` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var sequence = 1L
        while (queue.isNotEmpty()) {
            /**
             * `current` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val current = queue.removeAt(0)
            /**
             * `point` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val point = current.point
            /**
             * `occupant` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val occupant = occupantAt(point)
            if ((occupant == null || isSameActor(occupant, actor)) && terrainCost(actor, point) < IMPASSABLE_COST) {
                return point
            }
            orderedMovementOffsets.forEach { offset ->
                /**
                 * `next` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val next = point + offset
                if (next !in reachable || !visited.add(next) || !isInsideSearchArea(next)) return@forEach
                queue += Node(next, current.totalExpend + terrainCost(actor, next), sequence++)
            }
            queue.sortWith(compareBy<Node>({ it.totalExpend }, { it.order }))
        }
        return null
    }

    /**
     * `buildPath`: 필요한 객체나 결과를 생성한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    private fun buildPath(destination: Point, parents: Map<Point, Point?>): List<Point> {
        val path = mutableListOf<Point>()
        var cursor: Point? = destination
        while (cursor != null) {
            path += cursor
            cursor = parents[cursor]
        }
        return path.asReversed()
    }

    /**
     * `Point`: 타입의 핵심 동작을 수행한다.
     * 전달된 입력을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    private operator fun Point.plus(other: Point): Point = first + other.first to second + other.second

    private companion object {
        /**
         * `IMPASSABLE_COST` (상태 값): 현재 객체가 유지하는 구성·진행 상태를 보관한다.
         */

        const val IMPASSABLE_COST = 255
    }
}

private typealias Point = Pair<Int, Int>
