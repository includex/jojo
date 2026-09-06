package com.jojo.game
import com.jojo.game.presentation.shared.overlay.*

/** 설정 화면의 조건부 기타 도구 동작 상태이다. */
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
