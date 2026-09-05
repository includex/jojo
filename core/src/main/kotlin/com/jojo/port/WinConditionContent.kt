package com.jojo.port

/** Pure port of BattleLayer.winConProcess's `r` string construction. */
object WinConditionContent {
    data class HiddenItem(val variable: Int, val description: String)
    fun build(
        text: String?, vs: List<Int>?, talk: List<Int>?, items: List<HiddenItem>?,
        unitName: (Int) -> String?, variable: (Int) -> Int,
    ): String = buildString {
        if (text != null) append(text).append('\n')
        if (vs != null) {
            append("\n일대일 대결\n")
            vs.chunked(2).filter { it.size == 2 }.forEach { (a, b) ->
                val left = unitName(a); val right = unitName(b)
                if (left != null && right != null) append(left).append(" VS ").append(right).append('\n')
            }
        }
        if (talk != null) {
            append("\n대화\n")
            talk.chunked(2).filter { it.size == 2 }.forEach { (a, b) ->
                val left = unitName(a); val right = unitName(b)
                if (left != null && right != null) append(left).append(" -> ").append(right).append('\n')
            }
        }
        if (items != null) {
            append("\n아이템\n")
            items.forEachIndexed { index, item ->
                if (variable(item.variable) != 1) {
                    append(item.description).append(',')
                    if ((index + 1) % 5 == 0) append('\n')
                }
            }
        }
    }
}
