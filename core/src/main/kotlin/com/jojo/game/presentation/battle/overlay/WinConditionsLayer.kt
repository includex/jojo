// Game
package com.jojo.game.presentation.battle.overlay

/** WinConditionsLayer: 승리 조건 화면의 서식 텍스트와 취소 동작을 관리한다. */
class WinConditionsLayer {
    /**
     * `View`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    data class View(val first: String, val second: String, val attached: Boolean)

    /**
     * `done` ((() -> Unit)?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var done: (() -> Unit)? = null
    /**
     * `v` (View?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var v: View? = null


    /**
     * `onCreate`: 객체나 결과를 생성한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun onCreate(text: String, round: Int, onClose: () -> Unit): View {
        done = onClose
        val t = text.replaceFirst(
            "\n",
            "<br/>"
        ); return View(
            "<b><color=#ff0000>승리 조건</c><br/><color=#777777>$t<br/>제한 턴 수 $round</c></b>",
            "<b><color=#FFFFFF>승리 조건</c><br/><color=#FFFFFF>$t<br/>제한 턴 수 $round</c></b>",
            true
        ).also { v = it }
    }


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view(): View = v ?: View("", "", false)
    // 원본 콜백에는 중복 호출 방지가 없다. 일반적으로 화면 제거 뒤 입력 전달이
    // 멈춰도 직접 두 번째 종료 입력을 주면 fn()을 호출하는 계약을 유지한다.

    fun cancel(event: Int): Boolean {
        if (event != TOUCH_END) return false; done?.invoke(); v = v?.copy(attached = false); return true
    }

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
    }
}
