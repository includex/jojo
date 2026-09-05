package com.jojo.game.presentation.battle

/**
 * Stateful implementation of BattleScreen.END_ROUND -> MsgBox4 -> TuoGuanLayer.
 *
 * MsgBox4 encodes the chosen button in bit 0 and the persisted auto-battle
 * toggle in bit 1.  Keeping that compact result here makes the production
 * route follow the recovered callback instead of treating HHJS as an
 * immediate end-turn command.
 */
/**
 * class  `AutoBattleFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class AutoBattleFlow(initialStored: Boolean = false) {
    /**
     * enum class  `Overlay`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class Overlay { NONE, PROMPT, TUOGUAN }

    /**
     * data class  `View`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

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

    /**
     * 공개 메서드 `openEndRoundPrompt`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun openEndRoundPrompt() {
        checked = stored
        overlay = Overlay.PROMPT
    }

    /**
     * 공개 메서드 `toggle`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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

    /**
     * 공개 메서드 `installFixture`
     *
     * ### 파라미터
    - `target` (`Overlay`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `checked` (`Boolean = false`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `view`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun view() = View(overlay, checked, stored, collocation, endRoundRequests)

    companion object {
        const val TOUCH_END = 2
    }
}
