package com.jojo.game

/**
 * State contract of battle/Lose.js. Despite the historical fixture name
 * `lose-restart`, the source never restarts Battle: answer 0 opens Login and
 * every other visible MsgBox answer dispatches END_GAME.
 */
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

    /** MsgBox default flag 7 is normalized through `3 & flag` to tags 0/1. */
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
