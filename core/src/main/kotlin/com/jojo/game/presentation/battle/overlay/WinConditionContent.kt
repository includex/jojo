// Game
package com.jojo.game.presentation.battle.overlay

/** WinConditionContent: 승리 조건 문자열을 구성하는 순수 로직이다. */
object WinConditionContent {

    /**
     * `HiddenItem`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class HiddenItem(val variable: Int, val description: String)

    /**
     * `build`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun build(
        text: String?, vs: List<Int>?, talk: List<Int>?, items: List<HiddenItem>?,
        unitName: (Int) -> String?, variable: (Int) -> Int,
    ): String = buildString {
        if (text != null) append(text).append('\n')
        if (vs != null) {
            append("\n일대일 대결\n")
            vs.chunked(2).filter { it.size == 2 }.forEach { (a, b) ->
                /**
                 * `left` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val left = unitName(a)
                /**
                 * `right` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val right = unitName(b)
                if (left != null && right != null) append(left).append(" VS ").append(right).append('\n')
            }
        }
        if (talk != null) {
            append("\n대화\n")
            talk.chunked(2).filter { it.size == 2 }.forEach { (a, b) ->
                /**
                 * `left` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

                val left = unitName(a)
                /**
                 * `right` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
                 * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
                 */

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
