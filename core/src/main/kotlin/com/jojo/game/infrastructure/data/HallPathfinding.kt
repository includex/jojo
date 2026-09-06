// Infrastructure
package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

/** HallPathGrid: 홀 이동용 지도 격자와 안정적인 가중 경로 탐색을 제공한다. */
data class HallPathGrid(val rows: List<IntArray>) {

    /**
     * `blocked`: 타입의 핵심 동작을 수행한다.
     * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
     */

    fun blocked(x: Int, y: Int): Boolean = rows.getOrNull(y)?.getOrNull(x) != 0

    companion object {

        /**
         * `loadOrNull`: 상태나 데이터를 조회한다.
         * 반환값이 있으면 계산 결과를 돌려주고, 없으면 상태 변경 또는 외부 전달로 효과를 남긴다.
         */

        fun loadOrNull(sceneIndex: Int): HallPathGrid? = runCatching {
            val files = Gdx.files ?: return null
            val handle = files.internal("maps/pmaps/$sceneIndex.json")
            if (!handle.exists()) return null
            val root = JsonReader().parse(handle)
            val rows = generateSequence(root.child) { it.next }.map { it.asIntArray() }.toList()
            require(rows.size == 100 && rows.all { it.size == 100 })
            HallPathGrid(rows)
        }.getOrNull()
    }
}


/**
 * `HallPathfinder` 싱글턴 객체: data 패키지의 관련 상태와 동작을 묶는다.
 * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
 */

object HallPathfinder {
    /**
     * `SearchNode` 클래스: data 패키지의 관련 상태와 동작을 묶는다.
     * 입력 상태를 받아 도메인·화면 흐름에서 재사용할 수 있는 책임을 제공한다.
     */

    private data class SearchNode(
        /**
         * `x` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val x: Int,
        /**
         * `y` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val y: Int,
        /**
         * `totalExpend` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val totalExpend: Int,
        /**
         * `direction` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val direction: Int,
        /**
         * `parent` (Pair<Int, Int>?,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val parent: Pair<Int, Int>?,
        /**
         * `insertionOrder` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val insertionOrder: Int,
    )

    /** 장애물·유닛 비용과 동일 비용 경로의 순서를 반영해 길을 찾는다. */
    fun find(
        startX: Int,
        startY: Int,
        targetX: Int,
        targetY: Int,
        grid: HallPathGrid?,
        occupied: Set<Pair<Int, Int>>,
    ): List<Pair<Int, Int>>? {
        if (startX !in 0..99 || startY !in 0..99 || targetX !in 0..99 || targetY !in 0..99) return null
        /**
         * `directions` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val directions = arrayOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)
        /**
         * `best` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val best = HashMap<Int, Int>()
        /**
         * `queue` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val queue = mutableListOf(SearchNode(startX, startY, 0, -1, null, 0))
        /**
         * `popped` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val popped = mutableListOf<SearchNode>()
        best[(startX shl 8) or startY] = 1
        /**
         * `insertionOrder` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var insertionOrder = 1
        /**
         * `destination` (SearchNode?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var destination: SearchNode? = null

        while (queue.isNotEmpty()) {
            /**
             * `node` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
             * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
             */

            val node = queue.removeAt(0)
            popped.add(0, node)
            if (node.x == targetX && node.y == targetY) {
                destination = node
                break
            }
            directions.forEach { (dx, dy) ->
                /**
                 * `x` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val x = node.x + dx
                /**
                 * `y` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val y = node.y + dy
                if (x !in 0..99 || y !in 0..99) return@forEach
                /**
                 * `cost` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                var cost = 1
                if (grid?.blocked(x, y) == true) cost += 767
                if ((x to y) in occupied) cost += 511
                /**
                 * `direction` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val direction = direction(node.x, node.y, x, y)
                if (direction != node.direction) cost += 15
                /**
                 * `total` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val total = node.totalExpend + cost
                /**
                 * `key` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val key = (x shl 8) or y
                if (best[key]?.let { total >= it } == true) return@forEach
                best[key] = total
                queue += SearchNode(x, y, total, direction, node.x to node.y, insertionOrder++)
            }
            // 최신 JS의 Array.sort는 안정 정렬이다. 같은 비용 경로도 원본 순서를
            // 선택하도록 삽입 순서를 명시적으로 유지한다.
            queue.sortWith(compareBy<SearchNode> { it.totalExpend }.thenBy { it.insertionOrder })
        }
        if (destination == null) return null

        // 원본은 꺼낸 노드를 역순으로 저장한 뒤 좌표 맵에 할당한다. 이 순서로
        // 순회하면 같은 좌표에 여러 번 도달했을 때 가장 먼저 꺼낸 부모가 유지된다.
        val byPoint = HashMap<Pair<Int, Int>, SearchNode>()
        popped.forEach { byPoint[it.x to it.y] = it }
        /**
         * `reversed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val reversed = mutableListOf<Pair<Int, Int>>()
        /**
         * `cursor` (SearchNode?): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        var cursor: SearchNode? = destination
        while (cursor != null) {
            reversed += cursor.x to cursor.y
            cursor = cursor.parent?.let(byPoint::get)
        }
        return reversed.asReversed()
    }

    /** 두 좌표 사이의 이동 방향 값을 반환한다. */
    fun direction(fromX: Int, fromY: Int, toX: Int, toY: Int): Int = when {
        // 다음 좌표를 이전 좌표와 비교한다. 원본 Y 증가는 아래, 감소는 위다.
        toY - fromY < 0 -> 0 // 위
        toY - fromY > 0 -> 2 // 아래
        fromX - toX < 0 -> 1 // 오른쪽
        fromX - toX > 0 -> 3 // 왼쪽
        else -> -1
    }
}
