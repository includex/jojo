// Test
package com.jojo.game

import com.jojo.game.presentation.battle.overlay.RoundLayer

import kotlin.test.Test
import kotlin.test.assertEquals

/** RoundLayerTest: RoundLayer의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class RoundLayerTest {
    @Test
    fun `RoundLayer mirrors round labels, final turn and two-second completion`() {
        var removed = 0
        var completed = 0
        val layer = RoundLayer({ removed++ }, { completed++ })

        layer.onCreate(round = 3, max = 20)
        assertEquals(RoundLayer.View(true, false, "제3턴"), layer.view)
        layer.elapsed(1.99f)
        assertEquals(0, completed)
        layer.elapsed(2f)
        layer.elapsed(3f)
        assertEquals(1, removed)
        assertEquals(1, completed)

        val final = RoundLayer({}, {})
        final.onCreate(round = 21, max = 20)
        assertEquals(RoundLayer.View(true, false, "최종 턴"), final.view)

        val camp = RoundLayer({}, {})
        camp.onCreate(round = null, max = null)
        assertEquals(RoundLayer.View(false, true, ""), camp.view)
    }

    @Test
    fun `RoundLayer uses property presence and JavaScript missing-max comparison semantics`() {
        val layer = RoundLayer({}, {})

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        layer.onCreate(RoundLayer.CreateArgs(roundPresent = true, round = 0, max = null))
        assertEquals(RoundLayer.View(true, false, "제0턴"), layer.view)

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        layer.onCreate(RoundLayer.CreateArgs(roundPresent = false, max = 20))
        assertEquals(RoundLayer.View(false, true, ""), layer.view)
    }
}
