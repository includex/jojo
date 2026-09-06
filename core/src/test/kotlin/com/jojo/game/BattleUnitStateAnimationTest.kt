// Test
package com.jojo.game

import com.jojo.game.presentation.battle.unit.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** BattleUnitStateAnimationTest: BattleUnitStateAnimation의 핵심 동작과 입력 경계 조건을 자동화로 검증하는 테스트 묶음이다. */

class BattleUnitStateAnimationTest {
    @Test
    fun `refStateAnime creates two repeated frames for one source status`() {
        val state = BattleUnitStateAnimation()

        assertEquals(
            BattleUnitStateAnimation.Effect(textureIndices = listOf(2, 2)),
            state.refresh(listOf(false, false, true, false)),
        )
    }

    @Test
    fun `refStateAnime uses the first two active MB through ZD states only`() {
        val state = BattleUnitStateAnimation()

        assertEquals(
            BattleUnitStateAnimation.Effect(textureIndices = listOf(0, 2)),
            state.refresh(listOf(true, false, true, true)),
        )

        // 테스트 근거: 원본 구현의 처리 순서와 경계 조건을 검증한다.
        state.setVisible(false)
        assertTrue(state.refresh(listOf(true, false, true, false))!!.active)
        assertEquals(listOf(0, 2), state.current()!!.textureIndices)
    }

    @Test
    fun `refStateAnime destroys state node when source mask becomes empty`() {
        val state = BattleUnitStateAnimation()
        state.refresh(listOf(false, true, false, false))
        state.setVisible(false)
        assertFalse(state.current()!!.active)

        assertNull(state.refresh(listOf(false, false, false, false)))
        assertNull(state.current())
    }

    @Test
    fun `anime_state alternates texture and constant position at three fps`() {
        val effect = requireNotNull(BattleUnitStateAnimation().refresh(listOf(true, false, true, false)))
        assertEquals(BattleUnitStateAnimation.Effect.Sample(0, -16 to 16), effect.sampleAt(0f))
        assertEquals(BattleUnitStateAnimation.Effect.Sample(0, -16 to 16), effect.sampleAt(1f / 3f - .0001f))
        assertEquals(BattleUnitStateAnimation.Effect.Sample(2, 16 to 16), effect.sampleAt(1f / 3f))
        assertEquals(BattleUnitStateAnimation.Effect.Sample(0, -16 to 16), effect.sampleAt(2f / 3f))
    }

    @Test
    fun `single status keeps its texture while still moving left and right`() {
        val effect = requireNotNull(BattleUnitStateAnimation().refresh(listOf(false, true, false, false)))
        assertEquals(BattleUnitStateAnimation.Effect.Sample(1, -16 to 16), effect.sampleAt(0f))
        assertEquals(BattleUnitStateAnimation.Effect.Sample(1, 16 to 16), effect.sampleAt(1f / 3f))
    }
}
