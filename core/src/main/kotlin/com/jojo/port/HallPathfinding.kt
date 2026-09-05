package com.jojo.port

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader

/** Original HallLayer Pmap grid and its stable weighted A* implementation. */
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

    /** Port of HallLayer.AStar, including obstacle/unit penalties and stable ties. */
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
            // Modern JS Array.sort is stable. Keep insertion order explicit so
            // equal-cost routes select the same authored turn sequence.
            queue.sortWith(compareBy<SearchNode> { it.totalExpend }.thenBy { it.insertionOrder })
        }
        if (destination == null) return null

        // Source stores every popped node in reverse order, then assigns them
        // into a coordinate map. Iterating that list makes the oldest popped
        // parent win when a coordinate was reached more than once.
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

    /** Config.DIR values used by HallLayer._countDir. */
    fun direction(fromX: Int, fromY: Int, toX: Int, toY: Int): Int = when {
        // HallUnit._move2 compares the next point against the previous one.
        // Increasing authored Y is DOWN, decreasing Y is UP.
        toY - fromY < 0 -> 0 // UP
        toY - fromY > 0 -> 2 // DOWN
        fromX - toX < 0 -> 1 // RIGHT
        fromX - toX > 0 -> 3 // LEFT
        else -> -1
    }
}
