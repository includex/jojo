// Battle
package com.jojo.game.presentation.battle.overlay

/** 자동 전투 오버레이의 선택 상태와 라운드 종료 요청을 관리한다. */
class AutoBattleFlow(initialStored: Boolean = false) {
    enum class Overlay { NONE, PROMPT, TUOGUAN }


    /** View: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
    data class View(
        val overlay: Overlay,
        val checked: Boolean,
        val stored: Boolean,
        val collocation: Boolean,
        val endRoundRequests: Int,
    )

    private var overlay = Overlay.NONE
    private var checked = initialStored
    private var stored = initialStored
    private var collocation = false
    private var endRoundRequests = 0


    fun openEndRoundPrompt() {
        checked = stored
        overlay = Overlay.PROMPT
    }


    fun toggle() {
        if (overlay == Overlay.PROMPT) checked = !checked
    }
    fun answer(tag: Int, event: Int): Boolean {
        if (overlay != Overlay.PROMPT || event != TOUCH_END) return false
        require(tag == 0 || tag == 1) { "MsgBox4 tag must be OK(0) or CANCEL(1)" }
        stored = checked
        val result = tag or if (checked) 2 else 0
        overlay = Overlay.NONE
        if (result and 1 != 0) return true
        if (result and 2 != 0) {
            collocation = true
            overlay = Overlay.TUOGUAN
        }
        endRoundRequests++
        return true
    }

    /** cancelTuoGuan: 입력 또는 이벤트를 반영해 전투 상태를 전환한다. */
    fun cancelTuoGuan(event: Int): Boolean {
        if (overlay != Overlay.TUOGUAN || event != TOUCH_END) return false
        collocation = false
        overlay = Overlay.NONE
        return true
    }


    fun installFixture(target: Overlay, checked: Boolean = false) {
        stored = checked
        when (target) {
            Overlay.NONE -> Unit
            Overlay.PROMPT -> openEndRoundPrompt()
            Overlay.TUOGUAN -> {
                openEndRoundPrompt()
                if (this.checked != checked) toggle()
                answer(0, TOUCH_END)
            }
        }
    }


    fun view() = View(overlay, checked, stored, collocation, endRoundRequests)

    companion object {
        const val TOUCH_END = 2
    }
}
