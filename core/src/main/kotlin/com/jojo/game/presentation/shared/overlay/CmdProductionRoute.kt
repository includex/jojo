// Game
package com.jojo.game.presentation.shared.overlay

/** CmdProductionRoute: 설정 화면의 조건부 기타 도구 동작 상태이다. */
class CmdProductionRoute {
    /**
     * `State`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class State { SETTING, CMD, CLOSED }

    /**
     * `state` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var state = State.SETTING; private set
    /**
     * `input` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    val input = mutableListOf<String>()

    /**
     * `settingTool`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun settingTool(tag2: Int, touchEnd: Boolean, rFlag: Int): Boolean {
        val open = state == State.SETTING && touchEnd && tag2 == 3 && rFlag != 0
        if (open) {
            state = State.CMD
            input += "SettingLayer.button8 TOUCH_END"
        }
        return open
    }

    /**
     * `close`: 상태와 자원을 정리한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun close(touchEnd: Boolean): Boolean {
        if (!touchEnd || state != State.CMD) return false
        state = State.CLOSED
        input += "CmdLayer.button0 TOUCH_END"
        return true
    }
}
