package com.jojo.game.presentation.scenario.overlay

/**
 * Source SayLayer `_enabledAutoClose` timer.
 *
 * The Cocos component schedules `_next()` one second after its typewriter
 * handler finishes (or after a touch exposes the pending text).  A new page,
 * disabled setting, or manual advance cancels that pending callback.
 */

class SayLayerAutoClose(private val delaySeconds: Float = 1f) {
    private var remaining: Float? = null


    fun update(textComplete: Boolean, enabled: Boolean, delta: Float): Boolean {
        if (!enabled || !textComplete) {
            remaining = null
            return false
        }
        val scheduled = remaining
        if (scheduled == null) {
            remaining = delaySeconds
            return false
        }
        val next = scheduled - delta.coerceAtLeast(0f)
        if (next > 0f) {
            remaining = next
            return false
        }
        remaining = null
        return true
    }


    fun reset() {
        remaining = null
    }
}
