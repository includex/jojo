package com.jojo.game

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
class LoginRegistrationCheckFlow(
    private val pending: Boolean,
    private val clearPending: () -> Unit,
    private val requestCheck: (((Boolean) -> Unit) -> Unit),
    private val onRegistered: () -> Unit = {},
) {
    enum class State { IDLE, CHECKING, COMPLETE }
    var state = State.IDLE
        private set
    var loading: LoadingLayer? = null
        private set
    var registered = false
        private set

    fun start() {
        check(state == State.IDLE)
        if (!pending) { state = State.COMPLETE; return }
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
