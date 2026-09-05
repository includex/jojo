package com.jojo.game

/** battle/WinConditionsLayer.js RichText and Panel_cancel contract. */
class WinConditionsLayer {
    data class View(val first: String, val second: String, val attached: Boolean)

    private var done: (() -> Unit)? = null
    private var v: View? = null

    /**
     * 공개 메서드 `onCreate`
     *
     * ### 파라미터
    - `text` (`String`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `round` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `onClose` (`(`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `View`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view(): View = v ?: View("", "", false)
    // The original registered callback has no attached guard: a direct second
    // TOUCH_END still invokes fn(), even though Cocos normally stops routing
    // events after removeFromParent.  Preserve the handler contract itself.
    /**
     * 공개 메서드 `cancel`
     *
     * ### 파라미터
    - `event` (`Int`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun cancel(event: Int): Boolean {
        if (event != TOUCH_END) return false; done?.invoke(); v = v?.copy(attached = false); return true
    }

    companion object {
        const val TOUCH_END = 2
    }
}
