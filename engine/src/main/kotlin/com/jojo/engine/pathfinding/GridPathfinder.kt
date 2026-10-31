package com.jojo.engine.pathfinding

/** Four-direction weighted search with deterministic insertion-order tie breaking. */
object GridPathfinder {
    private data class SearchNode(
        val x: Int,
        val y: Int,
        val totalExpend: Int,
        val direction: Int,
        val parent: Pair<Int, Int>?,
        val insertionOrder: Int,
    )

    fun find(
        startX: Int,
        startY: Int,
        targetX: Int,
        targetY: Int,
        grid: PathGrid,
        occupied: Set<Pair<Int, Int>>,
        costs: PathCosts,
    ): List<Pair<Int, Int>>? {
        if (!grid.contains(startX, startY) || !grid.contains(targetX, targetY)) return null
        val directions = arrayOf(0 to -1, 1 to 0, 0 to 1, -1 to 0)
        val best = HashMap<Pair<Int, Int>, Int>()
        val queue = mutableListOf(SearchNode(startX, startY, 0, -1, null, 0))
        val popped = mutableListOf<SearchNode>()
        best[startX to startY] = 1
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
                if (!grid.contains(x, y)) return@forEach
                var cost = costs.step
                if (grid.blocked(x, y)) cost += costs.blocked
                if ((x to y) in occupied) cost += costs.occupied
                val direction = direction(node.x, node.y, x, y)
                if (direction != node.direction) cost += costs.directionChange
                val total = node.totalExpend + cost
                val key = x to y
                if (best[key]?.let { total >= it } == true) return@forEach
                best[key] = total
                queue += SearchNode(x, y, total, direction, node.x to node.y, insertionOrder++)
            }
            queue.sortWith(compareBy<SearchNode> { it.totalExpend }.thenBy { it.insertionOrder })
        }
        if (destination == null) return null

        // Preserve the first popped parent for each coordinate, including when
        // the same point was queued multiple times with different costs.
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

    /** Screen coordinates: up=0, right=1, down=2, left=3, stationary=-1. */
    fun direction(fromX: Int, fromY: Int, toX: Int, toY: Int): Int = when {
        toY - fromY < 0 -> 0
        toY - fromY > 0 -> 2
        fromX - toX < 0 -> 1
        fromX - toX > 0 -> 3
        else -> -1
    }
}
