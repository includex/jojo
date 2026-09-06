// Scenario
package com.jojo.game.presentation.scenario.overlay

/** SayLayerAutoClose: 자동 진행 대사 레이어의 남은 표시 시간을 계산하고 종료 시점을 판정한다. */

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
