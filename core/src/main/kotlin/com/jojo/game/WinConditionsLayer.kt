package com.jojo.game

/** 승리 조건 화면의 서식 텍스트와 취소 동작을 관리한다. */
class WinConditionsLayer {
    data class View(val first: String, val second: String, val attached: Boolean)

    private var done: (() -> Unit)? = null
    private var v: View? = null


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


    fun view(): View = v ?: View("", "", false)
    // 원본 콜백에는 중복 호출 방지가 없다. 일반적으로 화면 제거 뒤 입력 전달이
    // 멈춰도 직접 두 번째 종료 입력을 주면 fn()을 호출하는 계약을 유지한다.

    fun cancel(event: Int): Boolean {
        if (event != TOUCH_END) return false; done?.invoke(); v = v?.copy(attached = false); return true
    }

    companion object {
        const val TOUCH_END = 2
    }
}
