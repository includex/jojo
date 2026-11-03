package com.jojo.game.presentation.battle.overlay

/** Shared logical-space geometry for the character sheet and its pointer targets. */
object BattleUnitInfoLayout {
    data class Rect(val x: Float, val y: Float, val width: Float, val height: Float) {
        fun contains(px: Float, py: Float) = px >= x && px < x + width && py >= y && py < y + height
    }
    val panel = Rect(80f, 24f, 1328f, 752f)
    val portrait = Rect(112f, 316f, 216f, 270f)
    val tabs = List(5) { Rect(112f + it * 244f, 656f, 228f, 56f) }
    val previous = Rect(970f, 40f, 160f, 48f)
    val next = Rect(1140f, 40f, 160f, 48f)
    val close = Rect(760f, 40f, 160f, 48f)
    val rates = Rect(112f, 40f, 180f, 48f)
    fun buttonAt(x: Float, y: Float): Int? = when {
        rates.contains(x, y) -> 9
        previous.contains(x, y) -> 5
        next.contains(x, y) -> 6
        close.contains(x, y) -> 7
        else -> tabs.indexOfFirst { it.contains(x, y) }.takeIf { it >= 0 }
    }
}
