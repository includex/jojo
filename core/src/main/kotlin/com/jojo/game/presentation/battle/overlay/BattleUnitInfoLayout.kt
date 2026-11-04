package com.jojo.game.presentation.battle.overlay

/** Shared logical-space geometry for the character sheet and its pointer targets. */
object BattleUnitInfoLayout {
    data class Rect(val x: Float, val y: Float, val width: Float, val height: Float) {
        fun contains(px: Float, py: Float) = px >= x && px < x + width && py >= y && py < y + height
    }
    val panel = Rect(160f, 80f, 1168f, 640f)
    val portrait = Rect(176f, 96f, 360f, 568f)
    val tabs = List(5) { Rect(560f + it * 148f, 628f, 140f, 44f) }
    val previous = Rect(956f, 96f, 160f, 44f)
    val next = Rect(1132f, 96f, 160f, 44f)
    val close = Rect(1260f, 674f, 48f, 36f)
    val rates = Rect(560f, 96f, 160f, 44f)
    fun buttonAt(x: Float, y: Float): Int? = when {
        rates.contains(x, y) -> 9
        previous.contains(x, y) -> 5
        next.contains(x, y) -> 6
        close.contains(x, y) -> 7
        else -> tabs.indexOfFirst { it.contains(x, y) }.takeIf { it >= 0 }
    }
}
