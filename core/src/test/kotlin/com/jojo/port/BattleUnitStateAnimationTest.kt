package com.jojo.port

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

        // The third active status is past the source's `break` and therefore
        // does not alter its `_lastRefState` cache or recreate the node.
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
