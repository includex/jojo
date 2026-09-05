package com.jojo.game.presentation.shared.overlay

/** Behavioral implementation of registered Global104 `LoadingLayer`. */
class LoadingLayer(private val flag: Int = 0) {
    var imageVisible: Boolean = flag and 3 == 0
        private set
    var blockerOpacity: Float = if (imageVisible) .392f else 0f
        private set
    private var elapsed = 0f

    /** Cocos flag 1 reveals the spinner/blocker after five scheduler seconds. */
    fun advance(deltaSeconds: Float) {
        if (imageVisible || flag and 1 == 0) return
        elapsed += deltaSeconds.coerceAtLeast(0f)
        if (elapsed >= 5f) {
            imageVisible = true
            blockerOpacity = .392f
        }
    }
}

/**
 * Source Login's CHECK_REGISTER route: clear the one-shot marker, attach LoadingLayer,
 * wait for registerCheck, then detach it before handling the optional registration result.
 */
/**
 * class  `LoginRegistrationCheckFlow`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class LoginRegistrationCheckFlow(
    private val pending: Boolean,
    private val clearPending: () -> Unit,
    private val requestCheck: (((Boolean) -> Unit) -> Unit),
    private val onRegistered: () -> Unit = {},
) {
    /**
     * enum class  `State`
     *
     * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
     *
     * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
     */

    enum class State { IDLE, CHECKING, COMPLETE }

    var state = State.IDLE
        private set
    var loading: LoadingLayer? = null
        private set
    var registered = false
        private set

    /**
     * 공개 메서드 `start`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun start() {
        check(state == State.IDLE)
        if (!pending) {
            state = State.COMPLETE; return
        }
        clearPending()
        loading = LoadingLayer()
        state = State.CHECKING
        requestCheck { result ->
            if (state != State.CHECKING) return@requestCheck
            loading = null
            registered = result
            state = State.COMPLETE
            if (result) onRegistered()
        }
    }
}
