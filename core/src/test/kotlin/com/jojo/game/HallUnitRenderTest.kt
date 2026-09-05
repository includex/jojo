package com.jojo.game

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * class  `HallUnitRenderTest`
 *
 * 이 타입은 게임 핵심 로직의 공개 API 역할을 담당합니다.
 *
 * 클래스/타입의 책임, 입력 파라미터, 상태 영향도를 기준으로 세부 보강이 필요합니다.
 */

class HallUnitRenderTest {
    @Test fun `walking matches runtime sheet row and horizontal scale`() {
        assertEquals(HallUnitSpriteFrame(16, 1, false), HallUnitRender.frame(7, 20, 0, 0f))
        assertEquals(HallUnitSpriteFrame(15, 1, true), HallUnitRender.frame(7, 20, 1, 0f))
        assertEquals(HallUnitSpriteFrame(15, 1, false), HallUnitRender.frame(7, 20, 2, 0f))
        assertEquals(HallUnitSpriteFrame(16, 1, true), HallUnitRender.frame(7, 20, 3, 0f))
    }

    @Test fun `walking frame matches sampled runtime eighth seconds`() {
        assertEquals(1, HallUnitRender.frame(0, 20, 2, .12f).row)
        assertEquals(2, HallUnitRender.frame(0, 20, 2, .16f).row)
        assertEquals(1, HallUnitRender.frame(0, 20, 2, .25f).row)
    }

    @Test fun `walking keyframe boundary is exactly three sample-24 ticks`() {
        assertEquals(1, HallUnitRender.frame(0, 20, 2, .1249f).row)
        assertEquals(2, HallUnitRender.frame(0, 20, 2, .125f).row)
        assertEquals(2, HallUnitRender.frame(0, 20, 2, .2499f).row)
        assertEquals(1, HallUnitRender.frame(0, 20, 2, .25f).row)
        // A direction change calls Animation.play(newClip), so callers pass
        // local zero rather than the movement's global .08 second clock.
        assertEquals(1, HallUnitRender.frame(0, 20, 1, 0f).row)
        assertEquals(1, HallUnitRender.frame(0, 20, 2, 0f).row)
    }

    @Test fun `standing is static and one-shot movement holds its last key`() {
        assertEquals(0, HallUnitRender.frame(0, 0, 2, 8f).row)
        assertEquals(1, HallUnitRender.frame(0, 21, 0, .12f).row)
        assertEquals(2, HallUnitRender.frame(0, 21, 0, .13f).row)
        assertEquals(2, HallUnitRender.frame(0, 21, 0, 9f).row)
    }
}
