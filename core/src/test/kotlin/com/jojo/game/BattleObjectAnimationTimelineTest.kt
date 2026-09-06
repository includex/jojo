// Test
package com.jojo.game

import com.jojo.game.domain.battle.*

import com.jojo.game.domain.battle.BattleObjectAnimationTimeline

import kotlin.test.Test
import kotlin.test.assertEquals

/** BattleObjectAnimationTimelineTest: BattleObjectAnimationTimeline의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleObjectAnimationTimelineTest {
    @Test
    fun `fire uses four contiguous rows for eight ticks each`() {
        assertEquals(0, BattleObjectAnimationTimeline.row(0f, 0, 4))
        assertEquals(0, BattleObjectAnimationTimeline.row(7.9f / 24f, 0, 4))
        assertEquals(1, BattleObjectAnimationTimeline.row(8f / 24f, 0, 4))
        assertEquals(2, BattleObjectAnimationTimeline.row(16f / 24f, 0, 4))
        assertEquals(3, BattleObjectAnimationTimeline.row(24f / 24f, 0, 4))
        assertEquals(0, BattleObjectAnimationTimeline.row(32f / 24f, 0, 4))
        assertEquals(listOf(0, 48, 96, 144), (0..3).map(BattleObjectAnimationTimeline::sourceY))
    }

    @Test
    fun `river and secondary fire retain their authored row windows`() {
        assertEquals(4, BattleObjectAnimationTimeline.row(0f, 4, 2))
        assertEquals(5, BattleObjectAnimationTimeline.row(8f / 24f, 4, 2))
        assertEquals(6, BattleObjectAnimationTimeline.row(0f, 6, 2))
        assertEquals(7, BattleObjectAnimationTimeline.row(8f / 24f, 6, 2))
    }
}
