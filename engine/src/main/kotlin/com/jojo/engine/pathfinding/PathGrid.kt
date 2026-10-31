package com.jojo.engine.pathfinding

/** Rectangular terrain snapshot. Nonzero cells incur the configured terrain penalty. */
class PathGrid(rows: List<IntArray>) {
    private val cells = rows.map { it.copyOf() }
    val width: Int = cells.firstOrNull()?.size ?: 0
    val height: Int = cells.size

    init {
        require(width > 0 && height > 0) { "Path grid must not be empty" }
        require(cells.all { it.size == width }) { "Path grid rows must have equal widths" }
    }

    fun contains(x: Int, y: Int): Boolean = x in 0 until width && y in 0 until height
    fun blocked(x: Int, y: Int): Boolean = cells.getOrNull(y)?.getOrNull(x) != 0

    companion object {
        fun open(width: Int, height: Int): PathGrid {
            require(width > 0 && height > 0) { "Path grid dimensions must be positive" }
            return PathGrid(List(height) { IntArray(width) })
        }
    }
}

/** Game-owned movement policy supplied to the reusable search algorithm. */
data class PathCosts(
    val step: Int,
    val blocked: Int,
    val occupied: Int,
    val directionChange: Int,
) {
    init {
        require(step > 0) { "Step cost must be positive" }
        require(blocked >= 0 && occupied >= 0 && directionChange >= 0) { "Path penalties must not be negative" }
    }
}
