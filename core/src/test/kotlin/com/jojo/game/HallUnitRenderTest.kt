// Test
package com.jojo.game

import com.jojo.game.presentation.scenario.hall.*

import kotlin.test.Test
import kotlin.test.assertEquals

/** HallUnitRenderTest: HallUnitRender의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

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
        // 테스트 근거: 경로 탐색의 방문 순서와 목적지 선택을 검증한다.
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
