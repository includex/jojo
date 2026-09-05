package com.jojo.game.presentation.scenario.hall

/** Source-authored PropertyLayer table geometry and paint-order contract. */
internal object HallPropertyRenderPlan {
    fun rowY(index: Int): Float = 481.58f - index * 67.08f

    fun paintOrder(rowCount: Int): List<String> = buildList {
        addAll(listOf("background", "frame", "title", "headers"))
        repeat(rowCount) { add("row-$it") }
        addAll(listOf("tabs", "confirm"))
    }
}
