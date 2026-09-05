package com.jojo.game

/**
 * State contract of battle/Lose.js. Despite the historical fixture name
 * `lose-restart`, the source never restarts Battle: answer 0 opens Login and
 * every other visible MsgBox answer dispatches END_GAME.
 */
/**
 * class  `LoseSceneFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class LoseSceneFlow(
    private val openLogin: () -> Unit,
    private val endGame: () -> Unit,
) {
    /**
     * enum class  `State`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class State { LOGO, PROMPT, LOGIN, EXIT }

    var state: State = State.LOGO
        private set
    var elapsed: Float = 0f
        private set
    var interstitialRequested: Boolean = true
        private set
    var answerCount: Int = 0
        private set

    /**
     * 공개 메서드 `update`
     *
     * ### 파라미터
    - `delta` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

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
