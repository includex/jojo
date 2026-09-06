// Presentation
package com.jojo.game.presentation.shared.overlay

/** LoadingLayer: 지연 로딩 중 입력 차단막과 로딩 이미지를 언제 표시할지 계산하는 화면 상태다. */
class LoadingLayer(private val flag: Int = 0) {
    var imageVisible: Boolean = flag and 3 == 0
        private set
    var blockerOpacity: Float = if (imageVisible) .392f else 0f
        private set
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
    private val pending: Boolean,
    private val clearPending: () -> Unit,
    private val requestCheck: (((Boolean) -> Unit) -> Unit),
    private val onRegistered: () -> Unit = {},
) {
    /** 등록 확인 흐름의 진행 상태입니다. */
    enum class State { IDLE, CHECKING, COMPLETE }

    var state = State.IDLE
        private set
    var loading: LoadingLayer? = null
        private set
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
