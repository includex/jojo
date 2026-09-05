package com.jojo.game

/** Production state behind SettingLayer's gated other-tools action. */
class CmdProductionRoute {
    enum class State { SETTING, CMD, CLOSED }

    var state = State.SETTING; private set
    val input = mutableListOf<String>()

    fun settingTool(tag2: Int, touchEnd: Boolean, rFlag: Int): Boolean {
        val open = state == State.SETTING && touchEnd && tag2 == 3 && rFlag != 0
        if (open) {
            state = State.CMD
            input += "SettingLayer.button8 TOUCH_END"
        }
        return open
    }

    fun close(touchEnd: Boolean): Boolean {
        if (!touchEnd || state != State.CMD) return false
        state = State.CLOSED
        input += "CmdLayer.button0 TOUCH_END"
        return true
    }
}
