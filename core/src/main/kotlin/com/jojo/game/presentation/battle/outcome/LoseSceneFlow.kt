// Game
package com.jojo.game.presentation.battle.outcome

import com.jojo.game.domain.battle.*


/** LoseSceneFlow: battle/Lose.js의 상태 계약이다. 0번 응답은 Login을 열고, 다른 MsgBox 응답은 모두 END_GAME을 전달하며 전투를 다시 시작하지 않는다. */

class LoseSceneFlow(
    /** `openLogin` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val openLogin: () -> Unit,
    /** `endGame` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val endGame: () -> Unit,
) {

    /**
     * `State`: 관련 상태와 동작을 묶는 class다.
     * 패키지의 책임에 맞는 입력·상태·결과 계약을 제공한다.
     */

    enum class State { LOGO, PROMPT, LOGIN, EXIT }

    /**
     * `state` (State): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var state: State = State.LOGO
        private set
    /**
     * `elapsed` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var elapsed: Float = 0f
        private set
    /**
     * `interstitialRequested` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var interstitialRequested: Boolean = true
        private set
    /**
     * `answerCount` (Int): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var answerCount: Int = 0
        private set


    /**
     * `update`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun update(delta: Float) {
        if (state != State.LOGO) return
        elapsed += delta.coerceAtLeast(0f)
        if (elapsed >= PROMPT_DELAY_SECONDS) state = State.PROMPT
    }

    /** 대화상자 기본 플래그를 0 또는 1 태그로 정규화한다. */
    fun answer(tag: Int) {
        if (state != State.PROMPT || tag !in 0..1) return
        answerCount++
        if (tag == 0) {
            state = State.LOGIN
            openLogin()
        } else {
            state = State.EXIT
            endGame()
        }
    }

    companion object {
        /**
         * `PROMPT_DELAY_SECONDS` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val PROMPT_DELAY_SECONDS = 3f
        /**
         * `PROMPT_TEXT` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val PROMPT_TEXT = "다시 플레이하시겠습니까?"
        /**
         * `MSGBOX_FLAG` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
         * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
         */

        const val MSGBOX_FLAG = 7
    }
}
