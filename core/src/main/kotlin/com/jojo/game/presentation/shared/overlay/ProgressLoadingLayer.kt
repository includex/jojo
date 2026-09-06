// Presentation
package com.jojo.game.presentation.shared.overlay

/** LoadingLayer: 지연 로딩 중 입력 차단막과 로딩 이미지를 언제 표시할지 계산하는 화면 상태다. */
class LoadingLayer(private val flag: Int = 0) {
    /**
     * `imageVisible` (Boolean): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var imageVisible: Boolean = flag and 3 == 0
        private set
    /**
     * `blockerOpacity` (Float): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var blockerOpacity: Float = if (imageVisible) .392f else 0f
        private set
    /**
     * `elapsed` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var elapsed = 0f

    /** 지연 시간이 지나면 로딩 표시를 활성화합니다. */
    fun advance(deltaSeconds: Float) {
        if (imageVisible || flag and 1 == 0) return
        elapsed += deltaSeconds.coerceAtLeast(0f)
        if (elapsed >= 5f) {
            imageVisible = true
            blockerOpacity = .392f
        }
    }
}

/** 로그인 등록 여부를 비동기로 확인하는 흐름입니다. */
class LoginRegistrationCheckFlow(
    /** `pending` (Boolean): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val pending: Boolean,
    /** `clearPending` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val clearPending: () -> Unit,
    /** `requestCheck` ((((Boolean) -> Unit) -> Unit)): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val requestCheck: (((Boolean) -> Unit) -> Unit),
    /** `onRegistered` (() -> Unit): 객체가 유지하는 구성·진행 상태이며 후속 흐름의 입력으로 사용된다. */
    private val onRegistered: () -> Unit = {},
) {
    /** 등록 확인 흐름의 진행 상태입니다. */
    enum class State { IDLE, CHECKING, COMPLETE }

    /**
     * `state` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var state = State.IDLE
        private set
    /**
     * `loading` (LoadingLayer?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var loading: LoadingLayer? = null
        private set
    /**
     * `registered` (상태 값): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    var registered = false
        private set

    /** 등록 확인 요청을 시작합니다. */
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
