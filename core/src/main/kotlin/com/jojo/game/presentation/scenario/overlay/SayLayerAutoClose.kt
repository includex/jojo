// Scenario
package com.jojo.game.presentation.scenario.overlay

/** SayLayerAutoClose: 자동 진행 대사 레이어의 남은 표시 시간을 계산하고 종료 시점을 판정한다. */

class SayLayerAutoClose(private val delaySeconds: Float = 1f) {
    /**
     * `remaining` (Float?): 객체가 유지하는 구성·진행 상태를 보관한다.
     * 값의 변경은 현재 패키지의 흐름과 후속 계산에 반영된다.
     */

    private var remaining: Float? = null


    /**
     * `update`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

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


    /**
     * `reset`: 현재 상태를 갱신한다.
     * 입력값을 현재 타입의 규칙에 따라 처리하고 결과 또는 상태 변화를 남긴다.
     */

    fun reset() {
        remaining = null
    }
}
