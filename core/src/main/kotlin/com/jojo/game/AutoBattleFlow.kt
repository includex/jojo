package com.jojo.game

/**
 * Stateful implementation of BattleScreen.END_ROUND -> MsgBox4 -> TuoGuanLayer.
 *
 * MsgBox4 encodes the chosen button in bit 0 and the persisted auto-battle
 * toggle in bit 1.  Keeping that compact result here makes the production
 * route follow the recovered callback instead of treating HHJS as an
 * immediate end-turn command.
 */
class AutoBattleFlow(initialStored: Boolean = false) {
    enum class Overlay { NONE, PROMPT, TUOGUAN }

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

    /** MsgBox4 button0/button1 and Panel_cancel deliver only TOUCH_END. */
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

    /** TuoGuanLayer installs its listener at priority 2 on Panel_cancel. */
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

    companion object { const val TOUCH_END = 2 }
}
