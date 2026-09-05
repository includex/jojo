package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * class  `SayLayerAutoCloseTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class SayLayerAutoCloseTest {
    @Test fun `source timer advances one second after text completes`() {
        val timer = SayLayerAutoClose()
        assertFalse(timer.update(textComplete = false, enabled = true, delta = 10f))
        assertFalse(timer.update(textComplete = true, enabled = true, delta = .75f))
        assertFalse(timer.update(textComplete = true, enabled = true, delta = .99f))
        assertTrue(timer.update(textComplete = true, enabled = true, delta = .01f))
    }

    @Test fun `new page manual advance and disabled setting cancel callback`() {
        val timer = SayLayerAutoClose()
        assertFalse(timer.update(true, true, 0f))
        assertFalse(timer.update(false, true, 2f))
        assertFalse(timer.update(true, true, 0f))
        timer.reset()
        assertFalse(timer.update(true, true, 2f))
        assertFalse(timer.update(true, false, 2f))
        assertFalse(timer.update(true, true, 0f))
    }
}
