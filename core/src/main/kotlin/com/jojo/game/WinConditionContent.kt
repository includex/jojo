package com.jojo.game

/** Pure implementation of BattleScreen.winConProcess's `r` string construction. */
object WinConditionContent {
    /**
     * data class  `HiddenItem`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    data class HiddenItem(val variable: Int, val description: String)

    fun build(
        text: String?, vs: List<Int>?, talk: List<Int>?, items: List<HiddenItem>?,
        unitName: (Int) -> String?, variable: (Int) -> Int,
    ): String = buildString {
        if (text != null) append(text).append('\n')
        if (vs != null) {
            append("\n일대일 대결\n")
            vs.chunked(2).filter { it.size == 2 }.forEach { (a, b) ->
                val left = unitName(a)
                val right = unitName(b)
                if (left != null && right != null) append(left).append(" VS ").append(right).append('\n')
            }
        }
        if (talk != null) {
            append("\n대화\n")
            talk.chunked(2).filter { it.size == 2 }.forEach { (a, b) ->
                val left = unitName(a)
                val right = unitName(b)
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
