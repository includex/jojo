// Battle
package com.jojo.game.presentation.battle.overlay

/** 자동 전투 오버레이의 선택 상태와 라운드 종료 요청을 관리한다. */
class AutoBattleFlow(initialStored: Boolean = false) {
    /**
     * `Overlay`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class Overlay { NONE, PROMPT, TUOGUAN }


    /** View: 전투 화면에 전달할 불변 표시 상태를 보관한다. */
    data class View(
        /**
         * `overlay` (Overlay,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val overlay: Overlay,
        /**
         * `checked` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val checked: Boolean,
        /**
         * `stored` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val stored: Boolean,
        /**
         * `collocation` (Boolean,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val collocation: Boolean,
        /**
         * `endRoundRequests` (Int,): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        val endRoundRequests: Int,
    )

    /**
     * `overlay` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var overlay = Overlay.NONE
    /**
     * `checked` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var checked = initialStored
    /**
     * `stored` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var stored = initialStored
    /**
     * `collocation` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var collocation = false
    /**
     * `endRoundRequests` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var endRoundRequests = 0


    /**
     * `openEndRoundPrompt`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun openEndRoundPrompt() {
        checked = stored
        overlay = Overlay.PROMPT
    }


    /**
     * `toggle`: 입력을 규칙에 따라 계산·변환한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun toggle() {
        if (overlay == Overlay.PROMPT) checked = !checked
    }
    /**
     * `answer`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `installFixture`: 타입의 핵심 동작을 수행한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `view`: 상태나 데이터를 조회한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun view() = View(overlay, checked, stored, collocation, endRoundRequests)

    companion object {
        /**
         * `TOUCH_END` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val TOUCH_END = 2
    }
}
