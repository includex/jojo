// Game
package com.jojo.game.presentation.battle.outcome

import com.jojo.game.domain.battle.*


/** LoseSceneFlow: battle/Lose.js의 상태 계약이다. 0번 응답은 Login을 열고, 다른 MsgBox 응답은 모두 END_GAME을 전달하며 전투를 다시 시작하지 않는다. */

class LoseSceneFlow(
    private val openLogin: () -> Unit,
    private val endGame: () -> Unit,
) {

    enum class State { LOGO, PROMPT, LOGIN, EXIT }

    var state: State = State.LOGO
        private set
    var elapsed: Float = 0f
        private set
    var interstitialRequested: Boolean = true
        private set
    var answerCount: Int = 0
        private set


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
        const val PROMPT_DELAY_SECONDS = 3f
        const val PROMPT_TEXT = "다시 플레이하시겠습니까?"
        const val MSGBOX_FLAG = 7
    }
}
