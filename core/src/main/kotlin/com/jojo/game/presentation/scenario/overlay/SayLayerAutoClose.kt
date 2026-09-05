package com.jojo.game.presentation.scenario.overlay

/**
 * Source SayLayer `_enabledAutoClose` timer.
 *
 * The Cocos component schedules `_next()` one second after its typewriter
 * handler finishes (or after a touch exposes the pending text).  A new page,
 * disabled setting, or manual advance cancels that pending callback.
 */
/**
 * class  `SayLayerAutoClose`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SayLayerAutoClose(private val delaySeconds: Float = 1f) {
    private var remaining: Float? = null

    /**
     * 공개 메서드 `update`
     *
     * ### 파라미터
    - `textComplete` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `enabled` (`Boolean`): 구현 기준으로 역할 및 허용 값 정의 필요
    - `delta` (`Float`): 구현 기준으로 역할 및 허용 값 정의 필요
     *
     * ### 응답 스펙
     * - 반환 타입: `Boolean`
     * - 반환값: 동작 결과의 도메인 값입니다.
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
     * 공개 메서드 `reset`
     *
     * ### 파라미터
    - 입력 파라미터: 없음
     *
     * ### 응답 스펙
     * - 반환 타입: `Unit`
     * - 반환값: 동작 결과의 도메인 값입니다.
     */

    fun reset() {
        remaining = null
    }
}
