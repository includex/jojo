// Infrastructure
package com.jojo.game.infrastructure.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

/** HallPathGrid: 홀 이동용 지도 격자와 안정적인 가중 경로 탐색을 제공한다. */
data class HallPathGrid(val rows: List<IntArray>) {

    fun blocked(x: Int, y: Int): Boolean = rows.getOrNull(y)?.getOrNull(x) != 0

    companion object {

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


object HallPathfinder {
    private data class SearchNode(
        val x: Int,
        val y: Int,
        val totalExpend: Int,
        val direction: Int,
        val parent: Pair<Int, Int>?,
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
        val directions = arrayOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)
        val best = HashMap<Int, Int>()
        val queue = mutableListOf(SearchNode(startX, startY, 0, -1, null, 0))
        val popped = mutableListOf<SearchNode>()
        best[(startX shl 8) or startY] = 1
        var insertionOrder = 1
        var destination: SearchNode? = null

        while (queue.isNotEmpty()) {
            val node = queue.removeAt(0)
            popped.add(0, node)
            if (node.x == targetX && node.y == targetY) {
                destination = node
                break
            }
            directions.forEach { (dx, dy) ->
                val x = node.x + dx
                val y = node.y + dy
                if (x !in 0..99 || y !in 0..99) return@forEach
                var cost = 1
                if (grid?.blocked(x, y) == true) cost += 767
                if ((x to y) in occupied) cost += 511
                val direction = direction(node.x, node.y, x, y)
                if (direction != node.direction) cost += 15
                val total = node.totalExpend + cost
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
        val reversed = mutableListOf<Pair<Int, Int>>()
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
